package gregtech.loaders;

import static gregtech.api.enums.GT_Values.RES_PATH_ITEM;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ExtraIcons {

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
            steelLargeCellInner = reg.registerIcon(RES_PATH_ITEM + "large_fluid_cell_custom/steel_inner");
            aluminiumLargeCellInner = reg.registerIcon(RES_PATH_ITEM + "large_fluid_cell_custom/aluminium_inner");
            stainlesssteelLargeCellInner = reg.registerIcon(
                    RES_PATH_ITEM + "large_fluid_cell_custom/stainlesssteel_inner");
            tungstensteelLargeCellInner = reg.registerIcon(
                    RES_PATH_ITEM + "large_fluid_cell_custom/tungstensteel_inner");
            titaniumLargeCellInner = reg.registerIcon(RES_PATH_ITEM + "large_fluid_cell_custom/titanium_inner");
            chromiumLargeCellInner = reg.registerIcon(RES_PATH_ITEM + "large_fluid_cell_custom/chromium_inner");
            iridiumLargeCellInner = reg.registerIcon(RES_PATH_ITEM + "large_fluid_cell_custom/iridium_inner");
            osmiumLargeCellInner = reg.registerIcon(RES_PATH_ITEM + "large_fluid_cell_custom/osmium_inner");
            neutroniumLargeCellInner = reg.registerIcon(RES_PATH_ITEM + "large_fluid_cell_custom/neutronium_inner");
        }
    }
}
