package gtPlusPlus.core.block.machine;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockJukebox;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class Machine_SuperJukebox extends BlockJukebox
{
    @SideOnly(Side.CLIENT)
    private IIcon mIcon;

    public Machine_SuperJukebox(){
		this.setBlockName("blockSuperJukebox");
		this.setCreativeTab(CreativeTabs.tabRedstone);		
		setHardness(2.0F);
		setResistance(10.0F);
		setStepSound(soundTypePiston);
		setBlockTextureName("jukebox");		
		GameRegistry.registerBlock(this, "blockSuperJukebox");
		LanguageRegistry.addName(this, "Sir Mixalot [Jukebox]");
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int aSide, int aMeta)
    {
        return aSide == 1 ? this.mIcon : this.blockIcon;
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    @Override
    public boolean onBlockActivated(World aWorld, int aX, int aY, int aZ, EntityPlayer aPlayer, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
        if (aWorld.getBlockMetadata(aX, aY, aZ) == 0)
        {
            return false;
        }
        else
        {
            this.func_149925_e(aWorld, aX, aY, aZ);
            return true;
        }
    }

    /**
     * Set the record in the {@link SuperJukebox} {@link TileEntity}.
     */
    @Override
    public final void func_149926_b(World aWorld, int aX, int aY, int aZ, ItemStack aStackToSet) {
    	setRecordInJukeBox(aWorld, aX, aY, aZ, aStackToSet);
    }
    
	public void setRecordInJukeBox(World aWorld, int aX, int aY, int aZ, ItemStack aStackToSet) {
		if (!aWorld.isRemote) {
			TileEntitySuperJukebox tileentityjukebox = (TileEntitySuperJukebox) aWorld.getTileEntity(aX, aY, aZ);
			if (tileentityjukebox != null) {
				tileentityjukebox.func_145857_a(aStackToSet.copy());
				aWorld.setBlockMetadataWithNotify(aX, aY, aZ, 1, 2);
			}
		}
	}

	/**
	 * Function to handle playing of records.
	 */
    @Override
    public final void func_149925_e(World aWorld, int aX, int aY, int aZ) {
    	playerJukeboxRecord(aWorld, aX, aY, aZ);
	}
    
    public void playerJukeboxRecord(World aWorld, int aX, int aY, int aZ) {
    	if (!aWorld.isRemote) {
			TileEntitySuperJukebox tileentityjukebox = (TileEntitySuperJukebox) aWorld.getTileEntity(aX,
					aY, aZ);

			if (tileentityjukebox != null) {
				ItemStack itemstack = tileentityjukebox.func_145856_a();

				if (itemstack != null) {
					aWorld.playAuxSFX(1005, aX, aY, aZ, 0);
					aWorld.playRecord((String) null, aX, aY, aZ);
					tileentityjukebox.func_145857_a((ItemStack) null);
					aWorld.setBlockMetadataWithNotify(aX, aY, aZ, 0, 2);
					float f = 0.7F;
					double d0 = (double) (aWorld.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
					double d1 = (double) (aWorld.rand.nextFloat() * f) + (double) (1.0F - f) * 0.2D + 0.6D;
					double d2 = (double) (aWorld.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
					ItemStack itemstack1 = itemstack.copy();
					EntityItem entityitem = new EntityItem(aWorld, (double) aX + d0,
							(double) aY + d1, (double) aZ + d2, itemstack1);
					entityitem.delayBeforeCanPickup = 10;
					aWorld.spawnEntityInWorld(entityitem);
				}
			}
		}
    }

    @Override
    public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_, int p_149749_6_)
    {
        this.func_149925_e(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_);
        super.breakBlock(p_149749_1_, p_149749_2_, p_149749_3_, p_149749_4_, p_149749_5_, p_149749_6_);
    }

    /**
     * Drops the block items with a specified chance of dropping the specified items
     */
    @Override
    public void dropBlockAsItemWithChance(World p_149690_1_, int p_149690_2_, int p_149690_3_, int p_149690_4_, int p_149690_5_, float p_149690_6_, int p_149690_7_)
    {
        if (!p_149690_1_.isRemote)
        {
            super.dropBlockAsItemWithChance(p_149690_1_, p_149690_2_, p_149690_3_, p_149690_4_, p_149690_5_, p_149690_6_, 0);
        }
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
    {
        return new TileEntitySuperJukebox();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
        this.blockIcon = p_149651_1_.registerIcon(this.getTextureName() + "_side");
        this.mIcon = p_149651_1_.registerIcon(this.getTextureName() + "_top");
    }

    public static class TileEntitySuperJukebox extends TileEntityJukebox {
    	
            private ItemStack field_145858_a;

            @Override
            public void readFromNBT(NBTTagCompound aNBT)
            {
                super.readFromNBT(aNBT);

                if (aNBT.hasKey("RecordItem", 10))
                {
                    this.func_145857_a(ItemStack.loadItemStackFromNBT(aNBT.getCompoundTag("RecordItem")));
                }
                else if (aNBT.getInteger("Record") > 0)
                {
                    this.func_145857_a(new ItemStack(Item.getItemById(aNBT.getInteger("Record")), 1, 0));
                }
            }

            @Override
            public void writeToNBT(NBTTagCompound aNBT)
            {
                super.writeToNBT(aNBT);

                if (this.func_145856_a() != null)
                {
                    aNBT.setTag("RecordItem", this.func_145856_a().writeToNBT(new NBTTagCompound()));
                    aNBT.setInteger("Record", Item.getIdFromItem(this.func_145856_a().getItem()));
                }
            }

            /**
             * Called to get the internal stack
             */
            @Override
            public ItemStack func_145856_a()
            {
                return this.field_145858_a;
            }

            /**
             * Called to get the internal stack, wraps vanilla function {@link func_145856_a}.
             */
            public ItemStack getCurrentRecord() {
            	return func_145856_a();
            }

            /**
             * Called to set the internal stack
             */
            @Override
            public void func_145857_a(ItemStack p_145857_1_)
            {
                this.field_145858_a = p_145857_1_;
                this.markDirty();
            }

            /**
             * Called to set the internal stack, wraps vanilla function {@link func_145857_a}.
             */
            public void setCurrentRecord(ItemStack aStack) {
            	func_145857_a(aStack);
            }
        }
}