package gtPlusPlus.xmod.gregtech.api.enums;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.interfaces.internal.Interface_IconContainer;
import gtPlusPlus.xmod.gregtech.api.interfaces.internal.Interface_Texture;
import gtPlusPlus.xmod.gregtech.common.Meta_GT_Proxy;

public class GregtechTextures {
	public enum BlockIcons implements Interface_IconContainer, Runnable {

		VOID,

		LARGECENTRIFUGE1, LARGECENTRIFUGE2, LARGECENTRIFUGE3,
		LARGECENTRIFUGE4, LARGECENTRIFUGE5, LARGECENTRIFUGE6,
		LARGECENTRIFUGE7, LARGECENTRIFUGE8, LARGECENTRIFUGE9,
		LARGECENTRIFUGE_ACTIVE1, LARGECENTRIFUGE_ACTIVE2, LARGECENTRIFUGE_ACTIVE3,
		LARGECENTRIFUGE_ACTIVE4, LARGECENTRIFUGE_ACTIVE5, LARGECENTRIFUGE_ACTIVE6,
		LARGECENTRIFUGE_ACTIVE7, LARGECENTRIFUGE_ACTIVE8, LARGECENTRIFUGE_ACTIVE9;

		public static final Interface_IconContainer[]

				CENTRIFUGE = new Interface_IconContainer[]{
						LARGECENTRIFUGE1,
						LARGECENTRIFUGE2,
						LARGECENTRIFUGE3,
						LARGECENTRIFUGE4,
						LARGECENTRIFUGE5,
						LARGECENTRIFUGE6,
						LARGECENTRIFUGE7,
						LARGECENTRIFUGE8,
						LARGECENTRIFUGE9
		},
				CENTRIFUGE_ACTIVE = new Interface_IconContainer[]{
						LARGECENTRIFUGE_ACTIVE1,
						LARGECENTRIFUGE_ACTIVE2,
						LARGECENTRIFUGE_ACTIVE3,
						LARGECENTRIFUGE_ACTIVE4,
						LARGECENTRIFUGE_ACTIVE5,
						LARGECENTRIFUGE_ACTIVE6,
						LARGECENTRIFUGE_ACTIVE7,
						LARGECENTRIFUGE_ACTIVE8,
						LARGECENTRIFUGE_ACTIVE9
		};

		public static Interface_Texture[]
				GT_CASING_BLOCKS = new Interface_Texture[64];

		protected IIcon mIcon;

		private BlockIcons() {
			Meta_GT_Proxy.GT_BlockIconload.add(this);
		}

		@Override
		public IIcon getIcon() {
			return this.mIcon;
		}

		@Override
		public IIcon getOverlayIcon() {
			return null;
		}

		@Override
		public void run() {
			this.mIcon = Meta_GT_Proxy.sBlockIcons.registerIcon(CORE.MODID + ":" + "iconsets/" + this);
		}

		@Override
		public ResourceLocation getTextureFile() {
			return TextureMap.locationBlocksTexture;
		}

		public static class CustomIcon implements Interface_IconContainer, Runnable {
			protected IIcon mIcon;
			protected String mIconName;

			public CustomIcon(final String aIconName) {
				this.mIconName = aIconName;
				Meta_GT_Proxy.GT_BlockIconload.add(this);
			}

			@Override
			public IIcon getIcon() {
				return this.mIcon;
			}

			@Override
			public IIcon getOverlayIcon() {
				return null;
			}

			@Override
			public void run() {
				this.mIcon = Meta_GT_Proxy.sBlockIcons.registerIcon(CORE.MODID + ":" + this.mIconName);
			}

			@Override
			public ResourceLocation getTextureFile() {
				return TextureMap.locationBlocksTexture;
			}
		}
	}


	public enum ItemIcons implements Interface_IconContainer, Runnable {
		VOID, // The Empty Texture
		RENDERING_ERROR, //The Purple/Black Texture
		SKOOKUMCHOOCHER, //The Skookum Tool Texture
		TURBINE_SMALL, TURBINE_LARGE, TURBINE_HUGE;

		/* public static final Interface_IconContainer[]
                DURABILITY_BAR = new Interface_IconContainer[]{
                DURABILITY_BAR_0,
                DURABILITY_BAR_1,
                DURABILITY_BAR_2,
                DURABILITY_BAR_3,
                DURABILITY_BAR_4,
                DURABILITY_BAR_5,
                DURABILITY_BAR_6,
                DURABILITY_BAR_7,
                DURABILITY_BAR_8,
        },
                ENERGY_BAR = new Interface_IconContainer[]{
                        ENERGY_BAR_0,
                        ENERGY_BAR_1,
                        ENERGY_BAR_2,
                        ENERGY_BAR_3,
                        ENERGY_BAR_4,
                        ENERGY_BAR_5,
                        ENERGY_BAR_6,
                        ENERGY_BAR_7,
                        ENERGY_BAR_8,
                };*/

		//public static final Interface_Texture[] ERROR_RENDERING = new Interface_Texture[]{new GregtechRenderedTexture(RENDERING_ERROR)};

		protected IIcon mIcon, mOverlay;

		private ItemIcons() {
			Meta_GT_Proxy.GT_ItemIconload.add(this);
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
			this.mIcon = Meta_GT_Proxy.sItemIcons.registerIcon(CORE.MODID+ ":" + "iconsets/" + this);
			this.mOverlay = Meta_GT_Proxy.sItemIcons.registerIcon(CORE.MODID+ ":" + "iconsets/" + this + "_OVERLAY");
		}

		public static class CustomIcon implements Interface_IconContainer, Runnable {
			protected IIcon mIcon, mOverlay;
			protected String mIconName;

			public CustomIcon(final String aIconName) {
				this.mIconName = aIconName;
				Meta_GT_Proxy.GT_ItemIconload.add(this);
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
				this.mIcon = Meta_GT_Proxy.sItemIcons.registerIcon(CORE.MODID+ ":" + this.mIconName);
				this.mOverlay = Meta_GT_Proxy.sItemIcons.registerIcon(CORE.MODID+ ":" + this.mIconName + "_OVERLAY");
			}

			@Override
			public ResourceLocation getTextureFile() {
				return TextureMap.locationItemsTexture;
			}
		}
	}

}

