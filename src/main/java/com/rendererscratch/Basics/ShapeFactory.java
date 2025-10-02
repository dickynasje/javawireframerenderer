package com.rendererscratch.Basics;

public class ShapeFactory {

  public static Shape createCube() {
    return ShapeBuilder.create()
        .addVertex(-1, -1, -1) // 0
        .addVertex(-1, -1, 1) // 1
        .addVertex(1, -1, -1) // 2
        .addVertex(-1, 1, -1) // 3
        .addVertex(-1, 1, 1) // 4
        .addVertex(1, -1, 1) // 5
        .addVertex(1, 1, -1) // 6
        .addVertex(1, 1, 1) // 7
        // Bottom face
        .connectInLoop(0, 1, 5, 2)
        // Top face
        .connectInLoop(3, 4, 7, 6)
        // Vertical edges
        .addEdge(0, 3)
        .addEdge(1, 4)
        .addEdge(2, 6)
        .addEdge(5, 7)
        .build();
  }

  public static Shape createPyramid() {
    return ShapeBuilder.create()
        .addVertex(0, 1, 0) // 0: top
        .addVertex(-1, -1, -1) // 1: bottom corners
        .addVertex(1, -1, -1) // 2
        .addVertex(1, -1, 1) // 3
        .addVertex(-1, -1, 1) // 4
        // Base
        .connectInLoop(1, 2, 3, 4)
        // Sides to top
        .connectAllToVertex(0, 1, 2, 3, 4)
        .build();
  }

  public static Shape createOctahedron() {
    return ShapeBuilder.create()
        .addVertex(0, 1.5f, 0) // 0: top
        .addVertex(0, -1.5f, 0) // 1: bottom
        .addVertex(1, 0, 1) // 2: middle ring
        .addVertex(-1, 0, 1) // 3
        .addVertex(-1, 0, -1) // 4
        .addVertex(1, 0, -1) // 5
        // Top pyramid
        .connectAllToVertex(0, 2, 3, 4, 5)
        // Bottom pyramid
        .connectAllToVertex(1, 2, 3, 4, 5)
        // Middle ring
        .connectInLoop(2, 3, 4, 5)
        .build();
  }
  
  public static Shape createTetrahedron() {
    return ShapeBuilder.create()
        .addVertex(0, 1, 0) // 0: top
        .addVertex(-1, -1, 1) // 1: base
        .addVertex(1, -1, 1) // 2
        .addVertex(0, -1, -1) // 3
        // Base triangle
        .connectInLoop(1, 2, 3)
        // Connect to top
        .connectAllToVertex(0, 1, 2, 3)
        .build();
  }

  public static Shape createDiamond() {
    return ShapeBuilder.create()
        .addVertex(0, 2, 0) // 0: top
        .addVertex(0, -2, 0) // 1: bottom
        .addVertex(1, 0, 1) // 2: middle ring
        .addVertex(-1, 0, 1) // 3
        .addVertex(-1, 0, -1) // 4
        .addVertex(1, 0, -1) // 5
        // Top pyramid
        .connectAllToVertex(0, 2, 3, 4, 5)
        // Bottom pyramid
        .connectAllToVertex(1, 2, 3, 4, 5)
        // Middle ring
        .connectInLoop(2, 3, 4, 5)
        .build();
  }

  public static Shape createSphere() {
    float phi = (1.0f + (float) Math.sqrt(5.0f)) / 2.0f; // Golden ratio
    float scale = 1.0f / (float) Math.sqrt(1.0f + phi * phi);

    return ShapeBuilder.create()
        // 12 vertices of icosahedron (sphere approximation)
        .addVertex(-1 * scale, phi * scale, 0)
        .addVertex(1 * scale, phi * scale, 0)
        .addVertex(-1 * scale, -phi * scale, 0)
        .addVertex(1 * scale, -phi * scale, 0)
        .addVertex(0, -1 * scale, phi * scale)
        .addVertex(0, 1 * scale, phi * scale)
        .addVertex(0, -1 * scale, -phi * scale)
        .addVertex(0, 1 * scale, -phi * scale)
        .addVertex(phi * scale, 0, -1 * scale)
        .addVertex(phi * scale, 0, 1 * scale)
        .addVertex(-phi * scale, 0, -1 * scale)
        .addVertex(-phi * scale, 0, 1 * scale)
        // Connect the edges (30 edges total)
        .addEdge(0, 11).addEdge(0, 5).addEdge(0, 1).addEdge(0, 7).addEdge(0, 10)
        .addEdge(1, 5).addEdge(1, 7).addEdge(1, 9).addEdge(1, 8)
        .addEdge(2, 11).addEdge(2, 10).addEdge(2, 3).addEdge(2, 6).addEdge(2, 4)
        .addEdge(3, 4).addEdge(3, 6).addEdge(3, 8).addEdge(3, 9)
        .addEdge(4, 11).addEdge(4, 9).addEdge(4, 5)
        .addEdge(5, 11).addEdge(5, 9)
        .addEdge(6, 10).addEdge(6, 7).addEdge(6, 8)
        .addEdge(7, 10).addEdge(7, 8)
        .addEdge(8, 9)
        .addEdge(10, 11)
        .build();
  }

  public static Shape createHouse() {
    return ShapeBuilder.create()
        // Base cube (bottom 4 corners)
        .addVertex(-1, -1, -1) // 0
        .addVertex(1, -1, -1) // 1
        .addVertex(1, -1, 1) // 2
        .addVertex(-1, -1, 1) // 3
        // Walls (top 4 corners)
        .addVertex(-1, 0, -1) // 4
        .addVertex(1, 0, -1) // 5
        .addVertex(1, 0, 1) // 6
        .addVertex(-1, 0, 1) // 7
        // Roof peak
        .addVertex(0, 1, 0) // 8
        // Connect base
        .connectInLoop(0, 1, 2, 3)
        // Connect top of walls
        .connectInLoop(4, 5, 6, 7)
        // Connect vertical walls
        .addEdge(0, 4).addEdge(1, 5).addEdge(2, 6).addEdge(3, 7)
        // Connect roof to top of walls
        .connectAllToVertex(8, 4, 5, 6, 7)
        .build();
  }
  public static Shape createTriangle() {
    return ShapeBuilder.create()
        .addVertex(0, 1, 0) // Top
        .addVertex(-1, -1, 0) // Bottom left
        .addVertex(1, -1, 0) // Bottom right
        .connectInLoop(0, 1, 2) // Connect all three
        .build();
  }
}
