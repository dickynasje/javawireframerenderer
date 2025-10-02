package com.rendererscratch.Basics;

import java.util.ArrayList;
import java.util.List;

public class ShapeBuilder {
  private List<Point3d> vertices = new ArrayList<>();
  private List<Edge> edges = new ArrayList<>();

  public ShapeBuilder addVertex(float x, float y, float z) {
    vertices.add(new Point3d(x, y, z));
    return this;
  }

  public ShapeBuilder addEdge(int startIndex, int endIndex) {
    edges.add(new Edge(startIndex, endIndex));
    return this;
  }

  public ShapeBuilder connectVertices(int... indices) {
    for (int i = 0; i < indices.length - 1; i++) {
      addEdge(indices[i], indices[i + 1]);
    }
    return this;
  }

  public ShapeBuilder connectInLoop(int... indices) {
    connectVertices(indices);
    if (indices.length > 0) {
      addEdge(indices[indices.length - 1], indices[0]);
    }
    return this;
  }

  public ShapeBuilder connectAllToVertex(int centerVertex, int... otherVertices) {
    for (int vertex : otherVertices) {
      addEdge(centerVertex, vertex);
    }
    return this;
  }

  public Shape build() {
    Point3d[] vertexArray = vertices.toArray(new Point3d[0]);
    Edge[] edgeArray = edges.toArray(new Edge[0]);
    return new Shape(vertexArray, edgeArray);
  }

  public static ShapeBuilder create() {
    return new ShapeBuilder();
  }
}
