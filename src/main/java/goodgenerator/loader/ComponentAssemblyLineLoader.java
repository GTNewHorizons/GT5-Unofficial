package goodgenerator.loader;

import static bartworks.system.material.WerkstoffLoader.Ruridit;
import static goodgenerator.api.recipe.GoodGeneratorRecipeMaps.componentAssemblyLineRecipes;
import static gregtech.api.enums.ItemList.*;
import static gregtech.api.enums.Materials.*;
import static gregtech.api.enums.OrePrefixes.*;
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

import bartworks.system.material.Werkstoff;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
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
        for (var copper : new Materials[] { Copper, AnnealedCopper }) {
            GTValues.RA.stdBuilder()
                .itemOutputs(Electric_Motor_LV.get(64))
                .itemInputsUnsafe(
                    get(stickLong, IronMagnetic, 24),
                    get(stickLong, Iron, 48),
                    get(wireGt16, copper, 12),
                    get(cableGt16, Tin, 6))
                .duration(48 * SECONDS)
                .eut(RECIPE_ULV)
                .metadata(COAL_CASING_TIER, COAL_LV)
                .addTo(componentAssemblyLineRecipes);

            GTValues.RA.stdBuilder()
                .itemOutputs(Electric_Motor_LV.get(64))
                .itemInputsUnsafe(
                    get(stickLong, SteelMagnetic, 24),
                    get(stickLong, Steel, 48),
                    get(wireGt16, copper, 12),
                    get(cableGt16, Tin, 6))
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
                get(plateDense, Steel, 16),
                get(stickLong, Steel, 48),
                get(cableGt16, Tin, 6),
                get(gearGt, Steel, 12))
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
                get(stickLong, Steel, 48),
                get(wrapCircuit, LV, 3),
                get(cableGt16, Tin, 9))
            .duration(48 * SECONDS)
            .eut(RECIPE_ULV)
            .metadata(COAL_CASING_TIER, COAL_LV)
            .addTo(componentAssemblyLineRecipes);

        for (var rubber : new Materials[] { Rubber, RubberSilicone, StyreneButadieneRubber }) {
            // Pump
            GTValues.RA.stdBuilder()
                .itemOutputs(Electric_Pump_LV.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_LV, 48),
                    get(rotor, Tin, 48),
                    get(screw, Tin, 48),
                    get(cableGt16, Tin, 3),
                    get(pipeMedium, Bronze, 48))
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
                    get(plateDense, rubber, 32),
                    get(cableGt16, Tin, 3))
                .duration(48 * SECONDS)
                .eut(RECIPE_ULV)
                .metadata(COAL_CASING_TIER, COAL_LV)
                .addTo(componentAssemblyLineRecipes);
        }

        // Sensor
        GTValues.RA.stdBuilder()
            .itemOutputs(Sensor_LV.get(64))
            .itemInputsUnsafe(
                get(gem, CertusQuartz, 48),
                get(plateDense, Steel, 21),
                get(stickLong, Brass, 24),
                get(wrapCircuit, LV, 3))
            .duration(48 * SECONDS)
            .eut(RECIPE_ULV)
            .metadata(COAL_CASING_TIER, COAL_LV)
            .addTo(componentAssemblyLineRecipes);

        // Emitter
        GTValues.RA.stdBuilder()
            .itemOutputs(Emitter_LV.get(64))
            .itemInputsUnsafe(
                get(gem, CertusQuartz, 48),
                get(wrapCircuit, LV, 6),
                get(cableGt16, Tin, 6))
            .fluidInputs(
                Brass.getMolten(1 * STACKS + 32 * INGOTS))
            .duration(48 * SECONDS)
            .eut(RECIPE_ULV)
            .metadata(COAL_CASING_TIER, COAL_LV)
            .addTo(componentAssemblyLineRecipes);

        // Field Generator
        GTValues.RA.stdBuilder()
            .itemOutputs(Field_Generator_LV.get(64))
            .itemInputsUnsafe(
                get(plate, EnderPearl, 48),
                get(wrapCircuit, HV, 12))
            .fluidInputs(
                RedSteel.getMolten(1 * STACKS + 32 * INGOTS))
            .duration(24 * MINUTES)
            .eut(RECIPE_ULV)
            .metadata(COAL_CASING_TIER, COAL_LV)
            .addTo(componentAssemblyLineRecipes);
    }

    private static void mvRecipes() {
        // Motor
        for (var copper : new Materials[] { Copper, AnnealedCopper }) {
            GTValues.RA.stdBuilder()
                .itemOutputs(Electric_Motor_MV.get(64))
                .itemInputsUnsafe(
                    get(stickLong, SteelMagnetic, 24),
                    get(stickLong, Aluminium, 48),
                    get(wireGt16, Cupronickel, 24),
                    get(cableGt16, copper, 6))
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
                get(plateDense, Aluminium, 16),
                get(stickLong, Aluminium, 48),
                get(cableGt16, Copper, 6),
                get(gearGt, Aluminium, 12))
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
                get(stickLong, Aluminium, 48),
                get(wrapCircuit, MV, 3),
                get(cableGt16, Copper, 9))
            .duration(48 * SECONDS)
            .eut(RECIPE_LV)
            .metadata(COAL_CASING_TIER, COAL_MV)
            .addTo(componentAssemblyLineRecipes);

        for (var rubber : new Materials[] { Rubber, RubberSilicone, StyreneButadieneRubber }) {
            // Pump
            GTValues.RA.stdBuilder()
                .itemOutputs(Electric_Pump_MV.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_MV, 48),
                    get(rotor, Bronze, 48),
                    get(screw, Bronze, 48),
                    get(cableGt16, Copper, 3),
                    get(pipeMedium, Steel, 48))
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
                    get(plateDense, rubber, 32),
                    get(cableGt16, Copper, 3))
                .duration(48 * SECONDS)
                .eut(RECIPE_LV)
                .metadata(COAL_CASING_TIER, COAL_MV)
                .addTo(componentAssemblyLineRecipes);
        }

        // Sensor
        GTValues.RA.stdBuilder()
            .itemOutputs(Sensor_MV.get(64))
            .itemInputsUnsafe(
                get(gemFlawless, Emerald, 48),
                get(plateDense, Aluminium, 21),
                get(stickLong, Electrum, 24),
                get(wrapCircuit, MV, 3))
            .duration(48 * SECONDS)
            .eut(RECIPE_LV)
            .metadata(COAL_CASING_TIER, COAL_MV)
            .addTo(componentAssemblyLineRecipes);

        // Emitter
        GTValues.RA.stdBuilder()
            .itemOutputs(Emitter_MV.get(64))
            .itemInputsUnsafe(
                get(gem, EnderPearl, 48),
                get(wrapCircuit, MV, 6),
                get(cableGt16, Copper, 6))
            .fluidInputs(
                Electrum.getMolten(1 * STACKS + 32 * INGOTS))
            .duration(48 * SECONDS)
            .eut(RECIPE_LV)
            .metadata(COAL_CASING_TIER, COAL_MV)
            .addTo(componentAssemblyLineRecipes);

        // Field Generator
        GTValues.RA.stdBuilder()
            .itemOutputs(Field_Generator_MV.get(64))
            .itemInputsUnsafe(
                get(plate, EnderEye, 48),
                get(wrapCircuit, EV, 12))
            .fluidInputs(
                VibrantAlloy.getMolten(1 * STACKS + 32 * INGOTS))
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
                get(stickLong, SteelMagnetic, 24),
                get(stickLong, StainlessSteel, 48),
                get(wireGt16, Electrum, 48),
                get(cableGt16, Silver, 12))
            .duration(48 * SECONDS)
            .eut(RECIPE_MV)
            .metadata(COAL_CASING_TIER, COAL_HV)
            .addTo(componentAssemblyLineRecipes);

        // Piston
        GTValues.RA.stdBuilder()
            .itemOutputs(Electric_Piston_HV.get(64))
            .itemInputsUnsafe(
                get(Electric_Motor_HV, 48),
                get(plateDense, StainlessSteel, 16),
                get(stickLong, StainlessSteel, 48),
                get(cableGt16, Gold, 6),
                get(gearGt, StainlessSteel, 12))
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
                get(stickLong, StainlessSteel, 48),
                get(wrapCircuit, HV, 3),
                get(cableGt16, Gold, 9))
            .duration(48 * SECONDS)
            .eut(RECIPE_MV)
            .metadata(COAL_CASING_TIER, COAL_HV)
            .addTo(componentAssemblyLineRecipes);

        for (var rubber : new Materials[] { Rubber, RubberSilicone, StyreneButadieneRubber }) {
            // Pump
            GTValues.RA.stdBuilder()
                .itemOutputs(Electric_Pump_HV.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_HV, 48),
                    get(rotor, Steel, 48),
                    get(screw, Steel, 48),
                    get(cableGt16, Gold, 3),
                    get(pipeMedium, StainlessSteel, 48))
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
                    get(plateDense, rubber, 32),
                    get(cableGt16, Gold, 3))
                .duration(48 * SECONDS)
                .eut(RECIPE_MV)
                .metadata(COAL_CASING_TIER, COAL_HV)
                .addTo(componentAssemblyLineRecipes);
        }

        // Sensor
        GTValues.RA.stdBuilder()
            .itemOutputs(Sensor_HV.get(64))
            .itemInputsUnsafe(
                get(gem, EnderEye, 48),
                get(plateDense, StainlessSteel, 21),
                get(stickLong, Chrome, 24),
                get(wrapCircuit, HV, 3))
            .duration(48 * SECONDS)
            .eut(RECIPE_MV)
            .metadata(COAL_CASING_TIER, COAL_HV)
            .addTo(componentAssemblyLineRecipes);

        // Emitter
        GTValues.RA.stdBuilder()
            .itemOutputs(Emitter_HV.get(64))
            .itemInputsUnsafe(
                get(gem, EnderEye, 48),
                get(wrapCircuit, HV, 6),
                get(cableGt16, Gold, 6))
            .fluidInputs(
                Chrome.getMolten(1 * STACKS + 32 * INGOTS))
            .duration(48 * SECONDS)
            .eut(RECIPE_MV)
            .metadata(COAL_CASING_TIER, COAL_HV)
            .addTo(componentAssemblyLineRecipes);

        // Field Generator
        GTValues.RA.stdBuilder()
            .itemOutputs(Field_Generator_HV.get(64))
            .itemInputsUnsafe(
                get(QuantumEye, 48),
                get(wrapCircuit, IV, 12))
            .fluidInputs(
                NiobiumTitanium.getMolten(3 * STACKS))
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
                get(stickLong, NeodymiumMagnetic, 24),
                get(stickLong, Titanium, 48),
                get(wireGt16, BlackSteel, 48),
                get(cableGt16, Aluminium, 12))
            .duration(48 * SECONDS)
            .eut(RECIPE_HV)
            .metadata(COAL_CASING_TIER, COAL_EV)
            .addTo(componentAssemblyLineRecipes);

        // Piston
        GTValues.RA.stdBuilder()
            .itemOutputs(Electric_Piston_EV.get(64))
            .itemInputsUnsafe(
                get(Electric_Motor_EV, 48),
                get(plateDense, Titanium, 16),
                get(stickLong, Titanium, 48),
                get(cableGt16, Aluminium, 6),
                get(gearGt, Titanium, 12))
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
                get(stickLong, Titanium, 48),
                get(wrapCircuit, EV, 3),
                get(cableGt16, Aluminium, 9))
            .duration(48 * SECONDS)
            .eut(RECIPE_HV)
            .metadata(COAL_CASING_TIER, COAL_EV)
            .addTo(componentAssemblyLineRecipes);

        for (var rubber : new Materials[] { Rubber, RubberSilicone, StyreneButadieneRubber }) {
            // Pump
            GTValues.RA.stdBuilder()
                .itemOutputs(Electric_Pump_EV.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_EV, 48),
                    get(rotor, StainlessSteel, 48),
                    get(screw, StainlessSteel, 48),
                    get(cableGt16, Aluminium, 3),
                    get(pipeMedium, Titanium, 48))
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
                    get(plateDense, rubber, 32),
                    get(cableGt16, Aluminium, 3))
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
                get(plateDense, Titanium, 21),
                get(stickLong, Platinum, 24),
                get(wrapCircuit, EV, 3))
            .duration(48 * SECONDS)
            .eut(RECIPE_HV)
            .metadata(COAL_CASING_TIER, COAL_EV)
            .addTo(componentAssemblyLineRecipes);

        // Emitter
        GTValues.RA.stdBuilder()
            .itemOutputs(Emitter_EV.get(64))
            .itemInputsUnsafe(
                get(QuantumEye, 48),
                get(wrapCircuit, EV, 6),
                get(cableGt16, Aluminium, 6))
            .fluidInputs(
                Platinum.getMolten(1 * STACKS + 32 * INGOTS))
            .duration(48 * SECONDS)
            .eut(RECIPE_HV)
            .metadata(COAL_CASING_TIER, COAL_EV)
            .addTo(componentAssemblyLineRecipes);

        // Field Generator
        GTValues.RA.stdBuilder()
            .itemOutputs(Field_Generator_EV.get(64))
            .itemInputsUnsafe(
                get(gem, NetherStar, 48),
                get(wrapCircuit, LuV, 12))
            .fluidInputs(
                HSSG.getMolten(3 * STACKS))
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
                get(stickLong, NeodymiumMagnetic, 24),
                get(stickLong, TungstenSteel, 48),
                get(wireGt16, Graphene, 48),
                get(cableGt16, Tungsten, 12))
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
                get(plateDense, TungstenSteel, 16),
                get(stickLong, TungstenSteel, 48),
                get(cableGt16, Tungsten, 6),
                get(gearGt, TungstenSteel, 12))
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
                get(stickLong, TungstenSteel, 48),
                get(wrapCircuit, IV, 3),
                get(cableGt16, Tungsten, 9))
            .circuit(ROBOT_ARM_CIRCUIT)
            .duration(48 * SECONDS)
            .eut(RECIPE_EV)
            .metadata(COAL_CASING_TIER, COAL_IV)
            .addTo(componentAssemblyLineRecipes);

        for (var rubber : new Materials[] {RubberSilicone, StyreneButadieneRubber }) {
            // Pump
            GTValues.RA.stdBuilder()
                .itemOutputs(Electric_Pump_IV.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_IV, 48),
                    get(rotor, TungstenSteel, 48),
                    get(screw, TungstenSteel, 48),
                    get(cableGt16, Tungsten, 3),
                    get(pipeMedium, TungstenSteel, 48))
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
                    get(plateDense, rubber, 32),
                    get(cableGt16, Tungsten, 3))
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
                get(plateDense, TungstenSteel, 21),
                get(stickLong, Iridium, 24),
                get(wrapCircuit, IV, 3))
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
                get(wrapCircuit, IV, 6),
                get(cableGt16, Tungsten, 6))
            .circuit(EMITTER_CIRCUIT)
            .fluidInputs(
                Iridium.getMolten(1 * STACKS + 32 * INGOTS))
            .duration(48 * SECONDS)
            .eut(RECIPE_EV)
            .metadata(COAL_CASING_TIER, COAL_IV)
            .addTo(componentAssemblyLineRecipes);

        // Field Generator
        GTValues.RA.stdBuilder()
            .itemOutputs(Field_Generator_IV.get(64))
            .itemInputsUnsafe(
                get(QuantumStar, 48),
                get(wrapCircuit, Materials.ZPM, 12))
            .circuit(FIELD_GENERATOR_CIRCUIT)
            .fluidInputs(
                HSSS.getMolten(3 * STACKS))
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
                get(stickLong, SamariumMagnetic, 24),
                get(cableGt16, YttriumBariumCuprate, 6))
            .circuit(MOTOR_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(48 * INGOTS),
                Lubricant.getFluid(12_000),
                Ruridit.getMolten(12 * STACKS),
                HSSS.getMolten(1 * STACKS + 32 * INGOTS))
            .duration(24 * MINUTES)
            .eut(RECIPE_IV)
            .metadata(COAL_CASING_TIER, COAL_LuV)
            .addTo(componentAssemblyLineRecipes);

        // Piston
        GTValues.RA.stdBuilder()
            .itemOutputs(Electric_Piston_LuV.get(64))
            .itemInputsUnsafe(
                get(Electric_Motor_LuV, 48),
                get(plateDense, HSSS, 32),
                get(cableGt16, YttriumBariumCuprate, 12))
            .circuit(PISTON_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(48 * INGOTS),
                Lubricant.getFluid(12_000),
                HSSS.getMolten(9 * STACKS + 26 * INGOTS + 6 * NUGGETS))
            .duration(24 * MINUTES)
            .eut(RECIPE_IV)
            .metadata(COAL_CASING_TIER, COAL_LuV)
            .addTo(componentAssemblyLineRecipes);

        for (var rubber : new Materials[] {RubberSilicone, StyreneButadieneRubber }) {
            // Pump
            GTValues.RA.stdBuilder()
                .itemOutputs(Electric_Pump_LuV.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_LuV, 48),
                    get(plateDense, HSSS, 10),
                    get(cableGt16, YttriumBariumCuprate, 6))
                .circuit(PUMP_CIRCUIT)
                .fluidInputs(
                    INDALLOY_140.getFluidStack(48 * INGOTS),
                    Lubricant.getFluid(12_000),
                    HSSS.getMolten(7 * STACKS + 2 * INGOTS + 6 * NUGGETS),
                    NiobiumTitanium.getMolten(1 * STACKS + 32 * INGOTS),
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
                    get(plateDense, HSSS, 10),
                    get(cableGt16, YttriumBariumCuprate, 6),
                    get(plateDense, rubber, 53))
                .circuit(CONVEYOR_CIRCUIT)
                .fluidInputs(
                    INDALLOY_140.getFluidStack(48 * INGOTS),
                    Lubricant.getFluid(12_000),
                    HSSS.getMolten(3 * STACKS + 26 * INGOTS + 6 * NUGGETS))
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
                get(wrapCircuit, LuV, 6),
                get(wrapCircuit, IV, 12),
                get(wrapCircuit, EV, 24),
                get(cableGt16, YttriumBariumCuprate, 18))
            .circuit(ROBOT_ARM_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(3 * STACKS),
                Lubricant.getFluid(12_000),
                HSSS.getMolten(8 * STACKS + 16 * INGOTS))
            .duration(24 * MINUTES)
            .eut(RECIPE_IV)
            .metadata(COAL_CASING_TIER, COAL_LuV)
            .addTo(componentAssemblyLineRecipes);

        // Sensor
        GTValues.RA.stdBuilder()
            .itemOutputs(Sensor_LuV.get(64))
            .itemInputsUnsafe(
                get(frameGt, HSSS, 48),
                get(Electric_Motor_LuV, 48),
                get(plateDense, Ruridit, 42),
                get(QuantumStar, 48),
                get(wrapCircuit, LuV, 12),
                get(cableGt16, YttriumBariumCuprate, 21))
            .circuit(SENSOR_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(3 * STACKS),
                Gallium.getMolten(36 * STACKS))
            .duration(24 * MINUTES)
            .eut(RECIPE_IV)
            .metadata(COAL_CASING_TIER, COAL_LuV)
            .addTo(componentAssemblyLineRecipes);

        // Emitter
        GTValues.RA.stdBuilder()
            .itemOutputs(Emitter_LuV.get(64))
            .itemInputsUnsafe(
                get(frameGt, HSSS, 48),
                get(Electric_Motor_LuV, 48),
                get(QuantumStar, 48),
                get(wrapCircuit, LuV, 12),
                get(cableGt16, YttriumBariumCuprate, 21))
            .circuit(EMITTER_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(3 * STACKS),
                Gallium.getMolten(36 * STACKS),
                Ruridit.getMolten(3 * STACKS))
            .duration(24 * MINUTES)
            .eut(RECIPE_IV)
            .metadata(COAL_CASING_TIER, COAL_LuV)
            .addTo(componentAssemblyLineRecipes);

        // Field Generator
        GTValues.RA.stdBuilder()
            .itemOutputs(Field_Generator_LuV.get(64))
            .itemInputsUnsafe(
                get(frameGt, HSSS, 48),
                get(plateDense, HSSS, 32),
                get(QuantumStar, 96),
                get(Emitter_LuV, 192),
                get(wrapCircuit, Materials.ZPM, 12),
                get(cableGt16, YttriumBariumCuprate, 24))
            .circuit(FIELD_GENERATOR_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(3 * STACKS),
                Ruridit.getMolten(24 * STACKS))
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
                get(stickLong, SamariumMagnetic, 48),
                get(cableGt16, VanadiumGallium, 24))
            .circuit(MOTOR_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(1 * STACKS + 32 * INGOTS),
                Lubricant.getFluid(36_000),
                Europium.getMolten(18 * STACKS),
                NaquadahAlloy.getMolten(5 * STACKS + 5 * INGOTS + 3 * NUGGETS))
            .duration(24 * MINUTES)
            .eut(RECIPE_LuV)
            .metadata(COAL_CASING_TIER, COAL_ZPM)
            .addTo(componentAssemblyLineRecipes);

        // Piston
        GTValues.RA.stdBuilder()
            .itemOutputs(Electric_Piston_ZPM.get(64))
            .itemInputsUnsafe(
                get(Electric_Motor_ZPM, 48),
                get(plateDense, NaquadahAlloy, 32),
                get(cableGt16, VanadiumGallium, 48))
            .circuit(PISTON_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(1 * STACKS + 32 * INGOTS),
                Lubricant.getFluid(36_000),
                NaquadahAlloy.getMolten(9 * STACKS + 26 * INGOTS + 6 * NUGGETS))
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
                get(wrapCircuit, Materials.ZPM, 6),
                get(wrapCircuit, LuV, 12),
                get(wrapCircuit, IV, 24))
            .circuit(ROBOT_ARM_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(6 * STACKS),
                Lubricant.getFluid(36_000),
                VanadiumGallium.getMolten(9 * STACKS),
                NaquadahAlloy.getMolten(8 * STACKS + 16 * INGOTS))
            .duration(24 * MINUTES)
            .eut(RECIPE_LuV)
            .metadata(COAL_CASING_TIER, COAL_ZPM)
            .addTo(componentAssemblyLineRecipes);

        for (var rubber : new Materials[] {RubberSilicone, StyreneButadieneRubber }) {
            // Pump
            GTValues.RA.stdBuilder()
                .itemOutputs(Electric_Pump_ZPM.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_ZPM, 48),
                    get(plateDense, NaquadahAlloy, 10),
                    get(cableGt16, VanadiumGallium, 24))
                .circuit(PUMP_CIRCUIT)
                .fluidInputs(
                    INDALLOY_140.getFluidStack(1 * STACKS + 32 * INGOTS),
                    Lubricant.getFluid(36_000),
                    NaquadahAlloy.getMolten(7 * STACKS + 2 * INGOTS + 6 * NUGGETS),
                    Enderium.getMolten(4 * STACKS + 32 * INGOTS),
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
                    get(plateDense, NaquadahAlloy, 10),
                    get(cableGt16, VanadiumGallium, 24))
                .circuit(CONVEYOR_CIRCUIT)
                .fluidInputs(
                    INDALLOY_140.getFluidStack(1 * STACKS + 32 * INGOTS),
                    Lubricant.getFluid(36_000),
                    rubber.getMolten(14 * STACKS + 58 * INGOTS),
                    NaquadahAlloy.getMolten(3 * STACKS + 26 * INGOTS + 6 * NUGGETS))
                .duration(24 * MINUTES)
                .eut(RECIPE_LuV)
                .metadata(COAL_CASING_TIER, COAL_ZPM)
                .addTo(componentAssemblyLineRecipes);
        }

        // Sensor
        GTValues.RA.stdBuilder()
            .itemOutputs(Sensor_ZPM.get(64))
            .itemInputsUnsafe(
                get(frameGt, NaquadahAlloy, 48),
                get(Electric_Motor_ZPM, 48),
                get(plateDense, Osmiridium, 42),
                get(QuantumStar, 96),
                get(wrapCircuit, Materials.ZPM, 12))
            .circuit(SENSOR_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(6 * STACKS),
                Trinium.getMolten(36 * STACKS),
                VanadiumGallium.getMolten(10 * STACKS + 32 * INGOTS))
            .duration(24 * MINUTES)
            .eut(RECIPE_LuV)
            .metadata(COAL_CASING_TIER, COAL_ZPM)
            .addTo(componentAssemblyLineRecipes);

        // Emitter
        GTValues.RA.stdBuilder()
            .itemOutputs(Emitter_ZPM.get(64))
            .itemInputsUnsafe(
                get(frameGt, NaquadahAlloy, 48),
                get(Electric_Motor_ZPM, 48),
                get(QuantumStar, 96),
                get(wrapCircuit, Materials.ZPM, 12))
            .circuit(EMITTER_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(6 * STACKS),
                Trinium.getMolten(36 * STACKS),
                VanadiumGallium.getMolten(10 * STACKS + 32 * INGOTS),
                Osmiridium.getMolten(3 * STACKS))
            .duration(24 * MINUTES)
            .eut(RECIPE_LuV)
            .metadata(COAL_CASING_TIER, COAL_ZPM)
            .addTo(componentAssemblyLineRecipes);

        // Field Generator
        GTValues.RA.stdBuilder()
            .itemOutputs(Field_Generator_ZPM.get(64))
            .itemInputsUnsafe(
                get(frameGt, NaquadahAlloy, 48),
                get(plateDense, NaquadahAlloy, 32),
                get(QuantumStar, 96),
                get(Emitter_ZPM, 192),
                get(wrapCircuit, UV, 12))
            .circuit(FIELD_GENERATOR_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(6 * STACKS),
                Europium.getMolten(24 * STACKS),
                VanadiumGallium.getMolten(12 * STACKS))
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
                get(cableGt16, NaquadahAlloy, 24))
            .circuit(MOTOR_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(6 * STACKS + 48 * INGOTS),
                Lubricant.getFluid(96_000),
                Americium.getMolten(36 * STACKS),
                Naquadria.getMolten(6 * STACKS + 48 * INGOTS),
                Neutronium.getMolten(5 * STACKS + 5 * INGOTS + 3 * NUGGETS),
                Samarium.getMolten(1 * STACKS + 32 * INGOTS))
            .duration(24 * MINUTES)
            .eut(RECIPE_ZPM)
            .metadata(COAL_CASING_TIER, COAL_UV)
            .addTo(componentAssemblyLineRecipes);

        // Piston
        GTValues.RA.stdBuilder()
            .itemOutputs(Electric_Piston_UV.get(64))
            .itemInputsUnsafe(
                get(Electric_Motor_UV, 48),
                get(plateDense, Neutronium, 32),
                get(cableGt16, NaquadahAlloy, 48))
            .circuit(PISTON_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(6 * STACKS + 48 * INGOTS),
                Lubricant.getFluid(96_000),
                Neutronium.getMolten(9 * STACKS + 26 * INGOTS + 6 * NUGGETS),
                Naquadria.getMolten(6 * STACKS + 48 * INGOTS))
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
                get(wrapCircuit, UV, 6),
                get(wrapCircuit, Materials.ZPM, 12),
                get(wrapCircuit, LuV, 24))
            .circuit(ROBOT_ARM_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(12 * STACKS),
                Lubricant.getFluid(96_000),
                NaquadahAlloy.getMolten(9 * STACKS),
                Neutronium.getMolten(8 * STACKS + 16 * INGOTS),
                Naquadria.getMolten(6 * STACKS + 48 * INGOTS))
            .duration(24 * MINUTES)
            .eut(RECIPE_ZPM)
            .metadata(COAL_CASING_TIER, COAL_UV)
            .addTo(componentAssemblyLineRecipes);

        for (var rubber : new Materials[] {RubberSilicone, StyreneButadieneRubber }) {
            // Pump
            GTValues.RA.stdBuilder()
                .itemOutputs(Electric_Pump_UV.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_UV, 48),
                    get(plateDense, Neutronium, 10),
                    get(cableGt16, NaquadahAlloy, 24))
                .circuit(PUMP_CIRCUIT)
                .fluidInputs(
                    INDALLOY_140.getFluidStack(6 * STACKS + 48 * INGOTS),
                    Lubricant.getFluid(96_000),
                    Naquadah.getMolten(9 * STACKS),
                    Neutronium.getMolten(7 * STACKS + 2 * INGOTS + 6 * NUGGETS),
                    Naquadria.getMolten(6 * STACKS + 48 * INGOTS),
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
                    get(plateDense, Neutronium, 10),
                    get(cableGt16, NaquadahAlloy, 24))
                .circuit(CONVEYOR_CIRCUIT)
                .fluidInputs(
                    INDALLOY_140.getFluidStack(6 * STACKS + 48 * INGOTS),
                    Lubricant.getFluid(96_000),
                    rubber.getMolten(29 * STACKS + 61 * INGOTS),
                    Naquadria.getMolten(6 * STACKS + 48 * INGOTS),
                    Neutronium.getMolten(3 * STACKS + 26 * INGOTS + 6 * NUGGETS))
                .duration(24 * MINUTES)
                .eut(RECIPE_ZPM)
                .metadata(COAL_CASING_TIER, COAL_UV)
                .addTo(componentAssemblyLineRecipes);
        }

        // Sensor
        GTValues.RA.stdBuilder()
            .itemOutputs(Sensor_UV.get(64))
            .itemInputsUnsafe(
                get(frameGt, Neutronium, 48),
                get(Electric_Motor_UV, 48),
                get(plateDense, Neutronium, 42),
                get(Gravistar, 192),
                get(wrapCircuit, UV, 12))
            .circuit(SENSOR_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(12 * STACKS),
                Naquadria.getMolten(42 * STACKS + 48 * INGOTS),
                NaquadahAlloy.getMolten(10 * STACKS + 32 * INGOTS))
            .duration(24 * MINUTES)
            .eut(RECIPE_ZPM)
            .metadata(COAL_CASING_TIER, COAL_UV)
            .addTo(componentAssemblyLineRecipes);

        // Emitter
        GTValues.RA.stdBuilder()
            .itemOutputs(Emitter_UV.get(64))
            .itemInputsUnsafe(
                get(frameGt, Neutronium, 48),
                get(Electric_Motor_UV, 48),
                get(Gravistar, 192),
                get(wrapCircuit, UV, 12))
            .circuit(EMITTER_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(12 * STACKS),
                Naquadria.getMolten(42 * STACKS + 48 * INGOTS),
                NaquadahAlloy.getMolten(10 * STACKS + 32 * INGOTS),
                Neutronium.getMolten(3 * STACKS))
            .duration(24 * MINUTES)
            .eut(RECIPE_ZPM)
            .metadata(COAL_CASING_TIER, COAL_UV)
            .addTo(componentAssemblyLineRecipes);

        // Field Generator
        GTValues.RA.stdBuilder()
            .itemOutputs(Field_Generator_UV.get(64))
            .itemInputsUnsafe(
                get(frameGt, Neutronium, 48),
                get(plateDense, Neutronium, 32),
                get(Gravistar, 96),
                get(Emitter_UV, 192),
                get(wrapCircuit, UHV, 12))
            .circuit(FIELD_GENERATOR_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(12 * STACKS),
                Americium.getMolten(36 * STACKS),
                NaquadahAlloy.getMolten(12 * STACKS),
                Naquadria.getMolten(6 * STACKS + 48 * INGOTS))
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
                get(cableGt16, Bedrockium, 24))
            .circuit(MOTOR_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(13 * STACKS + 32 * INGOTS),
                Lubricant.getFluid(192_000),
                Neutronium.getMolten(48 * STACKS),
                Naquadria.getMolten(13 * STACKS + 32 * INGOTS),
                CosmicNeutronium.getMolten(10 * STACKS + 10 * INGOTS + 6 * NUGGETS),
                Samarium.getMolten(3 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UV)
            .metadata(COAL_CASING_TIER, COAL_UHV)
            .addTo(componentAssemblyLineRecipes);

        // Piston
        GTValues.RA.stdBuilder()
            .itemOutputs(Electric_Piston_UHV.get(64))
            .itemInputsUnsafe(
                get(Electric_Motor_UHV, 48),
                get(plateDense, CosmicNeutronium, 32),
                get(cableGt16, Bedrockium, 48))
            .circuit(PISTON_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(13 * STACKS + 32 * INGOTS),
                Lubricant.getFluid(192_000),
                CosmicNeutronium.getMolten(18 * STACKS + 53 * INGOTS + 3 * NUGGETS),
                Naquadria.getMolten(13 * STACKS + 32 * INGOTS))
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
                get(wrapCircuit, UHV, 6),
                get(wrapCircuit, UV, 12),
                get(wrapCircuit, Materials.ZPM, 24))
            .circuit(ROBOT_ARM_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(13 * STACKS + 32 * INGOTS),
                Lubricant.getFluid(192_000),
                CosmicNeutronium.getMolten(16 * STACKS + 32 * INGOTS),
                Naquadria.getMolten(13 * STACKS + 32 * INGOTS),
                Bedrockium.getMolten(9 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UV)
            .metadata(COAL_CASING_TIER, COAL_UHV)
            .addTo(componentAssemblyLineRecipes);

        for (var rubber : new Materials[] {RubberSilicone, StyreneButadieneRubber }) {
            // Pump
            GTValues.RA.stdBuilder()
                .itemOutputs(Electric_Pump_UHV.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_UHV, 48),
                    get(plateDense, CosmicNeutronium, 21),
                    get(cableGt16, Bedrockium, 24))
                .circuit(PUMP_CIRCUIT)
                .fluidInputs(
                    INDALLOY_140.getFluidStack(13 * STACKS + 32 * INGOTS),
                    Lubricant.getFluid(192_000),
                    CosmicNeutronium.getMolten(14 * STACKS + 5 * INGOTS + 3 * NUGGETS),
                    Naquadria.getMolten(13 * STACKS + 32 * INGOTS),
                    Neutronium.getMolten(9 * STACKS),
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
                    get(plateDense, CosmicNeutronium, 10),
                    get(cableGt16, Bedrockium, 24))
                .circuit(CONVEYOR_CIRCUIT)
                .fluidInputs(
                    INDALLOY_140.getFluidStack(13 * STACKS + 32 * INGOTS),
                    Lubricant.getFluid(192_000),
                    rubber.getMolten(29 * STACKS + 61 * INGOTS),
                    Naquadria.getMolten(13 * STACKS + 32 * INGOTS),
                    CosmicNeutronium.getMolten(6 * STACKS + 53 * INGOTS + 3 * NUGGETS))
                .duration(40 * MINUTES)
                .eut(RECIPE_UV)
                .metadata(COAL_CASING_TIER, COAL_UHV)
                .addTo(componentAssemblyLineRecipes);
        }

        // Sensor
        GTValues.RA.stdBuilder()
            .itemOutputs(Sensor_UHV.get(64))
            .itemInputsUnsafe(
                get(frameGt, CosmicNeutronium, 48),
                get(Electric_Motor_UHV, 48),
                get(plateDense, CosmicNeutronium, 42),
                get(Gravistar, 384),
                get(wrapCircuit, UHV, 12))
            .circuit(SENSOR_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(13 * STACKS + 32 * INGOTS),
                ElectrumFlux.getMolten(48 * STACKS),
                Naquadria.getMolten(13 * STACKS + 32 * INGOTS),
                Bedrockium.getMolten(10 * STACKS + 32 * INGOTS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UV)
            .metadata(COAL_CASING_TIER, COAL_UHV)
            .addTo(componentAssemblyLineRecipes);

        // Emitter
        GTValues.RA.stdBuilder()
            .itemOutputs(Emitter_UHV.get(64))
            .itemInputsUnsafe(
                get(frameGt, CosmicNeutronium, 48),
                get(Electric_Motor_UHV, 48),
                get(Gravistar, 384),
                get(wrapCircuit, UHV, 12))
            .circuit(EMITTER_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(13 * STACKS + 32 * INGOTS),
                ElectrumFlux.getMolten(48 * STACKS),
                Naquadria.getMolten(13 * STACKS + 32 * INGOTS),
                Bedrockium.getMolten(10 * STACKS + 32 * INGOTS),
                CosmicNeutronium.getMolten(3 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UV)
            .metadata(COAL_CASING_TIER, COAL_UHV)
            .addTo(componentAssemblyLineRecipes);

        // Field Generator
        GTValues.RA.stdBuilder()
            .itemOutputs(Field_Generator_UHV.get(64))
            .itemInputsUnsafe(
                get(frameGt, CosmicNeutronium, 48),
                get(plateDense, CosmicNeutronium, 32),
                get(Gravistar, 192),
                get(Emitter_UHV, 192),
                get(wrapCircuit, UEV, 12))
            .circuit(FIELD_GENERATOR_CIRCUIT)
            .fluidInputs(
                INDALLOY_140.getFluidStack(13 * STACKS + 32 * INGOTS),
                Neutronium.getMolten(48 * STACKS),
                Naquadria.getMolten(13 * STACKS + 32 * INGOTS),
                Bedrockium.getMolten(12 * STACKS))
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
                get(cableGt16, Draconium, 24))
            .circuit(MOTOR_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                Lubricant.getFluid(192_000),
                CosmicNeutronium.getMolten(48 * STACKS),
                Infinity.getMolten(16 * STACKS + 10 * INGOTS + 6 * NUGGETS),
                Quantium.getMolten(13 * STACKS + 32 * INGOTS),
                TengamPurified.getMolten(6 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UHV)
            .metadata(COAL_CASING_TIER, COAL_UEV)
            .addTo(componentAssemblyLineRecipes);

        // Piston
        GTValues.RA.stdBuilder()
            .itemOutputs(Electric_Piston_UEV.get(64))
            .itemInputsUnsafe(
                get(Electric_Motor_UEV, 48),
                get(plateDense, Infinity, 32),
                get(cableGt16, Draconium, 48))
            .circuit(PISTON_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                Lubricant.getFluid(192_000),
                Infinity.getMolten(18 * STACKS + 53 * INGOTS + 3 * NUGGETS),
                Quantium.getMolten(13 * STACKS + 32 * INGOTS))
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
                get(wrapCircuit, UEV, 6),
                get(wrapCircuit, UHV, 12),
                get(wrapCircuit, UV, 24))
            .circuit(ROBOT_ARM_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                Lubricant.getFluid(192_000),
                Infinity.getMolten(16 * STACKS + 32 * INGOTS),
                Quantium.getMolten(13 * STACKS + 32 * INGOTS),
                Draconium.getMolten(9 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UHV)
            .metadata(COAL_CASING_TIER, COAL_UEV)
            .addTo(componentAssemblyLineRecipes);

        for (var rubber : new Materials[] {RubberSilicone, StyreneButadieneRubber }) {
            // Pump
            GTValues.RA.stdBuilder()
                .itemOutputs(Electric_Pump_UEV.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_UEV, 48),
                    get(pipeLarge, NetherStar, 96),
                    get(plateDense, Infinity, 21),
                    get(cableGt16, Draconium, 24))
                .circuit(PUMP_CIRCUIT)
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                    Lubricant.getFluid(192_000),
                    Infinity.getMolten(14 * STACKS + 5 * INGOTS + 3 * NUGGETS),
                    Quantium.getMolten(13 * STACKS + 32 * INGOTS),
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
                    get(plateDense, Infinity, 10),
                    get(cableGt16, Draconium, 24))
                .circuit(CONVEYOR_CIRCUIT)
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                    Lubricant.getFluid(192_000),
                    rubber.getMolten(59 * STACKS + 58 * INGOTS),
                    Quantium.getMolten(13 * STACKS + 32 * INGOTS),
                    Infinity.getMolten(6 * STACKS + 53 * INGOTS + 3 * NUGGETS))
                .duration(40 * MINUTES)
                .eut(RECIPE_UHV)
                .metadata(COAL_CASING_TIER, COAL_UEV)
                .addTo(componentAssemblyLineRecipes);
        }

        // Sensor
        GTValues.RA.stdBuilder()
            .itemOutputs(Sensor_UEV.get(64))
            .itemInputsUnsafe(
                get(frameGt, Infinity, 48),
                get(Electric_Motor_UEV, 48),
                get(plateDense, Infinity, 42),
                get(Gravistar, 768),
                get(wrapCircuit, UEV, 12))
            .circuit(SENSOR_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                InfinityCatalyst.getMolten(48 * STACKS),
                Quantium.getMolten(13 * STACKS + 32 * INGOTS),
                Draconium.getMolten(10 * STACKS + 32 * INGOTS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UHV)
            .metadata(COAL_CASING_TIER, COAL_UEV)
            .addTo(componentAssemblyLineRecipes);

        // Emitter
        GTValues.RA.stdBuilder()
            .itemOutputs(Emitter_UEV.get(64))
            .itemInputsUnsafe(
                get(frameGt, Infinity, 48),
                get(Electric_Motor_UEV, 48),
                get(Gravistar, 768),
                get(wrapCircuit, UEV, 12))
            .circuit(EMITTER_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                InfinityCatalyst.getMolten(48 * STACKS),
                Quantium.getMolten(13 * STACKS + 32 * INGOTS),
                Draconium.getMolten(10 * STACKS + 32 * INGOTS),
                Infinity.getMolten(6 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UHV)
            .metadata(COAL_CASING_TIER, COAL_UEV)
            .addTo(componentAssemblyLineRecipes);

        // Field Generator
        GTValues.RA.stdBuilder()
            .itemOutputs(Field_Generator_UEV.get(64))
            .itemInputsUnsafe(
                get(frameGt, Infinity, 48),
                get(plateDense, Infinity, 32),
                get(Gravistar, 384),
                get(Emitter_UEV, 192),
                get(wrapCircuit, UIV, 12))
            .circuit(FIELD_GENERATOR_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                Tritanium.getMolten(48 * STACKS),
                Quantium.getMolten(13 * STACKS + 32 * INGOTS),
                Draconium.getMolten(12 * STACKS))
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
                get(cableGt16, NetherStar, 24))
            .circuit(MOTOR_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                DimensionallyShiftedSuperfluid.getFluid(32 * STACKS + 53 * INGOTS + 3 * NUGGETS),
                MoltenProtoHalkoniteBase.getFluid(48 * STACKS),
                Infinity.getMolten(48 * STACKS),
                TranscendentMetal.getMolten(16 * STACKS + 10 * INGOTS + 6 * NUGGETS),
                TengamPurified.getMolten(12 * STACKS),
                CELESTIAL_TUNGSTEN.getFluidStack(3 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UEV)
            .metadata(COAL_CASING_TIER, COAL_UIV)
            .addTo(componentAssemblyLineRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(Electric_Motor_UIV.get(64))
            .itemInputsUnsafe(
                get(cableGt16, NetherStar, 24))
            .circuit(MOTOR_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                DimensionallyShiftedSuperfluid.getFluid(32 * STACKS + 53 * INGOTS + 3 * NUGGETS),
                MoltenProtoHalkoniteBase.getFluid(24 * STACKS),
                Creon.getMolten(24 * STACKS),
                Mellion.getMolten(24 * STACKS),
                TranscendentMetal.getMolten(16 * STACKS + 10 * INGOTS + 6 * NUGGETS),
                TengamPurified.getMolten(12 * STACKS),
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
                get(plateDense, TranscendentMetal, 32),
                get(cableGt16, NetherStar, 48))
            .circuit(PISTON_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                DimensionallyShiftedSuperfluid.getFluid(192_000),
                TranscendentMetal.getMolten(18 * STACKS + 53 * INGOTS + 3 * NUGGETS),
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
                get(wrapCircuit, UIV, 6),
                get(wrapCircuit, UEV, 12),
                get(wrapCircuit, UHV, 24),
                get(cableGt16, NetherStar, 72))
            .circuit(ROBOT_ARM_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                DimensionallyShiftedSuperfluid.getFluid(192_000),
                TranscendentMetal.getMolten(16 * STACKS + 32 * INGOTS),
                CELESTIAL_TUNGSTEN.getFluidStack(3 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UEV)
            .metadata(COAL_CASING_TIER, COAL_UIV)
            .addTo(componentAssemblyLineRecipes);

        for (var rubber : new Materials[] {RubberSilicone, StyreneButadieneRubber }) {
            // Pump
            GTValues.RA.stdBuilder()
                .itemOutputs(Electric_Pump_UIV.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_UIV, 48),
                    get(plateDense, TranscendentMetal, 21),
                    get(cableGt16, NetherStar, 24))
                .circuit(PUMP_CIRCUIT)
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                    DimensionallyShiftedSuperfluid.getFluid(192_000),
                    TranscendentMetal.getMolten(14 * STACKS + 5 * INGOTS + 3 * NUGGETS),
                    rubber.getMolten(12 * STACKS),
                    DraconiumAwakened.getMolten(9 * STACKS),
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
                    get(plateDense, TranscendentMetal, 10),
                    get(cableGt16, NetherStar, 24))
                .circuit(CONVEYOR_CIRCUIT)
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                    DimensionallyShiftedSuperfluid.getFluid(192_000),
                    rubber.getMolten(59 * STACKS + 58 * INGOTS),
                    TranscendentMetal.getMolten(6 * STACKS + 53 * INGOTS + 3 * NUGGETS),
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
                get(frameGt, TranscendentMetal, 48),
                get(Electric_Motor_UIV, 48),
                get(plateDense, TranscendentMetal, 42),
                get(NuclearStar, 96),
                get(wrapCircuit, UIV, 12),
                get(cableGt16, NetherStar, 84))
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
                get(frameGt, TranscendentMetal, 48),
                get(Electric_Motor_UIV, 48),
                get(NuclearStar, 96),
                get(wrapCircuit, UIV, 12),
                get(cableGt16, NetherStar, 84))
            .circuit(EMITTER_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                TRINIUM_REINFORCED_STEEL.getFluidStack(12 * STACKS),
                LAFIUM.getFluidStack(12 * STACKS),
                CINOBITE.getFluidStack(12 * STACKS),
                PIKYONIUM.getFluidStack(12 * STACKS),
                TranscendentMetal.getMolten(6 * STACKS),
                CELESTIAL_TUNGSTEN.getFluidStack(3 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UEV)
            .metadata(COAL_CASING_TIER, COAL_UIV)
            .addTo(componentAssemblyLineRecipes);

        // Field Generator
        GTValues.RA.stdBuilder()
            .itemOutputs(Field_Generator_UIV.get(64))
            .itemInputsUnsafe(
                get(frameGt, TranscendentMetal, 48),
                get(plateDense, TranscendentMetal, 32),
                get(NuclearStar, 48),
                get(Emitter_UIV, 192),
                get(wrapCircuit, UMV, 12),
                get(cableGt16, NetherStar, 96))
            .circuit(FIELD_GENERATOR_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                DimensionallyShiftedSuperfluid.getFluid(12 * STACKS),
                MoltenProtoHalkoniteBase.getFluid(48 * STACKS),
                Infinity.getMolten(48 * STACKS),
                CELESTIAL_TUNGSTEN.getFluidStack(3 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UEV)
            .metadata(COAL_CASING_TIER, COAL_UIV)
            .addTo(componentAssemblyLineRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(Field_Generator_UIV.get(64))
            .itemInputsUnsafe(
                get(frameGt, TranscendentMetal, 48),
                get(plateDense, TranscendentMetal, 32),
                get(NuclearStar, 48),
                get(Emitter_UIV, 192),
                get(wrapCircuit, UMV, 12),
                get(cableGt16, NetherStar, 96))
            .circuit(FIELD_GENERATOR_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                DimensionallyShiftedSuperfluid.getFluid(12 * STACKS),
                MoltenProtoHalkoniteBase.getFluid(24 * STACKS),
                Creon.getMolten(24 * STACKS),
                Mellion.getMolten(24 * STACKS),
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
                get(cableGt16, Quantium, 24))
            .circuit(MOTOR_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                DimensionallyShiftedSuperfluid.getFluid(192_000),
                HYPOGEN.getFluidStack(51 * STACKS),
                TengamPurified.getMolten(24 * STACKS),
                SpaceTime.getMolten(16 * STACKS + 10 * INGOTS + 6 * NUGGETS),
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
                get(plateDense, SpaceTime, 32),
                get(cableGt16, Quantium, 48))
            .circuit(PISTON_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                DimensionallyShiftedSuperfluid.getFluid(192_000),
                SpaceTime.getMolten(18 * STACKS + 53 * INGOTS + 3 * NUGGETS),
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
                get(wrapCircuit, UMV, 6),
                get(wrapCircuit, UIV, 12),
                get(wrapCircuit, UEV, 24))
            .circuit(ROBOT_ARM_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                DimensionallyShiftedSuperfluid.getFluid(192_000),
                SpaceTime.getMolten(16 * STACKS + 32 * INGOTS),
                Quantium.getMolten(9 * STACKS),
                HYPOGEN.getFluidStack(3 * STACKS),
                CELESTIAL_TUNGSTEN.getFluidStack(3 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UIV)
            .metadata(COAL_CASING_TIER, COAL_UMV)
            .addTo(componentAssemblyLineRecipes);

        for (var rubber : new Materials[] {RubberSilicone, StyreneButadieneRubber }) {
            // Pump
            GTValues.RA.stdBuilder()
                .itemOutputs(Electric_Pump_UMV.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_UMV, 48),
                    get(plateDense, SpaceTime, 21),
                    get(cableGt16, Quantium, 24))
                .circuit(PUMP_CIRCUIT)
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                    DimensionallyShiftedSuperfluid.getFluid(192_000),
                    SpaceTime.getMolten(14 * STACKS + 5 * INGOTS + 3 * NUGGETS),
                    rubber.getMolten(12 * STACKS),
                    Infinity.getMolten(9 * STACKS),
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
                    get(plateDense, SpaceTime, 10),
                    get(cableGt16, Quantium, 24))
                .circuit(CONVEYOR_CIRCUIT)
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                    DimensionallyShiftedSuperfluid.getFluid(192_000),
                    rubber.getMolten(59 * STACKS + 58 * INGOTS),
                    SpaceTime.getMolten(6 * STACKS + 53 * INGOTS + 3 * NUGGETS),
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
                get(frameGt, SpaceTime, 48),
                get(Electric_Motor_UMV, 48),
                get(plateDense, SpaceTime, 42),
                get(NuclearStar, 192),
                get(wrapCircuit, UMV, 12))
            .circuit(SENSOR_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                CELESTIAL_TUNGSTEN.getFluidStack(15 * STACKS),
                QUANTUM.getFluidStack(12 * STACKS),
                ASTRAL_TITANIUM.getFluidStack(12 * STACKS),
                TITANSTEEL.getFluidStack(12 * STACKS),
                Quantium.getMolten(10 * STACKS + 32 * INGOTS),
                HYPOGEN.getFluidStack(3 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UIV)
            .metadata(COAL_CASING_TIER, COAL_UMV)
            .addTo(componentAssemblyLineRecipes);

        // Emitter
        GTValues.RA.stdBuilder()
            .itemOutputs(Emitter_UMV.get(64))
            .itemInputsUnsafe(
                get(frameGt, SpaceTime, 48),
                get(Electric_Motor_UMV, 48),
                get(NuclearStar, 192),
                get(wrapCircuit, UMV, 12))
            .circuit(EMITTER_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                CELESTIAL_TUNGSTEN.getFluidStack(15 * STACKS),
                QUANTUM.getFluidStack(12 * STACKS),
                ASTRAL_TITANIUM.getFluidStack(12 * STACKS),
                TITANSTEEL.getFluidStack(12 * STACKS),
                Quantium.getMolten(10 * STACKS + 32 * INGOTS),
                SpaceTime.getMolten(6 * STACKS),
                HYPOGEN.getFluidStack(3 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UIV)
            .metadata(COAL_CASING_TIER, COAL_UMV)
            .addTo(componentAssemblyLineRecipes);

        // Field Generator
        GTValues.RA.stdBuilder()
            .itemOutputs(Field_Generator_UMV.get(64))
            .itemInputsUnsafe(
                get(frameGt, SpaceTime, 48),
                get(plateDense, SpaceTime, 32),
                get(NuclearStar, 96),
                get(Emitter_UMV, 192),
                get(wrapCircuit, UXV, 12))
            .circuit(FIELD_GENERATOR_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(13 * STACKS + 32 * INGOTS),
                HYPOGEN.getFluidStack(51 * STACKS),
                Quantium.getMolten(12 * STACKS),
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
                get(wrapCircuit, UHV, 114),
                get(wireGt16, SpaceTime, 48),
                get(nanite, Gold, 12))
            .circuit(MOTOR_CIRCUIT)
            .fluidInputs(
                DimensionallyShiftedSuperfluid.getFluid(384_000),
                MHDCSM.getMolten(31 * STACKS + 10 * INGOTS + 6 * NUGGETS),
                Eternity.getMolten(28 * STACKS + 10 * INGOTS + 6 * NUGGETS),
                Universium.getMolten(15 * STACKS),
                MagMatter.getMolten(12 * STACKS),
                SuperconductorUMVBase.getMolten(12 * STACKS),
                SpaceTime.getMolten(3 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UMV)
            .metadata(COAL_CASING_TIER, COAL_UXV)
            .addTo(componentAssemblyLineRecipes);

        // Piston
        GTValues.RA.stdBuilder()
            .itemOutputs(Electric_Piston_UXV.get(64))
            .itemInputsUnsafe(
                get(Electric_Motor_UXV, 48),
                get(wrapCircuit, UHV, 84),
                get(nanite, Gold, 12))
            .circuit(PISTON_CIRCUIT)
            .fluidInputs(
                DimensionallyShiftedSuperfluid.getFluid(384_000),
                MHDCSM.getMolten(26 * STACKS + 21 * INGOTS + 3 * NUGGETS),
                Eternity.getMolten(23 * STACKS + 21 * INGOTS + 3 * NUGGETS),
                SpaceTime.getMolten(15 * STACKS),
                MagMatter.getMolten(9 * STACKS),
                Universium.getMolten(3 * STACKS))
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
                get(wrapCircuit, UXV, 6),
                get(wrapCircuit, UMV, 12),
                get(wrapCircuit, UIV, 24),
                get(wrapCircuit, UHV, 54),
                get(nanite, Gold, 24))
            .circuit(ROBOT_ARM_CIRCUIT)
            .fluidInputs(
                DimensionallyShiftedSuperfluid.getFluid(384_000),
                SpaceTime.getMolten(21 * STACKS),
                MHDCSM.getMolten(19 * STACKS + 32 * INGOTS),
                Eternity.getMolten(16 * STACKS + 32 * INGOTS),
                MagMatter.getMolten(10 * STACKS + 32 * INGOTS),
                Universium.getMolten(3 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UMV)
            .metadata(COAL_CASING_TIER, COAL_UXV)
            .addTo(componentAssemblyLineRecipes);

        // Pump
        GTValues.RA.stdBuilder()
            .itemOutputs(Electric_Pump_UXV.get(64))
            .itemInputsUnsafe(
                get(Electric_Motor_UXV, 48),
                get(wrapCircuit, UHV, 42),
                get(wireGt16, SpaceTime, 48),
                get(nanite, Gold, 12))
            .circuit(PUMP_CIRCUIT)
            .fluidInputs(
                DimensionallyShiftedSuperfluid.getFluid(384_000),
                MHDCSM.getMolten(20 * STACKS + 5 * INGOTS + 3 * NUGGETS),
                Eternity.getMolten(17 * STACKS + 5 * INGOTS + 3 * NUGGETS),
                MagMatter.getMolten(12 * STACKS + 48 * INGOTS),
                RadoxPolymer.getMolten(12 * STACKS),
                Kevlar.getMolten(12 * STACKS),
                SpaceTime.getMolten(12 * STACKS),
                Universium.getMolten(3 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UMV)
            .metadata(COAL_CASING_TIER, COAL_UXV)
            .addTo(componentAssemblyLineRecipes);

        // Conveyor
        GTValues.RA.stdBuilder()
            .itemOutputs(Conveyor_Module_UXV.get(64))
            .itemInputsUnsafe(
                get(Electric_Motor_UXV, 96),
                get(wrapCircuit, UHV, 36),
                get(wireGt16, SpaceTime, 48),
                get(nanite, Gold, 12))
            .circuit(CONVEYOR_CIRCUIT)
            .fluidInputs(
                DimensionallyShiftedSuperfluid.getFluid(384_000),
                RadoxPolymer.getMolten(59 * STACKS + 58 * INGOTS),
                Kevlar.getMolten(59 * STACKS + 58 * INGOTS),
                MHDCSM.getMolten(11 * STACKS + 21 * INGOTS + 3 * NUGGETS),
                Eternity.getMolten(8 * STACKS + 21 * INGOTS + 3 * NUGGETS),
                SpaceTime.getMolten(3 * STACKS),
                Universium.getMolten(3 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UMV)
            .metadata(COAL_CASING_TIER, COAL_UXV)
            .addTo(componentAssemblyLineRecipes);

        // Sensor
        GTValues.RA.stdBuilder()
            .itemOutputs(Sensor_UXV.get(64))
            .itemInputsUnsafe(
                get(frameGt, MHDCSM, 48),
                get(Electric_Motor_UXV, 48),
                get(NuclearStar, 768),
                get(wrapCircuit, UXV, 12),
                get(wrapCircuit, UHV, 48),
                get(nanite, Gold, 24))
            .circuit(SENSOR_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(75 * STACKS),
                SpaceTime.getMolten(36 * STACKS),
                MHDCSM.getMolten(21 * STACKS),
                Eternity.getMolten(18 * STACKS),
                Universium.getMolten(15 * STACKS),
                MagMatter.getMolten(12 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UMV)
            .metadata(COAL_CASING_TIER, COAL_UXV)
            .addTo(componentAssemblyLineRecipes);

        // Emitter
        GTValues.RA.stdBuilder()
            .itemOutputs(Emitter_UXV.get(64))
            .itemInputsUnsafe(
                get(frameGt, MHDCSM, 48),
                get(Electric_Motor_UXV, 48),
                get(NuclearStar, 768),
                get(wrapCircuit, UXV, 12),
                get(wrapCircuit, UHV, 48),
                get(nanite, Gold, 24))
            .circuit(EMITTER_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(75 * STACKS),
                SpaceTime.getMolten(36 * STACKS),
                MHDCSM.getMolten(21 * STACKS),
                Eternity.getMolten(18 * STACKS),
                Universium.getMolten(15 * STACKS),
                MagMatter.getMolten(12 * STACKS))
            .duration(40 * MINUTES)
            .eut(RECIPE_UMV)
            .metadata(COAL_CASING_TIER, COAL_UXV)
            .addTo(componentAssemblyLineRecipes);

        // Field Generator
        GTValues.RA.stdBuilder()
            .itemOutputs(Field_Generator_UXV.get(64))
            .itemInputsUnsafe(
                get(frameGt, MHDCSM, 48),
                get(NuclearStar, 3072),
                get(Emitter_UXV, 192),
                get(wrapCircuit, UXV, 24),
                get(wrapCircuit, UHV, 66),
                get(nanite, Gold, 36))
            .circuit(FIELD_GENERATOR_CIRCUIT)
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(75 * STACKS),
                SpaceTime.getMolten(27 * STACKS),
                MHDCSM.getMolten(19 * STACKS + 32 * INGOTS),
                Eternity.getMolten(16 * STACKS + 32 * INGOTS),
                Universium.getMolten(15 * STACKS),
                MagMatter.getMolten(12 * STACKS),
                SuperconductorUMVBase.getMolten(12 * STACKS))
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
