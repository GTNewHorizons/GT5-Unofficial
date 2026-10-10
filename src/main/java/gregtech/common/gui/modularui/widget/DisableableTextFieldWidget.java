package gregtech.common.gui.modularui.widget;

import java.util.Collections;
import java.util.function.BooleanSupplier;

import net.minecraft.util.EnumChatFormatting;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.screen.viewport.ModularGuiContext;
import com.cleanroommc.modularui.theme.TextFieldTheme;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.util.GTDataUtils;

public class DisableableTextFieldWidget extends TextFieldWidget {

    protected BooleanSupplier editableGetter;
    protected boolean editable;

    public DisableableTextFieldWidget setEditable(boolean editable) {
        this.editable = editable;
        return this;
    }

    public DisableableTextFieldWidget setEditable(BooleanSupplier editable) {
        this.editableGetter = editable;
        return this;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (editableGetter != null && editableGetter.getAsBoolean() != editable) {
            this.setEditable(editableGetter.getAsBoolean());
        }
    }

    @Override
    protected void drawText(ModularGuiContext context, TextFieldTheme widgetTheme) {
        if (this.handler.isTextEmpty() && this.hintText != null) {
            int c = this.renderer.getColor();
            int hintColor = this.hintTextColor != null ? this.hintTextColor : widgetTheme.getHintColor();
            this.renderer.setColor(hintColor);
            this.renderer.draw(Collections.singletonList(this.hintText));
            this.renderer.setColor(c);
        } else {
            if (!editable) {
                this.renderer.draw(
                    GTDataUtils.mapToList(
                        this.handler.getText(),
                        line -> EnumChatFormatting.GRAY.toString() + EnumChatFormatting.ITALIC + line));
            } else {
                this.renderer.draw(this.handler.getText());
            }
        }
        getScrollArea().getScrollX()
            .setScrollSize(Math.max(0, (int) (this.renderer.getLastActualWidth() + 0.5f)));
    }

    @Override
    public @NotNull Result onMousePressed(int mouseButton) {
        if (!editable) {
            return Result.STOP;
        }

        return super.onMousePressed(mouseButton);
    }
}
