package gregtech.common.ec.multiblock;

import static gregtech.api.util.GTUtility.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.component.IRecipeCheckHandlerComponent;
import gregtech.api.ec.component.multiblock.IDualInputLogicComponent;
import gregtech.api.ec.component.multiblock.IHatchConfigCopyableComponent;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.common.ec.multiblock.internal.BaseHatchContainerComponent;
import gregtech.common.tileentities.machines.IDualInputHatch;
import gregtech.common.tileentities.machines.IDualInputInventory;
import gregtech.common.tileentities.machines.IDualInputInventoryWithPattern;
import gregtech.common.tileentities.machines.MTEHatchCraftingInputSlave;

public class DualInputLogicComponent extends BaseHatchContainerComponent
    implements IDualInputLogicComponent, IRecipeCheckHandlerComponent, IHatchConfigCopyableComponent {

    private final List<IDualInputHatch> mDualInputHatches = new ArrayList<>();

    @Override
    public @NotNull List<IDualInputHatch> getDualInputHatches() {
        return mDualInputHatches;
    }

    @Override
    public void appendAllStoredItems(@NotNull List<ItemStack> list) {
        for (IDualInputHatch dualInputHatch : getValidDualInputHatches()) {
            Collections.addAll(list, dualInputHatch.getAllItems());
        }
    }

    @Override
    public void appendAllStoredFluids(@NotNull List<FluidStack> list) {
        for (IDualInputHatch dualInputHatch : getValidDualInputHatches()) {
            Collections.addAll(list, dualInputHatch.getAllFluids());
        }
    }

    @Override
    public boolean tryAttachDualItemInput(@NotNull IMetaTileEntity metaTile, int baseCasingIndex) {
        if (metaTile instanceof IDualInputHatch hatch) {
            if (!mDualInputHatches.contains(hatch)) {
                onHatchAttached(hatch, baseCasingIndex);
                mDualInputHatches.add(hatch);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean tryAttachDualFluidInput(@NotNull IMetaTileEntity metaTile, int baseCasingIndex) {
        if (metaTile instanceof IDualInputHatch hatch
            && (hatch.supportsFluids() || metaTile instanceof MTEHatchCraftingInputSlave)) {
            if (!mDualInputHatches.contains(hatch)) {
                onHatchAttached(hatch, baseCasingIndex);
                mDualInputHatches.add(hatch);
            }
            return true;
        }
        return false;
    }

    @Override
    public void detachAllHatches() {
        clearCraftingIcons(mDualInputHatches);
        mDualInputHatches.clear();
    }

    @Override
    public void explodeAllHatches(long power) {}

    private static boolean isColorAbsent(short hatchColors, byte color) {
        return (hatchColors & (1 << color)) == 0;
    }

    @Override
    public @NotNull CheckRecipeResult tryFindRecipe(@NotNull ProcessingLogic logic,
        @NotNull CheckRecipeResult lastResult) {
        CheckRecipeResult result = lastResult;
        List<IDualInputHatch> validHatches = uncheckFilterValidMTEs(mDualInputHatches);
        if (validHatches.isEmpty()) return result;

        short craftingHatchColors = 0;
        for (var hatch : validHatches) {
            craftingHatchColors |= (short) (1 << ((MetaTileEntity) hatch).getColor());
        }
        boolean doCraftingColorChecking = craftingHatchColors != 0;
        for (byte color = -1; color < (doCraftingColorChecking ? 16 : 0); color++) {
            if (color != -1 && isColorAbsent(craftingHatchColors, color)) continue;
            for (IDualInputHatch dualInputHatch : validHatches) {
                byte hatchColor = ((MetaTileEntity) dualInputHatch).getColor();
                if (hatchColor != color) continue;
                ItemStack[] sharedItems = dualInputHatch.getSharedItems();
                for (var it = dualInputHatch.inventories(); it.hasNext();) {
                    IDualInputInventory slot = it.next();

                    if (!slot.isEmpty()) {
                        // try to cache the possible recipes from pattern
                        if (slot instanceof IDualInputInventoryWithPattern withPattern) {
                            if (!logic.tryCachePossibleRecipesFromPattern(withPattern)) {
                                // move on to next slots if it returns false, which means there is no possible recipes
                                // with
                                // given pattern.
                                continue;
                            }
                        }

                        logic.setInputItems(ArrayUtils.addAll(sharedItems, slot.getItemInputs()));
                        logic.setInputFluids(slot.getFluidInputs());

                        CheckRecipeResult foundResult = logic.process();
                        if (foundResult.wasSuccessful()) {
                            return foundResult;
                        }
                        if (foundResult != CheckRecipeResultRegistry.NO_RECIPE) {
                            // Recipe failed in interesting way, so remember that and continue searching
                            result = foundResult;
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public int getRecipeCheckPriority() {
        return RecipeCheckPriority.DUAL_INPUT.priority;
    }

    @Override
    public @NotNull Map<String, @NotNull Collection<? extends MTEHatch>> getCopyableHatches() {
        List<MTEHatch> dualInputHatches = new ArrayList<>();
        for (IDualInputHatch dualInputHatch : mDualInputHatches) {
            if (dualInputHatch instanceof MTEHatch hatch) {
                dualInputHatches.add(hatch);
            }
        }
        return Collections.singletonMap("mDualInputHatches", dualInputHatches);
    }
}
