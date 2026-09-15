package gregtech.api.interfaces.modularui;

import gregtech.api.gui.modularui.GUITextureSet;

public interface IGetGUITextureSet {

    default GUITextureSet getGUITextureSet() {
        return null;
    }
}
