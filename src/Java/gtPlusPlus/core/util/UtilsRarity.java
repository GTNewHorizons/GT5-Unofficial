package gtPlusPlus.core.util;

import net.minecraft.util.EnumChatFormatting;

public enum UtilsRarity
{
	T1_poor(EnumChatFormatting.GRAY, "Poor"),
	T2_normal(EnumChatFormatting.WHITE, "Common"),
	T3_uncommon(EnumChatFormatting.GREEN, "Uncommon"),
	T4_magic(EnumChatFormatting.BLUE, "Magic"),
	T5_rare(EnumChatFormatting.LIGHT_PURPLE, "Rare"),
	T6_epic(EnumChatFormatting.YELLOW, "Epic"),
	T8_unique(EnumChatFormatting.GOLD, "Unique");

	public final EnumChatFormatting rarityColor;
	public final String rarityName;

	private UtilsRarity(final EnumChatFormatting rarity, final String name)
	{
		this.rarityColor = rarity;
		this.rarityName = name;
	}
}