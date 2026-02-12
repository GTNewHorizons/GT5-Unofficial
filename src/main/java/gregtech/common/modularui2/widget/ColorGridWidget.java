package gregtech.common.modularui2.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import net.minecraft.util.EnumChatFormatting;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.widget.sizer.Area;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.google.common.collect.ImmutableList;

import gregtech.api.enums.Dyes;

/**
 * A Grid containing 16 ToggleButtons arranged in a 4x4 pattern. Each button represents a different minecraft dye
 * color
 */
public class ColorGridWidget extends Column {

    public List<Byte> selected = new ArrayList<>();
    public Consumer<List<Byte>> onToggle;
    public int maxSelected = 16;
    public int buttonSize = 9;
    public int buttonPadding = 1;
    public int borderSize = 1;

    public ColorGridWidget() {
        super();
        this.coverChildren();
    }

    /**
     * @param value The initial selected button index (0-15) for the grid. Enter a number outside bounds for none
     * @return this
     */
    public ColorGridWidget setInitialSelected(byte value) {
        if (value >= 0 && value < 16) {
            selected = ImmutableList.of(value);
        }
        return this;
    }

    /**
     *
     * @param list The initial selected buttons index (0-15) for the grid. Enter an invalid index (like null) for none
     * @return
     */
    public ColorGridWidget setInitialSelected(List<Byte> list) {
        if (list == null) return this;
        for (Byte value : list) {
            if (value < 0 || value > 15) return this;
        }
        selected = list;
        return this;
    }

    /**
     * @param value The desired maximum number of colors that can be selected at once
     * @return this
     */
    public ColorGridWidget setMaxSelected(int value) {
        maxSelected = value;
        return this;
    }

    /**
     * @param value The total width/height desired for every button in the grid
     * @return this
     */
    public ColorGridWidget setButtonSize(int value) {
        buttonSize = value;
        return this;
    }

    /**
     * @param value The desired space between every button in the grid
     * @return this
     */
    public ColorGridWidget setButtonPadding(int value) {
        buttonPadding = value;
        return this;
    }

    /**
     * @param value The desired thickness of the border for every button in the grid
     * @return this
     */
    public ColorGridWidget setBorderSize(int value) {
        borderSize = value;
        return this;
    }

    /**
     * @param toRun What gets called on every button press. This is called after the button has been toggled and the
     *              grid checked for excess
     * @return this
     */
    public ColorGridWidget onButtonToggled(Consumer<List<Byte>> toRun) {
        onToggle = toRun;
        return this;
    }

    /**
     * Creates the ToggleButtons and the Grid
     *
     * @return this
     */
    public ColorGridWidget build() {
        createButtonRows();
        coverChildren().childPadding(buttonPadding);
        return this;
    }

    public void createButtonRows() {
        for (int i = 0; i < 4; i++) {
            Row row = new Row();
            for (int j = 0; j < 4; j++) {
                Dyes dye = Dyes.VALUES[(i * 4) + j];
                short[] colors = dye.getRGBA();
                row.child(createToggleButton(Color.argb(colors[0], colors[1], colors[2], 255), ((i * 4) + j)));
            }
            this.child(
                row.childPadding(buttonPadding)
                    .coverChildren());
        }
    }

    public ToggleButton createToggleButton(int color, int index) {
        return new ColorGridButton(index) {

            @Override
            public void onInit() {
                super.onInit();
                if (selected.contains((byte) index) && maxSelected >= selected.size()) {
                    setStateWithoutSelecting(1, true);
                }
            }

            @Override
            public @NotNull Result onMousePressed(int mouseButton) {
                Result result = super.onMousePressed(mouseButton);
                onToggled();
                onToggle.accept(selected);
                return result;
            }
        }.background(false, drawButton(false, Color.multiply(color, 0.5F, false)))
            .background(true, drawButton(true, color))
            .hoverOverlay(false, drawButton(false, Color.multiply(color, 0.7F, false)))
            .hoverOverlay(true, drawButton(true, color))
            .tooltipDynamic(tooltip -> {
                if (selected.contains((byte) index)) {
                    tooltip.add(Dyes.VALUES[index].getLocalizedDyeName());
                    tooltip.add(EnumChatFormatting.GREEN + " Selected");
                } else {
                    tooltip.addLine((EnumChatFormatting.GRAY + "Click to select"));
                    tooltip.add(Dyes.VALUES[index].getLocalizedDyeName());

                }
            })
            .size(buttonSize, buttonSize);
    }

    public IDrawable drawButtonBorder(boolean active) {
        return (context, x, y, w, h, widgetTheme) -> {
            new Rectangle().color(active ? Color.RED.main : Color.BLACK.main)
                .draw(context, new Area(x, y, buttonSize, buttonSize), widgetTheme);
        };
    }

    public IDrawable drawButton(boolean active, int color) {
        int insideSize = buttonSize - (borderSize * 2);
        return IDrawable.of(drawButtonBorder(active), (context, x, y, w, h, widgetTheme) -> {
            new Rectangle().color(color)
                .draw(context, new Area(x + borderSize, y + borderSize, insideSize, insideSize), widgetTheme);
        });
    }

    protected void onToggled() {
        if (selected.size() > maxSelected) {
            int target = selected.get(0);
            IWidget targetChild = getChildren().get(target / 4)
                .getChildren()
                .get(target % 4);
            if (!(targetChild instanceof ColorGridButton button)) return;
            button.setState(0, true);
        }
    }

    /**
     * @return The name of the current selected color at i.
     */
    public String getName(int i) {
        if (i >= getAmountSelected()) return "None";
        int select = selected.get(i);
        return select >= 0 && select < 16 ? getDye(i).getLocalizedDyeName() : "None";
    }

    /**
     * @return The currently selected button's EnumChatFormatting
     */
    public EnumChatFormatting getColorFormatting(int i) {
        int select = selected.get(i);
        return select >= 0 && select < 16 ? Dyes.VALUES[select].formatting : null;
    }

    /**
     * @return The current selected button's Dyes
     */
    public Dyes getDye(int i) {
        int select = selected.get(i);
        return select >= 0 && select < 16 ? Dyes.VALUES[select] : null;
    }

    /**
     * @return The list of selected button's indexes
     */
    public List<Byte> getSelected() {
        return selected;
    }

    public Byte getSelected(int i) {
        return selected.get(i);
    }

    /**
     * @return The amount of buttons currently selected
     */
    public int getAmountSelected() {
        return getSelected().size();
    }

    public class ColorGridButton extends ToggleButton {

        int index;

        public ColorGridButton(int value) {
            super();
            index = value;
        }

        @Override
        public void setState(int state, boolean setSource) {
            super.setState(state, setSource);
            if (!setSource) return;
            if (state == 0) {
                selected.remove((Byte) ((byte) index));
            } else {
                selected.add((byte) index);
            }
        }

        // Workaround for calling double super in the anonymous class ColorGridWidget uses
        public void setStateWithoutSelecting(int state, boolean setSource) {
            super.setState(state, setSource);
        }
    }

}
