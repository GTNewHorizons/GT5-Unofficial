package tectech.mechanics.pipe;

import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IConnectsToBECPipe {

    public static final int CONNECTION_NONE = 0, CONNECTION_INPUT = 0b1, CONNECTION_OUTPUT = 0b10,
        CONNECTION_PIPE = 0b11;

    @Nullable
    IConnectsToBECPipe getNext(@NotNull IConnectsToBECPipe source);

    int getConnectionOnSide(ForgeDirection side, byte colorization);

    void markUsed();
}
