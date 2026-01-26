package gregtech.common.tileentities.machines.multi.nanochip.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.item.ItemStack;

import gregtech.api.objects.GTItemStack;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;

public class CCNEIRepresentation {

    public static final Object2ObjectMap<ItemStack, List<ItemStack>> NEI_RECIPE_ASSOCIATIONS = new Object2ObjectOpenCustomHashMap<>(
        GTItemStack.ITEMSTACK_HASH_STRATEGY2);
    public static final Object2ObjectMap<ItemStack, List<ItemStack>> NEI_USAGE_ASSOCIATIONS = new Object2ObjectOpenCustomHashMap<>(
        GTItemStack.ITEMSTACK_HASH_STRATEGY2);

    public static void init() {
        for (CircuitComponent cc : CircuitComponent.VALUES) {
            // Circuits
            if (cc.circuitTier != 0) {
                ItemStack ccItem = cc.getFakeStack(1);
                ItemStack realItem = cc.realComponent.get();

                // Pressing U or R on a real circuit shows CC recipes and usages too
                addRecipeAssociation(realItem, ccItem);
                addUsageAssociation(realItem, ccItem);

                continue;
            }

            // Circuit components
            // Pressing U or R on a real item shows both CC and PC recipes and usages
            if (cc.isProcessed) {
                ItemStack pcItem = cc.getFakeStack(1);
                Supplier<CircuitComponent> ccRepresentation = cc.componentForProcessed;
                if (ccRepresentation == null) continue; // No CC representation for this PC, don't do anything special
                ItemStack realItem = ccRepresentation.get().realComponent.get();

                addRecipeAssociation(realItem, pcItem);
                addUsageAssociation(realItem, pcItem);
            } else {
                ItemStack ccItem = cc.getFakeStack(1);
                ItemStack realItem = cc.realComponent.get();

                addRecipeAssociation(realItem, ccItem);
                addUsageAssociation(realItem, ccItem);
            }
        }
    }

    private static void addRecipeAssociation(ItemStack original, ItemStack association) {
        List<ItemStack> list = NEI_RECIPE_ASSOCIATIONS.get(original);
        if (list == null) list = new ArrayList<>();
        list.add(association);
        NEI_RECIPE_ASSOCIATIONS.put(original, list);
    }

    private static void addUsageAssociation(ItemStack original, ItemStack association) {
        List<ItemStack> list = NEI_USAGE_ASSOCIATIONS.get(original);
        if (list == null) list = new ArrayList<>();
        list.add(association);
        NEI_USAGE_ASSOCIATIONS.put(original, list);
    }
}
