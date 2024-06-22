package gregtech.api.multitileentity;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.StatCollector;

import gregtech.common.render.GT_MultiTile_Renderer;

/*
 * The internal block used in the registry for lookup.
 * - Not the same as the block spawned with the MuTE to allow for multiple different types of blocks/materials
 * in each registry.
 */
public class MultiTileEntityBlockRegistryInternal extends Block {

    public MultiTileEntityRegistry registry;

    public MultiTileEntityBlockRegistryInternal() {
        super(Material.anvil);
    }

    @Override
    public void registerBlockIcons(IIconRegister aIconRegister) {
        /* Do Nothing */
    }

    @Override
    public int getRenderType() {
        return GT_MultiTile_Renderer.INSTANCE == null ? super.getRenderType()
            : GT_MultiTile_Renderer.INSTANCE.getRenderId();
    }

    @Override
    public final String getUnlocalizedName() {
        return registry.getInternalName();
    }

    @Override
    public final String getLocalizedName() {
        return StatCollector.translateToLocal(registry.getInternalName() + ".name");
    }

    public MultiTileEntityRegistry getRegistry() {
        return registry;
    }
}
