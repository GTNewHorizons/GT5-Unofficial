package gtPlusPlus.xmod.gregtech.api.world;

import static gtPlusPlus.xmod.gregtech.api.world.WorldGenUtils.mOresToRegister;

import gtPlusPlus.core.material.Material;

public class GTPP_Worldgen_Handler implements Runnable{

	

	@Override
	public void run() {

		for (GT_OreVein_Object ore : mOresToRegister){
			generateNewVein(ore);
		}

	}
	
	

	private final GTPP_Worldgen_GT_Ore_Layer generateNewVein(final GT_OreVein_Object ore){
		return generateNewVein(ore.mOreMixName, ore.minY, ore.maxY, ore.weight, ore.density, ore.size, ore.aPrimary, ore.aSecondary, ore.aBetween, ore.aSporadic);
	}
	
	private final GTPP_Worldgen_GT_Ore_Layer generateNewVein(String mOreMixName, int minY, int maxY, int weight, int density, int size,
			Material aPrimary, Material aSecondary, Material aBetween, Material aSporadic){
		return new GTPP_Worldgen_GT_Ore_Layer(
						"ore.mix."+mOreMixName, 	//String aName, 
						true,						//boolean aDefault, 
						minY, maxY,					//int aMinY, int aMaxY, 
						weight,						//int aWeight, 
						density,					//int aDensity, 
						size, 						//int aSize,
						aPrimary, 					//Materials aPrimary, 
						aSecondary, 				//Materials aSecondary, 
						aBetween, 					//Materials aBetween, 
						aSporadic); 				//Materials aSporadic
	}

}
