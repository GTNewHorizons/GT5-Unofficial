package gregtech.common.misc;

import static gregtech.common.misc.GlobalVariableStorage.GlobalEnergy;
import static gregtech.common.misc.GlobalVariableStorage.GlobalEnergyName;
import static gregtech.common.misc.GlobalVariableStorage.GlobalEnergyTeam;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.util.HashMap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.event.world.WorldEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class GlobalEnergyWorldSavedData extends WorldSavedData {

    public static GlobalEnergyWorldSavedData INSTANCE;

    private static final String DATA_NAME = "GregTech_WirelessEUWorldSavedData";

    private static final String GlobalEnergyNBTTag = "GregTech_GlobalEnergy_MapNBTTag";
    private static final String GlobalEnergyNameNBTTag = "GregTech_GlobalEnergyName_MapNBTTag";
    private static final String GlobalEnergyTeamNBTTag = "GregTech_GlobalEnergyTeam_MapNBTTag";

    private static void loadInstance(World world) {

        GlobalEnergy.clear();
        GlobalEnergyTeam.clear();
        GlobalEnergyName.clear();

        MapStorage storage = world.mapStorage;
        INSTANCE = (GlobalEnergyWorldSavedData) storage.loadData(GlobalEnergyWorldSavedData.class, DATA_NAME);
        if (INSTANCE == null) {
            INSTANCE = new GlobalEnergyWorldSavedData();
            storage.setData(DATA_NAME, INSTANCE);
        }
        INSTANCE.markDirty();
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (!event.world.isRemote && event.world.provider.dimensionId == 0) {
            loadInstance(event.world);
        }
    }

    public GlobalEnergyWorldSavedData() {
        super(DATA_NAME);
    }

    @SuppressWarnings("unused")
    public GlobalEnergyWorldSavedData(String name) {
        super(name);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void readFromNBT(NBTTagCompound nbtTagCompound) {

        try {
            byte[] ba = nbtTagCompound.getByteArray(GlobalEnergyNBTTag);
            InputStream byteArrayInputStream = new ByteArrayInputStream(ba);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object data = objectInputStream.readObject();
            GlobalEnergy = (HashMap<String, BigInteger>) data;
        } catch (IOException | ClassNotFoundException exception) {
            System.out.println(GlobalEnergyNBTTag + " FAILED");
            exception.printStackTrace();
        }

        try {
            byte[] ba = nbtTagCompound.getByteArray(GlobalEnergyNameNBTTag);
            InputStream byteArrayInputStream = new ByteArrayInputStream(ba);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object data = objectInputStream.readObject();
            GlobalEnergyName = (HashMap<String, String>) data;
        } catch (IOException | ClassNotFoundException exception) {
            System.out.println(GlobalEnergyNameNBTTag + " FAILED");
            exception.printStackTrace();
        }

        try {
            byte[] ba = nbtTagCompound.getByteArray(GlobalEnergyTeamNBTTag);
            InputStream byteArrayInputStream = new ByteArrayInputStream(ba);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object data = objectInputStream.readObject();
            GlobalEnergyTeam = (HashMap<String, String>) data;
        } catch (IOException | ClassNotFoundException exception) {
            System.out.println(GlobalEnergyTeamNBTTag + " FAILED");
            exception.printStackTrace();
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(GlobalEnergy);
            objectOutputStream.flush();
            byte[] data = byteArrayOutputStream.toByteArray();
            nbtTagCompound.setByteArray(GlobalEnergyNBTTag, data);
        } catch (IOException exception) {
            System.out.println(GlobalEnergyNBTTag + " SAVE FAILED");
            exception.printStackTrace();
        }

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(GlobalEnergyName);
            objectOutputStream.flush();
            byte[] data = byteArrayOutputStream.toByteArray();
            nbtTagCompound.setByteArray(GlobalEnergyNameNBTTag, data);
        } catch (Exception exception) {
            System.out.println(GlobalEnergyNameNBTTag + " SAVE FAILED");
            exception.printStackTrace();
        }

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(GlobalEnergyTeam);
            objectOutputStream.flush();
            byte[] data = byteArrayOutputStream.toByteArray();
            nbtTagCompound.setByteArray(GlobalEnergyTeamNBTTag, data);
        } catch (IOException exception) {
            System.out.println(GlobalEnergyTeamNBTTag + " SAVE FAILED");
            exception.printStackTrace();
        }
    }
}
