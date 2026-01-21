package goodgenerator.loader;

import static goodgenerator.util.ItemRefer.Compassline_Casing_EV;
import static goodgenerator.util.ItemRefer.Compassline_Casing_HV;
import static goodgenerator.util.ItemRefer.Compassline_Casing_IV;
import static goodgenerator.util.ItemRefer.Compassline_Casing_LV;
import static goodgenerator.util.ItemRefer.Compassline_Casing_LuV;
import static goodgenerator.util.ItemRefer.Compassline_Casing_MV;
import static goodgenerator.util.ItemRefer.Compassline_Casing_UEV;
import static goodgenerator.util.ItemRefer.Compassline_Casing_UHV;
import static goodgenerator.util.ItemRefer.Compassline_Casing_UIV;
import static goodgenerator.util.ItemRefer.Compassline_Casing_UMV;
import static goodgenerator.util.ItemRefer.Compassline_Casing_UV;
import static goodgenerator.util.ItemRefer.Compassline_Casing_UXV;
import static goodgenerator.util.ItemRefer.Compassline_Casing_ZPM;
import static goodgenerator.util.ItemRefer.Component_Assembly_Line;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.HALF_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.AssemblyLine;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GTRecipeConstants.SCANNING;

import java.util.HashMap;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import bartworks.system.material.WerkstoffLoader;
import goodgenerator.util.StackUtils;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.recipe.Scanning;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import tectech.recipe.TTRecipeAdder;

public class ComponentAssemblyLineMiscRecipes {

    public static final String[] circuitTierMaterials = { "Primitive", "Basic", "Good", "Advanced", "Data", "Elite",
        "Master", "Ultimate", "Superconductor", "Infinite", "Bio", "Optical", "Exotic", "Cosmic" };

    static final HashMap<String, Integer> NameToTier = new HashMap<>();

    static void run() {
        for (int i = 0; i < circuitTierMaterials.length; i++) NameToTier.put(circuitTierMaterials[i], i);
        // Cry about it
        NameToTier.put("Nano", 11);

        generateCasingRecipes();
        generateWrapRecipes();

        // The controller itself
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, Compassline_Casing_EV.get(1))
            .metadata(SCANNING, new Scanning(2 * MINUTES + 30 * SECONDS, TierEU.RECIPE_ZPM))
            .itemInputs(
                ItemList.Machine_Multi_Assemblyline.get(16L),
                ItemList.Casing_Assembler.get(16L),
                ItemList.Casing_Gearbox_TungstenSteel.get(32L),
                ComponentType.Robot_Arm.getComponent(8)
                    .get(16),
                ComponentType.Conveyor_Module.getComponent(8)
                    .get(32),
                ComponentType.Electric_Motor.getComponent(7)
                    .get(32),
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Polybenzimidazole, 16),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Iridium, 4),
                ItemList.FluidSolidifierZPM.get(16L),
                getALCircuit(8, 16),
                getALCircuit(7, 20),
                getALCircuit(6, 24))
            .fluidInputs(
                MaterialsAlloy.INDALLOY_140.getFluidStack(12 * INGOTS),
                Materials.Naquadria.getMolten(16 * INGOTS),
                Materials.Lubricant.getFluid(5_000))
            .itemOutputs(Component_Assembly_Line.get(1))
            .eut(TierEU.RECIPE_UHV / 2)
            .duration(30 * SECONDS)
            .addTo(AssemblyLine);
    }

    /** Recipes for the Component Assembly Line Casings */
    private static void generateCasingRecipes() {
        int t = 1;
        // lv 1
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Steel, 1),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Steel, 4),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(4),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(8),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(10),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Steel, 4),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.Tin, 6),
                getCircuit(t, 16))
            .fluidInputs(Materials.SolderingAlloy.getMolten((t + 1) * INGOTS))
            .itemOutputs(Compassline_Casing_LV.get(1))
            .duration(16 * SECONDS)
            .eut(GTValues.VP[t])
            .addTo(assemblerRecipes);
        // mv 2
        t++;
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Aluminium, 1),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Aluminium, 4),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(4),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(8),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(10),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Aluminium, 4),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.AnyCopper, 6),
                getCircuit(t, 8),
                getCircuit(t - 1, 16))
            .fluidInputs(Materials.SolderingAlloy.getMolten((t + 1) * INGOTS))
            .itemOutputs(Compassline_Casing_MV.get(1))
            .duration(16 * SECONDS)
            .eut(GTValues.VP[t])
            .addTo(assemblerRecipes);
        // hv 3
        t++;
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.StainlessSteel, 1),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.StainlessSteel, 4),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(4),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(8),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(10),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.StainlessSteel, 4),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.Gold, 6),
                getCircuit(t, 8),
                getCircuit(t - 1, 16))
            .fluidInputs(Materials.SolderingAlloy.getMolten((t + 1) * INGOTS))
            .itemOutputs(Compassline_Casing_HV.get(1))
            .duration(16 * SECONDS)
            .eut(GTValues.VP[t])
            .addTo(assemblerRecipes);
        // ev 4
        t++;
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Titanium, 1),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Titanium, 4),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(4),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(8),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(10),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Titanium, 4),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.Aluminium, 6),
                getCircuit(t, 8),
                getCircuit(t - 1, 16))
            .fluidInputs(Materials.SolderingAlloy.getMolten((t + 1) * INGOTS))
            .itemOutputs(Compassline_Casing_EV.get(1))
            .duration(16 * SECONDS)
            .eut(GTValues.VP[t])
            .addTo(assemblerRecipes);
        // iv 5
        t++;
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenSteel, 1),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.TungstenSteel, 4),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(4),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(8),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(10),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.TungstenSteel, 4),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.Tungsten, 6),
                getCircuit(t, 8),
                getCircuit(t - 1, 16))
            .fluidInputs(Materials.SolderingAlloy.getMolten((t + 1) * INGOTS))
            .itemOutputs(Compassline_Casing_IV.get(1))
            .duration(16 * SECONDS)
            .eut(GTValues.VP[t])
            .addTo(assemblerRecipes);
        // Assline Recipes!
        // luv 6
        t++;
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, Compassline_Casing_IV.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 30 * SECONDS, TierEU.RECIPE_IV))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Europium, 1),
                WerkstoffLoader.LuVTierMaterial.get(OrePrefixes.plateDense, 6),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(8),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(10),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(16),
                WerkstoffLoader.LuVTierMaterial.get(OrePrefixes.gearGt, 4),
                WerkstoffLoader.LuVTierMaterial.get(OrePrefixes.gearGtSmall, 16),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 8),
                getALCircuit(t, 8),
                getALCircuit(t - 1, 16))
            .fluidInputs(
                MaterialsAlloy.INDALLOY_140.getFluidStack(t * 4 * INGOTS),
                MaterialsAlloy.ZERON_100.getFluidStack(t * 2 * INGOTS),
                StackUtils.getTieredFluid(t, t * INGOTS),
                Materials.Lubricant.getFluid(1000 * (t - 2)))
            .itemOutputs(Compassline_Casing_LuV.get(1))
            .eut(TierEU.RECIPE_IV)
            .duration(30 * SECONDS)
            .addTo(AssemblyLine);
        // zpm 7
        t++;
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, Compassline_Casing_LuV.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 30 * SECONDS, TierEU.RECIPE_LuV))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Iridium, 1),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Iridium, 1),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(8),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(10),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(16),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Iridium, 4),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Iridium, 16),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Naquadah, 8),
                getALCircuit(t, 8),
                getALCircuit(t - 1, 16))
            .fluidInputs(
                MaterialsAlloy.INDALLOY_140.getFluidStack(t * 4 * INGOTS),
                MaterialsAlloy.PIKYONIUM.getFluidStack(t * 2 * INGOTS),
                StackUtils.getTieredFluid(t, t * INGOTS),
                Materials.Lubricant.getFluid(1000 * (t - 2)))
            .itemOutputs(Compassline_Casing_ZPM.get(1))
            .eut(TierEU.RECIPE_LuV)
            .duration(30 * SECONDS)
            .addTo(AssemblyLine);
        // uv 8
        t++;

        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, Compassline_Casing_ZPM.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 30 * SECONDS, TierEU.RECIPE_ZPM))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Osmium, 1),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Osmium, 1),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(8),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(10),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(16),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Osmium, 4),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Osmium, 16),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 8),
                getALCircuit(t, 8),
                getALCircuit(t - 1, 16))
            .fluidInputs(
                MaterialsAlloy.INDALLOY_140.getFluidStack(t * 4 * INGOTS),
                MaterialsElements.STANDALONE.ADVANCED_NITINOL.getFluidStack(t * 2 * INGOTS),
                StackUtils.getTieredFluid(t, t * INGOTS),
                Materials.Lubricant.getFluid(1000 * (t - 2)))
            .itemOutputs(Compassline_Casing_UV.get(1))
            .eut(TierEU.RECIPE_ZPM)
            .duration(30 * SECONDS)
            .addTo(AssemblyLine);
        // uhv 9
        t++;
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            Compassline_Casing_UV.get(1),
            375 << (t - 2),
            1 << (t - 3),
            (int) TierEU.RECIPE_UV,
            1,
            new Object[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 1),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.CosmicNeutronium, 1),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(8),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(10),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(16),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.CosmicNeutronium, 4),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.CosmicNeutronium, 16),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 8), getALCircuit(t, 8),
                getALCircuit(t - 1, 16) },
            new FluidStack[] { MaterialsAlloy.INDALLOY_140.getFluidStack(t * 4 * INGOTS),
                MaterialsAlloy.ABYSSAL.getFluidStack(t * 2 * INGOTS), StackUtils.getTieredFluid(t, t * INGOTS),
                Materials.Lubricant.getFluid(1000 * (t - 2)) },
            Compassline_Casing_UHV.get(1),
            50 * SECONDS,
            (int) TierEU.RECIPE_UV);
        // uev 10
        t++;
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            Compassline_Casing_UHV.get(1),
            375 << (t - 2),
            1 << (t - 3),
            (int) TierEU.RECIPE_UHV,
            1,
            new Object[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 1),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Infinity, 1),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(8),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(10),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(16),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Infinity, 4),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Infinity, 16),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Draconium, 8), getALCircuit(t, 8),
                getALCircuit(t - 1, 16) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(t * 4 * INGOTS),
                MaterialsAlloy.QUANTUM.getFluidStack(t * 2 * INGOTS), StackUtils.getTieredFluid(t, t * INGOTS),
                Materials.Lubricant.getFluid(1000 * (t - 2)) },
            Compassline_Casing_UEV.get(1),
            50 * SECONDS,
            (int) TierEU.RECIPE_UHV);
        // uiv 11
        t++;
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            Compassline_Casing_UEV.get(1),
            375 << (t - 2),
            1 << (t - 3),
            (int) TierEU.RECIPE_UEV,
            1,
            new Object[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.ProtoHalkonite, 1),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.TranscendentMetal, 1),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(8),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(10),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(16),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.TranscendentMetal, 4),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.ProtoHalkonite, 16),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.NetherStar, 8), getALCircuit(t, 8),
                getALCircuit(t - 1, 16) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(t * 4 * INGOTS),
                MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(t * 2 * INGOTS),
                StackUtils.getTieredFluid(t, t * INGOTS),
                Materials.DimensionallyShiftedSuperfluid.getFluid(1000 * (t - 2)) },
            Compassline_Casing_UIV.get(1),
            50 * SECONDS,
            (int) TierEU.RECIPE_UEV);
        // umv 12
        t++;
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            Compassline_Casing_UIV.get(1),
            375 << (t - 2),
            1 << (t - 3),
            (int) TierEU.RECIPE_UIV,
            1,
            new Object[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SpaceTime, 1),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.SpaceTime, 1),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(8),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(10),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(16),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.SpaceTime, 4),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.SpaceTime, 16),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Quantium, 8), getALCircuit(t, 8),
                getALCircuit(t - 1, 16) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(t * 4 * INGOTS),
                MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(t * 2 * INGOTS),
                StackUtils.getTieredFluid(t, t * INGOTS),
                Materials.DimensionallyShiftedSuperfluid.getFluid(1000 * (t - 2)) },
            Compassline_Casing_UMV.get(1),
            50 * 20,
            (int) TierEU.RECIPE_UIV);
        // uxv 13
        t++;
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            Compassline_Casing_UMV.get(1),
            375 << (t - 2),
            1 << (t - 3),
            (int) TierEU.RECIPE_UMV,
            1,
            new Object[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.MHDCSM, 1),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.MHDCSM, 3),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.MagMatter, 3),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(8),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(10),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(16),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.MHDCSM, 2),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.MagMatter, 2),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.MHDCSM, 8),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.MagMatter, 8),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SpaceTime, 8), getALCircuit(t, 8),
                getALCircuit(t - 1, 16) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(t * 4 * INGOTS),
                Materials.BlackDwarfMatter.getMolten(t * 2 * INGOTS), Materials.Eternity.getMolten(t * INGOTS),
                Materials.DimensionallyShiftedSuperfluid.getFluid(1000 * (t - 2)) },
            Compassline_Casing_UXV.get(1),
            50 * SECONDS,
            (int) TierEU.RECIPE_UMV);
    }

    private static void generateWrapRecipes() {
        for (int i = 0; i <= 11; i++) {
            GTValues.RA.stdBuilder()
                .itemInputs(getCircuit(i, 16))
                .circuit(16)
                .fluidInputs(Materials.SolderingAlloy.getMolten(1 * HALF_INGOTS))
                .itemOutputs(new ItemStack(Loaders.circuitWrap, 1, i))
                .duration(30 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);
        }
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UMV, 16))
            .circuit(16)
            .fluidInputs(Materials.SolderingAlloy.getMolten(1 * HALF_INGOTS))
            .itemOutputs(new ItemStack(Loaders.circuitWrap, 1, 12))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 16))
            .circuit(16)
            .fluidInputs(Materials.SolderingAlloy.getMolten(1 * HALF_INGOTS))
            .itemOutputs(new ItemStack(Loaders.circuitWrap, 1, 13))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);
    }

    @SuppressWarnings("unused")
    private enum ComponentType {

        Electric_Motor,
        Electric_Piston,
        Robot_Arm,
        Electric_Pump,
        Field_Generator,
        Conveyor_Module,
        Emitter,
        Sensor;

        public ItemList getComponent(int tier) {
            if (tier < 0 || tier > GTValues.VN.length) throw new IllegalArgumentException("Tier is out of range!");
            return ItemList.valueOf(this.name() + "_" + GTValues.VN[tier]);
        }
    }

    private static ItemStack getCircuit(int tier, long amount) {
        return GTOreDictUnificator.get(OrePrefixes.circuit, getCircuitMaterial(tier), amount);
    }

    private static Object[] getALCircuit(int tier, int amount) {
        return new Object[] { OrePrefixes.circuit.get(getCircuitMaterial(tier)), amount };
    }

    @Nullable
    public static Materials getCircuitMaterial(int tier) {
        return Materials.getRealMaterial(circuitTierMaterials[tier]);
    }
}
