package gregtech.common.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.HeatingCoilLevel;

/**
 * The casings are split into separate files because they are registered as regular blocks, and a regular block can have
 * 16 subtypes at most.
 */
public class ItemCasings5 extends ItemCasingsAbstract {

    public ItemCasings5(Block block) {
        super(block);
    }

    protected static final String mCoilHeatTooltip = StatCollector.translateToLocal("gt.coilheattooltip");
    protected static final String mCoilUnitTooltip = StatCollector.translateToLocal("gt.coilunittooltip");

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List<String> aList, boolean aF3_H) {
        super.addInformation(aStack, aPlayer, aList, aF3_H);
        int metadata = aStack.getItemDamage();
        HeatingCoilLevel coilLevel = BlockCasings5.getCoilHeatFromDamage(metadata);
        aList.add(mCoilHeatTooltip + coilLevel.getHeat() + mCoilUnitTooltip);
        aList.add(StatCollector.translateToLocalFormatted("GT5U.tooltip.channelvalue", metadata + 1, "coil"));
    }
}
