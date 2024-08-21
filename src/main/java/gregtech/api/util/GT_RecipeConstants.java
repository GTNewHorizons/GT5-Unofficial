package gregtech.api.util;

import static gregtech.api.recipe.RecipeMaps.scannerFakeRecipes;
import static gregtech.api.util.GT_RecipeMapUtil.convertCellToFluid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.elisis.gtnhlanth.common.item.MaskList;
import com.elisis.gtnhlanth.common.item.PhotolithographicMask;
import com.elisis.gtnhlanth.common.register.LanthItemList;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.RecipeCategories;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.api.recipe.metadata.SimpleRecipeMetadataKey;
import gregtech.common.items.GT_MetaGenerated_Item_03;
import gregtech.common.items.ID_MetaItem_03;

// this class is intended to be import-static-ed on every recipe script
// so take care to not put unrelated stuff here!
public class GT_RecipeConstants {

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
    public static final RecipeMetadataKey<Integer> FUSION_THRESHOLD = SimpleRecipeMetadataKey
        .create(Integer.class, "fusion_threshold");
    /**
     * Research time in a scanner used in ticks.
     */
    public static final RecipeMetadataKey<Integer> RESEARCH_TIME = SimpleRecipeMetadataKey
        .create(Integer.class, "research_time");
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
     * Nano Forge Tier.
     */
    public static final RecipeMetadataKey<Integer> NANO_FORGE_TIER = SimpleRecipeMetadataKey
        .create(Integer.class, "nano_forge_tier");

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
     * QFT Focus tier.
     */
    public static final RecipeMetadataKey<Integer> QFT_FOCUS_TIER = SimpleRecipeMetadataKey
        .create(Integer.class, "qft_focus_tier");

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
     * Research Station data.
     */
    public static final RecipeMetadataKey<Integer> RESEARCH_STATION_DATA = SimpleRecipeMetadataKey
        .create(Integer.class, "research_station_data");

    /**
     * glass tier required for the biovat recipes.
     */
    public static final RecipeMetadataKey<Integer> SIEVERTS = SimpleRecipeMetadataKey.create(Integer.class, "sieverts");

    public static final RecipeMetadataKey<Integer> DECAY_TICKS = SimpleRecipeMetadataKey
        .create(Integer.class, "decay_ticks");

    public static final RecipeMetadataKey<Boolean> NOBLE_GASES = SimpleRecipeMetadataKey
        .create(Boolean.class, "noble_gases");

    public static final RecipeMetadataKey<Boolean> ANAEROBE_GASES = SimpleRecipeMetadataKey
        .create(Boolean.class, "anaerobe_gases");

    public static final RecipeMetadataKey<Boolean> NO_GAS = SimpleRecipeMetadataKey.create(Boolean.class, "no_gas");

    /**
     * Add a arc furnace recipe. Adds to both normal arc furnace and plasma arc furnace.
     * Will override the fluid input with oxygen/plasma for the respective recipe maps, so there is no point setting it.
     */
    public static final IRecipeMap UniversalArcFurnace = IRecipeMap.newRecipeMap(builder -> {
        if (!GT_Utility.isArrayOfLength(builder.getItemInputsBasic(), 1)
            || GT_Utility.isArrayEmptyOrNull(builder.getItemOutputs())) return Collections.emptyList();
        int aDuration = builder.getDuration();
        if (aDuration <= 0) {
            return Collections.emptyList();
        }
        builder.duration(aDuration);
        boolean recycle = builder.getMetadataOrDefault(RECYCLE, false);
        Collection<GT_Recipe> ret = new ArrayList<>();
        for (Materials mat : new Materials[] { Materials.Argon, Materials.Nitrogen }) {
            int tPlasmaAmount = (int) Math.max(1L, aDuration / (mat.getMass() * 16L));
            GT_RecipeBuilder plasmaBuilder = builder.copy()
                .fluidInputs(mat.getPlasma(tPlasmaAmount))
                .fluidOutputs(mat.getGas(tPlasmaAmount));
            if (recycle) {
                plasmaBuilder.recipeCategory(RecipeCategories.plasmaArcFurnaceRecycling);
            }
            ret.addAll(RecipeMaps.plasmaArcFurnaceRecipes.doAdd(plasmaBuilder));
        }
        GT_RecipeBuilder arcBuilder = builder.copy()
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
            if (GT_Utility.isAnyIntegratedCircuit(input) && input.getItemDamage() >= 10) {
                return builder.addTo(RecipeMaps.chemicalReactorRecipes);
            }
        }
        return GT_Utility.concat(
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
        PhotolithographicMask t1Item = (PhotolithographicMask) LanthItemList.maskMap.get(MaskList.BLANK1);
        PhotolithographicMask t2Item = (PhotolithographicMask) LanthItemList.maskMap.get(MaskList.BLANK2);
        PhotolithographicMask t3Item = (PhotolithographicMask) LanthItemList.maskMap.get(MaskList.BLANK3);
        for (ItemStack input : builder.getItemInputsBasic()) {
            if (input.getItem() instanceof GT_MetaGenerated_Item_03) {
                int meta = input.getItemDamage() - 32000;
                // Check if this input item is indicating a wafer recipe we want to modify
                if (meta == ID_MetaItem_03.Circuit_Silicon_Wafer3.ID) wafer = Wafer.Naquadah;
                else if (meta == ID_MetaItem_03.Circuit_Silicon_Wafer4.ID) wafer = Wafer.Europium;
                else if (meta == ID_MetaItem_03.Circuit_Silicon_Wafer5.ID) wafer = Wafer.Americium;
            }

            // Now look for beamline masks
            if (input.getItem() instanceof PhotolithographicMask mask) {
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
                return GT_Utility.concat(
                    builder.copy()
                        .itemInputs(itemInputs)
                        .fluidInputs(GT_ModHandler.getDistilledWater(100L))
                        .addTo(RecipeMaps.laserEngraverRecipes),
                    builder.copy()
                        .itemInputs(itemInputs)
                        .fluidInputs(Materials.Grade1PurifiedWater.getFluid(100L))
                        .duration(halfBoostedRecipeTime)
                        .addTo(RecipeMaps.laserEngraverRecipes),
                    builder.copy()
                        .itemInputs(itemInputs)
                        .fluidInputs(Materials.Grade2PurifiedWater.getFluid(100L))
                        .duration(boostedRecipeTime)
                        .addTo(RecipeMaps.laserEngraverRecipes));
            }
            case Europium -> {
                // Require purified water for europium wafers, at least grade 3
                return GT_Utility.concat(
                    builder.copy()
                        .fluidInputs(Materials.Grade3PurifiedWater.getFluid(100L))
                        .duration(recipeTime)
                        .addTo(RecipeMaps.laserEngraverRecipes),
                    builder.copy()
                        .fluidInputs(Materials.Grade4PurifiedWater.getFluid(100L))
                        .duration(boostedRecipeTime)
                        .addTo(RecipeMaps.laserEngraverRecipes));
            }
            case Americium -> {
                // Require purified water for americium wafers, at least grade 5
                return GT_Utility.concat(
                    builder.copy()
                        .fluidInputs(Materials.Grade5PurifiedWater.getFluid(100L))
                        .duration(recipeTime)
                        .addTo(RecipeMaps.laserEngraverRecipes),
                    builder.copy()
                        .fluidInputs(Materials.Grade6PurifiedWater.getFluid(100L))
                        .duration(boostedRecipeTime)
                        .addTo(RecipeMaps.laserEngraverRecipes));
            }
            // Masks require much more purified water because they can make many wafers at once
            case MaskT1 -> {
                // T1 masks require grade 1, 2 or 3 purified water
                return GT_Utility.concat(
                    builder.copy()
                        .fluidInputs(Materials.Grade1PurifiedWater.getFluid(32000L))
                        .duration(recipeTime)
                        .addTo(RecipeMaps.laserEngraverRecipes),
                    builder.copy()
                        .fluidInputs(Materials.Grade2PurifiedWater.getFluid(32000L))
                        .duration(halfBoostedRecipeTime)
                        .addTo(RecipeMaps.laserEngraverRecipes),
                    builder.copy()
                        .fluidInputs(Materials.Grade3PurifiedWater.getFluid(32000L))
                        .duration(boostedRecipeTime)
                        .addTo(RecipeMaps.laserEngraverRecipes));
            }
            case MaskT2 -> {
                // T2 masks require grade 4 or 5 purified water
                return GT_Utility.concat(
                    builder.copy()
                        .fluidInputs(Materials.Grade4PurifiedWater.getFluid(32000L))
                        .duration(recipeTime)
                        .addTo(RecipeMaps.laserEngraverRecipes),
                    builder.copy()
                        .fluidInputs(Materials.Grade5PurifiedWater.getFluid(32000L))
                        .duration(boostedRecipeTime)
                        .addTo(RecipeMaps.laserEngraverRecipes));
            }
            case MaskT3 -> {
                // T3 masks require grade 6, 7 or 8 purified water
                return GT_Utility.concat(
                    builder.copy()
                        .fluidInputs(Materials.Grade6PurifiedWater.getFluid(32000L))
                        .duration(recipeTime)
                        .addTo(RecipeMaps.laserEngraverRecipes),
                    builder.copy()
                        .fluidInputs(Materials.Grade7PurifiedWater.getFluid(32000L))
                        .duration(halfBoostedRecipeTime)
                        .addTo(RecipeMaps.laserEngraverRecipes),
                    builder.copy()
                        .fluidInputs(Materials.Grade8PurifiedWater.getFluid(32000L))
                        .duration(boostedRecipeTime)
                        .addTo(RecipeMaps.laserEngraverRecipes));
            }
        }

        throw new RuntimeException("Unreachable code reached in Laser Engraver Recipe Transformer");
    });

    /**
     * The one and only :tm: assline recipe adder.
     * Uses {@link #RESEARCH_ITEM} metadata as research item, and {@link #RESEARCH_TIME} metadata as research time, unit
     * in ticks.
     */
    public static final IRecipeMap AssemblyLine = IRecipeMap.newRecipeMap(builder -> {
        Optional<GT_Recipe.GT_Recipe_WithAlt> rr = builder.forceOreDictInput()
            .validateInputCount(4, 16)
            .validateOutputCount(1, 1)
            .validateOutputFluidCount(-1, 0)
            .validateInputFluidCount(1, 4)
            .buildWithAlt();
        // noinspection SimplifyOptionalCallChains
        if (!rr.isPresent()) return Collections.emptyList();
        GT_Recipe.GT_Recipe_WithAlt r = rr.get();
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
                GT_Log.err.println(
                    "addAssemblingLineRecipe " + aResearchItem.getDisplayName()
                        + " --> "
                        + aOutput.getUnlocalizedName()
                        + " there is some null item in that recipe");
            }
            if (input instanceof ItemStack) {
                tPersistentHash = tPersistentHash * 31 + GT_Utility.persistentHash((ItemStack) input, true, false);
            } else if (input instanceof ItemStack[]) {
                for (ItemStack alt : ((ItemStack[]) input)) {
                    tPersistentHash = tPersistentHash * 31 + GT_Utility.persistentHash(alt, true, false);
                    if (alt == null) {
                        GT_Log.err.println(
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
        tPersistentHash = tPersistentHash * 31 + GT_Utility.persistentHash(aResearchItem, true, false);
        tPersistentHash = tPersistentHash * 31 + GT_Utility.persistentHash(aOutput, true, false);
        for (FluidStack fluidInput : r.mFluidInputs) {
            if (fluidInput == null) continue;
            tPersistentHash = tPersistentHash * 31 + GT_Utility.persistentHash(fluidInput, true, false);
        }
        int aResearchTime = builder.getMetadataOrDefault(RESEARCH_TIME, 0);
        tPersistentHash = tPersistentHash * 31 + aResearchTime;
        tPersistentHash = tPersistentHash * 31 + r.mDuration;
        tPersistentHash = tPersistentHash * 31 + r.mEUt;

        GT_Recipe.GT_Recipe_AssemblyLine tRecipe = new GT_Recipe.GT_Recipe_AssemblyLine(
            aResearchItem,
            aResearchTime,
            r.mInputs,
            r.mFluidInputs,
            aOutput,
            r.mDuration,
            r.mEUt,
            r.mOreDictAlt);
        tRecipe.setPersistentHash(tPersistentHash);
        GT_Recipe.GT_Recipe_AssemblyLine.sAssemblylineRecipes.add(tRecipe);
        GT_AssemblyLineUtils.addRecipeToCache(tRecipe);

        ItemStack writesDataStick = ItemList.Tool_DataStick.getWithName(1L, "Writes Research result");
        GT_AssemblyLineUtils.setAssemblyLineRecipeOnDataStick(writesDataStick, tRecipe, false);
        Collection<GT_Recipe> ret = new ArrayList<>(3);
        ret.addAll(
            GT_Values.RA.stdBuilder()
                .itemInputs(aResearchItem)
                .itemOutputs(aOutput)
                .special(writesDataStick)
                .duration(aResearchTime)
                .eut(TierEU.RECIPE_LV)
                .specialValue(-201) // means it's scanned
                .noOptimize()
                .ignoreCollision()
                .fake()
                .addTo(scannerFakeRecipes));

        ItemStack readsDataStick = ItemList.Tool_DataStick.getWithName(1L, "Reads Research result");
        GT_AssemblyLineUtils.setAssemblyLineRecipeOnDataStick(readsDataStick, tRecipe, false);
        ret.add(
            RecipeMaps.assemblylineVisualRecipes.addFakeRecipe(
                false,
                r.mInputs,
                new ItemStack[] { aOutput },
                new ItemStack[] { readsDataStick },
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
     * Adds an Electric Blast Furnace recipe that might use gas.
     */
    public static final IRecipeMap BlastFurnaceWithGas = IRecipeMap.newRecipeMap(builder -> {
        Collection<GT_Recipe> ret = new ArrayList<>();
        int basicGasAmount = builder.getMetadataOrDefault(ADDITIVE_AMOUNT, 1000);
        double durationBase = builder.getDuration();
        ArrayList<ItemStack> items = new ArrayList<>(Arrays.asList(builder.getItemInputsBasic()));
        int circuitConfig = 1;
        if (items.size() == 1) {// Set circuit config if it is a dust -> ingot recipe.
            ItemData data = GT_OreDictUnificator.getAssociation(items.get(0));
            if (data != null) {
                OrePrefixes prefix = data.mPrefix;
                if (OrePrefixes.dust.equals(prefix)) {
                    circuitConfig = 1;
                } else if (OrePrefixes.dustSmall.equals(prefix)) {
                    circuitConfig = 4;
                } else if (OrePrefixes.dustTiny.equals(prefix)) {
                    circuitConfig = 9;
                }
            }
        } else { // Set circuit config if there is an integrated circuit
            for (int i = 0; i < items.size(); i++) {
                if (GT_Utility.isAnyIntegratedCircuit(items.get(i))) {
                    circuitConfig = items.get(i)
                        .getItemDamage();
                    items.remove(i--);
                }
            }
        }

        if (builder.getMetadataOrDefault(NO_GAS, false)) {
            items.add(GT_Utility.getIntegratedCircuit(circuitConfig));
            ret.addAll(
                builder.copy()
                    .itemInputs(items.toArray(new ItemStack[0]))
                    .fluidInputs()
                    .duration((int) Math.max(durationBase * 1.1, 1))
                    .addTo(RecipeMaps.blastFurnaceRecipes));
            items.remove(items.size() - 1);
            circuitConfig += 10;
        }

        items.add(GT_Utility.getIntegratedCircuit(circuitConfig));
        boolean nobleGases = builder.getMetadataOrDefault(NOBLE_GASES, false);
        boolean anaerobeGases = builder.getMetadataOrDefault(ANAEROBE_GASES, false);
        Collection<BlastFurnaceGasStat> gases = new ArrayList<>();

        if (nobleGases && anaerobeGases) {
            gases = BlastFurnaceGasStat.getNobleAndAnaerobeGases();
        } else if (nobleGases) {
            gases = BlastFurnaceGasStat.getNobleGases();
        } else if (anaerobeGases) {
            gases = BlastFurnaceGasStat.getAnaerobeGases();
        }
        for (BlastFurnaceGasStat gas : gases) {
            int gasAmount = (int) (gas.recipeConsumedAmountMultiplier * basicGasAmount);
            int duration = (int) Math.max(gas.recipeTimeMultiplier * durationBase, 1);
            ret.addAll(
                builder.copy()
                    .itemInputs(items.toArray(new ItemStack[0]))
                    .fluidInputs(GT_Utility.copyAmount(gasAmount, gas.gas))
                    .duration(duration)
                    .addTo(RecipeMaps.blastFurnaceRecipes));
        }
        return ret;
    });

    /**
     * Just like any normal assembler recipe, however it accepts one input item to be oredicted. Pass in the item to
     * oredict via {@link #OREDICT_INPUT}. It will be used along all other item inputs as input of this recipe.
     */
    public static IRecipeMap AssemblerOD = IRecipeMap.newRecipeMap(builder -> {
        Collection<GT_Recipe> ret = new ArrayList<>();
        for (ItemStack input : GT_OreDictUnificator.getOresImmutable(builder.getMetadata(OREDICT_INPUT))) {
            ret.addAll(
                builder.copy()
                    .itemInputs(GT_RecipeMapUtil.appendArray(builder.getItemInputsBasic(), input))
                    .addTo(RecipeMaps.assemblerRecipes));
        }
        return ret;
    });

    /**
     * A universal fuel adder. It's actually just a dispatcher towards all actual fuel recipe maps.
     * Dispatch based on {@link #FUEL_TYPE}. Uses {@link #FUEL_VALUE} as fuel value.
     * Can use {@link FuelType#ordinal()} as a human-readable form of what FUEL_TYPE should be.
     * You can bypass this and add to relevant fuel maps directly if you wish.
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
        GT_RecipeMapUtil.SPECIAL_VALUE_ALIASES.add(COIL_HEAT);
        GT_RecipeMapUtil.SPECIAL_VALUE_ALIASES.add(FUSION_THRESHOLD);
        GT_RecipeMapUtil.SPECIAL_VALUE_ALIASES.add(FUEL_VALUE);
        GT_RecipeMapUtil.SPECIAL_VALUE_ALIASES.add(NANO_FORGE_TIER);
        GT_RecipeMapUtil.SPECIAL_VALUE_ALIASES.add(FOG_EXOTIC_TIER);
        GT_RecipeMapUtil.SPECIAL_VALUE_ALIASES.add(FOG_PLASMA_TIER);
        GT_RecipeMapUtil.SPECIAL_VALUE_ALIASES.add(DEFC_CASING_TIER);
        GT_RecipeMapUtil.SPECIAL_VALUE_ALIASES.add(CHEMPLANT_CASING_TIER);
        GT_RecipeMapUtil.SPECIAL_VALUE_ALIASES.add(QFT_FOCUS_TIER);
        GT_RecipeMapUtil.SPECIAL_VALUE_ALIASES.add(DISSOLUTION_TANK_RATIO);
        GT_RecipeMapUtil.SPECIAL_VALUE_ALIASES.add(RTG_DURATION_IN_DAYS);
        GT_RecipeMapUtil.SPECIAL_VALUE_ALIASES.add(LNG_BASIC_OUTPUT);
        GT_RecipeMapUtil.SPECIAL_VALUE_ALIASES.add(NFR_COIL_TIER);
        GT_RecipeMapUtil.SPECIAL_VALUE_ALIASES.add(NKE_RANGE);
        GT_RecipeMapUtil.SPECIAL_VALUE_ALIASES.add(PRECISE_ASSEMBLER_CASING_TIER);
        GT_RecipeMapUtil.SPECIAL_VALUE_ALIASES.add(COAL_CASING_TIER);
        GT_RecipeMapUtil.SPECIAL_VALUE_ALIASES.add(RESEARCH_STATION_DATA);
        GT_RecipeMapUtil.SPECIAL_VALUE_ALIASES.add(SIEVERTS);
        GT_RecipeMapUtil.SPECIAL_VALUE_ALIASES.add(DECAY_TICKS);

    }
}
