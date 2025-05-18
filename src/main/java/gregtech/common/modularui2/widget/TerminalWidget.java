package gregtech.common.modularui2.widget;

import java.util.Stack;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widget.SingleChildWidget;
import com.cleanroommc.modularui.widget.WidgetTree;
import com.cleanroommc.modularui.widget.scroll.ScrollData;
import com.cleanroommc.modularui.widget.sizer.Area;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.modularui2.GTGuiTextures;

/**
 * A ParentWidget containing a text field and a display, showing the player's messages as well as the "responses"
 */
public class TerminalWidget extends ParentWidget<TerminalWidget> {

    public Stack<String> textData = new Stack<>();
    public TerminalTextListWidget list;
    public TerminalTextFieldWidget textField;
    public Function<String, String> responseSupplier;
    private StringSyncValue dummySyncer = new StringSyncValue(() -> "");
    public int spaceBetweenMessages = 2;
    public int playerTextColor = Color.WHITE.main;
    public int responseTextColor = Color.CYAN.main;
    public int terminalHeight = 100;
    public int terminalWidth = 140;

    public TerminalWidget() {
        super();
    }

    /**
     * Sets the terminal's width
     * 
     * @param value Desired width of the terminal
     * @return
     */
    public TerminalWidget setTerminalWidth(int value) {
        terminalWidth = value;
        return this;
    }

    /**
     * Sets the terminal's height. The height of the display will be the set height minus 13 because the text field is
     * given 13px of space
     * 
     * @param value Desired height of the terminal
     * @return this
     */
    public TerminalWidget setTerminalHeight(int value) {
        terminalHeight = value;
        return this;
    }

    public TerminalWidget setPlayerTextColor(int value) {
        playerTextColor = value;
        return this;
    }

    public TerminalWidget setResponseTextColor(int value) {
        responseTextColor = value;
        return this;
    }

    public TerminalWidget setResponseSupplier(Function<String, String> value) {
        responseSupplier = value;
        return this;
    }

    public TerminalWidget setSpaceBetweenMessages(int value) {
        spaceBetweenMessages = value;
        return this;
    }

    /**
     * Creates all the component widgets
     *
     * @return this
     */
    public TerminalWidget build() {
        Area area = this.getArea();
        list = new TerminalTextListWidget().childSeparator(
            IDrawable.EMPTY.asIcon()
                .height(2))
            .size(area.width - 4, area.height - 17)
            .pos(2, 2);
        this.child(
            new SingleChildWidget<>().background(GTGuiTextures.BACKGROUND_TEXT_FIELD)
                .size(area.width, area.height - 13)
                .child(list));
        textField = new TerminalTextFieldWidget();
        textField.value(dummySyncer)
            .setFocusOnGuiOpen(true)
            .size(area.width, 10)
            .pos(0, area.height - 10);
        this.child(textField);
        return this;
    }

    public void saveTextToList(String playerText, String responseText) {
        textData.add(playerText);
        textData.add(responseText);
    }

    public TextWidget createPlayerTextWidget(String text) {
        return new TextWidget(text) {

            @Override
            public int getDefaultWidth() {
                return Math.max(super.getDefaultWidth(), getParentArea().width - 4);
            }
        }.alignment(Alignment.CenterRight)
            .color(playerTextColor);
    }

    @Override
    public TerminalWidget size(int w, int h) {
        super.size(w, h);
        getArea().width = w;
        getArea().height = h;
        return this;
    }

    public TextWidget createResponseTextWidget(String text) {
        return new TextWidget(text).color(responseTextColor);
    }

    public class TerminalTextFieldWidget extends TextFieldWidget {

        public TerminalTextFieldWidget() {
            super();
        }

        @Override
        public @NotNull Result onKeyPressed(char character, int keyCode) {
            if (keyCode == Keyboard.KEY_RETURN) {
                String text = this.getText();
                if (text.isEmpty()) return Result.IGNORE;
                String response = responseSupplier.apply(text);
                saveTextToList(text, response);
                list.child(createPlayerTextWidget(text));
                list.child(createResponseTextWidget(response));
                this.handler.clear();
                dummySyncer.setValue("");
                // Reset the text box to be blank
            } else return super.onKeyPressed(character, keyCode);
            return Result.SUCCESS;
        }
    }

    public class TerminalTextListWidget extends ListWidget<IWidget, TerminalTextListWidget> {

        public TerminalTextListWidget() {
            super();
        }

        @Override
        public void onChildAdd(IWidget child) {
            super.onChildAdd(child);
            WidgetTree.resize(this.getParent());
            ScrollData data = this.getScrollData();
            // Scroll to the bottom
            data.scrollTo(getScrollArea(), data.getScrollSize());
        }
    }

}
