package com.rendererscratch.Basics;

public class Projection {
  private final int screenWidth;
  private final int screenHeight;
  private final float focalLength;
  private final float scale;

  public Projection(int screenWidth, int screenHeight, float focalLength, float scale) {
    this.screenWidth = screenWidth;
    this.screenHeight = screenHeight;
    this.focalLength = focalLength;
    this.scale = scale;
  }

  public Point2d projectToScreen(Point3d point) {
    float depth = focalLength + point.getZ();
    float x = screenWidth / 2 + (focalLength * point.getX() / depth) * scale;
    float y = screenHeight / 2 + (focalLength * point.getY() / depth) * scale;
    return new Point2d(x, y);
  }

  public float normalizeX(float screenX) {
    return (screenX / screenWidth) * 2.0f - 1.0f;
  }

  public float normalizeY(float screenY) {
    return -((screenY / screenHeight) * 2.0f - 1.0f);
  }

  public Point2d normalize(Point2d screenPoint) {
    return new Point2d(
        normalizeX(screenPoint.getX()),
        normalizeY(screenPoint.getY()));
  }
}
