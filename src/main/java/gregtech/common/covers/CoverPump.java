package gregtech.common.covers;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.cover.base.CoverBaseGui;
import gregtech.common.gui.modularui.cover.base.CoverIOBaseGui;
import gregtech.common.gui.mui1.cover.PumpUIFactory;

public class CoverPump extends CoverIOBase {

    public final int mTransferRate;

    public CoverPump(CoverContext context, int aTransferRate, ITexture coverTexture) {
        super(context, coverTexture);
        this.mTransferRate = aTransferRate;
    }

    public boolean isRedstoneSensitive(long aTimer) {
        return false;
    }

    @Override
    protected void doTransfer(ICoverable coverable) {
        if (coverable instanceof IFluidHandler current) {
            final IFluidHandler toAccess = coverable.getITankContainerAtSide(coverSide);
            if (toAccess == null) return;

            transferFluid(current, toAccess, coverSide, this.coverData % 2 == 0);
        }
    }

    protected void transferFluid(IFluidHandler current, IFluidHandler toAccess, ForgeDirection coverSide,
        boolean export) {
        IFluidHandler source = export ? current : toAccess;
        IFluidHandler dest = export ? toAccess : current;
        ForgeDirection drainSide = export ? coverSide : coverSide.getOpposite();
        GTUtility.moveFluid(source, dest, drainSide, mTransferRate, this::canTransferFluid);
    }

    protected boolean canTransferFluid(FluidStack fluid) {
        return true;
    }

    @Override
    public boolean letsRedstoneGoIn() {
        return true;
    }

    @Override
    public boolean letsRedstoneGoOut() {
        return true;
    }

    @Override
    public boolean letsEnergyIn() {
        return true;
    }

    @Override
    public boolean letsEnergyOut() {
        return true;
    }

    @Override
    public boolean letsItemsIn(int aSlot) {
        return true;
    }

    @Override
    public boolean letsItemsOut(int aSlot) {
        return true;
    }

    @Override
    public boolean letsFluidIn(Fluid aFluid) {
        ICoverable coverable = coveredTile.get();
        if ((this.coverData > 1) && ((coverable instanceof IMachineProgress))) {
            if (((IMachineProgress) coverable).isAllowedToWork() != this.coverData % 6 < 4) {
                return false;
            }
        }
        return (this.coverData >= 6) || (this.coverData % 2 != 0);
    }

    @Override
    public boolean letsFluidOut(Fluid aFluid) {
        ICoverable coverable = coveredTile.get();
        if ((this.coverData > 1) && ((coverable instanceof IMachineProgress))) {
            if (((IMachineProgress) coverable).isAllowedToWork() != this.coverData % 6 < 4) {
                return false;
            }
        }
        return (this.coverData >= 6) || (this.coverData % 2 == 0);
    }

    @Override
    public boolean alwaysLookConnected() {
        return true;
    }

    @Override
    public int getMinimumTickRate() {
        return 1;
    }

    // GUI stuff

    @Override
    protected @NotNull CoverBaseGui<?> getCoverGui() {
        return new CoverIOBaseGui(this, "cover.pump");
    }

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new PumpUIFactory(buildContext).createWindow();
    }

}
