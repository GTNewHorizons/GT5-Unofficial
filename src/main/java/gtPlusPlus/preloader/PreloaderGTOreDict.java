package gtPlusPlus.preloader;

import static gregtech.api.enums.Mods.GregTech;

import net.minecraft.item.ItemStack;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import gregtech.common.items.MetaGeneratedItem01;
import gregtech.common.items.MetaGeneratedItem03;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class PreloaderGTOreDict {

    public static boolean shouldPreventRegistration(final String string, final ItemStack bannedItem) {

        if (bannedItem == null) {
            return false;
        } else if (!PreloaderCore.enableOldGTcircuits) {
            return false;
        }

        try {
            if (PreloaderCore.enableOldGTcircuits) {
                if ((bannedItem != null) && ItemUtils.getModId(bannedItem)
                    .toLowerCase()
                    .equals(GregTech.ID)) {
                    final int damageValue = bannedItem.getItemDamage() - 32000;
                    if (bannedItem.getItem() instanceof MetaGeneratedItem01) { // 700-720
                        if ((damageValue >= 700) && (damageValue <= 720)) {
                            return true;
                        }
                    } else if (bannedItem.getItem() instanceof MetaGeneratedItem03) {
                        if ((damageValue == 6) || (damageValue == 7)
                            || (damageValue == 11)
                            || (damageValue == 12)
                            || (damageValue == 14)
                            || (damageValue == 16)
                            || (damageValue == 20)
                            || (damageValue == 21)
                            || (damageValue == 22)) {
                            return true;
                        } else if ((damageValue >= 30) && (damageValue <= 57)) {
                            return true;
                        } else if ((damageValue >= 69) && (damageValue <= 73)) {
                            return true;
                        } else if ((damageValue >= 78) && (damageValue <= 96)) {
                            return true;
                        }
                    }
                }
            }

        } catch (final Throwable e) {
            if (GTPPCore.ConfigSwitches.showHiddenNEIItems) {
                FMLRelaunchLog.log(
                    "[GT++ ASM] OreDictTransformer",
                    Level.INFO,
                    "A mod tried to register an invalid item with the OreDictionary.");
                if (bannedItem != null) {
                    FMLRelaunchLog.log(
                        "[GT++ ASM] OreDictTransformer",
                        Level.INFO,
                        "Please report this issue to the authors of %s",
                        ItemUtils.getModId(bannedItem));
                    try {
                        if (bannedItem.getItemDamage() <= Short.MAX_VALUE - 1) {
                            FMLRelaunchLog.log(
                                "[GT++ ASM] OreDictTransformer",
                                Level.INFO,
                                "Item was not null, but still invalidly registering: %s",
                                bannedItem.getDisplayName() != null ? bannedItem.getDisplayName()
                                    : "INVALID ITEM FOUND");
                        } else {
                            FMLRelaunchLog.log(
                                "[GT++ ASM] OreDictTransformer",
                                Level.INFO,
                                "Item was not null, but still invalidly registering: %s",
                                "Found Wildcard item that is being registered too early.");
                        }
                    } catch (Exception h) {
                        h.printStackTrace();
                    }
                }
            }
            // FMLRelaunchLog.log("[GT++ ASM] OreDictTransformer", Level.INFO, "%s", e.getMessage());
        }
        return false;
    }

    // Simplification of Life.
    private static boolean isInstanceOf(final Class<?> clazz, final Object obj) {
        return clazz.isInstance(obj);
    }
}
