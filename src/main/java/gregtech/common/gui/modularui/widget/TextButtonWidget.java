package gregtech.common.gui.modularui.widget;

import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.MultiChildWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

public class TextButtonWidget extends MultiChildWidget {

    private ButtonWidget mButton;
    private TextWidget mText;

    private int leftMargin;

    public TextButtonWidget(String text) {
        mButton = new ButtonWidget();
        mText = new TextWidget(text);
        mText.setPos(0, 0);
        mButton.setPos(0, 0);

        this.addChild(mButton);
        this.addChild(mText);
    }

    public ButtonWidget button() {
        return mButton;
    }

    public TextWidget text() {
        return mText;
    }

    @Override
    public Widget setPos(int x, int y) {
        return super.setPos(x, y);
    }

    @Override
    public Widget setSize(int width, int height) {
        this.mButton.setSize(width, height);
        this.mText.setSize(width, height);
        return super.setSize(width, height);
    }

    @Override
    public Widget setSize(Size size) {
        this.mButton.setSize(size);
        return super.setSize(size);
    }

    public TextButtonWidget setLeftMargin(int margin) {
        this.leftMargin = margin;
        this.mText.setPos(this.leftMargin, this.mText.getPos().y);
        return this;
    }
}
