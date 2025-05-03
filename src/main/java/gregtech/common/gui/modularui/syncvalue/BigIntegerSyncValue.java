package gregtech.common.gui.modularui.syncvalue;

import com.cleanroommc.modularui.api.value.sync.IIntSyncValue;
import com.cleanroommc.modularui.api.value.sync.IStringSyncValue;
import com.cleanroommc.modularui.network.NetworkUtils;
import com.cleanroommc.modularui.value.sync.ValueSyncHandler;
import net.minecraft.network.PacketBuffer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BigIntegerSyncValue extends ValueSyncHandler<BigInteger> implements IBigIntegerSyncValue<BigInteger>, IIntSyncValue<BigInteger>, IStringSyncValue<BigInteger> {

    private final Supplier<BigInteger> getter;
    private final Consumer<BigInteger> setter;
    private BigInteger cache;

    public BigIntegerSyncValue(@NotNull Supplier<BigInteger> getter, @Nullable Consumer<BigInteger> setter) {
        this.getter = Objects.requireNonNull(getter);
        this.setter = setter;
        this.cache = getter.get();
    }

    public BigIntegerSyncValue(@NotNull Supplier<BigInteger> getter) {
        this(getter, (Consumer<BigInteger>) null);
    }

    @Contract("null, null -> fail")
    public BigIntegerSyncValue(@Nullable Supplier<BigInteger> clientGetter,
                         @Nullable Supplier<BigInteger> serverGetter) {
        this(clientGetter, null, serverGetter, null);
    }

    @Contract("null, _, null, _ -> fail")
    public BigIntegerSyncValue(@Nullable Supplier<BigInteger> clientGetter, @Nullable Consumer<BigInteger> clientSetter,
                         @Nullable Supplier<BigInteger> serverGetter, @Nullable Consumer<BigInteger> serverSetter) {
        if (clientGetter == null && serverGetter == null) {
            throw new NullPointerException("Client or server getter must not be null!");
        }
        if (NetworkUtils.isClient()) {
            this.getter = clientGetter != null ? clientGetter : serverGetter;
            this.setter = clientSetter != null ? clientSetter : serverSetter;
        } else {
            this.getter = serverGetter != null ? serverGetter : clientGetter;
            this.setter = serverSetter != null ? serverSetter : clientSetter;
        }
        this.cache = this.getter.get();
    }

    @Override
    public BigInteger getValue() {
        return this.cache;
    }

    @Override
    public BigInteger getBigIntegerValue() {
        return this.cache;
    }

    @Override
    public void setValue(BigInteger value, boolean setSource, boolean sync) {
        setBigIntegerValue(value, setSource, sync);
    }

    @Override
    public void setBigIntegerValue(BigInteger value, boolean setSource, boolean sync) {
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
        if (isFirstSync || this.getter.get() != this.cache) {
            setBigIntegerValue(this.getter.get(), false, false);
            return true;
        }
        return false;
    }

    @Override
    public void write(PacketBuffer buffer) {
        byte[] bytes = getValue().toByteArray();
        buffer.writeInt(bytes.length);
        buffer.writeBytes(bytes);
    }

    @Override
    public void read(PacketBuffer buffer) {
        int length = buffer.readInt();          // read length
        byte[] bytes = new byte[length];
        buffer.readBytes(bytes);                // read the bytes
        setValue(new BigInteger(bytes));        // reconstruct BigInteger
    }

    @Override
    public void setIntValue(int value, boolean setSource, boolean sync) {
        setBigIntegerValue(BigInteger.valueOf(value), setSource, sync);
    }

    @Override
    public void setStringValue(String value, boolean setSource, boolean sync) {
        setBigIntegerValue(new BigInteger(value), setSource, sync);
    }

    @Override
    public int getIntValue() {
        return this.cache.intValue();
    }

    @Override
    public String getStringValue() {
        return String.valueOf(this.cache);
    }

}
