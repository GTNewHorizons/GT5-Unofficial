package gregtech.common.misc.techtree.gui;

import com.cleanroommc.modularui.api.ITheme;
import com.cleanroommc.modularui.theme.WidgetTheme;
import com.cleanroommc.modularui.widgets.ButtonWidget;

public class TechSelectorButton<W extends TechSelectorButton<W>> extends ButtonWidget<W> {

    @Override
    public WidgetTheme getWidgetTheme(ITheme theme) {
        return theme.getFallback();
    }
}
