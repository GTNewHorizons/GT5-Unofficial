package com.detrav.proxies;

import com.detrav.enums.Textures01;
import gregtech.api.enums.Textures;

/**
 * Created by wital_000 on 19.03.2016.
 */
public class ClientProxy extends CommonProxy {

    public ClientProxy()
    {
        int test = Textures01.mTextures.length;
    }

    @Override
    public void onPostLoad() {
        super.onPostLoad();
        //Textures.ItemIcons.CustomIcon test = new Textures.ItemIcons.CustomIcon("iconsets/PRO_PICK_HEAD");
        //test.run();

    }
    @Override
    public void onLoad()
    {
        super.onLoad();
    }
}
