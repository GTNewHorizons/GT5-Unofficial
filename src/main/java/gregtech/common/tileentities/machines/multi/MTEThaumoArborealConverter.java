
import static com.gtnewhorizon.structurelib.structure.StructureUtility.isAir;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;

import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;

import java.util.Lists

import gregtech.api.enums.TAE;
import gregtech.api.GregTechAPI;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;

public class MTEThaumoArborealConverter extends GTPPMultiBlockBase<MTEThaumoArborealConverter> implements ISurvivalConstructable {
    
    private static final int TICKS_PER_OPERATION = 100;
    private static final int BASE_PRODUCTION = 10;
    private static final int FRAME_NUM = 56;
    
    private int mCasing;
    private int mFrame1, mFrame2, mFrame3, mFrame4;
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
            .addInfo("Consumes relevant tree materials to boost production")
            .addInfo("More advanced structural frames boost production and reduce tree material consumption")
            .addInfo("  Thaumium: 1x, 100%")
            .addInfo("  Shadow Metal: 2x, 75%")
            .addInfo("  Ichorium: 4x, 50%")
            .addInfo("  Infinity: 8x, 25%")
            .addSeparator()
            .addInfo("Work time is fixed at 5 seconds")
            .addInfo("Energy hatch limited by glass tier")
            .addInfo("Energy input tier multiplies output further")
            .addInfo("Output multiplier is equal to: 2*tier^2 - 2*tier + 5")
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(9, 8, 9, true)
            .addController("Front center")
            .addCasingInfoMin(mCasingName, 70, false)
            .addCasingInfoExactly("Any Tiered Glass (tiered)", 145, true)
            .addCasingInfoExactly("Frame Box (tiered)", FRAME_NUM, true)
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
        this.frameTier = this.getFrameTier();
        this.glassTier = 0;
        if (!this.checkPiece(mName, 4, 6, 0) || this.mCasing < 70 || !this.checkHatches() || this.frameTier == 0)
            return false;
        for (MTEHatchEnergy mEnergyHatch : this.mEnergyHatches) {
            if (this.glassTier < mEnergyHatch.mTier) {
                return false;
            }
        }
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
    
    private static final IStructureDefinition<MTEThaumoArborealConverter> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEThaumoArborealConverter>builder()
        .addShape(
            mName,
            transpose(
                new String[][] { // spotless:off
                    { "ccccc", "ccccc", "ccccc", "ccccc", "ccccc" },
                    { "  fffff  ", " fgggggf ", "fgggggggf", "fgggggggf", "fgggggggf", "fgggggggf", "fgggggggf", " fgggggf ", "  fffff  "},
                    { "   ggg   ", " fg---gf ", " g-----g ", "g-------g", "g-------g", "g-------g", " g-----g ", " fg---gf ", "   ggg   "},
                    { "   ggg   ", " fg---gf ", " g-----g ", "g-------g", "g-------g", "g-------g", " g-----g ", " fg---gf ", "   ggg   "},
                    { "   ggg   ", " fg---gf ", " g-----g ", "g-------g", "g-------g", "g-------g", " g-----g ", " fg---gf ", "   ggg   "},
                    { "   ggg   ", " fg---gf ", " g-----g ", "g-------g", "g-------g", "g-------g", " g-----g ", " fg---gf ", "   ggg   "},
                    { "   ggg   ", " fg---gf ", " g-----g ", "g-------g", "g-------g", "g-------g", " g-----g ", " fg---gf ", "   ggg   "},
                    { "   c~c   ", " cc-f-cc ", " c--f--c ", "c---f---c", "cfffdfffc", "c---f---c", " c--f--c ", " cc-f-cc ", "   ccc   "},
                    { "   ccc   ", " ccCCCcc ", " cCCCCCc ", "cCCCCCCCc", "cCCCCCCCc", "cCCCCCCCc", " cCCCCCc ", " ccCCCcc ", "   ccc   "},
                })) // spotless:on
        .addElement(
            'c',
            buildHatchAdder(MTEThaumoArborealConverter.class)
                .atLeast(InputBus, OutputBus, Energy, Maintenance)
                .casingIndex(CASING.textureId)
                .hint(1)
                .buildAndChain(onElementPass(t -> t.mCasing++, Casings.SterileFarmCasing.asElement())))
        .addElement(
            'f', 
            ofChain(
                onElementPass(t -> ++t.mFrame1, ofBlock(GregTechAPI.sBlockFrames, 330)), // thaumium
                onElementPass(t -> ++t.mFrame2, ofBlock(GregTechAPI.sBlockFrames, 368)), // shadow metal
                onElementPass(t -> ++t.mFrame3, ofBlock(GregTechAPI.sBlockFrames, 978)), // ichorium
                onElementPass(t -> ++t.mFrame4, ofBlock(GregTechAPI.sBlockFrames, 397))  // infinity
            ))
        .addElement('g', chainAllGlasses(-1, (te, t) -> te.glassTier = t.byteValue(), te -> (int) te.glassTier))
        .addElement('d', ofChain(ofBlock(TILLED_DIRT_BLOCK, 0), ofBlock(DIRT_BLOCK, 0)))
        .addElement('C', onElementPass(t -> t.mCasing++, Casings.SterileFarmCasing.asElement()))
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
        
        private static getFrameTier(){
            if (this.mFrame1 == FRAME_NUM) return 1;
            if (this.mFrame2 == FRAME_NUM) return 2;
            if (this.mFrame3 == FRAME_NUM) return 3;
            if (this.mFrame4 == FRAME_NUM) return 4;
            return 0;
        }
        
        private static int getFrameMultiplier() {
            return Math.pow(2, this.frameTier - 1);
        }
        
        private static double getFrameConsumption() {
            return 100 - ((this.frameTier - 1) * 25);
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
                    if (outputPerMode == null) {
                        Logger.INFO("No output found for sapling: " + sapling.getDisplayName());
                        return SimpleCheckRecipeResult.ofFailure("no_output_for_sapling");
                    }
                    
                    int tier = Math.max(1, GTUtility.getTier(availableVoltage * availableAmperage));
                    int tierMultiplier = getTierMultiplier(tier);
                    int tieredProduction = BASE_PRODUCTION * getFrameMultiplier() * tierMultiplier;
                    int consumedInput = consumeInput(tieredProduction);
                    int extraOutput = consumeCatalyst(tieredProduction, consumedInput);
                    int totalOutput = consumedInput + extraOutput;
                    int totalWeight = getSaplingTotalWeight(sapling);
    
                    List<ItemStack> outputs = new ArrayList<>();
                    for (ItemStack output : outputMap){
                        ItemStack out = output.copy();
                        //w???
                        int chance = 10000 * totalWeight / outputMap.get(output)
                        // non-random
                        //out.stackSize = totalOutput * totalWeight / outputMap.get(output);
                        // random - normal approximation
                        out.stackSize
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
        
        private static int consumeInput(int tieredProduction){
            // consume input stone and dirt up to tieredProduction
        }
        
        private static int consumeCatalyst(int tieredProduction, int consumedInput){
            // consume input tree materials weighted up to consumedInput
            int wgtLog = 4;
            int wgtSapling = 2;
            int wgtLeaves = 1;
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
        
        private static string getSaplingName(ItemStack sapling){
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
        private static EnumMap<Mode, ItemStack> getOutputsForSapling(ItemStack sapling) {
            return treeProductsMap.get(getSaplingName(sapling));
        }
        
        /**
         * Registers outputs for a sapling. Output amount is further modified by catalyst consumption and frame and power tier. Recipes
         * are added in {@link RecipeLoaderTACo}.
         *
         * @param saplingIn  The input sapling to farm.
         * @param output     ItemStack to output
         * @param weight     proportional weight of output, these should add up to 100 across all outputs of a sapling
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
                map = new HashMap<>(ItemStack.class);
                treeProductsMap.put(key, map);
            }
            map.put(output, weight);
        }
        
        private static int getSaplingTotalWeight(ItemStack saplingIn){
            int weight = 0;
            HashMap<ItemStack, Integer> saplingMap = treeProductsMap.get(getSaplingName(saplingIn));
            if (saplingMap == null) return 0;
            for (int w : saplingMap.values()){
                weight += w;
            }
            return weight;
        }
        
        public static int getInputSlots() return 1;
        public static int getOutputSlots() return 10;
        
        /**
         * Add a recipe for this tree to NEI. These recipes are only used in NEI, they are never used for processing logic.
         *
         * @return True if the recipe was added successfully.
         */
        public static boolean addFakeRecipeToNEI(ItemStack saplingIn, ItemStack inputStack, HashMap<ItemStack, Integer> outputMap){
            int recipeCount = GTPPRecipeMaps.thaumoArborealConverterFakeRecipes.getAllRecipes()
                .size();
            
            // Sapling goes into the "special" slot.
            ItemStack specialStack = saplingIn.copy();
            specialStack.stackSize = 0;
            
            int totalWeight = getSaplingTotalWeight(saplingIn);
            if (totalWeight <= 0){
                Logger.INFO("Invalid weight(" + totalWeight + ") for sapling: " + sapling.getDisplayName());
                return False;
            }
            ItemStack[] outputStacks = new ItemStack[getOutputSlots()];
            int[] outputChances = new int[getOutputSlots()];
            
            int i = 0;
            for (Map.Entry<ItemStack, Integer> o : outputMap.entrySet()) {
                outputStacks[i] = o.getKey();
                //w???
                outputChances[i] = 10000*o.getValue()/totalWeight;
                if (outputChances[i] == 0) outputChances[i] = 1;
                i++;
            }
            
            GTPPRecipeMaps.thaumoArborealConverterFakeRecipes.addFakeRecipe(
                false,
                new GTRecipe.GTRecipe_WithAlt(
                    false,
                    saplingIn,
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
            return GTPPRecipeMaps.thaumoArborealConverterFakeRecipes
                .getAllRecipes()
                .size() > recipeCount;
        }
}