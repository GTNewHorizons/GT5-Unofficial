package net.glease.ggfab.mui;

import java.util.Arrays;

import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.api.drawable.TextRenderer;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import org.jetbrains.annotations.NotNull;

public class ClickableTextWidget extends ButtonWidget {

    private Text caption;
    private int maxLines = 1;
    private int marginInLines = 1;

    public ClickableTextWidget(Text caption) {
        super();
        this.caption = caption;
        super.setBackground(caption);
    }

    public ClickableTextWidget setText(Text caption) {
        this.caption = caption;
        return this;
    }

    public ClickableTextWidget setMaxLines(int maxLines) {
        this.maxLines = maxLines;
        return this;
    }

    public ClickableTextWidget setMarginInLines(int margin) {
        this.marginInLines = margin;
        return this;
    }

    @Override
    public Widget setBackground(IDrawable... drawables) {
        IDrawable[] all = Arrays.copyOf(drawables, drawables.length + 1);
        all[drawables.length] = caption;
        return super.setBackground(all);
    }

    @Override
    protected @NotNull Size determineSize(int maxWidth, int maxHeight) {
        if (caption == null)
            return super.determineSize(maxWidth, maxHeight);
        return new Size(Math.min(maxWidth, TextRenderer.getFontRenderer().getStringWidth(caption.getFormatted())), (maxLines + marginInLines) * TextRenderer.getFontRenderer().FONT_HEIGHT);
    }
}
