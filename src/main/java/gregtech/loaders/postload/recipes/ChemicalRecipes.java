package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.Forestry;
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
import static gtPlusPlus.core.fluids.GTPPFluids.Formaldehyde;
import static gtPlusPlus.core.util.minecraft.FluidUtils.getFilledCellFromFluidName;
import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

import java.util.Locale;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.CellShapes;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.objects.OreDictItemStack;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.items.CombType;
import gregtech.loaders.misc.GTBees;
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
                    MaterialLibAPI.getStack(Materials.Salt, Shapes.dust, 2))
                .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 432))
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
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Glyceryl, FluidShapes.fluidLiquid, 500))
            .duration(8 * SECONDS)
            .eut(4)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Aluminium, Shapes.dust, 4))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Indium, Shapes.dustTiny, 1))
            .fluidInputs(new FluidStack(ItemList.sIndiumConcentrate, 8_000))
            .fluidOutputs(new FluidStack(ItemList.sLeadZincSolution, 8_000))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Aluminium, Shapes.dust, 36))
            .circuit(9)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Indium, Shapes.dust, 1))
            .fluidInputs(new FluidStack(ItemList.sIndiumConcentrate, 72_000))
            .fluidOutputs(new FluidStack(ItemList.sLeadZincSolution, 72_000))
            .duration(22 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // Platinum Group Sludge chain

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Pentlandite, Shapes.crushedPurified, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.PlatinumGroupSludge, Shapes.dustTiny, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(new FluidStack(ItemList.sNickelSulfate, 2_000))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Chalcopyrite, Shapes.crushedPurified, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.PlatinumGroupSludge, Shapes.dustTiny, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(new FluidStack(ItemList.sBlueVitriol, 2_000))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Fe + 3HCl = FeCl3 + 3H

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, 1), ItemList.Cell_Empty.get(3))
            .itemOutputs(MaterialParts.requireCell(Materials.Hydrogen, 3))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 3_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.IronIIIChloride, FluidShapes.fluidLiquid, 1_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.InfusedGold, Shapes.dust, 8),
                MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, 8))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Thaumium, Shapes.dust, 16))
            .fluidInputs(GTModHandler.getIC2Coolant(1_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                getModItem(GalaxySpace.ID, "item.UnknowCrystal", 4),
                MaterialLibAPI.getStack(Materials.Osmiridium, Shapes.dust, 2))
            .itemOutputs(ItemList.Circuit_Chip_Stemcell.get(64))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.GrowthMediumSterilized, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(getFluidStack("bacterialsludge", 1_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Chip_Stemcell.get(32),
                MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.dust, 4))
            .itemOutputs(ItemList.Circuit_Chip_Biocell.get(32))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.BiohMediumSterilized, FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(getFluidStack("mutagen", 2_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.sugar, 8), MaterialLibAPI.getStack(Materials.Plastic, Shapes.dust, 1))
            .itemOutputs(ItemList.GelledToluene.get(16))
            .fluidInputs(new FluidStack(ItemList.sToluene, 1_000))
            .duration(56 * SECONDS)
            .eut(192)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.GelledToluene.get(4))
            .circuit(1)
            .itemOutputs(new ItemStack(Blocks.tnt, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 250))
            .duration(10 * SECONDS)
            .eut(24)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.GelledToluene.get(4))
            .circuit(1)
            .itemOutputs(GTModHandler.getIC2Item("industrialTnt", 1))
            .fluidInputs(new FluidStack(ItemList.sNitrationMixture, 200))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.DilutedSulfuricAcid, FluidShapes.fluidLiquid, 150))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Hydrogen, 2))
            .circuit(4)
            .itemOutputs(
                MaterialParts.requireCell(Materials.HydricSulfide, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.NatruralGas, FluidShapes.fluidGas, 16_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Gas, FluidShapes.fluidGas, 16_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.NatruralGas, 2))
            .circuit(4)
            .itemOutputs(MaterialParts.requireCell(Materials.Gas, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 250))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HydricSulfide, FluidShapes.fluidGas, 125))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Hydrogen, 2))
            .circuit(4)
            .itemOutputs(
                MaterialParts.requireCell(Materials.HydricSulfide, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SulfuricGas, FluidShapes.fluidGas, 16_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Gas, FluidShapes.fluidGas, 16_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.SulfuricGas, 2))
            .circuit(4)
            .itemOutputs(MaterialParts.requireCell(Materials.Gas, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 250))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HydricSulfide, FluidShapes.fluidGas, 125))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Hydrogen, 2))
            .circuit(4)
            .itemOutputs(
                MaterialParts.requireCell(Materials.HydricSulfide, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SulfuricNaphtha, FluidShapes.fluidLiquid, 12_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Naphtha, FluidShapes.fluidLiquid, 12_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SulfuricNaphtha, CellShapes.cell, 3))
            .circuit(4)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Naphtha, CellShapes.cell, 3))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 500))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HydricSulfide, FluidShapes.fluidGas, 250))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Hydrogen, 2))
            .circuit(4)
            .itemOutputs(
                MaterialParts.requireCell(Materials.HydricSulfide, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SulfuricLightFuel, FluidShapes.fluidLiquid, 12_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.LightFuel, FluidShapes.fluidLiquid, 12_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SulfuricLightFuel, CellShapes.cell, 3))
            .circuit(4)
            .itemOutputs(MaterialLibAPI.getStack(Materials.LightFuel, CellShapes.cell, 3))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 500))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HydricSulfide, FluidShapes.fluidGas, 250))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Hydrogen, 2))
            .circuit(4)
            .itemOutputs(
                MaterialParts.requireCell(Materials.HydricSulfide, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SulfuricHeavyFuel, FluidShapes.fluidLiquid, 8_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HeavyFuel, FluidShapes.fluidLiquid, 8_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SulfuricHeavyFuel, CellShapes.cell, 1))
            .circuit(4)
            .itemOutputs(MaterialLibAPI.getStack(Materials.HeavyFuel, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 250))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HydricSulfide, FluidShapes.fluidGas, 125))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Saltpeter, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Potassium, Shapes.dustTiny, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Naphtha, FluidShapes.fluidLiquid, 576))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Polycaprolactam, FluidShapes.fluidMolten, 9 * INGOTS))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Saltpeter, Shapes.dust, 9))
            .circuit(9)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Potassium, Shapes.dust, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Naphtha, FluidShapes.fluidLiquid, 5_184))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Polycaprolactam, FluidShapes.fluidMolten, 1 * STACKS + 17 * INGOTS))
            .duration(4 * MINUTES + 48 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // 3quartz dust + Na + H2O = 3quartz gem (Na loss

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.NetherQuartz, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Sodium, Shapes.dust, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.NetherQuartz, 3))
            .fluidInputs(GTUtility.getWater(1_000))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.CertusQuartz, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Sodium, Shapes.dust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.CertusQuartz, Shapes.gem, 3))
            .fluidInputs(GTUtility.getWater(1_000))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Quartzite, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Sodium, Shapes.dust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Quartzite, Shapes.gem, 3))
            .fluidInputs(GTUtility.getWater(1_000))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.NetherQuartz, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Sodium, Shapes.dust, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.NetherQuartz, 3))
            .fluidInputs(GTModHandler.getDistilledWater(1_000))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.CertusQuartz, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Sodium, Shapes.dust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.CertusQuartz, Shapes.gem, 3))
            .fluidInputs(GTModHandler.getDistilledWater(1_000))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Quartzite, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Sodium, Shapes.dust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Quartzite, Shapes.gem, 3))
            .fluidInputs(GTModHandler.getDistilledWater(1_000))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // 3UO2 + 4Al = 3U + 2Al2O3

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Uraninite, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.Aluminium, Shapes.dust, 4))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Uranium, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Alumina, Shapes.dust, 10))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // UO2 + 2Mg = U + 2MgO

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Uraninite, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Magnesium, Shapes.dust, 2))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Uranium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Magnesia, Shapes.dust, 4))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Ca + C + 3O = CaCO3

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Calcium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Calcite, Shapes.dust, 5))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 3_000))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // C + 4H = CH4

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, 1))
            .circuit(1)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 4_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 1_000))
            .duration(100 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // TiO2 + 2C + 4Cl = TiCl4 + 2CO

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Rutile, Shapes.dust, 3),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Carbon, 2))
            .itemOutputs(MaterialParts.requireCell(Materials.CarbonMonoxide, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 4_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Titaniumtetrachloride, FluidShapes.fluidLiquid, 1_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Rutile, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 4_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Titaniumtetrachloride, FluidShapes.fluidLiquid, 1_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Rutile, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 4_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, FluidShapes.fluidGas, 2_000),
                MaterialLibAPI.getFluidStack(Materials.Titaniumtetrachloride, FluidShapes.fluidLiquid, 1_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Titanium, Shapes.dust, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 4_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Titaniumtetrachloride, FluidShapes.fluidLiquid, 1_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);

        // 4Na + 2MgCl2 = 2Mg + 4NaCl

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Sodium, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.Magnesiumchloride, Shapes.dust, 6))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Magnesium, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.Salt, Shapes.dust, 8))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // rubber

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.RawRubber, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, 1))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Rubber, FluidShapes.fluidMolten, 9 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.RawRubber, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dustTiny, 1))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Rubber, FluidShapes.fluidMolten, 1 * INGOTS))
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
                MaterialLibAPI.getStack(Materials.Plutonium, Shapes.ingot, 8),
                MaterialLibAPI.getStack(Materials.Uranium, Shapes.dustTiny, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Plutonium, Shapes.dust, 8))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Air, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Radon, FluidShapes.fluidGas, 100))
            .duration(10 * MINUTES)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        // Silicon Line
        // SiO2 + 2Mg = 2MgO + Si

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Magnesium, Shapes.dust, 2))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Silicon, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Magnesia, Shapes.dust, 4))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.NetherQuartz, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Magnesium, Shapes.dust, 2))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Silicon, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Magnesia, Shapes.dust, 4))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Quartzite, Shapes.dust, 6),
                MaterialLibAPI.getStack(Materials.Magnesium, Shapes.dust, 2))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Silicon, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Magnesia, Shapes.dust, 4))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.CertusQuartz, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Magnesium, Shapes.dust, 2))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Silicon, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Magnesia, Shapes.dust, 4))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Jasper, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Magnesium, Shapes.dust, 2))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Silicon, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Magnesia, Shapes.dust, 4))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Opal, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Magnesium, Shapes.dust, 2))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Silicon, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Magnesia, Shapes.dust, 4))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        // 3SiF4 + 4Al = 3Si + 4AlF3

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Aluminium, Shapes.dust, 4))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Silicon, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Aluminiumfluoride, Shapes.dust, 16))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SiliconTetrafluoride, FluidShapes.fluidGas, 3_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // SiO2 + 4HF = SiF4 + 2H2O

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 3))
            .circuit(2)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydrofluoricAcidGT5U, FluidShapes.fluidLiquid, 4_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SiliconTetrafluoride, FluidShapes.fluidGas, 1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.NetherQuartz, Shapes.dust, 3))
            .circuit(2)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydrofluoricAcidGT5U, FluidShapes.fluidLiquid, 4_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SiliconTetrafluoride, FluidShapes.fluidGas, 1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.CertusQuartz, Shapes.dust, 3))
            .circuit(2)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydrofluoricAcidGT5U, FluidShapes.fluidLiquid, 4_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SiliconTetrafluoride, FluidShapes.fluidGas, 1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Quartzite, Shapes.dust, 6))
            .circuit(2)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydrofluoricAcidGT5U, FluidShapes.fluidLiquid, 4_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SiliconTetrafluoride, FluidShapes.fluidGas, 1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // 4Na + SiCl4 = 4NaCl + Si

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Sodium, Shapes.dust, 4))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.SiliconSolarGrade, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Salt, Shapes.dust, 8))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SiliconTetrachloride, FluidShapes.fluidLiquid, 1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // HSiCl3 + 2H = 3HCl + Si

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Hydrogen, 2))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.SiliconSolarGrade, Shapes.dust, 1),
                ItemList.Cell_Empty.get(2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Trichlorosilane, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 3_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Trichlorosilane, CellShapes.cell, 1))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.SiliconSolarGrade, Shapes.dust, 1),
                ItemList.Cell_Empty.get(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 3_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // 4HSiCl3 = 3SiCl4 + SiH4

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(1))
            .circuit(2)
            .itemOutputs(MaterialParts.requireCell(Materials.Silane, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Trichlorosilane, FluidShapes.fluidLiquid, 4_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SiliconTetrachloride, FluidShapes.fluidLiquid, 3_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // SiH4 = Si + 4H

        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.SiliconSolarGrade, Shapes.dust, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Silane, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 4_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Ca + 2H = CaH2

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Calcium, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.CalciumHydride, Shapes.dust, 3))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 2_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Si + 4Cl = SiCl4

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Silicon, Shapes.dust, 1))
            .circuit(2)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 4_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SiliconTetrachloride, FluidShapes.fluidLiquid, 1_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // 2Na + S = Na2S

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Sodium, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SodiumSulfide, Shapes.dust, 3))
            .duration(60)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // H2S + H2O + (O2) = 0.5H2SO4(Diluted) ( S loss

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.HydricSulfide, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(GTUtility.getWater(1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.DilutedSulfuricAcid, FluidShapes.fluidLiquid, 750))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydricSulfide, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.DilutedSulfuricAcid, FluidShapes.fluidLiquid, 750))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // Ni + 4CO = Ni(CO)4

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Nickel, Shapes.dust, 1),
                MaterialParts.requireCell(Materials.CarbonMonoxide, 4))
            .itemOutputs(ItemList.Cell_Empty.get(4))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.NickelTetracarbonyl, FluidShapes.fluidLiquid, 1_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Nickel, Shapes.dust, 1))
            .circuit(1)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, FluidShapes.fluidGas, 4_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.NickelTetracarbonyl, FluidShapes.fluidLiquid, 1_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Nickel, Shapes.dust, 1), ItemList.Cell_Empty.get(1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.NickelTetracarbonyl, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, FluidShapes.fluidGas, 4_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(chemicalReactorRecipes);

        // C2H4O + H2O = C2H6O2

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.EthyleneOxide, 1))
            .circuit(1)
            .itemOutputs(ItemList.Cell_Empty.get(1))
            .fluidInputs(GTUtility.getWater(1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.EthyleneGlycol, FluidShapes.fluidLiquid, 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // C2H4 + O = C2H4O

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Ethylene, 2))
            .circuit(4)
            .itemOutputs(ItemList.Cell_Empty.get(2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Acetaldehyde, FluidShapes.fluidGas, 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Oxygen, 1))
            .circuit(5)
            .itemOutputs(ItemList.Cell_Empty.get(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Acetaldehyde, FluidShapes.fluidGas, 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        // NiAl3 + 2NaOH + 2H2O = NiAl + 2NaAlO2 + 6H

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.NickelAluminide, Shapes.ingot, 4),
                MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 6))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.RaneyNickelActivated, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.SodiumAluminate, Shapes.dust, 8))
            .fluidInputs(GTUtility.getWater(2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 6_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);

        // Cu + O = CuO

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Copper, Shapes.dust, 1),
                MaterialParts.requireCell(Materials.Oxygen, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.CupricOxide, Shapes.dust, 2), ItemList.Cell_Empty.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Copper, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.CupricOxide, Shapes.dust, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // 2Bi + 3O = Bi2O3

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Bismuth, Shapes.dust, 4),
                MaterialParts.requireCell(Materials.Oxygen, 6))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.BismuthIIIOxide, Shapes.dust, 10),
                ItemList.Cell_Empty.get(6))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Bismuth, Shapes.dust, 4))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.BismuthIIIOxide, Shapes.dust, 10))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 6_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalReactorRecipes);

        // C4H6O2 + CNH5 = C5H9NO + H2O

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialParts.requireCell(Materials.Methylamine, 1),
                MaterialLibAPI.getStack(Materials.GammaButyrolactone, CellShapes.cell, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.NMethylpyrolidone, CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, 8))
            .circuit(2)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 16_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SulfurDichloride, FluidShapes.fluidLiquid, 8_000))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // SCl2 + SO3 = SO2 + SOCl2

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialParts.requireCell(Materials.SulfurTrioxide, 1),
                MaterialLibAPI.getStack(Materials.SulfurDichloride, CellShapes.cell, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.ThionylChloride, CellShapes.cell, 1),
                ItemList.Cell_Empty.get(1))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SulfurDioxide, FluidShapes.fluidGas, 1_000))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // C8H10 + 6O =CoC22H14O4= C8H6O4 + 2H2O

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials._14Dimethylbenzene, CellShapes.cell, 1),
                MaterialLibAPI.getStack(Materials.CobaltIINaphthenate, Shapes.dust, 41))
            .itemOutputs(MaterialLibAPI.getStack(Materials.TerephthalicAcid, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 6_000))
            .fluidOutputs(GTUtility.getWater(2_000))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // 2CH4 + C6H6 = C8H10 + 4H

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Methane, 2))
            .circuit(13)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials._13Dimethylbenzene, CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 4_000))
            .duration(3 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Benzene, CellShapes.cell, 1))
            .circuit(14)
            .itemOutputs(MaterialLibAPI.getStack(Materials._13Dimethylbenzene, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 4_000))
            .duration(3 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        // 2CH4 + C6H6 = C8H10 + 4H

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Methane, 2))
            .circuit(15)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials._14Dimethylbenzene, CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 4_000))
            .duration(3 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Benzene, CellShapes.cell, 1))
            .circuit(16)
            .itemOutputs(MaterialLibAPI.getStack(Materials._14Dimethylbenzene, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 4_000))
            .duration(3 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.CobaltIIHydroxide, Shapes.dust, 5))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.CobaltIINaphthenate, Shapes.dust, 41))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.NaphthenicAcid, FluidShapes.fluidLiquid, 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.CobaltIIAcetate, Shapes.dust, 15))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.CobaltIINaphthenate, Shapes.dust, 41))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.NaphthenicAcid, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.AceticAcid, FluidShapes.fluidLiquid, 1_500))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // Co + 2HNO3 = Co(NO3)2 + 2H

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Cobalt, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.NitricAcid, CellShapes.cell, 2))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.CobaltIINitrate, Shapes.dust, 9),
                MaterialParts.requireCell(Materials.Hydrogen, 2))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        // Co(NO3)2 + 2KOH = CoH2O2 + 2KNO3

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.CobaltIINitrate, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.PotassiumHydroxideGT5U, Shapes.dust, 6))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.CobaltIIHydroxide, Shapes.dust, 5),
                MaterialLibAPI.getStack(Materials.Saltpeter, Shapes.dust, 10))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        // CoO + 2C2H4O2 = CoC4H6O4 + 2H

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.CobaltOxide, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.AceticAcid, CellShapes.cell, 2))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.CobaltIIAcetate, Shapes.dust, 15),
                ItemList.Cell_Empty.get(2))
            .fluidOutputs(GTUtility.getWater(2_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Phosphorus, Shapes.dust, 1))
            .circuit(1)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 3_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.PhosphorusTrichloride, FluidShapes.fluidLiquid, 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Phosphorus, Shapes.dust, 9))
            .circuit(9)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 27_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.PhosphorusTrichloride, FluidShapes.fluidLiquid, 9_000))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        // Na + H = NaH

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Sodium, Shapes.dust, 1))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials.SodiumHydride, Shapes.dust, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        // CH3ONa + H2O = CH4O + NaOH

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SodiumMethoxide, Shapes.dust, 6))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 3))
            .fluidInputs(GTUtility.getWater(1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // K + HNO3 = KNO3 + H (not real, but gameplay

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Potassium, Shapes.dust, 1))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Saltpeter, Shapes.dust, 5))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.NitricAcid, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // CH3COOH + CH3OH = CH3COOCH3 + H2O

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.AceticAcid, CellShapes.cell, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.MethylAcetate, FluidShapes.fluidLiquid, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Methanol, CellShapes.cell, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.AceticAcid, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.MethylAcetate, FluidShapes.fluidLiquid, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.AceticAcid, CellShapes.cell, 1))
            .circuit(2)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.MethylAcetate, FluidShapes.fluidLiquid, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Methanol, CellShapes.cell, 1))
            .circuit(2)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.AceticAcid, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.MethylAcetate, FluidShapes.fluidLiquid, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.AceticAcid, CellShapes.cell, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials.MethylAcetate, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(GTUtility.getWater(1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Methanol, CellShapes.cell, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials.MethylAcetate, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.AceticAcid, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(GTUtility.getWater(1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.AceticAcid, CellShapes.cell, 1))
            .circuit(12)
            .itemOutputs(MaterialLibAPI.getStack(Materials.MethylAcetate, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Methanol, CellShapes.cell, 1))
            .circuit(12)
            .itemOutputs(MaterialLibAPI.getStack(Materials.MethylAcetate, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.AceticAcid, FluidShapes.fluidLiquid, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // CO and CO2 recipes

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, 1))
            .circuit(1)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, FluidShapes.fluidGas, 1_000))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Ash, Shapes.dustTiny, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, FluidShapes.fluidGas, 1_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Coal, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Ash, Shapes.dustTiny, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, FluidShapes.fluidGas, 1_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Ash, Shapes.dustTiny, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, FluidShapes.fluidGas, 1_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Charcoal, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Ash, Shapes.dustTiny, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, FluidShapes.fluidGas, 1_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, 1))
            .circuit(2)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, 1_000))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 1))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Ash, Shapes.dustTiny, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, 1_000))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Coal, Shapes.dust, 1))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Ash, Shapes.dustTiny, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, 1_000))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 1))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Ash, Shapes.dustTiny, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, 1_000))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Charcoal, Shapes.dust, 1))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Ash, Shapes.dustTiny, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, 1_000))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, FluidShapes.fluidGas, 2_000))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 9))
            .circuit(9)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 9_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, FluidShapes.fluidGas, 9_000))
            .duration(36 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Coal, Shapes.dust, 9))
            .circuit(9)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 9_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, FluidShapes.fluidGas, 9_000))
            .duration(36 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 9))
            .circuit(9)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 9_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, FluidShapes.fluidGas, 9_000))
            .duration(36 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Charcoal, Shapes.dust, 9))
            .circuit(9)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 9_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, FluidShapes.fluidGas, 9_000))
            .duration(36 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 9))
            .circuit(8)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 18_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, 9_000))
            .duration(18 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Coal, Shapes.dust, 9))
            .circuit(8)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 18_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, 9_000))
            .duration(18 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 9))
            .circuit(8)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 18_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, 9_000))
            .duration(18 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Charcoal, Shapes.dust, 9))
            .circuit(8)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Ash, Shapes.dust, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 18_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, 9_000))
            .duration(18 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        // CO + 4H = CH3OH

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.CarbonMonoxide, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 4_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, 1_000))
            .duration(6 * SECONDS)
            .eut(96)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Hydrogen, 4))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 4))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, 1_000))
            .duration(6 * SECONDS)
            .eut(96)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.CarbonMonoxide, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Methanol, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 4_000))
            .duration(6 * SECONDS)
            .eut(96)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Hydrogen, 4))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Methanol, CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 3))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, FluidShapes.fluidGas, 1_000))
            .duration(6 * SECONDS)
            .eut(96)
            .addTo(UniversalChemical);

        // CO2 + 6H = CH3OH + H2O

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.CarbonDioxide, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 6_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, 1_000))
            .duration(6 * SECONDS)
            .eut(96)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Hydrogen, 6))
            .circuit(1)
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 5))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, 1_000))
            .duration(6 * SECONDS)
            .eut(96)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.CarbonDioxide, 1))
            .circuit(2)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 6_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, 1_000))
            .duration(6 * SECONDS)
            .eut(96)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Hydrogen, 6))
            .circuit(2)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 6))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, 1_000))
            .duration(6 * SECONDS)
            .eut(96)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.CarbonDioxide, 1))
            .circuit(12)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Methanol, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 6_000))
            .duration(6 * SECONDS)
            .eut(96)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Hydrogen, 6))
            .circuit(12)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Methanol, CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 5))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, 1_000))
            .duration(6 * SECONDS)
            .eut(96)
            .addTo(UniversalChemical);

        // CH3OH + CO = CH3COOH

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Methanol, CellShapes.cell, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.AceticAcid, FluidShapes.fluidLiquid, 1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.CarbonMonoxide, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.AceticAcid, FluidShapes.fluidLiquid, 1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Methanol, CellShapes.cell, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials.AceticAcid, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, FluidShapes.fluidGas, 1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.CarbonMonoxide, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials.AceticAcid, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, 1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // CH2CH2 + 2O = CH3COOH

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Ethylene, 1))
            .circuit(8)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.AceticAcid, FluidShapes.fluidLiquid, 1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Oxygen, 2))
            .circuit(8)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.AceticAcid, FluidShapes.fluidLiquid, 1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Ethylene, 1))
            .circuit(19)
            .itemOutputs(MaterialLibAPI.getStack(Materials.AceticAcid, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 2_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Oxygen, 2))
            .circuit(19)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.AceticAcid, CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // O + C2H4O2 + C2H4 = C4H6O2 + H2O

        GTValues.RA.stdBuilder()
            .circuit(4)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials.AceticAcid, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.VinylAcetate, FluidShapes.fluidLiquid, 1_000),
                GTUtility.getWater(1_000))
            .duration(9 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialParts.requireCell(Materials.Ethylene, 1),
                MaterialLibAPI.getStack(Materials.AceticAcid, CellShapes.cell, 1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.VinylAcetate, FluidShapes.fluidLiquid, 1_000))
            .duration(9 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.AceticAcid, CellShapes.cell, 1),
                MaterialParts.requireCell(Materials.Oxygen, 1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.VinylAcetate, FluidShapes.fluidLiquid, 1_000))
            .duration(9 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialParts.requireCell(Materials.Oxygen, 1),
                MaterialParts.requireCell(Materials.Ethylene, 1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.AceticAcid, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.VinylAcetate, FluidShapes.fluidLiquid, 1_000))
            .duration(9 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // Ethanol -> Ethylene (Intended loss for Sulfuric Acid)

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Ethanol, CellShapes.cell, 1))
            .circuit(1)
            .itemOutputs(MaterialParts.requireCell(Materials.Ethylene, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.DilutedSulfuricAcid, FluidShapes.fluidLiquid, 1_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SulfuricAcid, CellShapes.cell, 1))
            .circuit(1)
            .itemOutputs(MaterialParts.requireCell(Materials.Ethylene, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Ethanol, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.DilutedSulfuricAcid, FluidShapes.fluidLiquid, 1_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Ethanol, CellShapes.cell, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials.DilutedSulfuricAcid, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 1_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SulfuricAcid, CellShapes.cell, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials.DilutedSulfuricAcid, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Ethanol, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 1_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        // H2O + Na = NaOH + H

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Sodium, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 3))
            .fluidInputs(GTUtility.getWater(1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // H2O + K = KOH + H

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Potassium, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.PotassiumHydroxideGT5U, Shapes.dust, 3))
            .fluidInputs(GTUtility.getWater(1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // H2O + Cs = CsOH + H

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Caesium, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.CaesiumHydroxideGT5U, Shapes.dust, 3))
            .fluidInputs(GTUtility.getWater(1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // H + Cl = HCl

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Chlorine, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Hydrogen, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Chlorine, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 1_000))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Hydrogen, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 1_000))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        // NaOH + HCl = NaCl + H2O

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 3))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Salt, Shapes.dust, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(GTUtility.getWater(1000))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        // NaOH + 2 (HCl)(H2O) = 2 H2O + 2 NaCl
        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemInputs(MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 3))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Salt, Shapes.dust, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.DilutedHydrochloricAcidGT5U, FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(GTUtility.getWater(2000))
            .duration(3 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // C3H6 + 2Cl = HCl + C3H5Cl

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Chlorine, 2))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.AllylChloride, FluidShapes.fluidLiquid, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Propene, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.AllylChloride, FluidShapes.fluidLiquid, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Chlorine, 2))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.AllylChloride, CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Propene, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials.AllylChloride, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // 2Cl + H2O = HCl + HClO (Intended loss)

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Chlorine, 2))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.DilutedHydrochloricAcidGT5U, CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(GTUtility.getWater(1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HypochlorousAcid, FluidShapes.fluidLiquid, 1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.DilutedHydrochloricAcidGT5U, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HypochlorousAcid, FluidShapes.fluidLiquid, 1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Chlorine, 2))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.HypochlorousAcid, CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(GTUtility.getWater(1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.DilutedHydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials.HypochlorousAcid, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.DilutedHydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // HClO + NaOH + C3H5Cl = C3H5ClO + NaCl·H2O

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.HypochlorousAcid, CellShapes.cell, 1),
                MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 3))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SaltWater, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.AllylChloride, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Epichlorohydrin, FluidShapes.fluidLiquid, 1_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Ba + H2SO4 = BaSO4 + H2
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Barium, Shapes.dust, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 1000))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Barite, Shapes.dust, 6))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 2000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.AllylChloride, CellShapes.cell, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SaltWater, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HypochlorousAcid, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Epichlorohydrin, FluidShapes.fluidLiquid, 1_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Glycerol, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Epichlorohydrin, FluidShapes.fluidLiquid, 1_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Glycerol, CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Epichlorohydrin, FluidShapes.fluidLiquid, 1_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Epichlorohydrin, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Glycerol, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(GTUtility.getWater(2_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Glycerol, CellShapes.cell, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Epichlorohydrin, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(GTUtility.getWater(2_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 1))
            .circuit(2)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Glycerol, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Epichlorohydrin, FluidShapes.fluidLiquid, 1_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Glycerol, CellShapes.cell, 1))
            .circuit(2)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Epichlorohydrin, FluidShapes.fluidLiquid, 1_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 1))
            .circuit(12)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Epichlorohydrin, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Glycerol, FluidShapes.fluidLiquid, 1_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Glycerol, CellShapes.cell, 1))
            .circuit(12)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Epichlorohydrin, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // P4O10 + 6H2O = 4H3PO4

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.PhosphorousPentoxide, Shapes.dust, 14))
            .fluidInputs(GTUtility.getWater(6_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.PhosphoricAcidGT5U, FluidShapes.fluidLiquid, 4_000))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // C9H12 + 2O = C6H6O + C3H6O

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Isopropylbenzene, CellShapes.cell, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Phenol, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Acetone, FluidShapes.fluidLiquid, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Oxygen, 2))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Phenol, CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Isopropylbenzene, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Acetone, FluidShapes.fluidLiquid, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Isopropylbenzene, CellShapes.cell, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Acetone, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Phenol, FluidShapes.fluidLiquid, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Oxygen, 2))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Acetone, CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Isopropylbenzene, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Phenol, FluidShapes.fluidLiquid, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // C15H16O2 + 2C3H5ClO + 2NaOH = C15H14O2(C3H5O)2 + 2NaCl·H2O

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 6),
                MaterialLibAPI.getStack(Materials.Epichlorohydrin, CellShapes.cell, 2))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SaltWater, CellShapes.cell, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.BisphenolA, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Epoxid, FluidShapes.fluidMolten, 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // CH4O + HCl = CH3Cl + H2O

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Methanol, CellShapes.cell, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Chloromethane, FluidShapes.fluidGas, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Chloromethane, FluidShapes.fluidGas, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Methanol, CellShapes.cell, 1))
            .circuit(11)
            .itemOutputs(MaterialParts.requireCell(Materials.Chloromethane, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(GTUtility.getWater(1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 1))
            .circuit(11)
            .itemOutputs(MaterialParts.requireCell(Materials.Chloromethane, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(GTUtility.getWater(1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Methanol, CellShapes.cell, 1))
            .circuit(2)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Chloromethane, FluidShapes.fluidGas, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 1))
            .circuit(2)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Chloromethane, FluidShapes.fluidGas, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Methanol, CellShapes.cell, 1))
            .circuit(12)
            .itemOutputs(MaterialParts.requireCell(Materials.Chloromethane, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 1))
            .circuit(12)
            .itemOutputs(MaterialParts.requireCell(Materials.Chloromethane, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Chlorine, 2))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Chloromethane, FluidShapes.fluidGas, 1_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Methane, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Chloromethane, FluidShapes.fluidGas, 1_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Chlorine, 2))
            .circuit(11)
            .itemOutputs(
                MaterialParts.requireCell(Materials.Chloromethane, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Methane, 1))
            .circuit(11)
            .itemOutputs(MaterialParts.requireCell(Materials.Chloromethane, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Cl6 + CH4 = CHCl3 + 3HCl

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Chlorine, 6))
            .circuit(3)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 3),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 3))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Chloroform, FluidShapes.fluidLiquid, 1_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Chlorine, 6))
            .circuit(13)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Chloroform, CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 5))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 3_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Methane, 1))
            .circuit(13)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Chloroform, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 6_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 3_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // H + F = HF

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Fluorine, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HydrofluoricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Hydrogen, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Fluorine, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HydrofluoricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Fluorine, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials.HydrofluoricAcidGT5U, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 1_000))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Hydrogen, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials.HydrofluoricAcidGT5U, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Fluorine, FluidShapes.fluidGas, 1_000))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        // 4HF + 2CHCl3 = C2F4 + 6HCl

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Chloroform, CellShapes.cell, 2),
                MaterialLibAPI.getStack(Materials.HydrofluoricAcidGT5U, CellShapes.cell, 4))
            .itemOutputs(MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 6))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Tetrafluoroethylene, FluidShapes.fluidGas, 1_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Chloroform, CellShapes.cell, 2),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 4))
            .itemOutputs(MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 6))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydrofluoricAcidGT5U, FluidShapes.fluidLiquid, 4_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Tetrafluoroethylene, FluidShapes.fluidGas, 1_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.HydrofluoricAcidGT5U, CellShapes.cell, 4),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .itemOutputs(MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 6))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chloroform, FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Tetrafluoroethylene, FluidShapes.fluidGas, 1_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.HydrofluoricAcidGT5U, CellShapes.cell, 4))
            .circuit(11)
            .itemOutputs(
                MaterialParts.requireCell(Materials.Tetrafluoroethylene, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 3))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chloroform, FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 6_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Chloroform, CellShapes.cell, 2))
            .circuit(11)
            .itemOutputs(
                MaterialParts.requireCell(Materials.Tetrafluoroethylene, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydrofluoricAcidGT5U, FluidShapes.fluidLiquid, 4_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 6_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        // Si + 2CH3Cl = C2H6Cl2Si

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Silicon, Shapes.dust, 1))
            .circuit(1)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chloromethane, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Dimethyldichlorosilane, FluidShapes.fluidLiquid, 1_000))
            .duration(12 * SECONDS)
            .eut(96)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Dimethyldichlorosilane, CellShapes.cell, 1))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Polydimethylsiloxane, Shapes.dust, 3),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(GTUtility.getWater(1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.DilutedHydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .duration(12 * SECONDS)
            .eut(96)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Polydimethylsiloxane, Shapes.dust, 3),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Dimethyldichlorosilane, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.DilutedHydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .duration(12 * SECONDS)
            .eut(96)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Dimethyldichlorosilane, CellShapes.cell, 1))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Polydimethylsiloxane, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.DilutedHydrochloricAcidGT5U, CellShapes.cell, 1))
            .fluidInputs(GTUtility.getWater(1_000))
            .duration(12 * SECONDS)
            .eut(96)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Polydimethylsiloxane, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.DilutedHydrochloricAcidGT5U, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Dimethyldichlorosilane, FluidShapes.fluidLiquid, 1_000))
            .duration(12 * SECONDS)
            .eut(96)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Polydimethylsiloxane, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, 1))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Silicone, FluidShapes.fluidMolten, 9 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Potassium Nitride
        // K + HNO3 = KNO3 + H

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Potassium, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.PotassiumNitrate, Shapes.dust, 5))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.NitricAcid, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Chromium Trioxide
        // CrO2 + O = CrO3

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.ChromiumDioxide, Shapes.dust, 3))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Chromiumtrioxide, Shapes.dust, 4))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        // Nitrochlorobenzene
        // C6H5Cl + HNO3 = C6H4ClNO2 + H2O

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Chlorobenzene, CellShapes.cell, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials._2Nitrochlorobenzene, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.NitrationMixture, FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.DilutedSulfuricAcid, FluidShapes.fluidLiquid, 1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Chlorobenzene, CellShapes.cell, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials.DilutedSulfuricAcid, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.NitrationMixture, FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials._2Nitrochlorobenzene, FluidShapes.fluidLiquid, 1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.NitrationMixture, CellShapes.cell, 2))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials._2Nitrochlorobenzene, CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chlorobenzene, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.DilutedSulfuricAcid, FluidShapes.fluidLiquid, 1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.NitrationMixture, CellShapes.cell, 2))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.DilutedSulfuricAcid, CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chlorobenzene, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials._2Nitrochlorobenzene, FluidShapes.fluidLiquid, 1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // C6H6 + 2CH4 = C8H10 + 4H

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Methane, 2))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Dimethylbenzene, CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 4_000))
            .duration(3 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Benzene, CellShapes.cell, 1))
            .circuit(12)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Dimethylbenzene, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 4_000))
            .duration(3 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        // Phthalic Acid
        // C8H10 + 6O =K2Cr2O7= C8H6O4 + 2H2O

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Dimethylbenzene, CellShapes.cell, 1),
                MaterialLibAPI.getStack(Materials.PotassiumDichromate, Shapes.dustTiny, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.phtalicacid, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 6_000))
            .fluidOutputs(GTUtility.getWater(2_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialParts.requireCell(Materials.Oxygen, 6),
                MaterialLibAPI.getStack(Materials.PotassiumDichromate, Shapes.dustTiny, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.phtalicacid, CellShapes.cell, 1), ItemList.Cell_Empty.get(5))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Dimethylbenzene, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(GTUtility.getWater(2_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Dimethylbenzene, CellShapes.cell, 9),
                MaterialLibAPI.getStack(Materials.PotassiumDichromate, Shapes.dust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.phtalicacid, CellShapes.cell, 9))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 54_000))
            .fluidOutputs(GTUtility.getWater(18_000))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialParts.requireCell(Materials.Oxygen, 54),
                MaterialLibAPI.getStack(Materials.PotassiumDichromate, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.phtalicacid, CellShapes.cell, 9),
                ItemList.Cell_Empty.get(45))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Dimethylbenzene, FluidShapes.fluidLiquid, 9_000))
            .fluidOutputs(GTUtility.getWater(18_000))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(chemicalReactorRecipes);

        // These following recipes are broken in element term.
        // But they are kept in gamewise, too much existed setup will be broken.
        // Dichlorobenzidine

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Copper, Shapes.dustTiny, 1))
            .circuit(1)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials._2Nitrochlorobenzene, FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials._33Dichlorobenzidine, FluidShapes.fluidLiquid, 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Copper, Shapes.dust, 1))
            .circuit(9)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials._2Nitrochlorobenzene, FluidShapes.fluidLiquid, 18_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials._33Dichlorobenzidine, FluidShapes.fluidLiquid, 9_000))
            .duration(1 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);

        // Diphenyl Isophthalate

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.phtalicacid, CellShapes.cell, 1),
                MaterialLibAPI.getStack(Materials.SulfuricAcid, CellShapes.cell, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.DiphenylIsophtalate, CellShapes.cell, 1),
                ItemList.Cell_Empty.get(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Phenol, FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.DilutedSulfuricAcid, FluidShapes.fluidLiquid, 1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.phtalicacid, CellShapes.cell, 1),
                MaterialLibAPI.getStack(Materials.Phenol, CellShapes.cell, 2))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.DiphenylIsophtalate, CellShapes.cell, 1),
                ItemList.Cell_Empty.get(2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.DilutedSulfuricAcid, FluidShapes.fluidLiquid, 1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SulfuricAcid, CellShapes.cell, 1),
                MaterialLibAPI.getStack(Materials.Phenol, CellShapes.cell, 2))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.DiphenylIsophtalate, CellShapes.cell, 1),
                ItemList.Cell_Empty.get(2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.phtalicacid, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.DilutedSulfuricAcid, FluidShapes.fluidLiquid, 1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(chemicalReactorRecipes);

        // Diaminobenzidin

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialParts.requireCell(Materials.Ammonia, 2),
                MaterialLibAPI.getStack(Materials.Zinc, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials._33Diaminobenzidine, CellShapes.cell, 1),
                ItemList.Cell_Empty.get(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials._33Dichlorobenzidine, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 2_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(UniversalChemical);

        // Polybenzimidazole
        // C12H14N4 + C20H14O4 = C20H12N4 + 2C6H6O + 2H2O

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.DiphenylIsophtalate, CellShapes.cell, 1),
                MaterialLibAPI.getStack(Materials._33Diaminobenzidine, CellShapes.cell, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Phenol, CellShapes.cell, 2))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Polybenzimidazole, FluidShapes.fluidMolten, 1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Tin, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Saltpeter, Shapes.dust, 1))
            .itemOutputs(getModItem(Railcraft.ID, "glass", 6))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Glass, FluidShapes.fluidMolten, 6 * INGOTS))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // NH3 + 2CH4O = C2H7N + 2H2O

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Methanol, CellShapes.cell, 2))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Dimethylamine, FluidShapes.fluidGas, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Methanol, CellShapes.cell, 2))
            .circuit(11)
            .itemOutputs(
                MaterialParts.requireCell(Materials.Dimethylamine, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(GTUtility.getWater(1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Ammonia, 1))
            .circuit(11)
            .itemOutputs(MaterialParts.requireCell(Materials.Dimethylamine, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(GTUtility.getWater(1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Methanol, CellShapes.cell, 2))
            .circuit(2)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Dimethylamine, FluidShapes.fluidGas, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Methanol, CellShapes.cell, 2))
            .circuit(12)
            .itemOutputs(
                MaterialParts.requireCell(Materials.Dimethylamine, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Ammonia, 1))
            .circuit(12)
            .itemOutputs(MaterialParts.requireCell(Materials.Dimethylamine, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, 2_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        // NH3 + HClO = NH2Cl + H2O

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Ammonia, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HypochlorousAcid, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Chloramine, FluidShapes.fluidLiquid, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.HypochlorousAcid, CellShapes.cell, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Chloramine, FluidShapes.fluidLiquid, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Ammonia, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Chloramine, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HypochlorousAcid, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(GTUtility.getWater(1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.HypochlorousAcid, CellShapes.cell, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Chloramine, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(GTUtility.getWater(1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Ammonia, 1))
            .circuit(2)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HypochlorousAcid, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Chloramine, FluidShapes.fluidLiquid, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.HypochlorousAcid, CellShapes.cell, 1))
            .circuit(2)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Chloramine, FluidShapes.fluidLiquid, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Ammonia, 1))
            .circuit(12)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Chloramine, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HypochlorousAcid, FluidShapes.fluidLiquid, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.HypochlorousAcid, CellShapes.cell, 1))
            .circuit(12)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Chloramine, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // 2NO2 = N2O4

        GTValues.RA.stdBuilder()
            .circuit(6)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.NitrogenDioxide, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.DinitrogenTetroxide, FluidShapes.fluidGas, 1_000))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.NitrogenDioxide, 2))
            .circuit(2)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.DinitrogenTetroxide, FluidShapes.fluidGas, 1_000))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.NitrogenDioxide, 2))
            .circuit(12)
            .itemOutputs(
                MaterialParts.requireCell(Materials.DinitrogenTetroxide, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // 2NH3 + 5O = 2NO + 3H2O

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Ammonia, 4))
            .circuit(1)
            .itemOutputs(MaterialParts.requireCell(Materials.NitricOxide, 4))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 10_000))
            .fluidOutputs(GTUtility.getWater(6_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Oxygen, 10))
            .circuit(1)
            .itemOutputs(
                MaterialParts.requireCell(Materials.NitricOxide, 4),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 6))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, 4_000))
            .fluidOutputs(GTUtility.getWater(6_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Oxygen, 10))
            .circuit(11)
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 6),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 4))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, 4_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.NitricOxide, FluidShapes.fluidGas, 4_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Ammonia, 4))
            .circuit(2)
            .itemOutputs(MaterialParts.requireCell(Materials.NitricOxide, 4))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 10_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Oxygen, 10))
            .circuit(2)
            .itemOutputs(
                MaterialParts.requireCell(Materials.NitricOxide, 4),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 6))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, 4_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Oxygen, 10))
            .circuit(12)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 10))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, 4_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.NitricOxide, FluidShapes.fluidGas, 4_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // NO + O = NO2

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.NitricOxide, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.NitrogenDioxide, FluidShapes.fluidGas, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Oxygen, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.NitricOxide, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.NitrogenDioxide, FluidShapes.fluidGas, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.NitricOxide, 1))
            .circuit(11)
            .itemOutputs(MaterialParts.requireCell(Materials.NitrogenDioxide, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Oxygen, 1))
            .circuit(11)
            .itemOutputs(MaterialParts.requireCell(Materials.NitrogenDioxide, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.NitricOxide, FluidShapes.fluidGas, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // H2O + 3NO2 = 2HNO3 + NO

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1))
            .circuit(1)
            .itemOutputs(MaterialParts.requireCell(Materials.NitricOxide, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.NitrogenDioxide, FluidShapes.fluidGas, 3_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.NitricAcid, FluidShapes.fluidLiquid, 2_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.NitrogenDioxide, 3))
            .circuit(1)
            .itemOutputs(
                MaterialParts.requireCell(Materials.NitricOxide, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .fluidInputs(GTUtility.getWater(1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.NitricAcid, FluidShapes.fluidLiquid, 2_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.NitrogenDioxide, 3))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.NitricAcid, CellShapes.cell, 2),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(GTUtility.getWater(1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.NitricOxide, FluidShapes.fluidGas, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // S + 2H = H2S

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, 1))
            .circuit(1)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HydricSulfide, FluidShapes.fluidGas, 1_000))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        // S + 2O = SO2

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, 1))
            .circuit(3)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SulfurDioxide, FluidShapes.fluidGas, 1_000))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        // H2S + 3O = SO2 + H2O

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.HydricSulfide, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 3_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SulfurDioxide, FluidShapes.fluidGas, 1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Oxygen, 3))
            .circuit(1)
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydricSulfide, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SulfurDioxide, FluidShapes.fluidGas, 1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.HydricSulfide, 1))
            .circuit(11)
            .itemOutputs(MaterialParts.requireCell(Materials.SulfurDioxide, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 3_000))
            .fluidOutputs(GTUtility.getWater(1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Oxygen, 3))
            .circuit(11)
            .itemOutputs(
                MaterialParts.requireCell(Materials.SulfurDioxide, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydricSulfide, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(GTUtility.getWater(1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.HydricSulfide, 1))
            .circuit(2)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 3_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SulfurDioxide, FluidShapes.fluidGas, 1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Oxygen, 3))
            .circuit(2)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 3))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydricSulfide, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SulfurDioxide, FluidShapes.fluidGas, 1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.HydricSulfide, 1))
            .circuit(12)
            .itemOutputs(MaterialParts.requireCell(Materials.SulfurDioxide, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 3_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Oxygen, 3))
            .circuit(12)
            .itemOutputs(
                MaterialParts.requireCell(Materials.SulfurDioxide, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydricSulfide, FluidShapes.fluidGas, 1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // 2H2S + SO2 = 3S + 2H2O

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.SulfurDioxide, 1))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, 3),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydricSulfide, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(GTUtility.getWater(2_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.HydricSulfide, 2))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, 3),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SulfurDioxide, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(GTUtility.getWater(2_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.SulfurDioxide, 1))
            .circuit(2)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, 3),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydricSulfide, FluidShapes.fluidGas, 2_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.HydricSulfide, 2))
            .circuit(2)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, 3),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SulfurDioxide, FluidShapes.fluidGas, 1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // SO2 + O = SO3

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Oxygen, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SulfurDioxide, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SulfurTrioxide, FluidShapes.fluidGas, 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.SulfurDioxide, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SulfurTrioxide, FluidShapes.fluidGas, 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Oxygen, 1))
            .circuit(11)
            .itemOutputs(MaterialParts.requireCell(Materials.SulfurTrioxide, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SulfurDioxide, FluidShapes.fluidGas, 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.SulfurDioxide, 1))
            .circuit(11)
            .itemOutputs(MaterialParts.requireCell(Materials.SulfurTrioxide, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // SO3 + H2O = H2SO4

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SulfurTrioxide, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 1_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.SulfurTrioxide, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(GTUtility.getWater(1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 1_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials.SulfuricAcid, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SulfurTrioxide, FluidShapes.fluidGas, 1_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.SulfurTrioxide, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials.SulfuricAcid, CellShapes.cell, 1))
            .fluidInputs(GTUtility.getWater(1_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(UniversalChemical);

        // C2H4 + 2Cl = C2H3Cl + HCl

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Chlorine, 2))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.VinylChloride, FluidShapes.fluidGas, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Ethylene, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.VinylChloride, FluidShapes.fluidGas, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Chlorine, 2))
            .circuit(11)
            .itemOutputs(
                MaterialParts.requireCell(Materials.VinylChloride, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Ethylene, 1))
            .circuit(11)
            .itemOutputs(MaterialParts.requireCell(Materials.VinylChloride, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // C2H4O2 =H2SO4= C2H2O + H2O

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.AceticAcid, CellShapes.cell, 1))
            .circuit(1)
            .itemOutputs(MaterialParts.requireCell(Materials.Ethenone, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.DilutedSulfuricAcid, FluidShapes.fluidLiquid, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SulfuricAcid, CellShapes.cell, 1))
            .circuit(1)
            .itemOutputs(MaterialParts.requireCell(Materials.Ethenone, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.AceticAcid, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.DilutedSulfuricAcid, FluidShapes.fluidLiquid, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.AceticAcid, CellShapes.cell, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials.DilutedSulfuricAcid, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Ethenone, FluidShapes.fluidGas, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SulfuricAcid, CellShapes.cell, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials.DilutedSulfuricAcid, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.AceticAcid, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Ethenone, FluidShapes.fluidGas, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        // C2H2O + 8HNO3 = 2CN4O8 + 9H2O
        // Chemically this recipe is wrong, but kept for minimizing breaking change.

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialParts.requireCell(Materials.Ethenone, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Tetranitromethane, CellShapes.cell, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.NitricAcid, FluidShapes.fluidLiquid, 8_000))
            .fluidOutputs(GTUtility.getWater(9_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Ethenone, 1))
            .circuit(12)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.NitricAcid, FluidShapes.fluidLiquid, 8_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Tetranitromethane, FluidShapes.fluidLiquid, 2_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.NitricAcid, CellShapes.cell, 8))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Tetranitromethane, CellShapes.cell, 2),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 6))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Ethenone, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(GTUtility.getWater(9_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.NitricAcid, CellShapes.cell, 8))
            .circuit(2)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Tetranitromethane, CellShapes.cell, 2),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 6))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Ethenone, FluidShapes.fluidGas, 1_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.NitricAcid, CellShapes.cell, 8))
            .circuit(12)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 8))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Ethenone, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Tetranitromethane, FluidShapes.fluidLiquid, 2_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.NitricAcid, CellShapes.cell, 8),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 9))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Ethenone, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Tetranitromethane, FluidShapes.fluidLiquid, 2_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialParts.requireCell(Materials.Ethenone, 1),
                MaterialLibAPI.getStack(Materials.NitricAcid, CellShapes.cell, 8))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 9))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Tetranitromethane, FluidShapes.fluidLiquid, 2_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalReactorRecipes);

        // C3H6 + C2H4 = C5H8 + 2H

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialParts.requireCell(Materials.Propene, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .itemOutputs(MaterialParts.requireCell(Materials.Hydrogen, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Isoprene, FluidShapes.fluidLiquid, 1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialParts.requireCell(Materials.Ethylene, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .itemOutputs(MaterialParts.requireCell(Materials.Hydrogen, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Isoprene, FluidShapes.fluidLiquid, 1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Propene, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Isoprene, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 2_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Ethylene, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Isoprene, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 2_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .circuit(5)
            .itemOutputs(MaterialParts.requireCell(Materials.Methane, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Isoprene, FluidShapes.fluidLiquid, 1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Propene, 2))
            .circuit(5)
            .itemOutputs(
                MaterialParts.requireCell(Materials.Methane, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Isoprene, FluidShapes.fluidLiquid, 1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .circuit(15)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Isoprene, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Propene, 2))
            .circuit(15)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Isoprene, CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Air.get(2))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.RawRubber, Shapes.dust, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Isoprene, FluidShapes.fluidLiquid, 144))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Oxygen, 2))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.RawRubber, Shapes.dust, 3),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Isoprene, FluidShapes.fluidLiquid, 288))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Isoprene, CellShapes.cell, 1))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.RawRubber, Shapes.dust, 7),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Air, FluidShapes.fluidGas, 14_000))
            .duration(56 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Isoprene, CellShapes.cell, 2))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.RawRubber, Shapes.dust, 21),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 14_000))
            .duration(1 * MINUTES + 52 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Benzene, CellShapes.cell, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Styrene, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 2_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Ethylene, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Styrene, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 2_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.RawStyreneButadieneRubber, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, 1))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.StyreneButadieneRubber, FluidShapes.fluidMolten, 9 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // C6H6 + 4Cl = C6H4Cl2 + 2HCl

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Benzene, CellShapes.cell, 1))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Dichlorobenzene, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 4_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 2_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Chlorine, 4))
            .circuit(2)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Dichlorobenzene, CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 3))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 2_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Chlorine, 4))
            .circuit(12)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 2),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Dichlorobenzene, FluidShapes.fluidLiquid, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SodiumSulfide, Shapes.dust, 3), ItemList.Cell_Air.get(16))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Salt, Shapes.dust, 4),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 16))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Dichlorobenzene, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.PolyphenyleneSulfide, FluidShapes.fluidMolten, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SodiumSulfide, Shapes.dust, 3),
                MaterialParts.requireCell(Materials.Oxygen, 8))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Salt, Shapes.dust, 4),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 8))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Dichlorobenzene, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.PolyphenyleneSulfide, FluidShapes.fluidMolten, 1_500))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // NaCl + H2SO4 = NaHSO4 + HCl

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Salt, Shapes.dust, 2))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.SodiumBisulfate, Shapes.dust, 7))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // NaOH + H2SO4 = NaHSO4 + H2O

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 3))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.SodiumBisulfate, Shapes.dust, 7))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(GTUtility.getWater(1_000))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Biodiesel recipes

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dustTiny, 1),
                MaterialLibAPI.getStack(Materials.Methanol, CellShapes.cell, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Glycerol, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SeedOil, FluidShapes.fluidLiquid, 6_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.BioDiesel, FluidShapes.fluidLiquid, 6_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dustTiny, 1),
                MaterialLibAPI.getStack(Materials.SeedOil, CellShapes.cell, 6))
            .itemOutputs(MaterialLibAPI.getStack(Materials.BioDiesel, CellShapes.cell, 6))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Glycerol, FluidShapes.fluidLiquid, 1_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dustTiny, 1),
                MaterialLibAPI.getStack(Materials.Methanol, CellShapes.cell, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Glycerol, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.FishOil, FluidShapes.fluidLiquid, 6_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.BioDiesel, FluidShapes.fluidLiquid, 6_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dustTiny, 1),
                MaterialLibAPI.getStack(Materials.FishOil, CellShapes.cell, 6))
            .itemOutputs(MaterialLibAPI.getStack(Materials.BioDiesel, CellShapes.cell, 6))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Glycerol, FluidShapes.fluidLiquid, 1_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dustTiny, 1),
                MaterialLibAPI.getStack(Materials.Ethanol, CellShapes.cell, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Glycerol, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SeedOil, FluidShapes.fluidLiquid, 6_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.BioDiesel, FluidShapes.fluidLiquid, 6_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dustTiny, 1),
                MaterialLibAPI.getStack(Materials.SeedOil, CellShapes.cell, 6))
            .itemOutputs(MaterialLibAPI.getStack(Materials.BioDiesel, CellShapes.cell, 6))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Ethanol, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Glycerol, FluidShapes.fluidLiquid, 1_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dustTiny, 1),
                MaterialLibAPI.getStack(Materials.Ethanol, CellShapes.cell, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Glycerol, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.FishOil, FluidShapes.fluidLiquid, 6_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.BioDiesel, FluidShapes.fluidLiquid, 6_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dustTiny, 1),
                MaterialLibAPI.getStack(Materials.FishOil, CellShapes.cell, 6))
            .itemOutputs(MaterialLibAPI.getStack(Materials.BioDiesel, CellShapes.cell, 6))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Ethanol, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Glycerol, FluidShapes.fluidLiquid, 1_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Methanol, CellShapes.cell, 9))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Glycerol, CellShapes.cell, 9))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SeedOil, FluidShapes.fluidLiquid, 54_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.BioDiesel, FluidShapes.fluidLiquid, 54_000))
            .duration(4 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.SeedOil, CellShapes.cell, 54))
            .itemOutputs(MaterialLibAPI.getStack(Materials.BioDiesel, CellShapes.cell, 54))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, 9_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Glycerol, FluidShapes.fluidLiquid, 9_000))
            .duration(4 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Methanol, CellShapes.cell, 9))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Glycerol, CellShapes.cell, 9))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.FishOil, FluidShapes.fluidLiquid, 54_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.BioDiesel, FluidShapes.fluidLiquid, 54_000))
            .duration(4 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.FishOil, CellShapes.cell, 54))
            .itemOutputs(MaterialLibAPI.getStack(Materials.BioDiesel, CellShapes.cell, 54))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, 9_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Glycerol, FluidShapes.fluidLiquid, 9_000))
            .duration(4 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Ethanol, CellShapes.cell, 9))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Glycerol, CellShapes.cell, 9))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SeedOil, FluidShapes.fluidLiquid, 54_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.BioDiesel, FluidShapes.fluidLiquid, 54_000))
            .duration(4 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.SeedOil, CellShapes.cell, 54))
            .itemOutputs(MaterialLibAPI.getStack(Materials.BioDiesel, CellShapes.cell, 54))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Ethanol, FluidShapes.fluidLiquid, 9_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Glycerol, FluidShapes.fluidLiquid, 9_000))
            .duration(4 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Ethanol, CellShapes.cell, 9))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Glycerol, CellShapes.cell, 9))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.FishOil, FluidShapes.fluidLiquid, 54_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.BioDiesel, FluidShapes.fluidLiquid, 54_000))
            .duration(4 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.FishOil, CellShapes.cell, 54))
            .itemOutputs(MaterialLibAPI.getStack(Materials.BioDiesel, CellShapes.cell, 54))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Ethanol, FluidShapes.fluidLiquid, 9_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Glycerol, FluidShapes.fluidLiquid, 9_000))
            .duration(4 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // C3H8O3 + 3HNO3 =H2SO4= C3H5N3O9 + 3H2O

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Glycerol, CellShapes.cell, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Glyceryl, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.NitrationMixture, FluidShapes.fluidLiquid, 6_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.DilutedSulfuricAcid, FluidShapes.fluidLiquid, 3_000))
            .duration(9 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.NitrationMixture, CellShapes.cell, 6))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Glyceryl, CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 5))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Glycerol, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.DilutedSulfuricAcid, FluidShapes.fluidLiquid, 3_000))
            .duration(9 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.NitrationMixture, CellShapes.cell, 6))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.DilutedSulfuricAcid, CellShapes.cell, 3),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 3))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Glycerol, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Glyceryl, FluidShapes.fluidLiquid, 1_000))
            .duration(9 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // CaO + CO2 = CaCO3

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Quicklime, Shapes.dust, 2))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Calcite, Shapes.dust, 5))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, 1_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Calcite, Shapes.dust, 5))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Quicklime, Shapes.dust, 2))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // MgO + CO2 = MgCO3

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Magnesia, Shapes.dust, 2))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Magnesite, Shapes.dust, 5))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, 1_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Magnesite, Shapes.dust, 5))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Magnesia, Shapes.dust, 2))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // C6H6 + 2Cl = C6H5Cl + HCl

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Benzene, CellShapes.cell, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Chlorobenzene, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Chlorine, 2))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Chlorobenzene, CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Chlorine, 2))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Chlorobenzene, FluidShapes.fluidLiquid, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // C6H5Cl + H2O = C6H6O + HCl

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.DilutedHydrochloricAcidGT5U, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chlorobenzene, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Phenol, FluidShapes.fluidLiquid, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Chlorobenzene, CellShapes.cell, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.DilutedHydrochloricAcidGT5U, CellShapes.cell, 1))
            .fluidInputs(GTUtility.getWater(1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Phenol, FluidShapes.fluidLiquid, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Phenol, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chlorobenzene, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.DilutedHydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Chlorobenzene, CellShapes.cell, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Phenol, CellShapes.cell, 1))
            .fluidInputs(GTUtility.getWater(1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.DilutedHydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // C6H5Cl + NaOH = C6H6O + NaCl

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 12))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Salt, Shapes.dust, 8))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chlorobenzene, FluidShapes.fluidLiquid, 4_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Phenol, FluidShapes.fluidLiquid, 4_000))
            .duration(48 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Oxide Recipe

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Antimony, Shapes.dust, 2))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials.AntimonyTrioxide, Shapes.dust, 5))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 3_000))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Lead, Shapes.dust, 1))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Massicot, Shapes.dust, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 1_000))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Arsenic, Shapes.dust, 2))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials.ArsenicTrioxide, Shapes.dust, 5))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 3_000))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Cobalt, Shapes.dust, 1))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials.CobaltOxide, Shapes.dust, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 1_000))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Zinc, Shapes.dust, 1))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Zincite, Shapes.dust, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 1_000))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // CaSi2 + 2HCl = 2Si + CaCl2 + 2H

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.CalciumDisilicide, Shapes.dust, 3))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Silicon, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.CalciumChloride, Shapes.dust, 3))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 2_000))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // SiCl4 + 2Zn = 2ZnCl2 + Si

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Zinc, Shapes.dust, 2))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.SiliconSolarGrade, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.ZincChloride, Shapes.dust, 6))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SiliconTetrachloride, FluidShapes.fluidLiquid, 1_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // C4H8O + 2H =Pd= C4H10O

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Butyraldehyde, CellShapes.cell, 1),
                MaterialLibAPI.getStack(Materials.Palladium, Shapes.dustTiny, 1))
            .itemOutputs(ItemList.Cell_Empty.get(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(new FluidStack(FluidRegistry.getFluid("butanol"), 1000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // 4CH2O + C2H4O =NaOH= C5H12O4 + CO

        GTValues.RA.stdBuilder()
            .itemInputs(
                getFilledCellFromFluidName(Formaldehyde, 4),
                MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Pentaerythritol, Shapes.dust, 21),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 4))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Acetaldehyde, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, FluidShapes.fluidGas, 1_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // 4CH2O + C2H4O =NaOH= C5H12O4 + CO

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialParts.requireCell(Materials.Acetaldehyde, 1),
                MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Pentaerythritol, Shapes.dust, 21),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("fluid.formaldehyde"), 4_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, FluidShapes.fluidGas, 1_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);

        // CaC2 + 2H2O = Ca(OH)2 + C2H2

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.CacliumCarbide, Shapes.dust, 3))
            .circuit(1)
            .itemOutputs(GregtechItemList.CalciumHydroxideDust.get(5))
            .fluidInputs(GTUtility.getWater(2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Acetylene, FluidShapes.fluidGas, 1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // Co(NO3)2 + 2NaOH = Co(OH)2 + 2NaNO3

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.CobaltIINitrate, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 6))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.CobaltIIHydroxide, Shapes.dust, 5),
                MaterialLibAPI.getStack(Materials.SodiumNitrate, Shapes.dust, 10))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        if (Forestry.isModLoaded()) {

            // Americium comb processing

            GTValues.RA.stdBuilder()
                .itemInputs(GTBees.combs.getStackForType(CombType.AMERICIUM, 4))
                .itemOutputs(MaterialLibAPI.getStack(Materials.Americium, Shapes.crushedPurified, 1))
                .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidPlasma, 8_175))
                .duration(30 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(UniversalChemical);

            GTValues.RA.stdBuilder()
                .itemInputs(GTBees.combs.getStackForType(CombType.AMERICIUM, 4))
                .itemOutputs(MaterialLibAPI.getStack(Materials.Americium, Shapes.crushedPurified, 2))
                .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Nitrogen, FluidShapes.fluidPlasma, 1_211))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_UV)
                .addTo(UniversalChemical);

            GTValues.RA.stdBuilder()
                .itemInputs(GTBees.combs.getStackForType(CombType.AMERICIUM, 4))
                .itemOutputs(MaterialLibAPI.getStack(Materials.Americium, Shapes.crushedPurified, 4))
                .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Silver, FluidShapes.fluidPlasma, 310))
                .duration(7 * SECONDS + 10 * TICKS)
                .eut(TierEU.RECIPE_UHV)
                .addTo(UniversalChemical);

            GTValues.RA.stdBuilder()
                .itemInputs(GTBees.combs.getStackForType(CombType.AMERICIUM, 4))
                .itemOutputs(MaterialLibAPI.getStack(Materials.Americium, Shapes.crushedPurified, 8))
                .fluidInputs(new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Bromine), 29))
                .duration(3 * SECONDS + 15 * TICKS)
                .eut(TierEU.RECIPE_UEV)
                .addTo(UniversalChemical);

            GTValues.RA.stdBuilder()
                .itemInputs(GTBees.combs.getStackForType(CombType.AMERICIUM, 4))
                .itemOutputs(MaterialLibAPI.getStack(Materials.Americium, Shapes.crushedPurified, 16))
                .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Thorium, FluidShapes.fluidPlasma, 68))
                .duration(1 * SECONDS + 17 * TICKS)
                .eut(TierEU.RECIPE_UIV)
                .addTo(UniversalChemical);

            // Europium comb processing

            GTValues.RA.stdBuilder()
                .itemInputs(GTBees.combs.getStackForType(CombType.EUROPIUM, 4))
                .itemOutputs(MaterialLibAPI.getStack(Materials.Europium, Shapes.crushedPurified, 1))
                .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidPlasma, 606))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(UniversalChemical);

            GTValues.RA.stdBuilder()
                .itemInputs(GTBees.combs.getStackForType(CombType.EUROPIUM, 4))
                .itemOutputs(MaterialLibAPI.getStack(Materials.Europium, Shapes.crushedPurified, 2))
                .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Nitrogen, FluidShapes.fluidPlasma, 180))
                .duration(7 * SECONDS + 10 * TICKS)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(UniversalChemical);

            GTValues.RA.stdBuilder()
                .itemInputs(GTBees.combs.getStackForType(CombType.EUROPIUM, 4))
                .itemOutputs(MaterialLibAPI.getStack(Materials.Europium, Shapes.crushedPurified, 4))
                .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Silver, FluidShapes.fluidPlasma, 54))
                .duration(3 * SECONDS + 15 * TICKS)
                .eut(TierEU.RECIPE_UV)
                .addTo(UniversalChemical);

            GTValues.RA.stdBuilder()
                .itemInputs(GTBees.combs.getStackForType(CombType.EUROPIUM, 4))
                .itemOutputs(MaterialLibAPI.getStack(Materials.Europium, Shapes.crushedPurified, 8))
                .fluidInputs(new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Bromine), 6))
                .duration(1 * SECONDS + 17 * TICKS)
                .eut(TierEU.RECIPE_UHV)
                .addTo(UniversalChemical);

            GTValues.RA.stdBuilder()
                .itemInputs(GTBees.combs.getStackForType(CombType.EUROPIUM, 4))
                .itemOutputs(MaterialLibAPI.getStack(Materials.Europium, Shapes.crushedPurified, 16))
                .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Thorium, FluidShapes.fluidPlasma, 18))
                .duration(18 * TICKS)
                .eut(TierEU.RECIPE_UEV)
                .addTo(UniversalChemical);

            GTValues.RA.stdBuilder()
                .itemInputs(GTBees.combs.getStackForType(CombType.INDIUM, 4))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Indium, 4))
                .fluidInputs(MaterialUtils.fluid(Materials.UUMatter, 293))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_UHV)
                .addTo(UniversalChemical);
        }
    }

    public void addDefaultPolymerizationRecipes(Fluid aBasicMaterial, Fluid aPolymer) {
        // Oxygen/Titaniumtetrafluoride -> +50% Output each
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Air.get(2))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .fluidInputs(new FluidStack(aBasicMaterial, 1 * INGOTS))
            .fluidOutputs(new FluidStack(aPolymer, 1 * INGOTS))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Oxygen, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(new FluidStack(aBasicMaterial, 1 * INGOTS))
            .fluidOutputs(new FluidStack(aPolymer, 3 * HALF_INGOTS))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Air.get(16))
            .circuit(9)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 16))
            .fluidInputs(new FluidStack(aBasicMaterial, 8 * INGOTS))
            .fluidOutputs(new FluidStack(aPolymer, 8 * INGOTS))
            .duration(56 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Oxygen, 8))
            .circuit(9)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 8))
            .fluidInputs(new FluidStack(aBasicMaterial, 8 * INGOTS))
            .fluidOutputs(new FluidStack(aPolymer, 12 * INGOTS))
            .duration(56 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(
                new FluidStack(aBasicMaterial, 15 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Air, FluidShapes.fluidGas, 7_500),
                MaterialLibAPI.getFluidStack(Materials.Titaniumtetrachloride, FluidShapes.fluidLiquid, 100))
            .fluidOutputs(new FluidStack(aPolymer, 3_240))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(
                new FluidStack(aBasicMaterial, 15 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 7_500),
                MaterialLibAPI.getFluidStack(Materials.Titaniumtetrachloride, FluidShapes.fluidLiquid, 100))
            .fluidOutputs(new FluidStack(aPolymer, 4_320))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

    }

    public void polymerizationRecipes() {
        addDefaultPolymerizationRecipes(
            MaterialUtils.fluidOf(Materials.VinylAcetate),
            MaterialUtils.fluidOf(Materials.PolyvinylAcetate));

        addDefaultPolymerizationRecipes(
            MaterialUtils.gas(Materials.Ethylene, 1)
                .getFluid(),
            MaterialUtils.molten(Materials.Plastic, 1)
                .getFluid());

        addDefaultPolymerizationRecipes(
            MaterialUtils.gas(Materials.Tetrafluoroethylene, 1)
                .getFluid(),
            MaterialUtils.molten(Materials.Polytetrafluoroethylene, 1)
                .getFluid());

        addDefaultPolymerizationRecipes(
            MaterialUtils.gas(Materials.VinylChloride, 1)
                .getFluid(),
            MaterialUtils.molten(Materials.PolyvinylChloride, 1)
                .getFluid());

        addDefaultPolymerizationRecipes(
            MaterialUtils.fluidOf(Materials.Styrene),
            MaterialUtils.molten(Materials.Polystyrene, 1)
                .getFluid());
    }

    public void singleBlockOnly() {

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.RawGasoline, CellShapes.cell, 10),
                MaterialLibAPI.getStack(Materials.Toluene, CellShapes.cell, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Gasoline, CellShapes.cell, 11))
            .duration(10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Benzene, CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .itemOutputs(MaterialParts.requireCell(Materials.Hydrogen, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Styrene, FluidShapes.fluidLiquid, 1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialParts.requireCell(Materials.Ethylene, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .itemOutputs(MaterialParts.requireCell(Materials.Hydrogen, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Styrene, FluidShapes.fluidLiquid, 1_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialParts.requireCell(Materials.Methane, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .itemOutputs(MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 3))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 6_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Chloroform, FluidShapes.fluidLiquid, 1_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Silicon, Shapes.dust, 1),
                MaterialParts.requireCell(Materials.Chloromethane, 2))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Dimethyldichlorosilane, FluidShapes.fluidLiquid, 1_000))
            .duration(12 * SECONDS)
            .eut(96)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Dimethyldichlorosilane, CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Polydimethylsiloxane, Shapes.dust, 3),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.DilutedHydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .duration(12 * SECONDS)
            .eut(96)
            .addTo(chemicalReactorRecipes);

        // Ca5(PO4)3Cl + 5H2SO4 + 10H2O = 5CaSO4(H2O)2 + HCl + 3H3PO4

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Apatite, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.SulfuricAcid, CellShapes.cell, 5))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 4))
            .fluidInputs(GTUtility.getWater(10_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.PhosphoricAcidGT5U, FluidShapes.fluidLiquid, 3_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // 10O + 4P = P4O10

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Phosphorus, Shapes.dust, 4))
            .itemOutputs(MaterialLibAPI.getStack(Materials.PhosphorousPentoxide, Shapes.dust, 14))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 10_000))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // HCl + C3H8O3 = C3H5ClO + 2H2O

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 1),
                MaterialLibAPI.getStack(Materials.Glycerol, CellShapes.cell, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 2))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Epichlorohydrin, FluidShapes.fluidLiquid, 1_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // H2O + Cl =Hg= HClO + H

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialParts.requireCell(Materials.Chlorine, 10),
                MaterialLibAPI.getStack(Materials.Mercury, CellShapes.cell, 1))
            .itemOutputs(
                MaterialParts.requireCell(Materials.Hydrogen, 10),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(GTUtility.getWater(10_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HypochlorousAcid, FluidShapes.fluidLiquid, 10_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 10),
                MaterialLibAPI.getStack(Materials.Mercury, CellShapes.cell, 1))
            .itemOutputs(
                MaterialParts.requireCell(Materials.Hydrogen, 10),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 10_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HypochlorousAcid, FluidShapes.fluidLiquid, 10_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialParts.requireCell(Materials.Chlorine, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1))
            .itemOutputs(
                MaterialParts.requireCell(Materials.Hydrogen, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Mercury, FluidShapes.fluidLiquid, 100))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HypochlorousAcid, FluidShapes.fluidLiquid, 1_000))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(chemicalReactorRecipes);

        // P + 3Cl = PCl3

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Phosphorus, Shapes.dust, 1),
                MaterialParts.requireCell(Materials.Chlorine, 3))
            .itemOutputs(ItemList.Cell_Empty.get(3))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.PhosphorusTrichloride, FluidShapes.fluidLiquid, 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialParts.requireCell(Materials.EthyleneOxide, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 5))
            .itemOutputs(ItemList.Cell_Empty.get(6))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Dimethyldichlorosilane, FluidShapes.fluidLiquid, 4_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SiliconOil, FluidShapes.fluidLiquid, 5_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialParts.requireCell(Materials.EthyleneOxide, 1),
                MaterialLibAPI.getStack(Materials.Dimethyldichlorosilane, CellShapes.cell, 4))
            .itemOutputs(ItemList.Cell_Empty.get(5))
            .fluidInputs(GTUtility.getWater(5_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SiliconOil, FluidShapes.fluidLiquid, 5_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1))
            .circuit(2)
            .itemOutputs(ItemList.Cell_Empty.get(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.EthyleneOxide, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.EthyleneGlycol, FluidShapes.fluidLiquid, 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.CobaltIIHydroxide, Shapes.dust, 5),
                MaterialLibAPI.getStack(Materials.NaphthenicAcid, CellShapes.cell, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.CobaltIINaphthenate, Shapes.dust, 41),
                ItemList.Cell_Empty.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.CobaltIIAcetate, Shapes.dust, 15),
                MaterialLibAPI.getStack(Materials.NaphthenicAcid, CellShapes.cell, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.CobaltIINaphthenate, Shapes.dust, 41),
                ItemList.Cell_Empty.get(1))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.AceticAcid, FluidShapes.fluidLiquid, 1_500))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Silicon, Shapes.dust, 1),
                MaterialParts.requireCell(Materials.Chlorine, 4))
            .itemOutputs(ItemList.Cell_Empty.get(4))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SiliconTetrachloride, FluidShapes.fluidLiquid, 1_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Galena, Shapes.crushedPurified, 3),
                MaterialLibAPI.getStack(Materials.Sphalerite, Shapes.crushedPurified, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 4_000))
            .fluidOutputs(new FluidStack(ItemList.sIndiumConcentrate, 8_000))
            .duration(3 * SECONDS)
            .eut(150)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .itemOutputs(MaterialParts.requireCell(Materials.Methane, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 4_000))
            .duration(100 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // O + 2H = H2O

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Oxygen, 1))
            .circuit(22)
            .itemOutputs(ItemList.Cell_Empty.get(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(GTModHandler.getDistilledWater(1_000))
            .duration(10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Hydrogen, 1))
            .circuit(22)
            .itemOutputs(ItemList.Cell_Empty.get(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 500))
            .fluidOutputs(GTModHandler.getDistilledWater(500))
            .duration(5 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // Si + 4Cl = SiCl4

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Silicon, Shapes.dust, 1), ItemList.Cell_Empty.get(2))
            .itemOutputs(MaterialParts.requireCell(Materials.Hydrogen, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 3_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Trichlorosilane, FluidShapes.fluidLiquid, 1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Silane, 1))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.SiliconSolarGrade, Shapes.dust, 1),
                ItemList.Cell_Empty.get(1))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 4_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Silane, 1), ItemList.Cell_Empty.get(3))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.SiliconSolarGrade, Shapes.dust, 1),
                MaterialParts.requireCell(Materials.Hydrogen, 4))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // S + 2Cl = SCl2

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, 8),
                MaterialParts.requireCell(Materials.Chlorine, 16))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.SulfurDichloride, CellShapes.cell, 8),
                ItemList.Cell_Empty.get(8))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, 8), ItemList.Cell_Empty.get(8))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SulfurDichloride, CellShapes.cell, 8))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 16_000))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // C6H6 + C3H6 = C9H12

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialParts.requireCell(Materials.Propene, 8),
                MaterialLibAPI.getStack(Materials.PhosphoricAcidGT5U, CellShapes.cell, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 9))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, 8_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Isopropylbenzene, FluidShapes.fluidLiquid, 8_000))
            .duration(1 * MINUTES + 36 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.PhosphoricAcidGT5U, CellShapes.cell, 1),
                MaterialLibAPI.getStack(Materials.Benzene, CellShapes.cell, 8))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 9))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, 8_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Isopropylbenzene, FluidShapes.fluidLiquid, 8_000))
            .duration(1 * MINUTES + 36 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Benzene, CellShapes.cell, 1),
                MaterialParts.requireCell(Materials.Propene, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.PhosphoricAcidGT5U, FluidShapes.fluidLiquid, 125))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Isopropylbenzene, FluidShapes.fluidLiquid, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // C3H6O + 2C6H6O =HCl= C15H16O2 + H2O

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Acetone, CellShapes.cell, 1),
                MaterialLibAPI.getStack(Materials.Phenol, CellShapes.cell, 2))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.BisphenolA, FluidShapes.fluidLiquid, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 1),
                MaterialLibAPI.getStack(Materials.Acetone, CellShapes.cell, 1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Phenol, FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.BisphenolA, FluidShapes.fluidLiquid, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Phenol, CellShapes.cell, 2),
                MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Acetone, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.BisphenolA, FluidShapes.fluidLiquid, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // N + 3H = NH3

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Nitrogen, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 3_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, 1_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_HV * 4 / 5)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Hydrogen, 3))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 3))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Nitrogen, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, 1_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_HV * 4 / 5)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Nitrogen, 1))
            .circuit(11)
            .itemOutputs(MaterialParts.requireCell(Materials.Ammonia, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 3_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_HV * 4 / 5)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Hydrogen, 3))
            .circuit(11)
            .itemOutputs(
                MaterialParts.requireCell(Materials.Ammonia, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Nitrogen, FluidShapes.fluidGas, 1_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_HV * 4 / 5)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialParts.requireCell(Materials.Ammonia, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Dimethylamine, FluidShapes.fluidGas, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialParts.requireCell(Materials.Ammonia, 4),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 6))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 10_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.NitricOxide, FluidShapes.fluidGas, 4_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.NitricAcid, CellShapes.cell, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.NitrogenDioxide, FluidShapes.fluidGas, 3_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.NitricOxide, FluidShapes.fluidGas, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // 2NO2 + O + H2O = 2HNO3

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialParts.requireCell(Materials.NitrogenDioxide, 2),
                MaterialParts.requireCell(Materials.Oxygen, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 3))
            .fluidInputs(GTUtility.getWater(1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.NitricAcid, FluidShapes.fluidLiquid, 2_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialParts.requireCell(Materials.Oxygen, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.NitrogenDioxide, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.NitricAcid, FluidShapes.fluidLiquid, 2_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1),
                MaterialParts.requireCell(Materials.NitrogenDioxide, 2))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 3))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.NitricAcid, FluidShapes.fluidLiquid, 2_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .itemOutputs(MaterialParts.requireCell(Materials.HydricSulfide, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 2_000))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(chemicalReactorRecipes);

        // C2H4 + HCl + O = C2H3Cl + H2O

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialParts.requireCell(Materials.Ethylene, 1),
                MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.VinylChloride, FluidShapes.fluidGas, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 1),
                MaterialParts.requireCell(Materials.Oxygen, 1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.VinylChloride, FluidShapes.fluidGas, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialParts.requireCell(Materials.Oxygen, 1),
                MaterialParts.requireCell(Materials.Ethylene, 1))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.VinylChloride, FluidShapes.fluidGas, 1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Butadiene, 1), ItemList.Cell_Air.get(10))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.RawStyreneButadieneRubber, Shapes.dust, 9),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 11))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Styrene, FluidShapes.fluidLiquid, 350))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialParts.requireCell(Materials.Butadiene, 1),
                MaterialParts.requireCell(Materials.Oxygen, 5))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.RawStyreneButadieneRubber, Shapes.dust, 13),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 6))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Styrene, FluidShapes.fluidLiquid, 350))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Styrene, CellShapes.cell, 1), ItemList.Cell_Air.get(15))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.RawStyreneButadieneRubber, Shapes.dust, 27),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 16))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Butadiene, FluidShapes.fluidGas, 3_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Styrene, CellShapes.cell, 1),
                MaterialParts.requireCell(Materials.Oxygen, 15))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.RawStyreneButadieneRubber, Shapes.dust, 41),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 16))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Butadiene, FluidShapes.fluidGas, 3_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Styrene, CellShapes.cell, 1),
                MaterialParts.requireCell(Materials.Butadiene, 3))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.RawStyreneButadieneRubber, Shapes.dust, 27),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 4))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Air, FluidShapes.fluidGas, 15_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Styrene, CellShapes.cell, 1),
                MaterialParts.requireCell(Materials.Butadiene, 3))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.RawStyreneButadieneRubber, Shapes.dust, 41),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 4))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 15_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Benzene, CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 4_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Dichlorobenzene, FluidShapes.fluidLiquid, 1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Glycerol, CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .itemOutputs(MaterialLibAPI.getStack(Materials.DilutedSulfuricAcid, CellShapes.cell, 3))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.NitrationMixture, FluidShapes.fluidLiquid, 6_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Glyceryl, FluidShapes.fluidLiquid, 1_000))
            .duration(9 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 12),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 4))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Salt, Shapes.dust, 8),
                MaterialLibAPI.getStack(Materials.Phenol, CellShapes.cell, 4))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chlorobenzene, FluidShapes.fluidLiquid, 4_000))
            .duration(48 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 12),
                MaterialLibAPI.getStack(Materials.Chlorobenzene, CellShapes.cell, 4))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Salt, Shapes.dust, 8),
                MaterialLibAPI.getStack(Materials.Phenol, CellShapes.cell, 4))
            .duration(48 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // Recipes for gasoline
        // 2N + O = N2O

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialParts.requireCell(Materials.Nitrogen, 2),
                MaterialParts.requireCell(Materials.Oxygen, 1))
            .itemOutputs(
                MaterialParts.requireCell(Materials.NitrousOxide, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Nitrogen, 2))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.NitrousOxide, FluidShapes.fluidGas, 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Nitrogen, 2))
            .circuit(11)
            .itemOutputs(
                MaterialParts.requireCell(Materials.NitrousOxide, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Oxygen, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Nitrogen, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.NitrousOxide, FluidShapes.fluidGas, 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialParts.requireCell(Materials.Oxygen, 1))
            .circuit(11)
            .itemOutputs(MaterialParts.requireCell(Materials.NitrousOxide, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Nitrogen, FluidShapes.fluidGas, 2_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // C2H6O + C4H8 = C6H14O

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Ethanol, CellShapes.cell, 1),
                MaterialParts.requireCell(Materials.Butene, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.EthylTertButylEther, CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);

        // Potassium Dichromate
        // 2KNO3 + 2CrO3 = K2Cr2O7 + 2NO + 3O

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Saltpeter, Shapes.dust, 10),
                MaterialLibAPI.getStack(Materials.Chromiumtrioxide, Shapes.dust, 8))
            .itemOutputs(MaterialLibAPI.getStack(Materials.PotassiumDichromate, Shapes.dust, 11))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.NitricOxide, FluidShapes.fluidGas, 2_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.PotassiumNitrate, Shapes.dust, 10),
                MaterialLibAPI.getStack(Materials.Chromiumtrioxide, Shapes.dust, 8))
            .itemOutputs(MaterialLibAPI.getStack(Materials.PotassiumDichromate, Shapes.dust, 11))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.NitricOxide, FluidShapes.fluidGas, 2_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(chemicalReactorRecipes);
    }

    public void multiblockOnly() {

        GTValues.RA.stdBuilder()
            .circuit(22)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 16_000),
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 8_000))
            .fluidOutputs(GTModHandler.getDistilledWater(8_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.PotassiumNitrate, Shapes.dust, 10),
                MaterialLibAPI.getStack(Materials.Chromiumtrioxide, Shapes.dust, 8))
            .itemOutputs(MaterialLibAPI.getStack(Materials.PotassiumDichromate, Shapes.dust, 11))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.NitricOxide, FluidShapes.fluidGas, 2_000),
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 3_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Saltpeter, Shapes.dust, 10),
                MaterialLibAPI.getStack(Materials.Chromiumtrioxide, Shapes.dust, 8))
            .itemOutputs(MaterialLibAPI.getStack(Materials.PotassiumDichromate, Shapes.dust, 11))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.NitricOxide, FluidShapes.fluidGas, 2_000),
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 3_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        // Potassium Dichromate shortcut
        // 2 Cr + 6O + 10 Saltpeter/Potassium Dichromate = 10 K2Cr2O7 + 2NO + 3O

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.PotassiumNitrate, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.PotassiumNitrate, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.PotassiumNitrate, Shapes.dust, 32),
                MaterialLibAPI.getStack(Materials.Chrome, Shapes.dust, 2 * 16))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.PotassiumDichromate, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.PotassiumDichromate, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.PotassiumDichromate, Shapes.dust, 48))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 96_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.NitricOxide, FluidShapes.fluidGas, 32_000),
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 48_000))
            .duration(2 * MINUTES + 8 * SECONDS)
            .eut((int) GTValues.VP[7])
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Saltpeter, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Saltpeter, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Saltpeter, Shapes.dust, 32),
                MaterialLibAPI.getStack(Materials.Chrome, Shapes.dust, 2 * 16))
            .circuit(11)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.PotassiumDichromate, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.PotassiumDichromate, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.PotassiumDichromate, Shapes.dust, 48))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 96_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.NitricOxide, FluidShapes.fluidGas, 32_000),
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 48_000))
            .duration(2 * MINUTES + 8 * SECONDS)
            .eut((int) GTValues.VP[7])
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Dimethylbenzene, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 4_000))
            .duration(3 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Galena, Shapes.crushedPurified, 3),
                MaterialLibAPI.getStack(Materials.Sphalerite, Shapes.crushedPurified, 1))
            .circuit(1)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 4_000))
            .fluidOutputs(new FluidStack(ItemList.sIndiumConcentrate, 8_000))
            .duration(3 * SECONDS)
            .eut(150)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Galena, Shapes.crushedPurified, 27),
                MaterialLibAPI.getStack(Materials.Sphalerite, Shapes.crushedPurified, 9))
            .circuit(8)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 36_000))
            .fluidOutputs(new FluidStack(ItemList.sIndiumConcentrate, 72_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Pentlandite, Shapes.crushedPurified, 9))
            .circuit(9)
            .itemOutputs(MaterialLibAPI.getStack(Materials.PlatinumGroupSludge, Shapes.dust, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 9_000))
            .fluidOutputs(new FluidStack(ItemList.sNickelSulfate, 18_000))
            .duration(1 * SECONDS + 5 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Chalcopyrite, Shapes.crushedPurified, 9))
            .circuit(9)
            .itemOutputs(MaterialLibAPI.getStack(Materials.PlatinumGroupSludge, Shapes.dust, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 9_000))
            .fluidOutputs(new FluidStack(ItemList.sBlueVitriol, 18_000))
            .duration(1 * SECONDS + 5 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Plutonium, Shapes.ingot, 64),
                MaterialLibAPI.getStack(Materials.Uranium, Shapes.dust, 1))
            .circuit(8)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Plutonium, Shapes.dust, 64))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Air, FluidShapes.fluidGas, 8_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Radon, FluidShapes.fluidGas, 800))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        // 3SiO2 + 4Al = 3Si + 2Al2O3

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.Aluminium, Shapes.dust, 4))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Silicon, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Alumina, Shapes.dust, 10))
            .duration(10 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);

        // 10Si + 30HCl -> 0.3 SiH2Cl2 + 9 HSiCl3 + 0.3 SiCl4 + 0.2 Si2Cl6 + 20.4H

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Silicon, Shapes.dust, 10))
            .circuit(9)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 30_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Trichlorosilane, FluidShapes.fluidLiquid, 9_000),
                MaterialLibAPI.getFluidStack(Materials.SiliconTetrachloride, FluidShapes.fluidLiquid, 300),
                MaterialLibAPI.getFluidStack(Materials.Hexachlorodisilane, FluidShapes.fluidLiquid, 200),
                MaterialLibAPI.getFluidStack(Materials.Dichlorosilane, FluidShapes.fluidGas, 300),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 20_400))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        // 2CO + 2C3H6 + 4H =RhHCO(P(C6H5)3)3= C4H8O + C4H8O

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.OrganorhodiumCatalyst, Shapes.dustTiny, 1))
            .circuit(4)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 4_000),
                MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, 2_000),
                MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Butyraldehyde, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Isobutyraldehyde, FluidShapes.fluidLiquid, 1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.OrganorhodiumCatalyst, Shapes.dust, 1))
            .circuit(9)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 36_000),
                MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, 18_000),
                MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, FluidShapes.fluidGas, 18_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Butyraldehyde, FluidShapes.fluidLiquid, 9_000),
                MaterialLibAPI.getFluidStack(Materials.Isobutyraldehyde, FluidShapes.fluidLiquid, 9_000))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        // C2H4 + O =Al2O3,Ag= C2H4O

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Silver, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Alumina, Shapes.dust, 1))
            .circuit(2)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.EthyleneOxide, FluidShapes.fluidGas, 1_000))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Silver, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.Alumina, Shapes.dust, 9))
            .circuit(7)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 9_000),
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 9_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.EthyleneOxide, FluidShapes.fluidGas, 9_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.EthyleneOxide, FluidShapes.fluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Dimethyldichlorosilane, FluidShapes.fluidLiquid, 4_000),
                GTUtility.getWater(5_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SiliconOil, FluidShapes.fluidLiquid, 5_000))
            .duration(15 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(8)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.EthyleneOxide, FluidShapes.fluidGas, 9_000),
                MaterialLibAPI.getFluidStack(Materials.Dimethyldichlorosilane, FluidShapes.fluidLiquid, 36_000),
                GTUtility.getWater(45_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SiliconOil, FluidShapes.fluidLiquid, 45_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // NH3 + CH4O =SiO2,Al2O3= CH5N + H2O

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Alumina, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 1))
            .circuit(10)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Methylamine, FluidShapes.fluidGas, 1_000),
                GTUtility.getWater(1_000))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.PolyurethaneCatalystADust, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Pentaerythritol, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.DiphenylmethaneDiisocyanate, Shapes.dust, 5))
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.EthyleneGlycol, FluidShapes.fluidLiquid, 4_000),
                MaterialLibAPI.getFluidStack(Materials.SiliconOil, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.PolyurethaneResin, FluidShapes.fluidLiquid, 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.PolyurethaneCatalystADust, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.Pentaerythritol, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.DiphenylmethaneDiisocyanate, Shapes.dust, 45))
            .circuit(9)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.EthyleneGlycol, FluidShapes.fluidLiquid, 36_000),
                MaterialLibAPI.getFluidStack(Materials.SiliconOil, FluidShapes.fluidLiquid, 9_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.PolyurethaneResin, FluidShapes.fluidLiquid, 9_000))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(multiblockChemicalReactorRecipes);

        // 3NH3 + 6CH4O =Al2O3,SiO2= CH5N + C2H7N + C3H9N + 6H2O

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Alumina, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 1))
            .circuit(3)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, 6_000),
                MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, 3_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Methylamine, FluidShapes.fluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Dimethylamine, FluidShapes.fluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Trimethylamine, FluidShapes.fluidGas, 1_000),
                GTUtility.getWater(6_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Alumina, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 9))
            .circuit(11)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, 54_000),
                MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, 27_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Methylamine, FluidShapes.fluidGas, 9_000),
                MaterialLibAPI.getFluidStack(Materials.Dimethylamine, FluidShapes.fluidGas, 9_000),
                MaterialLibAPI.getFluidStack(Materials.Trimethylamine, FluidShapes.fluidGas, 9_000),
                GTUtility.getWater(54_000))
            .duration(2 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // 18SOCl2 + 5C10H10O4 + 6CO2 = 7C8H4Cl2O2 + 22HCl + 18SO2

        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.TerephthaloylChloride, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.TerephthaloylChloride, Shapes.dust, 48))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.ThionylChloride, FluidShapes.fluidLiquid, 18_000),
                MaterialLibAPI.getFluidStack(Materials.DimethylTerephthalate, FluidShapes.fluidLiquid, 5_000),
                MaterialLibAPI.getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, 6_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.DilutedHydrochloricAcidGT5U, FluidShapes.fluidLiquid, 22_000),
                MaterialLibAPI.getFluidStack(Materials.SulfurDioxide, FluidShapes.fluidGas, 18_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // 2CH4O + C8H6O4 =H2SO4= C10H10O4 + 2H2O

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.TerephthalicAcid, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, 2_000),
                MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.DimethylTerephthalate, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.DilutedSulfuricAcid, FluidShapes.fluidLiquid, 2_000))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.TerephthalicAcid, FluidShapes.fluidLiquid, 9_000),
                MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, 18_000),
                MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 18_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.DimethylTerephthalate, FluidShapes.fluidLiquid, 9_000),
                MaterialLibAPI.getFluidStack(Materials.DilutedSulfuricAcid, FluidShapes.fluidLiquid, 18_000))
            .duration(1 * MINUTES + 27 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials._13Dimethylbenzene, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 4_000))
            .duration(3 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(3)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials._14Dimethylbenzene, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 4_000))
            .duration(3 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);

        // Diluted Sulfuric acid undilution. 3000 Diluted is 2000 sulfuric 1000 water per DT recipe
        GTValues.RA.stdBuilder()
            .circuit(23)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials.SulfurDioxide, FluidShapes.fluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials.DilutedSulfuricAcid, FluidShapes.fluidLiquid, 3_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 3_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(23)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.SulfurTrioxide, FluidShapes.fluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials.DilutedSulfuricAcid, FluidShapes.fluidLiquid, 3_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 3_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(23)
            .itemInputs(MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 3_000),
                MaterialLibAPI.getFluidStack(Materials.DilutedSulfuricAcid, FluidShapes.fluidLiquid, 3_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 3_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(22)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 9_000),
                MaterialLibAPI.getFluidStack(Materials.SulfurDioxide, FluidShapes.fluidGas, 9_000),
                MaterialLibAPI.getFluidStack(Materials.DilutedSulfuricAcid, FluidShapes.fluidLiquid, 27_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 27_000))
            .duration(11 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(22)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.SulfurTrioxide, FluidShapes.fluidGas, 9_000),
                MaterialLibAPI.getFluidStack(Materials.DilutedSulfuricAcid, FluidShapes.fluidLiquid, 27_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 27_000))
            .duration(11 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(22)
            .itemInputs(MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, 9))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 27_000),
                MaterialLibAPI.getFluidStack(Materials.DilutedSulfuricAcid, FluidShapes.fluidLiquid, 27_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 27_000))
            .duration(11 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.CobaltIIAcetate, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.CobaltIIAcetate, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.CobaltIIAcetate, Shapes.dust, 7))
            .circuit(9)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.CobaltIINaphthenate, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.CobaltIINaphthenate, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.CobaltIINaphthenate, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.CobaltIINaphthenate, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.CobaltIINaphthenate, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.CobaltIINaphthenate, Shapes.dust, 49))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.NaphthenicAcid, FluidShapes.fluidLiquid, 9_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.AceticAcid, FluidShapes.fluidLiquid, 13_500))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.CobaltIIHydroxide, Shapes.dust, 45))
            .circuit(9)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.CobaltIINaphthenate, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.CobaltIINaphthenate, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.CobaltIINaphthenate, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.CobaltIINaphthenate, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.CobaltIINaphthenate, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.CobaltIINaphthenate, Shapes.dust, 49))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.NaphthenicAcid, FluidShapes.fluidLiquid, 9_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // PCl3 + 3C6H5Cl + 6Na = 6NaCl + C18H15P

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Sodium, Shapes.dust, 6))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Triphenylphosphene, Shapes.dust, 34),
                MaterialLibAPI.getStack(Materials.Salt, Shapes.dust, 12))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.PhosphorusTrichloride, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Chlorobenzene, FluidShapes.fluidLiquid, 3_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // 4NaH + C3H9BO3 = NaBH4 + 3CH3ONa

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SodiumHydride, Shapes.dust, 8))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.SodiumBorohydride, Shapes.dust, 6),
                MaterialLibAPI.getStack(Materials.SodiumMethoxide, Shapes.dust, 18))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.TrimethylBorate, FluidShapes.fluidLiquid, 1_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SodiumHydride, Shapes.dust, 64))
            .circuit(9)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.SodiumBorohydride, Shapes.dust, 48),
                MaterialLibAPI.getStack(Materials.SodiumMethoxide, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.SodiumMethoxide, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.SodiumMethoxide, Shapes.dust, 16))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.TrimethylBorate, FluidShapes.fluidLiquid, 8_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // 2CH3COOH = CH3COCH3 + CO2 + H

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(0, MaterialLibAPI.getStack(Materials.Calcium, Shapes.dust, 1)))
            .circuit(3)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.AceticAcid, FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Acetone, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, 1_000),
                GTUtility.getWater(1_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        // Same as above, but with Quicklime and Calcite. The line it's shortcutting accepts Calcium, quicklime, and
        // Calcite
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(0, MaterialLibAPI.getStack(Materials.Quicklime, Shapes.dust, 1)))
            .circuit(3)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.AceticAcid, FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Acetone, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, 1_000),
                GTUtility.getWater(1_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(0, MaterialLibAPI.getStack(Materials.Calcite, Shapes.dust, 1)))
            .circuit(3)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.AceticAcid, FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Acetone, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, 1_000),
                GTUtility.getWater(1_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        // C + 4H + O = CH3OH

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, 1))
            .circuit(23)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 4_000),
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, 1_000))
            .duration(16 * SECONDS)
            .eut(96)
            .addTo(multiblockChemicalReactorRecipes);

        // This recipe collides with one for Vinyl Chloride
        // 2C + 4H + 2O = CH3COOH

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, 2))
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 4_000),
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.AceticAcid, FluidShapes.fluidLiquid, 1_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        // 2CO + 4H = CH3COOH

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, FluidShapes.fluidGas, 2_000),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 4_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.AceticAcid, FluidShapes.fluidLiquid, 1_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(8)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 9_000),
                MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 9_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 9_000))
            .duration(7 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 10_000),
                GTUtility.getWater(10_000),
                MaterialLibAPI.getFluidStack(Materials.Mercury, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.HypochlorousAcid, FluidShapes.fluidLiquid, 10_000),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 10_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(multiblockChemicalReactorRecipes);

        // H2O + 4Cl + C3H6 + NaOH = C3H5ClO + NaCl·H2O + 2HCl

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 3))
            .circuit(23)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 4_000),
                GTUtility.getWater(1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Epichlorohydrin, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.SaltWater, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 2_000))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        // H2O + 2Cl + C3H6 + NaOH =Hg= C3H5ClO + NaCl·H2O + 2H

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 3))
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 2_000),
                GTUtility.getWater(1_000),
                MaterialLibAPI.getFluidStack(Materials.Mercury, FluidShapes.fluidLiquid, 100))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Epichlorohydrin, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.SaltWater, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 2_000))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        // HClO + 2Cl + C3H6 + NaOH = C3H5ClO + NaCl·H2O + HCl

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 3))
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 2_000),
                MaterialLibAPI.getFluidStack(Materials.HypochlorousAcid, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Epichlorohydrin, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.SaltWater, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Apatite, Shapes.dust, 9))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Gypsum, Shapes.dust, 40))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 5_000),
                GTUtility.getWater(10_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.PhosphoricAcidGT5U, FluidShapes.fluidLiquid, 3_000),
                MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Phosphorus, Shapes.dust, 4))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.PhosphorousPentoxide, Shapes.dust, 14))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 10_000))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        // 2P + 5O + 3H2O = 2H3PO4

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Phosphorus, Shapes.dust, 1))
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 2500),
                GTUtility.getWater(1_500))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.PhosphoricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, 8_000),
                MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, 8_000),
                MaterialLibAPI.getFluidStack(Materials.PhosphoricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Isopropylbenzene, FluidShapes.fluidLiquid, 8_000))
            .duration(1 * MINUTES + 36 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Propene, FluidShapes.fluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.PhosphoricAcidGT5U, FluidShapes.fluidLiquid, 100),
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Phenol, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Acetone, FluidShapes.fluidLiquid, 1_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Acetone, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Phenol, FluidShapes.fluidLiquid, 2_000),
                MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.BisphenolA, FluidShapes.fluidLiquid, 1_000),
                GTUtility.getWater(1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 6))
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Acetone, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Phenol, FluidShapes.fluidLiquid, 2_000),
                MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Epichlorohydrin, FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Epoxid, FluidShapes.fluidMolten, 1_000),
                MaterialLibAPI.getFluidStack(Materials.SaltWater, FluidShapes.fluidLiquid, 2_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 9_000),
                MaterialLibAPI.getFluidStack(Materials.Fluorine, FluidShapes.fluidGas, 9_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HydrofluoricAcidGT5U, FluidShapes.fluidLiquid, 9_000))
            .duration(7 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.HydrofluoricAcidGT5U, FluidShapes.fluidLiquid, 4_000),
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 2_000),
                MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 12_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Tetrafluoroethylene, FluidShapes.fluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 12_000))
            .duration(27 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Silicon, Shapes.dust, 1))
            .circuit(24)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Polydimethylsiloxane, Shapes.dust, 3))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 2_000),
                MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 4_000),
                GTUtility.getWater(1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 2_000),
                MaterialLibAPI.getFluidStack(Materials.DilutedHydrochloricAcidGT5U, FluidShapes.fluidLiquid, 2_000))
            .duration(24 * SECONDS)
            .eut(96)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Silicon, Shapes.dust, 1))
            .circuit(24)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Polydimethylsiloxane, Shapes.dust, 3))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, 2_000),
                MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.DilutedHydrochloricAcidGT5U, FluidShapes.fluidLiquid, 2_000))
            .duration(24 * SECONDS)
            .eut(96)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Nitrogen, FluidShapes.fluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 3_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, 1_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_HV * 4 / 5)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Nitrogen, FluidShapes.fluidGas, 10_000),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 30_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, 10_000))
            .duration(2 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_HV * 4 / 5)
            .addTo(multiblockChemicalReactorRecipes);

        // 2NH3 + 7O = N2O4 + 3H2O

        GTValues.RA.stdBuilder()
            .circuit(23)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, 2_000),
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 7_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.DinitrogenTetroxide, FluidShapes.fluidGas, 1_000),
                GTUtility.getWater(3_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        // 7O + 6H + 2N = N2O4 + 3H2O

        GTValues.RA.stdBuilder()
            .circuit(23)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Nitrogen, FluidShapes.fluidGas, 2_000),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 6_000),
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 7_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.DinitrogenTetroxide, FluidShapes.fluidGas, 1_000),
                GTUtility.getWater(3_000))
            .duration(55 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 100_000),
                MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, 36_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.NitricOxide, FluidShapes.fluidGas, 36_000),
                GTUtility.getWater(54_000))
            .duration(8 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(8)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 100_000),
                MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, 36_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.NitricOxide, FluidShapes.fluidGas, 36_000))
            .duration(8 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.NitricOxide, FluidShapes.fluidGas, 9_000),
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 9_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.NitrogenDioxide, FluidShapes.fluidGas, 9_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.NitrogenDioxide, FluidShapes.fluidGas, 27_000),
                GTUtility.getWater(9_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.NitricAcid, FluidShapes.fluidLiquid, 18_000),
                MaterialLibAPI.getFluidStack(Materials.NitricOxide, FluidShapes.fluidGas, 9_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(21)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 3_000),
                MaterialLibAPI.getFluidStack(Materials.Nitrogen, FluidShapes.fluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 4_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.NitricAcid, FluidShapes.fluidLiquid, 1_000),
                GTUtility.getWater(1_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 4_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.NitricAcid, FluidShapes.fluidLiquid, 1_000),
                GTUtility.getWater(1_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.NitrogenDioxide, FluidShapes.fluidGas, 2_000),
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 1_000),
                GTUtility.getWater(1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.NitricAcid, FluidShapes.fluidLiquid, 2_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, 9))
            .circuit(9)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 18_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HydricSulfide, FluidShapes.fluidGas, 9_000))
            .duration(4 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, 9))
            .circuit(9)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 18_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SulfurDioxide, FluidShapes.fluidGas, 9_000))
            .duration(4 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.HydricSulfide, FluidShapes.fluidGas, 9_000),
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 27_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.SulfurDioxide, FluidShapes.fluidGas, 9_000),
                GTUtility.getWater(9_000))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(8)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.HydricSulfide, FluidShapes.fluidGas, 9_000),
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 27_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SulfurDioxide, FluidShapes.fluidGas, 9_000))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(7)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, 27))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.SulfurDioxide, FluidShapes.fluidGas, 9_000),
                MaterialLibAPI.getFluidStack(Materials.HydricSulfide, FluidShapes.fluidGas, 18_000))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.SulfurTrioxide, FluidShapes.fluidGas, 9_000),
                GTUtility.getWater(9_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 9_000))
            .duration(13 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        // S + O3 + H2O = H2SO4

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, 1))
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 3_000),
                GTUtility.getWater(1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 1_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, 9))
            .circuit(7)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 27_000),
                GTUtility.getWater(9_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 9_000))
            .duration(13 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        // H2S + O4 = H2SO4

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.HydricSulfide, FluidShapes.fluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 4_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 1_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        // SO2 + O + H2O = H2SO4

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.SulfurDioxide, FluidShapes.fluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 1_000),
                GTUtility.getWater(1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 1_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.SulfurDioxide, FluidShapes.fluidGas, 9_000),
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 9_000),
                GTUtility.getWater(9_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 9_000))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.VinylChloride, FluidShapes.fluidGas, 1_000),
                GTUtility.getWater(1_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 2_000),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 2_000),
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.VinylChloride, FluidShapes.fluidGas, 2_000),
                GTUtility.getWater(1_000))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials.RawRubber, Shapes.dust, 18))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Isoprene, FluidShapes.fluidLiquid, 1728),
                MaterialLibAPI.getFluidStack(Materials.Air, FluidShapes.fluidGas, 6_000),
                MaterialLibAPI.getFluidStack(Materials.Titaniumtetrachloride, FluidShapes.fluidLiquid, 80))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials.RawRubber, Shapes.dust, 24))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Isoprene, FluidShapes.fluidLiquid, 1728),
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 6_000),
                MaterialLibAPI.getFluidStack(Materials.Titaniumtetrachloride, FluidShapes.fluidLiquid, 80))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(3)
            .itemOutputs(MaterialLibAPI.getStack(Materials.RawStyreneButadieneRubber, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Styrene, FluidShapes.fluidLiquid, 36),
                MaterialLibAPI.getFluidStack(Materials.Butadiene, FluidShapes.fluidGas, 108),
                MaterialLibAPI.getFluidStack(Materials.Air, FluidShapes.fluidGas, 2_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(3)
            .itemOutputs(MaterialLibAPI.getStack(Materials.RawStyreneButadieneRubber, Shapes.dust, 3))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Styrene, FluidShapes.fluidLiquid, 72),
                MaterialLibAPI.getFluidStack(Materials.Butadiene, FluidShapes.fluidGas, 216),
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 2_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(4)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.RawStyreneButadieneRubber, Shapes.dust, 22),
                MaterialLibAPI.getStack(Materials.RawStyreneButadieneRubber, Shapes.dustSmall, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Styrene, FluidShapes.fluidLiquid, 540),
                MaterialLibAPI.getFluidStack(Materials.Butadiene, FluidShapes.fluidGas, 1620),
                MaterialLibAPI.getFluidStack(Materials.Titaniumtetrachloride, FluidShapes.fluidLiquid, 100),
                MaterialLibAPI.getFluidStack(Materials.Air, FluidShapes.fluidGas, 15_000))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(4)
            .itemOutputs(MaterialLibAPI.getStack(Materials.RawStyreneButadieneRubber, Shapes.dust, 30))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Styrene, FluidShapes.fluidLiquid, 540),
                MaterialLibAPI.getFluidStack(Materials.Butadiene, FluidShapes.fluidGas, 1620),
                MaterialLibAPI.getFluidStack(Materials.Titaniumtetrachloride, FluidShapes.fluidLiquid, 100),
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 7_500))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Salt, Shapes.dust, 18))
            .circuit(9)
            .itemOutputs(MaterialLibAPI.getStack(Materials.SodiumBisulfate, Shapes.dust, 63))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 9_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 9_000))
            .duration(6 * SECONDS + 15 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 27))
            .circuit(9)
            .itemOutputs(MaterialLibAPI.getStack(Materials.SodiumBisulfate, Shapes.dust, 63))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 9_000))
            .fluidOutputs(GTUtility.getWater(9_000))
            .duration(6 * SECONDS + 15 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 2_000),
                GTUtility.getWater(1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Phenol, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.DilutedHydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .duration(28 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        // C6H6 + 2Cl + NaOH = C6H6O + NaCl + HCl

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 6))
            .circuit(24)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Salt, Shapes.dust, 4))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, 2_000),
                MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, 4_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Phenol, FluidShapes.fluidLiquid, 2_000),
                MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 2_000))
            .duration(56 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.LightFuel, FluidShapes.fluidLiquid, 20_000),
                MaterialLibAPI.getFluidStack(Materials.HeavyFuel, FluidShapes.fluidLiquid, 4_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Fuel, FluidShapes.fluidLiquid, 24_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Fuel, FluidShapes.fluidLiquid, 10_000),
                MaterialLibAPI.getFluidStack(Materials.Tetranitromethane, FluidShapes.fluidLiquid, 200))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.NitroFuel, FluidShapes.fluidLiquid, 10_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.BioDiesel, FluidShapes.fluidLiquid, 10_000),
                MaterialLibAPI.getFluidStack(Materials.Tetranitromethane, FluidShapes.fluidLiquid, 400))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.NitroFuel, FluidShapes.fluidLiquid, 9_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        // CH4 + 2H2O = CO2 + 8H

        GTValues.RA.stdBuilder()
            .circuit(11)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 5_000),
                GTModHandler.getDistilledWater(10_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.CarbonDioxide, FluidShapes.fluidGas, 5_000),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 40_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        // CH4 + H2O = CO + 6H

        GTValues.RA.stdBuilder()
            .circuit(12)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Methane, FluidShapes.fluidGas, 5_000),
                GTModHandler.getDistilledWater(5_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, FluidShapes.fluidGas, 5_000),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 30_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Nitrogen, FluidShapes.fluidGas, 20_000),
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 10_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.NitrousOxide, FluidShapes.fluidGas, 10_000))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Naphtha, FluidShapes.fluidLiquid, 16_000),
                MaterialLibAPI.getFluidStack(Materials.Gas, FluidShapes.fluidGas, 2_000),
                MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Acetone, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.RawGasoline, FluidShapes.fluidLiquid, 20_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.RawGasoline, FluidShapes.fluidLiquid, 10_000),
                MaterialLibAPI.getFluidStack(Materials.Toluene, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Gasoline, FluidShapes.fluidLiquid, 11_000))
            .duration(10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Gasoline, FluidShapes.fluidLiquid, 20_000),
                MaterialLibAPI.getFluidStack(Materials.Octane, FluidShapes.fluidLiquid, 2_000),
                MaterialLibAPI.getFluidStack(Materials.NitrousOxide, FluidShapes.fluidGas, 6_000),
                MaterialLibAPI.getFluidStack(Materials.Toluene, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.EthylTertButylEther, FluidShapes.fluidLiquid, 3_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.HighOctaneGasoline, FluidShapes.fluidLiquid, 32_000))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // C2H6O + C4H8 = C6H14O

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Ethanol, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Butene, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.EthylTertButylEther, FluidShapes.fluidLiquid, 1_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        // CH4O + C4H8 = C5H12O

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Butene, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.MTBEReactionMixtureButene, FluidShapes.fluidGas, 1_000))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(24)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Butane, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.MTBEReactionMixtureButane, FluidShapes.fluidGas, 1_000))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        // CH2O + 2C6H7N + HCl = C13H14N2(HCl) + H2O

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                new FluidStack(FluidRegistry.getFluid("fluid.formaldehyde"), 1_000),
                new FluidStack(FluidRegistry.getFluid("aniline"), 2_000),
                MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.DiaminodiphenylmethanMixture, FluidShapes.fluidLiquid, 1_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // C6H5NO2 + 6H =Pd= C6H7N + 2H2O

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Palladium, Shapes.dust, 1))
            .circuit(1)
            .fluidInputs(
                new FluidStack(FluidRegistry.getFluid("nitrobenzene"), 9000),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 54_000))
            .fluidOutputs(GTUtility.getWater(18_000), new FluidStack(FluidRegistry.getFluid("aniline"), 9_000))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // C6H6 + HNO3 =H2SO4= C6H5NO2 + H2O

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Benzene, FluidShapes.fluidLiquid, 5_000),
                MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 3_000),
                MaterialLibAPI.getFluidStack(Materials.NitricAcid, FluidShapes.fluidLiquid, 5_000),
                GTModHandler.getDistilledWater(10_000))
            .fluidOutputs(
                new FluidStack(FluidRegistry.getFluid("nitrobenzene"), 5_000),
                MaterialLibAPI.getFluidStack(Materials.DilutedSulfuricAcid, FluidShapes.fluidLiquid, 3_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(multiblockChemicalReactorRecipes);

        // C13H14N2(HCl) + 2COCl2 = C15H10N2O2(5HCl)

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.DiaminodiphenylmethanMixture, FluidShapes.fluidLiquid, 1_000),
                new FluidStack(FluidRegistry.getFluid("phosgene"), 2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.DiphenylmethaneDiisocyanateMixture, FluidShapes.fluidLiquid, 1_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Palladium, Shapes.dust, 1))
            .circuit(9)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Butyraldehyde, FluidShapes.fluidLiquid, 9_000),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 18_000))
            .fluidOutputs(new FluidStack(FluidRegistry.getFluid("butanol"), 9_000))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Tin, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.PolyurethaneCatalystADust, Shapes.dust, 1))
            .fluidInputs(
                new FluidStack(FluidRegistry.getFluid("butanol"), 2_000),
                new FluidStack(FluidRegistry.getFluid("propionicacid"), 1_000),
                MaterialLibAPI.getFluidStack(Materials.IronIIIChloride, FluidShapes.fluidLiquid, 100))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Tin, Shapes.dust, 9))
            .circuit(9)
            .itemOutputs(MaterialLibAPI.getStack(Materials.PolyurethaneCatalystADust, Shapes.dust, 9))
            .fluidInputs(
                new FluidStack(FluidRegistry.getFluid("butanol"), 18_000),
                new FluidStack(FluidRegistry.getFluid("propionicacid"), 9_000),
                MaterialLibAPI.getFluidStack(Materials.IronIIIChloride, FluidShapes.fluidLiquid, 900))
            .duration(3 * MINUTES + 45 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // C2H4 + CO + H2O =C4NiO= C3H6O2

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, FluidShapes.fluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials.NickelTetracarbonyl, FluidShapes.fluidLiquid, 100),
                GTUtility.getWater(1_000))
            .fluidOutputs(new FluidStack(FluidRegistry.getFluid("propionicacid"), 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, 9_000),
                MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, FluidShapes.fluidGas, 9_000),
                MaterialLibAPI.getFluidStack(Materials.NickelTetracarbonyl, FluidShapes.fluidLiquid, 900),
                GTUtility.getWater(9_000))
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
                MaterialLibAPI.getFluidStack(Materials.NitrationMixture, FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials._4Nitroaniline, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.DilutedSulfuricAcid, FluidShapes.fluidLiquid, 1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(
                new FluidStack(FluidRegistry.getFluid("aniline"), 9_000),
                new FluidStack(FluidRegistry.getFluid("molten.aceticanhydride"), 900),
                MaterialLibAPI.getFluidStack(Materials.NitrationMixture, FluidShapes.fluidLiquid, 18_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials._4Nitroaniline, FluidShapes.fluidLiquid, 9_000),
                MaterialLibAPI.getFluidStack(Materials.DilutedSulfuricAcid, FluidShapes.fluidLiquid, 9_000))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // C6H6N2O2 + 6H =Pd,NO2= C6H8N2 + 2H2O

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Palladium, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.pPhenylenediamine, Shapes.dust, 16))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.NitrogenDioxide, FluidShapes.fluidGas, 100),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 6_000),
                MaterialLibAPI.getFluidStack(Materials._4Nitroaniline, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(GTUtility.getWater(2_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(multiblockChemicalReactorRecipes);

        // C4H10O2 =Cu= C4H6O2 + 4H

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Copper, Shapes.dust, 1))
            .circuit(1)
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("1,4-butanediol"), 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.GammaButyrolactone, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 4_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Copper, Shapes.dust, 9))
            .circuit(9)
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("1,4-butanediol"), 9_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.GammaButyrolactone, FluidShapes.fluidLiquid, 9_000),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 36_000))
            .duration(35 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // 2CH2O + C2H2 =SiO2,CuO,Bi2O3= C4H6O2

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.CupricOxide, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.BismuthIIIOxide, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials._2Butin14diol, Shapes.dust, 12))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Acetylene, FluidShapes.fluidGas, 1_000),
                new FluidStack(FluidRegistry.getFluid("fluid.formaldehyde"), 2_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.CupricOxide, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.BismuthIIIOxide, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 9))
            .circuit(9)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials._2Butin14diol, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials._2Butin14diol, Shapes.dust, 44))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Acetylene, FluidShapes.fluidGas, 9_000),
                new FluidStack(FluidRegistry.getFluid("fluid.formaldehyde"), 18_000))
            .duration(2 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // C4H6O2 + 4H =NiAl= C4H10O2

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials._2Butin14diol, Shapes.dust, 12),
                MaterialLibAPI.getStack(Materials.RaneyNickelActivated, Shapes.dust, 1))
            .circuit(1)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 4_000))
            .fluidOutputs(new FluidStack(FluidRegistry.getFluid("1,4-butanediol"), 1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.CalciumChloride, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.pPhenylenediamine, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.TerephthaloylChloride, Shapes.dust, 9))
            .circuit(1)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.NMethylpyrolidone, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.LiquidCrystalKevlar, FluidShapes.fluidLiquid, 9_000),
                MaterialLibAPI.getFluidStack(Materials.DilutedHydrochloricAcidGT5U, FluidShapes.fluidLiquid, 2_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.CalciumChloride, Shapes.dust, 7),
                MaterialLibAPI.getStack(Materials.pPhenylenediamine, Shapes.dust, 63),
                MaterialLibAPI.getStack(Materials.TerephthaloylChloride, Shapes.dust, 63))
            .circuit(9)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.NMethylpyrolidone, FluidShapes.fluidLiquid, 7_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.LiquidCrystalKevlar, FluidShapes.fluidLiquid, 63_000),
                MaterialLibAPI.getFluidStack(Materials.DilutedHydrochloricAcidGT5U, FluidShapes.fluidLiquid, 14_000))
            .duration(2 * MINUTES + 55 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(multiblockChemicalReactorRecipes);

        // Na2B4O7(H2O)10 + 2HCl = 2NaCl + 4H3BO3 + 5H2O

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Borax, Shapes.dust, 23))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Salt, Shapes.dust, 4))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(new FluidStack(FluidRegistry.getFluid("boricacid"), 4_000), GTUtility.getWater(5_000))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        // H3BO3 + 3CH4O =H2SO4= C3H9BO3 + 3H2O

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, 3_000),
                new FluidStack(FluidRegistry.getFluid("boricacid"), 1_000),
                MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 6_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.DilutedSulfuricAcid, FluidShapes.fluidLiquid, 6_000),
                MaterialLibAPI.getFluidStack(Materials.TrimethylBorate, FluidShapes.fluidLiquid, 1_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Methanol, FluidShapes.fluidLiquid, 27_000),
                new FluidStack(FluidRegistry.getFluid("boricacid"), 9_000),
                MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, 54_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.DilutedSulfuricAcid, FluidShapes.fluidLiquid, 54_000),
                MaterialLibAPI.getFluidStack(Materials.TrimethylBorate, FluidShapes.fluidLiquid, 9_000))
            .duration(3 * MINUTES + 45 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // RhCl3 + 3C18H15P + 3NaBH4 + CO = RhC55H46P3O + 3NaCl + 3B + 11H

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.RhodiumChloride, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.Triphenylphosphene, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Triphenylphosphene, Shapes.dust, 38),
                MaterialLibAPI.getStack(Materials.SodiumBorohydride, Shapes.dust, 18))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.OrganorhodiumCatalyst, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.OrganorhodiumCatalyst, Shapes.dust, 42),
                MaterialLibAPI.getStack(Materials.Salt, Shapes.dust, 6),
                MaterialLibAPI.getStack(Materials.Boron, Shapes.dust, 3))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 11_000))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(multiblockChemicalReactorRecipes);

        // 2NaOH + N2H4 =Mn= 2N + 2H2O + 2NaH

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 6),
                MaterialLibAPI.getStack(Materials.Manganese, Shapes.dustTiny, 1))
            .circuit(9)
            .itemOutputs(MaterialLibAPI.getStack(Materials.SodiumHydride, Shapes.dust, 4))
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("fluid.hydrazine"), 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Nitrogen, FluidShapes.fluidGas, 2_000),
                GTUtility.getWater(2_000))
            .duration(10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 54),
                MaterialLibAPI.getStack(Materials.Manganese, Shapes.dust, 1))
            .circuit(18)
            .itemOutputs(MaterialLibAPI.getStack(Materials.SodiumHydride, Shapes.dust, 36))
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("fluid.hydrazine"), 9_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Nitrogen, FluidShapes.fluidGas, 18_000),
                GTUtility.getWater(18_000))
            .duration(3 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // Flawless Amalgatite
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.copyAmount(0, GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Magmatter, 1)),
                MaterialLibAPI.getStack(Materials.Amalgatite, Shapes.gem, 3),
                MaterialLibAPI.getStack(Materials.Bismutite, Shapes.gemFlawed, 64),
                MaterialLibAPI.getStack(Materials.GarnetYellow, Shapes.gemFlawed, 64),
                MaterialLibAPI.getStack(Materials.GreenSapphire, Shapes.gemFlawed, 64),
                MaterialLibAPI.getStack(Materials.Olivine, Shapes.gemFlawed, 64))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.prismaticacid, FluidShapes.fluidLiquid, 1_000_000),
                MaterialLibAPI
                    .getFluidStack(Materials.DimensionallyTranscendentResidue, FluidShapes.fluidLiquid, 1_000_000))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Amalgatite, Shapes.gemFlawless, 1))
            .duration(120 * SECONDS)
            .eut(TierEU.RECIPE_MAX)
            .addTo(multiblockChemicalReactorRecipes);
    }
}
