package gregtech.common.gui.modularui.widget;

import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.drawable.text.StringKey;
import com.cleanroommc.modularui.screen.viewport.GuiContext;
import com.cleanroommc.modularui.theme.WidgetTheme;
import com.cleanroommc.modularui.utils.Alignment;

public class StringKeyCustom extends StringKey {

    public StringKeyCustom(String string) {
        super(string);
    }

    public StringKeyCustom(String string, @Nullable Object[] args) {
        super(string, args);
    }

    @Override
    public void draw(GuiContext context, int x, int y, int width, int height, WidgetTheme widgetTheme) {
        renderer.setColor(widgetTheme.getTextColor());
        renderer.setShadow(widgetTheme.getTextShadow());
        renderer.setAlignment(Alignment.CenterLeft, (float) width, (float) height);
        renderer.setScale(1.0F);
        renderer.setPos(x + 5, y);
        renderer.draw(this.getFormatted());

    }
}
