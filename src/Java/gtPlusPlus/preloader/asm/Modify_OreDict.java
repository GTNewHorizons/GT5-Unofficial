package gtPlusPlus.preloader.asm;

import java.lang.reflect.Field;
import java.util.*;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameData;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

@SuppressWarnings("unchecked")
public class Modify_OreDict extends OreDictionary{

	static Field stack;
	static Field id;

    private static Map<Integer, List<Integer>> stackToId; // Calculated from 128 * 0.75
    private static List<ArrayList<ItemStack>> idToStack; //ToDo: Unqualify to List when possible {1.8}
	
    
    static {
    	try {
			stack = ReflectionUtils.getField(OreDictionary.class, "stackToId");
	    	id = ReflectionUtils.getField(OreDictionary.class, "idToStack");
	    	stackToId = (Map<Integer, List<Integer>>) stack.get(OreDictionary.class);
	    	idToStack = (List<ArrayList<ItemStack>>) stack.get(OreDictionary.class);
		}
		catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {}
    }

	//Utils.LOG_INFO("O-"+(byte) nameField.get(clazz) + " | "+newValue);
    
	/**
     * Registers a ore item into the dictionary.
     * Raises the registerOre function in all registered handlers.
     *
     * @param name The name of the ore
     * @param id The ID of the ore
     * @param ore The ore's ItemStack
     */
    private static void registerOreImpl(String name, ItemStack ore)
    {
        if (name == null || name.isEmpty() || "Unknown".equals(name)) return; //prevent bad IDs.
        if (ore == null || ore.getItem() == null)
        {
            FMLLog.bigWarning("Invalid registration attempt for an Ore Dictionary item with name %s has occurred. The registration has been denied to prevent crashes. The mod responsible for the registration needs to correct this.", name);
            return; //prevent bad ItemStacks.
        }

        int oreID = getOreID(name);
        // HACK: use the registry name's ID. It is unique and it knows about substitutions. Fallback to a -1 value (what Item.getIDForItem would have returned) in the case where the registry is not aware of the item yet
        // IT should be noted that -1 will fail the gate further down, if an entry already exists with value -1 for this name. This is what is broken and being warned about.
        // APPARENTLY it's quite common to do this. OreDictionary should be considered alongside Recipes - you can't make them properly until you've registered with the game.
        String registryName = ore.getItem().delegate.name();
        int hash;
        if (registryName == null)
        {
            FMLLog.bigWarning("A broken ore dictionary registration with name %s has occurred. It adds an item (type: %s) which is currently unknown to the game registry. This dictionary item can only support a single value when"
                    + " registered with ores like this, and NO I am not going to turn this spam off. Just register your ore dictionary entries after the GameRegistry.\n"
                    + "TO USERS: YES this is a BUG in the mod "+Loader.instance().activeModContainer().getName()+" report it to them!", name, ore.getItem().getClass());
            hash = -1;
        }
        else
        {
            hash = GameData.getItemRegistry().getId(registryName);
        }
        if (ore.getItemDamage() != WILDCARD_VALUE)
        {
            hash |= ((ore.getItemDamage() + 1) << 16); // +1 so 0 is significant
        }

        //Add things to the baked version, and prevent duplicates
        List<Integer> ids = stackToId.get(hash);
        if (ids != null && ids.contains(oreID)) return;
        if (ids == null)
        {
            ids = Lists.newArrayList();
            stackToId.put(hash, ids);
        }
        ids.add(oreID);

        //Add to the unbaked version
        ore = ore.copy();
        idToStack.get(oreID).add(ore);
        MinecraftForge.EVENT_BUS.post(new OreRegisterEvent(name, ore));
    }
	
}
