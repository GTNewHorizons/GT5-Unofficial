package gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic;

import static gregtech.api.enums.GT_Values.V;
import static gtPlusPlus.api.objects.minecraft.ChunkManager.mChunkLoaderManagerMap;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.MapMaker;
import com.google.common.collect.Sets;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.objects.GT_RenderedTexture;

import gtPlusPlus.GTplusplus;
import gtPlusPlus.api.interfaces.IChunkLoader;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.Triplet;
import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.api.objects.minecraft.ChunkManager;
import gtPlusPlus.api.objects.minecraft.DimChunkPos;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;

public class GregtechMetaTileEntityChunkLoader extends GT_MetaTileEntity_TieredMachineBlock implements IChunkLoader {

	private long mTicksLeft = 0;
	
	private boolean hasTicket;
	private boolean refreshTicket;

	@SuppressWarnings("unused")
	private final static int yMin = 0;
	private final static int yMax = 254;
	private final int xMin, xMax;
	private final int zMin, zMax;
	private int mChunkLoaderMapID = -1;
	private boolean mHasID = false;	
	private final short mMaxTicks = 100;	
	private final UUID mUUID;	
	private static final byte ANCHOR_RADIUS = 1;	
	private static final Map<UUID, Ticket> tickets = new MapMaker().makeMap();
	private Set<ChunkCoordIntPair> chunks;
	
	public GregtechMetaTileEntityChunkLoader(final int aID, final String aName, final String aNameRegional, final int aTier) {
		super(aID, aName, aNameRegional, aTier, 0, "Loads chunks: " + (16 + (48 * aTier)) + " powered");
		xMin = this.xCoord-47;
		xMax = this.xCoord+47;
		zMin = this.zCoord-47;
		zMax = this.zCoord+47;
		mUUID = UUID.randomUUID();
		this.prevX = this.xCoord;
		this.prevY = this.yCoord;
		this.prevZ = this.zCoord;
	}

	public GregtechMetaTileEntityChunkLoader(final String aName, final int aTier, final int aInvSlotCount, final String aDescription, final ITexture[][][] aTextures) {
		super(aName, aTier, aInvSlotCount, aDescription, aTextures);
		xMin = this.xCoord-47;
		xMax = this.xCoord+47;
		zMin = this.zCoord-47;
		zMax = this.zCoord+47;
		mUUID = UUID.randomUUID();
		this.prevX = this.xCoord;
		this.prevY = this.yCoord;
		this.prevZ = this.zCoord;
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntityChunkLoader(this.mName, this.mTier, this.mInventory.length, this.mDescription, this.mTextures);
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColorIndex + 1], (aSide != 1) ? null : aActive ? new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_TELEPORTER_ACTIVE) : new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_TELEPORTER)};
	}

	private int xCoord, yCoord, zCoord;
	private int prevX, prevY, prevZ;
	@Override
	public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTimer) {

		xCoord = aBaseMetaTileEntity.getXCoord();
		yCoord = aBaseMetaTileEntity.getYCoord();
		zCoord = aBaseMetaTileEntity.getZCoord();
		

		if (!aBaseMetaTileEntity.isServerSide()) {
			return;
		}
		
		if (aTimer % 300 == 0) {
			if (!isRegistered() && this.getEUVar() > 0) {
				registerLoader();
			}
			else if (isRegistered() && this.getEUVar() <= 0) {
				releaseTicket();
			}
		}

		if (!areChunksLoaded()) {
			return;
		}

		if (aBaseMetaTileEntity.isAllowedToWork()) {
			if (this.mTicksLeft > 0) {
				this.mTicksLeft--;
			}
			else if (this.mTicksLeft < 0) {
				this.mTicksLeft = 0;
			}		
			if (mTicksLeft < mMaxTicks) {  
				long h = this.getEUVar();
				if (h > 0) {
					if (h >(this.maxEUInput()+this.getMinimumStoredEU())) {
						this.setEUVar((this.getEUVar()-this.maxEUInput()));
						this.mTicksLeft += 2;
					}
				}
			}	
			if (mTicksLeft > mMaxTicks) {
				mTicksLeft = mMaxTicks;
			}

			//Somehow the Tile has moved, so lets release the ticket and update the previous xyz.
			if (xCoord != prevX || yCoord != prevY || zCoord != prevZ) {
				releaseTicket();
				prevX = xCoord;
				prevY = yCoord;
				prevZ = zCoord;
			}

			//Tile has an active ticket, but either (1) the ticket is for a different dimension. (2) A ticket refresh has been queued or (3) the Tile is not allowed to work (Disabled via mallet)
			if (hasActiveTicket() && (getTicket().world != aBaseMetaTileEntity.getWorld() || refreshTicket || !aBaseMetaTileEntity.isAllowedToWork())) {
				releaseTicket();
			}			

			//Tile does not have a ticket but has mTicksLeft, we had best create a ticket,
			if (!hasActiveTicket() && this.mTicksLeft > 0) {
				requestTicket();
			}
		}
	}

	@Override
	public void onFirstTick(final IGregTechTileEntity aBaseMetaTileEntity) {
		
	}

	@Override
	public void onRemoval() {
		releaseTicket();
	}

	@Override
	public boolean isSimpleMachine() {
		return false;
	}

	@Override
	public boolean isFacingValid(final byte aFacing) {
		return aFacing > 1;
	}

	@Override
	public boolean isEnetInput() {
		return true;
	}

	@Override
	public boolean isInputFacing(final byte aSide) {
		return true;
	}

	@Override
	public boolean isTeleporterCompatible() {
		return false;
	}

	@Override
	public long getMinimumStoredEU() {
		return 512;
	}

	@Override
	public long maxEUStore() {
		return 512 + (V[this.mTier] * 50);
	}

	@Override
	public long maxEUInput() {
		return V[this.mTier];
	}

	@Override
	public long maxAmperesIn() {
		return 2;
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
	public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
		return new ITexture[0][0][0];
	}

	@Override
	public void saveNBTData(final NBTTagCompound aNBT) {
		aNBT.setLong("mTicksLeft", getTicksRemaining());
		aNBT.setBoolean("mHasID", mHasID);
		aNBT.setInteger("mChunkLoaderMapID", mChunkLoaderMapID);
	}

	@Override
	public void loadNBTData(final NBTTagCompound aNBT) {
		this.mTicksLeft = aNBT.getLong("mTicksLeft");
		this.mHasID = aNBT.getBoolean("mHasID");
		this.mChunkLoaderMapID = aNBT.getInteger("mChunkLoaderMapID");
	}





	/**
	 * Chunk Handling
	 */

	public void registerLoader() {
		if (this.getBaseMetaTileEntity().getWorld().isRemote) {
			return;
		}		
		//Logger.INFO("=======================[Chunkloader Registration]========================");
		//Logger.INFO("Method Call [1]:"+ReflectionUtils.getMethodName(0));
		//Logger.INFO("Method Call [2]:"+ReflectionUtils.getMethodName(1));
		//Logger.INFO("Method Call [3]:"+ReflectionUtils.getMethodName(2));
		if (this.getEUVar() <= 0) {
			return;
		}
		
		if (this.getBaseMetaTileEntity() != null && this.getBaseMetaTileEntity().getWorld() != null && this.getBaseMetaTileEntity().getWorld().getWorldTime() >= 100) {		
			if (!mHasID) {
				this.mChunkLoaderMapID = ChunkManager.getIdFromUniqueString(getUniqueID());
				mHasID = true;
			}
			if (this != null && this.getBaseMetaTileEntity() != null) {		
				if (!isRegistered() && !getUniqueID().equalsIgnoreCase("0@0@0@0")) {
					Triplet<Integer, GregtechMetaTileEntityChunkLoader, DimChunkPos> loaderData = new Triplet<Integer, GregtechMetaTileEntityChunkLoader, DimChunkPos>((int) this.mChunkLoaderMapID, this, new DimChunkPos(this.getBaseMetaTileEntity().getWorld(), getPos()));
					mChunkLoaderManagerMap.put(getUniqueID(), loaderData);
					Logger.INFO("[Chunk Loader] Registered Chunk loader ["+this.mChunkLoaderMapID+"]"+getPos().getLocationString());
				}
				else {
					Logger.INFO("Tried to re-register a Chunk-Loader. Loader located @ "+getPos().getLocationString());
				}
			}
		}
	}	


	public void unregisterLoader() {
		if (this != null && this.getBaseMetaTileEntity() != null) {		
			if (isRegistered()) {
				mChunkLoaderManagerMap.remove(getUniqueID());
				Logger.INFO("[Chunk Loader] Removed Chunk loader ["+this.mChunkLoaderMapID+"]"+getPos().getLocationString());
			}
		}
	}

	public boolean isRegistered() {
		return mChunkLoaderManagerMap.containsKey(getUniqueID());	
	}

	private BlockPos mCachedLocation;
	public BlockPos getPos() {
		if (mCachedLocation == null) {
			mCachedLocation = new BlockPos(this.xCoord, this.yCoord, this.zCoord, this.getBaseMetaTileEntity().getWorld().provider.dimensionId);
		}
		else if (mCachedLocation.xPos == 0 && mCachedLocation.yPos == 0 && mCachedLocation.zPos == 0 && mCachedLocation.dim == 0) {
			mCachedLocation = null;
			return getPos();
		}
		return mCachedLocation;
	}

	public String getUniqueID() {
		if (getPos() == null) {
			return "InvalidChunkLoader";
		}		
		return getPos().getUniqueIdentifier();
	}

	public int getLoaderID() {
		return this.mChunkLoaderMapID;
	}

	public void onBlockRemoval() {
		releaseTicket();
	}

	public void invalidate() {
		releaseTicket();	
		super.inValidate();
		refreshTicket = true;
	}

	public void validate() {
		if (!isRegistered()) {
			registerLoader();
		}
	}

	protected void releaseTicket() {
		refreshTicket = false;
		if (getTicket() != null) {
			ForgeChunkManager.releaseTicket(getTicket());
		}
		setTicket(null);
		unregisterLoader();
	}

	protected void requestTicket() {
		Ticket chunkTicket = getTicketFromForge();
		if (chunkTicket != null) {
			setTicketData(chunkTicket);
			forceChunkLoading(chunkTicket);
		}
	}

	public Ticket getTicketFromForge() {
		if (this.getBaseMetaTileEntity().getWorld() != null)
			return ForgeChunkManager.requestTicket(GTplusplus.instance, this.getBaseMetaTileEntity().getWorld(), Type.NORMAL);
		return null;
	}

	protected void setTicketData(Ticket chunkTicket) {
		chunkTicket.getModData().setInteger("xCoord", this.getBaseMetaTileEntity().getXCoord());
		chunkTicket.getModData().setInteger("yCoord", this.getBaseMetaTileEntity().getYCoord());
		chunkTicket.getModData().setInteger("zCoord", this.getBaseMetaTileEntity().getZCoord());
		//chunkTicket.getModData().setString("type", getMachineType().getTag());
	}

	public boolean hasActiveTicket() {
		return getTicket() != null;
	}

	public Ticket getTicket() {
		//Logger.INFO("[Chunk Loader] "+"Getting Ticking. ["+this.getLoaderID()+"] @ [x: "+this.xCoord+"][y: "+this.yCoord+"][z: "+this.zCoord+"]");
		return tickets.get(this.mUUID);
	}

	public void setTicket(Ticket t) {
		boolean changed = false;
		Ticket ticket = getTicket();
		if (ticket != t) {
			if (ticket != null) {
				if (ticket.world == this.getBaseMetaTileEntity().getWorld()) {
					for (ChunkCoordIntPair chunk : ticket.getChunkList()) {
						if (ForgeChunkManager.getPersistentChunksFor(this.getBaseMetaTileEntity().getWorld()).keys().contains(chunk))
							ForgeChunkManager.unforceChunk(ticket, chunk);
					}
					ForgeChunkManager.releaseTicket(ticket);
				}
				Logger.INFO("[Chunk Loader] "+"Removing Ticket. ["+this.getLoaderID()+"] @ [x: "+this.xCoord+"][y: "+this.yCoord+"][z: "+this.zCoord+"]");
				tickets.remove(mUUID);
			}
			changed = true;
		}
		hasTicket = (t != null);
		if (hasTicket) {
			Logger.INFO("[Chunk Loader] "+"Putting Ticket. ["+this.getLoaderID()+"] @ [x: "+this.xCoord+"][y: "+this.yCoord+"][z: "+this.zCoord+"]");
			tickets.put(mUUID, t);
		}
		//if (changed)
		//sendUpdateToClient();
	}

	public void forceChunkLoading(Ticket ticket) {
		try {			
			setTicket(ticket);
			forceChunkLoading2(ticket);
			//setupChunks();
			/*if (chunks != null) {
				for (ChunkCoordIntPair chunk : chunks) {
					ForgeChunkManager.forceChunk(ticket, chunk);
				}
			}*/
		}
		catch (Throwable t){
			t.printStackTrace();
		}
	}

	public void forceChunkLoading2(Ticket ticket) {		
		chunks = Sets.newHashSet();
		ChunkCoordIntPair quarryChunk = new ChunkCoordIntPair(xCoord >> 4, zCoord >> 4);
		chunks.add(quarryChunk);
		ForgeChunkManager.forceChunk(ticket, quarryChunk);
		for (int chunkX = xMin >> 4; chunkX <= xMax >> 4; chunkX++) {
			for (int chunkZ = zMin >> 4; chunkZ <= zMax >> 4; chunkZ++) {
				ChunkCoordIntPair chunk = new ChunkCoordIntPair(chunkX, chunkZ);
				ForgeChunkManager.forceChunk(ticket, chunk);
				chunks.add(chunk);
			}
		}

	}

	public void setupChunks() {
		if (!hasTicket)
			chunks = null;
		else {
			/*if (this.mTier == 3) {
				chunks = ChunkManager.getInstance().getChunksAround(this.getBaseMetaTileEntity().getXCoord() >> 4, this.getBaseMetaTileEntity().getZCoord() >> 4, ANCHOR_RADIUS);
			}
			else if (this.mTier == 4) {
				chunks = ChunkManager.getInstance().getChunksAround(this.getBaseMetaTileEntity().getXCoord() >> 4 >> 4, (this.getBaseMetaTileEntity().getZCoord() >> 4) >> 4, ANCHOR_RADIUS);
			}
			else if (this.mTier == 5) {
				chunks = ChunkManager.getInstance().getChunksAround(this.getBaseMetaTileEntity().getXCoord() >> 4 >> 4 >> 4, ((this.getBaseMetaTileEntity().getZCoord() >> 4) >> 4) >> 4, ANCHOR_RADIUS);
			} */
			chunks = ChunkManager.getInstance().getChunksAround(this.getBaseMetaTileEntity().getXCoord() >> 4, this.getBaseMetaTileEntity().getZCoord() >> 4, ANCHOR_RADIUS);
		}
	}


	@Override
	public Chunk loadChunk(World p_75815_1_, int p_75815_2_, int p_75815_3_) throws IOException {
		Logger.INFO("1");
		return null;
	}

	@Override
	public void saveChunk(World p_75816_1_, Chunk p_75816_2_) throws MinecraftException, IOException {
		Logger.INFO("2");
	}

	@Override
	public void saveExtraChunkData(World p_75819_1_, Chunk p_75819_2_) {
		Logger.INFO("3");
	}

	@Override
	public void chunkTick() {
		Logger.INFO("4");
	}

	@Override
	public void saveExtraData() {
		Logger.INFO("5");
	}

	@Override
	public long getTicksRemaining() {
		return this.mTicksLeft;
	}

	@Override
	public String[] getDescription() {
		// TODO Auto-generated method stub
		return super.getDescription();
	}

	@Override
	public void onExplosion() {
		this.releaseTicket();
		super.onExplosion();
	}

	@Override
	public void inValidate() {
		invalidate();
		super.inValidate();
	}

	@Override
	public void onMachineBlockUpdate() {
		super.onMachineBlockUpdate();
	}

	@Override
	public void markDirty() {
		validate();
		super.markDirty();
	}

	@Override
	public void doExplosion(long aExplosionPower) {
		releaseTicket();
		super.doExplosion(aExplosionPower);
	}

	@Override
	public boolean isGivingInformation() {
		return true;
	}

	@Override
	public String[] getInfoData() {
		return new String[] {
				this.getLocalName(),
				"Ticks Left: "+this.mTicksLeft,
				"mRange: "+(16 + (48 * mTier)),
				"chunks: "+this.chunks.size()};
	}

	@Override
	public void onCreated(ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
		super.onCreated(aStack, aWorld, aPlayer);
	}

	@Override
	public void onServerStart() {
		validate();
		super.onServerStart();
	}

	@Override
	public void onWorldLoad(File aSaveDirectory) {
		validate();
		super.onWorldLoad(aSaveDirectory);
	}

	@Override
	public void onWorldSave(File aSaveDirectory) {
		validate();
		super.onWorldSave(aSaveDirectory);
	}

	//Based on BC 7


	private boolean hasCheckedChunks = false;
	public boolean areChunksLoaded() {
		if (hasCheckedChunks) {
			return true;
		}
		World worldObj = this.getBaseMetaTileEntity().getWorld();
		hasCheckedChunks = worldObj.blockExists(xMin, yMax, zMin)
				&& worldObj.blockExists(xMax, yMax, zMin)
				&& worldObj.blockExists(xMin, yMax, zMax)
				&& worldObj.blockExists(xMax, yMax, zMax);
		// Each chunk covers the full height, so we only check one of them per height.
		return hasCheckedChunks;
	}

}
