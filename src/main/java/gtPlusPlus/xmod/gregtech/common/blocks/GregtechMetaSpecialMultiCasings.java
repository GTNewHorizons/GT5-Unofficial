package gtPlusPlus.xmod.gregtech.common.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.render.TextureFactory;
import gregtech.common.blocks.MaterialCasings;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.turbine.LargeTurbineTextureHandler;

public class GregtechMetaSpecialMultiCasings extends GregtechMetaCasingBlocksAbstract {

    public static class SpecialCasingItemBlock extends GregtechMetaCasingItems {

        public SpecialCasingItemBlock(Block par1) {
            super(par1);
        }

        @Override
        public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List aList, boolean aF3_H) {
            super.addInformation(aStack, aPlayer, aList, aF3_H);
        }
    }

    public GregtechMetaSpecialMultiCasings() {
        super(SpecialCasingItemBlock.class, "gtplusplus.blockspecialcasings.1", MaterialCasings.INSTANCE);
        TAE.registerTexture(1, 12, TextureFactory.of(this, 14));

        GregtechItemList.Casing_Turbine_Shaft.set(new ItemStack(this, 1, 0));
        GregtechItemList.Casing_Turbine_LP.set(new ItemStack(this, 1, 1));
        GregtechItemList.Casing_Turbine_HP.set(new ItemStack(this, 1, 2));
        GregtechItemList.Casing_Turbine_Gas.set(new ItemStack(this, 1, 3));
        GregtechItemList.Casing_Turbine_Plasma.set(new ItemStack(this, 1, 4));
        GregtechItemList.Casing_SolarTower_Structural.set(new ItemStack(this, 1, 6));
        GregtechItemList.Casing_SolarTower_SaltContainment.set(new ItemStack(this, 1, 7));
        GregtechItemList.Casing_SolarTower_HeatContainment.set(new ItemStack(this, 1, 8));
        GregtechItemList.Casing_Flotation_Cell.set(new ItemStack(this, 1, 9));
        GregtechItemList.Casing_Reinforced_Engine_Casing.set(new ItemStack(this, 1, 10));
        GregtechItemList.Casing_Molecular_Transformer_1.set(new ItemStack(this, 1, 11));
        GregtechItemList.Casing_Molecular_Transformer_2.set(new ItemStack(this, 1, 12));
        GregtechItemList.Casing_Molecular_Transformer_3.set(new ItemStack(this, 1, 13));
        GregtechItemList.Casing_XL_HeatExchanger.set(new ItemStack(this, 1, 14));
        GregtechItemList.Casing_Turbine_SC.set(new ItemStack(this, 1, 15));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final IBlockAccess aWorld, final int xCoord, final int yCoord, final int zCoord,
        final int ordinalSide) {
        return LargeTurbineTextureHandler
            .handleCasingsGT(aWorld, xCoord, yCoord, zCoord, ForgeDirection.getOrientation(ordinalSide), this);
    }

    @Override
    public IIcon getIcon(final int ordinalSide, final int aMeta) {
        return getStaticIcon((byte) ordinalSide, aMeta);
    }

    public static IIcon getStaticIcon(final int ordinalSide, final int aMeta) {
        return switch (aMeta) {
            case 0 -> TexturesGtBlock.Casing_Redox_1.getIcon();
            case 1 -> Textures.BlockIcons.MACHINE_CASING_TURBINE_STEEL.getIcon();
            case 2 -> Textures.BlockIcons.MACHINE_CASING_TURBINE_TITANIUM.getIcon();
            case 3 -> Textures.BlockIcons.MACHINE_CASING_TURBINE_STAINLESSSTEEL.getIcon();
            case 4 -> Textures.BlockIcons.MACHINE_CASING_TURBINE_TUNGSTENSTEEL.getIcon();
            case 5 -> TexturesGtBlock.Casing_Material_RedSteel.getIcon();
            case 6 -> TexturesGtBlock.Casing_Material_MaragingSteel.getIcon();
            case 7 -> TexturesGtBlock.Casing_Material_Stellite.getIcon();
            case 8 -> TexturesGtBlock.Casing_Machine_Simple_Top.getIcon();
            case 9 -> TexturesGtBlock.TEXTURE_CASING_FLOTATION.getIcon();
            case 10, 14 -> TexturesGtBlock.Casing_Material_Talonite.getIcon();
            case 11 -> Textures.BlockIcons.MACHINE_CASING_RADIATIONPROOF.getIcon();
            case 12 -> TexturesGtBlock.Casing_Redox_5.getIcon();
            case 13 -> TexturesGtBlock.TEXTURE_MAGIC_PANEL_B.getIcon();
            case 15 -> TexturesGtBlock.Turbine_SC_Material_Casing.getIcon();
            default -> Textures.BlockIcons.RENDERING_ERROR.getIcon();
        };
    }
}
