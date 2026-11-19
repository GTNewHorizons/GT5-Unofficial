package gregtech.api.items.armor.renderer;

import java.util.ArrayList;
import java.util.List;

public final class BakedBone {

    public final String name;
    public final float pivotX, pivotY, pivotZ;
    public final List<BakedPolygon> polygons = new ArrayList<>();

    public BakedBone(String name, float pivotX, float pivotY, float pivotZ) {
        this.name = name;
        this.pivotX = pivotX;
        this.pivotY = pivotY;
        this.pivotZ = pivotZ;
    }
}
