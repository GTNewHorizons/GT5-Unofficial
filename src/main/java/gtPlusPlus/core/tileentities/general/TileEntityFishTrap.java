package gtPlusPlus.core.tileentities.general;

import static gregtech.api.enums.Mods.PamsHarvestCraft;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.FishingHooks;

import gregtech.api.enums.Materials;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.inventories.InventoryFishTrap;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class TileEntityFishTrap extends TileEntity implements ISidedInventory {

    private int tickCount = 0;
    private final InventoryFishTrap inventoryContents;
    private String customName;
    // The number of water blocks is used as an index to get the tick rate.
    private final static short[] waterBlocksToTickRate = new short[] { 0, 0, 5600, 4400, 3200, 2000, 1750 };
    private int surroundingWaterBlocks = 0;

    public TileEntityFishTrap() {
        this.inventoryContents = new InventoryFishTrap();
    }

    private int getNumberOfSurroundingWater() {
        if (!this.hasWorldObj() || this.getWorldObj().isRemote) {
            return 0;
        }
        final Block[] surroundingBlocks = new Block[6];
        surroundingBlocks[0] = this.worldObj.getBlock(this.xCoord, this.yCoord + 1, this.zCoord); // Above
        surroundingBlocks[1] = this.worldObj.getBlock(this.xCoord, this.yCoord - 1, this.zCoord); // Below
        surroundingBlocks[2] = this.worldObj.getBlock(this.xCoord + 1, this.yCoord, this.zCoord);
        surroundingBlocks[3] = this.worldObj.getBlock(this.xCoord - 1, this.yCoord, this.zCoord);
        surroundingBlocks[4] = this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord + 1);
        surroundingBlocks[5] = this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord - 1);
        int waterCount = 0;
        int trapCount = 0;
        for (final Block checkBlock : surroundingBlocks) {
            if (checkBlock == ModBlocks.blockFishTrap) {
                trapCount++;
            } else if ((checkBlock == Blocks.water) || (checkBlock == Blocks.flowing_water)) {
                waterCount++;
            }
        }
        // Explicitly check for at least 2 water blocks.
        if (waterCount < 2) {
            return 0;
        }
        // Only allow the first four traps to count towards valid neighbor blocks.
        return waterCount + Math.min(trapCount, 4);
    }

    public InventoryFishTrap getInventory() {
        return this.inventoryContents;
    }

    private void tryAddLoot() {
        ItemUtils.organiseInventory(getInventory());
        final ItemStack loot = this.generateLootForFishTrap();
        if (loot == null) {
            return;
        }
        for (final ItemStack contents : this.getInventory()
            .getInventory()) {
            if (GTUtility.areStacksEqual(loot, contents)) {
                if (contents.stackSize < contents.getMaxStackSize()) {
                    contents.stackSize++;
                    this.markDirty();
                    return;
                }
            }
        }
        int checkingSlot = 0;
        for (final ItemStack contents : this.getInventory()
            .getInventory()) {
            if (contents == null) {
                this.getInventory()
                    .setInventorySlotContents(checkingSlot, loot);
                this.markDirty();
                return;
            }
            checkingSlot++;
        }
    }

    @Nullable
    private ItemStack generateLootForFishTrap() {
        final int lootWeight = MathUtils.randInt(0, 100);
        ItemStack loot = null;
        if (lootWeight <= 5) {
            loot = ItemUtils.getSimpleStack(Items.slime_ball);
        } else if (lootWeight <= 10) {
            loot = ItemUtils.getSimpleStack(Items.bone);
        } else if (lootWeight <= 15) {
            loot = ItemUtils.getSimpleStack(Blocks.sand);
        } else if (lootWeight <= 20) {
            loot = ItemUtils.simpleMetaStack(Items.dye, 0, 1);
        }
        // Junk Loot
        else if (lootWeight <= 23) {
            if (PamsHarvestCraft.isModLoaded()) {
                loot = ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cropSeaweed", 1);
            } else {
                loot = ItemUtils.getSimpleStack(Blocks.dirt);
            }
        }
        // Pam Fish
        else if (lootWeight <= 99) {
            final Random xstr = new Random();
            loot = FishingHooks.getRandomFishable(xstr, 100);
        } else if (lootWeight == 100) {
            final int rareLoot = MathUtils.randInt(1, 10);
            if (rareLoot <= 4) {
                loot = Materials.Iron.getNuggets(1);
            } else if (rareLoot <= 7) {
                loot = ItemUtils.getSimpleStack(Items.gold_nugget);
            } else if (rareLoot <= 9) {
                loot = ItemUtils.getSimpleStack(Items.emerald);
            } else {
                loot = ItemUtils.getSimpleStack(Items.diamond);
            }
        }
        if (loot != null) {
            loot.stackSize = 1;
        }
        return loot;
    }

    @Override
    public void updateEntity() {
        if (this.worldObj.isRemote) {
            return;
        }

        this.tickCount++;
        // Only recalculate the amount of neighboring water/trap blocks every 20 ticks.
        if ((this.tickCount % 20) == 0) {
            this.surroundingWaterBlocks = getNumberOfSurroundingWater();
        }

        if ((waterBlocksToTickRate[this.surroundingWaterBlocks] != 0)
            && this.tickCount > waterBlocksToTickRate[this.surroundingWaterBlocks]) {
            int aExtraLootChance = MathUtils.randInt(1, 1000);
            if (aExtraLootChance >= 999) {
                this.tryAddLoot();
                this.tryAddLoot();
                this.tryAddLoot();
            } else {
                this.tryAddLoot();
            }
            this.markDirty();

            this.tickCount = 0;
        }
    }

    @Override
    public void writeToNBT(final NBTTagCompound nbt) {
        super.writeToNBT(nbt);
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
        this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, 1);
        this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
        this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord - 1, this.zCoord, this.getBlockType());
        this.getInventory()
            .openInventory();
    }

    @Override
    public void closeInventory() {
        this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), 1, 1);
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
        return false;
    }

    @Override
    public boolean canExtractItem(final int p_102008_1_, final ItemStack p_102008_2_, final int p_102008_3_) {
        return true;
    }

    public String getCustomName() {
        return this.customName;
    }

    public void setCustomName(final String customName) {
        this.customName = customName;
    }

    @Override
    public String getInventoryName() {
        return this.hasCustomInventoryName() ? this.customName : "container.fishtrap";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return (this.customName != null) && !this.customName.isEmpty();
    }
}
