package com.github.technus.tectech;

import com.github.technus.tectech.casing.GT_Loader_CasingsTT;

public class GT_CustomLoader {
	private GT_Loader_Machines ElementalLoader;
	private GT_Loader_CasingsTT ElementalCasing;
	private GT_Loader_Recipes ElementalRecipes;

	public GT_CustomLoader() {}

	public void run() {
		ElementalCasing = new GT_Loader_CasingsTT();
		ElementalLoader = new GT_Loader_Machines();
		ElementalCasing.run();
		ElementalLoader.run();
	}

	public void run2() {
		ElementalRecipes = new GT_Loader_Recipes();
		ElementalRecipes.run();
	}
}
