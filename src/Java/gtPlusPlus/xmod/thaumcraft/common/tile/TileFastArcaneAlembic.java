package gtPlusPlus.xmod.thaumcraft.common.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.xmod.thaumcraft.common.block.TC_BlockHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileAlembic;

public class TileFastArcaneAlembic extends TileAlembic {
	ForgeDirection fd;

	public TileFastArcaneAlembic() {
		this.aspectFilter = null;
		this.amount = 0;
		this.maxAmount = 64;
		this.facing = 2;
		this.aboveAlembic = false;
		this.aboveFurnace = false;
		this.fd = null;
	}

	@Override
	public AspectList getAspects() {
		return (this.aspect != null) ? new AspectList().add(this.aspect, this.amount) : new AspectList();
	}

	@Override
	public void setAspects(final AspectList aspects) {
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox() {
		return AxisAlignedBB.getBoundingBox(this.xCoord - 1, this.yCoord,
				this.zCoord - 1, this.xCoord + 2, this.yCoord + 1,
				this.zCoord + 2);
	}

	@Override
	public void readCustomNBT(final NBTTagCompound nbttagcompound) {
		this.facing = nbttagcompound.getByte("facing");
		this.aspectFilter = Aspect.getAspect(nbttagcompound.getString("AspectFilter"));
		final String tag = nbttagcompound.getString("aspect");
		if (tag != null) {
			this.aspect = Aspect.getAspect(tag);
		}
		this.amount = nbttagcompound.getShort("amount");
		this.fd = ForgeDirection.getOrientation(this.facing);
	}

	@Override
	public void writeCustomNBT(final NBTTagCompound nbttagcompound) {
		if (this.aspect != null) {
			nbttagcompound.setString("aspect", this.aspect.getTag());
		}
		if (this.aspectFilter != null) {
			nbttagcompound.setString("AspectFilter", this.aspectFilter.getTag());
		}
		nbttagcompound.setShort("amount", (short) this.amount);
		nbttagcompound.setByte("facing", (byte) this.facing);
	}

	@Override
	public boolean canUpdate() {
		return false;
	}

	@Override
	public int addToContainer(final Aspect tt, int am) {
		if (((this.amount < this.maxAmount) && (tt == this.aspect)) || (this.amount == 0)) {
			this.aspect = tt;
			final int added = Math.min(am, this.maxAmount - this.amount);
			this.amount += added;
			am -= added;
		}
		this.markDirty();
		this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
		return am;
	}

	@Override
	public boolean takeFromContainer(final Aspect tt, final int am) {
		if ((this.amount == 0) || (this.aspect == null)) {
			this.aspect = null;
			this.amount = 0;
		}
		if ((this.aspect != null) && (this.amount >= am) && (tt == this.aspect)) {
			this.amount -= am;
			if (this.amount <= 0) {
				this.aspect = null;
				this.amount = 0;
			}
			this.markDirty();
			this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
			return true;
		}
		return false;
	}

	@Override
	public boolean doesContainerContain(final AspectList ot) {
		return (this.amount > 0) && (this.aspect != null) && (ot.getAmount(this.aspect) > 0);
	}

	@Override
	public boolean doesContainerContainAmount(final Aspect tt, final int am) {
		return (this.amount >= am) && (tt == this.aspect);
	}

	@Override
	public int containerContains(final Aspect tt) {
		return (tt == this.aspect) ? this.amount : 0;
	}

	@Override
	public boolean doesContainerAccept(final Aspect tag) {
		return true;
	}

	@Override
	public boolean takeFromContainer(final AspectList ot) {
		return false;
	}

	@Override
	public void getAppearance() {
		this.aboveAlembic = false;
		this.aboveFurnace = false;
		if ((this.worldObj.getBlock(this.xCoord, this.yCoord - 1, this.zCoord) == ConfigBlocks.blockStoneDevice)
				&& (this.worldObj.getBlockMetadata(this.xCoord, this.yCoord - 1, this.zCoord) == 0)) {
			this.aboveFurnace = true;
		}
		else if ((this.worldObj.getBlock(this.xCoord, this.yCoord - 1, this.zCoord) == TC_BlockHandler.blockFastAlchemyFurnace)
				&& (this.worldObj.getBlockMetadata(this.xCoord, this.yCoord - 1, this.zCoord) == 0)) {
			this.aboveFurnace = true;
		}

		if ((this.worldObj.getBlock(this.xCoord, this.yCoord - 1, this.zCoord) == ConfigBlocks.blockMetalDevice)
				&& (this.worldObj.getBlockMetadata(this.xCoord, this.yCoord - 1, this.zCoord) == this
				.getBlockMetadata())) {
			this.aboveAlembic = true;
		}
		else if ((this.worldObj.getBlock(this.xCoord, this.yCoord - 1, this.zCoord) == TC_BlockHandler.blockFastArcaneAlembic)
				&& (this.worldObj.getBlockMetadata(this.xCoord, this.yCoord - 1, this.zCoord) == 1)) {
			this.aboveAlembic = true;
		}
	}

	@Override
	public void onDataPacket(final NetworkManager net, final S35PacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		this.getAppearance();
	}

	@Override
	public int onWandRightClick(final World world, final ItemStack wandstack, final EntityPlayer player, final int x,
			final int y, final int z, final int side, final int md) {
		if (side <= 1) {
			return 0;
		}
		this.facing = side;
		this.fd = ForgeDirection.getOrientation(this.facing);
		this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
		player.swingItem();
		this.markDirty();
		return 0;
	}

	@Override
	public ItemStack onWandRightClick(final World world, final ItemStack wandstack, final EntityPlayer player) {
		return null;
	}

	@Override
	public void onUsingWandTick(final ItemStack wandstack, final EntityPlayer player, final int count) {
	}

	@Override
	public void onWandStoppedUsing(final ItemStack wandstack, final World world, final EntityPlayer player,
			final int count) {
	}

	@Override
	public boolean isConnectable(final ForgeDirection face) {
		return (face != ForgeDirection.getOrientation(this.facing)) && (face != ForgeDirection.DOWN);
	}

	@Override
	public boolean canInputFrom(final ForgeDirection face) {
		return false;
	}

	@Override
	public boolean canOutputTo(final ForgeDirection face) {
		return (face != ForgeDirection.getOrientation(this.facing)) && (face != ForgeDirection.DOWN);
	}

	@Override
	public void setSuction(final Aspect aspect, final int amount) {
	}

	@Override
	public Aspect getSuctionType(final ForgeDirection loc) {
		return null;
	}

	@Override
	public int getSuctionAmount(final ForgeDirection loc) {
		return 0;
	}

	@Override
	public Aspect getEssentiaType(final ForgeDirection loc) {
		return this.aspect;
	}

	@Override
	public int getEssentiaAmount(final ForgeDirection loc) {
		return this.amount;
	}

	@Override
	public int takeEssentia(final Aspect aspect, final int amount, final ForgeDirection face) {
		return (this.canOutputTo(face) && this.takeFromContainer(aspect, amount)) ? amount : 0;
	}

	@Override
	public int addEssentia(final Aspect aspect, final int amount, final ForgeDirection loc) {
		return 0;
	}

	@Override
	public int getMinimumSuction() {
		return 0;
	}

	@Override
	public boolean renderExtendedTube() {
		return true;
	}
}