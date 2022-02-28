package com.elisis.gtnhlanth.xmod.nei;

import com.elisis.gtnhlanth.Tags;
import com.elisis.gtnhlanth.loader.RecipeAdder;

import codechicken.nei.api.IConfigureNEI;

public class NEI_Config implements IConfigureNEI {

	@Override
	public String getName() {
		return "GTNH: Lanthanides NEI";	
	}

	@Override
	public String getVersion() {
		return Tags.VERSION;
	}

	@Override
	public void loadConfig() {
		//new DigesterHandler(RecipeAdder.instance.DigesterRecipes);
		
	}
	

}
