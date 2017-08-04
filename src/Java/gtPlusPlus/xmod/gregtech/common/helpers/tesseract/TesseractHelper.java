package gtPlusPlus.xmod.gregtech.common.helpers.tesseract;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import static gtPlusPlus.core.lib.CORE.sTesseractGeneratorOwnershipMap;
import static gtPlusPlus.core.lib.CORE.sTesseractTerminalOwnershipMap;

import gtPlusPlus.xmod.gregtech.common.tileentities.automation.GT_MetaTileEntity_TesseractGenerator;
import gtPlusPlus.xmod.gregtech.common.tileentities.automation.GT_MetaTileEntity_TesseractTerminal;
import net.minecraft.entity.player.EntityPlayer;

public class TesseractHelper {
	
	/**
	 * Tesseract Generator Helpers
	 * 
	 * @param player
	 * @return
	 */
	
	public final boolean isGeneratorOwnedByPlayer(EntityPlayer player, GT_MetaTileEntity_TesseractGenerator generator){
		UUID playerIdentifier = player.getUniqueID();
		if (!sTesseractGeneratorOwnershipMap.isEmpty() && playerIdentifier != null){		
			Map<Integer, GT_MetaTileEntity_TesseractGenerator> generators = getGeneratorOwnershipByPlayer(player);
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
	
	public final boolean setGeneratorOwnershipByPlayer(EntityPlayer player, int freq, GT_MetaTileEntity_TesseractGenerator generator){
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
	
	
	
	/**
	 * 
	 * Tesseract Terminal Helpers
	 * 
	 * @param player
	 * @return
	 */	
	
	
	public final boolean isTerminalOwnedByPlayer(EntityPlayer player, GT_MetaTileEntity_TesseractTerminal generator){
		UUID playerIdentifier = player.getUniqueID();
		if (!sTesseractTerminalOwnershipMap.isEmpty() && playerIdentifier != null){		
			Map<Integer, GT_MetaTileEntity_TesseractTerminal> generators = getTerminalOwnershipByPlayer(player);
			Set<Entry<Integer, GT_MetaTileEntity_TesseractTerminal>> players = generators.entrySet();
			Iterator<Entry<Integer, GT_MetaTileEntity_TesseractTerminal>> i = players.iterator();
			while(i.hasNext()) {
				Entry<Integer, GT_MetaTileEntity_TesseractTerminal> current = i.next();
				if (current.getValue().equals(generator)){
					return true;
				}				
			}		
		}		
		return false;		
	}
	
	public final boolean setTerminalOwnershipByPlayer(EntityPlayer player, int freq, GT_MetaTileEntity_TesseractTerminal generator){
		UUID playerIdentifier = player.getUniqueID();
		if (playerIdentifier != null){
			Map<Integer, GT_MetaTileEntity_TesseractTerminal> playerOwned = sTesseractTerminalOwnershipMap.get(playerIdentifier);	
			if (playerOwned == null || playerOwned.isEmpty()){
				Map<Integer, GT_MetaTileEntity_TesseractTerminal> newOwnershipMap = new HashMap<Integer, GT_MetaTileEntity_TesseractTerminal>();
				newOwnershipMap.put(freq, generator);
				sTesseractTerminalOwnershipMap.put(playerIdentifier, newOwnershipMap);
				return true;
			}
			else if (sTesseractTerminalOwnershipMap.containsKey(playerIdentifier)){
				Map<Integer, GT_MetaTileEntity_TesseractTerminal> ownershipMap = sTesseractTerminalOwnershipMap.get(playerIdentifier);
				ownershipMap.put(freq, generator);
				sTesseractTerminalOwnershipMap.put(playerIdentifier, ownershipMap);
				return true;
			}
		}	
		return false;
	}
	
	
	
	
	
	
	
	/**
	 * 
	 * Internal Methods
	 *  
	 */
	
	private final Map<Integer, GT_MetaTileEntity_TesseractGenerator> getGeneratorOwnershipByPlayer(EntityPlayer player){
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
	
	private final Map<Integer, GT_MetaTileEntity_TesseractTerminal> getTerminalOwnershipByPlayer(EntityPlayer player){
		UUID playerIdentifier = player.getUniqueID();
		if (!sTesseractTerminalOwnershipMap.isEmpty() && playerIdentifier != null){
			Set<Entry<UUID, Map<Integer, GT_MetaTileEntity_TesseractTerminal>>> players = sTesseractTerminalOwnershipMap.entrySet();
			Iterator<Entry<UUID, Map<Integer, GT_MetaTileEntity_TesseractTerminal>>> i = players.iterator();
			while(i.hasNext()) {
				Entry<UUID, Map<Integer, GT_MetaTileEntity_TesseractTerminal>> current = i.next();
				if (current.getKey().compareTo(playerIdentifier) == 0){
					return current.getValue();
				}
				
			}			
		}	
		return null;
	}
	
	
}
