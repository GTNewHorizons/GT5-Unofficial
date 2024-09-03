package gtPlusPlus.xmod.gregtech.common.blocks.textures;

import static gregtech.api.enums.Mods.GTPlusPlus;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.IIconContainer;
import gtPlusPlus.api.objects.Logger;

public final class TexturesGtTools {

    public static final CustomIcon ANGLE_GRINDER = new CustomIcon("iconsets/ANGLE_GRINDER");
    public static final CustomIcon ELECTRIC_SNIPS = new CustomIcon("iconsets/ELECTRIC_SNIPS");

    public static final class CustomIcon implements IIconContainer, Runnable {

        private IIcon mIcon, mOverlay;
        private final String mIconName;

        public CustomIcon(final String aIconName) {
            this.mIconName = aIconName;
            Logger.INFO("Constructing a Custom Texture. " + this.mIconName);
            GregTechAPI.sGTItemIconload.add(this);
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
            this.mIcon = GregTechAPI.sItemIcons.registerIcon(GTPlusPlus.ID + ":" + this.mIconName);
            // Utils.LOG_INFO("Registering a Custom Texture. "+mIcon.g);
            this.mOverlay = GregTechAPI.sItemIcons.registerIcon(GTPlusPlus.ID + ":" + this.mIconName + "_OVERLAY");
        }

        @Override
        public ResourceLocation getTextureFile() {
            return TextureMap.locationItemsTexture;
        }
    }
}
