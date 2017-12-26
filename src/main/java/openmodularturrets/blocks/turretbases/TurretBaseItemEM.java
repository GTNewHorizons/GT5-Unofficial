package openmodularturrets.blocks.turretbases;

import com.github.technus.tectech.CommonValues;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.util.List;

/**
 * Created by Bass on 28/07/2017.
 */
public class TurretBaseItemEM extends ItemBlock {
    public TurretBaseItemEM(Block b) {
        super(b);
    }

    @Override
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List list, boolean p_77624_4_) {
        list.add(CommonValues.TEC_MARK_EM);
        list.add("");
        list.add(EnumChatFormatting.AQUA + "--" + StatCollector.translateToLocal("tooptip.energy.label") + "--");
        list.add(StatCollector.translateToLocal("tooltip.rf.max") + ": " + EnumChatFormatting.WHITE + 1000000000);
        list.add(StatCollector.translateToLocal("tooltip.rf.io") + ": " + EnumChatFormatting.WHITE + 50000);
        list.add("");
        list.add(EnumChatFormatting.GREEN + "--" + StatCollector.translateToLocal("tooltip.extras.label") + "--");
        list.add(StatCollector.translateToLocal("tooltip.extras.addons.0"));
        list.add("");
        list.add(EnumChatFormatting.DARK_GRAY + StatCollector.translateToLocal("flavour.base.0"));
    }
}
