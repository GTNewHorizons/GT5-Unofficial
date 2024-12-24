package gregtech.api.multitileentity;

import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import com.gtnewhorizons.mutecore.api.render.MuTEIcon;

import gregtech.api.interfaces.IIconContainer;

public class GTMuTEIcon extends MuTEIcon implements IIconContainer {

    public GTMuTEIcon(String modid, String path) {
        super(modid, path);
    }

    @Override
    public IIcon getIcon() {
        return getInternalIcon();
    }

    @Override
    public IIcon getOverlayIcon() {
        return null;
    }

    @Override
    public ResourceLocation getTextureFile() {
        return getIconLocation();
    }
}
