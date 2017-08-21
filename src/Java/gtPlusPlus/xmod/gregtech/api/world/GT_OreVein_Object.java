package gtPlusPlus.xmod.gregtech.api.world;

import gtPlusPlus.core.material.Material;

public class GT_OreVein_Object {

	final String mOreMixName; 	//String aName, 
	final int minY, maxY;		//int aMinY, int aMaxY, 
	final int weight;			//int aWeight, 
	final int density;			//int aDensity, 
	final int size;				//int aSize,
	final Material aPrimary; 	//Materials aPrimary, 
	final Material aSecondary; 	//Materials aSecondary, 
	final Material aBetween; 	//Materials aBetween, 
	final Material aSporadic;	//Materials aSporadic
	
	GT_OreVein_Object(String mOreMixName, int minY, int maxY, int weight, int density, int size,
			Material aPrimary, Material aSecondary, Material aBetween, Material aSporadic){
		this.mOreMixName = mOreMixName;
		this.minY = minY;
		this.maxY = maxY;
		this.weight = weight;
		this.density = density;
		this.size = size;
		this.aPrimary = aPrimary;
		this.aSecondary = aSecondary;
		this.aBetween = aBetween;
		this.aSporadic = aSporadic;
	}
	
}
