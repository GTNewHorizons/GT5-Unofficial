package gregtech.api.enums.materials;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ruling_0.materiallib.api.Shape;

/// The [Shape]s GregTech declares, collected in declaration order as the holder classes ([Shapes],
/// [CellShapes], [BlockShapes], [OreShapes], [TEBlockShapes]) build them. [gregtech.api.material.MaterialParts]
/// derives its prefix-to-shape map from [#all], so a shape that skips [#reg] is invisible to the legacy prefix
/// cutover. [FluidShapes] stays outside: fluid shapes carry no oredict prefixes.
public final class GTShapeStore {

    private static final List<Shape> SHAPES = new ArrayList<>();
    private static final List<Shape> VIEW = Collections.unmodifiableList(SHAPES);

    private GTShapeStore() {}

    /// Records `shape` and returns it.
    public static Shape reg(Shape shape) {
        SHAPES.add(shape);
        return shape;
    }

    /// Every collected shape, in declaration order. Empty before the holder classes' init.
    public static List<Shape> all() {
        return VIEW;
    }
}
