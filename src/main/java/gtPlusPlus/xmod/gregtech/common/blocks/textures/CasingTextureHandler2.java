package gtPlusPlus.xmod.gregtech.common.blocks.textures;

import net.minecraft.util.IIcon;

import gregtech.api.enums.Textures;

public class CasingTextureHandler2 {

    public static IIcon getIcon(final int ordinalSide, final int aMeta) { // Texture ID's. case 0 == ID[57]
        if ((aMeta >= 0) && (aMeta < 16)) {
            switch (aMeta) {
                case 0 -> {
                    return TexturesGtBlock.Casing_Material_RedSteel.getIcon();
                }
                case 1 -> {
                    return TexturesGtBlock.Casing_Material_HastelloyX.getIcon();
                }
                case 2 -> {
                    return TexturesGtBlock.Casing_Material_HastelloyN.getIcon();
                }
                case 3 -> {
                    return TexturesGtBlock.Casing_Material_Fluid_IncoloyDS.getIcon();
                }
                case 4 -> {
                    return TexturesGtBlock.Casing_Material_Grisium.getIcon();
                }
                case 5 -> {
                    return TexturesGtBlock.Casing_Machine_Metal_Panel_A.getIcon();
                }
                case 6 -> {
                    return TexturesGtBlock.Casing_Machine_Metal_Grate_A.getIcon();
                }
                case 7 -> {
                    return TexturesGtBlock.Casing_Redox_1.getIcon();
                }
                case 8 -> {
                    return TexturesGtBlock.Casing_Machine_Metal_Sheet_A.getIcon();
                }
                case 9 -> {
                    return TexturesGtBlock.Overlay_Machine_Cyber_A.getIcon();
                }
                case 10 -> {
                    return Textures.BlockIcons.MACHINE_CASING_RADIATIONPROOF.getIcon();
                }
                case 11 -> {
                    return TexturesGtBlock.Casing_Material_Tantalloy61.getIcon();
                }
                case 12 -> {
                    return TexturesGtBlock.Casing_Machine_Simple_Top.getIcon();
                }
                case 13 -> {
                    if (ordinalSide < 2) {
                        return TexturesGtBlock.TEXTURE_TECH_A.getIcon();
                    } else {
                        return TexturesGtBlock.TEXTURE_TECH_B.getIcon();
                    }
                }
                case 14 -> {
                    return Textures.BlockIcons.RENDERING_ERROR.getIcon();
                }
                case 15 -> {
                    return TexturesGtBlock.Casing_Machine_Acacia_Log.getIcon();
                }
                default -> {
                    return TexturesGtBlock.Overlay_UU_Matter.getIcon();
                }
            }
        }
        return TexturesGtBlock._PlaceHolder.getIcon();
    }
}
