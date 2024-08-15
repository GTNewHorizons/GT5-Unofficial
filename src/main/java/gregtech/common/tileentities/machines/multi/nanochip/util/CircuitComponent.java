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

    CircuitComponent(String unlocalizedName) {
        this.unlocalizedName = unlocalizedName;
        // Hide this stack in NEI
        codechicken.nei.api.API.hideItem(getStackForm(1));
    }

    public String getLocalizedName() {
        return StatCollector.translateToLocal(unlocalizedName);
    }

    // ItemStack of a fake item, only for display and recipe checking purposes
    public ItemStack getStackForm(int amount) {
        return new ItemStack(GT_CircuitComponent_FakeItem.INSTANCE, amount, this.ordinal());
    }
}
