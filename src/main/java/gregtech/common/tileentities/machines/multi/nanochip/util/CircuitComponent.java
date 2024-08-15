package gregtech.common.tileentities.machines.multi.nanochip.util;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.items.GT_CircuitComponent_FakeItem;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public enum CircuitComponent {

    // TODO: Consider if this whole fake stack system is overcomplicated and if we can't just use the real stacks
    // The main drawback of this is that either we have to override a LOT of things in the NEI display, or simply accept
    // that
    // the circuit components will be displayed with the same textures and names as the real stacks backing them.
    // This also wouldn't work well with components that don't have a 'real' backing ItemStack, such as components
    // that are only created through a module in the NAC

    // When adding to this list, PLEASE only add to the end! The ordinals are used as item ids for the fake items!
    WireNiobiumTitanium("gt.circuitcomponent.wire",
        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.NiobiumTitanium, 1), Materials.NiobiumTitanium),
    SMDTransistor("gt.circuitcomponent.smd.transistor", ItemList.Circuit_Parts_TransistorSMD.get(1)),
    SMDInductor("gt.circuitcomponent.smd.inductor", ItemList.Circuit_Parts_InductorSMD.get(1)),
    SMDCapacitor("gt.circuitcomponent.smd.capacitor", ItemList.Circuit_Parts_CapacitorSMD.get(1)),
    SMDDiode("gt.circuitcomponent.smd.diode", ItemList.Circuit_Parts_DiodeSMD.get(1)),
    AdvSMDTransistor("gt.circuitcomponent.asmd.transistor", ItemList.Circuit_Parts_TransistorASMD.get(1)),
    AdvSMDInductor("gt.circuitcomponent.asmd.inductor", ItemList.Circuit_Parts_InductorASMD.get(1)),
    AdvSMDCapacitor("gt.circuitcomponent.asmd.capacitor", ItemList.Circuit_Parts_CapacitorASMD.get(1)),
    AdvSMDDiode("gt.circuitcomponent.asmd.diode", ItemList.Circuit_Parts_DiodeASMD.get(1)),
    OpticalSMDTransistor("gt.circuitcomponent.xsmd.transistor", ItemList.Circuit_Parts_TransistorXSMD.get(1)),
    OpticalSMDInductor("gt.circuitcomponent.xsmd.inductor", ItemList.Circuit_Parts_InductorXSMD.get(1)),
    OpticalSMDCapacitor("gt.circuitcomponent.xsmd.capacitor", ItemList.Circuit_Parts_CapacitorXSMD.get(1)),
    OpticalSMDDiode("gt.circuitcomponent.xsmd.diode", ItemList.Circuit_Parts_DiodeXSMD.get(1)),;

    public final String unlocalizedName;
    public final Materials material;
    public final ItemStack realStack;

    private static final Map<GT_Utility.ItemId, CircuitComponent> stackTranslationMap = new HashMap<>();

    CircuitComponent(String unlocalizedName, ItemStack realStack) {
        this(unlocalizedName, realStack, null);
    }

    CircuitComponent(String unlocalizedName, ItemStack realStack, Materials material) {
        this.unlocalizedName = unlocalizedName;
        // Hide the fake stack in NEI
        codechicken.nei.api.API.hideItem(getFakeStack(1));
        this.material = material;
        this.realStack = realStack;
    }

    public String getLocalizedName() {
        return StatCollector.translateToLocal(unlocalizedName);
    }

    // ItemStack of a fake item, only for display and recipe checking purposes
    public ItemStack getFakeStack(int amount) {
        return new ItemStack(GT_CircuitComponent_FakeItem.INSTANCE, amount, this.ordinal());
    }

    // ItemStack of the real item used to create this component
    public ItemStack getRealStack() {
        return realStack;
    }

    public static CircuitComponent getFromFakeStack(ItemStack stack) {
        // If this throws an IndexOutOfBounds exception, there is a bug
        return CircuitComponent.values()[stack.getItemDamage()];
    }

    public static CircuitComponent getFromRealStack(ItemStack stack) {
        return stackTranslationMap.get(GT_Utility.ItemId.createNoCopy(stack));
    }

    static {
        // Fill out the translation map. We can't do this inside the constructor because it is illegal
        for (CircuitComponent component : CircuitComponent.values()) {
            GT_Utility.ItemId id = GT_Utility.ItemId.createNoCopy(component.getRealStack());
            stackTranslationMap.put(id, component);
        }
    }
}
