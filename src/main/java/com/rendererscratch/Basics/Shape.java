package com.rendererscratch.Basics;

public class Shape {
  private final Point3d[] vertices;
  private final Edge[] edges;

  public Shape(Point3d[] vertices, Edge[] edges) {
    this.vertices = vertices;
    this.edges = edges;
  }

  public Point3d[] getVertices() {
    return vertices;
  }

  public Edge[] getEdges() {
    return edges;
  }

  public int getVertexCount() {
    return vertices.length;
  }

  public int getEdgeCount() {
    return edges.length;
  }
}
