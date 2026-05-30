package gregtech.common.blocks.rubbertree;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;

public final class RubberTreeResinLogic {

    public static final int RESIN_PER_DRIP = 1;

    public static final int HIGH_RESIN_THRESHOLD = 100;
    public static final int MEDIUM_RESIN_THRESHOLD = 40;

    public static final int HIGH_RESIN_MIN_DRIP_TICKS = 20 * 5;
    public static final int HIGH_RESIN_MAX_DRIP_TICKS = 20 * 10;

    public static final int MEDIUM_RESIN_MIN_DRIP_TICKS = 20 * 12;
    public static final int MEDIUM_RESIN_MAX_DRIP_TICKS = 20 * 20;

    public static final int LOW_RESIN_MIN_DRIP_TICKS = 20 * 30;
    public static final int LOW_RESIN_MAX_DRIP_TICKS = 20 * 45;

    public static final int MIN_TREE_RESIN_UNITS = 140;
    public static final int MAX_TREE_RESIN_UNITS = 200;

    public static final int MAX_TAPPED_HOLES_PER_TREE = 1;
    public static final int MAX_TAP_HEIGHT_FROM_BASE = 1;

    public static final int FELLING_TRIGGER_HEIGHT_FROM_BASE = 5;

    private static final int HOPPER_INSERT_SIDE = ForgeDirection.UP.ordinal();

    private RubberTreeResinLogic() {}

    public static final class LogPosition {

        public final int x;
        public final int y;
        public final int z;

        private LogPosition(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public boolean is(int otherX, int otherY, int otherZ) {
            return this.x == otherX && this.y == otherY && this.z == otherZ;
        }
    }

    public static int nextTapDelay(@NotNull Random random, int remainingResin) {
        if (remainingResin > HIGH_RESIN_THRESHOLD) {
            return randomBetween(random, HIGH_RESIN_MIN_DRIP_TICKS, HIGH_RESIN_MAX_DRIP_TICKS);
        }

        if (remainingResin >= MEDIUM_RESIN_THRESHOLD) {
            return randomBetween(random, MEDIUM_RESIN_MIN_DRIP_TICKS, MEDIUM_RESIN_MAX_DRIP_TICKS);
        }

        if (remainingResin > 0) {
            return randomBetween(random, LOW_RESIN_MIN_DRIP_TICKS, LOW_RESIN_MAX_DRIP_TICKS);
        }

        return 0;
    }

    private static int randomBetween(@NotNull Random random, int minInclusive, int maxInclusive) {
        return minInclusive + random.nextInt(maxInclusive - minInclusive + 1);
    }

    public static int nextTreeResinUnits(@NotNull Random random) {
        return MIN_TREE_RESIN_UNITS + random.nextInt(MAX_TREE_RESIN_UNITS - MIN_TREE_RESIN_UNITS + 1);
    }

    public static int findTrunkBaseY(@NotNull World world, int x, int y, int z) {
        while (y > 1 && world.getBlock(x, y - 1, z) == GregTechAPI.sBlockRubberLogNatural) {
            y--;
        }

        return y;
    }

    public static boolean isLowerTrunkPosition(@NotNull World world, int x, int y, int z) {
        int baseY = findTrunkBaseY(world, x, y, z);

        return y >= baseY && y <= baseY + MAX_TAP_HEIGHT_FROM_BASE && isValidRubberTreeBase(world, x, baseY, z);
    }

    public static boolean isValidRubberTreeBase(@NotNull World world, int x, int baseY, int z) {
        if (baseY <= 0 || world.getBlock(x, baseY, z) != GregTechAPI.sBlockRubberLogNatural) {
            return false;
        }

        Block soil = world.getBlock(x, baseY - 1, z);
        if (soil == null || soil.isAir(world, x, baseY - 1, z)) {
            return false;
        }

        if (GregTechAPI.sBlockRubberSapling instanceof IPlantable) {
            return soil.canSustainPlant(
                world,
                x,
                baseY - 1,
                z,
                ForgeDirection.UP,
                (IPlantable) GregTechAPI.sBlockRubberSapling);
        }

        return soil.getMaterial()
            .isSolid()
            && !soil.getMaterial()
                .isReplaceable();
    }

    public static int countTappedHolesOnLowerTrunk(@NotNull World world, int x, int y, int z) {
        int baseY = findTrunkBaseY(world, x, y, z);
        int count = 0;

        for (int dy = 0; dy <= MAX_TAP_HEIGHT_FROM_BASE; dy++) {
            int currentY = baseY + dy;

            if (world.getBlock(x, currentY, z) != GregTechAPI.sBlockRubberLogNatural) {
                continue;
            }

            if (BlockRubberLogNatural.hasTappedHole(world.getBlockMetadata(x, currentY, z))) {
                count++;
            }
        }

        return count;
    }

    public static int getInitialRemainingResinForNewHole(@NotNull World world, int x, int y, int z,
        @NotNull Random random) {
        int sharedRemaining = getSharedRemainingResin(world, x, y, z);
        return sharedRemaining >= 0 ? sharedRemaining : nextTreeResinUnits(random);
    }

    public static boolean isTreeReservoirExhausted(@NotNull World world, int x, int y, int z) {
        return getSharedRemainingResin(world, x, y, z) == 0;
    }

    public static int getSharedRemainingResin(@NotNull World world, int x, int y, int z) {
        int baseY = findTrunkBaseY(world, x, y, z);
        int sharedRemaining = -1;

        for (int dy = 0; dy <= MAX_TAP_HEIGHT_FROM_BASE; dy++) {
            TileEntityRubberLogTapped tile = getTappedTileEntity(world, x, baseY + dy, z);
            if (tile == null || !tile.isReservoirInitialized()) {
                continue;
            }

            int remaining = tile.getRemainingResin();
            sharedRemaining = sharedRemaining < 0 ? remaining : Math.min(sharedRemaining, remaining);
        }

        return sharedRemaining;
    }

    public static void syncRemainingResinOnLowerTrunk(@NotNull World world, int x, int y, int z, int remainingResin) {
        int baseY = findTrunkBaseY(world, x, y, z);
        int normalized = Math.max(0, remainingResin);

        for (int dy = 0; dy <= MAX_TAP_HEIGHT_FROM_BASE; dy++) {
            TileEntityRubberLogTapped tile = getTappedTileEntity(world, x, baseY + dy, z);
            if (tile != null) {
                tile.setRemainingResin(normalized);
            }
        }
    }

    private static int consumeResinFromTree(@NotNull World world, int x, int y, int z, @NotNull Random random) {
        int sharedRemaining = getSharedRemainingResin(world, x, y, z);

        if (sharedRemaining < 0) {
            sharedRemaining = nextTreeResinUnits(random);
            syncRemainingResinOnLowerTrunk(world, x, y, z, sharedRemaining);
        }

        if (sharedRemaining <= 0) {
            syncRemainingResinOnLowerTrunk(world, x, y, z, 0);
            return 0;
        }

        int producedAmount = RESIN_PER_DRIP == 1 ? RESIN_PER_DRIP : Math.min(RESIN_PER_DRIP, sharedRemaining);
        syncRemainingResinOnLowerTrunk(world, x, y, z, sharedRemaining - producedAmount);

        return producedAmount;
    }

    public static boolean produceFromTap(@NotNull World world, int x, int y, int z, int side, @NotNull Random random) {
        if (!isLowerTrunkPosition(world, x, y, z)) {
            return false;
        }

        int producedAmount = consumeResinFromTree(world, x, y, z, random);

        if (producedAmount <= 0) {
            return false;
        }

        ItemStack resin = ItemList.Sticky_Resin.get(producedAmount);
        ItemStack remaining = tryInsertIntoInventoryBelowTap(world, x, y, z, side, resin);

        if (remaining != null && remaining.stackSize > 0) {
            spawnResinAtTap(world, x, y, z, side, remaining);
        }

        RubberTreeEffects.playResinHarvestSound(world, x, y, z);
        return true;
    }

    private static @Nullable TileEntityRubberLogTapped getTappedTileEntity(@NotNull World world, int x, int y, int z) {
        if (world.getBlock(x, y, z) != GregTechAPI.sBlockRubberLogNatural) {
            return null;
        }

        if (!BlockRubberLogNatural.hasTappedHole(world.getBlockMetadata(x, y, z))) {
            return null;
        }

        TileEntity tile = world.getTileEntity(x, y, z);
        return tile instanceof TileEntityRubberLogTapped ? (TileEntityRubberLogTapped) tile : null;
    }

    private static @Nullable ItemStack tryInsertIntoInventoryBelowTap(@NotNull World world, int x, int y, int z,
        int side, @NotNull ItemStack stack) {
        ForgeDirection tapDirection = ForgeDirection.getOrientation(side);
        int inventoryX = x + tapDirection.offsetX;
        int inventoryY = y - 1;
        int inventoryZ = z + tapDirection.offsetZ;

        TileEntity tile = world.getTileEntity(inventoryX, inventoryY, inventoryZ);
        if (!(tile instanceof IInventory)) {
            return stack;
        }

        return insertIntoInventory((IInventory) tile, stack.copy());
    }

    private static @Nullable ItemStack insertIntoInventory(@NotNull IInventory inventory, @NotNull ItemStack stack) {
        int[] slots = getAccessibleSlots(inventory);

        for (int slot : slots) {
            if (stack.stackSize <= 0) {
                inventory.markDirty();
                return null;
            }

            if (!canInsert(inventory, stack, slot)) {
                continue;
            }

            ItemStack inSlot = inventory.getStackInSlot(slot);
            int inventoryLimit = Math.min(inventory.getInventoryStackLimit(), stack.getMaxStackSize());

            if (inSlot == null) {
                int inserted = Math.min(stack.stackSize, inventoryLimit);
                ItemStack insertedStack = stack.copy();
                insertedStack.stackSize = inserted;
                inventory.setInventorySlotContents(slot, insertedStack);
                stack.stackSize -= inserted;
                continue;
            }

            if (!canStacksMerge(inSlot, stack)) {
                continue;
            }

            int inserted = Math.min(stack.stackSize, inventoryLimit - inSlot.stackSize);
            if (inserted <= 0) {
                continue;
            }

            inSlot.stackSize += inserted;
            stack.stackSize -= inserted;
        }

        inventory.markDirty();
        return stack.stackSize <= 0 ? null : stack;
    }

    private static int[] getAccessibleSlots(@NotNull IInventory inventory) {
        if (inventory instanceof ISidedInventory) {
            return ((ISidedInventory) inventory).getAccessibleSlotsFromSide(HOPPER_INSERT_SIDE);
        }

        int[] slots = new int[inventory.getSizeInventory()];
        for (int i = 0; i < slots.length; i++) {
            slots[i] = i;
        }

        return slots;
    }

    private static boolean canInsert(@NotNull IInventory inventory, @NotNull ItemStack stack, int slot) {
        if (!inventory.isItemValidForSlot(slot, stack)) {
            return false;
        }

        return !(inventory instanceof ISidedInventory)
            || ((ISidedInventory) inventory).canInsertItem(slot, stack, HOPPER_INSERT_SIDE);
    }

    private static boolean canStacksMerge(@NotNull ItemStack left, @NotNull ItemStack right) {
        return left.getItem() == right.getItem() && left.getItemDamage() == right.getItemDamage()
            && ItemStack.areItemStackTagsEqual(left, right);
    }

    public static void spawnResinAtTap(@NotNull World world, int x, int y, int z, int side, @NotNull ItemStack stack) {
        ForgeDirection direction = ForgeDirection.getOrientation(side);
        double spawnX = x + 0.5D + direction.offsetX * 0.70D;
        double spawnY = y + 0.55D;
        double spawnZ = z + 0.5D + direction.offsetZ * 0.70D;

        EntityItem entityItem = new EntityItem(world, spawnX, spawnY, spawnZ, stack);
        entityItem.motionX = direction.offsetX * 0.08D;
        entityItem.motionY = 0.02D + world.rand.nextDouble() * 0.03D;
        entityItem.motionZ = direction.offsetZ * 0.08D;
        entityItem.delayBeforeCanPickup = 5;
        world.spawnEntityInWorld(entityItem);
    }

    public static boolean isReplaceableSide(@NotNull World world, int x, int y, int z, int side) {
        ForgeDirection direction = ForgeDirection.getOrientation(side);
        int nx = x + direction.offsetX;
        int ny = y + direction.offsetY;
        int nz = z + direction.offsetZ;
        Block neighbour = world.getBlock(nx, ny, nz);

        return neighbour == null || neighbour.isAir(world, nx, ny, nz)
            || neighbour.isLeaves(world, nx, ny, nz)
            || neighbour.canBeReplacedByLeaves(world, nx, ny, nz)
            || neighbour.getMaterial()
                .isReplaceable();
    }

    public static boolean isFellingCutPosition(@NotNull World world, int x, int y, int z) {
        if (world.getBlock(x, y, z) != GregTechAPI.sBlockRubberLogNatural) {
            return false;
        }

        int baseY = findTrunkBaseY(world, x, y, z);

        return isValidRubberTreeBase(world, x, baseY, z) && y >= baseY && y <= baseY + FELLING_TRIGGER_HEIGHT_FROM_BASE;
    }

    public static @NotNull List<LogPosition> collectNaturalTrunkLogs(@NotNull World world, int x, int y, int z) {
        List<LogPosition> logs = new ArrayList<>();

        if (world.getBlock(x, y, z) != GregTechAPI.sBlockRubberLogNatural) {
            return logs;
        }

        int baseY = findTrunkBaseY(world, x, y, z);

        if (!isValidRubberTreeBase(world, x, baseY, z)) {
            return logs;
        }

        int currentY = baseY;

        while (currentY < world.getHeight() && world.getBlock(x, currentY, z) == GregTechAPI.sBlockRubberLogNatural) {
            logs.add(new LogPosition(x, currentY, z));
            currentY++;
        }

        return logs;
    }

    public static void fellNaturalTree(@NotNull World world, @NotNull List<LogPosition> logs, int skippedX,
        int skippedY, int skippedZ) {
        if (world.isRemote || logs.isEmpty()) {
            return;
        }

        exhaustTappedLogs(logs, world);

        for (LogPosition log : logs) {
            if (log.is(skippedX, skippedY, skippedZ)) {
                continue;
            }

            if (world.getBlock(log.x, log.y, log.z) != GregTechAPI.sBlockRubberLogNatural) {
                continue;
            }

            world.setBlock(log.x, log.y, log.z, GregTechAPI.sBlockRubberLog, 0, 3);
        }
    }

    private static void exhaustTappedLogs(@NotNull List<LogPosition> logs, @NotNull World world) {
        for (LogPosition log : logs) {
            TileEntityRubberLogTapped tile = getTappedTileEntity(world, log.x, log.y, log.z);

            if (tile != null) {
                tile.setRemainingResin(0);
            }
        }
    }
}
