/*package gtPlusPlus.xmod.rftools.tileentities;

import cpw.mods.fml.common.Optional.InterfaceList;
import cpw.mods.fml.common.Optional.Method;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import java.util.Map;
import java.util.Random;
import mcjty.lib.container.ContainerFactory;
import mcjty.lib.container.InventoryHelper;
import mcjty.lib.entity.GenericEnergyReceiverTileEntity;
import mcjty.lib.network.Argument;
import mcjty.lib.network.PacketRequestIntegerFromServer;
import mcjty.lib.varia.BlockTools;
import mcjty.lib.varia.Logging;
import mcjty.rftools.blocks.RedstoneMode;
import mcjty.rftools.blocks.dimlets.DimensionBuilderContainer;
import mcjty.rftools.blocks.dimlets.DimletConfiguration;
import mcjty.rftools.blocks.dimlets.DimletSetup;
import mcjty.rftools.dimension.DimensionStorage;
import mcjty.rftools.dimension.RfToolsDimensionManager;
import mcjty.rftools.dimension.description.DimensionDescriptor;
import mcjty.rftools.network.RFToolsMessages;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class TE_RFT_AdvancedWorldBuilder extends GenericEnergyReceiverTileEntity
		implements ISidedInventory {
	public static final String CMD_GETBUILDING = "getBuilding";
	public static final String CLIENTCMD_GETBUILDING = "getBuilding";
	public static final String CMD_RSMODE = "rsMode";
	public static final String COMPONENT_NAME = "dimension_builder";
	private static int buildPercentage = 0;

	private int creative = -1;
	private RedstoneMode redstoneMode = RedstoneMode.REDSTONE_IGNORED;
	private int powered = 0;

	public static int OK = 0;
	public static int ERROR_NOOWNER = -1;
	public static int ERROR_TOOMANYDIMENSIONS = -2;
	private int errorMode = 0;

	private InventoryHelper inventoryHelper = new InventoryHelper(???, DimensionBuilderContainer.factory, 1);

	private static int counter = 20;

	private static Random random = new Random();

	public TE_RFT_AdvancedWorldBuilder() {
		super(DimletConfiguration.BUILDER_MAXENERGY, DimletConfiguration.BUILDER_RECEIVEPERTICK);
	}

	private boolean isCreative() {
		if (this.creative == -1) {
			Block block = this.field_145850_b.func_147439_a(this.field_145851_c, this.field_145848_d,
					this.field_145849_e);
			if (DimletSetup.creativeDimensionBuilderBlock.equals(block))
				this.creative = 1;
			else {
				this.creative = 0;
			}
		}
		return (this.creative == 1);
	}

	protected void checkStateServer() {
		NBTTagCompound tagCompound = hasTab();
		if (tagCompound == null) {
			setState(-1);
			return;
		}

		if (this.redstoneMode != RedstoneMode.REDSTONE_IGNORED) {
			boolean rs = this.powered > 0;
			if (this.redstoneMode == RedstoneMode.REDSTONE_OFFREQUIRED) {
				if (rs) {
					setState(-1);
					return;
				}
			} else if ((this.redstoneMode == RedstoneMode.REDSTONE_ONREQUIRED) && (!(rs))) {
				setState(-1);
				return;
			}

		}

		int ticksLeft = tagCompound.getInteger("ticksLeft");
		if (ticksLeft > 0)
			ticksLeft = createDimensionTick(tagCompound, ticksLeft);
		else {
			maintainDimensionTick(tagCompound);
		}

		setState(ticksLeft);
	}

	public void setPowered(int powered) {
		if (this.powered != powered) {
			this.powered = powered;
			markDirty();
		}
	}

	private Object[] getBuildingPercentage() {
		NBTTagCompound tagCompound = hasTab();
		if (tagCompound != null) {
			int ticksLeft = tagCompound.getInteger("ticksLeft");
			int tickCost = tagCompound.getInteger("tickCost");
			int pct = (tickCost - ticksLeft) * 100 / tickCost;
			return new Object[] { Integer.valueOf(pct) };
		}
		return new Object[] { Integer.valueOf(0) };
	}

	private Object[] getDimensionPower() {
		NBTTagCompound tagCompound = hasTab();
		if (tagCompound != null) {
			int id = tagCompound.getInteger("id");
			int power = 0;
			if (id != 0) {
				DimensionStorage dimensionStorage = DimensionStorage.getDimensionStorage(this.field_145850_b);
				power = dimensionStorage.getEnergyLevel(id);
			}
			return new Object[] { Integer.valueOf(power) };
		}
		return new Object[] { Integer.valueOf(0) };
	}

	private Object[] setRedstoneMode(String mode) {
		RedstoneMode redstoneMode = RedstoneMode.getMode(mode);
		if (redstoneMode == null) {
			throw new IllegalArgumentException("Not a valid mode");
		}
		setRedstoneMode(redstoneMode);
		return null;
	}

	public RedstoneMode getRedstoneMode() {
		return this.redstoneMode;
	}

	public void setRedstoneMode(RedstoneMode redstoneMode) {
		this.redstoneMode = redstoneMode;
		this.field_145850_b.func_147471_g(this.field_145851_c, this.field_145848_d, this.field_145849_e);
		markDirty();
	}

	private NBTTagCompound hasTab() {
		ItemStack itemStack = this.inventoryHelper.getStackInSlot(0);
		if ((itemStack == null) || (itemStack.stackSize == 0)) {
			return null;
		}

		NBTTagCompound tagCompound = itemStack.getTagCompound();
		return tagCompound;
	}

	private void maintainDimensionTick(NBTTagCompound tagCompound) {
		int id = tagCompound.getInteger("id");

		if (id != 0) {
			DimensionStorage dimensionStorage = DimensionStorage.getDimensionStorage(this.field_145850_b);
			int rf;
			int rf;
			if (isCreative())
				rf = DimletConfiguration.BUILDER_MAXENERGY;
			else {
				rf = getEnergyStored(ForgeDirection.DOWN);
			}
			int energy = dimensionStorage.getEnergyLevel(id);
			int maxEnergy = DimletConfiguration.MAX_DIMENSION_POWER - energy;
			if (rf > maxEnergy) {
				rf = maxEnergy;
			}
			counter -= 1;
			if (counter < 0) {
				counter = 20;
				if (Logging.debugMode) {
					Logging.log("#################### id:" + id + ", rf:" + rf + ", energy:" + energy + ", max:"
							+ maxEnergy);
				}
			}
			if (!(isCreative())) {
				consumeEnergy(rf);
			}
			dimensionStorage.setEnergyLevel(id, energy + rf);
			dimensionStorage.save(this.field_145850_b);
		}
	}

	private int createDimensionTick(NBTTagCompound tagCompound, int ticksLeft) {
		if (DimletConfiguration.dimensionBuilderNeedsOwner) {
			if (getOwnerUUID() == null) {
				this.errorMode = ERROR_NOOWNER;
				return ticksLeft;
			}
			if (DimletConfiguration.maxDimensionsPerPlayer >= 0) {
				int tickCost = tagCompound.getInteger("tickCost");
				if ((ticksLeft == tickCost) || (ticksLeft < 5)) {
					RfToolsDimensionManager manager = RfToolsDimensionManager.getDimensionManager(this.field_145850_b);
					int cnt = manager.countOwnedDimensions(getOwnerUUID());
					if (cnt >= DimletConfiguration.maxDimensionsPerPlayer) {
						this.errorMode = ERROR_TOOMANYDIMENSIONS;
						return ticksLeft;
					}
				}
			}
		}
		this.errorMode = OK;

		int createCost = tagCompound.getInteger("rfCreateCost");
		createCost = (int) (createCost * (2.0F - getInfusedFactor()) / 2.0F);

		if ((isCreative()) || (getEnergyStored(ForgeDirection.DOWN) >= createCost)) {
			if (isCreative()) {
				ticksLeft = 0;
			} else {
				consumeEnergy(createCost);
				--ticksLeft;
				if (random.nextFloat() < getInfusedFactor()) {
					--ticksLeft;
					if (ticksLeft < 0) {
						ticksLeft = 0;
					}
				}
			}
			tagCompound.setInteger("ticksLeft", ticksLeft);
			if (ticksLeft <= 0) {
				RfToolsDimensionManager manager = RfToolsDimensionManager.getDimensionManager(this.field_145850_b);
				DimensionDescriptor descriptor = new DimensionDescriptor(tagCompound);
				String name = tagCompound.getString("name");
				int id = manager.createNewDimension(this.field_145850_b, descriptor, name, getOwnerName(),
						getOwnerUUID());
				tagCompound.setInteger("id", id);
			}
		}
		return ticksLeft;
	}

	private void setState(int ticksLeft) {
		int state = 0;
		if (ticksLeft == 0)
			state = 0;
		else if (ticksLeft == -1)
			state = 1;
		else if ((ticksLeft >> 2 & 0x1) == 0)
			state = 2;
		else {
			state = 3;
		}
		int metadata = this.field_145850_b.func_72805_g(this.field_145851_c, this.field_145848_d, this.field_145849_e);
		int newmeta = BlockTools.setState(metadata, state);
		if (newmeta != metadata)
			this.field_145850_b.func_72921_c(this.field_145851_c, this.field_145848_d, this.field_145849_e, newmeta, 2);
	}

	public int[] func_94128_d(int side) {
		return DimensionBuilderContainer.factory.getAccessibleSlots();
	}

	public boolean func_102007_a(int index, ItemStack item, int side) {
		return DimensionBuilderContainer.factory.isInputSlot(index);
	}

	public boolean func_102008_b(int index, ItemStack item, int side) {
		return DimensionBuilderContainer.factory.isOutputSlot(index);
	}

	public int func_70302_i_() {
		return this.inventoryHelper.getCount();
	}

	public ItemStack func_70301_a(int index) {
		return this.inventoryHelper.getStackInSlot(index);
	}

	public ItemStack func_70298_a(int index, int amount) {
		return this.inventoryHelper.decrStackSize(index, amount);
	}

	public ItemStack func_70304_b(int index) {
		return null;
	}

	public void func_70299_a(int index, ItemStack stack) {
		this.inventoryHelper.setInventorySlotContents(func_70297_j_(), index, stack);
	}

	public String func_145825_b() {
		return "Builder Inventory";
	}

	public boolean func_145818_k_() {
		return false;
	}

	public int func_70297_j_() {
		return 1;
	}

	public boolean func_70300_a(EntityPlayer player) {
		return canPlayerAccess(player);
	}

	public void func_70295_k_() {
	}

	public void func_70305_f() {
	}

	public boolean func_94041_b(int index, ItemStack stack) {
		return true;
	}

	public void requestBuildingPercentage() {
		RFToolsMessages.INSTANCE.sendToServer(new PacketRequestIntegerFromServer(this.field_145851_c,
				this.field_145848_d, this.field_145849_e, "getBuilding", "getBuilding", new Argument[0]));
	}

	public Integer executeWithResultInteger(String command, Map<String, Argument> args) {
		Integer rc = super.executeWithResultInteger(command, args);
		if (rc != null) {
			return rc;
		}
		if ("getBuilding".equals(command)) {
			ItemStack itemStack = this.inventoryHelper.getStackInSlot(0);
			if ((itemStack == null) || (itemStack.stackSize == 0)) {
				return Integer.valueOf(0);
			}
			NBTTagCompound tagCompound = itemStack.getTagCompound();
			if (tagCompound == null) {
				return Integer.valueOf(0);
			}
			if (this.errorMode != OK) {
				return Integer.valueOf(this.errorMode);
			}
			int ticksLeft = tagCompound.getInteger("ticksLeft");
			int tickCost = tagCompound.getInteger("tickCost");
			return Integer.valueOf((tickCost - ticksLeft) * 100 / tickCost);
		}

		return null;
	}

	public boolean execute(String command, Integer result) {
		boolean rc = super.execute(command, result);
		if (rc) {
			return true;
		}
		if ("getBuilding".equals(command)) {
			buildPercentage = result.intValue();
			return true;
		}
		return false;
	}

	public boolean execute(EntityPlayerMP playerMP, String command, Map<String, Argument> args) {
		boolean rc = super.execute(playerMP, command, args);
		if (rc) {
			return true;
		}
		if ("rsMode".equals(command)) {
			String m = ((Argument) args.get("rs")).getString();
			setRedstoneMode(RedstoneMode.getMode(m));
			return true;
		}
		return false;
	}

	public static int getBuildPercentage() {
		return buildPercentage;
	}

	public void func_145839_a(NBTTagCompound tagCompound) {
		super.func_145839_a(tagCompound);
		this.powered = tagCompound.getByte("powered");
	}

	public void readRestorableFromNBT(NBTTagCompound tagCompound) {
		super.readRestorableFromNBT(tagCompound);
		readBufferFromNBT(tagCompound);
		int m = tagCompound.getByte("rsMode");
		this.redstoneMode = RedstoneMode.values()[m];
	}

	private void readBufferFromNBT(NBTTagCompound tagCompound) {
		NBTTagList bufferTagList = tagCompound.getTagList("Items", 10);
		for (int i = 0; i < bufferTagList.tagCount(); ++i) {
			NBTTagCompound nbtTagCompound = bufferTagList.getCompoundTagAt(i);
			this.inventoryHelper.setStackInSlot(i, ItemStack.loadItemStackFromNBT(nbtTagCompound));
		}
	}

	public void func_145841_b(NBTTagCompound tagCompound) {
		super.func_145841_b(tagCompound);
		tagCompound.setByte("powered", (byte) this.powered);
	}

	public void writeRestorableToNBT(NBTTagCompound tagCompound) {
		super.writeRestorableToNBT(tagCompound);
		writeBufferToNBT(tagCompound);
		tagCompound.setByte("rsMode", (byte) this.redstoneMode.ordinal());
	}

	private void writeBufferToNBT(NBTTagCompound tagCompound) {
		NBTTagList bufferTagList = new NBTTagList();
		for (int i = 0; i < this.inventoryHelper.getCount(); ++i) {
			ItemStack stack = this.inventoryHelper.getStackInSlot(i);
			NBTTagCompound nbtTagCompound = new NBTTagCompound();
			if (stack != null) {
				stack.writeToNBT(nbtTagCompound);
			}
			bufferTagList.appendTag(nbtTagCompound);
		}
		tagCompound.setTag("Items", bufferTagList);
	}

	@Override
	public int getSizeInventory() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ItemStack getStackInSlot(int p_70301_1_) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getInventoryName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasCustomInventoryName() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void openInventory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeInventory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_, int p_102007_3_) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_) {
		// TODO Auto-generated method stub
		return false;
	}
}*/