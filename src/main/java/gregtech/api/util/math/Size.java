package gregtech.api.util.math;

import java.awt.*;
import java.util.Objects;

public class Size {

    public static final Size ZERO = zero();

    public static Size zero() {
        return new Size(0, 0);
    }

    public final int width, height;

    public Size(int width, int height) {
        this.width = Math.max(0, width);
        this.height = Math.max(0, height);
    }

    public Size(double width, double height) {
        this((int) width, (int) height);
    }

    public Size(Size widthComponent, Size heightComponent) {
        this(widthComponent.width, heightComponent.height);
    }

    public Size(int width, Size heightComponent) {
        this(width, heightComponent.height);
    }

    public Size(Size widthComponent, int height) {
        this(widthComponent.width, height);
    }

    public static Size ofDimension(Dimension dimension) {
        return new Size(dimension.width, dimension.height);
    }

    public boolean isLargerThan(Size size) {
        return (size.width * size.height) < (width * height);
    }

    public boolean hasLargerDimensionsThan(Size size) {
        return width > size.width && height > size.height;
    }

    public Size shrink(int width, int height) {
        return new Size(this.width - width, this.height - height);
    }

    public Size grow(int width, int height) {
        return new Size(this.width + width, this.height + height);
    }

    public Size scale(float widthScale, float heightScale) {
        return new Size(this.width * widthScale, this.height * heightScale);
    }

    public Size scale(float scale) {
        return scale(scale, scale);
    }

    public boolean isZero() {
        return width == 0 && height == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Size size = (Size) o;
        return Float.compare(size.width, width) == 0 && Float.compare(size.height, height) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, height);
    }

    @Override
    public String toString() {
        return "[" + width + ", " + height + "]";
    }

    public Dimension asDimension() {
        return new Dimension(width, height);
    }

    public Rectangle asRectangle() {
        return new Rectangle(width, height);
    }
}
