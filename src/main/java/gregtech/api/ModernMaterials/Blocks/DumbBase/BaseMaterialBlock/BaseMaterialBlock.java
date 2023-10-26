package gregtech.api.ModernMaterials.Blocks.DumbBase.BaseMaterialBlock;

import java.lang.reflect.InvocationTargetException;
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

import gregtech.api.ModernMaterials.Blocks.Registration.BlocksEnum;
import gregtech.api.ModernMaterials.ModernMaterial;
import gregtech.api.ModernMaterials.ModernMaterialUtilities;

public abstract class BaseMaterialBlock extends Block {

    List<Integer> validIDs;

    private final int blockIDOffset;

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(@NotNull World world, int metadata) {

        final int ID = getMaterialID(metadata);
        final ModernMaterial material = ModernMaterialUtilities.getMaterialFromID(ID);

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

    public BaseMaterialBlock(int blockIDOffset, List<Integer> validIDs) {
        super(Material.rock);

        setHardness(1.5F);
        setResistance(10.0F);

        this.validIDs = validIDs;
        this.blockIDOffset = blockIDOffset;
    }

    public abstract BlocksEnum getBlockEnum();

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
        for (int ID : validIDs) {
            list.add(new ItemStack(item, 1, ID % 16));
        }
    }

    public int getMaterialID(int metadata) {
        return blockIDOffset * 16 + metadata;
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }
}
