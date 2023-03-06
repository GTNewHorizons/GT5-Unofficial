package com.gtnewhorizons.gtnhintergalactic.client.lore;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.StatCollector;

import com.gtnewhorizons.gtnhintergalactic.GTNHIntergalactic;
import gregtech.api.objects.XSTR;

public class LoreHandler implements IResourceManagerReloadListener {

    /**
     * key: field to be updated; value: translation key to use
     */
    private static final Map<Field, String> LORE_HOLDERS = new HashMap<>();

    @Override
    public void onResourceManagerReload(IResourceManager p_110549_1_) {
        updateLoreHolders();
    }

    /**
     * Register a class containing one or more static fields of type {@link String} annotated with {@link LoreHolder}.
     * When the resources are reloaded, the field(s) are updated with a random translation. The possible lines are
     * defined via lang files, using the translation key defined by {@link LoreHolder#value()}, appended by an index
     * (starting with 0). Blank translations are ignored.
     * 
     * @param clazz The class containing the field(s) to be updated when the resources are reloaded
     * @author glowredman
     */
    public static void registerLoreHolder(Class<?> clazz) {
        try {
            for (Field field : clazz.getDeclaredFields()) {
                if (!field.getType().isAssignableFrom(String.class) || !Modifier.isStatic(field.getModifiers()))
                    continue;

                LoreHolder loreHolder = field.getDeclaredAnnotation(LoreHolder.class);
                if (loreHolder == null) continue;

                field.setAccessible(true);
                LORE_HOLDERS.put(field, loreHolder.value());
            }
        } catch (Exception e) {
            GTNHIntergalactic.LOG.warn("Unable to find LoreHolder in " + clazz.toString(), e);
        }
    }

    private static void updateLoreHolders() {
        LORE_HOLDERS.forEach((field, keyPrefix) -> {
            try {
                field.set(null, getRandomLine(keyPrefix));
            } catch (Exception e) {
                GTNHIntergalactic.LOG.warn(
                        "Unable to update LoreHolder in " + field.getDeclaringClass().toString()
                                + " (Field: "
                                + field.getName()
                                + ")",
                        e);
            }
        });
    }

    private static String getRandomLine(String keyPrefix) {
        List<String> allLines = getAllLines(keyPrefix);
        int size = allLines.size();
        return size == 0 ? null : allLines.get(XSTR.XSTR_INSTANCE.nextInt(size));
    }

    private static List<String> getAllLines(String keyPrefix) {
        List<String> allLines = new ArrayList<>();

        for (int i = 0; true; i++) {
            String unlocalizedLine = keyPrefix + i;
            String localizedLine = StatCollector.translateToLocal(unlocalizedLine);
            if (unlocalizedLine.equals(localizedLine)) {
                break;
            } else {
                if (!localizedLine.isBlank()) {
                    allLines.add(localizedLine);
                }
            }
        }

        return allLines;
    }

}
