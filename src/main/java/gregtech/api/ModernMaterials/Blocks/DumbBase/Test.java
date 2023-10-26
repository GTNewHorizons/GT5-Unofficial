package gregtech.api.ModernMaterials.Blocks.DumbBase;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import gregtech.api.ModernMaterials.Blocks.DumbBase.BaseMaterialBlock.BaseMaterialBlock;
import gregtech.api.ModernMaterials.Blocks.DumbBase.BaseMaterialBlock.BaseMaterialItemBlock;
import gregtech.api.ModernMaterials.Blocks.DumbBase.BaseMaterialBlock.BaseMaterialTileEntity;

public abstract class Test {

    public abstract Class<? extends BaseMaterialBlock> getBlock();
    public abstract Class<? extends BaseMaterialItemBlock> getItemBlock();
    public abstract Class<? extends BaseMaterialTileEntity> getTileEntity();

    public abstract ISimpleBlockRenderingHandler getSimpleRenderer();

}
