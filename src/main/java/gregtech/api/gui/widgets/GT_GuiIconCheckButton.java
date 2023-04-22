package gregtech.api.gui.widgets;

import gregtech.api.interfaces.IGuiScreen;

public class GT_GuiIconCheckButton extends GT_GuiIconButton {

    private final GT_GuiIcon checkedIcon;
    private final GT_GuiIcon normalIcon;
    private final String checkedTooltip;
    private final String normalTooltip;
    private boolean checked = false;

    public GT_GuiIconCheckButton(IGuiScreen gui, int id, int x, int y, GT_GuiIcon checkedIcon, GT_GuiIcon normalIcon) {
        this(gui, id, x, y, checkedIcon, normalIcon, null, null);
    }

    public GT_GuiIconCheckButton(IGuiScreen gui, int id, int x, int y, GT_GuiIcon checkedIcon, GT_GuiIcon normalIcon,
        String checkedTooltip, String normalTooltip) {
        super(gui, id, x, y, normalIcon);
        this.checkedIcon = checkedIcon;
        this.normalIcon = normalIcon;
        this.checkedTooltip = checkedTooltip;
        this.normalTooltip = normalTooltip;
    }

    @Override
    public GT_GuiIcon getButtonTexture(boolean mouseOver) {
        if (!enabled) return GT_GuiIcon.BUTTON_DISABLED;
        if (this.equals(super.gui.getSelectedButton()))
            return mouseOver ? GT_GuiIcon.BUTTON_HIGHLIGHT_DOWN : GT_GuiIcon.BUTTON_DOWN;
        return mouseOver ? GT_GuiIcon.BUTTON_HIGHLIGHT : GT_GuiIcon.BUTTON_NORMAL;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        super.setIcon(checked ? checkedIcon : normalIcon);
        super.setTooltipText(checked ? checkedTooltip : normalTooltip);
        this.checked = checked;
    }
}
