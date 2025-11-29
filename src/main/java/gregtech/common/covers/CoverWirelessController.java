package gregtech.common.covers;

import java.lang.ref.WeakReference;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import org.jetbrains.annotations.NotNull;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.covers.CoverContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.conditions.RedstoneCondition;
import gregtech.common.covers.redstone.CoverAdvancedRedstoneReceiverBase;
import gregtech.common.covers.redstone.CoverAdvancedWirelessRedstoneBase;
import gregtech.common.gui.modularui.cover.CoverWirelessControllerGui;
import gregtech.common.gui.modularui.cover.base.CoverBaseGui;
import io.netty.buffer.ByteBuf;

public class CoverWirelessController extends CoverAdvancedWirelessRedstoneBase {

    private enum State {
        ENABLE_WITH_SIGNAL,
        DISABLE_WITH_SIGNAL,
        DISABLED,
        ENABLE_WITH_SIGNAL_SAFE,
        DISABLE_WITH_SIGNAL_SAFE;
    }

    private State state = State.DISABLED;
    private boolean handledShutdown = false;
    protected WeakReference<EntityPlayer> lastPlayer = null;
    private boolean mPlayerNotified = false;

    public CoverWirelessController(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    @Override
    protected void readDataFromNbt(NBTBase nbt) {
        super.readDataFromNbt(nbt);
        NBTTagCompound tag = (NBTTagCompound) nbt;
        state = State.values()[tag.getByte("state")];
    }

    @Override
    public void readDataFromPacket(ByteArrayDataInput byteData) {
        super.readDataFromPacket(byteData);
        state = State.values()[byteData.readByte()];
    }

    @Override
    protected @NotNull NBTBase saveDataToNbt() {
        NBTTagCompound tag = (NBTTagCompound) super.saveDataToNbt();
        tag.setByte("state", (byte) state.ordinal());
        return tag;
    }

    @Override
    protected void writeDataToByteBuf(ByteBuf byteBuf) {
        super.writeDataToByteBuf(byteBuf);
        byteBuf.writeByte(state.ordinal());
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        byte signal = CoverAdvancedWirelessRedstoneBase
            .getSignalAt(uuid, frequency, CoverAdvancedRedstoneReceiverBase.GateMode.SINGLE_SOURCE);

        if (coverable instanceof IMachineProgress machine) {
            switch (state) {
                case ENABLE_WITH_SIGNAL, DISABLE_WITH_SIGNAL -> {
                    if ((signal > 0) == (state == State.ENABLE_WITH_SIGNAL)) {
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
                        if (!mPlayerNotified) {
                            EntityPlayer player = lastPlayer == null ? null : lastPlayer.get();
                            if (player != null) {
                                lastPlayer = null;
                                mPlayerNotified = true;
                                GTUtility.sendChatToPlayer(
                                    player,
                                    coverable.getInventoryName() + "at "
                                        + String.format(
                                            "(%d,%d,%d)",
                                            coverable.getXCoord(),
                                            coverable.getYCoord(),
                                            coverable.getZCoord())
                                        + " shut down.");
                            }
                        }
                        handledShutdown = true;
                        state = State.DISABLED;
                    } else {
                        if ((signal > 0) == (state == State.ENABLE_WITH_SIGNAL_SAFE)) {
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

    public RedstoneCondition getRedstoneCondition() {
        return switch (state) {
            case ENABLE_WITH_SIGNAL, ENABLE_WITH_SIGNAL_SAFE -> RedstoneCondition.ENABLE_WITH_REDSTONE;
            case DISABLE_WITH_SIGNAL, DISABLE_WITH_SIGNAL_SAFE -> RedstoneCondition.DISABLE_WITH_REDSTONE;
            case DISABLED -> RedstoneCondition.DISABLE;
        };
    }

    public void setRedstoneCondition(RedstoneCondition mode) {
        final boolean safeMode = isSafeMode();
        state = switch (mode) {
            case ENABLE_WITH_REDSTONE -> safeMode ? State.ENABLE_WITH_SIGNAL_SAFE : State.ENABLE_WITH_SIGNAL;
            case DISABLE_WITH_REDSTONE -> safeMode ? State.DISABLE_WITH_SIGNAL_SAFE : State.DISABLE_WITH_SIGNAL;
            case DISABLE -> State.DISABLED;
        };
    }

    public boolean isSafeMode() {
        return switch (state) {
            case ENABLE_WITH_SIGNAL_SAFE, DISABLE_WITH_SIGNAL_SAFE -> true;
            case ENABLE_WITH_SIGNAL, DISABLED, DISABLE_WITH_SIGNAL -> false;
        };
    }

    public void setSafeMode(boolean safeMode) {
        state = switch (state) {
            case ENABLE_WITH_SIGNAL, ENABLE_WITH_SIGNAL_SAFE -> safeMode ? State.ENABLE_WITH_SIGNAL_SAFE
                : State.ENABLE_WITH_SIGNAL;
            case DISABLE_WITH_SIGNAL, DISABLE_WITH_SIGNAL_SAFE -> safeMode ? State.DISABLE_WITH_SIGNAL_SAFE
                : State.DISABLE_WITH_SIGNAL;
            case DISABLED -> State.DISABLED;
        };
    }

    // GUI stuff

    @Override
    protected @NotNull CoverBaseGui<?> getCoverGui() {
        return new CoverWirelessControllerGui(this);
    }
}
