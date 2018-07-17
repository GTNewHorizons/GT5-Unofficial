package gtPlusPlus.xmod.gregtech.api.enums;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.objects.GT_RenderedTexture;

import gtPlusPlus.core.lib.CORE;

public class CustomGtTextures {
	public enum ItemIcons implements IIconContainer, Runnable {
		VOID, // The Empty Texture
		RENDERING_ERROR,
		PUMP, 
		SKOOKUMCHOOCHER;

		public static final ITexture[] ERROR_RENDERING = new ITexture[]{new GT_RenderedTexture(RENDERING_ERROR)};

		protected IIcon mIcon, mOverlay;

		private ItemIcons() {
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
			this.mIcon = GregTech_API.sItemIcons.registerIcon(CORE.RES_PATH_ITEM + "iconsets/" + this);
			this.mOverlay = GregTech_API.sItemIcons.registerIcon(CORE.RES_PATH_ITEM + "iconsets/" + this + "_OVERLAY");
		}

		public static class CustomIcon implements IIconContainer, Runnable {
			protected IIcon mIcon, mOverlay;
			protected String mIconName;

			public CustomIcon(final String aIconName) {
				this.mIconName = aIconName;
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
			public void run() {
				this.mIcon = GregTech_API.sItemIcons.registerIcon(CORE.RES_PATH_ITEM + this.mIconName);
				this.mOverlay = GregTech_API.sItemIcons.registerIcon(CORE.RES_PATH_ITEM + this.mIconName + "_OVERLAY");
			}

			@Override
			public ResourceLocation getTextureFile() {
				return TextureMap.locationItemsTexture;
			}
		}
	}
}