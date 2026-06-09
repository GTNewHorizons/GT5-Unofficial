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
        return switch (side) {
            case DOWN -> DOWN;
            case UP -> UP;
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            case EAST -> EAST;
            default -> null;
        };
    }
}
