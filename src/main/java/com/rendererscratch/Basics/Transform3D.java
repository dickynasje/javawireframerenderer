package com.rendererscratch.Basics;

public class Transform3D {

  public static Point3d rotateX(Point3d point, float angle) {
    float cos = (float) Math.cos(angle);
    float sin = (float) Math.sin(angle);

    return new Point3d(
        point.getX(),
        cos * point.getY() - sin * point.getZ(),
        sin * point.getY() + cos * point.getZ());
  }

  public static Point3d rotateY(Point3d point, float angle) {
    float cos = (float) Math.cos(angle);
    float sin = (float) Math.sin(angle);

    return new Point3d(
        cos * point.getX() - sin * point.getZ(),
        point.getY(),
        sin * point.getX() + cos * point.getZ());
  }

  public static Point3d rotateZ(Point3d point, float angle) {
    float cos = (float) Math.cos(angle);
    float sin = (float) Math.sin(angle);

    return new Point3d(
        cos * point.getX() - sin * point.getY(),
        sin * point.getX() + cos * point.getY(),
        point.getZ());
  }

  public static Point3d rotate(Point3d point, float angleX, float angleY, float angleZ) {
    Point3d result = point;
    if (angleX != 0)
      result = rotateX(result, angleX);
    if (angleY != 0)
      result = rotateY(result, angleY);
    if (angleZ != 0)
      result = rotateZ(result, angleZ);
    return result;
  }

  public static Point3d scale(Point3d point, float scale) {
    return new Point3d(
        point.getX() * scale,
        point.getY() * scale,
        point.getZ() * scale);
  }

  public static Point3d translate(Point3d point, float dx, float dy, float dz) {
    return new Point3d(
        point.getX() + dx,
        point.getY() + dy,
        point.getZ() + dz);
  }
}
