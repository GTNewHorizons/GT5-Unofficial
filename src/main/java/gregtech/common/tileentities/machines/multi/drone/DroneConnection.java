package gregtech.common.tileentities.machines.multi.drone;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.util.GTUtil;
import gregtech.api.util.shutdown.ShutDownReason;

public class DroneConnection {

    // To make it work on client, no mte should be stored inside connection
    String customName;
    String unlocalizedName;
    public ItemStack machineItem;
    public ChunkCoordinates machineCoord;
    boolean machineStatus;
    String shutdownReason = "";
    ChunkCoordinates centreCoord;
    public UUID uuid;
    public boolean isSelected;
    int worldID;

    public DroneConnection(MTEMultiBlockBase machine, MTEDroneCentre centre) {
        this.machineItem = machine.getStackForm(1);
        this.machineCoord = machine.getBaseMetaTileEntity()
            .getCoords();
        this.centreCoord = centre.getBaseMetaTileEntity()
            .getCoords();
        this.worldID = centre.getBaseMetaTileEntity()
            .getWorld().provider.dimensionId;
        this.uuid = UUID.nameUUIDFromBytes(
            machineCoord.toString()
                .getBytes());
        this.unlocalizedName = machine.mName;
        this.customName = Optional.ofNullable(centre.tempNameList.remove(machineCoord.toString()))
            .orElse(machine.getLocalName());
    }

    public DroneConnection(NBTTagCompound aNBT) {
        NBTTagCompound machineTag = aNBT.getCompoundTag("machine");
        NBTTagCompound centreTag = aNBT.getCompoundTag("centre");
        this.worldID = aNBT.getInteger("worldID");
        machineItem = ItemStack.loadItemStackFromNBT(aNBT.getCompoundTag("item"));
        machineCoord = new ChunkCoordinates(
            machineTag.getInteger("x"),
            machineTag.getInteger("y"),
            machineTag.getInteger("z"));
        centreCoord = new ChunkCoordinates(
            centreTag.getInteger("x"),
            centreTag.getInteger("y"),
            centreTag.getInteger("z"));
        this.customName = aNBT.getString("name");
        this.unlocalizedName = aNBT.getString("unlocalizedName");
        this.uuid = UUID.fromString(aNBT.getString("uuid"));
        this.machineStatus = aNBT.getBoolean("machineStatus");
        this.shutdownReason = aNBT.getString("shutdownReason");
        this.isSelected = aNBT.getBoolean("isSelected");
    }

    @Nullable
    public MTEMultiBlockBase getLinkedMachine() {
        return getLoadedGTBaseMachineAt(machineCoord, DimensionManager.getWorld(worldID), false);
    }

    @Nullable
    public MTEMultiBlockBase getLinkedCentre() {
        // Has to be carefully since the centre maybe cross-dimension. Do not call this method in the client.
        return getLoadedGTBaseMachineAt(centreCoord, DimensionManager.getWorld(worldID), true);
    }

    public String getCustomName() {
        return customName;
    }

    public String getLocalizedName() {
        return StatCollector.translateToLocal("gt.blockmachines." + unlocalizedName + ".name");
    }

    public String getUUIDString() {
        return uuid.toString();
    }

    public float getDistanceSquared() {
        return centreCoord.getDistanceSquaredToChunkCoordinates(machineCoord);
    }

    public void setCustomName(String name) {
        customName = name;
    }

    public boolean isMachineShutdown() {
        return false;
    }

    public NBTTagCompound transConnectionToNBT() {
        NBTTagCompound aNBT = new NBTTagCompound();
        aNBT.setTag("machine", transCoordsToNBT(machineCoord));
        aNBT.setTag("centre", transCoordsToNBT(centreCoord));
        aNBT.setTag("item", machineItem.writeToNBT(new NBTTagCompound()));
        aNBT.setInteger("worldID", worldID);
        aNBT.setString("name", getCustomName());
        aNBT.setString("unlocalizedName", unlocalizedName);
        aNBT.setString("uuid", this.uuid.toString());
        aNBT.setBoolean("machineStatus", machineStatus);
        aNBT.setString("shutdownReason", shutdownReason);
        aNBT.setBoolean("isSelected", isSelected);
        return aNBT;
    }

    public MTEMultiBlockBase getLoadedGTBaseMachineAt(ChunkCoordinates coords, World world, boolean isLoaded) {
        TileEntity te = GTUtil.getTileEntity(world, coords, isLoaded);
        if (te == null) return null;
        return (MTEMultiBlockBase) ((IGregTechTileEntity) te).getMetaTileEntity();
    }

    private NBTTagCompound transCoordsToNBT(ChunkCoordinates coord) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("x", coord.posX);
        tag.setInteger("y", coord.posY);
        tag.setInteger("z", coord.posZ);
        return tag;
    }

    public boolean isValid() {
        return getLinkedMachine() != null && getLinkedMachine().isValid()
            && getLinkedCentre() != null
            && getLinkedCentre().isValid();
    }

    public void setActive(boolean active) {
        machineStatus = active;
    }

    public void setShutdownReason(ShutDownReason reason) {
        shutdownReason = reason.getDisplayString();
    }

    public static DroneConnection deserialize(PacketBuffer buf) throws IOException {
        NBTTagCompound tag = buf.readNBTTagCompoundFromBuffer();
        if (tag == null) return null;
        return new DroneConnection(tag);
    }

    public static void serialize(PacketBuffer buf, DroneConnection connection) throws IOException {
        buf.writeNBTTagCompoundToBuffer(connection.transConnectionToNBT());
    }

    public static boolean areEqual(DroneConnection a, DroneConnection b) {
        if (a == null || b == null) return false;
        return a.machineCoord.equals(b.machineCoord) && a.centreCoord.equals(b.centreCoord)
            && a.unlocalizedName.equals(b.unlocalizedName)
            && a.customName.equals(b.customName)
            && a.isSelected == b.isSelected
            && a.machineStatus == b.machineStatus
            && a.shutdownReason.equals(b.shutdownReason);

    }
}
