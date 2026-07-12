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
import static goodgenerator.util.ItemRefer.Compassline_Casing_UV;
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

import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.system.material.WerkstoffLoader;
import goodgenerator.util.StackUtils;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.recipe.Scanning;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import tectech.recipe.TTRecipeAdder;

public class ComponentAssemblyLineMiscRecipes {

    public static final String[] circuitTierMaterials = { "Primitive", "Basic", "Good", "Advanced", "Data", "Elite",
        "Master", "Ultimate", "Superconductor", "Infinite", "Bio", "Optical", "Exotic", "Cosmic", "Transcendent" };

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
                MaterialLibAPI.getStack(Materials2Materials.Iridium, Materials2Shapes.plateSuperdense, 4),
                ItemList.FluidSolidifierZPM.get(16L),
                getALCircuit(8, 16),
                getALCircuit(7, 20),
                getALCircuit(6, 24))
            .fluidInputs(
                MaterialsAlloy.INDALLOY_140.getFluidStack(12 * INGOTS),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Naquadria, Materials2FluidShapes.fluidMolten, 16 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, 5_000))
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
                MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.plateDense, 4),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(4),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(8),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(10),
                MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.gearGt, 4),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.Tin, 6),
                getCircuit(t, 16))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SolderingAlloy,
                    Materials2FluidShapes.fluidMolten,
                    (t + 1) * INGOTS))
            .itemOutputs(Compassline_Casing_LV.get(1))
            .duration(16 * SECONDS)
            .eut(GTValues.VP[t])
            .addTo(assemblerRecipes);
        // mv 2
        t++;
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Aluminium, 1),
                MaterialLibAPI.getStack(Materials2Materials.Aluminium, Materials2Shapes.plateDense, 4),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(4),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(8),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(10),
                MaterialLibAPI.getStack(Materials2Materials.Aluminium, Materials2Shapes.gearGt, 4),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.AnyCopper, 6),
                getCircuit(t, 8),
                getCircuit(t - 1, 16))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SolderingAlloy,
                    Materials2FluidShapes.fluidMolten,
                    (t + 1) * INGOTS))
            .itemOutputs(Compassline_Casing_MV.get(1))
            .duration(16 * SECONDS)
            .eut(GTValues.VP[t])
            .addTo(assemblerRecipes);
        // hv 3
        t++;
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.StainlessSteel, 1),
                MaterialLibAPI.getStack(Materials2Materials.StainlessSteel, Materials2Shapes.plateDense, 4),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(4),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(8),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(10),
                MaterialLibAPI.getStack(Materials2Materials.StainlessSteel, Materials2Shapes.gearGt, 4),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.Gold, 6),
                getCircuit(t, 8),
                getCircuit(t - 1, 16))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SolderingAlloy,
                    Materials2FluidShapes.fluidMolten,
                    (t + 1) * INGOTS))
            .itemOutputs(Compassline_Casing_HV.get(1))
            .duration(16 * SECONDS)
            .eut(GTValues.VP[t])
            .addTo(assemblerRecipes);
        // ev 4
        t++;
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Titanium, 1),
                MaterialLibAPI.getStack(Materials2Materials.Titanium, Materials2Shapes.plateDense, 4),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(4),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(8),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(10),
                MaterialLibAPI.getStack(Materials2Materials.Titanium, Materials2Shapes.gearGt, 4),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.Aluminium, 6),
                getCircuit(t, 8),
                getCircuit(t - 1, 16))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SolderingAlloy,
                    Materials2FluidShapes.fluidMolten,
                    (t + 1) * INGOTS))
            .itemOutputs(Compassline_Casing_EV.get(1))
            .duration(16 * SECONDS)
            .eut(GTValues.VP[t])
            .addTo(assemblerRecipes);
        // iv 5
        t++;
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenSteel, 1),
                MaterialLibAPI.getStack(Materials2Materials.TungstenSteel, Materials2Shapes.plateDense, 4),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(4),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(8),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(10),
                MaterialLibAPI.getStack(Materials2Materials.TungstenSteel, Materials2Shapes.gearGt, 4),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.Tungsten, 6),
                getCircuit(t, 8),
                getCircuit(t - 1, 16))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SolderingAlloy,
                    Materials2FluidShapes.fluidMolten,
                    (t + 1) * INGOTS))
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
                WerkstoffLoader.RhodiumPlatedPalladium.get(OrePrefixes.plateDense, 6),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(8),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(10),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(16),
                WerkstoffLoader.RhodiumPlatedPalladium.get(OrePrefixes.gearGt, 4),
                WerkstoffLoader.RhodiumPlatedPalladium.get(OrePrefixes.gearGtSmall, 16),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 8),
                getALCircuit(t, 8),
                getALCircuit(t - 1, 16))
            .fluidInputs(
                MaterialsAlloy.INDALLOY_140.getFluidStack(t * 4 * INGOTS),
                MaterialsAlloy.ZERON_100.getFluidStack(t * 2 * INGOTS),
                StackUtils.getTieredFluid(t, t * INGOTS),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, 1000 * (t - 2)))
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
                MaterialLibAPI.getStack(Materials2Materials.Iridium, Materials2Shapes.plateSuperdense, 1),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(8),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(10),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(16),
                MaterialLibAPI.getStack(Materials2Materials.Iridium, Materials2Shapes.gearGt, 4),
                MaterialLibAPI.getStack(Materials2Materials.Iridium, Materials2Shapes.gearGtSmall, 16),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Naquadah, 8),
                getALCircuit(t, 8),
                getALCircuit(t - 1, 16))
            .fluidInputs(
                MaterialsAlloy.INDALLOY_140.getFluidStack(t * 4 * INGOTS),
                MaterialsAlloy.PIKYONIUM.getFluidStack(t * 2 * INGOTS),
                StackUtils.getTieredFluid(t, t * INGOTS),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, 1000 * (t - 2)))
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
                MaterialLibAPI.getStack(Materials2Materials.Osmium, Materials2Shapes.plateSuperdense, 1),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(8),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(10),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(16),
                MaterialLibAPI.getStack(Materials2Materials.Osmium, Materials2Shapes.gearGt, 4),
                MaterialLibAPI.getStack(Materials2Materials.Osmium, Materials2Shapes.gearGtSmall, 16),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.NaquadahAlloy, 8),
                getALCircuit(t, 8),
                getALCircuit(t - 1, 16))
            .fluidInputs(
                MaterialsAlloy.INDALLOY_140.getFluidStack(t * 4 * INGOTS),
                MaterialsElements.STANDALONE.ADVANCED_NITINOL.getFluidStack(t * 2 * INGOTS),
                StackUtils.getTieredFluid(t, t * INGOTS),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, 1000 * (t - 2)))
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
                MaterialLibAPI.getStack(Materials2Materials.CosmicNeutronium, Materials2Shapes.plateSuperdense, 1),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(8),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(10),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(16),
                MaterialLibAPI.getStack(Materials2Materials.CosmicNeutronium, Materials2Shapes.gearGt, 4),
                MaterialLibAPI.getStack(Materials2Materials.CosmicNeutronium, Materials2Shapes.gearGtSmall, 16),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 8), getALCircuit(t, 8),
                getALCircuit(t - 1, 16) },
            new FluidStack[] { MaterialsAlloy.INDALLOY_140.getFluidStack(t * 4 * INGOTS),
                MaterialsAlloy.ABYSSAL.getFluidStack(t * 2 * INGOTS), StackUtils.getTieredFluid(t, t * INGOTS),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, 1000 * (t - 2)) },
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
                MaterialLibAPI.getStack(Materials2Materials.Infinity, Materials2Shapes.plateSuperdense, 1),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(8),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(10),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(16),
                MaterialLibAPI.getStack(Materials2Materials.Infinity, Materials2Shapes.gearGt, 4),
                MaterialLibAPI.getStack(Materials2Materials.Infinity, Materials2Shapes.gearGtSmall, 16),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Draconium, 8), getALCircuit(t, 8),
                getALCircuit(t - 1, 16) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(t * 4 * INGOTS),
                MaterialsAlloy.QUANTUM.getFluidStack(t * 2 * INGOTS), StackUtils.getTieredFluid(t, t * INGOTS),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, 1000 * (t - 2)) },
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
                MaterialLibAPI.getStack(Materials2Materials.TranscendentMetal, Materials2Shapes.plateSuperdense, 1),
                ComponentType.Robot_Arm.getComponent(t)
                    .get(8),
                ComponentType.Electric_Piston.getComponent(t)
                    .get(10),
                ComponentType.Electric_Motor.getComponent(t)
                    .get(16),
                MaterialLibAPI.getStack(Materials2Materials.TranscendentMetal, Materials2Shapes.gearGt, 4),
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
    }

    private static void generateWrapRecipes() {
        for (int i = 0; i <= 14; i++) {
            GTValues.RA.stdBuilder()
                .itemInputs(getCircuit(i, 16))
                .circuit(16)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.SolderingAlloy,
                        Materials2FluidShapes.fluidMolten,
                        1 * HALF_INGOTS))
                .itemOutputs(new ItemStack(Loaders.circuitWrap, 1, i))
                .duration(30 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(assemblerRecipes);
        }
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
