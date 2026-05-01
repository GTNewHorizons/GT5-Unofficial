package gregtech.common.tileentities.machines.multi.purification;

import static gregtech.GTMod.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTUtil;

/**
 * Small wrapper around a MTEPurificationUnitBase, to be stored in the main purification plant controller. May be useful
 * for storing additional data in the controller that the individual units do not need to know about.
 */
public class LinkedPurificationUnit {

    /**
     * Whether this unit is active in the current cycle. We need to keep track of this so units cannot come online in
     * the middle of a cycle and suddenly start processing.
     */
    private boolean mIsActive = false;

    private final MTEPurificationUnitBase<?> mMetaTileEntity;

    public LinkedPurificationUnit(MTEPurificationUnitBase<?> unit) {
        this.mMetaTileEntity = unit;
    }

    /**
     * Construct new link data from a NBT compound. This is intended to sync the linked units to the client.
     *
     * @param nbtData NBT data obtained from writeLinkDataToNBT()
     */
    public LinkedPurificationUnit(NBTTagCompound nbtData) {
        this.mIsActive = nbtData.getBoolean("active");
        NBTTagCompound linkData = nbtData.getCompoundTag("linkData");
        World world = null;
        if (!proxy.isClientSide()) {
            world = DimensionManager.getWorld(nbtData.getInteger("worldID"));
        } else {
            world = Minecraft.getMinecraft().thePlayer.worldObj;
        }

        // Load coordinates from link data
        int x = linkData.getInteger("x");
        int y = linkData.getInteger("y");
        int z = linkData.getInteger("z");

        // Find a TileEntity at this location
        TileEntity te = GTUtil.getTileEntity(world, x, y, z, true);
        if (te == null) {
            // This is a bug, throw a fatal error.
            throw new NullPointerException("Unit disappeared during server sync. This is a bug.");
        }

        // Cast TileEntity to proper GT TileEntity
        this.mMetaTileEntity = (MTEPurificationUnitBase<?>) ((IGregTechTileEntity) te).getMetaTileEntity();
    }

    public MTEPurificationUnitBase<?> metaTileEntity() {
        return mMetaTileEntity;
    }

    /**
     * Whether this unit is considered as active in the current cycle
     *
     * @return true if this unit is active in the current cycle
     */
    public boolean isActive() {
        return mIsActive;
    }

    public void setActive(boolean active) {
        this.mIsActive = active;
    }

    public String getStatusString() {
        PurificationUnitStatus status = this.mMetaTileEntity.status();
        switch (status) {
            case ACTIVE -> {
                return EnumChatFormatting.GREEN
                    + StatCollector.translateToLocal("GT5U.infodata.purification_plant.linked_units.status.active");
            }
            case IDLE -> {
                return EnumChatFormatting.GREEN
                    + StatCollector.translateToLocal("GT5U.infodata.purification_plant.linked_units.status.idle");
            }
            case DISABLED -> {
                return EnumChatFormatting.YELLOW
                    + StatCollector.translateToLocal("GT5U.infodata.purification_plant.linked_units.status.disabled");
            }
            case INCOMPLETE_STRUCTURE -> {
                return EnumChatFormatting.RED
                    + StatCollector.translateToLocal("GT5U.infodata.purification_plant.linked_units.status.incomplete");
            }
        }

        // If this happens, this is a bug and there is a switch case missing.
        return null;
    }

    public String getStatusUnlocalized() {
        PurificationUnitStatus status = this.mMetaTileEntity.status();
        return switch (status) {
            case ACTIVE -> "active";
            case IDLE -> "idle";
            case DISABLED -> "disabled";
            case INCOMPLETE_STRUCTURE -> "incomplete";
        };
    }

    /**
     * Save link data to a NBT tag, so it can be reconstructed at the client side
     */
    public NBTTagCompound writeLinkDataToNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("active,", this.mIsActive);

        NBTTagCompound linkData = new NBTTagCompound();
        IGregTechTileEntity mte = this.mMetaTileEntity.getBaseMetaTileEntity();
        linkData.setInteger("x", mte.getXCoord());
        linkData.setInteger("y", mte.getYCoord());
        linkData.setInteger("z", mte.getZCoord());
        tag.setTag("linkData", linkData);

        tag.setInteger(
            "worldID",
            this.mMetaTileEntity.getBaseMetaTileEntity()
                .getWorld().provider.dimensionId);
        return tag;
    }
}
