package gregtech.common.covers.redstone;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import org.jetbrains.annotations.NotNull;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.KeyProvider;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.common.covers.CoverPosition;
import gregtech.common.gui.modularui.cover.base.CoverBaseGui;
import gregtech.common.gui.modularui.cover.redstone.CoverWirelessDoesWorkDetectorGui;
import gregtech.common.gui.mui1.cover.WirelessActivityDetectorUIFactory;
import io.netty.buffer.ByteBuf;

public class CoverWirelessDoesWorkDetector extends CoverAdvancedRedstoneTransmitterBase {

    private ActivityMode mode;
    /** Whether the wireless detector cover also sets the tiles sided Redstone output */
    private boolean physical;

    public CoverWirelessDoesWorkDetector(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
        this.mode = ActivityMode.MACHINE_IDLE;
        this.physical = true;
    }

    public ActivityMode getMode() {
        return mode;
    }

    public CoverWirelessDoesWorkDetector setMode(ActivityMode mode) {
        this.mode = mode;
        return this;
    }

    public boolean isPhysical() {
        return physical;
    }

    public CoverWirelessDoesWorkDetector setPhysical(boolean physical) {
        this.physical = physical;
        return this;
    }

    @Override
    protected void readDataFromNbt(NBTBase nbt) {
        super.readDataFromNbt(nbt);

        NBTTagCompound tag = (NBTTagCompound) nbt;
        mode = ActivityMode.values()[tag.getInteger("mode")];
        if (tag.hasKey("physical")) {
            physical = tag.getBoolean("physical");
        } else {
            physical = false;
        }
    }

    @Override
    public void readDataFromPacket(ByteArrayDataInput byteData) {
        super.readDataFromPacket(byteData);
        mode = ActivityMode.values()[byteData.readInt()];
        physical = byteData.readBoolean();
    }

    @Override
    protected @NotNull NBTBase saveDataToNbt() {
        NBTTagCompound tag = (NBTTagCompound) super.saveDataToNbt();
        tag.setInteger("mode", mode.ordinal());
        tag.setBoolean("physical", physical);

        return tag;
    }

    @Override
    protected void writeDataToByteBuf(ByteBuf byteBuf) {
        super.writeDataToByteBuf(byteBuf);
        byteBuf.writeInt(mode.ordinal());
        byteBuf.writeBoolean(physical);
    }

    private byte computeSignalBasedOnActivity(ICoverable tileEntity) {

        if (tileEntity instanceof IMachineProgress mProgress) {
            boolean inverted = invert;
            int signal = 0;

            switch (mode) {
                case MACHINE_ENABLED -> signal = inverted == mProgress.isAllowedToWork() ? 0 : 15;
                case MACHINE_IDLE -> signal = inverted == (mProgress.getMaxProgress() == 0) ? 0 : 15;
                case RECIPE_PROGRESS -> {
                    int tScale = mProgress.getMaxProgress() / 15;

                    if (tScale > 0 && mProgress.hasThingsToDo()) {
                        signal = inverted ? (15 - mProgress.getProgress() / tScale)
                            : (mProgress.getProgress() / tScale);
                    } else {
                        signal = inverted ? 15 : 0;
                    }
                }
            }

            return (byte) signal;
        } else {
            return (byte) 0;
        }

    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) {
            return;
        }
        final byte signal = computeSignalBasedOnActivity(coverable);
        final CoverPosition key = getCoverKey(coverable, coverSide);
        setSignalAt(getUuid(), getFrequency(), key, signal);

        if (physical) {
            coverable.setOutputRedstoneSignal(coverSide, signal);
        } else {
            coverable.setOutputRedstoneSignal(coverSide, (byte) 0);
        }
    }

    @Override
    public boolean letsRedstoneGoOut() {
        return true;
    }

    @Override
    public boolean manipulatesSidedRedstoneOutput() {
        return true;
    }

    public enum ActivityMode implements KeyProvider {

        RECIPE_PROGRESS("gt.interact.desc.recipeprogress"),
        MACHINE_IDLE("gt.interact.desc.machineidle"),
        MACHINE_ENABLED("gt.interact.desc.mach_on");

        private final String key;

        ActivityMode(String key) {
            this.key = key;
        }

        @Override
        public String getKey() {
            return this.key;
        }
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new WirelessActivityDetectorUIFactory(buildContext).createWindow();
    }

    @Override
    protected @NotNull CoverBaseGui<?> getCoverGui() {
        return new CoverWirelessDoesWorkDetectorGui(this);
    }
}
