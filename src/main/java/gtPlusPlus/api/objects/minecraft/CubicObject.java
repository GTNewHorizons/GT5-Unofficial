package gtPlusPlus.api.objects.minecraft;

import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.Nullable;

public final class CubicObject<T> {

    public final T NORTH;
    public final T SOUTH;

    public final T WEST;
    public final T EAST;

    public final T UP;
    public final T DOWN;

    public CubicObject(T aDOWN, T aUP, T aNORTH, T aSOUTH, T aWEST, T aEAST) {
        DOWN = aDOWN;
        UP = aUP;
        NORTH = aNORTH;
        SOUTH = aSOUTH;
        WEST = aWEST;
        EAST = aEAST;
    }

    public T get(int ordinalSide) {
        return get(ForgeDirection.getOrientation(ordinalSide));
    }

    public @Nullable T get(ForgeDirection side) {
        if (side == ForgeDirection.DOWN) {
            return DOWN;
        } else if (side == ForgeDirection.UP) {
            return UP;
        } else if (side == ForgeDirection.NORTH) {
            return NORTH;
        } else if (side == ForgeDirection.SOUTH) {
            return SOUTH;
        } else if (side == ForgeDirection.WEST) {
            return WEST;
        } else if (side == ForgeDirection.EAST) {
            return EAST;
        } else {
            return null;
        }
    }
}
