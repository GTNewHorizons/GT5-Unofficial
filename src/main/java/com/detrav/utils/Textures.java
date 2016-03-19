package com.detrav.utils;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.IIconContainer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

/**
 * Created by wital_000 on 19.03.2016.
 */
public class Textures {

    public static void load() {
    }

    public enum ItemIcons implements IIconContainer, Runnable {
        VOID,RENDERING_ERROR,PRO_PICK_HEAD;



        protected IIcon mIcon, mOverlay;

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

        private ItemIcons() {
            GregTech_API.sGTItemIconload.add(this);
        }


        @Override
        public void run() {
            mIcon = GregTech_API.sItemIcons.registerIcon("gregtech:iconsets/"  + this);
            mOverlay = GregTech_API.sItemIcons.registerIcon("gregtech:iconsets/" + this + "_OVERLAY");
        }
    }
}
