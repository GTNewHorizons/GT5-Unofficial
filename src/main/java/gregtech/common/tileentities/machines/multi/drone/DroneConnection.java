package gregtech.common.tileentities.machines.multi.drone;

import static gregtech.GT_Mod.gregtechproxy;

import java.util.Optional;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Util;
import gregtech.api.util.GT_Utility;

public class DroneConnection {

    String customName;
    String unlocalizedName;
    GT_MetaTileEntity_MultiBlockBase machine;
    ItemStack machineItem;
    ChunkCoordinates machineCoord;
    GT_MetaTileEntity_DroneCentre centre;
    ChunkCoordinates centreCoord;
    World world;

    public DroneConnection(GT_MetaTileEntity_MultiBlockBase machine, GT_MetaTileEntity_DroneCentre centre) {
        this.machine = machine;
        this.machineItem = machine.getStackForm(1);
        machineCoord = machine.getBaseMetaTileEntity()
            .getCoords();
        this.centre = centre;
        centreCoord = centre.getBaseMetaTileEntity()
            .getCoords();
        this.world = centre.getBaseMetaTileEntity()
            .getWorld();
        unlocalizedName = machine.mName;
        customName = Optional.ofNullable(centre.tempNameList.remove(machineCoord.toString()))
            .orElse(machine.getLocalName());
    }

    public DroneConnection(NBTTagCompound aNBT) {
        NBTTagCompound machineTag = aNBT.getCompoundTag("machine");
        NBTTagCompound centreTag = aNBT.getCompoundTag("centre");
        if (!gregtechproxy.isClientSide()) {
            this.world = DimensionManager.getWorld(aNBT.getInteger("worldID"));
        } else {
            this.world = Minecraft.getMinecraft().thePlayer.worldObj;
        }
        machineItem = ItemStack.loadItemStackFromNBT(aNBT.getCompoundTag("item"));
        machineCoord = new ChunkCoordinates(
            machineTag.getInteger("x"),
            machineTag.getInteger("y"),
            machineTag.getInteger("z"));
        this.machine = getLoadedGT_BaseMachineAt(machineCoord, world, true);
        centreCoord = new ChunkCoordinates(
            centreTag.getInteger("x"),
            centreTag.getInteger("y"),
            centreTag.getInteger("z"));
        this.centre = (GT_MetaTileEntity_DroneCentre) getLoadedGT_BaseMachineAt(centreCoord, world, true);
        this.customName = aNBT.getString("name");
        this.unlocalizedName = aNBT.getString("unlocalizedName");
    }

    public GT_MetaTileEntity_MultiBlockBase getMachine() {
        return machine;
    }

    public boolean reCheckConnection() {
        if (machine == null) this.machine = getLoadedGT_BaseMachineAt(machineCoord, world, true);
        if (centre == null)
            this.centre = (GT_MetaTileEntity_DroneCentre) getLoadedGT_BaseMachineAt(centreCoord, world, true);
        if (machine != null && centre != null
            && !centre.getConnectionList()
                .contains(this))
            centre.getConnectionList()
                .add(this);
        return isValid();
    }

    public String getCustomName(boolean localized) {
        if (localized) return GT_LanguageManager.getTranslation("gt.blockmachines." + unlocalizedName + ".name");
        return customName;
    }

    public float getDistance() {
        return centreCoord.getDistanceSquaredToChunkCoordinates(machineCoord);
    }

    public void setCustomName(String name) {
        customName = name;
    }

    public boolean isMachineShutdown() {
        return machine != null && machine.shouldDisplayShutDownReason()
            && !machine.getBaseMetaTileEntity()
                .isActive()
            && GT_Utility.isStringValid(
                machine.getBaseMetaTileEntity()
                    .getLastShutDownReason()
                    .getDisplayString())
            && machine.getBaseMetaTileEntity()
                .wasShutdown();
    }

    public NBTTagCompound transConnectionToNBT() {
        NBTTagCompound aNBT = new NBTTagCompound();
        aNBT.setTag("machine", transCoordsToNBT(machineCoord));
        aNBT.setTag("centre", transCoordsToNBT(centreCoord));
        aNBT.setTag("item", machineItem.writeToNBT(new NBTTagCompound()));
        aNBT.setInteger(
            "worldID",
            centre.getBaseMetaTileEntity()
                .getWorld().provider.dimensionId);
        aNBT.setString("name", getCustomName(false));
        aNBT.setString("unlocalizedName", unlocalizedName);
        return aNBT;
    }

    public GT_MetaTileEntity_MultiBlockBase getLoadedGT_BaseMachineAt(ChunkCoordinates coords, World world,
        boolean isLoaded) {
        TileEntity te = GT_Util.getTileEntity(world, coords, isLoaded);
        if (te == null) return null;
        return (GT_MetaTileEntity_MultiBlockBase) ((IGregTechTileEntity) te).getMetaTileEntity();
    }

    private NBTTagCompound transCoordsToNBT(ChunkCoordinates coord) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("x", coord.posX);
        tag.setInteger("y", coord.posY);
        tag.setInteger("z", coord.posZ);
        return tag;
    }

    public boolean isValid() {
        return machine != null && machine.isValid() && centre != null && centre.isValid();
    }
}
