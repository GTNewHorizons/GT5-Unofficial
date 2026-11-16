package gregtech.loaders.oreprocessing;

import static gregtech.api.recipe.RecipeMaps.vacuumFreezerRecipes;

import java.math.RoundingMode;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.google.common.math.LongMath;
import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.CellShapes;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTUtility;

public final class ProcessingPlasma {

    private ProcessingPlasma() {}

    public static void generate(Shape shape, Material material) {
        if (MaterialUtils.hasFlag(material, GTMaterialFlag.NO_RECIPES)) return;

        int amount = material.getProperty(GTMaterialProperties.PLASMA_COOLING_AMOUNT);
        int defaultDuration = Math.toIntExact(Math.max(MaterialUtils.mass(material) * 2L, 1L));
        Integer configuredDuration = material.getProperty(GTMaterialProperties.PLASMA_COOLING_DURATION);
        int duration = configuredDuration == null ? defaultDuration : configuredDuration;
        int voltage = material.getProperty(GTMaterialProperties.PLASMA_COOLING_VOLTAGE);
        if (amount <= 0 || duration <= 0 || voltage <= 0) {
            throw new IllegalArgumentException("Invalid plasma cooling properties for " + material.getName());
        }

        FluidStack cooled = MaterialUtils.anyFluid(material, amount);
        if (cooled != null) {
            GTValues.RA.stdBuilder()
                .fluidInputs(MaterialLibAPI.getFluidStack(material, shape, amount))
                .fluidOutputs(cooled)
                .duration(duration)
                .eut(voltage)
                .addTo(vacuumFreezerRecipes);
        }

        for (Shape cellShape : new Shape[] { CellShapes.cellPlasma, CellShapes.cellPlasmaLight }) {
            ItemStack plasmaCell = MaterialLibAPI.findStack(material, cellShape, 1);
            if (plasmaCell == null) continue;
            ItemStack solidCell = MaterialLibAPI.findStack(material, CellShapes.cellSolid, 1);
            if (solidCell != null) {
                GTValues.RA.stdBuilder()
                    .itemInputs(plasmaCell)
                    .itemOutputs(solidCell)
                    .duration(defaultDuration)
                    .eut(TierEU.RECIPE_MV)
                    .addTo(vacuumFreezerRecipes);
            } else if (cooled != null) {
                coolFluidCell(material, plasmaCell, cooled, duration, voltage);
            }
        }
    }

    private static void coolFluidCell(Material material, ItemStack plasmaCell, FluidStack cooled, int duration,
        int voltage) {
        FluidStack plasma = GTUtility.getFluidForFilledItem(plasmaCell, true);
        if (plasma == null) return;

        ItemStack outputCell = ItemList.Cell_Empty.get(1);
        int remainder = plasma.amount;
        for (Shape cellShape : new Shape[] { CellShapes.cell, CellShapes.cellGas, CellShapes.cellMolten }) {
            ItemStack cell = MaterialLibAPI.findStack(material, cellShape, 1);
            FluidStack contents = GTUtility.getFluidForFilledItem(cell, true);
            if (contents != null && contents.isFluidEqual(cooled) && contents.amount <= plasma.amount) {
                outputCell = cell;
                remainder -= contents.amount;
                break;
            }
        }

        GTRecipeBuilder builder = GTValues.RA.stdBuilder()
            .itemInputs(plasmaCell)
            .itemOutputs(outputCell)
            .duration(
                Math.toIntExact(LongMath.divide((long) duration * plasma.amount, cooled.amount, RoundingMode.CEILING)))
            .eut(voltage);
        if (remainder > 0) {
            FluidStack excess = cooled.copy();
            excess.amount = remainder;
            builder.fluidOutputs(excess);
        }
        // The fluid recipe is registered above, so bypass the cell-to-fluid recipe emitter
        builder.build()
            .ifPresent(vacuumFreezerRecipes::add);
    }
}
