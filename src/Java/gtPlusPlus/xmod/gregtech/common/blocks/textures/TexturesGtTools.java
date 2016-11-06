package gtPlusPlus.xmod.gregtech.common.blocks.textures;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.IIconContainer;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public final class TexturesGtTools {

	public final static CustomIcon SKOOKUM_CHOOCHER = new CustomIcon("iconsets/SKOOKUMCHOOCHER");
	
	
	
	
	
	
	
	
	
	
	
	
	public final static class CustomIcon implements IIconContainer, Runnable {
        protected IIcon mIcon, mOverlay;
        protected final String mIconName;

        public CustomIcon(final String aIconName) {
            mIconName = aIconName;
        	Utils.LOG_INFO("Constructing a Custom Texture. " + mIconName);
            GregTech_API.sGTItemIconload.add(this);
        }

        @Override
        public IIcon getIcon() {
            return mIcon;
        }

        @Override
        public IIcon getOverlayIcon() {
            return mOverlay;
        }

        @Override
        public void run() {
            mIcon = GregTech_API.sItemIcons.registerIcon(CORE.MODID + ":"  + mIconName);
        	//Utils.LOG_INFO("Registering a Custom Texture. "+mIcon.g);
            mOverlay = GregTech_API.sItemIcons.registerIcon(CORE.MODID + ":"  + mIconName + "_OVERLAY");
        }

        @Override
        public ResourceLocation getTextureFile() {
            return TextureMap.locationItemsTexture;
        }
    }

}
