package gtPlusPlus.xmod.gregtech.common.helpers;

import static gtPlusPlus.core.lib.CORE.sTesseractGeneratorOwnershipMap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import gtPlusPlus.xmod.gregtech.common.tileentities.automation.GT_MetaTileEntity_TesseractGenerator;
import net.minecraft.entity.player.EntityPlayer;

public class TesseractHelper {

	public Map<Integer, GT_MetaTileEntity_TesseractGenerator> getOwnershipByPlayer(EntityPlayer player){
		UUID playerIdentifier = player.getUniqueID();
		if (!sTesseractGeneratorOwnershipMap.isEmpty() && playerIdentifier != null){
			Set<Entry<UUID, Map<Integer, GT_MetaTileEntity_TesseractGenerator>>> players = sTesseractGeneratorOwnershipMap.entrySet();
			Iterator<Entry<UUID, Map<Integer, GT_MetaTileEntity_TesseractGenerator>>> i = players.iterator();
			while(i.hasNext()) {
				Entry<UUID, Map<Integer, GT_MetaTileEntity_TesseractGenerator>> current = i.next();
				if (current.getKey().compareTo(playerIdentifier) == 0){
					return current.getValue();
				}
				
			}			
		}	
		return null;
	}
	
	public boolean isTesseractOwnedByPlayer(EntityPlayer player, GT_MetaTileEntity_TesseractGenerator generator){
		UUID playerIdentifier = player.getUniqueID();
		if (!sTesseractGeneratorOwnershipMap.isEmpty() && playerIdentifier != null){
		
			Map<Integer, GT_MetaTileEntity_TesseractGenerator> generators = getOwnershipByPlayer(player);
			Set<Entry<Integer, GT_MetaTileEntity_TesseractGenerator>> players = generators.entrySet();
			Iterator<Entry<Integer, GT_MetaTileEntity_TesseractGenerator>> i = players.iterator();
			while(i.hasNext()) {
				Entry<Integer, GT_MetaTileEntity_TesseractGenerator> current = i.next();
				if (current.getValue().equals(generator)){
					return true;
				}				
			}		
		}		
		return false;		
	}
	
	public boolean setOwnershipByPlayer(EntityPlayer player, int freq, GT_MetaTileEntity_TesseractGenerator generator){
		UUID playerIdentifier = player.getUniqueID();
		if (playerIdentifier != null){
			Map<Integer, GT_MetaTileEntity_TesseractGenerator> playerOwned = sTesseractGeneratorOwnershipMap.get(playerIdentifier);	
			if (playerOwned == null || playerOwned.isEmpty()){
				Map<Integer, GT_MetaTileEntity_TesseractGenerator> newOwnershipMap = new HashMap<Integer, GT_MetaTileEntity_TesseractGenerator>();
				newOwnershipMap.put(freq, generator);
				sTesseractGeneratorOwnershipMap.put(playerIdentifier, newOwnershipMap);
				return true;
			}
			else if (sTesseractGeneratorOwnershipMap.containsKey(playerIdentifier)){
				Map<Integer, GT_MetaTileEntity_TesseractGenerator> ownershipMap = sTesseractGeneratorOwnershipMap.get(playerIdentifier);
				ownershipMap.put(freq, generator);
				sTesseractGeneratorOwnershipMap.put(playerIdentifier, ownershipMap);
				return true;
			}
		}	
		return false;
	}
	
}
