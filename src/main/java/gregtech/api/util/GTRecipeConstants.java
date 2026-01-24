package gregtech.api.util;

import static gregtech.api.recipe.RecipeMaps.scannerFakeRecipes;
import static gregtech.api.util.GTRecipeMapUtil.convertCellToFluid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.ArrayUtils;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.recipe.RecipeCategories;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.api.recipe.metadata.SimpleRecipeMetadataKey;
import gregtech.api.util.recipe.QuantumComputerRecipeData;
import gregtech.api.util.recipe.Scanning;
import gregtech.api.util.recipe.Sievert;
import gregtech.common.items.IDMetaItem03;
import gregtech.common.items.MetaGeneratedItem03;
import gregtech.common.tileentities.machines.multi.foundry.FoundryModule;
import gtnhlanth.common.item.ItemPhotolithographicMask;
import gtnhlanth.common.item.MaskList;
import gtnhlanth.common.register.LanthItemList;

// this class is intended to be import-static-ed on every recipe script
// so take care to not put unrelated stuff here!
public class GTRecipeConstants {

    /**
     * Set to true to signal the recipe require low gravity. do nothing if recipe set specialValue explicitly. Can
     * coexist with CLEANROOM just fine
     */
    public static final RecipeMetadataKey<Boolean> LOW_GRAVITY = SimpleRecipeMetadataKey
        .create(Boolean.class, "low_gravity");
    /**
     * Set to true to signal the recipe require cleanroom. do nothing if recipe set specialValue explicitly. Can coexist
     * with LOW_GRAVITY just fine
     */
    public static final RecipeMetadataKey<Boolean> CLEANROOM = SimpleRecipeMetadataKey
        .create(Boolean.class, "cleanroom");
    /**
     * Common additive to use in recipe, e.g. for PBF, this is coal amount.
     */
    public static final RecipeMetadataKey<Integer> ADDITIVE_AMOUNT = SimpleRecipeMetadataKey
        .create(Integer.class, "additives");
    /**
     * Used for fusion reactor. Denotes ignition threshold.
     */
    public static final RecipeMetadataKey<Long> FUSION_THRESHOLD = SimpleRecipeMetadataKey
        .create(Long.class, "fusion_threshold");

    /**
     * Scanning data used for scanner for assembly line recipes (time and voltage). Scanning time should be between 30
     * seconds and 3 minutes, and the voltage 1 tiers below the available scanner tier for the recipe.
     */
    public static final RecipeMetadataKey<Scanning> SCANNING = SimpleRecipeMetadataKey
        .create(Scanning.class, "scanning");
    /**
     * Fuel type. TODO should we use enum directly?
     */
    public static final RecipeMetadataKey<Integer> FUEL_TYPE = SimpleRecipeMetadataKey
        .create(Integer.class, "fuel_type");
    /**
     * Fuel value.
     */
    public static final RecipeMetadataKey<Integer> FUEL_VALUE = SimpleRecipeMetadataKey
        .create(Integer.class, "fuel_value");
    /**
     * Required heat for heating coil (Kelvin).
     */
    public static final RecipeMetadataKey<Integer> COIL_HEAT = SimpleRecipeMetadataKey
        .create(Integer.class, "coil_heat");
    /**
     * Research item used by assline recipes.
     */
    public static final RecipeMetadataKey<ItemStack> RESEARCH_ITEM = SimpleRecipeMetadataKey
        .create(ItemStack.class, "research_item");
    /**
     * For assembler. It accepts a single item as oredict. It looks like no one uses this anyway...
     */
    public static final RecipeMetadataKey<Object> OREDICT_INPUT = SimpleRecipeMetadataKey
        .create(Object.class, "oredict_input");
    /**
     * Replicator output material.
     */
    public static final RecipeMetadataKey<Materials> MATERIAL = SimpleRecipeMetadataKey
        .create(Materials.class, "material");
    /**
     * Marker for {@link #UniversalArcFurnace} to tell that the recipe belongs to recycling category.
     */
    public static final RecipeMetadataKey<Boolean> RECYCLE = SimpleRecipeMetadataKey.create(Boolean.class, "recycle");
    /**
     * For Microwave.
     */
    public static final RecipeMetadataKey<Boolean> EXPLODE = SimpleRecipeMetadataKey.create(Boolean.class, "explode");
    /**
     * For Microwave.
     */
    public static final RecipeMetadataKey<Boolean> ON_FIRE = SimpleRecipeMetadataKey.create(Boolean.class, "on_fire");

    /**
     * Values of items used in quantum computer, used to show NEI recipes
     */
    public static final RecipeMetadataKey<QuantumComputerRecipeData> QUANTUM_COMPUTER_DATA = SimpleRecipeMetadataKey
        .create(QuantumComputerRecipeData.class, "quantum_computer_data");

    /**
     * Nano Forge Tier.
     */
    public static final RecipeMetadataKey<Integer> NANO_FORGE_TIER = SimpleRecipeMetadataKey
        .create(Integer.class, "nano_forge_tier");

    /**
     * PCB Factory nanite material
     */
    public static final RecipeMetadataKey<Materials> PCB_NANITE_MATERIAL = SimpleRecipeMetadataKey
        .create(Materials.class, "pcb_nanite_material");

    /**
     * FOG Exotic recipe tier.
     */
    public static final RecipeMetadataKey<Integer> FOG_EXOTIC_TIER = SimpleRecipeMetadataKey
        .create(Integer.class, "fog_exotic_tier");

    /**
     * FOG Plasma recipe tier.
     */
    public static final RecipeMetadataKey<Integer> FOG_PLASMA_TIER = SimpleRecipeMetadataKey
        .create(Integer.class, "fog_plasma_tier");

    /**
     * FOG Plasma multistep requirement.
     */
    public static final RecipeMetadataKey<Boolean> FOG_PLASMA_MULTISTEP = SimpleRecipeMetadataKey
        .create(Boolean.class, "fog_plasma_multistep");

    /**
     * FOG Shortened upgrade name.
     */
    public static final RecipeMetadataKey<String> FOG_UPGRADE_NAME_SHORT = SimpleRecipeMetadataKey
        .create(String.class, "fog_plasma_upgrade_name_short");

    public static final RecipeMetadataKey<FoundryModule> FOUNDRY_MODULE = SimpleRecipeMetadataKey
        .create(FoundryModule.class, "foundry_module");

    /**
     * DEFC Casing tier.
     */
    public static final RecipeMetadataKey<Integer> DEFC_CASING_TIER = SimpleRecipeMetadataKey
        .create(Integer.class, "defc_casing_tier");

    /**
     * Chemplant Casing tier. Beware, codewise index starts at 0, but it is tier 1.
     */
    public static final RecipeMetadataKey<Integer> CHEMPLANT_CASING_TIER = SimpleRecipeMetadataKey
        .create(Integer.class, "chemplant_casing_tier");

    /**
     * Algae Pond tier.
     */
    public static final RecipeMetadataKey<Integer> ALGAE_POND_TIER = SimpleRecipeMetadataKey
        .create(Integer.class, "algae_pond_tier");

    /**
     * QFT Focus tier.
     */
    public static final RecipeMetadataKey<Integer> QFT_FOCUS_TIER = SimpleRecipeMetadataKey
        .create(Integer.class, "qft_focus_tier");

    /**
     * QFT catalyst meta.
     */
    public static final RecipeMetadataKey<ItemStack> QFT_CATALYST = SimpleRecipeMetadataKey
        .create(ItemStack.class, "qft_catalyst");

    /**
     * Tier of advanced compression (HIP/black hole)
     */
    public static final RecipeMetadataKey<Integer> COMPRESSION_TIER = SimpleRecipeMetadataKey
        .create(Integer.class, "compression");

    /**
     * Dissolution Tank Ratio.
     */
    public static final RecipeMetadataKey<Integer> DISSOLUTION_TANK_RATIO = SimpleRecipeMetadataKey
        .create(Integer.class, "dissolution_tank_ratio");

    /**
     * Duration in days for the RTG.
     */
    public static final RecipeMetadataKey<Integer> RTG_DURATION_IN_DAYS = SimpleRecipeMetadataKey
        .create(Integer.class, "rtg_duration_in_days");

    /**
     * Basic output for the Large Naquadah Generator.
     */
    public static final RecipeMetadataKey<Integer> LNG_BASIC_OUTPUT = SimpleRecipeMetadataKey
        .create(Integer.class, "lng_basic_output");

    /**
     * Coil tier for the Naquadah Fuel Refinery.
     */
    public static final RecipeMetadataKey<Integer> NFR_COIL_TIER = SimpleRecipeMetadataKey
        .create(Integer.class, "nfr_coil_tier");

    /**
     * NKE range for the neutron activator.
     */
    public static final RecipeMetadataKey<Integer> NKE_RANGE = SimpleRecipeMetadataKey
        .create(Integer.class, "nke_range");
    /**
     * Precise Assembler casing tier.
     */
    public static final RecipeMetadataKey<Integer> PRECISE_ASSEMBLER_CASING_TIER = SimpleRecipeMetadataKey
        .create(Integer.class, "precise_assembler_casing_tier");
    /**
     * CoAL casing tier.
     */
    public static final RecipeMetadataKey<Integer> COAL_CASING_TIER = SimpleRecipeMetadataKey
        .create(Integer.class, "coal_casing_tier");

    /**
     * LFTR output power.
     */
    public static final RecipeMetadataKey<Integer> LFTR_OUTPUT_POWER = SimpleRecipeMetadataKey
        .create(Integer.class, "lftr_output_power");

    /**
     * Sparge Tower maximum byproduct outputs.
     */
    public static final RecipeMetadataKey<Integer> SPARGE_MAX_BYPRODUCT = SimpleRecipeMetadataKey
        .create(Integer.class, "sparge_max_byproduct");

    /**
     * Research Station data.
     */
    public static final RecipeMetadataKey<Integer> RESEARCH_STATION_DATA = SimpleRecipeMetadataKey
        .create(Integer.class, "research_station_data");

    /**
     * sievert data required for the biovat recipes.
     */

    public static final RecipeMetadataKey<Sievert> SIEVERT = SimpleRecipeMetadataKey.create(Sievert.class, "SIEVERT");

    public static final RecipeMetadataKey<Integer> GLASS = SimpleRecipeMetadataKey.create(Integer.class, "GLASS");

    public static final RecipeMetadataKey<Integer> MASS = SimpleRecipeMetadataKey.create(Integer.class, "mass");

    /**
     * Whether non-gas recipe should be generated together with gas recipes.
     */
    public static final RecipeMetadataKey<Boolean> NO_GAS = SimpleRecipeMetadataKey.create(Boolean.class, "no_gas");

    /**
     * Circuit config in non-gas recipe. No integrated circuit applied if this is set to -1 (default).
     */
    public static final RecipeMetadataKey<Integer> NO_GAS_CIRCUIT_CONFIG = SimpleRecipeMetadataKey
        .create(Integer.class, "no_gas_circuit_config");

    public static final RecipeMetadataKey<Integer> EU_MULTIPLIER = SimpleRecipeMetadataKey
        .create(Integer.class, "eu_multiplier");

    public static final RecipeMetadataKey<Double> HALF_LIFE = SimpleRecipeMetadataKey.create(Double.class, "half-life");

    /**
     * Just some trivia to show in the decay recipes, since they don't have a lot of relevant info. Maybe this will come
     * in handy some day.
     */
    public enum DecayType {
        Unknown,
        /** Nucleus emits 2 protons and 2 neutrons, to form a single new nucleus. */
        Alpha,
        /** Nucleus splits into two smaller nuclei, often emitting several alpha particles. */
        SpontaneousFission,
        /** Nucleus emits a small cluster of protons/neutrons instead of individual protons or neutrons. */
        Cluster,
        /** Nucleus emits an alpha particle, often emitting or absorbing other particles in the process. */
        AlphaTransfer,
        /**
         * Nucleus emits a positron, which typically converts a neutron into a proton, emitting a neutrino in the
         * process.
         */
        BetaMinus,
        /** A proton in the nucleus captures an electron to form a neutron, emitting a neutrino in the process. */
        BetaPlus,
    }

    public static final RecipeMetadataKey<DecayType> DECAY_TYPE = SimpleRecipeMetadataKey
        .create(DecayType.class, "decay-type");

    /**
     * Add a arc furnace recipe. Adds to both normal arc furnace and plasma arc furnace. Will override the fluid input
     * with oxygen/plasma for the respective recipe maps, so there is no point setting it.
     */
    public static final IRecipeMap UniversalArcFurnace = IRecipeMap.newRecipeMap(builder -> {
        if (!GTUtility.isArrayOfLength(builder.getItemInputsBasic(), 1)
            || GTUtility.isArrayEmptyOrNull(builder.getItemOutputs())) return Collections.emptyList();
        int aDuration = builder.getDuration();
        if (aDuration <= 0) {
            return Collections.emptyList();
        }
        boolean recycle = builder.getMetadataOrDefault(RECYCLE, false);
        Collection<GTRecipe> ret = new ArrayList<>();
        for (Materials mat : new Materials[] { Materials.Argon, Materials.Nitrogen }) {
            builder.duration(Math.max(1, mat == Materials.Nitrogen ? aDuration / 4 : aDuration / 24));
            int tPlasmaAmount = (int) Math.max(1L, aDuration / (mat.getMass() * 16L));
            GTRecipeBuilder plasmaBuilder = builder.copy()
                .fluidInputs(mat.getPlasma(tPlasmaAmount))
                .fluidOutputs(mat.getGas(tPlasmaAmount));
            if (recycle) {
                continue;
            }
            ret.addAll(RecipeMaps.plasmaArcFurnaceRecipes.doAdd(plasmaBuilder));
        }
        builder.duration(aDuration);
        GTRecipeBuilder arcBuilder = builder.copy()
            .fluidInputs(Materials.Oxygen.getGas(aDuration));
        if (recycle) {
            arcBuilder.recipeCategory(RecipeCategories.arcFurnaceRecycling);
        }
        ret.addAll(RecipeMaps.arcFurnaceRecipes.doAdd(arcBuilder));
        return ret;
    });
    /**
     * Add a chemical reactor recipe to both LCR and singleblocks.
     */
    public static final IRecipeMap UniversalChemical = IRecipeMap.newRecipeMap(builder -> {
        for (ItemStack input : builder.getItemInputsBasic()) {
            // config >= 10 -> this is a special chemical recipe that output fluid/canned fluid variant.
            // it doesn't belong to multiblocks
            if (GTUtility.isAnyIntegratedCircuit(input) && input.getItemDamage() >= 10) {
                return builder.addTo(RecipeMaps.chemicalReactorRecipes);
            }
        }
        return GTUtility.concat(
            builder.copy()
                .addTo(RecipeMaps.chemicalReactorRecipes),
            convertCellToFluid(builder, false)
                // LCR does not need cleanroom.
                .metadata(CLEANROOM, false)
                .addTo(RecipeMaps.multiblockChemicalReactorRecipes));
    });

    /**
     * Adds an engraver recipe that might use purified water. Still added to the regular recipemap if it ends up not
     * needing it.
     */
    public static final IRecipeMap WaferEngravingRecipes = IRecipeMap.newRecipeMap(builder -> {
        // spotless:off
        enum Wafer{
            Naquadah,
            Europium,
            Americium,
            // Beamline masks
            MaskT1,
            MaskT2,
            MaskT3,
        }
        // spotless:on
        // Find the wafer used
        Wafer wafer = null;
        ItemPhotolithographicMask t1Item = (ItemPhotolithographicMask) LanthItemList.maskMap.get(MaskList.BLANK1);
        ItemPhotolithographicMask t2Item = (ItemPhotolithographicMask) LanthItemList.maskMap.get(MaskList.BLANK2);
        ItemPhotolithographicMask t3Item = (ItemPhotolithographicMask) LanthItemList.maskMap.get(MaskList.BLANK3);
        for (ItemStack input : builder.getItemInputsBasic()) {
            if (input.getItem() instanceof MetaGeneratedItem03) {
                int meta = input.getItemDamage() - 32000;
                // Check if this input item is indicating a wafer recipe we want to modify
                if (meta == IDMetaItem03.Circuit_Silicon_Wafer3.ID) wafer = Wafer.Naquadah;
                else if (meta == IDMetaItem03.Circuit_Silicon_Wafer4.ID) wafer = Wafer.Europium;
                else if (meta == IDMetaItem03.Circuit_Silicon_Wafer5.ID) wafer = Wafer.Americium;
            }

            // Now look for beamline masks
            if (input.getItem() instanceof ItemPhotolithographicMask mask) {
                String spectrum = mask.getDescSpectrum();
                if (spectrum.equals(t1Item.getDescSpectrum())) wafer = Wafer.MaskT1;
                else if (spectrum.equals(t2Item.getDescSpectrum())) wafer = Wafer.MaskT2;
                else if (spectrum.equals(t3Item.getDescSpectrum())) wafer = Wafer.MaskT3;
            }

            // Found a wafer, stop checking inputs
            if (wafer != null) break;
        }

        int recipeTime = builder.duration;
        // Bonus for using purified water of a higher tier than necessary
        int halfBoostedRecipeTime = (int) (recipeTime * 0.75);
        int boostedRecipeTime = (int) (recipeTime * 0.5);

        // If this recipe does not use a wafer, exit without modifying it.
        if (wafer == null) return builder.addTo(RecipeMaps.laserEngraverRecipes);
        switch (wafer) {
            case Naquadah -> {
                ArrayList<ItemStack> items = new ArrayList<>(Arrays.asList(builder.getItemInputsBasic()));
                ItemStack[] itemInputs = items.toArray(new ItemStack[] {});
                // Naquadah wafers can use grade 1-2 purified water for a bonus, otherwise use distilled so we don't
                // have to
                // deal with circuits
                return GTUtility.concat(
                    builder.copy()
                        .itemInputs(itemInputs)
                        .fluidInputs(ArrayUtils.addAll(builder.fluidInputs, GTModHandler.getDistilledWater(100L)))
                        .addTo(RecipeMaps.laserEngraverRecipes),
                    builder.copy()
                        .itemInputs(itemInputs)
                        .fluidInputs(
                            ArrayUtils.addAll(builder.fluidInputs, Materials.Grade1PurifiedWater.getFluid(100L)))
                        .duration(halfBoostedRecipeTime)
                        .addTo(RecipeMaps.laserEngraverRecipes),
                    builder.copy()
                        .itemInputs(itemInputs)
                        .fluidInputs(
                            ArrayUtils.addAll(builder.fluidInputs, Materials.Grade2PurifiedWater.getFluid(100L)))
                        .duration(boostedRecipeTime)
                        .addTo(RecipeMaps.laserEngraverRecipes));
            }
            case Europium -> {
                // Require purified water for europium wafers, at least grade 3
                return GTUtility.concat(
                    builder.copy()
                        .fluidInputs(
                            ArrayUtils.addAll(builder.fluidInputs, Materials.Grade3PurifiedWater.getFluid(100L)))
                        .duration(recipeTime)
                        .addTo(RecipeMaps.laserEngraverRecipes),
                    builder.copy()
                        .fluidInputs(
                            ArrayUtils.addAll(builder.fluidInputs, Materials.Grade4PurifiedWater.getFluid(100L)))
                        .duration(boostedRecipeTime)
                        .addTo(RecipeMaps.laserEngraverRecipes));
            }
            case Americium -> {
                // Require purified water for americium wafers, at least grade 5
                return GTUtility.concat(
                    builder.copy()
                        .fluidInputs(
                            ArrayUtils.addAll(builder.fluidInputs, Materials.Grade5PurifiedWater.getFluid(100L)))
                        .duration(recipeTime)
                        .addTo(RecipeMaps.laserEngraverRecipes),
                    builder.copy()
                        .fluidInputs(
                            ArrayUtils.addAll(builder.fluidInputs, Materials.Grade6PurifiedWater.getFluid(100L)))
                        .duration(boostedRecipeTime)
                        .addTo(RecipeMaps.laserEngraverRecipes));
            }
            // Masks require much more purified water because they can make many wafers at once
            case MaskT1 -> {
                // T1 masks require grade 1, 2 or 3 purified water
                return GTUtility.concat(
                    builder.copy()
                        .fluidInputs(
                            ArrayUtils.addAll(builder.fluidInputs, Materials.Grade1PurifiedWater.getFluid(32000L)))
                        .duration(recipeTime)
                        .addTo(RecipeMaps.laserEngraverRecipes),
                    builder.copy()
                        .fluidInputs(
                            ArrayUtils.addAll(builder.fluidInputs, Materials.Grade2PurifiedWater.getFluid(32000L)))
                        .duration(halfBoostedRecipeTime)
                        .addTo(RecipeMaps.laserEngraverRecipes),
                    builder.copy()
                        .fluidInputs(
                            ArrayUtils.addAll(builder.fluidInputs, Materials.Grade3PurifiedWater.getFluid(32000L)))
                        .duration(boostedRecipeTime)
                        .addTo(RecipeMaps.laserEngraverRecipes));
            }
            case MaskT2 -> {
                // T2 masks require grade 4 or 5 purified water
                return GTUtility.concat(
                    builder.copy()
                        .fluidInputs(
                            ArrayUtils.addAll(builder.fluidInputs, Materials.Grade4PurifiedWater.getFluid(32000L)))
                        .duration(recipeTime)
                        .addTo(RecipeMaps.laserEngraverRecipes),
                    builder.copy()
                        .fluidInputs(
                            ArrayUtils.addAll(builder.fluidInputs, Materials.Grade5PurifiedWater.getFluid(32000L)))
                        .duration(boostedRecipeTime)
                        .addTo(RecipeMaps.laserEngraverRecipes));
            }
            case MaskT3 -> {
                // T3 masks require grade 6, 7 or 8 purified water
                return GTUtility.concat(
                    builder.copy()
                        .fluidInputs(
                            ArrayUtils.addAll(builder.fluidInputs, Materials.Grade6PurifiedWater.getFluid(32000L)))
                        .duration(recipeTime)
                        .addTo(RecipeMaps.laserEngraverRecipes),
                    builder.copy()
                        .fluidInputs(
                            ArrayUtils.addAll(builder.fluidInputs, Materials.Grade7PurifiedWater.getFluid(32000L)))
                        .duration(halfBoostedRecipeTime)
                        .addTo(RecipeMaps.laserEngraverRecipes),
                    builder.copy()
                        .fluidInputs(
                            ArrayUtils.addAll(builder.fluidInputs, Materials.Grade8PurifiedWater.getFluid(32000L)))
                        .duration(boostedRecipeTime)
                        .addTo(RecipeMaps.laserEngraverRecipes));
            }
        }

        throw new RuntimeException("Unreachable code reached in Laser Engraver Recipe Transformer");
    });

    /**
     * The one and only :tm: assline recipe adder. Uses {@link #RESEARCH_ITEM} metadata as research item, and
     * {@link #SCANNING} metadata as research time and voltage.
     */
    public static final IRecipeMap AssemblyLine = IRecipeMap.newRecipeMap(builder -> {
        Optional<GTRecipe.GTRecipe_WithAlt> rr = builder.forceOreDictInput()
            .validateInputCount(4, 16)
            .validateOutputCount(1, 1)
            .validateOutputFluidCount(-1, 0)
            .validateInputFluidCount(1, 4)
            .buildWithAlt();
        // noinspection SimplifyOptionalCallChains
        if (!rr.isPresent()) return Collections.emptyList();
        GTRecipe.GTRecipe_WithAlt r = rr.get();
        ItemStack[][] mOreDictAlt = r.mOreDictAlt;
        Object[] inputs = builder.getItemInputsOreDict();
        ItemStack aResearchItem = builder.getMetadata(RESEARCH_ITEM);
        if (aResearchItem == null) {
            return Collections.emptyList();
        }
        ItemStack aOutput = r.mOutputs[0];
        int tPersistentHash = 1;
        for (int i = 0, mOreDictAltLength = mOreDictAlt.length; i < mOreDictAltLength; i++) {
            ItemStack[] alts = mOreDictAlt[i];
            Object input = inputs[i];
            if (input == null) {
                GTLog.err.println(
                    "addAssemblingLineRecipe " + aResearchItem.getDisplayName()
                        + " --> "
                        + aOutput.getUnlocalizedName()
                        + " there is some null item in that recipe");
            }
            if (input instanceof ItemStack) {
                tPersistentHash = tPersistentHash * 31 + GTUtility.persistentHash((ItemStack) input, true, false);
            } else if (input instanceof ItemStack[]) {
                for (ItemStack alt : ((ItemStack[]) input)) {
                    tPersistentHash = tPersistentHash * 31 + GTUtility.persistentHash(alt, true, false);
                    if (alt == null) {
                        GTLog.err.println(
                            "addAssemblingLineRecipe " + aResearchItem.getDisplayName()
                                + " --> "
                                + aOutput.getUnlocalizedName()
                                + " there is some null alt item in that recipe");
                    }
                }
                tPersistentHash *= 31;
            } else if (input instanceof Object[]objs) {
                Arrays.sort(
                    alts,
                    Comparator
                        .<ItemStack, String>comparing(s -> GameRegistry.findUniqueIdentifierFor(s.getItem()).modId)
                        .thenComparing(s -> GameRegistry.findUniqueIdentifierFor(s.getItem()).name)
                        .thenComparingInt(Items.feather::getDamage)
                        .thenComparingInt(s -> s.stackSize));

                tPersistentHash = tPersistentHash * 31 + (objs[0] == null ? "" : objs[0].toString()).hashCode();
                tPersistentHash = tPersistentHash * 31 + ((Number) objs[1]).intValue();
            }
        }
        tPersistentHash = tPersistentHash * 31 + GTUtility.persistentHash(aResearchItem, true, false);
        tPersistentHash = tPersistentHash * 31 + GTUtility.persistentHash(aOutput, true, false);
        for (FluidStack fluidInput : r.mFluidInputs) {
            if (fluidInput == null) continue;
            tPersistentHash = tPersistentHash * 31 + GTUtility.persistentHash(fluidInput, true, false);
        }
        Scanning scanningData = builder.getMetadataOrDefault(SCANNING, new Scanning(0, 0));
        tPersistentHash = tPersistentHash * 31 + scanningData.time;
        tPersistentHash = tPersistentHash * 31 + (int) scanningData.voltage;
        tPersistentHash = tPersistentHash * 31 + r.mDuration;
        tPersistentHash = tPersistentHash * 31 + r.mEUt;

        GTRecipe.RecipeAssemblyLine tRecipe = new GTRecipe.RecipeAssemblyLine(
            aResearchItem,
            scanningData.time,
            (int) scanningData.voltage,
            r.mInputs,
            r.mFluidInputs,
            aOutput,
            r.mDuration,
            r.mEUt,
            r.mOreDictAlt);
        tRecipe.setPersistentHash(tPersistentHash);
        GTRecipe.RecipeAssemblyLine.sAssemblylineRecipes.add(tRecipe);

        Collection<GTRecipe> ret = new ArrayList<>(3);
        ret.addAll(
            GTValues.RA.stdBuilder()
                .itemInputs(aResearchItem)
                .itemOutputs(aOutput)
                .special(tRecipe.newDataStickForNEI("Writes Research result", 1))
                .duration(scanningData.time)
                .eut(scanningData.voltage)
                .specialValue(-201) // means it's scanned
                .ignoreCollision()
                .fake()
                .addTo(scannerFakeRecipes));

        ret.add(
            RecipeMaps.assemblylineVisualRecipes.addFakeRecipe(
                false,
                r.mInputs,
                new ItemStack[] { aOutput },
                new ItemStack[] { tRecipe.newDataStickForNEI("Reads Research result", 0) },
                r.mFluidInputs,
                null,
                r.mDuration,
                r.mEUt,
                0,
                r.mOreDictAlt,
                false));

        return ret;
    });

    /**
     * Add Electric Blast Furnace recipes that use gasses to reduce recipe time. Keep circuit config.
     * <p>
     * Use {@link GTRecipeConstants#COIL_HEAT} as heat level. Use {@link #ADDITIVE_AMOUNT} metadata as base gas consumed
     * amount, and {@link #NO_GAS} metadata to generate recipe that is without gas. Recipe time will be 1.25x without
     * gas.<br>
     * Use {@link #NO_GAS_CIRCUIT_CONFIG} metadata as circuit config used in non-gas recipe if {@link #NO_GAS} is set to
     * true.
     */
    public static final IRecipeMap BlastFurnaceWithGas = IRecipeMap.newRecipeMap(builder -> {
        Collection<GTRecipe> ret = new ArrayList<>();
        int baseGasAmount = builder.getMetadataOrDefault(ADDITIVE_AMOUNT, 1000);
        double baseDuration = builder.getDuration();
        ArrayList<ItemStack> items = new ArrayList<>(Arrays.asList(builder.getItemInputsBasic()));

        // Generate recipe with gas
        for (BlastFurnaceGasStat gasStat : BlastFurnaceGasStat.BlastFurnaceGasStats) {
            int gasAmount = (int) (gasStat.recipeConsumedAmountMultiplier * baseGasAmount);
            int duration = (int) Math.max(gasStat.recipeTimeMultiplier * baseDuration, 1);
            ret.addAll(
                builder.copy()
                    .itemInputs(items.toArray(new ItemStack[0]))
                    .fluidInputs(GTUtility.copyAmount(gasAmount, gasStat.gas))
                    .duration(duration)
                    .addTo(RecipeMaps.blastFurnaceRecipes));
        }

        // Generate recipe without gas
        if (builder.getMetadataOrDefault(NO_GAS, false)) {
            int circuitConfig = builder.getMetadataOrDefault(NO_GAS_CIRCUIT_CONFIG, -1);
            for (int i = 0; i < items.size(); i++) {
                if (GTUtility.isAnyIntegratedCircuit(items.get(i))) {
                    items.remove(i);
                    break;
                }
            }
            if (circuitConfig == -1) {
                ret.addAll(
                    builder.copy()
                        .itemInputs(items.toArray(new ItemStack[0]))
                        .fluidInputs()
                        .duration((int) Math.max(baseDuration * 1.25, 1))
                        .addTo(RecipeMaps.blastFurnaceRecipes));
            } else {
                ret.addAll(
                    builder.copy()
                        .itemInputs(items.toArray(new ItemStack[0]))
                        .circuit(circuitConfig)
                        .fluidInputs()
                        .duration((int) Math.max(baseDuration * 1.25, 1))
                        .addTo(RecipeMaps.blastFurnaceRecipes));
            }
        }
        return ret;
    });

    /**
     * Just like any normal assembler recipe, however it accepts one input item to be oredicted. Pass in the item to
     * oredict via {@link #OREDICT_INPUT}. It will be used along all other item inputs as input of this recipe.
     */
    public static IRecipeMap AssemblerOD = IRecipeMap.newRecipeMap(builder -> {
        Collection<GTRecipe> ret = new ArrayList<>();
        for (ItemStack input : GTOreDictUnificator.getOresImmutable(builder.getMetadata(OREDICT_INPUT))) {
            ret.addAll(
                builder.copy()
                    .itemInputs(GTRecipeMapUtil.appendArray(builder.getItemInputsBasic(), input))
                    .addTo(RecipeMaps.assemblerRecipes));
        }
        return ret;
    });

    /**
     * A universal fuel adder. It's actually just a dispatcher towards all actual fuel recipe maps. Dispatch based on
     * {@link #FUEL_TYPE}. Uses {@link #FUEL_VALUE} as fuel value. Can use {@link FuelType#ordinal()} as a
     * human-readable form of what FUEL_TYPE should be. You can bypass this and add to relevant fuel maps directly if
     * you wish.
     */
    public static IRecipeMap Fuel = IRecipeMap.newRecipeMap(builder -> {
        builder.validateInputCount(1, 1)
            .validateNoInputFluid()
            .validateOutputCount(-1, 1)
            .validateNoOutputFluid();
        if (!builder.isValid()) return Collections.emptyList();
        Integer fuelType = builder.getMetadata(FUEL_TYPE);
        if (fuelType == null) return Collections.emptyList();
        builder.metadata(FUEL_VALUE, builder.getMetadataOrDefault(FUEL_VALUE, 0));
        return FuelType.get(fuelType)
            .getTarget()
            .doAdd(builder);
    });

    public enum FuelType {

        // ORDER MATTERS. DO NOT INSERT ELEMENT BETWEEN EXISTING ONES
        DieselFuel(RecipeMaps.dieselFuels),
        GasTurbine(RecipeMaps.gasTurbineFuels),
        // appears unused
        HotFuel(RecipeMaps.hotFuels),
        SemiFluid(RecipeMaps.denseLiquidFuels),
        PlasmaTurbine(RecipeMaps.plasmaFuels),
        Magic(RecipeMaps.magicFuels),;

        private static final FuelType[] VALUES = values();
        private final IRecipeMap target;

        FuelType(IRecipeMap target) {
            this.target = target;
        }

        public static FuelType get(int fuelType) {
            if (fuelType < 0 || fuelType >= VALUES.length) return SemiFluid;
            return VALUES[fuelType];
        }

        public IRecipeMap getTarget() {
            return target;
        }
    }

    static {
        GTRecipeMapUtil.SPECIAL_VALUE_ALIASES.add(COIL_HEAT);
        GTRecipeMapUtil.SPECIAL_VALUE_ALIASES.add(FUEL_VALUE);
        GTRecipeMapUtil.SPECIAL_VALUE_ALIASES.add(NANO_FORGE_TIER);
        GTRecipeMapUtil.SPECIAL_VALUE_ALIASES.add(FOG_EXOTIC_TIER);
        GTRecipeMapUtil.SPECIAL_VALUE_ALIASES.add(FOG_PLASMA_TIER);
        GTRecipeMapUtil.SPECIAL_VALUE_ALIASES.add(DEFC_CASING_TIER);
        GTRecipeMapUtil.SPECIAL_VALUE_ALIASES.add(CHEMPLANT_CASING_TIER);
        GTRecipeMapUtil.SPECIAL_VALUE_ALIASES.add(ALGAE_POND_TIER);
        GTRecipeMapUtil.SPECIAL_VALUE_ALIASES.add(QFT_FOCUS_TIER);
        GTRecipeMapUtil.SPECIAL_VALUE_ALIASES.add(DISSOLUTION_TANK_RATIO);
        GTRecipeMapUtil.SPECIAL_VALUE_ALIASES.add(RTG_DURATION_IN_DAYS);
        GTRecipeMapUtil.SPECIAL_VALUE_ALIASES.add(LNG_BASIC_OUTPUT);
        GTRecipeMapUtil.SPECIAL_VALUE_ALIASES.add(NFR_COIL_TIER);
        GTRecipeMapUtil.SPECIAL_VALUE_ALIASES.add(NKE_RANGE);
        GTRecipeMapUtil.SPECIAL_VALUE_ALIASES.add(PRECISE_ASSEMBLER_CASING_TIER);
        GTRecipeMapUtil.SPECIAL_VALUE_ALIASES.add(COAL_CASING_TIER);
        GTRecipeMapUtil.SPECIAL_VALUE_ALIASES.add(RESEARCH_STATION_DATA);
    }
}
