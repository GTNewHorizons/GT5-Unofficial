package miscutil.gregtech.api.enums;

import static gregtech.api.enums.GT_Values.RES_PATH_BLOCK;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class GregtechTextures {
    public enum BlockIcons implements IIconContainer, Runnable {	


	LARGECENTRIFUGE_TI1, LARGECENTRIFUGE_TI2, LARGECENTRIFUGE_TI3, LARGECENTRIFUGE_TI4, LARGECENTRIFUGE_TI5, 
	LARGECENTRIFUGE_TI6, LARGECENTRIFUGE_TI7, LARGECENTRIFUGE_TI8, LARGECENTRIFUGE_TI9, 
	LARGECENTRIFUGE_TI_ACTIVE1, LARGECENTRIFUGE_TI_ACTIVE2, LARGECENTRIFUGE_TI_ACTIVE3, LARGECENTRIFUGE_TI_ACTIVE4, 
	LARGECENTRIFUGE_TI_ACTIVE5, LARGECENTRIFUGE_TI_ACTIVE6, LARGECENTRIFUGE_TI_ACTIVE7, LARGECENTRIFUGE_TI_ACTIVE8, LARGECENTRIFUGE_TI_ACTIVE9, 
	
	LARGECENTRIFUGE_TU1, LARGECENTRIFUGE_TU2, LARGECENTRIFUGE_TU3, LARGECENTRIFUGE_TU4, LARGECENTRIFUGE_TU5, 
	LARGECENTRIFUGE_TU6, LARGECENTRIFUGE_TU7, LARGECENTRIFUGE_TU8, LARGECENTRIFUGE_TU9, 
	LARGECENTRIFUGE_TU_ACTIVE1, LARGECENTRIFUGE_TU_ACTIVE2, LARGECENTRIFUGE_TU_ACTIVE3, LARGECENTRIFUGE_TU_ACTIVE4, LARGECENTRIFUGE_TU_ACTIVE5,
	LARGECENTRIFUGE_TU_ACTIVE6, LARGECENTRIFUGE_TU_ACTIVE7, LARGECENTRIFUGE_TU_ACTIVE8, LARGECENTRIFUGE_TU_ACTIVE9;
	
    public static final IIconContainer[]
    		
    CENTRIFUGE1 = new IIconContainer[]{
            LARGECENTRIFUGE_TI1,
            LARGECENTRIFUGE_TI2,
            LARGECENTRIFUGE_TI3,
            LARGECENTRIFUGE_TI4,
            LARGECENTRIFUGE_TI5,
            LARGECENTRIFUGE_TI6,
            LARGECENTRIFUGE_TI7,
            LARGECENTRIFUGE_TI8,
            LARGECENTRIFUGE_TI9
    },
    CENTRIFUGE_ACTIVE1 = new IIconContainer[]{
            LARGECENTRIFUGE_TI_ACTIVE1,
            LARGECENTRIFUGE_TI_ACTIVE2,
            LARGECENTRIFUGE_TI_ACTIVE3,
            LARGECENTRIFUGE_TI_ACTIVE4,
            LARGECENTRIFUGE_TI_ACTIVE5,
            LARGECENTRIFUGE_TI_ACTIVE6,
            LARGECENTRIFUGE_TI_ACTIVE7,
            LARGECENTRIFUGE_TI_ACTIVE8,
            LARGECENTRIFUGE_TI_ACTIVE9
    },
    CENTRIFUGE2 = new IIconContainer[]{
            LARGECENTRIFUGE_TU1,
            LARGECENTRIFUGE_TU2,
            LARGECENTRIFUGE_TU3,
            LARGECENTRIFUGE_TU4,
            LARGECENTRIFUGE_TU5,
            LARGECENTRIFUGE_TU6,
            LARGECENTRIFUGE_TU7,
            LARGECENTRIFUGE_TU8,
            LARGECENTRIFUGE_TU9
    },
    CENTRIFUGE_ACTIVE2 = new IIconContainer[]{
            LARGECENTRIFUGE_TU_ACTIVE1,
            LARGECENTRIFUGE_TU_ACTIVE2,
            LARGECENTRIFUGE_TU_ACTIVE3,
            LARGECENTRIFUGE_TU_ACTIVE4,
            LARGECENTRIFUGE_TU_ACTIVE5,
            LARGECENTRIFUGE_TU_ACTIVE6,
            LARGECENTRIFUGE_TU_ACTIVE7,
            LARGECENTRIFUGE_TU_ACTIVE8,
            LARGECENTRIFUGE_TU_ACTIVE9
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
        mIcon = GregTech_API.sBlockIcons.registerIcon(RES_PATH_BLOCK + "iconsets/" + this);
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
            mIcon = GregTech_API.sBlockIcons.registerIcon(RES_PATH_BLOCK + mIconName);
        }

        @Override
        public ResourceLocation getTextureFile() {
            return TextureMap.locationBlocksTexture;
        }
    }
}}

