package goodgenerator.loader;

import static bartworks.system.material.WerkstoffLoader.Ruridit;
import static goodgenerator.api.recipe.GoodGeneratorRecipeMaps.componentAssemblyLineRecipes;
import static gregtech.api.enums.ItemList.*;
import static gregtech.api.enums.Materials.*;
import static gregtech.api.enums.MaterialsKevlar.Kevlar;
import static gregtech.api.enums.MaterialsUEVplus.*;
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
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.COAL_CASING_TIER;
import static gregtech.api.util.GTUtility.getIntegratedCircuit;
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
 *     <li>16 Gravi Stars -> 1 Nuclear Star for UHV+.
 *     <li>16 Tier N Nanites -> 1 Tier N+1 Nanite (i.e., 16 Neutronium -> 1 Gold).
 * </ul>
 * Fluid conversion rules:
 * <ul>
 *     <li>Convert metal items to fluid IF the stack size exceeds 64.
 *     <li>Convert fluids to their "basic" form when needed, like converting
 *           Magnetic Samarium to normal, as magnetic has no molten form.
 * </ul>
 * Circuit Numbers (LuV+ only):
 * <ul>
 *     <li>None: Field Generator
 *     <li>1: Motor
 *     <li>2: Piston
 *     <li>3: Pump
 *     <li>4: Robot Arm
 *     <li>5: Conveyor
 *     <li>6: Emitter
 *     <li>7: Sensor
 * </ul>
 * </pre>
 */
public class ComponentAssemblyLineLoader {

    private static final int L = (int) GTValues.L;
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
        MOTOR_CIRCUIT     = 1,
        PISTON_CIRCUIT    = 2,
        PUMP_CIRCUIT      = 3,
        ROBOT_ARM_CIRCUIT = 4,
        CONVEYOR_CIRCUIT  = 5,
        EMITTER_CIRCUIT   = 6,
        SENSOR_CIRCUIT    = 7;

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

        for (var rubber : new Materials[] { Rubber, Silicone, StyreneButadieneRubber }) {
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
                    rubber.getMolten(24 * L))
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
                Brass.getMolten(96 * L))
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
                RedSteel.getMolten(96 * L))
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

        for (var rubber : new Materials[] { Rubber, Silicone, StyreneButadieneRubber }) {
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
                    rubber.getMolten(24 * L))
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
                Electrum.getMolten(96 * L))
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
                TungstenSteel.getMolten(96 * L))
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

        for (var rubber : new Materials[] { Rubber, Silicone, StyreneButadieneRubber }) {
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
                    rubber.getMolten(24 * L))
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
                Chrome.getMolten(96 * L))
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
                NiobiumTitanium.getMolten(192 * L))
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

        for (var rubber : new Materials[] { Rubber, Silicone, StyreneButadieneRubber }) {
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
                    rubber.getMolten(24 * L))
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
                Platinum.getMolten(96 * L))
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
                HSSG.getMolten(192 * L))
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
            .duration(48 * SECONDS)
            .eut(RECIPE_EV)
            .metadata(COAL_CASING_TIER, COAL_IV)
            .addTo(componentAssemblyLineRecipes);

        for (var rubber : new Materials[] { Silicone, StyreneButadieneRubber }) {
            // Pump
            GTValues.RA.stdBuilder()
                .itemOutputs(Electric_Pump_IV.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_IV, 48),
                    get(rotor, TungstenSteel, 48),
                    get(screw, TungstenSteel, 48),
                    get(cableGt16, Tungsten, 3),
                    get(pipeMedium, TungstenSteel, 48))
                .fluidInputs(
                    rubber.getMolten(24 * L))
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
            .fluidInputs(
                Iridium.getMolten(96 * L))
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
            .fluidInputs(
                HSSS.getMolten(192 * L))
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
                get(cableGt16, YttriumBariumCuprate, 6),
                getIntegratedCircuit(MOTOR_CIRCUIT))
            .fluidInputs(
                INDALLOY_140.getFluidStack(48 * L),
                Lubricant.getFluid(12000),
                Ruridit.getMolten(768 * L),
                HSSS.getMolten(96 * L))
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
                get(cableGt16, YttriumBariumCuprate, 12),
                getIntegratedCircuit(PISTON_CIRCUIT))
            .fluidInputs(
                INDALLOY_140.getFluidStack(48 * L),
                Lubricant.getFluid(12000),
                HSSS.getMolten(602 * L + 96))
            .duration(24 * MINUTES)
            .eut(RECIPE_IV)
            .metadata(COAL_CASING_TIER, COAL_LuV)
            .addTo(componentAssemblyLineRecipes);

        for (var rubber : new Materials[] { Silicone, StyreneButadieneRubber }) {
            // Pump
            GTValues.RA.stdBuilder()
                .itemOutputs(Electric_Pump_LuV.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_LuV, 48),
                    get(plateDense, HSSS, 10),
                    get(cableGt16, YttriumBariumCuprate, 6),
                    getIntegratedCircuit(PUMP_CIRCUIT))
                .fluidInputs(
                    INDALLOY_140.getFluidStack(48 * L),
                    Lubricant.getFluid(12000),
                    HSSS.getMolten(450 * L + 96),
                    NiobiumTitanium.getMolten(96 * L),
                    rubber.getMolten(48 * L))
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
                    get(plateDense, rubber, 53),
                    getIntegratedCircuit(CONVEYOR_CIRCUIT))
                .fluidInputs(
                    INDALLOY_140.getFluidStack(48 * L),
                    Lubricant.getFluid(12000),
                    HSSS.getMolten(218 * L + 96))
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
                get(cableGt16, YttriumBariumCuprate, 18),
                getIntegratedCircuit(ROBOT_ARM_CIRCUIT))
            .fluidInputs(
                INDALLOY_140.getFluidStack(192 * L),
                Lubricant.getFluid(12000),
                HSSS.getMolten(528 * L))
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
                get(cableGt16, YttriumBariumCuprate, 21),
                getIntegratedCircuit(SENSOR_CIRCUIT))
            .fluidInputs(
                INDALLOY_140.getFluidStack(192 * L),
                Gallium.getMolten(2304 * L))
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
                get(cableGt16, YttriumBariumCuprate, 21),
                getIntegratedCircuit(EMITTER_CIRCUIT))
            .fluidInputs(
                INDALLOY_140.getFluidStack(192 * L),
                Gallium.getMolten(2304 * L),
                Ruridit.getMolten(192 * L))
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
            .fluidInputs(
                INDALLOY_140.getFluidStack(192 * L),
                Ruridit.getMolten(1536 * L))
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
                get(cableGt16, VanadiumGallium, 24),
                getIntegratedCircuit(MOTOR_CIRCUIT))
            .fluidInputs(
                INDALLOY_140.getFluidStack(96 * L),
                Lubricant.getFluid(36000),
                Europium.getMolten(1152 * L),
                NaquadahAlloy.getMolten(325 * L + 48))
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
                get(cableGt16, VanadiumGallium, 48),
                getIntegratedCircuit(PISTON_CIRCUIT))
            .fluidInputs(
                INDALLOY_140.getFluidStack(96 * L),
                Lubricant.getFluid(36000),
                NaquadahAlloy.getMolten(602 * L + 96))
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
                get(wrapCircuit, IV, 24),
                getIntegratedCircuit(ROBOT_ARM_CIRCUIT))
            .fluidInputs(
                INDALLOY_140.getFluidStack(384 * L),
                Lubricant.getFluid(36000),
                VanadiumGallium.getMolten(576 * L),
                NaquadahAlloy.getMolten(528 * L))
            .duration(24 * MINUTES)
            .eut(RECIPE_LuV)
            .metadata(COAL_CASING_TIER, COAL_ZPM)
            .addTo(componentAssemblyLineRecipes);

        for (var rubber : new Materials[] { Silicone, StyreneButadieneRubber }) {
            // Pump
            GTValues.RA.stdBuilder()
                .itemOutputs(Electric_Pump_ZPM.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_ZPM, 48),
                    get(plateDense, NaquadahAlloy, 10),
                    get(cableGt16, VanadiumGallium, 24),
                    getIntegratedCircuit(PUMP_CIRCUIT))
                .fluidInputs(
                    INDALLOY_140.getFluidStack(96 * L),
                    Lubricant.getFluid(36000),
                    NaquadahAlloy.getMolten(450 * L + 96),
                    Enderium.getMolten(288 * L),
                    rubber.getMolten(96 * L))
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
                    get(cableGt16, VanadiumGallium, 24),
                    getIntegratedCircuit(CONVEYOR_CIRCUIT))
                .fluidInputs(
                    INDALLOY_140.getFluidStack(96 * L),
                    Lubricant.getFluid(36000),
                    rubber.getMolten(954 * L),
                    NaquadahAlloy.getMolten(218 * L + 96))
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
                get(wrapCircuit, Materials.ZPM, 12),
                getIntegratedCircuit(SENSOR_CIRCUIT))
            .fluidInputs(
                INDALLOY_140.getFluidStack(384 * L),
                Trinium.getMolten(2304 * L),
                VanadiumGallium.getMolten(672 * L))
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
                get(wrapCircuit, Materials.ZPM, 12),
                getIntegratedCircuit(EMITTER_CIRCUIT))
            .fluidInputs(
                INDALLOY_140.getFluidStack(384 * L),
                Trinium.getMolten(2304 * L),
                VanadiumGallium.getMolten(672 * L),
                Osmiridium.getMolten(192 * L))
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
            .fluidInputs(
                INDALLOY_140.getFluidStack(384 * L),
                Europium.getMolten(1536 * L),
                VanadiumGallium.getMolten(768 * L))
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
                get(cableGt16, NaquadahAlloy, 24),
                getIntegratedCircuit(MOTOR_CIRCUIT))
            .fluidInputs(
                INDALLOY_140.getFluidStack(432 * L),
                Lubricant.getFluid(96000),
                Neutronium.getMolten(2304 * L),
                Americium.getMolten(2304 * L),
                Naquadria.getMolten(432 * L),
                Samarium.getMolten(96 * L))
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
                get(cableGt16, NaquadahAlloy, 48),
                getIntegratedCircuit(PISTON_CIRCUIT))
            .fluidInputs(
                INDALLOY_140.getFluidStack(432 * L),
                Lubricant.getFluid(96000),
                Neutronium.getMolten(602 * L + 96),
                Naquadria.getMolten(432 * L))
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
                get(wrapCircuit, LuV, 24),
                getIntegratedCircuit(ROBOT_ARM_CIRCUIT))
            .fluidInputs(
                INDALLOY_140.getFluidStack(768 * L),
                Lubricant.getFluid(96000),
                NaquadahAlloy.getMolten(576 * L),
                Neutronium.getMolten(528 * L),
                Naquadria.getMolten(432 * L))
            .duration(24 * MINUTES)
            .eut(RECIPE_ZPM)
            .metadata(COAL_CASING_TIER, COAL_UV)
            .addTo(componentAssemblyLineRecipes);

        for (var rubber : new Materials[] { Silicone, StyreneButadieneRubber }) {
            // Pump
            GTValues.RA.stdBuilder()
                .itemOutputs(Electric_Pump_UV.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_UV, 48),
                    get(plateDense, Neutronium, 10),
                    get(cableGt16, NaquadahAlloy, 24),
                    getIntegratedCircuit(PUMP_CIRCUIT))
                .fluidInputs(
                    INDALLOY_140.getFluidStack(768 * L),
                    Lubricant.getFluid(96000),
                    Naquadah.getMolten(576 * L),
                    Neutronium.getMolten(450 * L + 96),
                    Naquadria.getMolten(432 * L),
                    rubber.getMolten(192 * L))
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
                    get(cableGt16, NaquadahAlloy, 24),
                    getIntegratedCircuit(CONVEYOR_CIRCUIT))
                .fluidInputs(
                    INDALLOY_140.getFluidStack(432 * L),
                    Lubricant.getFluid(96000),
                    rubber.getMolten(1917 * L),
                    Naquadria.getMolten(432 * L),
                    Neutronium.getMolten(218 * L + 96))
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
                get(wrapCircuit, UV, 12),
                getIntegratedCircuit(SENSOR_CIRCUIT))
            .fluidInputs(
                INDALLOY_140.getFluidStack(768 * L),
                Naquadria.getMolten(2736 * L),
                NaquadahAlloy.getMolten(672 * L))
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
                get(wrapCircuit, UV, 12),
                getIntegratedCircuit(EMITTER_CIRCUIT))
            .fluidInputs(
                INDALLOY_140.getFluidStack(768 * L),
                Naquadria.getMolten(2736 * L),
                NaquadahAlloy.getMolten(672 * L),
                Neutronium.getMolten(192 * L))
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
            .fluidInputs(
                INDALLOY_140.getFluidStack(768 * L),
                Neutronium.getMolten(2304 * L),
                NaquadahAlloy.getMolten(768 * L),
                Naquadria.getMolten(432 * L))
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
                get(cableGt16, Bedrockium, 24),
                getIntegratedCircuit(MOTOR_CIRCUIT))
            .fluidInputs(
                INDALLOY_140.getFluidStack(864 * L),
                Lubricant.getFluid(192000),
                Neutronium.getMolten(3072 * L),
                Naquadria.getMolten(864 * L),
                CosmicNeutronium.getMolten(650 * L + 96),
                Samarium.getMolten(192 * L))
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
                get(cableGt16, Bedrockium, 48),
                getIntegratedCircuit(PISTON_CIRCUIT))
            .fluidInputs(
                INDALLOY_140.getFluidStack(864 * L),
                Lubricant.getFluid(192000),
                CosmicNeutronium.getMolten(1205 * L + 48),
                Naquadria.getMolten(864 * L))
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
                get(wrapCircuit, Materials.ZPM, 24),
                getIntegratedCircuit(ROBOT_ARM_CIRCUIT))
            .fluidInputs(
                INDALLOY_140.getFluidStack(864 * L),
                Lubricant.getFluid(192000),
                CosmicNeutronium.getMolten(1056 * L),
                Naquadria.getMolten(864 * L),
                Bedrockium.getMolten(576 * L))
            .duration(40 * MINUTES)
            .eut(RECIPE_UV)
            .metadata(COAL_CASING_TIER, COAL_UHV)
            .addTo(componentAssemblyLineRecipes);

        for (var rubber : new Materials[] { Silicone, StyreneButadieneRubber }) {
            // Pump
            GTValues.RA.stdBuilder()
                .itemOutputs(Electric_Pump_UHV.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_UHV, 48),
                    get(plateDense, CosmicNeutronium, 21),
                    get(cableGt16, Bedrockium, 24),
                    getIntegratedCircuit(PUMP_CIRCUIT))
                .fluidInputs(
                    INDALLOY_140.getFluidStack(864 * L),
                    Lubricant.getFluid(192000),
                    CosmicNeutronium.getMolten(901 * L + 48),
                    Naquadria.getMolten(864 * L),
                    Neutronium.getMolten(576 * L),
                    rubber.getMolten(384 * L))
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
                    get(cableGt16, Bedrockium, 24),
                    getIntegratedCircuit(CONVEYOR_CIRCUIT))
                .fluidInputs(
                    INDALLOY_140.getFluidStack(864 * L),
                    Lubricant.getFluid(192000),
                    rubber.getMolten(1917 * L),
                    Naquadria.getMolten(864 * L),
                    CosmicNeutronium.getMolten(437 * L + 48))
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
                get(NuclearStar, 24),
                get(wrapCircuit, UHV, 12),
                getIntegratedCircuit(SENSOR_CIRCUIT))
            .fluidInputs(
                INDALLOY_140.getFluidStack(864 * L),
                ElectrumFlux.getMolten(3072 * L),
                Naquadria.getMolten(864 * L),
                Bedrockium.getMolten(672 * L))
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
                get(NuclearStar, 24),
                get(wrapCircuit, UHV, 12),
                getIntegratedCircuit(EMITTER_CIRCUIT))
            .fluidInputs(
                INDALLOY_140.getFluidStack(864 * L),
                ElectrumFlux.getMolten(3072 * L),
                Naquadria.getMolten(864 * L),
                Bedrockium.getMolten(672 * L),
                CosmicNeutronium.getMolten(192 * L))
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
                get(NuclearStar, 12),
                get(Emitter_UHV, 192),
                get(wrapCircuit, UEV, 12))
            .fluidInputs(
                INDALLOY_140.getFluidStack(864 * L),
                Neutronium.getMolten(3072 * L),
                Naquadria.getMolten(864 * L),
                Bedrockium.getMolten(768 * L))
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
                get(cableGt16, Draconium, 24),
                getIntegratedCircuit(MOTOR_CIRCUIT))
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(864 * L),
                Lubricant.getFluid(192000),
                CosmicNeutronium.getMolten(3072 * L),
                Infinity.getMolten(1034 * L + 96),
                Quantium.getMolten(864 * L),
                TengamPurified.getMolten(384 * L))
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
                get(cableGt16, Draconium, 48),
                getIntegratedCircuit(PISTON_CIRCUIT))
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(864 * L),
                Lubricant.getFluid(192000),
                Infinity.getMolten(1205 * L + 48),
                Quantium.getMolten(864 * L))
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
                get(wrapCircuit, UV, 24),
                getIntegratedCircuit(ROBOT_ARM_CIRCUIT))
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(864 * L),
                Lubricant.getFluid(192000),
                Infinity.getMolten(1056 * L),
                Quantium.getMolten(864 * L),
                Draconium.getMolten(576 * L))
            .duration(40 * MINUTES)
            .eut(RECIPE_UHV)
            .metadata(COAL_CASING_TIER, COAL_UEV)
            .addTo(componentAssemblyLineRecipes);

        for (var rubber : new Materials[] { Silicone, StyreneButadieneRubber }) {
            // Pump
            GTValues.RA.stdBuilder()
                .itemOutputs(Electric_Pump_UEV.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_UEV, 48),
                    get(pipeLarge, NetherStar, 96),
                    get(plateDense, Infinity, 21),
                    get(cableGt16, Draconium, 24),
                    getIntegratedCircuit(PUMP_CIRCUIT))
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(864 * L),
                    Lubricant.getFluid(192000),
                    Infinity.getMolten(901 * L + 48),
                    Quantium.getMolten(864 * L),
                    rubber.getMolten(768 * L))
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
                    get(cableGt16, Draconium, 24),
                    getIntegratedCircuit(CONVEYOR_CIRCUIT))
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(864 * L),
                    Lubricant.getFluid(192000),
                    rubber.getMolten(3834 * L),
                    Quantium.getMolten(864 * L),
                    Infinity.getMolten(437 * L + 48))
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
                get(NuclearStar, 48),
                get(wrapCircuit, UEV, 12),
                getIntegratedCircuit(SENSOR_CIRCUIT))
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(864 * L),
                InfinityCatalyst.getMolten(3072 * L),
                Quantium.getMolten(864 * L),
                Draconium.getMolten(672 * L))
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
                get(NuclearStar, 48),
                get(wrapCircuit, UEV, 12),
                getIntegratedCircuit(EMITTER_CIRCUIT))
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(864 * L),
                InfinityCatalyst.getMolten(3072 * L),
                Quantium.getMolten(864 * L),
                Draconium.getMolten(672 * L),
                Infinity.getMolten(384 * L))
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
                get(NuclearStar, 24),
                get(Emitter_UEV, 192),
                get(wrapCircuit, UIV, 12))
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(864 * L),
                Tritanium.getMolten(3072 * L),
                Quantium.getMolten(864 * L),
                Draconium.getMolten(768 * L))
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
                get(cableGt16, NetherStar, 24),
                getIntegratedCircuit(MOTOR_CIRCUIT))
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(864 * L),
                DimensionallyShiftedSuperfluid.getFluid(2101 * L + 48),
                MoltenProtoHalkoniteBase.getFluid(3072 * L),
                Infinity.getMolten(3072 * L),
                TranscendentMetal.getMolten(1034 * L + 96),
                TengamPurified.getMolten(768 * L),
                CELESTIAL_TUNGSTEN.getFluidStack(192 * L))
            .duration(40 * MINUTES)
            .eut(RECIPE_UEV)
            .metadata(COAL_CASING_TIER, COAL_UIV)
            .addTo(componentAssemblyLineRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(Electric_Motor_UIV.get(64))
            .itemInputsUnsafe(
                get(cableGt16, NetherStar, 24),
                getIntegratedCircuit(MOTOR_CIRCUIT))
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(864 * L),
                DimensionallyShiftedSuperfluid.getFluid(2101 * L + 48),
                MoltenProtoHalkoniteBase.getFluid(1536 * L),
                Creon.getMolten(1536 * L),
                Mellion.getMolten(1536 * L),
                TranscendentMetal.getMolten(1034 * L + 96),
                TengamPurified.getMolten(768 * L),
                CELESTIAL_TUNGSTEN.getFluidStack(192 * L))
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
                get(cableGt16, NetherStar, 48),
                getIntegratedCircuit(PISTON_CIRCUIT))
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(864 * L),
                DimensionallyShiftedSuperfluid.getFluid(192000),
                TranscendentMetal.getMolten(1205 * L + 48),
                CELESTIAL_TUNGSTEN.getFluidStack(192 * L))
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
                get(cableGt16, NetherStar, 72),
                getIntegratedCircuit(ROBOT_ARM_CIRCUIT))
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(864 * L),
                DimensionallyShiftedSuperfluid.getFluid(192000),
                TranscendentMetal.getMolten(1056 * L),
                CELESTIAL_TUNGSTEN.getFluidStack(192 * L))
            .duration(40 * MINUTES)
            .eut(RECIPE_UEV)
            .metadata(COAL_CASING_TIER, COAL_UIV)
            .addTo(componentAssemblyLineRecipes);

        for (var rubber : new Materials[] { Silicone, StyreneButadieneRubber }) {
            // Pump
            GTValues.RA.stdBuilder()
                .itemOutputs(Electric_Pump_UIV.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_UIV, 48),
                    get(plateDense, TranscendentMetal, 21),
                    get(cableGt16, NetherStar, 24),
                    getIntegratedCircuit(PUMP_CIRCUIT))
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(864 * L),
                    DimensionallyShiftedSuperfluid.getFluid(192000),
                    TranscendentMetal.getMolten(901 * L + 48),
                    rubber.getMolten(768 * L),
                    DraconiumAwakened.getMolten(576 * L),
                    CELESTIAL_TUNGSTEN.getFluidStack(192 * L))
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
                    get(cableGt16, NetherStar, 24),
                    getIntegratedCircuit(CONVEYOR_CIRCUIT))
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(864 * L),
                    DimensionallyShiftedSuperfluid.getFluid(192000),
                    rubber.getMolten(3834 * L),
                    TranscendentMetal.getMolten(437 * L + 48),
                    CELESTIAL_TUNGSTEN.getFluidStack(192 * L))
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
                get(cableGt16, NetherStar, 84),
                getIntegratedCircuit(SENSOR_CIRCUIT))
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(864 * L),
                TRINIUM_REINFORCED_STEEL.getFluidStack(768 * L),
                LAFIUM.getFluidStack(768 * L),
                CINOBITE.getFluidStack(768 * L),
                PIKYONIUM.getFluidStack(768 * L),
                CELESTIAL_TUNGSTEN.getFluidStack(192 * L))
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
                get(cableGt16, NetherStar, 84),
                getIntegratedCircuit(EMITTER_CIRCUIT))
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(864 * L),
                TRINIUM_REINFORCED_STEEL.getFluidStack(768 * L),
                LAFIUM.getFluidStack(768 * L),
                CINOBITE.getFluidStack(768 * L),
                PIKYONIUM.getFluidStack(768 * L),
                TranscendentMetal.getMolten(384 * L),
                CELESTIAL_TUNGSTEN.getFluidStack(192 * L))
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
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(864 * L),
                DimensionallyShiftedSuperfluid.getFluid(768 * L),
                MoltenProtoHalkoniteBase.getFluid(3072 * L),
                Infinity.getMolten(3072 * L),
                CELESTIAL_TUNGSTEN.getFluidStack(192 * L))
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
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(864 * L),
                DimensionallyShiftedSuperfluid.getFluid(768 * L),
                MoltenProtoHalkoniteBase.getFluid(1536 * L),
                Creon.getMolten(1536 * L),
                Mellion.getMolten(1536 * L),
                CELESTIAL_TUNGSTEN.getFluidStack(192 * L))
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
                get(cableGt16, Quantium, 24),
                getIntegratedCircuit(MOTOR_CIRCUIT))
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(864 * L),
                DimensionallyShiftedSuperfluid.getFluid(192000),
                HYPOGEN.getFluidStack(3264 * L),
                TengamPurified.getMolten(1536 * L),
                SpaceTime.getMolten(1034 * L + 96),
                CELESTIAL_TUNGSTEN.getFluidStack(192 * L))
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
                get(cableGt16, Quantium, 48),
                getIntegratedCircuit(PISTON_CIRCUIT))
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(864 * L),
                DimensionallyShiftedSuperfluid.getFluid(192000),
                SpaceTime.getMolten(1205 * L + 48),
                HYPOGEN.getFluidStack(192 * L),
                CELESTIAL_TUNGSTEN.getFluidStack(192 * L))
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
                get(wrapCircuit, UEV, 24),
                getIntegratedCircuit(ROBOT_ARM_CIRCUIT))
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(864 * L),
                DimensionallyShiftedSuperfluid.getFluid(192000),
                SpaceTime.getMolten(1056 * L),
                Quantium.getMolten(576 * L),
                HYPOGEN.getFluidStack(192 * L),
                CELESTIAL_TUNGSTEN.getFluidStack(192 * L))
            .duration(40 * MINUTES)
            .eut(RECIPE_UIV)
            .metadata(COAL_CASING_TIER, COAL_UMV)
            .addTo(componentAssemblyLineRecipes);

        for (var rubber : new Materials[] { Silicone, StyreneButadieneRubber }) {
            // Pump
            GTValues.RA.stdBuilder()
                .itemOutputs(Electric_Pump_UMV.get(64))
                .itemInputsUnsafe(
                    get(Electric_Motor_UMV, 48),
                    get(plateDense, SpaceTime, 21),
                    get(cableGt16, Quantium, 24),
                    getIntegratedCircuit(PUMP_CIRCUIT))
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(864 * L),
                    DimensionallyShiftedSuperfluid.getFluid(192000),
                    SpaceTime.getMolten(901 * L + 48),
                    rubber.getMolten(768 * L),
                    Infinity.getMolten(576 * L),
                    HYPOGEN.getFluidStack(192 * L),
                    CELESTIAL_TUNGSTEN.getFluidStack(192 * L))
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
                    get(cableGt16, Quantium, 24),
                    getIntegratedCircuit(CONVEYOR_CIRCUIT))
                .fluidInputs(
                    MUTATED_LIVING_SOLDER.getFluidStack(864 * L),
                    DimensionallyShiftedSuperfluid.getFluid(192000),
                    rubber.getMolten(3834 * L),
                    SpaceTime.getMolten(437 * L + 48),
                    HYPOGEN.getFluidStack(192 * L),
                    CELESTIAL_TUNGSTEN.getFluidStack(192 * L))
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
                get(wrapCircuit, UMV, 12),
                getIntegratedCircuit(SENSOR_CIRCUIT))
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(864 * L),
                CELESTIAL_TUNGSTEN.getFluidStack(960 * L),
                QUANTUM.getFluidStack(768 * L),
                ASTRAL_TITANIUM.getFluidStack(768 * L),
                TITANSTEEL.getFluidStack(768 * L),
                Quantium.getMolten(672 * L),
                HYPOGEN.getFluidStack(192 * L))
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
                get(wrapCircuit, UMV, 12),
                getIntegratedCircuit(EMITTER_CIRCUIT))
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(864 * L),
                CELESTIAL_TUNGSTEN.getFluidStack(960 * L),
                QUANTUM.getFluidStack(768 * L),
                ASTRAL_TITANIUM.getFluidStack(768 * L),
                TITANSTEEL.getFluidStack(768 * L),
                Quantium.getMolten(672 * L),
                SpaceTime.getMolten(384 * L),
                HYPOGEN.getFluidStack(192 * L))
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
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(864 * L),
                HYPOGEN.getFluidStack(3264 * L),
                Quantium.getMolten(768 * L),
                CELESTIAL_TUNGSTEN.getFluidStack(192 * L))
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
                get(nanite, Gold, 12),
                getIntegratedCircuit(MOTOR_CIRCUIT))
            .fluidInputs(
                DimensionallyShiftedSuperfluid.getFluid(384000),
                MagnetohydrodynamicallyConstrainedStarMatter.getMolten(1994 * L + 96),
                Eternity.getMolten(1802 * L + 96),
                Universium.getMolten(960 * L),
                MagMatter.getMolten(768 * L),
                SuperconductorUMVBase.getMolten(768 * L),
                SpaceTime.getMolten(192 * L))
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
                get(nanite, Gold, 12),
                getIntegratedCircuit(PISTON_CIRCUIT))
            .fluidInputs(
                DimensionallyShiftedSuperfluid.getFluid(384000),
                MagnetohydrodynamicallyConstrainedStarMatter.getMolten(1685 * L + 48),
                Eternity.getMolten(1493 * L + 48),
                SpaceTime.getMolten(960 * L),
                MagMatter.getMolten(576 * L),
                Universium.getMolten(192 * L))
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
                get(nanite, Gold, 24),
                getIntegratedCircuit(ROBOT_ARM_CIRCUIT))
            .fluidInputs(
                DimensionallyShiftedSuperfluid.getFluid(384000),
                SpaceTime.getMolten(1344 * L),
                MagnetohydrodynamicallyConstrainedStarMatter.getMolten(1248 * L),
                Eternity.getMolten(1056 * L),
                MagMatter.getMolten(672 * L),
                Universium.getMolten(192 * L))
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
                get(nanite, Gold, 12),
                getIntegratedCircuit(PUMP_CIRCUIT))
            .fluidInputs(
                DimensionallyShiftedSuperfluid.getFluid(384000),
                MagnetohydrodynamicallyConstrainedStarMatter.getMolten(1285 * L + 48),
                Eternity.getMolten(1093 * L + 48),
                MagMatter.getMolten(816 * L),
                RadoxPolymer.getMolten(768 * L),
                Kevlar.getMolten(768 * L),
                SpaceTime.getMolten(768 * L),
                Universium.getMolten(192 * L))
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
                get(nanite, Gold, 12),
                getIntegratedCircuit(CONVEYOR_CIRCUIT))
            .fluidInputs(
                DimensionallyShiftedSuperfluid.getFluid(384000),
                RadoxPolymer.getMolten(3834 * L),
                Kevlar.getMolten(3834 * L),
                MagnetohydrodynamicallyConstrainedStarMatter.getMolten(725 * L + 48),
                Eternity.getMolten(533 * L + 48),
                SpaceTime.getMolten(192 * L),
                Universium.getMolten(192 * L))
            .duration(40 * MINUTES)
            .eut(RECIPE_UMV)
            .metadata(COAL_CASING_TIER, COAL_UXV)
            .addTo(componentAssemblyLineRecipes);

        // Sensor
        GTValues.RA.stdBuilder()
            .itemOutputs(Sensor_UXV.get(64))
            .itemInputsUnsafe(
                get(frameGt, MagnetohydrodynamicallyConstrainedStarMatter, 48),
                get(Electric_Motor_UXV, 48),
                get(NuclearStar, 768),
                get(wrapCircuit, UXV, 12),
                get(wrapCircuit, UHV, 48),
                get(nanite, Gold, 24),
                getIntegratedCircuit(SENSOR_CIRCUIT))
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(4800 * L),
                SpaceTime.getMolten(2304 * L),
                MagnetohydrodynamicallyConstrainedStarMatter.getMolten(1344 * L),
                Eternity.getMolten(1152 * L),
                Universium.getMolten(960 * L),
                MagMatter.getMolten(768 * L))
            .duration(40 * MINUTES)
            .eut(RECIPE_UMV)
            .metadata(COAL_CASING_TIER, COAL_UXV)
            .addTo(componentAssemblyLineRecipes);

        // Emitter
        GTValues.RA.stdBuilder()
            .itemOutputs(Emitter_UXV.get(64))
            .itemInputsUnsafe(
                get(frameGt, MagnetohydrodynamicallyConstrainedStarMatter, 48),
                get(Electric_Motor_UXV, 48),
                get(NuclearStar, 768),
                get(wrapCircuit, UXV, 12),
                get(wrapCircuit, UHV, 48),
                get(nanite, Gold, 24),
                getIntegratedCircuit(EMITTER_CIRCUIT))
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(4800 * L),
                SpaceTime.getMolten(2304 * L),
                MagnetohydrodynamicallyConstrainedStarMatter.getMolten(1344 * L),
                Eternity.getMolten(1152 * L),
                Universium.getMolten(960 * L),
                MagMatter.getMolten(768 * L))
            .duration(40 * MINUTES)
            .eut(RECIPE_UMV)
            .metadata(COAL_CASING_TIER, COAL_UXV)
            .addTo(componentAssemblyLineRecipes);

        // Field Generator
        GTValues.RA.stdBuilder()
            .itemOutputs(Field_Generator_UXV.get(64))
            .itemInputsUnsafe(
                get(frameGt, MagnetohydrodynamicallyConstrainedStarMatter, 48),
                get(NuclearStar, 3072),
                get(Emitter_UXV, 192),
                get(wrapCircuit, UXV, 24),
                get(wrapCircuit, UHV, 66),
                get(nanite, Gold, 36))
            .fluidInputs(
                MUTATED_LIVING_SOLDER.getFluidStack(4800 * L),
                SpaceTime.getMolten(1728 * L),
                MagnetohydrodynamicallyConstrainedStarMatter.getMolten(1248 * L),
                Eternity.getMolten(1056 * L),
                Universium.getMolten(960 * L),
                MagMatter.getMolten(768 * L),
                SuperconductorUMVBase.getMolten(768 * L))
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
