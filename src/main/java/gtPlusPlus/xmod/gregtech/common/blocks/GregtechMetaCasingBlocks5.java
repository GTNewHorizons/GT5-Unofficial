package gtPlusPlus.xmod.gregtech.common.blocks;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.render.TextureFactory;
import gregtech.common.blocks.MaterialCasings;
import gregtech.common.misc.GTStructureChannels;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGrinderMultiblock;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMetaCasingBlocks5 extends GregtechMetaCasingBlocksAbstract {

    // Free Indexes within TAE: 91, 92, 94, 100, 101, 102, 103, 104, 114, 116, 117, 118, 119, 120, 121, 124, 125, 126,
    // 127
    // 19 Free Indexes
    private static final TexturesGrinderMultiblock mGrinderOverlayHandler = new TexturesGrinderMultiblock();

    public GregtechMetaCasingBlocks5() {
        super(GregtechMetaCasingItems.class, "gtplusplus.blockcasings.5", MaterialCasings.INSTANCE);
        TAE.registerTexture(0, 2, TextureFactory.of(this, 0));
        TAE.registerTexture(0, 3, TextureFactory.of(this, 3));
        TAE.registerTexture(0, 4, TextureFactory.of(this, 4));
        TAE.registerTexture(1, 10, TextureFactory.of(this, 5));
        TAE.registerTexture(1, 11, TextureFactory.of(this, 6));

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

        for (int i = 0; i < 4; i++) {
            GTStructureChannels.QFT_MANIPULATOR.registerAsIndicator(new ItemStack(this, 1, i + 7), i + 1);
        }
        for (int i = 0; i < 4; i++) {
            GTStructureChannels.QFT_SHIELDING.registerAsIndicator(new ItemStack(this, 1, i + 11), i + 1);
        }
    }

    @Override
    public IIcon getIcon(final int ordinalSide, final int aMeta) {
        return getStaticIcon(ordinalSide, aMeta);
    }

    public static IIcon getStaticIcon(final int ordinalSide, final int aMeta) {
        if ((aMeta >= 0) && (aMeta < 16)) {
            return switch (aMeta) {
                case 0 -> TexturesGtBlock.TEXTURE_PIPE_GRINDING_MILL.getIcon();
                case 1 -> TexturesGtBlock.TEXTURE_CASING_GRINDING_MILL.getIcon();
                case 2 -> TexturesGtBlock.TEXTURE_GEARBOX_GRINDING_MILL.getIcon();
                case 3 -> Textures.BlockIcons.ELEMENTAL_CONFINEMENT_SHELL.getIcon();
                case 4 -> TexturesGtBlock.Casing_Machine_Metal_Sheet_H.getIcon();
                case 5 -> TexturesGtBlock.Casing_Machine_Metal_Sheet_I.getIcon();
                case 6 -> Textures.BlockIcons.FORGE_CASING.getIcon();
                case 7 -> ordinalSide < 2 ? Textures.BlockIcons.Manipulator_Top.getIcon()
                    : Textures.BlockIcons.NeutronPulseManipulator.getIcon();
                case 8 -> ordinalSide < 2 ? Textures.BlockIcons.Manipulator_Top.getIcon()
                    : Textures.BlockIcons.CosmicFabricManipulator.getIcon();
                case 9 -> ordinalSide < 2 ? Textures.BlockIcons.Manipulator_Top.getIcon()
                    : Textures.BlockIcons.InfinityInfusedManipulator.getIcon();
                case 10 -> ordinalSide < 2 ? Textures.BlockIcons.Manipulator_Top.getIcon()
                    : Textures.BlockIcons.SpaceTimeContinuumRipper.getIcon();
                case 11 -> Textures.BlockIcons.NeutronShieldingCore.getIcon();
                case 12 -> Textures.BlockIcons.CosmicFabricShieldingCore.getIcon();
                case 13 -> Textures.BlockIcons.InfinityInfusedShieldingCore.getIcon();
                case 14 -> Textures.BlockIcons.SpaceTimeBendingCore.getIcon();
                case 15 -> ordinalSide < 2 ? Textures.BlockIcons.ForceFieldGlassTop.getIcon()
                    : Textures.BlockIcons.ForceFieldGlass.getIcon();
                default -> Textures.GlobalIcons.RENDERING_ERROR.getIcon();
            };
        }
        return Textures.GlobalIcons.RENDERING_ERROR.getIcon();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final IBlockAccess aWorld, final int xCoord, final int yCoord, final int zCoord,
        final int ordinalSide) {
        final GregtechMetaCasingBlocks5 i = this;
        return mGrinderOverlayHandler.handleCasingsGT(aWorld, xCoord, yCoord, zCoord, ordinalSide, i);
    }
}
