package gtPlusPlus.xmod.gregtech.common.covers;

import java.util.concurrent.atomic.AtomicBoolean;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.tileentity.ICoverable;
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

    public int doCoverThings(ForgeDirection side, byte aInputRedstone, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity, long aTimer) {
        if (aCoverVariable == 0) {
            return aCoverVariable;
        }
        if ((aTileEntity instanceof IFluidHandler)) {
            // Logger.INFO("Trying to Void via Overflow.");
            IFluidHandler tTank1;
            ForgeDirection directionFrom;
            directionFrom = ForgeDirection.UNKNOWN;
            tTank1 = (IFluidHandler) aTileEntity;
            if (tTank1 != null) {
                FluidStack aTankStack = tTank1.getTankInfo(directionFrom)[0].fluid;
                if (aTankStack != null) {
                    // Logger.INFO("Found Fluid inside self - "+aTankStack.getLocalizedName()+", overflow point set at
                    // "+aCoverVariable+"L and we have "+aTankStack.amount+"L inside.");
                    if (aTankStack.amount > aCoverVariable) {
                        int aAmountToDrain = aTankStack.amount - aCoverVariable;
                        // Logger.INFO("There is "+aAmountToDrain+" more fluid in the tank than we would like.");
                        if (aAmountToDrain > 0) {
                            FluidStack tLiquid = tTank1.drain(directionFrom, Math.abs(aAmountToDrain), true);
                            if (tLiquid != null) {
                                // Logger.INFO("Drained "+aAmountToDrain+"L.");
                            }
                        }
                    }
                } else {
                    // Logger.INFO("Could not simulate drain on self.");
                }
            }
        }
        return aCoverVariable;
    }

    public int onCoverScrewdriverclick(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (GTUtility.getClickedFacingCoords(side, aX, aY, aZ)[0] >= 0.5F) {
            aCoverVariable += (mMaxTransferRate * (aPlayer.isSneaking() ? 0.1f : 0.01f));
        } else {
            aCoverVariable -= (mMaxTransferRate * (aPlayer.isSneaking() ? 0.1f : 0.01f));
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

    public boolean letsRedstoneGoIn(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    public boolean letsRedstoneGoOut(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    public boolean letsEnergyIn(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    public boolean letsEnergyOut(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    public boolean letsItemsIn(ForgeDirection side, int aCoverID, int aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return true;
    }

    public boolean letsItemsOut(ForgeDirection side, int aCoverID, int aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return true;
    }

    public boolean letsFluidIn(ForgeDirection side, int aCoverID, int aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return false;
    }

    public boolean letsFluidOut(ForgeDirection side, int aCoverID, int aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return true;
    }

    public boolean alwaysLookConnected(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

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
