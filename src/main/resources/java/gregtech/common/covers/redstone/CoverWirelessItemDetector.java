package gregtech.common.covers.redstone;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import org.jetbrains.annotations.NotNull;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.common.covers.CoverItemMeter;
import gregtech.common.covers.CoverPosition;
import gregtech.common.gui.modularui.cover.base.CoverBaseGui;
import gregtech.common.gui.modularui.cover.redstone.CoverWirelessItemDetectorGui;
import gregtech.common.gui.mui1.cover.WirelessItemDetectorUIFactory;
import io.netty.buffer.ByteBuf;

public class CoverWirelessItemDetector extends CoverAdvancedRedstoneTransmitterBase {

    /**
     * The special value {@code -1} means all slots.
     */
    private int slot;
    /**
     * The special value {@code 0} means threshold check is disabled.
     */
    private int threshold;
    /** Whether the wireless detector cover also sets the tiles sided Redstone output */
    private boolean physical;

    public CoverWirelessItemDetector(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
        this.threshold = 0;
        this.slot = -1;
        this.physical = true;
    }

    public int getSlot() {
        return this.slot;
    }

    public CoverWirelessItemDetector setSlot(int slot) {
        this.slot = slot;
        return this;
    }

    public int getThreshold() {
        return this.threshold;
    }

    public CoverWirelessItemDetector setThreshold(int threshold) {
        this.threshold = threshold;
        return this;
    }

    public boolean isPhysical() {
        return physical;
    }

    public CoverWirelessItemDetector setPhysical(boolean physical) {
        this.physical = physical;
        return this;
    }

    @Override
    protected void readDataFromNbt(NBTBase nbt) {
        super.readDataFromNbt(nbt);

        NBTTagCompound tag = (NBTTagCompound) nbt;
        threshold = tag.getInteger("threshold");
        slot = tag.getInteger("slot");

        if (tag.hasKey("physical")) {
            physical = tag.getBoolean("physical");
        } else {
            physical = false;
        }
    }

    @Override
    public void readDataFromPacket(ByteArrayDataInput byteData) {
        super.readDataFromPacket(byteData);
        threshold = byteData.readInt();
        slot = byteData.readInt();
        physical = byteData.readBoolean();
    }

    @Override
    protected @NotNull NBTBase saveDataToNbt() {
        NBTTagCompound tag = (NBTTagCompound) super.saveDataToNbt();
        tag.setInteger("threshold", threshold);
        tag.setInteger("slot", slot);
        tag.setBoolean("physical", physical);

        return tag;
    }

    @Override
    protected void writeDataToByteBuf(ByteBuf byteBuf) {
        super.writeDataToByteBuf(byteBuf);
        byteBuf.writeInt(threshold);
        byteBuf.writeInt(slot);
        byteBuf.writeBoolean(physical);
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) {
            return;
        }
        byte signal = CoverItemMeter.computeSignalBasedOnItems(coverable, invert, threshold, slot, coverSide.ordinal());
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

    // GUI stuff

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new WirelessItemDetectorUIFactory(buildContext).createWindow();
    }

    @Override
    protected @NotNull CoverBaseGui<?> getCoverGui() {
        return new CoverWirelessItemDetectorGui(this);
    }
}
