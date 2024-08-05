package gregtech.api.gui.modularui2;

public class UITextureAdapter extends com.cleanroommc.modularui.drawable.UITexture {

    public UITextureAdapter(com.gtnewhorizons.modularui.api.drawable.UITexture texture) {
        super(texture.location, texture.u0, texture.v0, texture.u1, texture.v1, false);
    }
}
