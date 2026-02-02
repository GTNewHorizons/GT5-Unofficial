package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTUtility.validMTEList;
import static gregtech.common.items.IDMetaTool01.BRANCHCUTTER;
import static gregtech.common.items.IDMetaTool01.BUZZSAW_HV;
import static gregtech.common.items.IDMetaTool01.BUZZSAW_LV;
import static gregtech.common.items.IDMetaTool01.BUZZSAW_MV;
import static gregtech.common.items.IDMetaTool01.CHAINSAW_HV;
import static gregtech.common.items.IDMetaTool01.CHAINSAW_LV;
import static gregtech.common.items.IDMetaTool01.CHAINSAW_MV;
import static gregtech.common.items.IDMetaTool01.KNIFE;
import static gregtech.common.items.IDMetaTool01.POCKET_BRANCHCUTTER;
import static gregtech.common.items.IDMetaTool01.POCKET_KNIFE;
import static gregtech.common.items.IDMetaTool01.POCKET_MULTITOOL;
import static gregtech.common.items.IDMetaTool01.POCKET_SAW;
import static gregtech.common.items.IDMetaTool01.POCKET_WIRECUTTER;
import static gregtech.common.items.IDMetaTool01.SAW;
import static gregtech.common.items.IDMetaTool01.WIRECUTTER;
import static gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase.GTPPHatchElement.TTEnergy;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import forestry.api.arboriculture.IToolGrafter;
import forestry.api.arboriculture.ITree;
import forestry.api.arboriculture.TreeManager;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Mods;
import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.VoidProtectionHelper;
import gregtech.common.items.IDMetaTool01;
import gregtech.common.items.MetaGeneratedTool01;
import gregtech.common.pollution.PollutionConfig;
import gregtech.common.tileentities.machines.MTEHatchInputBusME;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.items.MetaGeneratedGregtechTools;
import gtPlusPlus.xmod.gregtech.loaders.recipe.RecipeLoaderTreeFarm;

public class MTETreeFarm extends GTPPMultiBlockBase<MTETreeFarm> implements ISurvivalConstructable {

    public static int CASING_TEXTURE_ID;
    private static final int TICKS_PER_OPERATION = 100;
    private static final int TOOL_DAMAGE_PER_OPERATION = 1;
    private static final int TOOL_CHARGE_PER_OPERATION = 32;

    private int mCasing;
    public static String mCasingName = "Sterile Farm Casing";
    private static IStructureDefinition<MTETreeFarm> STRUCTURE_DEFINITION = null;

    public MTETreeFarm(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
        CASING_TEXTURE_ID = TAE.getIndexFromPage(1, 15);
    }

    public MTETreeFarm(final String aName) {
        super(aName);
        CASING_TEXTURE_ID = TAE.getIndexFromPage(1, 15);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTETreeFarm(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Tree Farm, TGS";
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addInfo("Farms and harvests trees using EU")
            .addInfo("Place a sapling in the controller slot")
            .addInfo("Place a tool in an input bus")
            .addInfo("Different tools are required for different outputs")
            .addInfo("Advanced tools multiply output amount")
            .addInfo("  Logs: Saw (1x), Buzzsaw (2x), Chainsaw (4x)")
            .addInfo("  Saplings: Branch Cutter (1x), Grafter (4x)")
            .addInfo("  Leaves: Shears (1x), Wire Cutter (2x), Automatic Snips (4x)")
            .addInfo("  Fruit: Knife (1x)")
            .addInfo("Multiple tools can be used at the same time")
            .addSeparator()
            .addInfo("Work time is fixed at 5 seconds")
            .addInfo("Energy input tier multiplies output further")
            .addInfo("Output multiplier is equal to: 2*tier^2 - 2*tier + 5")
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(3, 3, 3, true)
            .addController("Front center")
            .addCasingInfoMin(mCasingName, 8, false)
            .addInputBus("Any casing", 1)
            .addStructureInfo(
                EnumChatFormatting.YELLOW + "Stocking Input Buses and Crafting Input Buses/Buffers are not allowed!")
            .addOutputBus("Any casing", 1)
            .addEnergyHatch("Any casing", 1)
            .addMaintenanceHatch("Any casing", 1)
            .addMufflerHatch("Any casing", 1)
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

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        return checkPiece(mName, 1, 1, 0) && mCasing >= 8 && checkHatch();
    }

    @Override
    public boolean addInputBusToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        // Tools from a stocking inout bus can not be damaged, this would cause an infinite durability exploit.
        // Therefore disallow ME input bus.
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEHatchInputBusME) return false;
        return super.addInputBusToMachineList(aTileEntity, aBaseCasingIndex);
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

    @Override
    public IStructureDefinition<MTETreeFarm> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTETreeFarm>builder()
                .addShape(
                    mName,
                    transpose(
                        new String[][] { { "CCC", "CCC", "CCC" }, { "C~C", "C-C", "CCC" }, { "CCC", "CCC", "CCC" }, }))
                .addElement(
                    'C',
                    buildHatchAdder(MTETreeFarm.class)
                        .atLeast(
                            InputHatch,
                            OutputHatch,
                            InputBus,
                            OutputBus,
                            Maintenance,
                            Energy.or(TTEnergy),
                            Muffler)
                        .casingIndex(CASING_TEXTURE_ID)
                        .hint(1)
                        .buildAndChain(onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings2Misc, 15))))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 1, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(mName, stackSize, 1, 1, 0, elementBudget, env, false, true);
    }

    /* Processing logic. */

    @Override
    public boolean isCorrectMachinePart(final ItemStack aStack) {
        if (aStack == null) return false;
        if (isValidSapling(aStack)) return true;
        /*
         * In previous versions, a saw used to go in the controller slot. We do not want an update to stop processing of
         * a machine set up like this. Instead, a sapling is placed in this slot at the start of the next operation.
         */
        return aStack.getItem() instanceof MetaGeneratedTool01;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        // Only for NEI, not used in processing logic.
        return GTPPRecipeMaps.treeGrowthSimulatorFakeRecipes;
    }

    /**
     * Valid processing modes (types of output) for the Tree Growth Simulator.
     */
    public enum Mode {
        LOG,
        SAPLING,
        LEAVES,
        FRUIT
    }

    /**
     * Edit this to change relative yields for different modes. For example, logs are output at 5 times the rate of
     * saplings.
     */
    private static final EnumMap<Mode, Integer> modeMultiplier = new EnumMap<>(Mode.class);
    static {
        modeMultiplier.put(Mode.LOG, 5);
        modeMultiplier.put(Mode.SAPLING, 5);
        modeMultiplier.put(Mode.LEAVES, 2);
        modeMultiplier.put(Mode.FRUIT, 1);
    }

    /**
     * Return the output multiplier for a given power tier.
     *
     * @param tier Power tier the machine runs on.
     * @return Factor to multiply all outputs by.
     */
    private static int getTierMultiplier(int tier) {
        /*
         * Where does this formula come from? [12:57 AM] boubou_19: i did. Basically Pandoro measured the output of a
         * WA-ed farming station for each tier of WA, then i computed the Lagrange interpolating polynomial of his
         * dataset, which gave this
         */
        return (2 * (tier * tier)) - (2 * tier) + 5;
    }

    /**
     * Key of this map is the registry name of the sapling, followed by ":", and the sapling's metadata value.
     * <p>
     * The value of the map is a list of products by {@link Mode}. Products for some modes can be null if the tree does
     * not produce anything in that mode (for example, it has no fruit).
     */
    public static final HashMap<String, EnumMap<Mode, ItemStack>> treeProductsMap = new HashMap<>();

    @Override
    public ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

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

                EnumMap<Mode, ItemStack> outputPerMode = getOutputsForSapling(sapling);
                if (outputPerMode == null) {
                    // This should usually not be possible, outputs for all valid saplings should be defined.
                    Logger.INFO("No output found for sapling: " + sapling.getDisplayName());
                    return SimpleCheckRecipeResult.ofFailure("no_output_for_sapling");
                }

                int tier = Math.max(1, GTUtility.getTier(availableVoltage * availableAmperage));
                int tierMultiplier = getTierMultiplier(tier);

                List<ItemStack> outputs = new ArrayList<>();
                final Mode[] MODE_VALUES = Mode.values();
                for (Mode mode : MODE_VALUES) {
                    ItemStack output = outputPerMode.get(mode);
                    if (output == null) continue; // This sapling has no output in this mode.

                    // Find a tool to use in this mode.
                    int toolMultiplier = useToolForMode(mode, false);
                    if (toolMultiplier < 0) continue; // No valid tool for this mode found.

                    // Increase output by the relevant multipliers.
                    ItemStack out = output.copy();
                    out.stackSize *= tierMultiplier * modeMultiplier.get(mode) * toolMultiplier;
                    outputs.add(out);
                }

                if (outputs.isEmpty()) {
                    // No outputs can be produced using the tools we have available.
                    return SimpleCheckRecipeResult.ofFailure("no_tools");
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

                for (Mode mode : MODE_VALUES) {
                    if (outputPerMode.get(mode) != null) {
                        useToolForMode(mode, true);
                    }
                }

                return SimpleCheckRecipeResult.ofSuccess("growing_trees");
            }
        };
    }

    /**
     * Attempts to find a tool appropriate for the given mode, and damage/discharge it by one use.
     *
     * @param mode         The mode to use. This specifies which tools are valid.
     * @param shouldDamage if true, then the tool will have reduced durability/charge
     */

    private int useToolForMode(Mode mode, boolean shouldDamage) {
        final ArrayList<ItemStack> inputs = getStoredInputs();
        final int inputsSize = inputs.size();
        for (int i = inputsSize - 1; i >= 0; i--) {
            ItemStack stack = inputs.get(i);

            int toolMultiplier = getToolMultiplier(stack, mode);
            if (toolMultiplier < 0) {
                continue;
            }
            boolean canDamage = shouldDamage
                ? GTModHandler.damageOrDechargeItem(stack, TOOL_DAMAGE_PER_OPERATION, TOOL_CHARGE_PER_OPERATION, null)
                : GTModHandler.damageOrDechargeItem(stack, 0, 0, null);
            if (shouldDamage) {
                if (!canDamage || GTModHandler.isElectricItem(stack)
                    && !GTModHandler.canUseElectricItem(stack, TOOL_CHARGE_PER_OPERATION)) {
                    if (addOutputAtomic(stack)) {
                        depleteInput(stack);
                    }
                }
            }
            if (canDamage) {
                return toolMultiplier;
            }
        }
        return -1;
    }

    /**
     * Calculate output multiplier for a given tool and mode.
     *
     * @param toolStack The tool to use.
     * @param mode      The mode to use.
     * @return Output multiplier for the given tool used in the given mode. If the tool is not appropriate for this
     *         mode, returns -1.
     */
    public static int getToolMultiplier(ItemStack toolStack, Mode mode) {
        Item tool = toolStack.getItem();
        int damage = toolStack.getItemDamage();
        switch (mode) {
            case LOG:
                if (tool instanceof MetaGeneratedTool01) {

                    if (damage == SAW.ID || damage == POCKET_SAW.ID || damage == POCKET_MULTITOOL.ID) {
                        return 1;
                    }

                    if (damage == BUZZSAW_LV.ID || damage == BUZZSAW_MV.ID || damage == BUZZSAW_HV.ID) {
                        return 2;
                    }

                    if (damage == CHAINSAW_LV.ID || damage == CHAINSAW_MV.ID || damage == CHAINSAW_HV.ID) {
                        return 4;
                    }
                }
                break;

            case SAPLING:
                if (tool instanceof MetaGeneratedTool01
                    && (damage == BRANCHCUTTER.ID || damage == POCKET_BRANCHCUTTER.ID
                        || damage == POCKET_MULTITOOL.ID)) {
                    return 1;
                }
                if (Forestry.isModLoaded() && tool instanceof IToolGrafter && tool.isDamageable()) {
                    return 4;
                }
                break;

            case LEAVES:
                // Do not allow unbreakable tools. Operation should have a running cost.
                if (tool instanceof ItemShears && tool.isDamageable()) {
                    return 1;
                }
                if (tool instanceof MetaGeneratedTool01) {
                    if (damage == POCKET_MULTITOOL.ID) {
                        return 1;
                    }
                    if (damage == WIRECUTTER.ID || damage == POCKET_WIRECUTTER.ID) {
                        return 2;
                    }
                }
                if (tool instanceof MetaGeneratedGregtechTools) {
                    if (toolStack.getItemDamage() == MetaGeneratedGregtechTools.ELECTRIC_SNIPS) {
                        return 4;
                    }
                }
                break;

            case FRUIT:
                if (tool instanceof MetaGeneratedTool01
                    && (damage == KNIFE.ID || damage == POCKET_KNIFE.ID || damage == POCKET_MULTITOOL.ID)) {
                    return 1;
                }
                break;
        }

        // No valid tool was found.
        return -1;
    }

    /* Handling saplings. */

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
            // Non-sapling item in controller slot. This could be a saw from an older version of the TGS.
            // We first try to swap it with a sapling from an input bus to not interrupt existing setups.
            if (!legacyToolSwap()) {
                // Swap failed, output whatever is blocking the slot.
                if (addOutputAtomic(controllerSlot)) {
                    mInventory[1] = null;
                } else {
                    return null;
                }
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

    /**
     * In previous versions, the saw used to be placed in the controller slot and the sapling into an input bus. We do
     * not want to break existing setups like this, so we attempt to swap the two if possible.
     *
     * @return True on success, false otherwise.
     */
    private boolean legacyToolSwap() {
        ItemStack controllerSlot = getControllerSlot();
        if (controllerSlot == null || !(controllerSlot.getItem() instanceof MetaGeneratedTool01)) return false;

        for (MTEHatchInputBus inputBus : validMTEList(mInputBusses)) {
            ItemStack[] inventory = inputBus.getRealInventory();
            for (int slot = 0; slot < inventory.length; ++slot) {
                if (isValidSapling(inventory[slot])) {
                    // Do the swap.
                    mInventory[1] = inventory[slot];
                    inventory[slot] = controllerSlot;
                    inputBus.updateSlots();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Check if an ItemStack is a sapling that can be farmed.
     *
     * @param stack An ItemStack.
     * @return True if stack is a valid sapling that can be farmed.
     */
    private boolean isValidSapling(ItemStack stack) {
        if (stack == null) return false;
        String registryName = Item.itemRegistry.getNameForObject(stack.getItem());
        return treeProductsMap.containsKey(registryName + ":" + stack.getItemDamage())
            || "Forestry:sapling".equals(registryName);
    }

    /**
     * Get a list of possible outputs for a sapling, for each mode. This is either recovered from
     * {@link #treeProductsMap}, or generated from stats of Forestry saplings.
     *
     * @param sapling A sapling to farm.
     * @return A map of outputs for each mode. Outputs for some modes might be null.
     */
    private static EnumMap<Mode, ItemStack> getOutputsForSapling(ItemStack sapling) {
        String registryName = Item.itemRegistry.getNameForObject(sapling.getItem());
        if (Forestry.isModLoaded() && "Forestry:sapling".equals(registryName)) {
            return getOutputsForForestrySapling(sapling);
        } else {
            return treeProductsMap.get(registryName + ":" + sapling.getItemDamage());
        }
    }

    /**
     * Calculate outputs for Forestry saplings. Default amounts stored in {@link #treeProductsMap} are adjusted based
     * the genetics of the input sapling.
     * <p>
     * Relevant stats:
     * <ul>
     * <li>height, girth: Affects log output.</li>
     * <li>fertility (called Saplings in game): Affects sapling output.</li>
     * <li>yield: Affects fruit output.</li>
     * </ul>
     * See {@link forestry.core.genetics.alleles.EnumAllele} for detailed numeric values for each allele.
     *
     * @param sapling A sapling to farm. Must be a Forestry sapling with a valid genome.
     * @return A map of outputs for each mode. Outputs for some modes might be null.
     */
    private static EnumMap<Mode, ItemStack> getOutputsForForestrySapling(ItemStack sapling) {
        ITree tree = TreeManager.treeRoot.getMember(sapling);
        if (tree == null) return null;

        String speciesUUID = tree.getIdent();

        EnumMap<Mode, ItemStack> defaultMap = treeProductsMap.get("Forestry:sapling:" + speciesUUID);
        if (defaultMap == null) return null;

        // We need to make a new map so that we don't modify the stored amounts of outputs.
        EnumMap<Mode, ItemStack> adjustedMap = new EnumMap<>(Mode.class);

        ItemStack log = defaultMap.get(Mode.LOG);
        if (log != null) {
            double height = Math.max(
                3 * (tree.getGenome()
                    .getHeight() - 1),
                0) + 1;
            double girth = tree.getGenome()
                .getGirth();

            log = log.copy();
            log.stackSize = (int) (log.stackSize * height * girth);
            adjustedMap.put(Mode.LOG, log);
        }

        ItemStack saplingOut = defaultMap.get(Mode.SAPLING);
        if (saplingOut != null) {
            // Lowest = 0.01 ... Average = 0.05 ... Highest = 0.3
            double fertility = tree.getGenome()
                .getFertility() * 10;

            // Return a copy of the *input* sapling, retaining its genetics.
            int stackSize = Math.max(1, (int) (saplingOut.stackSize * fertility));
            saplingOut = sapling.copy();
            saplingOut.stackSize = stackSize;
            adjustedMap.put(Mode.SAPLING, saplingOut);
        }

        ItemStack leaves = defaultMap.get(Mode.LEAVES);
        if (leaves != null) {
            adjustedMap.put(Mode.LEAVES, leaves.copy());
        }

        ItemStack fruit = defaultMap.get(Mode.FRUIT);
        if (fruit != null) {
            // Lowest = 0.025 ... Average = 0.2 ... Highest = 0.4
            double yield = tree.getGenome()
                .getYield() * 10;

            fruit = fruit.copy();
            fruit.stackSize = (int) (fruit.stackSize * yield);
            adjustedMap.put(Mode.FRUIT, fruit);
        }

        return adjustedMap;
    }

    /* Recipe registration. */

    /**
     * Registers outputs for a sapling. This method assumes that output in mode SAPLING is the same as the input
     * sapling. Output amount is further modified by mode, machine tier, and tool used. Recipes are added in
     * {@link RecipeLoaderTreeFarm}.
     *
     * @param sapling The input sapling to farm, and also the output in mode SAPLING.
     * @param log     ItemStack to output in mode LOG.
     * @param leaves  ItemStack to output in mode LEAVES.
     * @param fruit   ItemStack to output in mode FRUIT.
     */
    public static void registerTreeProducts(ItemStack sapling, ItemStack log, ItemStack leaves, ItemStack fruit) {
        registerTreeProducts(sapling, log, sapling, leaves, fruit);
    }

    /**
     * Registers outputs for a sapling. Output amount is further modified by mode, machine tier, and tool used. Recipes
     * are added in {@link RecipeLoaderTreeFarm}.
     *
     * @param saplingIn  The input sapling to farm.
     * @param log        ItemStack to output in mode LOG.
     * @param saplingOut ItemStack to output in mode SAPLING.
     * @param leaves     ItemStack to output in mode LEAVES.
     * @param fruit      ItemStack to output in mode FRUIT.
     */
    public static void registerTreeProducts(ItemStack saplingIn, ItemStack log, ItemStack saplingOut, ItemStack leaves,
        ItemStack fruit) {
        if (saplingIn == null) {
            Logger.ERROR("Null sapling passed for registerTreeProducts()");
            return;
        }
        String key = Item.itemRegistry.getNameForObject(saplingIn.getItem()) + ":" + saplingIn.getItemDamage();
        EnumMap<Mode, ItemStack> map = new EnumMap<>(Mode.class);
        if (log != null) map.put(Mode.LOG, log);
        if (saplingOut != null) map.put(Mode.SAPLING, saplingOut);
        if (leaves != null) map.put(Mode.LEAVES, leaves);
        if (fruit != null) map.put(Mode.FRUIT, fruit);
        treeProductsMap.put(key, map);

        if (!addFakeRecipeToNEI(saplingIn, log, saplingOut, leaves, fruit)) {
            Logger.INFO("Registering NEI fake recipe for " + key + " failed!");
        }
    }

    /**
     * For Forestry trees, the output amounts depend on the genetics of the sapling. Here we register only the types of
     * items to output. In {@link #getOutputsForForestrySapling(ItemStack)} these outputs are then multiplied according
     * to the stats of the real sapling that is in the controller slot.
     */
    public static void registerForestryTree(String speciesUID, ItemStack sapling, ItemStack log, ItemStack leaves,
        ItemStack fruit) {
        String key = "Forestry:sapling:" + speciesUID;
        EnumMap<Mode, ItemStack> map = new EnumMap<>(Mode.class);
        map.put(Mode.LOG, log);
        map.put(Mode.SAPLING, sapling);
        map.put(Mode.LEAVES, leaves);
        map.put(Mode.FRUIT, fruit);
        treeProductsMap.put(key, map);

        // In the NEI recipe we want to display outputs adjusted for the default genetics of this tree type.
        // To do this we use the same method as when calculating real outputs.
        map = getOutputsForForestrySapling(sapling);
        if (map == null) {
            Logger.INFO("Could not create Forestry tree output map for " + speciesUID);
            return;
        }
        addFakeRecipeToNEI(
            sapling,
            map.get(Mode.LOG),
            map.get(Mode.SAPLING),
            map.get(Mode.LEAVES),
            map.get(Mode.FRUIT));
    }

    /**
     * This array is used to get the rotating display of items in NEI showing all possible tools for a given mode.
     */
    private static final ItemStack[][] altToolsForNEI;
    static {
        MetaGeneratedTool toolInstance = MetaGeneratedTool01.INSTANCE;
        altToolsForNEI = new ItemStack[][] {
            // Mode.LOG
            { toolInstance.getToolWithStats(SAW.ID, 1, null, null, null),
                toolInstance.getToolWithStats(POCKET_SAW.ID, 1, null, null, null),
                toolInstance.getToolWithStats(IDMetaTool01.BUZZSAW_LV.ID, 1, null, null, null),
                toolInstance.getToolWithStats(CHAINSAW_LV.ID, 1, null, null, null),
                toolInstance.getToolWithStats(IDMetaTool01.BUZZSAW_MV.ID, 1, null, null, null),
                toolInstance.getToolWithStats(CHAINSAW_MV.ID, 1, null, null, null),
                toolInstance.getToolWithStats(IDMetaTool01.BUZZSAW_HV.ID, 1, null, null, null),
                toolInstance.getToolWithStats(CHAINSAW_HV.ID, 1, null, null, null), },
            // Mode.SAPLING
            { toolInstance.getToolWithStats(IDMetaTool01.BRANCHCUTTER.ID, 1, null, null, null),
                toolInstance.getToolWithStats(IDMetaTool01.POCKET_BRANCHCUTTER.ID, 1, null, null, null),
                GTModHandler.getModItem(Mods.Forestry.ID, "grafter", 1, 0), },
            // Mode.LEAVES
            { new ItemStack(Items.shears),
                toolInstance.getToolWithStats(IDMetaTool01.WIRECUTTER.ID, 1, null, null, null),
                toolInstance.getToolWithStats(IDMetaTool01.POCKET_WIRECUTTER.ID, 1, null, null, null),
                MetaGeneratedGregtechTools.getInstance()
                    .getToolWithStats(MetaGeneratedGregtechTools.ELECTRIC_SNIPS, 1, null, null, null), },
            // Mode.FRUIT
            { toolInstance.getToolWithStats(IDMetaTool01.KNIFE.ID, 1, null, null, null),
                toolInstance.getToolWithStats(IDMetaTool01.POCKET_KNIFE.ID, 1, null, null, null), } };
    }

    /**
     * Add a recipe for this tree to NEI. These recipes are only used in NEI, they are never used for processing logic.
     *
     * @return True if the recipe was added successfully.
     */
    public static boolean addFakeRecipeToNEI(ItemStack saplingIn, ItemStack log, ItemStack saplingOut, ItemStack leaves,
        ItemStack fruit) {
        int recipeCount = GTPPRecipeMaps.treeGrowthSimulatorFakeRecipes.getAllRecipes()
            .size();

        // Sapling goes into the "special" slot.
        ItemStack specialStack = saplingIn.copy();
        specialStack.stackSize = 0;

        /*
         * Calculate the correct amount of outputs for each mode. The amount displayed in NEI should take into account
         * the mode multiplier, but not tool/tier multipliers as those can change dynamically. If the sapling has an
         * output in this mode, also add the tools usable for this mode as inputs.
         */
        final Mode[] MODE_VALUES = Mode.values();
        ItemStack[][] inputStacks = new ItemStack[MODE_VALUES.length][];
        ItemStack[] outputStacks = new ItemStack[MODE_VALUES.length];

        for (Mode mode : MODE_VALUES) {
            ItemStack output = switch (mode) {
                case LOG -> log;
                case SAPLING -> saplingOut;
                case LEAVES -> leaves;
                case FRUIT -> fruit;
            };
            if (output != null) {
                int ordinal = mode.ordinal();
                inputStacks[ordinal] = altToolsForNEI[ordinal];
                outputStacks[ordinal] = output.copy();
                outputStacks[ordinal].stackSize *= modeMultiplier.get(mode);
            }
        }

        Logger.INFO(
            "Adding Tree Growth Simulation NEI recipe for " + specialStack.getDisplayName()
                + " -> "
                + ItemUtils.getArrayStackNames(outputStacks));

        GTPPRecipeMaps.treeGrowthSimulatorFakeRecipes.addFakeRecipe(
            false,
            new GTRecipe.GTRecipe_WithAlt(
                null, // All inputs are taken from aAtl argument.
                outputStacks,
                specialStack,
                null,
                null,
                null,
                TICKS_PER_OPERATION,
                0,
                recipeCount, // special value, also sorts recipes correctly in order of addition.
                inputStacks));

        return GTPPRecipeMaps.treeGrowthSimulatorFakeRecipes.getAllRecipes()
            .size() > recipeCount;
    }
}
