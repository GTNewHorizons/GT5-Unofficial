package gregtech.api.multitileentity.base;

import static gregtech.GT_Mod.GT_FML_LOGGER;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.GT_Values;
import gregtech.api.multitileentity.interfaces.IMultiTileEntity.IMTE_OnNeighborBlockChange;
import gregtech.api.task.TaskHost;
import gregtech.api.task.TickableTask;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Util;

public abstract class TickableMultiTileEntity extends MultiTileEntity implements TaskHost, IMTE_OnNeighborBlockChange {

    /** Variable for seeing if the Tick Function is called right now. */
    public boolean isRunningTick = false;
    /** Gets set to true when the Block received a Block Update. */
    public boolean blockUpdated = false;
    /** Timer Value */
    protected long timer = 0;
    /** Variable for updating Data to the Client */
    private boolean sendClientData = false;

    private final Map<String, TickableTask<?>> tasks = new HashMap<>();

    public TickableMultiTileEntity() {
        super(true);
    }

    @Override
    public final void registerTask(@Nonnull TickableTask<?> task) {
        if (tasks.containsKey(task.getName())) {
            throw new IllegalStateException(String.format("Task with name %s is already registered", task.getName()));
        }
        tasks.put(task.getName(), task);
    }

    @Nullable
    public TickableTask<?> getTask(@Nonnull String name) {
        return tasks.get(name);
    }

    @Override
    public final void updateEntity() {
        isRunningTick = true;
        final boolean isServerSide = isServerSide();
        try {
            if (timer++ == 0) {
                markDirty();
                GT_Util.markChunkDirty(this);
                onFirstTick(isServerSide);
            }
            if (isDead()) {
                return;
            }
            onPreTick(timer, isServerSide);
            super.updateEntity();
            if (!isServerSide && needsUpdate) {
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                needsUpdate = false;
            }
            onTick(timer, isServerSide);
            for (TickableTask<?> task : tasks.values()) {
                task.update(timer, isServerSide);
            }
            if (isServerSide && timer > 2 && sendClientData) {
                sendClientData(null);
            }
            onPostTick(timer, isServerSide);

        } catch (Throwable e) {
            GT_FML_LOGGER.error("UpdateEntity Failed", e);
            e.printStackTrace(GT_Log.err);
            try {
                onTickFailed(timer, isServerSide);
            } catch (Throwable e2) {
                GT_FML_LOGGER.error("UpdateEntity:onTickFailed Failed", e);
            }
        }

        isRunningTick = false;
    }

    @Override
    public void sendClientData(EntityPlayerMP aPlayer) {
        if (sendClientData) {
            // GT_FML_LOGGER.info("Sending client data");
            super.sendClientData(aPlayer);
            sendClientData = false;
        }
    }

    /**
     * The very first Tick happening to this TileEntity.
     */
    public void onFirstTick(boolean isServerSide) {
        if (isServerSide) {
            checkDropCover();
        } else {
            requestCoverDataIfNeeded();
        }
    }

    /**
     * The first part of the Tick, before block update.
     */
    public void onPreTick(long tick, boolean isServerSide) {}

    /**
     * The regular Tick. After block update, before sending data to client.
     */
    public void onTick(long tick, boolean isServerSide) {}

    /**
     * The absolute last part of the Tick, after sending data to client.
     */
    public void onPostTick(long tick, boolean isServerSide) {}

    /**
     * Gets called when there is an Exception/Error happening during one of the Tick methods.
     */
    public void onTickFailed(long tick, boolean isServerSide) {}

    @Override
    protected final void readTasksNBT(NBTTagCompound nbt) {
        if (nbt.hasKey(GT_Values.NBT.TASKS)) {
            NBTTagCompound tasksTag = nbt.getCompoundTag(GT_Values.NBT.TASKS);
            for (TickableTask<?> task : tasks.values()) {
                if (tasksTag.hasKey(task.getName())) {
                    task.readFromNBT(tasksTag.getCompoundTag(task.getName()));
                }
            }
        }
    }

    @Override
    protected final void writeTasksNBT(NBTTagCompound aNBT) {
        NBTTagCompound tasksTag = new NBTTagCompound();
        for (TickableTask<?> task : tasks.values()) {
            NBTTagCompound tag = new NBTTagCompound();
            task.writeToNBT(tag);
            tasksTag.setTag(task.getName(), tag);
        }
        aNBT.setTag(GT_Values.NBT.TASKS, tasksTag);
    }

    @Override
    public void onNeighborBlockChange(World aWorld, Block aBlock) {
        blockUpdated = true;
    }

    @Override
    public void issueClientUpdate() {
        sendClientData = true;
        sendGraphicPacket();
    }

    @Override
    public byte getComparatorValue(ForgeDirection side) {
        return 0;
    }
}
