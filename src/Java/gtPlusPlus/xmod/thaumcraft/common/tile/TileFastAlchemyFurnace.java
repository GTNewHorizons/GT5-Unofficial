package gtPlusPlus.xmod.thaumcraft.common.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.tiles.TileAlembic;
import thaumcraft.common.tiles.TileBellows;

public class TileFastAlchemyFurnace extends TileThaumcraft implements ISidedInventory {
	private static final int[] slots_bottom;
	private static final int[] slots_top;
	private static final int[] slots_sides;
	public AspectList aspects;
	public int vis;
	private final int maxVis;
	public int smeltTime;
	int bellows;
	boolean speedBoost;
	private ItemStack[] furnaceItemStacks;
	public int furnaceBurnTime;
	public int currentItemBurnTime;
	public int furnaceCookTime;
	private String customName;
	int count;

	public TileFastAlchemyFurnace() {
		this.aspects = new AspectList();
		this.maxVis = 150;
		this.smeltTime = 100;
		this.bellows = -1;
		this.speedBoost = true;
		this.furnaceItemStacks = new ItemStack[2];
		this.count = 0;
	}

	@Override
	public int getSizeInventory() {
		return this.furnaceItemStacks.length;
	}

	@Override
	public ItemStack getStackInSlot(final int par1) {
		return this.furnaceItemStacks[par1];
	}

	@Override
	public ItemStack decrStackSize(final int par1, final int par2) {
		if (this.furnaceItemStacks[par1] == null) {
			return null;
		}
		if (this.furnaceItemStacks[par1].stackSize <= par2) {
			final ItemStack itemstack = this.furnaceItemStacks[par1];
			this.furnaceItemStacks[par1] = null;
			return itemstack;
		}
		final ItemStack itemstack = this.furnaceItemStacks[par1].splitStack(par2);
		if (this.furnaceItemStacks[par1].stackSize == 0) {
			this.furnaceItemStacks[par1] = null;
		}
		return itemstack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(final int par1) {
		if (this.furnaceItemStacks[par1] != null) {
			final ItemStack itemstack = this.furnaceItemStacks[par1];
			this.furnaceItemStacks[par1] = null;
			return itemstack;
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(final int par1, final ItemStack par2ItemStack) {
		this.furnaceItemStacks[par1] = par2ItemStack;
		if ((par2ItemStack != null) && (par2ItemStack.stackSize > this.getInventoryStackLimit())) {
			par2ItemStack.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public String getInventoryName() {
		return this.hasCustomInventoryName() ? this.customName : "container.alchemyfurnace";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return (this.customName != null) && (this.customName.length() > 0);
	}

	public void setGuiDisplayName(final String par1Str) {
		this.customName = par1Str;
	}

	@Override
	public void readCustomNBT(final NBTTagCompound nbttagcompound) {
		this.furnaceBurnTime = nbttagcompound.getShort("BurnTime");
		this.vis = nbttagcompound.getShort("Vis");
	}

	@Override
	public void writeCustomNBT(final NBTTagCompound nbttagcompound) {
		nbttagcompound.setShort("BurnTime", (short) this.furnaceBurnTime);
		nbttagcompound.setShort("Vis", (short) this.vis);
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbtCompound) {
		super.readFromNBT(nbtCompound);
		final NBTTagList nbttaglist = nbtCompound.getTagList("Items", 10);
		this.furnaceItemStacks = new ItemStack[this.getSizeInventory()];
		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			final NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			final byte b0 = nbttagcompound1.getByte("Slot");
			if ((b0 >= 0) && (b0 < this.furnaceItemStacks.length)) {
				this.furnaceItemStacks[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
		this.speedBoost = nbtCompound.getBoolean("speedBoost");
		this.furnaceCookTime = nbtCompound.getShort("CookTime");
		this.currentItemBurnTime = TileEntityFurnace.getItemBurnTime(this.furnaceItemStacks[1]);
		if (nbtCompound.hasKey("CustomName")) {
			this.customName = nbtCompound.getString("CustomName");
		}
		this.aspects.readFromNBT(nbtCompound);
		this.vis = this.aspects.visSize();
	}

	@Override
	public void writeToNBT(final NBTTagCompound nbtCompound) {
		super.writeToNBT(nbtCompound);
		nbtCompound.setBoolean("speedBoost", this.speedBoost);
		nbtCompound.setShort("CookTime", (short) this.furnaceCookTime);
		final NBTTagList nbttaglist = new NBTTagList();
		for (int i = 0; i < this.furnaceItemStacks.length; ++i) {
			if (this.furnaceItemStacks[i] != null) {
				final NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				this.furnaceItemStacks[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		nbtCompound.setTag("Items", nbttaglist);
		if (this.hasCustomInventoryName()) {
			nbtCompound.setString("CustomName", this.customName);
		}
		this.aspects.writeToNBT(nbtCompound);
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@SideOnly(Side.CLIENT)
	public int getCookProgressScaled(final int par1) {
		if (this.smeltTime <= 0) {
			this.smeltTime = 1;
		}
		return (this.furnaceCookTime * par1) / this.smeltTime;
	}

	@SideOnly(Side.CLIENT)
	public int getContentsScaled(final int par1) {
		return (this.vis * par1) / this.maxVis;
	}

	@SideOnly(Side.CLIENT)
	public int getBurnTimeRemainingScaled(final int par1) {
		if (this.currentItemBurnTime == 0) {
			this.currentItemBurnTime = 200;
		}
		return (this.furnaceBurnTime * par1) / this.currentItemBurnTime;
	}

	public boolean isBurning() {
		return this.furnaceBurnTime > 0;
	}

	@Override
	public void onDataPacket(final NetworkManager net, final S35PacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		if (this.worldObj != null) {
			this.worldObj.updateLightByType(EnumSkyBlock.Block, this.xCoord, this.yCoord, this.zCoord);
		}
	}

	@Override
	public boolean canUpdate() {
		return true;
	}

	@Override
	public void updateEntity() {
		final boolean flag = this.furnaceBurnTime > 0;
		boolean flag2 = false;
		++this.count;
		if (this.furnaceBurnTime > 0) {
			--this.furnaceBurnTime;
		}
		if (!this.worldObj.isRemote) {
			if (this.bellows < 0) {
				this.getBellows();
			}
			if (((this.count % (this.speedBoost ? 10 : 20)) == 0) && (this.aspects.size() > 0)) {
				final AspectList exlude = new AspectList();
				int deep = 0;
				TileEntity tile = null;
				while (deep < 5) {
					++deep;
					tile = this.worldObj.getTileEntity(this.xCoord, this.yCoord + deep, this.zCoord);
					if (!(tile instanceof TileAlembic) || !(tile instanceof TileFastArcaneAlembic)) {
						break;
					}

					final TileAlembic alembic = (TileAlembic) tile;
					if ((alembic.aspect != null) && (alembic.amount < alembic.maxAmount)
							&& (this.aspects.getAmount(alembic.aspect) > 0)) {
						this.takeFromContainer(alembic.aspect, 1);
						alembic.addToContainer(alembic.aspect, 1);
						exlude.merge(alembic.aspect, 1);
						this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
						this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord + deep, this.zCoord);
					}
					tile = null;
				}
				deep = 0;
				while (deep < 5) {
					++deep;
					tile = this.worldObj.getTileEntity(this.xCoord, this.yCoord + deep, this.zCoord);
					if (!(tile instanceof TileAlembic)) {
						break;
					}
					final TileAlembic alembic = (TileAlembic) tile;
					if ((alembic.aspect != null) && (alembic.amount != 0)) {
						continue;
					}
					Aspect as = null;
					if (alembic.aspectFilter == null) {
						as = this.takeRandomAspect(exlude);
					} else if (this.takeFromContainer(alembic.aspectFilter, 1)) {
						as = alembic.aspectFilter;
					}
					if (as != null) {
						alembic.addToContainer(as, 1);
						this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
						this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord + deep, this.zCoord);
						break;
					}
				}
			}
			if ((this.furnaceBurnTime == 0) && this.canSmelt()) {
				final int itemBurnTime = TileEntityFurnace.getItemBurnTime(this.furnaceItemStacks[1]);
				this.furnaceBurnTime = itemBurnTime;
				this.currentItemBurnTime = itemBurnTime;
				if (this.furnaceBurnTime > 0) {
					flag2 = true;
					this.speedBoost = false;
					if (this.furnaceItemStacks[1] != null) {
						if (this.furnaceItemStacks[1].isItemEqual(new ItemStack(ConfigItems.itemResource, 1, 0))) {
							this.speedBoost = true;
						}
						final ItemStack itemStack = this.furnaceItemStacks[1];
						--itemStack.stackSize;
						if (this.furnaceItemStacks[1].stackSize == 0) {
							this.furnaceItemStacks[1] = this.furnaceItemStacks[1].getItem()
									.getContainerItem(this.furnaceItemStacks[1]);
						}
					}
				}
			}
			if (this.isBurning() && this.canSmelt()) {
				++this.furnaceCookTime;
				if (this.furnaceCookTime >= this.smeltTime) {
					this.furnaceCookTime = 0;
					this.smeltItem();
					flag2 = true;
				}
			} else {
				this.furnaceCookTime = 0;
			}
			if (flag != (this.furnaceBurnTime > 0)) {
				flag2 = true;
				this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
			}
		}
		if (flag2) {
			this.markDirty();
		}
	}

	private boolean canSmelt() {
		if (this.furnaceItemStacks[0] == null) {
			return false;
		}
		AspectList al = ThaumcraftCraftingManager.getObjectTags(this.furnaceItemStacks[0]);
		al = ThaumcraftCraftingManager.getBonusTags(this.furnaceItemStacks[0], al);
		if ((al == null) || (al.size() == 0)) {
			return false;
		}
		final int vs = al.visSize();
		if (vs > (this.maxVis - this.vis)) {
			return false;
		}
		this.smeltTime = (int) (vs * 10 * (1.0f - (0.125f * this.bellows)));
		return true;
	}

	public void getBellows() {
		this.bellows = TileBellows.getBellows(this.worldObj, this.xCoord, this.yCoord, this.zCoord,
				ForgeDirection.VALID_DIRECTIONS);
	}

	public void smeltItem() {
		if (this.canSmelt()) {
			AspectList al = ThaumcraftCraftingManager.getObjectTags(this.furnaceItemStacks[0]);
			al = ThaumcraftCraftingManager.getBonusTags(this.furnaceItemStacks[0], al);
			for (final Aspect a : al.getAspects()) {
				this.aspects.add(a, al.getAmount(a));
			}
			this.vis = this.aspects.visSize();
			final ItemStack itemStack = this.furnaceItemStacks[0];
			--itemStack.stackSize;
			if (this.furnaceItemStacks[0].stackSize <= 0) {
				this.furnaceItemStacks[0] = null;
			}
		}
	}

	public static boolean isItemFuel(final ItemStack par0ItemStack) {
		return TileEntityFurnace.getItemBurnTime(par0ItemStack) > 0;
	}

	@Override
	public boolean isUseableByPlayer(final EntityPlayer par1EntityPlayer) {
		return (this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this)
				&& (par1EntityPlayer.getDistanceSq(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5) <= 64.0);
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public boolean isItemValidForSlot(final int par1, final ItemStack par2ItemStack) {
		if (par1 == 0) {
			AspectList al = ThaumcraftCraftingManager.getObjectTags(par2ItemStack);
			al = ThaumcraftCraftingManager.getBonusTags(par2ItemStack, al);
			if ((al != null) && (al.size() > 0)) {
				return true;
			}
		}
		return (par1 == 1) && isItemFuel(par2ItemStack);
	}

	@Override
	public int[] getAccessibleSlotsFromSide(final int par1) {
		return (par1 == 0)
				? TileFastAlchemyFurnace.slots_bottom
						: ((par1 == 1) ? TileFastAlchemyFurnace.slots_top : TileFastAlchemyFurnace.slots_sides);
	}

	@Override
	public boolean canInsertItem(final int par1, final ItemStack par2ItemStack, final int par3) {
		return (par3 != 1) && this.isItemValidForSlot(par1, par2ItemStack);
	}

	@Override
	public boolean canExtractItem(final int par1, final ItemStack par2ItemStack, final int par3) {
		return (par3 != 0) || (par1 != 1) || (par2ItemStack.getItem() == Items.bucket);
	}

	public Aspect takeRandomAspect(final AspectList exlude) {
		if (this.aspects.size() > 0) {
			final AspectList temp = this.aspects.copy();
			if (exlude.size() > 0) {
				for (final Aspect a : exlude.getAspects()) {
					temp.remove(a);
				}
			}
			if (temp.size() > 0) {
				final Aspect tag = temp.getAspects()[this.worldObj.rand.nextInt(temp.getAspects().length)];
				this.aspects.remove(tag, 1);
				--this.vis;
				return tag;
			}
		}
		return null;
	}

	public boolean takeFromContainer(final Aspect tag, final int amount) {
		if ((this.aspects != null) && (this.aspects.getAmount(tag) >= amount)) {
			this.aspects.remove(tag, amount);
			this.vis -= amount;
			return true;
		}
		return false;
	}

	static {
		slots_bottom = new int[]{1};
		slots_top = new int[0];
		slots_sides = new int[]{0};
	}
}