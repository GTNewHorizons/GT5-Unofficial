package gtPlusPlus.xmod.sol;

import static gregtech.api.enums.Mods.SpiceOfLife;

import java.lang.reflect.Constructor;

import net.minecraft.item.Item;

import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.reflect.ReflectionUtils;

public class HANDLER_SpiceOfLife {

    public static final void preInit() {
        if (SpiceOfLife.isModLoaded()) {
            // Add a new Lunch Box with a reasonable amount of slots
            tryRegisterNewLunchBox("foodcrate", 12);
        }
    }

    private static boolean tryRegisterNewLunchBox(String aItemName, int aSlots) {
        Item aNewBox = getNewLunchBox(aItemName, aSlots);
        if (aNewBox != null) {
            GameRegistry.registerItem(aNewBox, aItemName);
            Logger.INFO("[Spice of Life] Registered " + aItemName + " as a new food container.");
            return true;
        }
        return false;
    }

    private static Item getNewLunchBox(String aItemName, int aSlots) {
        Class aItemFoodContainer = ReflectionUtils.getClass("squeek.spiceoflife.items.ItemFoodContainer");
        if (aItemFoodContainer != null) {
            Constructor aItemFoodContainerConstructor = ReflectionUtils
                    .getConstructor(aItemFoodContainer, new Class[] { String.class, int.class });
            if (aItemFoodContainerConstructor != null) {
                Object aNewObject = ReflectionUtils.createNewInstanceFromConstructor(
                        aItemFoodContainerConstructor,
                        new Object[] { aItemName, aSlots });
                if (aNewObject instanceof Item) {
                    Item aNewInstance = (Item) aNewObject;
                    return aNewInstance;
                }
            }
        }
        return null;
    }
}
