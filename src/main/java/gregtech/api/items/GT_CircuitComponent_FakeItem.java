package gregtech.api.items;

import net.minecraft.item.ItemStack;

import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;

public class GT_CircuitComponent_FakeItem extends GT_Generic_Item {

    public static GT_CircuitComponent_FakeItem INSTANCE = null;

    public GT_CircuitComponent_FakeItem() {
        super("gt.fakecircuitcomponent", "Fake Circuit Component Item", null);
        setMaxDamage(0);
        setHasSubtypes(true);
        INSTANCE = this;
    }

    @Override
    public String getUnlocalizedName(ItemStack aStack) {
        CircuitComponent component = CircuitComponent.getFromFakeStack(aStack);
        return component.unlocalizedName;
    }
}
