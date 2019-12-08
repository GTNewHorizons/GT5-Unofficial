package gtPlusPlus.api.objects.minecraft;

import gtPlusPlus.api.objects.data.AutoMap;
import net.minecraftforge.common.util.ForgeDirection;

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

	public T get(int aSide) {
		return get(ForgeDirection.getOrientation(aSide));
	}

	public T get(ForgeDirection aSide) {
		if (aSide == ForgeDirection.DOWN) {
			return DOWN;
		}
		else if (aSide == ForgeDirection.UP) {
			return UP;
		}
		else if (aSide == ForgeDirection.NORTH) {
			return NORTH;
		}
		else if (aSide == ForgeDirection.SOUTH) {
			return SOUTH;
		}
		else if (aSide == ForgeDirection.WEST) {
			return WEST;
		}
		else if (aSide == ForgeDirection.EAST) {
			return EAST;
		}
		else {
			return null;
		}
	}

}
