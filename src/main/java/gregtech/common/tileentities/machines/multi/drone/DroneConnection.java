package gregtech.common.tileentities.machines.multi.drone;

import static gregtech.GTMod.proxy;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
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
    ItemStack machineItem;
    public ChunkCoordinates machineCoord;
    boolean machineStatus;
    ShutDownReason shutdownReason;
    ChunkCoordinates centreCoord;
    public UUID uuid;
    World world;

    public DroneConnection(MTEMultiBlockBase machine, MTEDroneCentre centre) {
        this.machineItem = machine.getStackForm(1);
        machineCoord = machine.getBaseMetaTileEntity()
            .getCoords();
        centreCoord = centre.getBaseMetaTileEntity()
            .getCoords();
        this.world = centre.getBaseMetaTileEntity()
            .getWorld();
        uuid = UUID.nameUUIDFromBytes(
            machineCoord.toString()
                .getBytes());
        unlocalizedName = machine.mName;
        customName = Optional.ofNullable(centre.tempNameList.remove(machineCoord.toString()))
            .orElse(machine.getLocalName());
    }

    public DroneConnection(NBTTagCompound aNBT) {
        NBTTagCompound machineTag = aNBT.getCompoundTag("machine");
        NBTTagCompound centreTag = aNBT.getCompoundTag("centre");
        if (!proxy.isClientSide()) {
            this.world = DimensionManager.getWorld(aNBT.getInteger("worldID"));
        } else {
            this.world = Minecraft.getMinecraft().thePlayer.worldObj;
        }
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
        if (aNBT.hasKey("uuid")) {
            this.uuid = UUID.fromString(aNBT.getString("uuid"));
        }
    }

    @Nullable
    public MTEMultiBlockBase getLinkedMachine() {
        return getLoadedGTBaseMachineAt(machineCoord, world, false);
    }

    public MTEMultiBlockBase getLinkedCentre() {
        return getLoadedGTBaseMachineAt(centreCoord, world, true);
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
        aNBT.setInteger(
            "worldID",
            getLinkedCentre().getBaseMetaTileEntity()
                .getWorld().provider.dimensionId);
        aNBT.setString("name", getCustomName());
        aNBT.setString("unlocalizedName", unlocalizedName);
        aNBT.setString("uuid", this.uuid.toString());
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
        shutdownReason = reason;
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
            && a.customName.equals(b.customName)
            && a.unlocalizedName.equals(b.unlocalizedName);
    }
}
