package gtPlusPlus.core.gui.beta;

import gtPlusPlus.core.interfaces.IGuiManagerMiscUtils;

public class MUGuild {

    private final int id;
    private final GUITypes MU_GuiType;
    private final Class<? extends IGuiManagerMiscUtils> guiHandlerClass;

    MUGuild(final int id, final GUITypes MU_GuiType, final Class<? extends IGuiManagerMiscUtils> guiHandlerClass) {
        this.id = id;
        this.MU_GuiType = MU_GuiType;
        this.guiHandlerClass = guiHandlerClass;
    }

    public GUITypes getGuiType() {
        return this.MU_GuiType;
    }

    public Class<? extends IGuiManagerMiscUtils> getGuiHandlerClass() {
        return this.guiHandlerClass;
    }

    public int getId() {
        return this.id;
    }
}
