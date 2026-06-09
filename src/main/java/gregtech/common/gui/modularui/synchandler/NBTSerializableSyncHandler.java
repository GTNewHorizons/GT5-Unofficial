package gregtech.common.gui.modularui.synchandler;

import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.nbt.NBTTagCompound;

import com.cleanroommc.modularui.utils.item.INBTSerializable;

public class NBTSerializableSyncHandler<T extends INBTSerializable<NBTTagCompound>> extends NBTTagSyncHandler {

    public NBTSerializableSyncHandler(Supplier<T> ctor, Supplier<T> getter, Consumer<T> setter) {
        super(
            () -> getter.get()
                .serializeNBT(),
            tag -> {
                T value = ctor.get();
                value.deserializeNBT(tag);
                setter.accept(value);
            });
    }

    public NBTSerializableSyncHandler(Supplier<T> getter, Consumer<T> setter) {
        super(
            () -> getter.get()
                .serializeNBT(),
            tag -> {
                T value = getter.get();
                value.deserializeNBT(tag);
                setter.accept(value);
            });
    }

    public NBTSerializableSyncHandler(Supplier<T> ref) {
        super(
            () -> ref.get()
                .serializeNBT(),
            tag -> {
                T value = ref.get();
                value.deserializeNBT(tag);
            });
    }

    @Override
    public NBTSerializableSyncHandler<T> withEqualityFunc(BiPredicate<NBTTagCompound, NBTTagCompound> equality) {
        super.withEqualityFunc(equality);
        return this;
    }
}
