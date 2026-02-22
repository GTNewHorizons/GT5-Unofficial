package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.GTValues.VN;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.IFluidBlock;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizons.modularui.api.NumberFormatMUI;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.gui.widgets.LockedWhileActiveButton;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;

public abstract class MTEConcreteBackfillerBase extends MTEDrillerBase {

    private int mLastXOff = 0, mLastZOff = 0;

    /** Used to drive the readout in the GUI for the backfiller's current y-level. */
    private int clientYHead;

    protected boolean mLiquidEnabled = true;

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
        addResultMessage(STATE_UPWARD, true, "backfiller_working");
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
            .beginStructureBlock(3, 7, 3, false)
            .addController("Front bottom")
            .addOtherStructurePart(casings, "form the 3x1x3 Base")
            .addOtherStructurePart(casings, "1x3x1 pillar above the center of the base (2 minimum total)")
            .addOtherStructurePart(getFrameMaterial().mName + " Frame Boxes", "Each pillar's side and 1x3x1 on top")
            .addEnergyHatch("1x " + VN[getMinTier()] + "+, Any base casing", 1)
            .addMaintenanceHatch("Any base casing", 1)
            .addInputBus("Mining Pipes, optional, any base casing", 1)
            .addInputHatch("GT Concrete, any base casing", 1)
            .addOutputBus("Mining Pipes, optional, any base casing", 1)
            .toolTipFinisher();
        return tt;
    }

    protected abstract int getRadius();

    @Override
    protected boolean checkHatches() {
        return !mMaintenanceHatches.isEmpty() && !mInputHatches.isEmpty() && mEnergyHatches.size() == 1;
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
        return (int) Math.max(1, (workState == STATE_UPWARD || simulateWorking ? 240 : 80) / GTUtility.powInt(2, tier));
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
            workState = STATE_DOWNWARD;
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
        if (!depleteInput(Materials.Concrete.getMolten(1 * INGOTS))) {
            mMaxProgresstime = 0;
            return false;
        }
        return true;
    }

    protected static final NumberFormatMUI numberFormat = new NumberFormatMUI();

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);
        screenElements
            .widget(
                TextWidget
                    .dynamicString(
                        () -> StatCollector.translateToLocalFormatted(
                            "GT5U.gui.text.backfiller_current_area",
                            numberFormat.format(clientYHead)))
                    .setSynced(false)
                    .setTextAlignment(Alignment.CenterLeft)
                    .setEnabled(widget -> getBaseMetaTileEntity().isActive() && workState == STATE_UPWARD))
            .widget(new FakeSyncWidget.IntegerSyncer(this::getYHead, newInt -> clientYHead = newInt))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> workState, newInt -> workState = newInt));
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        final int BUTTON_Y_LEVEL = 91;

        builder.widget(
            new LockedWhileActiveButton(this.getBaseMetaTileEntity(), builder)
                .setOnClick((clickData, widget) -> mLiquidEnabled = !mLiquidEnabled)
                .setPlayClickSound(true)
                .setBackground(() -> {
                    if (mLiquidEnabled) {
                        return new IDrawable[] { GTUITextures.BUTTON_STANDARD_PRESSED,
                            GTUITextures.OVERLAY_BUTTON_LIQUIDMODE };
                    }
                    return new IDrawable[] { GTUITextures.BUTTON_STANDARD, GTUITextures.OVERLAY_BUTTON_LIQUIDMODE_OFF };
                })
                .attachSyncer(
                    new FakeSyncWidget.BooleanSyncer(() -> mLiquidEnabled, newBoolean -> mLiquidEnabled = newBoolean),
                    builder,
                    (widget, val) -> widget.notifyTooltipChange())
                .dynamicTooltip(
                    () -> ImmutableList.of(
                        StatCollector.translateToLocal(
                            mLiquidEnabled ? "GT5U.gui.button.liquid_filling_ON"
                                : "GT5U.gui.button.liquid_filling_OFF")))
                .setTooltipShowUpDelay(TOOLTIP_DELAY)
                .setPos(new Pos2d(100, BUTTON_Y_LEVEL))
                .setSize(16, 16));
        int left = 98;
        for (ButtonWidget button : getAdditionalButtons(builder, buildContext)) {
            button.setPos(new Pos2d(left, BUTTON_Y_LEVEL))
                .setSize(16, 16);
            builder.widget(button);
            left += 18;
        }
    }
}
