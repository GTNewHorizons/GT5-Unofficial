package kubatech.loaders;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.formingPressRecipes;
import static gregtech.api.recipe.RecipeMaps.neutroniumCompressorRecipes;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.COMPRESSION_TIER;

import java.util.Arrays;
import java.util.HashSet;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import kubatech.api.utils.ModUtils;
import kubatech.loaders.item.arcfurnace.ElectrodeItem;

public class ArcFurnaceLoader {

    public static final ElectrodeItem ARC_FURNACE_ELECTRODE = new ElectrodeItem();

    public static void load() {
        GameRegistry.registerItem(ARC_FURNACE_ELECTRODE, "arc_furnace_electrode");
        ArcFurnaceElectrode.registerElectrodes();
        if (ModUtils.isClientSided) GTMod.clientProxy().metaItemRenderer.registerItem(ARC_FURNACE_ELECTRODE);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_Bus_IV.get(1),
                MaterialLibAPI.getStack(Materials2Materials.Graphite, Materials2Shapes.dust, (int) (4)),
                MaterialLibAPI.getStack(Materials2Materials.Naquadah, Materials2Shapes.plate, (int) (4)),
                new Object[] { OrePrefixes.circuit.get(Materials.IV), 1L })
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SolderingAlloy, Materials2FluidShapes.fluidMolten, (int) (1152)))
            .itemOutputs(kubatech.api.enums.ItemList.ElectrodeHatch.get(1))
            .circuit(1)
            .eut(TierEU.RECIPE_IV)
            .duration(10 * SECONDS)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_IV.get(1),
                ItemList.Casing_SolidSteel.get(1),
                ItemList.Sensor_IV.get(2),
                new Object[] { OrePrefixes.circuit.get(Materials.IV), 4L })
            .itemOutputs(kubatech.api.enums.ItemList.ElectrodeDetectorHatch.get(1))
            .circuit(4)
            .eut(TierEU.RECIPE_IV)
            .duration(30 * SECONDS)
            .addTo(assemblerRecipes);

        final HashSet<ArcFurnaceElectrode> naniteElectrodes = new HashSet<>(
            Arrays.asList(
                ArcFurnaceElectrode.NeutroniumNaniteElectrode,
                ArcFurnaceElectrode.TranscendentNaniteElectrode,
                ArcFurnaceElectrode.UniversiumNaniteElectrode));

        for (ArcFurnaceElectrode electrode : ArcFurnaceElectrode.values()) {
            if (naniteElectrodes.contains(electrode)) continue;

            GTValues.RA.stdBuilder()
                .itemInputs(
                    electrode.associatedMaterial.getPart(OrePrefixes.dust, 64),
                    ItemList.Shape_Mold_Rod_Long.get(0))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.SolderingAlloy,
                        Materials2FluidShapes.fluidMolten,
                        (int) (1152)))
                .itemOutputs(electrode.getElectrodeItem(1))
                .eut(TierEU.RECIPE_IV)
                .duration(2 * MINUTES)
                .addTo(formingPressRecipes);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Neutronium.getNanite(64))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Neutronium, Materials2FluidShapes.fluidMolten, (int) (64 * 144)))
            .itemOutputs(ArcFurnaceElectrode.NeutroniumNaniteElectrode.getElectrodeItem(1))
            .eut(TierEU.RECIPE_UV)
            .duration(2 * MINUTES)
            .addTo(neutroniumCompressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.TranscendentMetal.getNanite(16))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.TranscendentMetal,
                    Materials2FluidShapes.fluidMolten,
                    (int) (16 * 144)))
            .itemOutputs(ArcFurnaceElectrode.TranscendentNaniteElectrode.getElectrodeItem(1))
            .eut(TierEU.RECIPE_UIV)
            .duration(4 * MINUTES)
            .metadata(COMPRESSION_TIER, 2)
            .addTo(neutroniumCompressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Universium.getNanite(4))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Universium, Materials2FluidShapes.fluidMolten, (int) (4 * 144)))
            .itemOutputs(ArcFurnaceElectrode.UniversiumNaniteElectrode.getElectrodeItem(1))
            .eut(TierEU.RECIPE_UXV)
            .duration(8 * MINUTES)
            .metadata(COMPRESSION_TIER, 2)
            .addTo(neutroniumCompressorRecipes);

    }

}
