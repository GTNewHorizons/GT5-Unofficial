package gtPlusPlus.core.item.base.itemblock;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import gtPlusPlus.api.interfaces.ITileTooltip;

public class ItemBlockBasicTile extends ItemBlock {

    private final int mID;

    public ItemBlockBasicTile(final Block block) {
        super(block);
        this.mID = ((ITileTooltip) block).getTooltipID();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
        if (this.mID == 1) { // Fish trap
            list.add(StatCollector.translateToLocal("GTPP.tooltip.fish_trap.0"));
            list.add(StatCollector.translateToLocal("GTPP.tooltip.fish_trap.1"));
            list.add(StatCollector.translateToLocal("GTPP.tooltip.fish_trap.2"));
            list.add(StatCollector.translateToLocal("GTPP.tooltip.fish_trap.3"));
        } else if (this.mID == 2) { // Circuit Table
            list.add(StatCollector.translateToLocal("GTPP.tooltip.circuit_table.0"));
            list.add(StatCollector.translateToLocal("GTPP.tooltip.circuit_table.1"));
            list.add(StatCollector.translateToLocal("GTPP.tooltip.circuit_table.2"));
        } else if (this.mID == 3) { // Decayables Chest
            list.add(StatCollector.translateToLocal("GTPP.tooltip.decayables_chest.0"));
            list.add(StatCollector.translateToLocal("GTPP.tooltip.decayables_chest.1"));
            list.add(StatCollector.translateToLocal("GTPP.tooltip.decayables_chest.2"));
        } else if (this.mID == 5) { // Volumetric Flask Setter
            list.add(StatCollector.translateToLocal("GTPP.tooltip.volumetric_flask_setter.0"));
            list.add(StatCollector.translateToLocal("GTPP.tooltip.volumetric_flask_setter.1"));
        } else {
            list.add(StatCollector.translateToLocalFormatted("GTPP.tooltip.bad_tooltip_id", mID));
        }
        super.addInformation(stack, aPlayer, list, bool);
    }
}
