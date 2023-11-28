package gregtech.api.modernmaterials.blocks.blocktypes.framebox.special;

import static gregtech.api.enums.Mods.GregTech;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.TextureStitchEvent;

public class CustomTextureRegister {

    public static IIcon universiumFrameTexture;
    public static IIcon universiumSolidBlockTexutre;

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
        // This is a bit of a hack here, we do this solely because rendering a block icon will cause the stars
        // in the shader to not appear as they are part of the item atlas. So we store our block in the item atlas
        // to draw it along with the stars. Otherwise, the shader would need to be edited.
        universiumFrameTexture = map.registerIcon(GregTech.getResourcePath() + "test");
        universiumSolidBlockTexutre = map.registerIcon(GregTech.getResourcePath() + "test1");
    }

    private static void registerBlocks(TextureMap map) {
        // Normal block/fluid textures go here.
    }
}
