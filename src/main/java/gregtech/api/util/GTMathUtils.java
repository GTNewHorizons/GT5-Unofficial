package gregtech.api.util;

import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;
import org.joml.Vector3ic;

public class GTMathUtils {

    private static final Vector3ic[] DIRS = new Vector3ic[ForgeDirection.values().length];

    static {
        for (int i = 0; i < DIRS.length; i++) {
            ForgeDirection dir = ForgeDirection.values()[i];
            DIRS[i] = new Vector3i(dir.offsetX, dir.offsetY, dir.offsetZ);
        }
    }

    @NotNull
    public static Vector3ic vec(@NotNull ForgeDirection dir) {
        return DIRS[dir.ordinal()];
    }
}
