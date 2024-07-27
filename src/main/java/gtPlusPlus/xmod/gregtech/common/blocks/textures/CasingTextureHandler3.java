package gtPlusPlus.xmod.gregtech.common.blocks.textures;

import net.minecraft.util.IIcon;

import gtPlusPlus.xmod.gregtech.common.blocks.GregtechMetaCasingBlocks3;

public class CasingTextureHandler3 {

    public static IIcon getIcon(final int ordinalSide, final int aMeta) { // Texture ID's. case 0 == ID[57]
        if ((aMeta >= 0) && (aMeta < 16)) {
            return switch (aMeta) {
                case 0 ->
                    // Aquatic Casing
                    TexturesGtBlock.TEXTURE_METAL_PANEL_B.getIcon();
                case 1 ->
                    // Inconel Reinforced Casing
                    TexturesGtBlock.TEXTURE_METAL_PANEL_D.getIcon();
                case 2 ->
                    // Multi-Use Casing
                    TexturesGtBlock.TEXTURE_METAL_PANEL_C.getIcon();
                case 3 ->
                    // Trinium Plated Mining Platform Casing
                    TexturesGtBlock.Casing_Trinium_Naquadah_Vent.getIcon();
                case 4 ->
                    // Vanadium Redox IV
                    TexturesGtBlock.Casing_Redox_2.getIcon();
                case 5 ->
                    // Vanadium Redox LuV
                    TexturesGtBlock.Casing_Redox_3.getIcon();
                case 6 ->
                    // Vanadium Redox ZPM
                    TexturesGtBlock.Casing_Redox_4.getIcon();
                case 7 ->
                    // Vanadium Redox UV
                    TexturesGtBlock.Casing_Redox_5.getIcon();
                case 8 ->
                    // Vanadium Redox MAX
                    TexturesGtBlock.Casing_Redox_6.getIcon();
                case 9 ->
                    // Amazon Warehouse Casing
                    TexturesGtBlock.TEXTURE_CASING_AMAZON.getIcon();
                case 10 ->
                    // Adv. Vac. Freezer
                    TexturesGtBlock.TEXTURE_CASING_ADVANCED_CRYOGENIC.getIcon();
                case 11 ->
                    // Adv. EBF
                    TexturesGtBlock.TEXTURE_CASING_ADVANCED_VOLCNUS.getIcon();
                case 12 -> TexturesGtBlock.TEXTURE_CASING_FUSION_COIL_II.getIcon();
                case 13 -> TexturesGtBlock.TEXTURE_CASING_FUSION_COIL_II_INNER.getIcon();
                case 14 -> TexturesGtBlock.TEXTURE_CASING_FUSION_CASING_ULTRA.getIcon();
                case 15 -> TexturesGtBlock.TEXTURE_MAGIC_PANEL_A.getIcon();
                default -> TexturesGtBlock._PlaceHolder.getIcon();
            };
        }
        return TexturesGtBlock._PlaceHolder.getIcon();
    }

    static {
        GregtechMetaCasingBlocks3.mConnectedMachineTextures = true;
    }
}
