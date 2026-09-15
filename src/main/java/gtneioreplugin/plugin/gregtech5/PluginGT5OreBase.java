package gtneioreplugin.plugin.gregtech5;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

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

    protected void drawKeyValueLine(String unlocalizedKey, String value, int x, int y) {
        String key = I18n.format("gtnop.gui.nei.lineKey", I18n.format(unlocalizedKey)) + ": ";
        value = I18n.format("gtnop.gui.nei.lineValue", value);
        drawLine(key, x, y, LINE_KEY_COLOR);
        drawLine(value, x + GuiDraw.fontRenderer.getStringWidth(key), y, LINE_VALUE_COLOR);
    }

    protected void drawLine(String text, int x, int y, int color) {
        String trimmed = text;
        if (GuiDraw.fontRenderer.getStringWidth(text) > getGuiWidth() - x) {
            trimmed = GuiDraw.fontRenderer.trimStringToWidth(text, getGuiWidth() - x - 10);
        }

        GuiDraw.drawString(trimmed + (trimmed.length() < text.length() ? "..." : ""), x, y, color, false);
    }

    protected void drawHeader(String key, int x, int y) {
        key = I18n.format("gtnop.gui.nei.lineKey", I18n.format(key));
        GuiDraw.drawString(key, x, y, LINE_KEY_COLOR, false);
    }

    protected void drawDimHeader(int y) {
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.worldNames") + ": ", LEFT_PADDING, y, LINE_KEY_COLOR, false);
    }
}
