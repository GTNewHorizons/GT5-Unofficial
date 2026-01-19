package gregtech.common.gui.modularui.widget;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.cleanroommc.modularui.drawable.GuiDraw;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.screen.viewport.ModularGuiContext;
import com.cleanroommc.modularui.theme.WidgetThemeEntry;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.utils.ColorShade;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widget.sizer.Area;

public class SegmentedBarWidget extends Widget<SegmentedBarWidget> {

    List<SegmentInfo> segments;
    int maximum;
    int borderSize;

    public SegmentedBarWidget(int maximum, int borderSize, SegmentInfo... segments) {
        this.maximum = maximum;
        this.borderSize = borderSize;
        this.segments = Arrays.stream(segments)
            .collect(Collectors.toList());

        tooltip().setAutoUpdate(true);
        tooltipBuilder(this::createTooltip);
    }

    public void createTooltip(RichTooltip builder) {
        for (SegmentInfo segment : segments) {
            builder.addLine(segment.label + ": " + segment.valueSupplier.get());
        }
    }

    @Override
    public void draw(ModularGuiContext context, WidgetThemeEntry<?> widgetTheme) {
        Area area = getArea();

        segments.sort(
            Comparator.comparingInt((SegmentInfo s) -> s.valueSupplier.get())
                .reversed());

        int start = borderSize;

        GuiDraw.drawRect(0, 0, area.width, area.height, Color.BLACK.main);
        for (SegmentInfo segment : segments) {
            int segWidth = Math.max(borderSize, (int) (((double) segment.valueSupplier.get() / maximum) * area.width));
            GuiDraw.drawHorizontalGradientRect(
                start,
                borderSize,
                segWidth,
                area.height - borderSize - 1,
                segment.color.main,
                segment.color.darkerSafe(1));
            start += segWidth;
        }
        GuiDraw.drawRect(
            start,
            borderSize,
            area.width - start - borderSize,
            area.height - borderSize - 1,
            Color.GREY.main);
    }

    public static class SegmentInfo {

        public Supplier<Integer> valueSupplier;
        public ColorShade color;
        public String label;

        public SegmentInfo(Supplier<Integer> valueSupplier, ColorShade color, String label) {
            this.valueSupplier = valueSupplier;
            this.color = color;
            this.label = label;
        }
    }
}
