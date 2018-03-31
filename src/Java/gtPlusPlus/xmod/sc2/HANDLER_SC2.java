package gtPlusPlus.xmod.sc2;

import java.util.HashMap;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.xmod.sc2.items.ItemCartModuleEx;
import vswe.stevescarts.Items.ItemCartModule;
import vswe.stevescarts.ModuleData.ModuleData;
import vswe.stevescarts.ModuleData.ModuleDataLoader;

public class HANDLER_SC2 {

	public static ItemCartModule modules;
	public static HashMap<Byte, Boolean> validModules;
	
	public synchronized static void preInit(){
		if (LoadedMods.StevesCarts){			
			modules = new ItemCartModuleEx();
			GameRegistry.registerItem((Item) modules, "CartModule++");
			ModuleDataLoader.load();
			for (final ModuleData module : ModuleDataLoader.getList().values()) {
				if (!module.getIsLocked()) {
					validModules.put(module.getID(), true);
				}
			}
			for (final ModuleData module : ModuleDataLoader.getList().values()) {
				final ItemStack submodule = new ItemStack((Item) modules, 1, (int) module.getID());
				GameRegistry.registerCustomItemStack(submodule.getUnlocalizedName(), submodule);
			}			
		}
	}

	public static void init(){
		if (LoadedMods.StevesCarts){

		}
	}

	public static void postInit(){
		if (LoadedMods.StevesCarts){
			
		}
	}


}
