package gregtech.common.tileentities.machines.multi.nanochip.util;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.ItemList;
import gregtech.api.util.GT_Utility;

// Holds all conversions between raw circuit component items and their corresponding circuit components
public class ComponentConversionMap {

    private static GT_Utility.ItemId makeKey(ItemStack stack) {
        return GT_Utility.ItemId.createNoCopy(stack);
    }

    public static void put(ItemStack stack, CircuitComponent component) {
        GT_Utility.ItemId key = makeKey(stack);
        conversions.put(key, component);
    }

    public static final Map<GT_Utility.ItemId, CircuitComponent> conversions = new HashMap<>();

    public static void load() {
        // TODO: Figure out if we perhaps want to auto-generate this from the circuit assembler recipe map?
        // Either way this is not complete obviously
        put(ItemList.Circuit_Parts_TransistorSMD.get(1), CircuitComponent.SMDTransistor);
        put(ItemList.Circuit_Parts_InductorSMD.get(1), CircuitComponent.SMDInductor);
        put(ItemList.Circuit_Parts_CapacitorSMD.get(1), CircuitComponent.SMDCapacitor);
        put(ItemList.Circuit_Parts_DiodeSMD.get(1), CircuitComponent.SMDDiode);
    }

    public static CircuitComponent find(ItemStack stack) {
        return conversions.get(makeKey(stack));
    }
}
