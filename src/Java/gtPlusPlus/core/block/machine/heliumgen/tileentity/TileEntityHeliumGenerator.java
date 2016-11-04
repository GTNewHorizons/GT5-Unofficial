package gtPlusPlus.core.block.machine.heliumgen.tileentity;

import java.util.List;

import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.block.machine.heliumgen.slots.InvSlotRadiation;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import ic2.api.Direction;
import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;
import ic2.api.tile.IWrenchable;
import ic2.core.*;
import ic2.core.block.TileEntityInventory;
import ic2.core.init.MainConfig;
import ic2.core.item.reactor.ItemReactorHeatStorage;
import ic2.core.util.ConfigUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class TileEntityHeliumGenerator extends TileEntityInventory implements IInventory, IReactor, IWrenchable {

	private ItemStack	heliumStack;
	private int			facing	= 2;
	private int			progress;

	public Block[][][]				surroundings		= new Block[5][5][5];

	public final InvSlotRadiation	reactorSlot;

	public float					output				= 0.0F;

	/*
	 * @Override public void readCustomNBT(NBTTagCompound tag) {
	 * this.heliumStack =
	 * ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Helium"));
	 * this.progress = tag.getInteger("Progress"); this.facing =
	 * tag.getShort("Facing"); this.heat = tag.getInteger("heat");
	 * this.prevActive = (this.active = tag.getBoolean("active")); }
	 * 
	 * @Override public void writeCustomNBT(NBTTagCompound tag) {
	 * tag.setInteger("Progress", this.progress); tag.setShort("Facing", (short)
	 * this.facing); tag.setInteger("heat", this.heat); tag.setBoolean("active",
	 * this.active); if(heliumStack != null) { NBTTagCompound produce = new
	 * NBTTagCompound(); heliumStack.writeToNBT(produce); tag.setTag("Helium",
	 * produce); } else tag.removeTag("Helium"); }
	 */

	public int						updateTicker;

	public int						heat				= 5000;

	public int						maxHeat				= 100000;

	public float					hem					= 1.0F;

	private int						EmitHeatbuffer		= 0;

	public int						EmitHeat			= 0;

	private boolean					redstone			= false;

	private final boolean			fluidcoolreactor	= false;

	private boolean					active				= true;

	public boolean					prevActive			= false;

	public TileEntityHeliumGenerator() {
		this.updateTicker = IC2.random.nextInt(this.getTickRate());
		this.reactorSlot = new InvSlotRadiation(this, "helium_collector", 0, 54); // TODO
	}

	@Override
	public void addEmitHeat(final int heat) {
		this.EmitHeatbuffer += heat;
	}

	@Override
	public int addHeat(final int amount) {
		this.heat += amount;
		return this.heat;
	}

	@Override
	public float addOutput(final float energy) {
		return this.output += energy;
	}

	public boolean calculateHeatEffects() {
		Utils.LOG_WARNING("calculateHeatEffects");
		if (this.heat < 8000 || !IC2.platform.isSimulating()
				|| ConfigUtil.getFloat(MainConfig.get(), "protection/reactorExplosionPowerLimit") <= 0.0F) {
			return false;
		}
		final float power = this.heat / this.maxHeat;
		if (power >= 1.0F) {
			this.explode();
			return true;
		}
		if (power >= 0.85F && this.worldObj.rand.nextFloat() <= 0.2F * this.hem) {
			final int[] coord = this.getRandCoord(2);
			if (coord != null) {
				final Block block = this.worldObj.getBlock(coord[0], coord[1], coord[2]);
				if (block.isAir(this.worldObj, coord[0], coord[1], coord[2])) {
					this.worldObj.setBlock(coord[0], coord[1], coord[2], Blocks.fire, 0, 7);
				}
				else if (block.getBlockHardness(this.worldObj, coord[0], coord[1], coord[2]) >= 0.0F
						&& this.worldObj.getTileEntity(coord[0], coord[1], coord[2]) == null) {
					final Material mat = block.getMaterial();
					if (mat == Material.rock || mat == Material.iron || mat == Material.lava || mat == Material.ground
							|| mat == Material.clay) {
						this.worldObj.setBlock(coord[0], coord[1], coord[2], Blocks.flowing_lava, 15, 7);
					}
					else {
						this.worldObj.setBlock(coord[0], coord[1], coord[2], Blocks.fire, 0, 7);
					}
				}
			}
		}
		if (power >= 0.7F) {
			final List list1 = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class,
					AxisAlignedBB.getBoundingBox(this.xCoord - 3, this.yCoord - 3, this.zCoord - 3, this.xCoord + 4,
							this.yCoord + 4, this.zCoord + 4));
			for (int l = 0; l < list1.size(); l++) {
				final Entity ent = (Entity) list1.get(l);
				ent.attackEntityFrom(IC2DamageSource.radiation, (int) (this.worldObj.rand.nextInt(4) * this.hem));
			}
		}
		if (power >= 0.5F && this.worldObj.rand.nextFloat() <= this.hem) {
			final int[] coord = this.getRandCoord(2);
			if (coord != null) {
				final Block block = this.worldObj.getBlock(coord[0], coord[1], coord[2]);
				if (block.getMaterial() == Material.water) {
					this.worldObj.setBlockToAir(coord[0], coord[1], coord[2]);
				}
			}
		}
		if (power >= 0.4F && this.worldObj.rand.nextFloat() <= this.hem) {
			final int[] coord = this.getRandCoord(2);
			if (coord != null && this.worldObj.getTileEntity(coord[0], coord[1], coord[2]) == null) {
				final Block block = this.worldObj.getBlock(coord[0], coord[1], coord[2]);
				final Material mat = block.getMaterial();
				if (mat == Material.wood || mat == Material.leaves || mat == Material.cloth) {
					this.worldObj.setBlock(coord[0], coord[1], coord[2], Blocks.fire, 0, 7);
				}
			}
		}
		return false;
	}

	@Override
	public void closeInventory() {
	}

	// IC2 Nuclear Code

	@Override
	public ItemStack decrStackSize(final int slot, final int decrement) {
		Utils.LOG_WARNING("decrStackSize");
		if (this.heliumStack == null) {
			return null;
		}
		if (decrement < this.heliumStack.stackSize) {
			final ItemStack take = this.heliumStack.splitStack(decrement);
			if (this.heliumStack.stackSize <= 0) {
				this.heliumStack = null;
			}
			return take;
		}
		final ItemStack take = this.heliumStack;
		this.heliumStack = null;
		return take;
	}
	public void dropAllUnfittingStuff() {
		Utils.LOG_WARNING("dropAllUnfittingStuff");
		for (int i = 0; i < this.reactorSlot.size(); i++) {
			final ItemStack stack = this.reactorSlot.get(i);
			if (stack != null && !this.isUsefulItem(stack, false)) {
				this.reactorSlot.put(i, null);
				this.eject(stack);
			}
		}
		for (int i = this.reactorSlot.size(); i < this.reactorSlot.rawSize(); i++) {
			final ItemStack stack = this.reactorSlot.get(i);

			this.reactorSlot.put(i, null);
			this.eject(stack);
		}
	}
	public void eject(final ItemStack drop) {
		Utils.LOG_WARNING("eject");
		if (!IC2.platform.isSimulating() || drop == null) {
			return;
		}
		final float f = 0.7F;
		final double d = this.worldObj.rand.nextFloat() * f + (1.0F - f) * 0.5D;
		final double d1 = this.worldObj.rand.nextFloat() * f + (1.0F - f) * 0.5D;
		final double d2 = this.worldObj.rand.nextFloat() * f + (1.0F - f) * 0.5D;
		final EntityItem entityitem = new EntityItem(this.worldObj, this.xCoord + d, this.yCoord + d1, this.zCoord + d2,
				drop);
		entityitem.delayBeforeCanPickup = 10;
		this.worldObj.spawnEntityInWorld(entityitem);
	}
	@Override
	public void explode() {
		Utils.LOG_WARNING("Explosion");
		// TODO
	}
	@Override
	public Packet getDescriptionPacket() {
		final NBTTagCompound tag = new NBTTagCompound();
		this.writeToNBT(tag);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, -999, tag);
	}
	@Override
	public short getFacing() {
		return (short) this.facing;
	}
	@Override
	public int getHeat() {
		return this.heat;
	}
	@Override
	public float getHeatEffectModifier() {
		return this.hem;
	}
	/**
	 * Returns the name of the inventory
	 */
	@Override
	public String getInventoryName() {
		// return "container.helium_collector";
		return "container.helium_collector";
	}
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	@Override
	public ItemStack getItemAt(final int x, final int y) {
		Utils.LOG_WARNING("getItemAt");
		if (x < 0 || x >= this.getReactorSize() || y < 0 || y >= 6) {
			return null;
		}
		return this.reactorSlot.get(x, y);
	}
	@Override
	public int getMaxHeat() {
		return this.maxHeat;
	}
	public double getOfferedEnergy() {
		return this.getReactorEnergyOutput() * 5.0F * 1.0F;
	}

	@Override
	public ChunkCoordinates getPosition() {
		return new ChunkCoordinates(this.xCoord, this.yCoord, this.zCoord);
	}

	public int[] getRandCoord(final int radius) {
		if (radius <= 0) {
			return null;
		}
		final int[] c = new int[3];
		c[0] = this.xCoord + this.worldObj.rand.nextInt(2 * radius + 1) - radius;
		c[1] = this.yCoord + this.worldObj.rand.nextInt(2 * radius + 1) - radius;
		c[2] = this.zCoord + this.worldObj.rand.nextInt(2 * radius + 1) - radius;
		if (c[0] == this.xCoord && c[1] == this.yCoord && c[2] == this.zCoord) {
			return null;
		}
		return c;
	}

	@Override
	public float getReactorEnergyOutput() {
		return this.output;
	}

	@Override
	public double getReactorEUEnergyOutput() {
		return this.getOfferedEnergy();
	}

	public short getReactorSize() {
		// Utils.LOG_WARNING("getReactorSize");
		if (this.worldObj == null) {
			Utils.LOG_WARNING("getReactorSize == 9");
			return 9;
		}
		short cols = 3;
		// Utils.LOG_WARNING("getReactorSize == "+cols);
		for (final Direction direction : Direction.directions) {
			final TileEntity target = direction.applyToTileEntity(this);
			if (target instanceof TileEntityHeliumGenerator) {
				cols = (short) (cols + 1);
				Utils.LOG_WARNING("getReactorSize =1= " + cols);
			}
		}
		// Utils.LOG_WARNING("getReactorSize == "+cols);
		return cols;
	}

	@Override
	public int getSizeInventory() {
		return 19;
	}

	@Override
	public ItemStack getStackInSlot(final int slot) {
		return this.heliumStack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(final int slot) {
		return null;
	}

	@Override
	public int getTickRate() {
		return 20;
	}

	@Override
	public World getWorld() {
		return this.worldObj;
	}

	@Override
	public ItemStack getWrenchDrop(final EntityPlayer entityPlayer) {
		return new ItemStack(ModBlocks.blockHeliumGenerator, 1);
	}

	@Override
	public float getWrenchDropRate() {
		return 1F;
	}

	/**
	 * Returns if the inventory is named
	 */
	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public boolean isFluidCooled() {
		Utils.LOG_WARNING("isFluidCooled");
		return false;
	}

	@Override
	public boolean isItemValidForSlot(final int slot, final ItemStack stack) {
		return false;
	}

	@Override
	public boolean isUseableByPlayer(final EntityPlayer player) {
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this
				&& player.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64.0D;
	}

	public boolean isUsefulItem(final ItemStack stack, final boolean forInsertion) {
		// Utils.LOG_WARNING("isUsefulItem");
		final Item item = stack.getItem();
		if (forInsertion && this.fluidcoolreactor && item instanceof ItemReactorHeatStorage
				&& ((ItemReactorHeatStorage) item).getCustomDamage(stack) > 0) {
			return false;
		}
		if (item instanceof IReactorComponent) {
			return true;
		}
		return item == Ic2Items.TritiumCell.getItem() || item == Ic2Items.reactorDepletedUraniumSimple.getItem()
				|| item == Ic2Items.reactorDepletedUraniumDual.getItem()
				|| item == Ic2Items.reactorDepletedUraniumQuad.getItem()
				|| item == Ic2Items.reactorDepletedMOXSimple.getItem()
				|| item == Ic2Items.reactorDepletedMOXDual.getItem()
				|| item == Ic2Items.reactorDepletedMOXQuad.getItem();
	}

	@Override
	public void onDataPacket(final net.minecraft.network.NetworkManager net, final S35PacketUpdateTileEntity packet) {
		super.onDataPacket(net, packet);
		this.readFromNBT(packet.func_148857_g());
	}

	@Override
	public void openInventory() {
	}

	public void processChambers() {
		Utils.LOG_WARNING("processChambers");
		final int size = this.getReactorSize();
		for (int pass = 0; pass < 6; pass++) {
			for (int y = 0; y < 3; y++) {
				for (int x = 0; x < size; x++) {
					final ItemStack stack = this.reactorSlot.get(x, y);
					if (stack != null && stack.getItem() instanceof IReactorComponent) {
						final IReactorComponent comp = (IReactorComponent) stack.getItem();
						comp.processChamber(this, stack, x, y, pass == 0);
					}
				}
			}
		}
	}

	@Override
	public boolean produceEnergy() {
		Utils.LOG_WARNING("produceEnergy");
		return this.receiveredstone()
				&& ConfigUtil.getFloat(MainConfig.get(), "balance/energy/generator/generator") > 0.0F;
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);

		// this.heliumStack =
		// ItemStack.loadItemStackFromNBT(nbttagcompound.getCompoundTag("Helium"));
		final NBTTagList list = nbttagcompound.getTagList("Items", 10);
		for (int i = 0; i < list.tagCount(); ++i) {
			final NBTTagCompound stackTag = list.getCompoundTagAt(i);
			final int slot = stackTag.getByte("Slot") & 255;
			this.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(stackTag));
		}
		this.progress = nbttagcompound.getInteger("Progress");
		this.facing = nbttagcompound.getShort("Facing");
		this.heat = nbttagcompound.getInteger("heat");
		this.output = nbttagcompound.getShort("output");
		this.prevActive = this.active = nbttagcompound.getBoolean("active");
	}

	public boolean receiveredstone() {
		Utils.LOG_WARNING("receiveRedstone");
		if (this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord) || this.redstone) {
			this.decrStackSize(-1, 1);
			return true;
		}
		return false;
	}

	@Override
	public void setActive(final boolean active1) {
		Utils.LOG_WARNING("setActive");
		this.active = active1;
		if (this.prevActive != active1) {
			IC2.network.get().updateTileEntityField(this, "active");
		}
		this.prevActive = active1;
	}

	@Override
	public void setFacing(final short dir) {
		this.facing = dir;
	}

	@Override
	public void setHeat(final int heat1) {
		this.heat = heat1;
	}

	@Override
	public void setHeatEffectModifier(final float newHEM) {
		this.hem = newHEM;
	}

	@Override
	public void setInventorySlotContents(final int slot, final ItemStack stack) {
		this.heliumStack = stack;
	}

	@Override
	public void setItemAt(final int x, final int y, final ItemStack item) {
		Utils.LOG_WARNING("setItemAt");
		if (x < 0 || x >= this.getReactorSize() || y < 0 || y >= 6) {
			return;
		}
		this.reactorSlot.put(x, y, item);
	}

	@Override
	public void setMaxHeat(final int newMaxHeat) {
		this.maxHeat = newMaxHeat;
	}

	@Override
	public void setRedstoneSignal(final boolean redstone) {
		this.redstone = redstone;
	}

	public void update2Entity() {
		Utils.LOG_WARNING("updateEntity");
		if (++this.progress >= 40) {
			// if(++progress >= 300){
			if (this.heliumStack == null) {
				this.heliumStack = ItemUtils.getSimpleStack(ModItems.itemHeliumBlob);
			}
			else if (this.heliumStack.getItem() == ModItems.itemHeliumBlob && this.heliumStack.stackSize < 64) {
				this.heliumStack.stackSize++;
			}
			this.progress = 0;
			this.markDirty();
		}
	}

	@Override
	protected void updateEntityServer() {
		Utils.LOG_WARNING("updateEntityServer");
		super.updateEntity();

		if (this.updateTicker++ % this.getTickRate() != 0) {
			return;
		}
		if (!this.worldObj.doChunksNearChunkExist(this.xCoord, this.yCoord, this.zCoord, 2)) {
			this.output = 0.0F;
		}
		else {

			this.dropAllUnfittingStuff();

			this.output = 0.0F;
			this.maxHeat = 10000;
			this.hem = 1.0F;

			this.processChambers();
			this.EmitHeatbuffer = 0;
			if (this.calculateHeatEffects()) {
				return;
			}
			this.setActive(this.heat >= 1000 || this.output > 0.0F);

			this.markDirty();
		}
		IC2.network.get().updateTileEntityField(this, "output");
	}

	@Override
	public boolean wrenchCanRemove(final EntityPlayer entityPlayer) {
		return true;
	}

	@Override
	public boolean wrenchCanSetFacing(final EntityPlayer entityPlayer, final int side) {
		return true;
	}

	@Override
	public void writeToNBT(final NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);

		nbttagcompound.setInteger("Progress", this.progress);
		nbttagcompound.setShort("Facing", (short) this.facing);
		nbttagcompound.setInteger("heat", this.heat);
		nbttagcompound.setShort("output", (short) (int) this.getReactorEnergyOutput());
		nbttagcompound.setBoolean("active", this.active);
		/*
		 * if(heliumStack != null) { NBTTagCompound produce = new
		 * NBTTagCompound(); heliumStack.writeToNBT(produce);
		 * nbttagcompound.setTag("Helium", produce); } else
		 * nbttagcompound.removeTag("Helium");
		 */
		final NBTTagList list = new NBTTagList();
		for (int i = 0; i < this.getSizeInventory(); ++i) {
			if (this.getStackInSlot(i) != null) {
				final NBTTagCompound stackTag = new NBTTagCompound();
				stackTag.setByte("Slot", (byte) i);
				this.getStackInSlot(i).writeToNBT(stackTag);
				list.appendTag(stackTag);
			}
		}
		nbttagcompound.setTag("Items", list);
	}

}
