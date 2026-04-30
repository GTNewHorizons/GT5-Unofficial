package gregtech.common.tileentities.machines.multi.xlturbines;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Dynamo;
import static gregtech.api.enums.HatchElement.ExoticDynamo;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTUtility.validMTEList;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.utils.item.LimitingItemStackHandler;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.IToolStats;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.TurbineStatCalculator;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.gui.modularui.multiblock.MTEXLTurbineGui;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tools.ToolTurbine;
import gregtech.common.tools.ToolTurbineHuge;
import gregtech.common.tools.ToolTurbineLarge;
import gregtech.common.tools.ToolTurbineNormal;
import gregtech.common.tools.ToolTurbineSmall;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public abstract class MTEXLTurbineBase extends MTEExtendedPowerMultiBlockBase<MTEXLTurbineBase>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int OFFSET_X = 4;
    private static final int OFFSET_Y = 4;
    private static final int OFFSET_Z = 0;
    public static int casingAmount;
    private static final int TURBINE_SLOTS = 12;
    private static final String TURBINE_HOLDER_NBT = "turbineHolder";
    private static final ClassValue<IStructureDefinition<MTEXLTurbineBase>> STRUCTURE_DEFINITION = new ClassValue<>() {

        @Override
        protected IStructureDefinition<MTEXLTurbineBase> computeValue(Class<?> type) {
            return StructureDefinition.<MTEXLTurbineBase>builder()
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    new String[][] {
                        { "         ", "         ", "         ", "    F    ", "   F~F   ", "    F    ", "         ",
                            "         ", "         " },
                        { "         ", "         ", "         ", "   FFF   ", "   FDF   ", "   FFF   ", "         ",
                            "         ", "         " },
                        { "         ", "         ", "         ", "   FFF   ", "   FDF   ", "   FFF   ", "         ",
                            "         ", "         " },
                        { "         ", "         ", "    F    ", "   F F   ", "  F   F  ", "   F F   ", "    F    ",
                            "         ", "         " },
                        { "         ", "         ", "    F    ", "   FCF   ", "  FCECF  ", "   FCF   ", "    F    ",
                            "         ", "         " },
                        { "         ", "         ", "   FFF   ", "  F B F  ", "  FBEBF  ", "  F B F  ", "   FFF   ",
                            "         ", "         " },
                        { "         ", "         ", "   FFF   ", "  F B F  ", "  FBEBF  ", "  F B F  ", "   FFF   ",
                            "         ", "         " },
                        { "         ", "         ", "   AAA   ", "  A B A  ", "  ABEBA  ", "  A B A  ", "   AAA   ",
                            "         ", "         " },
                        { "         ", "         ", "   AAA   ", "  A   A  ", "  A E A  ", "  A   A  ", "   AAA   ",
                            "         ", "         " },
                        { "         ", "         ", "   AAA   ", "  A B A  ", "  ABEBA  ", "  A B A  ", "   AAA   ",
                            "         ", "         " },
                        { "         ", "         ", "   FFF   ", "  F B F  ", "  FBEBF  ", "  F B F  ", "   FFF   ",
                            "         ", "         " },
                        { "         ", "   FFF   ", "  F   F  ", " F     F ", " F  E  F ", " F     F ", "  F   F  ",
                            "   FFF   ", "         " },
                        { "         ", "  FFFFF  ", " F     F ", " F     F ", " F  E  F ", " F     F ", " F     F ",
                            "  FFFFF  ", "         " },
                        { "   FFF   ", "  F   F  ", " F     F ", "F   C   F", "F  CCC  F", "F   C   F", " F     F ",
                            "  F   F  ", "   FFF   " },
                        { "  FFDFF  ", " F     F ", "F       F", "F       F", "D   E   D", "F       F", "F       F",
                            " F     F ", "  FFDFF  " },
                        { "  FFDFF  ", " FC   CF ", "FCC  CCCF", "F CC C  F", "D   C   D", "F  C CC F", "FCCC  CCF",
                            " FC   CF ", "  FFDFF  " },
                        { " FFFDFFF ", "F       F", "F       F", "F       F", "D   E   D", "F       F", "F       F",
                            "F       F", " FFFDFFF " },
                        { " FFFDFFF ", "F C   C F", "FCCC  CCF", "F  C CC F", "D   C   D", "F CC C  F", "FCC  CCCF",
                            "F C   C F", " FFFDFFF " },
                        { "  FFFFF  ", " F     F ", "F       F", "F       F", "F   E   F", "F       F", "F       F",
                            " F     F ", "  FFFFF  " },
                        { "    F    ", "  FF FF  ", " F C   F ", " F  C CF ", "F  CCC  F", " FC C  F ", " F   C F ",
                            "  FF FF  ", "    F    " },
                        { "         ", "   FFF   ", "  F   F  ", " F     F ", " F  E  F ", " F     F ", "  F   F  ",
                            "   FFF   ", "         " },
                        { "    F    ", "  FF FF  ", " F   C F ", " FC C  F ", "F  CCC  F", " F  C CF ", " F C   F ",
                            "  FF FF  ", "    F    " },
                        { "  FFFFF  ", " F     F ", "F       F", "F       F", "F   E   F", "F       F", "F       F",
                            " F     F ", "  FFFFF  " },
                        { " FFFDFFF ", "F       F", "F  C    F", "F   C C F", "D  CCC  D", "F C C   F", "F    C  F",
                            "F       F", " FFFDFFF " },
                        { " FFFDFFF ", "F       F", "F       F", "F       F", "D   E   D", "F       F", "F       F",
                            "F       F", " FFFDFFF " },
                        { "  FFDFF  ", " FC  CCF ", "FCC  CCCF", "F CC C  F", "D   C   D", "F  C CC F", "FCCC  CCF",
                            " FC   CF ", "  FFDFF  " },
                        { "  FFDFF  ", " F     F ", "F       F", "F       F", "D   E   D", "F       F", "F       F",
                            " F     F ", "  FFDFF  " },
                        { "   FFF   ", "  F   F  ", " F     F ", "F       F", "F       F", "F       F", " F     F ",
                            "  F   F  ", "   FFF   " },
                        { "         ", "   FFF   ", "  F   F  ", " F     F ", " F     F ", " F     F ", "  F   F  ",
                            "   FFF   ", "         " } })
                .addElement('A', chainAllGlasses())
                .addElement('B', Casings.MVSolenoidSuperconductorCoil.asElement())
                .addElement(
                    'C',
                    lazy(
                        t -> t.getRotorCasing()
                            .asElement()))
                .addElement('D', lazy(t -> ofFrame(t.getFrameMaterial())))
                .addElement('E', Casings.TurbineShaft.asElement())
                .addElement(
                    'F',
                    lazy(
                        t -> buildHatchAdder(MTEXLTurbineBase.class).atLeastList(t.getAllowedHatches())
                            .casingIndex(t.getCasing().textureId)
                            .hint(1)
                            .buildAndChain(
                                onElementPass(
                                    x -> ++casingAmount,
                                    t.getCasing()
                                        .asElement()))))
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

    public final LimitingItemStackHandler turbineHolder = new LimitingItemStackHandler(TURBINE_SLOTS, 1);

    public MTEXLTurbineBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEXLTurbineBase(String aName) {
        super(aName);
    }

    public boolean requiresOutputHatch() {
        return false;
    }

    public boolean requiresMufflerHatch() {
        return false;
    }

    protected List<IHatchElement<? super MTEXLTurbineBase>> getAllowedHatches() {
        List<IHatchElement<? super MTEXLTurbineBase>> hatches = new ArrayList<>();
        hatches.add(InputBus);
        hatches.add(InputHatch);
        hatches.add(Maintenance);
        hatches.add(Dynamo.or(ExoticDynamo));
        if (requiresOutputHatch()) hatches.add(OutputHatch);
        if (requiresMufflerHatch()) hatches.add(Muffler);
        return hatches;
    }

    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

    @Override
    public IStructureDefinition<MTEXLTurbineBase> getStructureDefinition() {
        return STRUCTURE_DEFINITION.get(getClass());
    }

    private boolean requiresMufflers() {
        return getPollutionPerSecond(null) > 0;
    }

    public int minCasingAmount() {
        return 372;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        casingAmount = 0;
        boolean aStructure = checkPiece(
            STRUCTURE_PIECE_MAIN,
            getStructureOffsetX(),
            getStructureOffsetY(),
            getStructureOffsetZ());
        if (mMaintenanceHatches.size() != 1 || (mDynamoHatches.isEmpty() && mExoticDynamoHatches.isEmpty())
            || (requiresMufflers() && mMufflerHatches.size() != 4)
            || mInputBusses.isEmpty()
            || mInputHatches.isEmpty()
            || (requiresOutputHatch() && mOutputHatches.isEmpty())
            || casingAmount < minCasingAmount()) {
            return false;
        }
        return aStructure;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            hintsOnly,
            getStructureOffsetX(),
            getStructureOffsetY(),
            getStructureOffsetZ());
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 2);
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            getStructureOffsetX(),
            getStructureOffsetY(),
            getStructureOffsetZ(),
            realBudget,
            env,
            false,
            true);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return getMaxEfficiency(aStack) > 0;
    }

    protected int getStructureOffsetX() {
        return OFFSET_X;
    }

    protected int getStructureOffsetY() {
        return OFFSET_Y;
    }

    protected int getStructureOffsetZ() {
        return OFFSET_Z;
    }

    protected Casings getCasing() {
        return Casings.ReinforcedSteamTurbineCasing;
    }

    protected Materials getFrameMaterial() {
        return Materials.Steel;
    }

    protected Casings getRotorCasing() {
        return Casings.SteelPipeCasing;
    }

    public static boolean isValidTurbine(ItemStack aTurbine) {
        if (aTurbine == null || aTurbine.stackSize <= 0) {
            return false;
        }
        if (!(aTurbine.getItem() instanceof MetaGeneratedTool tool)) {
            return false;
        }

        IToolStats stats = tool.getToolStats(aTurbine);
        if (!(stats instanceof ToolTurbine)) {
            return false;
        }

        Materials material = MetaGeneratedTool.getPrimaryMaterial(aTurbine);
        return material != null && material.mToolSpeed > 0;
    }

    public boolean areAllTurbinesTheSame() {
        ItemStack aBaseTurbine = null;
        Materials aBaseMat = null;
        int aBaseSize = 0;

        for (int i = 0; i < turbineHolder.getSlots(); i++) {
            ItemStack aTurbine = turbineHolder.getStackInSlot(i);
            if (!isValidTurbine(aTurbine)) {
                return false;
            }
            if (aBaseTurbine == null) {
                aBaseTurbine = aTurbine;
                aBaseMat = MetaGeneratedTool.getPrimaryMaterial(aTurbine);
                aBaseSize = getTurbineSize(aTurbine);
            } else if (aBaseMat != MetaGeneratedTool.getPrimaryMaterial(aTurbine)
                || aBaseSize != getTurbineSize(aTurbine)) {
                    return false;
                }
        }

        return true;
    }

    protected ItemStack getPrimaryTurbine() {
        for (int i = 0; i < turbineHolder.getSlots(); i++) {
            ItemStack aTurbine = turbineHolder.getStackInSlot(i);
            if (isValidTurbine(aTurbine)) {
                return aTurbine;
            }
        }
        return null;
    }

    private int getLoadedTurbineCount() {
        int aCount = 0;
        for (int i = 0; i < turbineHolder.getSlots(); i++) {
            if (isValidTurbine(turbineHolder.getStackInSlot(i))) {
                aCount++;
            }
        }
        return aCount;
    }

    public static int getTurbineSize(ItemStack aTurbine) {
        if (isValidTurbine(aTurbine)) {
            IToolStats stats = ((MetaGeneratedTool) aTurbine.getItem()).getToolStats(aTurbine);
            if (stats instanceof ToolTurbineSmall) {
                return 1;
            } else if (stats instanceof ToolTurbineNormal) {
                return 2;
            } else if (stats instanceof ToolTurbineLarge) {
                return 3;
            } else if (stats instanceof ToolTurbineHuge) {
                return 4;
            }
        }
        return 0;
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        try {
            if (!areAllTurbinesTheSame()) {
                stopMachine(ShutDownReasonRegistry.NO_TURBINE);
                return CheckRecipeResultRegistry.NO_TURBINE_FOUND;
            }

            ArrayList<FluidStack> tFluids = getStoredFluids();

            ItemStack aStack = getPrimaryTurbine();
            if (aStack == null) {
                stopMachine(ShutDownReasonRegistry.NO_TURBINE);
                return CheckRecipeResultRegistry.NO_TURBINE_FOUND;
            }

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

                    aTotalBaseEff += turbine.getBaseEfficiency() * 100;
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
                return CheckRecipeResultRegistry.GENERATING;
            }
        } catch (Exception t) {
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

            if (shouldCheckMaintenance() && getBaseMetaTileEntity().getRandomNumber(6000) < getMaintenanceThreshold()) {
                causeMaintenanceIssue();
            }
            for (int slot = 0; slot < turbineHolder.getSlots(); slot++) {
                for (int i = 0; i < getTurbineDamageMultiplier(); i++) {
                    ItemStack aTurbine = turbineHolder.getStackInSlot(slot);
                    if (aTurbine == null) {
                        break;
                    }
                    damageTurbine(aTurbine, slot, lEUt / 5);
                }
            }
        }
        return true;
    }

    private void damageTurbine(ItemStack aTurbine, int slot, long aEUt) {
        if (isValidTurbine(aTurbine) && MathUtils.randInt(0, 1) == 0) {
            ((MetaGeneratedTool) aTurbine.getItem()).doDamage(
                aTurbine,
                (long) getDamageToComponent(aTurbine)
                    * (long) Math.min((float) aEUt / (float) damageFactorLow, Math.pow(aEUt, damageFactorHigh)));
            if (aTurbine.stackSize <= 0) {
                turbineHolder.setStackInSlot(slot, null);
                stopMachine(ShutDownReasonRegistry.NO_TURBINE);
            }
            markDirty();
        }
    }

    @Override
    public int getMaxParallelRecipes() {
        return getLoadedTurbineCount();
    }

    abstract long fluidIntoPower(ArrayList<FluidStack> aFluids, TurbineStatCalculator turbine);

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 1;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return this.getMaxParallelRecipes() == TURBINE_SLOTS ? 10000 : 0;
    }

    public boolean isLooseMode() {
        return looseFit;
    }

    @Override
    public long maxAmperesOut() {
        return 16;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("turbineFitting", looseFit);
        aNBT.setTag(TURBINE_HOLDER_NBT, turbineHolder.serializeNBT());
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        looseFit = aNBT.getBoolean("turbineFitting");
        turbineHolder.deserializeNBT(aNBT.getCompoundTag(TURBINE_HOLDER_NBT));
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        if (side == getBaseMetaTileEntity().getFrontFacing()) {
            looseFit ^= true;
            GTUtility.sendChatTrans(
                aPlayer,
                looseFit ? "GT5U.chat.turbine.fitting.loose" : "GT5U.chat.turbine.fitting.tight");
        }
    }

    @Override
    public final ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side,
        ForgeDirection facing, int aColorIndex, boolean aActive, boolean aRedstone) {
        return new ITexture[] { getCasing().getCasingTexture(),
            facing == side ? getFrontFacingTurbineTexture(aActive) : getCasing().getCasingTexture() };
    }

    protected ITexture getFrontFacingTurbineTexture(boolean isActive) {
        if (isActive) {
            return TextureFactory.builder()
                .addIcon(TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active)
                .extFacing()
                .build();
        }
        return TextureFactory.builder()
            .addIcon(TexturesGtBlock.Overlay_Machine_Controller_Advanced)
            .extFacing()
            .build();
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            if (this.maxProgresstime() <= 0) {
                stopMachine(ShutDownReasonRegistry.NONE);
            }
        }
    }

    @Override
    public void stopMachine(@NotNull ShutDownReason reason) {
        baseEff = 0;
        optFlow = 0;
        super.stopMachine(reason);
    }

    @Override
    public void onBlockDestroyed() {
        dropTurbineHolderInventory();
        super.onBlockDestroyed();
    }

    private void dropTurbineHolderInventory() {
        IGregTechTileEntity meta = getBaseMetaTileEntity();
        if (meta == null || meta.getWorld() == null || meta.getWorld().isRemote) {
            return;
        }

        World world = meta.getWorld();
        double x = meta.getXCoord() + 0.5;
        double y = meta.getYCoord() + 0.5;
        double z = meta.getZCoord() + 0.5;
        for (int i = 0; i < turbineHolder.getSlots(); i++) {
            ItemStack stack = turbineHolder.extractItem(i, Integer.MAX_VALUE, false);
            if (stack != null) {
                world.spawnEntityInWorld(new EntityItem(world, x, y, z, stack));
            }
        }
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
        if (!mDynamoHatches.isEmpty() || !mExoticDynamoHatches.isEmpty()) {
            return addEnergyOutputMultipleDynamos(aEU, true);
        }
        return false;
    }

    @Override
    public boolean addEnergyOutputMultipleDynamos(long aEU, boolean aAllowMixedVoltageDynamos) {
        long injected = 0;

        for (MTEHatch aDynamo : validMTEList(mDynamoHatches)) {
            injected = injectEnergyIntoDynamo(aEU, injected, aDynamo);
        }
        for (MTEHatch aDynamo : validMTEList(mExoticDynamoHatches)) {
            injected = injectEnergyIntoDynamo(aEU, injected, aDynamo);
        }
        return injected > 0;
    }

    private long injectEnergyIntoDynamo(long aEU, long injected, MTEHatch aDynamo) {
        long leftToInject = aEU - injected;
        long aVoltage = aDynamo.maxEUOutput();
        long aAmpsToInject = leftToInject / aVoltage;
        long aRemainder = leftToInject - (aAmpsToInject * aVoltage);
        long ampsOnCurrentHatch = Math.min(aDynamo.maxAmperesOut(), aAmpsToInject);

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
        return injected;
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

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    @Override
    public boolean showRecipeTextInGUI() {
        return false;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTEXLTurbineGui(this);
    }
}
