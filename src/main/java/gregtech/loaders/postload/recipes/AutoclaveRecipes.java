package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.recipe.RecipeMaps.autoclaveRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.EIGHTH_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.QUARTER_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class AutoclaveRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.IC2_Energium_Dust.get(9L))
            .itemOutputs(ItemList.IC2_EnergyCrystal.get(1L))
            .outputChances(10000)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.EnergeticAlloy, FluidShapes.fluidMolten, 2 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.IC2_Energium_Dust.get(9L))
            .itemOutputs(ItemList.IC2_EnergyCrystal.get(1L))
            .outputChances(10000)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.ConductiveIron, FluidShapes.fluidMolten, 4 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(autoclaveRecipes);

        final ItemStack certusQuartzSeed = getModItem(AppliedEnergistics2.ID, "item.ItemCrystalSeed", 1L, 0);
        NBTTagCompound certusQuartzTag = new NBTTagCompound();
        certusQuartzTag.setInteger("progress", 0);
        certusQuartzSeed.setTagCompound(certusQuartzTag);

        final ItemStack netherQuartzSeed = getModItem(AppliedEnergistics2.ID, "item.ItemCrystalSeed", 1L, 600);
        NBTTagCompound netherQuartzTag = new NBTTagCompound();
        netherQuartzTag.setInteger("progress", 600);
        netherQuartzSeed.setTagCompound(netherQuartzTag);

        final ItemStack fluixSeed = getModItem(AppliedEnergistics2.ID, "item.ItemCrystalSeed", 1L, 1200);
        NBTTagCompound fluixTag = new NBTTagCompound();
        fluixTag.setInteger("progress", 1200);
        fluixSeed.setTagCompound(fluixTag);

        GTValues.RA.stdBuilder()
            .itemInputs(certusQuartzSeed)
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 10))
            .outputChances(8000)
            .fluidInputs(GTUtility.getWater(200L))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(netherQuartzSeed)
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 11))
            .outputChances(8000)
            .fluidInputs(GTUtility.getWater(200L))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(fluixSeed)
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 12))
            .outputChances(8000)
            .fluidInputs(GTUtility.getWater(200L))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(certusQuartzSeed)
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 10))
            .outputChances(9000)
            .fluidInputs(GTModHandler.getDistilledWater(100L))
            .duration(50 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(netherQuartzSeed)
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 11))
            .outputChances(9000)
            .fluidInputs(GTModHandler.getDistilledWater(100L))
            .duration(50 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(fluixSeed)
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 12))
            .outputChances(9000)
            .fluidInputs(GTModHandler.getDistilledWater(100L))
            .duration(50 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(certusQuartzSeed)
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 10))
            .outputChances(10000)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Void, FluidShapes.fluidMolten, QUARTER_INGOTS))
            .duration(25 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(netherQuartzSeed)
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 11))
            .outputChances(10000)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Void, FluidShapes.fluidMolten, QUARTER_INGOTS))
            .duration(25 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(fluixSeed)
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 12))
            .outputChances(10000)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Void, FluidShapes.fluidMolten, QUARTER_INGOTS))
            .duration(25 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, 8))
            .itemOutputs(GTModHandler.getIC2Item("carbonFiber", 16L))
            .outputChances(10000)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Polybenzimidazole, FluidShapes.fluidMolten, 9))
            .duration(1 * SECONDS + 17 * TICKS)
            .eut((int) TierEU.RECIPE_EV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, 4))
            .itemOutputs(GTModHandler.getIC2Item("carbonFiber", 4L))
            .outputChances(10000)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Epoxid, FluidShapes.fluidMolten, 9))
            .duration(18 * TICKS)
            .eut((int) TierEU.RECIPE_HV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, 4))
            .itemOutputs(GTModHandler.getIC2Item("carbonFiber", 2L))
            .outputChances(10000)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.Polytetrafluoroethylene,
                    FluidShapes.fluidMolten,
                    (int) (1 * EIGHTH_INGOTS)))
            .duration(1 * SECONDS + 5 * TICKS)
            .eut((int) TierEU.RECIPE_MV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, 4))
            .itemOutputs(GTModHandler.getIC2Item("carbonFiber", 1L))
            .outputChances(10000)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Plastic, FluidShapes.fluidMolten, 1 * QUARTER_INGOTS))
            .duration(1 * SECONDS + 17 * TICKS)
            .eut((int) TierEU.RECIPE_LV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.NetherStar, Shapes.dust, 1))
            .itemOutputs(GTOreDictUnificator.get("gemNetherStar", 1))
            .outputChances(3333)
            .fluidInputs(MaterialUtils.fluid(Materials.UUMatter, 4 * INGOTS))
            .duration(60 * MINUTES)
            .eut((int) TierEU.RECIPE_HV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(ItemList.QuantumStar.get(1L)))
            .itemOutputs(ItemList.Gravistar.get(1L))
            .outputChances(10000)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Neutronium, FluidShapes.fluidMolten, 2 * INGOTS))
            .duration(24 * SECONDS)
            .eut((int) TierEU.RECIPE_IV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(ItemList.Gravistar.get(16L)))
            .itemOutputs(ItemList.NuclearStar.get(1L))
            .outputChances(10000)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Infinity, FluidShapes.fluidMolten, 2 * INGOTS))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get("gemNetherStar", 64))
            .itemOutputs(ItemList.NuclearStar.get(1L))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Infinity, FluidShapes.fluidPlasma, 4 * INGOTS))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_UIV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Quartzite, Shapes.gem, 1))
            .outputChances(750)
            .fluidInputs(GTUtility.getWater(200L))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Quartzite, Shapes.gem, 1))
            .outputChances(1000)
            .fluidInputs(GTModHandler.getDistilledWater(100L))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Quartzite, Shapes.gem, 1))
            .outputChances(10000)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Void, FluidShapes.fluidMolten, QUARTER_INGOTS))
            .duration(50 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Silicon, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 3))
            .outputChances(7500)
            .fluidInputs(GTUtility.getWater(1_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Silicon, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 3))
            .outputChances(9000)
            .fluidInputs(GTModHandler.getDistilledWater(1_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Olivine, Shapes.gem, 15))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Asbestos, Shapes.dust, 18),
                MaterialLibAPI.getStack(Materials.Magnetite, Shapes.dust, 7))
            .fluidInputs(GTModHandler.getDistilledWater(9_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 14_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(autoclaveRecipes);

        // Marble Block
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Calcite, Shapes.dust, 1))
            .itemOutputs(GTOreDictUnificator.get("blockMarble", 1L))
            .fluidInputs(GTUtility.getWater(1_000L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(autoclaveRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Calcite, Shapes.dust, 1))
            .itemOutputs(GTOreDictUnificator.get("stoneMarble", 1L))
            .fluidInputs(GTModHandler.getDistilledWater(500L))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get("naniteTranscendentMetal", 1L),
                MaterialLibAPI.getStack(Materials.Mellion, Shapes.dust, 32))
            .itemOutputs(ItemList.Phononic_Seed_Crystal.get(8L))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Grade8PurifiedWater, FluidShapes.fluidLiquid, 32_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_UMV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get("roundMagmatter", 1))
            .itemOutputs(ItemList.Phononic_Seed_Crystal.get(5))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.PhononCrystalSolution, FluidShapes.fluidLiquid, 250))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_UXV)
            .addTo(autoclaveRecipes);

        // Exquisite Amalgatite
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.copyAmount(0, GTOreDictUnificator.get("naniteMagmatter", 1)),
                MaterialLibAPI.getStack(Materials.Amalgatite, Shapes.gemFlawless, 3),
                MaterialLibAPI.getStack(Materials.Olenite, Shapes.gemChipped, 64),
                MaterialLibAPI.getStack(Materials.Salt, Shapes.gemChipped, 64),
                MaterialLibAPI.getStack(Materials.Diamond, Shapes.gemChipped, 64),
                MaterialLibAPI.getStack(Materials.VanadioOxyDravite, Shapes.gemChipped, 64))
            .fluidInputs(new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.ChromaticGlass), 1_000_000))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Amalgatite, Shapes.gemExquisite, 1))
            .duration(100 * SECONDS)
            .eut(TierEU.RECIPE_MAX)
            .addTo(autoclaveRecipes);

    }
}
