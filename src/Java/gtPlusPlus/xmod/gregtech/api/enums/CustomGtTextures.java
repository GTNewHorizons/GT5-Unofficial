package gtPlusPlus.xmod.gregtech.api.enums;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.objects.GT_RenderedTexture;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class CustomGtTextures {
	public enum ItemIcons implements IIconContainer, Runnable {
		VOID // The Empty Texture
		, RENDERING_ERROR, WRENCH, MORTAR, CROWBAR, JACKHAMMER, WIRE_CUTTER, KNIFE, BUTCHERYKNIFE, SICKLE, SCOOP, GRAFTER, PLUNGER, ROLLING_PIN, HANDLE_SWORD, HANDLE_FILE, HANDLE_SAW, HANDLE_SCREWDRIVER, HANDLE_BUZZSAW, HANDLE_ELECTRIC_SCREWDRIVER, HANDLE_SOLDERING, POWER_UNIT_LV, POWER_UNIT_MV, POWER_UNIT_HV, DURABILITY_BAR_0, DURABILITY_BAR_1, DURABILITY_BAR_2, DURABILITY_BAR_3, DURABILITY_BAR_4, DURABILITY_BAR_5, DURABILITY_BAR_6, DURABILITY_BAR_7, DURABILITY_BAR_8, ENERGY_BAR_0, ENERGY_BAR_1, ENERGY_BAR_2, ENERGY_BAR_3, ENERGY_BAR_4, ENERGY_BAR_5, ENERGY_BAR_6, ENERGY_BAR_7, ENERGY_BAR_8,

		SKOOKUMCHOOCHER, TURBINE_SMALL, TURBINE_LARGE, TURBINE_HUGE;

		public static class CustomIcon implements IIconContainer, Runnable {
			protected IIcon		mIcon, mOverlay;
			protected String	mIconName;

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
			public ResourceLocation getTextureFile() {
				return TextureMap.locationItemsTexture;
			}

			@Override
			public void run() {
				this.mIcon = GregTech_API.sItemIcons.registerIcon(CORE.RES_PATH_ITEM + this.mIconName);
				this.mOverlay = GregTech_API.sItemIcons.registerIcon(CORE.RES_PATH_ITEM + this.mIconName + "_OVERLAY");
			}
		}

		public static final ITexture[]	ERROR_RENDERING	= new ITexture[] {
				new GT_RenderedTexture(RENDERING_ERROR)
		};

		protected IIcon					mIcon, mOverlay;

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
	}
}