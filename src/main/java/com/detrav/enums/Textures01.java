package com.detrav.enums;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

/**
 * Created by wital_000 on 19.03.2016.
 */
public class Textures01 {
    public static final IIconContainer[] mTextures = new IIconContainer[]
            {
                    new Textures.ItemIcons.CustomIcon("gt.detrav.metaitem.01/PRO_PICK_HEAD"),
                    new Textures.ItemIcons.CustomIcon("gt.detrav.metatool.01/ELECTRIC_LV_PRO_PICK_HEAD"),
                    new Textures.ItemIcons.CustomIcon("gt.detrav.metatool.01/ELECTRIC_MV_PRO_PICK_HEAD"),
                    new Textures.ItemIcons.CustomIcon("gt.detrav.metatool.01/ELECTRIC_HV_PRO_PICK_HEAD"),
                    new Textures.ItemIcons.CustomIcon("gt.detrav.metatool.01/PORTABE_CHARGER"),
                    new Textures.ItemIcons.CustomIcon("gt.detrav.metatool.01/PORTABE_CHARGER_BORDER")
            };
}