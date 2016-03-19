package com.detrav.proxies;

import com.detrav.tools.DetravRenderItemMetaGenerated;
import com.detrav.utils.Textures;

/**
 * Created by wital_000 on 19.03.2016.
 */
public class ClientProxy extends CommonProxy {
    @Override
    public void onPostLoad()
    {
        Textures.load();
    }
    @Override
    public void onLoad()
    {
        new DetravRenderItemMetaGenerated();
    }
}
