package vswe.stevescarts.ModuleData;

import java.util.HashMap;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.sc2.modules.workers.tools.ModuleExoticFarmerUpgraded;
import vswe.stevescarts.Helpers.ComponentTypes;
import vswe.stevescarts.Helpers.ResourceHelper;
import vswe.stevescarts.Models.Cart.ModelCartbase;
import vswe.stevescarts.Models.Cart.ModelFarmer;
import vswe.stevescarts.ModuleData.ModuleData;
import vswe.stevescarts.ModuleData.ModuleData.SIDE;
import vswe.stevescarts.ModuleData.ModuleDataGroup;
import vswe.stevescarts.ModuleData.ModuleDataTool;
import vswe.stevescarts.Modules.ModuleBase;

public class ModuleDataLoader {	

	static int ID_ExoticFarmer = 0;
	public static HashMap<Byte, ModuleData> moduleListCustom = new HashMap<Byte, ModuleData>();

	public synchronized static HashMap<Byte, ModuleData> getList() {
		return ModuleData.getList();
	}

	public static void load() {
		HashMap<Byte, ModuleData> u = getList();
		if (u.size() < Byte.MAX_VALUE) {
			int mNextFreeID = u.size()+1;
			ID_ExoticFarmer= 105;
			final ModuleDataGroup farmerGroup = new ModuleDataGroup(vswe.stevescarts.Helpers.Localization.MODULE_INFO.FARMER_GROUP);
			final ModuleData farmerExotic = new ModuleDataTool(mNextFreeID, "Exotic Farmer",
					(Class<? extends ModuleBase>) ModuleExoticFarmerUpgraded.class, 75, true)
					.addSide(SIDE.FRONT).addRecipe(new Object[][]{
						{ComponentTypes.GALGADORIAN_METAL.getItemStack(),
							ComponentTypes.GALGADORIAN_METAL.getItemStack(),
							ComponentTypes.ENHANCED_GALGADORIAN_METAL.getItemStack()},
						{null, ComponentTypes.HUGE_DYNAMIC_PANE.getItemStack(), null},
						{ComponentTypes.ADVANCED_PCB.getItemStack(), ItemUtils.getItemStackOfAmountFromOreDict("blockNaquadah", 1),
							ComponentTypes.GRAPHICAL_INTERFACE.getItemStack()}});			
			farmerGroup.add(farmerExotic);	
			moduleListCustom.put((byte) ID_ExoticFarmer, farmerExotic);
		}
	}

	public static void load2() {
		ModuleData y = getList().get((byte) ID_ExoticFarmer);
		if (y != null) {
			y.addModel("Farmer",	(ModelCartbase) new ModelFarmer(ResourceHelper.getResource("/models/farmerModelGalgadorian.png"))).setModelMult(0.45f);
			Logger.REFLECTION("Added Model Data for Exotic Farm Module.");
		}
		else {
			Logger.REFLECTION("Failed getting ModuleData from Local Cache.");
		}

	}
}
