package openmodularturrets.blocks.turretheads;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import openmodularturrets.handler.ConfigHandler;

import java.text.DecimalFormat;
import java.util.List;

import static com.github.technus.tectech.CommonValues.TEC_MARK_EM;

/**
 * Created by Bass on 28/07/2017.
 */
public class TurretHeadItemEM extends ItemBlock {
    private static final DecimalFormat df = new DecimalFormat("0.0");

    public TurretHeadItemEM(Block block) {
        super(block);
    }

    @Override
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List list, boolean p_77624_4_) {
        list.add(TEC_MARK_EM);
        list.add("");
        list.add(EnumChatFormatting.GOLD + "--" + StatCollector.translateToLocal("tooltip.info") + "--");
        list.add(StatCollector.translateToLocal("tooltip.tier") + ": " + EnumChatFormatting.WHITE + '5');
        list.add(StatCollector.translateToLocal("tooltip.range") + ": " + EnumChatFormatting.WHITE + ConfigHandler.getLaserTurretSettings().getRange());
        list.add(StatCollector.translateToLocal("tooltip.accuracy") + ": " + EnumChatFormatting.WHITE + StatCollector.translateToLocal("turret.accuracy.high"));
        list.add(StatCollector.translateToLocal("tooltip.ammo") + ": " + EnumChatFormatting.WHITE + StatCollector.translateToLocal("turret.ammo.4"));
        list.add(StatCollector.translateToLocal("tooltip.tierRequired") + ": " + EnumChatFormatting.WHITE + StatCollector.translateToLocal("base.tier.5"));
        list.add("");
        list.add(EnumChatFormatting.DARK_PURPLE + "--" + StatCollector.translateToLocal("tooltip.damage.label") + "--");
        list.add(StatCollector.translateToLocal("tooltip.damage.stat") + ": " + EnumChatFormatting.WHITE + (float)ConfigHandler.getLaserTurretSettings().getDamage() / 2.0F + ' ' + StatCollector.translateToLocal("tooltip.health"));
        list.add(StatCollector.translateToLocal("tooltip.aoe") + ": " + EnumChatFormatting.WHITE + '0');
        list.add(StatCollector.translateToLocal("tooltip.firerate") + ": " + EnumChatFormatting.WHITE + df.format((double)(20.0F / (float)ConfigHandler.getLaserTurretSettings().getFireRate())));
        list.add(StatCollector.translateToLocal("tooltip.energy.stat") + ": " + EnumChatFormatting.WHITE + ConfigHandler.getLaserTurretSettings().getPowerUsage() + " RF");
        list.add("");
        list.add(EnumChatFormatting.DARK_GRAY + StatCollector.translateToLocal("flavour.turret.4"));
    }
}
