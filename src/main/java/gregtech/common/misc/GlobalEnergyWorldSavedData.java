package gregtech.common.misc;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.common.DimensionManager;

import java.io.*;
import java.math.BigInteger;
import java.util.HashMap;
import static gregtech.common.misc.GlobalVariableStorage.*;


public class GlobalEnergyWorldSavedData extends WorldSavedData {

    private static GlobalEnergyWorldSavedData INSTANCE;


    private static final String DATA_NAME = "GregTech_WirelessEUWorldSavedData";

    private static final String GlobalEnergyNBTTag = "GregTech_GlobalEnergy_MapNBTTag";
    private static final String GlobalEnergyNameNBTTag = "GregTech_GlobalEnergyName_MapNBTTag";
    private static final String GlobalEnergyTeamNBTTag = "GregTech_GlobalEnergyTeam_MapNBTTag";

    private static void loadInstance() {
        if (INSTANCE == null) {
            MapStorage storage = DimensionManager.getWorld(0).mapStorage;
            INSTANCE = (GlobalEnergyWorldSavedData) storage.loadData(GlobalEnergyWorldSavedData.class, DATA_NAME);
            if (INSTANCE == null) {
                INSTANCE = new GlobalEnergyWorldSavedData();
                storage.setData(DATA_NAME, INSTANCE);
            }
        }
        INSTANCE.markDirty();
    }


    public GlobalEnergyWorldSavedData() {
        super(DATA_NAME);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {

        try {
            byte[] ba = nbtTagCompound.getByteArray(GlobalEnergyNBTTag);
            InputStream byteArrayInputStream = new ByteArrayInputStream(ba);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object data = objectInputStream.readObject();
            GlobalEnergy = (HashMap<String, BigInteger>) data;
        } catch (IOException | ClassNotFoundException ignored) {
            System.out.println(GlobalEnergyNBTTag + " FAILED");
            System.out.println(ignored);
        }

        try {
            byte[] ba = nbtTagCompound.getByteArray(GlobalEnergyNameNBTTag);
            InputStream byteArrayInputStream = new ByteArrayInputStream(ba);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object data = objectInputStream.readObject();
            GlobalEnergyName = (HashMap<String, String>) data;
        } catch (IOException | ClassNotFoundException ignored) {
            System.out.println(GlobalEnergyNameNBTTag + " FAILED");
            System.out.println(ignored);
        }

        try {
            byte[] ba = nbtTagCompound.getByteArray(GlobalEnergyTeamNBTTag);
            InputStream byteArrayInputStream = new ByteArrayInputStream(ba);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object data = objectInputStream.readObject();
            GlobalEnergyTeam = (HashMap<String, String>) data;
        } catch (IOException | ClassNotFoundException ignored) {
            System.out.println(GlobalEnergyTeamNBTTag + " FAILED");
            System.out.println(ignored);
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
        } catch (IOException ignored) {
            System.out.println(GlobalEnergyNBTTag + " SAVE FAILED");
            System.out.println(ignored);
        }

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(GlobalEnergyName);
            objectOutputStream.flush();
            byte[] data = byteArrayOutputStream.toByteArray();
            nbtTagCompound.setByteArray(GlobalEnergyNameNBTTag, data);
        } catch (IOException ignored) {
            System.out.println(GlobalEnergyNameNBTTag + " SAVE FAILED");
            System.out.println(ignored);
        }

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(GlobalEnergyTeam);
            objectOutputStream.flush();
            byte[] data = byteArrayOutputStream.toByteArray();
            nbtTagCompound.setByteArray(GlobalEnergyTeamNBTTag, data);
        } catch (IOException ignored) {
            System.out.println(GlobalEnergyTeamNBTTag + " SAVE FAILED");
            System.out.println(ignored);
        }

    }
}
