package gregtech.common.misc;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;

import gregtech.common.items.ItemScrapbox;

public class ScrapboxDump {

    private ScrapboxDump() {}

    static void process(ICommandSender sender, int rolls) {
        Map<String, Integer> tally = new LinkedHashMap<>();

        long startTime = System.nanoTime();

        for (int i = 0; i < rolls; i++) {
            ItemStack result = ItemScrapbox.ScrapDrop.getDrop();
            String key = result == null ? "null (no drop)" : result.getDisplayName();
            tally.compute(key, (_, count) -> count == null ? 1 : count + 1);
        }

        long elapsedTime = (System.nanoTime() - startTime) / 1_000_000;

        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(tally.entrySet());
        sorted.sort((a, b) -> b.getValue() - a.getValue());

        sender.addChatMessage(new ChatComponentText("=== Drop dump: " + rolls + " rolls in " + elapsedTime + "ms ==="));

        for (Map.Entry<String, Integer> entry : sorted) {
            double percentage = (entry.getValue() * 100.0) / rolls;
            sender.addChatMessage(
                new ChatComponentText(
                    String.format("%-30s %8d hits  %6.3f%%", entry.getKey(), entry.getValue(), percentage)));
        }
    }
}
