package gregtech.common.tileentities.machines.multi.nanochip.util;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import gregtech.api.items.GT_CircuitComponent_FakeItem;

public enum CircuitComponent {

    // When adding to this list, PLEASE only add to the end! The ordinals are used as item ids for the fake items!
    Wire("gt.circuitcomponent.wire"),
    SMDTransistor("gt.circuitcomponent.smd.transistor"),
    SMDInductor("gt.circuitcomponent.smd.inductor"),
    SMDCapacitor("gt.circuitcomponent.smd.capacitor"),
    SMDDiode("gt.circuitcomponent.smd.diode");

    public final String unlocalizedName;
    // ItemStack of a fake item representing this component
    public final ItemStack stack;

    CircuitComponent(String unlocalizedName) {
        this.unlocalizedName = unlocalizedName;
        // Create ItemStack that represents this component. We will use this for recipe checking.
        this.stack = new ItemStack(GT_CircuitComponent_FakeItem.INSTANCE, 1, this.ordinal());
        // Hide this stack in NEI
        codechicken.nei.api.API.hideItem(this.stack);
    }

    public String getLocalizedName() {
        return StatCollector.translateToLocal(unlocalizedName);
    }
}
