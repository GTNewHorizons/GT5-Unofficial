package gtPlusPlus.xmod.gregtech.common.blocks.textures;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.IIconContainer;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public final class TexturesGtTools {

	public final static class CustomIcon implements IIconContainer, Runnable {
		protected IIcon			mIcon, mOverlay;
		protected final String	mIconName;

		public CustomIcon(final String aIconName) {
			this.mIconName = aIconName;
			Utils.LOG_INFO("Constructing a Custom Texture. " + this.mIconName);
			GregTech_API.sGTItemIconload.add(this);
		}

		@Override
		public IIcon getIcon() {
			return this.mIcon;
		}

		@Override
		public IIcon getOverlayIcon() {
			return this.mOverlay;
		}

		@Override
		public ResourceLocation getTextureFile() {
			return TextureMap.locationItemsTexture;
		}

		@Override
		public void run() {
			this.mIcon = GregTech_API.sItemIcons.registerIcon(CORE.MODID + ":" + this.mIconName);
			// Utils.LOG_INFO("Registering a Custom Texture. "+mIcon.g);
			this.mOverlay = GregTech_API.sItemIcons.registerIcon(CORE.MODID + ":" + this.mIconName + "_OVERLAY");
		}
	}

	public final static CustomIcon SKOOKUM_CHOOCHER = new CustomIcon("iconsets/SKOOKUMCHOOCHER");

}
