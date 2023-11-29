package gregtech.api.modernmaterials.tooltips;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CustomTooltips {

    public static long accessCounter;

    public static ArrayList<String> createRainbowText(@NotNull ItemStack itemStack, EntityPlayer player, List<String> tooltipList, boolean aF3_H) {
        accessCounter++;

        ArrayList<String> outputText = new ArrayList<>();

        String unrainbowText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";
        outputText.add(createRainbowTextHelper(unrainbowText, accessCounter / 10));

        return outputText;
    }

    private static String createRainbowTextHelper(String text, long shift) {
        String[] rainbowColors = {"§4", "§6", "§e", "§a", "§b", "§d", "§5"}; // Minecraft color codes for red, gold, yellow, green, aqua, light purple, dark purple
        String boldItalicFormat = "§l§o"; // Bold and italic formatting codes

        StringBuilder rainbowTextBuilder = new StringBuilder();
        int colorLength = rainbowColors.length;

        for (int i = 0; i < text.length(); i++) {
            // Apply color codes in reverse sequence, using accessCounter to shift the starting color
            // The calculation for reverse cycling is adjusted
            int colorIndex = (int)((colorLength - 1 - (i + shift) % colorLength) + colorLength) % colorLength;
            String color = rainbowColors[colorIndex];

            // Append the color code, followed by bold and italic formatting, and then the character
            rainbowTextBuilder.append(color).append(boldItalicFormat).append(text.charAt(i));
        }

        return rainbowTextBuilder.toString();
    }



}
