package goodgenerator.loader;

import static goodgenerator.util.StackUtils.*;

import goodgenerator.util.MyRecipeAdder;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.GT_IntegratedCircuit_Item;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;

public class ComponentAssemblyLineRecipeLoader {
    private static final String[] compPrefixes = {
        "Electric_Motor_",
        "Electric_Piston_",
        "Electric_Pump_",
        "Robot_Arm_",
        "Conveyor_Module_",
        "Emitter_",
        "Sensor_",
        "Field_Generator_",
    };
    private static final String[] blacklistedDictPrefixes = {"circuit"};
    private static final String[] softBlacklistedDictPrefixes = {"Any", "crafting"};

    private static LinkedHashMap<List<GT_Recipe>, Pair<ItemList, Integer>> allAssemblerRecipes;
    private static LinkedHashMap<List<GT_Recipe.GT_Recipe_AssemblyLine>, Pair<ItemList, Integer>> allAsslineRecipes;

    private static final HashMap<OrePrefixes, OrePrefixes> conversion = new HashMap<>();

    private static final int INPUT_MULTIPLIER = 48;
    private static final int OUTPUT_MULTIPLIER = 64;

    public static void run() {
        ComponentAssemblyLineMiscRecipes.run();
        conversion.put(OrePrefixes.cableGt01, OrePrefixes.cableGt16);
        conversion.put(OrePrefixes.wireGt01, OrePrefixes.wireGt16);
        conversion.put(OrePrefixes.cableGt02, OrePrefixes.cableGt16);
        conversion.put(OrePrefixes.wireGt02, OrePrefixes.wireGt16);
        conversion.put(OrePrefixes.cableGt04, OrePrefixes.cableGt16);
        conversion.put(OrePrefixes.wireGt04, OrePrefixes.wireGt16);
        conversion.put(OrePrefixes.cableGt08, OrePrefixes.cableGt16);
        conversion.put(OrePrefixes.wireGt08, OrePrefixes.wireGt16);
        conversion.put(OrePrefixes.plate, OrePrefixes.plateDense);
        conversion.put(OrePrefixes.foil, OrePrefixes.plate);
        conversion.put(OrePrefixes.stick, OrePrefixes.stickLong);
        conversion.put(OrePrefixes.gearGtSmall, OrePrefixes.gearGt);
        findAllRecipes();
        generateAssemblerRecipes();
        generateAsslineRecipes();
    }

    /** Normal assembler recipes (LV-IV) */
    private static void generateAssemblerRecipes() {
        allAssemblerRecipes.forEach((recipeList, info) -> {
            for (GT_Recipe recipe : recipeList) {
                if (recipe != null) {
                    ArrayList<ItemStack> fixedInputs = new ArrayList<>();
                    ArrayList<FluidStack> fixedFluids = new ArrayList<>();

                    // This is done in order to differentiate between emitter and sensor recipes. Without the circuit,
                    // both components have virtually the same recipe after the inputs are melted.
                    if (info.getLeft().name().contains("Sensor")) {
                        fixedInputs.add(GT_Utility.getIntegratedCircuit(1));
                    }
                    for (int j = 0; j < recipe.mInputs.length; j++) {
                        ItemStack input = recipe.mInputs[j];
                        if (GT_Utility.isStackValid(input) && !(input.getItem() instanceof GT_IntegratedCircuit_Item))
                            fixedInputs.addAll(multiplyAndSplitIntoStacks(input, INPUT_MULTIPLIER));
                    }
                    for (int j = 0; j < recipe.mFluidInputs.length; j++) {
                        FluidStack currFluid = recipe.mFluidInputs[j].copy();
                        currFluid.amount = currFluid.amount * INPUT_MULTIPLIER;
                        fixedFluids.add(currFluid);
                    }

                    int tier = info.getRight();
                    int energy = (int) Math.min(Integer.MAX_VALUE - 7, (GT_Values.V[tier] - (GT_Values.V[tier] >> 4)));

                    MyRecipeAdder.instance.addComponentAssemblyLineRecipe(
                            compactItems(fixedInputs, info.getRight()).toArray(new ItemStack[0]),
                            fixedFluids.toArray(new FluidStack[0]),
                            info.getLeft().get(OUTPUT_MULTIPLIER),
                            recipe.mDuration * INPUT_MULTIPLIER,
                            energy,
                            info.getRight());
                }
            }
        });
    }
    /** Assembly Line Recipes (LuV+) **/
    private static void generateAsslineRecipes() {
        allAsslineRecipes.forEach((recipeList, info) -> {
            for (GT_Recipe.GT_Recipe_AssemblyLine recipe : recipeList) {
                if (recipe != null) {
                    // Arrays of the item and fluid inputs, that are updated to be multiplied and/or condensed in the
                    // following code
                    ArrayList<ItemStack> fixedInputs = new ArrayList<>();
                    ArrayList<FluidStack> fixedFluids = new ArrayList<>();

                    // This is done in order to differentiate between emitter and sensor recipes. Without the circuit,
                    // both components have virtually the same recipe after the inputs are melted.
                    if (info.getLeft().name().contains("Sensor")) {
                        fixedInputs.add(GT_Utility.getIntegratedCircuit(1));
                    }

                    // Multiplies the original fluid inputs
                    for (int j = 0; j < recipe.mFluidInputs.length; j++) {
                        FluidStack currFluid = recipe.mFluidInputs[j].copy();
                        currFluid.amount *= INPUT_MULTIPLIER;
                        fixedFluids.add(currFluid);
                    }

                    // First pass.
                    for (ItemStack input : recipe.mInputs) {
                        if (GT_Utility.isStackValid(input)) {
                            int count = input.stackSize;
                            // Mulitplies the input by its multiplier, and adjusts the stacks accordingly
                            if (!(input.getItem() instanceof GT_IntegratedCircuit_Item)) {

                                ItemData data = GT_OreDictUnificator.getAssociation(input);
                                // trying to fix some circuit oredicting issues

                                if (data != null && data.mPrefix == OrePrefixes.circuit)
                                    fixedInputs.addAll(multiplyAndSplitIntoStacks(
                                            GT_OreDictUnificator.get(data.mPrefix, data.mMaterial.mMaterial, count),
                                            INPUT_MULTIPLIER));
                                else fixedInputs.addAll(multiplyAndSplitIntoStacks(input, INPUT_MULTIPLIER));
                            }
                        }
                    }

                    fixedInputs = compactItems(fixedInputs, info.getRight());
                    replaceIntoFluids(fixedInputs, fixedFluids, 128);
                    // If it overflows then it tries REALLY HARD to cram as much stuff into there.
                    if (fixedInputs.size() > 9) replaceIntoFluids(fixedInputs, fixedFluids, 32);
                    MyRecipeAdder.instance.addComponentAssemblyLineRecipe(
                            fixedInputs.toArray(new ItemStack[0]),
                            fixedFluids.toArray(new FluidStack[0]),
                            info.getLeft().get(OUTPUT_MULTIPLIER),
                            recipe.mDuration,
                            recipe.mEUt,
                            info.getRight());
                }
            }
        });
    }
    /**
     * Returns {@code true} if the {@code ItemStack} is able to be compacted
     * into its respective gear/wire/etc.
     * */
    private static boolean isCompactable(ItemStack toCompact) {
        ItemData data = GT_OreDictUnificator.getAssociation(toCompact);
        return data != null
                && conversion.containsKey(data.mPrefix)
                && conversion.get(data.mPrefix) != null
                && GT_OreDictUnificator.get(conversion.get(data.mPrefix), data.mMaterial.mMaterial, 1) != null;
    }

    private static void replaceIntoFluids(List<ItemStack> inputs, List<FluidStack> fluidOutputs, int threshold) {
        HashMap<ItemStack, Integer> totals = getTotalItems(inputs.toArray(new ItemStack[0]));
        ArrayList<ItemStack> newInputs = new ArrayList<>();
        for (ItemStack input : totals.keySet()) {
            int count = totals.get(input);
            boolean isConverted = false;
            if (OreDictionary.getOreIDs(input).length > 0 && count > threshold) {
                FluidStack foundFluidStack = tryConvertItemStackToFluidMaterial(input);
                // Looks for a matching fluid stack and merges the amount of the converted fluid with
                // the one it found. Otherwise it will add the converted to the fluid inputs.

                // Prevents the uncraftable molten magnetic samarium from being converted into fluid during auto
                // generation

                ItemData data = GT_OreDictUnificator.getAssociation(input);
                if (data != null && data.mMaterial.mMaterial == Materials.SamariumMagnetic) {
                    input = GT_OreDictUnificator.get(data.mPrefix, Materials.Samarium, 1);
                    foundFluidStack = tryConvertItemStackToFluidMaterial(input);
                }

                if (foundFluidStack != null) {
                    foundFluidStack.amount *= count;
                    boolean alreadyHasFluid = false;
                    for (FluidStack fluidstack : fluidOutputs) {
                        if (foundFluidStack.getFluid().equals(fluidstack.getFluid())) {
                            fluidstack.amount += foundFluidStack.amount;
                            alreadyHasFluid = true;
                            break;
                        }
                    }
                    if (!alreadyHasFluid) {
                        fluidOutputs.add(foundFluidStack);
                    }
                    isConverted = true;
                }
            }
            if (!isConverted) {
                newInputs.addAll(multiplyAndSplitIntoStacks(input, count));
            }
        }
        inputs.clear();
        inputs.addAll(newInputs);
    }

    /** Tries to convert {@code input} into its molten form.
     * Because the internal names for material fluids in GT5u, GT++, and BartWorks follow the same naming scheme,
     * this method should work for any {@code ItemStack} from any of the 3 material systems.
     * */
    @Nullable
    private static FluidStack tryConvertItemStackToFluidMaterial(ItemStack input) {
        ArrayList<String> oreDicts = new ArrayList<>();
        for (int id : OreDictionary.getOreIDs(input)) {
            oreDicts.add(OreDictionary.getOreName(id));
        }
        oreDictLoop:
        for (String dict : oreDicts) {
            for (String blacklistedPrefix : blacklistedDictPrefixes) {
                if (dict.startsWith(blacklistedPrefix)) {
                    return null;
                }
            }
            for (String blacklistedPrefix : softBlacklistedDictPrefixes) {
                if (dict.startsWith(blacklistedPrefix)) {
                    continue oreDictLoop;
                }
            }
            OrePrefixes orePrefix;
            try {
                orePrefix = OrePrefixes.valueOf(findBestPrefix(dict));
            } catch (Exception e) {
                continue;
            }

            String strippedOreDict = dict.substring(orePrefix.toString().length());

            // Prevents things like AnyCopper or AnyIron from messing the search up.
            if (strippedOreDict.contains("Any")) continue;
            return FluidRegistry.getFluidStack(
                    "molten." + strippedOreDict.toLowerCase(),
                    (int) (orePrefix.mMaterialAmount / (GT_Values.M / 144)) * input.stackSize);
        }
        return null;
    }
    /**
     * Gives the longest Ore Prefix that the OreDictionary string starts with. This makes it the most accurate prefix.
     * For example: If your OreDictionary is something like {@code gearGtSmallSpaceTime}, a conventional search would
     * return something like {@code gearGt} instead of {@code gearGtSmall}. This makes the longer String the most accurate.
     * @param oreDict The Ore Dictionary entry
     * @return The longest ore prefix that the OreDict string starts with. This makes it the most accurate prefix.
     */
    private static String findBestPrefix(String oreDict) {
        int longestPrefixLength = 0;
        String matchingPrefix = null;
        for (OrePrefixes prefix : OrePrefixes.values()) {
            String name = prefix.toString();
            if (oreDict.startsWith(name)) {
                if (name.length() > longestPrefixLength) {
                    longestPrefixLength = name.length();
                    matchingPrefix = name;
                }
            }
        }
        return matchingPrefix;
    }
    /**
     * Transforms each {@code ItemStack}, if possible, into a more compact form.
     * For example, a stack of 16 1x cables, when passed into the {@code items} array,
     * will be converted into a single 16x cable. Also handles GraviStar conversion.
     * */
    private static ArrayList<ItemStack> compactItems(List<ItemStack> items, int tier) {
        ArrayList<ItemStack> stacks = new ArrayList<>();
        HashMap<ItemStack, Integer> totals = getTotalItems(items);
        for (ItemStack itemstack : totals.keySet()) {
            int totalItems = totals.get(itemstack);
            ItemData data = GT_OreDictUnificator.getAssociation(itemstack);
            boolean isCompacted = false;
            if (data != null) {
                if (data.mPrefix == OrePrefixes.circuit) {
                    stacks.addAll(getWrappedCircuits(itemstack, totalItems));
                    isCompacted = true;
                } else {
                    OrePrefixes goInto = conversion.get(data.mPrefix);
                    if (goInto != null && GT_OreDictUnificator.get(goInto, data.mMaterial.mMaterial, 1) != null) {
                        compactorHelper(data, goInto, stacks, totalItems);
                        isCompacted = true;
                    }
                }
            }
            if (GT_Utility.areStacksEqual(itemstack, ItemList.Gravistar.get(1)) && tier >= 9) {
                stacks.addAll(multiplyAndSplitIntoStacks(ItemList.NuclearStar.get(1), totalItems / 16));
                isCompacted = true;
            }
            if (!isCompacted) stacks.addAll(multiplyAndSplitIntoStacks(itemstack, totalItems));
        }
        stacks = mergeStacks(stacks);
        return stacks;
    }
    /** A helper method for compacting items */
    private static void compactorHelper(
            ItemData data, OrePrefixes compactInto, ArrayList<ItemStack> output, int total) {
        int materialRatio = (int) ((double) compactInto.mMaterialAmount / data.mPrefix.mMaterialAmount);
        output.addAll(multiplyAndSplitIntoStacks(
                GT_OreDictUnificator.get(compactInto, data.mMaterial.mMaterial, 1), total / materialRatio));
    }

    /**
     * Searches the Assembler and Assembly line registry for all the base component recipes.
     */
    private static void findAllRecipes() {
        allAssemblerRecipes = new LinkedHashMap<>();
        allAsslineRecipes = new LinkedHashMap<>();
        for (String compPrefix : compPrefixes) {

            for (int t = 1; t <= 12; t++) {
                String vName = GT_Values.VN[t];
                ItemList currentComponent = ItemList.valueOf(compPrefix + vName);
                if (currentComponent.hasBeenSet()) {
                    if (t < 6) {
                        allAssemblerRecipes.put(
                                GT_Recipe.GT_Recipe_Map.sAssemblerRecipes.mRecipeList.stream()
                                        .filter(rec -> rec.mOutputs[0].isItemEqual(currentComponent.get(1)))
                                        .collect(Collectors.toList()),
                                Pair.of(currentComponent, t));
                    } else {
                        allAsslineRecipes.put(
                                GT_Recipe.GT_Recipe_AssemblyLine.sAssemblylineRecipes.stream()
                                        .filter(rec -> rec.mOutput.isItemEqual(currentComponent.get(1)))
                                        .collect(Collectors.toList()),
                                Pair.of(currentComponent, t));
                    }
                }
            }
        }
    }

    private static List<ItemStack> getWrappedCircuits(ItemStack item, int total) {
        ArrayList<ItemStack> stacks = new ArrayList<>();
        for (ItemStack i2 : ComponentAssemblyLineMiscRecipes.CircuitToTier.keySet()) {
            int tier = ComponentAssemblyLineMiscRecipes.CircuitToTier.get(i2);
            if (GT_Utility.areStacksEqual(item, i2)) {
                if (total >= 16)
                    stacks.addAll(multiplyAndSplitIntoStacks(new ItemStack(Loaders.circuitWrap, 1, tier), total / 16));
                else stacks.addAll(multiplyAndSplitIntoStacks(item, total));
                break;
            }
        }
        return stacks;
    }
}
