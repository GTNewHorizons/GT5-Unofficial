package goodgenerator.blocks.regularBlock;

import goodgenerator.main.GoodGenerator;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.common.render.GTRendererCasing;

public class BlockTurbineCasing extends BlockCasing {

    public IIconContainer base;

    public BlockTurbineCasing(String name, String texture) {
        super(name, new String[] { GoodGenerator.MOD_ID + ":" + texture });
        base = new Textures.BlockIcons.CustomIcon("icons/" + texture);
    }

    @Override
    public int getRenderType() {
        return GTRendererCasing.mRenderID;
    }
}
