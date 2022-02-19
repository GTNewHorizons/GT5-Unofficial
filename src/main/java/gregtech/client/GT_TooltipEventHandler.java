package gregtech.client;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.oredict.OreDictionary;

public class GT_TooltipEventHandler {

	private static final Map<String, Supplier<String>> tooltipMap = new HashMap<>();

	private static final String BLACK, DARK_BLUE, DARK_GREEN, DARK_AQUA, DARK_RED, DARK_PURPLE, GOLD, GRAY, DARK_GRAY, BLUE, GREEN, AQUA, RED, LIGHT_PURPLE, YELLOW, WHITE, OBFUSCATED, BOLD, STRIKETHROUGH, UNDERLINE, ITALIC, RESET;

	private static final Supplier<String> NEW_LINE;

	/*
	 * What you can do:
	 * - add simple, unformated text
	 * - add formatted text
	 *   Note: you can chain formatting codes but the color must be used first:
	 *   e.g. BLACK + ITALIC will create black, italic text but ITALIC + BLACK will only create black text.
	 * - add animated text
	 * - chain multiple static and/or animated text together using chain()
	 *   (Although chaining only static text together is pointless, text() is already able to to that)
	 * - add multiple lines by using NEW_LINE (either as String or as Supplier<String>)
	 *   Note: formatting only applies for one line
	 *   Note: having a NEW_LINE in animated text results in "skipping" of one formatting since NEW_LINE
	 *         counts as character but is not displayed
	 *
	 * Note: adding a tooltip to an item multiple times also creates multiple lines (in the same order they were added)
	 *
	 * What you cannot do:
	 * - add a tooltip depending on NBT, tooltips are only mapped to name and meta
	 *
	 * This method is executed on postInit
	 */
	public static void init() {

		// Tooltip tiers for ALL items given the appropriate tier oredict tag.
		addOredictTooltip("ULV", text(WHITE + "ULV-Tier"));
		addOredictTooltip("LV" , text(GRAY + "LV-Tier"));
		addOredictTooltip("MV" , text(GOLD + "MV-Tier"));
		addOredictTooltip("HV" , text(YELLOW + "HV-Tier"));
		addOredictTooltip("EV" , text(DARK_GRAY + "EV-Tier"));
		addOredictTooltip("IV" , text(GREEN + "IV-Tier"));
		addOredictTooltip("LuV", text(LIGHT_PURPLE + "LuV-Tier"));
		addOredictTooltip("ZPM", text(AQUA + "ZPM-Tier"));
		addOredictTooltip("UV" , text(DARK_GREEN + "UV-Tier"));
		addOredictTooltip("UHV", text(DARK_RED + "UHV-Tier"));
		addOredictTooltip("UEV", text(DARK_PURPLE + "UEV-Tier"));
		addOredictTooltip("UIV", text(DARK_BLUE + BOLD + "UIV-Tier"));
		addOredictTooltip("UMV", text(RED + BOLD + UNDERLINE + "UMV-Tier"));
		addOredictTooltip("UXV", animatedText("UXV-Tier", 1, 100, DARK_PURPLE + BOLD + UNDERLINE, DARK_RED + UNDERLINE + BOLD));
		addOredictTooltip("MAX", chain(
				animatedText("X", 1, 100, LIGHT_PURPLE + BOLD + OBFUSCATED + UNDERLINE, RED + BOLD + OBFUSCATED + UNDERLINE, GOLD + OBFUSCATED + BOLD + UNDERLINE, YELLOW + OBFUSCATED + BOLD + UNDERLINE, GREEN + OBFUSCATED + BOLD + UNDERLINE, AQUA + OBFUSCATED + BOLD + UNDERLINE, BLUE + OBFUSCATED + BOLD + UNDERLINE),
				animatedText("MAX-Tier", 1, 100, RED + BOLD + UNDERLINE, GOLD + BOLD + UNDERLINE, YELLOW + BOLD + UNDERLINE, GREEN + BOLD + UNDERLINE, AQUA + BOLD + UNDERLINE, BLUE + BOLD + UNDERLINE, LIGHT_PURPLE + BOLD + UNDERLINE),
				animatedText("X", 1, 100, GOLD + OBFUSCATED + BOLD + UNDERLINE, YELLOW + OBFUSCATED + BOLD + UNDERLINE, GREEN + OBFUSCATED + BOLD + UNDERLINE, AQUA + OBFUSCATED + BOLD + UNDERLINE, BLUE + OBFUSCATED + BOLD + UNDERLINE, LIGHT_PURPLE + OBFUSCATED + BOLD + UNDERLINE, RED + OBFUSCATED + BOLD + UNDERLINE)
		));

	}

	@SubscribeEvent
	public void renderTooltip(ItemTooltipEvent event) {
		Supplier<String> tooltip = tooltipMap.get(getStackIdentifier(event.itemStack));
		if(tooltip != null) {
			String text = tooltip.get();
			if(!text.isEmpty()) {
				for(String line : text.split("\n")) {
					event.toolTip.add(line);
				}
			}
		}
	}

	@SafeVarargs
	private static Supplier<String> chain(Supplier<String>... parts) {
		return () -> {
			String s = "";
			for(Supplier<String> text : parts) {
				s += text.get();
			}
			return s;
		};
	}

	private static Supplier<String> text(String text) {
		return () -> text;
	}

	/** Taken and adapted from <a href=https://github.com/GTNewHorizons/Avaritia/blob/7b7eaed652f6be320b10f33d8f8e6a04e66ca14f/src/main/java/fox/spiteful/avaritia/LudicrousText.java#L19>Avaritia</a>, licensed under MIT */
	private static Supplier<String> animatedText(String text, int posstep, int delay, String... formattingArray) {
		if(text == null || text.isEmpty() || formattingArray == null || formattingArray.length == 0) return () -> "";

		final int finalDelay = delay = delay <= 0 ? delay = 1 : delay;

		return () -> {
			StringBuilder sb = new StringBuilder(text.length() * 3);
			int offset = (int) ((System.currentTimeMillis() / finalDelay) % formattingArray.length);
			for(int i = 0; i < text.length(); i++) {
				char c = text.charAt(i);
				int indexColorArray = (i * posstep + formattingArray.length - offset) % formattingArray.length;
				sb.append(formattingArray[indexColorArray]);
				sb.append(c);
			}
			return sb.toString();
		};
	}

	private static void addOredictTooltip(String oredictName, Supplier<String> tooltip) {
		for(ItemStack item : OreDictionary.getOres(oredictName)) {
			addItemTooltip(item, tooltip);
		}
	}

	private static void addItemTooltip(String modID, String registryName, int meta, Supplier<String> tooltip) {
		Item item = GameRegistry.findItem(modID, registryName);
		if(item == null || meta < 0 || meta >= OreDictionary.WILDCARD_VALUE || tooltip == null) return;
		String identifier = item.getUnlocalizedName() + "@" + meta;
		Supplier<String> previous = tooltipMap.get(identifier);
		if(previous == null) {
			tooltipMap.put(identifier, tooltip);
		} else {
			tooltipMap.put(identifier, chain(previous, NEW_LINE, tooltip));
		}
	}

	private static void addItemTooltip(ItemStack item, Supplier<String> tooltip) {
		if(item == null || tooltip == null) return;
		String identifier = getStackIdentifier(item);
		Supplier<String> previous = tooltipMap.get(identifier);
		if(previous == null) {
			tooltipMap.put(identifier, tooltip);
		} else {
			tooltipMap.put(identifier, chain(previous, NEW_LINE, tooltip));
		}
	}

	private static String getStackIdentifier(ItemStack stack) {
		return stack == null ? "" : stack.getItem().getUnlocalizedName() + "@" + stack.getItemDamage();
	}

	static {
		AQUA          = EnumChatFormatting.AQUA.toString();
		BLACK         = EnumChatFormatting.BLACK.toString();
		BLUE          = EnumChatFormatting.BLUE.toString();
		BOLD          = EnumChatFormatting.BOLD.toString();
		DARK_AQUA     = EnumChatFormatting.DARK_AQUA.toString();
		DARK_BLUE     = EnumChatFormatting.DARK_BLUE.toString();
		DARK_GRAY     = EnumChatFormatting.DARK_GRAY.toString();
		DARK_GREEN    = EnumChatFormatting.DARK_GREEN.toString();
		DARK_PURPLE   = EnumChatFormatting.DARK_PURPLE.toString();
		DARK_RED      = EnumChatFormatting.DARK_RED.toString();
		GOLD          = EnumChatFormatting.GOLD.toString();
		GRAY          = EnumChatFormatting.GRAY.toString();
		GREEN         = EnumChatFormatting.GREEN.toString();
		ITALIC        = EnumChatFormatting.ITALIC.toString();
		LIGHT_PURPLE  = EnumChatFormatting.LIGHT_PURPLE.toString();
		OBFUSCATED    = EnumChatFormatting.OBFUSCATED.toString();
		RED           = EnumChatFormatting.RED.toString();
		RESET         = EnumChatFormatting.RESET.toString();
		STRIKETHROUGH = EnumChatFormatting.STRIKETHROUGH.toString();
		UNDERLINE     = EnumChatFormatting.UNDERLINE.toString();
		WHITE         = EnumChatFormatting.WHITE.toString();
		YELLOW        = EnumChatFormatting.YELLOW.toString();

		NEW_LINE = new Supplier<String>() {

			@Override
			public String get() {
				return "\n";
			}

			@Override
			public String toString() {
				return "\n";
			}
		};
	}

}