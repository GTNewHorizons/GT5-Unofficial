package miscutil.core.block.machine.heliumgen.tileentity;

import ic2.api.Direction;
import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;
import ic2.api.tile.IWrenchable;
import ic2.core.IC2;
import ic2.core.IC2DamageSource;
import ic2.core.Ic2Items;
import ic2.core.block.TileEntityInventory;
import ic2.core.init.MainConfig;
import ic2.core.item.reactor.ItemReactorHeatStorage;
import ic2.core.network.NetworkManager;
import ic2.core.util.ConfigUtil;

import java.util.List;

import miscutil.core.block.ModBlocks;
import miscutil.core.block.machine.heliumgen.slots.InvSlotRadiation;
import miscutil.core.item.ModItems;
import miscutil.core.util.Utils;
import miscutil.core.util.item.UtilsItems;
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

public class TileEntityHeliumGenerator extends TileEntityInventory implements IInventory ,IReactor, IWrenchable {

	private ItemStack heliumStack;
	private int facing = 2;
	private int progress;

	@Override
	public void updateEntity(){
		Utils.LOG_WARNING("updateEntity");
		if(++progress >= 40){
			//if(++progress >= 300){
			if(heliumStack == null)
				heliumStack = UtilsItems.getSimpleStack(ModItems.itemHeliumBlob);
			else if(heliumStack.getItem() == ModItems.itemHeliumBlob && heliumStack.stackSize < 64)
				heliumStack.stackSize++;
			progress = 0;
			markDirty();
		}
	}

	@Override
	public short getFacing(){
		return (short) facing;
	}

	@Override
	public void setFacing(short dir){
		facing = dir;
	}

	/*@Override
	public void readCustomNBT(NBTTagCompound tag)
	{
		this.heliumStack = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Helium"));
		this.progress = tag.getInteger("Progress");
		this.facing = tag.getShort("Facing");
		this.heat = tag.getInteger("heat");
		this.prevActive = (this.active = tag.getBoolean("active"));
	}

	@Override
	public void writeCustomNBT(NBTTagCompound tag)
	{
		tag.setInteger("Progress", this.progress);
		tag.setShort("Facing", (short) this.facing);
		tag.setInteger("heat", this.heat);
		tag.setBoolean("active", this.active);
		if(heliumStack != null) {
			NBTTagCompound produce = new NBTTagCompound();
			heliumStack.writeToNBT(produce);
			tag.setTag("Helium", produce);
		}
		else
			tag.removeTag("Helium");
	}*/


	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound)
	{
		super.readFromNBT(nbttagcompound);

		//this.heliumStack = ItemStack.loadItemStackFromNBT(nbttagcompound.getCompoundTag("Helium"));
		NBTTagList list = nbttagcompound.getTagList("Items", 10);
	    for (int i = 0; i < list.tagCount(); ++i) {
	        NBTTagCompound stackTag = list.getCompoundTagAt(i);
	        int slot = stackTag.getByte("Slot") & 255;
	        this.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(stackTag));
	    }
		this.progress = nbttagcompound.getInteger("Progress");
		this.facing = nbttagcompound.getShort("Facing");
		this.heat = nbttagcompound.getInteger("heat");
		this.output = nbttagcompound.getShort("output");
		this.prevActive = (this.active = nbttagcompound.getBoolean("active"));
	}

	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound)
	{
		super.writeToNBT(nbttagcompound);

		nbttagcompound.setInteger("Progress", this.progress);
		nbttagcompound.setShort("Facing", (short) this.facing);
		nbttagcompound.setInteger("heat", this.heat);
		nbttagcompound.setShort("output", (short)(int)getReactorEnergyOutput());
		nbttagcompound.setBoolean("active", this.active);
		/*if(heliumStack != null) {
			NBTTagCompound produce = new NBTTagCompound();
			heliumStack.writeToNBT(produce);
			nbttagcompound.setTag("Helium", produce);
		}
		else
			nbttagcompound.removeTag("Helium");*/
		NBTTagList list = new NBTTagList();
	    for (int i = 0; i < this.getSizeInventory(); ++i) {
	        if (this.getStackInSlot(i) != null) {
	            NBTTagCompound stackTag = new NBTTagCompound();
	            stackTag.setByte("Slot", (byte) i);
	            this.getStackInSlot(i).writeToNBT(stackTag);
	            list.appendTag(stackTag);
	        }
	    }
	    nbttagcompound.setTag("Items", list);
	}


	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, -999, tag);
	}

	@Override
	public void onDataPacket(net.minecraft.network.NetworkManager net, S35PacketUpdateTileEntity packet) {
		super.onDataPacket(net, packet);
		readFromNBT(packet.func_148857_g());
	}


	@Override
	public int getSizeInventory()
	{
		return 19;
	}

	@Override
	public ItemStack getStackInSlot(int slot){
		return heliumStack;
	}

	@Override
	public ItemStack decrStackSize(int slot, int decrement){
		Utils.LOG_WARNING("decrStackSize");
		if(heliumStack == null)
			return null;
		if(decrement < heliumStack.stackSize){
			ItemStack take = heliumStack.splitStack(decrement);
			if(heliumStack.stackSize <= 0)
				heliumStack = null;
			return take;
		}
		ItemStack take = heliumStack;
		heliumStack = null;
		return take;
	}

	@Override
	public void openInventory() {}
	@Override
	public void closeInventory() {}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && player.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack){
		return false;
	}

	@Override
	public int getInventoryStackLimit(){
		return 64;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack){
		heliumStack = stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot){
		return null;
	}

	/**
	 * Returns the name of the inventory
	 */
	@Override
	public String getInventoryName()
	{
		//return  "container.helium_collector";
		return  "container.helium_collector";
	}

	/**
	 * Returns if the inventory is named
	 */
	@Override
	public boolean hasCustomInventoryName()
	{
		return false;
	}

	//IC2 Nuclear Code


	public Block[][][] surroundings = new Block[5][5][5];
	public final InvSlotRadiation reactorSlot;
	public float output = 0.0F;
	public int updateTicker;
	public int heat = 5000;
	public int maxHeat = 100000;
	public float hem = 1.0F;
	private int EmitHeatbuffer = 0;
	public int EmitHeat = 0;
	private boolean redstone = false;
	private boolean fluidcoolreactor = false;
	private boolean active = true;
	public boolean prevActive = false;


	public short getReactorSize()
	{
		//Utils.LOG_WARNING("getReactorSize");
		if (this.worldObj == null) {
			Utils.LOG_WARNING("getReactorSize == 9");
			return 9;
		}
		short cols = 3;
		//Utils.LOG_WARNING("getReactorSize == "+cols);
		for (Direction direction : Direction.directions)
		{
			TileEntity target = direction.applyToTileEntity(this);
			if ((target instanceof TileEntityHeliumGenerator)) {
				cols = (short)(cols + 1);
				Utils.LOG_WARNING("getReactorSize =1= "+cols);
			}
		}
		//Utils.LOG_WARNING("getReactorSize == "+cols);
		return cols;
	}

	protected void updateEntityServer()
	{
		Utils.LOG_WARNING("updateEntityServer");
		super.updateEntity();

		if (this.updateTicker++ % getTickRate() != 0) {
			return;
		}
		if (!this.worldObj.doChunksNearChunkExist(this.xCoord, this.yCoord, this.zCoord, 2))
		{
			this.output = 0.0F;
		}
		else
		{

			dropAllUnfittingStuff();

			this.output = 0.0F;
			this.maxHeat = 10000;
			this.hem = 1.0F;

			processChambers();       
			this.EmitHeatbuffer = 0;
			if (calculateHeatEffects()) {
				return;
			}
			setActive((this.heat >= 1000) || (this.output > 0.0F));

			markDirty();
		}
		((NetworkManager)IC2.network.get()).updateTileEntityField(this, "output");
	}

	@Override
	public void setActive(boolean active1)
	{
		Utils.LOG_WARNING("setActive");
		this.active = active1;
		if (this.prevActive != active1) {
			((NetworkManager)IC2.network.get()).updateTileEntityField(this, "active");
		}
		this.prevActive = active1;
	}

	public void dropAllUnfittingStuff()
	{
		Utils.LOG_WARNING("dropAllUnfittingStuff");
		for (int i = 0; i < this.reactorSlot.size(); i++)
		{
			ItemStack stack = this.reactorSlot.get(i);
			if ((stack != null) && (!isUsefulItem(stack, false)))
			{
				this.reactorSlot.put(i, null);
				eject(stack);
			}
		}
		for (int i = this.reactorSlot.size(); i < this.reactorSlot.rawSize(); i++)
		{
			ItemStack stack = this.reactorSlot.get(i);

			this.reactorSlot.put(i, null);
			eject(stack);
		}
	}

	public void eject(ItemStack drop)
	{
		Utils.LOG_WARNING("eject");
		if ((!IC2.platform.isSimulating()) || (drop == null)) {
			return;
		}
		float f = 0.7F;
		double d = this.worldObj.rand.nextFloat() * f + (1.0F - f) * 0.5D;
		double d1 = this.worldObj.rand.nextFloat() * f + (1.0F - f) * 0.5D;
		double d2 = this.worldObj.rand.nextFloat() * f + (1.0F - f) * 0.5D;
		EntityItem entityitem = new EntityItem(this.worldObj, this.xCoord + d, this.yCoord + d1, this.zCoord + d2, drop);
		entityitem.delayBeforeCanPickup = 10;
		this.worldObj.spawnEntityInWorld(entityitem);
	}

	public boolean isUsefulItem(ItemStack stack, boolean forInsertion)
	{
		//Utils.LOG_WARNING("isUsefulItem");
		Item item = stack.getItem();
		if ((forInsertion) && (this.fluidcoolreactor) && 
				((item instanceof ItemReactorHeatStorage)) && 
				(((ItemReactorHeatStorage)item).getCustomDamage(stack) > 0)) {
			return false;
		}
		if ((item instanceof IReactorComponent)) {
			return true;
		}
		return (item == Ic2Items.TritiumCell.getItem()) || (item == Ic2Items.reactorDepletedUraniumSimple.getItem()) || (item == Ic2Items.reactorDepletedUraniumDual.getItem()) || (item == Ic2Items.reactorDepletedUraniumQuad.getItem()) || (item == Ic2Items.reactorDepletedMOXSimple.getItem()) || (item == Ic2Items.reactorDepletedMOXDual.getItem()) || (item == Ic2Items.reactorDepletedMOXQuad.getItem());
	}

	public boolean calculateHeatEffects()
	{
		Utils.LOG_WARNING("calculateHeatEffects");
		if ((this.heat < 8000) || (!IC2.platform.isSimulating()) || (ConfigUtil.getFloat(MainConfig.get(), "protection/reactorExplosionPowerLimit") <= 0.0F)) {
			return false;
		}
		float power = this.heat / this.maxHeat;
		if (power >= 1.0F)
		{
			explode();
			return true;
		}
		if ((power >= 0.85F) && (this.worldObj.rand.nextFloat() <= 0.2F * this.hem))
		{
			int[] coord = getRandCoord(2);
			if (coord != null)
			{
				Block block = this.worldObj.getBlock(coord[0], coord[1], coord[2]);
				if (block.isAir(this.worldObj, coord[0], coord[1], coord[2]))
				{
					this.worldObj.setBlock(coord[0], coord[1], coord[2], Blocks.fire, 0, 7);
				}
				else if ((block.getBlockHardness(this.worldObj, coord[0], coord[1], coord[2]) >= 0.0F) && (this.worldObj.getTileEntity(coord[0], coord[1], coord[2]) == null))
				{
					Material mat = block.getMaterial();
					if ((mat == Material.rock) || (mat == Material.iron) || (mat == Material.lava) || (mat == Material.ground) || (mat == Material.clay)) {
						this.worldObj.setBlock(coord[0], coord[1], coord[2], Blocks.flowing_lava, 15, 7);
					} else {
						this.worldObj.setBlock(coord[0], coord[1], coord[2], Blocks.fire, 0, 7);
					}
				}
			}
		}
		if (power >= 0.7F)
		{
			List list1 = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(this.xCoord - 3, this.yCoord - 3, this.zCoord - 3, this.xCoord + 4, this.yCoord + 4, this.zCoord + 4));
			for (int l = 0; l < list1.size(); l++)
			{
				Entity ent = (Entity)list1.get(l);
				ent.attackEntityFrom(IC2DamageSource.radiation, (int)(this.worldObj.rand.nextInt(4) * this.hem));
			}
		}
		if ((power >= 0.5F) && (this.worldObj.rand.nextFloat() <= this.hem))
		{
			int[] coord = getRandCoord(2);
			if (coord != null)
			{
				Block block = this.worldObj.getBlock(coord[0], coord[1], coord[2]);
				if (block.getMaterial() == Material.water) {
					this.worldObj.setBlockToAir(coord[0], coord[1], coord[2]);
				}
			}
		}
		if ((power >= 0.4F) && (this.worldObj.rand.nextFloat() <= this.hem))
		{
			int[] coord = getRandCoord(2);
			if ((coord != null) && 
					(this.worldObj.getTileEntity(coord[0], coord[1], coord[2]) == null))
			{
				Block block = this.worldObj.getBlock(coord[0], coord[1], coord[2]);
				Material mat = block.getMaterial();
				if ((mat == Material.wood) || (mat == Material.leaves) || (mat == Material.cloth)) {
					this.worldObj.setBlock(coord[0], coord[1], coord[2], Blocks.fire, 0, 7);
				}
			}
		}
		return false;
	}

	public int[] getRandCoord(int radius)
	{
		if (radius <= 0) {
			return null;
		}
		int[] c = new int[3];
		c[0] = (this.xCoord + this.worldObj.rand.nextInt(2 * radius + 1) - radius);
		c[1] = (this.yCoord + this.worldObj.rand.nextInt(2 * radius + 1) - radius);
		c[2] = (this.zCoord + this.worldObj.rand.nextInt(2 * radius + 1) - radius);
		if ((c[0] == this.xCoord) && (c[1] == this.yCoord) && (c[2] == this.zCoord)) {
			return null;
		}
		return c;
	}

	public void processChambers()
	{
		Utils.LOG_WARNING("processChambers");
		int size = getReactorSize();
		for (int pass = 0; pass < 6; pass++) {
			for (int y = 0; y < 3; y++) {
				for (int x = 0; x < size; x++)
				{
					ItemStack stack = this.reactorSlot.get(x, y);
					if ((stack != null) && ((stack.getItem() instanceof IReactorComponent)))
					{
						IReactorComponent comp = (IReactorComponent)stack.getItem();
						comp.processChamber(this, stack, x, y, pass == 0);
					}
				}
			}
		}
	}

	@Override
	public ChunkCoordinates getPosition()
	{
		return new ChunkCoordinates(this.xCoord, this.yCoord, this.zCoord);
	}

	@Override
	public World getWorld() {
		return this.worldObj;
	}

	@Override
	public int getHeat() {
		return this.heat;
	}

	@Override
	public void setHeat(int heat1)
	{
		this.heat = heat1;
	}

	@Override
	public int addHeat(int amount)
	{
		this.heat += amount;
		return this.heat;
	}

	@Override
	public int getMaxHeat()
	{
		return this.maxHeat;
	}

	@Override
	public void setMaxHeat(int newMaxHeat)
	{
		this.maxHeat = newMaxHeat;
	}

	@Override
	public void addEmitHeat(int heat)
	{
		this.EmitHeatbuffer += heat;
	}

	@Override
	public float getHeatEffectModifier()
	{
		return this.hem;
	}

	@Override
	public void setHeatEffectModifier(float newHEM)
	{
		this.hem = newHEM;
	}

	@Override
	public float getReactorEnergyOutput()
	{
		return this.output;
	}

	@Override
	public double getReactorEUEnergyOutput()
	{
		return getOfferedEnergy();
	}

	public double getOfferedEnergy()
	{
		return getReactorEnergyOutput() * 5.0F * 1.0F;
	}

	@Override
	public float addOutput(float energy)
	{
		return this.output += energy;
	}

	@Override
	public ItemStack getItemAt(int x, int y)
	{
		Utils.LOG_WARNING("getItemAt");
		if ((x < 0) || (x >= getReactorSize()) || (y < 0) || (y >= 6)) {
			return null;
		}
		return this.reactorSlot.get(x, y);
	}

	@Override
	public void setItemAt(int x, int y, ItemStack item)
	{
		Utils.LOG_WARNING("setItemAt");
		if ((x < 0) || (x >= getReactorSize()) || (y < 0) || (y >= 6)) {
			return;
		}
		this.reactorSlot.put(x, y, item);
	}

	public TileEntityHeliumGenerator() {
		this.updateTicker = IC2.random.nextInt(getTickRate());	    
		this.reactorSlot = new InvSlotRadiation(this, "helium_collector", 0, 54); //TODO
	}

	@Override
	public void explode() {
		Utils.LOG_WARNING("Explosion");
		//TODO
	}

	@Override
	public int getTickRate()
	{
		return 20;
	}

	public boolean receiveredstone()
	{
		Utils.LOG_WARNING("receiveRedstone");
		if ((this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord)) || (this.redstone)) {
			decrStackSize(-1, 1);
			return true;
		}
		return false;
	}

	@Override
	public boolean produceEnergy()
	{
		Utils.LOG_WARNING("produceEnergy");
		return (receiveredstone()) && (ConfigUtil.getFloat(MainConfig.get(), "balance/energy/generator/generator") > 0.0F);
	}

	@Override
	public void setRedstoneSignal(boolean redstone)
	{
		this.redstone = redstone;
	}

	@Override
	public boolean isFluidCooled() {
		Utils.LOG_WARNING("isFluidCooled");
		return false;
	}

	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side)
	{
		return true;
	}

	@Override
	public boolean wrenchCanRemove(EntityPlayer entityPlayer)
	{
		return true;
	}

	@Override
	public float getWrenchDropRate()
	{
		return 1F;
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer)
	{
		return new ItemStack(ModBlocks.blockHeliumGenerator, 1);
	}

}
