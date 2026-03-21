package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Mods.ExtraUtilities;
import static gregtech.api.enums.Mods.RandomThings;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.VoidProtectionHelper;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEThaumoArborealConverter extends GTPPMultiBlockBase<MTEThaumoArborealConverter>
    implements ISurvivalConstructable {

    public static int CASING_TEXTURE_ID;
    private static final Casings CASING = Casings.SterileFarmCasing;
    private static final int TICKS_PER_OPERATION = 100;
    private static final int BASE_PRODUCTION = 10;
    private static final int FRAME_NUM = 56;
    private static final int[] FRAME_IDS = { 330, // Thaumium
        368, // Shadow
        978, // Ichorium
        397 // Infinity
    };
    private static final int[] FRAME_WEIGHTS = { 100, // Thaumium
        150, // Shadow
        200, // Ichorium
        400 // Infinity
    };
    private static final int STONE_NUM = 24;
    private static final int STONE_TIERS = 8;

    private static final Block DIRT_BLOCK = RandomThings.isModLoaded()
        ? Block.getBlockFromName("RandomThings:fertilizedDirt")
        : Blocks.dirt;
    private static final Block TILLED_DIRT_BLOCK = RandomThings.isModLoaded()
        ? Block.getBlockFromName("RandomThings:fertilizedDirt_tilled")
        : Blocks.farmland;

    private int mCasing;
    private byte glassTier = -1;
    private int[] mFrame = new int[FRAME_IDS.length];
    private int mFrameTier;
    private int[] mStone = new int[STONE_TIERS];
    public static String mCasingName = "Sterile Farm Casing";

    public MTEThaumoArborealConverter(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
        CASING_TEXTURE_ID = TAE.getIndexFromPage(1, 15);
    }

    public MTEThaumoArborealConverter(final String aName) {
        super(aName);
        CASING_TEXTURE_ID = TAE.getIndexFromPage(1, 15);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEThaumoArborealConverter(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Thaumo-Arboreal Converter, TACo";
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addInfo("Channels dimensional tree magic to convert rock and soil")
            .addInfo("Place a sapling in the controller slot")
            .addInfo("Consumes relevant tree materials to boost conversion rate (CR)")
            .addInfo("More advanced structural frames boost CR and increase catalyst weight")
            .addInfo("  Thaumium: 1x, 100%")
            .addInfo("  Shadow Metal: 2x, 150%")
            .addInfo("  Ichorium: 4x, 200%")
            .addInfo("  Infinity: 8x, 400%")
            .addInfo("Using higher tiers of compressed cobblestone boosts CR up to 128x")
            .addInfo("Multiplier = 2^(average compression tier - 1)")
            .addSeparator()
            .addInfo("Work time is fixed at 5 seconds")
            .addInfo("Energy hatch limited by glass tier")
            .addInfo("Energy input tier multiplies CR further")
            .addInfo("Multiplier = 2*tier^2 - 2*tier + 5")
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(9, 8, 9, true)
            .addController("Front center")
            .addCasingInfoMin(mCasingName, 70, false)
            .addCasingInfoExactly("Any Tiered Glass (tiered)", 145, true)
            .addCasingInfoExactly("Frame Box (tiered)", FRAME_NUM, true)
            .addCasingInfoExactly("Compressed cobblestone (any)", STONE_NUM, true)
            .addInputBus("Any outer casing", 1)
            .addOutputBus("Any outer casing", 1)
            .addEnergyHatch("Any outer casing", 1)
            .addMaintenanceHatch("Any outer casing", 1)
            .toolTipFinisher();
        return tt;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.oMCATreeFarmActive;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.oMCATreeFarm;
    }

    @Override
    protected int getCasingTextureId() {
        return CASING_TEXTURE_ID;
    }

    protected boolean checkHatches() {
        return !mMaintenanceHatches.isEmpty() && !mOutputBusses.isEmpty() && !mEnergyHatches.isEmpty();
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.mCasing = 0;
        this.mFrameTier = this.getMultiFrameTier();
        this.glassTier = 0;
        if (!this.checkPiece(mName, 4, 6, 0) || this.mCasing < 70 || !this.checkHatches() || this.mFrameTier == 0)
            return false;
        for (MTEHatchEnergy mEnergyHatch : this.mEnergyHatches) {
            if (this.glassTier < mEnergyHatch.mTier) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean supportsCraftingMEBuffer() {
        return false;
    }

    @Override
    public boolean supportsBatchMode() {
        // Batch mode would not do anything, processing time is fixed at 100 ticks.
        return false;
    }

    @Override
    public boolean isBatchModeEnabled() {
        return false;
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiTreeFarm;
    }

    private static int getFrameTier(int id) {
        for (int i = 0; i < FRAME_IDS.length; i++) {
            if (FRAME_IDS[i] == id) {
                return i;
            }
        }
        return -1;
    }

    private static IStructureElement<MTEThaumoArborealConverter> frameElement(int id) {
        int tier = getFrameTier(id);

        return onElementPass(t -> {
            if (tier >= 0) {
                ++t.mFrame[tier];
            }
        }, ofBlock(GregTechAPI.sBlockFrames, id));
    }

    private static IStructureElement<MTEThaumoArborealConverter>[] frameElements() {
        IStructureElement<MTEThaumoArborealConverter>[] elements = new IStructureElement[FRAME_IDS.length];

        for (int i = 0; i < FRAME_IDS.length; i++) {
            elements[i] = frameElement(FRAME_IDS[i]);
        }

        return elements;
    }

    private static IStructureElement<MTEThaumoArborealConverter>[] stoneElements() {
        IStructureElement<MTEThaumoArborealConverter>[] elements = new IStructureElement[STONE_TIERS];

        // allow compressed dirt and gravel too?
        for (int i = 0; i < STONE_TIERS; i++) {
            elements[i] = onElementPass(
                t -> t.mStone[i]++,
                ofBlock(GameRegistry.findBlock(ExtraUtilities.ID, "cobblestone_compressed"), i));
        }

        return elements;
    }

    private /* static */ final IStructureDefinition<MTEThaumoArborealConverter> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEThaumoArborealConverter>builder()
        .addShape(
            mName,
            transpose(
                new String[][] { // spotless:off
                    { "  fffff  ", " fgggggf ", "fgggggggf", "fgggggggf", "fgggggggf", "fgggggggf", "fgggggggf", " fgggggf ", "  fffff  "},
                    { "   ggg   ", " fg---gf ", " g-----g ", "g-------g", "g-------g", "g-------g", " g-----g ", " fg---gf ", "   ggg   "},
                    { "   ggg   ", " fg---gf ", " g-----g ", "g-------g", "g-------g", "g-------g", " g-----g ", " fg---gf ", "   ggg   "},
                    { "   ggg   ", " fg---gf ", " g-----g ", "g-------g", "g-------g", "g-------g", " g-----g ", " fg---gf ", "   ggg   "},
                    { "   ggg   ", " fg---gf ", " g-----g ", "g-------g", "g-------g", "g-------g", " g-----g ", " fg---gf ", "   ggg   "},
                    { "   ggg   ", " fg---gf ", " g-----g ", "g-------g", "g-------g", "g-------g", " g-----g ", " fg---gf ", "   ggg   "},
                    { "   c~c   ", " ccsfscc ", " cssfssc ", "csssfsssc", "cfffdfffc", "csssfsssc", " cssfssc ", " ccsfscc ", "   ccc   "},
                    { "   ccc   ", " ccCCCcc ", " cCCCCCc ", "cCCCCCCCc", "cCCCCCCCc", "cCCCCCCCc", " cCCCCCc ", " ccCCCcc ", "   ccc   "},
                })) // spotless:on
        .addElement(
            'c',
            buildHatchAdder(MTEThaumoArborealConverter.class).atLeast(InputBus, OutputBus, Energy, Maintenance)
                .casingIndex(CASING.textureId)
                .hint(1)
                .buildAndChain(onElementPass(t -> t.mCasing++, Casings.SterileFarmCasing.asElement())))
        .addElement('f', ofChain(frameElements()))
        .addElement('g', chainAllGlasses(-1, (te, t) -> te.glassTier = t.byteValue(), te -> (int) te.glassTier))
        .addElement('d', ofChain(ofBlock(TILLED_DIRT_BLOCK, 0), ofBlock(DIRT_BLOCK, 0)))
        .addElement('C', onElementPass(t -> t.mCasing++, Casings.SterileFarmCasing.asElement()))
        .addElement('s', ofChain(stoneElements()))
        .build();

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 4, 6, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(mName, stackSize, 4, 6, 0, elementBudget, env, false, true);
    }

    /* Processing logic. */

    @Override
    public boolean isCorrectMachinePart(final ItemStack aStack) {
        if (aStack == null) return false;
        return isValidSapling(aStack);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        // TODO
        // Only for NEI, not used in processing logic.
        return GTPPRecipeMaps.thaumoArborealConverterFakeRecipes;
    }

    private static int getTierMultiplier(int tier) {
        return (2 * (tier * tier)) - (2 * tier) + 5;
    }

    private int getMultiFrameTier() {
        for (int i = 0; i < FRAME_IDS.length; i++) {
            if (mFrame[i] == FRAME_NUM) {
                return i;
            }
        }
        return -1;
    }

    private int getFrameMultiplier() {
        return (int) Math.pow(2, this.mFrameTier);
    }

    private int getFrameWeightMul() {
        return FRAME_WEIGHTS[this.mFrameTier];
    }

    private double getStoneTier() {
        double totalStoneWeight = 0;
        for (int i = 0; i < STONE_TIERS; i++) {
            totalStoneWeight += this.mStone[i] * i;
        }
        return totalStoneWeight / STONE_NUM;
    }

    private int getStoneMultiplier() {
        return (int) Math.pow(2, getStoneTier());
    }

    /**
     * Key of this map is the registry name of the sapling, followed by ":", and the sapling's metadata value.
     * <p>
     * The value of the map is a list of products and weights. Weights for a given sapling should add up to 100.
     */
    public static final HashMap<String, HashMap<ItemStack, Integer>> treeProductsMap = new HashMap<>();

    @Override
    public ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {
            // TODO peaceful tree consumes plant matter instead of rocks

            @Override
            @Nonnull
            public CheckRecipeResult process() {
                if (inputItems == null) {
                    inputItems = GTValues.emptyItemStackArray;
                }
                if (inputFluids == null) {
                    inputFluids = GTValues.emptyFluidStackArray;
                }

                ItemStack sapling = findSapling();
                if (sapling == null) return SimpleCheckRecipeResult.ofFailure("no_sapling");

                HashMap<ItemStack, Integer> outputMap = getOutputsForSapling(sapling);

                int tier = Math.max(1, GTUtility.getTier(availableVoltage * availableAmperage));
                int tierMultiplier = getTierMultiplier(tier);
                int tieredProduction = BASE_PRODUCTION * getFrameMultiplier() * tierMultiplier * getStoneMultiplier();
                int consumedInput = consumeInput(tieredProduction);
                int extraOutput = consumeCatalyst(consumedInput);
                int totalOutput = consumedInput + extraOutput;
                int totalWeight = getSaplingTotalWeight(sapling);

                List<ItemStack> outputs = new ArrayList<>();
                for (ItemStack output : outputMap.keySet()) {
                    ItemStack out = output.copy();
                    // w???
                    int chance = 10000 * totalWeight / outputMap.get(output);
                    // non-random
                    // out.stackSize = totalOutput * totalWeight / outputMap.get(output);
                    // random - normal approximation
                    // TODO
                    out.stackSize = 1;
                    outputs.add(out);
                }

                outputItems = outputs.toArray(new ItemStack[0]);

                VoidProtectionHelper voidProtection = new VoidProtectionHelper().setMachine(machine)
                    .setItemOutputs(outputItems)
                    .build();
                if (voidProtection.isItemFull()) {
                    return CheckRecipeResultRegistry.ITEM_OUTPUT_FULL;
                }

                duration = TICKS_PER_OPERATION;
                calculatedEut = GTValues.VP[tier];

                return SimpleCheckRecipeResult.ofSuccess("growing_trees");
            }
        };
    }

    private int consumeInput(int tieredProduction) {
        // consume input stone and dirt up to tieredProduction
        int remainingCapacity = tieredProduction;
        for (ItemStack stack : getStoredInputs()) {
            if (!getValidInputsForSapling().contains(stack)) continue;
            if (stack.stackSize <= remainingCapacity) {
                remainingCapacity -= stack.stackSize;
                stack.stackSize = 0;
            } else {
                stack.stackSize -= remainingCapacity;
                remainingCapacity = 0;
                break;
            }
        }
        return tieredProduction - remainingCapacity;
    }

    private static List<ItemStack> getValidInputsForSapling() {
        // TODO
        // use isOverworldBlock from thaumic bases?
        return new ArrayList<>(); // placeholder
    }

    private int consumeCatalyst(int consumedInput) {
        // consume input tree materials weighted up to consumedInput
        int consumedCatalystWeight = 0;
        int mult = getFrameWeightMul();
        for (ItemStack stack : getStoredInputs()) {
            int wgt = getCatalystWeight(stack) * mult / 100;
            if (wgt <= 0) continue;
            if (stack.stackSize * wgt <= consumedInput - consumedCatalystWeight) {
                consumedCatalystWeight += stack.stackSize * wgt;
                stack.stackSize = 0;
            } else {
                int remainingCapacity = consumedInput - consumedCatalystWeight;
                stack.stackSize = (remainingCapacity / wgt) + (remainingCapacity % wgt > 0 ? 1 : 0);
                consumedCatalystWeight = consumedInput;
                break;
            }
        }
        return consumedCatalystWeight;
    }

    private static int getCatalystWeight(ItemStack stack) {
        // TODO
        final int wgtLog = 4;
        final int wgtSapling = 2;
        final int wgtLeaves = 1;
        return 0; // placeholder
    }

    /**
     * Finds a valid sapling from input buses, and places it into the controller slot.
     *
     * @return The sapling that was found (now in the controller slot).
     */
    private ItemStack findSapling() {
        ItemStack controllerSlot = getControllerSlot();

        if (isValidSapling(controllerSlot)) {
            return controllerSlot;
        }

        if (controllerSlot != null) {
            // Non-sapling item in controller slot -> output
            if (addOutputAtomic(controllerSlot)) {
                mInventory[1] = null;
            } else {
                return null;
            }
        }

        // Here controller slot is empty, find a valid sapling to use.
        for (ItemStack stack : getStoredInputs()) {
            if (isValidSapling(stack)) {
                mInventory[1] = stack.splitStack(1);
                return mInventory[1];
            }
        }

        // No saplings were found.
        return null;
    }

    private static String getSaplingName(ItemStack sapling) {
        return Item.itemRegistry.getNameForObject(sapling.getItem()) + ":" + sapling.getItemDamage();
    }

    /**
     * Check if an ItemStack is a sapling that can be farmed.
     *
     * @param stack An ItemStack.
     * @return True if stack is a valid sapling that can be farmed.
     */
    private boolean isValidSapling(ItemStack stack) {
        if (stack == null) return false;
        return treeProductsMap.containsKey(getSaplingName(stack));
    }

    /**
     * Get a list of possible outputs for a sapling. This is recovered from
     * {@link #treeProductsMap}.
     *
     * @param sapling A sapling to farm.
     * @return A map of outputs for each mode. Outputs for some modes might be null.
     */
    private static HashMap<ItemStack, Integer> getOutputsForSapling(ItemStack sapling) {
        return treeProductsMap.get(getSaplingName(sapling));
    }

    /**
     * Registers outputs for a sapling. Output amount is further modified by catalyst consumption and frame and power
     * tier. Recipes
     * are added in {@link RecipeLoaderTACo}.
     *
     * @param saplingIn The input sapling to farm.
     * @param output    ItemStack to output
     * @param weight    proportional weight of output, these should add up to 100 across all outputs of a sapling
     */

    public static void registerTreeProducts(ItemStack saplingIn, ItemStack output, int weight) {
        // TODO normalize weight to 10k
        if (saplingIn == null) {
            Logger.ERROR("Null sapling passed for registerTreeProducts()");
            return;
        }
        if (weight <= 0 || weight > 100) {
            Logger.ERROR("Invalid weight passed for registerTreeProducts()");
            return;
        }
        String key = getSaplingName(saplingIn);
        HashMap<ItemStack, Integer> map = treeProductsMap.get(key);
        if (map == null) {
            map = new HashMap<>();
            treeProductsMap.put(key, map);
        }
        map.put(output, weight);
    }

    private static int getSaplingTotalWeight(ItemStack saplingIn) {
        int weight = 0;
        HashMap<ItemStack, Integer> saplingMap = treeProductsMap.get(getSaplingName(saplingIn));
        if (saplingMap == null) return 0;
        for (int w : saplingMap.values()) {
            weight += w;
        }
        return weight;
    }

    public static int getInputSlots() {
        return 1;
    }

    public static int getOutputSlots() {
        return 10;
    }

    /**
     * Add a recipe for this tree to NEI. These recipes are only used in NEI, they are never used for processing logic.
     *
     * @return True if the recipe was added successfully.
     */
    public static boolean addFakeRecipeToNEI(ItemStack saplingIn) {
        int recipeCount = GTPPRecipeMaps.thaumoArborealConverterFakeRecipes.getAllRecipes()
            .size();

        // Sapling goes into the "special" slot.
        ItemStack specialStack = saplingIn.copy();
        specialStack.stackSize = 0;

        int totalWeight = getSaplingTotalWeight(saplingIn);
        if (totalWeight <= 0) {
            Logger.INFO("Invalid weight(" + totalWeight + ") for sapling: " + saplingIn.getDisplayName());
            return false;
        }
        ItemStack[] inputStacks = new ItemStack[getInputSlots()];
        ItemStack[] outputStacks = new ItemStack[getOutputSlots()];
        int[] outputChances = new int[getOutputSlots()];

        HashMap<ItemStack, Integer> outputMap = getOutputsForSapling(saplingIn);

        int i = 0;
        for (ItemStack s : getValidInputsForSapling()) {
            inputStacks[i++] = s;
        }
        i = 0;
        for (Map.Entry<ItemStack, Integer> o : outputMap.entrySet()) {
            outputStacks[i] = o.getKey();
            outputChances[i] = 10000 * o.getValue() / totalWeight;
            if (outputChances[i] == 0) outputChances[i] = 1;
            i++;
        }

        GTPPRecipeMaps.thaumoArborealConverterFakeRecipes.addFakeRecipe(
            false,
            new GTRecipe.GTRecipe_WithAlt(
                false,
                inputStacks,
                outputStacks,
                specialStack,
                null,
                outputChances,
                null,
                null,
                null,
                null,
                TICKS_PER_OPERATION,
                0,
                recipeCount, // special value, also sorts recipes correctly in order of addition.
                null));
        return GTPPRecipeMaps.thaumoArborealConverterFakeRecipes.getAllRecipes()
            .size() > recipeCount;
    }
}
