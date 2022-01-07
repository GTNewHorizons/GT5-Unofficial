package gregtech.api.interfaces;

/**
 * To allow addons to make use of GT_GuiIcon
 */
public interface IGuiIcon {
    int getX();
    int getY();
    int getWidth();
    int getHeight();
    int getTexId();
    IGuiIcon getOverlay();
}
