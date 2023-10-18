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
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.blocks.GT_Material_Casings;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.objects.GTPP_CopiedBlockTexture;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.turbine.LargeTurbineTextureHandler;

public class GregtechMetaSpecialMultiCasings extends GregtechMetaCasingBlocksAbstract {

    public static class SpecialCasingItemBlock extends GregtechMetaCasingItems {

        public SpecialCasingItemBlock(Block par1) {
            super(par1);
        }

        @Override
        public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List aList, boolean aF3_H) {
            int aMeta = aStack.getItemDamage();
            if (aMeta < 10) {
                // aList.add("Tier: "+GT_Values.VN[aMeta]);
            }
            super.addInformation(aStack, aPlayer, aList, aF3_H);
        }
    }

    public GregtechMetaSpecialMultiCasings() {
        super(SpecialCasingItemBlock.class, "gtplusplus.blockspecialcasings.1", GT_Material_Casings.INSTANCE);
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".0.name", "Turbine Shaft");
        GT_LanguageManager
                .addStringLocalization(this.getUnlocalizedName() + ".1.name", "Reinforced Steam Turbine Casing");
        GT_LanguageManager
                .addStringLocalization(this.getUnlocalizedName() + ".2.name", "Reinforced HP Steam Turbine Casing");
        GT_LanguageManager
                .addStringLocalization(this.getUnlocalizedName() + ".3.name", "Reinforced Gas Turbine Casing");
        GT_LanguageManager
                .addStringLocalization(this.getUnlocalizedName() + ".4.name", "Reinforced Plasma Turbine Casing");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".5.name", "Tesla Containment Casing");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".6.name", "Structural Solar Casing");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".7.name", "Salt Containment Casing");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".8.name", "Thermally Insulated Casing");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".9.name", "Flotation Cell Casings");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".10.name", "Reinforced Engine Casing");
        GT_LanguageManager
                .addStringLocalization(this.getUnlocalizedName() + ".11.name", "Molecular Containment Casing");
        GT_LanguageManager
                .addStringLocalization(this.getUnlocalizedName() + ".12.name", "High Voltage Current Capacitor");
        GT_LanguageManager.addStringLocalization(this.getUnlocalizedName() + ".13.name", "Particle Containment Casing");
        GT_LanguageManager
                .addStringLocalization(this.getUnlocalizedName() + ".14.name", "Reinforced Heat Exchanger Casing");
        GT_LanguageManager
                .addStringLocalization(this.getUnlocalizedName() + ".15.name", "Reinforced SC Turbine Casing");
        TAE.registerTexture(1, 12, new GTPP_CopiedBlockTexture(this, 6, 14));

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
        return getStaticIcon((byte) ordinalSide, (byte) aMeta);
    }

    public static IIcon getStaticIcon(final int ordinalSide, final byte aMeta) {
        return switch (aMeta) {
            case 0 -> TexturesGtBlock.Casing_Redox_1.getIcon();
            case 1 -> Textures.BlockIcons.MACHINE_CASING_TURBINE.getIcon();
            case 2 -> Textures.BlockIcons.MACHINE_CASING_STABLE_TITANIUM.getIcon();
            case 3 -> Textures.BlockIcons.MACHINE_CASING_CLEAN_STAINLESSSTEEL.getIcon();
            case 4 -> Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
            case 5 -> TexturesGtBlock.Casing_Material_RedSteel.getIcon();
            case 6 -> TexturesGtBlock.Casing_Material_MaragingSteel.getIcon();
            case 7 -> TexturesGtBlock.Casing_Material_Stellite.getIcon();
            case 8 -> TexturesGtBlock.Casing_Machine_Simple_Top.getIcon();
            case 9 -> TexturesGtBlock.TEXTURE_CASING_FLOTATION.getIcon();
            case 10 -> TexturesGtBlock.Casing_Material_Talonite.getIcon();
            case 11 -> Textures.BlockIcons.MACHINE_CASING_RADIATIONPROOF.getIcon();
            case 12 -> TexturesGtBlock.Casing_Redox_5.getIcon();
            case 13 -> TexturesGtBlock.TEXTURE_MAGIC_PANEL_B.getIcon();
            case 14 -> TexturesGtBlock.Casing_Material_Talonite.getIcon();
            case 15 -> TexturesGtBlock.Turbine_SC_Material_Casing.getIcon();
            default -> Textures.BlockIcons.RENDERING_ERROR.getIcon();
        };
    }
}
