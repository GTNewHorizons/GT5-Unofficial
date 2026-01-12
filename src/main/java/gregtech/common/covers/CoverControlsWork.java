package gregtech.common.covers;

import java.lang.ref.WeakReference;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.conditions.RedstoneCondition;
import gregtech.common.gui.modularui.cover.CoverControlsWorkGui;
import gregtech.common.gui.modularui.cover.base.CoverBaseGui;
import gregtech.common.gui.mui1.cover.ControlsWorkUIFactory;

public class CoverControlsWork extends CoverLegacyData {

    public static boolean isCoverPlaceable(ForgeDirection side, ItemStack coverItem, ICoverable coverable) {
        for (final ForgeDirection tSide : ForgeDirection.VALID_DIRECTIONS) {
            if (coverable.getCoverAtSide(tSide) instanceof CoverControlsWork) {
                return false;
            }
        }
        return true;
    }

    private enum State {
        ENABLE_WITH_SIGNAL,
        DISABLE_WITH_SIGNAL,
        DISABLED,
        ENABLE_WITH_SIGNAL_SAFE,
        DISABLE_WITH_SIGNAL_SAFE;
    }

    private boolean handledShutdown = false;
    protected WeakReference<EntityPlayer> lastPlayer = null;

    public CoverControlsWork(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    public RedstoneCondition getRedstoneCondition() {
        if (coverData % 3 == 0) {
            return RedstoneCondition.ENABLE_WITH_REDSTONE;
        } else if (coverData % 3 == 1) {
            return RedstoneCondition.DISABLE_WITH_REDSTONE;
        }
        return RedstoneCondition.DISABLE;
    }

    public void setRedstoneCondition(RedstoneCondition mode) {
        boolean safeMode = isSafeMode();
        coverData = switch (mode) {
            case ENABLE_WITH_REDSTONE -> safeMode ? 3 : 0;
            case DISABLE_WITH_REDSTONE -> safeMode ? 4 : 1;
            case DISABLE -> safeMode ? 5 : 2;
        };
    }

    public boolean isSafeMode() {
        return coverData > 2;
    }

    public void setSafeMode(boolean safeMode) {
        if (safeMode && coverData < 3) {
            coverData = coverData + 3;
        } else if (!safeMode && coverData > 2) {
            coverData = coverData - 3;
        }
    }

    @Override
    public boolean onCoverShiftRightClick(EntityPlayer aPlayer) {
        ICoverable coverable = coveredTile.get();
        if (coverable != null && hasCoverGUI() && aPlayer instanceof EntityPlayerMP) {
            lastPlayer = new WeakReference<>(aPlayer);
            return super.onCoverShiftRightClick(aPlayer);
        }
        return false;
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable instanceof IMachineProgress machine) {
            State state = this.coverData < State.values().length ? State.values()[this.coverData] : State.DISABLED;
            switch (state) {
                case ENABLE_WITH_SIGNAL, DISABLE_WITH_SIGNAL -> {
                    if ((aInputRedstone > 0) == (state == State.ENABLE_WITH_SIGNAL)) {
                        if (!machine.isAllowedToWork()) {
                            machine.enableWorking();
                            handledShutdown = false;
                        }
                    } else {
                        if (machine.isAllowedToWork()) machine.disableWorking();
                    }
                }
                case DISABLED -> {
                    if (machine.isAllowedToWork()) machine.disableWorking();
                }
                case ENABLE_WITH_SIGNAL_SAFE, DISABLE_WITH_SIGNAL_SAFE -> {
                    if (machine.wasShutdown() && machine.getLastShutDownReason()
                        .wasCritical() && !handledShutdown) {
                        handledShutdown = true;
                        coverData = State.DISABLED.ordinal();
                    } else {
                        if ((aInputRedstone > 0) == (state == State.ENABLE_WITH_SIGNAL_SAFE)) {
                            if (!machine.isAllowedToWork()) {
                                machine.enableWorking();
                                handledShutdown = false;
                            }
                        } else {
                            if (machine.isAllowedToWork()) machine.disableWorking();
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isRedstoneSensitive(long aTimer) {
        return coverData != 2; // always off, so no redstone needed either
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
    public boolean letsFluidIn(Fluid fluid) {
        return true;
    }

    @Override
    public boolean letsFluidOut(Fluid fluid) {
        return true;
    }

    @Override
    public boolean letsItemsIn(int slot) {
        return true;
    }

    @Override
    public boolean letsItemsOut(int slot) {
        return true;
    }

    @Override
    public void onCoverRemoval() {
        if ((coveredTile.get() instanceof IMachineProgress machine)) {
            machine.enableWorking();
        }
    }

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        int newCoverData = (coverData + (aPlayer.isSneaking() ? -1 : 1)) % 5;
        if (newCoverData < 0) {
            newCoverData = 2;
        }
        if (newCoverData == 0) {
            GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.signal_on");
        }
        if (newCoverData == 1) {
            GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.signal_inverted");
        }
        if (newCoverData == 2) {
            GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.signal_off");
        }
        if (newCoverData == 3) {
            GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.signal_on_safe");
        }
        if (newCoverData == 4) {
            GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.signal_inverted_safe");
        }
        // TODO: Set lastPlayer
        coverData = newCoverData;
    }

    @Override
    public int getMinimumTickRate() {
        return 1;
    }

    // GUI stuff

    @Override
    protected @NotNull CoverBaseGui<?> getCoverGui() {
        return new CoverControlsWorkGui(this);
    }

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new ControlsWorkUIFactory(buildContext).createWindow();
    }

}
