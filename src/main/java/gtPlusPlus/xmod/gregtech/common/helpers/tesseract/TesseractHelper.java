package gtPlusPlus.xmod.gregtech.common.helpers.tesseract;

import static gtPlusPlus.core.lib.GTPPCore.sTesseractGeneratorOwnershipMap;
import static gtPlusPlus.core.lib.GTPPCore.sTesseractTerminalOwnershipMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.common.tileentities.automation.MTETesseractGenerator;
import gtPlusPlus.xmod.gregtech.common.tileentities.automation.MTETesseractTerminal;

public class TesseractHelper {

    /**
     * Tesseract Generator Helpers
     *
     * @param player
     * @return
     */

    // Checks if a Generator is owned by a player.
    public static final boolean isGeneratorOwnedByPlayer(EntityPlayer player, MTETesseractGenerator generator) {
        if (player == null) {
            Logger.WARNING("Failed. [isGeneratorOwnedByPlayer]");
            return false;
        }
        // Utils.LOG_WARNING("Success. [isGeneratorOwnedByPlayer] 1");
        UUID playerIdentifier = player.getUniqueID();
        if (!sTesseractGeneratorOwnershipMap.isEmpty() && playerIdentifier != null) {
            // Utils.LOG_WARNING("Success. [isGeneratorOwnedByPlayer] 2");
            Map<Integer, MTETesseractGenerator> generators = getGeneratorOwnershipByPlayer(player);
            Set<Entry<Integer, MTETesseractGenerator>> players = generators.entrySet();
            for (Entry<Integer, MTETesseractGenerator> current : players) {
                // Utils.LOG_WARNING("Success. [isGeneratorOwnedByPlayer] 3");
                if (current.getValue()
                    .equals(generator)) {
                    // Utils.LOG_WARNING("Success. [isGeneratorOwnedByPlayer] 4");
                    return true;
                }
            }
        }
        Logger.WARNING("Failed. [isGeneratorOwnedByPlayer]");
        return false;
    }

    // Saves A Generator to the Players UUID map along with the Freq.
    public static final boolean setGeneratorOwnershipByPlayer(EntityPlayer player, int freq,
        MTETesseractGenerator generator) {
        if (player == null) {
            return false;
        }
        UUID playerIdentifier = player.getUniqueID();
        Logger.WARNING("Setting Generator on " + freq + " for " + player.getDisplayName() + ".");
        if (playerIdentifier != null) {
            Map<Integer, MTETesseractGenerator> playerOwned = sTesseractGeneratorOwnershipMap.get(playerIdentifier);
            if (playerOwned == null || playerOwned.isEmpty()) {
                Map<Integer, MTETesseractGenerator> newOwnershipMap = new HashMap<>();
                newOwnershipMap.put(freq, generator);
                sTesseractGeneratorOwnershipMap.put(playerIdentifier, newOwnershipMap);
                Logger.WARNING("Success! [Empty Map]");
                return true;
            } else if (sTesseractGeneratorOwnershipMap.containsKey(playerIdentifier)) {
                Map<Integer, MTETesseractGenerator> ownershipMap = sTesseractGeneratorOwnershipMap
                    .get(playerIdentifier);
                if (!ownershipMap.containsKey(freq)) {
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

    // Gets Generator based on Frequency.
    public static final MTETesseractGenerator getGeneratorByFrequency(EntityPlayer player, int freq) {
        if (player == null) {
            return null;
        }
        UUID playerIdentifier = player.getUniqueID();
        Logger.WARNING("Getting Generator on " + freq + " for " + player.getDisplayName() + ".");
        if (!sTesseractGeneratorOwnershipMap.isEmpty() && playerIdentifier != null) {
            // Utils.LOG_WARNING("Success. [getGeneratorByFrequency] 1");
            Map<Integer, MTETesseractGenerator> generators = getGeneratorOwnershipByPlayer(player);
            Set<Entry<Integer, MTETesseractGenerator>> players = generators.entrySet();
            for (Entry<Integer, MTETesseractGenerator> current : players) {
                // Utils.LOG_WARNING("Success. [getGeneratorByFrequency] 2");
                if (current.getKey()
                    .equals(freq)) {
                    // Utils.LOG_WARNING("Success. [getGeneratorByFrequency] 3");
                    Logger.WARNING("Success!");
                    return current.getValue();
                }
            }
        }
        Logger.WARNING("Failed. [getGeneratorByFrequency]");
        return null;
    }

    // Remove Tesseract Generator
    public static final boolean removeGenerator(EntityPlayer player, int frequency) {
        if (player == null) {
            return false;
        }
        UUID playerIdentifier = player.getUniqueID();
        if (!sTesseractGeneratorOwnershipMap.isEmpty() && playerIdentifier != null) {
            sTesseractGeneratorOwnershipMap.get(playerIdentifier)
                .remove(frequency);
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

    // Checks if a Terminal is owned by a player.
    public static final boolean isTerminalOwnedByPlayer(EntityPlayer player, MTETesseractTerminal generator) {
        if (player == null) {
            return false;
        }
        UUID playerIdentifier = player.getUniqueID();
        if (!sTesseractTerminalOwnershipMap.isEmpty() && playerIdentifier != null) {
            Map<Integer, MTETesseractTerminal> generators = getTerminalOwnershipByPlayer(player);
            Set<Entry<Integer, MTETesseractTerminal>> players = generators.entrySet();
            for (Entry<Integer, MTETesseractTerminal> current : players) {
                if (current.getValue()
                    .equals(generator)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Saves A Terminal to the Players UUID map along with the Freq.
    public static final boolean setTerminalOwnershipByPlayer(EntityPlayer player, int freq,
        MTETesseractTerminal generator) {
        if (player == null) {
            return false;
        }
        UUID playerIdentifier = player.getUniqueID();
        if (playerIdentifier != null) {
            Logger.WARNING("Setting Terminal on " + freq + " for " + player.getDisplayName() + ".");
            Map<Integer, MTETesseractTerminal> playerOwned = sTesseractTerminalOwnershipMap.get(playerIdentifier);
            if (playerOwned == null || playerOwned.isEmpty()) {
                Map<Integer, MTETesseractTerminal> newOwnershipMap = new HashMap<>();
                newOwnershipMap.put(freq, generator);
                sTesseractTerminalOwnershipMap.put(playerIdentifier, newOwnershipMap);
                Logger.WARNING("Success! [Empty Map]");
                return true;
            } else if (sTesseractTerminalOwnershipMap.containsKey(playerIdentifier)) {
                Map<Integer, MTETesseractTerminal> ownershipMap = sTesseractTerminalOwnershipMap.get(playerIdentifier);
                if (!ownershipMap.containsKey(freq)) {
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

    // Gets Terminal based on Frequency.
    public static final MTETesseractTerminal getTerminalByFrequency(EntityPlayer player, int freq) {
        if (player == null) {
            return null;
        }
        UUID playerIdentifier = player.getUniqueID();
        Logger.WARNING("Getting Terminal on " + freq + " for " + player.getDisplayName() + ".");
        if (!sTesseractTerminalOwnershipMap.isEmpty() && playerIdentifier != null) {
            Map<Integer, MTETesseractTerminal> generators = getTerminalOwnershipByPlayer(player);
            Set<Entry<Integer, MTETesseractTerminal>> players = generators.entrySet();
            for (Entry<Integer, MTETesseractTerminal> current : players) {
                if (current.getKey()
                    .equals(freq)) {
                    Logger.WARNING("Success!");
                    return current.getValue();
                }
            }
        }
        Logger.WARNING("Failed. [getTerminalByFrequency]");
        return null;
    }

    // Remove Tesseract Terminal
    public static final boolean removeTerminal(EntityPlayer player, int frequency) {
        if (player == null) {
            return false;
        }
        UUID playerIdentifier = player.getUniqueID();
        if (!sTesseractTerminalOwnershipMap.isEmpty() && playerIdentifier != null) {
            sTesseractTerminalOwnershipMap.get(playerIdentifier)
                .remove(frequency);
            return true;
        }
        return false;
    }

    /**
     *
     * Internal Methods
     *
     */
    private static Map<Integer, MTETesseractGenerator> getGeneratorOwnershipByPlayer(EntityPlayer player) {
        UUID playerIdentifier = player.getUniqueID();
        if (!sTesseractGeneratorOwnershipMap.isEmpty() && playerIdentifier != null) {
            Set<Entry<UUID, Map<Integer, MTETesseractGenerator>>> players = sTesseractGeneratorOwnershipMap.entrySet();
            for (Entry<UUID, Map<Integer, MTETesseractGenerator>> current : players) {
                if (current.getKey()
                    .compareTo(playerIdentifier) == 0) {
                    return current.getValue();
                }
            }
        }
        return null;
    }

    private static Map<Integer, MTETesseractTerminal> getTerminalOwnershipByPlayer(EntityPlayer player) {
        UUID playerIdentifier = player.getUniqueID();
        if (!sTesseractTerminalOwnershipMap.isEmpty() && playerIdentifier != null) {
            Set<Entry<UUID, Map<Integer, MTETesseractTerminal>>> players = sTesseractTerminalOwnershipMap.entrySet();
            for (Entry<UUID, Map<Integer, MTETesseractTerminal>> current : players) {
                if (current.getKey()
                    .compareTo(playerIdentifier) == 0) {
                    return current.getValue();
                }
            }
        }
        return null;
    }
}
