package gregtech.common.gui.modularui.multiblock.godforge.util;

import java.io.IOException;
import java.math.BigInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.api.value.IStringValue;
import com.cleanroommc.modularui.utils.ICopy;
import com.cleanroommc.modularui.utils.serialization.IByteBufAdapter;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;

public class BigIntSyncValue extends GenericSyncValue<BigInteger> implements IStringValue<BigInteger> {

    public BigIntSyncValue(@NotNull Supplier<BigInteger> getter, @Nullable Consumer<BigInteger> setter) {
        super(getter, setter, new IByteBufAdapter<>() {

            @Override
            public BigInteger deserialize(PacketBuffer buffer) throws IOException {
                return new BigInteger(
                    buffer.readBytes(buffer.readableBytes())
                        .array());
            }

            @Override
            public void serialize(PacketBuffer buffer, BigInteger u) throws IOException {
                buffer.writeBytes(u.toByteArray());
            }

            @Override
            public boolean areEqual(@NotNull BigInteger t1, @NotNull BigInteger t2) {
                return t1.equals(t2);
            }
        }, ICopy.immutable());
    }

    @Override
    public String getStringValue() {
        return getValue().toString();
    }

    @Override
    public void setStringValue(String val) {
        setValue(new BigInteger(val));
    }
}
