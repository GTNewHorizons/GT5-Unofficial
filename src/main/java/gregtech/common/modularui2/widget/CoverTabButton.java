package gregtech.common.modularui2.widget;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.ITheme;
import com.cleanroommc.modularui.api.MCHelper;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.theme.WidgetTheme;
import com.cleanroommc.modularui.widgets.ButtonWidget;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.common.covers.Cover;

public class CoverTabButton extends ButtonWidget<CoverTabButton> {

    private static final String[] COVER_DIRECTION_NAMES = new String[] { "GT5U.interface.coverTabs.down",
        "GT5U.interface.coverTabs.up", "GT5U.interface.coverTabs.north", "GT5U.interface.coverTabs.south",
        "GT5U.interface.coverTabs.west", "GT5U.interface.coverTabs.east" };

    private final ICoverable coverable;
    private final ForgeDirection side;

    public CoverTabButton(ICoverable coverable, ForgeDirection side, IPanelHandler panel) {
        this.coverable = coverable;
        this.side = side;
        this.setEnabledIf($ -> coverable.hasCoverAtSide(side))
            .onMousePressed(mouseButton -> {
                if (coverable.getCoverAtSide(side)
                    .hasCoverGUI()) {
                    panel.openPanel();
                }
                return true;
            })
            .overlay(
                new DynamicDrawable(
                    () -> new ItemDrawable(coverable.getCoverItemAtSide(side)).asIcon()
                        .marginLeft(2)
                        .marginTop(1)))
            .tooltipBuilder(this::buildTooltip)
            .tooltipAutoUpdate(true)
            .size(18, 20);
    }

    private void buildTooltip(RichTooltip builder) {
        Cover cover = coverable.getCoverAtSide(side);
        ItemStack coverItem = cover.asItemStack();
        if (coverItem == null) return;

        boolean coverHasGui = cover.hasCoverGUI();
        List<String> tooltips = MCHelper.getItemToolTip(coverItem);
        builder
            .add(
                (coverHasGui ? EnumChatFormatting.UNDERLINE : EnumChatFormatting.DARK_GRAY)
                    + StatCollector.translateToLocal(COVER_DIRECTION_NAMES[side.ordinal()])
                    + (coverHasGui ? EnumChatFormatting.RESET + ": " : ": " + EnumChatFormatting.RESET)
                    + tooltips.get(0))
            .newLine()
            .addStringLines(cover.getAdditionalTooltip())
            .addStringLines(
                IntStream.range(1, tooltips.size())
                    .mapToObj(index -> EnumChatFormatting.GRAY + tooltips.get(index))
                    .collect(Collectors.toList()));
    }

    @Override
    public WidgetTheme getWidgetThemeInternal(ITheme theme) {
        Cover cover = coverable.getCoverAtSide(side);
        String widgetThemeId = cover.hasCoverGUI() ? GTWidgetThemes.BUTTON_COVER_TAB_ENABLED
            : GTWidgetThemes.BUTTON_COVER_TAB_DISABLED;
        return theme.getWidgetTheme(widgetThemeId);
    }
}
