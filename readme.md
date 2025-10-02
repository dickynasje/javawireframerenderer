# 3D Wireframe Renderer

A modular 3D wireframe renderer built with LWJGL and OpenGL.

Main reference: 
- https://www.youtube.com/watch?v=nvWDgBGcAIM
- https://www.lwjgl.org/guide
- and many more googling


#### Adjust java in pom.xml with the one installed in your computer

### Creating Your Own Shape

Add a new method to `ShapeFactory.java`:

```java
public static Shape shapeExample() {
    return ShapeBuilder.create()
        .addVertex(...)
        .addVertex(...)
        .connectInLoop(...)
        .build();
}
```

Then use it in `Main.java`:

```java
loadShape(ShapeFactory.shapeExample());
```
configs in `Main.java`:

```java
WINDOW_WIDTH = 800          // Window width in pixels
WINDOW_HEIGHT = 600         // Window height in pixels
FOCAL_LENGTH = 10.0f        // Perspective focal length
ROTATION_SPEED = 1.0f       // Rotation speed (radians/sec)
PROJECTION_SCALE = 100.0f   // Scale factor for projection
```