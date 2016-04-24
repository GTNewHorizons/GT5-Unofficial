package miscutil.gregtech.api.enums;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import miscutil.core.lib.CORE;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class GregtechTextures {
    public enum BlockIcons implements IIconContainer, Runnable {	


	LARGECENTRIFUGE1, LARGECENTRIFUGE2, LARGECENTRIFUGE3, LARGECENTRIFUGE4, LARGECENTRIFUGE5, 
	LARGECENTRIFUGE6, LARGECENTRIFUGE7, LARGECENTRIFUGE8, LARGECENTRIFUGE9, 
	LARGECENTRIFUGE_ACTIVE1, LARGECENTRIFUGE_ACTIVE2, LARGECENTRIFUGE_ACTIVE3, LARGECENTRIFUGE_ACTIVE4, 
	LARGECENTRIFUGE_ACTIVE5, LARGECENTRIFUGE_ACTIVE6, LARGECENTRIFUGE_ACTIVE7, LARGECENTRIFUGE_ACTIVE8, LARGECENTRIFUGE_ACTIVE9;
	
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
        mIcon = GregTech_API.sBlockIcons.registerIcon(CORE.RES_PATH_BLOCK + "iconsets/" + this);
    }

    @Override
    public ResourceLocation getTextureFile() {
        return TextureMap.locationBlocksTexture;
    }

    public static class CustomIcon implements IIconContainer, Runnable {
        protected IIcon mIcon;
        protected String mIconName;

        public CustomIcon(String aIconName) {
            mIconName = aIconName;
            GregTech_API.sGTBlockIconload.add(this);
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
            mIcon = GregTech_API.sBlockIcons.registerIcon(CORE.RES_PATH_BLOCK + mIconName);
        }

        @Override
        public ResourceLocation getTextureFile() {
            return TextureMap.locationBlocksTexture;
        }
    }
}}

