package toxiceverglades.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import gtPlusPlus.api.interfaces.ITileTooltip;

public class ItemBlockToxicEverglades extends ItemBlock {

    protected final int mID;

    public ItemBlockToxicEverglades(final Block block) {
        super(block);
        this.mID = ((ITileTooltip) block).getTooltipID();
    }

    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List<String> list,
        final boolean bool) {
        if (this.mID == 0) { // blockDarkWorldPortalFrame
            list.add(StatCollector.translateToLocal("gtpp.tooltip.dark_world_portal.frame.0"));
            list.add(StatCollector.translateToLocal("gtpp.tooltip.dark_world_portal.frame.1"));
        } else if (this.mID == 1) { // blockDarkWorldPortal
            list.add(StatCollector.translateToLocal("gtpp.tooltip.dark_world_portal.0"));
        } else if (this.mID == 2) { // blockDarkWorldGround
            list.add(StatCollector.translateToLocal("gtpp.tooltip.dark_world.ground"));
        } else if (this.mID == 3) { // blockDarkWorldPollutedDirt
            list.add(StatCollector.translateToLocal("gtpp.tooltip.dark_world.polluted_dirt"));
            if (stack.getItemDamage() > 0) {
                list.add(StatCollector.translateToLocal("gtpp.tooltip.dark_world.polluted_dirt.1"));
            }
        }
    }
}
