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
import gregtech.common.covers.CoverLiquidMeter;
import gregtech.common.covers.CoverPosition;
import gregtech.common.gui.mui1.cover.WirelessFluidDetectorUIFactory;
import io.netty.buffer.ByteBuf;

public class CoverWirelessFluidDetector extends CoverAdvancedRedstoneTransmitterBase {

    /** The special value {@code 0} means threshold check is disabled. */
    private int threshold;
    /** Whether the wireless detector cover also sets the tiles sided Redstone output */
    private boolean physical;

    public CoverWirelessFluidDetector(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    public int getThreshold() {
        return threshold;
    }

    public CoverWirelessFluidDetector setThreshold(int threshold) {
        this.threshold = threshold;
        return this;
    }

    public boolean isPhysical() {
        return physical;
    }

    public CoverWirelessFluidDetector setPhysical(boolean physical) {
        this.physical = physical;
        return this;
    }

    @Override
    protected void initializeData() {
        super.initializeData();
        this.threshold = 0;
        this.physical = true;
    }

    @Override
    protected void loadFromNbt(NBTBase nbt) {
        super.loadFromNbt(nbt);

        NBTTagCompound tag = (NBTTagCompound) nbt;
        threshold = tag.getInteger("threshold");
        if (tag.hasKey("physical")) {
            physical = tag.getBoolean("physical");
        } else {
            physical = false;
        }
    }

    @Override
    protected void readFromPacket(ByteArrayDataInput byteData) {
        super.readFromPacket(byteData);
        threshold = byteData.readInt();
        physical = byteData.readBoolean();
    }

    @Override
    protected @NotNull NBTBase saveDataToNbt() {
        NBTTagCompound tag = (NBTTagCompound) super.saveDataToNbt();
        tag.setInteger("threshold", threshold);
        tag.setBoolean("physical", physical);

        return tag;
    }

    @Override
    protected void writeDataToByteBuf(ByteBuf byteBuf) {
        super.writeToByteBuf(byteBuf);
        byteBuf.writeInt(threshold);
        byteBuf.writeBoolean(physical);
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) {
            return;
        }
        final byte signal = CoverLiquidMeter.computeSignalBasedOnFluid(coverable, invert, threshold);
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
        return new WirelessFluidDetectorUIFactory(buildContext).createWindow();
    }

}
