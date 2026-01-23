package gtPlusPlus.xmod.gregtech.common.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import gregtech.api.enums.TAE;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTUtility;
import gregtech.common.blocks.MaterialCasings;
import gregtech.common.misc.GTStructureChannels;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.CasingTextureHandler2;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.storage.MTEPowerSubStation;

public class GregtechMetaCasingBlocks2 extends GregtechMetaCasingBlocksAbstract {

    @Override
    public void getSubBlocks(Item aItem, CreativeTabs par2CreativeTabs, List aList) {
        aList.add(new ItemStack(aItem, 1, 0));
        aList.add(new ItemStack(aItem, 1, 1));
        aList.add(new ItemStack(aItem, 1, 2));
        aList.add(new ItemStack(aItem, 1, 3));
        aList.add(new ItemStack(aItem, 1, 4));
        aList.add(new ItemStack(aItem, 1, 5));
        aList.add(new ItemStack(aItem, 1, 6));
        aList.add(new ItemStack(aItem, 1, 7));
        aList.add(new ItemStack(aItem, 1, 8));
        aList.add(new ItemStack(aItem, 1, 9));
        aList.add(new ItemStack(aItem, 1, 10));
        aList.add(new ItemStack(aItem, 1, 11));
        aList.add(new ItemStack(aItem, 1, 12));
        aList.add(new ItemStack(aItem, 1, 13));

        aList.add(new ItemStack(aItem, 1, 15));

    }

    public static class GregtechMetaCasingItemBlocks2 extends GregtechMetaCasingItems {

        public GregtechMetaCasingItemBlocks2(Block par1) {
            super(par1);
        }

        @Override
        public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List aList, boolean aF3_H) {
            int meta = aStack.getItemDamage();
            int tier = MTEPowerSubStation.getCellTier(field_150939_a, meta);
            if (meta == 7 && tier > 0) {
                long capacity = MTEPowerSubStation.getCapacityFromCellTier(tier);
                aList.add(
                    StatCollector.translateToLocalFormatted(
                        "gtpp.tooltip.meta_casing.energy_storage",
                        formatNumber(capacity)));
            }
            super.addInformation(aStack, aPlayer, aList, aF3_H);
        }
    }

    public GregtechMetaCasingBlocks2() {
        super(GregtechMetaCasingItemBlocks2.class, "gtplusplus.blockcasings.2", MaterialCasings.INSTANCE);
        for (byte i = 0; i < 16; i = (byte) (i + 1)) {
            if (i == 4 || i == 10 || i == 11 || i == 12 || i == 14) {
                continue;
            }
            TAE.registerTexture(1, i, TextureFactory.of(this, i));
        }
        GTLanguageManager.addStringLocalization(this.getUnlocalizedName() + ".0.name", "Thermal Processing Casing");
        GTLanguageManager.addStringLocalization(this.getUnlocalizedName() + ".1.name", "Hastelloy-N Sealant Block");
        GTLanguageManager.addStringLocalization(this.getUnlocalizedName() + ".2.name", "Hastelloy-X Structural Block");
        GTLanguageManager
            .addStringLocalization(this.getUnlocalizedName() + ".3.name", "Incoloy-DS Fluid Containment Block");
        GTLanguageManager.addStringLocalization(this.getUnlocalizedName() + ".4.name", "Wash Plant Casing");
        GTLanguageManager.addStringLocalization(this.getUnlocalizedName() + ".5.name", "Industrial Sieve Casing");
        GTLanguageManager.addStringLocalization(this.getUnlocalizedName() + ".6.name", "Large Sieve Grate");
        GTLanguageManager
            .addStringLocalization(this.getUnlocalizedName() + ".7.name", "Vanadium Redox Power Cell (EV)");
        GTLanguageManager.addStringLocalization(this.getUnlocalizedName() + ".8.name", "Sub-Station External Casing");
        GTLanguageManager.addStringLocalization(this.getUnlocalizedName() + ".9.name", "Cyclotron Coil");
        GTLanguageManager.addStringLocalization(this.getUnlocalizedName() + ".10.name", "Cyclotron Outer Casing");
        GTLanguageManager.addStringLocalization(this.getUnlocalizedName() + ".11.name", "Thermal Containment Casing");
        GTLanguageManager.addStringLocalization(this.getUnlocalizedName() + ".12.name", "Bulk Production Frame");
        GTLanguageManager.addStringLocalization(this.getUnlocalizedName() + ".13.name", "Cutting Factory Frame");

        GTLanguageManager.addStringLocalization(this.getUnlocalizedName() + ".15.name", "Sterile Farm Casing");

        GregtechItemList.Casing_ThermalCentrifuge.set(new ItemStack(this, 1, 0));
        GregtechItemList.Casing_Refinery_External.set(new ItemStack(this, 1, 1));
        GregtechItemList.Casing_Refinery_Structural.set(new ItemStack(this, 1, 2));
        GregtechItemList.Casing_Refinery_Internal.set(new ItemStack(this, 1, 3));
        GregtechItemList.Casing_WashPlant.set(new ItemStack(this, 1, 4));
        GregtechItemList.Casing_Sifter.set(new ItemStack(this, 1, 5));
        GregtechItemList.Casing_SifterGrate.set(new ItemStack(this, 1, 6));
        GregtechItemList.Casing_Vanadium_Redox.set(new ItemStack(this, 1, 7));
        GregtechItemList.Casing_Power_SubStation.set(new ItemStack(this, 1, 8));
        GregtechItemList.Casing_Cyclotron_Coil.set(new ItemStack(this, 1, 9));
        GregtechItemList.Casing_Cyclotron_External.set(new ItemStack(this, 1, 10));
        GregtechItemList.Casing_ThermalContainment.set(new ItemStack(this, 1, 11));
        GregtechItemList.Casing_Autocrafter.set(new ItemStack(this, 1, 12));
        GregtechItemList.Casing_CuttingFactoryFrame.set(new ItemStack(this, 1, 13));

        GregtechItemList.Casing_PLACEHOLDER_TreeFarmer.set(new ItemStack(this, 1, 15)); // Tree Farmer Textures

        GTStructureChannels.PSS_CELL.registerAsIndicator(new ItemStack(this, 1, 7), 1);
    }

    @Override
    public IIcon getIcon(final int ordinalSide, final int aMeta) {
        return CasingTextureHandler2.getIcon(ordinalSide, aMeta);
    }
}
