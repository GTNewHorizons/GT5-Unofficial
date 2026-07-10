package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.GalaxySpace;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.recipe.RecipeMaps.chemicalReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.multiblockChemicalReactorRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.HALF_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.STACKS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.UniversalChemical;
import static gtPlusPlus.core.material.MaterialMisc.SODIUM_NITRATE;
import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

import java.util.Locale;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.system.material.WerkstoffLoader;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2CellShapes;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.objects.OreDictItemStack;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.items.CombType;
import gregtech.loaders.misc.GTBees;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class ChemicalRecipes implements Runnable {

    @Override
    public void run() {
        singleBlockOnly();
        multiblockOnly();
        registerBoth();

        polymerizationRecipes();

        // From ProcessingDye - chemical dye
        for (Dyes dye : Dyes.VALUES) {
            String fluidName = "dye.chemical." + dye.name()
                .toLowerCase(Locale.ENGLISH);
            GTValues.RA.stdBuilder()
                .itemInputs(
                    new OreDictItemStack(dye.name(), 1),
                    MaterialLibAPI.getStack(Materials2Materials.Salt, Materials2Shapes.shapeDust, 2))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.shapeFluidLiquid, 432))
                .fluidOutputs(FluidRegistry.getFluidStack(fluidName, 288))
                .duration(30 * SECONDS)
                .eut(48)
                .addTo(UniversalChemical);
        }
    }

    public void registerBoth() {
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.paper, 1), new ItemStack(Items.string, 1))
            .itemOutputs(GTModHandler.getIC2Item("dynamite", 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Glyceryl, Materials2FluidShapes.shapeFluidLiquid, 500))
            .duration(8 * SECONDS)
            .eut(4)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Aluminium, Materials2Shapes.shapeDust, 4))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Indium, Materials2Shapes.shapeDustTiny, 1))
            .fluidInputs(new FluidStack(ItemList.sIndiumConcentrate, 8_000))
            .fluidOutputs(new FluidStack(ItemList.sLeadZincSolution, 8_000))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Aluminium, Materials2Shapes.shapeDust, 36))
            .circuit(9)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Indium, Materials2Shapes.shapeDust, 1))
            .fluidInputs(new FluidStack(ItemList.sIndiumConcentrate, 72_000))
            .fluidOutputs(new FluidStack(ItemList.sLeadZincSolution, 72_000))
            .duration(22 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // Platinum Group Sludge chain

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Pentlandite, Materials2Shapes.shapeCrushedPurified, 1))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.PlatinumGroupSludge, Materials2Shapes.shapeDustTiny, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(new FluidStack(ItemList.sNickelSulfate, 2_000))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Chalcopyrite, Materials2Shapes.shapeCrushedPurified, 1))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.PlatinumGroupSludge, Materials2Shapes.shapeDustTiny, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(new FluidStack(ItemList.sBlueVitriol, 2_000))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Fe + 3HCl = FeCl3 + 3H

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.shapeDust, 1),
                ItemList.Cell_Empty.get(3))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, 3))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(3_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.IronIIIChloride, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.InfusedGold, Materials2Shapes.shapeDust, 8),
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.shapeDust, 8))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Thaumium, Materials2Shapes.shapeDust, 16))
            .fluidInputs(GTModHandler.getIC2Coolant(1_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                getModItem(GalaxySpace.ID, "item.UnknowCrystal", 4),
                MaterialLibAPI.getStack(Materials2Materials.Osmiridium, Materials2Shapes.shapeDust, 2))
            .itemOutputs(ItemList.Circuit_Chip_Stemcell.get(64))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.GrowthMediumSterilized,
                    Materials2FluidShapes.shapeFluidLiquid,
                    1_000))
            .fluidOutputs(getFluidStack("bacterialsludge", 1_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Chip_Stemcell.get(32),
                MaterialLibAPI.getStack(Materials2Materials.CosmicNeutronium, Materials2Shapes.shapeDust, 4))
            .itemOutputs(ItemList.Circuit_Chip_Biocell.get(32))
            .fluidInputs(Materials.BioMediumSterilized.getFluid(2_000))
            .fluidOutputs(getFluidStack("mutagen", 2_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Items.sugar, 8),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Polyethylene, 1))
            .itemOutputs(ItemList.GelledToluene.get(16))
            .fluidInputs(new FluidStack(ItemList.sToluene, 1_000))
            .duration(56 * SECONDS)
            .eut(192)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.GelledToluene.get(4))
            .circuit(1)
            .itemOutputs(new ItemStack(Blocks.tnt, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.shapeFluidLiquid, 250))
            .duration(10 * SECONDS)
            .eut(24)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.GelledToluene.get(4))
            .circuit(1)
            .itemOutputs(GTModHandler.getIC2Item("industrialTnt", 1))
            .fluidInputs(new FluidStack(ItemList.sNitrationMixture, 200))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DilutedSulfuricAcid,
                    Materials2FluidShapes.shapeFluidLiquid,
                    150))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, 2))
            .circuit(4)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.HydricSulfide, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(1))
            .fluidInputs(Materials.NaturalGas.getGas(16_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Gas, Materials2FluidShapes.shapeFluidGas, 16_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.NaturalGas, 2))
            .circuit(4)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Gas, Materials2CellShapes.shapeCell, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 250))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HydricSulfide, Materials2FluidShapes.shapeFluidGas, 125))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, 2))
            .circuit(4)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.HydricSulfide, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricGas, Materials2FluidShapes.shapeFluidGas, 16_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Gas, Materials2FluidShapes.shapeFluidGas, 16_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.SulfuricGas, Materials2CellShapes.shapeCell, 2))
            .circuit(4)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Gas, Materials2CellShapes.shapeCell, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 250))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HydricSulfide, Materials2FluidShapes.shapeFluidGas, 125))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, 2))
            .circuit(4)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.HydricSulfide, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricNaphtha, Materials2FluidShapes.shapeFluidLiquid, 12_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Naphtha, Materials2FluidShapes.shapeFluidLiquid, 12_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.SulfuricNaphtha, Materials2CellShapes.shapeCell, 3))
            .circuit(4)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Naphtha, Materials2CellShapes.shapeCell, 3))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 500))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HydricSulfide, Materials2FluidShapes.shapeFluidGas, 250))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, 2))
            .circuit(4)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.HydricSulfide, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SulfuricLightFuel,
                    Materials2FluidShapes.shapeFluidLiquid,
                    12_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.LightFuel, Materials2FluidShapes.shapeFluidLiquid, 12_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.SulfuricLightFuel, Materials2CellShapes.shapeCell, 3))
            .circuit(4)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.LightFuel, Materials2CellShapes.shapeCell, 3))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 500))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HydricSulfide, Materials2FluidShapes.shapeFluidGas, 250))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, 2))
            .circuit(4)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.HydricSulfide, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SulfuricHeavyFuel,
                    Materials2FluidShapes.shapeFluidLiquid,
                    8_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HeavyFuel, Materials2FluidShapes.shapeFluidLiquid, 8_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.SulfuricHeavyFuel, Materials2CellShapes.shapeCell, 1))
            .circuit(4)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.HeavyFuel, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 250))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HydricSulfide, Materials2FluidShapes.shapeFluidGas, 125))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Saltpeter, Materials2Shapes.shapeDust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Potassium, Materials2Shapes.shapeDustTiny, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Naphtha, Materials2FluidShapes.shapeFluidLiquid, 576))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Polycaprolactam,
                    Materials2FluidShapes.shapeFluidMolten,
                    9 * INGOTS))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Saltpeter, Materials2Shapes.shapeDust, 9))
            .circuit(9)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Potassium, Materials2Shapes.shapeDust, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Naphtha, Materials2FluidShapes.shapeFluidLiquid, 5_184))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Polycaprolactam,
                    Materials2FluidShapes.shapeFluidMolten,
                    1 * STACKS + 17 * INGOTS))
            .duration(4 * MINUTES + 48 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // 3quartz dust + Na + H2O = 3quartz gem (Na loss

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.NetherQuartz, Materials2Shapes.shapeDust, 3),
                MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.shapeDust, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.NetherQuartz, 3))
            .fluidInputs(Materials.Water.getFluid(1_000))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.CertusQuartz, Materials2Shapes.shapeDust, 3),
                MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.shapeDust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.CertusQuartz, Materials2Shapes.shapeGem, 3))
            .fluidInputs(Materials.Water.getFluid(1_000))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Quartzite, Materials2Shapes.shapeDust, 3),
                MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.shapeDust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Quartzite, Materials2Shapes.shapeGem, 3))
            .fluidInputs(Materials.Water.getFluid(1_000))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.NetherQuartz, Materials2Shapes.shapeDust, 3),
                MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.shapeDust, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.NetherQuartz, 3))
            .fluidInputs(GTModHandler.getDistilledWater(1_000))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.CertusQuartz, Materials2Shapes.shapeDust, 3),
                MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.shapeDust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.CertusQuartz, Materials2Shapes.shapeGem, 3))
            .fluidInputs(GTModHandler.getDistilledWater(1_000))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Quartzite, Materials2Shapes.shapeDust, 3),
                MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.shapeDust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Quartzite, Materials2Shapes.shapeGem, 3))
            .fluidInputs(GTModHandler.getDistilledWater(1_000))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // 3UO2 + 4Al = 3U + 2Al2O3

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Uraninite, Materials2Shapes.shapeDust, 9),
                MaterialLibAPI.getStack(Materials2Materials.Aluminium, Materials2Shapes.shapeDust, 4))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Uranium, Materials2Shapes.shapeDust, 3),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Aluminiumoxide, 10))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // UO2 + 2Mg = U + 2MgO

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Uraninite, Materials2Shapes.shapeDust, 3),
                MaterialLibAPI.getStack(Materials2Materials.Magnesium, Materials2Shapes.shapeDust, 2))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Uranium, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Magnesia, Materials2Shapes.shapeDust, 4))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Ca + C + 3O = CaCO3

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Calcium, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.shapeDust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Calcite, Materials2Shapes.shapeDust, 5))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 3_000))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // C + 4H = CH4

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.shapeDust, 1))
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 4_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(100 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // TiO2 + 2C + 4Cl = TiCl4 + 2CO

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Rutile, Materials2Shapes.shapeDust, 3),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Carbon, 2))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.CarbonMonoxide, Materials2CellShapes.shapeCell, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, 4_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Titaniumtetrachloride,
                    Materials2FluidShapes.shapeFluidLiquid,
                    1_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Rutile, Materials2Shapes.shapeDust, 3),
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.shapeDust, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, 4_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Titaniumtetrachloride,
                    Materials2FluidShapes.shapeFluidLiquid,
                    1_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Rutile, Materials2Shapes.shapeDust, 3),
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.shapeDust, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, 4_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonMonoxide, Materials2FluidShapes.shapeFluidGas, 2_000),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Titaniumtetrachloride,
                    Materials2FluidShapes.shapeFluidLiquid,
                    1_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Titanium, Materials2Shapes.shapeDust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, 4_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Titaniumtetrachloride,
                    Materials2FluidShapes.shapeFluidLiquid,
                    1_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);

        // 4Na + 2MgCl2 = 2Mg + 4NaCl

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.shapeDust, 4),
                MaterialLibAPI.getStack(Materials2Materials.Magnesiumchloride, Materials2Shapes.shapeDust, 6))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Magnesium, Materials2Shapes.shapeDust, 2),
                MaterialLibAPI.getStack(Materials2Materials.Salt, Materials2Shapes.shapeDust, 8))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // rubber

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.RubberRaw, 9),
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.shapeDust, 1))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Rubber, Materials2FluidShapes.shapeFluidMolten, 9 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.RubberRaw, 1),
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.shapeDustTiny, 1))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Rubber, Materials2FluidShapes.shapeFluidMolten, 1 * INGOTS))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(UniversalChemical);

        // vanilla recipe

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Gold, 8),
                new ItemStack(Items.melon, 1, 32767))
            .itemOutputs(new ItemStack(Items.speckled_melon, 1, 0))
            .duration(50)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Gold, 8),
                new ItemStack(Items.carrot, 1, 32767))
            .itemOutputs(new ItemStack(Items.golden_carrot, 1, 0))
            .duration(50)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Gold, 8),
                new ItemStack(Items.apple, 1, 32767))
            .itemOutputs(new ItemStack(Items.golden_apple, 1, 0))
            .duration(50)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Gold, 8),
                new ItemStack(Items.apple, 1, 32767))
            .itemOutputs(new ItemStack(Items.golden_apple, 1, 1))
            .duration(50)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1),
                GTOreDictUnificator.get(OrePrefixes.gem, Materials.EnderPearl, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.EnderEye, 1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1),
                new ItemStack(Items.slime_ball, 1, 32767))
            .itemOutputs(new ItemStack(Items.magma_cream, 1, 0))
            .duration(50)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // 1/9U +Air ==Pu== 0.1Rn

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Plutonium, Materials2Shapes.shapeIngot, 8),
                MaterialLibAPI.getStack(Materials2Materials.Uranium, Materials2Shapes.shapeDustTiny, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Plutonium, Materials2Shapes.shapeDust, 8))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Air, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Radon, Materials2FluidShapes.shapeFluidGas, 100))
            .duration(10 * MINUTES)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        // Silicon Line
        // SiO2 + 2Mg = 2MgO + Si

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.shapeDust, 3),
                MaterialLibAPI.getStack(Materials2Materials.Magnesium, Materials2Shapes.shapeDust, 2))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Silicon, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Magnesia, Materials2Shapes.shapeDust, 4))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.NetherQuartz, Materials2Shapes.shapeDust, 3),
                MaterialLibAPI.getStack(Materials2Materials.Magnesium, Materials2Shapes.shapeDust, 2))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Silicon, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Magnesia, Materials2Shapes.shapeDust, 4))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Quartzite, Materials2Shapes.shapeDust, 6),
                MaterialLibAPI.getStack(Materials2Materials.Magnesium, Materials2Shapes.shapeDust, 2))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Silicon, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Magnesia, Materials2Shapes.shapeDust, 4))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.CertusQuartz, Materials2Shapes.shapeDust, 3),
                MaterialLibAPI.getStack(Materials2Materials.Magnesium, Materials2Shapes.shapeDust, 2))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Silicon, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Magnesia, Materials2Shapes.shapeDust, 4))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Jasper, Materials2Shapes.shapeDust, 3),
                MaterialLibAPI.getStack(Materials2Materials.Magnesium, Materials2Shapes.shapeDust, 2))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Silicon, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Magnesia, Materials2Shapes.shapeDust, 4))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Opal, Materials2Shapes.shapeDust, 3),
                MaterialLibAPI.getStack(Materials2Materials.Magnesium, Materials2Shapes.shapeDust, 2))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Silicon, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Magnesia, Materials2Shapes.shapeDust, 4))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        // 3SiF4 + 4Al = 3Si + 4AlF3

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Aluminium, Materials2Shapes.shapeDust, 4))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Silicon, Materials2Shapes.shapeDust, 3),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.AluminiumFluoride, 16))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SiliconTetrafluoride,
                    Materials2FluidShapes.shapeFluidGas,
                    3_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // SiO2 + 4HF = SiF4 + 2H2O

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.shapeDust, 3))
            .circuit(2)
            .fluidInputs(Materials.HydrofluoricAcid.getFluid(4_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SiliconTetrafluoride,
                    Materials2FluidShapes.shapeFluidGas,
                    1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.NetherQuartz, Materials2Shapes.shapeDust, 3))
            .circuit(2)
            .fluidInputs(Materials.HydrofluoricAcid.getFluid(4_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SiliconTetrafluoride,
                    Materials2FluidShapes.shapeFluidGas,
                    1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.CertusQuartz, Materials2Shapes.shapeDust, 3))
            .circuit(2)
            .fluidInputs(Materials.HydrofluoricAcid.getFluid(4_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SiliconTetrafluoride,
                    Materials2FluidShapes.shapeFluidGas,
                    1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Quartzite, Materials2Shapes.shapeDust, 6))
            .circuit(2)
            .fluidInputs(Materials.HydrofluoricAcid.getFluid(4_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SiliconTetrafluoride,
                    Materials2FluidShapes.shapeFluidGas,
                    1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // 4Na + SiCl4 = 4NaCl + Si

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.shapeDust, 4))
            .circuit(1)
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.SiliconSG, 1),
                MaterialLibAPI.getStack(Materials2Materials.Salt, Materials2Shapes.shapeDust, 8))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SiliconTetrachloride,
                    Materials2FluidShapes.shapeFluidLiquid,
                    1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // HSiCl3 + 2H = 3HCl + Si

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, 2))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.SiliconSG, 1), ItemList.Cell_Empty.get(2))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Trichlorosilane, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(3_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Trichlorosilane, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.SiliconSG, 1), ItemList.Cell_Empty.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 2_000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(3_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // 4HSiCl3 = 3SiCl4 + SiH4

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(1))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Silane, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Trichlorosilane, Materials2FluidShapes.shapeFluidLiquid, 4_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SiliconTetrachloride,
                    Materials2FluidShapes.shapeFluidLiquid,
                    3_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // SiH4 = Si + 4H

        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.SiliconSG, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Silane, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 4_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Ca + 2H = CaH2

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Calcium, Materials2Shapes.shapeDust, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Calciumhydride, 3))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 2_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Si + 4Cl = SiCl4

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Silicon, Materials2Shapes.shapeDust, 1))
            .circuit(2)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, 4_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SiliconTetrachloride,
                    Materials2FluidShapes.shapeFluidLiquid,
                    1_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // 2Na + S = Na2S

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.shapeDust, 2),
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.shapeDust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.SodiumSulfide, Materials2Shapes.shapeDust, 3))
            .duration(60)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // H2S + H2O + (O2) = 0.5H2SO4(Diluted) ( S loss

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.HydricSulfide, Materials2CellShapes.shapeCell, 1))
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(Materials.Water.getFluid(1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DilutedSulfuricAcid,
                    Materials2FluidShapes.shapeFluidLiquid,
                    750))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Water.getCells(1))
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HydricSulfide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DilutedSulfuricAcid,
                    Materials2FluidShapes.shapeFluidLiquid,
                    750))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // Ni + 4CO = Ni(CO)4

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Nickel, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.CarbonMonoxide, Materials2CellShapes.shapeCell, 4))
            .itemOutputs(ItemList.Cell_Empty.get(4))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.NickelTetracarbonyl,
                    Materials2FluidShapes.shapeFluidLiquid,
                    1_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Nickel, Materials2Shapes.shapeDust, 1))
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonMonoxide, Materials2FluidShapes.shapeFluidGas, 4_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.NickelTetracarbonyl,
                    Materials2FluidShapes.shapeFluidLiquid,
                    1_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Nickel, Materials2Shapes.shapeDust, 1),
                ItemList.Cell_Empty.get(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.NickelTetracarbonyl, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonMonoxide, Materials2FluidShapes.shapeFluidGas, 4_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(chemicalReactorRecipes);

        // C2H4O + H2O = C2H6O2

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.EthyleneOxide, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(ItemList.Cell_Empty.get(1))
            .fluidInputs(Materials.Water.getFluid(1_000))
            .fluidOutputs(Materials.Ethyleneglycol.getFluid(1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // C2H4 + O = C2H4O

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Ethylene, Materials2CellShapes.shapeCell, 2))
            .circuit(4)
            .itemOutputs(ItemList.Cell_Empty.get(2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Acetaldehyde, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, 1))
            .circuit(5)
            .itemOutputs(ItemList.Cell_Empty.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.shapeFluidGas, 2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Acetaldehyde, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        // NiAl3 + 2NaOH + 2H2O = NiAl + 2NaAlO2 + 6H

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.NickelAluminide, Materials2Shapes.shapeIngot, 4),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.SodiumHydroxide, 6))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.RaneyNickelActivated, Materials2Shapes.shapeDust, 2),
                MaterialLibAPI.getStack(Materials2Materials.SodiumAluminate, Materials2Shapes.shapeDust, 8))
            .fluidInputs(Materials.Water.getFluid(2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 6_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);

        // Cu + O = CuO

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Copper, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.CupricOxide, Materials2Shapes.shapeDust, 2),
                ItemList.Cell_Empty.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Copper, Materials2Shapes.shapeDust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.CupricOxide, Materials2Shapes.shapeDust, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // 2Bi + 3O = Bi2O3

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Bismuth, Materials2Shapes.shapeDust, 4),
                MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, 6))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.BismuthIIIOxide, Materials2Shapes.shapeDust, 10),
                ItemList.Cell_Empty.get(6))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Bismuth, Materials2Shapes.shapeDust, 4))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.BismuthIIIOxide, Materials2Shapes.shapeDust, 10))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 6_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalReactorRecipes);

        // C4H6O2 + CNH5 = C5H9NO + H2O

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Methylamine, Materials2CellShapes.shapeCell, 1),
                MaterialLibAPI.getStack(Materials2Materials.GammaButyrolactone, Materials2CellShapes.shapeCell, 1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.NMethylIIPyrrolidone, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.shapeDust, 8))
            .circuit(2)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, 16_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfurDichloride, Materials2FluidShapes.shapeFluidLiquid, 8_000))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // SCl2 + SO3 = SO2 + SOCl2

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.SulfurTrioxide, Materials2CellShapes.shapeCell, 1),
                MaterialLibAPI.getStack(Materials2Materials.SulfurDichloride, Materials2CellShapes.shapeCell, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.ThionylChloride, Materials2CellShapes.shapeCell, 1),
                ItemList.Cell_Empty.get(1))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfurDioxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // C8H10 + 6O =CoC22H14O4= C8H6O4 + 2H2O

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.IVDimethylbenzene, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.CobaltIINaphthenate, 41))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.TerephthalicAcid, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 6_000))
            .fluidOutputs(Materials.Water.getFluid(2_000))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // 2CH4 + C6H6 = C8H10 + 4H

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Methane, Materials2CellShapes.shapeCell, 2))
            .circuit(13)
            .itemOutputs(Materials.IIIDimethylbenzene.getCells(1), Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Benzene, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 4_000))
            .duration(3 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Benzene, Materials2CellShapes.shapeCell, 1))
            .circuit(14)
            .itemOutputs(Materials.IIIDimethylbenzene.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.shapeFluidGas, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 4_000))
            .duration(3 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        // 2CH4 + C6H6 = C8H10 + 4H

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Methane, Materials2CellShapes.shapeCell, 2))
            .circuit(15)
            .itemOutputs(Materials.IVDimethylbenzene.getCells(1), Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Benzene, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 4_000))
            .duration(3 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Benzene, Materials2CellShapes.shapeCell, 1))
            .circuit(16)
            .itemOutputs(Materials.IVDimethylbenzene.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.shapeFluidGas, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 4_000))
            .duration(3 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.CobaltIIHydroxide, Materials2Shapes.shapeDust, 5))
            .circuit(1)
            .itemOutputs(Materials.CobaltIINaphthenate.getDust(41))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NaphthenicAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.CobaltIIAcetate.getDust(15))
            .circuit(1)
            .itemOutputs(Materials.CobaltIINaphthenate.getDust(41))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NaphthenicAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.shapeFluidLiquid, 1_500))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // Co + 2HNO3 = Co(NO3)2 + 2H

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Cobalt, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.NitricAcid, Materials2CellShapes.shapeCell, 2))
            .itemOutputs(
                Materials.CobaltIINitrate.getDust(9),
                MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, 2))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        // Co(NO3)2 + 2KOH = CoH2O2 + 2KNO3

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.CobaltIINitrate.getDust(9), Materials.PotassiumHydroxide.getDust(6))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.CobaltIIHydroxide, Materials2Shapes.shapeDust, 5),
                MaterialLibAPI.getStack(Materials2Materials.Saltpeter, Materials2Shapes.shapeDust, 10))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        // CoO + 2C2H4O2 = CoC4H6O4 + 2H

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.CobaltOxide, Materials2Shapes.shapeDust, 2),
                MaterialLibAPI.getStack(Materials2Materials.AceticAcid, Materials2CellShapes.shapeCell, 2))
            .itemOutputs(Materials.CobaltIIAcetate.getDust(15), ItemList.Cell_Empty.get(2))
            .fluidOutputs(Materials.Water.getFluid(2_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Phosphorus, Materials2Shapes.shapeDust, 1))
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, 3_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.PhosphorusTrichloride,
                    Materials2FluidShapes.shapeFluidLiquid,
                    1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Phosphorus, Materials2Shapes.shapeDust, 9))
            .circuit(9)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, 27_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.PhosphorusTrichloride,
                    Materials2FluidShapes.shapeFluidLiquid,
                    9_000))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        // Na + H = NaH

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.shapeDust, 1))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.SodiumHydride, Materials2Shapes.shapeDust, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        // CH3ONa + H2O = CH4O + NaOH

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.SodiumMethoxide, Materials2Shapes.shapeDust, 6))
            .circuit(1)
            .itemOutputs(Materials.SodiumHydroxide.getDust(3))
            .fluidInputs(Materials.Water.getFluid(1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methanol, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // K + HNO3 = KNO3 + H (not real, but gameplay

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Potassium, Materials2Shapes.shapeDust, 1))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Saltpeter, Materials2Shapes.shapeDust, 5))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // CH3COOH + CH3OH = CH3COOCH3 + H2O

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.AceticAcid, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(Materials.Water.getCells(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methanol, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.MethylAcetate, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Methanol, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(Materials.Water.getCells(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.MethylAcetate, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.AceticAcid, Materials2CellShapes.shapeCell, 1))
            .circuit(2)
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methanol, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.MethylAcetate, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Methanol, Materials2CellShapes.shapeCell, 1))
            .circuit(2)
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.MethylAcetate, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.AceticAcid, Materials2CellShapes.shapeCell, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.MethylAcetate, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methanol, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(Materials.Water.getFluid(1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Methanol, Materials2CellShapes.shapeCell, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.MethylAcetate, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(Materials.Water.getFluid(1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.AceticAcid, Materials2CellShapes.shapeCell, 1))
            .circuit(12)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.MethylAcetate, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methanol, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Methanol, Materials2CellShapes.shapeCell, 1))
            .circuit(12)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.MethylAcetate, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // CO and CO2 recipes

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.shapeDust, 1))
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonMonoxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Coal.getGems(1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.shapeDustTiny, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonMonoxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Coal, Materials2Shapes.shapeDust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.shapeDustTiny, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonMonoxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Charcoal.getGems(1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.shapeDustTiny, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonMonoxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Charcoal, Materials2Shapes.shapeDust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.shapeDustTiny, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonMonoxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.shapeDust, 1))
            .circuit(2)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Coal.getGems(1))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.shapeDustTiny, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Coal, Materials2Shapes.shapeDust, 1))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.shapeDustTiny, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Charcoal.getGems(1))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.shapeDustTiny, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Charcoal, Materials2Shapes.shapeDust, 1))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.shapeDustTiny, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.shapeDust, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonMonoxide, Materials2FluidShapes.shapeFluidGas, 2_000))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Coal.getGems(9))
            .circuit(9)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.shapeDust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 9_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonMonoxide, Materials2FluidShapes.shapeFluidGas, 9_000))
            .duration(36 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Coal, Materials2Shapes.shapeDust, 9))
            .circuit(9)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.shapeDust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 9_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonMonoxide, Materials2FluidShapes.shapeFluidGas, 9_000))
            .duration(36 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Charcoal.getGems(9))
            .circuit(9)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.shapeDust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 9_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonMonoxide, Materials2FluidShapes.shapeFluidGas, 9_000))
            .duration(36 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Charcoal, Materials2Shapes.shapeDust, 9))
            .circuit(9)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.shapeDust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 9_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonMonoxide, Materials2FluidShapes.shapeFluidGas, 9_000))
            .duration(36 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Coal.getGems(9))
            .circuit(8)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.shapeDust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 18_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.shapeFluidGas, 9_000))
            .duration(18 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Coal, Materials2Shapes.shapeDust, 9))
            .circuit(8)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.shapeDust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 18_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.shapeFluidGas, 9_000))
            .duration(18 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Charcoal.getGems(9))
            .circuit(8)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.shapeDust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 18_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.shapeFluidGas, 9_000))
            .duration(18 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Charcoal, Materials2Shapes.shapeDust, 9))
            .circuit(8)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Ash, Materials2Shapes.shapeDust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 18_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.shapeFluidGas, 9_000))
            .duration(18 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        // CO + 4H = CH3OH

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.CarbonMonoxide, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 4_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methanol, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(6 * SECONDS)
            .eut(96)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, 4))
            .circuit(1)
            .itemOutputs(Materials.Empty.getCells(4))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonMonoxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methanol, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(6 * SECONDS)
            .eut(96)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.CarbonMonoxide, Materials2CellShapes.shapeCell, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Methanol, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 4_000))
            .duration(6 * SECONDS)
            .eut(96)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, 4))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Methanol, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(3))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonMonoxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(6 * SECONDS)
            .eut(96)
            .addTo(UniversalChemical);

        // CO2 + 6H = CH3OH + H2O

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.CarbonDioxide, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(Materials.Water.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 6_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methanol, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(6 * SECONDS)
            .eut(96)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, 6))
            .circuit(1)
            .itemOutputs(Materials.Water.getCells(1), Materials.Empty.getCells(5))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methanol, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(6 * SECONDS)
            .eut(96)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.CarbonDioxide, Materials2CellShapes.shapeCell, 1))
            .circuit(2)
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 6_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methanol, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(6 * SECONDS)
            .eut(96)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, 6))
            .circuit(2)
            .itemOutputs(Materials.Empty.getCells(6))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methanol, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(6 * SECONDS)
            .eut(96)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.CarbonDioxide, Materials2CellShapes.shapeCell, 1))
            .circuit(12)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Methanol, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 6_000))
            .duration(6 * SECONDS)
            .eut(96)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, 6))
            .circuit(12)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Methanol, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(5))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(6 * SECONDS)
            .eut(96)
            .addTo(UniversalChemical);

        // CH3OH + CO = CH3COOH

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Methanol, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonMonoxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.CarbonMonoxide, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methanol, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Methanol, Materials2CellShapes.shapeCell, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.AceticAcid, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonMonoxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.CarbonMonoxide, Materials2CellShapes.shapeCell, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.AceticAcid, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methanol, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // CH2CH2 + 2O = CH3COOH

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Ethylene, Materials2CellShapes.shapeCell, 1))
            .circuit(8)
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, 2))
            .circuit(8)
            .itemOutputs(Materials.Empty.getCells(2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Ethylene, Materials2CellShapes.shapeCell, 1))
            .circuit(19)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.AceticAcid, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 2_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, 2))
            .circuit(19)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.AceticAcid, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // O + C2H4O2 + C2H4 = C4H6O2 + H2O

        GTValues.RA.stdBuilder()
            .circuit(4)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.shapeFluidGas, 1_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.VinylAcetate, Materials2FluidShapes.shapeFluidLiquid, 1_000),
                Materials.Water.getFluid(1_000))
            .duration(9 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Ethylene, Materials2CellShapes.shapeCell, 1),
                MaterialLibAPI.getStack(Materials2Materials.AceticAcid, Materials2CellShapes.shapeCell, 1))
            .itemOutputs(Materials.Water.getCells(1), Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.VinylAcetate, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(9 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.AceticAcid, Materials2CellShapes.shapeCell, 1),
                MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, 1))
            .itemOutputs(Materials.Water.getCells(1), Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.VinylAcetate, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(9 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, 1),
                MaterialLibAPI.getStack(Materials2Materials.Ethylene, Materials2CellShapes.shapeCell, 1))
            .itemOutputs(Materials.Water.getCells(1), Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.VinylAcetate, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(9 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // Ethanol -> Ethylene (Intended loss for Sulfuric Acid)

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Ethanol, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Ethylene, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DilutedSulfuricAcid,
                    Materials2FluidShapes.shapeFluidLiquid,
                    1_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.SulfuricAcid, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Ethylene, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Ethanol, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DilutedSulfuricAcid,
                    Materials2FluidShapes.shapeFluidLiquid,
                    1_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Ethanol, Materials2CellShapes.shapeCell, 1))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.DilutedSulfuricAcid, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.SulfuricAcid, Materials2CellShapes.shapeCell, 1))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.DilutedSulfuricAcid, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Ethanol, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        // H2O + Na = NaOH + H

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.shapeDust, 1))
            .circuit(1)
            .itemOutputs(Materials.SodiumHydroxide.getDust(3))
            .fluidInputs(Materials.Water.getFluid(1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // H2O + K = KOH + H

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Potassium, Materials2Shapes.shapeDust, 1))
            .circuit(1)
            .itemOutputs(Materials.PotassiumHydroxide.getDust(3))
            .fluidInputs(Materials.Water.getFluid(1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // H2O + Cs = CsOH + H

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Caesium, Materials2Shapes.shapeDust, 1))
            .circuit(1)
            .itemOutputs(Materials.CaesiumHydroxide.getDust(3))
            .fluidInputs(Materials.Water.getFluid(1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // H + Cl = HCl

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Chlorine, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(1_000))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(1_000))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Chlorine, Materials2CellShapes.shapeCell, 1))
            .circuit(11)
            .itemOutputs(Materials.HydrochloricAcid.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, 1))
            .circuit(11)
            .itemOutputs(Materials.HydrochloricAcid.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        // NaOH + HCl = NaCl + H2O

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.SodiumHydroxide.getDust(3))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Salt, Materials2Shapes.shapeDust, 2))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(1_000))
            .fluidOutputs(Materials.Water.getFluid(1000))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        // NaOH + 2 (HCl)(H2O) = 2 H2O + 2 NaCl
        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemInputs(Materials.SodiumHydroxide.getDust(3))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Salt, Materials2Shapes.shapeDust, 2))
            .fluidInputs(Materials.DilutedHydrochloricAcid.getFluid(2_000))
            .fluidOutputs(Materials.Water.getFluid(2000))
            .duration(3 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // C3H6 + 2Cl = HCl + C3H5Cl

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Chlorine, Materials2CellShapes.shapeCell, 2))
            .circuit(1)
            .itemOutputs(Materials.HydrochloricAcid.getCells(1), Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Propene, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.AllylChloride, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Propene, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(Materials.HydrochloricAcid.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, 2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.AllylChloride, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Chlorine, Materials2CellShapes.shapeCell, 2))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.AllylChloride, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Propene, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Propene, Materials2CellShapes.shapeCell, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.AllylChloride, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, 2_000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // 2Cl + H2O = HCl + HClO (Intended loss)

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Chlorine, Materials2CellShapes.shapeCell, 2))
            .circuit(1)
            .itemOutputs(Materials.DilutedHydrochloricAcid.getCells(1), Materials.Empty.getCells(1))
            .fluidInputs(Materials.Water.getFluid(1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HypochlorousAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Water.getCells(1))
            .circuit(1)
            .itemOutputs(Materials.DilutedHydrochloricAcid.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, 2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HypochlorousAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Chlorine, Materials2CellShapes.shapeCell, 2))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.HypochlorousAcid, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(1))
            .fluidInputs(Materials.Water.getFluid(1_000))
            .fluidOutputs(Materials.DilutedHydrochloricAcid.getFluid(1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Water.getCells(1))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.HypochlorousAcid, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, 2_000))
            .fluidOutputs(Materials.DilutedHydrochloricAcid.getFluid(1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // HClO + NaOH + C3H5Cl = C3H5ClO + NaCl·H2O

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.HypochlorousAcid, Materials2CellShapes.shapeCell, 1),
                Materials.SodiumHydroxide.getDust(3))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.SaltWater, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.AllylChloride, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Epichlorohydrin, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Ba + H2SO4 = BaSO4 + H2
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Barium, Materials2Shapes.shapeDust, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.shapeFluidLiquid, 1000))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Barite, Materials2Shapes.shapeDust, 6))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 2000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.SodiumHydroxide.getDust(3),
                MaterialLibAPI.getStack(Materials2Materials.AllylChloride, Materials2CellShapes.shapeCell, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.SaltWater, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HypochlorousAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Epichlorohydrin, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.HydrochloricAcid.getCells(1), Materials.Empty.getCells(1))
            .itemOutputs(Materials.Water.getCells(2))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Glycerol, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Epichlorohydrin, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Glycerol, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(1))
            .itemOutputs(Materials.Water.getCells(2))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Epichlorohydrin, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.HydrochloricAcid.getCells(1))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Epichlorohydrin, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Glycerol, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(Materials.Water.getFluid(2_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Glycerol, Materials2CellShapes.shapeCell, 1))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Epichlorohydrin, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(1_000))
            .fluidOutputs(Materials.Water.getFluid(2_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.HydrochloricAcid.getCells(1))
            .circuit(2)
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Glycerol, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Epichlorohydrin, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Glycerol, Materials2CellShapes.shapeCell, 1))
            .circuit(2)
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Epichlorohydrin, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.HydrochloricAcid.getCells(1))
            .circuit(12)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Epichlorohydrin, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Glycerol, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Glycerol, Materials2CellShapes.shapeCell, 1))
            .circuit(12)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Epichlorohydrin, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(1_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // P4O10 + 6H2O = 4H3PO4

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.PhosphorousPentoxide, Materials2Shapes.shapeDust, 14))
            .fluidInputs(Materials.Water.getFluid(6_000))
            .fluidOutputs(Materials.PhosphoricAcid.getFluid(4_000))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // C9H12 + 2O = C6H6O + C3H6O

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Cumene.getCells(1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Phenol, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Acetone, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, 2))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Phenol, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(1))
            .fluidInputs(Materials.Cumene.getFluid(1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Acetone, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Cumene.getCells(1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Acetone, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Phenol, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, 2))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Acetone, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(1))
            .fluidInputs(Materials.Cumene.getFluid(1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Phenol, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // C15H16O2 + 2C3H5ClO + 2NaOH = C15H14O2(C3H5O)2 + 2NaCl·H2O

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.SodiumHydroxide.getDust(6),
                MaterialLibAPI.getStack(Materials2Materials.Epichlorohydrin, Materials2CellShapes.shapeCell, 2))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.SaltWater, Materials2CellShapes.shapeCell, 2))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.BisphenolA, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Epoxid, Materials2FluidShapes.shapeFluidMolten, 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // CH4O + HCl = CH3Cl + H2O

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Methanol, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(Materials.Water.getCells(1))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Chloromethane, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.HydrochloricAcid.getCells(1))
            .circuit(1)
            .itemOutputs(Materials.Water.getCells(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methanol, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Chloromethane, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Methanol, Materials2CellShapes.shapeCell, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Chloromethane, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(1_000))
            .fluidOutputs(Materials.Water.getFluid(1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.HydrochloricAcid.getCells(1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Chloromethane, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methanol, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(Materials.Water.getFluid(1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Methanol, Materials2CellShapes.shapeCell, 1))
            .circuit(2)
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Chloromethane, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.HydrochloricAcid.getCells(1))
            .circuit(2)
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methanol, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Chloromethane, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Methanol, Materials2CellShapes.shapeCell, 1))
            .circuit(12)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Chloromethane, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.HydrochloricAcid.getCells(1))
            .circuit(12)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Chloromethane, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methanol, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Chlorine, Materials2CellShapes.shapeCell, 2))
            .circuit(1)
            .itemOutputs(Materials.HydrochloricAcid.getCells(1), Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Chloromethane, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Methane, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(Materials.HydrochloricAcid.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, 2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Chloromethane, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Chlorine, Materials2CellShapes.shapeCell, 2))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Chloromethane, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(1_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Methane, Materials2CellShapes.shapeCell, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Chloromethane, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, 2_000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(1_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Cl6 + CH4 = CHCl3 + 3HCl

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Chlorine, Materials2CellShapes.shapeCell, 6))
            .circuit(3)
            .itemOutputs(Materials.HydrochloricAcid.getCells(3), Materials.Empty.getCells(3))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Chloroform, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Chlorine, Materials2CellShapes.shapeCell, 6))
            .circuit(13)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Chloroform, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(5))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(3_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Methane, Materials2CellShapes.shapeCell, 1))
            .circuit(13)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Chloroform, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, 6_000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(3_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // H + F = HF

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Fluorine, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(Materials.HydrofluoricAcid.getFluid(1_000))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Fluorine, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(Materials.HydrofluoricAcid.getFluid(1_000))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Fluorine, Materials2CellShapes.shapeCell, 1))
            .circuit(11)
            .itemOutputs(Materials.HydrofluoricAcid.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, 1))
            .circuit(11)
            .itemOutputs(Materials.HydrofluoricAcid.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Fluorine, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        // 4HF + 2CHCl3 = C2F4 + 6HCl

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Chloroform, Materials2CellShapes.shapeCell, 2),
                Materials.HydrofluoricAcid.getCells(4))
            .itemOutputs(Materials.HydrochloricAcid.getCells(6))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Tetrafluoroethylene, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Chloroform, Materials2CellShapes.shapeCell, 2),
                Materials.Empty.getCells(4))
            .itemOutputs(Materials.HydrochloricAcid.getCells(6))
            .fluidInputs(Materials.HydrofluoricAcid.getFluid(4_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Tetrafluoroethylene, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.HydrofluoricAcid.getCells(4), Materials.Empty.getCells(2))
            .itemOutputs(Materials.HydrochloricAcid.getCells(6))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Chloroform, Materials2FluidShapes.shapeFluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Tetrafluoroethylene, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.HydrofluoricAcid.getCells(4))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Tetrafluoroethylene, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(3))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Chloroform, Materials2FluidShapes.shapeFluidLiquid, 2_000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(6_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Chloroform, Materials2CellShapes.shapeCell, 2))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Tetrafluoroethylene, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(1))
            .fluidInputs(Materials.HydrofluoricAcid.getFluid(4_000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(6_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        // Si + 2CH3Cl = C2H6Cl2Si

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Silicon, Materials2Shapes.shapeDust, 1))
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Chloromethane, Materials2FluidShapes.shapeFluidGas, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Dimethyldichlorosilane,
                    Materials2FluidShapes.shapeFluidLiquid,
                    1_000))
            .duration(12 * SECONDS)
            .eut(96)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Dimethyldichlorosilane, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Polydimethylsiloxane, Materials2Shapes.shapeDust, 3),
                Materials.Empty.getCells(1))
            .fluidInputs(Materials.Water.getFluid(1_000))
            .fluidOutputs(Materials.DilutedHydrochloricAcid.getFluid(1_000))
            .duration(12 * SECONDS)
            .eut(96)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Water.getCells(1))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Polydimethylsiloxane, Materials2Shapes.shapeDust, 3),
                Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Dimethyldichlorosilane,
                    Materials2FluidShapes.shapeFluidLiquid,
                    1_000))
            .fluidOutputs(Materials.DilutedHydrochloricAcid.getFluid(1_000))
            .duration(12 * SECONDS)
            .eut(96)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Dimethyldichlorosilane, Materials2CellShapes.shapeCell, 1))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Polydimethylsiloxane, Materials2Shapes.shapeDust, 3),
                Materials.DilutedHydrochloricAcid.getCells(1))
            .fluidInputs(Materials.Water.getFluid(1_000))
            .duration(12 * SECONDS)
            .eut(96)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Water.getCells(1))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Polydimethylsiloxane, Materials2Shapes.shapeDust, 3),
                Materials.DilutedHydrochloricAcid.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Dimethyldichlorosilane,
                    Materials2FluidShapes.shapeFluidLiquid,
                    1_000))
            .duration(12 * SECONDS)
            .eut(96)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Polydimethylsiloxane, Materials2Shapes.shapeDust, 9),
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.shapeDust, 1))
            .fluidOutputs(Materials.RubberSilicone.getMolten(9 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Potassium Nitride
        // K + HNO3 = KNO3 + H

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Potassium, Materials2Shapes.shapeDust, 1))
            .circuit(1)
            .itemOutputs(Materials.PotassiumNitrade.getDust(5))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Chromium Trioxide
        // CrO2 + O = CrO3

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.ChromiumDioxide, Materials2Shapes.shapeDust, 3))
            .circuit(1)
            .itemOutputs(Materials.ChromiumTrioxide.getDust(4))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        // Nitrochlorobenzene
        // C6H5Cl + HNO3 = C6H4ClNO2 + H2O

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Chlorobenzene, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(Materials.Nitrochlorobenzene.getCells(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitrationMixture, Materials2FluidShapes.shapeFluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DilutedSulfuricAcid,
                    Materials2FluidShapes.shapeFluidLiquid,
                    1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Chlorobenzene, Materials2CellShapes.shapeCell, 1))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.DilutedSulfuricAcid, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitrationMixture, Materials2FluidShapes.shapeFluidLiquid, 2_000))
            .fluidOutputs(Materials.Nitrochlorobenzene.getFluid(1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.NitrationMixture, Materials2CellShapes.shapeCell, 2))
            .circuit(1)
            .itemOutputs(Materials.Nitrochlorobenzene.getCells(1), Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Chlorobenzene, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DilutedSulfuricAcid,
                    Materials2FluidShapes.shapeFluidLiquid,
                    1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.NitrationMixture, Materials2CellShapes.shapeCell, 2))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.DilutedSulfuricAcid, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Chlorobenzene, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(Materials.Nitrochlorobenzene.getFluid(1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // C6H6 + 2CH4 = C8H10 + 4H

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Methane, Materials2CellShapes.shapeCell, 2))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Dimethylbenzene, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Benzene, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 4_000))
            .duration(3 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Benzene, Materials2CellShapes.shapeCell, 1))
            .circuit(12)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Dimethylbenzene, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.shapeFluidGas, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 4_000))
            .duration(3 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        // Phthalic Acid
        // C8H10 + 6O =K2Cr2O7= C8H6O4 + 2H2O

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Dimethylbenzene, Materials2CellShapes.shapeCell, 1),
                Materials.Potassiumdichromate.getDustTiny(1))
            .itemOutputs(Materials.PhthalicAcid.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 6_000))
            .fluidOutputs(Materials.Water.getFluid(2_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, 6),
                Materials.Potassiumdichromate.getDustTiny(1))
            .itemOutputs(Materials.PhthalicAcid.getCells(1), ItemList.Cell_Empty.get(5))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Dimethylbenzene, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(Materials.Water.getFluid(2_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Dimethylbenzene, Materials2CellShapes.shapeCell, 9),
                Materials.Potassiumdichromate.getDust(1))
            .itemOutputs(Materials.PhthalicAcid.getCells(9))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 54_000))
            .fluidOutputs(Materials.Water.getFluid(18_000))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, 54),
                Materials.Potassiumdichromate.getDust(1))
            .itemOutputs(Materials.PhthalicAcid.getCells(9), ItemList.Cell_Empty.get(45))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Dimethylbenzene, Materials2FluidShapes.shapeFluidLiquid, 9_000))
            .fluidOutputs(Materials.Water.getFluid(18_000))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(chemicalReactorRecipes);

        // These following recipes are broken in element term.
        // But they are kept in gamewise, too much existed setup will be broken.
        // Dichlorobenzidine

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Copper, Materials2Shapes.shapeDustTiny, 1))
            .circuit(1)
            .fluidInputs(Materials.Nitrochlorobenzene.getFluid(2_000))
            .fluidOutputs(Materials.Dichlorobenzidine.getFluid(1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Copper, Materials2Shapes.shapeDust, 1))
            .circuit(9)
            .fluidInputs(Materials.Nitrochlorobenzene.getFluid(18_000))
            .fluidOutputs(Materials.Dichlorobenzidine.getFluid(9_000))
            .duration(1 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);

        // Diphenyl Isophthalate

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.PhthalicAcid.getCells(1),
                MaterialLibAPI.getStack(Materials2Materials.SulfuricAcid, Materials2CellShapes.shapeCell, 1))
            .itemOutputs(Materials.Diphenylisophthalate.getCells(1), ItemList.Cell_Empty.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Phenol, Materials2FluidShapes.shapeFluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DilutedSulfuricAcid,
                    Materials2FluidShapes.shapeFluidLiquid,
                    1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.PhthalicAcid.getCells(1),
                MaterialLibAPI.getStack(Materials2Materials.Phenol, Materials2CellShapes.shapeCell, 2))
            .itemOutputs(Materials.Diphenylisophthalate.getCells(1), ItemList.Cell_Empty.get(2))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DilutedSulfuricAcid,
                    Materials2FluidShapes.shapeFluidLiquid,
                    1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.SulfuricAcid, Materials2CellShapes.shapeCell, 1),
                MaterialLibAPI.getStack(Materials2Materials.Phenol, Materials2CellShapes.shapeCell, 2))
            .itemOutputs(Materials.Diphenylisophthalate.getCells(1), ItemList.Cell_Empty.get(2))
            .fluidInputs(Materials.PhthalicAcid.getFluid(1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DilutedSulfuricAcid,
                    Materials2FluidShapes.shapeFluidLiquid,
                    1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(chemicalReactorRecipes);

        // Diaminobenzidin

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Ammonia, Materials2CellShapes.shapeCell, 2),
                MaterialLibAPI.getStack(Materials2Materials.Zinc, Materials2Shapes.shapeDust, 1))
            .itemOutputs(Materials.Diaminobenzidin.getCells(1), ItemList.Cell_Empty.get(1))
            .fluidInputs(Materials.Dichlorobenzidine.getFluid(1_000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(2_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(UniversalChemical);

        // Polybenzimidazole
        // C12H14N4 + C20H14O4 = C20H12N4 + 2C6H6O + 2H2O

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Diphenylisophthalate.getCells(1), Materials.Diaminobenzidin.getCells(1))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Phenol, Materials2CellShapes.shapeCell, 2))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Polybenzimidazole,
                    Materials2FluidShapes.shapeFluidMolten,
                    1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Tin, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Saltpeter, Materials2Shapes.shapeDust, 1))
            .itemOutputs(getModItem(Railcraft.ID, "glass", 6))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Glass, Materials2FluidShapes.shapeFluidMolten, 6 * INGOTS))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // NH3 + 2CH4O = C2H7N + 2H2O

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Methanol, Materials2CellShapes.shapeCell, 2))
            .circuit(1)
            .itemOutputs(Materials.Water.getCells(2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ammonia, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Dimethylamine, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Methanol, Materials2CellShapes.shapeCell, 2))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Dimethylamine, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ammonia, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(Materials.Water.getFluid(1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Ammonia, Materials2CellShapes.shapeCell, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Dimethylamine, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methanol, Materials2FluidShapes.shapeFluidLiquid, 2_000))
            .fluidOutputs(Materials.Water.getFluid(1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Methanol, Materials2CellShapes.shapeCell, 2))
            .circuit(2)
            .itemOutputs(Materials.Empty.getCells(2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ammonia, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Dimethylamine, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Methanol, Materials2CellShapes.shapeCell, 2))
            .circuit(12)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Dimethylamine, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ammonia, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Ammonia, Materials2CellShapes.shapeCell, 1))
            .circuit(12)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Dimethylamine, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methanol, Materials2FluidShapes.shapeFluidLiquid, 2_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        // NH3 + HClO = NH2Cl + H2O

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Ammonia, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(Materials.Water.getCells(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HypochlorousAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Chloramine, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.HypochlorousAcid, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(Materials.Water.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ammonia, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Chloramine, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Ammonia, Materials2CellShapes.shapeCell, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Chloramine, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HypochlorousAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(Materials.Water.getFluid(1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.HypochlorousAcid, Materials2CellShapes.shapeCell, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Chloramine, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ammonia, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(Materials.Water.getFluid(1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Ammonia, Materials2CellShapes.shapeCell, 1))
            .circuit(2)
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HypochlorousAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Chloramine, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.HypochlorousAcid, Materials2CellShapes.shapeCell, 1))
            .circuit(2)
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ammonia, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Chloramine, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Ammonia, Materials2CellShapes.shapeCell, 1))
            .circuit(12)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Chloramine, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HypochlorousAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.HypochlorousAcid, Materials2CellShapes.shapeCell, 1))
            .circuit(12)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Chloramine, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ammonia, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // 2NO2 = N2O4

        GTValues.RA.stdBuilder()
            .circuit(6)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitrogenDioxide, Materials2FluidShapes.shapeFluidGas, 2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.DinitrogenTetroxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.NitrogenDioxide, Materials2CellShapes.shapeCell, 2))
            .circuit(2)
            .itemOutputs(Materials.Empty.getCells(2))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.DinitrogenTetroxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.NitrogenDioxide, Materials2CellShapes.shapeCell, 2))
            .circuit(12)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.DinitrogenTetroxide, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(1))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // 2NH3 + 5O = 2NO + 3H2O

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Ammonia, Materials2CellShapes.shapeCell, 4))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.NitricOxide, Materials2CellShapes.shapeCell, 4))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 10_000))
            .fluidOutputs(Materials.Water.getFluid(6_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, 10))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.NitricOxide, Materials2CellShapes.shapeCell, 4),
                Materials.Empty.getCells(6))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ammonia, Materials2FluidShapes.shapeFluidGas, 4_000))
            .fluidOutputs(Materials.Water.getFluid(6_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, 10))
            .circuit(11)
            .itemOutputs(Materials.Water.getCells(6), Materials.Empty.getCells(4))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ammonia, Materials2FluidShapes.shapeFluidGas, 4_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricOxide, Materials2FluidShapes.shapeFluidGas, 4_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Ammonia, Materials2CellShapes.shapeCell, 4))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.NitricOxide, Materials2CellShapes.shapeCell, 4))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 10_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, 10))
            .circuit(2)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.NitricOxide, Materials2CellShapes.shapeCell, 4),
                Materials.Empty.getCells(6))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ammonia, Materials2FluidShapes.shapeFluidGas, 4_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, 10))
            .circuit(12)
            .itemOutputs(Materials.Empty.getCells(10))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ammonia, Materials2FluidShapes.shapeFluidGas, 4_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricOxide, Materials2FluidShapes.shapeFluidGas, 4_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // NO + O = NO2

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.NitricOxide, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitrogenDioxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricOxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitrogenDioxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.NitricOxide, Materials2CellShapes.shapeCell, 1))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.NitrogenDioxide, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, 1))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.NitrogenDioxide, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricOxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // H2O + 3NO2 = 2HNO3 + NO

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Water.getCells(1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.NitricOxide, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitrogenDioxide, Materials2FluidShapes.shapeFluidGas, 3_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricAcid, Materials2FluidShapes.shapeFluidLiquid, 2_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.NitrogenDioxide, Materials2CellShapes.shapeCell, 3))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.NitricOxide, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(2))
            .fluidInputs(Materials.Water.getFluid(1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricAcid, Materials2FluidShapes.shapeFluidLiquid, 2_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.NitrogenDioxide, Materials2CellShapes.shapeCell, 3))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.NitricAcid, Materials2CellShapes.shapeCell, 2),
                Materials.Empty.getCells(1))
            .fluidInputs(Materials.Water.getFluid(1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricOxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // S + 2H = H2S

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.shapeDust, 1))
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HydricSulfide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        // S + 2O = SO2

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.shapeDust, 1))
            .circuit(3)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfurDioxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        // H2S + 3O = SO2 + H2O

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.HydricSulfide, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(Materials.Water.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 3_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfurDioxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, 3))
            .circuit(1)
            .itemOutputs(Materials.Water.getCells(1), Materials.Empty.getCells(2))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HydricSulfide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfurDioxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.HydricSulfide, Materials2CellShapes.shapeCell, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.SulfurDioxide, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 3_000))
            .fluidOutputs(Materials.Water.getFluid(1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, 3))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.SulfurDioxide, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(2))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HydricSulfide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(Materials.Water.getFluid(1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.HydricSulfide, Materials2CellShapes.shapeCell, 1))
            .circuit(2)
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 3_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfurDioxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, 3))
            .circuit(2)
            .itemOutputs(Materials.Empty.getCells(3))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HydricSulfide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfurDioxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.HydricSulfide, Materials2CellShapes.shapeCell, 1))
            .circuit(12)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.SulfurDioxide, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 3_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, 3))
            .circuit(12)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.SulfurDioxide, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(2))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HydricSulfide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // 2H2S + SO2 = 3S + 2H2O

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.SulfurDioxide, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.shapeDust, 3),
                Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HydricSulfide, Materials2FluidShapes.shapeFluidGas, 2_000))
            .fluidOutputs(Materials.Water.getFluid(2_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.HydricSulfide, Materials2CellShapes.shapeCell, 2))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.shapeDust, 3),
                Materials.Empty.getCells(2))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfurDioxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(Materials.Water.getFluid(2_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.SulfurDioxide, Materials2CellShapes.shapeCell, 1))
            .circuit(2)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.shapeDust, 3),
                Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HydricSulfide, Materials2FluidShapes.shapeFluidGas, 2_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.HydricSulfide, Materials2CellShapes.shapeCell, 2))
            .circuit(2)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.shapeDust, 3),
                Materials.Empty.getCells(2))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfurDioxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // SO2 + O = SO3

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfurDioxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfurTrioxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.SulfurDioxide, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfurTrioxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.SulfurTrioxide, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfurDioxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.SulfurDioxide, Materials2CellShapes.shapeCell, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.SulfurTrioxide, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // SO3 + H2O = H2SO4

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Water.getCells(1))
            .circuit(1)
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfurTrioxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.SulfurTrioxide, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(Materials.Water.getFluid(1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Water.getCells(1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.SulfuricAcid, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfurTrioxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.SulfurTrioxide, Materials2CellShapes.shapeCell, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.SulfuricAcid, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(Materials.Water.getFluid(1_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        // C2H4 + 2Cl = C2H3Cl + HCl

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Chlorine, Materials2CellShapes.shapeCell, 2))
            .circuit(1)
            .itemOutputs(Materials.HydrochloricAcid.getCells(1), Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.VinylChloride, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Ethylene, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(Materials.HydrochloricAcid.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, 2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.VinylChloride, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Chlorine, Materials2CellShapes.shapeCell, 2))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.VinylChloride, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Ethylene, Materials2CellShapes.shapeCell, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.VinylChloride, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, 2_000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // C2H4O2 =H2SO4= C2H2O + H2O

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.AceticAcid, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Ethenone, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DilutedSulfuricAcid,
                    Materials2FluidShapes.shapeFluidLiquid,
                    1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.SulfuricAcid, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Ethenone, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DilutedSulfuricAcid,
                    Materials2FluidShapes.shapeFluidLiquid,
                    1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.AceticAcid, Materials2CellShapes.shapeCell, 1))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.DilutedSulfuricAcid, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethenone, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.SulfuricAcid, Materials2CellShapes.shapeCell, 1))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.DilutedSulfuricAcid, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethenone, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        // C2H2O + 8HNO3 = 2CN4O8 + 9H2O
        // Chemically this recipe is wrong, but kept for minimizing breaking change.

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Ethenone, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Tetranitromethane, Materials2CellShapes.shapeCell, 2))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricAcid, Materials2FluidShapes.shapeFluidLiquid, 8_000))
            .fluidOutputs(Materials.Water.getFluid(9_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Ethenone, Materials2CellShapes.shapeCell, 1))
            .circuit(12)
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricAcid, Materials2FluidShapes.shapeFluidLiquid, 8_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Tetranitromethane,
                    Materials2FluidShapes.shapeFluidLiquid,
                    2_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.NitricAcid, Materials2CellShapes.shapeCell, 8))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Tetranitromethane, Materials2CellShapes.shapeCell, 2),
                Materials.Empty.getCells(6))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethenone, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(Materials.Water.getFluid(9_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.NitricAcid, Materials2CellShapes.shapeCell, 8))
            .circuit(2)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Tetranitromethane, Materials2CellShapes.shapeCell, 2),
                Materials.Empty.getCells(6))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethenone, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.NitricAcid, Materials2CellShapes.shapeCell, 8))
            .circuit(12)
            .itemOutputs(Materials.Empty.getCells(8))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethenone, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Tetranitromethane,
                    Materials2FluidShapes.shapeFluidLiquid,
                    2_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.NitricAcid, Materials2CellShapes.shapeCell, 8),
                Materials.Empty.getCells(1))
            .itemOutputs(Materials.Water.getCells(9))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethenone, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Tetranitromethane,
                    Materials2FluidShapes.shapeFluidLiquid,
                    2_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Ethenone, Materials2CellShapes.shapeCell, 1),
                MaterialLibAPI.getStack(Materials2Materials.NitricAcid, Materials2CellShapes.shapeCell, 8))
            .itemOutputs(Materials.Water.getCells(9))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Tetranitromethane,
                    Materials2FluidShapes.shapeFluidLiquid,
                    2_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalReactorRecipes);

        // C3H6 + C2H4 = C5H8 + 2H

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Propene, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(1))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Isoprene, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Ethylene, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(1))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Propene, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Isoprene, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Propene, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Isoprene, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 2_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Ethylene, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Isoprene, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Propene, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 2_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(1))
            .circuit(5)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Methane, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Propene, Materials2FluidShapes.shapeFluidGas, 2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Isoprene, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Propene, Materials2CellShapes.shapeCell, 2))
            .circuit(5)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Methane, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(1))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Isoprene, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(1))
            .circuit(15)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Isoprene, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Propene, Materials2FluidShapes.shapeFluidGas, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Propene, Materials2CellShapes.shapeCell, 2))
            .circuit(15)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Isoprene, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(1))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Air.get(1))
            .circuit(1)
            .itemOutputs(Materials.RubberRaw.getDust(1), Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Isoprene, Materials2FluidShapes.shapeFluidLiquid, 144))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, 2))
            .circuit(1)
            .itemOutputs(Materials.RubberRaw.getDust(3), Materials.Empty.getCells(2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Isoprene, Materials2FluidShapes.shapeFluidLiquid, 288))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Isoprene, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(Materials.RubberRaw.getDust(7), Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Air, Materials2FluidShapes.shapeFluidGas, 14_000))
            .duration(56 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Isoprene, Materials2CellShapes.shapeCell, 2))
            .circuit(1)
            .itemOutputs(Materials.RubberRaw.getDust(21), Materials.Empty.getCells(2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 14_000))
            .duration(1 * MINUTES + 52 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Benzene, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Styrene, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 2_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Ethylene, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Styrene, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Benzene, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 2_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.RawStyreneButadieneRubber, Materials2Shapes.shapeDust, 9),
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.shapeDust, 1))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.StyreneButadieneRubber,
                    Materials2FluidShapes.shapeFluidMolten,
                    9 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // C6H6 + 4Cl = C6H4Cl2 + 2HCl

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Benzene, Materials2CellShapes.shapeCell, 1))
            .circuit(2)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Dichlorobenzene, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, 4_000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(2_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Chlorine, Materials2CellShapes.shapeCell, 4))
            .circuit(2)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Dichlorobenzene, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(3))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Benzene, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(2_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Chlorine, Materials2CellShapes.shapeCell, 4))
            .circuit(12)
            .itemOutputs(Materials.HydrochloricAcid.getCells(2), Materials.Empty.getCells(2))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Benzene, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Dichlorobenzene, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.SodiumSulfide, Materials2Shapes.shapeDust, 3),
                ItemList.Cell_Air.get(8))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Salt, Materials2Shapes.shapeDust, 4),
                Materials.Empty.getCells(8))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Dichlorobenzene, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.PolyphenyleneSulfide,
                    Materials2FluidShapes.shapeFluidMolten,
                    1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.SodiumSulfide, Materials2Shapes.shapeDust, 3),
                MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, 8))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Salt, Materials2Shapes.shapeDust, 4),
                Materials.Empty.getCells(8))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Dichlorobenzene, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.PolyphenyleneSulfide,
                    Materials2FluidShapes.shapeFluidMolten,
                    1_500))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // NaCl + H2SO4 = NaHSO4 + HCl

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Salt, Materials2Shapes.shapeDust, 2))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.SodiumBisulfate, Materials2Shapes.shapeDust, 7))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(1_000))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // NaOH + H2SO4 = NaHSO4 + H2O

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.SodiumHydroxide.getDust(3))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.SodiumBisulfate, Materials2Shapes.shapeDust, 7))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(Materials.Water.getFluid(1_000))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Biodiesel recipes

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.SodiumHydroxide.getDustTiny(1),
                MaterialLibAPI.getStack(Materials2Materials.Methanol, Materials2CellShapes.shapeCell, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Glycerol, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SeedOil, Materials2FluidShapes.shapeFluidLiquid, 6_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.BioDiesel, Materials2FluidShapes.shapeFluidLiquid, 6_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.SodiumHydroxide.getDustTiny(1),
                MaterialLibAPI.getStack(Materials2Materials.SeedOil, Materials2CellShapes.shapeCell, 6))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.BioDiesel, Materials2CellShapes.shapeCell, 6))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methanol, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Glycerol, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.SodiumHydroxide.getDustTiny(1),
                MaterialLibAPI.getStack(Materials2Materials.Methanol, Materials2CellShapes.shapeCell, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Glycerol, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.FishOil, Materials2FluidShapes.shapeFluidLiquid, 6_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.BioDiesel, Materials2FluidShapes.shapeFluidLiquid, 6_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.SodiumHydroxide.getDustTiny(1),
                MaterialLibAPI.getStack(Materials2Materials.FishOil, Materials2CellShapes.shapeCell, 6))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.BioDiesel, Materials2CellShapes.shapeCell, 6))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methanol, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Glycerol, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.SodiumHydroxide.getDustTiny(1),
                MaterialLibAPI.getStack(Materials2Materials.Ethanol, Materials2CellShapes.shapeCell, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Glycerol, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SeedOil, Materials2FluidShapes.shapeFluidLiquid, 6_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.BioDiesel, Materials2FluidShapes.shapeFluidLiquid, 6_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.SodiumHydroxide.getDustTiny(1),
                MaterialLibAPI.getStack(Materials2Materials.SeedOil, Materials2CellShapes.shapeCell, 6))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.BioDiesel, Materials2CellShapes.shapeCell, 6))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Ethanol, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Glycerol, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.SodiumHydroxide.getDustTiny(1),
                MaterialLibAPI.getStack(Materials2Materials.Ethanol, Materials2CellShapes.shapeCell, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Glycerol, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.FishOil, Materials2FluidShapes.shapeFluidLiquid, 6_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.BioDiesel, Materials2FluidShapes.shapeFluidLiquid, 6_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.SodiumHydroxide.getDustTiny(1),
                MaterialLibAPI.getStack(Materials2Materials.FishOil, Materials2CellShapes.shapeCell, 6))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.BioDiesel, Materials2CellShapes.shapeCell, 6))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Ethanol, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Glycerol, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.SodiumHydroxide.getDust(1),
                MaterialLibAPI.getStack(Materials2Materials.Methanol, Materials2CellShapes.shapeCell, 9))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Glycerol, Materials2CellShapes.shapeCell, 9))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SeedOil, Materials2FluidShapes.shapeFluidLiquid, 54_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.BioDiesel, Materials2FluidShapes.shapeFluidLiquid, 54_000))
            .duration(4 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.SodiumHydroxide.getDust(1),
                MaterialLibAPI.getStack(Materials2Materials.SeedOil, Materials2CellShapes.shapeCell, 54))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.BioDiesel, Materials2CellShapes.shapeCell, 54))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methanol, Materials2FluidShapes.shapeFluidLiquid, 9_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Glycerol, Materials2FluidShapes.shapeFluidLiquid, 9_000))
            .duration(4 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.SodiumHydroxide.getDust(1),
                MaterialLibAPI.getStack(Materials2Materials.Methanol, Materials2CellShapes.shapeCell, 9))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Glycerol, Materials2CellShapes.shapeCell, 9))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.FishOil, Materials2FluidShapes.shapeFluidLiquid, 54_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.BioDiesel, Materials2FluidShapes.shapeFluidLiquid, 54_000))
            .duration(4 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.SodiumHydroxide.getDust(1),
                MaterialLibAPI.getStack(Materials2Materials.FishOil, Materials2CellShapes.shapeCell, 54))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.BioDiesel, Materials2CellShapes.shapeCell, 54))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methanol, Materials2FluidShapes.shapeFluidLiquid, 9_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Glycerol, Materials2FluidShapes.shapeFluidLiquid, 9_000))
            .duration(4 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.SodiumHydroxide.getDust(1),
                MaterialLibAPI.getStack(Materials2Materials.Ethanol, Materials2CellShapes.shapeCell, 9))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Glycerol, Materials2CellShapes.shapeCell, 9))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SeedOil, Materials2FluidShapes.shapeFluidLiquid, 54_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.BioDiesel, Materials2FluidShapes.shapeFluidLiquid, 54_000))
            .duration(4 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.SodiumHydroxide.getDust(1),
                MaterialLibAPI.getStack(Materials2Materials.SeedOil, Materials2CellShapes.shapeCell, 54))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.BioDiesel, Materials2CellShapes.shapeCell, 54))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Ethanol, Materials2FluidShapes.shapeFluidLiquid, 9_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Glycerol, Materials2FluidShapes.shapeFluidLiquid, 9_000))
            .duration(4 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.SodiumHydroxide.getDust(1),
                MaterialLibAPI.getStack(Materials2Materials.Ethanol, Materials2CellShapes.shapeCell, 9))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Glycerol, Materials2CellShapes.shapeCell, 9))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.FishOil, Materials2FluidShapes.shapeFluidLiquid, 54_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.BioDiesel, Materials2FluidShapes.shapeFluidLiquid, 54_000))
            .duration(4 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.SodiumHydroxide.getDust(1),
                MaterialLibAPI.getStack(Materials2Materials.FishOil, Materials2CellShapes.shapeCell, 54))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.BioDiesel, Materials2CellShapes.shapeCell, 54))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Ethanol, Materials2FluidShapes.shapeFluidLiquid, 9_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Glycerol, Materials2FluidShapes.shapeFluidLiquid, 9_000))
            .duration(4 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // C3H8O3 + 3HNO3 =H2SO4= C3H5N3O9 + 3H2O

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Glycerol, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Glyceryl, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitrationMixture, Materials2FluidShapes.shapeFluidLiquid, 6_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DilutedSulfuricAcid,
                    Materials2FluidShapes.shapeFluidLiquid,
                    3_000))
            .duration(9 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.NitrationMixture, Materials2CellShapes.shapeCell, 6))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Glyceryl, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(5))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Glycerol, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DilutedSulfuricAcid,
                    Materials2FluidShapes.shapeFluidLiquid,
                    3_000))
            .duration(9 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.NitrationMixture, Materials2CellShapes.shapeCell, 6))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.DilutedSulfuricAcid, Materials2CellShapes.shapeCell, 3),
                Materials.Empty.getCells(3))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Glycerol, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Glyceryl, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(9 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // CaO + CO2 = CaCO3

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Quicklime, Materials2Shapes.shapeDust, 2))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Calcite, Materials2Shapes.shapeDust, 5))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Calcite, Materials2Shapes.shapeDust, 5))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Quicklime, Materials2Shapes.shapeDust, 2))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // MgO + CO2 = MgCO3

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Magnesia, Materials2Shapes.shapeDust, 2))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Magnesite, Materials2Shapes.shapeDust, 5))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Magnesite, Materials2Shapes.shapeDust, 5))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Magnesia, Materials2Shapes.shapeDust, 2))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // C6H6 + 2Cl = C6H5Cl + HCl

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Benzene, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Chlorobenzene, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, 2_000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Chlorine, Materials2CellShapes.shapeCell, 2))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Chlorobenzene, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Benzene, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Chlorine, Materials2CellShapes.shapeCell, 2))
            .circuit(11)
            .itemOutputs(Materials.HydrochloricAcid.getCells(1), Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Benzene, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Chlorobenzene, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // C6H5Cl + H2O = C6H6O + HCl

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Water.getCells(1))
            .circuit(1)
            .itemOutputs(Materials.DilutedHydrochloricAcid.getCells(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Chlorobenzene, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Phenol, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Chlorobenzene, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(Materials.DilutedHydrochloricAcid.getCells(1))
            .fluidInputs(Materials.Water.getFluid(1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Phenol, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Water.getCells(1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Phenol, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Chlorobenzene, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(Materials.DilutedHydrochloricAcid.getFluid(1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Chlorobenzene, Materials2CellShapes.shapeCell, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Phenol, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(Materials.Water.getFluid(1_000))
            .fluidOutputs(Materials.DilutedHydrochloricAcid.getFluid(1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // C6H5Cl + NaOH = C6H6O + NaCl

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.SodiumHydroxide.getDust(12))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Salt, Materials2Shapes.shapeDust, 8))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Chlorobenzene, Materials2FluidShapes.shapeFluidLiquid, 4_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Phenol, Materials2FluidShapes.shapeFluidLiquid, 4_000))
            .duration(48 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Oxide Recipe

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Antimony, Materials2Shapes.shapeDust, 2))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.AntimonyTrioxide, Materials2Shapes.shapeDust, 5))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 3_000))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Lead, Materials2Shapes.shapeDust, 1))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Massicot, Materials2Shapes.shapeDust, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Arsenic, Materials2Shapes.shapeDust, 2))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.ArsenicTrioxide, Materials2Shapes.shapeDust, 5))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 3_000))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Cobalt, Materials2Shapes.shapeDust, 1))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.CobaltOxide, Materials2Shapes.shapeDust, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Zinc, Materials2Shapes.shapeDust, 1))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Zincite, Materials2Shapes.shapeDust, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // CaSi2 + 2HCl = 2Si + CaCl2 + 2H

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.CalciumDisilicide, Materials2Shapes.shapeDust, 3))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Silicon, Materials2Shapes.shapeDust, 2),
                new ItemStack(WerkstoffLoader.items.get(OrePrefixes.dust), 3, 63))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 2_000))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // SiCl4 + 2Zn = 2ZnCl2 + Si

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Zinc, Materials2Shapes.shapeDust, 2))
            .circuit(1)
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.SiliconSG, 1),
                new ItemStack(WerkstoffLoader.items.get(OrePrefixes.dust), 6, 10052))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SiliconTetrachloride,
                    Materials2FluidShapes.shapeFluidLiquid,
                    1_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // C4H8O + 2H =Pd= C4H10O

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Butyraldehyde, Materials2CellShapes.shapeCell, 1),
                MaterialLibAPI.getStack(Materials2Materials.Palladium, Materials2Shapes.shapeDustTiny, 1))
            .itemOutputs(ItemList.Cell_Empty.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 2_000))
            .fluidOutputs(new FluidStack(FluidRegistry.getFluid("butanol"), 1000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // 4CH2O + C2H4O =NaOH= C5H12O4 + CO

        GTValues.RA.stdBuilder()
            .itemInputs( // very poor way of looking for it, but getModItem on GT++ within GT5U jar
                         // is prohibited now,
                // and i don't feel like reworking GT++ cell registration for now
                GameRegistry.findItemStack(GTPlusPlus.ID, "Formaldehyde", 4),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.SodiumHydroxide, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Pentaerythritol, Materials2Shapes.shapeDust, 21),
                Materials.Empty.getCells(4))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Acetaldehyde, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonMonoxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // 4CH2O + C2H4O =NaOH= C5H12O4 + CO

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Acetaldehyde, Materials2CellShapes.shapeCell, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.SodiumHydroxide, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Pentaerythritol, Materials2Shapes.shapeDust, 21),
                Materials.Empty.getCells(1))
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("fluid.formaldehyde"), 4_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonMonoxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);

        // CaC2 + 2H2O = Ca(OH)2 + C2H2

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.CalciumCarbide, 3))
            .circuit(1)
            .itemOutputs(GregtechItemList.CalciumHydroxideDust.get(5))
            .fluidInputs(Materials.Water.getFluid(2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Acetylene, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // Co(NO3)2 + 2NaOH = Co(OH)2 + 2NaNO3

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.CobaltIINitrate.getDust(9),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.SodiumHydroxide, 6))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.CobaltIIHydroxide, Materials2Shapes.shapeDust, 5),
                SODIUM_NITRATE.getDust(10))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        if (Forestry.isModLoaded()) {

            // Americium comb processing

            GTValues.RA.stdBuilder()
                .itemInputs(GTBees.combs.getStackForType(CombType.AMERICIUM, 4))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials2Materials.Americium, Materials2Shapes.shapeCrushedPurified, 1))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Helium, Materials2FluidShapes.shapeFluidPlasma, 8_175))
                .duration(30 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(UniversalChemical);

            GTValues.RA.stdBuilder()
                .itemInputs(GTBees.combs.getStackForType(CombType.AMERICIUM, 4))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials2Materials.Americium, Materials2Shapes.shapeCrushedPurified, 2))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Nitrogen, Materials2FluidShapes.shapeFluidPlasma, 1_211))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_UV)
                .addTo(UniversalChemical);

            GTValues.RA.stdBuilder()
                .itemInputs(GTBees.combs.getStackForType(CombType.AMERICIUM, 4))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials2Materials.Americium, Materials2Shapes.shapeCrushedPurified, 4))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Silver, Materials2FluidShapes.shapeFluidPlasma, 310))
                .duration(7 * SECONDS + 10 * TICKS)
                .eut(TierEU.RECIPE_UHV)
                .addTo(UniversalChemical);

            GTValues.RA.stdBuilder()
                .itemInputs(GTBees.combs.getStackForType(CombType.AMERICIUM, 4))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials2Materials.Americium, Materials2Shapes.shapeCrushedPurified, 8))
                .fluidInputs(new FluidStack(MaterialsElements.getInstance().BROMINE.getPlasma(), 29))
                .duration(3 * SECONDS + 15 * TICKS)
                .eut(TierEU.RECIPE_UEV)
                .addTo(UniversalChemical);

            GTValues.RA.stdBuilder()
                .itemInputs(GTBees.combs.getStackForType(CombType.AMERICIUM, 4))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials2Materials.Americium, Materials2Shapes.shapeCrushedPurified, 16))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Thorium, Materials2FluidShapes.shapeFluidPlasma, 68))
                .duration(1 * SECONDS + 17 * TICKS)
                .eut(TierEU.RECIPE_UIV)
                .addTo(UniversalChemical);

            // Europium comb processing

            GTValues.RA.stdBuilder()
                .itemInputs(GTBees.combs.getStackForType(CombType.EUROPIUM, 4))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials2Materials.Europium, Materials2Shapes.shapeCrushedPurified, 1))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Helium, Materials2FluidShapes.shapeFluidPlasma, 606))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(UniversalChemical);

            GTValues.RA.stdBuilder()
                .itemInputs(GTBees.combs.getStackForType(CombType.EUROPIUM, 4))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials2Materials.Europium, Materials2Shapes.shapeCrushedPurified, 2))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Nitrogen, Materials2FluidShapes.shapeFluidPlasma, 180))
                .duration(7 * SECONDS + 10 * TICKS)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(UniversalChemical);

            GTValues.RA.stdBuilder()
                .itemInputs(GTBees.combs.getStackForType(CombType.EUROPIUM, 4))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials2Materials.Europium, Materials2Shapes.shapeCrushedPurified, 4))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Silver, Materials2FluidShapes.shapeFluidPlasma, 54))
                .duration(3 * SECONDS + 15 * TICKS)
                .eut(TierEU.RECIPE_UV)
                .addTo(UniversalChemical);

            GTValues.RA.stdBuilder()
                .itemInputs(GTBees.combs.getStackForType(CombType.EUROPIUM, 4))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials2Materials.Europium, Materials2Shapes.shapeCrushedPurified, 8))
                .fluidInputs(new FluidStack(MaterialsElements.getInstance().BROMINE.getPlasma(), 6))
                .duration(1 * SECONDS + 17 * TICKS)
                .eut(TierEU.RECIPE_UHV)
                .addTo(UniversalChemical);

            GTValues.RA.stdBuilder()
                .itemInputs(GTBees.combs.getStackForType(CombType.EUROPIUM, 4))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials2Materials.Europium, Materials2Shapes.shapeCrushedPurified, 16))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Thorium, Materials2FluidShapes.shapeFluidPlasma, 18))
                .duration(18 * TICKS)
                .eut(TierEU.RECIPE_UEV)
                .addTo(UniversalChemical);
        }
    }

    public void addDefaultPolymerizationRecipes(Fluid aBasicMaterial, Fluid aPolymer) {
        // Oxygen/Titaniumtetrafluoride -> +50% Output each
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Air.get(1))
            .circuit(1)
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(new FluidStack(aBasicMaterial, 1 * INGOTS))
            .fluidOutputs(new FluidStack(aPolymer, 1 * INGOTS))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(new FluidStack(aBasicMaterial, 1 * INGOTS))
            .fluidOutputs(new FluidStack(aPolymer, 3 * HALF_INGOTS))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Air.get(8))
            .circuit(9)
            .itemOutputs(Materials.Empty.getCells(8))
            .fluidInputs(new FluidStack(aBasicMaterial, 8 * INGOTS))
            .fluidOutputs(new FluidStack(aPolymer, 8 * INGOTS))
            .duration(56 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, 8))
            .circuit(9)
            .itemOutputs(Materials.Empty.getCells(8))
            .fluidInputs(new FluidStack(aBasicMaterial, 8 * INGOTS))
            .fluidOutputs(new FluidStack(aPolymer, 12 * INGOTS))
            .duration(56 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(
                new FluidStack(aBasicMaterial, 15 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Air, Materials2FluidShapes.shapeFluidGas, 7_500),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Titaniumtetrachloride,
                    Materials2FluidShapes.shapeFluidLiquid,
                    100))
            .fluidOutputs(new FluidStack(aPolymer, 3_240))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(
                new FluidStack(aBasicMaterial, 15 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 7_500),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Titaniumtetrachloride,
                    Materials2FluidShapes.shapeFluidLiquid,
                    100))
            .fluidOutputs(new FluidStack(aPolymer, 4_320))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

    }

    public void polymerizationRecipes() {
        addDefaultPolymerizationRecipes(Materials.VinylAcetate.mFluid, Materials.PolyvinylAcetate.mFluid);

        addDefaultPolymerizationRecipes(Materials.Ethylene.mGas, Materials.Polyethylene.mStandardMoltenFluid);

        addDefaultPolymerizationRecipes(
            Materials.Tetrafluoroethylene.mGas,
            Materials.Polytetrafluoroethylene.mStandardMoltenFluid);

        addDefaultPolymerizationRecipes(Materials.VinylChloride.mGas, Materials.PolyvinylChloride.mStandardMoltenFluid);

        addDefaultPolymerizationRecipes(Materials.Styrene.mFluid, Materials.Polystyrene.mStandardMoltenFluid);
    }

    public void singleBlockOnly() {

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.GasolineRaw.getCells(10),
                MaterialLibAPI.getStack(Materials2Materials.Toluene, Materials2CellShapes.shapeCell, 1))
            .itemOutputs(Materials.GasolineRegular.getCells(11))
            .duration(10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Benzene, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(1))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Styrene, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Ethylene, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(1))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, 2))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Benzene, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Styrene, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Methane, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(2))
            .itemOutputs(Materials.HydrochloricAcid.getCells(3))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, 6_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Chloroform, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Silicon, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Chloromethane, Materials2CellShapes.shapeCell, 2))
            .itemOutputs(Materials.Empty.getCells(2))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Dimethyldichlorosilane,
                    Materials2FluidShapes.shapeFluidLiquid,
                    1_000))
            .duration(12 * SECONDS)
            .eut(96)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Dimethyldichlorosilane, Materials2CellShapes.shapeCell, 1),
                Materials.Water.getCells(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Polydimethylsiloxane, Materials2Shapes.shapeDust, 3),
                Materials.Empty.getCells(2))
            .fluidOutputs(Materials.DilutedHydrochloricAcid.getFluid(1_000))
            .duration(12 * SECONDS)
            .eut(96)
            .addTo(chemicalReactorRecipes);

        // Ca5(PO4)3Cl + 5H2SO4 + 10H2O = 5CaSO4(H2O)2 + HCl + 3H3PO4

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Apatite, Materials2Shapes.shapeDust, 9),
                MaterialLibAPI.getStack(Materials2Materials.SulfuricAcid, Materials2CellShapes.shapeCell, 5))
            .itemOutputs(Materials.HydrochloricAcid.getCells(1), Materials.Empty.getCells(4))
            .fluidInputs(Materials.Water.getFluid(10_000))
            .fluidOutputs(Materials.PhosphoricAcid.getFluid(3_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // 10O + 4P = P4O10

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Phosphorus, Materials2Shapes.shapeDust, 4))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.PhosphorousPentoxide, Materials2Shapes.shapeDust, 14))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 10_000))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // HCl + C3H8O3 = C3H5ClO + 2H2O

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.HydrochloricAcid.getCells(1),
                MaterialLibAPI.getStack(Materials2Materials.Glycerol, Materials2CellShapes.shapeCell, 1))
            .itemOutputs(Materials.Water.getCells(2))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Epichlorohydrin, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // H2O + Cl =Hg= HClO + H

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Chlorine, Materials2CellShapes.shapeCell, 10),
                MaterialLibAPI.getStack(Materials2Materials.Mercury, Materials2CellShapes.shapeCell, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, 10),
                Materials.Empty.getCells(1))
            .fluidInputs(Materials.Water.getFluid(10_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.HypochlorousAcid,
                    Materials2FluidShapes.shapeFluidLiquid,
                    10_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.Water.getCells(10),
                MaterialLibAPI.getStack(Materials2Materials.Mercury, Materials2CellShapes.shapeCell, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, 10),
                Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, 10_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.HypochlorousAcid,
                    Materials2FluidShapes.shapeFluidLiquid,
                    10_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Chlorine, Materials2CellShapes.shapeCell, 1),
                Materials.Water.getCells(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Mercury, Materials2FluidShapes.shapeFluidLiquid, 100))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HypochlorousAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(chemicalReactorRecipes);

        // P + 3Cl = PCl3

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Phosphorus, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Chlorine, Materials2CellShapes.shapeCell, 3))
            .itemOutputs(ItemList.Cell_Empty.get(3))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.PhosphorusTrichloride,
                    Materials2FluidShapes.shapeFluidLiquid,
                    1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.EthyleneOxide, Materials2CellShapes.shapeCell, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 5))
            .itemOutputs(ItemList.Cell_Empty.get(6))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Dimethyldichlorosilane,
                    Materials2FluidShapes.shapeFluidLiquid,
                    4_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SiliconOil, Materials2FluidShapes.shapeFluidLiquid, 5_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.EthyleneOxide, Materials2CellShapes.shapeCell, 1),
                MaterialLibAPI.getStack(Materials2Materials.Dimethyldichlorosilane, Materials2CellShapes.shapeCell, 4))
            .itemOutputs(ItemList.Cell_Empty.get(5))
            .fluidInputs(Materials.Water.getFluid(5_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SiliconOil, Materials2FluidShapes.shapeFluidLiquid, 5_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1))
            .circuit(2)
            .itemOutputs(ItemList.Cell_Empty.get(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.EthyleneOxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(Materials.Ethyleneglycol.getFluid(1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.CobaltIIHydroxide, Materials2Shapes.shapeDust, 5),
                MaterialLibAPI.getStack(Materials2Materials.NaphthenicAcid, Materials2CellShapes.shapeCell, 1))
            .itemOutputs(Materials.CobaltIINaphthenate.getDust(41), ItemList.Cell_Empty.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.CobaltIIAcetate.getDust(15),
                MaterialLibAPI.getStack(Materials2Materials.NaphthenicAcid, Materials2CellShapes.shapeCell, 1))
            .itemOutputs(Materials.CobaltIINaphthenate.getDust(41), ItemList.Cell_Empty.get(1))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.shapeFluidLiquid, 1_500))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Silicon, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.Chlorine, Materials2CellShapes.shapeCell, 4))
            .itemOutputs(ItemList.Cell_Empty.get(4))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SiliconTetrachloride,
                    Materials2FluidShapes.shapeFluidLiquid,
                    1_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Galena, Materials2Shapes.shapeCrushedPurified, 3),
                MaterialLibAPI.getStack(Materials2Materials.Sphalerite, Materials2Shapes.shapeCrushedPurified, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.shapeFluidLiquid, 4_000))
            .fluidOutputs(new FluidStack(ItemList.sIndiumConcentrate, 8_000))
            .duration(3 * SECONDS)
            .eut(150)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.shapeDust, 1),
                Materials.Empty.getCells(1))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Methane, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 4_000))
            .duration(100 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // O + 2H = H2O

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, 1))
            .circuit(22)
            .itemOutputs(ItemList.Cell_Empty.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 2_000))
            .fluidOutputs(GTModHandler.getDistilledWater(1_000))
            .duration(10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, 1))
            .circuit(22)
            .itemOutputs(ItemList.Cell_Empty.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 500))
            .fluidOutputs(GTModHandler.getDistilledWater(500))
            .duration(5 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // Si + 4Cl = SiCl4

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Silicon, Materials2Shapes.shapeDust, 1),
                ItemList.Cell_Empty.get(2))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, 2))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(3_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Trichlorosilane, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Silane, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.SiliconSG, 1), ItemList.Cell_Empty.get(1))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 4_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Silane, Materials2CellShapes.shapeCell, 1),
                ItemList.Cell_Empty.get(3))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.SiliconSG, 1),
                MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, 4))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // S + 2Cl = SCl2

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.shapeDust, 8),
                MaterialLibAPI.getStack(Materials2Materials.Chlorine, Materials2CellShapes.shapeCell, 16))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.SulfurDichloride, Materials2CellShapes.shapeCell, 8),
                ItemList.Cell_Empty.get(8))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.shapeDust, 8),
                ItemList.Cell_Empty.get(8))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.SulfurDichloride, Materials2CellShapes.shapeCell, 8))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, 16_000))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // C6H6 + C3H6 = C9H12

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Propene, Materials2CellShapes.shapeCell, 8),
                Materials.PhosphoricAcid.getCells(1))
            .itemOutputs(Materials.Empty.getCells(9))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Benzene, Materials2FluidShapes.shapeFluidLiquid, 8_000))
            .fluidOutputs(Materials.Cumene.getFluid(8_000))
            .duration(1 * MINUTES + 36 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.PhosphoricAcid.getCells(1),
                MaterialLibAPI.getStack(Materials2Materials.Benzene, Materials2CellShapes.shapeCell, 8))
            .itemOutputs(Materials.Empty.getCells(9))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Propene, Materials2FluidShapes.shapeFluidGas, 8_000))
            .fluidOutputs(Materials.Cumene.getFluid(8_000))
            .duration(1 * MINUTES + 36 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Benzene, Materials2CellShapes.shapeCell, 1),
                MaterialLibAPI.getStack(Materials2Materials.Propene, Materials2CellShapes.shapeCell, 1))
            .itemOutputs(Materials.Empty.getCells(2))
            .fluidInputs(Materials.PhosphoricAcid.getFluid(125))
            .fluidOutputs(Materials.Cumene.getFluid(1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // C3H6O + 2C6H6O =HCl= C15H16O2 + H2O

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Acetone, Materials2CellShapes.shapeCell, 1),
                MaterialLibAPI.getStack(Materials2Materials.Phenol, Materials2CellShapes.shapeCell, 2))
            .itemOutputs(Materials.Water.getCells(1), Materials.Empty.getCells(2))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.BisphenolA, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.HydrochloricAcid.getCells(1),
                MaterialLibAPI.getStack(Materials2Materials.Acetone, Materials2CellShapes.shapeCell, 1))
            .itemOutputs(Materials.Water.getCells(1), Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Phenol, Materials2FluidShapes.shapeFluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.BisphenolA, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Phenol, Materials2CellShapes.shapeCell, 2),
                Materials.HydrochloricAcid.getCells(1))
            .itemOutputs(Materials.Water.getCells(1), Materials.Empty.getCells(2))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Acetone, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.BisphenolA, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // N + 3H = NH3

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Nitrogen, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 3_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ammonia, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_HV * 4 / 5)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, 3))
            .circuit(1)
            .itemOutputs(Materials.Empty.getCells(3))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Nitrogen, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ammonia, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_HV * 4 / 5)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Nitrogen, Materials2CellShapes.shapeCell, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Ammonia, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 3_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_HV * 4 / 5)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.shapeCell, 3))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Ammonia, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Nitrogen, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_HV * 4 / 5)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Ammonia, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(1))
            .itemOutputs(Materials.Water.getCells(2))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methanol, Materials2FluidShapes.shapeFluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Dimethylamine, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Ammonia, Materials2CellShapes.shapeCell, 4),
                Materials.Empty.getCells(2))
            .itemOutputs(Materials.Water.getCells(6))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 10_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricOxide, Materials2FluidShapes.shapeFluidGas, 4_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Water.getCells(1), Materials.Empty.getCells(1))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.NitricAcid, Materials2CellShapes.shapeCell, 2))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitrogenDioxide, Materials2FluidShapes.shapeFluidGas, 3_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricOxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // 2NO2 + O + H2O = 2HNO3

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.NitrogenDioxide, Materials2CellShapes.shapeCell, 2),
                MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, 1))
            .itemOutputs(Materials.Empty.getCells(3))
            .fluidInputs(Materials.Water.getFluid(1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricAcid, Materials2FluidShapes.shapeFluidLiquid, 2_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, 1),
                Materials.Water.getCells(1))
            .itemOutputs(Materials.Empty.getCells(2))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitrogenDioxide, Materials2FluidShapes.shapeFluidGas, 2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricAcid, Materials2FluidShapes.shapeFluidLiquid, 2_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.Water.getCells(1),
                MaterialLibAPI.getStack(Materials2Materials.NitrogenDioxide, Materials2CellShapes.shapeCell, 2))
            .itemOutputs(Materials.Empty.getCells(3))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricAcid, Materials2FluidShapes.shapeFluidLiquid, 2_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.shapeDust, 1),
                Materials.Empty.getCells(1))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.HydricSulfide, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 2_000))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(chemicalReactorRecipes);

        // C2H4 + HCl + O = C2H3Cl + H2O

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Ethylene, Materials2CellShapes.shapeCell, 1),
                Materials.HydrochloricAcid.getCells(1))
            .itemOutputs(Materials.Water.getCells(1), Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.VinylChloride, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.HydrochloricAcid.getCells(1),
                MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, 1))
            .itemOutputs(Materials.Water.getCells(1), Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.VinylChloride, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, 1),
                MaterialLibAPI.getStack(Materials2Materials.Ethylene, Materials2CellShapes.shapeCell, 1))
            .itemOutputs(Materials.Water.getCells(1), Materials.Empty.getCells(1))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.VinylChloride, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Butadiene, Materials2CellShapes.shapeCell, 1),
                ItemList.Cell_Air.get(5))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.RawStyreneButadieneRubber, Materials2Shapes.shapeDust, 9),
                Materials.Empty.getCells(6))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Styrene, Materials2FluidShapes.shapeFluidLiquid, 350))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Butadiene, Materials2CellShapes.shapeCell, 1),
                MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, 5))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.RawStyreneButadieneRubber, Materials2Shapes.shapeDust, 13),
                Materials.Empty.getCells(6))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Styrene, Materials2FluidShapes.shapeFluidLiquid, 350))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Styrene, Materials2CellShapes.shapeCell, 1),
                ItemList.Cell_Air.get(15))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.RawStyreneButadieneRubber, Materials2Shapes.shapeDust, 27),
                Materials.Empty.getCells(16))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Butadiene, Materials2FluidShapes.shapeFluidGas, 3_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Styrene, Materials2CellShapes.shapeCell, 1),
                MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, 15))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.RawStyreneButadieneRubber, Materials2Shapes.shapeDust, 41),
                Materials.Empty.getCells(16))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Butadiene, Materials2FluidShapes.shapeFluidGas, 3_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Styrene, Materials2CellShapes.shapeCell, 1),
                MaterialLibAPI.getStack(Materials2Materials.Butadiene, Materials2CellShapes.shapeCell, 3))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.RawStyreneButadieneRubber, Materials2Shapes.shapeDust, 27),
                Materials.Empty.getCells(4))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Air, Materials2FluidShapes.shapeFluidGas, 15_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Styrene, Materials2CellShapes.shapeCell, 1),
                MaterialLibAPI.getStack(Materials2Materials.Butadiene, Materials2CellShapes.shapeCell, 3))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.RawStyreneButadieneRubber, Materials2Shapes.shapeDust, 41),
                Materials.Empty.getCells(4))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 15_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Benzene, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(1))
            .itemOutputs(Materials.HydrochloricAcid.getCells(2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, 4_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Dichlorobenzene, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Glycerol, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(2))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.DilutedSulfuricAcid, Materials2CellShapes.shapeCell, 3))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitrationMixture, Materials2FluidShapes.shapeFluidLiquid, 6_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Glyceryl, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(9 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.SodiumHydroxide.getDust(12), Materials.Empty.getCells(4))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Salt, Materials2Shapes.shapeDust, 8),
                MaterialLibAPI.getStack(Materials2Materials.Phenol, Materials2CellShapes.shapeCell, 4))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Chlorobenzene, Materials2FluidShapes.shapeFluidLiquid, 4_000))
            .duration(48 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.SodiumHydroxide.getDust(12),
                MaterialLibAPI.getStack(Materials2Materials.Chlorobenzene, Materials2CellShapes.shapeCell, 4))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Salt, Materials2Shapes.shapeDust, 8),
                MaterialLibAPI.getStack(Materials2Materials.Phenol, Materials2CellShapes.shapeCell, 4))
            .duration(48 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // Recipes for gasoline
        // 2N + O = N2O

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Nitrogen, Materials2CellShapes.shapeCell, 2),
                MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.NitrousOxide, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(2))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Nitrogen, Materials2CellShapes.shapeCell, 2))
            .circuit(1)
            .itemOutputs(Materials.Empty.getCells(2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitrousOxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Nitrogen, Materials2CellShapes.shapeCell, 2))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.NitrousOxide, Materials2CellShapes.shapeCell, 1),
                Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, 1))
            .circuit(1)
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Nitrogen, Materials2FluidShapes.shapeFluidGas, 2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitrousOxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Oxygen, Materials2CellShapes.shapeCell, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.NitrousOxide, Materials2CellShapes.shapeCell, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Nitrogen, Materials2FluidShapes.shapeFluidGas, 2_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // C2H6O + C4H8 = C6H14O

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Ethanol, Materials2CellShapes.shapeCell, 1),
                MaterialLibAPI.getStack(Materials2Materials.Butene, Materials2CellShapes.shapeCell, 1))
            .itemOutputs(Materials.AntiKnock.getCells(1), Materials.Empty.getCells(1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);

        // Potassium Dichromate
        // 2KNO3 + 2CrO3 = K2Cr2O7 + 2NO + 3O

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Saltpeter, Materials2Shapes.shapeDust, 10),
                Materials.ChromiumTrioxide.getDust(8))
            .itemOutputs(Materials.Potassiumdichromate.getDust(11))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricOxide, Materials2FluidShapes.shapeFluidGas, 2_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.PotassiumNitrade.getDust(10), Materials.ChromiumTrioxide.getDust(8))
            .itemOutputs(Materials.Potassiumdichromate.getDust(11))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricOxide, Materials2FluidShapes.shapeFluidGas, 2_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);
    }

    public void multiblockOnly() {

        GTValues.RA.stdBuilder()
            .circuit(22)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 16_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 8_000))
            .fluidOutputs(GTModHandler.getDistilledWater(8_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.PotassiumNitrade.getDust(10), Materials.ChromiumTrioxide.getDust(8))
            .itemOutputs(Materials.Potassiumdichromate.getDust(11))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricOxide, Materials2FluidShapes.shapeFluidGas, 2_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 3_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Saltpeter, Materials2Shapes.shapeDust, 10),
                Materials.ChromiumTrioxide.getDust(8))
            .itemOutputs(Materials.Potassiumdichromate.getDust(11))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricOxide, Materials2FluidShapes.shapeFluidGas, 2_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 3_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        // Potassium Dichromate shortcut
        // 2 Cr + 6O + 10 Saltpeter/Potassium Dichromate = 10 K2Cr2O7 + 2NO + 3O

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.PotassiumNitrade.getDust(64),
                Materials.PotassiumNitrade.getDust(64),
                Materials.PotassiumNitrade.getDust(32),
                MaterialLibAPI.getStack(Materials2Materials.Chrome, Materials2Shapes.shapeDust, 2 * 16))
            .circuit(11)
            .itemOutputs(
                Materials.Potassiumdichromate.getDust(64),
                Materials.Potassiumdichromate.getDust(64),
                Materials.Potassiumdichromate.getDust(48))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 96_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricOxide, Materials2FluidShapes.shapeFluidGas, 32_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 48_000))
            .duration(2 * MINUTES + 8 * SECONDS)
            .eut((int) GTValues.VP[7])
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Saltpeter, Materials2Shapes.shapeDust, 64),
                MaterialLibAPI.getStack(Materials2Materials.Saltpeter, Materials2Shapes.shapeDust, 64),
                MaterialLibAPI.getStack(Materials2Materials.Saltpeter, Materials2Shapes.shapeDust, 32),
                MaterialLibAPI.getStack(Materials2Materials.Chrome, Materials2Shapes.shapeDust, 2 * 16))
            .circuit(11)
            .itemOutputs(
                Materials.Potassiumdichromate.getDust(64),
                Materials.Potassiumdichromate.getDust(64),
                Materials.Potassiumdichromate.getDust(48))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 96_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricOxide, Materials2FluidShapes.shapeFluidGas, 32_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 48_000))
            .duration(2 * MINUTES + 8 * SECONDS)
            .eut((int) GTValues.VP[7])
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Benzene, Materials2FluidShapes.shapeFluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.shapeFluidGas, 2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Dimethylbenzene, Materials2FluidShapes.shapeFluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 4_000))
            .duration(3 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Galena, Materials2Shapes.shapeCrushedPurified, 3),
                MaterialLibAPI.getStack(Materials2Materials.Sphalerite, Materials2Shapes.shapeCrushedPurified, 1))
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.shapeFluidLiquid, 4_000))
            .fluidOutputs(new FluidStack(ItemList.sIndiumConcentrate, 8_000))
            .duration(3 * SECONDS)
            .eut(150)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Galena, Materials2Shapes.shapeCrushedPurified, 27),
                MaterialLibAPI.getStack(Materials2Materials.Sphalerite, Materials2Shapes.shapeCrushedPurified, 9))
            .circuit(8)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.shapeFluidLiquid, 36_000))
            .fluidOutputs(new FluidStack(ItemList.sIndiumConcentrate, 72_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Pentlandite, Materials2Shapes.shapeCrushedPurified, 9))
            .circuit(9)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.PlatinumGroupSludge, Materials2Shapes.shapeDust, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.shapeFluidLiquid, 9_000))
            .fluidOutputs(new FluidStack(ItemList.sNickelSulfate, 18_000))
            .duration(1 * SECONDS + 5 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Chalcopyrite, Materials2Shapes.shapeCrushedPurified, 9))
            .circuit(9)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.PlatinumGroupSludge, Materials2Shapes.shapeDust, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.shapeFluidLiquid, 9_000))
            .fluidOutputs(new FluidStack(ItemList.sBlueVitriol, 18_000))
            .duration(1 * SECONDS + 5 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Plutonium, Materials2Shapes.shapeIngot, 64),
                MaterialLibAPI.getStack(Materials2Materials.Uranium, Materials2Shapes.shapeDust, 1))
            .circuit(8)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Plutonium, Materials2Shapes.shapeDust, 64))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Air, Materials2FluidShapes.shapeFluidGas, 8_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Radon, Materials2FluidShapes.shapeFluidGas, 800))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        // 3SiO2 + 4Al = 3Si + 2Al2O3

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.shapeDust, 9),
                MaterialLibAPI.getStack(Materials2Materials.Aluminium, Materials2Shapes.shapeDust, 4))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Silicon, Materials2Shapes.shapeDust, 3),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Aluminiumoxide, 10))
            .duration(10 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);

        // 10Si + 30HCl -> 0.3 SiH2Cl2 + 9 HSiCl3 + 0.3 SiCl4 + 0.2 Si2Cl6 + 20.4H

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Silicon, Materials2Shapes.shapeDust, 10))
            .circuit(9)
            .fluidInputs(Materials.HydrochloricAcid.getFluid(30_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Trichlorosilane, Materials2FluidShapes.shapeFluidLiquid, 9_000),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SiliconTetrachloride,
                    Materials2FluidShapes.shapeFluidLiquid,
                    300),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Hexachlorodisilane, Materials2FluidShapes.shapeFluidLiquid, 200),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Dichlorosilane, Materials2FluidShapes.shapeFluidGas, 300),
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 20_400))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        // 2CO + 2C3H6 + 4H =RhHCO(P(C6H5)3)3= C4H8O + C4H8O

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.OrganorhodiumCatalyst, Materials2Shapes.shapeDustTiny, 1))
            .circuit(4)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 4_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Propene, Materials2FluidShapes.shapeFluidGas, 2_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonMonoxide, Materials2FluidShapes.shapeFluidGas, 2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Butyraldehyde, Materials2FluidShapes.shapeFluidLiquid, 1_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Isobutyraldehyde, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.OrganorhodiumCatalyst, Materials2Shapes.shapeDust, 1))
            .circuit(9)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 36_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Propene, Materials2FluidShapes.shapeFluidGas, 18_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonMonoxide, Materials2FluidShapes.shapeFluidGas, 18_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Butyraldehyde, Materials2FluidShapes.shapeFluidLiquid, 9_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Isobutyraldehyde, Materials2FluidShapes.shapeFluidLiquid, 9_000))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        // C2H4 + O =Al2O3,Ag= C2H4O

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Silver, Materials2Shapes.shapeDust, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Aluminiumoxide, 1))
            .circuit(2)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.shapeFluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.EthyleneOxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Silver, Materials2Shapes.shapeDust, 9),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Aluminiumoxide, 9))
            .circuit(7)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.shapeFluidGas, 9_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 9_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.EthyleneOxide, Materials2FluidShapes.shapeFluidGas, 9_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.EthyleneOxide, Materials2FluidShapes.shapeFluidGas, 1_000),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Dimethyldichlorosilane,
                    Materials2FluidShapes.shapeFluidLiquid,
                    4_000),
                Materials.Water.getFluid(5_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SiliconOil, Materials2FluidShapes.shapeFluidLiquid, 5_000))
            .duration(15 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(8)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.EthyleneOxide, Materials2FluidShapes.shapeFluidGas, 9_000),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Dimethyldichlorosilane,
                    Materials2FluidShapes.shapeFluidLiquid,
                    36_000),
                Materials.Water.getFluid(45_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SiliconOil, Materials2FluidShapes.shapeFluidLiquid, 45_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // NH3 + CH4O =SiO2,Al2O3= CH5N + H2O

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Aluminiumoxide, 1),
                MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.shapeDust, 1))
            .circuit(10)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methanol, Materials2FluidShapes.shapeFluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ammonia, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methylamine, Materials2FluidShapes.shapeFluidGas, 1_000),
                Materials.Water.getFluid(1_000))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.KevlarCatalyst, 1),
                MaterialLibAPI.getStack(Materials2Materials.Pentaerythritol, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.DiphenylmethaneDiisocyanate, Materials2Shapes.shapeDust, 5))
            .circuit(1)
            .fluidInputs(
                Materials.Ethyleneglycol.getFluid(4_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SiliconOil, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.PolyurethaneResin,
                    Materials2FluidShapes.shapeFluidLiquid,
                    1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.KevlarCatalyst, 9),
                MaterialLibAPI.getStack(Materials2Materials.Pentaerythritol, Materials2Shapes.shapeDust, 9),
                MaterialLibAPI
                    .getStack(Materials2Materials.DiphenylmethaneDiisocyanate, Materials2Shapes.shapeDust, 45))
            .circuit(9)
            .fluidInputs(
                Materials.Ethyleneglycol.getFluid(36_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SiliconOil, Materials2FluidShapes.shapeFluidLiquid, 9_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.PolyurethaneResin,
                    Materials2FluidShapes.shapeFluidLiquid,
                    9_000))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(multiblockChemicalReactorRecipes);

        // 3NH3 + 6CH4O =Al2O3,SiO2= CH5N + C2H7N + C3H9N + 6H2O

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Aluminiumoxide, 1),
                MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.shapeDust, 1))
            .circuit(3)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methanol, Materials2FluidShapes.shapeFluidLiquid, 6_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ammonia, Materials2FluidShapes.shapeFluidGas, 3_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methylamine, Materials2FluidShapes.shapeFluidGas, 1_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Dimethylamine, Materials2FluidShapes.shapeFluidGas, 1_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Trimethylamine, Materials2FluidShapes.shapeFluidGas, 1_000),
                Materials.Water.getFluid(6_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Aluminiumoxide, 9),
                MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.shapeDust, 9))
            .circuit(11)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methanol, Materials2FluidShapes.shapeFluidLiquid, 54_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ammonia, Materials2FluidShapes.shapeFluidGas, 27_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methylamine, Materials2FluidShapes.shapeFluidGas, 9_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Dimethylamine, Materials2FluidShapes.shapeFluidGas, 9_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Trimethylamine, Materials2FluidShapes.shapeFluidGas, 9_000),
                Materials.Water.getFluid(54_000))
            .duration(2 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // 18SOCl2 + 5C10H10O4 + 6CO2 = 7C8H4Cl2O2 + 22HCl + 18SO2

        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.TerephthaloylChloride, Materials2Shapes.shapeDust, 64),
                MaterialLibAPI.getStack(Materials2Materials.TerephthaloylChloride, Materials2Shapes.shapeDust, 48))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.ThionylChloride, Materials2FluidShapes.shapeFluidLiquid, 18_000),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DimethylTerephthalate,
                    Materials2FluidShapes.shapeFluidLiquid,
                    5_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.shapeFluidGas, 6_000))
            .fluidOutputs(
                Materials.DilutedHydrochloricAcid.getFluid(22_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfurDioxide, Materials2FluidShapes.shapeFluidGas, 18_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // 2CH4O + C8H6O4 =H2SO4= C10H10O4 + 2H2O

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.TerephthalicAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methanol, Materials2FluidShapes.shapeFluidLiquid, 2_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.shapeFluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DimethylTerephthalate,
                    Materials2FluidShapes.shapeFluidLiquid,
                    1_000),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DilutedSulfuricAcid,
                    Materials2FluidShapes.shapeFluidLiquid,
                    2_000))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.TerephthalicAcid, Materials2FluidShapes.shapeFluidLiquid, 9_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methanol, Materials2FluidShapes.shapeFluidLiquid, 18_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.shapeFluidLiquid, 18_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DimethylTerephthalate,
                    Materials2FluidShapes.shapeFluidLiquid,
                    9_000),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DilutedSulfuricAcid,
                    Materials2FluidShapes.shapeFluidLiquid,
                    18_000))
            .duration(1 * MINUTES + 27 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Benzene, Materials2FluidShapes.shapeFluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.shapeFluidGas, 2_000))
            .fluidOutputs(
                Materials.IIIDimethylbenzene.getFluid(1_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 4_000))
            .duration(3 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(3)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Benzene, Materials2FluidShapes.shapeFluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.shapeFluidGas, 2_000))
            .fluidOutputs(
                Materials.IVDimethylbenzene.getFluid(1_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 4_000))
            .duration(3 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);

        // Diluted Sulfuric acid undilution. 3000 Diluted is 2000 sulfuric 1000 water per DT recipe
        GTValues.RA.stdBuilder()
            .circuit(23)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 1_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfurDioxide, Materials2FluidShapes.shapeFluidGas, 1_000),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DilutedSulfuricAcid,
                    Materials2FluidShapes.shapeFluidLiquid,
                    3_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.shapeFluidLiquid, 3_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(23)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfurTrioxide, Materials2FluidShapes.shapeFluidGas, 1_000),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DilutedSulfuricAcid,
                    Materials2FluidShapes.shapeFluidLiquid,
                    3_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.shapeFluidLiquid, 3_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(23)
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.shapeDust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 3_000),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DilutedSulfuricAcid,
                    Materials2FluidShapes.shapeFluidLiquid,
                    3_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.shapeFluidLiquid, 3_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(22)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 9_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfurDioxide, Materials2FluidShapes.shapeFluidGas, 9_000),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DilutedSulfuricAcid,
                    Materials2FluidShapes.shapeFluidLiquid,
                    27_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.shapeFluidLiquid, 27_000))
            .duration(11 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(22)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfurTrioxide, Materials2FluidShapes.shapeFluidGas, 9_000),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DilutedSulfuricAcid,
                    Materials2FluidShapes.shapeFluidLiquid,
                    27_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.shapeFluidLiquid, 27_000))
            .duration(11 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(22)
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.shapeDust, 9))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 27_000),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DilutedSulfuricAcid,
                    Materials2FluidShapes.shapeFluidLiquid,
                    27_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.shapeFluidLiquid, 27_000))
            .duration(11 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.CobaltIIAcetate, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.CobaltIIAcetate, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.CobaltIIAcetate, 7))
            .circuit(9)
            .itemOutputs(
                Materials.CobaltIINaphthenate.getDust(64),
                Materials.CobaltIINaphthenate.getDust(64),
                Materials.CobaltIINaphthenate.getDust(64),
                Materials.CobaltIINaphthenate.getDust(64),
                Materials.CobaltIINaphthenate.getDust(64),
                Materials.CobaltIINaphthenate.getDust(49))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NaphthenicAcid, Materials2FluidShapes.shapeFluidLiquid, 9_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.shapeFluidLiquid, 13_500))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.CobaltIIHydroxide, Materials2Shapes.shapeDust, 45))
            .circuit(9)
            .itemOutputs(
                Materials.CobaltIINaphthenate.getDust(64),
                Materials.CobaltIINaphthenate.getDust(64),
                Materials.CobaltIINaphthenate.getDust(64),
                Materials.CobaltIINaphthenate.getDust(64),
                Materials.CobaltIINaphthenate.getDust(64),
                Materials.CobaltIINaphthenate.getDust(49))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NaphthenicAcid, Materials2FluidShapes.shapeFluidLiquid, 9_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // PCl3 + 3C6H5Cl + 6Na = 6NaCl + C18H15P

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.shapeDust, 6))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Triphenylphosphene, Materials2Shapes.shapeDust, 34),
                MaterialLibAPI.getStack(Materials2Materials.Salt, Materials2Shapes.shapeDust, 12))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.PhosphorusTrichloride,
                    Materials2FluidShapes.shapeFluidLiquid,
                    1_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Chlorobenzene, Materials2FluidShapes.shapeFluidLiquid, 3_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // 4NaH + C3H9BO3 = NaBH4 + 3CH3ONa

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.SodiumHydride, Materials2Shapes.shapeDust, 8))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.SodiumBorohydride, Materials2Shapes.shapeDust, 6),
                MaterialLibAPI.getStack(Materials2Materials.SodiumMethoxide, Materials2Shapes.shapeDust, 18))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.TrimethylBorate, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.SodiumHydride, Materials2Shapes.shapeDust, 64))
            .circuit(9)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.SodiumBorohydride, Materials2Shapes.shapeDust, 48),
                MaterialLibAPI.getStack(Materials2Materials.SodiumMethoxide, Materials2Shapes.shapeDust, 64),
                MaterialLibAPI.getStack(Materials2Materials.SodiumMethoxide, Materials2Shapes.shapeDust, 64),
                MaterialLibAPI.getStack(Materials2Materials.SodiumMethoxide, Materials2Shapes.shapeDust, 16))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.TrimethylBorate, Materials2FluidShapes.shapeFluidLiquid, 8_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // 2CH3COOH = CH3COCH3 + CO2 + H

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility
                    .copyAmount(0, MaterialLibAPI.getStack(Materials2Materials.Calcium, Materials2Shapes.shapeDust, 1)))
            .circuit(3)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.shapeFluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Acetone, Materials2FluidShapes.shapeFluidLiquid, 1_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.shapeFluidGas, 1_000),
                Materials.Water.getFluid(1_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        // Same as above, but with Quicklime and Calcite. The line it's shortcutting accepts Calcium, quicklime, and
        // Calcite
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.copyAmount(
                    0,
                    MaterialLibAPI.getStack(Materials2Materials.Quicklime, Materials2Shapes.shapeDust, 1)))
            .circuit(3)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.shapeFluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Acetone, Materials2FluidShapes.shapeFluidLiquid, 1_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.shapeFluidGas, 1_000),
                Materials.Water.getFluid(1_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility
                    .copyAmount(0, MaterialLibAPI.getStack(Materials2Materials.Calcite, Materials2Shapes.shapeDust, 1)))
            .circuit(3)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.shapeFluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Acetone, Materials2FluidShapes.shapeFluidLiquid, 1_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.shapeFluidGas, 1_000),
                Materials.Water.getFluid(1_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        // C + 4H + O = CH3OH

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.shapeDust, 1))
            .circuit(23)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 4_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methanol, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(16 * SECONDS)
            .eut(96)
            .addTo(multiblockChemicalReactorRecipes);

        // This recipe collides with one for Vinyl Chloride
        // 2C + 4H + 2O = CH3COOH

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.shapeDust, 2))
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 4_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        // 2CO + 4H = CH3COOH

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonMonoxide, Materials2FluidShapes.shapeFluidGas, 2_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 4_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(8)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 9_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, 9_000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(9_000))
            .duration(7 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, 10_000),
                Materials.Water.getFluid(10_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Mercury, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.HypochlorousAcid,
                    Materials2FluidShapes.shapeFluidLiquid,
                    10_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 10_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(multiblockChemicalReactorRecipes);

        // H2O + 4Cl + C3H6 + NaOH = C3H5ClO + NaCl·H2O + 2HCl

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.SodiumHydroxide.getDust(3))
            .circuit(23)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Propene, Materials2FluidShapes.shapeFluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, 4_000),
                Materials.Water.getFluid(1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Epichlorohydrin, Materials2FluidShapes.shapeFluidLiquid, 1_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SaltWater, Materials2FluidShapes.shapeFluidLiquid, 1_000),
                Materials.HydrochloricAcid.getFluid(2_000))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        // H2O + 2Cl + C3H6 + NaOH =Hg= C3H5ClO + NaCl·H2O + 2H

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.SodiumHydroxide.getDust(3))
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Propene, Materials2FluidShapes.shapeFluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, 2_000),
                Materials.Water.getFluid(1_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Mercury, Materials2FluidShapes.shapeFluidLiquid, 100))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Epichlorohydrin, Materials2FluidShapes.shapeFluidLiquid, 1_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SaltWater, Materials2FluidShapes.shapeFluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 2_000))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        // HClO + 2Cl + C3H6 + NaOH = C3H5ClO + NaCl·H2O + HCl

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.SodiumHydroxide.getDust(3))
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Propene, Materials2FluidShapes.shapeFluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, 2_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HypochlorousAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Epichlorohydrin, Materials2FluidShapes.shapeFluidLiquid, 1_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SaltWater, Materials2FluidShapes.shapeFluidLiquid, 1_000),
                Materials.HydrochloricAcid.getFluid(1_000))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Apatite, Materials2Shapes.shapeDust, 9))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Gypsum, Materials2Shapes.shapeDust, 40))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.shapeFluidLiquid, 5_000),
                Materials.Water.getFluid(10_000))
            .fluidOutputs(Materials.PhosphoricAcid.getFluid(3_000), Materials.HydrochloricAcid.getFluid(1_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Phosphorus, Materials2Shapes.shapeDust, 4))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.PhosphorousPentoxide, Materials2Shapes.shapeDust, 14))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 10_000))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        // 2P + 5O + 3H2O = 2H3PO4

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Phosphorus, Materials2Shapes.shapeDust, 1))
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 2500),
                Materials.Water.getFluid(1_500))
            .fluidOutputs(Materials.PhosphoricAcid.getFluid(1_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Propene, Materials2FluidShapes.shapeFluidGas, 8_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Benzene, Materials2FluidShapes.shapeFluidLiquid, 8_000),
                Materials.PhosphoricAcid.getFluid(1_000))
            .fluidOutputs(Materials.Cumene.getFluid(8_000))
            .duration(1 * MINUTES + 36 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Propene, Materials2FluidShapes.shapeFluidGas, 1_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Benzene, Materials2FluidShapes.shapeFluidLiquid, 1_000),
                Materials.PhosphoricAcid.getFluid(100),
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Phenol, Materials2FluidShapes.shapeFluidLiquid, 1_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Acetone, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Acetone, Materials2FluidShapes.shapeFluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Phenol, Materials2FluidShapes.shapeFluidLiquid, 2_000),
                Materials.HydrochloricAcid.getFluid(1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.BisphenolA, Materials2FluidShapes.shapeFluidLiquid, 1_000),
                Materials.Water.getFluid(1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.SodiumHydroxide.getDust(6))
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Acetone, Materials2FluidShapes.shapeFluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Phenol, Materials2FluidShapes.shapeFluidLiquid, 2_000),
                Materials.HydrochloricAcid.getFluid(1_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Epichlorohydrin, Materials2FluidShapes.shapeFluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Epoxid, Materials2FluidShapes.shapeFluidMolten, 1_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SaltWater, Materials2FluidShapes.shapeFluidLiquid, 2_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 9_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Fluorine, Materials2FluidShapes.shapeFluidGas, 9_000))
            .fluidOutputs(Materials.HydrofluoricAcid.getFluid(9_000))
            .duration(7 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                Materials.HydrofluoricAcid.getFluid(4_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.shapeFluidGas, 2_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, 12_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Tetrafluoroethylene, Materials2FluidShapes.shapeFluidGas, 1_000),
                Materials.HydrochloricAcid.getFluid(12_000))
            .duration(27 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Silicon, Materials2Shapes.shapeDust, 1))
            .circuit(24)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Polydimethylsiloxane, Materials2Shapes.shapeDust, 3))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.shapeFluidGas, 2_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, 4_000),
                Materials.Water.getFluid(1_000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(2_000), Materials.DilutedHydrochloricAcid.getFluid(2_000))
            .duration(24 * SECONDS)
            .eut(96)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Silicon, Materials2Shapes.shapeDust, 1))
            .circuit(24)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Polydimethylsiloxane, Materials2Shapes.shapeDust, 3))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methanol, Materials2FluidShapes.shapeFluidLiquid, 2_000),
                Materials.HydrochloricAcid.getFluid(2_000))
            .fluidOutputs(Materials.DilutedHydrochloricAcid.getFluid(2_000))
            .duration(24 * SECONDS)
            .eut(96)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Nitrogen, Materials2FluidShapes.shapeFluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 3_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ammonia, Materials2FluidShapes.shapeFluidGas, 1_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_HV * 4 / 5)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Nitrogen, Materials2FluidShapes.shapeFluidGas, 10_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 30_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ammonia, Materials2FluidShapes.shapeFluidGas, 10_000))
            .duration(2 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_HV * 4 / 5)
            .addTo(multiblockChemicalReactorRecipes);

        // 2NH3 + 7O = N2O4 + 3H2O

        GTValues.RA.stdBuilder()
            .circuit(23)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ammonia, Materials2FluidShapes.shapeFluidGas, 2_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 7_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.DinitrogenTetroxide, Materials2FluidShapes.shapeFluidGas, 1_000),
                Materials.Water.getFluid(3_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        // 7O + 6H + 2N = N2O4 + 3H2O

        GTValues.RA.stdBuilder()
            .circuit(23)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Nitrogen, Materials2FluidShapes.shapeFluidGas, 2_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 6_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 7_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.DinitrogenTetroxide, Materials2FluidShapes.shapeFluidGas, 1_000),
                Materials.Water.getFluid(3_000))
            .duration(55 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 100_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ammonia, Materials2FluidShapes.shapeFluidGas, 36_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricOxide, Materials2FluidShapes.shapeFluidGas, 36_000),
                Materials.Water.getFluid(54_000))
            .duration(8 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(8)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 100_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ammonia, Materials2FluidShapes.shapeFluidGas, 36_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricOxide, Materials2FluidShapes.shapeFluidGas, 36_000))
            .duration(8 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricOxide, Materials2FluidShapes.shapeFluidGas, 9_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 9_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitrogenDioxide, Materials2FluidShapes.shapeFluidGas, 9_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitrogenDioxide, Materials2FluidShapes.shapeFluidGas, 27_000),
                Materials.Water.getFluid(9_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricAcid, Materials2FluidShapes.shapeFluidLiquid, 18_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricOxide, Materials2FluidShapes.shapeFluidGas, 9_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(21)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 3_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Nitrogen, Materials2FluidShapes.shapeFluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 4_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000),
                Materials.Water.getFluid(1_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ammonia, Materials2FluidShapes.shapeFluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 4_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000),
                Materials.Water.getFluid(1_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitrogenDioxide, Materials2FluidShapes.shapeFluidGas, 2_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 1_000),
                Materials.Water.getFluid(1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricAcid, Materials2FluidShapes.shapeFluidLiquid, 2_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.shapeDust, 9))
            .circuit(9)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 18_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HydricSulfide, Materials2FluidShapes.shapeFluidGas, 9_000))
            .duration(4 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.shapeDust, 9))
            .circuit(9)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 18_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfurDioxide, Materials2FluidShapes.shapeFluidGas, 9_000))
            .duration(4 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HydricSulfide, Materials2FluidShapes.shapeFluidGas, 9_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 27_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfurDioxide, Materials2FluidShapes.shapeFluidGas, 9_000),
                Materials.Water.getFluid(9_000))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(8)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HydricSulfide, Materials2FluidShapes.shapeFluidGas, 9_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 27_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfurDioxide, Materials2FluidShapes.shapeFluidGas, 9_000))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(7)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.shapeDust, 27))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfurDioxide, Materials2FluidShapes.shapeFluidGas, 9_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HydricSulfide, Materials2FluidShapes.shapeFluidGas, 18_000))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfurTrioxide, Materials2FluidShapes.shapeFluidGas, 9_000),
                Materials.Water.getFluid(9_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.shapeFluidLiquid, 9_000))
            .duration(13 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        // S + O3 + H2O = H2SO4

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.shapeDust, 1))
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 3_000),
                Materials.Water.getFluid(1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.shapeDust, 9))
            .circuit(7)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 27_000),
                Materials.Water.getFluid(9_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.shapeFluidLiquid, 9_000))
            .duration(13 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        // H2S + O4 = H2SO4

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HydricSulfide, Materials2FluidShapes.shapeFluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 4_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        // SO2 + O + H2O = H2SO4

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfurDioxide, Materials2FluidShapes.shapeFluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 1_000),
                Materials.Water.getFluid(1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfurDioxide, Materials2FluidShapes.shapeFluidGas, 9_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 9_000),
                Materials.Water.getFluid(9_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.shapeFluidLiquid, 9_000))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(
                Materials.HydrochloricAcid.getFluid(1_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.shapeFluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.VinylChloride, Materials2FluidShapes.shapeFluidGas, 1_000),
                Materials.Water.getFluid(1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, 2_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.shapeFluidGas, 2_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.VinylChloride, Materials2FluidShapes.shapeFluidGas, 2_000),
                Materials.Water.getFluid(1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .itemOutputs(Materials.RubberRaw.getDust(18))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Isoprene, Materials2FluidShapes.shapeFluidLiquid, 1728),
                MaterialLibAPI.getFluidStack(Materials2Materials.Air, Materials2FluidShapes.shapeFluidGas, 6_000),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Titaniumtetrachloride,
                    Materials2FluidShapes.shapeFluidLiquid,
                    80))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .itemOutputs(Materials.RubberRaw.getDust(24))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Isoprene, Materials2FluidShapes.shapeFluidLiquid, 1728),
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 6_000),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Titaniumtetrachloride,
                    Materials2FluidShapes.shapeFluidLiquid,
                    80))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(3)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.RawStyreneButadieneRubber, Materials2Shapes.shapeDust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Styrene, Materials2FluidShapes.shapeFluidLiquid, 36),
                MaterialLibAPI.getFluidStack(Materials2Materials.Butadiene, Materials2FluidShapes.shapeFluidGas, 108),
                MaterialLibAPI.getFluidStack(Materials2Materials.Air, Materials2FluidShapes.shapeFluidGas, 2_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(3)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.RawStyreneButadieneRubber, Materials2Shapes.shapeDust, 3))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Styrene, Materials2FluidShapes.shapeFluidLiquid, 72),
                MaterialLibAPI.getFluidStack(Materials2Materials.Butadiene, Materials2FluidShapes.shapeFluidGas, 216),
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 2_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(4)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.RawStyreneButadieneRubber, Materials2Shapes.shapeDust, 22),
                MaterialLibAPI
                    .getStack(Materials2Materials.RawStyreneButadieneRubber, Materials2Shapes.shapeDustSmall, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Styrene, Materials2FluidShapes.shapeFluidLiquid, 540),
                MaterialLibAPI.getFluidStack(Materials2Materials.Butadiene, Materials2FluidShapes.shapeFluidGas, 1620),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Titaniumtetrachloride,
                    Materials2FluidShapes.shapeFluidLiquid,
                    100),
                MaterialLibAPI.getFluidStack(Materials2Materials.Air, Materials2FluidShapes.shapeFluidGas, 15_000))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(4)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.RawStyreneButadieneRubber, Materials2Shapes.shapeDust, 30))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Styrene, Materials2FluidShapes.shapeFluidLiquid, 540),
                MaterialLibAPI.getFluidStack(Materials2Materials.Butadiene, Materials2FluidShapes.shapeFluidGas, 1620),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Titaniumtetrachloride,
                    Materials2FluidShapes.shapeFluidLiquid,
                    100),
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 7_500))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Salt, Materials2Shapes.shapeDust, 18))
            .circuit(9)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.SodiumBisulfate, Materials2Shapes.shapeDust, 63))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.shapeFluidLiquid, 9_000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(9_000))
            .duration(6 * SECONDS + 15 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.SodiumHydroxide.getDust(27))
            .circuit(9)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.SodiumBisulfate, Materials2Shapes.shapeDust, 63))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.shapeFluidLiquid, 9_000))
            .fluidOutputs(Materials.Water.getFluid(9_000))
            .duration(6 * SECONDS + 15 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Benzene, Materials2FluidShapes.shapeFluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, 2_000),
                Materials.Water.getFluid(1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Phenol, Materials2FluidShapes.shapeFluidLiquid, 1_000),
                Materials.HydrochloricAcid.getFluid(1_000),
                Materials.DilutedHydrochloricAcid.getFluid(1_000))
            .duration(28 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        // C6H6 + 2Cl + NaOH = C6H6O + NaCl + HCl

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.SodiumHydroxide.getDust(6))
            .circuit(24)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Salt, Materials2Shapes.shapeDust, 4))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Benzene, Materials2FluidShapes.shapeFluidLiquid, 2_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.shapeFluidGas, 4_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Phenol, Materials2FluidShapes.shapeFluidLiquid, 2_000),
                Materials.HydrochloricAcid.getFluid(2_000))
            .duration(56 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.LightFuel, Materials2FluidShapes.shapeFluidLiquid, 20_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HeavyFuel, Materials2FluidShapes.shapeFluidLiquid, 4_000))
            .fluidOutputs(Materials.Diesel.getFluid(24_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                Materials.Diesel.getFluid(10_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Tetranitromethane, Materials2FluidShapes.shapeFluidLiquid, 200))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitroFuel, Materials2FluidShapes.shapeFluidLiquid, 10_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.BioDiesel, Materials2FluidShapes.shapeFluidLiquid, 10_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Tetranitromethane, Materials2FluidShapes.shapeFluidLiquid, 400))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitroFuel, Materials2FluidShapes.shapeFluidLiquid, 9_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        // CH4 + 2H2O = CO2 + 8H

        GTValues.RA.stdBuilder()
            .circuit(11)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.shapeFluidGas, 5_000),
                GTModHandler.getDistilledWater(10_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.shapeFluidGas, 5_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 40_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        // CH4 + H2O = CO + 6H

        GTValues.RA.stdBuilder()
            .circuit(12)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.shapeFluidGas, 5_000),
                GTModHandler.getDistilledWater(5_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonMonoxide, Materials2FluidShapes.shapeFluidGas, 5_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 30_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Nitrogen, Materials2FluidShapes.shapeFluidGas, 20_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.shapeFluidGas, 10_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitrousOxide, Materials2FluidShapes.shapeFluidGas, 10_000))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Naphtha, Materials2FluidShapes.shapeFluidLiquid, 16_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Gas, Materials2FluidShapes.shapeFluidGas, 2_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methanol, Materials2FluidShapes.shapeFluidLiquid, 1_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Acetone, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(Materials.GasolineRaw.getFluid(20_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                Materials.GasolineRaw.getFluid(10_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Toluene, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .fluidOutputs(Materials.GasolineRegular.getFluid(11_000))
            .duration(10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                Materials.GasolineRegular.getFluid(20_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Octane, Materials2FluidShapes.shapeFluidLiquid, 2_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitrousOxide, Materials2FluidShapes.shapeFluidGas, 6_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Toluene, Materials2FluidShapes.shapeFluidLiquid, 1_000),
                Materials.AntiKnock.getFluid(3_000))
            .fluidOutputs(Materials.GasolinePremium.getFluid(32_000))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // C2H6O + C4H8 = C6H14O

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Ethanol, Materials2FluidShapes.shapeFluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Butene, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(Materials.AntiKnock.getFluid(1_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        // CH4O + C4H8 = C5H12O

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methanol, Materials2FluidShapes.shapeFluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Butene, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(Materials.MTBEMixture.getGas(1_000))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methanol, Materials2FluidShapes.shapeFluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Butane, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(Materials.MTBEMixtureAlt.getGas(1_000))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        // CH2O + 2C6H7N + HCl = C13H14N2(HCl) + H2O

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                new FluidStack(FluidRegistry.getFluid("fluid.formaldehyde"), 1_000),
                new FluidStack(FluidRegistry.getFluid("aniline"), 2_000),
                Materials.HydrochloricAcid.getFluid(1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DiaminodiphenylmethanMixture,
                    Materials2FluidShapes.shapeFluidLiquid,
                    1_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // C6H5NO2 + 6H =Pd= C6H7N + 2H2O

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Palladium, Materials2Shapes.shapeDust, 1))
            .circuit(1)
            .fluidInputs(
                new FluidStack(FluidRegistry.getFluid("nitrobenzene"), 9000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 54_000))
            .fluidOutputs(Materials.Water.getFluid(18_000), new FluidStack(FluidRegistry.getFluid("aniline"), 9_000))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // C6H6 + HNO3 =H2SO4= C6H5NO2 + H2O

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Benzene, Materials2FluidShapes.shapeFluidLiquid, 5_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.shapeFluidLiquid, 3_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricAcid, Materials2FluidShapes.shapeFluidLiquid, 5_000),
                GTModHandler.getDistilledWater(10_000))
            .fluidOutputs(
                new FluidStack(FluidRegistry.getFluid("nitrobenzene"), 5_000),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DilutedSulfuricAcid,
                    Materials2FluidShapes.shapeFluidLiquid,
                    3_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(multiblockChemicalReactorRecipes);

        // C13H14N2(HCl) + 2COCl2 = C15H10N2O2(5HCl)

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DiaminodiphenylmethanMixture,
                    Materials2FluidShapes.shapeFluidLiquid,
                    1_000),
                new FluidStack(FluidRegistry.getFluid("phosgene"), 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DiphenylmethaneDiisocyanateMixture,
                    Materials2FluidShapes.shapeFluidLiquid,
                    1_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Palladium, Materials2Shapes.shapeDust, 1))
            .circuit(9)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Butyraldehyde, Materials2FluidShapes.shapeFluidLiquid, 9_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 18_000))
            .fluidOutputs(new FluidStack(FluidRegistry.getFluid("butanol"), 9_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Tin, Materials2Shapes.shapeDust, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.KevlarCatalyst, 1))
            .fluidInputs(
                new FluidStack(FluidRegistry.getFluid("butanol"), 2_000),
                new FluidStack(FluidRegistry.getFluid("propionicacid"), 1_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.IronIIIChloride, Materials2FluidShapes.shapeFluidLiquid, 100))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Tin, Materials2Shapes.shapeDust, 9))
            .circuit(9)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.KevlarCatalyst, 9))
            .fluidInputs(
                new FluidStack(FluidRegistry.getFluid("butanol"), 18_000),
                new FluidStack(FluidRegistry.getFluid("propionicacid"), 9_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.IronIIIChloride, Materials2FluidShapes.shapeFluidLiquid, 900))
            .duration(3 * MINUTES + 45 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // C2H4 + CO + H2O =C4NiO= C3H6O2

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.shapeFluidGas, 1_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonMonoxide, Materials2FluidShapes.shapeFluidGas, 1_000),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.NickelTetracarbonyl,
                    Materials2FluidShapes.shapeFluidLiquid,
                    100),
                Materials.Water.getFluid(1_000))
            .fluidOutputs(new FluidStack(FluidRegistry.getFluid("propionicacid"), 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.shapeFluidGas, 9_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonMonoxide, Materials2FluidShapes.shapeFluidGas, 9_000),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.NickelTetracarbonyl,
                    Materials2FluidShapes.shapeFluidLiquid,
                    900),
                Materials.Water.getFluid(9_000))
            .fluidOutputs(new FluidStack(FluidRegistry.getFluid("propionicacid"), 9_000))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // C6H7N + HNO3 =H2SO4,C4H6O3= C6H6N2O2 + H2O

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(
                new FluidStack(FluidRegistry.getFluid("aniline"), 1_000),
                new FluidStack(FluidRegistry.getFluid("molten.aceticanhydride"), 100),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitrationMixture, Materials2FluidShapes.shapeFluidLiquid, 2_000))
            .fluidOutputs(
                Materials.IVNitroaniline.getFluid(1_000),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DilutedSulfuricAcid,
                    Materials2FluidShapes.shapeFluidLiquid,
                    1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(
                new FluidStack(FluidRegistry.getFluid("aniline"), 9_000),
                new FluidStack(FluidRegistry.getFluid("molten.aceticanhydride"), 900),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.NitrationMixture,
                    Materials2FluidShapes.shapeFluidLiquid,
                    18_000))
            .fluidOutputs(
                Materials.IVNitroaniline.getFluid(9_000),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DilutedSulfuricAcid,
                    Materials2FluidShapes.shapeFluidLiquid,
                    9_000))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // C6H6N2O2 + 6H =Pd,NO2= C6H8N2 + 2H2O

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Palladium, Materials2Shapes.shapeDust, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.ParaPhenylenediamine, 16))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitrogenDioxide, Materials2FluidShapes.shapeFluidGas, 100),
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 6_000),
                Materials.IVNitroaniline.getFluid(1_000))
            .fluidOutputs(Materials.Water.getFluid(2_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(multiblockChemicalReactorRecipes);

        // C4H10O2 =Cu= C4H6O2 + 4H

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Copper, Materials2Shapes.shapeDust, 1))
            .circuit(1)
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("1,4-butanediol"), 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.GammaButyrolactone,
                    Materials2FluidShapes.shapeFluidLiquid,
                    1_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 4_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Copper, Materials2Shapes.shapeDust, 9))
            .circuit(9)
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("1,4-butanediol"), 9_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.GammaButyrolactone,
                    Materials2FluidShapes.shapeFluidLiquid,
                    9_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 36_000))
            .duration(35 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // 2CH2O + C2H2 =SiO2,CuO,Bi2O3= C4H6O2

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.CupricOxide, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.BismuthIIIOxide, Materials2Shapes.shapeDust, 1),
                MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.shapeDust, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.IIButinIIVdiol, 12))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Acetylene, Materials2FluidShapes.shapeFluidGas, 1_000),
                new FluidStack(FluidRegistry.getFluid("fluid.formaldehyde"), 2_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.CupricOxide, Materials2Shapes.shapeDust, 9),
                MaterialLibAPI.getStack(Materials2Materials.BismuthIIIOxide, Materials2Shapes.shapeDust, 9),
                MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.shapeDust, 9))
            .circuit(9)
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.IIButinIIVdiol, 64),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.IIButinIIVdiol, 44))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Acetylene, Materials2FluidShapes.shapeFluidGas, 9_000),
                new FluidStack(FluidRegistry.getFluid("fluid.formaldehyde"), 18_000))
            .duration(2 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // C4H6O2 + 4H =NiAl= C4H10O2

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.IIButinIIVdiol, 12),
                MaterialLibAPI.getStack(Materials2Materials.RaneyNickelActivated, Materials2Shapes.shapeDust, 1))
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 4_000))
            .fluidOutputs(new FluidStack(FluidRegistry.getFluid("1,4-butanediol"), 1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                WerkstoffLoader.CalciumChloride.get(OrePrefixes.dust, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.ParaPhenylenediamine, 9),
                MaterialLibAPI.getStack(Materials2Materials.TerephthaloylChloride, Materials2Shapes.shapeDust, 9))
            .circuit(1)
            .fluidInputs(Materials.NMethylIIPyrrolidone.getFluid(1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.LiquidCrystalKevlar,
                    Materials2FluidShapes.shapeFluidLiquid,
                    9_000),
                Materials.DilutedHydrochloricAcid.getFluid(2_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                WerkstoffLoader.CalciumChloride.get(OrePrefixes.dust, 7),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.ParaPhenylenediamine, 63),
                MaterialLibAPI.getStack(Materials2Materials.TerephthaloylChloride, Materials2Shapes.shapeDust, 63))
            .circuit(9)
            .fluidInputs(Materials.NMethylIIPyrrolidone.getFluid(7_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.LiquidCrystalKevlar,
                    Materials2FluidShapes.shapeFluidLiquid,
                    63_000),
                Materials.DilutedHydrochloricAcid.getFluid(14_000))
            .duration(2 * MINUTES + 55 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(multiblockChemicalReactorRecipes);

        // Na2B4O7(H2O)10 + 2HCl = 2NaCl + 4H3BO3 + 5H2O

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Borax, Materials2Shapes.shapeDust, 23))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Salt, Materials2Shapes.shapeDust, 4))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(2_000))
            .fluidOutputs(new FluidStack(FluidRegistry.getFluid("boricacid"), 4_000), Materials.Water.getFluid(5_000))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        // H3BO3 + 3CH4O =H2SO4= C3H9BO3 + 3H2O

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methanol, Materials2FluidShapes.shapeFluidLiquid, 3_000),
                new FluidStack(FluidRegistry.getFluid("boricacid"), 1_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.shapeFluidLiquid, 6_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DilutedSulfuricAcid,
                    Materials2FluidShapes.shapeFluidLiquid,
                    6_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.TrimethylBorate, Materials2FluidShapes.shapeFluidLiquid, 1_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Methanol, Materials2FluidShapes.shapeFluidLiquid, 27_000),
                new FluidStack(FluidRegistry.getFluid("boricacid"), 9_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.shapeFluidLiquid, 54_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DilutedSulfuricAcid,
                    Materials2FluidShapes.shapeFluidLiquid,
                    54_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.TrimethylBorate, Materials2FluidShapes.shapeFluidLiquid, 9_000))
            .duration(3 * MINUTES + 45 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // RhCl3 + 3C18H15P + 3NaBH4 + CO = RhC55H46P3O + 3NaCl + 3B + 11H

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.RhodiumChloride, Materials2Shapes.shapeDust, 4),
                MaterialLibAPI.getStack(Materials2Materials.Triphenylphosphene, Materials2Shapes.shapeDust, 64),
                MaterialLibAPI.getStack(Materials2Materials.Triphenylphosphene, Materials2Shapes.shapeDust, 38),
                MaterialLibAPI.getStack(Materials2Materials.SodiumBorohydride, Materials2Shapes.shapeDust, 18))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.OrganorhodiumCatalyst, Materials2Shapes.shapeDust, 64),
                MaterialLibAPI.getStack(Materials2Materials.OrganorhodiumCatalyst, Materials2Shapes.shapeDust, 42),
                MaterialLibAPI.getStack(Materials2Materials.Salt, Materials2Shapes.shapeDust, 6),
                MaterialLibAPI.getStack(Materials2Materials.Boron, Materials2Shapes.shapeDust, 3))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonMonoxide, Materials2FluidShapes.shapeFluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, 11_000))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(multiblockChemicalReactorRecipes);

        // 2NaOH + N2H4 =Mn= 2N + 2H2O + 2NaH

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.SodiumHydroxide.getDust(6),
                MaterialLibAPI.getStack(Materials2Materials.Manganese, Materials2Shapes.shapeDustTiny, 1))
            .circuit(9)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.SodiumHydride, Materials2Shapes.shapeDust, 4))
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("fluid.hydrazine"), 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Nitrogen, Materials2FluidShapes.shapeFluidGas, 2_000),
                Materials.Water.getFluid(2_000))
            .duration(10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.SodiumHydroxide.getDust(54),
                MaterialLibAPI.getStack(Materials2Materials.Manganese, Materials2Shapes.shapeDust, 1))
            .circuit(18)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.SodiumHydride, Materials2Shapes.shapeDust, 36))
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("fluid.hydrazine"), 9_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Nitrogen, Materials2FluidShapes.shapeFluidGas, 18_000),
                Materials.Water.getFluid(18_000))
            .duration(3 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // Flawless Amalgatite
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.copyAmount(0, GTOreDictUnificator.get(OrePrefixes.nanite, Materials.MagMatter, 1)),
                MaterialLibAPI.getStack(Materials2Materials.Amalgatite, Materials2Shapes.shapeGem, 3),
                WerkstoffLoader.Bismutite.get(OrePrefixes.gemFlawed, 64),
                MaterialLibAPI.getStack(Materials2Materials.GarnetYellow, Materials2Shapes.shapeGemFlawed, 64),
                MaterialLibAPI.getStack(Materials2Materials.GreenSapphire, Materials2Shapes.shapeGemFlawed, 64),
                MaterialLibAPI.getStack(Materials2Materials.Olivine, Materials2Shapes.shapeGemFlawed, 64))
            .fluidInputs(Materials.PrismaticAcid.getFluid(1_000_000), Materials.DTR.getFluid(1_000_000))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Amalgatite, Materials2Shapes.shapeGemFlawless, 1))
            .duration(120 * SECONDS)
            .eut(TierEU.RECIPE_MAX)
            .addTo(multiblockChemicalReactorRecipes);
    }
}
