package gregtech.api.util.math;

import java.awt.*;
import java.util.Objects;

public class Pos2d {

    public static final Pos2d ZERO = zero();

    public static Pos2d zero() {
        return new Pos2d(0, 0);
    }

    public final int x, y;

    public Pos2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Pos2d(double x, double y) {
        this((int) x, (int) y);
    }

    public static Pos2d ofPoint(Point point) {
        return new Pos2d(point.x, point.y);
    }

    public static Pos2d cartesian(int x, int y) {
        return new Pos2d(x, y);
    }

    public static Pos2d polar(float angle, float length) {
        float sin = (float) Math.sin(Math.toRadians(angle));
        float cos = (float) Math.cos(Math.toRadians(angle));
        return new Pos2d(cos * length, sin * length);
    }

    public Pos2d add(Pos2d p) {
        return add(p.x, p.y);
    }

    public Pos2d add(int x, int y) {
        return new Pos2d(this.x + x, this.y + y);
    }

    public Pos2d subtract(Pos2d p) {
        return subtract(p.x, p.y);
    }

    public Pos2d subtract(int x, int y) {
        return new Pos2d(this.x - x, this.y - y);
    }

    public double distance(Pos2d p) {
        float x = Math.max(this.x - p.x, p.x - this.x);
        float y = Math.max(this.y - p.y, p.y - this.y);
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public float angle(Pos2d p) {
        float x = this.x - p.x;
        float y = this.y - p.y;
        return (float) Math.toDegrees(Math.atan(y / x)) + 90;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public boolean isZero() {
        return x == 0 && y == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pos2d pos = (Pos2d) o;
        return Float.compare(pos.x, x) == 0 && Float.compare(pos.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + ']';
    }
}
