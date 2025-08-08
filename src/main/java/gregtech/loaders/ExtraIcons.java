package gregtech.loaders;

import static gregtech.api.enums.Mods.GregTech;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ExtraIcons {

    private static final String LARGE_FLUID_CELL_CUSTOM = "large_fluid_cell_custom";
    public static IIcon steelLargeCellInner;
    public static IIcon aluminiumLargeCellInner;
    public static IIcon stainlesssteelLargeCellInner;
    public static IIcon tungstensteelLargeCellInner;
    public static IIcon titaniumLargeCellInner;
    public static IIcon chromiumLargeCellInner;
    public static IIcon iridiumLargeCellInner;
    public static IIcon osmiumLargeCellInner;
    public static IIcon neutroniumLargeCellInner;

    @SubscribeEvent
    public void regIcons(TextureStitchEvent.Pre event) {
        TextureMap reg = event.map;
        if (reg.getTextureType() == 1) { // are for items
            steelLargeCellInner = reg.registerIcon(GregTech.getResourcePath(LARGE_FLUID_CELL_CUSTOM, "steel_inner"));
            aluminiumLargeCellInner = reg
                .registerIcon(GregTech.getResourcePath(LARGE_FLUID_CELL_CUSTOM, "aluminium_inner"));
            stainlesssteelLargeCellInner = reg
                .registerIcon(GregTech.getResourcePath(LARGE_FLUID_CELL_CUSTOM, "stainlesssteel_inner"));
            tungstensteelLargeCellInner = reg
                .registerIcon(GregTech.getResourcePath(LARGE_FLUID_CELL_CUSTOM, "tungstensteel_inner"));
            titaniumLargeCellInner = reg
                .registerIcon(GregTech.getResourcePath(LARGE_FLUID_CELL_CUSTOM, "titanium_inner"));
            chromiumLargeCellInner = reg
                .registerIcon(GregTech.getResourcePath(LARGE_FLUID_CELL_CUSTOM, "chromium_inner"));
            iridiumLargeCellInner = reg
                .registerIcon(GregTech.getResourcePath(LARGE_FLUID_CELL_CUSTOM, "iridium_inner"));
            osmiumLargeCellInner = reg.registerIcon(GregTech.getResourcePath(LARGE_FLUID_CELL_CUSTOM, "osmium_inner"));
            neutroniumLargeCellInner = reg
                .registerIcon(GregTech.getResourcePath(LARGE_FLUID_CELL_CUSTOM, "neutronium_inner"));
        }
    }
}
