package gregtech.common.covers.redstone;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import org.jetbrains.annotations.NotNull;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.common.gui.mui1.cover.AdvancedRedstoneReceiverBaseUIFactory;
import io.netty.buffer.ByteBuf;

public abstract class CoverAdvancedRedstoneReceiverBase extends CoverAdvancedWirelessRedstoneBase {

    private GateMode mode;

    public CoverAdvancedRedstoneReceiverBase(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
        this.mode = GateMode.AND;
    }

    public GateMode getGateMode() {
        return mode;
    }

    public CoverAdvancedRedstoneReceiverBase setMode(GateMode mode) {
        this.mode = mode;
        return this;
    }

    @Override
    protected void readDataFromNbt(NBTBase nbt) {
        super.readDataFromNbt(nbt);

        NBTTagCompound tag = (NBTTagCompound) nbt;
        mode = GateMode.values()[tag.getByte("mode")];
    }

    @Override
    public void readDataFromPacket(ByteArrayDataInput byteData) {
        super.readDataFromPacket(byteData);
        mode = GateMode.values()[byteData.readByte()];
    }

    @Override
    protected @NotNull NBTBase saveDataToNbt() {
        NBTTagCompound tag = (NBTTagCompound) super.saveDataToNbt();
        tag.setByte("mode", (byte) mode.ordinal());

        return tag;
    }

    @Override
    protected void writeDataToByteBuf(ByteBuf byteBuf) {
        super.writeDataToByteBuf(byteBuf);
        byteBuf.writeByte(mode.ordinal());
    }

    // GUI stuff

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new AdvancedRedstoneReceiverBaseUIFactory(buildContext).createWindow();
    }

    public enum GateMode {
        AND,
        NAND,
        OR,
        NOR,
        SINGLE_SOURCE
    }
}
