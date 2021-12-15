package gtPlusPlus.xmod.gregtech.common.helpers.tesseract;

import static gtPlusPlus.core.lib.CORE.*;

import java.util.*;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.common.tileentities.automation.GT_MetaTileEntity_TesseractGenerator;
import gtPlusPlus.xmod.gregtech.common.tileentities.automation.GT_MetaTileEntity_TesseractTerminal;

public class TesseractHelper {

	/**
	 * Tesseract Generator Helpers
	 * 
	 * @param player
	 * @return
	 */

	//Checks if a Generator is owned by a player.
	public final static boolean isGeneratorOwnedByPlayer(EntityPlayer player,
			GT_MetaTileEntity_TesseractGenerator generator) {
		if (player == null){
			Logger.WARNING("Failed. [isGeneratorOwnedByPlayer]");
			return false;
		}
		//Utils.LOG_WARNING("Success. [isGeneratorOwnedByPlayer] 1");
		UUID playerIdentifier = player.getUniqueID();
		if (!sTesseractGeneratorOwnershipMap.isEmpty() && playerIdentifier != null) {
			//Utils.LOG_WARNING("Success. [isGeneratorOwnedByPlayer] 2");
			Map<Integer, GT_MetaTileEntity_TesseractGenerator> generators = getGeneratorOwnershipByPlayer(player);
			Set<Entry<Integer, GT_MetaTileEntity_TesseractGenerator>> players = generators.entrySet();
			Iterator<Entry<Integer, GT_MetaTileEntity_TesseractGenerator>> i = players.iterator();
			while (i.hasNext()) {
				//Utils.LOG_WARNING("Success. [isGeneratorOwnedByPlayer] 3");
				Entry<Integer, GT_MetaTileEntity_TesseractGenerator> current = i.next();
				if (current.getValue().equals(generator)) {
					//Utils.LOG_WARNING("Success. [isGeneratorOwnedByPlayer] 4");
					return true;
				}
			}
		}
		Logger.WARNING("Failed. [isGeneratorOwnedByPlayer]");
		return false;
	}

	//Saves A Generator to the Players UUID map along with the Freq.
	public final static boolean setGeneratorOwnershipByPlayer(EntityPlayer player, int freq,
			GT_MetaTileEntity_TesseractGenerator generator) {
		if (player == null){
			return false;
		}
		UUID playerIdentifier = player.getUniqueID();
		Logger.WARNING("Setting Generator on "+freq+" for "+player.getDisplayName()+".");
		if (playerIdentifier != null) {
			Map<Integer, GT_MetaTileEntity_TesseractGenerator> playerOwned = sTesseractGeneratorOwnershipMap
					.get(playerIdentifier);
			if (playerOwned == null || playerOwned.isEmpty()) {
				Map<Integer, GT_MetaTileEntity_TesseractGenerator> newOwnershipMap = new HashMap<Integer, GT_MetaTileEntity_TesseractGenerator>();
				newOwnershipMap.put(freq, generator);
				sTesseractGeneratorOwnershipMap.put(playerIdentifier, newOwnershipMap);
				Logger.WARNING("Success! [Empty Map]");
				return true;
			} else if (sTesseractGeneratorOwnershipMap.containsKey(playerIdentifier)) {
				Map<Integer, GT_MetaTileEntity_TesseractGenerator> ownershipMap = sTesseractGeneratorOwnershipMap
						.get(playerIdentifier);
				if (!ownershipMap.containsKey(freq)){
					ownershipMap.put(freq, generator);
				}
				ownershipMap.put(freq, generator);
				sTesseractGeneratorOwnershipMap.put(playerIdentifier, ownershipMap);
				Logger.WARNING("Success!");
				return true;
			}
		}
		Logger.WARNING("Failed. [setGeneratorOwnershipByPlayer]");
		return false;
	}

	//Gets Generator based on Frequency.
	public final static GT_MetaTileEntity_TesseractGenerator getGeneratorByFrequency(EntityPlayer player,
			int freq) {
		if (player == null){
			return null;
		}
		UUID playerIdentifier = player.getUniqueID();
		Logger.WARNING("Getting Generator on "+freq+" for "+player.getDisplayName()+".");
		if (!sTesseractGeneratorOwnershipMap.isEmpty() && playerIdentifier != null) {
			//Utils.LOG_WARNING("Success. [getGeneratorByFrequency] 1");
			Map<Integer, GT_MetaTileEntity_TesseractGenerator> generators = getGeneratorOwnershipByPlayer(player);
			Set<Entry<Integer, GT_MetaTileEntity_TesseractGenerator>> players = generators.entrySet();
			Iterator<Entry<Integer, GT_MetaTileEntity_TesseractGenerator>> i = players.iterator();
			while (i.hasNext()) {
				//Utils.LOG_WARNING("Success. [getGeneratorByFrequency] 2");
				Entry<Integer, GT_MetaTileEntity_TesseractGenerator> current = i.next();
				if (current.getKey().equals(freq)) {
					//Utils.LOG_WARNING("Success. [getGeneratorByFrequency] 3");
					Logger.WARNING("Success!");
					return current.getValue();
				}
			}
		}
		Logger.WARNING("Failed. [getGeneratorByFrequency]");
		return null;
	}

	//Remove Tesseract Generator
	public final static boolean removeGenerator(EntityPlayer player, int frequency) {
		if (player == null){
			return false;
		}
		UUID playerIdentifier = player.getUniqueID();
		if (!sTesseractGeneratorOwnershipMap.isEmpty() && playerIdentifier != null) {
			sTesseractGeneratorOwnershipMap.get(playerIdentifier).remove(frequency);
			return true;
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

	//Checks if a Terminal is owned by a player.
	public final static boolean isTerminalOwnedByPlayer(EntityPlayer player,
			GT_MetaTileEntity_TesseractTerminal generator) {
		if (player == null){
			return false;
		}
		UUID playerIdentifier = player.getUniqueID();
		if (!sTesseractTerminalOwnershipMap.isEmpty() && playerIdentifier != null) {
			Map<Integer, GT_MetaTileEntity_TesseractTerminal> generators = getTerminalOwnershipByPlayer(player);
			Set<Entry<Integer, GT_MetaTileEntity_TesseractTerminal>> players = generators.entrySet();
			Iterator<Entry<Integer, GT_MetaTileEntity_TesseractTerminal>> i = players.iterator();
			while (i.hasNext()) {
				Entry<Integer, GT_MetaTileEntity_TesseractTerminal> current = i.next();
				if (current.getValue().equals(generator)) {
					return true;
				}
			}
		}
		return false;
	}

	//Saves A Terminal to the Players UUID map along with the Freq.
	public final static boolean setTerminalOwnershipByPlayer(EntityPlayer player, int freq,
			GT_MetaTileEntity_TesseractTerminal generator) {
		if (player == null){
			return false;
		}
		UUID playerIdentifier = player.getUniqueID();
		if (playerIdentifier != null) {
			Logger.WARNING("Setting Terminal on "+freq+" for "+player.getDisplayName()+".");
			Map<Integer, GT_MetaTileEntity_TesseractTerminal> playerOwned = sTesseractTerminalOwnershipMap
					.get(playerIdentifier);
			if (playerOwned == null || playerOwned.isEmpty()) {
				Map<Integer, GT_MetaTileEntity_TesseractTerminal> newOwnershipMap = new HashMap<Integer, GT_MetaTileEntity_TesseractTerminal>();
				newOwnershipMap.put(freq, generator);
				sTesseractTerminalOwnershipMap.put(playerIdentifier, newOwnershipMap);
				Logger.WARNING("Success! [Empty Map]");
				return true;
			} else if (sTesseractTerminalOwnershipMap.containsKey(playerIdentifier)) {
				Map<Integer, GT_MetaTileEntity_TesseractTerminal> ownershipMap = sTesseractTerminalOwnershipMap
						.get(playerIdentifier);
				if (!ownershipMap.containsKey(freq)){
					ownershipMap.put(freq, generator);
				}
				sTesseractTerminalOwnershipMap.put(playerIdentifier, ownershipMap);
				Logger.WARNING("Success!");
				return true;
			}
		}
		Logger.WARNING("Failed. [setTerminalOwnershipByPlayer]");
		return false;
	}

	//Gets Terminal based on Frequency.
	public final static GT_MetaTileEntity_TesseractTerminal getTerminalByFrequency(EntityPlayer player,
			int freq) {
		if (player == null){
			return null;
		}
		UUID playerIdentifier = player.getUniqueID();
		Logger.WARNING("Getting Terminal on "+freq+" for "+player.getDisplayName()+".");
		if (!sTesseractTerminalOwnershipMap.isEmpty() && playerIdentifier != null) {
			Map<Integer, GT_MetaTileEntity_TesseractTerminal> generators = getTerminalOwnershipByPlayer(player);
			Set<Entry<Integer, GT_MetaTileEntity_TesseractTerminal>> players = generators.entrySet();
			Iterator<Entry<Integer, GT_MetaTileEntity_TesseractTerminal>> i = players.iterator();
			while (i.hasNext()) {
				Entry<Integer, GT_MetaTileEntity_TesseractTerminal> current = i.next();
				if (current.getKey().equals(freq)) {
					Logger.WARNING("Success!");
					return current.getValue();
				}
			}
		}
		Logger.WARNING("Failed. [getTerminalByFrequency]");
		return null;
	}

	//Remove Tesseract Terminal
	public final static boolean removeTerminal(EntityPlayer player, int frequency) {
		if (player == null){
			return false;
		}
		UUID playerIdentifier = player.getUniqueID();
		if (!sTesseractTerminalOwnershipMap.isEmpty() && playerIdentifier != null) {
			sTesseractTerminalOwnershipMap.get(playerIdentifier).remove(frequency);
			return true;
		}
		return false;
	}

	/**
	 * 
	 * Internal Methods
	 * 
	 */

	private final static Map<Integer, GT_MetaTileEntity_TesseractGenerator> getGeneratorOwnershipByPlayer(
			EntityPlayer player) {
		UUID playerIdentifier = player.getUniqueID();
		if (!sTesseractGeneratorOwnershipMap.isEmpty() && playerIdentifier != null) {
			Set<Entry<UUID, Map<Integer, GT_MetaTileEntity_TesseractGenerator>>> players = sTesseractGeneratorOwnershipMap
					.entrySet();
			Iterator<Entry<UUID, Map<Integer, GT_MetaTileEntity_TesseractGenerator>>> i = players.iterator();
			while (i.hasNext()) {
				Entry<UUID, Map<Integer, GT_MetaTileEntity_TesseractGenerator>> current = i.next();
				if (current.getKey().compareTo(playerIdentifier) == 0) {
					return current.getValue();
				}

			}
		}
		return null;
	}

	private final static Map<Integer, GT_MetaTileEntity_TesseractTerminal> getTerminalOwnershipByPlayer(
			EntityPlayer player) {
		UUID playerIdentifier = player.getUniqueID();
		if (!sTesseractTerminalOwnershipMap.isEmpty() && playerIdentifier != null) {
			Set<Entry<UUID, Map<Integer, GT_MetaTileEntity_TesseractTerminal>>> players = sTesseractTerminalOwnershipMap
					.entrySet();
			Iterator<Entry<UUID, Map<Integer, GT_MetaTileEntity_TesseractTerminal>>> i = players.iterator();
			while (i.hasNext()) {
				Entry<UUID, Map<Integer, GT_MetaTileEntity_TesseractTerminal>> current = i.next();
				if (current.getKey().compareTo(playerIdentifier) == 0) {
					return current.getValue();
				}

			}
		}
		return null;
	}

}
