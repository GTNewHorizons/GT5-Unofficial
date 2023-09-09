package gregtech.common.tileentities.machines.multi;

import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_Values.VN;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;

public abstract class GT_MetaTileEntity_ConcreteBackfillerBase extends GT_MetaTileEntity_DrillerBase {

    private int mLastXOff = 0, mLastZOff = 0;

    /** Used to drive the readout in the GUI for the backfiller's current y-level. */
    private int clientYHead;

    public GT_MetaTileEntity_ConcreteBackfillerBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        initRecipeResults();
    }

    public GT_MetaTileEntity_ConcreteBackfillerBase(String aName) {
        super(aName);
        initRecipeResults();
    }

    private void initRecipeResults() {
        addResultMessage(STATE_UPWARD, true, "backfiller_working");
    }

    protected GT_Multiblock_Tooltip_Builder createTooltip(String aStructureName) {
        String casings = getCasingBlockItem().get(0)
            .getDisplayName();

        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Concrete Backfiller")
            .addInfo("Controller Block for the " + aStructureName)
            .addInfo("Will fill in areas below it with light concrete. This goes through walls")
            .addInfo("Use it to remove any spawning locations beneath your base to reduce lag")
            .addInfo("Will pull back the pipes after it finishes that layer")
            .addInfo("Radius is " + getRadius() + " blocks")
            .addSeparator()
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
            .toolTipFinisher("Gregtech");
        return tt;
    }

    protected abstract int getRadius();

    @Override
    protected boolean checkHatches() {
        return !mMaintenanceHatches.isEmpty() && !mInputHatches.isEmpty() && mEnergyHatches.size() == 1;
    }

    @Override
    protected List<IHatchElement<? super GT_MetaTileEntity_DrillerBase>> getAllowedHatches() {
        return ImmutableList.of(InputHatch, InputBus, Maintenance, Energy);
    }

    @Override
    protected void setElectricityStats() {
        this.mEfficiency = getCurrentEfficiency(null);
        this.mEfficiencyIncrease = 10000;
        int tier = Math.max(1, GT_Utility.getTier(getMaxInputVoltage()));
        this.mEUt = -6 * (1 << (tier << 1));
        this.mMaxProgresstime = (workState == STATE_UPWARD ? 240 : 80) / (1 << tier);
        this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
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
            stopMachine();
            setShutdownReason(StatCollector.translateToLocal("GT5U.gui.text.backfiller_finished"));
            return false;
        }
    }

    private boolean isRefillableBlock(int aX, int aY, int aZ) {
        IGregTechTileEntity aBaseTile = getBaseMetaTileEntity();
        if (!aBaseTile.getBlock(aX, aY, aZ)
            .isAir(aBaseTile.getWorld(), aX, aY, aZ) || aBaseTile.getBlock(aX, aY, aZ)
                .getMaterial()
                .isSolid())
            return false;
        return GT_Utility
            .setBlockByFakePlayer(getFakePlayer(aBaseTile), aX, aY, aZ, GregTech_API.sBlockConcretes, 8, true);
    }

    private boolean tryRefillBlock(int aX, int aY, int aZ) {
        if (!tryConsumeFluid()) {
            setRuntimeFailureReason(CheckRecipeResultRegistry.BACKFILLER_NO_CONCRETE);
            return false;
        }
        getBaseMetaTileEntity().getWorld()
            .setBlock(aX, aY, aZ, GregTech_API.sBlockConcretes, 8, 3);
        return true;
    }

    private boolean tryConsumeFluid() {
        if (!depleteInput(Materials.Concrete.getMolten(144L))) {
            mMaxProgresstime = 0;
            return false;
        }
        return true;
    }

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);
        screenElements
            .widget(
                TextWidget
                    .dynamicString(
                        () -> StatCollector.translateToLocalFormatted(
                            "GT5U.gui.text.backfiller_current_area",
                            GT_Utility.formatNumbers(clientYHead)))
                    .setSynced(false)
                    .setTextAlignment(Alignment.CenterLeft)
                    .setEnabled(widget -> getBaseMetaTileEntity().isActive() && workState == STATE_UPWARD))
            .widget(new FakeSyncWidget.IntegerSyncer(this::getYHead, newInt -> clientYHead = newInt))
            .widget(new FakeSyncWidget.IntegerSyncer(() -> workState, newInt -> workState = newInt));
    }
}
