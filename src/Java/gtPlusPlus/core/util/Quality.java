package gtPlusPlus.core.util;

import gtPlusPlus.core.util.math.MathUtils;
import net.minecraft.util.EnumChatFormatting;

public enum Quality {

	// Magic Blue
	// Rare Yellow
	// Set Green
	// Unique Gold/Purple
	// Trade-off Brown

	POOR("Poor", EnumChatFormatting.GRAY), COMMON("Common", EnumChatFormatting.WHITE), UNCOMMON("Uncommon",
			EnumChatFormatting.DARK_GREEN), MAGIC("Magic", EnumChatFormatting.BLUE), RARE("Rare",
					EnumChatFormatting.YELLOW), UNIQUE("Unique", EnumChatFormatting.GOLD), ARTIFACT("Artifact",
							EnumChatFormatting.AQUA), SET("Set Piece", EnumChatFormatting.GREEN), TRADEOFF("Trade-off",
									EnumChatFormatting.DARK_RED), EPIC("Epic", EnumChatFormatting.LIGHT_PURPLE);

	public static Quality getRandomQuality() {
		final int lootChance = MathUtils.randInt(0, 100);
		if (lootChance <= 10) {
			return Quality.POOR;
		}
		else if (lootChance <= 45) {
			return Quality.COMMON;
		}
		else if (lootChance <= 65) {
			return Quality.UNCOMMON;
		}
		else if (lootChance <= 82) {
			return Quality.MAGIC;
		}
		else if (lootChance <= 92) {
			return Quality.EPIC;
		}
		else if (lootChance <= 97) {
			return Quality.RARE;
		}
		else if (lootChance <= 99) {
			return Quality.ARTIFACT;
		}
		else {
			return null;
		}
	}
	private String				LOOT;

	private EnumChatFormatting	COLOUR;

	private Quality(final String lootTier, final EnumChatFormatting tooltipColour) {
		this.LOOT = lootTier;
		this.COLOUR = tooltipColour;
	}

	public String formatted() {
		return this.COLOUR + this.LOOT;
	}

	protected EnumChatFormatting getColour() {
		return this.COLOUR;
	}

	public String getQuality() {
		return this.LOOT;
	}

}
