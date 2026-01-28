package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ORE_FACTORY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ORE_FACTORY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ORE_FACTORY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ORE_FACTORY_GLOW;
import static gregtech.api.enums.TickTime.SECOND;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.simpleWasherRecipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEIntegratedOreFactory extends MTEExtendedPowerMultiBlockBase<MTEIntegratedOreFactory>
    implements ISurvivalConstructable {

    private static final int CASING_INDEX1 = 183;
    private static final int CASING_INDEX2 = 49;
    private static final int MAX_PARA = 1024;
    private static final long RECIPE_EUT = 30;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTEIntegratedOreFactory> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEIntegratedOreFactory>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(
                new String[][] {
                    { "           ", "           ", "       WWW ", "       WWW ", "           ", "           " },
                    { "           ", "       sss ", "      sppps", "      sppps", "       sss ", "           " },
                    { "           ", "       sss ", "      s   s", "      s   s", "       sss ", "           " },
                    { "           ", "       sss ", "      sppps", "      sppps", "       sss ", "           " },
                    { "           ", "       sss ", "      s   s", "      s   s", "       sss ", "           " },
                    { "           ", "       sss ", "      sppps", "      sppps", "       sss ", "           " },
                    { "iiiiii     ", "iIIIIiisssi", "iIIIIis   s", "iIIIIis   s", "iIIIIiisssi", "iiiiii     " },
                    { "iggggi     ", "gt  t isssi", "g xx  sppps", "g xx  sppps", "gt  t isssi", "iggggi     " },
                    { "iggggi     ", "gt  t isssi", "g xx  s   s", "g xx  s   s", "gt  t isssi", "iggggi     " },
                    { "iggggi     ", "gt  t is~si", "g xx  spppO", "g xx  spppO", "gt  t isssi", "iggggi     " },
                    { "iggggi     ", "gt  t isssi", "g xx  s   O", "g xx  s   O", "gt  t isssi", "iggggi     " },
                    { "EEEEEE     ", "EEEEEEEEEEE", "EEEEEEEEEEE", "EEEEEEEEEEE", "EEEEEEEEEEE", "EEEEEE     " } }))
        .addElement('i', ofBlock(GregTechAPI.sBlockCasings8, 7))
        .addElement('s', ofBlock(GregTechAPI.sBlockCasings4, 1))
        .addElement('g', chainAllGlasses())
        .addElement('x', ofBlock(GregTechAPI.sBlockCasings2, 3))
        .addElement('p', ofBlock(GregTechAPI.sBlockCasings2, 15))
        .addElement('t', ofFrame(Materials.TungstenSteel))
        .addElement(
            'E',
            buildHatchAdder(MTEIntegratedOreFactory.class).atLeast(Energy, Maintenance)
                .casingIndex(CASING_INDEX1)
                .hint(1)
                .buildAndChain(GregTechAPI.sBlockCasings8, 7))
        .addElement(
            'I',
            buildHatchAdder(MTEIntegratedOreFactory.class).atLeast(InputBus)
                .casingIndex(CASING_INDEX1)
                .hint(2)
                .buildAndChain(GregTechAPI.sBlockCasings8, 7))
        .addElement(
            'W',
            buildHatchAdder(MTEIntegratedOreFactory.class).atLeast(InputHatch, Muffler)
                .casingIndex(CASING_INDEX2)
                .hint(3)
                .buildAndChain(GregTechAPI.sBlockCasings4, 1))
        .addElement(
            'O',
            buildHatchAdder(MTEIntegratedOreFactory.class).atLeast(OutputBus, OutputHatch)
                .casingIndex(CASING_INDEX2)
                .hint(4)
                .buildAndChain(GregTechAPI.sBlockCasings4, 1))
        .build();

    private static final IntOpenHashSet isCrushedOre = new IntOpenHashSet();
    private static final IntOpenHashSet isCrushedPureOre = new IntOpenHashSet();
    private static final IntOpenHashSet isPureDust = new IntOpenHashSet();
    private static final IntOpenHashSet isImpureDust = new IntOpenHashSet();
    private static final IntOpenHashSet isThermal = new IntOpenHashSet();
    private static final IntOpenHashSet isOre = new IntOpenHashSet();
    private static boolean isInit = false;
    private ItemStack[] sMidProduct;
    private int sMode = 0;
    private boolean sVoidStone = false;
    private int currentParallelism = 0;

    @SuppressWarnings("ForLoopReplaceableByForEach")
    private static void initHash() {
        for (String name : OreDictionary.getOreNames()) {
            if (name == null || name.isEmpty()) continue;
            if (name.startsWith("crushedPurified")) {
                ArrayList<ItemStack> ores = OreDictionary.getOres(name);
                for (int i = 0, size = ores.size(); i < size; i++) {
                    isCrushedPureOre.add(GTUtility.stackToInt(ores.get(i)));
                }
            } else if (name.startsWith("crushedCentrifuged")) {
                ArrayList<ItemStack> ores = OreDictionary.getOres(name);
                for (int i = 0, size = ores.size(); i < size; i++) {
                    isThermal.add(GTUtility.stackToInt(ores.get(i)));
                }
            } else if (name.startsWith("crushed")) {
                ArrayList<ItemStack> ores = OreDictionary.getOres(name);
                for (int i = 0, size = ores.size(); i < size; i++) {
                    isCrushedOre.add(GTUtility.stackToInt(ores.get(i)));
                }
            } else if (name.startsWith("dustImpure")) {
                ArrayList<ItemStack> ores = OreDictionary.getOres(name);
                for (int i = 0, size = ores.size(); i < size; i++) {
                    isImpureDust.add(GTUtility.stackToInt(ores.get(i)));
                }
            } else if (name.startsWith("dustPure")) {
                ArrayList<ItemStack> ores = OreDictionary.getOres(name);
                for (int i = 0, size = ores.size(); i < size; i++) {
                    isPureDust.add(GTUtility.stackToInt(ores.get(i)));
                }
            } else if (name.startsWith("ore")) {
                ArrayList<ItemStack> ores = OreDictionary.getOres(name);
                for (int i = 0, size = ores.size(); i < size; i++) {
                    isOre.add(GTUtility.stackToInt(ores.get(i)));
                }
            } else if (name.startsWith("rawOre")) {
                ArrayList<ItemStack> ores = OreDictionary.getOres(name);
                for (int i = 0, size = ores.size(); i < size; i++) {
                    isOre.add(GTUtility.stackToInt(ores.get(i)));
                }
            }
        }
    }

    // Not GPL
    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

    public MTEIntegratedOreFactory(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEIntegratedOreFactory(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTEIntegratedOreFactory> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Ore Processor, IOF")
            .addInfo("Does all ore processing in one step")
            .addStaticParallelInfo(1024)
            .addInfo("Every ore costs 30EU/t, 2L lubricant, 200L distilled water")
            .addInfo("Recipes that need extra input require their extra inputs on top of the normal costs")
            .addInfo("Processing time is dependent on mode")
            .addInfo("Use a screwdriver to switch mode")
            .addInfo("Sneak click with screwdriver to void the stone dust")
            .addPollutionAmount(getPollutionPerSecond(null))
            .addSeparator()
            .addInfo(EnumChatFormatting.GREEN + "OP stands for Ore Processor ;)")
            .beginStructureBlock(6, 12, 11, false)
            .addController("The third layer")
            .addCasingInfoExactly("Advanced Iridium Plated Machine Casing", 128, false)
            .addCasingInfoExactly("Clean Stainless Steel Machine Casing", 105, false)
            .addCasingInfoExactly("Any Tiered Glass", 48, false)
            .addCasingInfoExactly("Tungstensteel Pipe Casing", 30, false)
            .addCasingInfoExactly("Tungstensteel Frame Box", 16, false)
            .addCasingInfoExactly("Steel Gear Box Casing", 16, false)
            .addEnergyHatch("Any bottom Casing", 1)
            .addMaintenanceHatch("Any bottom Casing", 1)
            .addInputBus("Input ore/crushed ore", 2)
            .addInputHatch("Input lubricant/distilled water/washing chemicals", 3)
            .addMufflerHatch("Output Pollution", 3)
            .addOutputBus("Output products", 4)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        buildPiece(STRUCTURE_PIECE_MAIN, itemStack, b, 8, 9, 1);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 8, 9, 1, elementBudget, env, false, true);
    }

    private static int getTime(int mode) {
        return switch (mode) {
            case 0 -> 30 * SECOND;
            case 1 -> 15 * SECOND;
            case 2 -> 10 * SECOND;
            case 3 -> 20 * SECOND;
            case 4 -> 17 * SECOND;
            case 5 -> 32 * SECOND;
            case 6 -> 1 * SECOND;
            default ->
                // go to hell
                1000000000;
        };
    }

    @Override
    @NotNull
    @SuppressWarnings("ForLoopReplaceableByForEach")
    public CheckRecipeResult checkProcessing() {
        if (!isInit) {
            initHash();
            isInit = true;
        }

        ArrayList<ItemStack> tInput = getStoredInputs();
        ArrayList<FluidStack> tInputFluid = getStoredFluids();
        if (tInput.isEmpty() || tInputFluid.isEmpty()) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }
        long availableEUt = GTUtility.roundUpVoltage(getMaxInputVoltage());
        if (availableEUt < RECIPE_EUT) {
            return CheckRecipeResultRegistry.insufficientPower(RECIPE_EUT);
        }

        int maxParallel = MAX_PARA;
        int originalMaxParallel = maxParallel;

        OverclockCalculator calculator = new OverclockCalculator().setEUt(availableEUt)
            .setRecipeEUt(RECIPE_EUT)
            .setDuration(getTime(sMode))
            .setParallel(originalMaxParallel);

        maxParallel = GTUtility.safeInt((long) (maxParallel * calculator.calculateMultiplierUnderOneTick()), 0);

        int maxParallelBeforeBatchMode = maxParallel;
        if (isBatchModeEnabled()) {
            maxParallel = GTUtility.safeInt((long) maxParallel * getMaxBatchSize(), 0);
        }

        int currentParallel = (int) Math.min(maxParallel, availableEUt / RECIPE_EUT);
        // Calculate parallel by fluids
        int tLube = 0;
        int tWater = 0;
        for (int i = 0, size = tInputFluid.size(); i < size; i++) {
            FluidStack fluid = tInputFluid.get(i);
            if (fluid != null && fluid.equals(GTModHandler.getDistilledWater(1L))) {
                tWater += fluid.amount;
            } else if (fluid != null && fluid.equals(Materials.Lubricant.getFluid(1L))) {
                tLube += fluid.amount;
            }
        }
        currentParallel = Math.min(currentParallel, tLube / 2);
        currentParallel = Math.min(currentParallel, tWater / 200);
        if (currentParallel <= 0) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        // Calculate parallel by items
        int itemParallel = 0;
        for (int i = 0, size = tInput.size(); i < size; i++) {
            ItemStack ore = tInput.get(i);
            int tID = GTUtility.stackToInt(ore);
            if (tID == 0) continue;
            if (isPureDust.contains(tID) || isImpureDust.contains(tID)
                || isCrushedPureOre.contains(tID)
                || isThermal.contains(tID)
                || isCrushedOre.contains(tID)
                || isOre.contains(tID)) {
                if (itemParallel + ore.stackSize <= currentParallel) {
                    itemParallel += ore.stackSize;
                } else {
                    itemParallel = currentParallel;
                    break;
                }
            }
        }
        currentParallel = itemParallel;
        if (currentParallel <= 0) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }
        int currentParallelBeforeBatchMode = Math.min(currentParallel, maxParallelBeforeBatchMode);

        calculator.setCurrentParallel(currentParallelBeforeBatchMode)
            .calculate();

        double batchMultiplierMax = 1;
        // In case batch mode enabled
        if (currentParallel > maxParallelBeforeBatchMode && calculator.getDuration() < getMaxBatchSize()) {
            batchMultiplierMax = (double) getMaxBatchSize() / calculator.getDuration();
            batchMultiplierMax = Math.min(batchMultiplierMax, (double) currentParallel / maxParallelBeforeBatchMode);
        }

        int finalParallel = (int) (batchMultiplierMax * currentParallelBeforeBatchMode);
        lastParallel = finalParallel;

        // for scanner
        setCurrentParallelism(finalParallel);

        // Consume fluids
        depleteInput(GTModHandler.getDistilledWater(finalParallel * 200L));
        depleteInput(Materials.Lubricant.getFluid(finalParallel * 2L));

        // Consume items and generate outputs
        List<ItemStack> tOres = new ArrayList<>();
        int remainingCost = finalParallel;
        for (int i = 0, size = tInput.size(); i < size; i++) {
            ItemStack ore = tInput.get(i);
            int tID = GTUtility.stackToInt(ore);
            if (tID == 0) continue;
            if (isPureDust.contains(tID) || isImpureDust.contains(tID)
                || isCrushedPureOre.contains(tID)
                || isThermal.contains(tID)
                || isCrushedOre.contains(tID)
                || isOre.contains(tID)) {
                if (remainingCost >= ore.stackSize) {
                    tOres.add(GTUtility.copy(ore));
                    remainingCost -= ore.stackSize;
                    ore.stackSize = 0;
                } else {
                    tOres.add(GTUtility.copyAmountUnsafe(remainingCost, ore));
                    ore.stackSize -= remainingCost;
                    break;
                }
            }
        }
        sMidProduct = tOres.toArray(new ItemStack[0]);
        switch (sMode) {
            case 0 -> {
                doMac(isOre);
                doWash(isCrushedOre);
                doThermal(isCrushedPureOre, isCrushedOre);
                doMac(isThermal, isOre, isCrushedOre, isCrushedPureOre);
            }
            case 1 -> {
                doMac(isOre);
                doWash(isCrushedOre);
                doMac(isOre, isCrushedOre, isCrushedPureOre);
                doCentrifuge(isImpureDust, isPureDust);
            }
            case 2 -> {
                doMac(isOre);
                doMac(isThermal, isOre, isCrushedOre, isCrushedPureOre);
                doCentrifuge(isImpureDust, isPureDust);
            }
            case 3 -> {
                doMac(isOre);
                doWash(isCrushedOre);
                doSift(isCrushedPureOre);
            }
            case 4 -> {
                doMac(isOre);
                doChemWash(isCrushedOre, isCrushedPureOre);
                doMac(isCrushedOre, isCrushedPureOre);
                doCentrifuge(isImpureDust, isPureDust);
            }
            case 5 -> {
                doMac(isOre);
                doChemWash(isCrushedOre, isCrushedPureOre);
                doThermal(isCrushedPureOre, isCrushedOre);
                doMac(isThermal, isOre, isCrushedOre, isCrushedPureOre);
            }
            case 6 -> {
                doHam(isOre);
                doHam(isThermal, isOre, isCrushedOre, isCrushedPureOre);
                doSimWash(isImpureDust, isPureDust);
            }
            default -> {
                return CheckRecipeResultRegistry.NO_RECIPE;
            }
        }

        this.mEfficiency = 10000 - (getIdealStatus() - getRepairStatus()) * 1000;
        this.mEfficiencyIncrease = 10000;
        this.mOutputItems = sMidProduct;
        this.mMaxProgresstime = (int) (calculator.getDuration() * batchMultiplierMax);
        this.lEUt = calculator.getConsumption();
        if (this.lEUt > 0) {
            this.lEUt = -this.lEUt;
        }
        this.updateSlots();
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    protected void runMachine(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (mProgresstime >= mMaxProgresstime - 1 && mMaxProgresstime > 0) {
            // Multiblock base already includes 1 parallel
            this.recipesDone += lastParallel - 1;
        }
        super.runMachine(aBaseMetaTileEntity, aTick);
    }

    private boolean checkTypes(int aID, IntOpenHashSet... aTables) {
        for (IntOpenHashSet set : aTables) {
            if (set.contains(aID)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        if (aPlayer.isSneaking()) {
            sVoidStone = !sVoidStone;
            GTUtility.sendChatToPlayer(
                aPlayer,
                StatCollector.translateToLocalFormatted("GT5U.machines.oreprocessor.void", sVoidStone));
            return;
        }
        sMode = (sMode + 1) % 7;
        List<String> des = getDisplayMode(sMode);
        GTUtility.sendChatToPlayer(aPlayer, String.join("", des));
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        sMode = aNBT.getInteger("ssMode");
        sVoidStone = aNBT.getBoolean("ssStone");
        currentParallelism = aNBT.getInteger("currentParallelism");
        super.loadNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("ssMode", sMode);
        aNBT.setBoolean("ssStone", sVoidStone);
        aNBT.setInteger("currentParallelism", currentParallelism);
        super.saveNBTData(aNBT);
    }

    private void doMac(IntOpenHashSet... aTables) {
        List<ItemStack> tProduct = new ArrayList<>();
        if (sMidProduct != null) {
            for (ItemStack aStack : sMidProduct) {
                int tID = GTUtility.stackToInt(aStack);
                if (checkTypes(tID, aTables)) {
                    GTRecipe tRecipe = RecipeMaps.maceratorRecipes.findRecipeQuery()
                        .caching(false)
                        .items(aStack)
                        .find();
                    if (tRecipe != null) {
                        tProduct.addAll(getOutputStack(tRecipe, aStack.stackSize));
                    } else {
                        tProduct.add(aStack);
                    }
                } else {
                    tProduct.add(aStack);
                }
            }
        }
        doCompress(tProduct);
    }

    private void doWash(IntOpenHashSet... aTables) {
        List<ItemStack> tProduct = new ArrayList<>();
        if (sMidProduct != null) {
            for (ItemStack aStack : sMidProduct) {
                int tID = GTUtility.stackToInt(aStack);
                if (checkTypes(tID, aTables)) {
                    GTRecipe tRecipe = RecipeMaps.oreWasherRecipes.findRecipeQuery()
                        .caching(false)
                        .items(aStack)
                        .fluids(GTModHandler.getDistilledWater(Integer.MAX_VALUE))
                        .find();
                    if (tRecipe != null) {
                        tProduct.addAll(getOutputStack(tRecipe, aStack.stackSize));
                    } else {
                        tProduct.add(aStack);
                    }
                } else {
                    tProduct.add(aStack);
                }
            }
        }
        doCompress(tProduct);
    }

    private void doThermal(IntOpenHashSet... aTables) {
        List<ItemStack> tProduct = new ArrayList<>();
        if (sMidProduct != null) {
            for (ItemStack aStack : sMidProduct) {
                int tID = GTUtility.stackToInt(aStack);
                if (checkTypes(tID, aTables)) {
                    GTRecipe tRecipe = RecipeMaps.thermalCentrifugeRecipes.findRecipeQuery()
                        .caching(false)
                        .items(aStack)
                        .find();
                    if (tRecipe != null) {
                        tProduct.addAll(getOutputStack(tRecipe, aStack.stackSize));
                    } else {
                        tProduct.add(aStack);
                    }
                } else {
                    tProduct.add(aStack);
                }
            }
        }
        doCompress(tProduct);
    }

    private void doCentrifuge(IntOpenHashSet... aTables) {
        List<ItemStack> tProduct = new ArrayList<>();
        if (sMidProduct != null) {
            for (ItemStack aStack : sMidProduct) {
                int tID = GTUtility.stackToInt(aStack);
                if (checkTypes(tID, aTables)) {
                    GTRecipe tRecipe = RecipeMaps.centrifugeRecipes.findRecipeQuery()
                        .items(aStack)
                        .find();
                    if (tRecipe != null) {
                        tProduct.addAll(getOutputStack(tRecipe, aStack.stackSize));
                    } else {
                        tProduct.add(aStack);
                    }
                } else {
                    tProduct.add(aStack);
                }
            }
        }
        doCompress(tProduct);
    }

    private void doSift(IntOpenHashSet... aTables) {
        List<ItemStack> tProduct = new ArrayList<>();
        if (sMidProduct != null) {
            for (ItemStack aStack : sMidProduct) {
                int tID = GTUtility.stackToInt(aStack);
                if (checkTypes(tID, aTables)) {
                    GTRecipe tRecipe = RecipeMaps.sifterRecipes.findRecipeQuery()
                        .items(aStack)
                        .find();
                    if (tRecipe != null) {
                        tProduct.addAll(getOutputStack(tRecipe, aStack.stackSize));
                    } else {
                        tProduct.add(aStack);
                    }
                } else {
                    tProduct.add(aStack);
                }
            }
        }
        doCompress(tProduct);
    }

    private void doChemWash(IntOpenHashSet... aTables) {
        List<ItemStack> tProduct = new ArrayList<>();
        if (sMidProduct != null) {
            for (ItemStack aStack : sMidProduct) {
                int tID = GTUtility.stackToInt(aStack);
                if (checkTypes(tID, aTables)) {
                    GTRecipe tRecipe = RecipeMaps.chemicalBathRecipes.findRecipeQuery()
                        .items(aStack)
                        .fluids(getStoredFluids().toArray(new FluidStack[0]))
                        .find();
                    if (tRecipe != null && tRecipe.getRepresentativeFluidInput(0) != null) {
                        FluidStack tInputFluid = tRecipe.getRepresentativeFluidInput(0)
                            .copy();
                        int tStored = getFluidAmount(tInputFluid);
                        int tWashed = Math.min(tStored / tInputFluid.amount, aStack.stackSize);
                        depleteInput(new FluidStack(tInputFluid.getFluid(), tWashed * tInputFluid.amount));
                        tProduct.addAll(getOutputStack(tRecipe, tWashed));
                        if (tWashed < aStack.stackSize) {
                            tProduct.add(GTUtility.copyAmountUnsafe(aStack.stackSize - tWashed, aStack));
                        }
                    } else {
                        tProduct.add(aStack);
                    }
                } else {
                    tProduct.add(aStack);
                }
            }
        }
        doCompress(tProduct);
    }

    private void doHam(IntOpenHashSet... aTables) {
        List<ItemStack> tProduct = new ArrayList<>();
        if (sMidProduct != null) {
            for (ItemStack aStack : sMidProduct) {
                int tID = GTUtility.stackToInt(aStack);
                if (checkTypes(tID, aTables)) {
                    GTRecipe tRecipe = RecipeMaps.hammerRecipes.findRecipeQuery()
                        .caching(false)
                        .items(aStack)
                        .find();
                    if (tRecipe != null) {
                        tProduct.addAll(getOutputStack(tRecipe, aStack.stackSize));
                    } else {
                        tProduct.add(aStack);
                    }
                } else {
                    tProduct.add(aStack);
                }
            }
        }
        doCompress(tProduct);
    }

    private void doSimWash(IntOpenHashSet... aTables) {
        List<ItemStack> tProduct = new ArrayList<>();
        if (sMidProduct != null) {
            for (ItemStack aStack : sMidProduct) {
                int tID = GTUtility.stackToInt(aStack);
                if (checkTypes(tID, aTables)) {
                    GTRecipe tRecipe = simpleWasherRecipes.findRecipeQuery()
                        .items(aStack)
                        .fluids(Materials.Water.getFluid(100))
                        .find();
                    if (tRecipe != null) {
                        tProduct.addAll(getOutputStack(tRecipe, aStack.stackSize));
                    } else {
                        tProduct.add(aStack);
                    }
                } else {
                    tProduct.add(aStack);
                }
            }
        }
        doCompress(tProduct);
    }

    private int getFluidAmount(FluidStack aFluid) {
        int tAmt = 0;
        if (aFluid == null) return 0;
        for (FluidStack fluid : getStoredFluids()) {
            if (aFluid.isFluidEqual(fluid)) {
                tAmt += fluid.amount;
            }
        }
        return tAmt;
    }

    private List<ItemStack> getOutputStack(GTRecipe aRecipe, int aTime) {
        List<ItemStack> tOutput = new ArrayList<>();
        for (int i = 0; i < aRecipe.mOutputs.length; i++) {
            if (aRecipe.getOutput(i) == null) {
                continue;
            }
            int tChance = aRecipe.getOutputChance(i);
            if (tChance == 10000) {
                tOutput.add(GTUtility.copyAmountUnsafe(aTime * aRecipe.getOutput(i).stackSize, aRecipe.getOutput(i)));
            } else {
                // Use Normal Distribution
                double u = aTime * (tChance / 10000D);
                double e = aTime * (tChance / 10000D) * (1 - (tChance / 10000D));
                Random random = new Random();
                int tAmount = (int) Math.ceil(Math.sqrt(e) * random.nextGaussian() + u);
                tOutput.add(GTUtility.copyAmountUnsafe(tAmount * aRecipe.getOutput(i).stackSize, aRecipe.getOutput(i)));
            }
        }
        return tOutput.stream()
            .filter(i -> (i != null && i.stackSize > 0))
            .collect(Collectors.toList());
    }

    private void doCompress(List<ItemStack> aList) {
        HashMap<Integer, Integer> rProduct = new HashMap<>();
        for (ItemStack stack : aList) {
            int tID = GTUtility.stackToInt(stack);
            if (sVoidStone) {
                if (GTUtility.areStacksEqual(Materials.Stone.getDust(1), stack)) {
                    continue;
                }
            }
            if (tID != 0) {
                if (rProduct.containsKey(tID)) {
                    rProduct.put(tID, rProduct.get(tID) + stack.stackSize);
                } else {
                    rProduct.put(tID, stack.stackSize);
                }
            }
        }
        sMidProduct = new ItemStack[rProduct.size()];
        int cnt = 0;
        for (Integer id : rProduct.keySet()) {
            ItemStack stack = GTUtility.intToStack(id);
            sMidProduct[cnt] = GTUtility.copyAmountUnsafe(rProduct.get(id), stack);
            cnt++;
        }
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece(STRUCTURE_PIECE_MAIN, 8, 9, 1) && mMaintenanceHatches.size() <= 1
            && !mMufflerHatches.isEmpty();
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return 200;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEIntegratedOreFactory(mName);
    }

    private void setCurrentParallelism(int parallelism) {
        this.currentParallelism = parallelism;
    }

    private int getCurrentParallelism() {
        return this.currentParallelism;
    }

    @Override
    public void getExtraInfoData(ArrayList<String> info) {
        info.add(
            StatCollector.translateToLocal("GT5U.multiblock.parallelism") + ": "
                + EnumChatFormatting.BLUE
                + getCurrentParallelism()
                + EnumChatFormatting.RESET);
        info.add(StatCollector.translateToLocalFormatted("GT5U.machines.oreprocessor.void", sVoidStone));
        info.addAll(getDisplayMode(sMode));
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX2),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ORE_FACTORY_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ORE_FACTORY_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX2), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ORE_FACTORY)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ORE_FACTORY_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX2) };
    }

    private static List<String> getDisplayMode(int mode) {
        final EnumChatFormatting AQUA = EnumChatFormatting.AQUA;
        final String CRUSH = StatCollector.translateToLocalFormatted("GT5U.machines.oreprocessor.Macerate");
        final String WASH = StatCollector.translateToLocalFormatted("GT5U.machines.oreprocessor.Ore_Washer")
            .replace(" ", " " + AQUA);
        final String THERMAL = StatCollector.translateToLocalFormatted("GT5U.machines.oreprocessor.Thermal_Centrifuge")
            .replace(" ", " " + AQUA);
        final String CENTRIFUGE = StatCollector.translateToLocalFormatted("GT5U.machines.oreprocessor.Centrifuge");
        final String SIFTER = StatCollector.translateToLocalFormatted("GT5U.machines.oreprocessor.Sifter");
        final String CHEM_WASH = StatCollector.translateToLocalFormatted("GT5U.machines.oreprocessor.Chemical_Bathing")
            .replace(" ", " " + AQUA);
        final String HAMMER = StatCollector.translateToLocalFormatted("GT5U.machines.oreprocessor.Forge_Hammer");
        final String SIM_WASHER = StatCollector.translateToLocalFormatted("GT5U.machines.oreprocessor.Simple_Washer");
        final String ARROW = " " + AQUA + "-> ";

        List<String> des = new ArrayList<>();
        des.add(StatCollector.translateToLocalFormatted("GT5U.multiblock.runningMode") + " ");

        switch (mode) {
            case 0 -> {
                des.add(AQUA + CRUSH + ARROW);
                des.add(AQUA + WASH + ARROW);
                des.add(AQUA + THERMAL + ARROW);
                des.add(AQUA + CRUSH + ' ');
            }
            case 1 -> {
                des.add(AQUA + CRUSH + ARROW);
                des.add(AQUA + WASH + ARROW);
                des.add(AQUA + CRUSH + ARROW);
                des.add(AQUA + CENTRIFUGE + ' ');
            }
            case 2 -> {
                des.add(AQUA + CRUSH + ARROW);
                des.add(AQUA + CRUSH + ARROW);
                des.add(AQUA + CENTRIFUGE + ' ');
            }
            case 3 -> {
                des.add(AQUA + CRUSH + ARROW);
                des.add(AQUA + WASH + ARROW);
                des.add(AQUA + SIFTER + ' ');
            }
            case 4 -> {
                des.add(AQUA + CRUSH + ARROW);
                des.add(AQUA + CHEM_WASH + ARROW);
                des.add(AQUA + CRUSH + ARROW);
                des.add(AQUA + CENTRIFUGE + ' ');
            }
            case 5 -> {
                des.add(AQUA + CRUSH + ARROW);
                des.add(AQUA + CHEM_WASH + ARROW);
                des.add(AQUA + THERMAL + ARROW);
                des.add(AQUA + CRUSH + ' ');
            }
            case 6 -> {
                des.add(AQUA + HAMMER + ARROW);
                des.add(AQUA + HAMMER + ARROW);
                des.add(AQUA + SIM_WASHER + ' ');
            }
            default -> des.add(StatCollector.translateToLocalFormatted("GT5U.machines.oreprocessor.WRONG_MODE"));
        }

        des.add(StatCollector.translateToLocalFormatted("GT5U.machines.oreprocessor2", getTime(mode) / 20));

        return des;

    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);
        NBTTagCompound tag = accessor.getNBTData();

        currenttip.add(
            StatCollector.translateToLocal("GT5U.multiblock.parallelism") + ": "
                + EnumChatFormatting.BLUE
                + tag.getInteger("currentParallelism")
                + EnumChatFormatting.RESET);
        currenttip.addAll(getDisplayMode(tag.getInteger("ssMode")));
        currenttip
            .add(StatCollector.translateToLocalFormatted("GT5U.machines.oreprocessor.void", tag.getBoolean("ssStone")));

    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("ssMode", sMode);
        tag.setBoolean("ssStone", sVoidStone);
        tag.setInteger("currentParallelism", currentParallelism);
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }
}
