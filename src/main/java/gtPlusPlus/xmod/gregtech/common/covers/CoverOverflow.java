package gtPlusPlus.xmod.gregtech.common.covers;

import java.util.concurrent.atomic.AtomicBoolean;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicTank;
import gregtech.api.metatileentity.implementations.MTEFluid;
import gregtech.api.util.CoverBehavior;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerNumericWidget;

public class CoverOverflow extends CoverBehavior {

    public final int mTransferRate;
    public final int mInitialTransferRate;
    public final int mMaxTransferRate;

    public CoverOverflow(int aTransferRate) {
        this.mTransferRate = aTransferRate * 1000 / 10;
        this.mInitialTransferRate = aTransferRate;
        this.mMaxTransferRate = aTransferRate * 1000;
    }

    private FluidStack doOverflowThing(FluidStack mFluid, int aAmountLimit) {
        if (mFluid != null)
            while (mFluid.amount > aAmountLimit) mFluid.amount -= Math.min(mFluid.amount - aAmountLimit, mTransferRate);
        return mFluid;
    }

    private FluidStack[] doOverflowThings(FluidStack[] mFluids, int aAmountLimit) {
        for (FluidStack mFluid : mFluids) doOverflowThing(mFluid, aAmountLimit);
        return mFluids;
    }

    public int doCoverThings(ForgeDirection mOutMachine, byte aInputRedstone, int aCoverID, int aAmountLimit,
        ICoverable aTileEntity, long aTimer) {

        if (aAmountLimit == 0) return aAmountLimit;

        if (aTileEntity instanceof BaseMetaTileEntity mBMTE) {
            IMetaTileEntity mIMTE = mBMTE.getMetaTileEntity();
            if (mIMTE instanceof MTEBasicTank mMTEBT)
                mMTEBT.setDrainableStack(doOverflowThing(mMTEBT.getDrainableStack(), aAmountLimit));
            else if (mIMTE instanceof MTEFluid mMTEF) doOverflowThings(mMTEF.mFluids, aAmountLimit);
        }

        return aAmountLimit;
    }

    public int onCoverScrewdriverclick(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (GTUtility.getClickedFacingCoords(side, aX, aY, aZ)[0] >= 0.5F) {
            aCoverVariable += (int) (mMaxTransferRate * (aPlayer.isSneaking() ? 0.1f : 0.01f));
        } else {
            aCoverVariable -= (int) (mMaxTransferRate * (aPlayer.isSneaking() ? 0.1f : 0.01f));
        }
        if (aCoverVariable > mMaxTransferRate) {
            aCoverVariable = mInitialTransferRate;
        }
        if (aCoverVariable <= 0) {
            aCoverVariable = mMaxTransferRate;
        }
        GTUtility.sendChatToPlayer(
            aPlayer,
            GTUtility.trans("322", "Overflow point: ") + aCoverVariable + GTUtility.trans("323", "L"));
        return aCoverVariable;
    }

    public boolean onCoverRightclick(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        EntityPlayer aPlayer, float aX, float aY, float aZ) {
        boolean aShift = aPlayer.isSneaking();
        int aAmount = aShift ? 128 : 8;
        if (GTUtility.getClickedFacingCoords(side, aX, aY, aZ)[0] >= 0.5F) {
            aCoverVariable += aAmount;
        } else {
            aCoverVariable -= aAmount;
        }
        if (aCoverVariable > mMaxTransferRate) {
            aCoverVariable = mInitialTransferRate;
        }
        if (aCoverVariable <= 0) {
            aCoverVariable = mMaxTransferRate;
        }
        GTUtility.sendChatToPlayer(
            aPlayer,
            GTUtility.trans("322", "Overflow point: ") + aCoverVariable + GTUtility.trans("323", "L"));
        aTileEntity.setCoverDataAtSide(side, new ISerializableObject.LegacyCoverData(aCoverVariable));
        return true;
    }

    @Override
    public boolean letsRedstoneGoIn(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsRedstoneGoOut(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsEnergyIn(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsEnergyOut(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsItemsIn(ForgeDirection side, int aCoverID, int aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsItemsOut(ForgeDirection side, int aCoverID, int aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsFluidIn(ForgeDirection side, int aCoverID, int aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsFluidOut(ForgeDirection side, int aCoverID, int aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return false;
    }

    @Override
    public boolean alwaysLookConnected(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public int getTickRate(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 5;
    }

    // GUI

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new OverflowUIFactory(buildContext).createWindow();
    }

    private class OverflowUIFactory extends UIFactory {

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        public OverflowUIFactory(CoverUIBuildContext buildContext) {
            super(buildContext);
        }

        @SuppressWarnings("PointlessArithmeticExpression")
        @Override
        protected void addUIWidgets(ModularWindow.Builder builder) {
            AtomicBoolean warn = new AtomicBoolean(false);

            builder
                .widget(
                    new CoverDataControllerWidget<>(this::getCoverData, this::setCoverData, CoverOverflow.this)
                        .addFollower(
                            new CoverDataFollowerNumericWidget<>(),
                            coverData -> (double) convert(coverData),
                            (coverData, state) -> new ISerializableObject.LegacyCoverData(state.intValue()),
                            widget -> widget.setBounds(0, mMaxTransferRate)
                                .setScrollValues(1000, 144, 100000)
                                .setFocusOnGuiOpen(true)
                                .setPos(startX + spaceX * 0, startY + spaceY * 1 + 8)
                                .setSize(spaceX * 4 - 3, 12)))
                .widget(
                    new TextWidget(GTUtility.trans("322", "Overflow point: ")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX, 4 + startY + spaceY * 0 + 8));
        }
    }
}
