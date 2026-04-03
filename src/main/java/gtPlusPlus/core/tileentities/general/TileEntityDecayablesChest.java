package gtPlusPlus.core.tileentities.general;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.factory.GuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.item.InvWrapper;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.SyncHandlers;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.SlotGroup;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.inventories.InventoryDecayablesChest;
import gtPlusPlus.core.item.materials.DustDecayable;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class TileEntityDecayablesChest extends TileEntity implements ISidedInventory, IGuiHolder<GuiData> {

    private final InventoryDecayablesChest inventoryContents;

    /** Determines if the check for adjacent chests has taken place. */
    public float lidAngle;
    /** The angle of the lid last tick */
    public float prevLidAngle;
    /** The number of players currently using this chest */
    public int numPlayersUsing;
    private final Set<EntityPlayer> playersUsing = Collections.newSetFromMap(new WeakHashMap<>());

    private String customName;

    private int cachedChestType;
    private int tickCount = -1;
    private int facing;

    public TileEntityDecayablesChest() {
        this.inventoryContents = new InventoryDecayablesChest();
    }

    public InventoryDecayablesChest getInventory() {
        return this.inventoryContents;
    }

    @Override
    public void updateEntity() {

        // Try do chesty stuff
        try {
            this.updateEntityChest();
        } catch (Exception ignored) {

        }

        try {
            if (!this.worldObj.isRemote) {
                this.tickCount++;
                if ((this.tickCount % 10) == 0) {
                    cachedChestType = 1;
                }

                if ((this.tickCount % 20) == 0) {
                    for (ItemStack inv : this.getInventory()
                        .getInventory()) {
                        if (inv == null) {
                            continue;
                        }
                        if (inv.getItem() instanceof DustDecayable D) {
                            tryUpdateDecayable(D, inv, this.worldObj);
                        }
                    }
                }
                updateSlots();
            }
        } catch (final Exception ignored) {}
    }

    public void tryUpdateDecayable(final DustDecayable b, ItemStack iStack, final World world) {
        if (world == null || iStack == null) {
            return;
        }
        if (world.isRemote) {
            return;
        }

        boolean a1, a2;
        int u = 0;
        a1 = b.isTicking(world, iStack);
        a2 = false;
        int SECONDS_TO_PROCESS = 1;
        while (u < (20 * SECONDS_TO_PROCESS)) {
            if (!a1) {
                break;
            }
            a1 = b.isTicking(world, iStack);
            a2 = b.tickItemTag(world, iStack);
            u++;
        }
        Logger.MACHINE_INFO("| " + b.getUnlocalizedName() + " | " + a1 + "/" + a2);

        if (!a1 && !a2) {
            ItemStack replacement = b.getDecayResult();
            replacement.stackSize = 1;
            // iStack = replacement.copy();
            for (int fff = 0; fff < this.inventoryContents.getSizeInventory(); fff++) {
                if (this.inventoryContents.getStackInSlot(fff) == iStack) {
                    this.inventoryContents.setInventorySlotContents(fff, replacement.copy());
                }
            }

            updateSlots();
            this.inventoryContents.markDirty();
        }
    }

    public boolean anyPlayerInRange() {
        return this.worldObj.getClosestPlayer(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D, 32) != null;
    }

    public NBTTagCompound getTag(final NBTTagCompound nbt, final String tag) {
        if (!nbt.hasKey(tag)) {
            nbt.setTag(tag, new NBTTagCompound());
        }
        return nbt.getCompoundTag(tag);
    }

    @Override
    public void writeToNBT(final NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        // Utils.LOG_WARNING("Trying to write NBT data to TE.");
        final NBTTagCompound chestData = new NBTTagCompound();
        this.inventoryContents.writeToNBT(chestData);
        nbt.setTag("ContentsChest", chestData);
        if (this.hasCustomInventoryName()) {
            nbt.setString("CustomName", this.getCustomName());
        }
        nbt.setByte("facing", (byte) facing);
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        // Utils.LOG_WARNING("Trying to read NBT data from TE.");
        this.inventoryContents.readFromNBT(nbt.getCompoundTag("ContentsChest"));
        if (nbt.hasKey("CustomName", 8)) {
            this.setCustomName(nbt.getString("CustomName"));
        }
        facing = nbt.getByte("facing");
    }

    @Override
    public int getSizeInventory() {
        return this.getInventory()
            .getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(final int slot) {
        return this.getInventory()
            .getStackInSlot(slot);
    }

    @Override
    public ItemStack decrStackSize(final int slot, final int count) {
        return this.getInventory()
            .decrStackSize(slot, count);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(final int slot) {
        return this.getInventory()
            .getStackInSlotOnClosing(slot);
    }

    @Override
    public void setInventorySlotContents(final int slot, final ItemStack stack) {
        this.getInventory()
            .setInventorySlotContents(slot, stack);
    }

    @Override
    public int getInventoryStackLimit() {
        return this.getInventory()
            .getInventoryStackLimit();
    }

    public int getFacing() {
        return this.facing;
    }

    public void setFacing(int facing) {
        this.facing = facing;
    }

    @Override
    public boolean isUseableByPlayer(final EntityPlayer entityplayer) {
        return this.getInventory()
            .isUseableByPlayer(entityplayer);
    }

    @Override
    public void openInventory() {
        if (this.numPlayersUsing < 0) {
            this.numPlayersUsing = 0;
        }
        if (!this.worldObj.isRemote) {
            this.numPlayersUsing++;
            cachedChestType = 1;
        }
        this.worldObj
            .addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, this.numPlayersUsing);
        this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
        this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord - 1, this.zCoord, this.getBlockType());
        this.getInventory()
            .openInventory();
    }

    @Override
    public void closeInventory() {
        if (!this.worldObj.isRemote) {
            this.numPlayersUsing--;
            cachedChestType = 1;
        }
        this.worldObj
            .addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, this.numPlayersUsing);
        this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
        this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord - 1, this.zCoord, this.getBlockType());
        this.getInventory()
            .closeInventory();
    }

    @Override
    public boolean isItemValidForSlot(final int slot, final ItemStack itemstack) {
        return this.getInventory()
            .isItemValidForSlot(slot, itemstack);
    }

    @Override
    public int[] getAccessibleSlotsFromSide(final int p_94128_1_) {
        final int[] accessibleSides = new int[this.getSizeInventory()];
        for (int r = 0; r < this.getInventory()
            .getSizeInventory(); r++) {
            accessibleSides[r] = r;
        }
        return accessibleSides;
    }

    @Override
    public boolean canInsertItem(final int p_102007_1_, final ItemStack p_102007_2_, final int p_102007_3_) {
        return this.getInventory()
            .isItemValidForSlot(0, p_102007_2_);
    }

    @Override
    public boolean canExtractItem(final int p_102008_1_, final ItemStack p_102008_2_, final int p_102008_3_) {
        return this.getInventory()
            .isItemValidForSlot(0, p_102008_2_);
    }

    public String getCustomName() {
        return this.customName;
    }

    public void setCustomName(final String customName) {
        this.customName = customName;
    }

    @Override
    public String getInventoryName() {
        return this.hasCustomInventoryName() ? this.customName : "container.DecayablesChest";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return (this.customName != null) && !this.customName.isEmpty();
    }

    public void updateEntityChest() {
        float f;
        this.prevLidAngle = this.lidAngle;
        f = 0.04F;
        double d2;
        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F) {
            double d1 = (double) this.xCoord + 0.5D;
            d2 = (double) this.zCoord + 0.5D;
            this.worldObj.playSoundEffect(
                d1,
                (double) this.yCoord + 0.5D,
                d2,
                "random.chestopen",
                0.5F,
                this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (!worldObj.isRemote && tickCount < 0) {
            worldObj.addBlockEvent(
                xCoord,
                yCoord,
                zCoord,
                ModBlocks.blockDecayablesChest,
                3,
                ((numPlayersUsing << 3) & 0xF8) | (facing & 0x7));
        }

        if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F) {
            float f1 = this.lidAngle;
            if (this.numPlayersUsing > 0) {
                // this.lidAngle += f;
                this.lidAngle += (float) (f * (1 + 0.10 * 0.01));
            } else {
                // this.lidAngle -= f;
                this.lidAngle -= (float) (f * (1 + 0.10 * 0.01));
            }
            if (this.lidAngle > 1.0F) {
                this.lidAngle = 1.0F;
            }
            float f2 = 0.5F;
            if (this.lidAngle < f2 && f1 >= f2) {
                d2 = (double) this.xCoord + 0.5D;
                double d0 = (double) this.zCoord + 0.5D;
                this.worldObj.playSoundEffect(
                    d2,
                    (double) this.yCoord + 0.5D,
                    d0,
                    "random.chestclosed",
                    0.5F,
                    this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (this.lidAngle < 0.0F) {
                this.lidAngle = 0.0F;
            }
        }
    }

    /**
     * Called when a client event is received with the event number and argument, see World.sendClientEvent
     */
    @Override
    public boolean receiveClientEvent(int id, int type) {
        if (id == 1) {
            this.numPlayersUsing = type;
            return true;
        } else if (id == 2) {
            facing = (byte) type;
        } else if (id == 3) {
            facing = (byte) (type & 0x7);
            numPlayersUsing = (type & 0xF8) >> 3;
        } else {
            return super.receiveClientEvent(id, type);
        }
        return true;
    }

    /**
     * invalidates a tile entity
     */
    @Override
    public final void invalidate() {
        super.invalidate();
        cachedChestType = 1;
        this.updateContainingBlockInfo();
    }

    private int updateSlots() {
        // Have slots changed?
        if (cachedChestType == 0) {
            return 0;
        }
        ItemUtils.organiseInventory(getInventory());
        cachedChestType = 0;
        return cachedChestType;
    }

    @Override
    public ModularPanel buildUI(GuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        final SlotGroup SLOT_GROUP = new SlotGroup("decayables", 5);
        syncManager.registerSlotGroup(SLOT_GROUP);
        syncManager.addOpenListener(player -> {
            if (playersUsing.add(player)) {
                this.openInventory();
            }
        });
        syncManager.addCloseListener(player -> {
            if (playersUsing.remove(player)) {
                this.closeInventory();
            }
        });
        final InvWrapper contents = new InvWrapper(inventoryContents);
        final ModularPanel panel = ModularPanel.defaultPanel("decayablesChest");
        panel.bindPlayerInventory();
        panel.child(
            new TextWidget<>(IKey.lang("tile.blockDecayablesChest.name")).top(7)
                .left(5));
        panel.child(
            SlotGroupWidget.builder()
                .matrix("IIIII", "IIIII", "IIIII")
                .key(
                    'I',
                    index -> new ItemSlot().slot(
                        SyncHandlers.itemSlot(contents, index)
                            .slotGroup(SLOT_GROUP)))
                .build()
                .resizer(
                    a -> a.anchor(Alignment.TopCenter)
                        .leftRelAnchor(0.5f, 0.5f)
                        .topRelAnchor(0.125f, 0f)));

        return panel;
    }

    public void rotateAround(ForgeDirection axis) {
        setFacing(
            (byte) ForgeDirection.getOrientation(facing)
                .getRotation(axis)
                .ordinal());
        worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, ModBlocks.blockDecayablesChest, 2, getFacing());
    }
}
