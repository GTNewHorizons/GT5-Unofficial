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

        String unrainbowText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit";
        String rainbowText = createRainbowTextHelper(unrainbowText, accessCounter / 10);

        outputText.add(rainbowText);

        return outputText;
    }

    private static String createRainbowTextHelper(String text, long shift) {
        String[] rainbowColors = {"§4", "§6", "§e", "§a", "§b", "§d", "§5"}; // Minecraft color codes for red, gold, yellow, green, aqua, light purple, dark purple

        StringBuilder rainbowTextBuilder = new StringBuilder();
        int colorLength = rainbowColors.length;

        for (int i = 0; i < text.length(); i++) {
            // Apply color codes in sequence, using accessCounter to shift the starting color
            String color = rainbowColors[(int)((i + shift) % colorLength)];
            rainbowTextBuilder.append(color).append(text.charAt(i));
        }

        return rainbowTextBuilder.toString();
    }


}
