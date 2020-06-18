package common.reactorItem;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class AbstractReactorItem extends Item {

    private final int[] behaviourID;

    protected AbstractReactorItem(int...behaviourID) {
        this.behaviourID = behaviourID;
    }

    @Override
    public abstract double getDurabilityForDisplay(ItemStack stack);

    @Override
    public abstract boolean showDurabilityBar(ItemStack stack);

    @Override
    public final String getUnlocalizedName(ItemStack stack) {
        return super.hasSubtypes ? (super.getUnlocalizedName() + "." + stack.getItemDamage())
                : super.getUnlocalizedName();
    }

    public final int getBehaviourID(int meta) {
        return behaviourID[meta];
    }
}
