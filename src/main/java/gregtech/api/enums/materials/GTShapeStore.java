package gregtech.api.enums.materials;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ruling_0.materiallib.api.Shape;
import com.ruling_0.materiallib.api.StandardProperties;

import gregtech.api.enums.OrePrefixes;

/// The [Shape]s GregTech declares, in declaration order. [gregtech.api.material.MaterialParts] maps oredict
/// prefixes to shapes from [#all], so every shape with a prefix must go through [#reg]. [FluidShapes] has no
/// prefixes and stays out.
public final class GTShapeStore {

    private static final List<Shape> SHAPES = new ArrayList<>();
    private static final List<Shape> VIEW = Collections.unmodifiableList(SHAPES);

    private GTShapeStore() {}

    /// Records `shape`, names its parts through [OrePrefixes#shapeDisplayName], and returns it.
    public static Shape reg(Shape shape) {
        SHAPES.add(shape);
        return shape.setProperty(StandardProperties.NAME_FORMATTER, OrePrefixes::shapeDisplayName);
    }

    /// Every collected shape, in declaration order. Empty before the holder classes' init.
    public static List<Shape> all() {
        return VIEW;
    }
}
