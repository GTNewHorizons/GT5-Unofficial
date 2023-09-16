package gregtech.api.ModernMaterials.Blocks.FrameBox.TESR;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.GregTech_API;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;

import static gregtech.api.enums.Mods.GregTech;

public class CustomTextureRegister {

    public static IIcon universiumFrameTexture;

    public static void registerIcons(TextureStitchEvent.Pre event) {

        TextureMap map = event.map;

        // Yes I know this is the item atlas, yes it is deliberate.
        if (map.getTextureType() == 1) {
            registerItems(map);
        } else {
            registerBlocks(map);
        }
    }

    private static void registerItems(TextureMap map) {
        universiumFrameTexture = map.registerIcon(GregTech.getResourcePath() + "test");
    }

    private static void registerBlocks(TextureMap map) {
        // Normal block/fluid textures go here.
    }
}
