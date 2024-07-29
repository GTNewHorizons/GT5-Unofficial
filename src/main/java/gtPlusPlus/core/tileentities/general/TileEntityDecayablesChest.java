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

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.factory.GuiData;
import com.cleanroommc.modularui.future.InvWrapper;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.GuiSyncManager;
import com.cleanroommc.modularui.value.sync.SyncHandlers;
import com.cleanroommc.modularui.widgets.ItemSlot;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.slot.SlotGroup;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.inventories.Inventory_DecayablesChest;
import gtPlusPlus.core.item.materials.DustDecayable;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class TileEntityDecayablesChest extends TileEntity implements ISidedInventory, IGuiHolder {

    private final Inventory_DecayablesChest inventoryContents;

    /** Determines if the check for adjacent chests has taken place. */
    public boolean adjacentChestChecked;
    /** Contains the chest tile located adjacent to this one (if any) */
    public TileEntityDecayablesChest adjacentChestZNeg;
    /** Contains the chest tile located adjacent to this one (if any) */
    public TileEntityDecayablesChest adjacentChestXPos;
    /** Contains the chest tile located adjacent to this one (if any) */
    public TileEntityDecayablesChest adjacentChestXNeg;
    /** Contains the chest tile located adjacent to this one (if any) */
    public TileEntityDecayablesChest adjacentChestZPos;
    /** The current angle of the lid (between 0 and 1) */
    public float lidAngle;
    /** The angle of the lid last tick */
    public float prevLidAngle;
    /** The number of players currently using this chest */
    public int numPlayersUsing;
    private final Set<EntityPlayer> playersUsing = Collections.newSetFromMap(new WeakHashMap<>());

    private String customName;

    private int cachedChestType;
    private int tickCount = 0;

    public TileEntityDecayablesChest() {
        this.inventoryContents = new Inventory_DecayablesChest();
    }

    public Inventory_DecayablesChest getInventory() {
        return this.inventoryContents;
    }

    @Override
    public void updateEntity() {

        // Try do chesty stuff
        try {
            this.updateEntityChest();
        } catch (Throwable t) {

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
        } catch (final Throwable t) {}
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
            ItemStack replacement = ItemUtils.getSimpleStack(b.getDecayResult());
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
    }

    @Override
    public void readFromNBT(final NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        // Utils.LOG_WARNING("Trying to read NBT data from TE.");
        this.inventoryContents.readFromNBT(nbt.getCompoundTag("ContentsChest"));
        if (nbt.hasKey("CustomName", 8)) {
            this.setCustomName(nbt.getString("CustomName"));
        }
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
        return (this.customName != null) && !this.customName.equals("");
    }

    /**
     * Causes the TileEntity to reset all it's cached values for it's container Block, metadata and in the case of
     * chests, the adjacent chest check
     */
    @Override
    public void updateContainingBlockInfo() {
        super.updateContainingBlockInfo();
        this.adjacentChestChecked = false;
    }

    /**
     * Performs the check for adjacent chests to determine if this chest is double or not.
     */
    public void checkForAdjacentChests() {
        if (!this.adjacentChestChecked) {
            this.adjacentChestChecked = true;
            this.adjacentChestZNeg = null;
            this.adjacentChestXPos = null;
            this.adjacentChestXNeg = null;
            this.adjacentChestZPos = null;
        }
    }

    public void updateEntityChest() {
        float f;
        this.prevLidAngle = this.lidAngle;
        f = 0.04F;
        double d2;
        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F
            && this.adjacentChestZNeg == null
            && this.adjacentChestXNeg == null) {
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
            if (this.lidAngle < f2 && f1 >= f2 && this.adjacentChestZNeg == null && this.adjacentChestXNeg == null) {
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
    public boolean receiveClientEvent(int p_145842_1_, int p_145842_2_) {
        if (p_145842_1_ == 1) {
            this.numPlayersUsing = p_145842_2_;
            return true;
        } else {
            return super.receiveClientEvent(p_145842_1_, p_145842_2_);
        }
    }

    /**
     * invalidates a tile entity
     */
    @Override
    public final void invalidate() {
        super.invalidate();
        cachedChestType = 1;
        this.updateContainingBlockInfo();
        this.checkForAdjacentChests();
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
    public ModularPanel buildUI(GuiData data, GuiSyncManager syncManager) {
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
            new TextWidget(IKey.lang("tile.blockDecayablesChest.name")).top(5)
                .left(5));
        panel.child(
            SlotGroupWidget.builder()
                .matrix("IIIII", "IIIII", "IIIII")
                .key(
                    'I',
                    index -> new ItemSlot<>().slot(
                        SyncHandlers.itemSlot(contents, index)
                            .slotGroup(SLOT_GROUP)))
                .build()
                .flex(
                    flex -> flex.anchor(Alignment.TopCenter)
                        .marginTop(15)
                        .leftRelAnchor(0.5f, 0.5f)));
        return panel;
    }
}
