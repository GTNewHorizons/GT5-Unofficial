package gregtech.common.blocks.rubbertree;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockSapling;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.TerrainGen;

import org.jetbrains.annotations.NotNull;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;

public class BlockRubberSapling extends BlockSapling {

    public static final String NAME = "gt.block_rubber_sapling";

    private IIcon saplingIcon;

    public BlockRubberSapling() {
        super();

        setBlockName(NAME);
        setCreativeTab(GregTechAPI.TAB_GREGTECH);
        setStepSound(soundTypeGrass);
        setHardness(0.0F);

        GameRegistry.registerBlock(this, ItemBlockRubberSapling.class, NAME);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(@NotNull IIconRegister iconRegister) {
        this.saplingIcon = iconRegister.registerIcon("gregtech:rubbertree/rubber_sapling");
        this.blockIcon = this.saplingIcon;
    }

    @Override
    public String getUnlocalizedName() {
        return NAME;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return this.saplingIcon;
    }

    @Override
    public int damageDropped(int meta) {
        return 0;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, @NotNull List list) {
        list.add(new ItemStack(item, 1, 0));
    }

    @Override
    public void func_149878_d(World world, int x, int y, int z, Random random) {
        if (!TerrainGen.saplingGrowTree(world, random, x, y, z)) {
            return;
        }

        WorldGenerator generator = new RubberTreeWorldGenerator(true);

        // First, the sapling is removed, just like vanilla
        world.setBlockToAir(x, y, z);

        if (!generator.generate(world, random, x, y, z)) {
            // If the generation fails, we'll put the sapling back in
            world.setBlock(x, y, z, this, 0, 3);
        }
    }
}
