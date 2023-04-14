package gregtech.common.render;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;

public interface MultiTileBasicRender {

    ITexture getTexture(ForgeDirection direction);
}
