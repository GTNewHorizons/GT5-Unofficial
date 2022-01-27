package gregtech.client;

import static net.minecraft.util.EnumChatFormatting.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.oredict.OreDictionary;

public class GT_TooltipEventHandler {
	
	private static final Map<String, Supplier<String>[]> tooltipMap = new HashMap<>();


	
	public static void init() {

		// Circuits
		addOredictTooltip("circuitPrimitive", formattedText("ULV-Tier", WHITE));
		addOredictTooltip("circuitBasic" , formattedText("LV-Tier", GRAY));
		addOredictTooltip("circuitGood" , formattedText("MV-Tier", GOLD));
		addOredictTooltip("circuitAdvanced" , formattedText("HV-Tier", YELLOW));
		addOredictTooltip("circuitData" , formattedText("EV-Tier", DARK_GRAY));
		addOredictTooltip("circuitElite" , formattedText("IV-Tier", GREEN));
		addOredictTooltip("circuitMaster", formattedText("LuV-Tier", LIGHT_PURPLE));
		addOredictTooltip("circuitUltimate", formattedText("ZPM-Tier", AQUA));
		addOredictTooltip("circuitSuperconductor" , formattedText("UV-Tier", DARK_GREEN));
		addOredictTooltip("circuitInfinite", formattedText("UHV-Tier", DARK_RED));
		addOredictTooltip("circuitBio", formattedText("UEV-Tier", DARK_PURPLE));
		addOredictTooltip("circuitUIV", formattedText("UIV-Tier", DARK_BLUE));
		addOredictTooltip("circuitUMV", formattedText("UMV-Tier", RED));
		addOredictTooltip("circuitUXV", animatedString("UXV-Tier", 1, 80, DARK_PURPLE, DARK_RED));
		addOredictTooltip("circuitMAX", animatedString("MAX-Tier", 1, 80, RED, GOLD, YELLOW, GREEN, AQUA, BLUE, LIGHT_PURPLE));

		// Batteries
		addOredictTooltip("batteryULV", formattedText("ULV-Tier", WHITE));
		addOredictTooltip("calclavia:ADVANCED_BATTERY" , formattedText("LV-Tier", GRAY));
		addOredictTooltip("batteryMV" , formattedText("MV-Tier", GOLD));
		addOredictTooltip("batteryHV" , formattedText("HV-Tier", YELLOW));
		addOredictTooltip("batteryElite" , formattedText("HV-Tier", YELLOW)); // Because of energy crystal
		addOredictTooltip("batteryEV" , formattedText("EV-Tier", DARK_GRAY));
		addOredictTooltip("batteryMaster" , formattedText("EV-Tier", DARK_GRAY)); // Because of lapotron crystal
		addOredictTooltip("batteryIV" , formattedText("IV-Tier", GREEN));
		addOredictTooltip("batteryLuV", formattedText("LuV-Tier", LIGHT_PURPLE));
		addOredictTooltip("batteryZPM", formattedText("ZPM-Tier", AQUA));
		addOredictTooltip("batteryUV" , formattedText("UV-Tier", DARK_GREEN));
		addOredictTooltip("batteryUHV", formattedText("UHV-Tier", DARK_RED));
		addOredictTooltip("batteryUEV", formattedText("UEV-Tier", DARK_PURPLE));
		addOredictTooltip("batteryUIV", formattedText("UIV-Tier", DARK_BLUE));
		addOredictTooltip("batteryUMV", formattedText("UMV-Tier", RED));
		addOredictTooltip("batteryUXV", animatedString("UXV-Tier", 1, 80, DARK_PURPLE, DARK_RED));
		addOredictTooltip("batteryMAX", animatedString("MAX-Tier", 1, 80, RED, GOLD, YELLOW, GREEN, AQUA, BLUE, LIGHT_PURPLE));


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
	private static void addItemTooltip(ItemStack item, Supplier<String>... tooltips) {
		if(item == null || tooltips == null) return;
		tooltipMap.put(getStackIdentifier(item), tooltips);
	}
	
	private static String getStackIdentifier(ItemStack stack) {
		return stack == null ? "" : stack.getItem().getUnlocalizedName() + "@" + stack.getItemDamage();
	}

}
