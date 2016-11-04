package gtPlusPlus.xmod.forestry.bees.alveary;

import java.util.*;

import forestry.api.apiculture.*;
import forestry.api.arboriculture.EnumGermlingType;
import forestry.api.genetics.*;
import forestry.api.multiblock.IAlvearyComponent;
import forestry.apiculture.AlvearyBeeModifier;
import forestry.apiculture.network.packets.PacketActiveUpdate;
import forestry.apiculture.worldgen.*;
import forestry.core.inventory.IInventoryAdapter;
import forestry.core.inventory.wrappers.IInvSlot;
import forestry.core.inventory.wrappers.InventoryIterator;
import forestry.core.proxy.Proxies;
import forestry.core.tiles.IActivatable;
import forestry.core.utils.ItemStackUtil;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.forestry.bees.alveary.gui.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class TileAlvearyFrameHousing extends FR_TileAlveary implements ISidedFrameWearingInventory, IActivatable,
		IAlvearyComponent.Active, IAlvearyComponent.BeeModifier, IAlvearyFrameHousing, IAlvearyComponent.BeeListener {
	static class AlvearyFrameHousingBeeListener extends DefaultBeeListener {
		private final InventoryFrameHousing inventory;

		public AlvearyFrameHousingBeeListener(final InventoryFrameHousing inventory) {
			this.inventory = inventory;
		}

		@Override
		public boolean onPollenRetrieved(final IIndividual pollen) {
			/*
			 * if (!((Object) this.inventory).canStorePollen()) { return false;
			 * }
			 */
			final ISpeciesRoot speciesRoot = AlleleManager.alleleRegistry.getSpeciesRoot(pollen.getClass());

			final ItemStack pollenStack = speciesRoot.getMemberStack(pollen, EnumGermlingType.POLLEN.ordinal());
			if (pollenStack != null) {
				// ((Object) this.inventory).storePollenStack(pollenStack);
				return true;
			}
			return false;
		}
	}
	private final InventoryFrameHousing	inventory;
	private final IBeeListener			beeListener;
	private final Stack<ItemStack>		pendingSpawns	= new Stack<ItemStack>();

	private boolean						active;

	private final IBeeModifier				beeModifier		= new AlvearyBeeModifier();

	// private final IBeeListener beeListener = new AlvearyBeeListener(this);
	private final Iterable<IBeeListener>	beeListenerList	= this.getMultiblockLogic().getController()
			.getBeeListeners();

	public TileAlvearyFrameHousing() {
		super(FR_BlockAlveary.Type.FRAME);
		this.inventory = new InventoryFrameHousing(this);
		this.beeListener = new AlvearyFrameHousingBeeListener(this.inventory);

	}

	@Override
	public boolean allowsAutomation() {
		return true;
	}

	private int consumeInducerAndGetChance() {
		if (this.getInternalInventory() == null) {
			return 0;
		}
		for (final Iterator<?> i$ = InventoryIterator.getIterable(this.getInternalInventory()).iterator(); i$
				.hasNext();) {
			final IInvSlot slot = (IInvSlot) i$.next();
			final ItemStack stack = slot.getStackInSlot();
			for (final Map.Entry<ItemStack, Integer> entry : BeeManager.inducers.entrySet()) {
				if (ItemStackUtil.isIdenticalItem(entry.getKey(), stack)) {
					slot.decreaseStackInSlot();
					return entry.getValue().intValue();
				}
			}
		}
		final IInvSlot slot;
		final ItemStack stack;
		return 0;
	}

	@Override
	protected void decodeDescriptionPacket(final NBTTagCompound packetData) {
		super.decodeDescriptionPacket(packetData);
		this.setActive(packetData.getBoolean("Active"));
	}

	@Override
	protected void encodeDescriptionPacket(final NBTTagCompound packetData) {
		super.encodeDescriptionPacket(packetData);
		packetData.setBoolean("Active", this.active);
	}

	@Override
	public InventoryFrameHousing getAlvearyInventory() {
		return this.inventory;
	}

	@Override
	public IBeeListener getBeeListener() {
		return this.beeListener;
	}

	@Override
	public IBeeModifier getBeeModifier() {
		final List<IBeeModifier> beeModifiers = new ArrayList<IBeeModifier>();

		// beeModifiers.add(this.beeModifier);
		for (final IHiveFrame frame : this.getFrames(this.inventory)) {
			beeModifiers.add(frame.getBeeModifier());
		}
		return beeModifiers.get(0);
	}

	@Override
	public Collection<IBeeModifier> getBeeModifiers() {
		final List<IBeeModifier> beeModifiers = new ArrayList<IBeeModifier>();

		beeModifiers.add(this.beeModifier);
		for (final IHiveFrame frame : this.getFrames(this.inventory)) {
			beeModifiers.add(frame.getBeeModifier());
		}
		return beeModifiers;
	}

	@Override
	public Object getContainer(final EntityPlayer player, final int data) {
		return new CONTAINER_FrameHousing(this, player);
	}

	public Collection<IHiveFrame> getFrames(final IInventory inventory) {
		final Collection<IHiveFrame> hiveFrames = new ArrayList<IHiveFrame>(inventory.getSizeInventory());
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			final ItemStack stackInSlot = this.getStackInSlot(i);
			if (stackInSlot != null) {
				final Item itemInSlot = stackInSlot.getItem();
				if (itemInSlot instanceof IHiveFrame) {
					hiveFrames.add((IHiveFrame) itemInSlot);
				}
			}
		}
		return hiveFrames;
	}

	@Override
	public Object getGui(final EntityPlayer player, final int data) {
		return new GUI_FrameHousing(this, player);
	}

	@Override
	public int getIcon(final int side) {
		if (side == 0 || side == 1) {
			return 2;
		}
		if (this.active) {
			return 6;
		}
		return 5;
	}

	@Override
	public IInventoryAdapter getInternalInventory() {
		return this.inventory;
	}

	private ItemStack getPrincessStack() {
		final ItemStack princessStack = this.getMultiblockLogic().getController().getBeeInventory().getQueen();
		if (BeeManager.beeRoot.isMated(princessStack)) {
			return princessStack;
		}
		return null;
	}
	private ItemStack getQueenStack() {
		final ItemStack queenStack = this.getMultiblockLogic().getController().getBeeInventory().getQueen();
		return queenStack;
	}

	@Override
	public boolean isActive() {
		return this.active;
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		this.setActive(nbttagcompound.getBoolean("Active"));

		final NBTTagList nbttaglist = nbttagcompound.getTagList("PendingSpawns", 10);
		for (int i = 0; i < nbttaglist.tagCount(); i++) {
			final NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			this.pendingSpawns.add(ItemStack.loadItemStackFromNBT(nbttagcompound1));
		}
	}

	@Override
	public void setActive(final boolean active) {
		if (this.active == active) {
			return;
		}
		this.active = active;
		if (!this.worldObj.isRemote) {
			Proxies.net.sendNetworkPacket(new PacketActiveUpdate(this), this.worldObj);
		}
	}

	private void trySpawnSwarm() {
		final ItemStack toSpawn = this.pendingSpawns.peek();
		final HiveDescriptionSwarmer hiveDescription = new HiveDescriptionSwarmer(new ItemStack[] {
				toSpawn
		});
		final Hive hive = new Hive(hiveDescription);

		final int chunkX = (this.xCoord + this.worldObj.rand.nextInt(80) - 40) / 16;
		final int chunkZ = (this.zCoord + this.worldObj.rand.nextInt(80) - 40) / 16;
		if (HiveDecorator.genHive(this.worldObj, this.worldObj.rand, chunkX, chunkZ, hive)) {
			this.pendingSpawns.pop();
		}
	}

	@Override
	public void updateClient(final int tickCount) {
	}

	@Override
	public void updateServer(final int tickCount) {

		if (this.getInternalInventory() == null) {
			return;
		}

		if (this.inventory.getStackInSlot(0) != null) {
			if (this.getMultiblockLogic().getController().getBeekeepingLogic().canWork()) {
				this.setActive(true);
				if (tickCount % 1000 == 0) {
					this.wearOutFrames(this, 1);
				}
			}
			else {
				Utils.LOG_INFO("Cannot work - Probably no queen alive.");
			}

		}
		else {
			this.setActive(false);
		}
		if (tickCount % 500 != 0) {
			return;
		}

	}

	@Override
	public void wearOutFrames(final IBeeHousing beeHousing, final int amount) {
		final IBeekeepingMode beekeepingMode = BeeManager.beeRoot.getBeekeepingMode(beeHousing.getWorld());
		final int wear = Math.round(amount * beekeepingMode.getWearModifier());
		for (int i = 0; i < this.inventory.getSizeInventory(); i++) {
			final ItemStack hiveFrameStack = this.getStackInSlot(i);
			if (hiveFrameStack != null) {
				final Item hiveFrameItem = hiveFrameStack.getItem();
				if (hiveFrameItem instanceof IHiveFrame) {
					final IHiveFrame hiveFrame = (IHiveFrame) hiveFrameItem;
					Utils.LOG_INFO("Wearing out frame by " + amount);
					final ItemStack queenStack = this.getQueenStack();
					final IBee queen = BeeManager.beeRoot.getMember(queenStack);
					final ItemStack usedFrame = hiveFrame.frameUsed(beeHousing, hiveFrameStack, queen, wear);

					// ((MultiblockLogicAlveary)getMultiblockLogic()).getController().getBeeListeners().

					this.setInventorySlotContents(i, usedFrame);
				}
			}
		}
	}

	@Override
	public void writeToNBT(final NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		nbttagcompound.setBoolean("Active", this.active);

		final NBTTagList nbttaglist = new NBTTagList();
		final ItemStack[] offspring = this.pendingSpawns.toArray(new ItemStack[this.pendingSpawns.size()]);
		for (int i = 0; i < offspring.length; i++) {
			if (offspring[i] != null) {
				final NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				offspring[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		nbttagcompound.setTag("PendingSpawns", nbttaglist);
	}

}
