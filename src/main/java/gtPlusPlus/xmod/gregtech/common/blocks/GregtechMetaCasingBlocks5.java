package gtPlusPlus.xmod.gregtech.common.blocks;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.blocks.GT_Material_Casings;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.objects.GTPP_CopiedBlockTexture;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGrinderMultiblock;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMetaCasingBlocks5 extends GregtechMetaCasingBlocksAbstract {

    // Free Indexes within TAE: 91, 92, 94, 100, 101, 102, 103, 104, 114, 116, 117, 118, 119, 120, 121, 124, 125, 126,
    // 127
    // 19 Free Indexes
    private static final TexturesGrinderMultiblock mGrinderOverlayHandler = new TexturesGrinderMultiblock();

    public GregtechMetaCasingBlocks5() {
        super(GregtechMetaCasingItems.class, "gtplusplus.blockcasings.5", GT_Material_Casings.INSTANCE);
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".0.name", "IsaMill Exterior Casing"); // IsaMill
                                                                                                                    // Casing
        TAE.registerTexture(0, 2, new GTPP_CopiedBlockTexture(this, 6, 0));
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".1.name", "IsaMill Piping"); // IsaMill
                                                                                                           // Pipe
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".2.name", "IsaMill Gearbox"); // IsaMill
                                                                                                            // Gearbox
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".3.name", "Elemental Confinement Shell"); // Duplicator
                                                                                                                        // Casing
        TAE.registerTexture(0, 3, new GTPP_CopiedBlockTexture(this, 6, 3));
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".4.name", "Sparge Tower Exterior Casing"); // Sparge
                                                                                                                         // Tower
                                                                                                                         // Casing
        TAE.registerTexture(0, 4, new GTPP_CopiedBlockTexture(this, 6, 4));
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".5.name", "Sturdy Printer Casing"); // Unused
        TAE.registerTexture(1, 10, new GTPP_CopiedBlockTexture(this, 6, 5));
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".6.name", "Forge Casing"); // Forge Hammer
                                                                                                         // Casing
        TAE.registerTexture(1, 11, new GTPP_CopiedBlockTexture(this, 6, 6));
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".7.name", "Neutron Pulse Manipulator");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".8.name", "Cosmic Fabric Manipulator");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".9.name", "Infinity Infused Manipulator");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".10.name", "SpaceTime Continuum Ripper");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".11.name", "Neutron Shielding Core");
        GT_LanguageManager
                .addStringLocalization(this.getUnlocalizedName() + ".12.name", "Cosmic Fabric Shielding Core");
        GT_LanguageManager
                .addStringLocalization(this.getUnlocalizedName() + ".13.name", "Infinity Infused Shielding Core");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".14.name", "SpaceTime Bending Core");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".15.name", "Force Field Glass");

        GregtechItemList.Casing_IsaMill_Casing.set(new ItemStack(this, 1, 0));
        GregtechItemList.Casing_IsaMill_Pipe.set(new ItemStack(this, 1, 1));
        GregtechItemList.Casing_IsaMill_Gearbox.set(new ItemStack(this, 1, 2));
        GregtechItemList.Casing_ElementalDuplicator.set(new ItemStack(this, 1, 3));
        GregtechItemList.Casing_Sparge_Tower_Exterior.set(new ItemStack(this, 1, 4));
        GregtechItemList.Casing_IndustrialAutoChisel.set(new ItemStack(this, 1, 5));
        GregtechItemList.Casing_IndustrialForgeHammer.set(new ItemStack(this, 1, 6));
        GregtechItemList.NeutronPulseManipulator.set(new ItemStack(this, 1, 7));
        GregtechItemList.CosmicFabricManipulator.set(new ItemStack(this, 1, 8));
        GregtechItemList.InfinityInfusedManipulator.set(new ItemStack(this, 1, 9));
        GregtechItemList.SpaceTimeContinuumRipper.set(new ItemStack(this, 1, 10));
        GregtechItemList.NeutronShieldingCore.set(new ItemStack(this, 1, 11));
        GregtechItemList.CosmicFabricShieldingCore.set(new ItemStack(this, 1, 12));
        GregtechItemList.InfinityInfusedShieldingCore.set(new ItemStack(this, 1, 13));
        GregtechItemList.SpaceTimeBendingCore.set(new ItemStack(this, 1, 14));
        GregtechItemList.ForceFieldGlass.set(new ItemStack(this, 1, 15));
    }

    @Override
    public IIcon getIcon(final int ordinalSide, final int aMeta) {
        return getStaticIcon(ordinalSide, aMeta);
    }

    public static IIcon getStaticIcon(final int ordinalSide, final int aMeta) {
        if ((aMeta >= 0) && (aMeta < 16)) {
            switch (aMeta) {
                case 0 -> {
                    return TexturesGtBlock.TEXTURE_PIPE_GRINDING_MILL.getIcon();
                }
                case 1 -> {
                    return TexturesGtBlock.TEXTURE_CASING_GRINDING_MILL.getIcon();
                }
                case 2 -> {
                    return TexturesGtBlock.TEXTURE_GEARBOX_GRINDING_MILL.getIcon();
                }
                case 3 -> {
                    return TexturesGtBlock.TEXTURE_TECH_PANEL_D.getIcon();
                }
                case 4 -> {
                    return TexturesGtBlock.Casing_Machine_Metal_Sheet_H.getIcon();
                }
                case 5 -> {
                    return TexturesGtBlock.Casing_Machine_Metal_Sheet_I.getIcon();
                }
                case 6 -> {
                    return TexturesGtBlock.TEXTURE_TECH_PANEL_H.getIcon();
                }
                case 7 -> {
                    if (ordinalSide == 0 || ordinalSide == 1) {
                        return TexturesGtBlock.Manipulator_Top.getIcon();
                    }
                    return TexturesGtBlock.NeutronPulseManipulator.getIcon();
                }
                case 8 -> {
                    if (ordinalSide == 0 || ordinalSide == 1) {
                        return TexturesGtBlock.Manipulator_Top.getIcon();
                    }
                    return TexturesGtBlock.CosmicFabricManipulator.getIcon();
                }
                case 9 -> {
                    if (ordinalSide == 0 || ordinalSide == 1) {
                        return TexturesGtBlock.Manipulator_Top.getIcon();
                    }
                    return TexturesGtBlock.InfinityInfusedManipulator.getIcon();
                }
                case 10 -> {
                    if (ordinalSide == 0 || ordinalSide == 1) {
                        return TexturesGtBlock.Manipulator_Top.getIcon();
                    }
                    return TexturesGtBlock.SpaceTimeContinuumRipper.getIcon();
                }
                case 11 -> {
                    return TexturesGtBlock.NeutronShieldingCore.getIcon();
                }
                case 12 -> {
                    return TexturesGtBlock.CosmicFabricShieldingCore.getIcon();
                }
                case 13 -> {
                    return TexturesGtBlock.InfinityInfusedShieldingCore.getIcon();
                }
                case 14 -> {
                    return TexturesGtBlock.SpaceTimeBendingCore.getIcon();
                }
                case 15 -> {
                    if (ordinalSide == 0 || ordinalSide == 1) {
                        return TexturesGtBlock.Blank.getIcon();
                    }
                    return TexturesGtBlock.ForceFieldGlass.getIcon();
                }
            }
        }
        return Textures.BlockIcons.RENDERING_ERROR.getIcon();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final IBlockAccess aWorld, final int xCoord, final int yCoord, final int zCoord,
            final int ordinalSide) {
        final GregtechMetaCasingBlocks5 i = this;
        return mGrinderOverlayHandler.handleCasingsGT(aWorld, xCoord, yCoord, zCoord, ordinalSide, i);
    }
}
