package gregtech.api.util;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

public class GT_UtilityClient {

	@SuppressWarnings("unchecked")
	public static List<String> getTooltip(ItemStack aStack, boolean aGuiStyle) {
		try {
			List<String> tooltip = aStack.getTooltip(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().gameSettings.advancedItemTooltips);
			if (aGuiStyle) {
				tooltip.set(0, (aStack.getRarity() == null ? EnumRarity.common : aStack.getRarity()).rarityColor +tooltip.get(0));
				for (int i = 1; i < tooltip.size(); i++) {
					tooltip.set(i, EnumChatFormatting.GRAY + tooltip.get(i));
				}
			}
			return tooltip;
		} catch (RuntimeException e) {
			// Collections.singletonList() can not be added to. we don't want that
			if (aGuiStyle)
				return Lists.newArrayList((aStack.getRarity() == null ? EnumRarity.common : aStack.getRarity()).rarityColor + aStack.getDisplayName());
			return Lists.newArrayList(aStack.getDisplayName());
		}
	}

}
