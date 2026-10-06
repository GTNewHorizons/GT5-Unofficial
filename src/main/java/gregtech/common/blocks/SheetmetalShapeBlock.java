package gregtech.common.blocks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.ShapeBlock;

import appeng.api.parts.IFacadeControl;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.IBlockWithTextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.material.GTMaterialIcons;
import gregtech.api.material.GTMaterialTextures;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.client.DynamicLangManager;
import gregtech.common.render.GTRendererBlock;

/// The `sheetmetal` MaterialLib shape block, a decorative multiblock structure part. Metadata is the material
/// index. AE2 cannot make facades of it.
public class SheetmetalShapeBlock extends ShapeBlock implements IBlockWithTextures, IFacadeControl {

    private final Map<Integer, ITexture[][]> texturesByIndex = new ConcurrentHashMap<>();

    public SheetmetalShapeBlock(String name, String displayNameFormat, String... oreDicts) {
        super("gregtech", name, displayNameFormat, oreDicts);
        GregTechAPI.registerMachineBlock(this, -1);
    }

    /// Registers each served material's stack with [DynamicLangManager] so its runtime display name gets a lang
    /// key.
    @Override
    public void bindServedMaterials(Material[] materials) {
        super.bindServedMaterials(materials);
        for (Material material : materials) {
            DynamicLangManager.addStack(getStack(material, 1));
        }
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        GregTechAPI.causeMachineUpdate(world, x, y, z);
        super.onBlockAdded(world, x, y, z);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        GregTechAPI.causeMachineUpdate(world, x, y, z);
        super.breakBlock(world, x, y, z, block, meta);
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
            ITexture[] texture = { GTMaterialTextures.stored(
                GTMaterialIcons.block(getName(), material),
                () -> Dyes.getModulation(-1, MaterialUtils.rgba(material)),
                false) };
            return new ITexture[][] { texture, texture, texture, texture, texture, texture };
        });
    }

    @Override
    public boolean createFacadeForBlock(int meta) {
        return false;
    }
}
