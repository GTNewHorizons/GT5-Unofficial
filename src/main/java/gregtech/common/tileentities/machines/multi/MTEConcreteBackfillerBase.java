package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.IFluidBlock;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.gui.modularui.multiblock.MTEConcreteBackfillerBaseGui;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;

public abstract class MTEConcreteBackfillerBase extends MTEDrillerBase {

    private int mLastXOff = 0, mLastZOff = 0;
    private int clientYHead;
    private boolean mLiquidEnabled = true;

    public boolean isLiquidEnabled() {
        return mLiquidEnabled;
    }

    public void setLiquidEnabled(boolean val) {
        mLiquidEnabled = val;
    }

    public void setClientYHead(int val) {
        clientYHead = val;
    }

    private static boolean isWater(Block aBlock) {
        return aBlock == Blocks.water || aBlock == Blocks.flowing_water;
    }

    private static boolean isLava(Block aBlock) {
        return aBlock == Blocks.lava || aBlock == Blocks.flowing_lava;
    }

    private static boolean isFluid(Block aBlock) {
        return isWater(aBlock) || isLava(aBlock) || aBlock instanceof IFluidBlock;
    }

    public MTEConcreteBackfillerBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        initRecipeResults();
    }

    public MTEConcreteBackfillerBase(String aName) {
        super(aName);
        initRecipeResults();
    }

    private void initRecipeResults() {
        addResultMessage(WorkState.UPWARD, true, "backfiller_working");
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("liquidenabled", mLiquidEnabled);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("liquidenabled")) mLiquidEnabled = aNBT.getBoolean("liquidenabled");
    }

    protected MultiblockTooltipBuilder createTooltip(String aStructureName) {
        String casings = getCasingBlockItem().get(0)
            .getDisplayName();

        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        final int baseCycleTime = calculateMaxProgressTime(getMinTier(), true);
        tt.addMachineType("Concrete Backfiller")
            .addInfo("Will fill in areas below it with light concrete. This goes through walls")
            .addInfo("Use it to remove any spawning locations beneath your base to reduce lag")
            .addInfo("Will pull back the pipes after it finishes that layer")
            .addInfo("Range is " + getRadius() + "x" + getRadius() + " blocks horizontally")
            .addInfo("Minimum energy hatch tier: " + GTUtility.getColoredTierNameFromTier((byte) getMinTier()))
            .addInfo(
                "Base cycle time: " + (baseCycleTime < 20 ? formatNumber(baseCycleTime) + " ticks"
                    : formatNumber(baseCycleTime / 20.0) + " seconds"))
            .beginStructureBlock(3, 3, 7, false)
            .addController("Front bottom center")
            .addCasing("15", getFrameMaterial().mName + " Frame Box", false)
            .addCasing("3-8", casings, false)
            .addEnergyHatch("1+", "Any bottom casing", 1)
            .addMaintenanceHatch("1", "Any bottom casing", 1)
            .addInputBus("0+", "Any bottom casing", 1)
            .addInputHatch("1+", "Any bottom casing", 1)
            .addOutputBus("0+", "Any bottom casing", 1)
            .toolTipFinisher();
        return tt;
    }

    protected abstract int getRadius();

    @Override
    protected void checkHatches(List<StructureError> errors) {
        checkOneEnergyHatch(errors);
        checkHasMaintenanceHatch(errors);
        checkHasInputHatch(errors);
    }

    @Override
    protected List<IHatchElement<? super MTEDrillerBase>> getAllowedHatches() {
        return ImmutableList.of(InputHatch, InputBus, OutputBus, Maintenance, Energy);
    }

    @Override
    protected void setElectricityStats() {
        this.mEfficiency = getCurrentEfficiency(null);
        this.mEfficiencyIncrease = 10000;
        int tier = Math.max(1, GTUtility.getTier(getMaxInputVoltage()));
        this.mEUt = -6 * (1 << (tier << 1));
        this.mMaxProgresstime = calculateMaxProgressTime(tier);
        this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
    }

    @Override
    public int calculateMaxProgressTime(int tier, boolean simulateWorking) {
        return (int) Math
            .max(1, (workState == WorkState.UPWARD || simulateWorking ? 240 : 80) / GTUtility.powInt(2, tier));
    }

    @Override
    protected boolean workingUpward(ItemStack aStack, int xDrill, int yDrill, int zDrill, int xPipe, int zPipe,
        int yHead, int oldYHead) {
        if (isRefillableBlock(xPipe, yHead - 1, zPipe)) return tryRefillBlock(xPipe, yHead - 1, zPipe);
        int radius = getRadius();
        if (mLastXOff == 0 && mLastZOff == 0) {
            mLastXOff = -radius;
            mLastZOff = -radius;
        }
        if (yHead != yDrill) {
            for (int i = mLastXOff; i <= radius; i++) {
                for (int j = (i == mLastXOff ? mLastZOff : -radius); j <= radius; j++) {
                    if (isRefillableBlock(xPipe + i, yHead, zPipe + j)) {
                        mLastXOff = i;
                        mLastZOff = j;
                        return tryRefillBlock(xPipe + i, yHead, zPipe + j);
                    }
                }
            }
        }

        if (tryPickPipe()) {
            mLastXOff = 0;
            mLastZOff = 0;
            return true;
        } else {
            workState = WorkState.DOWNWARD;
            stopMachine(ShutDownReasonRegistry.NONE);
            setShutdownReason(StatCollector.translateToLocal("GT5U.gui.text.backfiller_finished"));
            return false;
        }
    }

    private boolean isRefillableBlock(int aX, int aY, int aZ) {
        IGregTechTileEntity aBaseTile = getBaseMetaTileEntity();
        Block aBlock = aBaseTile.getBlock(aX, aY, aZ);
        if (!aBlock.isAir(aBaseTile.getWorld(), aX, aY, aZ)) {
            if (mLiquidEnabled) {
                if (!isFluid(aBlock)) {
                    return false;
                }
            } else {
                return false;
            }
        }
        if (aBlock.getMaterial()
            .isSolid()) {
            return false;
        }
        return GTUtility
            .setBlockByFakePlayer(getFakePlayer(aBaseTile), aX, aY, aZ, GregTechAPI.sBlockConcretes, 8, true);
    }

    private boolean tryRefillBlock(int aX, int aY, int aZ) {
        if (!tryConsumeFluid()) {
            setRuntimeFailureReason(CheckRecipeResultRegistry.BACKFILLER_NO_CONCRETE);
            return false;
        }
        getBaseMetaTileEntity().getWorld()
            .setBlock(aX, aY, aZ, GregTechAPI.sBlockConcretes, 8, 3);
        return true;
    }

    private boolean tryConsumeFluid() {
        if (!depleteInput(
            MaterialLibAPI.getFluidStack(
                Materials2Materials.Concrete,
                Materials2FluidShapes.shapeFluidMolten,
                (int) (1 * INGOTS)))) {
            mMaxProgresstime = 0;
            return false;
        }
        return true;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTEConcreteBackfillerBaseGui(this);
    }
}
