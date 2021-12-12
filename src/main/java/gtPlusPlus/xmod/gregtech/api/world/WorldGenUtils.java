package gtPlusPlus.xmod.gregtech.api.world;

import java.util.ArrayList;
import java.util.List;

import gtPlusPlus.core.material.Material;

public class WorldGenUtils {

	static List<GT_OreVein_Object> mOresToRegister = new ArrayList<GT_OreVein_Object>();
	
	public static final void addNewOreMixForWorldgen(GT_OreVein_Object newVein){
		mOresToRegister.add(newVein);
	}
	
	public static boolean generateNewOreVeinObject(String mOreMixName, int minY, int maxY, int weight, int density, int size,
			Material aPrimary, Material aSecondary, Material aBetween, Material aSporadic){
		GT_OreVein_Object newVein = new GT_OreVein_Object(mOreMixName, minY, maxY, weight, density, size, aPrimary, aSecondary, aBetween, aSporadic);
		addNewOreMixForWorldgen(newVein);
		return true;
	}
	
}
