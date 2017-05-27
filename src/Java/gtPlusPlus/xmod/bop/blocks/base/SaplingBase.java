package gtPlusPlus.xmod.bop.blocks.base;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.*;

public class SaplingBase extends BlockSapling
{
	protected String[] saplingTypes = new String[] {};
    protected IIcon[] saplingTextures = new IIcon[] {};

    //Sapling types - field_149882_a
    //Iicons - field_149881_b
    
    protected SaplingBase(String blockNameLocalized, String blockNameUnlocalized, String[] saplingTypes){
        float f = 0.4F;
        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
        this.saplingTypes = saplingTypes;
        this.saplingTextures = new IIcon[saplingTypes.length];
        String blockName = "block"+Utils.sanitizeString(blockNameLocalized)+"Sapling";
		GameRegistry.registerBlock(this, ItemBlock.class, blockName);
		this.setBlockName(blockName);
		ItemUtils.addItemToOreDictionary(ItemUtils.getSimpleStack(this), "treeSapling");
		this.setCreativeTab(AddToCreativeTab.tabBOP);
		LanguageRegistry.addName(this, blockNameLocalized);
    }
    
    private final void setVanillaVariable(Object toSet, Object value){
		toSet = value;
	}

    /**
     * Gets the block's texture. Args: side, meta
     */
    @Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(int someInt, int meta){
       /* p_149691_2_ &= 7;
        return saplingTextures[MathHelper.clamp_int(p_149691_2_, 0, 5)];*/
        //return this.saplingTextures[meta % this.saplingTextures.length];
    	Utils.LOG_INFO("Sapling meta is "+meta);
      return this.saplingTextures[meta];        
    }
    
    

   /* @Override
	public void func_149879_c(World p_149879_1_, int p_149879_2_, int p_149879_3_, int p_149879_4_, Random p_149879_5_)
    {
        int l = p_149879_1_.getBlockMetadata(p_149879_2_, p_149879_3_, p_149879_4_);

        if ((l & 8) == 0)
        {
            p_149879_1_.setBlockMetadataWithNotify(p_149879_2_, p_149879_3_, p_149879_4_, l | 8, 4);
        }
        else
        {
            this.func_149878_d(p_149879_1_, p_149879_2_, p_149879_3_, p_149879_4_, p_149879_5_);
        }
    }*/

    @Override
	public void func_149878_d(World world, int x, int y, int z, Random rand)
    {
        if (!net.minecraftforge.event.terraingen.TerrainGen.saplingGrowTree(world, rand, x, y, z)) return;
        int l = world.getBlockMetadata(x, y, z) & 7;
        Object object = rand.nextInt(10) == 0 ? new WorldGenBigTree(true) : new WorldGenTrees(true);
        int i1 = 0;
        int j1 = 0;
        boolean flag = false;

        switch (l)
        {
            case 0:
            default:
                break;
            case 1:
                label78:

                for (i1 = 0; i1 >= -1; --i1)
                {
                    for (j1 = 0; j1 >= -1; --j1)
                    {
                        if (this.func_149880_a(world, x + i1, y, z + j1, 1) && this.func_149880_a(world, x + i1 + 1, y, z + j1, 1) && this.func_149880_a(world, x + i1, y, z + j1 + 1, 1) && this.func_149880_a(world, x + i1 + 1, y, z + j1 + 1, 1))
                        {
                            object = new WorldGenMegaPineTree(false, rand.nextBoolean());
                            flag = true;
                            break label78;
                        }
                    }
                }

                if (!flag)
                {
                    j1 = 0;
                    i1 = 0;
                    object = new WorldGenTaiga2(true);
                }

                break;
            case 2:
                object = new WorldGenForest(true, false);
                break;
            case 3:
                label93:

                for (i1 = 0; i1 >= -1; --i1)
                {
                    for (j1 = 0; j1 >= -1; --j1)
                    {
                        if (this.func_149880_a(world, x + i1, y, z + j1, 3) && this.func_149880_a(world, x + i1 + 1, y, z + j1, 3) && this.func_149880_a(world, x + i1, y, z + j1 + 1, 3) && this.func_149880_a(world, x + i1 + 1, y, z + j1 + 1, 3))
                        {
                            object = new WorldGenMegaJungle(true, 10, 20, 3, 3);
                            flag = true;
                            break label93;
                        }
                    }
                }

                if (!flag)
                {
                    j1 = 0;
                    i1 = 0;
                    object = new WorldGenTrees(true, 4 + rand.nextInt(7), 3, 3, false);
                }

                break;
            case 4:
                object = new WorldGenSavannaTree(true);
                break;
            case 5:
                label108:

                for (i1 = 0; i1 >= -1; --i1)
                {
                    for (j1 = 0; j1 >= -1; --j1)
                    {
                        if (this.func_149880_a(world, x + i1, y, z + j1, 5) && this.func_149880_a(world, x + i1 + 1, y, z + j1, 5) && this.func_149880_a(world, x + i1, y, z + j1 + 1, 5) && this.func_149880_a(world, x + i1 + 1, y, z + j1 + 1, 5))
                        {
                            object = new WorldGenCanopyTree(true);
                            flag = true;
                            break label108;
                        }
                    }
                }

                if (!flag)
                {
                    return;
                }
        }

        Block block = Blocks.air;

        if (flag)
        {
            world.setBlock(x + i1, y, z + j1, block, 0, 4);
            world.setBlock(x + i1 + 1, y, z + j1, block, 0, 4);
            world.setBlock(x + i1, y, z + j1 + 1, block, 0, 4);
            world.setBlock(x + i1 + 1, y, z + j1 + 1, block, 0, 4);
        }
        else
        {
            world.setBlock(x, y, z, block, 0, 4);
        }

        if (!((WorldGenerator)object).generate(world, rand, x + i1, y, z + j1))
        {
            if (flag)
            {
                world.setBlock(x + i1, y, z + j1, this, l, 4);
                world.setBlock(x + i1 + 1, y, z + j1, this, l, 4);
                world.setBlock(x + i1, y, z + j1 + 1, this, l, 4);
                world.setBlock(x + i1 + 1, y, z + j1 + 1, this, l, 4);
            }
            else
            {
                world.setBlock(x, y, z, this, l, 4);
            }
        }
    }

    @Override
	public boolean func_149880_a(World world, int p_149880_2_, int p_149880_3_, int p_149880_4_, int p_149880_5_){
        return world.getBlock(p_149880_2_, p_149880_3_, p_149880_4_) == this && (world.getBlockMetadata(p_149880_2_, p_149880_3_, p_149880_4_) & 7) == p_149880_5_;
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    @Override
	public int damageDropped(int meta){
        return MathHelper.clamp_int(meta & 7, 0, 5);
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    @Override
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List metaList){
		for (int i = 0; i < this.saplingTextures.length; ++i){
			metaList.add(new ItemStack(item, 1, i));
		}
	}

    @Override
	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iIcon){
        for (int i = 0; i < saplingTextures.length; ++i){
            saplingTextures[i] = iIcon.registerIcon(CORE.MODID + ":" + "trees/" + "saplings/" + "sapling_" + saplingTypes[i]);
        }
    }

}