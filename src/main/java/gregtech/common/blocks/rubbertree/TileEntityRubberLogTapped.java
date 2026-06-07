package gregtech.common.blocks.rubbertree;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.tileentity.IGregtechWailaProvider;
import gregtech.common.items.ItemRubberTreeTap;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class TileEntityRubberLogTapped extends TileEntity implements IGregtechWailaProvider {

    private static final String NBT_TAP = "Tap";
    private static final String NBT_SIDE = "Side";
    private static final String NBT_TICKS_UNTIL_NEXT_DROP = "TicksUntilNextDrop";
    private static final String NBT_REMAINING_RESIN = "RemainingResin";
    private static final String WAILA_REMAINING_RESIN = "RemainingResin";
    private static final String WAILA_HAS_INSTALLED_TAP = "HasInstalledTap";
    private static final String WAILA_TAP_DAMAGE = "TapDamage";
    private static final String WAILA_TAP_MAX_DAMAGE = "TapMaxDamage";

    private static final int UNINITIALIZED_REMAINING_RESIN = -1;

    private ItemStack tapStack;
    private int side = -1;
    private int ticksUntilNextDrop;
    private int remainingResin = UNINITIALIZED_REMAINING_RESIN;

    public void configureNewHole(@NotNull ItemStack installedTap, int side, @NotNull Random random,
        int initialRemainingResin) {
        this.remainingResin = Math.max(0, initialRemainingResin);
        installTap(installedTap, side, random);
    }

    public void installTap(@NotNull ItemStack installedTap, int side, @NotNull Random random) {
        this.tapStack = installedTap.copy();
        this.tapStack.stackSize = 1;
        this.side = side;
        this.ticksUntilNextDrop = RubberTreeResinLogic.nextTapDelay(random, this.remainingResin);

        markDirtyAndUpdate();
    }

    public boolean hasInstalledTap() {
        return isInstalledTapValid();
    }

    public boolean canAcceptTap() {
        return this.tapStack == null || this.tapStack.stackSize <= 0;
    }

    public boolean isReservoirInitialized() {
        return this.remainingResin >= 0;
    }

    public int getRemainingResin() {
        return this.remainingResin;
    }

    public void setRemainingResin(int remainingResin) {
        int normalized = Math.max(0, remainingResin);

        if (this.remainingResin != normalized) {
            this.remainingResin = normalized;
            markDirtyAndUpdate();
        }
    }

    public void setSide(int side) {
        if (this.side != side) {
            this.side = side;
            markDirtyAndUpdate();
        }
    }

    public @Nullable ItemStack removeInstalledTapForDrop() {
        ItemStack drop = getTapStackForDrop();

        this.tapStack = null;
        this.ticksUntilNextDrop = 0;

        markDirtyAndUpdate();

        return drop;
    }

    public void clearInstalledTap() {
        this.tapStack = null;
        this.ticksUntilNextDrop = 0;

        markDirtyAndUpdate();
    }

    public @Nullable ItemStack getTapStackForDrop() {
        if (this.tapStack == null || this.tapStack.stackSize <= 0) {
            return null;
        }

        ItemStack drop = this.tapStack.copy();
        drop.stackSize = 1;
        return drop;
    }

    @Override
    public void updateEntity() {
        if (this.worldObj == null || this.worldObj.isRemote) {
            return;
        }

        Block block = this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord);
        if (!(block instanceof BlockRubberLogNatural)) {
            return;
        }

        int meta = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
        if (!BlockRubberLogNatural.hasTappedHole(meta)) {
            return;
        }

        if (this.side != meta) {
            this.side = meta;
            markDirty();
        }

        if (!isInstalledTapValid()) {
            if (this.tapStack != null) {
                clearInstalledTap();
            }
            return;
        }

        if (!RubberTreeResinLogic.isLowerTrunkPosition(this.worldObj, this.xCoord, this.yCoord, this.zCoord)) {
            return;
        }

        int remainingBeforeDrop = RubberTreeResinLogic
            .getSharedRemainingResin(this.worldObj, this.xCoord, this.yCoord, this.zCoord);

        if (remainingBeforeDrop == 0) {
            if (this.ticksUntilNextDrop != 0) {
                this.ticksUntilNextDrop = 0;
                markDirty();
            }

            return;
        }

        if (this.ticksUntilNextDrop > 0) {
            this.ticksUntilNextDrop--;
            return;
        }

        boolean produced = RubberTreeResinLogic
            .produceFromTap(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.side, this.worldObj.rand);

        if (!produced) {
            this.ticksUntilNextDrop = 0;
            markDirty();
            return;
        }

        if (damageTapAndCheckBroken()) {
            clearInstalledTap();
            return;
        }

        int remainingAfterDrop = RubberTreeResinLogic
            .getSharedRemainingResin(this.worldObj, this.xCoord, this.yCoord, this.zCoord);

        this.ticksUntilNextDrop = RubberTreeResinLogic.nextTapDelay(this.worldObj.rand, remainingAfterDrop);
        markDirty();
    }

    private boolean isInstalledTapValid() {
        return this.tapStack != null && this.tapStack.stackSize > 0
            && this.tapStack.getItem() instanceof ItemRubberTreeTap
            && this.tapStack.getItemDamage() < this.tapStack.getMaxDamage();
    }

    private boolean damageTapAndCheckBroken() {
        if (this.tapStack == null || !this.tapStack.isItemStackDamageable()) {
            return false;
        }

        this.tapStack.setItemDamage(this.tapStack.getItemDamage() + 1);
        markDirty();

        return this.tapStack.getItemDamage() >= this.tapStack.getMaxDamage();
    }

    private void markDirtyAndUpdate() {
        markDirty();

        if (this.worldObj == null) {
            return;
        }

        if (this.worldObj.isRemote) {
            this.worldObj.markBlockRangeForRenderUpdate(
                this.xCoord,
                this.yCoord,
                this.zCoord,
                this.xCoord,
                this.yCoord,
                this.zCoord);
        } else {
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        }
    }

    @Override
    public void readFromNBT(@NotNull NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        this.side = nbt.getInteger(NBT_SIDE);
        this.ticksUntilNextDrop = nbt.getInteger(NBT_TICKS_UNTIL_NEXT_DROP);
        this.remainingResin = nbt.hasKey(NBT_REMAINING_RESIN) ? nbt.getInteger(NBT_REMAINING_RESIN)
            : UNINITIALIZED_REMAINING_RESIN;

        if (nbt.hasKey(NBT_TAP)) {
            this.tapStack = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag(NBT_TAP));
        } else {
            this.tapStack = null;
        }
    }

    @Override
    public void writeToNBT(@NotNull NBTTagCompound nbt) {
        super.writeToNBT(nbt);

        nbt.setInteger(NBT_SIDE, this.side);
        nbt.setInteger(NBT_TICKS_UNTIL_NEXT_DROP, this.ticksUntilNextDrop);
        nbt.setInteger(NBT_REMAINING_RESIN, this.remainingResin);

        if (this.tapStack != null) {
            NBTTagCompound tapTag = new NBTTagCompound();
            this.tapStack.writeToNBT(tapTag);
            nbt.setTag(NBT_TAP, tapTag);
        }
    }

    // Waila integration
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getWailaBody(ItemStack itemStack, List currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        NBTTagCompound tag = accessor.getNBTData();

        int remaining = tag.getInteger(WAILA_REMAINING_RESIN);
        boolean hasTap = tag.getBoolean(WAILA_HAS_INSTALLED_TAP);

        if (remaining > 0) {
            currenttip.add(
                EnumChatFormatting.GREEN
                    + StatCollector.translateToLocalFormatted("gt.waila.rubber_tree.remaining_resin", remaining));
        } else {
            currenttip.add(EnumChatFormatting.RED + StatCollector.translateToLocal("gt.waila.rubber_tree.exhausted"));
        }

        if (hasTap) {
            int damage = tag.getInteger(WAILA_TAP_DAMAGE);
            int maxDamage = tag.getInteger(WAILA_TAP_MAX_DAMAGE);
            int durabilityLeft = Math.max(0, maxDamage - damage);

            currenttip.add(
                EnumChatFormatting.GRAY + StatCollector
                    .translateToLocalFormatted("gt.waila.rubber_tree.tap_durability", durabilityLeft, maxDamage));
        } else {
            currenttip.add(EnumChatFormatting.YELLOW + StatCollector.translateToLocal("gt.waila.rubber_tree.no_tap"));
        }
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        tag.setInteger(WAILA_REMAINING_RESIN, Math.max(0, this.remainingResin));

        boolean hasTap = isInstalledTapValid();
        tag.setBoolean(WAILA_HAS_INSTALLED_TAP, hasTap);

        if (hasTap && this.tapStack != null) {
            tag.setInteger(WAILA_TAP_DAMAGE, this.tapStack.getItemDamage());
            tag.setInteger(WAILA_TAP_MAX_DAMAGE, this.tapStack.getMaxDamage());
        } else {
            tag.setInteger(WAILA_TAP_DAMAGE, 0);
            tag.setInteger(WAILA_TAP_MAX_DAMAGE, 0);
        }
    }

    // 3D special renderer
    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager networkManager, S35PacketUpdateTileEntity packet) {
        readFromNBT(packet.func_148857_g());

        if (this.worldObj != null) {
            this.worldObj.markBlockRangeForRenderUpdate(
                this.xCoord,
                this.yCoord,
                this.zCoord,
                this.xCoord,
                this.yCoord,
                this.zCoord);
        }
    }

    public int getSideForRender() {
        return this.side;
    }

    public ItemStack getTapStackForRender() {
        return this.tapStack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return AxisAlignedBB.getBoundingBox(
            this.xCoord - 1,
            this.yCoord,
            this.zCoord - 1,
            this.xCoord + 2,
            this.yCoord + 1,
            this.zCoord + 2);
    }
}
