package gregtech.client;

import static net.minecraft.util.EnumChatFormatting.*;

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
	
	private static final Map<String, Supplier<String>[]> tooltipMap = new HashMap<>();
	
	public static void init() {
		addOredictTooltip("circuitPrimitive", formattedText("ULV-Tier", RED));
		addOredictTooltip("circuitBasic", formattedText("LV-Tier", DARK_BLUE));
		addOredictTooltip("circuitGood", formattedText("MV-Tier", GRAY));
		addOredictTooltip("circuitAdvanced", formattedText("HV-Tier", GOLD));
		addOredictTooltip("circuitData", formattedText("EV-Tier", DARK_PURPLE));
		addOredictTooltip("circuitElite", formattedText("IV-Tier", DARK_BLUE));
		addOredictTooltip("circuitMaster", formattedText("LuV-Tier", LIGHT_PURPLE));
		addOredictTooltip("circuitUltimate", formattedText("ZPM-Tier", WHITE));
		addOredictTooltip("circuitSuperconductor", formattedText("UV-Tier", AQUA));
		addOredictTooltip("circuitInfinite", formattedText("UHV-Tier", DARK_RED));
		addOredictTooltip("circuitBio", formattedText("UEV-Tier", GREEN));
		//addOredictTooltip("circuitOptical", formattedText("UIV-Tier", DARK_GREEN));
		//addOredictTooltip("circuitExotic", formattedText("UV-Tier", DARK_AQUA));
		//addOredictTooltip("circuitCosmic", animatedString("UXV-Tier", 8, 500, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, BLACK, WHITE, WHITE, WHITE, WHITE, WHITE, WHITE, WHITE, WHITE));
		//addOredictTooltip("circuitTranscendental", animatedString("MAX-Tier", 1, 80, RED, GOLD, YELLOW, GREEN, AQUA, BLUE, LIGHT_PURPLE));
	}
	
	@SubscribeEvent
	public void renderTooltip(ItemTooltipEvent event) {
		Supplier<String>[] texts = tooltipMap.get(getStackIdentifier(event.itemStack));
		if(texts == null) return;
		String s = "";
		for(Supplier<String> snippet : texts) {
			s += snippet.get();
		}
		event.toolTip.add(s);
	}
	
	private static Supplier<String> text(String text) {
		return () -> text;
	}
	
	private static Supplier<String> formattedText(String text, EnumChatFormatting formatting){
		return () -> formatting + text;
	}
	
	/** Taken and adapted from <a href=https://github.com/GTNewHorizons/Avaritia/blob/7b7eaed652f6be320b10f33d8f8e6a04e66ca14f/src/main/java/fox/spiteful/avaritia/LudicrousText.java#L19>Avaritia</a>, licensed under MIT */
	private static Supplier<String> animatedString(String text, int posstep, int delay, EnumChatFormatting... formattingArray) {
		if(text.isEmpty()) return () -> "";
		
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
	
	@SafeVarargs
	private static void addOredictTooltip(String oredictName, Supplier<String>... tooltips) {
		for(ItemStack item : OreDictionary.getOres(oredictName)) {
			addItemTooltip(item, tooltips);
		}
	}
	
	@SafeVarargs
	private static void addItemTooltip(String modID, String registryName, int meta, Supplier<String>... tooltips) {
		Item item = GameRegistry.findItem(modID, registryName);
		if(item == null || meta < 0 || meta >= OreDictionary.WILDCARD_VALUE || tooltips == null) return;
		tooltipMap.put(item.getUnlocalizedName() + "@" + meta, tooltips);
	}
	
	@SafeVarargs
	private static void addItemTooltip(ItemStack item, Supplier<String>... tooltips) {
		if(item == null || tooltips == null) return;
		tooltipMap.put(getStackIdentifier(item), tooltips);
	}
	
	private static String getStackIdentifier(ItemStack stack) {
		return stack == null ? "" : stack.getItem().getUnlocalizedName() + "@" + stack.getItemDamage();
	}

}
