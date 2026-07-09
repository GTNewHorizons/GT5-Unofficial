package gregtech.nei;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import gregtech.api.enums.CondensateType;
import gregtech.api.objects.GTItemStack;
import gregtech.api.util.GTUtility;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitCalibration;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;

/// Registry for NEI item associations. When a key item is looked up in NEI (via R or U), the associated items are
/// also included in the search, causing their recipes or usages to appear alongside the key's own.
public class GTNEIAssociations {

    /// Associations for recipe lookup (R key). Value items are included as additional recipe outputs to search for.
    public static final Object2ObjectMap<ItemStack, List<ItemStack>> NEI_RECIPE_ASSOCIATIONS = new Object2ObjectOpenCustomHashMap<>(
        GTItemStack.ITEMSTACK_HASH_STRATEGY2);

    /// Associations for usage lookup (U key). Value items are included as additional recipe inputs to search for.
    public static final Object2ObjectMap<ItemStack, List<ItemStack>> NEI_USAGE_ASSOCIATIONS = new Object2ObjectOpenCustomHashMap<>(
        GTItemStack.ITEMSTACK_HASH_STRATEGY2);

    public static void initCircuitComponents() {
        for (CircuitComponent cc : CircuitComponent.VALUES) {
            // Circuits
            if (cc.circuitType != CircuitCalibration.NONE) {
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

    /// Pressing R on a condensate fluid also shows recipes producing its source fluid.
    /// Pressing U on a source fluid also shows recipes consuming its condensate variant.
    /// Pressing U on a condensate cell also shows recipes consuming its condensate fluid.
    public static void initCondensates() {
        for (CondensateType type : CondensateType.values()) {
            FluidStack condensate = type.getEntangled(type.getUnit());
            FluidStack source = type.getSourceFluid();

            ItemStack condensateDisplay = GTUtility.getFluidDisplayStack(condensate, FluidDisplayStackMode.HIDDEN);
            ItemStack sourceDisplay = GTUtility.getFluidDisplayStack(source, FluidDisplayStackMode.HIDDEN);

            addRecipeAssociation(condensateDisplay, sourceDisplay);
            addUsageAssociation(sourceDisplay, condensateDisplay);
            addUsageAssociation(type.getEntangledCellStack(), condensateDisplay);
        }
    }

    /// Adds a recipe association: when pressing R on {@code original}, recipes producing {@code association} are also
    /// shown.
    public static void addRecipeAssociation(@NotNull ItemStack original, @NotNull ItemStack association) {
        NEI_RECIPE_ASSOCIATIONS.computeIfAbsent(original, k -> new ArrayList<>())
            .add(association);
    }

    /// Adds a usage association: when pressing U on {@code original}, recipes using {@code association} are also shown.
    public static void addUsageAssociation(@NotNull ItemStack original, @NotNull ItemStack association) {
        NEI_USAGE_ASSOCIATIONS.computeIfAbsent(original, k -> new ArrayList<>())
            .add(association);
    }
}
