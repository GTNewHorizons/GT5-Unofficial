package gtPlusPlus.xmod.gregtech.common.helpers.tesseract;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import static gtPlusPlus.core.lib.CORE.sTesseractGeneratorOwnershipMap;
import static gtPlusPlus.core.lib.CORE.sTesseractTerminalOwnershipMap;

import gtPlusPlus.core.util.Utils;
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

	//Checks if a Generator is owned by a player.
	public final static boolean isGeneratorOwnedByPlayer(EntityPlayer player,
			GT_MetaTileEntity_TesseractGenerator generator) {
		UUID playerIdentifier = player.getUniqueID();
		if (!sTesseractGeneratorOwnershipMap.isEmpty() && playerIdentifier != null) {
			Map<Integer, GT_MetaTileEntity_TesseractGenerator> generators = getGeneratorOwnershipByPlayer(player);
			Set<Entry<Integer, GT_MetaTileEntity_TesseractGenerator>> players = generators.entrySet();
			Iterator<Entry<Integer, GT_MetaTileEntity_TesseractGenerator>> i = players.iterator();
			while (i.hasNext()) {
				Entry<Integer, GT_MetaTileEntity_TesseractGenerator> current = i.next();
				if (current.getValue().equals(generator)) {
					return true;
				}
			}
		}
		return false;
	}

	//Saves A Generator to the Players UUID map along with the Freq.
	public final static boolean setGeneratorOwnershipByPlayer(EntityPlayer player, int freq,
			GT_MetaTileEntity_TesseractGenerator generator) {
		UUID playerIdentifier = player.getUniqueID();
		Utils.LOG_INFO("Setting Generator on "+freq+" for "+player.getDisplayName()+".");
		if (playerIdentifier != null) {
			Map<Integer, GT_MetaTileEntity_TesseractGenerator> playerOwned = sTesseractGeneratorOwnershipMap
					.get(playerIdentifier);
			if (playerOwned == null || playerOwned.isEmpty()) {
				Map<Integer, GT_MetaTileEntity_TesseractGenerator> newOwnershipMap = new HashMap<Integer, GT_MetaTileEntity_TesseractGenerator>();
				newOwnershipMap.put(freq, generator);
				sTesseractGeneratorOwnershipMap.put(playerIdentifier, newOwnershipMap);
				Utils.LOG_INFO("Success! [Empty Map]");
				return true;
			} else if (sTesseractGeneratorOwnershipMap.containsKey(playerIdentifier)) {
				Map<Integer, GT_MetaTileEntity_TesseractGenerator> ownershipMap = sTesseractGeneratorOwnershipMap
						.get(playerIdentifier);
				ownershipMap.put(freq, generator);
				sTesseractGeneratorOwnershipMap.put(playerIdentifier, ownershipMap);
				Utils.LOG_INFO("Success!");
				return true;
			}
		}
		Utils.LOG_INFO("Failed.");
		return false;
	}

	//Gets Generator based on Frequency.
	public final static GT_MetaTileEntity_TesseractGenerator getGeneratorByFrequency(EntityPlayer player,
			int freq) {
		UUID playerIdentifier = player.getUniqueID();
		Utils.LOG_INFO("Getting Generator on "+freq+" for "+player.getDisplayName()+".");
		if (!sTesseractGeneratorOwnershipMap.isEmpty() && playerIdentifier != null) {
			Map<Integer, GT_MetaTileEntity_TesseractGenerator> generators = getGeneratorOwnershipByPlayer(player);
			Set<Entry<Integer, GT_MetaTileEntity_TesseractGenerator>> players = generators.entrySet();
			Iterator<Entry<Integer, GT_MetaTileEntity_TesseractGenerator>> i = players.iterator();
			while (i.hasNext()) {
				Entry<Integer, GT_MetaTileEntity_TesseractGenerator> current = i.next();
				if (current.getKey().equals(freq)) {
					Utils.LOG_INFO("Success!");
					return current.getValue();
				}
			}
		}
		Utils.LOG_INFO("Failed.");
		return null;
	}

	//Remove Tesseract Generator
	public final static boolean removeGenerator(EntityPlayer player, int frequency) {
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
		UUID playerIdentifier = player.getUniqueID();
		if (playerIdentifier != null) {
			Utils.LOG_INFO("Setting Terminal on "+freq+" for "+player.getDisplayName()+".");
			Map<Integer, GT_MetaTileEntity_TesseractTerminal> playerOwned = sTesseractTerminalOwnershipMap
					.get(playerIdentifier);
			if (playerOwned == null || playerOwned.isEmpty()) {
				Map<Integer, GT_MetaTileEntity_TesseractTerminal> newOwnershipMap = new HashMap<Integer, GT_MetaTileEntity_TesseractTerminal>();
				newOwnershipMap.put(freq, generator);
				sTesseractTerminalOwnershipMap.put(playerIdentifier, newOwnershipMap);
				Utils.LOG_INFO("Success! [Empty Map]");
				return true;
			} else if (sTesseractTerminalOwnershipMap.containsKey(playerIdentifier)) {
				Map<Integer, GT_MetaTileEntity_TesseractTerminal> ownershipMap = sTesseractTerminalOwnershipMap
						.get(playerIdentifier);
				ownershipMap.put(freq, generator);
				sTesseractTerminalOwnershipMap.put(playerIdentifier, ownershipMap);
				Utils.LOG_INFO("Success!");
				return true;
			}
		}
		Utils.LOG_INFO("Failed.");
		return false;
	}

	//Gets Terminal based on Frequency.
	public final static GT_MetaTileEntity_TesseractTerminal getTerminalByFrequency(EntityPlayer player,
			int freq) {
		UUID playerIdentifier = player.getUniqueID();
		Utils.LOG_INFO("Getting Terminal on "+freq+" for "+player.getDisplayName()+".");
		if (!sTesseractTerminalOwnershipMap.isEmpty() && playerIdentifier != null) {
			Map<Integer, GT_MetaTileEntity_TesseractTerminal> generators = getTerminalOwnershipByPlayer(player);
			Set<Entry<Integer, GT_MetaTileEntity_TesseractTerminal>> players = generators.entrySet();
			Iterator<Entry<Integer, GT_MetaTileEntity_TesseractTerminal>> i = players.iterator();
			while (i.hasNext()) {
				Entry<Integer, GT_MetaTileEntity_TesseractTerminal> current = i.next();
				if (current.getKey().equals(freq)) {
					Utils.LOG_INFO("Success!");
					return current.getValue();
				}
			}
		}
		Utils.LOG_INFO("Failed.");
		return null;
	}

	//Remove Tesseract Terminal
	public final static boolean removeTerminal(EntityPlayer player, int frequency) {
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
