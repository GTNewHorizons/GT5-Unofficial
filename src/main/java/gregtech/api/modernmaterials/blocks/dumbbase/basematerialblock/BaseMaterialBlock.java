package gregtech.api.modernmaterials.blocks.dumbbase.basematerialblock;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import gregtech.api.modernmaterials.blocks.registration.BlocksEnum;
import gregtech.api.modernmaterials.ModernMaterial;

public abstract class BaseMaterialBlock extends Block {

    private final BlocksEnum blockEnum;
    private final HashSet<ModernMaterial> validMaterials;

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(@NotNull World world, int metadata) {

        final ModernMaterial material = ModernMaterial.getMaterialFromID(metadata);

        if (!getBlockEnum().getSpecialBlockRenderAssociatedMaterials()
            .contains(material)) return null;

        try {
            return getBlockEnum().getTileEntityClass()
                .getDeclaredConstructor()
                .newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException
            | InvocationTargetException e) {
            throw new RuntimeException("Failed to instantiate tile entity.", e);
        }
    }

    @Override
    public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z) {
        return false;
    }

    public BaseMaterialBlock(BlocksEnum blockEnum, HashSet<ModernMaterial> validMaterials) {
        super(Material.rock);

        setHardness(1.5F);
        setResistance(10.0F);

        this.blockEnum = blockEnum;
        this.validMaterials = validMaterials;
    }

    public BlocksEnum getBlockEnum() {
        return blockEnum;
    }

    @Override
    public int getRenderType() {
        return getBlockEnum().getRenderId();
    }

    @Override
    public int getRenderBlockPass() {
        return 0;
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (ModernMaterial material : validMaterials) {
            list.add(new ItemStack(item, 1, material.getMaterialID()));
        }
    }

    @Override
    public int damageDropped(int meta)
    {
        return meta;
    }
}
