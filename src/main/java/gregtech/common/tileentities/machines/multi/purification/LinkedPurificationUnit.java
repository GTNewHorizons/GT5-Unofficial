package gregtech.common.tileentities.machines.multi.purification;

import static gregtech.GT_Mod.gregtechproxy;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Util;

/**
 * Small wrapper around a GT_MetaTileEntity_PurificationUnitBase, to be stored in the main purification plant
 * controller. May be useful for storing additional data in the controller that the individual units do not need
 * to know about.
 */
public class LinkedPurificationUnit {

    /**
     * Whether this unit is active in the current cycle. We need to keep track of this so units cannot come online
     * in the middle of a cycle and suddenly start processing.
     */
    private boolean mIsActive = false;

    private final GT_MetaTileEntity_PurificationUnitBase<?> mMetaTileEntity;

    public LinkedPurificationUnit(GT_MetaTileEntity_PurificationUnitBase<?> unit) {
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
        if (!gregtechproxy.isClientSide()) {
            world = DimensionManager.getWorld(nbtData.getInteger("worldID"));
        } else {
            world = Minecraft.getMinecraft().thePlayer.worldObj;
        }

        // Load coordinates from link data
        int x = linkData.getInteger("x");
        int y = linkData.getInteger("y");
        int z = linkData.getInteger("z");

        // Find a TileEntity at this location
        TileEntity te = GT_Util.getTileEntity(world, x, y, z, true);
        if (te == null) {
            // This is a bug, throw a fatal error.
            throw new NullPointerException("Unit disappeared during server sync. This is a bug.");
        }

        // Cast TileEntity to proper GT TileEntity
        this.mMetaTileEntity = (GT_MetaTileEntity_PurificationUnitBase<?>) ((IGregTechTileEntity) te)
            .getMetaTileEntity();
    }

    public GT_MetaTileEntity_PurificationUnitBase<?> metaTileEntity() {
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
        if (this.isActive()) {
            return EnumChatFormatting.GREEN + "Active";
        }

        PurificationUnitStatus status = this.mMetaTileEntity.status();
        switch (status) {
            case ONLINE -> {
                return EnumChatFormatting.GREEN + "Online";
            }
            case DISABLED -> {
                return EnumChatFormatting.YELLOW + "Disabled";
            }
            case INCOMPLETE_STRUCTURE -> {
                return EnumChatFormatting.RED + "Incomplete Structure";
            }
        }

        // If this happens, this is a bug and there is a switch case missing.
        return null;
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
