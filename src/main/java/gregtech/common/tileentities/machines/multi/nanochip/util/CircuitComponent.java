package gregtech.common.tileentities.machines.multi.nanochip.util;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import gregtech.api.items.GT_CircuitComponent_FakeItem;

public enum CircuitComponent {

    Wire(0, "gt.circuitcomponent.wire"),
    SMDTransistor(1, "gt.circuitcomponent.smd.transistor"),
    SMDInductor(2, "gt.circuitcomponent.smd.inductor"),
    SMDCapacitor(3, "gt.circuitcomponent.smd.capacitor"),
    SMDDiode(4, "gt.circuitcomponent.smd.diode");

    public final int id;
    public final String unlocalizedName;
    // ItemStack of a fake item representing this component
    public final ItemStack stack;

    CircuitComponent(int id, String unlocalizedName) {
        this.id = id;
        this.unlocalizedName = unlocalizedName;
        this.stack = new ItemStack(new GT_CircuitComponent_FakeItem(), 1, id);
    }

    public String getLocalizedName() {
        return StatCollector.translateToLocal(unlocalizedName);
    }
}
