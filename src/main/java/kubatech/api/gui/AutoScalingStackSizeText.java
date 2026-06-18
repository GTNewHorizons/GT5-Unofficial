package kubatech.api.gui;

import java.util.Collections;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.modularui.api.NumberFormatMUI;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.TextRenderer;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.common.internal.Theme;

public class AutoScalingStackSizeText implements IDrawable {

    private static final TextRenderer measuringRenderer = new TextRenderer();
    private static final NumberFormatMUI muiNumberFormat = new NumberFormatMUI();
    private static final TextRenderer renderer = new TextRenderer();
    private Alignment alignment = Alignment.Center;
    private final String text;
    private int simWidth;

    private int color;
    private boolean shadow = false;

    public AutoScalingStackSizeText(long stackSize) {
        this.text = muiNumberFormat.formatWithSuffix(stackSize);
        this.color = Theme.INSTANCE.getText();
        this.measure();
    }

    public @NotNull AutoScalingStackSizeText color(int color) {
        this.color = Color.withAlpha(color, 255);
        return this;
    }

    public @NotNull AutoScalingStackSizeText shadow(boolean shadow) {
        this.shadow = shadow;
        return this;
    }

    public AutoScalingStackSizeText shadow() {
        return shadow(true);
    }

    public @NotNull AutoScalingStackSizeText alignment(Alignment alignment) {
        this.alignment = alignment;
        return this;
    }

    public @NotNull AutoScalingStackSizeText measure() {
        this.simWidth = measuringRenderer.getMaxWidth(Collections.singletonList(this.text));
        return this;
    }

    public boolean hasColor() {
        return Color.getAlpha(color) > 0;
    }

    @Override
    public void applyThemeColor(int color) {
        renderer.setColor(hasColor() ? this.color : Theme.INSTANCE.getText());
    }

    @Override
    public void draw(float x, float y, float width, float height, float partialTicks) {
        renderer.setPos((int) (x - 0.5), (int) (y - 0.5));
        renderer.setShadow(this.shadow);
        renderer.setAlignment(alignment, width, height);
        renderer.setColor(this.color);
        renderer.setScale(this.simWidth <= 16.0f ? 1.0f : 16.0f / this.simWidth);
        renderer.draw(this.text);
    }
}
