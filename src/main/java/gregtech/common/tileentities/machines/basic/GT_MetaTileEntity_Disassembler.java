package gregtech.common.tileentities.machines.basic;

import com.google.common.collect.ArrayListMultimap;
import gregtech.api.GregTech_API;
import gregtech.api.enums.*;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GT_MetaTileEntity_Disassembler extends GT_MetaTileEntity_BasicMachine {

    public GT_MetaTileEntity_Disassembler(int aID, String aName, String aNameRegional, int aTier) {
        super(
                aID,
                aName,
                aNameRegional,
                aTier,
                1,
                new String[]{
                    "Disassembles Machines up to " + GT_Values.TIER_COLORS[aTier] + GT_Values.VOLTAGE_NAMES[aTier],
                    "Can also disassemble most assembler recipes!"
                },
                1,
                9,
                "Disassembler.png",
                "",

                //Textures
                new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_SIDE_DISASSEMBLER_ACTIVE),
                new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_SIDE_DISASSEMBLER),
                new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_DISASSEMBLER_ACTIVE),
                new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_DISASSEMBLER),
                new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_TOP_DISASSEMBLER_ACTIVE),
                new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_TOP_DISASSEMBLER),
                new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_BOTTOM_DISASSEMBLER_ACTIVE),
                new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_BOTTOM_DISASSEMBLER)
        );
    }

    public GT_MetaTileEntity_Disassembler(String aName, int aTier, String aDescription, ITexture[][][] aTextures, String aGUIName, String aNEIName) {
        super(aName, aTier, 1, aDescription, aTextures, 1, 9, aGUIName, aNEIName);
    }

    public GT_MetaTileEntity_Disassembler(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures, String aGUIName, String aNEIName) {
        super(aName, aTier, 1, aDescription, aTextures, 1, 9, aGUIName, aNEIName);
    }

    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Disassembler(this.mName, this.mTier, this.mDescriptionArray, this.mTextures, this.mGUIName, this.mNEIName);
    }

    private static final ItemStack[][] alwaysReplace = new ItemStack[][]{
            {
                //ItemStack to look for
                new ItemStack(Blocks.trapped_chest, 1, OreDictionary.WILDCARD_VALUE)
            },
            {
                //ItemStack to replace
                new ItemStack(Blocks.chest)
            }
    };

    private static final Object[][] OreDictionaryOverride = new Object[][]{
            {
                    //String to look for
                    "plankWood",
                    "stoneCobble",
                    "gemDiamond",
                    "logWood",
                    "stickWood",
                    "treeSapling"
            },
            {
                    //ItemStack to replace
                    new ItemStack(Blocks.planks),
                    new ItemStack(Blocks.cobblestone),
                    new ItemStack(Items.diamond),
                    new ItemStack(Blocks.log),
                    new ItemStack(Items.stick),
                    new ItemStack(Blocks.sapling)
            }
    };

    public static ArrayListMultimap<GT_ItemStack, ItemStack> getOutputHardOverrides() {
        return outputHardOverrides;
    }

    private static final ArrayListMultimap<GT_ItemStack, ItemStack> outputHardOverrides;

    private static final Set<GT_ItemStack> blackList;

    public static Set<GT_ItemStack> getBlackList() {
        return blackList;
    }

    static {
        outputHardOverrides = ArrayListMultimap.create();
        outputHardOverrides.put(new GT_ItemStack(new ItemStack(Blocks.torch,6)), new ItemStack(Items.stick));
        blackList = new HashSet<>();
        blackList.add(new GT_ItemStack(ItemList.Casing_Coil_Superconductor.get(1L)));
    }

    private boolean compareToUnpacker(ItemStack is){
       return null != GT_Recipe.GT_Recipe_Map.sUnboxinatorRecipes.findRecipe(
               null,
               true,
               true,
               Long.MAX_VALUE,
               null,
               is);
    }

    public int checkRecipe() {
        ItemStack is = getInputAt(0);

        if (GT_Utility.isStackInvalid(is))
            return DID_NOT_FIND_RECIPE;

        if (
                is.getItem() instanceof GT_MetaGenerated_Tool
                || blackList.contains(new GT_ItemStack(is))
                || compareToUnpacker(is)
        )
            return DID_NOT_FIND_RECIPE;

        if (checkTier(is))
            return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;

        Integer handleHardOverride = handleHardOverride(is);
        if (handleHardOverride != null)
            return handleHardOverride;

        return process();
    }

    private Integer handleHardOverride(ItemStack is) {
        Set<GT_ItemStack> stacks = outputHardOverrides.keySet();
        for (GT_ItemStack stack : stacks) {
            ItemStack in = is.copy();
            in.stackSize = 1;
            if (stack.isStackEqual(in) && stack.mStackSize <= is.stackSize) {
                return setOutputsAndTime(outputHardOverrides.get(stack).toArray(new ItemStack[0]), stack.mStackSize)
                        ? FOUND_AND_SUCCESSFULLY_USED_RECIPE
                        : DID_NOT_FIND_RECIPE;
            }
        }
        return null;
    }

    private boolean checkTier(ItemStack is) {
        ItemStack comp = new ItemStack(GregTech_API.sBlockMachines);
        if (is.getItem() == comp.getItem()) {
            IMetaTileEntity iMetaTileEntity = GregTech_API.METATILEENTITIES[is.getItemDamage()];

            return iMetaTileEntity instanceof GT_MetaTileEntity_TieredMachineBlock &&
                    ((GT_MetaTileEntity_TieredMachineBlock) iMetaTileEntity).mTier > this.mTier;
        }
        return false;
    }

    private int process(){
        int statusCode = checkRecipeMap();
        if (statusCode != DID_NOT_FIND_RECIPE)
            return statusCode;

        return onTheFlyGeneration();
    }

    private int onTheFlyGeneration() {
        Collection<DissassembleReference> recipes = this.findRecipeFromMachine();
        if (recipes.isEmpty())
            return DID_NOT_FIND_RECIPE;

        DissassembleReference recipe = ensureDowncasting(recipes);

        removeInvalidStacks(recipe);

        return setOutputsAndTime(recipe.inputs, recipe.stackSize)
                ? FOUND_AND_SUCCESSFULLY_USED_RECIPE
                : FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
    }

    private void removeInvalidStacks(DissassembleReference recipe) {
        for (int i = 0; i < recipe.inputs.length; i++)
            if (GT_Utility.isStackInvalid(recipe.inputs[i]) || recipe.inputs[i].stackSize < 1)
                recipe.inputs[i] = null;

        recipe.inputs = GT_Utility.getArrayListWithoutNulls(recipe.inputs).toArray(new ItemStack[0]);
    }

    private int checkRecipeMap() {
        GT_Recipe gt_recipe = GT_Recipe.GT_Recipe_Map.sDisassemblerRecipes.findRecipe(this.getBaseMetaTileEntity(), true, this.maxEUInput(), null, this.getAllInputs());
        if (gt_recipe == null)
            return DID_NOT_FIND_RECIPE;
        if (gt_recipe.isRecipeInputEqual(false, null, this.getAllInputs()))
            return setOutputsAndTime(gt_recipe.mOutputs, gt_recipe.mInputs[0].stackSize)
                    ? FOUND_AND_SUCCESSFULLY_USED_RECIPE
                    : FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;

        return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
    }

    private boolean setOutputsAndTime(ItemStack[] inputs, int stackSize){
        if (this.getInputAt(0).stackSize >= stackSize)
            this.getInputAt(0).stackSize -= stackSize;
        else
            return false;

        System.arraycopy(inputs, 0, this.mOutputItems, 0, inputs.length);
        this.calculateOverclockedNess(30,600);

        return true;
    }

    private static DissassembleReference ensureDowncasting(Collection<DissassembleReference> recipes) {
        ItemStack[] inputs = recipes.stream()
                .findFirst()
                .orElseThrow(NullPointerException::new)
                .inputs;

        ItemStack[] output = new ItemStack[inputs.length];
        List<GT_Recipe> recipesColl = null;
        if (recipes.size() > 1)
            recipesColl = recipes.stream()
                    .skip(1)
                    .map(x -> x.recipe)
                    .collect(Collectors.toList());

        handleRecipeTransformation(inputs, output, recipesColl);

        return new DissassembleReference(recipes.stream().mapToInt(x -> x.stackSize).min().orElseThrow(NumberFormatException::new), output, null);
    }

    private static void handleRecipeTransformation(ItemStack[] inputs, ItemStack[] output, List<GT_Recipe> recipesColl) {
        for (int i = 0, inputsLength = inputs.length; i < inputsLength; i++) {
            Set<ItemStack[]> inputsStacks = null;
            if (recipesColl != null)
                inputsStacks = recipesColl.stream()
                        .map(x -> x.mInputs)
                        .collect(Collectors.toSet());
            handleRecipeTransformationInternal(inputs, output, inputsStacks, i);
        }
        addOthersAndHandleAlwaysReplace(inputs, output);
    }

    /**
     * Public Interface for ReverseRecipes, do not call inside of this class.
     */
    public static void handleRecipeTransformation(ItemStack[] inputs, ItemStack[] output, Set<ItemStack[]> inputsStacks) {
        for (int i = 0, inputsLength = inputs.length; i < inputsLength; i++)
            handleRecipeTransformationInternal(inputs, output, inputsStacks, i);
        addOthersAndHandleAlwaysReplace(inputs, output);
    }

    private static void handleRecipeTransformationInternal(ItemStack[] inputs, ItemStack[] output, Set<ItemStack[]> inputsStacks, int i) {
        ItemStack input = inputs[i];
        ItemData data = GT_OreDictUnificator.getItemData(input);
        if (data == null || data.mMaterial == null || data.mMaterial.mMaterial == null || data.mPrefix == null) {
            output[i] = input;
            return;
        }
        handleReplacement(inputsStacks, data, output, input, i);
    }

    private static void addOthersAndHandleAlwaysReplace(ItemStack[] inputs, ItemStack[] output){
        for (int i = 0; i < inputs.length; i++) {
            //Adds rest of Items
            if (output[i] == null)
                output[i] = inputs[i];

            //Math.min the recipe output if Items are the same
            if (GT_Utility.areStacksEqual(output[i], inputs[i]))
                output[i].stackSize = Math.min(output[i].stackSize, inputs[i].stackSize);

            //Handles replacement Overrides
            ItemStack[] itemStacks = GT_MetaTileEntity_Disassembler.alwaysReplace[0];
            for (int j = 0; j < itemStacks.length; j++) {
                ItemStack x = itemStacks[j];
                if (GT_Utility.areStacksEqual(x, output[i], true)) {
                    output[i] = GT_MetaTileEntity_Disassembler.alwaysReplace[1][j];
                    break;
                }
            }

            //Unification
            output[i] = handleUnification(output[i]);
        }
    }

    private static ItemStack handleUnification(ItemStack stack) {
        for (int oreID : OreDictionary.getOreIDs(stack)) {
            for (int i = 0; i < OreDictionaryOverride[0].length; i++)
                if (OreDictionary.getOreName(oreID).equals(OreDictionaryOverride[0][i])){
                    ItemStack ret = ((ItemStack) OreDictionaryOverride[1][i]).copy();
                    ret.stackSize = stack.stackSize;
                    return ret;
                }
        }
        return GT_OreDictUnificator.get(stack);
    }

    private static void handleReplacement(Set<ItemStack[]> inputsStacks, ItemData data, ItemStack[] output, ItemStack input, int i){
        AtomicReference<Materials> toRpl = new AtomicReference<>();
        Materials first = data.mMaterial.mMaterial;
        if (inputsStacks != null) {
            handleInputStacks(inputsStacks, toRpl, data, first, i);
        }
        if (toRpl.get() == null) {
            //Remove Magnetic and Annealed Modifiers
            handleBetterMaterialsVersions(data, toRpl);
        }
        if (toRpl.get() != null) {
            output[i] = GT_OreDictUnificator.get(data.mPrefix, toRpl.get(), input.stackSize);
            return;
        }
        if (data.mPrefix == OrePrefixes.circuit) {
            handleCircuits(first, output, input, i);
        }
    }

    private static void handleInputStacks(Set<ItemStack[]> inputsStacks, AtomicReference<Materials> toRpl, ItemData data, Materials first, int i){
        final int finalIndex = i;
        inputsStacks.forEach(stackArray -> {
            ItemData dataAgainst = GT_OreDictUnificator.getItemData(stackArray[finalIndex]);
            if (
                    dataAgainst == null ||
                            dataAgainst.mMaterial == null ||
                            dataAgainst.mMaterial.mMaterial == null ||
                            dataAgainst.mPrefix == null ||
                            dataAgainst.mPrefix != data.mPrefix
            ) {
                return;
            }
            handleDifferentMaterialsOnRecipes(first, dataAgainst.mMaterial.mMaterial, toRpl);
            handleAnyMaterials(first,toRpl);
        });
    }

    private static void handleAnyMaterials(Materials first, AtomicReference<Materials> toRpl){
        if (first.mOreReRegistrations.stream().anyMatch(y -> y.equals(Materials.AnyIron)))
            toRpl.set(Materials.Iron);
        else if (first.mOreReRegistrations.stream().anyMatch(y -> y.equals(Materials.AnyCopper)))
            toRpl.set(Materials.Copper);
        else if (first.mOreReRegistrations.stream().anyMatch(y -> y.equals(Materials.AnyRubber)))
            toRpl.set(Materials.Rubber);
        else if (first.mOreReRegistrations.stream().anyMatch(y -> y.equals(Materials.AnyBronze)))
            toRpl.set(Materials.Bronze);
        else if (first.mOreReRegistrations.stream().anyMatch(y -> y.equals(Materials.AnySyntheticRubber)))
            toRpl.set(Materials.Rubber);
    }

    private static void handleDifferentMaterialsOnRecipes(Materials first, Materials second, AtomicReference<Materials> toRpl){
        if (!first.equals(second))
            if (first.equals(Materials.Aluminium) && second.equals(Materials.Iron))
                toRpl.set(second);
            else if (first.equals(Materials.Steel) && second.equals(Materials.Iron))
                toRpl.set(second);
            else if (first.equals(Materials.WroughtIron) && second.equals(Materials.Iron))
                toRpl.set(second);
            else if (first.equals(Materials.Aluminium) && second.equals(Materials.WroughtIron))
                toRpl.set(Materials.Iron);
            else if (first.equals(Materials.Aluminium) && second.equals(Materials.Steel))
                toRpl.set(second);
            else if (first.equals(Materials.Polytetrafluoroethylene) && second.equals(Materials.Plastic))
                toRpl.set(second);
            else if (first.equals(Materials.Polybenzimidazole) && second.equals(Materials.Plastic))
                toRpl.set(second);
            else if (first.equals(Materials.Polystyrene) && second.equals(Materials.Plastic))
                toRpl.set(second);
            else if (first.equals(Materials.Silicone) && second.equals(Materials.Plastic))
                toRpl.set(second);
            else if (first.equals(Materials.NetherQuartz) || first.equals(Materials.CertusQuartz) && second.equals(Materials.Quartzite))
                toRpl.set(second);
            else if (first.equals(Materials.Plastic) && second.equals(Materials.Wood))
                toRpl.set(second);
            else if (first.equals(Materials.Diamond) && second.equals(Materials.Glass))
                toRpl.set(second);
    }

    private static void handleBetterMaterialsVersions(ItemData data, AtomicReference<Materials> toRpl){
        if (Materials.SteelMagnetic.equals(data.mMaterial.mMaterial)) {
            toRpl.set(Materials.Steel);
        } else if (Materials.IronMagnetic.equals(data.mMaterial.mMaterial)) {
            toRpl.set(Materials.Iron);
        } else if (Materials.NeodymiumMagnetic.equals(data.mMaterial.mMaterial)) {
            toRpl.set(Materials.Neodymium);
        } else if (Materials.SamariumMagnetic.equals(data.mMaterial.mMaterial)) {
            toRpl.set(Materials.Samarium);
        } else if (Materials.AnnealedCopper.equals(data.mMaterial.mMaterial)) {
            toRpl.set(Materials.Copper);
        }
    }

    @SuppressWarnings("deprecation")
    private static void handleCircuits(Materials first, ItemStack[] output, ItemStack input, int i){
        if (first.equals(Materials.Primitive))
            output[i] = ItemList.NandChip.get(input.stackSize);
        else if (first.equals(Materials.Basic))
            output[i] = ItemList.Circuit_Microprocessor.get(input.stackSize);
        else if (first.equals(Materials.Good))
            output[i] = ItemList.Circuit_Good.get(input.stackSize);
        else if (first.equals(Materials.Advanced))
            output[i] = ItemList.Circuit_Advanced.get(input.stackSize);
        else if (first.equals(Materials.Data))
            output[i] = ItemList.Circuit_Data.get(input.stackSize);
        else if (first.equals(Materials.Master))
            output[i] = ItemList.Circuit_Master.get(input.stackSize);
        else if (first.equals(Materials.Ultimate))
            output[i] = ItemList.Circuit_Quantummainframe.get(input.stackSize);
        else if (first.equals(Materials.Superconductor))
            output[i] = ItemList.Circuit_Crystalmainframe.get(input.stackSize);
        else if (first.equals(Materials.Infinite))
            output[i] = ItemList.Circuit_Wetwaremainframe.get(input.stackSize);
        else if (first.equals(Materials.Bio))
            output[i] = ItemList.Circuit_Biomainframe.get(input.stackSize);
    }

    static class DissassembleReference {
        final int stackSize;
        ItemStack[] inputs;
        final GT_Recipe recipe;

        public DissassembleReference(int stackSize, ItemStack[] inputs, GT_Recipe recipe) {
            this.stackSize = stackSize;
            this.inputs = inputs;
            this.recipe = recipe;
        }
    }

    private Collection<DissassembleReference> findRecipeFromMachine() {
        ItemStack is = getInputAt(0);
        if (GT_Utility.isStackInvalid(is))
            return Collections.emptySet();

        AtomicInteger stacksize = new AtomicInteger();
        //Check Recipe Maps for creation of Item
        List<DissassembleReference> possibleRecipes = GT_Recipe.GT_Recipe_Map.sAssemblerRecipes.mRecipeList.stream()
                .filter(x -> Arrays.stream(x.mOutputs)
                        .anyMatch(y ->
                        {
                            ItemStack out = is.copy();
                            out.stackSize = y.stackSize;
                            boolean isDone = GT_Utility.areStacksEqual(y, out, true) && y.stackSize <= is.stackSize;
                            if (isDone)
                                stacksize.set(y.stackSize);
                            return isDone;
                        })
                            )
                .map(x -> new DissassembleReference(stacksize.get(), x.mInputs, x))
                .collect(Collectors.toList());

        //Is there only one way to create it?
        if (possibleRecipes.size() == 1)
            return possibleRecipes;

        //There are Multiple Ways -> Get recipe with cheapest inputs
        //More Inputs should mean cheaper Materials
        return possibleRecipes
                .stream()
                .sorted(Comparator.comparingDouble(GT_MetaTileEntity_Disassembler::getCheaperInputs))
                .collect(Collectors.toList());
    }

    private static double getCheaperInputs(GT_MetaTileEntity_Disassembler.DissassembleReference x){
        double fluidInputValueRaw = Arrays.stream(x.recipe.mFluidInputs).flatMapToInt(f -> IntStream.of(f.amount)).sum();
        fluidInputValueRaw = fluidInputValueRaw > 0 ? fluidInputValueRaw : 144D;
        double inputValue = Arrays.stream(x.inputs).flatMapToInt(f -> IntStream.of(f.stackSize)).sum() +
                (fluidInputValueRaw / 144D);
        double fluidOutputValueRaw = Arrays.stream(x.recipe.mFluidOutputs).flatMapToInt(f -> IntStream.of(f.amount)).sum();
        fluidOutputValueRaw = fluidOutputValueRaw > 0 ? fluidOutputValueRaw : 144D;
        double outputValue = Arrays.stream(x.recipe.mOutputs).flatMapToInt(f -> IntStream.of(f.stackSize)).sum() +
                (fluidOutputValueRaw / 144D);
        return inputValue / outputValue;
    }

    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return (super.allowPutStack(aBaseMetaTileEntity, aIndex, aSide, aStack)) && (mDisableFilter || (aStack.getTagCompound() != null) && (aStack.getTagCompound().getCompoundTag("GT.CraftingComponents") != null));
    }
}