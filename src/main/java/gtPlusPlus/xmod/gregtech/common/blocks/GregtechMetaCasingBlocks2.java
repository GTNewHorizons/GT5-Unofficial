package gtPlusPlus.xmod.gregtech.common.blocks;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.render.TextureFactory;
import gregtech.common.blocks.MaterialCasings;
import gregtech.common.misc.GTStructureChannels;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.storage.MTEPowerSubStation;

public class GregtechMetaCasingBlocks2 extends GregtechMetaCasingBlocksAbstract {

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
        list.add(new ItemStack(item, 1, 2));
        list.add(new ItemStack(item, 1, 3));
        list.add(new ItemStack(item, 1, 4));
        list.add(new ItemStack(item, 1, 5));
        list.add(new ItemStack(item, 1, 6));
        list.add(new ItemStack(item, 1, 7));
        list.add(new ItemStack(item, 1, 8));
        list.add(new ItemStack(item, 1, 9));
        list.add(new ItemStack(item, 1, 10));
        list.add(new ItemStack(item, 1, 11));
        list.add(new ItemStack(item, 1, 12));
        list.add(new ItemStack(item, 1, 13));
        list.add(new ItemStack(item, 1, 15));
    }

    public static class GregtechMetaCasingItemBlocks2 extends GregtechMetaCasingItems {

        public GregtechMetaCasingItemBlocks2(Block par1) {
            super(par1);
        }

        @Override
        public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean aF3_H) {
            int meta = stack.getItemDamage();
            int tier = MTEPowerSubStation.getCellTier(field_150939_a, meta);
            if (meta == 7 && tier > 0) {
                long capacity = MTEPowerSubStation.getCapacityFromCellTier(tier);
                tooltip.add(
                    StatCollector
                        .translateToLocalFormatted("gtpp.tooltip.meta_casing.energy_storage", formatNumber(capacity)));
            }
            super.addInformation(stack, player, tooltip, aF3_H);
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
        TAE.registerTexture(3, 2, TextureFactory.of(this, 4));

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
        if ((aMeta >= 0) && (aMeta < 16)) {
            return switch (aMeta) {
                case 0 -> TexturesGtBlock.Casing_Material_RedSteel.getIcon();
                case 1 -> TexturesGtBlock.Casing_Material_HastelloyX.getIcon();
                case 2 -> TexturesGtBlock.Casing_Material_HastelloyN.getIcon();
                case 3 -> TexturesGtBlock.Casing_Material_Fluid_IncoloyDS.getIcon();
                case 4 -> ordinalSide < 2 ? TexturesGtBlock.Casing_Material_Grisium_Top.getIcon()
                    : TexturesGtBlock.Casing_Material_Grisium.getIcon();
                case 5 -> Textures.BlockIcons.INDUSTRIAL_SIEVE_CASING.getIcon();
                case 6 -> Textures.BlockIcons.LARGE_SIEVE_GRATE.getIcon();
                case 7 -> Textures.BlockIcons.CASING_REDOX_EV.getIcon();
                case 8 -> Textures.BlockIcons.SUB_STATION_EXTERNAL_CASING.getIcon();
                case 9 -> Textures.BlockIcons.CYCLOTRON_COIL.getIcon();
                case 10 -> Textures.BlockIcons.MACHINE_CASING_RADIATIONPROOF.getIcon();
                case 11 -> TexturesGtBlock.Casing_Material_Tantalloy61.getIcon();
                case 12 -> TexturesGtBlock.Casing_Machine_Simple_Top.getIcon();
                case 13 -> ordinalSide < 2 ? Textures.BlockIcons.CUTTING_FACTORY_FRAME_TOP.getIcon()
                    : Textures.BlockIcons.CUTTING_FACTORY_FRAME_SIDE.getIcon();
                case 14 -> Textures.GlobalIcons.RENDERING_ERROR.getIcon();
                case 15 -> TexturesGtBlock.Sterile_Casing.getIcon();
                default -> TexturesGtBlock.Overlay_UU_Matter.getIcon();
            };
        }
        return Textures.GlobalIcons.RENDERING_ERROR.getIcon();
    }
}
