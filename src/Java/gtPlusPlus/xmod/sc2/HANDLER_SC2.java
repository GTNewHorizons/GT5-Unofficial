package gtPlusPlus.xmod.sc2;

import java.lang.reflect.Field;
import java.util.HashMap;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.sc2.items.ItemCartModuleEx;
import vswe.stevescarts.Items.ItemCartModule;
import vswe.stevescarts.ModuleData.ModuleData;
import vswe.stevescarts.ModuleData.ModuleDataLoader;

public class HANDLER_SC2 {

	private static final String MODITEMS = "vswe.stevescarts.Items.ModItems";
	private static Class MODITEMSCLASS;
	public static ItemCartModule modules;

	@SuppressWarnings("unchecked")
	public synchronized static void preInit(){
		if (LoadedMods.StevesCarts){	

			HashMap<Byte, Boolean> validModulesOld = null;	
			HashMap<Byte, Boolean> validModulesNew = new HashMap<Byte, Boolean>();			
			try {
				MODITEMSCLASS = Class.forName(MODITEMS);
				if (MODITEMSCLASS == null) {
					Logger.REFLECTION("Failed Registering Custom SC2 Modules. [1]");
					return;
				}				
				Field validModulesField = ReflectionUtils.getField(MODITEMSCLASS, "validModules");
				if (validModulesField != null) {
					validModulesOld = (HashMap<Byte, Boolean>) validModulesField.get(null);
				}
			}
			catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException | ClassNotFoundException e) {
				e.printStackTrace();
				Logger.REFLECTION("Failed Registering Custom SC2 Modules. [2]");
				return;
			}

			if (validModulesOld == null) {
				Logger.REFLECTION("Failed Registering Custom SC2 Modules. [3]");
				return;
			}
			else {
				validModulesNew.putAll(validModulesOld);
			}

			if (validModulesNew == null || validModulesNew.isEmpty()) {
				Logger.REFLECTION("Failed Registering Custom SC2 Modules. [4]");
				return;
			}

			modules = new ItemCartModuleEx();
			GameRegistry.registerItem((Item) modules, "CartModule++");
			ModuleDataLoader.load();
			for (final ModuleData module : ModuleDataLoader.moduleListCustom.values()) {
				if (!module.getIsLocked()) {
					Logger.REFLECTION("Mapping Custom SC2 Module. Using ID: "+module.getID());
					validModulesNew.put(module.getID(), true);
				}
			}
			for (final ModuleData module : ModuleDataLoader.moduleListCustom.values()) {
				final ItemStack submodule = new ItemStack((Item) modules, 1, (int) module.getID());
				if (submodule != null) {
					Logger.REFLECTION("Registering Custom SC2 Module. Using ID: "+module.getID());
					GameRegistry.registerCustomItemStack(submodule.getUnlocalizedName(), submodule);					
				}
			}

			try {
				Logger.REFLECTION("Setting 'validModules' field in "+MODITEMS+". Old Map was "+validModulesOld.size()+" objects, New map is "+validModulesNew.size()+" objects.");
				ReflectionUtils.setFieldValue(MODITEMSCLASS, "validModules", validModulesNew);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			Logger.REFLECTION("Failed Registering Custom SC2 Modules. [0]");
			return;
		}
	}

	public static void init(){
		if (LoadedMods.StevesCarts){
			ModuleDataLoader.load2();
		}
	}

	public static void postInit(){
		if (LoadedMods.StevesCarts){

		}
	}


}
