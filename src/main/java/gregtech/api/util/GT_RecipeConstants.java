package gregtech.api.util;

import java.util.*;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IGT_RecipeMap;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;

// this class is intended to be import-static-ed on every recipe script
// so take care to not put unrelated stuff here!
public class GT_RecipeConstants {

    /**
     * Set to true to signal the recipe require low gravity. do nothing if recipe set specialValue explicitly. Can
     * coexist with CLEANROOM just fine
     */
    public static final GT_RecipeBuilder.MetadataIdentifier<Boolean> LOW_GRAVITY = GT_RecipeBuilder.MetadataIdentifier.create(
            Boolean.class,
            "low_gravity");
    /**
     * Set to true to signal the recipe require cleanroom. do nothing if recipe set specialValue explicitly. Can coexist
     * with LOW_GRAVITY just fine
     */
    public static final GT_RecipeBuilder.MetadataIdentifier<Boolean> CLEANROOM = GT_RecipeBuilder.MetadataIdentifier.create(
            Boolean.class,
            "cleanroom");
    /**
     * Common additive to use in recipe, e.g. for PBF, this is coal amount.
     */
    public static final GT_RecipeBuilder.MetadataIdentifier<Integer> ADDITIVE_AMOUNT = GT_RecipeBuilder.MetadataIdentifier.create(
            Integer.class,
            "additives");
    /**
     * Used for fusion reactor. Denotes ignition threshold.
     */
    public static final GT_RecipeBuilder.MetadataIdentifier<Integer> FUSION_THRESHOLD = GT_RecipeBuilder.MetadataIdentifier.create(
            Integer.class,
            "fusion_threshold");
    /**
     * Research time in a scanner used in ticks.
     */
    public static final GT_RecipeBuilder.MetadataIdentifier<Integer> RESEARCH_TIME = GT_RecipeBuilder.MetadataIdentifier.create(
            Integer.class,
            "research_time");
    /**
     * Fuel type. TODO should we use enum directly?
     */
    public static final GT_RecipeBuilder.MetadataIdentifier<Integer> FUEL_TYPE = GT_RecipeBuilder.MetadataIdentifier.create(
            Integer.class,
            "fuel_type");
    /**
     * Fuel value.
     */
    public static final GT_RecipeBuilder.MetadataIdentifier<Integer> FUEL_VALUE = GT_RecipeBuilder.MetadataIdentifier.create(
            Integer.class,
            "fuel_value");
    /**
     * Fuel value.
     */
    public static final GT_RecipeBuilder.MetadataIdentifier<Integer> COIL_HEAT = GT_RecipeBuilder.MetadataIdentifier.create(
            Integer.class,
            "coil_heat");
    /**
     * Research item used by assline recipes.
     */
    public static final GT_RecipeBuilder.MetadataIdentifier<ItemStack> RESEARCH_ITEM = GT_RecipeBuilder.MetadataIdentifier.create(
            ItemStack.class,
            "research_item");
    /**
     * For assembler. It accepts a single item as oredict. It looks like no one uses this anyway...
     */
    public static final GT_RecipeBuilder.MetadataIdentifier<Object> OREDICT_INPUT = GT_RecipeBuilder.MetadataIdentifier.create(
            Object.class,
            "oredict_input");

    /**
     * Add fusion recipes. Dispatcher between complex fusion (which accepts arbitrarily many input/outputs) and classic
     * fusion (2 in 1 out).
     */
    public static final IGT_RecipeMap Fusion = IGT_RecipeMap.newRecipeMap(builder -> {
        if (GT_Utility.isArrayEmptyOrNull(builder.getFluidInputs())
                || GT_Utility.isArrayEmptyOrNull(builder.getFluidOutputs()))
            return Collections.emptyList();
        if (builder.getFluidInputs().length > 2 || builder.getFluidOutputs().length > 2)
            return GT_Recipe_Map.sComplexFusionRecipes.doAdd(builder);
        return GT_Recipe_Map.sFusionRecipes.doAdd(builder);
    });

    /**
     * Add a arc furnace recipe. Adds to both normal arc furnace and plasma arc furnace.
     * Will override the fluid input with oxygen/plasma for the respective recipe maps, so there is no point setting it.
     */
    public static final IGT_RecipeMap UniversalArcFurnace = IGT_RecipeMap.newRecipeMap(builder -> {
        if (!GT_Utility.isArrayOfLength(builder.getItemInputsBasic(), 1)
                || GT_Utility.isArrayEmptyOrNull(builder.getItemOutputs()))
            return Collections.emptyList();
        int aDuration = builder.getDuration();
        if ((aDuration = GregTech_API.sRecipeFile.get("arcfurnace", builder.getItemInputBasic(0), aDuration)) <= 0) {
            return Collections.emptyList();
        }
        builder.duration(aDuration);
        Collection<GT_Recipe> ret = new ArrayList<>();
        for (Materials mat : new Materials[] { Materials.Argon, Materials.Nitrogen }) {
            int tPlasmaAmount = (int) Math.max(1L, aDuration / (mat.getMass() * 16L));
            GT_RecipeBuilder b2 = builder.copy();
            b2.fluidInputs(mat.getPlasma(tPlasmaAmount))
              .fluidOutputs(mat.getGas(tPlasmaAmount));
            ret.addAll(GT_Recipe_Map.sPlasmaArcFurnaceRecipes.doAdd(b2));
        }
        ret.addAll(
                GT_Recipe_Map.sArcFurnaceRecipes.doAdd(
                        builder.copy()
                               .fluidInputs(Materials.Oxygen.getGas(aDuration))));
        return ret;
    });

    /**
     * Add a chemical reactor recipe to both LCR and singleblocks.
     */
    public static final IGT_RecipeMap UniversalChemical = IGT_RecipeMap.newRecipeMap(builder -> {
        for (ItemStack input : builder.getItemInputsBasic()) {
            // config >= 10 -> this is a special chemical recipe that output fluid/canned fluid variant.
            // it doesn't belong to multiblocks
            if (GT_Utility.isAnyIntegratedCircuit(input) && input.getItemDamage() >= 10) {
                return builder.addTo(GT_Recipe_Map.sChemicalRecipes);
            }
        }
        return GT_Utility.concat(
                builder.copy()
                       .addTo(GT_Recipe_Map.sChemicalRecipes),
                // LCR does not need cleanroom, for now.
                builder.metadata(CLEANROOM, false)
                       .addTo(GT_Recipe_Map.sMultiblockChemicalRecipes));
    });

    /**
     * The one and only :tm: assline recipe adder.
     * Uses {@link #RESEARCH_ITEM} metadata as research item, and {@link #RESEARCH_TIME} metadata as research time, unit
     * in ticks.
     */
    public static final IGT_RecipeMap AssemblyLine = IGT_RecipeMap.newRecipeMap(builder -> {
        Optional<GT_Recipe.GT_Recipe_WithAlt> rr = builder.forceOreDictInput()
                                                          .validateInputCount(4, 16)
                                                          .validateOutputCount(1, 1)
                                                          .validateOutputFluidCount(-1, 0)
                                                          .validateInputFluidCount(0, 4)
                                                          .buildWithAlt();
        if (!rr.isPresent()) return Collections.emptyList();
        GT_Recipe.GT_Recipe_WithAlt r = rr.get();
        ItemStack[][] mOreDictAlt = r.mOreDictAlt;
        Object[] inputs = builder.getItemInputsOreDict();
        ItemStack aResearchItem = builder.getMetadata(RESEARCH_ITEM);
        ItemStack aOutput = r.mOutputs[0];
        int tPersistentHash = 1;
        for (int i = 0, mOreDictAltLength = mOreDictAlt.length; i < mOreDictAltLength; i++) {
            ItemStack[] alts = mOreDictAlt[i];
            Object input = inputs[i];
            if (input instanceof ItemStack)
                tPersistentHash = tPersistentHash * 31 + GT_Utility.persistentHash((ItemStack) input, true, false);
            else if (input instanceof ItemStack[]) {
                for (ItemStack alt : ((ItemStack[]) input)) {
                    tPersistentHash = tPersistentHash * 31 + GT_Utility.persistentHash(alt, true, false);
                }
                tPersistentHash *= 31;
            } else if (input instanceof Object[]objs) {
                Arrays.sort(
                        alts,
                        Comparator.<ItemStack, String>comparing(
                                s -> GameRegistry.findUniqueIdentifierFor(s.getItem()).modId)
                                  .thenComparing(s -> GameRegistry.findUniqueIdentifierFor(s.getItem()).name)
                                  .thenComparingInt(Items.feather::getDamage)
                                  .thenComparingInt(s -> s.stackSize));

                tPersistentHash = tPersistentHash * 31 + (objs[0] == null ? "" : objs[0].toString()).hashCode();
                tPersistentHash = tPersistentHash * 31 + ((Number) objs[1]).intValue();
            }
            GT_Log.err.println(
                    "addAssemblingLineRecipe " + aResearchItem.getDisplayName()
                            + " --> "
                            + aOutput.getUnlocalizedName()
                            + " there is some null item in that recipe");
        }
        tPersistentHash = tPersistentHash * 31 + GT_Utility.persistentHash(aResearchItem, true, false);
        tPersistentHash = tPersistentHash * 31 + GT_Utility.persistentHash(aOutput, true, false);
        for (FluidStack fluidInput : r.mFluidInputs) {
            if (fluidInput == null) continue;
            tPersistentHash = tPersistentHash * 31 + GT_Utility.persistentHash(fluidInput, true, false);
        }
        int aResearchTime = builder.getMetadata(RESEARCH_TIME);
        tPersistentHash = tPersistentHash * 31 + aResearchTime;
        tPersistentHash = tPersistentHash * 31 + r.mDuration;
        tPersistentHash = tPersistentHash * 31 + r.mEUt;
        Collection<GT_Recipe> ret = new ArrayList<>(3);
        ret.add(
                GT_Recipe.GT_Recipe_Map.sScannerFakeRecipes.addFakeRecipe(
                        false,
                        new ItemStack[] { aResearchItem },
                        new ItemStack[] { aOutput },
                        new ItemStack[] { ItemList.Tool_DataStick.getWithName(1L, "Writes Research result") },
                        null,
                        null,
                        aResearchTime,
                        30,
                        -201)); // means it's scanned

        ret.add(
                GT_Recipe.GT_Recipe_Map.sAssemblylineVisualRecipes.addFakeRecipe(
                        false,
                        r.mInputs,
                        new ItemStack[] { aOutput },
                        new ItemStack[] { ItemList.Tool_DataStick.getWithName(1L, "Reads Research result") },
                        r.mFluidInputs,
                        null,
                        r.mDuration,
                        r.mEUt,
                        0,
                        r.mOreDictAlt,
                        false));

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
        return ret;
    });

    /**
     * Just like any normal assembler recipe, however it accepts one input item to be oredicted. Pass in the item to
     * oredict via {@link #OREDICT_INPUT}. It will be used along all other item inputs as input of this recipe.
     */
    public static IGT_RecipeMap AssemblerOD = IGT_RecipeMap.newRecipeMap(builder -> {
        Collection<GT_Recipe> ret = new ArrayList<>();
        for (ItemStack input : GT_OreDictUnificator.getOresImmutable(builder.getMetadata(OREDICT_INPUT))) {
            ret.addAll(
                    builder.copy()
                           .itemInputs(GT_RecipeMapUtil.appendArray(builder.getItemInputsBasic(), input))
                           .addTo(GT_Recipe_Map.sAssemblerRecipes));
        }
        return ret;
    });

    /**
     * A universal fuel adder. It's actually just a dispatcher towards all actual fuel recipe maps.
     * Dispatch based on {@link #FUEL_TYPE}. Uses {@link #FUEL_VALUE} as fuel value.
     * Can use {@link FuelType#ordinal()} as a human-readable form of what FUEL_TYPE should be.
     * You can bypass this and add to relevant fuel maps directly if you wish.
     */
    public static IGT_RecipeMap Fuel = IGT_RecipeMap.newRecipeMap(builder -> {
        builder.validateInputCount(1, 1)
               .validateNoInputFluid()
               .validateOutputCount(-1, 0)
               .validateNoOutputFluid();
        if (!builder.isValid()) return Collections.emptyList();
        int fuelType = builder.getMetadata(FUEL_TYPE);
        builder.metadata(
                FUEL_VALUE,
                GregTech_API.sRecipeFile.get(
                        "fuel_" + fuelType,
                        builder.getItemInputBasic(0),
                        builder.getMetadata(FUEL_VALUE)));
        return FuelType.get(fuelType)
                       .getTarget()
                       .doAdd(builder);
    });

    public enum FuelType {

        // ORDER MATTERS. DO NOT INSERT ELEMENT BETWEEN EXISTING ONES
        DieselFuel(GT_Recipe_Map.sDieselFuels),
        GasTurbine(GT_Recipe_Map.sTurbineFuels),
        // appears unused
        HotFuel(GT_Recipe_Map.sHotFuels),
        SemiFluid(GT_Recipe_Map.sDenseLiquidFuels),
        PlasmaTurbine(GT_Recipe_Map.sPlasmaFuels),
        Magic(GT_Recipe_Map.sMagicFuels),;

        private static final FuelType[] VALUES = values();
        private final IGT_RecipeMap target;

        FuelType(IGT_RecipeMap target) {
            this.target = target;
        }

        public static FuelType get(int fuelType) {
            if (fuelType < 0 || fuelType >= VALUES.length) return SemiFluid;
            return VALUES[fuelType];
        }

        public IGT_RecipeMap getTarget() {
            return target;
        }
    }

    static {
        GT_RecipeMapUtil.SPECIAL_VALUE_ALIASES.add(COIL_HEAT);
        GT_RecipeMapUtil.SPECIAL_VALUE_ALIASES.add(FUSION_THRESHOLD);
        GT_RecipeMapUtil.SPECIAL_VALUE_ALIASES.add(FUEL_VALUE);
    }
}
