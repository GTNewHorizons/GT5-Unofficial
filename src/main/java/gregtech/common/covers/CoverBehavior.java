package gregtech.common.covers;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;

import org.jetbrains.annotations.NotNull;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import io.netty.buffer.ByteBuf;

/**
 * For Covers with a special behavior. Has fixed storage format of 4 byte. Not very convenient...
 */
public class CoverBehavior extends Cover {

    protected int coverData;

    protected CoverBehavior(CoverContext context) {
        super(context, null);
    }

    protected CoverBehavior(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
        initializeData(context.getCoverInitializer());
    }

    public int getVariable() {
        return coverData;
    }

    public CoverBehavior setVariable(int newValue) {
        this.coverData = newValue;
        return this;
    }

    @Override
    public final void initializeData() {}

    @Override
    protected void loadFromNbt(NBTBase nbt) {
        this.coverData = nbt instanceof NBTTagInt ? ((NBTTagInt) nbt).func_150287_d() : 0;
    }

    @Override
    protected void readFromPacket(ByteArrayDataInput byteData) {
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

    protected static abstract class CoverBehaviorUIFactory extends CoverUiFactory<CoverBehavior> {

        protected CoverBehaviorUIFactory(CoverUIBuildContext buildContext) {
            super(buildContext);
        }

        public CoverBehavior adaptCover(Cover cover) {
            if (cover instanceof CoverBehavior adapterCover) {
                return adapterCover;
            }
            return null;
        }
    }
}
