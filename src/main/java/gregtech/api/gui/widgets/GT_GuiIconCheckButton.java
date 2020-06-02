package gregtech.api.gui.widgets;

import gregtech.api.interfaces.IGuiScreen;

public class GT_GuiIconCheckButton extends GT_GuiIconButton {
    private GT_GuiIcon checkedIcon, normalIcon;
    private boolean checked = false;

    public GT_GuiIconCheckButton(IGuiScreen gui, int id, int x, int y, GT_GuiIcon checkedIcon, GT_GuiIcon normalIcon) {
        super(gui, id, x, y, normalIcon);
        this.checkedIcon = checkedIcon;
        this.normalIcon = normalIcon;
    }

    public GT_GuiIcon getButtonTexture(boolean mouseOver) {
        if (!enabled)
            return GT_GuiIcon.BUTTON_DISABLED;
        if (this.equals(super.gui.getSelectedButton()))
            return mouseOver ? GT_GuiIcon.BUTTON_HIGHLIGHT_DOWN : GT_GuiIcon.BUTTON_DOWN;
        return mouseOver ? GT_GuiIcon.BUTTON_HIGHLIGHT : GT_GuiIcon.BUTTON_NORMAL;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        super.setIcon(checked ? checkedIcon : normalIcon);
        this.checked = checked;
    }
}
