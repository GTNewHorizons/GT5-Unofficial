package gregtech.common.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.Fluid;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.modes.ShutterMode;
import gregtech.common.gui.modularui.cover.CoverShutterGui;
import gregtech.common.gui.modularui.cover.base.CoverBaseGui;
import gregtech.common.gui.mui1.cover.ShutterUIFactory;

public class CoverShutter extends CoverLegacyData {

    public CoverShutter(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    public ShutterMode getShutterMode() {
        int coverVariable = coverData;
        if (coverVariable >= 0 && coverVariable < ShutterMode.values().length) {
            return ShutterMode.values()[coverVariable];
        }
        return ShutterMode.OPEN_IF_ENABLED;
    }

    public void setShutterMode(ShutterMode mode) {
        setVariable(mode.ordinal());
    }

    @Override
    public boolean isRedstoneSensitive(long aTimer) {
        return false;
    }

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        this.coverData = (this.coverData + (aPlayer.isSneaking() ? -1 : 1)) % 4;
        if (this.coverData < 0) {
            this.coverData = 3;
        }
        switch (this.coverData) {
            case 0 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("082", "Open if work enabled"));
            case 1 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("083", "Open if work disabled"));
            case 2 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("084", "Only Output allowed"));
            case 3 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("085", "Only Input allowed"));
        }
        if (coveredTile.get() instanceof BaseMetaPipeEntity bmpe) {
            bmpe.reloadLocks();
        }
    }

    public boolean letsRedstoneGoIn() {
        return shouldAllow(3);
    }

    public boolean letsRedstoneGoOut() {
        return shouldAllow(2);
    }

    @Override
    public boolean letsEnergyIn() {
        return shouldAllow(3);
    }

    @Override
    public boolean letsEnergyOut() {
        return shouldAllow(2);
    }

    @Override
    public boolean letsFluidIn(Fluid aFluid) {
        return shouldAllow(3);
    }

    public boolean letsFluidOut(Fluid fluid) {
        return shouldAllow(2);
    }

    public boolean letsItemsIn(int slot) {
        return shouldAllow(3);
    }

    public boolean letsItemsOut(int slot) {
        return shouldAllow(2);
    }

    private boolean shouldAllow(int shutterMode) {
        if (this.coverData >= 2) {
            return this.coverData == shutterMode;
        }
        return !(coveredTile.get() instanceof IMachineProgress machine)
            || machine.isAllowedToWork() == (this.coverData % 2 == 0);
    }

    @Override
    public boolean alwaysLookConnected() {
        return true;
    }

    // GUI stuff

    @Override
    protected @NotNull CoverBaseGui<?> getCoverGui() {
        return new CoverShutterGui(this);
    }

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new ShutterUIFactory(buildContext).createWindow();
    }

}
