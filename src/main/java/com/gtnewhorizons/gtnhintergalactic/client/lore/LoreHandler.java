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
import net.minecraft.util.WeightedRandom;

import org.apache.commons.lang3.StringUtils;

import com.gtnewhorizons.gtnhintergalactic.GTNHIntergalactic;
import gregtech.api.objects.XSTR;

/**
 * Helper class for providing random, localized Strings to fields annotated with {@link LoreHolder}.
 * 
 * @since 1.0.0
 * @author glowredman
 */
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
     * (starting with 0). Blank translations are ignored. The translations may be weighted by using {@code <weight>:} as
     * prefix, {@code <weight>} being a non-negative integer. If no weight is specified, a default value of 1 is used.
     * To prevent ':' being used as delimiter, escape it using '\'.
     * 
     * @param clazz The class containing the field(s) to be updated when the resources are reloaded
     * @since 1.0.0
     * @since 1.0.9 Lines can be weighted
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
            GTNHIntergalactic.LOG
                    .error("An exception occured while looking for @LoreHolder annotations in " + clazz.toString(), e);
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
        List<WeightedRandom.Item> lines = getAllLines(keyPrefix);

        if (lines.size() == 0) {
            return null;
        }

        try {
            return ((WeightedText) WeightedRandom.getRandomItem(XSTR.XSTR_INSTANCE, lines)).text;
        } catch (IllegalArgumentException e) {
            GTNHIntergalactic.LOG
                    .warn("The total weight of all lines for \"" + keyPrefix + "\" exceeds " + Integer.MAX_VALUE, e);
        } catch (Exception e) {
            GTNHIntergalactic.LOG
                    .error("An unexpected Exception occured while choosing a random lore for \"" + keyPrefix + '"', e);
        }

        return null;
    }

    private static List<WeightedRandom.Item> getAllLines(String keyPrefix) {
        List<WeightedRandom.Item> allLines = new ArrayList<>();

        for (int i = 0; true; i++) {
            String unlocalizedLine = keyPrefix + i;
            String localizedLine = StatCollector.translateToLocal(unlocalizedLine);
            if (unlocalizedLine.equals(localizedLine)) {
                break;
            } else {
                if (!StringUtils.isBlank(localizedLine)) {
                    allLines.add(new WeightedText(localizedLine));
                }
            }
        }

        return allLines;
    }

    private static class WeightedText extends WeightedRandom.Item {

        private String text;

        private WeightedText(String weightedText) {
            super(0);
            this.extractWeightAndText(weightedText);
        }

        private void extractWeightAndText(String weightedText) {
            int endOfWeight = weightedText.indexOf(':');

            // no ':' was found or the ':' was escaped using '\'
            // -> lore line has no weight specified
            if (endOfWeight < 1) {
                this.itemWeight = 1;
                this.text = weightedText;
                return;
            }

            if (weightedText.charAt(endOfWeight - 1) == '\\') {
                this.itemWeight = 1;
                this.text = weightedText.substring(0, endOfWeight - 1) + weightedText.substring(endOfWeight);
                return;
            }

            // if a ':' was found, attempt to parse everything before it as int
            String weightString = weightedText.substring(0, endOfWeight);
            try {
                int weight = Integer.parseInt(weightString);

                if (weight < 0) {
                    GTNHIntergalactic.LOG.warn(
                            "\"{}\" has a negative weight ({}). This is not allowed, a weight of 1 will be used instead.",
                            weightedText,
                            weight);
                    this.itemWeight = 1;
                } else {
                    this.itemWeight = weight;
                }

                this.text = weightedText.substring(endOfWeight + 1);
                return;
            } catch (NumberFormatException e) {
                GTNHIntergalactic.LOG.warn(
                        "Could not parse \"" + weightString
                                + "\" as Integer. If it is not supposed to be a weight, escape the ':' delimiter using '\\'.",
                        e);
            } catch (Exception e) {
                GTNHIntergalactic.LOG.error(
                        "An unexpected Exception occured while extracting weight and text from lore \"" + weightedText
                                + '"',
                        e);
            }

            // fallback
            this.itemWeight = 1;
            this.text = weightedText;
        }

    }

}
