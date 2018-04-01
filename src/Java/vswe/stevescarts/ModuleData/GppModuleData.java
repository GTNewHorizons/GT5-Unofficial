package vswe.stevescarts.ModuleData;

import java.util.ArrayList;
import java.util.HashMap;

import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.sc2.modules.ModuleExoticSeeds;
import net.minecraft.init.Items;
import vswe.stevescarts.Helpers.ComponentTypes;
import vswe.stevescarts.Items.ModItems;
import vswe.stevescarts.Modules.ModuleBase;

public class GppModuleData extends ModuleData {

	static byte ID_ExoticSeedAddon = 105;
	public static ArrayList<ModuleData> moduleListCustom = new ArrayList<>();

	public GppModuleData(int id, String name, Class<? extends ModuleBase> moduleClass, int modularCost) {
		super(id, name, moduleClass, modularCost);
	}

	@SuppressWarnings("unchecked")
	public static void loadGpp() {
		try {
			HashMap<Byte, ModuleData> moduleList =
					(HashMap<Byte, ModuleData>) (ReflectionUtils.getField(ModuleData.class, "moduleList").get(ModuleData.class));
			ModuleData netherWartModule = moduleList.get((byte)58);

			ModuleDataGroup farmerGroup = netherWartModule.getRequirement().get(0);

			ModuleData exoticSeedModule =
					new ModuleData(ID_ExoticSeedAddon, "Crop: Exotic Seeds", ModuleExoticSeeds.class, 20).addRequirement(farmerGroup)
					.addRecipe(new Object[][]{{Items.wheat_seeds},
							{ComponentTypes.EMPTY_DISK.getItemStack()}
					});

			moduleListCustom.add(exoticSeedModule);

			HashMap<Byte, Boolean> validModules =
					(HashMap<Byte, Boolean>) (ReflectionUtils.getField(ModItems.class, "validModules").get(ModItems.class));

			validModules.put(ID_ExoticSeedAddon, true);

		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

}
