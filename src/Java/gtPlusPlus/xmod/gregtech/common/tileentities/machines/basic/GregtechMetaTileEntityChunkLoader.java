package gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic;

import static gregtech.api.enums.GT_Values.V;
import static gtPlusPlus.core.util.minecraft.gregtech.PollutionUtils.mPollution;

import java.util.HashSet;
import java.util.Set;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.interfaces.IChunkLoader;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.chunkloading.GTPP_ChunkManager;
import gtPlusPlus.core.chunkloading.StaticChunkFunctions;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.core.util.minecraft.gregtech.PollutionUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMetaTileEntity;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class GregtechMetaTileEntityChunkLoader extends GT_MetaTileEntity_BasicMachine implements IChunkLoader {

	public GregtechMetaTileEntityChunkLoader(int aID, String aName, String aNameRegional, int aTier) {
		super(aID, aName, aNameRegional, aTier, 2, "Loads " + getMaxChunksToLoadForTier(aTier) + " chunks when powered", 0, 0, "Recycler.png", "", 
				new ITexture[]{
						new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_SIDE_MASSFAB_ACTIVE),
						new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_SIDE_MASSFAB),
						new GT_RenderedTexture(TexturesGtBlock.Overlay_MatterFab_Active),
						new GT_RenderedTexture(TexturesGtBlock.Overlay_MatterFab),
						new GT_RenderedTexture(TexturesGtBlock.Overlay_Machine_Vent_Fast),
						new GT_RenderedTexture(TexturesGtBlock.Overlay_Machine_Vent),
						new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_BOTTOM_MASSFAB_ACTIVE),
						new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_BOTTOM_MASSFAB)
		});
	}

	public GregtechMetaTileEntityChunkLoader(String aName, int aTier, String aDescription, ITexture[][][] aTextures, String aGUIName, String aNEIName) {
		super(aName, aTier, 2, aDescription, aTextures, 0, 0, aGUIName, aNEIName);
	}

	public static int getMaxChunksToLoadForTier(int aTier) {
		return (aTier * aTier);
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				this.mDescription,
		};
	}

	@Override
	public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
		final ITexture[][][] rTextures = new ITexture[10][17][];
		for (byte i = -1; i < 16; i++) {
			rTextures[0][i + 1] = this.getFront(i);
			rTextures[1][i + 1] = this.getBack(i);
			rTextures[2][i + 1] = this.getBottom(i);
			rTextures[3][i + 1] = this.getTop(i);
			rTextures[4][i + 1] = this.getSides(i);
			rTextures[5][i + 1] = this.getFrontActive(i);
			rTextures[6][i + 1] = this.getBackActive(i);
			rTextures[7][i + 1] = this.getBottomActive(i);
			rTextures[8][i + 1] = this.getTopActive(i);
			rTextures[9][i + 1] = this.getSidesActive(i);
		}
		return rTextures;
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		return this.mTextures[(aActive ? 5 : 0) + (aSide == aFacing ? 0 : aSide == GT_Utility.getOppositeSide(aFacing) ? 1 : aSide == 0 ? 2 : aSide == 1 ? 3 : 4)][aColorIndex + 1];
	}


	public ITexture[] getFront(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Material_RedSteel)};
	}


	public ITexture[] getBack(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Material_RedSteel)};
	}


	public ITexture[] getBottom(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Material_Grisium)};
	}


	public ITexture[] getTop(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Material_Grisium)};
	}


	public ITexture[] getSides(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Redox_3)};
	}


	public ITexture[] getFrontActive(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Material_RedSteel)};
	}


	public ITexture[] getBackActive(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Material_RedSteel)};
	}


	public ITexture[] getBottomActive(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Material_Grisium)};
	}


	public ITexture[] getTopActive(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Material_Grisium)};
	}


	public ITexture[] getSidesActive(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1], new GT_RenderedTexture(TexturesGtBlock.Casing_Redox_3)};
	}

	@Override
	public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
		PlayerUtils.messagePlayer(aPlayer, "Running every "+" minutes.");	
		super.onScrewdriverRightClick(aSide, aPlayer, aX, aY, aZ);
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntityChunkLoader(this.mName, this.mTier, this.mDescription, this.mTextures, this.mGUIName, this.mNEIName);
	}

	@Override
	public boolean onRightclick(final IGregTechTileEntity aBaseMetaTileEntity, final EntityPlayer aPlayer) {
		if (aBaseMetaTileEntity.isClientSide()){
			return true;
		}
		this.showPollution(aPlayer.getEntityWorld(), aPlayer);
		return true;
	}

	private void showPollution(final World worldIn, final EntityPlayer playerIn){
		//PlayerUtils.messagePlayer(playerIn, "Running every "+mFrequency+" minutes. Owner: "+this.getBaseMetaTileEntity().getOwnerName());
		//PlayerUtils.messagePlayer(playerIn, "Last run: "+Utils.getSecondsFromMillis(aDiff)+" seconds ago.");	
	}

	@Override
	public boolean allowPullStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide, final ItemStack aStack) {
		return false;
	}

	@Override
	public boolean allowPutStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide, final ItemStack aStack) {
		return false;
	}


	@Override
	public String[] getInfoData() {
		return new String[] {
				this.getLocalName()
		};
	}

	@Override
	public boolean isGivingInformation() {
		return true;
	}

	@Override
	public boolean canInsertItem(final int p_102007_1_, final ItemStack p_102007_2_, final int p_102007_3_) {
		return false;
	}

	@Override
	public boolean canExtractItem(final int p_102008_1_, final ItemStack p_102008_2_, final int p_102008_3_) {
		return false;
	}

	@Override
	public int getSizeInventory() {
		return 0;
	}

	@Override
	public boolean isUseableByPlayer(final EntityPlayer p_70300_1_) {
		return true;
	}

	@Override
	public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
		super.onPostTick(aBaseMetaTileEntity, aTick);
		
		// Have we set the Chunk this Tile resides in yet?
        if (mCurrentChunk == null) {
            int xTile = getBaseMetaTileEntity().getXCoord();
            int zTile = getBaseMetaTileEntity().getZCoord();
        	createInitialWorkingChunk(aBaseMetaTileEntity, xTile, zTile);
        }       
		
        // Try unload all chunks if fail to meet global chunkloading conditions.
		if (StaticChunkFunctions.onPostTick(aBaseMetaTileEntity, aTick)) {
			// Can this tile actively chunkload?
			if (getChunkLoadingActive()) {
				// Consume some power
				this.setEUVar(this.getEUVar() - (maxEUInput() * maxAmperesIn()));
				
		        // Do we need to re-request tickets?
		        if (getDoesWorkChunkNeedReload()) {
		        	// Request ticket for current chunk.
		            GTPP_ChunkManager.requestChunkLoad((TileEntity)getBaseMetaTileEntity(), mCurrentChunk);
		            // Request a ticket for each chunk we have mapped out in a spiral pattern.
		            if (!mLoadedChunks.isEmpty()) {
		            	for (ChunkCoordIntPair Y : mLoadedChunks) {
		                    GTPP_ChunkManager.requestChunkLoad((TileEntity)getBaseMetaTileEntity(), Y);            		
		            	}
		            }
		            setDoesWorkChunkNeedReload(false);            
		        }
				
			}
		}		
	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		super.saveNBTData(aNBT);
		StaticChunkFunctions.saveNBTDataForTileEntity(this.getBaseMetaTileEntity(), aNBT);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		super.loadNBTData(aNBT);
		StaticChunkFunctions.loadNBTDataForTileEntity(this.getBaseMetaTileEntity(), aNBT);
	}

	@Override
	public long maxAmperesIn() {
		return 2;
	}

	@Override
	public long getMinimumStoredEU() {
		return V[mTier] * 2;
	}

	@Override
	public long maxEUStore() {
		return V[mTier] * 256;
	}

	@Override
	public long maxEUInput() {
		return V[mTier];
	}

	
	/*
	 * Chunkloading Vars
	 */
	
	private long mTicksRemainingForChunkloading = -1;
	private ChunkCoordIntPair mCurrentChunk;
	private Set<ChunkCoordIntPair> mLoadedChunks = new HashSet<ChunkCoordIntPair>();
	private boolean mRefreshChunkTickets = false;
	
	@Override
	public long getTicksRemaining() {
		return -1;
	}

	@Override
	public void setTicksRemaining(long aTicks) {
		mTicksRemainingForChunkloading = aTicks;
	}

	@Override
	public ChunkCoordIntPair getResidingChunk() {
		return mCurrentChunk;
	}

	@Override
	public void setResidingChunk(ChunkCoordIntPair aCurrentChunk) {
		mCurrentChunk = aCurrentChunk;
	}

	@Override
	public boolean getChunkLoadingActive() {
		return this.getEUVar() >= maxEUInput() * maxAmperesIn();
	}

	@Override
	public void setChunkLoadingActive(boolean aActive) {
		
	}

	@Override
	public boolean getDoesWorkChunkNeedReload() {		
		return mRefreshChunkTickets;
	}

	@Override
	public void setDoesWorkChunkNeedReload(boolean aActive) {
		mRefreshChunkTickets = aActive;
	}

	@Override
	public boolean addChunkToLoadedList(ChunkCoordIntPair aActiveChunk) {
		return mLoadedChunks.add(aActiveChunk);
	}

	@Override
	public boolean removeChunkFromLoadedList(ChunkCoordIntPair aActiveChunk) {
		return mLoadedChunks.remove(aActiveChunk);
	}

	@Override
	public Set<ChunkCoordIntPair> getManagedChunks() {
		return mLoadedChunks;
	}

	@Override
	public void onRemoval() {
		StaticChunkFunctions.onRemoval(getBaseMetaTileEntity());
		super.onRemoval();
	}
	
	public static Set<ChunkCoordIntPair> spiralChunks(final IGregTechTileEntity aBaseMetaTileEntity, int X, int Z) {
		World w = aBaseMetaTileEntity.getWorld();
		HashSet<ChunkCoordIntPair> aSet = new HashSet<ChunkCoordIntPair>();
		if (w == null) {
			return aSet;
		}
		Chunk thisChunk = w.getChunkFromBlockCoords(aBaseMetaTileEntity.getXCoord(), aBaseMetaTileEntity.getZCoord());
		ChunkCoordIntPair aChunkCo = new ChunkCoordIntPair(thisChunk.xPosition, thisChunk.zPosition);		
	    int x,z,dx,dz;
	    x = z = dx =0;
	    dz = -1;
	    int t = Math.max(X,Z);
	    int maxI = t*t;
	    for(int i =0; i < maxI; i++){
	        if ((-X/2 <= x) && (x <= X/2) && (-Z/2 <= z) && (z <= Z/2)){
	            Chunk C = w.getChunkFromChunkCoords(aChunkCo.chunkXPos + x, aChunkCo.chunkZPos + z);
	            if (C != null) {
	            	aSet.add(new ChunkCoordIntPair(C.xPosition, C.zPosition));
	            }
	        }
	        if( (x == z) || ((x < 0) && (x == -z)) || ((x > 0) && (x == 1-z))){
	            t = dx;
	            dx = -dz;
	            dz = t;
	        }
	        x += dx;
	        z += dz;
	    }
	    return aSet;
	}

	@Override
	public int getChunkloaderTier() {
		return mTier;
	}
	
	public void createInitialWorkingChunk(IGregTechTileEntity aBaseMetaTileEntity, int aTileX, int aTileZ) {
		final int centerX = aTileX >> 4;
		final int centerZ = aTileZ >> 4;
		addChunkToLoadedList(new ChunkCoordIntPair(centerX, centerZ));
		GTPP_ChunkManager.requestChunkLoad((TileEntity)aBaseMetaTileEntity.getMetaTileEntity(), getResidingChunk());		
		// If this surrounding chunk map for this tile is empty, we spiral out and map chunks to keep loaded.
		if (getManagedChunks().isEmpty()) {
			int aChunks = GregtechMetaTileEntityChunkLoader.getMaxChunksToLoadForTier(getChunkloaderTier());					
			mLoadedChunks.addAll(spiralChunks(aBaseMetaTileEntity, getChunkloaderTier(), getChunkloaderTier()));
		}	
		if (!mLoadedChunks.isEmpty()) {
        	for (ChunkCoordIntPair Y : mLoadedChunks) {
                GTPP_ChunkManager.requestChunkLoad((TileEntity)aBaseMetaTileEntity.getMetaTileEntity(), Y);            		
        	}
        }
		setDoesWorkChunkNeedReload(false);
	}
	

}