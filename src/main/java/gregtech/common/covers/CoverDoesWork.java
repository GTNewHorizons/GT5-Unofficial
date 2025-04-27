package gregtech.common.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.Fluid;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.mui1.cover.DoesWorkUIFactory;

public class CoverDoesWork extends CoverLegacyData {

    public static final int FLAG_INVERTED = 0x1;
    public static final int FLAG_PROGRESS = 0x2;
    public static final int FLAG_ENABLED = 0x4;

    public CoverDoesWork(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    @Override
    public boolean isRedstoneSensitive(long aTimer) {
        return false;
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) {
            return;
        }
        if ((coverable instanceof IMachineProgress mProgress)) {
            boolean inverted = isFlagSet(this.coverData, FLAG_INVERTED);
            int signal = 0;

            if (isFlagSet(this.coverData, FLAG_ENABLED)) {
                signal = inverted == mProgress.isAllowedToWork() ? 0 : 15;
            } else if (isFlagSet(this.coverData, FLAG_PROGRESS)) {
                signal = inverted == (mProgress.getMaxProgress() == 0) ? 0 : 15;
            } else {
                int tScale = mProgress.getMaxProgress() / 15;

                if (tScale > 0 && mProgress.hasThingsToDo()) {
                    signal = inverted ? (15 - mProgress.getProgress() / tScale) : (mProgress.getProgress() / tScale);
                } else {
                    signal = inverted ? 15 : 0;
                }
            }

            coverable.setOutputRedstoneSignal(coverSide, (byte) signal);
        } else {
            coverable.setOutputRedstoneSignal(coverSide, (byte) 0);
        }
    }

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        this.coverData = (this.coverData + (aPlayer.isSneaking() ? -1 : 1)) % 6;
        if (this.coverData < 0) {
            this.coverData = 5;
        }
        switch (this.coverData) {
            case 0 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("018", "Normal"));
            // Progress scaled
            case 1 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("019", "Inverted"));
            // ^ inverted
            case 2 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("020", "Ready to work"));
            // Not Running
            case 3 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("021", "Not ready to work"));
            // Running
            case 4 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("028", "Machine Enabled"));
            // Enabled
            case 5 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("029", "Machine Disabled"));
            // Disabled
        }
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
    public boolean letsFluidIn(Fluid aFluid) {
        return true;
    }

    @Override
    public boolean letsFluidOut(Fluid aFluid) {
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
    public boolean manipulatesSidedRedstoneOutput() {
        return true;
    }

    @Override
    public int getMinimumTickRate() {
        return 5;
    }

    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new DoesWorkUIFactory(buildContext).createWindow();
    }

    public static boolean isFlagSet(int coverVariable, int flag) {
        return (coverVariable & flag) == flag;
    }

}
