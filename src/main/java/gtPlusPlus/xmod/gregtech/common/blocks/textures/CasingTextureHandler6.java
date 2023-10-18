package gtPlusPlus.xmod.gregtech.common.blocks.textures;

import net.minecraft.util.IIcon;

import gtPlusPlus.xmod.gregtech.common.blocks.GregtechMetaCasingBlocks6;

public class CasingTextureHandler6 {

    public static IIcon getIcon(final int ordinalSide, final int aMeta) { // Texture ID's. case 0 == ID[57]
        if ((aMeta >= 0) && (aMeta < 16)) {
            return switch (aMeta) {
                case 0 -> TexturesGtBlock.TEXTURE_CASING_FUSION_COIL_III.getIcon();
                case 1 -> TexturesGtBlock.TEXTURE_CASING_FUSION_COIL_III_INNER.getIcon();
                case 2 -> TexturesGtBlock.TEXTURE_CASING_FUSION_CASING_HYPER.getIcon();
                default -> TexturesGtBlock._PlaceHolder.getIcon();
            };
        }
        return TexturesGtBlock._PlaceHolder.getIcon();
    }

    static {
        GregtechMetaCasingBlocks6.mConnectedMachineTextures = true;
    }

}
