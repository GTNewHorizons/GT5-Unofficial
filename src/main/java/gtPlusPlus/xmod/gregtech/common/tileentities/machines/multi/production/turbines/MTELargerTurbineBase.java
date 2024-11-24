package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.turbines;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.HatchElement.Dynamo;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTUtility.validMTEList;
import static gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase.GTPPHatchElement.TTDynamo;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.GTMod;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchDynamo;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.TurbineStatCalculator;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchTurbine;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;
import gtPlusPlus.xmod.gregtech.api.objects.GTPPRenderedTexture;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public abstract class MTELargerTurbineBase extends GTPPMultiBlockBase<MTELargerTurbineBase>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final ClassValue<IStructureDefinition<MTELargerTurbineBase>> STRUCTURE_DEFINITION = new ClassValue<>() {

        @Override
        @SuppressWarnings("SpellCheckingInspection")
        protected IStructureDefinition<MTELargerTurbineBase> computeValue(Class<?> type) {
            return StructureDefinition.<MTELargerTurbineBase>builder()
                // c = turbine casing
                // s = turbine shaft
                // t = turbine housing
                // h = dynamo/maint
                // m = muffler
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    (new String[][] { { "ccchccc", "ccccccc", "ccmmmcc", "ccm~mcc", "ccmmmcc", "ccccccc", "ccchccc" },
                        { "ctchctc", "cscccsc", "cscccsc", "cscccsc", "cscccsc", "cscccsc", "ctchctc" },
                        { "ccchccc", "ccccccc", "ccccccc", "ccccccc", "ccccccc", "ccccccc", "ccchccc" },
                        { "ccchccc", "ccccccc", "ccccccc", "ccccccc", "ccccccc", "ccccccc", "ccchccc" },
                        { "ctchctc", "cscccsc", "cscccsc", "cscccsc", "cscccsc", "cscccsc", "ctchctc" },
                        { "ccchccc", "ccccccc", "ccccccc", "ccccccc", "ccccccc", "ccccccc", "ccchccc" },
                        { "ccchccc", "ccccccc", "ccccccc", "ccccccc", "ccccccc", "ccccccc", "ccchccc" },
                        { "ctchctc", "cscccsc", "cscccsc", "cscccsc", "cscccsc", "cscccsc", "ctchctc" },
                        { "ccchccc", "ccccccc", "ccccccc", "ccccccc", "ccccccc", "ccccccc", "ccchccc" }, }))
                .addElement('c', lazy(t -> ofBlock(t.getCasingBlock(), t.getCasingMeta())))
                .addElement('s', lazy(t -> ofBlock(t.getShaftBlock(), t.getTurbineShaftMeta())))
                .addElement(
                    't',
                    lazy(
                        t -> buildHatchAdder(MTELargerTurbineBase.class).adder(MTELargerTurbineBase::addTurbineHatch)
                            .hatchClass(MTEHatchTurbine.class)
                            .casingIndex(t.getCasingTextureIndex())
                            .dot(1)
                            .build()))
                .addElement(
                    'h',
                    lazy(
                        t -> buildHatchAdder(MTELargerTurbineBase.class)
                            .atLeast(InputBus, InputHatch, OutputHatch, Dynamo.or(TTDynamo), Maintenance)
                            .casingIndex(t.getCasingTextureIndex())
                            .dot(4)
                            .buildAndChain(t.getCasingBlock(), t.getCasingMeta())))
                .addElement(
                    'm',
                    lazy(
                        t -> buildHatchAdder(MTELargerTurbineBase.class).atLeast(Muffler)
                            .casingIndex(t.getCasingTextureIndex())
                            .dot(7)
                            .buildAndChain(t.getCasingBlock(), t.getCasingMeta())))
                .build();
        }
    };

    protected int baseEff = 0;
    protected long optFlow = 0;
    protected long euPerTurbine = 0;
    protected double realOptFlow = 0;
    protected int storedFluid = 0;
    protected int counter = 0;
    protected boolean looseFit = false;
    protected float[] flowMultipliers = new float[] { 1, 1, 1 };

    public ITexture frontFace = new GTPPRenderedTexture(TexturesGtBlock.Overlay_Machine_Controller_Advanced);
    public ITexture frontFaceActive = new GTPPRenderedTexture(
        TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active);

    public ArrayList<MTEHatchTurbine> mTurbineRotorHatches = new ArrayList<>();

    public MTELargerTurbineBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTELargerTurbineBase(String aName) {
        super(aName);
    }

    protected abstract String getTurbineType();

    protected abstract String getCasingName();

    protected abstract boolean requiresOutputHatch();

    @Override
    protected final MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addInfo("Runs as fast as 16 Large Turbines of the same type, takes the space of 12")
            .addInfo("Right-click with screwdriver to enable loose fit")
            .addInfo("Optimal flow will increase or decrease depending on fitting")
            .addInfo("Loose fit increases flow in exchange for efficiency");
        if (getTurbineType().equals("Plasma")) {
            tt.addInfo("Plasma fuel efficiency is lower for high tier turbines when using low-grade plasmas")
                .addInfo("Efficiency = ((FuelValue / 200,000)^2) / (EU per Turbine)");
        }
        tt.addTecTechHatchInfo();
        tt.addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(7, 9, 7, false)
            .addController("Top Middle")
            .addCasingInfoMin(getCasingName(), 360, false)
            .addCasingInfoMin("Rotor Shaft", 30, false)
            .addOtherStructurePart("Rotor Assembly", "Any 1 dot hint", 1)
            .addInputBus("Any 4 dot hint (min 1)", 4)
            .addInputHatch("Any 4 dot hint(min 1)", 4);
        if (requiresOutputHatch()) {
            tt.addOutputHatch("Any 4 dot hint(min 1)", 4);
        }
        tt.addDynamoHatch("Any 4 dot hint(min 1)", 4)
            .addMaintenanceHatch("Any 4 dot hint(min 1)", 4);
        if (requiresMufflers()) {
            tt.addMufflerHatch("Any 7 dot hint (x4)", 7);
        }
        tt.toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<MTELargerTurbineBase> getStructureDefinition() {
        return STRUCTURE_DEFINITION.get(getClass());
    }

    private boolean requiresMufflers() {
        if (!GTMod.gregtechproxy.mPollution) {
            return false;
        }
        return getPollutionPerSecond(null) > 0;
    }

    @Override
    public void clearHatches() {
        super.clearHatches();
        mTurbineRotorHatches.clear();
    }

    @Override
    public boolean checkStructure(boolean aForceReset, IGregTechTileEntity aBaseMetaTileEntity) {
        boolean f = super.checkStructure(aForceReset, aBaseMetaTileEntity);
        if (f) {
            for (MTEHatchTurbine tHatch : mTurbineRotorHatches) {
                tHatch.sendUpdate();
            }
        }
        return f;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        // we do not check for casing count here. the bare minimal is 372 but we only require 360
        boolean aStructure = checkPiece(STRUCTURE_PIECE_MAIN, 3, 3, 0);
        log("Structure Check: " + aStructure);
        if (mTurbineRotorHatches.size() != 12 || mMaintenanceHatches.size() != 1
            || (mDynamoHatches.isEmpty() && mTecTechDynamoHatches.isEmpty())
            || (requiresMufflers() && mMufflerHatches.size() != 4)
            || mInputBusses.isEmpty()
            || mInputHatches.isEmpty()
            || (requiresOutputHatch() && mOutputHatches.isEmpty())) {
            log(
                "Bad Hatches - Turbine Housings: " + mTurbineRotorHatches.size()
                    + ", Maint: "
                    + mMaintenanceHatches.size()
                    + ", Dynamo: "
                    + mDynamoHatches.size()
                    + ", Muffler: "
                    + mMufflerHatches.size()
                    + ", Input Buses: "
                    + mInputBusses.size()
                    + ", Input Hatches: "
                    + mInputHatches.size()
                    + ", Output Hatches: "
                    + mOutputHatches.size());
            return false;
        }
        return aStructure;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 3, 3, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 2);
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 3, 3, 0, realBudget, env, false, true);
    }

    public boolean addTurbineHatch(final IGregTechTileEntity aTileEntity, final int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof MTEHatchTurbine aTurbineHatch) {
            log("Found MTEHatchTurbine");
            updateTexture(aTileEntity, aBaseCasingIndex);
            IGregTechTileEntity g = this.getBaseMetaTileEntity();
            if (aTurbineHatch.setController(new BlockPos(g.getXCoord(), g.getYCoord(), g.getZCoord(), g.getWorld()))) {
                boolean aDidAdd = this.mTurbineRotorHatches.add(aTurbineHatch);
                Logger.INFO("Injected Controller into Turbine Assembly. Found: " + this.mTurbineRotorHatches.size());
                return aDidAdd;
            } else {
                Logger.INFO("Failed to inject controller into Turbine Assembly Hatch.");
            }
        }
        log("Bad Turbine Housing");
        return false;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return getMaxEfficiency(aStack) > 0;
    }

    public Block getCasingBlock() {
        return ModBlocks.blockSpecialMultiCasings;
    }

    public final Block getShaftBlock() {
        return ModBlocks.blockSpecialMultiCasings;
    }

    public abstract int getCasingMeta();

    public byte getTurbineShaftMeta() {
        return 0;
    }

    public abstract int getCasingTextureIndex();

    public abstract int getFuelValue(FluidStack aLiquid);

    public static boolean isValidTurbine(ItemStack aTurbine) {
        return (aTurbine != null && aTurbine.getItem() instanceof MetaGeneratedTool
            && aTurbine.getItemDamage() >= 170
            && aTurbine.getItemDamage() <= 176);
    }

    protected ArrayList<ItemStack> getAllBufferedTurbines() {
        startRecipeProcessing();
        ArrayList<ItemStack> aTurbinesInStorage = new ArrayList<>();
        for (ItemStack aStack : getStoredInputs()) {
            if (isValidTurbine(aStack)) {
                int stackSize = aStack.stackSize;
                while (stackSize > 0) {
                    int tmpStackSize = Math.min(stackSize, aStack.getMaxStackSize());
                    ItemStack copy = aStack.copy();
                    copy.stackSize = tmpStackSize;
                    aTurbinesInStorage.add(copy);
                    stackSize -= tmpStackSize;
                }
            }
        }
        endRecipeProcessing();
        return aTurbinesInStorage;
    }

    public boolean areAllTurbinesTheSame() {
        ArrayList<MTEHatchTurbine> aTurbineAssemblies = getFullTurbineAssemblies();
        if (aTurbineAssemblies.size() < 12) {
            log("Found " + aTurbineAssemblies.size() + ", expected 12.");
            return false;
        }
        ArrayList<Materials> aTurbineMats = new ArrayList<>();
        ArrayList<Integer> aTurbineSizes = new ArrayList<>();
        for (MTEHatchTurbine aHatch : aTurbineAssemblies) {
            aTurbineMats.add(MetaGeneratedTool.getPrimaryMaterial(aHatch.getTurbine()));
            aTurbineSizes.add(getTurbineSize(aHatch.getTurbine()));
        }
        Materials aBaseMat = aTurbineMats.get(0);
        int aBaseSize = aTurbineSizes.get(0);
        for (int aSize : aTurbineSizes) {
            if (aBaseSize != aSize) {
                return false;
            }
        }
        for (Materials aMat : aTurbineMats) {
            if (aBaseMat != aMat) {
                return false;
            }
        }
        return true;
    }

    public static int getTurbineSize(ItemStack aTurbine) {
        if (isValidTurbine(aTurbine)) {
            if (aTurbine.getItemDamage() >= 170 && aTurbine.getItemDamage() < 172) {
                return 1;
            } else if (aTurbine.getItemDamage() >= 172 && aTurbine.getItemDamage() < 174) {
                return 2;
            } else if (aTurbine.getItemDamage() >= 174 && aTurbine.getItemDamage() < 176) {
                return 3;
            } else if (aTurbine.getItemDamage() >= 176 && aTurbine.getItemDamage() < 178) {
                return 4;
            }
        }
        return 0;
    }

    public static String getTurbineSizeString(int aSize) {
        return switch (aSize) {
            case 1 -> "Small Turbine";
            case 2 -> "Turbine";
            case 3 -> "Large Turbine";
            case 4 -> "Huge Turbine";
            default -> "";
        };
    }

    protected ArrayList<MTEHatchTurbine> getEmptyTurbineAssemblies() {
        ArrayList<MTEHatchTurbine> aEmptyTurbineRotorHatches = new ArrayList<>();
        // log("Checking "+mTurbineRotorHatches.size()+" Assemblies for empties.");
        for (MTEHatchTurbine aTurbineHatch : this.mTurbineRotorHatches) {
            if (!aTurbineHatch.hasTurbine()) {
                aEmptyTurbineRotorHatches.add(aTurbineHatch);
            }
        }
        return aEmptyTurbineRotorHatches;
    }

    protected ArrayList<MTEHatchTurbine> getFullTurbineAssemblies() {
        ArrayList<MTEHatchTurbine> aTurbineRotorHatches = new ArrayList<>();
        // log("Checking "+mTurbineRotorHatches.size()+" Assemblies for Turbines.");
        for (MTEHatchTurbine aTurbineHatch : this.mTurbineRotorHatches) {
            if (aTurbineHatch.hasTurbine()) {
                // log("Found Assembly with Turbine.");
                aTurbineRotorHatches.add(aTurbineHatch);
            }
        }
        return aTurbineRotorHatches;
    }

    protected void depleteTurbineFromStock(ItemStack aTurbine) {
        if (aTurbine == null) {
            return;
        }
        startRecipeProcessing();
        for (MTEHatchInputBus aInputBus : this.mInputBusses) {
            for (int slot = aInputBus.getSizeInventory() - 1; slot >= 0; slot--) {
                ItemStack aStack = aInputBus.getStackInSlot(slot);
                if (GTUtility.areStacksEqual(aStack, aTurbine)) {
                    aStack.stackSize -= aTurbine.stackSize;
                    updateSlots();
                    endRecipeProcessing();
                    return;
                }
            }
        }
        endRecipeProcessing();
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        try {
            ArrayList<MTEHatchTurbine> aEmptyTurbineRotorHatches = getEmptyTurbineAssemblies();
            if (!aEmptyTurbineRotorHatches.isEmpty()) {
                hatch: for (MTEHatchTurbine aHatch : aEmptyTurbineRotorHatches) {
                    ArrayList<ItemStack> aTurbines = getAllBufferedTurbines();
                    for (ItemStack aTurbineItem : aTurbines) {
                        if (aTurbineItem == null) {
                            continue;
                        }
                        if (aHatch.insertTurbine(aTurbineItem.copy())) {
                            depleteTurbineFromStock(aTurbineItem);
                            continue hatch;
                        }
                    }
                }
            }

            if (!getEmptyTurbineAssemblies().isEmpty() || !areAllTurbinesTheSame()) {
                stopMachine(ShutDownReasonRegistry.NO_TURBINE);
                return CheckRecipeResultRegistry.NO_TURBINE_FOUND;
            }

            ArrayList<FluidStack> tFluids = getStoredFluids();

            ItemStack aStack = getFullTurbineAssemblies().get(0)
                .getTurbine();

            TurbineStatCalculator turbine = new TurbineStatCalculator((MetaGeneratedTool) aStack.getItem(), aStack);

            if (!tFluids.isEmpty()) {
                if (baseEff == 0 || optFlow == 0
                    || counter >= 512
                    || this.getBaseMetaTileEntity()
                        .hasWorkJustBeenEnabled()
                    || this.getBaseMetaTileEntity()
                        .hasInventoryBeenModified()) {
                    counter = 0;
                    float aTotalBaseEff = 0;
                    float aTotalOptimalFlow = 0;

                    aTotalBaseEff += turbine.getEfficiency() * 100;
                    aTotalOptimalFlow += GTUtility
                        .safeInt((long) Math.max(Float.MIN_NORMAL, getSpeedMultiplier() * turbine.getOptimalFlow()));
                    baseEff = MathUtils.roundToClosestInt(aTotalBaseEff);
                    optFlow = MathUtils.roundToClosestInt(aTotalOptimalFlow);
                    if (aTotalOptimalFlow < 0) {
                        aTotalOptimalFlow = 100;
                    }

                    if (optFlow <= 0 || baseEff <= 0) {
                        stopMachine(ShutDownReasonRegistry.NONE); // in case the turbine got removed
                        return CheckRecipeResultRegistry.NO_FUEL_FOUND;
                    }
                } else {
                    counter++;
                }
            }

            // How much the turbine should be producing with this flow
            long newPower = fluidIntoPower(tFluids, turbine);
            long difference = newPower - this.lEUt; // difference between current output and new output

            // Magic numbers: can always change by at least 10 eu/t, but otherwise by at most 1 percent of the
            // difference in power level (per tick)
            // This is how much the turbine can actually change during this tick
            int maxChangeAllowed = Math.max(10, GTUtility.safeInt(Math.abs(difference) / 100));

            if (Math.abs(difference) > maxChangeAllowed) { // If this difference is too big, use the maximum allowed
                                                           // change
                int change = maxChangeAllowed * (difference > 0 ? 1 : -1); // Make the change positive or negative.
                this.lEUt += change; // Apply the change
            } else {
                this.lEUt = newPower;
            }
            if (this.lEUt <= 0) {
                this.lEUt = 0;
                this.mEfficiency = 0;
                return CheckRecipeResultRegistry.NO_FUEL_FOUND;
            } else {
                this.mMaxProgresstime = 1;
                this.mEfficiencyIncrease = 10;
                // Overvoltage is handled inside the MultiBlockBase when pushing out to dynamos. no need to do it here.
                // Play sounds (GT++ addition - GT multiblocks play no sounds)
                enableAllTurbineHatches();
                return CheckRecipeResultRegistry.GENERATING;
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return CheckRecipeResultRegistry.NO_FUEL_FOUND;
    }

    @Override
    public boolean doRandomMaintenanceDamage() {
        if (getMaxParallelRecipes() == 0) {
            stopMachine(ShutDownReasonRegistry.NO_TURBINE);
            return false;
        }

        if (mRuntime++ > 1000) {
            mRuntime = 0;

            if (getBaseMetaTileEntity().getRandomNumber(6000) < getMaintenanceThreshold()) {
                causeMaintenanceIssue();
            }
            for (MTEHatchTurbine aHatch : getFullTurbineAssemblies()) {
                // This cycle depletes durability from the turbine rotors.
                // The amount of times it is run depends on turbineDamageMultiplier
                // In XL turbines, durability loss is around 5.2-5.3x faster than in singles
                // To compensate for that, the mEU/t scaling is divided by 5 to make it only slightly faster
                for (int i = 0; i < getTurbineDamageMultiplier(); i++) {
                    aHatch.damageTurbine(lEUt / 5, damageFactorLow, damageFactorHigh);
                }
            }
        }
        return true;
    }

    @Override
    public int getMaxParallelRecipes() {
        return (getFullTurbineAssemblies().size());
    }

    abstract long fluidIntoPower(ArrayList<FluidStack> aFluids, TurbineStatCalculator turbine);

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 1;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return this.getMaxParallelRecipes() == 12 ? 10000 : 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    public boolean isLooseMode() {
        return looseFit;
    }

    @Override
    public String[] getExtraInfoData() {
        String tRunning = mMaxProgresstime > 0
            ? EnumChatFormatting.GREEN + StatCollector.translateToLocal("GT5U.turbine.running.true")
                + EnumChatFormatting.RESET
            : EnumChatFormatting.RED + StatCollector.translateToLocal("GT5U.turbine.running.false")
                + EnumChatFormatting.RESET;

        String tMaintenance = getIdealStatus() == getRepairStatus()
            ? EnumChatFormatting.GREEN + StatCollector.translateToLocal("GT5U.turbine.maintenance.false")
                + EnumChatFormatting.RESET
            : EnumChatFormatting.RED + StatCollector.translateToLocal("GT5U.turbine.maintenance.true")
                + EnumChatFormatting.RESET;
        int tDura;

        StringBuilder aTurbineDamage = new StringBuilder();
        for (MTEHatchTurbine aHatch : this.getFullTurbineAssemblies()) {
            ItemStack aTurbine = aHatch.getTurbine();
            tDura = MathUtils.safeInt(
                (long) (100.0f / MetaGeneratedTool.getToolMaxDamage(aTurbine)
                    * (MetaGeneratedTool.getToolDamage(aTurbine)) + 1));
            aTurbineDamage.append(EnumChatFormatting.RED)
                .append(GTUtility.formatNumbers(tDura))
                .append(EnumChatFormatting.RESET)
                .append("% | ");
        }

        long storedEnergy = 0;
        long maxEnergy = 0;
        for (MTEHatchDynamo tHatch : validMTEList(mDynamoHatches)) {
            storedEnergy += tHatch.getBaseMetaTileEntity()
                .getStoredEU();
            maxEnergy += tHatch.getBaseMetaTileEntity()
                .getEUCapacity();
        }

        boolean aIsSteam = this.getClass()
            .getName()
            .toLowerCase()
            .contains("steam");

        return new String[] {
            // 8 Lines available for information panels
            tRunning + ": "
                + EnumChatFormatting.RED
                + GTUtility.formatNumbers(((lEUt * mEfficiency) / 10000))
                + EnumChatFormatting.RESET
                + " EU/t",
            tMaintenance,
            StatCollector.translateToLocal("GT5U.turbine.efficiency") + ": "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers((mEfficiency / 100F))
                + EnumChatFormatting.RESET
                + "%",
            StatCollector.translateToLocal("GT5U.multiblock.energy") + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(storedEnergy)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(maxEnergy)
                + EnumChatFormatting.RESET
                + " EU",
            StatCollector.translateToLocal("GT5U.turbine.flow") + ": "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(MathUtils.safeInt((long) realOptFlow))
                + EnumChatFormatting.RESET
                + " L/s"
                + EnumChatFormatting.YELLOW
                + " ("
                + (isLooseMode() ? StatCollector.translateToLocal("GT5U.turbine.loose")
                    : StatCollector.translateToLocal("GT5U.turbine.tight"))
                + ")",
            StatCollector.translateToLocal("GT5U.turbine.fuel") + ": "
                + EnumChatFormatting.GOLD
                + GTUtility.formatNumbers(storedFluid)
                + EnumChatFormatting.RESET
                + "L",
            StatCollector.translateToLocal("GT5U.turbine.dmg") + ": " + aTurbineDamage,
            StatCollector.translateToLocal("GT5U.multiblock.pollution") + ": "
                + EnumChatFormatting.GREEN
                + getAveragePollutionPercentage()
                + EnumChatFormatting.RESET
                + " %" };
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public long maxAmperesOut() {
        return 16;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("turbineFitting", looseFit);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        looseFit = aNBT.getBoolean("turbineFitting");
    }

    @Override
    public void onModeChangeByScrewdriver(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (side == getBaseMetaTileEntity().getFrontFacing()) {
            looseFit ^= true;
            GTUtility.sendChatToPlayer(
                aPlayer,
                looseFit ? "Fitting: Loose - More Flow" : "Fitting: Tight - More Efficiency");
        }
    }

    public final ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side,
        ForgeDirection facing, int aColorIndex, boolean aActive, boolean aRedstone) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[1][aColorIndex + 1],
            facing == side ? getFrontFacingTurbineTexture(aActive)
                : Textures.BlockIcons.getCasingTextureForId(getCasingTextureIndex()) };
    }

    protected ITexture getFrontFacingTurbineTexture(boolean isActive) {
        if (isActive) {
            return frontFaceActive;
        }
        return frontFace;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            if (this.maxProgresstime() > 0 || this.getBaseMetaTileEntity()
                .hasWorkJustBeenEnabled()) {
                enableAllTurbineHatches();
            }
            if (this.maxProgresstime() <= 0) {
                stopMachine(ShutDownReasonRegistry.NONE);
            }
        }
    }

    @Override
    public void stopMachine(@NotNull ShutDownReason reason) {
        baseEff = 0;
        optFlow = 0;
        disableAllTurbineHatches();
        super.stopMachine(reason);
    }

    @Override
    public void onRemoval() {
        super.onRemoval();
        for (MTEHatchTurbine h : this.mTurbineRotorHatches) {
            h.clearController();
        }
        disableAllTurbineHatches();
        this.mTurbineRotorHatches.clear();
    }

    public void enableAllTurbineHatches() {
        updateTurbineHatches(this.isMachineRunning());
    }

    public void disableAllTurbineHatches() {
        updateTurbineHatches(false);
    }

    private Long mLastHatchUpdate;

    public void updateTurbineHatches(boolean aState) {
        if (mLastHatchUpdate == null) {
            mLastHatchUpdate = System.currentTimeMillis() / 1000;
        }
        if (this.mTurbineRotorHatches.isEmpty() || ((System.currentTimeMillis() / 1000) - mLastHatchUpdate) <= 2) {
            return;
        }
        for (MTEHatchTurbine h : validMTEList(this.mTurbineRotorHatches)) {
            h.setActive(aState);
        }

        mLastHatchUpdate = System.currentTimeMillis() / 1000;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> d == ForgeDirection.UP;
    }

    /**
     * Called every tick the Machine runs
     */
    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (lEUt > 0) {
            addEnergyOutput((lEUt * mEfficiency) / 10000);
            return true;
        }
        return false;
    }

    @Override
    public boolean addEnergyOutput(long aEU) {
        if (aEU <= 0) {
            return true;
        }
        if (!this.mAllDynamoHatches.isEmpty()) {
            return addEnergyOutputMultipleDynamos(aEU, true);
        }
        return false;
    }

    @Override
    public boolean addEnergyOutputMultipleDynamos(long aEU, boolean aAllowMixedVoltageDynamos) {
        int injected = 0;
        long aFirstVoltageFound = -1;
        for (MTEHatch aDynamo : validMTEList(mAllDynamoHatches)) {
            long aVoltage = aDynamo.maxEUOutput();
            // Check against voltage to check when hatch mixing
            if (aFirstVoltageFound == -1) {
                aFirstVoltageFound = aVoltage;
            }
        }

        long leftToInject;
        long aVoltage;
        int aAmpsToInject;
        int aRemainder;
        int ampsOnCurrentHatch;
        for (MTEHatch aDynamo : validMTEList(mAllDynamoHatches)) {
            leftToInject = aEU - injected;
            aVoltage = aDynamo.maxEUOutput();
            aAmpsToInject = (int) (leftToInject / aVoltage);
            aRemainder = (int) (leftToInject - (aAmpsToInject * aVoltage));
            ampsOnCurrentHatch = (int) Math.min(aDynamo.maxAmperesOut(), aAmpsToInject);

            // add full amps
            aDynamo.getBaseMetaTileEntity()
                .increaseStoredEnergyUnits(aVoltage * ampsOnCurrentHatch, false);
            injected += aVoltage * ampsOnCurrentHatch;

            // add reminder
            if (aRemainder > 0 && ampsOnCurrentHatch < aDynamo.maxAmperesOut()) {
                aDynamo.getBaseMetaTileEntity()
                    .increaseStoredEnergyUnits(aRemainder, false);
                injected += aRemainder;
            }
        }
        return injected > 0;
    }

    public int getSpeedMultiplier() {
        return 16;
    }

    public int getMaintenanceThreshold() {
        return 1;
    }

    public int getTurbineDamageMultiplier() {
        return 1;
    }

    @Override
    public boolean supportsBatchMode() {
        return false;
    }
}
