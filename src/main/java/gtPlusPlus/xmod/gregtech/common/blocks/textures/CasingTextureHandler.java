package gtPlusPlus.xmod.gregtech.common.blocks.textures;

import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Textures;
import gtPlusPlus.xmod.gregtech.common.blocks.GregtechMetaCasingBlocks;

public class CasingTextureHandler {

    public static IIcon getIcon(final int ordinalSide, final int aMeta) { // Texture ID's. case 0 == ID[57]
        if ((aMeta >= 0) && (aMeta < 16)) {
            return switch (aMeta) {
                // Centrifuge
                case 0 -> TexturesGtBlock.Casing_Material_Centrifuge.getIcon();
                // Coke Oven Frame
                case 1 -> TexturesGtBlock.Casing_Material_Tantalloy61.getIcon();
                // Coke Oven Casing Tier 1
                case 2 -> Textures.BlockIcons.MACHINE_CASING_FIREBOX_BRONZE.getIcon();
                // Coke Oven Casing Tier 2
                case 3 -> Textures.BlockIcons.MACHINE_CASING_FIREBOX_STEEL.getIcon();
                // Material Press Casings
                case 4 -> Textures.BlockIcons.MACHINE_CASING_STABLE_TITANIUM.getIcon();
                // Electrolyzer Casings
                case 5 -> TexturesGtBlock.Casing_Material_Potin.getIcon();
                // Broken Blue Fusion Casings
                case 6 -> TexturesGtBlock.Casing_Material_MaragingSteel.getIcon();
                // Maceration Stack Casings
                case 7 -> TexturesGtBlock.Casing_Material_Tumbaga.getIcon();
                // Broken Pink Fusion Casings
                case 8 -> TexturesGtBlock.TEXTURE_ORGANIC_PANEL_A_GLOWING.getIcon();
                // Matter Fabricator Casings
                case 9 -> TexturesGtBlock.TEXTURE_METAL_PANEL_F.getIcon();
                // Iron Blast Fuance Textures
                case 10 -> TexturesGtBlock.Casing_Machine_Simple_Top.getIcon();
                // Multitank Exterior Casing
                case 11 -> TexturesGtBlock.Casing_Material_Grisium.getIcon();
                // Reactor Casing I
                case 12 -> TexturesGtBlock.Casing_Material_Stellite.getIcon();
                // Reactor Casing II
                case 13 -> TexturesGtBlock.Casing_Material_Zeron100.getIcon();
                case 14 -> TexturesGtBlock.Casing_Staballoy_Firebox.getIcon();
                case 15 -> TexturesGtBlock.Casing_Material_ZirconiumCarbide.getIcon();
                default -> Textures.BlockIcons.MACHINE_CASING_RADIOACTIVEHAZARD.getIcon();
            };
        }
        return Textures.BlockIcons.MACHINE_CASING_GEARBOX_TUNGSTENSTEEL.getIcon();
    }

    public static IIcon handleCasingsGT(final IBlockAccess aWorld, final int xCoord, final int yCoord, final int zCoord,
        final ForgeDirection side, final GregtechMetaCasingBlocks thisBlock) {
        final int tMeta = aWorld.getBlockMetadata(xCoord, yCoord, zCoord);
        final int ordinalSide = side.ordinal();
        if (tMeta != 0) {
            return getIcon(ordinalSide, tMeta);
        }

        return TexturesGtBlock.Casing_Material_Centrifuge.getIcon();
    }
}
