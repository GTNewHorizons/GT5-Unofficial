package gregtech.common.blocks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.ShapeBlock;

import appeng.api.parts.IFacadeControl;
import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.IBlockWithTextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.material.GTMaterialIcons;
import gregtech.api.material.GTMaterialTextures;
import gregtech.api.material.MaterialUtils;
import gregtech.common.render.GTRendererBlock;

/// The block backing the `sheetmetal` MaterialLib shape: a decorative material block with no tile entity,
/// metadata equal to the material's global index. Harvested with a pickaxe at level 1, with the hardness and
/// resistance of vanilla's iron block, and rendered in both the opaque and alpha-blended passes. AE2 is denied
/// facades of it through [appeng.api.parts.IFacadeControl].
public class SheetmetalShapeBlock extends ShapeBlock implements IBlockWithTextures, IFacadeControl {

    private final Map<Integer, ITexture[][]> texturesByIndex = new ConcurrentHashMap<>();

    public SheetmetalShapeBlock(String name, String displayNameFormat, String... oreDicts) {
        super("gregtech", name, displayNameFormat, oreDicts);
    }

    @Override
    public float getBlockHardness(World world, int x, int y, int z) {
        return Blocks.iron_block.getBlockHardness(world, x, y, z);
    }

    @Override
    public float getExplosionResistance(Entity exploder) {
        return Blocks.iron_block.getExplosionResistance(exploder);
    }

    @Override
    public String getHarvestTool(int meta) {
        return "pickaxe";
    }

    @Override
    public int getHarvestLevel(int meta) {
        return 1;
    }

    @Override
    public int getRenderType() {
        return GTRendererBlock.RENDER_ID;
    }

    @Override
    public boolean canRenderInPass(int pass) {
        return pass == 0 || pass == 1;
    }

    @Override
    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    public ITexture[][] getTextures(int meta) {
        return texturesByIndex.computeIfAbsent(meta, index -> {
            Material material = MaterialLibAPI.getMaterialByIndex(index);
            short[] rgba = MaterialUtils.rgba(material);
            if (material == null || rgba == null) return null;
            ITexture[] texture = {
                GTMaterialTextures.of(GTMaterialIcons.block(getName(), material), Dyes.getModulation(-1, rgba)) };
            return new ITexture[][] { texture, texture, texture, texture, texture, texture };
        });
    }

    @Override
    public boolean createFacadeForBlock(int meta) {
        return false;
    }
}
