package gtPlusPlus.core.recipe;

import static gregtech.api.enums.Mods.EternalSingularity;
import static gregtech.api.enums.Mods.RemoteIO;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTModHandler.RecipeBits.BITS;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.AssemblyLine;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GTRecipeConstants.SCANNING;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import goodgenerator.util.ItemRefer;
import gregtech.api.enums.Circuits;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.enums.materials.TEBlockShapes;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.objects.OreDictItemStack;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.recipe.Scanning;
import gtPlusPlus.core.item.crafting.ItemDummyResearch;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import tectech.recipe.TTRecipeAdder;
import tectech.thing.CustomItemList;

public class RecipesMachinesCustom {

    public static void loadRecipes() {
        xlTurbines();
        solarTower();
        chemPlant();
        algaeFarm();
        alloyBlastSmelter();
        quantumForceTransformer();
        treeGrowthSimulator();
        lftr();
        cyclotron();
        powerSubstation();
        zhuhai();
        milling();
        sparging();
        molecularTransformer();
        thermalBoiler();
    }

    private static void xlTurbines() {
        // Turbine Shaft
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.IncoloyDS, Shapes.plateDouble, 4),
                ItemList.Electric_Motor_HV.get(2),
                ItemList.Casing_Gearbox_Titanium.get(1))
            .itemOutputs(GregtechItemList.Casing_Turbine_Shaft.get(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, 2_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // Rotor Assembly
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Turbine.get(1),
                MaterialLibAPI.getStack(Materials.IncoloyDS, Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials.IncoloyDS, Shapes.screw, 8),
                Circuits.EV.get(4),
                MaterialLibAPI.getStack(Materials.TantalumCarbide, Shapes.gearGt, 8))
            .circuit(18)
            .itemOutputs(GregtechItemList.Hatch_Turbine_Rotor.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.StainlessSteel, FluidShapes.fluidMolten, (int) (8 * INGOTS)))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // Reinforced Steam Turbine Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Turbine.get(1),
                MaterialLibAPI.getStack(Materials.Inconel625, Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials.Inconel625, Shapes.screw, 8))
            .circuit(18)
            .itemOutputs(GregtechItemList.Casing_Turbine_LP.get(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Aluminium, FluidShapes.fluidMolten, (int) (2 * INGOTS)))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // XL Turbo Steam Turbine
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.SteamTurbine.get(1),
                MaterialLibAPI.getStack(Materials.IncoloyDS, Shapes.plate, 8),
                MaterialLibAPI.getStack(Materials.IncoloyDS, Shapes.screw, 16),
                MaterialLibAPI.getStack(Materials.IncoloyDS, Shapes.gearGt, 4),
                Circuits.EV.get(8))
            .circuit(18)
            .itemOutputs(ItemList.SteamTurbineXL.get(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Titanium, FluidShapes.fluidMolten, (int) (8 * INGOTS)))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // Reinforced Gas Turbine Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Turbine1.get(1),
                MaterialLibAPI.getStack(Materials.Inconel625, Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials.Inconel625, Shapes.screw, 8))
            .circuit(18)
            .itemOutputs(GregtechItemList.Casing_Turbine_Gas.get(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Titanium, FluidShapes.fluidMolten, (int) (2 * INGOTS)))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // XL Turbo Gas Turbine
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.GasTurbine.get(1),
                MaterialLibAPI.getStack(Materials.Zeron100, Shapes.plate, 8),
                MaterialLibAPI.getStack(Materials.Zeron100, Shapes.screw, 16),
                MaterialLibAPI.getStack(Materials.Zeron100, Shapes.gearGt, 4),
                Circuits.LuV.get(8))
            .circuit(18)
            .itemOutputs(ItemList.GasTurbineXL.get(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chrome, FluidShapes.fluidMolten, (int) (8 * INGOTS)))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        // Reinforced HP Steam Turbine Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Turbine2.get(1),
                MaterialLibAPI.getStack(Materials.IncoloyDS, Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials.IncoloyDS, Shapes.screw, 8))
            .circuit(18)
            .itemOutputs(GregtechItemList.Casing_Turbine_HP.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.StainlessSteel, FluidShapes.fluidMolten, (int) (2 * INGOTS)))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // XL Turbo HP Steam Turbine
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.HPSteamTurbine.get(1),
                MaterialLibAPI.getStack(Materials.Inconel625, Shapes.plate, 8),
                MaterialLibAPI.getStack(Materials.Inconel625, Shapes.screw, 16),
                MaterialLibAPI.getStack(Materials.Inconel625, Shapes.gearGt, 4),
                Circuits.IV.get(8))
            .circuit(18)
            .itemOutputs(ItemList.HPSteamTurbineXL.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.TungstenSteel, FluidShapes.fluidMolten, (int) (8 * INGOTS)))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Reinforced Plasma Turbine Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Turbine3.get(1),
                MaterialLibAPI.getStack(Materials.Zeron100, Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials.Zeron100, Shapes.screw, 8))
            .circuit(18)
            .itemOutputs(GregtechItemList.Casing_Turbine_Plasma.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.TungstenSteel, FluidShapes.fluidMolten, (int) (2 * INGOTS)))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        // XL Turbo Plasma Turbine
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.PlasmaTurbine.get(1),
                MaterialLibAPI.getStack(Materials.Pikyonium64B, Shapes.plate, 8),
                MaterialLibAPI.getStack(Materials.Pikyonium64B, Shapes.screw, 16),
                MaterialLibAPI.getStack(Materials.Pikyonium64B, Shapes.gearGt, 4),
                Circuits.ZPM.get(8))
            .circuit(18)
            .itemOutputs(ItemList.PlasmaTurbineXL.get(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Iridium, FluidShapes.fluidMolten, (int) (8 * INGOTS)))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        // Reinforced SC Turbine Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemRefer.SC_Turbine_Casing.get(1),
                MaterialLibAPI.getStack(Materials.Lumiium, Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials.Lumiium, Shapes.screw, 8))
            .circuit(18)
            .itemOutputs(GregtechItemList.Casing_Turbine_SC.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.AdamantiumAlloy, FluidShapes.fluidMolten, (int) (2 * INGOTS)))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        // XL Turbo SC Steam Turbine
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.SCSteamTurbine.get(1),
                MaterialLibAPI.getStack(Materials.Dalisenite, Shapes.plate, 8),
                MaterialLibAPI.getStack(Materials.Dalisenite, Shapes.screw, 16),
                MaterialLibAPI.getStack(Materials.Dalisenite, Shapes.gearGt, 4),
                Circuits.ZPM.get(8))
            .circuit(18)
            .itemOutputs(ItemList.SCSteamTurbineXL.get(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Hikarium, FluidShapes.fluidMolten, (int) (8 * INGOTS)))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);
    }

    private static void solarTower() {
        // Solar Tower
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.GTPP_Casing_HV.get(4),
                MaterialLibAPI.getStack(Materials.MaragingSteel250, Shapes.plate, 8),
                MaterialLibAPI.getStack(Materials.MaragingSteel250, Shapes.bolt, 8),
                MaterialLibAPI.getStack(Materials.MaragingSteel250, Shapes.screw, 8),
                Circuits.IV.get(8))
            .circuit(17)
            .itemOutputs(GregtechItemList.Industrial_Solar_Tower.get(1))
            .fluidInputs(MaterialUtils.anyFluid(Materials.TantalumCarbide, 16 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // Structural Solar Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.MaragingSteel350, TEBlockShapes.frameGt, 1),
                MaterialLibAPI.getStack(Materials.StainlessSteel, Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials.MaragingSteel350, Shapes.screw, 8))
            .circuit(17)
            .itemOutputs(GregtechItemList.Casing_SolarTower_Structural.get(1))
            .fluidInputs(MaterialUtils.anyFluid(Materials.TantalumCarbide, 4 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // Salt Containment Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.MaragingSteel250, TEBlockShapes.frameGt, 1),
                MaterialLibAPI.getStack(Materials.StainlessSteel, Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials.MaragingSteel250, Shapes.bolt, 16),
                MaterialLibAPI.getStack(Materials.Aluminium, Shapes.screw, 8))
            .circuit(17)
            .itemOutputs(GregtechItemList.Casing_SolarTower_SaltContainment.get(1))
            .fluidInputs(MaterialUtils.anyFluid(Materials.TantalumCarbide, 4 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // Thermally Insulated Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.MaragingSteel250, TEBlockShapes.frameGt, 1),
                MaterialLibAPI.getStack(Materials.BlackSteel, Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials.MaragingSteel250, Shapes.screw, 8))
            .circuit(17)
            .itemOutputs(GregtechItemList.Casing_SolarTower_HeatContainment.get(1))
            .fluidInputs(MaterialUtils.anyFluid(Materials.TantalumCarbide, 4 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // Solar Reflector
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.GTPP_Casing_MV.get(1),
                MaterialLibAPI.getStack(Materials.Inconel625, Shapes.plate, 2),
                MaterialLibAPI.getStack(Materials.Inconel625, Shapes.gearGt, 4),
                ItemList.Electric_Motor_HV.get(2),
                Circuits.HV.get(4))
            .circuit(17)
            .itemOutputs(GregtechItemList.Solar_Tower_Reflector.get(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Titanium, FluidShapes.fluidMolten, (int) (4 * INGOTS)))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
    }

    private static void chemPlant() {
        // Strong Bronze Machine Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_Machine_Custom_1.get(2),
            BITS | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "PhP", "PFP", "PwP", 'P', OrePrefixes.plate.ingredient(Materials.Bronze), 'F',
                OrePrefixes.frameGt.ingredient(Materials.Bronze) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Bronze, Shapes.plate, 6),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Bronze, 1))
            .circuit(2)
            .itemOutputs(GregtechItemList.Casing_Machine_Custom_1.get(2))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);

        // Sturdy Aluminium Machine Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_Machine_Custom_2.get(2),
            BITS | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "PPP", "hFw", "PPP", 'P', OrePrefixes.plate.ingredient(Materials.Aluminium), 'F',
                OrePrefixes.frameGt.ingredient(Materials.Aluminium) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Aluminium, Shapes.plate, 6),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Aluminium, 1))
            .circuit(2)
            .itemOutputs(GregtechItemList.Casing_Machine_Custom_2.get(2))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);

        // ExxonMobil Chemical Plant
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.GTPP_Casing_MV.get(4),
                MaterialLibAPI.getStack(Materials.Aluminium, Shapes.gearGt, 4),
                MaterialLibAPI.getStack(Materials.AnnealedCopper, Shapes.plate, 16),
                GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Plastic, 4),
                MaterialLibAPI.getStack(Materials.BlackMetal, TEBlockShapes.frameGt, 4))
            .circuit(19)
            .itemOutputs(GregtechItemList.ChemicalPlant_Controller.get(1))
            .fluidInputs(MaterialUtils.anyFluid(Materials.BlackSteel, 8 * INGOTS))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        // Catalyst Housing
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.GTPP_Casing_LV.get(2),
                ItemList.Hatch_Input_Bus_MV.get(1),
                MaterialLibAPI.getStack(Materials.Bronze, Shapes.gearGt, 8),
                MaterialLibAPI.getStack(Materials.Lead, Shapes.plate, 48),
                MaterialLibAPI.getStack(Materials.SolderingAlloy, Shapes.wireFine, 16))
            .circuit(15)
            .itemOutputs(GregtechItemList.Bus_Catalysts.get(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Bronze, FluidShapes.fluidMolten, (int) (8 * INGOTS)))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);
    }

    private static void algaeFarm() {
        // Algae Farm
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.GTPP_Casing_ULV.get(4),
                MaterialLibAPI.getStack(Materials.Aluminium, Shapes.stick, 12),
                MaterialLibAPI.getStack(Materials.Wood, Shapes.plate, 32),
                MaterialLibAPI.getStack(Materials.Steel, Shapes.bolt, 16),
                MaterialLibAPI.getStack(Materials.Redstone, Shapes.dust, 32))
            .circuit(21)
            .itemOutputs(ItemList.AlgaeFarm.get(1))
            .fluidInputs(MaterialUtils.anyFluid(Materials.Potin, 8 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);
    }

    private static void alloyBlastSmelter() {
        // Alloy Blast Smelter
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Industrial_AlloyBlastSmelter.get(1),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "PCP", "WMW", "PCP", 'P',
                MaterialLibAPI.getStack(Materials.ZirconiumCarbide, Shapes.plate, 1), 'C', "circuitElite", 'W',
                OrePrefixes.cableGt04.ingredient(Materials.Tungsten), 'M', ItemList.Machine_IV_AlloySmelter });

        // Blast Smelter Casing Block
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_BlastSmelter.get(1),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "PhP", "PFP", "PwP", 'P',
                MaterialLibAPI.getStack(Materials.ZirconiumCarbide, Shapes.plate, 1), 'F',
                MaterialLibAPI.getStack(Materials.ZirconiumCarbide, TEBlockShapes.frameGt, 1) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.ZirconiumCarbide, Shapes.plate, 6),
                MaterialLibAPI.getStack(Materials.ZirconiumCarbide, TEBlockShapes.frameGt, 1))
            .circuit(1)
            .itemOutputs(GregtechItemList.Casing_BlastSmelter.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);

        // Blast Smelter Heat Containment Coil
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_Coil_BlastSmelter.get(1),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "PPP", "FCF", "PPP", 'P', MaterialLibAPI.getStack(Materials.Staballoy, Shapes.plate, 1), 'F',
                MaterialLibAPI.getStack(Materials.Staballoy, TEBlockShapes.frameGt, 1), 'C',
                ItemList.Casing_Gearbox_Titanium });

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Staballoy, Shapes.plate, 6),
                MaterialLibAPI.getStack(Materials.Staballoy, TEBlockShapes.frameGt, 2),
                ItemList.Casing_Gearbox_Titanium.get(1))
            .circuit(1)
            .itemOutputs(GregtechItemList.Casing_Coil_BlastSmelter.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);
    }

    private static void quantumForceTransformer() {
        // QFT Coil Casings
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Coil_Infinity.get(1),
                ItemList.Reactor_Coolant_Sp_6.get(4),
                MaterialLibAPI.getStack(Materials.Laurenium, Shapes.plateDouble, 2),
                CustomItemList.eM_Coil.get(1))
            .itemOutputs(GregtechItemList.Casing_Coil_QuantumForceTransformer.get(1))
            .fluidInputs(MaterialUtils.anyFluid(Materials.Quantum, 4 * INGOTS))
            .duration(1 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        // Quantum Force Transformer
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GregtechItemList.Casing_Coil_QuantumForceTransformer.get(1),
            2048 * 120 * 20,
            2048,
            (int) TierEU.RECIPE_UIV,
            16,
            new Object[] { ItemList.MolecularTransformer.get(1),
                GTModHandler.getModItem(EternalSingularity.ID, "eternal_singularity", 1),
                new Object[] { Circuits.UEV.getIngredient(), 8 }, ItemList.Electric_Pump_UEV.get(4),
                ItemList.Field_Generator_UEV.get(4), GregtechItemList.Laser_Lens_Special.get(1) },
            new FluidStack[] { MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 10 * INGOTS),
                MaterialUtils.anyFluid(Materials.Pikyonium64B, 32 * INGOTS) },
            GregtechItemList.QuantumForceTransformer.get(1),
            3 * MINUTES,
            (int) TierEU.RECIPE_UIV);
    }

    private static void treeGrowthSimulator() {
        // Tree Growth Simulator
        GTModHandler.addCraftingRecipe(
            ItemList.TreeGrowSimulator.get(1),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "FRF", "PHP", "FXF", 'F', ItemList.Field_Generator_IV, 'R',
                MaterialLibAPI.getStack(Materials.IncoloyMA956, Shapes.rotor, 1), 'P',
                MaterialLibAPI.getStack(Materials.Nitinol60, Shapes.plate, 1), 'H',
                GregtechItemList.GTPP_Casing_IV.get(1), 'X',
                MaterialLibAPI.getStack(Materials.Inconel792, TEBlockShapes.pipeMedium, 1) });

        // Sterile Farm Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Tumbaga, TEBlockShapes.frameGt, 1),
                GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Steel, 1),
                ItemList.MV_Coil.get(1),
                ItemList.IC2_Plantball.get(4),
                new OreDictItemStack("plankWood", 8))
            .circuit(2)
            .itemOutputs(GregtechItemList.Casing_PLACEHOLDER_TreeFarmer.get(1))
            .fluidInputs(GTModHandler.getDistilledWater(2_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(assemblerRecipes);
    }

    private static void lftr() {
        // Thorium Reactor [LFTR]
        GTModHandler.addCraftingRecipe(
            GregtechItemList.ThoriumReactor.get(1),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "ABA", "CDC", "EFE", 'A', GregtechItemList.LFTRControlCircuit, 'B',
                OrePrefixes.cableGt12.ingredient(Materials.Naquadah), 'C',
                MaterialLibAPI.getStack(Materials.HastelloyN, Shapes.plateDouble, 1), 'D',
                GregtechItemList.Gregtech_Computer_Cube, 'E',
                MaterialLibAPI.getStack(Materials.Thorium232, Shapes.plate, 1), 'F', ItemList.Hull_IV });

        // Reactor Shield Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_Reactor_II.get(1),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "PdP", "GFG", "PhP", 'P',
                MaterialLibAPI.getStack(Materials.HastelloyC276, Shapes.plateDouble, 1), 'G',
                MaterialLibAPI.getStack(Materials.Talonite, Shapes.gearGt, 1), 'F', ItemList.Field_Generator_LV });

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.HastelloyC276, Shapes.plateDouble, 4),
                MaterialLibAPI.getStack(Materials.Talonite, Shapes.gearGt, 2),
                ItemList.Field_Generator_LV.get(1))
            .itemOutputs(GregtechItemList.Casing_Reactor_II.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // Hastelloy-N Reactor Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_Reactor_I.get(1),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "PIP", "IFI", "PIP", 'P',
                MaterialLibAPI.getStack(Materials.HastelloyN, Shapes.plateDouble, 1), 'I',
                getModItem(Mods.IndustrialCraft2.ID, "reactorPlatingHeat", 1), 'F',
                MaterialLibAPI.getStack(Materials.HastelloyC276, TEBlockShapes.frameGt, 1) });

        // LFTR Control Circuit
        GTValues.RA.stdBuilder()
            .itemInputs(Circuits.LuV.get(1), ItemList.Field_Generator_HV.get(1))
            .itemOutputs(GregtechItemList.LFTRControlCircuit.get(1))
            .duration(4 * MINUTES)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // Reactor Fuel Processing Plant
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Industrial_FuelRefinery.get(1),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "CiC", "PXP", "GHG", 'C', "circuitElite", 'P',
                OrePrefixes.plateDense.ingredient(Materials.TungstenSteel), 'X',
                GregtechItemList.Gregtech_Computer_Cube, 'G',
                MaterialLibAPI.getStack(Materials.Stellite, Shapes.gearGt, 1), 'H', ItemList.Hull_IV });

        // Incoloy-DS Fluid Containment Block
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_Refinery_Internal.get(1),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "PHP", "GTG", "PHP", 'P', MaterialLibAPI.getStack(Materials.IncoloyDS, Shapes.plate, 1), 'H',
                MaterialLibAPI.getStack(Materials.Staballoy, TEBlockShapes.pipeHuge, 1), 'G',
                MaterialLibAPI.getStack(Materials.IncoloyDS, Shapes.gearGt, 1), 'T', ItemList.Super_Tank_IV });

        // Hastelloy-N Sealant Block
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_Refinery_External.get(1),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "ABA", "BFB", "ABA", 'A', MaterialLibAPI.getStack(Materials.IncoloyMA956, Shapes.plate, 1),
                'B', MaterialLibAPI.getStack(Materials.HastelloyN, Shapes.plate, 1), 'F',
                MaterialLibAPI.getStack(Materials.HastelloyC276, TEBlockShapes.frameGt, 1) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.HastelloyN, Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials.IncoloyMA956, Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials.HastelloyC276, TEBlockShapes.frameGt, 1))
            .itemOutputs(GregtechItemList.Casing_Refinery_External.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // Hastelloy-X Structural Block
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_Refinery_Structural.get(1),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "RGP", "hFw", "PHR", 'R', MaterialLibAPI.getStack(Materials.Inconel792, Shapes.ring, 1), 'G',
                MaterialLibAPI.getStack(Materials.HastelloyX, Shapes.gearGt, 1), 'P',
                OrePrefixes.plate.ingredient(Materials.Steel), 'F',
                MaterialLibAPI.getStack(Materials.HastelloyC276, TEBlockShapes.frameGt, 1), 'H', ItemList.Casing_EV });

        // Cold Trap I
        GTModHandler.addCraftingRecipe(
            GregtechItemList.ColdTrap_IV.get(1),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "PDP", "PCP", "RFR", 'P', MaterialLibAPI.getStack(Materials.Inconel625, Shapes.plate, 1),
                'D', MaterialLibAPI.getStack(Materials.HastelloyX, Shapes.plateDouble, 1), 'C', ItemList.Casing_IV, 'R',
                ItemList.Robot_Arm_IV, 'F', ItemList.Casing_FrostProof });

        // Cold Trap II
        GTModHandler.addCraftingRecipe(
            GregtechItemList.ColdTrap_ZPM.get(1),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "PDP", "PCP", "RFR", 'P', MaterialLibAPI.getStack(Materials.Pikyonium64B, Shapes.plate, 1),
                'D', MaterialLibAPI.getStack(Materials.HS188A, Shapes.plateDouble, 1), 'C',
                GregtechItemList.ColdTrap_IV, 'R', ItemList.Robot_Arm_ZPM, 'F', ItemList.Casing_FrostProof });

        // Reactor Processing Unit I
        GTModHandler.addCraftingRecipe(
            GregtechItemList.ReactorProcessingUnit_IV.get(1),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "FRP", "DCD", "PDF", 'F', ItemList.Field_Generator_HV, 'R', ItemList.Robot_Arm_IV, 'P',
                MaterialLibAPI.getStack(Materials.Inconel625, Shapes.plate, 1), 'D',
                MaterialLibAPI.getStack(Materials.HastelloyN, Shapes.plateDouble, 1), 'C',
                ItemList.Machine_IV_ChemicalReactor });

        // Reactor Processing Unit II
        GTModHandler.addCraftingRecipe(
            GregtechItemList.ReactorProcessingUnit_ZPM.get(1),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "FRP", "DCD", "PDF", 'F', ItemList.Field_Generator_IV, 'R', ItemList.Robot_Arm_ZPM, 'P',
                MaterialLibAPI.getStack(Materials.Pikyonium64B, Shapes.plate, 1), 'D',
                MaterialLibAPI.getStack(Materials.HS188A, Shapes.plateDouble, 1), 'C',
                GregtechItemList.ReactorProcessingUnit_IV });

        // Nuclear Salt Processing Plant
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Nuclear_Salt_Processing_Plant.get(1),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "ABA", "CDC", "AEA", 'A', OrePrefixes.plate.ingredient(Materials.Osmiridium), 'B',
                GregtechItemList.ReactorProcessingUnit_IV, 'C',
                MaterialLibAPI.getStack(Materials.Ruridit, Shapes.plate, 1), 'D', "circuitUltimate", 'E',
                GregtechItemList.ColdTrap_IV });
    }

    private static void cyclotron() {
        // Cyclotron Outer Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_FrostProof.get(1),
                GregtechItemList.DehydratorCoilWireEV.get(4),
                MaterialLibAPI.getStack(Materials.IncoloyDS, Shapes.plate, 8),
                MaterialLibAPI.getStack(Materials.Inconel690, Shapes.screw, 16),
                MaterialLibAPI.getStack(Materials.EglinSteel, Shapes.stickLong, 4),
                ItemList.Electric_Piston_HV.get(2))
            .itemOutputs(GregtechItemList.Casing_Cyclotron_External.get(1))
            .fluidInputs(MaterialUtils.anyFluid(Materials.ZirconiumCarbide, 8 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // Cyclotron Coil
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Coil_Nichrome.get(1),
                GregtechItemList.DehydratorCoilWireIV.get(8),
                MaterialLibAPI.getStack(Materials.IncoloyMA956, Shapes.plate, 8),
                MaterialLibAPI.getStack(Materials.Tantalloy61, Shapes.bolt, 16),
                MaterialLibAPI.getStack(Materials.Incoloy020, Shapes.screw, 32),
                ItemList.Field_Generator_EV.get(1))
            .itemOutputs(GregtechItemList.Casing_Cyclotron_Coil.get(1))
            .fluidInputs(MaterialUtils.anyFluid(Materials.HG1223, 5 * INGOTS))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

    }

    private static void powerSubstation() {
        // Sub-Station External Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_Power_SubStation.get(1),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "SPS", "PFP", "SPS", 'S', OrePrefixes.screw.ingredient(Materials.Titanium), 'P',
                MaterialLibAPI.getStack(Materials.Incoloy020, Shapes.plate, 1), 'F',
                MaterialLibAPI.getStack(Materials.IncoloyMA956, TEBlockShapes.frameGt, 1) });

        // Power Station Control Node
        GTModHandler.addCraftingRecipe(
            GregtechItemList.PowerSubStation.get(1),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "ABA", "CDC", "EAE", 'A', MaterialLibAPI.getStack(Materials.IncoloyMA956, Shapes.plate, 1),
                'B', GregtechItemList.LFTRControlCircuit, 'C', GregtechItemList.Casing_Power_SubStation, 'D',
                GregtechItemList.Casing_Vanadium_Redox, 'E',
                MaterialLibAPI.getStack(Materials.Incoloy020, Shapes.plate, 1) });

        // Vanadium Redox Power Cell (EV)
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.HalfCompleteCasing_II.get(1),
                MaterialLibAPI.getStack(Materials.Lead, Shapes.plateDense, 4),
                Circuits.HV.get(4),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorHV, 2))
            .itemOutputs(GregtechItemList.Casing_Vanadium_Redox.get(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, 16_000))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(assemblerRecipes);

        // Vanadium Redox Power Cell (IV)
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Casing_Vanadium_Redox.get(1),
                MaterialLibAPI.getStack(Materials.Titanium, Shapes.plateDense, 4),
                Circuits.EV.get(4),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorEV, 2))
            .itemOutputs(GregtechItemList.Casing_Vanadium_Redox_IV.get(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Nitrogen, FluidShapes.fluidGas, 16_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // Vanadium Redox Power Cell (LuV)
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Casing_Vanadium_Redox_IV.get(1),
                MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.plateDense, 4),
                Circuits.IV.get(4),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorIV, 2))
            .itemOutputs(GregtechItemList.Casing_Vanadium_Redox_LuV.get(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Helium, FluidShapes.fluidGas, 8_000))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Vanadium Redox Power Cell (ZPM)
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Casing_Vanadium_Redox_LuV.get(1),
                ItemUtils.getItemStackOfAmountFromOreDict("plateAlloyIridium", 16),
                Circuits.LuV.get(4),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 2))
            .itemOutputs(GregtechItemList.Casing_Vanadium_Redox_ZPM.get(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Argon, FluidShapes.fluidGas, 4_000))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        // Vanadium Redox Power Cell (UV)
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Casing_Vanadium_Redox_ZPM.get(1),
                MaterialLibAPI.getStack(Materials.Naquadah, Shapes.plateDense, 4),
                Circuits.ZPM.get(4),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 2))
            .itemOutputs(GregtechItemList.Casing_Vanadium_Redox_UV.get(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Radon, FluidShapes.fluidGas, 4_000))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        // Vanadium Redox Power Cell (UHV)
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Casing_Vanadium_Redox_UV.get(1),
                MaterialLibAPI.getStack(Materials.Americium, Shapes.plateDense, 4),
                Circuits.UV.get(4),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUV, 2))
            .itemOutputs(GregtechItemList.Casing_Vanadium_Redox_MAX.get(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Krypton, FluidShapes.fluidLiquid, 500))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(assemblerRecipes);
    }

    private static void zhuhai() {
        // Aquatic Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_FishPond.get(1),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "PhP", "EFE", "PwP", 'P',
                MaterialLibAPI.getStack(Materials.WatertightSteel, Shapes.plate, 1), 'E',
                MaterialLibAPI.getStack(Materials.EglinSteel, Shapes.plate, 1), 'F',
                MaterialLibAPI.getStack(Materials.EglinSteel, TEBlockShapes.frameGt, 1) });

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.WatertightSteel, Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials.EglinSteel, Shapes.plate, 2),
                MaterialLibAPI.getStack(Materials.EglinSteel, TEBlockShapes.frameGt, 1))
            .circuit(1)
            .itemOutputs(GregtechItemList.Casing_FishPond.get(1))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);

        // Zhuhai - Fishing Port
        GTModHandler.addCraftingRecipe(
            ItemList.FishingPort.get(1),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "PCP", "WFW", "PCP", 'P',
                MaterialLibAPI.getStack(Materials.WatertightSteel, Shapes.plate, 1), 'C', "circuitElite", 'W',
                OrePrefixes.wireFine.ingredient(Materials.Electrum), 'F', GregtechItemList.FishTrap });
    }

    private static void milling() {
        // IsaMill Grinding Machine
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Machine_IV_Macerator.get(1))
            .metadata(SCANNING, new Scanning(40 * SECONDS, TierEU.RECIPE_IV))
            .itemInputs(
                GregtechItemList.Casing_IsaMill_Casing.get(4),
                GregtechItemList.Casing_IsaMill_Gearbox.get(4),
                ItemList.Component_Grinder_Tungsten.get(16),
                new Object[] { "circuitMaster", 16 },
                MaterialLibAPI.getStack(Materials.Inconel625, Shapes.gearGt, 8),
                MaterialLibAPI.getStack(Materials.Inconel625, Shapes.plate, 32),
                MaterialLibAPI.getStack(Materials.Zeron100, Shapes.plateDouble, 16),
                MaterialLibAPI.getStack(Materials.Zeron100, Shapes.screw, 64))
            .fluidInputs(
                MaterialUtils.anyFluid(Materials.Zeron100, 16 * INGOTS),
                MaterialUtils.anyFluid(Materials.LafiumCompound, 32 * INGOTS),
                MaterialUtils.anyFluid(Materials.TriniumNaquadahCarbonite, 32 * INGOTS))
            .itemOutputs(GregtechItemList.Controller_IsaMill.get(1))
            .eut(TierEU.RECIPE_LuV)
            .duration(10 * MINUTES)
            .addTo(AssemblyLine);

        // IsaMill Gearbox
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Gearbox_Titanium.get(2),
                MaterialLibAPI.getStack(Materials.Inconel625, Shapes.gearGt, 4),
                MaterialLibAPI.getStack(Materials.Inconel625, Shapes.plate, 16))
            .circuit(7)
            .itemOutputs(GregtechItemList.Casing_IsaMill_Gearbox.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.TungstenSteel, FluidShapes.fluidMolten, (int) (8 * INGOTS)))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        // IsaMill Exterior Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Casing_MacerationStack.get(1),
                MaterialLibAPI.getStack(Materials.Zeron100, Shapes.plateDouble, 2),
                MaterialLibAPI.getStack(Materials.Zeron100, Shapes.stick, 4),
                MaterialLibAPI.getStack(Materials.Zeron100, Shapes.screw, 8))
            .circuit(7)
            .itemOutputs(GregtechItemList.Casing_IsaMill_Casing.get(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Titanium, FluidShapes.fluidMolten, (int) (4 * INGOTS)))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        // IsaMill Piping
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Casing_IsaMill_Casing.get(1),
                ItemList.Casing_Item_Pipe_Quantium.get(1),
                MaterialLibAPI.getStack(Materials.HSSE, Shapes.ring, 8),
                MaterialLibAPI.getStack(Materials.HSSE, Shapes.plate, 8),
                MaterialLibAPI.getStack(Materials.HSSE, Shapes.screw, 8))
            .circuit(7)
            .itemOutputs(GregtechItemList.Casing_IsaMill_Pipe.get(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Aluminium, FluidShapes.fluidMolten, (int) (8 * INGOTS)))
            .duration(8 * MINUTES)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // Flotation Cell Regulator
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.Distillation_Tower.get(1))
            .metadata(SCANNING, new Scanning(40 * SECONDS, TierEU.RECIPE_IV))
            .itemInputs(
                GregtechItemList.Machine_Adv_DistillationTower.get(2),
                GregtechItemList.Casing_Extruder.get(4L),
                GregtechItemList.Casing_Flotation_Cell.get(4),
                ItemList.Electric_Pump_LuV.get(4),
                MaterialLibAPI.getStack(Materials.Stellite, Shapes.gearGt, 8),
                MaterialLibAPI.getStack(Materials.Stellite, Shapes.plate, 32),
                MaterialLibAPI.getStack(Materials.HastelloyN, Shapes.plateDouble, 16),
                MaterialLibAPI.getStack(Materials.HastelloyN, Shapes.screw, 64))
            .fluidInputs(
                MaterialUtils.anyFluid(Materials.Inconel625, 16 * INGOTS),
                MaterialUtils.anyFluid(Materials.Inconel792, 32 * INGOTS),
                MaterialUtils.anyFluid(Materials.HastelloyN, 32 * INGOTS))
            .itemOutputs(ItemList.FlotationCell.get(1))
            .eut(TierEU.RECIPE_LuV)
            .duration(60 * SECONDS)
            .addTo(AssemblyLine);

        // Flotation Cell Casings
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Casing_Extruder.get(4L),
                MaterialLibAPI.getStack(Materials.HSSG, Shapes.plateDouble, 4),
                MaterialLibAPI.getStack(Materials.WatertightSteel, Shapes.plate, 8),
                MaterialLibAPI.getStack(Materials.WatertightSteel, Shapes.ring, 8),
                MaterialLibAPI.getStack(Materials.WatertightSteel, Shapes.rotor, 4))
            .circuit(7)
            .itemOutputs(GregtechItemList.Casing_Flotation_Cell.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.StainlessSteel, FluidShapes.fluidMolten, (int) (8 * INGOTS)))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        // Ball Housing
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.GTPP_Casing_IV.get(1),
                ItemList.Hatch_Input_Bus_EV.get(1),
                MaterialLibAPI.getStack(Materials.Titanium, Shapes.gearGt, 8),
                MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.plate, 32),
                MaterialLibAPI.getStack(Materials.SolderingAlloy, Shapes.wireFine, 16))
            .circuit(7)
            .itemOutputs(GregtechItemList.Bus_Milling_Balls.get(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Tungsten, FluidShapes.fluidMolten, (int) (8 * INGOTS)))
            .duration(4 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
    }

    private static void sparging() {
        // Research on Gas Sparging
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialParts.requireCell(Materials.Helium, 8),
                MaterialParts.requireCell(Materials.Fluorine, 8),
                MaterialLibAPI.getStack(Materials.HS188A, Shapes.ingot, 8),
                ItemList.Distillation_Tower.get(1))
            .circuit(8)
            .itemOutputs(
                ItemDummyResearch.getResearchStack(ItemDummyResearch.ASSEMBLY_LINE_RESEARCH.RESEARCH_10_SPARGING, 1))
            .duration(5 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Sparge Tower Controller
        GTValues.RA.stdBuilder()
            .metadata(
                RESEARCH_ITEM,
                ItemDummyResearch.getResearchStack(ItemDummyResearch.ASSEMBLY_LINE_RESEARCH.RESEARCH_10_SPARGING, 1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 20 * SECONDS, TierEU.RECIPE_IV))
            .itemInputs(
                GregtechItemList.Casing_Sparge_Tower_Exterior.get(4),
                GregtechItemList.GTPP_Casing_EV.get(4),
                ItemList.Machine_IV_Distillery.get(1),
                new Object[] { "circuitElite", 8 },
                MaterialLibAPI.getStack(Materials.HS188A, Shapes.gearGt, 8),
                MaterialLibAPI.getStack(Materials.HS188A, Shapes.plate, 32),
                MaterialLibAPI.getStack(Materials.HastelloyN, Shapes.plateDouble, 16),
                MaterialLibAPI.getStack(Materials.HastelloyN, Shapes.screw, 64),
                MaterialLibAPI.getStack(Materials.YttriumBariumCuprate, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.YttriumBariumCuprate, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.Platinum, Shapes.foil, 64))
            .fluidInputs(
                MaterialUtils.anyFluid(Materials.IncoloyDS, 16 * INGOTS),
                MaterialUtils.anyFluid(Materials.TantalumCarbide, 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Titanium, FluidShapes.fluidMolten, (int) (32 * INGOTS)))
            .itemOutputs(GregtechItemList.Controller_Sparge_Tower.get(1))
            .eut(TierEU.RECIPE_LuV)
            .duration(60 * SECONDS)
            .addTo(AssemblyLine);

        // Sparge Tower Exterior Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.GTPP_Casing_HV.get(1),
                MaterialLibAPI.getStack(Materials.HS188A, Shapes.plate, 2),
                MaterialLibAPI.getStack(Materials.HastelloyN, Shapes.ring, 4),
                MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.plateDouble, 4),
                MaterialLibAPI.getStack(Materials.HastelloyN, Shapes.screw, 4))
            .circuit(8)
            .itemOutputs(GregtechItemList.Casing_Sparge_Tower_Exterior.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.StainlessSteel, FluidShapes.fluidMolten, (int) (8 * INGOTS)))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
    }

    private static void molecularTransformer() {
        // Research on Molecular Transformation
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.GTPP_Casing_LuV.get(1),
                MaterialLibAPI.getStack(Materials.Inconel625, Shapes.plate, 16),
                MaterialLibAPI.getStack(Materials.EnergyCrystal, Shapes.bolt, 32),
                MaterialLibAPI.getStack(Materials.HG1223, Shapes.wireFine, 64),
                ItemList.Emitter_EV.get(8),
                Circuits.LuV.get(10))
            .itemOutputs(
                ItemDummyResearch
                    .getResearchStack(ItemDummyResearch.ASSEMBLY_LINE_RESEARCH.RESEARCH_11_MOLECULAR_TRANSFORMER, 1))
            .fluidInputs(MaterialUtils.anyFluid(Materials.Inconel625, 16 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Molecular Transformer
        GTValues.RA.stdBuilder()
            .metadata(
                RESEARCH_ITEM,
                ItemDummyResearch
                    .getResearchStack(ItemDummyResearch.ASSEMBLY_LINE_RESEARCH.RESEARCH_11_MOLECULAR_TRANSFORMER, 1))
            .metadata(SCANNING, new Scanning(50 * SECONDS, TierEU.RECIPE_IV))
            .itemInputs(
                MaterialLibAPI.getStack(Materials.HG1223, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.HG1223, Shapes.wireFine, 64),
                ItemList.Electric_Motor_IV.get(16),
                ItemList.Energy_LapotronicOrb.get(16),
                GTOreDictUnificator.get(OrePrefixes.cableGt12, Materials.Platinum, 16),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.Nichrome, 32),
                MaterialLibAPI.getStack(Materials.Zeron100, TEBlockShapes.frameGt, 4),
                MaterialLibAPI.getStack(Materials.ZirconiumCarbide, Shapes.plateDouble, 32),
                MaterialLibAPI.getStack(Materials.BabbitAlloy, Shapes.plate, 64),
                MaterialLibAPI.getStack(Materials.Grisium, Shapes.gearGt, 8),
                new Object[] { "circuitData", 64 },
                new Object[] { "circuitElite", 32 },
                new Object[] { "circuitMaster", 16 },
                GregtechItemList.Laser_Lens_WoodsGlass.get(1))
            .fluidInputs(
                MaterialUtils.anyFluid(Materials.Nitinol60, 18 * INGOTS),
                MaterialUtils.anyFluid(Materials.IncoloyMA956, 72 * INGOTS),
                MaterialUtils.anyFluid(Materials.Kanthal, 4 * INGOTS))
            .itemOutputs(ItemList.MolecularTransformer.get(1))
            .eut(TierEU.RECIPE_LuV)
            .duration(2 * MINUTES)
            .addTo(AssemblyLine);

        // Molecular Containment Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Zeron100, Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials.Zeron100, Shapes.screw, 8),
                MaterialLibAPI.getStack(Materials.Palladium, Shapes.wireFine, 16),
                ItemList.Sensor_IV.get(2),
                Circuits.IV.get(4))
            .circuit(16)
            .itemOutputs(GregtechItemList.Casing_Molecular_Transformer_1.get(1))
            .fluidInputs(MaterialUtils.anyFluid(Materials.Inconel625, 4 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // High Voltage Current Capacitor
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Inconel625, Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials.Inconel625, Shapes.screw, 8),
                ItemList.Casing_Coil_Nichrome.get(2),
                ItemList.Field_Generator_HV.get(2),
                Circuits.EV.get(8))
            .circuit(16)
            .itemOutputs(GregtechItemList.Casing_Molecular_Transformer_2.get(1))
            .fluidInputs(MaterialUtils.anyFluid(Materials.Inconel625, 4 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Particle Containment Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.glowstone, 16),
                MaterialLibAPI.getStack(Materials.Inconel625, Shapes.gearGt, 8),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.Titanium, 4),
                ItemList.Field_Generator_EV.get(2),
                Circuits.EV.get(8))
            .circuit(16)
            .itemOutputs(GregtechItemList.Casing_Molecular_Transformer_3.get(1))
            .fluidInputs(MaterialUtils.anyFluid(Materials.Inconel625, 4 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
    }

    private static void thermalBoiler() {
        // Thermal Boiler
        GTModHandler.addCraftingRecipe(
            ItemList.ThermalBoiler.get(1),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "LCL", "GIG", "LCL", 'L', getModItem(RemoteIO.ID, "tile.machine", 1, 1), 'C',
                ItemList.Machine_HV_Centrifuge, 'G', OrePrefixes.gearGt.ingredient(Materials.TungstenSteel), 'I',
                "circuitElite" });

        // Thermal Containment Casing
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_ThermalContainment.get(2),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "PSP", "CHC", "PPP", 'P',
                MaterialLibAPI.getStack(Materials.MaragingSteel350, Shapes.plate, 1), 'S',
                OrePrefixes.plate.ingredient(Materials.StainlessSteel), 'C', "circuitAdvanced", 'H',
                ItemList.Casing_HV });

        // Lava Filter
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, 32),
                MaterialLibAPI.getStack(Materials.Steel, Shapes.wireFine, 32),
                MaterialLibAPI.getStack(Materials.Tumbaga, Shapes.ring, 16),
                MaterialLibAPI.getStack(Materials.Copper, Shapes.foil, 4),
                getModItem(Mods.IndustrialCraft2.ID, "itemPartCarbonMesh", 64, 0))
            .circuit(18)
            .itemOutputs(GregtechItemList.LavaFilter.get(16))
            .fluidInputs(MaterialUtils.anyFluid(Materials.TantalumCarbide, 1 * INGOTS))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(assemblerRecipes);
    }
}
