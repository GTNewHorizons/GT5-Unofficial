package com.detrav.tools;

import com.detrav.utils.DetravMetaGeneratedItem;
import com.detrav.utils.Textures;
import gregtech.api.enums.TC_Aspects;

/**
 * Created by wital_000 on 19.03.2016.
 */
public class DetravMetaGeneratedItem01 extends DetravMetaGeneratedItem {

    public static DetravMetaGeneratedItem01 INSTANCE;

    public DetravMetaGeneratedItem01() {
        super("detrav.meta.item.01");
        INSTANCE = this;
        this.addItem(1,"ProPick Head",null, new Object[] {new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 4L)});
        //this.getIco
        mIconList[1][1] = Textures.ItemIcons.PRO_PICK_HEAD.getIcon();
    }
}
