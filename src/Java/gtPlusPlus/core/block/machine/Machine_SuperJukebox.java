package gtPlusPlus.core.block.machine;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.handler.GuiHandler;
import gtPlusPlus.core.inventories.Inventory_SuperJukebox;
import gtPlusPlus.core.util.math.MathUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockJukebox;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemRecord;
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
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, final float lx, final float ly, final float lz)
	{
		if (world.isRemote) {
			return true;
		}

		final TileEntity te = world.getTileEntity(x, y, z);
		if ((te != null) && (te instanceof TileEntitySuperJukebox)){
			player.openGui(GTplusplus.instance, GuiHandler.GUI14, world, x, y, z);
			return true;
		}
		return false;


		/* if (aWorld.getBlockMetadata(aX, aY, aZ) == 0)
        {
            return false;
        }
        else
        {
            this.func_149925_e(aWorld, aX, aY, aZ);
            return true;
        }*/
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
			if (tileentityjukebox != null && aStackToSet.getItem() instanceof ItemRecord) {
				tileentityjukebox.setCurrentRecord(aStackToSet.copy());
				//aWorld.setBlockMetadataWithNotify(aX, aY, aZ, 1, 2);
			}
		}
	}

	/**
	 * Function to handle playing of records.
	 */
	@Override
	public final void func_149925_e(World aWorld, int aX, int aY, int aZ) {
		playJukeboxRecord(aWorld, aX, aY, aZ);
	}

	public void playJukeboxRecord(World aWorld, int aX, int aY, int aZ) {
		if (!aWorld.isRemote) {
			TileEntitySuperJukebox tileentityjukebox = (TileEntitySuperJukebox) aWorld.getTileEntity(aX,
					aY, aZ);

			if (tileentityjukebox != null) {
				ItemStack itemstack = tileentityjukebox.func_145856_a();

				if (itemstack != null) {
					

					
					aWorld.playAuxSFX(1005, aX, aY, aZ, Item.getIdFromItem(itemstack.getItem()));
					//aWorld.playRecord((String) null, aX, aY, aZ);
					//tileentityjukebox.func_145857_a((ItemStack) null);
					//aWorld.setBlockMetadataWithNotify(aX, aY, aZ, 0, 2);
					/*float f = 0.7F;
					double d0 = (double) (aWorld.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
					double d1 = (double) (aWorld.rand.nextFloat() * f) + (double) (1.0F - f) * 0.2D + 0.6D;
					double d2 = (double) (aWorld.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
					ItemStack itemstack1 = itemstack.copy();
					EntityItem entityitem = new EntityItem(aWorld, (double) aX + d0,
							(double) aY + d1, (double) aZ + d2, itemstack1);
					entityitem.delayBeforeCanPickup = 10;
					aWorld.spawnEntityInWorld(entityitem);*/
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

	public static class TileEntitySuperJukebox extends TileEntityJukebox implements ISidedInventory {

		/** The number of players currently using this chest */
		public int numPlayersUsing;
		private ItemStack mCurrentlyPlayingStack;
		private final Inventory_SuperJukebox inventoryContents;
		private String customName;
		
		
		/*
		 * Important Data
		 */

		public int a_TEST_INT_VAR_1;
		public int a_TEST_INT_VAR_2;
		public int a_TEST_INT_VAR_3;
		public int a_TEST_INT_VAR_4;

		public boolean mIsPlaying = false;
		public boolean mIsLooping = false;
		public boolean a_TEST_BOOL_VAR_3;
		public boolean a_TEST_BOOL_VAR_4;
		
		

		public TileEntitySuperJukebox() {
			this.inventoryContents = new Inventory_SuperJukebox();
		}

		@Override
		public void readFromNBT(NBTTagCompound aNBT) {
			super.readFromNBT(aNBT);

			if (aNBT.hasKey("RecordItem", 10)) {
				this.func_145857_a(ItemStack.loadItemStackFromNBT(aNBT.getCompoundTag("RecordItem")));
			} else if (aNBT.getInteger("Record") > 0) {
				this.func_145857_a(new ItemStack(Item.getItemById(aNBT.getInteger("Record")), 1, 0));
			}

			this.inventoryContents.readFromNBT(aNBT.getCompoundTag("ContentsChest"));
			if (aNBT.hasKey("CustomName", 8)) {
				this.setCustomName(aNBT.getString("CustomName"));
			}
			
			mIsPlaying = aNBT.getBoolean("mIsPlaying");
			mIsLooping = aNBT.getBoolean("mIsLooping");
			
			
		}

		@Override
		public void writeToNBT(NBTTagCompound aNBT) {
			super.writeToNBT(aNBT);

			if (this.getCurrentRecord() != null) {
				aNBT.setTag("RecordItem", this.func_145856_a().writeToNBT(new NBTTagCompound()));
				aNBT.setInteger("Record", Item.getIdFromItem(this.func_145856_a().getItem()));
			}

			final NBTTagCompound chestData = new NBTTagCompound();
			this.inventoryContents.writeToNBT(chestData);
			aNBT.setTag("ContentsChest", chestData);
			if (this.hasCustomInventoryName()) {
				aNBT.setString("CustomName", this.getCustomName());
			}

			aNBT.setBoolean("mIsPlaying", mIsPlaying);
			aNBT.setBoolean("mIsLooping", mIsLooping);

		}

		/**
		 * Called to get the internal stack
		 */
		@Override
		public ItemStack func_145856_a() {
			return this.mCurrentlyPlayingStack;
		}

		/**
		 * Called to get the internal stack, wraps vanilla function
		 * {@link func_145856_a}.
		 */
		public ItemStack getCurrentRecord() {
			return func_145856_a();
		}

		/**
		 * Called to set the internal stack
		 */
		@Override
		public void func_145857_a(ItemStack p_145857_1_) {
			this.mCurrentlyPlayingStack = p_145857_1_;
			this.markDirty();
		}

		/**
		 * Called to set the internal stack, wraps vanilla function
		 * {@link func_145857_a}.
		 */
		public void setCurrentRecord(ItemStack aStack) {
			func_145857_a(aStack);
			this.markDirty();
		}

		public Inventory_SuperJukebox getInventory() {
			return this.inventoryContents;
		}





		public boolean playRecord(ItemStack aRecord) {



			return false;
		}

		public boolean stopRecord() {
			return openDiscDrive();
		}

		public void setLoopState(boolean isShufflingForever) {



		}


		//Play button pressed
		public boolean jukeboxLogicUpdate() {	
			
			if (this.worldObj.isRemote) {
				return true;
			}
			
			Logger.INFO("a");	
			if (this.mIsPlaying || this.mIsLooping) {
				return selectRecordToPlayFromInventoryAndSetViaVanillaHandler();
			}
			else {
				return stopRecord();
			}
		}


		//Determine which record to play
		public boolean selectRecordToPlayFromInventoryAndSetViaVanillaHandler() {			
			AutoMap<ItemStack> mValidRecords = new AutoMap<ItemStack>();			
			for (ItemStack g : this.getInventory().getInventory()) {
				if (g != null) {
					if (g.getItem() instanceof ItemRecord) {
						mValidRecords.put(g);
					}
				}
			}

			Logger.INFO("b1");
			//Select First Record
			ItemStack aRecordToPlay;
			if (mValidRecords.size() == 0) {
				Logger.INFO("bX");
				return false;
			}
			else {
				aRecordToPlay = mValidRecords.get(!mIsLooping ? 0 : MathUtils.randInt(0, (mValidRecords.size()-1)));
			}
			Logger.INFO("b2 - "+aRecordToPlay.getDisplayName());
			
			int aSlotCounter = 0;
			for (ItemStack g : this.getInventory().getInventory()) {
				if (g != null && aSlotCounter <= 17) {
					Logger.INFO("b3 - "+g.getDisplayName());
					if (GT_Utility.areStacksEqual(g, aRecordToPlay, true)) {
						IInventory aThisInv = this.getInventory();						
						if (aThisInv.getStackInSlot(20) != null) {
							openDiscDrive();
						}						
						
						GT_Utility.moveStackFromSlotAToSlotB(aThisInv, aThisInv, aSlotCounter, 20, (byte) 1, (byte) 1, (byte) 1, (byte) 1);
						setCurrentRecord(aThisInv.getStackInSlot(20));
						
						World aWorld = this.worldObj;
						int aX = this.xCoord;
						int aY = this.yCoord;
						int aZ = this.zCoord;			
						if (!aWorld.isRemote) {
							aRecordToPlay = this.func_145856_a();
							if (aRecordToPlay != null) {
								aWorld.playAuxSFX(1005, aX, aY, aZ, Item.getIdFromItem(aRecordToPlay.getItem()));
								this.markDirty();
								return true;
							}
						}				
						
						Logger.INFO("b++");
						this.markDirty();
						return false;
					}
				}
				aSlotCounter++;
			}			
			

			Logger.INFO("b4");
			this.markDirty();
			return false;
		}


		public boolean genericMethodThree(Object a1, Object a2, Object a3, Object a4) {			
			return false;
		}


		public void vanillaStopJukebox() {			
			World aWorld = this.worldObj;
			int aX = this.xCoord;
			int aY = this.yCoord;
			int aZ = this.zCoord;			
			if (!aWorld.isRemote) {
				TileEntitySuperJukebox tileentityjukebox = (TileEntitySuperJukebox) aWorld.getTileEntity(aX, aY, aZ);
				if (tileentityjukebox != null) {
					ItemStack aRecordToPlay = tileentityjukebox.func_145856_a();
					if (aRecordToPlay != null) {
						aWorld.playAuxSFX(1005, aX, aY, aZ, 0);
						aWorld.playRecord((String) null, aX, aY, aZ);
						tileentityjukebox.func_145857_a((ItemStack) null);
						this.markDirty();
					}
				}
			}
		}

		public boolean openDiscDrive() {			
			int aSlotCounter = 17;
			
			ItemStack g;
			
			for (int i = 17; i >= 0; i--) {
				g = this.getInventory().getInventory()[i];
				if (g == null && aSlotCounter >= 0) {
					IInventory aThisInv = this.getInventory();
					GT_Utility.moveStackFromSlotAToSlotB(aThisInv, aThisInv, 20, i, (byte) 1, (byte) 1, (byte) 1, (byte) 1);
					vanillaStopJukebox();
					Logger.INFO("b++");
					this.markDirty();
					return true;
				
			}
			}
			
			
			/*for (ItemStack g : this.getInventory().getInventory()) {
				if (g == null && aSlotCounter >= 0) {
						IInventory aThisInv = this.getInventory();
						GT_Utility.moveStackFromSlotAToSlotB(aThisInv, aThisInv, 20, aSlotCounter, (byte) 1, (byte) 1, (byte) 1, (byte) 1);
						vanillaStopJukebox();
						Logger.INFO("b++");
						return true;
					
				}
				aSlotCounter--;
			}	*/
			this.markDirty();
			return false;
		}










		public boolean anyPlayerInRange() {
			return this.worldObj.getClosestPlayer(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D,
					32) != null;
		}

		public NBTTagCompound getTag(final NBTTagCompound nbt, final String tag) {
			if (!nbt.hasKey(tag)) {
				nbt.setTag(tag, new NBTTagCompound());
			}
			return nbt.getCompoundTag(tag);
		}

		@Override
		public int getSizeInventory() {
			return this.getInventory().getSizeInventory()-3;
		}

		@Override
		public ItemStack getStackInSlot(final int slot) {
			return this.getInventory().getStackInSlot(slot);
		}

		@Override
		public ItemStack decrStackSize(final int slot, final int count) {
			return this.getInventory().decrStackSize(slot, count);
		}

		@Override
		public ItemStack getStackInSlotOnClosing(final int slot) {
			return this.getInventory().getStackInSlotOnClosing(slot);
		}

		@Override
		public void setInventorySlotContents(final int slot, final ItemStack stack) {
			this.getInventory().setInventorySlotContents(slot, stack);
		}

		@Override
		public int getInventoryStackLimit() {
			return 1;
		}

		@Override
		public boolean isUseableByPlayer(final EntityPlayer entityplayer) {
			return this.getInventory().isUseableByPlayer(entityplayer);
		}

		@Override
		public void openInventory() {
			if (this.numPlayersUsing < 0) {
				this.numPlayersUsing = 0;
			}
			if (!this.worldObj.isRemote) {
				this.numPlayersUsing++;
			}
			this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1,
					this.numPlayersUsing);
			this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
			this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord - 1, this.zCoord, this.getBlockType());
			this.getInventory().openInventory();
		}

		@Override
		public void closeInventory() {
			if (!this.worldObj.isRemote) {
				this.numPlayersUsing--;
			}
			this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1,
					this.numPlayersUsing);
			this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
			this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord - 1, this.zCoord, this.getBlockType());
			this.getInventory().closeInventory();
		}

		@Override
		public boolean isItemValidForSlot(final int slot, final ItemStack itemstack) {			
			if (slot >= 18) {
				return false;
			}			
			return this.getInventory().isItemValidForSlot(slot, itemstack);
		}

		@Override
		public int[] getAccessibleSlotsFromSide(final int p_94128_1_) {
			final int[] accessibleSides = new int[this.getSizeInventory()];
			for (int r = 0; r < this.getInventory().getSizeInventory(); r++) {
				accessibleSides[r] = r;
			}
			return accessibleSides;

		}

		@Override
		public boolean canInsertItem(final int p_102007_1_, final ItemStack p_102007_2_, final int p_102007_3_) {
			if (p_102007_1_ >= 18) {
				return false;
			}
			return this.getInventory().isItemValidForSlot(p_102007_1_, p_102007_2_);
		}

		@Override
		public boolean canExtractItem(final int p_102008_1_, final ItemStack p_102008_2_, final int p_102008_3_) {			
			if (p_102008_1_ >= 18) {
				return false;
			}			
			return this.getInventory().isItemValidForSlot(p_102008_1_, p_102008_2_);
		}

		public String getCustomName() {
			return this.customName;
		}

		public void setCustomName(final String customName) {
			this.customName = customName;
		}

		@Override
		public String getInventoryName() {
			return this.hasCustomInventoryName() ? this.customName : "container.SuperJukebox";
		}

		@Override
		public boolean hasCustomInventoryName() {
			return (this.customName != null) && !this.customName.equals("");
		}

	}
}