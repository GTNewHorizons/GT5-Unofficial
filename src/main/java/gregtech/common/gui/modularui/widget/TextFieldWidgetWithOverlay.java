package gregtech.common.gui.modularui.widget;

import java.util.ArrayList;
import java.util.function.BooleanSupplier;

import com.cleanroommc.modularui.screen.viewport.ModularGuiContext;
import com.cleanroommc.modularui.theme.WidgetTheme;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

public class TextFieldWidgetWithOverlay extends TextFieldWidget {

    private BooleanSupplier allowedToRenderText;
    private boolean intNums = false;

    public TextFieldWidgetWithOverlay(BooleanSupplier shouldRenderText) {
        this.allowedToRenderText = shouldRenderText;
    }

    @Override
    public void drawText(ModularGuiContext context) {

        this.renderer.setSimulate(false);
        this.renderer.setPos(
            this.getArea()
                .getPadding().left,
            0);
        this.renderer.setScale(this.scale);
        this.renderer.setAlignment(this.textAlignment, -1.0F, (float) this.getArea().height);
        this.renderer.draw(
            allowedToRenderText.getAsBoolean() || this.getMathFailMessage() != null ? this.handler.getText()
                : new ArrayList<>());
        this.getScrollData()
            .setScrollSize(Math.max(0, (int) this.renderer.getLastWidth()));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        // if (!getText().equals(this.stringValue.getStringValue())) {
        // this.stringValue.setStringValue(this.validator.apply(getText()));
        // }
    }

    @Override
    public void drawOverlay(ModularGuiContext context, WidgetTheme widgetTheme) {
        if (allowedToRenderText.getAsBoolean() || this.getMathFailMessage() != null) return;
        super.drawOverlay(context, widgetTheme);
    }
}
