package gtneioreplugin.plugin.gregtech5;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.google.common.collect.ImmutableList;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.StoneType;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.common.ores.OreInfo;
import gregtech.common.ores.OreManager;
import gtneioreplugin.plugin.PluginBase;
import gtneioreplugin.plugin.item.ItemDimensionDisplay;
import gtneioreplugin.util.DimensionHelper;
import gtneioreplugin.util.DimensionHelper.Dimension;

public abstract class PluginGT5OreBase extends PluginBase {

    protected static final int LEFT_PADDING = 2;
    protected static final int TITLE_Y_POS = 1;

    // spotless:off
    public static final List<OrePrefixes> PREFIX_WHITELIST = ImmutableList.of(
        OrePrefixes.dust,
        OrePrefixes.dustPure,
        OrePrefixes.dustImpure,
        OrePrefixes.oreBlackgranite,
        OrePrefixes.oreRedgranite,
        OrePrefixes.oreMarble,
        OrePrefixes.oreBasalt,
        OrePrefixes.oreNetherrack,
        OrePrefixes.oreNether,
        OrePrefixes.oreEndstone,
        OrePrefixes.oreEnd,
        OrePrefixes.ore,
        OrePrefixes.crushedCentrifuged,
        OrePrefixes.crushedPurified,
        OrePrefixes.crushed,
        OrePrefixes.rawOre,
        OrePrefixes.gemChipped,
        OrePrefixes.gemFlawed,
        OrePrefixes.gemFlawless,
        OrePrefixes.gemExquisite,
        OrePrefixes.gem
    );
    // spotless:on

    protected void sortDimNamesByTier(String[] dims) {
        Arrays.sort(dims, (a, b) -> {
            int indexA = DimensionHelper.getIndexByAbbr(a);
            int indexB = DimensionHelper.getIndexByAbbr(b);
            return Integer.compare(indexA, indexB);
        });
    }

    protected String getGTOreLocalizedName(IOreMaterial ore, boolean small) {
        try (OreInfo<IOreMaterial> info = OreInfo.getNewInfo()) {
            info.material = ore;
            info.isSmall = small;

            return OreManager.getLocalizedName(info);
        }
    }

    protected void createDimensionDisplayItems(String[] dimAbbr, int headerY, List<PositionedStack> list) {
        int count = 0;
        int itemsPerLine = 9;
        int itemSize = 18;
        for (String dim : dimAbbr) {
            ItemStack item = ItemDimensionDisplay.getItem(dim);
            if (item != null) {
                int xPos = LEFT_PADDING + itemSize * (count % itemsPerLine);
                int yPos = headerY + 10 + itemSize * (count / itemsPerLine);
                list.add(new PositionedStack(item, xPos, yPos, false));
                count++;
            }
        }
    }

    protected Set<StoneType> getStoneTypesForDimensions(String[] dimAbbr) {
        Set<StoneType> stoneTypes = new HashSet<>();

        for (String abbr : dimAbbr) {
            Dimension dimension = DimensionHelper.getByIndex(DimensionHelper.getIndexByAbbr(abbr));
            if (dimension != null) {
                stoneTypes.addAll(dimension.stoneTypes());
            }
        }
        return stoneTypes;
    }

    protected void updateRenderPermutation(PositionedStack ps) {
        ps.setPermutationToRender((cycleticks / 20) % ps.items.length);
    }

    protected List<String> getTitleLines(String text) {
        return GuiDraw.fontRenderer.listFormattedStringToWidth(text, getGuiWidth() - 4);
    }

    protected void drawTitle(List<String> lines) {
        GuiDraw.drawRect(0, TITLE_Y_POS - 1, getGuiWidth(), lines.size() * 10 + 1, 0xff939393);
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            int x = (getGuiWidth() - GuiDraw.fontRenderer.getStringWidth(line)) / 2;
            GuiDraw.drawString(line, x, TITLE_Y_POS + i * 11, 0xfafafa, true);
        }
    }

    protected void drawLine(String lineKey, String value, int x, int y) {
        String text = I18n.format(lineKey) + ": " + value;
        drawLine(text, x, y);
    }

    protected void drawLine(String text, int x, int y) {
        String trimmed = text;
        if (GuiDraw.fontRenderer.getStringWidth(text) > getGuiWidth() - x) {
            trimmed = GuiDraw.fontRenderer.trimStringToWidth(text, getGuiWidth() - x - 10);
        }

        GuiDraw.drawString(trimmed + (trimmed.length() < text.length() ? "..." : ""), x, y, 0x404040, false);
    }

    protected void drawHeader(String key, int x, int y) {
        GuiDraw.drawString(EnumChatFormatting.UNDERLINE + I18n.format(key), x, y, 0x303030, false);
    }

    protected void drawDimHeader(int y) {
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.worldNames") + ": ", 2, y, 0x404040, false);
    }
}
