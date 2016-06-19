package miscutil.gregtech.api.enums;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.objects.GT_RenderedTexture;
import miscutil.core.lib.CORE;
import miscutil.core.util.Utils;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class GregtechTextures {
	public enum BlockIcons implements IIconContainer, Runnable {	


		LARGECENTRIFUGE1, LARGECENTRIFUGE2, LARGECENTRIFUGE3, 
		LARGECENTRIFUGE4, LARGECENTRIFUGE5, LARGECENTRIFUGE6, 
		LARGECENTRIFUGE7, LARGECENTRIFUGE8, LARGECENTRIFUGE9, 
		LARGECENTRIFUGE_ACTIVE1, LARGECENTRIFUGE_ACTIVE2, LARGECENTRIFUGE_ACTIVE3, 
		LARGECENTRIFUGE_ACTIVE4, LARGECENTRIFUGE_ACTIVE5, LARGECENTRIFUGE_ACTIVE6, 
		LARGECENTRIFUGE_ACTIVE7, LARGECENTRIFUGE_ACTIVE8, LARGECENTRIFUGE_ACTIVE9;

		public static final IIconContainer[]

				CENTRIFUGE = new IIconContainer[]{
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
		CENTRIFUGE_ACTIVE = new IIconContainer[]{
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

		public static ITexture[]   
				GT_CASING_BLOCKS = new ITexture[64];

		protected IIcon mIcon;

        private BlockIcons() {
            CORE.GT_BlockIconload.add(this);
        }

        @Override
        public IIcon getIcon() {
            return mIcon;
        }

        @Override
        public IIcon getOverlayIcon() {
            return null;
        }

        @Override
        public void run() {
            mIcon = CORE.GT_BlockIcons.registerIcon(CORE.RES_PATH_BLOCK + "iconsets/" + this);
        }

        @Override
        public ResourceLocation getTextureFile() {
            return TextureMap.locationBlocksTexture; //TODO
        }

        public static class CustomIcon implements IIconContainer, Runnable {
            protected IIcon mIcon;
            protected String mIconName;

            public CustomIcon(String aIconName) {
                mIconName = aIconName;
                CORE.GT_BlockIconload.add(this);
            }

            @Override
            public IIcon getIcon() {
                return mIcon;
            }

            @Override
            public IIcon getOverlayIcon() {
                return null;
            }

            @Override
            public void run() {
                mIcon = CORE.GT_BlockIcons.registerIcon(CORE.RES_PATH_BLOCK + mIconName);
            }

            @Override
            public ResourceLocation getTextureFile() {
                return TextureMap.locationBlocksTexture;
            }
        }
	}
	
	
	public enum ItemIcons implements IIconContainer, Runnable {
        VOID, RENDERING_ERROR, // The Empty Texture
        TURBINE, TURBINE_SMALL, TURBINE_LARGE, TURBINE_HUGE;

        public static final ITexture[] ERROR_RENDERING = new ITexture[]{new GT_RenderedTexture(RENDERING_ERROR)};

        protected IIcon mIcon, mOverlay;

        private ItemIcons() {
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
        public ResourceLocation getTextureFile() {
            return TextureMap.locationItemsTexture;
        }

        @Override
        public void run() {
            mIcon = GregTech_API.sItemIcons.registerIcon(CORE.MODID+":" + "iconsets/" + this);
            if (mIcon != null){
            	Utils.LOG_INFO("Found Texture at "+CORE.MODID+":" + "iconsets/" + this);
            }
            else if (mIcon == null){
            	mIcon = GregTech_API.sItemIcons.registerIcon(CORE.RES_PATH_ITEM + "iconsets/" + this);
            	 if (mIcon != null){
                 	Utils.LOG_INFO("Found Texture at "+CORE.RES_PATH_ITEM + "iconsets/" + this);
                 }
            	 else {
            		 Utils.LOG_INFO("Did not find Texture at "+CORE.RES_PATH_ITEM + "iconsets/" + this);
            	 }
            }
            mOverlay = GregTech_API.sItemIcons.registerIcon(CORE.MODID+":" + "iconsets/" + this + "_OVERLAY");
            if (mOverlay != null){
            	Utils.LOG_INFO("Found Texture at "+CORE.MODID+":" + "iconsets/" + this+ "_OVERLAY");
            }
            else if (mOverlay == null){
            	mOverlay = GregTech_API.sItemIcons.registerIcon(CORE.RES_PATH_ITEM + "iconsets/" + this+ "_OVERLAY");
            	 if (mOverlay != null){
                 	Utils.LOG_INFO("Found Texture at "+CORE.RES_PATH_ITEM + "iconsets/" + this+ "_OVERLAY");
                 }
            	 else {
            		 Utils.LOG_INFO("Did not find Texture at "+CORE.RES_PATH_ITEM + "iconsets/" + this+ "_OVERLAY");
            	 }
            }
        }

        public static class CustomIcon implements IIconContainer, Runnable {
            protected IIcon mIcon, mOverlay;
            protected String mIconName;

            public CustomIcon(String aIconName) {
                mIconName = aIconName;
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
                mIcon = GregTech_API.sItemIcons.registerIcon(CORE.MODID+":" + mIconName);
                mOverlay = GregTech_API.sItemIcons.registerIcon(CORE.MODID+":" + mIconName + "_OVERLAY");
            }

            @Override
            public ResourceLocation getTextureFile() {
                return TextureMap.locationItemsTexture;
            }
        }
    }
	
}

