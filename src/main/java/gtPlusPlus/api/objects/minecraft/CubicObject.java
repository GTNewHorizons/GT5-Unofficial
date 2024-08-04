package gtPlusPlus.api.objects.minecraft;

import net.minecraftforge.common.util.ForgeDirection;

import gtPlusPlus.api.objects.data.AutoMap;

public class CubicObject<T> {

    public final T NORTH;
    public final T SOUTH;

    public final T WEST;
    public final T EAST;

    public final T UP;
    public final T DOWN;

    public CubicObject(AutoMap<T> aDataSet) {
        this(aDataSet.get(0), aDataSet.get(1), aDataSet.get(2), aDataSet.get(3), aDataSet.get(4), aDataSet.get(5));
    }

    public CubicObject(T[] aDataSet) {
        this(aDataSet[0], aDataSet[1], aDataSet[2], aDataSet[3], aDataSet[4], aDataSet[5]);
    }

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

    public T get(ForgeDirection side) {
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
