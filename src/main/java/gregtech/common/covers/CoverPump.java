package gregtech.common.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.mui1.cover.PumpUIFactory;

public class CoverPump extends CoverLegacyData {

    public final int mTransferRate;

    public CoverPump(CoverContext context, int aTransferRate, ITexture coverTexture) {
        super(context, coverTexture);
        this.mTransferRate = aTransferRate;
    }

    public boolean isRedstoneSensitive(long aTimer) {
        return false;
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) {
            return;
        }
        if ((this.coverData % 6 > 1) && ((coverable instanceof IMachineProgress machine))) {
            if (machine.isAllowedToWork() != this.coverData % 6 < 4) {
                return;
            }
        }

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
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        coverData = (coverData + (aPlayer.isSneaking() ? -1 : 1)) % 12;
        if (coverData < 0) {
            coverData = 11;
        }
        switch (coverData) {
            case 0 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("006", "Export"));
            case 1 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("007", "Import"));
            case 2 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("008", "Export (conditional)"));
            case 3 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("009", "Import (conditional)"));
            case 4 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("010", "Export (invert cond)"));
            case 5 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("011", "Import (invert cond)"));
            case 6 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("012", "Export allow Input"));
            case 7 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("013", "Import allow Output"));
            case 8 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("014", "Export allow Input (conditional)"));
            case 9 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("015", "Import allow Output (conditional)"));
            case 10 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("016", "Export allow Input (invert cond)"));
            case 11 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("017", "Import allow Output (invert cond)"));
        }
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
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new PumpUIFactory(buildContext).createWindow();
    }

}
