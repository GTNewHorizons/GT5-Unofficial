package gregtech.common.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.util.GT_LanguageManager;

public class GT_Item_Casings5 extends GT_Item_Casings_Abstract {

    public GT_Item_Casings5(Block block) {
        super(block);
    }

    protected static final String mCoilHeatTooltip = GT_LanguageManager
        .addStringLocalization("gt.coilheattooltip", "Base Heating Capacity = ");
    protected static final String mCoilUnitTooltip = GT_LanguageManager
        .addStringLocalization("gt.coilunittooltip", " Kelvin");

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List<String> aList, boolean aF3_H) {
        super.addInformation(aStack, aPlayer, aList, aF3_H);
        HeatingCoilLevel coilLevel = GT_Block_Casings5.getCoilHeatFromDamage(aStack.getItemDamage());
        aList.add(mCoilHeatTooltip + coilLevel.getHeat() + mCoilUnitTooltip);
    }
}
