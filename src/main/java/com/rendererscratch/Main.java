package com.rendererscratch;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import com.rendererscratch.Basics.Edge;
import com.rendererscratch.Basics.Point2d;
import com.rendererscratch.Basics.Point3d;
import com.rendererscratch.Basics.Projection;
import com.rendererscratch.Basics.Shape;
import com.rendererscratch.Basics.ShapeFactory;
import com.rendererscratch.Basics.Transform3D;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main {

	private static final int WINDOW_WIDTH = 800;
	private static final int WINDOW_HEIGHT = 600;
	private static final float FOCAL_LENGTH = 10.0f;
	private static final float ROTATION_SPEED = 1.0f;
	private static final float PROJECTION_SCALE = 100.0f;

	private long window;
	private int vao, vbo, shaderProgram;

	private float rotation = 0.0f;
	private float deltaTime = 0.0f;
	private double lastFrameTime = 0.0;

	private Shape currentShape;
	private Projection projection;

	public void run() {
		System.out.println("LWJGL " + Version.getVersion());

		initializeWindow();
		setupRenderer();

		loadShape(ShapeFactory.createCube());

		renderLoop();
		cleanup();
	}

	private void renderLoop() {
		lastFrameTime = glfwGetTime();

		while (!glfwWindowShouldClose(window)) {
			updateDeltaTime();
			updateRotation();

			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			renderShape();

			glfwSwapBuffers(window);
			glfwPollEvents();
		}
	}

	private void initializeWindow() {
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}

		configureWindow();
		createWindow();
		setupCallbacks();
		centerWindow();
		showWindow();
		initializeOpenGL();
	}

	private void configureWindow() {
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
	}

	private void createWindow() {
		window = glfwCreateWindow(WINDOW_WIDTH, WINDOW_HEIGHT, "3D Renderer", NULL, NULL);
		if (window == NULL) {
			throw new RuntimeException("Failed to create GLFW window");
		}
	}

	private void setupCallbacks() {
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
				glfwSetWindowShouldClose(window, true);
			}
		});
	}

	private void centerWindow() {
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(
				window,
				(vidmode.width() - WINDOW_WIDTH) / 2,
				(vidmode.height() - WINDOW_HEIGHT) / 2);
	}

	private void showWindow() {
		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);
		glfwShowWindow(window);
	}

	private void initializeOpenGL() {
		GL.createCapabilities();
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		System.out.println("OpenGL version: " + glGetString(GL_VERSION));
	}

	private void setupRenderer() {
		compileShaders();
		createBuffers();
		projection = new Projection(WINDOW_WIDTH, WINDOW_HEIGHT, FOCAL_LENGTH, PROJECTION_SCALE);
	}

	private void compileShaders() {
		int vertexShader = createVertexShader();
		int fragmentShader = createFragmentShader();

		shaderProgram = glCreateProgram();
		glAttachShader(shaderProgram, vertexShader);
		glAttachShader(shaderProgram, fragmentShader);
		glLinkProgram(shaderProgram);

		glDeleteShader(vertexShader);
		glDeleteShader(fragmentShader);
	}
	//copy paste from somewhere
	private int createVertexShader() {
		String source = "#version 330 core\n" +
				"layout (location = 0) in vec2 aPos;\n" +
				"void main() {\n" +
				"    gl_Position = vec4(aPos, 0.0, 1.0);\n" +
				"}\n";

		int shader = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(shader, source);
		glCompileShader(shader);
		return shader;
	}

	private int createFragmentShader() {
		String source = "#version 330 core\n" +
				"out vec4 FragColor;\n" +
				"void main() {\n" +
				"    FragColor = vec4(1.0, 1.0, 1.0, 1.0);\n" +
				"}\n";

		int shader = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(shader, source);
		glCompileShader(shader);
		return shader;
	}

	private void createBuffers() {
		vao = glGenVertexArrays();
		vbo = glGenBuffers();

		glBindVertexArray(vao);
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glVertexAttribPointer(0, 2, GL_FLOAT, false, 2 * Float.BYTES, 0);
		glEnableVertexAttribArray(0);
		glBindVertexArray(0);
	}

	private void loadShape(Shape shape) {
		this.currentShape = shape;
	}

	private void updateDeltaTime() {
		double currentTime = glfwGetTime();
		deltaTime = (float) (currentTime - lastFrameTime);
		lastFrameTime = currentTime;
	}

	private void updateRotation() {
		rotation += ROTATION_SPEED * deltaTime;
	}

	private void renderShape() {
		float[] lineVertices = buildLineVertices();
		uploadToGPU(lineVertices);
		draw(currentShape.getEdgeCount() * 2);
	}

	private float[] buildLineVertices() {
		Edge[] edges = currentShape.getEdges();
		Point3d[] vertices = currentShape.getVertices();

		int vertexCount = edges.length * 4;
		float[] lineVertices = new float[vertexCount];
		int index = 0;

		for (Edge edge : edges) {
			Point3d start3D = vertices[edge.getStart()];
			Point3d end3D = vertices[edge.getEnd()];

			Point3d rotatedStart = applyRotation(start3D);
			Point3d rotatedEnd = applyRotation(end3D);

			Point2d start2D = projection.projectToScreen(rotatedStart);
			Point2d end2D = projection.projectToScreen(rotatedEnd);

			lineVertices[index++] = projection.normalizeX(start2D.getX());
			lineVertices[index++] = projection.normalizeY(start2D.getY());
			lineVertices[index++] = projection.normalizeX(end2D.getX());
			lineVertices[index++] = projection.normalizeY(end2D.getY());
		}

		return lineVertices;
	}

	private Point3d applyRotation(Point3d point) {
		Point3d rotatedX = Transform3D.rotateX(point, rotation);
		return Transform3D.rotateY(rotatedX, rotation);
	}

	private void uploadToGPU(float[] vertices) {
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_DYNAMIC_DRAW);
	}

	private void draw(int vertexCount) {
		glUseProgram(shaderProgram);
		glBindVertexArray(vao);
		glDrawArrays(GL_LINES, 0, vertexCount);
	}

	private void cleanup() {
		glfwDestroyWindow(window);
		glfwTerminate();
	}

	public static void main(String[] args) {
		new Main().run();
	}
}