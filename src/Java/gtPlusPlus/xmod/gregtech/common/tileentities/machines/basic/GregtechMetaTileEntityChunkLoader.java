package gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic;

import static gregtech.api.enums.GT_Values.V;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.MapMaker;
import com.google.common.collect.UnmodifiableIterator;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.objects.GT_RenderedTexture;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.api.interfaces.IGregtechPacketEntity;
import gtPlusPlus.core.handler.chunkloading.ChunkManager;
import gtPlusPlus.core.util.minecraft.network.PacketBuilder;

public class GregtechMetaTileEntityChunkLoader extends GT_MetaTileEntity_TieredMachineBlock implements IGregtechPacketEntity {

	@SuppressWarnings("unused")
	private final static int yMin = 0;
	private final static int yMax = 254;
	private final int xMin, xMax;
	private final int zMin, zMax;
	
	public GregtechMetaTileEntityChunkLoader(final int aID, final String aName, final String aNameRegional, final int aTier) {
		super(aID, aName, aNameRegional, aTier, 0, "Loads chunks: " + (16 + (48 * aTier)) + " powered");
		xMin = this.xCoord-47;
		xMax = this.xCoord+47;
		zMin = this.zCoord-47;
		zMax = this.zCoord+47;
	}

	public GregtechMetaTileEntityChunkLoader(final String aName, final int aTier, final int aInvSlotCount, final String aDescription, final ITexture[][][] aTextures) {
		super(aName, aTier, aInvSlotCount, aDescription, aTextures);
		xMin = this.xCoord-47;
		xMax = this.xCoord+47;
		zMin = this.zCoord-47;
		zMax = this.zCoord+47;
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntityChunkLoader(this.mName, this.mTier, this.mInventory.length, this.mDescription, this.mTextures);
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive, final boolean aRedstone) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColorIndex + 1], (aSide != 1) ? null : aActive ? new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_TELEPORTER_ACTIVE) : new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_TELEPORTER)};
	}

	@Override
	public void onFirstTick(final IGregTechTileEntity aBaseMetaTileEntity) {
		
	}

	@Override
	public void onRemoval() {
		
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
		
	}

	@Override
	public void loadNBTData(final NBTTagCompound aNBT) {
		
	}
	
	
	
	
	/**
	 * Chunkloading Code from Railcraft
	 */

	private static final Map<UUID, Ticket> tickets = new MapMaker().makeMap();
	private static final byte MAX_CHUNKS = 25;
	private static final byte ANCHOR_RADIUS = 1;
	private int prevX;
	private int prevY;
	private int prevZ;
	private Set<ChunkCoordIntPair> chunks;
	private boolean hasTicket;
	private boolean refreshTicket;
	private boolean powered;
	private UUID uuid;
	private int xCoord, yCoord, zCoord;	

	public UUID getUUID() {
		if (this.uuid == null) {
			this.uuid = UUID.randomUUID();
		}

		return this.uuid;
	}

	private boolean sendClientUpdate = false;
	
	public void sendUpdateToClient(IGregTechTileEntity aBaseMetaTileEntity) {
		if (aBaseMetaTileEntity.hasWorkJustBeenEnabled()) {
			this.sendClientUpdate = true;
		} else {
			PacketBuilder.instance().sendTileEntityPacket(aBaseMetaTileEntity);
		}

	}

	
	@Override
	public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTimer) {
		super.onPostTick(aBaseMetaTileEntity, aTimer);
		if (!aBaseMetaTileEntity.isServerSide()) {			
			return;		
		} 
		else {
			if (this.xCoord != this.prevX || this.yCoord != this.prevY || this.zCoord != this.prevZ) {
				this.releaseTicket(aBaseMetaTileEntity);
				this.prevX = this.xCoord;
				this.prevY = this.yCoord;
				this.prevZ = this.zCoord;
			}
			
			this.powered = meetsTicketRequirements();
			if (this.hasActiveTicket() && (this.getTicket().world != aBaseMetaTileEntity.getWorld() || this.refreshTicket || !this.powered)) {
				this.releaseTicket(aBaseMetaTileEntity);
			}
			if (!this.hasActiveTicket()) {
				this.requestTicket(aBaseMetaTileEntity);
			}
		}	
		if (this.sendClientUpdate) {
			this.sendClientUpdate = false;
			PacketBuilder.instance().sendTileEntityPacket(aBaseMetaTileEntity);
		}	
	}

	public void validate() {
		this.refreshTicket = true;
	}

	protected void releaseTicket(IGregTechTileEntity aBaseMetaTileEntity) {
		this.refreshTicket = false;
		this.setTicket(aBaseMetaTileEntity, (Ticket) null);
	}

	protected void requestTicket(IGregTechTileEntity aBaseMetaTileEntity) {
		if (this.meetsTicketRequirements()) {
			Ticket chunkTicket = this.getTicketFromForge(aBaseMetaTileEntity);
			if (chunkTicket != null) {
				this.setTicketData(aBaseMetaTileEntity, chunkTicket);
				this.forceChunkLoading(aBaseMetaTileEntity, chunkTicket);
			}
		}
	}

	protected boolean meetsTicketRequirements() {
		return this.getEUVar() > (V[this.mTier]*2);
	}
	
	public Ticket getTicketFromForge(IGregTechTileEntity aBaseMetaTileEntity) {
		return ForgeChunkManager.requestTicket(GTplusplus.instance, aBaseMetaTileEntity.getWorld(), Type.NORMAL);
	}

	protected void setTicketData(IGregTechTileEntity aBaseMetaTileEntity, Ticket chunkTicket) {
		chunkTicket.getModData().setInteger("xCoord", this.xCoord);
		chunkTicket.getModData().setInteger("yCoord", this.yCoord);
		chunkTicket.getModData().setInteger("zCoord", this.zCoord);
		chunkTicket.getModData().setString("type", "StandardChunkLoader");
	}

	public boolean hasActiveTicket() {
		return this.getTicket() != null;
	}

	public Ticket getTicket() {
		return (Ticket) tickets.get(this.getUUID());
	}

	public void setTicket(IGregTechTileEntity aBaseMetaTileEntity, Ticket t) {
		boolean changed = false;
		Ticket ticket = this.getTicket();
		if (ticket != t) {
			if (ticket != null) {
				if (ticket.world == aBaseMetaTileEntity.getWorld()) {
					UnmodifiableIterator<ChunkCoordIntPair> var4 = ticket.getChunkList().iterator();

					while (var4.hasNext()) {
						ChunkCoordIntPair chunk = (ChunkCoordIntPair) var4.next();
						if (ForgeChunkManager.getPersistentChunksFor(aBaseMetaTileEntity.getWorld()).keys().contains(chunk)) {
							ForgeChunkManager.unforceChunk(ticket, chunk);
						}
					}

					ForgeChunkManager.releaseTicket(ticket);
				}

				tickets.remove(this.getUUID());
			}

			changed = true;
		}

		this.hasTicket = t != null;
		if (this.hasTicket) {
			tickets.put(this.getUUID(), t);
		}

		if (changed) {
			this.sendUpdateToClient(aBaseMetaTileEntity);
		}

	}
	
	public void forceChunkLoading(IGregTechTileEntity aBaseMetaTileEntity, Ticket ticket) {
		this.setTicket(aBaseMetaTileEntity, ticket);
		this.setupChunks();
		if (this.chunks != null) {
			Iterator<ChunkCoordIntPair> var2 = this.chunks.iterator();
			while (var2.hasNext()) {
				ChunkCoordIntPair chunk = (ChunkCoordIntPair) var2.next();
				ForgeChunkManager.forceChunk(ticket, chunk);
			}
		}
	}
	
	public void setupChunks() {
		if (!this.hasTicket) {
			this.chunks = null;
		}
		else {
			this.chunks = ChunkManager.getInstance().getChunksAround(this.xCoord >> 4, this.zCoord >> 4, 1);
		}
	}

	@Override
	public void onExplosion() {
		this.releaseTicket(this.getBaseMetaTileEntity());
		super.onExplosion();
	}

	@Override
	public void onValueUpdate(byte aValue) {
		super.onValueUpdate(aValue);
	}

	@Override
	public void onMachineBlockUpdate() {
		super.onMachineBlockUpdate();
	}

	@Override
	public void markDirty() {
		this.refreshTicket = true;
		super.markDirty();
	}

	@Override
	public boolean connectsToItemPipe(byte aSide) {
		return false;
	}

	@Override
	public void doExplosion(long aExplosionPower) {
		this.releaseTicket(this.getBaseMetaTileEntity());
		super.doExplosion(aExplosionPower);
	}

	public void writePacketData(DataOutputStream data) throws IOException {
		data.writeBoolean(this.hasTicket);
	}

	public void readPacketData(DataInputStream data) throws IOException {
		boolean tick = data.readBoolean();
		if (this.hasTicket != tick) {
			this.hasTicket = tick;
			this.markDirty();
		}
		this.setupChunks();
	}
}
