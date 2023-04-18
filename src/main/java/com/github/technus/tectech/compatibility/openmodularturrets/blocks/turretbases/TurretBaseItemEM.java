package com.github.technus.tectech.compatibility.openmodularturrets.blocks.turretbases;

import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.github.technus.tectech.util.CommonValues;

/**
 * Created by Tec on 28/07/2017.
 */
public class TurretBaseItemEM extends ItemBlock {

    public TurretBaseItemEM(Block block) {
        super(block);
    }

    @Override
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List<String> list, boolean p_77624_4_) {
        list.add(CommonValues.TEC_MARK_EM);
        list.add("");
        list.add(EnumChatFormatting.AQUA + "--" + translateToLocal("tooptip.energy.label") + "--");
        list.add(translateToLocal("tooltip.rf.max") + ": " + EnumChatFormatting.WHITE + 1000000000);
        list.add(translateToLocal("tooltip.rf.io") + ": " + EnumChatFormatting.WHITE + 50000);
        list.add("");
        list.add(EnumChatFormatting.GREEN + "--" + translateToLocal("tooltip.extras.label") + "--");
        list.add(translateToLocal("tooltip.extras.addons.2"));
        list.add(translateToLocal("tooltip.extras.upgrade.2"));
        list.add("");
        list.add(EnumChatFormatting.DARK_GRAY + translateToLocal("flavour.base.0"));
    }
}
