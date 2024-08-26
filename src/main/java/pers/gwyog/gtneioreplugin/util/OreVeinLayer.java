package pers.gwyog.gtneioreplugin.util;

public class OreVeinLayer {

    public static final int VEIN_PRIMARY = 0;
    public static final int VEIN_SECONDARY = 1;
    public static final int VEIN_BETWEEN = 2;
    public static final int VEIN_SPORADIC = 3;

    private static final String[] LAYER_NAMES = { "gtnop.gui.nei.primaryOre", "gtnop.gui.nei.secondaryOre",
        "gtnop.gui.nei.betweenOre", "gtnop.gui" + ".nei.sporadicOre" };

    public static String getOreVeinLayerName(int layerId) {
        return LAYER_NAMES[layerId];
    }

    private OreVeinLayer() {}
}
