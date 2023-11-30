package gregtech.common.tileentities.machines.multi.drone;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.util.GT_Util;

public class droneConnection {

    String customName;
    GT_MetaTileEntity_MultiBlockBase machine;
    ChunkCoordinates machineCoord;
    GT_MetaTileEntity_DroneCentre centre;
    ChunkCoordinates centreCoord;
    World world;
    private static int COUNT = 0;
    int id;

    public droneConnection(GT_MetaTileEntity_MultiBlockBase machine, GT_MetaTileEntity_DroneCentre centre) {
        this.machine = machine;
        machineCoord = machine.getBaseMetaTileEntity()
            .getCoords();
        this.centre = centre;
        centreCoord = centre.getBaseMetaTileEntity()
            .getCoords();
        this.world = centre.getBaseMetaTileEntity()
            .getWorld();
        customName = machine.getLocalName();
        id = COUNT++;
    }

    public droneConnection(NBTTagCompound aNBT) {
        NBTTagCompound machineTag = aNBT.getCompoundTag("machine");
        NBTTagCompound centreTag = aNBT.getCompoundTag("centre");
        this.world = DimensionManager.getWorld(aNBT.getInteger("worldID"));
        machineCoord = new ChunkCoordinates(
            machineTag.getInteger("x"),
            machineTag.getInteger("y"),
            machineTag.getInteger("z"));
        this.machine = getGT_BaseMachineAt(machineCoord, world);
        centreCoord = new ChunkCoordinates(
            centreTag.getInteger("x"),
            centreTag.getInteger("y"),
            centreTag.getInteger("z"));
        this.centre = (GT_MetaTileEntity_DroneCentre) getGT_BaseMachineAt(centreCoord, world);
        this.id = aNBT.getInteger("id");
        this.customName = aNBT.getString("name");
    }

    public GT_MetaTileEntity_MultiBlockBase getMachine() {
        return machine;
    }

    public boolean reCheckConnection() {
        if (machine == null) this.machine = getGT_BaseMachineAt(machineCoord, world);
        if (centre == null) this.centre = (GT_MetaTileEntity_DroneCentre) getGT_BaseMachineAt(centreCoord, world);
        if (machine != null && centre != null && !centre.connectionList.contains(this)) centre.connectionList.add(this);
        return isValid();
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String name) {
        customName = name;
    }

    public NBTTagCompound transConnectionToNBT() {
        NBTTagCompound aNBT = new NBTTagCompound();
        if (!this.isValid()) return aNBT;
        aNBT.setTag("machine", transGT_BaseMachineToNBT(machine));
        aNBT.setTag("centre", transGT_BaseMachineToNBT(centre));
        aNBT.setInteger(
            "worldID",
            machine.getBaseMetaTileEntity()
                .getWorld().provider.dimensionId);
        aNBT.setInteger("id", id);
        aNBT.setString("name", getCustomName());
        return aNBT;
    }

    public GT_MetaTileEntity_MultiBlockBase getGT_BaseMachineAt(ChunkCoordinates coords, World world) {
        TileEntity te = GT_Util.getTileEntity(world, coords, false);
        if (te == null) return null;
        return (GT_MetaTileEntity_MultiBlockBase) ((IGregTechTileEntity) te).getMetaTileEntity();
    }

    public NBTTagCompound transGT_BaseMachineToNBT(GT_MetaTileEntity_MultiBlockBase machine) {
        IHasWorldObjectAndCoords baseCoord = machine.getBaseMetaTileEntity();
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("x", baseCoord.getXCoord());
        tag.setInteger("y", baseCoord.getYCoord());
        tag.setInteger("z", baseCoord.getZCoord());
        return tag;
    }

    public boolean isValid() {
        return machine != null && machine.isValid() && centre != null && centre.isValid();
    }
}
