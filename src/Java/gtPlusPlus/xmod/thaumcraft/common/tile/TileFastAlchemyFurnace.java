package gtPlusPlus.xmod.thaumcraft.common.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.lib.crafting.ThaumcraftCraftingManager;
import thaumcraft.common.tiles.TileAlchemyFurnace;
import thaumcraft.common.tiles.TileAlembic;
import thaumcraft.common.tiles.TileBellows;

public class TileFastAlchemyFurnace extends TileAlchemyFurnace {
	private static final int[] slots_bottom = {1};
	private static final int[] slots_top = new int[0];
	private static final int[] slots_sides = {0};
	public AspectList aspects;
	public int vis;
	private int maxVis;
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

		this.maxVis = 100;
		this.smeltTime = 25;
		this.bellows = -1;
		this.speedBoost = false;

		this.furnaceItemStacks = new ItemStack[2];

		this.count = 0;
	}

	public int func_70302_i_() {
		return this.furnaceItemStacks.length;
	}

	public ItemStack func_70301_a(int par1) {
		return this.furnaceItemStacks[par1];
	}

	public ItemStack func_70298_a(int par1, int par2) {
		if (this.furnaceItemStacks[par1] != null) {
			if (this.furnaceItemStacks[par1].stackSize <= par2) {
				ItemStack itemstack = this.furnaceItemStacks[par1];
				this.furnaceItemStacks[par1] = null;
				return itemstack;
			}

			ItemStack itemstack = this.furnaceItemStacks[par1].splitStack(par2);

			if (this.furnaceItemStacks[par1].stackSize == 0) {
				this.furnaceItemStacks[par1] = null;
			}

			return itemstack;
		}

		return null;
	}

	public ItemStack func_70304_b(int par1) {
		if (this.furnaceItemStacks[par1] != null) {
			ItemStack itemstack = this.furnaceItemStacks[par1];
			this.furnaceItemStacks[par1] = null;
			return itemstack;
		}

		return null;
	}

	public void func_70299_a(int par1, ItemStack par2ItemStack) {
		this.furnaceItemStacks[par1] = par2ItemStack;

		if ((par2ItemStack == null) || (par2ItemStack.stackSize <= func_70297_j_()))
			return;
		par2ItemStack.stackSize = func_70297_j_();
	}

	public String func_145825_b() {
		return ((func_145818_k_()) ? this.customName : "container.alchemyfurnace");
	}

	public boolean func_145818_k_() {
		return ((this.customName != null) && (this.customName.length() > 0));
	}

	public void setGuiDisplayName(String par1Str) {
		this.customName = par1Str;
	}

	public void readCustomNBT(NBTTagCompound nbttagcompound) {
		this.furnaceBurnTime = nbttagcompound.getShort("BurnTime");
		this.vis = nbttagcompound.getShort("Vis");
	}

	public void writeCustomNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setShort("BurnTime", (short) this.furnaceBurnTime);
		nbttagcompound.setShort("Vis", (short) this.vis);
	}

	public void func_145839_a(NBTTagCompound nbtCompound) {
		super.func_145839_a(nbtCompound);
		NBTTagList nbttaglist = nbtCompound.getTagList("Items", 10);
		this.furnaceItemStacks = new ItemStack[func_70302_i_()];

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			byte b0 = nbttagcompound1.getByte("Slot");

			if ((b0 < 0) || (b0 >= this.furnaceItemStacks.length))
				continue;
			this.furnaceItemStacks[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
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

	public void func_145841_b(NBTTagCompound nbtCompound) {
		super.func_145841_b(nbtCompound);
		nbtCompound.setBoolean("speedBoost", this.speedBoost);
		nbtCompound.setShort("CookTime", (short) this.furnaceCookTime);
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < this.furnaceItemStacks.length; ++i) {
			if (this.furnaceItemStacks[i] == null)
				continue;
			NBTTagCompound nbttagcompound1 = new NBTTagCompound();
			nbttagcompound1.setByte("Slot", (byte) i);
			this.furnaceItemStacks[i].writeToNBT(nbttagcompound1);
			nbttaglist.appendTag(nbttagcompound1);
		}

		nbtCompound.setTag("Items", nbttaglist);

		if (func_145818_k_()) {
			nbtCompound.setString("CustomName", this.customName);
		}

		this.aspects.writeToNBT(nbtCompound);
	}

	public int func_70297_j_() {
		return 64;
	}

	@SideOnly(Side.CLIENT)
	public int getCookProgressScaled(int par1) {
		if (this.smeltTime <= 0)
			this.smeltTime = 1;
		return (this.furnaceCookTime * par1 / this.smeltTime);
	}

	@SideOnly(Side.CLIENT)
	public int getContentsScaled(int par1) {
		return (this.vis * par1 / this.maxVis);
	}

	@SideOnly(Side.CLIENT)
	public int getBurnTimeRemainingScaled(int par1) {
		if (this.currentItemBurnTime == 0) {
			this.currentItemBurnTime = 200;
		}

		return (this.furnaceBurnTime * par1 / this.currentItemBurnTime);
	}

	public boolean isBurning() {
		return (this.furnaceBurnTime > 0);
	}

	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		if (this.worldObj != null)
			this.worldObj.updateLightByType(EnumSkyBlock.Block, this.xCoord, this.yCoord,
					this.zCoord);
	}

	public boolean canUpdate() {
		return true;
	}

	public void func_145845_h() {
		boolean flag = this.furnaceBurnTime > 0;
		boolean flag1 = false;
		this.count += 1;
		if (this.furnaceBurnTime > 0) {
			this.furnaceBurnTime -= 1;
		}

		if (!(this.worldObj.isRemote)) {
			if (this.bellows < 0)
				getBellows();

			if ((this.count % ((this.speedBoost) ? 20 : 40) == 0) && (this.aspects.size() > 0)) {
				AspectList exlude = new AspectList();
				int deep = 0;
				TileEntity tile = null;
				while (deep < 5) {
					++deep;
					tile = this.worldObj.getTileEntity(this.xCoord, this.yCoord + deep,
							this.zCoord);
					if (!(tile instanceof TileAlembic))
						break;
					TileAlembic alembic = (TileAlembic) tile;
					if ((alembic.aspect != null) && (alembic.amount < alembic.maxAmount)
							&& (this.aspects.getAmount(alembic.aspect) > 0)) {
						takeFromContainer(alembic.aspect, 1);
						alembic.addToContainer(alembic.aspect, 1);
						exlude.merge(alembic.aspect, 1);
						this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord,
								this.zCoord);
						this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord + deep,
								this.zCoord);
					}
					tile = null;
				}

				deep = 0;
				while (deep < 5) {
					++deep;
					tile = this.worldObj.getTileEntity(this.xCoord, this.yCoord + deep,
							this.zCoord);
					if (!(tile instanceof TileAlembic))
						break;
					TileAlembic alembic = (TileAlembic) tile;
					if ((alembic.aspect == null) || (alembic.amount == 0))
						;
					Aspect as = null;
					if (alembic.aspectFilter == null) {
						as = takeRandomAspect(exlude);
					} else if (takeFromContainer(alembic.aspectFilter, 1)) {
						as = alembic.aspectFilter;
					}

					if (as != null) {
						alembic.addToContainer(as, 1);
						this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord,
								this.zCoord);
						this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord + deep,
								this.zCoord);
						break;
					}

				}

			}

			if ((this.furnaceBurnTime == 0) && (canSmelt())) {
				this.currentItemBurnTime = (this.furnaceBurnTime = TileEntityFurnace
						.getItemBurnTime(this.furnaceItemStacks[1]));

				if (this.furnaceBurnTime > 0) {
					flag1 = true;
					this.speedBoost = false;

					if (this.furnaceItemStacks[1] != null) {
						if (this.furnaceItemStacks[1].isItemEqual(new ItemStack(ConfigItems.itemResource, 1, 0))) {
							this.speedBoost = true;
						}
						this.furnaceItemStacks[1].stackSize -= 1;

						if (this.furnaceItemStacks[1].stackSize == 0) {
							this.furnaceItemStacks[1] = this.furnaceItemStacks[1].getItem()
									.getContainerItem(this.furnaceItemStacks[1]);
						}
					}
				}
			}

			if ((isBurning()) && (canSmelt())) {
				this.furnaceCookTime += 1;

				if (this.furnaceCookTime >= this.smeltTime) {
					this.furnaceCookTime = 0;
					smeltItem();
					flag1 = true;
				}
			} else {
				this.furnaceCookTime = 0;
			}

			if (flag != this.furnaceBurnTime > 0) {
				flag1 = true;
				this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
			}
		}

		if (!(flag1))
			return;
		markDirty();
	}

	private boolean canSmelt() {
		if (this.furnaceItemStacks[0] == null) {
			return false;
		}

		AspectList al = ThaumcraftCraftingManager.getObjectTags(this.furnaceItemStacks[0]);
		al = ThaumcraftCraftingManager.getBonusTags(this.furnaceItemStacks[0], al);

		if ((al == null) || (al.size() == 0))
			return false;
		int vs = al.visSize();
		if (vs > this.maxVis - this.vis)
			return false;
		this.smeltTime = (int) (vs * 10 * (1.0F - (0.125F * this.bellows)));
		return true;
	}

	public void getBellows() {
		this.bellows = TileBellows.getBellows(this.worldObj, this.xCoord, this.yCoord,
				this.zCoord, ForgeDirection.VALID_DIRECTIONS);
	}

	public void smeltItem() {
		if (!(canSmelt())) {
			return;
		}
		AspectList al = ThaumcraftCraftingManager.getObjectTags(this.furnaceItemStacks[0]);
		al = ThaumcraftCraftingManager.getBonusTags(this.furnaceItemStacks[0], al);

		for (Aspect a : al.getAspects()) {
			this.aspects.add(a, al.getAmount(a));
		}

		this.vis = this.aspects.visSize();

		this.furnaceItemStacks[0].stackSize -= 1;

		if (this.furnaceItemStacks[0].stackSize > 0)
			return;
		this.furnaceItemStacks[0] = null;
	}

	public static boolean isItemFuel(ItemStack par0ItemStack) {
		return (TileEntityFurnace.getItemBurnTime(par0ItemStack) > 0);
	}

	public boolean func_70300_a(EntityPlayer par1EntityPlayer) {
		return (this.worldObj.getTileEntity(this.xCoord, this.yCoord,
				this.zCoord) == this);
	}

	public void func_70295_k_() {
	}

	public void func_70305_f() {
	}

	public boolean func_94041_b(int par1, ItemStack par2ItemStack) {
		if (par1 == 0) {
			AspectList al = ThaumcraftCraftingManager.getObjectTags(par2ItemStack);
			al = ThaumcraftCraftingManager.getBonusTags(par2ItemStack, al);
			if ((al != null) && (al.size() > 0))
				return true;
		}
		return ((par1 == 1) ? isItemFuel(par2ItemStack) : false);
	}

	public int[] func_94128_d(int par1) {
		return ((par1 == 1) ? slots_top : (par1 == 0) ? slots_bottom : slots_sides);
	}

	public boolean func_102007_a(int par1, ItemStack par2ItemStack, int par3) {
		return ((par3 == 1) ? false : func_94041_b(par1, par2ItemStack));
	}

	public boolean func_102008_b(int par1, ItemStack par2ItemStack, int par3) {
		return ((par3 != 0) || (par1 != 1) || (par2ItemStack.getItem() == Items.bucket));
	}

	public Aspect takeRandomAspect(AspectList exlude) {
		if (this.aspects.size() > 0) {
			AspectList temp = this.aspects.copy();
			if (exlude.size() > 0)
				for (Aspect a : exlude.getAspects())
					temp.remove(a);
			if (temp.size() > 0) {
				Aspect tag = temp.getAspects()[this.worldObj.rand.nextInt(temp.getAspects().length)];
				this.aspects.remove(tag, 1);
				this.vis -= 1;
				return tag;
			}
		}
		return null;
	}

	public boolean takeFromContainer(Aspect tag, int amount) {
		if ((this.aspects != null) && (this.aspects.getAmount(tag) >= amount)) {
			this.aspects.remove(tag, amount);
			this.vis -= amount;
			return true;
		}
		return false;
	}
}