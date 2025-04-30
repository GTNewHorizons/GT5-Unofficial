package gregtech.common.modularui2.widget;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.widgets.ToggleButton;

/**
 * This variant of the ToggleButton cannot be manually un-toggled by the user. Any click interaction results in the
 * button becoming "selected" and clicks on an already selected button are ignored.
 */
public class SelectButton extends ToggleButton {

    private static final int UNSELECTED_STATE = 0;

    @Override
    public @NotNull Result onMousePressed(int mouseButton) {
        if (getState() == UNSELECTED_STATE) {
            return super.onMousePressed(mouseButton);
        }
        return Result.IGNORE;
    }

}
