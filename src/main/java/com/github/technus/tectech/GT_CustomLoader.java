package com.github.technus.tectech;

import com.github.technus.tectech.blocks.QuantumGlass;
import com.github.technus.tectech.casing.GT_Loader_CasingsTT;
import cpw.mods.fml.common.registry.GameRegistry;

public class GT_CustomLoader {


	public GT_CustomLoader() {
		ElementalCasing = new GT_Loader_CasingsTT();
		ElementalLoader = new GT_Loader_Elemental();
	}

	private GT_Loader_Elemental ElementalLoader = null;
	private GT_Loader_CasingsTT ElementalCasing = null;

	public void run() {
		ElementalCasing.run();
		ElementalLoader.run();
	}
}
