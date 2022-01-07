package gtPlusPlus.core.block.general;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockSuperLight extends BlockContainer {

	@SideOnly(Side.CLIENT)
	private IIcon textureFront;
	
	//propecia (Inhibit DHD - recover hair get depression)
	
	public BlockSuperLight() {
		super(Material.circuits);
		this.setBlockName("blockSuperLight");
		this.setCreativeTab(CreativeTabs.tabRedstone);
		GameRegistry.registerBlock(this, "blockSuperLight");
		LanguageRegistry.addName(this, "Shining Star");
	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(final int p_149691_1_, final int p_149691_2_) {
		return this.blockIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(final IIconRegister p_149651_1_) {
		this.blockIcon = p_149651_1_.registerIcon(CORE.MODID + ":" + "SwirlBigBlue");
	}

	/**
	 * Returns a new instance of a block's tile entity class. Called on placing the
	 * block.
	 */
	public TileEntity createNewTileEntity(World aWorld, int p_149915_2_) {
		return new TileEntitySuperLight();
	}

	public static class TileEntitySuperLight extends TileEntity {

		private long mCreated;
		
		private long mLastUpdateTick = 0;
		
		private int mLitBlockCount = 0;
		
		private int[][][][] aLitBlocks = new int[50][10][50][1];
		
		private boolean mPowered = false;
		
		public TileEntitySuperLight() {		
			mCreated = System.currentTimeMillis();
			Logger.INFO("Created Super-Lamp");
		}
		
		public void readFromNBT(NBTTagCompound aNBT) {
			super.readFromNBT(aNBT);
			mCreated = aNBT.getLong("mCreated");	
			mPowered = aNBT.getBoolean("mPowered");			
			NBTTagCompound aLightingData = aNBT.getCompoundTag("lighting");			
			for (int x = 0; x < 50; x++) {
				for (int y = 0; y < 10; y++) {
					for (int z = 0; z < 50; z++) {						
						int aData = aLightingData.getInteger("["+x+"]["+y+"]["+z+"]");
						aLitBlocks[x][y][z][0] = aData;
					}
				}
			}			
		}

		public void writeToNBT(NBTTagCompound aNBT) {
			super.writeToNBT(aNBT);
			aNBT.setLong("mCreated", mCreated);
			aNBT.setBoolean("mPowered", mPowered);
			NBTTagCompound aLightingData = new NBTTagCompound();
			for (int x = 0; x < 50; x++) {
				for (int y = 0; y < 10; y++) {
					for (int z = 0; z < 50; z++) {						
						int aFlag = aLitBlocks[x][y][z][0];						
						aLightingData.setInteger("["+x+"]["+y+"]["+z+"]", aFlag);
					}
				}
			}
			aNBT.setTag("lighting", aLightingData);			
		}

		@Override
		public void updateEntity() {
			super.updateEntity();
			
			if (this.worldObj.isRemote) {
				return;
			}
			
			try {
				if (mLastUpdateTick == 0 || (System.currentTimeMillis() - mLastUpdateTick) >= 30000) {
					boolean powered = (this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord));
					boolean aLastState = mPowered;
					//Logger.INFO("Powered: "+powered);
					mPowered = powered;
					if (mPowered != aLastState) {
						updateLighting(powered);
					}
				}
			} catch (Throwable t) {
			}
		}

		@Override
		public void markDirty() {
			super.markDirty();
		}

		@Override
		public boolean canUpdate() {
			return super.canUpdate();
		}
		
		public void updateLighting(boolean enable) {
			
			
			mLastUpdateTick = System.currentTimeMillis();
			
			if (false) {
				return;
			}
			
			aLitBlocks = new int[50][10][50][1];
			int aLitCounter = 0;
			AutoMap<BlockPos> aBlocksToUpdate = new AutoMap<BlockPos>();
			Logger.INFO("Trying to relight area.");
			
			BlockPos aStartIterationPoint = new BlockPos(this.xCoord-24, this.yCoord-4, this.zCoord-24, this.worldObj);			
			for (int x = 0; x < 50; x++) {
				for (int y = 0; y < 10; y++) {
					for (int z = 0; z < 50; z++) {
						int xOff = aStartIterationPoint.xPos + x;
						int yOff = aStartIterationPoint.yPos + y;
						int zOff = aStartIterationPoint.zPos + z;						
						Block aBlockGet = this.worldObj.getBlock(xOff, yOff, zOff);
						if (aBlockGet != null) {
							if (aBlockGet instanceof BlockAir || aBlockGet instanceof LightGlass) {
								
								int aLight = aBlockGet.getLightValue();
								
								//Don't Need to relight anything.
								if ((enable && aLight > 0) || (!enable && aLight == 0)) {
									continue;
								}
								//Turning Lights on
								else if (enable && aLight == 0) {
									aBlocksToUpdate.put(new BlockPos(xOff, yOff, zOff, this.worldObj));									
									if (aBlockGet instanceof BlockAir) {
										Logger.INFO("Lit air.");
										this.worldObj.setBlock(xOff, yOff, zOff, ModBlocks.MatterFabricatorEffectBlock, 0, 3);
									}									
									//aBlockGet.setLightLevel(15);
									aLitCounter++;
								}
								//Turning Lights off
								else if (!enable && aLight > 0) {
									aBlocksToUpdate.put(new BlockPos(xOff, yOff, zOff, this.worldObj));
									if (aBlockGet instanceof LightGlass) {
										Logger.INFO("Dimmed air.");
										this.worldObj.setBlock(xOff, yOff, zOff, Blocks.air, 0, 3);
									}	
									//aBlockGet.setLightLevel(0);
								}
								aLitBlocks[x][y][z][0] = enable ? 15 : 0;								
							}
							else {
								aLitBlocks[x][y][z][0] = -1;								
							}
						}
						else {
							aLitBlocks[x][y][z][0] = -1;							
						}
					}
				}
			}
			mLitBlockCount = aLitCounter;
			doLargeBlockUpdate(aBlocksToUpdate); 
		}
		
		public void doLargeBlockUpdate(AutoMap<BlockPos> aUpdateMap) {			
			if (aUpdateMap.isEmpty()) {
				return;
			}			
			for (BlockPos p : aUpdateMap) {				
				//this.worldObj.markBlockForUpdate(p.xPos, p.yPos, p.zPos);
				//this.worldObj.markBlocksDirtyVertical(p_72975_1_, p_72975_2_, p_72975_3_, p_72975_4_);
			}			
		}

	}

}
