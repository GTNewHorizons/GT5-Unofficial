package gtPlusPlus.xmod.gregtech.common.helpers.autocrafter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.GT4Entity_AutoCrafter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayerFactory;

public class AC_Helper_Utils {

	//AC maps
	public static final Map<Integer, GT4Entity_AutoCrafter> sAutocrafterMap = new HashMap<Integer, GT4Entity_AutoCrafter>();

	//Add Crafter
	public final static int addCrafter(GT4Entity_AutoCrafter AC) {
		if (!sAutocrafterMap.containsValue(AC)){
			int increase = sAutocrafterMap.size()+1;
			sAutocrafterMap.put(increase, AC);
			Utils.LOG_INFO("[A-C] "+"Added Auto-Crafter to index on position "+increase+".");
			return increase;
		}
		else {
			Utils.LOG_INFO("[A-C] Tried adding an Auto-Crafter to Index, but found one already there.");
		}
		return 0;
	}

	//Remove Crafter
	public final static boolean removeCrafter(int frequency) {		
		if (!sAutocrafterMap.isEmpty()){
			if (sAutocrafterMap.containsKey(frequency)){
				sAutocrafterMap.remove(frequency);
				return true;
			}
		}
		return false;
	}

	public final static boolean removeCrafter(GT4Entity_AutoCrafter AC) {		
		if (!sAutocrafterMap.isEmpty()){
			if (sAutocrafterMap.containsValue(AC)){
				sAutocrafterMap.remove(AC);
				return true;
			}
		}
		return false;
	}

	//Get Crafter
	public final static GT4Entity_AutoCrafter getCrafterByID(int ID) {
		if (!sAutocrafterMap.isEmpty()) {
			Set<Entry<Integer, GT4Entity_AutoCrafter>> players = sAutocrafterMap.entrySet();
			Iterator<Entry<Integer, GT4Entity_AutoCrafter>> i = players.iterator();
			while (i.hasNext()) {
				Entry<Integer, GT4Entity_AutoCrafter> current = i.next();
				if (current.getKey().equals(ID)) {
					return current.getValue();
				}
			}
		}
		Utils.LOG_WARNING("Failed. [getCrafterByID]");
		return null;
	}

	public final static int getIDByCrafter(GT4Entity_AutoCrafter AC) {
		if (!sAutocrafterMap.isEmpty()) {
			Set<Entry<Integer, GT4Entity_AutoCrafter>> players = sAutocrafterMap.entrySet();
			Iterator<Entry<Integer, GT4Entity_AutoCrafter>> i = players.iterator();
			while (i.hasNext()) {
				Entry<Integer, GT4Entity_AutoCrafter> current = i.next();
				if (current.getValue().equals(AC)) {
					return current.getKey();
				}
			}
		}
		Utils.LOG_WARNING("Failed. [getIDByCrafter]");
		return 0;
	}

}
