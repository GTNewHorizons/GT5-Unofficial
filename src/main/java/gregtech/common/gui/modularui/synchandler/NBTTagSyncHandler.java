package gregtech.common.gui.modularui.synchandler;

import java.io.IOException;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

import com.cleanroommc.modularui.value.sync.ValueSyncHandler;

public class NBTTagSyncHandler extends ValueSyncHandler<NBTTagCompound> {

    private final Supplier<NBTTagCompound> getter;
    private final Consumer<NBTTagCompound> setter;

    private BiPredicate<NBTTagCompound, NBTTagCompound> equality = Object::equals;

    private NBTTagCompound cache;

    public NBTTagSyncHandler(Supplier<NBTTagCompound> getter, Consumer<NBTTagCompound> setter) {
        this.getter = getter;
        this.setter = setter;
    }

    public NBTTagSyncHandler withEqualityFunc(BiPredicate<NBTTagCompound, NBTTagCompound> equality) {
        this.equality = equality;
        return this;
    }

    @Override
    public void setValue(NBTTagCompound value, boolean setSource, boolean sync) {
        this.cache = value;
        if (setSource && this.setter != null) {
            this.setter.accept(value);
        }
        if (sync) {
            sync(0, this::write);
        }
    }

    @Override
    public boolean updateCacheFromSource(boolean isFirstSync) {
        NBTTagCompound value = this.getter.get();

        if (isFirstSync || !equality.test(value, cache)) {
            setValue(value, false, false);
            return true;
        }

        return false;
    }

    @Override
    public void notifyUpdate() {
        setValue(this.getter.get(), false, true);
    }

    @Override
    public void write(PacketBuffer buffer) throws IOException {
        buffer.writeNBTTagCompoundToBuffer(this.getter.get());
    }

    @Override
    public void read(PacketBuffer buffer) throws IOException {
        setValue(buffer.readNBTTagCompoundFromBuffer(), true, false);
    }

    @Override
    public NBTTagCompound getValue() {
        return cache;
    }

    @Override
    public Class<NBTTagCompound> getValueType() {
        return NBTTagCompound.class;
    }
}
