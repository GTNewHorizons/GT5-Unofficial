package gtPlusPlus.xmod.ic2.item;

import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.CoreItem;
import gtPlusPlus.core.item.wearable.hazmat.ItemArmorHazmatEx;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class IC2Items {

    public static void register() {

        // Rotor Blades
        GregtechItemList.EnergeticAlloyRotorBlade.set(
            new CoreItem(
                "itemEnergeticRotorBlade",
                AddToCreativeTab.tabMachines,
                16,
                "A part for an advanced Kinetic Rotor"));
        GregtechItemList.TungstenSteelRotorBlade.set(
            new CoreItem(
                "itemTungstenSteelRotorBlade",
                AddToCreativeTab.tabMachines,
                16,
                "A part for an advanced Kinetic Rotor"));
        GregtechItemList.VibrantAlloyRotorBlade.set(
            new CoreItem(
                "itemVibrantRotorBlade",
                AddToCreativeTab.tabMachines,
                16,
                "A part for an advanced Kinetic Rotor"));
        GregtechItemList.IridiumRotorBlade.set(
            new CoreItem(
                "itemIridiumRotorBlade",
                AddToCreativeTab.tabMachines,
                16,
                "A part for an advanced Kinetic Rotor"));

        // Rotor Shafts
        GregtechItemList.EnergeticAlloyShaft.set(
            new CoreItem(
                "itemEnergeticShaft",
                AddToCreativeTab.tabMachines,
                16,
                "A part for an advanced Kinetic Rotor"));
        GregtechItemList.TungstenSteelShaft.set(
            new CoreItem(
                "itemTungstenSteelShaft",
                AddToCreativeTab.tabMachines,
                16,
                "A part for an advanced Kinetic Rotor"));
        GregtechItemList.VibrantAlloyShaft.set(
            new CoreItem("itemVibrantShaft", AddToCreativeTab.tabMachines, 16, "A part for an advanced Kinetic Rotor"));
        GregtechItemList.IridiumShaft.set(
            new CoreItem("itemIridiumShaft", AddToCreativeTab.tabMachines, 16, "A part for an advanced Kinetic Rotor"));

        // Rotors
        GregtechItemList.EnergeticAlloyRotor.set(new CustomKineticRotor(0));
        GregtechItemList.TungstenSteelRotor.set(new CustomKineticRotor(1));
        GregtechItemList.VibrantAlloyRotor.set(new CustomKineticRotor(2));
        GregtechItemList.IridiumRotor.set(new CustomKineticRotor(3));

        ItemArmorHazmatEx.init();
    }
}
