package goodgenerator.blocks.regularBlock;

import gregtech.api.enums.Mods;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.common.render.GTRendererCasing;

public class BlockTurbineCasing extends BlockCasing {

    public IIconContainer base;

    public BlockTurbineCasing(String name, String texture) {
        super(name, new String[] { Mods.ModIDs.GOOD_GENERATOR + ":" + texture });
        base = Textures.BlockIcons.custom("icons/" + texture);
    }

    @Override
    public int getRenderType() {
        return GTRendererCasing.mRenderID;
    }
}
