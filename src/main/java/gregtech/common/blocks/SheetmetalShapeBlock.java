package gregtech.common.blocks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.ShapeBlock;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TextureSet;
import gregtech.api.interfaces.IBlockWithTextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.material.MU;
import gregtech.api.render.TextureFactory;
import gregtech.common.render.GTRendererBlock;

/// The block backing the `sheetmetal` MaterialLib shape: a decorative material block with no tile entity,
/// metadata equal to the material's global index. Ports the legacy `gregtech.common.blocks.BlockSheetMetal`
/// behavior (inherited there from `BlockStorage`): pickaxe harvesting at level 1, hardness/resistance matching
/// vanilla's iron block, and rendering in both the opaque and alpha-blended passes.
public class SheetmetalShapeBlock extends ShapeBlock implements IBlockWithTextures {

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
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        TextureSet textureSet = MU.iconSet(MaterialLibAPI.getMaterialByIndex(meta));
        if (textureSet == null) return super.getIcon(side, meta);
        return textureSet.mTextures[OrePrefixes.sheetmetal.getTextureIndex()].getIcon();
    }

    @Override
    public ITexture[][] getTextures(int meta) {
        return texturesByIndex.computeIfAbsent(meta, index -> {
            Material material = MaterialLibAPI.getMaterialByIndex(index);
            TextureSet textureSet = MU.iconSet(material);
            short[] rgba = MU.rgba(material);
            if (textureSet == null || rgba == null) return null;
            ITexture[] texture = { TextureFactory
                .of(textureSet.mTextures[OrePrefixes.sheetmetal.getTextureIndex()], Dyes.getModulation(-1, rgba)) };
            return new ITexture[][] { texture, texture, texture, texture, texture, texture };
        });
    }
}
