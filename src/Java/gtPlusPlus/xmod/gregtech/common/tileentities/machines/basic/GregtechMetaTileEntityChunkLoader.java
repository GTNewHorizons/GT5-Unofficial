package gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic;

import static gregtech.api.enums.GT_Values.V;
import static gtPlusPlus.api.objects.ChunkManager.mChunkLoaders;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.MapMaker;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_SpawnEventHandler;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.api.interfaces.IChunkLoader;
import gtPlusPlus.api.objects.ChunkManager;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.array.BlockPos;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;

public class GregtechMetaTileEntityChunkLoader extends GT_MetaTileEntity_TieredMachineBlock implements IChunkLoader {

	public int mRange = 16;
	private long mTicksLeft = 0;
	private UUID mUUID = UUID.randomUUID();

	public GregtechMetaTileEntityChunkLoader(final int aID, final String aName, final String aNameRegional, final int aTier) {
		super(aID, aName, aNameRegional, aTier, 0, "Loads chunks: " + (16 + (48 * aTier)) + " powered");
	}

	public GregtechMetaTileEntityChunkLoader(final String aName, final int aTier, final int aInvSlotCount, final String aDescription, final ITexture[][][] aTextures) {
		super(aName, aTier, aInvSlotCount, aDescription, aTextures);
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
		if (!aBaseMetaTileEntity.isServerSide()) {
			return;
		}

		if (aBaseMetaTileEntity.isAllowedToWork() && aBaseMetaTileEntity.isServerSide()) {



			if (this.mTicksLeft > 0) {
				this.mTicksLeft--;
			}
			else if (this.mTicksLeft < 0) {
				this.mTicksLeft = 0;
			}		
			if (mTicksLeft < 10) {  
				long h = this.getEUVar();
				if (h > 0) {
					if (h >(this.maxEUInput()+this.getMinimumStoredEU())) {
						this.setEUVar((this.getEUVar()-this.maxEUInput()));
						this.mTicksLeft += 10;
					}
				}
			}			

			xCoord = aBaseMetaTileEntity.getXCoord();
			yCoord = aBaseMetaTileEntity.getYCoord();
			zCoord = aBaseMetaTileEntity.getZCoord();

			if (xCoord != prevX || yCoord != prevY || zCoord != prevZ) {
				releaseTicket();
				prevX = xCoord;
				prevY = yCoord;
				prevZ = zCoord;
			}

			if (hasActiveTicket() && (getTicket().world != aBaseMetaTileEntity.getWorld() || refreshTicket || !aBaseMetaTileEntity.isAllowedToWork())) {
				releaseTicket();
			}        

			if (!hasActiveTicket() && this.mTicksLeft > 0) {
				requestTicket();
			}


		}




	}

	@Override
	public void onFirstTick(final IGregTechTileEntity aBaseMetaTileEntity) {
		registerLoader();
	}

	@Override
	public void onRemoval() {
		ChunkManager.mChunkLoaders.remove(new BlockPos(this.xCoord, this.yCoord, this.zCoord, this.getBaseMetaTileEntity().getWorld().provider.dimensionId));
	}

	@Override
	public boolean isSimpleMachine() {
		return false;
	}

	@Override
	public boolean isFacingValid(final byte aFacing) {
		return true;
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
		return null;
	}

	@Override
	public void saveNBTData(final NBTTagCompound aNBT) {
		aNBT.setLong("mTicksLeft", getTicksRemaining());
	}

	@Override
	public void loadNBTData(final NBTTagCompound aNBT) {
		this.mTicksLeft = aNBT.getLong("mTicksLeft");
	}





	/**
	 * Chunk Handling
	 */
	private static final Map<UUID, Ticket> tickets = new MapMaker().makeMap();
	private static final byte ANCHOR_RADIUS = 1;
	private boolean hasTicket;
	private boolean refreshTicket;
	private Set<ChunkCoordIntPair> chunks;
	private short mChunkLoaderMapID = -1;

	public void registerLoader() {
		short mSize = (short) ChunkManager.mChunkLoaders.size();
		this.mChunkLoaderMapID = mSize;
		if (!mChunkLoaders.containsValue(this)) {
			mChunkLoaders.put(new BlockPos(this.xCoord, this.yCoord, this.zCoord, this.getBaseMetaTileEntity().getWorld().provider.dimensionId),this);
		}
	}	

	public short getLoaderID() {
		return this.mChunkLoaderMapID;
	}

	public void onBlockRemoval() {
		releaseTicket();
	}

	public void invalidate() {
		refreshTicket = true;
	}

	public void validate() {
		refreshTicket = true;
	}

	protected void releaseTicket() {
		refreshTicket = false;
		setTicket(null);
	}

	protected void requestTicket() {
		Ticket chunkTicket = getTicketFromForge();
		if (chunkTicket != null) {
			setTicketData(chunkTicket);
			forceChunkLoading(chunkTicket);
		}
	}

	protected Ticket getTicketFromForge() {
		return ForgeChunkManager.requestTicket(GTplusplus.instance, this.getBaseMetaTileEntity().getWorld(), Type.NORMAL);
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
				Logger.INFO("[Chunk Loader] "+"Removing Ticking. ["+this.getLoaderID()+"] @ [x: "+this.xCoord+"][y: "+this.yCoord+"][z: "+this.zCoord+"]");
				tickets.remove(mUUID);
			}
			changed = true;
		}
		hasTicket = t != null;
		if (hasTicket) {
			Logger.INFO("[Chunk Loader] "+"Putting Ticking. ["+this.getLoaderID()+"] @ [x: "+this.xCoord+"][y: "+this.yCoord+"][z: "+this.zCoord+"]");
			tickets.put(mUUID, t);
		}
		//if (changed)
		//sendUpdateToClient();
	}

	public void forceChunkLoading(Ticket ticket) {
		try {
		setTicket(ticket);

		setupChunks();

		if (chunks != null) {
			for (ChunkCoordIntPair chunk : chunks) {
				ForgeChunkManager.forceChunk(ticket, chunk);
			}
		}
		}
		catch (Throwable t){
			t.printStackTrace();
		}
	}

	public void setupChunks() {
		if (!hasTicket)
			chunks = null;
		else
			chunks = ChunkManager.getInstance().getChunksAround(this.getBaseMetaTileEntity().getXCoord() >> 4, this.getBaseMetaTileEntity().getZCoord() >> 4, ANCHOR_RADIUS);
	}


	@Override
	public Chunk loadChunk(World p_75815_1_, int p_75815_2_, int p_75815_3_) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveChunk(World p_75816_1_, Chunk p_75816_2_) throws MinecraftException, IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveExtraChunkData(World p_75819_1_, Chunk p_75819_2_) {
		// TODO Auto-generated method stub

	}

	@Override
	public void chunkTick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveExtraData() {
		// TODO Auto-generated method stub

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
		validate();
		super.onMachineBlockUpdate();
	}

	@Override
	public void markDirty() {
		validate();
		super.markDirty();
	}

	@Override
	public void doExplosion(long aExplosionPower) {
		this.releaseTicket();
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
				"mRange: "+this.mRange,
				"chunks: "+this.chunks.size()};
	}

}
