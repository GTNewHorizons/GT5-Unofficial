package gregtech.common.covers;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;

import org.jetbrains.annotations.NotNull;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.covers.CoverContext;
import gregtech.api.interfaces.ITexture;
import io.netty.buffer.ByteBuf;

/**
 * For legacy covers using an int to store all their data, typically as a bit field. Has fixed storage format of 4 byte.
 * Not very convenient...
 *
 * @deprecated deprecated for new Cover implementations. Covers implementing this should be updated on an opportunistic
 *             basis
 */
@Deprecated
public class CoverLegacyData extends Cover {

    protected int coverData;

    protected CoverLegacyData(CoverContext context) {
        super(context, null);
    }

    protected CoverLegacyData(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
        this.coverData = 0;
    }

    public int getVariable() {
        return coverData;
    }

    public CoverLegacyData setVariable(int newValue) {
        this.coverData = newValue;
        return this;
    }

    @Override
    protected void readDataFromNbt(NBTBase nbt) {
        this.coverData = nbt instanceof NBTTagInt ? ((NBTTagInt) nbt).func_150287_d() : 0;
    }

    @Override
    public void readDataFromPacket(ByteArrayDataInput byteData) {
        coverData = byteData.readInt();
    }

    @Override
    protected @NotNull NBTBase saveDataToNbt() {
        return new NBTTagInt(this.coverData);
    }

    @Override
    protected void writeDataToByteBuf(ByteBuf byteBuf) {
        byteBuf.writeInt(coverData);
    }

    @Override
    public boolean isRedstoneSensitive(long aTimer) {
        return true;
    }

}
