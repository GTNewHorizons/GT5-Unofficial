package gregtech.common.tileentities.machines.multi;

import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
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
import static gregtech.api.util.GTStructureUtility.ofSheetMetal;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.simpleWasherRecipes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.cleanroommc.modularui.drawable.UITexture;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.objects.XSTR;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEIntegratedOreFactory extends MTEExtendedPowerMultiBlockBase<MTEIntegratedOreFactory>
    implements ISurvivalConstructable {

    private static final long RECIPE_EUT = 30;
    private static final int MODE_AMOUNT = 7;
    private static final String STRUCTURE_PIECE_MAIN = "main";

    private static final int OFFSET_X = 7;
    private static final int OFFSET_Y = 6;
    private static final int OFFSET_Z = 1;

    private static final IStructureDefinition<MTEIntegratedOreFactory> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEIntegratedOreFactory>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            // spotless:off
            new String[][] {
                { "               ", "               ", "               ", "               ", "     DDDDD     ", "    GG   GG    ", "    G     G    ", "  J G     G J  ", " JJJG     GJJJ " },
                { " FFF       FFF ", " FAF       FAF ", " FAFF     FFAF ", " FAFFFF FFFFAF ", " FAFFDDDDDFFAF ", " FAFFDDDDDFFAF ", " FFFFDD~DDFFFF ", " FFFFDDDDDFFFF ", "JFFFJDDDDDJFFFJ" },
                { " FFF       FFF ", " AEF       FBA ", " AEFJ     JFBA ", " AEFFJJGJJFFBA ", " AEFFDDDDDFFBA ", " AEF       FBA ", " FEF       FBF ", "JFEF      JFBFJ", "JFFFJDDDDDJFFFJ" },
                { " FFF       FFF ", " FAF       FAF ", " FAFF     FFAF ", " FAFFFF FFFFAF ", " FAFFDDDDDFFAF ", " FAFF     FFAF ", " FFFF     FFFF ", " FFFF     FFFF ", "JFFFJDDDDDJFFFJ" },
                { "               ", "               ", "               ", "               ", "    DDDDDDD    ", "    D     D    ", "    D     D    ", "  J D     D J  ", " JJJDDDDDDDJJJ " },
                { " FFF       FFF ", " FAF       FAF ", " FAFF     FFAF ", " FAFFFF FFFFAF ", " FAFFDDDDDFFAF ", " FAFF     FFAF ", " FFFF     FFFF ", " FFFF     FFFF ", "JFFFJDDDDDJFFFJ" },
                { " FFF       FFF ", " A F       FCA ", " A FJ     JFCA ", " A FFJJGJJFFCA ", " A FFDDDDDFFCA ", " A F       FCA ", " F F       FCF ", "JF FJ     JFCFJ", "JFFFJDDDDDJFFFJ" },
                { " FFF       FFF ", " FAF       FAF ", " FAFF     FFAF ", " FAFFFF FFFFAF ", " FAFFDDDDDFFAF ", " FAFF     FFAF ", " FFFF     FFFF ", " FFFF     FFFF ", "JFFFJDDDDDJFFFJ" },
                { "               ", "               ", "               ", "               ", "    DDDDDDD    ", "    D     D    ", "    D     D    ", "  J D     D J  ", " JJJDDDDDDDJJJ " },
                { " FFF       FFF ", " FAF       FAF ", " FAFF     FFAF ", " FAFFFF FFFFAF ", " FAFFDDDDDFFAF ", " FAFF     FFAF ", " FFFF     FFFF ", " FFFF     FFFF ", "JFFFJDDDDDJFFFJ" },
                { " FFF       FFF ", " AHF       FIA ", " AHFJ     JFIA ", " AHFFJJGJJFFIA ", " AHFFDDDDDFFIA ", " AHF       FIA ", " FHF       FIF ", "JFHF      JFIFJ", "JFFFJDDDDDJFFFJ" },
                { " FFF       FFF ", " FAF       FAF ", " FAFF     FFAF ", " FAFFFF FFFFAF ", " FAFFDDDDDFFAF ", " FAFFDDDDDFFAF ", " FFFFDDDDDFFFF ", " FFFFDDDDDFFFF ", "JFFFJDDDDDJFFFJ" },
                { "               ", "               ", "               ", "               ", "     DDDDD     ", "    GG   GG    ", "    G     G    ", "  J G     G J  ", " JJJG     GJJJ " } })
            //spotless:on
        .addElement('A', chainAllGlasses())
        .addElement('B', Casings.TitaniumGearBoxCasing.asElement())
        .addElement('C', Casings.GrateMachineCasing.asElement())
        .addElement(
            'D',
            buildHatchAdder(MTEIntegratedOreFactory.class)
                .atLeast(Energy, ExoticEnergy, InputBus, InputHatch, Muffler, OutputBus, OutputHatch)
                .casingIndex(Casings.CleanStainlessSteelMachineCasing.textureId)
                .hint(1)
                .buildAndChain(Casings.CleanStainlessSteelMachineCasing.asElement()))
        .addElement('E', Casings.ElectrumFluxCoilBlock.asElement()) // the latest available at uhv
        .addElement('F', Casings.AdvancedIridiumPlatedMachineCasing.asElement())

        .addElement('G', ofFrame(Materials.StainlessSteel))
        .addElement('J', ofSheetMetal(Materials.Iridium))
        .addElement('I', Casings.CentrifugeCasing.asElement())
        .addElement('H', Casings.LargeSieveGrate.asElement())
        .build();

    private static final IntOpenHashSet isCrushedOre = new IntOpenHashSet();
    private static final IntOpenHashSet isCrushedPureOre = new IntOpenHashSet();
    private static final IntOpenHashSet isPureDust = new IntOpenHashSet();
    private static final IntOpenHashSet isImpureDust = new IntOpenHashSet();
    private static final IntOpenHashSet isThermal = new IntOpenHashSet();
    private static final IntOpenHashSet isOre = new IntOpenHashSet();
    private static boolean isInit = false;
    private ItemStack[] midProduct;
    private int mode = 0;
    private boolean doesVoidStone = false;
    private int currentParallelism = 0;
    private final XSTR random = new XSTR();

    private static void registerOrePrefix(String prefix, IntOpenHashSet target) {
        for (ItemStack stack : OreDictionary.getOres(prefix)) {
            target.add(GTUtility.stackToInt(stack));
        }
    }

    private static void initHash() {
        for (String name : OreDictionary.getOreNames()) {
            if (name == null || name.isEmpty()) continue;

            if (name.startsWith("crushedPurified")) registerOrePrefix(name, isCrushedPureOre);
            else if (name.startsWith("crushedCentrifuged")) registerOrePrefix(name, isThermal);
            else if (name.startsWith("crushed")) registerOrePrefix(name, isCrushedOre);
            else if (name.startsWith("dustImpure")) registerOrePrefix(name, isImpureDust);
            else if (name.startsWith("dustPure")) registerOrePrefix(name, isPureDust);
            else if (name.startsWith("ore") || name.startsWith("rawOre")) registerOrePrefix(name, isOre);
        }
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
    public void construct(ItemStack itemStack, boolean b) {
        buildPiece(STRUCTURE_PIECE_MAIN, itemStack, b, OFFSET_X, OFFSET_Y, OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            OFFSET_X,
            OFFSET_Y,
            OFFSET_Z,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        // other order makes nei preview go crazy
        boolean piece = checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z);
        if (mExoticEnergyHatches.size() > 1) return false;
        if (mMufflerHatches.isEmpty()) return false;
        return piece;
    }

    private static int getRecipeTickTime(int mode) {
        return switch (mode) {
            case 0 -> 30 * SECOND;
            case 1 -> 15 * SECOND;
            case 2 -> 10 * SECOND;
            case 3 -> 20 * SECOND;
            case 4 -> 17 * SECOND;
            case 5 -> 32 * SECOND;
            case 6 -> 1 * SECOND;
            default -> 1_000_000_000; // should never happen
        };
    }

    @Override
    @NotNull
    public CheckRecipeResult checkProcessing() {
        if (!isInit) {
            initHash();
            isInit = true;
        }

        ArrayList<ItemStack> inputItem = getStoredInputs();
        ArrayList<FluidStack> inputFluid = getStoredFluids();
        if (inputItem.isEmpty() || inputFluid.isEmpty()) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }
        final long availableEUt = getMaxInputVoltage() * getMaxInputAmps();
        final int maxParallelFromPower = GTUtility.safeInt(availableEUt / RECIPE_EUT);

        int lubricantAmount = 0;
        int waterAmount = 0;

        for (FluidStack fluid : inputFluid) {
            if (fluid == null) continue;
            if (fluid.equals(GTModHandler.getDistilledWater(1L))) waterAmount += fluid.amount;
            else if (fluid.equals(Materials.Lubricant.getFluid(1L))) lubricantAmount += fluid.amount;
        }

        final int parallelFromFluids = Math.min(lubricantAmount / 2, waterAmount / 200);
        if (parallelFromFluids <= 0) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        int parallelFromItems = 0;
        for (ItemStack ore : inputItem) {
            int tID = GTUtility.stackToInt(ore);
            if (tID == 0 || !isValidOreInput(tID)) continue;
            parallelFromItems += ore.stackSize;
        }

        final int baseParallel = Math.min(Math.min(maxParallelFromPower, parallelFromFluids), parallelFromItems);
        if (baseParallel <= 0) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        final int effectiveParallel = GTUtility.safeInt(baseParallel);
        if (effectiveParallel <= 0) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        depleteInput(GTModHandler.getDistilledWater(effectiveParallel * 200L));
        depleteInput(Materials.Lubricant.getFluid(effectiveParallel * 2L));

        final long fixedEUt = -RECIPE_EUT * baseParallel;

        List<ItemStack> tOres = new ArrayList<>();
        int remaining = effectiveParallel;

        for (int i = 0; i < inputItem.size() && remaining > 0; i++) {
            ItemStack ore = inputItem.get(i);
            int tID = GTUtility.stackToInt(ore);
            if (tID == 0 || !isValidOreInput(tID)) continue;

            int take = Math.min(remaining, ore.stackSize);

            tOres.add(GTUtility.copyAmountUnsafe(take, ore));
            ore.stackSize -= take;
            remaining -= take;
        }

        if (tOres.isEmpty()) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        }
        midProduct = tOres.toArray(new ItemStack[0]);

        switch (mode) {
            case 0 -> {
                macerate(isOre);
                wash(isCrushedOre);
                thermalRefine(isCrushedPureOre, isCrushedOre);
                macerate(isThermal, isOre, isCrushedOre, isCrushedPureOre);
            }
            case 1 -> {
                macerate(isOre);
                wash(isCrushedOre);
                macerate(isOre, isCrushedOre, isCrushedPureOre);
                centrifuge(isImpureDust, isPureDust);
            }
            case 2 -> {
                macerate(isOre);
                macerate(isThermal, isOre, isCrushedOre, isCrushedPureOre);
                centrifuge(isImpureDust, isPureDust);
            }
            case 3 -> {
                macerate(isOre);
                wash(isCrushedOre);
                sift(isCrushedPureOre);
            }
            case 4 -> {
                macerate(isOre);
                chemicalWash(isCrushedOre, isCrushedPureOre);
                macerate(isCrushedOre, isCrushedPureOre);
                centrifuge(isImpureDust, isPureDust);
            }
            case 5 -> {
                macerate(isOre);
                chemicalWash(isCrushedOre, isCrushedPureOre);
                thermalRefine(isCrushedPureOre, isCrushedOre);
                macerate(isThermal, isOre, isCrushedOre, isCrushedPureOre);
            }
            case 6 -> {
                forgeHammer(isOre);
                forgeHammer(isThermal, isOre, isCrushedOre, isCrushedPureOre);
                simpleWash(isImpureDust, isPureDust);
            }
            default -> {
                return CheckRecipeResultRegistry.NO_RECIPE;
            }
        }

        this.mEfficiency = 10000 - (getIdealStatus() - getRepairStatus()) * 1000;
        this.mEfficiencyIncrease = 10000;
        this.mOutputItems = midProduct;
        this.mMaxProgresstime = getRecipeTickTime(this.mode);
        this.lEUt = fixedEUt;

        lastParallel = effectiveParallel;
        setCurrentParallelism(effectiveParallel);

        this.updateSlots();
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    private static boolean isValidOreInput(int id) {
        return isPureDust.contains(id) || isImpureDust.contains(id)
            || isCrushedPureOre.contains(id)
            || isThermal.contains(id)
            || isCrushedOre.contains(id)
            || isOre.contains(id);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    protected void runMachine(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (mProgresstime >= mMaxProgresstime - 1 && mMaxProgresstime > 0) {
            // MTEExtendedPowerMultiBlockBase already counts 1 parallel; add the rest.
            this.recipesDone += lastParallel - 1;
        }
        super.runMachine(aBaseMetaTileEntity, aTick);
    }

    @FunctionalInterface
    private interface RecipeLookup {

        /** Returns the recipe for {@code stack}, or {@code null} if none found. */
        GTRecipe find(ItemStack stack);
    }

    private void processStep(RecipeLookup lookup, IntOpenHashSet... tables) {
        if (midProduct == null) return;
        List<ItemStack> output = new ArrayList<>();
        for (ItemStack stack : midProduct) {
            int id = GTUtility.stackToInt(stack);
            if (checkTypes(id, tables)) {
                GTRecipe recipe = lookup.find(stack);
                if (recipe != null) {
                    output.addAll(getOutputStack(recipe, stack.stackSize));
                } else {
                    output.add(stack);
                }
            } else {
                output.add(stack);
            }
        }
        doCompress(output);
    }

    private void macerate(IntOpenHashSet... tables) {
        processStep(
            stack -> RecipeMaps.maceratorRecipes.findRecipeQuery()
                .caching(false)
                .items(stack)
                .find(),
            tables);
    }

    private void wash(IntOpenHashSet... tables) {
        processStep(
            stack -> RecipeMaps.oreWasherRecipes.findRecipeQuery()
                .caching(false)
                .items(stack)
                .fluids(GTModHandler.getDistilledWater(Integer.MAX_VALUE))
                .find(),
            tables);
    }

    private void thermalRefine(IntOpenHashSet... tables) {
        processStep(
            stack -> RecipeMaps.thermalCentrifugeRecipes.findRecipeQuery()
                .caching(false)
                .items(stack)
                .find(),
            tables);
    }

    private void centrifuge(IntOpenHashSet... tables) {
        processStep(
            stack -> RecipeMaps.centrifugeRecipes.findRecipeQuery()
                .items(stack)
                .find(),
            tables);
    }

    private void sift(IntOpenHashSet... tables) {
        processStep(
            stack -> RecipeMaps.sifterRecipes.findRecipeQuery()
                .items(stack)
                .find(),
            tables);
    }

    private void forgeHammer(IntOpenHashSet... tables) {
        processStep(
            stack -> RecipeMaps.hammerRecipes.findRecipeQuery()
                .caching(false)
                .items(stack)
                .find(),
            tables);
    }

    private void simpleWash(IntOpenHashSet... tables) {
        processStep(
            stack -> simpleWasherRecipes.findRecipeQuery()
                .items(stack)
                .fluids(Materials.Water.getFluid(100))
                .find(),
            tables);
    }

    private void chemicalWash(IntOpenHashSet... tables) {
        if (midProduct == null) return;
        List<ItemStack> output = new ArrayList<>();
        for (ItemStack stack : midProduct) {
            int id = GTUtility.stackToInt(stack);
            if (!checkTypes(id, tables)) {
                output.add(stack);
                continue;
            }
            GTRecipe recipe = RecipeMaps.chemicalBathRecipes.findRecipeQuery()
                .items(stack)
                .fluids(getStoredFluids().toArray(new FluidStack[0]))
                .find();

            if (recipe != null && recipe.getRepresentativeFluidInput(0) != null) {
                FluidStack requiredFluid = recipe.getRepresentativeFluidInput(0)
                    .copy();
                int available = getFluidAmount(requiredFluid);
                int canProcess = Math.min(available / requiredFluid.amount, stack.stackSize);

                depleteInput(new FluidStack(requiredFluid.getFluid(), canProcess * requiredFluid.amount));
                output.addAll(getOutputStack(recipe, canProcess));

                if (canProcess < stack.stackSize) {
                    output.add(GTUtility.copyAmountUnsafe(stack.stackSize - canProcess, stack));
                }
            } else {
                output.add(stack);
            }
        }
        doCompress(output);
    }

    private boolean checkTypes(int aID, IntOpenHashSet... aTables) {
        for (IntOpenHashSet set : aTables) {
            if (set.contains(aID)) return true;
        }
        return false;
    }

    private int getFluidAmount(FluidStack aFluid) {
        if (aFluid == null) return 0;
        int total = 0;
        for (FluidStack fluid : getStoredFluids()) {
            if (aFluid.isFluidEqual(fluid)) total += fluid.amount;
        }
        return total;
    }

    private List<ItemStack> getOutputStack(GTRecipe aRecipe, int aTime) {
        List<ItemStack> outputs = new ArrayList<>();
        for (int i = 0; i < aRecipe.mOutputs.length; i++) {
            ItemStack template = aRecipe.getOutput(i);
            if (template == null) continue;

            int chance = aRecipe.getOutputChance(i);
            int quantity;
            if (chance == 10000) {
                quantity = aTime * template.stackSize;
            } else {
                // Normal-distribution approximation for probabilistic drops
                double p = chance / 10000.0;
                double mean = aTime * p;
                double std = Math.sqrt(aTime * p * (1 - p));
                quantity = (int) Math.ceil(std * random.nextGaussian() + mean);
                quantity *= template.stackSize;
            }
            if (quantity > 0) {
                outputs.add(GTUtility.copyAmountUnsafe(quantity, template));
            }
        }
        return outputs;
    }

    private void doCompress(List<ItemStack> aList) {
        HashMap<Integer, Integer> merged = new HashMap<>();
        for (ItemStack stack : aList) {
            if (doesVoidStone && GTUtility.areStacksEqual(Materials.Stone.getDust(1), stack)) continue;
            int id = GTUtility.stackToInt(stack);
            if (id != 0) {
                merged.merge(id, stack.stackSize, Integer::sum);
            }
        }

        midProduct = new ItemStack[merged.size()];
        int index = 0;
        for (Map.Entry<Integer, Integer> entry : merged.entrySet()) {
            ItemStack template = GTUtility.intToStack(entry.getKey());
            midProduct[index++] = GTUtility.copyAmountUnsafe(entry.getValue(), template);
        }
    }

    private void setCurrentParallelism(int parallelism) {
        this.currentParallelism = parallelism;
    }

    private int getCurrentParallelism() {
        return this.currentParallelism;
    }

    // Parallels are automatical
    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return 200;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEIntegratedOreFactory(mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Ore Processor, IOF")
            .addInfo("Does all ore processing in one step")
            .addInfo("Parallel count scales with total input power: EU/t / 30")
            .addInfo("Every ore costs 30EU/t, 2L lubricant, 200L distilled water")
            .addInfo("Recipes that need extra input require their extra inputs on top of the normal costs")
            .addInfo("Processing time is dependent on mode")
            .addInfo("Use a screwdriver to switch mode")
            .addInfo("Sneak click with screwdriver to void the stone dust")
            .addTecTechHatchInfo()
            .addPollutionAmount(getPollutionPerSecond(null))
            .addSeparator()
            .addInfo(EnumChatFormatting.GREEN + "OP stands for Ore Processor ;)")
            .beginStructureBlock(15, 9, 13, false)
            .addController("The third layer")
            .addCasingInfoExactly("Awakened Draconium Coil Block", 7, false)
            .addCasingInfoExactly("Centrifuge Casing", 7, false)
            .addCasingInfoExactly("Grate Machine Casing", 7, false)
            .addCasingInfoExactly("Large Sieve Grate", 7, false)
            .addCasingInfoExactly("Titanium Gear Box Casing", 7, false)
            .addCasingInfoExactly("Any Tiered Glass", 90, false)
            .addCasingInfoExactly("Stainless Steel Frame Box", 23, false)
            .addCasingInfoExactly("Clean Stainless Steel Machine Casing", 169, false)
            .addCasingInfoExactly("Iridium Sheetmetal", 96, false)
            .addCasingInfoExactly("Advanced Iridium Plated Machine Casing", 462, false)
            .addEnergyHatch("Any bottom Casing", 1)
            .addMaintenanceHatch("Any bottom Casing", 1)
            .addInputBus("Input ore/crushed ore", 2)
            .addInputHatch("Input lubricant/distilled water/washing chemicals", 3)
            .addMufflerHatch("Output Pollution", 3)
            .addOutputBus("Output products", 4)
            .addStructureAuthors(EnumChatFormatting.GOLD + "Bavib")
            .toolTipFinisher();
        return tt;
    }

    private static List<String> getDisplayMode(int mode) {
        final EnumChatFormatting AQUA = EnumChatFormatting.AQUA;
        final String ARROW = " " + AQUA + "-> ";
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

        List<String> lines = new ArrayList<>();
        lines.add(StatCollector.translateToLocalFormatted("GT5U.multiblock.runningMode") + " ");

        switch (mode) {
            case 0 -> {
                lines.add(AQUA + CRUSH + ARROW);
                lines.add(AQUA + WASH + ARROW);
                lines.add(AQUA + THERMAL + ARROW);
                lines.add(AQUA + CRUSH + ' ');
            }
            case 1 -> {
                lines.add(AQUA + CRUSH + ARROW);
                lines.add(AQUA + WASH + ARROW);
                lines.add(AQUA + CRUSH + ARROW);
                lines.add(AQUA + CENTRIFUGE + ' ');
            }
            case 2 -> {
                lines.add(AQUA + CRUSH + ARROW);
                lines.add(AQUA + CRUSH + ARROW);
                lines.add(AQUA + CENTRIFUGE + ' ');
            }
            case 3 -> {
                lines.add(AQUA + CRUSH + ARROW);
                lines.add(AQUA + WASH + ARROW);
                lines.add(AQUA + SIFTER + ' ');
            }
            case 4 -> {
                lines.add(AQUA + CRUSH + ARROW);
                lines.add(AQUA + CHEM_WASH + ARROW);
                lines.add(AQUA + CRUSH + ARROW);
                lines.add(AQUA + CENTRIFUGE + ' ');
            }
            case 5 -> {
                lines.add(AQUA + CRUSH + ARROW);
                lines.add(AQUA + CHEM_WASH + ARROW);
                lines.add(AQUA + THERMAL + ARROW);
                lines.add(AQUA + CRUSH + ' ');
            }
            case 6 -> {
                lines.add(AQUA + HAMMER + ARROW);
                lines.add(AQUA + HAMMER + ARROW);
                lines.add(AQUA + SIM_WASHER + ' ');
            }
            default -> lines.add(StatCollector.translateToLocalFormatted("GT5U.machines.oreprocessor.WRONG_MODE"));
        }

        lines.add(StatCollector.translateToLocalFormatted("GT5U.machines.oreprocessor2", getRecipeTickTime(mode) / 20));
        return lines;
    }

    @Override
    public String[] getInfoData() {
        List<String> info = new ArrayList<>(Arrays.asList(super.getInfoData()));
        info.add(
            StatCollector.translateToLocal("GT5U.multiblock.parallelism") + ": "
                + EnumChatFormatting.BLUE
                + getCurrentParallelism()
                + EnumChatFormatting.RESET);
        info.add(StatCollector.translateToLocalFormatted("GT5U.machines.oreprocessor.void", doesVoidStone));
        info.addAll(getDisplayMode(mode));
        return info.toArray(new String[0]);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side != aFacing) {
            return new ITexture[] { Casings.CleanStainlessSteelMachineCasing.getCasingTexture() };
        }
        if (aActive) {
            return new ITexture[] { Casings.CleanStainlessSteelMachineCasing.getCasingTexture(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ORE_FACTORY_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ORE_FACTORY_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Casings.CleanStainlessSteelMachineCasing.getCasingTexture(), TextureFactory.builder()
            .addIcon(OVERLAY_FRONT_ORE_FACTORY)
            .extFacing()
            .build(),
            TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_ORE_FACTORY_GLOW)
                .extFacing()
                .glow()
                .build() };
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        if (aPlayer.isSneaking()) {
            doesVoidStone = !doesVoidStone;
            GTUtility.sendChatTrans(
                aPlayer,
                StatCollector.translateToLocalFormatted("GT5U.machines.oreprocessor.void", doesVoidStone));
            return;
        }
        mode = (mode + 1) % MODE_AMOUNT;
        GTUtility.sendChatTrans(aPlayer, String.join("", getDisplayMode(mode)));
    }

    private static final UITexture[] MODE_ICONS = { GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_DEFAULT, // mode 0
        GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_DEFAULT, // mode 1
        GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_DEFAULT, // mode 2
        GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_DEFAULT, // mode 3
        GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_DEFAULT, // mode 4
        GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_DEFAULT, // mode 5
        GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_DEFAULT, // mode 6
    };

    @Override
    public int getMachineMode() {
        return mode;
    }

    @Override
    public void setMachineMode(int mode) {
        this.mode = mode % MODE_AMOUNT;
    }

    @Override
    public String getMachineModeName() {
        return String.join("\n", getDisplayMode(mode));
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui getGui() {
        return super.getGui().withMachineModeIcons(MODE_ICONS);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        mode = aNBT.getInteger("mode");
        doesVoidStone = aNBT.getBoolean("doesVoidStone");
        currentParallelism = aNBT.getInteger("currentParallelism");
        super.loadNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("mode", mode);
        aNBT.setBoolean("doesVoidStone", doesVoidStone);
        aNBT.setInteger("currentParallelism", currentParallelism);
        super.saveNBTData(aNBT);
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
        currenttip.addAll(getDisplayMode(tag.getInteger("mode")));
        currenttip.add(
            StatCollector
                .translateToLocalFormatted("GT5U.machines.oreprocessor.void", tag.getBoolean("doesVoidStone")));
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("mode", mode);
        tag.setBoolean("doesVoidStone", doesVoidStone);
        tag.setInteger("currentParallelism", currentParallelism);
    }
}
