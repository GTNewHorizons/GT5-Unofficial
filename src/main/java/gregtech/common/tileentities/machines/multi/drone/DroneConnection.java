package gregtech.common.tileentities.machines.multi.drone;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.util.GTUtil;
import gregtech.api.util.shutdown.ShutDownReason;

public class DroneConnection {

    // To make it work on client, no mte should be stored inside connection
    private final String unlocalizedName;
    private final ItemStack machineItem;
    private final ChunkCoordinates machineCoord;
    private final ChunkCoordinates centreCoord;
    private final int worldID;

    public String customName;
    public boolean machineStatus;
    public String shutdownReason;
    public UUID uuid;
    public boolean isSelected;
    public int group;

    public DroneConnection(MTEMultiBlockBase machine, MTEDroneCentre centre, HashMap<String, String> tempNameList,
        HashMap<String, Integer> tempGroupList) {
        this.machineItem = machine.getStackForm(1);
        this.machineCoord = machine.getBaseMetaTileEntity()
            .getCoords();
        this.centreCoord = centre.getBaseMetaTileEntity()
            .getCoords();
        this.worldID = centre.getBaseMetaTileEntity()
            .getWorld().provider.dimensionId;
        this.uuid = UUID.nameUUIDFromBytes((machineCoord.toString() + worldID).getBytes());
        this.unlocalizedName = machine.mName;
        this.customName = Optional.ofNullable(tempNameList.remove(uuid.toString()))
            .orElse(machine.getLocalName());
        this.group = Optional.ofNullable(tempGroupList.remove(uuid.toString()))
            .orElse(0);
        this.machineStatus = machine.isAllowedToWork();
        this.shutdownReason = machine.getBaseMetaTileEntity()
            .getLastShutDownReason()
            .getDisplayString();
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
        this.group = aNBT.getInteger("group");
    }

    public Optional<MTEMultiBlockBase> getLinkedMachine() {
        // It will always be null if this method is called on client with out-range coords
        return Optional.ofNullable(getLoadedGTBaseMachineAt(machineCoord, DimensionManager.getWorld(worldID), false));
    }

    public Optional<MTEMultiBlockBase> getLinkedCentre() {
        return Optional.ofNullable(getLoadedGTBaseMachineAt(centreCoord, DimensionManager.getWorld(worldID), true));
    }

    public String getCustomName() {
        return customName;
    }

    public ChunkCoordinates getCentreCoord() {
        return centreCoord;
    }

    public ChunkCoordinates getMachineCoord() {
        return machineCoord;
    }

    public ItemStack getMachineItem() {
        return machineItem;
    }

    public String getLocalizedName() {
        return StatCollector.translateToLocal("gt.blockmachines." + unlocalizedName + ".name");
    }

    public float getDistanceSquared() {
        return centreCoord.getDistanceSquaredToChunkCoordinates(machineCoord);
    }

    public void setCustomName(String name) {
        customName = name;
    }

    public boolean isMachineShutdown() {
        return !shutdownReason.isEmpty() && !machineStatus;
    }

    public String getShutdownReason() {
        return shutdownReason;
    }

    public NBTTagCompound writeToNBT() {
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
        aNBT.setInteger("group", group);
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
        return getLinkedMachine().map(MetaTileEntity::isValid)
            .orElse(false);
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
        buf.writeNBTTagCompoundToBuffer(connection.writeToNBT());
    }

    public static boolean areEqual(DroneConnection a, DroneConnection b) {
        if (a == null || b == null) return false;
        return a.machineCoord.equals(b.machineCoord) && a.centreCoord.equals(b.centreCoord)
            && a.unlocalizedName.equals(b.unlocalizedName)
            && a.customName.equals(b.customName)
            && a.isSelected == b.isSelected
            && a.machineStatus == b.machineStatus
            && a.shutdownReason.equals(b.shutdownReason)
            && a.group == b.group;

    }
}
