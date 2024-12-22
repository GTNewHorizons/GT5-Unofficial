package gregtech.api.items;

import net.minecraft.item.ItemStack;

import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;

public class CircuitComponentFakeItem extends GTGenericItem {

    public static CircuitComponentFakeItem INSTANCE = null;

    public CircuitComponentFakeItem() {
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

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        CircuitComponent component = CircuitComponent.getFromFakeStack(stack);
        return component.getLocalizedName();
    }
}
