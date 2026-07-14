package goodgenerator.loader;

import static goodgenerator.api.recipe.GoodGeneratorRecipeMaps.componentAssemblyLineRecipes;
import static gregtech.api.enums.ItemList.*;
import static gregtech.api.enums.TierEU.RECIPE_EV;
import static gregtech.api.enums.TierEU.RECIPE_HV;
import static gregtech.api.enums.TierEU.RECIPE_IV;
import static gregtech.api.enums.TierEU.RECIPE_LV;
import static gregtech.api.enums.TierEU.RECIPE_LuV;
import static gregtech.api.enums.TierEU.RECIPE_MV;
import static gregtech.api.enums.TierEU.RECIPE_UEV;
import static gregtech.api.enums.TierEU.RECIPE_UHV;
import static gregtech.api.enums.TierEU.RECIPE_UIV;
import static gregtech.api.enums.TierEU.RECIPE_ULV;
import static gregtech.api.enums.TierEU.RECIPE_UMV;
import static gregtech.api.enums.TierEU.RECIPE_UV;
import static gregtech.api.enums.TierEU.RECIPE_ZPM;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.NUGGETS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.STACKS;
import static gregtech.api.util.GTRecipeConstants.COAL_CASING_TIER;
import static gtPlusPlus.core.material.MaterialMisc.MUTATED_LIVING_SOLDER;
import static gtPlusPlus.core.material.MaterialsAlloy.CINOBITE;
import static gtPlusPlus.core.material.MaterialsAlloy.INDALLOY_140;
import static gtPlusPlus.core.material.MaterialsAlloy.LAFIUM;
import static gtPlusPlus.core.material.MaterialsAlloy.PIKYONIUM;
import static gtPlusPlus.core.material.MaterialsAlloy.QUANTUM;
import static gtPlusPlus.core.material.MaterialsAlloy.TITANSTEEL;
import static gtPlusPlus.core.material.MaterialsAlloy.TRINIUM_REINFORCED_STEEL;
import static gtPlusPlus.core.material.MaterialsElements.STANDALONE.ASTRAL_TITANIUM;
import static gtPlusPlus.core.material.MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN;
import static gtPlusPlus.core.material.MaterialsElements.STANDALONE.HYPOGEN;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.system.material.Werkstoff;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

// spotless:off
/**
 * <h3>Guide to making Component Assembly Line (CoAL) recipes</h3>
 * <pre>
 * <ul>
 *     <li>For Item order, match the Assembly Line/Assembler recipe.
 *     <li>For Fluid order, put Solder material first, then Lubricant material, then order by descending amount.
 *     <li>Duplicated recipes for multiple inputs (i.e., SBR vs Silicone) should be respected.
 *     <li>Multiply all inputs by 48x, but output 64 at a time.
 * </ul>
 * Item conversion rules (in case of odd numbers, round down):
 * <ul>
 *     <li>All wires/cables should convert to 16x sizes (excluding fine wires).
 *     <li>All plates should convert to dense plates.
 *     <li>All circuits should convert to circuit wraps.
 *     <li>All rods should convert to long rods.
 *     <li>All small gears should convert into gears.
 *     <li>16 Tier N Nanites -> 1 Tier N+1 Nanite (i.e., 16 Neutronium -> 1 Gold).
 * </ul>
 * Fluid conversion rules:
 * <ul>
 *     <li>Convert metal items to fluid IF the stack size exceeds 64.
 *     <li>Convert fluids to their "basic" form when needed, like converting
 *           Magnetic Samarium to normal, as magnetic has no molten form.
 * </ul>
 * Circuit Numbers (IV+ only):
 * <ul>
 *     <li>1: Motor
 *     <li>2: Piston
 *     <li>3: Pump
 *     <li>4: Robot Arm
 *     <li>5: Conveyor
 *     <li>6: Emitter
 *     <li>7: Sensor
 *     <li>8: Field Generator
 * </ul>
 * </pre>
 */
public class ComponentAssemblyLineLoader {

    private static final int
        COAL_LV  = 1,
        COAL_MV  = 2,
        COAL_HV  = 3,
        COAL_EV  = 4,
        COAL_IV  = 5,
        COAL_LuV = 6,
        COAL_ZPM = 7,
        COAL_UV  = 8,
        COAL_UHV = 9,
        COAL_UEV = 10,
        COAL_UIV = 11,
        COAL_UMV = 12,
        COAL_UXV = 13;

    private static final int
        MOTOR_CIRCUIT           = 1,
        PISTON_CIRCUIT          = 2,
        PUMP_CIRCUIT            = 3,
        ROBOT_ARM_CIRCUIT       = 4,
        CONVEYOR_CIRCUIT        = 5,
        EMITTER_CIRCUIT         = 6,
        SENSOR_CIRCUIT          = 7,
        FIELD_GENERATOR_CIRCUIT = 8;

    public static void run() {
        ComponentAssemblyLineMiscRecipes.run();

        lvRecipes();
        mvRecipes();
        hvRecipes();
        evRecipes();
        ivRecipes();
        luvRecipes();
        zpmRecipes();
        uvRecipes();
        uhvRecipes();
        uevRecipes();
        uivRecipes();
        umvRecipes();
        uxvRecipes();
    }

    private static void lvRecipes() {
        // Motor
        for (var copper : new Materials[] { Materials.Copper, Materials.AnnealedCopper }) {
            GTValues.RA.stdBuilder()
                .itemOutputs(Electric_Motor_LV.get(64))
                .itemInputsUnsafe(
                    MaterialLibAPI.getStack(Materials2Materials.IronMagnetic, Materials2Shapes.stickLong, 24),
                    MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.stickLong, 48),
                    get(OrePrefixes.wireGt16, copper, 12),
                    get(OrePrefixes.cableGt16, Materials.Tin, 6))
                .duration(48 * SECONDS)
                .eut(RECIPE_ULV)
                .metadata(COAL_CASING_TIER, COAL_LV)
                .addTo(componentAssemblyLineRecipes);

            GTValues.RA.stdBuilder()
                .itemOutputs(Electric_Motor_LV.get(64))
                .itemInputsUnsafe(
                    MaterialLibAPI.getStack(Materials2Materials.SteelMagnetic, Materials2Shapes.stickLong, 24),
                    MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.stickLong, 48),
                    get(OrePrefixes.wireGt16, copper, 12),
                    get(OrePrefixes.cableGt16, Materials.Tin, 6))
                .duration(48 * SECONDS)
                .eut(RECIPE_ULV)
                .metadata(COAL_CASING_TIER, COAL_LV)
                .addTo(componentAssemblyLineRecipes);
        }

        // Piston
        GTValues.RA.stdBuilder()
            .itemOutputs(Electric_Piston_LV.get(64))
            .itemInputsUnsafe(
                get(Electric_Motor_LV, 48),
                MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.plateDense, 16),
                MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.stickLong, 48),
                get(OrePrefixes.cableGt16, Materials.Tin, 6),
                MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.gearGt, 12))
            .duration(48 * SECONDS)
            .eut(RECIPE_ULV)
            .metadata(COAL_CASING_TIER, COAL_LV)
            .addTo(componentAssemblyLineRecipes);

        // Robot Arm
        GTValues.RA.stdBuilder()
            .itemOutputs(Robot_Arm_LV.get(64))
            .itemInputsUnsafe(
                get(Electric_Motor_LV, 96),
                get(Electric_Piston_LV, 48),
                MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.stickLong, 48),
                get(OrePrefixes.wrapCircuit, Materials.LV, 3),
                get(OrePrefixes.cableGt16, Materials.Tin, 9))
            .duration(48 * SECONDS)
            .eut(RECIPE_ULV)
            .metadata(COAL_CASING_TIER, COAL_LV)
            .addTo(componentAssemblyLineRecipes);

        for (var rubber : new Materials[] { Materials.Rubber, Materials.RubberSilicone, Materials.StyreneButadieneRubber }) {
            // Pump
            GTValues.RA.stdBuilder()
                .itemOutputs(Electric_Pump_LV.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_LV, 48),
                    MaterialLibAPI.getStack(Materials2Materials.Tin, Materials2Shapes.rotor, 48),
                    MaterialLibAPI.getStack(Materials2Materials.Tin, Materials2Shapes.screw, 48),
                    get(OrePrefixes.cableGt16, Materials.Tin, 3),
                    get(OrePrefixes.pipeMedium, Materials.Bronze, 48))
                .fluidInputs(
                    rubber.getMolten(24 * INGOTS))
                .duration(48 * SECONDS)
                .eut(RECIPE_ULV)
                .metadata(COAL_CASING_TIER, COAL_LV)
                .addTo(componentAssemblyLineRecipes);

            // Conveyor
            GTValues.RA.stdBuilder()
                .itemOutputs(Conveyor_Module_LV.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_LV, 96),
                    get(OrePrefixes.plateDense, rubber, 32),
                    get(OrePrefixes.cableGt16, Materials.Tin, 3))
                .duration(48 * SECONDS)
                .eut(RECIPE_ULV)
                .metadata(COAL_CASING_TIER, COAL_LV)
                .addTo(componentAssemblyLineRecipes);
        }

        // Sensor
        GTValues.RA.stdBuilder()
            .itemOutputs(Sensor_LV.get(64))
            .itemInputsUnsafe(
                MaterialLibAPI.getStack(Materials2Materials.CertusQuartz, Materials2Shapes.gem, 48),
                MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.plateDense, 21),
                MaterialLibAPI.getStack(Materials2Materials.Brass, Materials2Shapes.stickLong, 24),
                get(OrePrefixes.wrapCircuit, Materials.LV, 3))
            .duration(48 * SECONDS)
            .eut(RECIPE_ULV)
            .metadata(COAL_CASING_TIER, COAL_LV)
            .addTo(componentAssemblyLineRecipes);

        // Emitter
        GTValues.RA.stdBuilder()
            .itemOutputs(Emitter_LV.get(64))
            .itemInputsUnsafe(
                MaterialLibAPI.getStack(Materials2Materials.CertusQuartz, Materials2Shapes.gem, 48),
                get(OrePrefixes.wrapCircuit, Materials.LV, 6),
                get(OrePrefixes.cableGt16, Materials.Tin, 6))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Brass, Materials2FluidShapes.fluidMolten, 1 * STACKS + 32 * INGOTS))
            .duration(48 * SECONDS)
            .eut(RECIPE_ULV)
            .metadata(COAL_CASING_TIER, COAL_LV)
            .addTo(componentAssemblyLineRecipes);

        // Field Generator
        GTValues.RA.stdBuilder()
            .itemOutputs(Field_Generator_LV.get(64))
            .itemInputsUnsafe(
                MaterialLibAPI.getStack(Materials2Materials.EnderPearl, Materials2Shapes.plate, 48),
                get(OrePrefixes.wrapCircuit, Materials.HV, 12))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.RedSteel, Materials2FluidShapes.fluidMolten, 1 * STACKS + 32 * INGOTS))
            .duration(24 * MINUTES)
            .eut(RECIPE_ULV)
            .metadata(COAL_CASING_TIER, COAL_LV)
            .addTo(componentAssemblyLineRecipes);
    }

    private static void mvRecipes() {
        // Motor
        for (var copper : new Materials[] { Materials.Copper, Materials.AnnealedCopper }) {
            GTValues.RA.stdBuilder()
                .itemOutputs(Electric_Motor_MV.get(64))
                .itemInputsUnsafe(
                    MaterialLibAPI.getStack(Materials2Materials.SteelMagnetic, Materials2Shapes.stickLong, 24),
                    MaterialLibAPI.getStack(Materials2Materials.Aluminium, Materials2Shapes.stickLong, 48),
                    get(OrePrefixes.wireGt16, Materials.Cupronickel, 24),
                    get(OrePrefixes.cableGt16, copper, 6))
                .duration(48 * SECONDS)
                .eut(RECIPE_LV)
                .metadata(COAL_CASING_TIER, COAL_MV)
                .addTo(componentAssemblyLineRecipes);
        }

        // Piston
        GTValues.RA.stdBuilder()
            .itemOutputs(Electric_Piston_MV.get(64))
            .itemInputsUnsafe(
                get(Electric_Motor_MV, 48),
                MaterialLibAPI.getStack(Materials2Materials.Aluminium, Materials2Shapes.plateDense, 16),
                MaterialLibAPI.getStack(Materials2Materials.Aluminium, Materials2Shapes.stickLong, 48),
                get(OrePrefixes.cableGt16, Materials.Copper, 6),
                MaterialLibAPI.getStack(Materials2Materials.Aluminium, Materials2Shapes.gearGt, 12))
            .duration(48 * SECONDS)
            .eut(RECIPE_LV)
            .metadata(COAL_CASING_TIER, COAL_MV)
            .addTo(componentAssemblyLineRecipes);

        // Robot Arm
        GTValues.RA.stdBuilder()
            .itemOutputs(Robot_Arm_MV.get(64))
            .itemInputsUnsafe(
                get(Electric_Motor_MV, 96),
                get(Electric_Piston_MV, 48),
                MaterialLibAPI.getStack(Materials2Materials.Aluminium, Materials2Shapes.stickLong, 48),
                get(OrePrefixes.wrapCircuit, Materials.MV, 3),
                get(OrePrefixes.cableGt16, Materials.Copper, 9))
            .duration(48 * SECONDS)
            .eut(RECIPE_LV)
            .metadata(COAL_CASING_TIER, COAL_MV)
            .addTo(componentAssemblyLineRecipes);

        for (var rubber : new Materials[] { Materials.Rubber, Materials.RubberSilicone, Materials.StyreneButadieneRubber }) {
            // Pump
            GTValues.RA.stdBuilder()
                .itemOutputs(Electric_Pump_MV.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_MV, 48),
                    MaterialLibAPI.getStack(Materials2Materials.Bronze, Materials2Shapes.rotor, 48),
                    MaterialLibAPI.getStack(Materials2Materials.Bronze, Materials2Shapes.screw, 48),
                    get(OrePrefixes.cableGt16, Materials.Copper, 3),
                    get(OrePrefixes.pipeMedium, Materials.Steel, 48))
                .fluidInputs(
                    rubber.getMolten(24 * INGOTS))
                .duration(48 * SECONDS)
                .eut(RECIPE_LV)
                .metadata(COAL_CASING_TIER, COAL_MV)
                .addTo(componentAssemblyLineRecipes);

            // Conveyor
            GTValues.RA.stdBuilder()
                .itemOutputs(Conveyor_Module_MV.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_MV, 96),
                    get(OrePrefixes.plateDense, rubber, 32),
                    get(OrePrefixes.cableGt16, Materials.Copper, 3))
                .duration(48 * SECONDS)
                .eut(RECIPE_LV)
                .metadata(COAL_CASING_TIER, COAL_MV)
                .addTo(componentAssemblyLineRecipes);
        }

        // Sensor
        GTValues.RA.stdBuilder()
            .itemOutputs(Sensor_MV.get(64))
            .itemInputsUnsafe(
                MaterialLibAPI.getStack(Materials2Materials.Emerald, Materials2Shapes.gemFlawless, 48),
                MaterialLibAPI.getStack(Materials2Materials.Aluminium, Materials2Shapes.plateDense, 21),
                MaterialLibAPI.getStack(Materials2Materials.Electrum, Materials2Shapes.stickLong, 24),
                get(OrePrefixes.wrapCircuit, Materials.MV, 3))
            .duration(48 * SECONDS)
            .eut(RECIPE_LV)
            .metadata(COAL_CASING_TIER, COAL_MV)
            .addTo(componentAssemblyLineRecipes);

        // Emitter
        GTValues.RA.stdBuilder()
            .itemOutputs(Emitter_MV.get(64))
            .itemInputsUnsafe(
                get(OrePrefixes.gem, Materials.EnderPearl, 48),
                get(OrePrefixes.wrapCircuit, Materials.MV, 6),
                get(OrePrefixes.cableGt16, Materials.Copper, 6))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Electrum, Materials2FluidShapes.fluidMolten, 1 * STACKS + 32 * INGOTS))
            .duration(48 * SECONDS)
            .eut(RECIPE_LV)
            .metadata(COAL_CASING_TIER, COAL_MV)
            .addTo(componentAssemblyLineRecipes);

        // Field Generator
        GTValues.RA.stdBuilder()
            .itemOutputs(Field_Generator_MV.get(64))
            .itemInputsUnsafe(
                MaterialLibAPI.getStack(Materials2Materials.EnderEye, Materials2Shapes.plate, 48),
                get(OrePrefixes.wrapCircuit, Materials.EV, 12))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Titanium, Materials2FluidShapes.fluidMolten, 1 * STACKS + 32 * INGOTS))
            .duration(24 * MINUTES)
            .eut(RECIPE_LV)
            .metadata(COAL_CASING_TIER, COAL_MV)
            .addTo(componentAssemblyLineRecipes);
    }

    private static void hvRecipes() {
        // Motor
        GTValues.RA.stdBuilder()
            .itemOutputs(Electric_Motor_HV.get(64))
            .itemInputsUnsafe(
                MaterialLibAPI.getStack(Materials2Materials.SteelMagnetic, Materials2Shapes.stickLong, 24),
                MaterialLibAPI.getStack(Materials2Materials.StainlessSteel, Materials2Shapes.stickLong, 48),
                get(OrePrefixes.wireGt16, Materials.Electrum, 48),
                get(OrePrefixes.cableGt16, Materials.Silver, 12))
            .duration(48 * SECONDS)
            .eut(RECIPE_MV)
            .metadata(COAL_CASING_TIER, COAL_HV)
            .addTo(componentAssemblyLineRecipes);

        // Piston
        GTValues.RA.stdBuilder()
            .itemOutputs(Electric_Piston_HV.get(64))
            .itemInputsUnsafe(
                get(Electric_Motor_HV, 48),
                MaterialLibAPI.getStack(Materials2Materials.StainlessSteel, Materials2Shapes.plateDense, 16),
                MaterialLibAPI.getStack(Materials2Materials.StainlessSteel, Materials2Shapes.stickLong, 48),
                get(OrePrefixes.cableGt16, Materials.Gold, 6),
                MaterialLibAPI.getStack(Materials2Materials.StainlessSteel, Materials2Shapes.gearGt, 12))
            .duration(48 * SECONDS)
            .eut(RECIPE_MV)
            .metadata(COAL_CASING_TIER, COAL_HV)
            .addTo(componentAssemblyLineRecipes);

        // Robot Arm
        GTValues.RA.stdBuilder()
            .itemOutputs(Robot_Arm_HV.get(64))
            .itemInputsUnsafe(
                get(Electric_Motor_HV, 96),
                get(Electric_Piston_HV, 48),
                MaterialLibAPI.getStack(Materials2Materials.StainlessSteel, Materials2Shapes.stickLong, 48),
                get(OrePrefixes.wrapCircuit, Materials.HV, 3),
                get(OrePrefixes.cableGt16, Materials.Gold, 9))
            .duration(48 * SECONDS)
            .eut(RECIPE_MV)
            .metadata(COAL_CASING_TIER, COAL_HV)
            .addTo(componentAssemblyLineRecipes);

        for (var rubber : new Materials[] { Materials.Rubber, Materials.RubberSilicone, Materials.StyreneButadieneRubber }) {
            // Pump
            GTValues.RA.stdBuilder()
                .itemOutputs(Electric_Pump_HV.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_HV, 48),
                    MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.rotor, 48),
                    MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.screw, 48),
                    get(OrePrefixes.cableGt16, Materials.Gold, 3),
                    get(OrePrefixes.pipeMedium, Materials.StainlessSteel, 48))
                .fluidInputs(
                    rubber.getMolten(24 * INGOTS))
                .duration(48 * SECONDS)
                .eut(RECIPE_MV)
                .metadata(COAL_CASING_TIER, COAL_HV)
                .addTo(componentAssemblyLineRecipes);

            // Conveyor
            GTValues.RA.stdBuilder()
                .itemOutputs(Conveyor_Module_HV.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_HV, 96),
                    get(OrePrefixes.plateDense, rubber, 32),
                    get(OrePrefixes.cableGt16, Materials.Gold, 3))
                .duration(48 * SECONDS)
                .eut(RECIPE_MV)
                .metadata(COAL_CASING_TIER, COAL_HV)
                .addTo(componentAssemblyLineRecipes);
        }

        // Sensor
        GTValues.RA.stdBuilder()
            .itemOutputs(Sensor_HV.get(64))
            .itemInputsUnsafe(
                get(OrePrefixes.gem, Materials.EnderEye, 48),
                MaterialLibAPI.getStack(Materials2Materials.StainlessSteel, Materials2Shapes.plateDense, 21),
                MaterialLibAPI.getStack(Materials2Materials.Chrome, Materials2Shapes.stickLong, 24),
                get(OrePrefixes.wrapCircuit, Materials.HV, 3))
            .duration(48 * SECONDS)
            .eut(RECIPE_MV)
            .metadata(COAL_CASING_TIER, COAL_HV)
            .addTo(componentAssemblyLineRecipes);

        // Emitter
        GTValues.RA.stdBuilder()
            .itemOutputs(Emitter_HV.get(64))
            .itemInputsUnsafe(
                get(OrePrefixes.gem, Materials.EnderEye, 48),
                get(OrePrefixes.wrapCircuit, Materials.HV, 6),
                get(OrePrefixes.cableGt16, Materials.Gold, 6))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Chrome, Materials2FluidShapes.fluidMolten, 1 * STACKS + 32 * INGOTS))
            .duration(48 * SECONDS)
            .eut(RECIPE_MV)
            .metadata(COAL_CASING_TIER, COAL_HV)
            .addTo(componentAssemblyLineRecipes);

        // Field Generator
        GTValues.RA.stdBuilder()
            .itemOutputs(Field_Generator_HV.get(64))
            .itemInputsUnsafe(
                get(QuantumEye, 48),
                get(OrePrefixes.wrapCircuit, Materials.IV, 12))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.NiobiumTitanium, Materials2FluidShapes.fluidMolten, 3 * STACKS))
            .duration(24 * MINUTES)
            .eut(RECIPE_MV)
            .metadata(COAL_CASING_TIER, COAL_HV)
            .addTo(componentAssemblyLineRecipes);
    }

    private static void evRecipes() {
        // Motor
        GTValues.RA.stdBuilder()
            .itemOutputs(Electric_Motor_EV.get(64))
            .itemInputsUnsafe(
                MaterialLibAPI.getStack(Materials2Materials.NeodymiumMagnetic, Materials2Shapes.stickLong, 24),
                MaterialLibAPI.getStack(Materials2Materials.Titanium, Materials2Shapes.stickLong, 48),
                get(OrePrefixes.wireGt16, Materials.BlackSteel, 48),
                get(OrePrefixes.cableGt16, Materials.Aluminium, 12))
            .duration(48 * SECONDS)
            .eut(RECIPE_HV)
            .metadata(COAL_CASING_TIER, COAL_EV)
            .addTo(componentAssemblyLineRecipes);

        // Piston
        GTValues.RA.stdBuilder()
            .itemOutputs(Electric_Piston_EV.get(64))
            .itemInputsUnsafe(
                get(Electric_Motor_EV, 48),
                MaterialLibAPI.getStack(Materials2Materials.Titanium, Materials2Shapes.plateDense, 16),
                MaterialLibAPI.getStack(Materials2Materials.Titanium, Materials2Shapes.stickLong, 48),
                get(OrePrefixes.cableGt16, Materials.Aluminium, 6),
                MaterialLibAPI.getStack(Materials2Materials.Titanium, Materials2Shapes.gearGt, 12))
            .duration(48 * SECONDS)
            .eut(RECIPE_HV)
            .metadata(COAL_CASING_TIER, COAL_EV)
            .addTo(componentAssemblyLineRecipes);

        // Robot Arm
        GTValues.RA.stdBuilder()
            .itemOutputs(Robot_Arm_EV.get(64))
            .itemInputsUnsafe(
                get(Electric_Motor_EV, 96),
                get(Electric_Piston_EV, 48),
                MaterialLibAPI.getStack(Materials2Materials.Titanium, Materials2Shapes.stickLong, 48),
                get(OrePrefixes.wrapCircuit, Materials.EV, 3),
                get(OrePrefixes.cableGt16, Materials.Aluminium, 9))
            .duration(48 * SECONDS)
            .eut(RECIPE_HV)
            .metadata(COAL_CASING_TIER, COAL_EV)
            .addTo(componentAssemblyLineRecipes);

        for (var rubber : new Materials[] { Materials.Rubber, Materials.RubberSilicone, Materials.StyreneButadieneRubber }) {
            // Pump
            GTValues.RA.stdBuilder()
                .itemOutputs(Electric_Pump_EV.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_EV, 48),
                    MaterialLibAPI.getStack(Materials2Materials.StainlessSteel, Materials2Shapes.rotor, 48),
                    MaterialLibAPI.getStack(Materials2Materials.StainlessSteel, Materials2Shapes.screw, 48),
                    get(OrePrefixes.cableGt16, Materials.Aluminium, 3),
                    get(OrePrefixes.pipeMedium, Materials.Titanium, 48))
                .fluidInputs(
                    rubber.getMolten(24 * INGOTS))
                .duration(48 * SECONDS)
                .eut(RECIPE_HV)
                .metadata(COAL_CASING_TIER, COAL_EV)
                .addTo(componentAssemblyLineRecipes);

            // Conveyor
            GTValues.RA.stdBuilder()
                .itemOutputs(Conveyor_Module_EV.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_EV, 96),
                    get(OrePrefixes.plateDense, rubber, 32),
                    get(OrePrefixes.cableGt16, Materials.Aluminium, 3))
                .duration(48 * SECONDS)
                .eut(RECIPE_HV)
                .metadata(COAL_CASING_TIER, COAL_EV)
                .addTo(componentAssemblyLineRecipes);
        }

        // Sensor
        GTValues.RA.stdBuilder()
            .itemOutputs(Sensor_EV.get(64))
            .itemInputsUnsafe(
                get(QuantumEye, 48),
                MaterialLibAPI.getStack(Materials2Materials.Titanium, Materials2Shapes.plateDense, 21),
                MaterialLibAPI.getStack(Materials2Materials.Platinum, Materials2Shapes.stickLong, 24),
                get(OrePrefixes.wrapCircuit, Materials.EV, 3))
            .duration(48 * SECONDS)
            .eut(RECIPE_HV)
            .metadata(COAL_CASING_TIER, COAL_EV)
            .addTo(componentAssemblyLineRecipes);

        // Emitter
        GTValues.RA.stdBuilder()
            .itemOutputs(Emitter_EV.get(64))
            .itemInputsUnsafe(
                get(QuantumEye, 48),
                get(OrePrefixes.wrapCircuit, Materials.EV, 6),
                get(OrePrefixes.cableGt16, Materials.Aluminium, 6))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Platinum, Materials2FluidShapes.fluidMolten, 1 * STACKS + 32 * INGOTS))
            .duration(48 * SECONDS)
            .eut(RECIPE_HV)
            .metadata(COAL_CASING_TIER, COAL_EV)
            .addTo(componentAssemblyLineRecipes);

        // Field Generator
        GTValues.RA.stdBuilder()
            .itemOutputs(Field_Generator_EV.get(64))
            .itemInputsUnsafe(
                get(OrePrefixes.gem, Materials.NetherStar, 48),
                get(OrePrefixes.wrapCircuit, Materials.LuV, 12))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.HSSG, Materials2FluidShapes.fluidMolten, 3 * STACKS))
            .duration(24 * MINUTES)
            .eut(RECIPE_HV)
            .metadata(COAL_CASING_TIER, COAL_EV)
            .addTo(componentAssemblyLineRecipes);
    }

    private static void ivRecipes() {
        // Motor
        GTValues.RA.stdBuilder()
            .itemOutputs(Electric_Motor_IV.get(64))
            .itemInputsUnsafe(
                MaterialLibAPI.getStack(Materials2Materials.NeodymiumMagnetic, Materials2Shapes.stickLong, 24),
                MaterialLibAPI.getStack(Materials2Materials.TungstenSteel, Materials2Shapes.stickLong, 48),
                get(OrePrefixes.wireGt16, Materials.Graphene, 48),
                get(OrePrefixes.cableGt16, Materials.Tungsten, 12))
            .circuit(MOTOR_CIRCUIT)
            .duration(48 * SECONDS)
            .eut(RECIPE_EV)
            .metadata(COAL_CASING_TIER, COAL_IV)
            .addTo(componentAssemblyLineRecipes);

        // Piston
        GTValues.RA.stdBuilder()
            .itemOutputs(Electric_Piston_IV.get(64))
            .itemInputsUnsafe(
                get(Electric_Motor_IV, 48),
                MaterialLibAPI.getStack(Materials2Materials.TungstenSteel, Materials2Shapes.plateDense, 16),
                MaterialLibAPI.getStack(Materials2Materials.TungstenSteel, Materials2Shapes.stickLong, 48),
                get(OrePrefixes.cableGt16, Materials.Tungsten, 6),
                MaterialLibAPI.getStack(Materials2Materials.TungstenSteel, Materials2Shapes.gearGt, 12))
            .circuit(PISTON_CIRCUIT)
            .duration(48 * SECONDS)
            .eut(RECIPE_EV)
            .metadata(COAL_CASING_TIER, COAL_IV)
            .addTo(componentAssemblyLineRecipes);

        // Robot Arm
        GTValues.RA.stdBuilder()
            .itemOutputs(Robot_Arm_IV.get(64))
            .itemInputsUnsafe(
                get(Electric_Motor_IV, 96),
                get(Electric_Piston_IV, 48),
                MaterialLibAPI.getStack(Materials2Materials.TungstenSteel, Materials2Shapes.stickLong, 48),
                get(OrePrefixes.wrapCircuit, Materials.IV, 3),
                get(OrePrefixes.cableGt16, Materials.Tungsten, 9))
            .circuit(ROBOT_ARM_CIRCUIT)
            .duration(48 * SECONDS)
            .eut(RECIPE_EV)
            .metadata(COAL_CASING_TIER, COAL_IV)
            .addTo(componentAssemblyLineRecipes);

        for (var rubber : new Materials[] {Materials.RubberSilicone, Materials.StyreneButadieneRubber }) {
            // Pump
            GTValues.RA.stdBuilder()
                .itemOutputs(Electric_Pump_IV.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_IV, 48),
                    MaterialLibAPI.getStack(Materials2Materials.TungstenSteel, Materials2Shapes.rotor, 48),
                    MaterialLibAPI.getStack(Materials2Materials.TungstenSteel, Materials2Shapes.screw, 48),
                    get(OrePrefixes.cableGt16, Materials.Tungsten, 3),
                    get(OrePrefixes.pipeMedium, Materials.TungstenSteel, 48))
                .circuit(PUMP_CIRCUIT)
                .fluidInputs(
                    rubber.getMolten(24 * INGOTS))
                .duration(48 * SECONDS)
                .eut(RECIPE_EV)
                .metadata(COAL_CASING_TIER, COAL_IV)
                .addTo(componentAssemblyLineRecipes);

            // Conveyor
            GTValues.RA.stdBuilder()
                .itemOutputs(Conveyor_Module_IV.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_IV, 96),
                    get(OrePrefixes.plateDense, rubber, 32),
                    get(OrePrefixes.cableGt16, Materials.Tungsten, 3))
                .circuit(CONVEYOR_CIRCUIT)
                .duration(48 * SECONDS)
                .eut(RECIPE_EV)
                .metadata(COAL_CASING_TIER, COAL_IV)
                .addTo(componentAssemblyLineRecipes);
        }

        // Sensor
        GTValues.RA.stdBuilder()
            .itemOutputs(Sensor_IV.get(64))
            .itemInputsUnsafe(
                get(QuantumStar, 48),
                MaterialLibAPI.getStack(Materials2Materials.TungstenSteel, Materials2Shapes.plateDense, 21),
                MaterialLibAPI.getStack(Materials2Materials.Iridium, Materials2Shapes.stickLong, 24),
                get(OrePrefixes.wrapCircuit, Materials.IV, 3))
            .circuit(SENSOR_CIRCUIT)
            .duration(48 * SECONDS)
            .eut(RECIPE_EV)
            .metadata(COAL_CASING_TIER, COAL_IV)
            .addTo(componentAssemblyLineRecipes);

        // Emitter
        GTValues.RA.stdBuilder()
            .itemOutputs(Emitter_IV.get(64))
            .itemInputsUnsafe(
                get(QuantumStar, 48),
                get(OrePrefixes.wrapCircuit, Materials.IV, 6),
                get(OrePrefixes.cableGt16, Materials.Tungsten, 6))
            .circuit(EMITTER_CIRCUIT)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Iridium, Materials2FluidShapes.fluidMolten, 1 * STACKS + 32 * INGOTS))
            .duration(48 * SECONDS)
            .eut(RECIPE_EV)
            .metadata(COAL_CASING_TIER, COAL_IV)
            .addTo(componentAssemblyLineRecipes);

        // Field Generator
        GTValues.RA.stdBuilder()
            .itemOutputs(Field_Generator_IV.get(64))
            .itemInputsUnsafe(
                get(QuantumStar, 48),
                get(OrePrefixes.wrapCircuit, Materials.ZPM, 12))
            .circuit(FIELD_GENERATOR_CIRCUIT)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.HSSS, Materials2FluidShapes.fluidMolten, 3 * STACKS))
            .duration(24 * MINUTES)
            .eut(RECIPE_EV)
            .metadata(COAL_CASING_TIER, COAL_IV)
            .addTo(componentAssemblyLineRecipes);
    }

    private static void luvRecipes() {
        // Motor
        GTValues.RA.stdBuilder()
            .itemOutputs(Electric_Motor_LuV.get(64))
            .itemInputsUnsafe(
                MaterialLibAPI.getStack(Materials2Materials.SamariumMagnetic, Materials2Shapes.stickLong, 24),
                get(OrePrefixes.cableGt16, Materials.YttriumBariumCuprate, 6))
            .circuit(MOTOR_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(48 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, 12_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ruridit, Materials2FluidShapes.fluidMolten, 12 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.HSSS, Materials2FluidShapes.fluidMolten, 1 * STACKS + 32 * INGOTS))
            .duration(24 * MINUTES)
            .eut(RECIPE_IV)
            .metadata(COAL_CASING_TIER, COAL_LuV)
            .addTo(componentAssemblyLineRecipes);

        // Piston
        GTValues.RA.stdBuilder()
            .itemOutputs(Electric_Piston_LuV.get(64))
            .itemInputsUnsafe(
                get(Electric_Motor_LuV, 48),
                MaterialLibAPI.getStack(Materials2Materials.HSSS, Materials2Shapes.plateDense, 32),
                get(OrePrefixes.cableGt16, Materials.YttriumBariumCuprate, 12))
            .circuit(PISTON_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(48 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, 12_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.HSSS, Materials2FluidShapes.fluidMolten, 9 * STACKS + 26 * INGOTS + 6 * NUGGETS))
            .duration(24 * MINUTES)
            .eut(RECIPE_IV)
            .metadata(COAL_CASING_TIER, COAL_LuV)
            .addTo(componentAssemblyLineRecipes);

        for (var rubber : new Materials[] {Materials.RubberSilicone, Materials.StyreneButadieneRubber }) {
            // Pump
            GTValues.RA.stdBuilder()
                .itemOutputs(Electric_Pump_LuV.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_LuV, 48),
                    MaterialLibAPI.getStack(Materials2Materials.HSSS, Materials2Shapes.plateDense, 10),
                    get(OrePrefixes.cableGt16, Materials.YttriumBariumCuprate, 6))
                .circuit(PUMP_CIRCUIT)
                .fluidInputs(
                    INDALLOY_140.getFluidStack(48 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, 12_000),
                    MaterialLibAPI.getFluidStack(Materials2Materials.HSSS, Materials2FluidShapes.fluidMolten, 7 * STACKS + 2 * INGOTS + 6 * NUGGETS),
                    MaterialLibAPI.getFluidStack(Materials2Materials.NiobiumTitanium, Materials2FluidShapes.fluidMolten, 1 * STACKS + 32 * INGOTS),
                    rubber.getMolten(48 * INGOTS))
                .duration(24 * MINUTES)
                .eut(RECIPE_IV)
                .metadata(COAL_CASING_TIER, COAL_LuV)
                .addTo(componentAssemblyLineRecipes);

            // Conveyor
            GTValues.RA.stdBuilder()
                .itemOutputs(Conveyor_Module_LuV.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_LuV, 96),
                    MaterialLibAPI.getStack(Materials2Materials.HSSS, Materials2Shapes.plateDense, 10),
                    get(OrePrefixes.cableGt16, Materials.YttriumBariumCuprate, 6),
                    get(OrePrefixes.plateDense, rubber, 53))
                .circuit(CONVEYOR_CIRCUIT)
                .fluidInputs(
                    INDALLOY_140.getFluidStack(48 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, 12_000),
                    MaterialLibAPI.getFluidStack(Materials2Materials.HSSS, Materials2FluidShapes.fluidMolten, 3 * STACKS + 26 * INGOTS + 6 * NUGGETS))
                .duration(24 * MINUTES)
                .eut(RECIPE_IV)
                .metadata(COAL_CASING_TIER, COAL_LuV)
                .addTo(componentAssemblyLineRecipes);
        }

        // Robot Arm
        GTValues.RA.stdBuilder()
            .itemOutputs(Robot_Arm_LuV.get(64))
            .itemInputsUnsafe(
                get(Electric_Motor_LuV, 96),
                get(Electric_Piston_LuV, 48),
                get(OrePrefixes.wrapCircuit, Materials.LuV, 6),
                get(OrePrefixes.wrapCircuit, Materials.IV, 12),
                get(OrePrefixes.wrapCircuit, Materials.EV, 24),
                get(OrePrefixes.cableGt16, Materials.YttriumBariumCuprate, 18))
            .circuit(ROBOT_ARM_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(3 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, 12_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.HSSS, Materials2FluidShapes.fluidMolten, 8 * STACKS + 16 * INGOTS))
            .duration(24 * MINUTES)
            .eut(RECIPE_IV)
            .metadata(COAL_CASING_TIER, COAL_LuV)
            .addTo(componentAssemblyLineRecipes);

        // Sensor
        GTValues.RA.stdBuilder()
            .itemOutputs(Sensor_LuV.get(64))
            .itemInputsUnsafe(
                get(OrePrefixes.frameGt, Materials.HSSS, 48),
                get(Electric_Motor_LuV, 48),
                MaterialLibAPI.getStack(Materials2Materials.Ruridit, Materials2Shapes.plateDense, 42),
                get(QuantumStar, 48),
                get(OrePrefixes.wrapCircuit, Materials.LuV, 12),
                get(OrePrefixes.cableGt16, Materials.YttriumBariumCuprate, 21))
            .circuit(SENSOR_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(3 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Gallium, Materials2FluidShapes.fluidMolten, 36 * STACKS))
            .duration(24 * MINUTES)
            .eut(RECIPE_IV)
            .metadata(COAL_CASING_TIER, COAL_LuV)
            .addTo(componentAssemblyLineRecipes);

        // Emitter
        GTValues.RA.stdBuilder()
            .itemOutputs(Emitter_LuV.get(64))
            .itemInputsUnsafe(
                get(OrePrefixes.frameGt, Materials.HSSS, 48),
                get(Electric_Motor_LuV, 48),
                get(QuantumStar, 48),
                get(OrePrefixes.wrapCircuit, Materials.LuV, 12),
                get(OrePrefixes.cableGt16, Materials.YttriumBariumCuprate, 21))
            .circuit(EMITTER_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(3 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Gallium, Materials2FluidShapes.fluidMolten, 36 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ruridit, Materials2FluidShapes.fluidMolten, 3 * STACKS))
            .duration(24 * MINUTES)
            .eut(RECIPE_IV)
            .metadata(COAL_CASING_TIER, COAL_LuV)
            .addTo(componentAssemblyLineRecipes);

        // Field Generator
        GTValues.RA.stdBuilder()
            .itemOutputs(Field_Generator_LuV.get(64))
            .itemInputsUnsafe(
                get(OrePrefixes.frameGt, Materials.HSSS, 48),
                MaterialLibAPI.getStack(Materials2Materials.HSSS, Materials2Shapes.plateDense, 32),
                get(QuantumStar, 96),
                get(Emitter_LuV, 192),
                get(OrePrefixes.wrapCircuit, Materials.ZPM, 12),
                get(OrePrefixes.cableGt16, Materials.YttriumBariumCuprate, 24))
            .circuit(FIELD_GENERATOR_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(3 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Ruridit, Materials2FluidShapes.fluidMolten, 24 * STACKS))
            .duration(24 * MINUTES)
            .eut(RECIPE_IV)
            .metadata(COAL_CASING_TIER, COAL_LuV)
            .addTo(componentAssemblyLineRecipes);
    }

    private static void zpmRecipes() {
        // Motor
        GTValues.RA.stdBuilder()
            .itemOutputs(Electric_Motor_ZPM.get(64))
            .itemInputsUnsafe(
                MaterialLibAPI.getStack(Materials2Materials.SamariumMagnetic, Materials2Shapes.stickLong, 48),
                get(OrePrefixes.cableGt16, Materials.VanadiumGallium, 24))
            .circuit(MOTOR_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(1 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, 36_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Europium, Materials2FluidShapes.fluidMolten, 18 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.NaquadahAlloy, Materials2FluidShapes.fluidMolten, 5 * STACKS + 5 * INGOTS + 3 * NUGGETS))
            .duration(24 * MINUTES)
            .eut(RECIPE_LuV)
            .metadata(COAL_CASING_TIER, COAL_ZPM)
            .addTo(componentAssemblyLineRecipes);

        // Piston
        GTValues.RA.stdBuilder()
            .itemOutputs(Electric_Piston_ZPM.get(64))
            .itemInputsUnsafe(
                get(Electric_Motor_ZPM, 48),
                MaterialLibAPI.getStack(Materials2Materials.NaquadahAlloy, Materials2Shapes.plateDense, 32),
                get(OrePrefixes.cableGt16, Materials.VanadiumGallium, 48))
            .circuit(PISTON_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(1 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, 36_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.NaquadahAlloy, Materials2FluidShapes.fluidMolten, 9 * STACKS + 26 * INGOTS + 6 * NUGGETS))
            .duration(24 * MINUTES)
            .eut(RECIPE_LuV)
            .metadata(COAL_CASING_TIER, COAL_ZPM)
            .addTo(componentAssemblyLineRecipes);

        // Robot Arm
        GTValues.RA.stdBuilder()
            .itemOutputs(Robot_Arm_ZPM.get(64))
            .itemInputsUnsafe(
                get(Electric_Motor_ZPM, 96),
                get(Electric_Piston_ZPM, 48),
                get(OrePrefixes.wrapCircuit, Materials.ZPM, 6),
                get(OrePrefixes.wrapCircuit, Materials.LuV, 12),
                get(OrePrefixes.wrapCircuit, Materials.IV, 24))
            .circuit(ROBOT_ARM_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(6 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, 36_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.VanadiumGallium, Materials2FluidShapes.fluidMolten, 9 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.NaquadahAlloy, Materials2FluidShapes.fluidMolten, 8 * STACKS + 16 * INGOTS))
            .duration(24 * MINUTES)
            .eut(RECIPE_LuV)
            .metadata(COAL_CASING_TIER, COAL_ZPM)
            .addTo(componentAssemblyLineRecipes);

        for (var rubber : new Materials[] {Materials.RubberSilicone, Materials.StyreneButadieneRubber }) {
            // Pump
            GTValues.RA.stdBuilder()
                .itemOutputs(Electric_Pump_ZPM.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_ZPM, 48),
                    MaterialLibAPI.getStack(Materials2Materials.NaquadahAlloy, Materials2Shapes.plateDense, 10),
                    get(OrePrefixes.cableGt16, Materials.VanadiumGallium, 24))
                .circuit(PUMP_CIRCUIT)
                .fluidInputs(
                    INDALLOY_140.getFluidStack(1 * STACKS + 32 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, 36_000),
                    MaterialLibAPI.getFluidStack(Materials2Materials.NaquadahAlloy, Materials2FluidShapes.fluidMolten, 7 * STACKS + 2 * INGOTS + 6 * NUGGETS),
                    MaterialLibAPI.getFluidStack(Materials2Materials.Enderium, Materials2FluidShapes.fluidMolten, 4 * STACKS + 32 * INGOTS),
                    rubber.getMolten(1 * STACKS + 32 * INGOTS))
                .duration(24 * MINUTES)
                .eut(RECIPE_LuV)
                .metadata(COAL_CASING_TIER, COAL_ZPM)
                .addTo(componentAssemblyLineRecipes);

            // Conveyor
            GTValues.RA.stdBuilder()
                .itemOutputs(Conveyor_Module_ZPM.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_ZPM, 96),
                    MaterialLibAPI.getStack(Materials2Materials.NaquadahAlloy, Materials2Shapes.plateDense, 10),
                    get(OrePrefixes.cableGt16, Materials.VanadiumGallium, 24))
                .circuit(CONVEYOR_CIRCUIT)
                .fluidInputs(
                    INDALLOY_140.getFluidStack(1 * STACKS + 32 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, 36_000),
                    rubber.getMolten(14 * STACKS + 58 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials2Materials.NaquadahAlloy, Materials2FluidShapes.fluidMolten, 3 * STACKS + 26 * INGOTS + 6 * NUGGETS))
                .duration(24 * MINUTES)
                .eut(RECIPE_LuV)
                .metadata(COAL_CASING_TIER, COAL_ZPM)
                .addTo(componentAssemblyLineRecipes);
        }

        // Sensor
        GTValues.RA.stdBuilder()
            .itemOutputs(Sensor_ZPM.get(64))
            .itemInputsUnsafe(
                get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 48),
                get(Electric_Motor_ZPM, 48),
                MaterialLibAPI.getStack(Materials2Materials.Osmiridium, Materials2Shapes.plateDense, 42),
                get(QuantumStar, 96),
                get(OrePrefixes.wrapCircuit, Materials.ZPM, 12))
            .circuit(SENSOR_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(6 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Trinium, Materials2FluidShapes.fluidMolten, 36 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.VanadiumGallium, Materials2FluidShapes.fluidMolten, 10 * STACKS + 32 * INGOTS))
            .duration(24 * MINUTES)
            .eut(RECIPE_LuV)
            .metadata(COAL_CASING_TIER, COAL_ZPM)
            .addTo(componentAssemblyLineRecipes);

        // Emitter
        GTValues.RA.stdBuilder()
            .itemOutputs(Emitter_ZPM.get(64))
            .itemInputsUnsafe(
                get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 48),
                get(Electric_Motor_ZPM, 48),
                get(QuantumStar, 96),
                get(OrePrefixes.wrapCircuit, Materials.ZPM, 12))
            .circuit(EMITTER_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(6 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Trinium, Materials2FluidShapes.fluidMolten, 36 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.VanadiumGallium, Materials2FluidShapes.fluidMolten, 10 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Osmiridium, Materials2FluidShapes.fluidMolten, 3 * STACKS))
            .duration(24 * MINUTES)
            .eut(RECIPE_LuV)
            .metadata(COAL_CASING_TIER, COAL_ZPM)
            .addTo(componentAssemblyLineRecipes);

        // Field Generator
        GTValues.RA.stdBuilder()
            .itemOutputs(Field_Generator_ZPM.get(64))
            .itemInputsUnsafe(
                get(OrePrefixes.frameGt, Materials.NaquadahAlloy, 48),
                MaterialLibAPI.getStack(Materials2Materials.NaquadahAlloy, Materials2Shapes.plateDense, 32),
                get(QuantumStar, 96),
                get(Emitter_ZPM, 192),
                get(OrePrefixes.wrapCircuit, Materials.UV, 12))
            .circuit(FIELD_GENERATOR_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(6 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Europium, Materials2FluidShapes.fluidMolten, 24 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.VanadiumGallium, Materials2FluidShapes.fluidMolten, 12 * STACKS))
            .duration(24 * MINUTES)
            .eut(RECIPE_LuV)
            .metadata(COAL_CASING_TIER, COAL_ZPM)
            .addTo(componentAssemblyLineRecipes);
    }

    private static void uvRecipes() {
        // Motor
        GTValues.RA.stdBuilder()
            .itemOutputs(Electric_Motor_UV.get(64))
            .itemInputsUnsafe(
                get(OrePrefixes.cableGt16, Materials.NaquadahAlloy, 24))
            .circuit(MOTOR_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(6 * STACKS + 48 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, 96_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Americium, Materials2FluidShapes.fluidMolten, 36 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Naquadria, Materials2FluidShapes.fluidMolten, 6 * STACKS + 48 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Neutronium, Materials2FluidShapes.fluidMolten, 5 * STACKS + 5 * INGOTS + 3 * NUGGETS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Samarium, Materials2FluidShapes.fluidMolten, 1 * STACKS + 32 * INGOTS))
            .duration(24 * MINUTES)
            .eut(RECIPE_ZPM)
            .metadata(COAL_CASING_TIER, COAL_UV)
            .addTo(componentAssemblyLineRecipes);

        // Piston
        GTValues.RA.stdBuilder()
            .itemOutputs(Electric_Piston_UV.get(64))
            .itemInputsUnsafe(
                get(Electric_Motor_UV, 48),
                MaterialLibAPI.getStack(Materials2Materials.Neutronium, Materials2Shapes.plateDense, 32),
                get(OrePrefixes.cableGt16, Materials.NaquadahAlloy, 48))
            .circuit(PISTON_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(6 * STACKS + 48 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, 96_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Neutronium, Materials2FluidShapes.fluidMolten, 9 * STACKS + 26 * INGOTS + 6 * NUGGETS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Naquadria, Materials2FluidShapes.fluidMolten, 6 * STACKS + 48 * INGOTS))
            .duration(24 * MINUTES)
            .eut(RECIPE_ZPM)
            .metadata(COAL_CASING_TIER, COAL_UV)
            .addTo(componentAssemblyLineRecipes);

        // Robot Arm
        GTValues.RA.stdBuilder()
            .itemOutputs(Robot_Arm_UV.get(64))
            .itemInputsUnsafe(
                get(Electric_Motor_UV, 96),
                get(Electric_Piston_UV, 48),
                get(OrePrefixes.wrapCircuit, Materials.UV, 6),
                get(OrePrefixes.wrapCircuit, Materials.ZPM, 12),
                get(OrePrefixes.wrapCircuit, Materials.LuV, 24))
            .circuit(ROBOT_ARM_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(12 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, 96_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.NaquadahAlloy, Materials2FluidShapes.fluidMolten, 9 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Neutronium, Materials2FluidShapes.fluidMolten, 8 * STACKS + 16 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Naquadria, Materials2FluidShapes.fluidMolten, 6 * STACKS + 48 * INGOTS))
            .duration(24 * MINUTES)
            .eut(RECIPE_ZPM)
            .metadata(COAL_CASING_TIER, COAL_UV)
            .addTo(componentAssemblyLineRecipes);

        for (var rubber : new Materials[] {Materials.RubberSilicone, Materials.StyreneButadieneRubber }) {
            // Pump
            GTValues.RA.stdBuilder()
                .itemOutputs(Electric_Pump_UV.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_UV, 48),
                    MaterialLibAPI.getStack(Materials2Materials.Neutronium, Materials2Shapes.plateDense, 10),
                    get(OrePrefixes.cableGt16, Materials.NaquadahAlloy, 24))
                .circuit(PUMP_CIRCUIT)
                .fluidInputs(
                    INDALLOY_140.getFluidStack(6 * STACKS + 48 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, 96_000),
                    MaterialLibAPI.getFluidStack(Materials2Materials.Naquadah, Materials2FluidShapes.fluidMolten, 9 * STACKS),
                    MaterialLibAPI.getFluidStack(Materials2Materials.Neutronium, Materials2FluidShapes.fluidMolten, 7 * STACKS + 2 * INGOTS + 6 * NUGGETS),
                    MaterialLibAPI.getFluidStack(Materials2Materials.Naquadria, Materials2FluidShapes.fluidMolten, 6 * STACKS + 48 * INGOTS),
                    rubber.getMolten(3 * STACKS))
                .duration(24 * MINUTES)
                .eut(RECIPE_ZPM)
                .metadata(COAL_CASING_TIER, COAL_UV)
                .addTo(componentAssemblyLineRecipes);

            // Conveyor
            GTValues.RA.stdBuilder()
                .itemOutputs(Conveyor_Module_UV.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_UV, 96),
                    MaterialLibAPI.getStack(Materials2Materials.Neutronium, Materials2Shapes.plateDense, 10),
                    get(OrePrefixes.cableGt16, Materials.NaquadahAlloy, 24))
                .circuit(CONVEYOR_CIRCUIT)
                .fluidInputs(
                    INDALLOY_140.getFluidStack(6 * STACKS + 48 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, 96_000),
                    rubber.getMolten(29 * STACKS + 61 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials2Materials.Naquadria, Materials2FluidShapes.fluidMolten, 6 * STACKS + 48 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials2Materials.Neutronium, Materials2FluidShapes.fluidMolten, 3 * STACKS + 26 * INGOTS + 6 * NUGGETS))
                .duration(24 * MINUTES)
                .eut(RECIPE_ZPM)
                .metadata(COAL_CASING_TIER, COAL_UV)
                .addTo(componentAssemblyLineRecipes);
        }

        // Sensor
        GTValues.RA.stdBuilder()
            .itemOutputs(Sensor_UV.get(64))
            .itemInputsUnsafe(
                get(OrePrefixes.frameGt, Materials.Neutronium, 48),
                get(Electric_Motor_UV, 48),
                MaterialLibAPI.getStack(Materials2Materials.Neutronium, Materials2Shapes.plateDense, 42),
                get(Gravistar, 192),
                get(OrePrefixes.wrapCircuit, Materials.UV, 12))
            .circuit(SENSOR_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(12 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Naquadria, Materials2FluidShapes.fluidMolten, 42 * STACKS + 48 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.NaquadahAlloy, Materials2FluidShapes.fluidMolten, 10 * STACKS + 32 * INGOTS))
            .duration(24 * MINUTES)
            .eut(RECIPE_ZPM)
            .metadata(COAL_CASING_TIER, COAL_UV)
            .addTo(componentAssemblyLineRecipes);

        // Emitter
        GTValues.RA.stdBuilder()
            .itemOutputs(Emitter_UV.get(64))
            .itemInputsUnsafe(
                get(OrePrefixes.frameGt, Materials.Neutronium, 48),
                get(Electric_Motor_UV, 48),
                get(Gravistar, 192),
                get(OrePrefixes.wrapCircuit, Materials.UV, 12))
            .circuit(EMITTER_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(12 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Naquadria, Materials2FluidShapes.fluidMolten, 42 * STACKS + 48 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.NaquadahAlloy, Materials2FluidShapes.fluidMolten, 10 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Neutronium, Materials2FluidShapes.fluidMolten, 3 * STACKS))
            .duration(24 * MINUTES)
            .eut(RECIPE_ZPM)
            .metadata(COAL_CASING_TIER, COAL_UV)
            .addTo(componentAssemblyLineRecipes);

        // Field Generator
        GTValues.RA.stdBuilder()
            .itemOutputs(Field_Generator_UV.get(64))
            .itemInputsUnsafe(
                get(OrePrefixes.frameGt, Materials.Neutronium, 48),
                MaterialLibAPI.getStack(Materials2Materials.Neutronium, Materials2Shapes.plateDense, 32),
                get(Gravistar, 96),
                get(Emitter_UV, 192),
                get(OrePrefixes.wrapCircuit, Materials.UHV, 12))
            .circuit(FIELD_GENERATOR_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(12 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Americium, Materials2FluidShapes.fluidMolten, 36 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.NaquadahAlloy, Materials2FluidShapes.fluidMolten, 12 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Naquadria, Materials2FluidShapes.fluidMolten, 6 * STACKS + 48 * INGOTS))
            .duration(24 * MINUTES)
            .eut(RECIPE_ZPM)
            .metadata(COAL_CASING_TIER, COAL_UV)
            .addTo(componentAssemblyLineRecipes);
    }

    private static void uhvRecipes() {
        // Motor
        GTValues.RA.stdBuilder()
            .itemOutputs(Electric_Motor_UHV.get(64))
            .itemInputsUnsafe(
                get(OrePrefixes.cableGt16, Materials.Bedrockium, 24))
            .circuit(MOTOR_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(13 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, 192_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Neutronium, Materials2FluidShapes.fluidMolten, 48 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Naquadria, Materials2FluidShapes.fluidMolten, 13 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.CosmicNeutronium, Materials2FluidShapes.fluidMolten, 10 * STACKS + 10 * INGOTS + 6 * NUGGETS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Samarium, Materials2FluidShapes.fluidMolten, 3 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UV)
            .metadata(COAL_CASING_TIER, COAL_UHV)
            .addTo(componentAssemblyLineRecipes);

        // Piston
        GTValues.RA.stdBuilder()
            .itemOutputs(Electric_Piston_UHV.get(64))
            .itemInputsUnsafe(
                get(Electric_Motor_UHV, 48),
                MaterialLibAPI.getStack(Materials2Materials.CosmicNeutronium, Materials2Shapes.plateDense, 32),
                get(OrePrefixes.cableGt16, Materials.Bedrockium, 48))
            .circuit(PISTON_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(13 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, 192_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.CosmicNeutronium, Materials2FluidShapes.fluidMolten, 18 * STACKS + 53 * INGOTS + 3 * NUGGETS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Naquadria, Materials2FluidShapes.fluidMolten, 13 * STACKS + 32 * INGOTS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UV)
            .metadata(COAL_CASING_TIER, COAL_UHV)
            .addTo(componentAssemblyLineRecipes);

        // Robot Arm
        GTValues.RA.stdBuilder()
            .itemOutputs(Robot_Arm_UHV.get(64))
            .itemInputsUnsafe(
                get(Electric_Motor_UHV, 96),
                get(Electric_Piston_UHV, 48),
                get(OrePrefixes.wrapCircuit, Materials.UHV, 6),
                get(OrePrefixes.wrapCircuit, Materials.UV, 12),
                get(OrePrefixes.wrapCircuit, Materials.ZPM, 24))
            .circuit(ROBOT_ARM_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(13 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, 192_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.CosmicNeutronium, Materials2FluidShapes.fluidMolten, 16 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Naquadria, Materials2FluidShapes.fluidMolten, 13 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Bedrockium, Materials2FluidShapes.fluidMolten, 9 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UV)
            .metadata(COAL_CASING_TIER, COAL_UHV)
            .addTo(componentAssemblyLineRecipes);

        for (var rubber : new Materials[] {Materials.RubberSilicone, Materials.StyreneButadieneRubber }) {
            // Pump
            GTValues.RA.stdBuilder()
                .itemOutputs(Electric_Pump_UHV.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_UHV, 48),
                    MaterialLibAPI.getStack(Materials2Materials.CosmicNeutronium, Materials2Shapes.plateDense, 21),
                    get(OrePrefixes.cableGt16, Materials.Bedrockium, 24))
                .circuit(PUMP_CIRCUIT)
                .fluidInputs(
                    INDALLOY_140.getFluidStack(13 * STACKS + 32 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, 192_000),
                    MaterialLibAPI.getFluidStack(Materials2Materials.CosmicNeutronium, Materials2FluidShapes.fluidMolten, 14 * STACKS + 5 * INGOTS + 3 * NUGGETS),
                    MaterialLibAPI.getFluidStack(Materials2Materials.Naquadria, Materials2FluidShapes.fluidMolten, 13 * STACKS + 32 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials2Materials.Neutronium, Materials2FluidShapes.fluidMolten, 9 * STACKS),
                    rubber.getMolten(6 * STACKS))
                .duration(40 * MINUTES)
                .eut(RECIPE_UV)
                .metadata(COAL_CASING_TIER, COAL_UHV)
                .addTo(componentAssemblyLineRecipes);

            // Conveyor
            GTValues.RA.stdBuilder()
                .itemOutputs(Conveyor_Module_UHV.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_UHV, 96),
                    MaterialLibAPI.getStack(Materials2Materials.CosmicNeutronium, Materials2Shapes.plateDense, 10),
                    get(OrePrefixes.cableGt16, Materials.Bedrockium, 24))
                .circuit(CONVEYOR_CIRCUIT)
                .fluidInputs(
                    INDALLOY_140.getFluidStack(13 * STACKS + 32 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, 192_000),
                    rubber.getMolten(29 * STACKS + 61 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials2Materials.Naquadria, Materials2FluidShapes.fluidMolten, 13 * STACKS + 32 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials2Materials.CosmicNeutronium, Materials2FluidShapes.fluidMolten, 6 * STACKS + 53 * INGOTS + 3 * NUGGETS))
                .duration(40 * MINUTES)
                .eut(RECIPE_UV)
                .metadata(COAL_CASING_TIER, COAL_UHV)
                .addTo(componentAssemblyLineRecipes);
        }

        // Sensor
        GTValues.RA.stdBuilder()
            .itemOutputs(Sensor_UHV.get(64))
            .itemInputsUnsafe(
                get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 48),
                get(Electric_Motor_UHV, 48),
                MaterialLibAPI.getStack(Materials2Materials.CosmicNeutronium, Materials2Shapes.plateDense, 42),
                get(Gravistar, 384),
                get(OrePrefixes.wrapCircuit, Materials.UHV, 12))
            .circuit(SENSOR_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(13 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.ElectrumFlux, Materials2FluidShapes.fluidMolten, 48 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Naquadria, Materials2FluidShapes.fluidMolten, 13 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Bedrockium, Materials2FluidShapes.fluidMolten, 10 * STACKS + 32 * INGOTS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UV)
            .metadata(COAL_CASING_TIER, COAL_UHV)
            .addTo(componentAssemblyLineRecipes);

        // Emitter
        GTValues.RA.stdBuilder()
            .itemOutputs(Emitter_UHV.get(64))
            .itemInputsUnsafe(
                get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 48),
                get(Electric_Motor_UHV, 48),
                get(Gravistar, 384),
                get(OrePrefixes.wrapCircuit, Materials.UHV, 12))
            .circuit(EMITTER_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(13 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.ElectrumFlux, Materials2FluidShapes.fluidMolten, 48 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Naquadria, Materials2FluidShapes.fluidMolten, 13 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Bedrockium, Materials2FluidShapes.fluidMolten, 10 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.CosmicNeutronium, Materials2FluidShapes.fluidMolten, 3 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UV)
            .metadata(COAL_CASING_TIER, COAL_UHV)
            .addTo(componentAssemblyLineRecipes);

        // Field Generator
        GTValues.RA.stdBuilder()
            .itemOutputs(Field_Generator_UHV.get(64))
            .itemInputsUnsafe(
                get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 48),
                MaterialLibAPI.getStack(Materials2Materials.CosmicNeutronium, Materials2Shapes.plateDense, 32),
                get(Gravistar, 192),
                get(Emitter_UHV, 192),
                get(OrePrefixes.wrapCircuit, Materials.UEV, 12))
            .circuit(FIELD_GENERATOR_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(13 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Neutronium, Materials2FluidShapes.fluidMolten, 48 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Naquadria, Materials2FluidShapes.fluidMolten, 13 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Bedrockium, Materials2FluidShapes.fluidMolten, 12 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UV)
            .metadata(COAL_CASING_TIER, COAL_UHV)
            .addTo(componentAssemblyLineRecipes);
    }

    private static void uevRecipes() {
        // Motor
        GTValues.RA.stdBuilder()
            .itemOutputs(Electric_Motor_UEV.get(64))
            .itemInputsUnsafe(
                get(OrePrefixes.cableGt16, Materials.Draconium, 24))
            .circuit(MOTOR_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, 192_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.CosmicNeutronium, Materials2FluidShapes.fluidMolten, 48 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Infinity, Materials2FluidShapes.fluidMolten, 16 * STACKS + 10 * INGOTS + 6 * NUGGETS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Quantium, Materials2FluidShapes.fluidMolten, 13 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.TengamPurified, Materials2FluidShapes.fluidMolten, 6 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UHV)
            .metadata(COAL_CASING_TIER, COAL_UEV)
            .addTo(componentAssemblyLineRecipes);

        // Piston
        GTValues.RA.stdBuilder()
            .itemOutputs(Electric_Piston_UEV.get(64))
            .itemInputsUnsafe(
                get(Electric_Motor_UEV, 48),
                MaterialLibAPI.getStack(Materials2Materials.Infinity, Materials2Shapes.plateDense, 32),
                get(OrePrefixes.cableGt16, Materials.Draconium, 48))
            .circuit(PISTON_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, 192_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Infinity, Materials2FluidShapes.fluidMolten, 18 * STACKS + 53 * INGOTS + 3 * NUGGETS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Quantium, Materials2FluidShapes.fluidMolten, 13 * STACKS + 32 * INGOTS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UHV)
            .metadata(COAL_CASING_TIER, COAL_UEV)
            .addTo(componentAssemblyLineRecipes);

        // Robot Arm
        GTValues.RA.stdBuilder()
            .itemOutputs(Robot_Arm_UEV.get(64))
            .itemInputsUnsafe(
                get(Electric_Motor_UEV, 96),
                get(Electric_Piston_UEV, 48),
                get(OrePrefixes.wrapCircuit, Materials.UEV, 6),
                get(OrePrefixes.wrapCircuit, Materials.UHV, 12),
                get(OrePrefixes.wrapCircuit, Materials.UV, 24))
            .circuit(ROBOT_ARM_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, 192_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.Infinity, Materials2FluidShapes.fluidMolten, 16 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Quantium, Materials2FluidShapes.fluidMolten, 13 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Draconium, Materials2FluidShapes.fluidMolten, 9 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UHV)
            .metadata(COAL_CASING_TIER, COAL_UEV)
            .addTo(componentAssemblyLineRecipes);

        for (var rubber : new Materials[] {Materials.RubberSilicone, Materials.StyreneButadieneRubber }) {
            // Pump
            GTValues.RA.stdBuilder()
                .itemOutputs(Electric_Pump_UEV.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_UEV, 48),
                    get(OrePrefixes.pipeLarge, Materials.NetherStar, 96),
                    MaterialLibAPI.getStack(Materials2Materials.Infinity, Materials2Shapes.plateDense, 21),
                    get(OrePrefixes.cableGt16, Materials.Draconium, 24))
                .circuit(PUMP_CIRCUIT)
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, 192_000),
                    MaterialLibAPI.getFluidStack(Materials2Materials.Infinity, Materials2FluidShapes.fluidMolten, 14 * STACKS + 5 * INGOTS + 3 * NUGGETS),
                    MaterialLibAPI.getFluidStack(Materials2Materials.Quantium, Materials2FluidShapes.fluidMolten, 13 * STACKS + 32 * INGOTS),
                    rubber.getMolten(12 * STACKS))
                .duration(40 * MINUTES)
                .eut(RECIPE_UHV)
                .metadata(COAL_CASING_TIER, COAL_UEV)
                .addTo(componentAssemblyLineRecipes);

            // Conveyor
            GTValues.RA.stdBuilder()
                .itemOutputs(Conveyor_Module_UEV.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_UEV, 96),
                    MaterialLibAPI.getStack(Materials2Materials.Infinity, Materials2Shapes.plateDense, 10),
                    get(OrePrefixes.cableGt16, Materials.Draconium, 24))
                .circuit(CONVEYOR_CIRCUIT)
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials2Materials.Lubricant, Materials2FluidShapes.fluidLiquid, 192_000),
                    rubber.getMolten(59 * STACKS + 58 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials2Materials.Quantium, Materials2FluidShapes.fluidMolten, 13 * STACKS + 32 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials2Materials.Infinity, Materials2FluidShapes.fluidMolten, 6 * STACKS + 53 * INGOTS + 3 * NUGGETS))
                .duration(40 * MINUTES)
                .eut(RECIPE_UHV)
                .metadata(COAL_CASING_TIER, COAL_UEV)
                .addTo(componentAssemblyLineRecipes);
        }

        // Sensor
        GTValues.RA.stdBuilder()
            .itemOutputs(Sensor_UEV.get(64))
            .itemInputsUnsafe(
                get(OrePrefixes.frameGt, Materials.Infinity, 48),
                get(Electric_Motor_UEV, 48),
                MaterialLibAPI.getStack(Materials2Materials.Infinity, Materials2Shapes.plateDense, 42),
                get(Gravistar, 768),
                get(OrePrefixes.wrapCircuit, Materials.UEV, 12))
            .circuit(SENSOR_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.InfinityCatalyst, Materials2FluidShapes.fluidMolten, 48 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Quantium, Materials2FluidShapes.fluidMolten, 13 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Draconium, Materials2FluidShapes.fluidMolten, 10 * STACKS + 32 * INGOTS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UHV)
            .metadata(COAL_CASING_TIER, COAL_UEV)
            .addTo(componentAssemblyLineRecipes);

        // Emitter
        GTValues.RA.stdBuilder()
            .itemOutputs(Emitter_UEV.get(64))
            .itemInputsUnsafe(
                get(OrePrefixes.frameGt, Materials.Infinity, 48),
                get(Electric_Motor_UEV, 48),
                get(Gravistar, 768),
                get(OrePrefixes.wrapCircuit, Materials.UEV, 12))
            .circuit(EMITTER_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.InfinityCatalyst, Materials2FluidShapes.fluidMolten, 48 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Quantium, Materials2FluidShapes.fluidMolten, 13 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Draconium, Materials2FluidShapes.fluidMolten, 10 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Infinity, Materials2FluidShapes.fluidMolten, 6 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UHV)
            .metadata(COAL_CASING_TIER, COAL_UEV)
            .addTo(componentAssemblyLineRecipes);

        // Field Generator
        GTValues.RA.stdBuilder()
            .itemOutputs(Field_Generator_UEV.get(64))
            .itemInputsUnsafe(
                get(OrePrefixes.frameGt, Materials.Infinity, 48),
                MaterialLibAPI.getStack(Materials2Materials.Infinity, Materials2Shapes.plateDense, 32),
                get(Gravistar, 384),
                get(Emitter_UEV, 192),
                get(OrePrefixes.wrapCircuit, Materials.UIV, 12))
            .circuit(FIELD_GENERATOR_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Tritanium, Materials2FluidShapes.fluidMolten, 48 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Quantium, Materials2FluidShapes.fluidMolten, 13 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Draconium, Materials2FluidShapes.fluidMolten, 12 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UHV)
            .metadata(COAL_CASING_TIER, COAL_UEV)
            .addTo(componentAssemblyLineRecipes);
    }

    /**
     * UIV Special Rule:
     * Proto-Halkonite parts are reduced to their composite parts:
     * - Create two recipes, one for Infinity, one for Creon + Mellion (50% each)
     * - Include the Superfluid cost from the Vacuum Freezer step
     * - Drop the Super Coolant cost from the Vacuum Freezer step
     */
    private static void uivRecipes() {
        // Motor
        GTValues.RA.stdBuilder()
            .itemOutputs(Electric_Motor_UIV.get(64))
            .itemInputsUnsafe(
                get(OrePrefixes.cableGt16, Materials.NetherStar, 24))
            .circuit(MOTOR_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.dimensionallyshiftedsuperfluid, Materials2FluidShapes.fluidLiquid, 32 * STACKS + 53 * INGOTS + 3 * NUGGETS),
                MaterialLibAPI.getFluidStack(Materials2Materials.protohalkonitebase, Materials2FluidShapes.fluidLiquid, 48 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Infinity, Materials2FluidShapes.fluidMolten, 48 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.TranscendentMetal, Materials2FluidShapes.fluidMolten, 16 * STACKS + 10 * INGOTS + 6 * NUGGETS),
                MaterialLibAPI.getFluidStack(Materials2Materials.TengamPurified, Materials2FluidShapes.fluidMolten, 12 * STACKS),
                CELESTIAL_TUNGSTEN.getFluidStack(3 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UEV)
            .metadata(COAL_CASING_TIER, COAL_UIV)
            .addTo(componentAssemblyLineRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(Electric_Motor_UIV.get(64))
            .itemInputsUnsafe(
                get(OrePrefixes.cableGt16, Materials.NetherStar, 24))
            .circuit(MOTOR_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.dimensionallyshiftedsuperfluid, Materials2FluidShapes.fluidLiquid, 32 * STACKS + 53 * INGOTS + 3 * NUGGETS),
                MaterialLibAPI.getFluidStack(Materials2Materials.protohalkonitebase, Materials2FluidShapes.fluidLiquid, 24 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Creon, Materials2FluidShapes.fluidMolten, 24 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Mellion, Materials2FluidShapes.fluidMolten, 24 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.TranscendentMetal, Materials2FluidShapes.fluidMolten, 16 * STACKS + 10 * INGOTS + 6 * NUGGETS),
                MaterialLibAPI.getFluidStack(Materials2Materials.TengamPurified, Materials2FluidShapes.fluidMolten, 12 * STACKS),
                CELESTIAL_TUNGSTEN.getFluidStack(3 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UEV)
            .metadata(COAL_CASING_TIER, COAL_UIV)
            .addTo(componentAssemblyLineRecipes);

        // Piston
        GTValues.RA.stdBuilder()
            .itemOutputs(Electric_Piston_UIV.get(64))
            .itemInputsUnsafe(
                get(Electric_Motor_UIV, 48),
                MaterialLibAPI.getStack(Materials2Materials.TranscendentMetal, Materials2Shapes.plateDense, 32),
                get(OrePrefixes.cableGt16, Materials.NetherStar, 48))
            .circuit(PISTON_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.dimensionallyshiftedsuperfluid, Materials2FluidShapes.fluidLiquid, 192_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.TranscendentMetal, Materials2FluidShapes.fluidMolten, 18 * STACKS + 53 * INGOTS + 3 * NUGGETS),
                CELESTIAL_TUNGSTEN.getFluidStack(3 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UEV)
            .metadata(COAL_CASING_TIER, COAL_UIV)
            .addTo(componentAssemblyLineRecipes);

        // Robot Arm
        GTValues.RA.stdBuilder()
            .itemOutputs(Robot_Arm_UIV.get(64))
            .itemInputsUnsafe(
                get(Electric_Motor_UIV, 96),
                get(Electric_Piston_UIV, 48),
                get(OrePrefixes.wrapCircuit, Materials.UIV, 6),
                get(OrePrefixes.wrapCircuit, Materials.UEV, 12),
                get(OrePrefixes.wrapCircuit, Materials.UHV, 24),
                get(OrePrefixes.cableGt16, Materials.NetherStar, 72))
            .circuit(ROBOT_ARM_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.dimensionallyshiftedsuperfluid, Materials2FluidShapes.fluidLiquid, 192_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.TranscendentMetal, Materials2FluidShapes.fluidMolten, 16 * STACKS + 32 * INGOTS),
                CELESTIAL_TUNGSTEN.getFluidStack(3 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UEV)
            .metadata(COAL_CASING_TIER, COAL_UIV)
            .addTo(componentAssemblyLineRecipes);

        for (var rubber : new Materials[] {Materials.RubberSilicone, Materials.StyreneButadieneRubber }) {
            // Pump
            GTValues.RA.stdBuilder()
                .itemOutputs(Electric_Pump_UIV.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_UIV, 48),
                    MaterialLibAPI.getStack(Materials2Materials.TranscendentMetal, Materials2Shapes.plateDense, 21),
                    get(OrePrefixes.cableGt16, Materials.NetherStar, 24))
                .circuit(PUMP_CIRCUIT)
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials2Materials.dimensionallyshiftedsuperfluid, Materials2FluidShapes.fluidLiquid, 192_000),
                    MaterialLibAPI.getFluidStack(Materials2Materials.TranscendentMetal, Materials2FluidShapes.fluidMolten, 14 * STACKS + 5 * INGOTS + 3 * NUGGETS),
                    rubber.getMolten(12 * STACKS),
                    MaterialLibAPI.getFluidStack(Materials2Materials.DraconiumAwakened, Materials2FluidShapes.fluidMolten, 9 * STACKS),
                    CELESTIAL_TUNGSTEN.getFluidStack(3 * STACKS))
                .duration(40 * MINUTES)
                .eut(RECIPE_UEV)
                .metadata(COAL_CASING_TIER, COAL_UIV)
                .addTo(componentAssemblyLineRecipes);

            // Conveyor
            GTValues.RA.stdBuilder()
                .itemOutputs(Conveyor_Module_UIV.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_UIV, 96),
                    MaterialLibAPI.getStack(Materials2Materials.TranscendentMetal, Materials2Shapes.plateDense, 10),
                    get(OrePrefixes.cableGt16, Materials.NetherStar, 24))
                .circuit(CONVEYOR_CIRCUIT)
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials2Materials.dimensionallyshiftedsuperfluid, Materials2FluidShapes.fluidLiquid, 192_000),
                    rubber.getMolten(59 * STACKS + 58 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials2Materials.TranscendentMetal, Materials2FluidShapes.fluidMolten, 6 * STACKS + 53 * INGOTS + 3 * NUGGETS),
                    CELESTIAL_TUNGSTEN.getFluidStack(3 * STACKS))
                .duration(40 * MINUTES)
                .eut(RECIPE_UEV)
                .metadata(COAL_CASING_TIER, COAL_UIV)
                .addTo(componentAssemblyLineRecipes);
        }

        // Sensor
        GTValues.RA.stdBuilder()
            .itemOutputs(Sensor_UIV.get(64))
            .itemInputsUnsafe(
                get(OrePrefixes.frameGt, Materials.TranscendentMetal, 48),
                get(Electric_Motor_UIV, 48),
                MaterialLibAPI.getStack(Materials2Materials.TranscendentMetal, Materials2Shapes.plateDense, 42),
                get(NuclearStar, 96),
                get(OrePrefixes.wrapCircuit, Materials.UIV, 12),
                get(OrePrefixes.cableGt16, Materials.NetherStar, 84))
            .circuit(SENSOR_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                TRINIUM_REINFORCED_STEEL.getFluidStack(12 * STACKS),
                LAFIUM.getFluidStack(12 * STACKS),
                CINOBITE.getFluidStack(12 * STACKS),
                PIKYONIUM.getFluidStack(12 * STACKS),
                CELESTIAL_TUNGSTEN.getFluidStack(3 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UEV)
            .metadata(COAL_CASING_TIER, COAL_UIV)
            .addTo(componentAssemblyLineRecipes);

        // Emitter
        GTValues.RA.stdBuilder()
            .itemOutputs(Emitter_UIV.get(64))
            .itemInputsUnsafe(
                get(OrePrefixes.frameGt, Materials.TranscendentMetal, 48),
                get(Electric_Motor_UIV, 48),
                get(NuclearStar, 96),
                get(OrePrefixes.wrapCircuit, Materials.UIV, 12),
                get(OrePrefixes.cableGt16, Materials.NetherStar, 84))
            .circuit(EMITTER_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                TRINIUM_REINFORCED_STEEL.getFluidStack(12 * STACKS),
                LAFIUM.getFluidStack(12 * STACKS),
                CINOBITE.getFluidStack(12 * STACKS),
                PIKYONIUM.getFluidStack(12 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.TranscendentMetal, Materials2FluidShapes.fluidMolten, 6 * STACKS),
                CELESTIAL_TUNGSTEN.getFluidStack(3 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UEV)
            .metadata(COAL_CASING_TIER, COAL_UIV)
            .addTo(componentAssemblyLineRecipes);

        // Field Generator
        GTValues.RA.stdBuilder()
            .itemOutputs(Field_Generator_UIV.get(64))
            .itemInputsUnsafe(
                get(OrePrefixes.frameGt, Materials.TranscendentMetal, 48),
                MaterialLibAPI.getStack(Materials2Materials.TranscendentMetal, Materials2Shapes.plateDense, 32),
                get(NuclearStar, 48),
                get(Emitter_UIV, 192),
                get(OrePrefixes.wrapCircuit, Materials.UMV, 12),
                get(OrePrefixes.cableGt16, Materials.NetherStar, 96))
            .circuit(FIELD_GENERATOR_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.dimensionallyshiftedsuperfluid, Materials2FluidShapes.fluidLiquid, 12 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.protohalkonitebase, Materials2FluidShapes.fluidLiquid, 48 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Infinity, Materials2FluidShapes.fluidMolten, 48 * STACKS),
                CELESTIAL_TUNGSTEN.getFluidStack(3 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UEV)
            .metadata(COAL_CASING_TIER, COAL_UIV)
            .addTo(componentAssemblyLineRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(Field_Generator_UIV.get(64))
            .itemInputsUnsafe(
                get(OrePrefixes.frameGt, Materials.TranscendentMetal, 48),
                MaterialLibAPI.getStack(Materials2Materials.TranscendentMetal, Materials2Shapes.plateDense, 32),
                get(NuclearStar, 48),
                get(Emitter_UIV, 192),
                get(OrePrefixes.wrapCircuit, Materials.UMV, 12),
                get(OrePrefixes.cableGt16, Materials.NetherStar, 96))
            .circuit(FIELD_GENERATOR_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.dimensionallyshiftedsuperfluid, Materials2FluidShapes.fluidLiquid, 12 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.protohalkonitebase, Materials2FluidShapes.fluidLiquid, 24 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Creon, Materials2FluidShapes.fluidMolten, 24 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Mellion, Materials2FluidShapes.fluidMolten, 24 * STACKS),
                CELESTIAL_TUNGSTEN.getFluidStack(3 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UEV)
            .metadata(COAL_CASING_TIER, COAL_UIV)
            .addTo(componentAssemblyLineRecipes);
    }

    private static void umvRecipes() {
        // Motor
        GTValues.RA.stdBuilder()
            .itemOutputs(Electric_Motor_UMV.get(64))
            .itemInputsUnsafe(
                get(OrePrefixes.cableGt16, Materials.Quantium, 24))
            .circuit(MOTOR_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.dimensionallyshiftedsuperfluid, Materials2FluidShapes.fluidLiquid, 192_000),
                HYPOGEN.getFluidStack(51 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.TengamPurified, Materials2FluidShapes.fluidMolten, 24 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.SpaceTime, Materials2FluidShapes.fluidMolten, 16 * STACKS + 10 * INGOTS + 6 * NUGGETS),
                CELESTIAL_TUNGSTEN.getFluidStack(3 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UIV)
            .metadata(COAL_CASING_TIER, COAL_UMV)
            .addTo(componentAssemblyLineRecipes);

        // Piston
        GTValues.RA.stdBuilder()
            .itemOutputs(Electric_Piston_UMV.get(64))
            .itemInputsUnsafe(
                get(Electric_Motor_UMV, 48),
                MaterialLibAPI.getStack(Materials2Materials.SpaceTime, Materials2Shapes.plateDense, 32),
                get(OrePrefixes.cableGt16, Materials.Quantium, 48))
            .circuit(PISTON_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.dimensionallyshiftedsuperfluid, Materials2FluidShapes.fluidLiquid, 192_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.SpaceTime, Materials2FluidShapes.fluidMolten, 18 * STACKS + 53 * INGOTS + 3 * NUGGETS),
                HYPOGEN.getFluidStack(3 * STACKS),
                CELESTIAL_TUNGSTEN.getFluidStack(3 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UIV)
            .metadata(COAL_CASING_TIER, COAL_UMV)
            .addTo(componentAssemblyLineRecipes);

        // Robot Arm
        GTValues.RA.stdBuilder()
            .itemOutputs(Robot_Arm_UMV.get(64))
            .itemInputsUnsafe(
                get(Electric_Motor_UMV, 96),
                get(Electric_Piston_UMV, 48),
                get(OrePrefixes.wrapCircuit, Materials.UMV, 6),
                get(OrePrefixes.wrapCircuit, Materials.UIV, 12),
                get(OrePrefixes.wrapCircuit, Materials.UEV, 24))
            .circuit(ROBOT_ARM_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.dimensionallyshiftedsuperfluid, Materials2FluidShapes.fluidLiquid, 192_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.SpaceTime, Materials2FluidShapes.fluidMolten, 16 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Quantium, Materials2FluidShapes.fluidMolten, 9 * STACKS),
                HYPOGEN.getFluidStack(3 * STACKS),
                CELESTIAL_TUNGSTEN.getFluidStack(3 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UIV)
            .metadata(COAL_CASING_TIER, COAL_UMV)
            .addTo(componentAssemblyLineRecipes);

        for (var rubber : new Materials[] {Materials.RubberSilicone, Materials.StyreneButadieneRubber }) {
            // Pump
            GTValues.RA.stdBuilder()
                .itemOutputs(Electric_Pump_UMV.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_UMV, 48),
                    MaterialLibAPI.getStack(Materials2Materials.SpaceTime, Materials2Shapes.plateDense, 21),
                    get(OrePrefixes.cableGt16, Materials.Quantium, 24))
                .circuit(PUMP_CIRCUIT)
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials2Materials.dimensionallyshiftedsuperfluid, Materials2FluidShapes.fluidLiquid, 192_000),
                    MaterialLibAPI.getFluidStack(Materials2Materials.SpaceTime, Materials2FluidShapes.fluidMolten, 14 * STACKS + 5 * INGOTS + 3 * NUGGETS),
                    rubber.getMolten(12 * STACKS),
                    MaterialLibAPI.getFluidStack(Materials2Materials.Infinity, Materials2FluidShapes.fluidMolten, 9 * STACKS),
                    HYPOGEN.getFluidStack(3 * STACKS),
                    CELESTIAL_TUNGSTEN.getFluidStack(3 * STACKS))
                .duration(40 * MINUTES)
                .eut(RECIPE_UIV)
                .metadata(COAL_CASING_TIER, COAL_UMV)
                .addTo(componentAssemblyLineRecipes);

            // Conveyor
            GTValues.RA.stdBuilder()
                .itemOutputs(Conveyor_Module_UMV.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_UMV, 96),
                    MaterialLibAPI.getStack(Materials2Materials.SpaceTime, Materials2Shapes.plateDense, 10),
                    get(OrePrefixes.cableGt16, Materials.Quantium, 24))
                .circuit(CONVEYOR_CIRCUIT)
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials2Materials.dimensionallyshiftedsuperfluid, Materials2FluidShapes.fluidLiquid, 192_000),
                    rubber.getMolten(59 * STACKS + 58 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials2Materials.SpaceTime, Materials2FluidShapes.fluidMolten, 6 * STACKS + 53 * INGOTS + 3 * NUGGETS),
                    HYPOGEN.getFluidStack(3 * STACKS),
                    CELESTIAL_TUNGSTEN.getFluidStack(3 * STACKS))
                .duration(40 * MINUTES)
                .eut(RECIPE_UIV)
                .metadata(COAL_CASING_TIER, COAL_UMV)
                .addTo(componentAssemblyLineRecipes);
        }

        // Sensor
        GTValues.RA.stdBuilder()
            .itemOutputs(Sensor_UMV.get(64))
            .itemInputsUnsafe(
                get(OrePrefixes.frameGt, Materials.SpaceTime, 48),
                get(Electric_Motor_UMV, 48),
                MaterialLibAPI.getStack(Materials2Materials.SpaceTime, Materials2Shapes.plateDense, 42),
                get(NuclearStar, 192),
                get(OrePrefixes.wrapCircuit, Materials.UMV, 12))
            .circuit(SENSOR_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                CELESTIAL_TUNGSTEN.getFluidStack(15 * STACKS),
                QUANTUM.getFluidStack(12 * STACKS),
                ASTRAL_TITANIUM.getFluidStack(12 * STACKS),
                TITANSTEEL.getFluidStack(12 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Quantium, Materials2FluidShapes.fluidMolten, 10 * STACKS + 32 * INGOTS),
                HYPOGEN.getFluidStack(3 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UIV)
            .metadata(COAL_CASING_TIER, COAL_UMV)
            .addTo(componentAssemblyLineRecipes);

        // Emitter
        GTValues.RA.stdBuilder()
            .itemOutputs(Emitter_UMV.get(64))
            .itemInputsUnsafe(
                get(OrePrefixes.frameGt, Materials.SpaceTime, 48),
                get(Electric_Motor_UMV, 48),
                get(NuclearStar, 192),
                get(OrePrefixes.wrapCircuit, Materials.UMV, 12))
            .circuit(EMITTER_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                CELESTIAL_TUNGSTEN.getFluidStack(15 * STACKS),
                QUANTUM.getFluidStack(12 * STACKS),
                ASTRAL_TITANIUM.getFluidStack(12 * STACKS),
                TITANSTEEL.getFluidStack(12 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Quantium, Materials2FluidShapes.fluidMolten, 10 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.SpaceTime, Materials2FluidShapes.fluidMolten, 6 * STACKS),
                HYPOGEN.getFluidStack(3 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UIV)
            .metadata(COAL_CASING_TIER, COAL_UMV)
            .addTo(componentAssemblyLineRecipes);

        // Field Generator
        GTValues.RA.stdBuilder()
            .itemOutputs(Field_Generator_UMV.get(64))
            .itemInputsUnsafe(
                get(OrePrefixes.frameGt, Materials.SpaceTime, 48),
                MaterialLibAPI.getStack(Materials2Materials.SpaceTime, Materials2Shapes.plateDense, 32),
                get(NuclearStar, 96),
                get(Emitter_UMV, 192),
                get(OrePrefixes.wrapCircuit, Materials.UXV, 12))
            .circuit(FIELD_GENERATOR_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                HYPOGEN.getFluidStack(51 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Quantium, Materials2FluidShapes.fluidMolten, 12 * STACKS),
                CELESTIAL_TUNGSTEN.getFluidStack(3 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UIV)
            .metadata(COAL_CASING_TIER, COAL_UMV)
            .addTo(componentAssemblyLineRecipes);
    }

    /**
     * UXV Special Rule:
     * MHDCSM parts are reduced to their composite parts:
     * - When breaking down MHDCSM parts into fluid with usual rules, add an equal amount of Eternity as well.
     * - Gather the total number of UHV circuits that would have been used, reduce by 25%, then add as circuit wraps.
     * - Put these added UHV circuits in the middle, approximately where they would go if this recipe took circuits.
     *   If this recipe does take circuits, put them directly after higher tiered circuits.
     */
    private static void uxvRecipes() {
        // Motor
        GTValues.RA.stdBuilder()
            .itemOutputs(Electric_Motor_UXV.get(64))
            .itemInputsUnsafe(
                get(EnergisedTesseract, 48),
                get(OrePrefixes.wrapCircuit, Materials.UHV, 114),
                get(OrePrefixes.wireGt16, Materials.SpaceTime, 48),
                get(OrePrefixes.nanite, Materials.Gold, 12))
            .circuit(MOTOR_CIRCUIT)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.dimensionallyshiftedsuperfluid, Materials2FluidShapes.fluidLiquid, 384_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.MagnetohydrodynamicallyConstrainedStarMatter, Materials2FluidShapes.fluidMolten, 31 * STACKS + 10 * INGOTS + 6 * NUGGETS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Eternity, Materials2FluidShapes.fluidMolten, 28 * STACKS + 10 * INGOTS + 6 * NUGGETS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Universium, Materials2FluidShapes.fluidMolten, 15 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Magmatter, Materials2FluidShapes.fluidMolten, 12 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.SuperconductorUMVBase, Materials2FluidShapes.fluidMolten, 12 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.SpaceTime, Materials2FluidShapes.fluidMolten, 3 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UMV)
            .metadata(COAL_CASING_TIER, COAL_UXV)
            .addTo(componentAssemblyLineRecipes);

        // Piston
        GTValues.RA.stdBuilder()
            .itemOutputs(Electric_Piston_UXV.get(64))
            .itemInputsUnsafe(
                get(Electric_Motor_UXV, 48),
                get(OrePrefixes.wrapCircuit, Materials.UHV, 84),
                get(OrePrefixes.nanite, Materials.Gold, 12))
            .circuit(PISTON_CIRCUIT)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.dimensionallyshiftedsuperfluid, Materials2FluidShapes.fluidLiquid, 384_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.MagnetohydrodynamicallyConstrainedStarMatter, Materials2FluidShapes.fluidMolten, 26 * STACKS + 21 * INGOTS + 3 * NUGGETS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Eternity, Materials2FluidShapes.fluidMolten, 23 * STACKS + 21 * INGOTS + 3 * NUGGETS),
                MaterialLibAPI.getFluidStack(Materials2Materials.SpaceTime, Materials2FluidShapes.fluidMolten, 15 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Magmatter, Materials2FluidShapes.fluidMolten, 9 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Universium, Materials2FluidShapes.fluidMolten, 3 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UMV)
            .metadata(COAL_CASING_TIER, COAL_UXV)
            .addTo(componentAssemblyLineRecipes);

        // Robot Arm
        GTValues.RA.stdBuilder()
            .itemOutputs(Robot_Arm_UXV.get(64))
            .itemInputsUnsafe(
                get(Electric_Motor_UXV, 96),
                get(Electric_Piston_UXV, 48),
                get(OrePrefixes.wrapCircuit, Materials.UXV, 6),
                get(OrePrefixes.wrapCircuit, Materials.UMV, 12),
                get(OrePrefixes.wrapCircuit, Materials.UIV, 24),
                get(OrePrefixes.wrapCircuit, Materials.UHV, 54),
                get(OrePrefixes.nanite, Materials.Gold, 24))
            .circuit(ROBOT_ARM_CIRCUIT)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.dimensionallyshiftedsuperfluid, Materials2FluidShapes.fluidLiquid, 384_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.SpaceTime, Materials2FluidShapes.fluidMolten, 21 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.MagnetohydrodynamicallyConstrainedStarMatter, Materials2FluidShapes.fluidMolten, 19 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Eternity, Materials2FluidShapes.fluidMolten, 16 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Magmatter, Materials2FluidShapes.fluidMolten, 10 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Universium, Materials2FluidShapes.fluidMolten, 3 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UMV)
            .metadata(COAL_CASING_TIER, COAL_UXV)
            .addTo(componentAssemblyLineRecipes);

        // Pump
        GTValues.RA.stdBuilder()
            .itemOutputs(Electric_Pump_UXV.get(64))
            .itemInputsUnsafe(
                get(Electric_Motor_UXV, 48),
                get(OrePrefixes.wrapCircuit, Materials.UHV, 42),
                get(OrePrefixes.wireGt16, Materials.SpaceTime, 48),
                get(OrePrefixes.nanite, Materials.Gold, 12))
            .circuit(PUMP_CIRCUIT)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.dimensionallyshiftedsuperfluid, Materials2FluidShapes.fluidLiquid, 384_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.MagnetohydrodynamicallyConstrainedStarMatter, Materials2FluidShapes.fluidMolten, 20 * STACKS + 5 * INGOTS + 3 * NUGGETS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Eternity, Materials2FluidShapes.fluidMolten, 17 * STACKS + 5 * INGOTS + 3 * NUGGETS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Magmatter, Materials2FluidShapes.fluidMolten, 12 * STACKS + 48 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.RadoxPoly, Materials2FluidShapes.fluidMolten, 12 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Kevlar, Materials2FluidShapes.fluidMolten, 12 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.SpaceTime, Materials2FluidShapes.fluidMolten, 12 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Universium, Materials2FluidShapes.fluidMolten, 3 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UMV)
            .metadata(COAL_CASING_TIER, COAL_UXV)
            .addTo(componentAssemblyLineRecipes);

        // Conveyor
        GTValues.RA.stdBuilder()
            .itemOutputs(Conveyor_Module_UXV.get(64))
            .itemInputsUnsafe(
                get(Electric_Motor_UXV, 96),
                get(OrePrefixes.wrapCircuit, Materials.UHV, 36),
                get(OrePrefixes.wireGt16, Materials.SpaceTime, 48),
                get(OrePrefixes.nanite, Materials.Gold, 12))
            .circuit(CONVEYOR_CIRCUIT)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.dimensionallyshiftedsuperfluid, Materials2FluidShapes.fluidLiquid, 384_000),
                MaterialLibAPI.getFluidStack(Materials2Materials.RadoxPoly, Materials2FluidShapes.fluidMolten, 59 * STACKS + 58 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Kevlar, Materials2FluidShapes.fluidMolten, 59 * STACKS + 58 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.MagnetohydrodynamicallyConstrainedStarMatter, Materials2FluidShapes.fluidMolten, 11 * STACKS + 21 * INGOTS + 3 * NUGGETS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Eternity, Materials2FluidShapes.fluidMolten, 8 * STACKS + 21 * INGOTS + 3 * NUGGETS),
                MaterialLibAPI.getFluidStack(Materials2Materials.SpaceTime, Materials2FluidShapes.fluidMolten, 3 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Universium, Materials2FluidShapes.fluidMolten, 3 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UMV)
            .metadata(COAL_CASING_TIER, COAL_UXV)
            .addTo(componentAssemblyLineRecipes);

        // Sensor
        GTValues.RA.stdBuilder()
            .itemOutputs(Sensor_UXV.get(64))
            .itemInputsUnsafe(
                get(OrePrefixes.frameGt, Materials.MHDCSM, 48),
                get(Electric_Motor_UXV, 48),
                get(NuclearStar, 768),
                get(OrePrefixes.wrapCircuit, Materials.UXV, 12),
                get(OrePrefixes.wrapCircuit, Materials.UHV, 48),
                get(OrePrefixes.nanite, Materials.Gold, 24))
            .circuit(SENSOR_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(75 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.SpaceTime, Materials2FluidShapes.fluidMolten, 36 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.MagnetohydrodynamicallyConstrainedStarMatter, Materials2FluidShapes.fluidMolten, 21 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Eternity, Materials2FluidShapes.fluidMolten, 18 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Universium, Materials2FluidShapes.fluidMolten, 15 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Magmatter, Materials2FluidShapes.fluidMolten, 12 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UMV)
            .metadata(COAL_CASING_TIER, COAL_UXV)
            .addTo(componentAssemblyLineRecipes);

        // Emitter
        GTValues.RA.stdBuilder()
            .itemOutputs(Emitter_UXV.get(64))
            .itemInputsUnsafe(
                get(OrePrefixes.frameGt, Materials.MHDCSM, 48),
                get(Electric_Motor_UXV, 48),
                get(NuclearStar, 768),
                get(OrePrefixes.wrapCircuit, Materials.UXV, 12),
                get(OrePrefixes.wrapCircuit, Materials.UHV, 48),
                get(OrePrefixes.nanite, Materials.Gold, 24))
            .circuit(EMITTER_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(75 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.SpaceTime, Materials2FluidShapes.fluidMolten, 36 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.MagnetohydrodynamicallyConstrainedStarMatter, Materials2FluidShapes.fluidMolten, 21 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Eternity, Materials2FluidShapes.fluidMolten, 18 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Universium, Materials2FluidShapes.fluidMolten, 15 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Magmatter, Materials2FluidShapes.fluidMolten, 12 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UMV)
            .metadata(COAL_CASING_TIER, COAL_UXV)
            .addTo(componentAssemblyLineRecipes);

        // Field Generator
        GTValues.RA.stdBuilder()
            .itemOutputs(Field_Generator_UXV.get(64))
            .itemInputsUnsafe(
                get(OrePrefixes.frameGt, Materials.MHDCSM, 48),
                get(NuclearStar, 3072),
                get(Emitter_UXV, 192),
                get(OrePrefixes.wrapCircuit, Materials.MAX, 12),
                get(OrePrefixes.wrapCircuit, Materials.UHV, 66),
                get(OrePrefixes.nanite, Materials.Gold, 36))
            .circuit(FIELD_GENERATOR_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(75 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.SpaceTime, Materials2FluidShapes.fluidMolten, 27 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.MagnetohydrodynamicallyConstrainedStarMatter, Materials2FluidShapes.fluidMolten, 19 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Eternity, Materials2FluidShapes.fluidMolten, 16 * STACKS + 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Universium, Materials2FluidShapes.fluidMolten, 15 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.Magmatter, Materials2FluidShapes.fluidMolten, 12 * STACKS),
                MaterialLibAPI.getFluidStack(Materials2Materials.SuperconductorUMVBase, Materials2FluidShapes.fluidMolten, 12 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UMV)
            .metadata(COAL_CASING_TIER, COAL_UXV)
            .addTo(componentAssemblyLineRecipes);
    }
    // spotless:on

    // CoAL recipes regularly require overly large ItemStacks, this lets use cleanly and
    // conveniently get over-sized stacks of many different types of items with different storages.
    private static ItemStack get(OrePrefixes prefix, Object material, int amount) {
        if (material instanceof Werkstoff w) {
            return GTUtility.copyAmountUnsafe(amount, w.get(prefix, 1));
        }
        return GTUtility.copyAmountUnsafe(amount, GTOreDictUnificator.get(prefix, material, 1));
    }

    private static ItemStack get(ItemList item, int amount) {
        return GTUtility.copyAmountUnsafe(amount, item.get(1));
    }
}
