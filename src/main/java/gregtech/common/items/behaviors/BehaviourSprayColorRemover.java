package gregtech.common.items.behaviors;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import gregtech.api.util.ColoredBlockContainer;

public class BehaviourSprayColorRemover extends BehaviourSprayColor {

    public BehaviourSprayColorRemover(ItemStack aEmpty, ItemStack aUsed, ItemStack aFull, long aUses) {
        super(aEmpty, aUsed, aFull, aUses);
        this.tooltip = () -> StatCollector.translateToLocal("gt.behaviour.paintspray.solvent.tooltip");
    }

    @Override
    protected boolean colorize(ColoredBlockContainer container) {
        return container.removeColor();
    }

    @Override
    public int getDye(final ItemStack itemStack) {
        return -1;
    }
}
