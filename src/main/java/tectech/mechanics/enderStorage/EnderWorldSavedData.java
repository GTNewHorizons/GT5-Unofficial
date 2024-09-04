package tectech.mechanics.enderStorage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fluids.IFluidHandler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import tectech.Reference;

public class EnderWorldSavedData extends WorldSavedData {

    private static EnderWorldSavedData INSTANCE;

    private static final String DATA_NAME = Reference.MODID + "_EnderWorldSavedData";
    private static final String ENDER_LIQUID_TAG_LINK = DATA_NAME + "_EnderLiquidTagLink";
    private static final String ENDER_LIQUID_TANK_LINK = DATA_NAME + "_EnderLiquidTankLink";
    private static final EnderLinkTag DEFAULT_LINK_TAG = new EnderLinkTag("", null);

    private Map<EnderLinkTag, EnderFluidContainer> EnderLiquidTagLink = new HashMap<>();
    private Map<EnderLinkTank, EnderLinkTag> EnderLiquidTankLink = new HashMap<>();

    public EnderWorldSavedData() {
        super(DATA_NAME);
    }

    public EnderWorldSavedData(String s) {
        super(s);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        try {
            byte[] ba = nbtTagCompound.getByteArray(ENDER_LIQUID_TAG_LINK);
            InputStream is = new ByteArrayInputStream(ba);
            ObjectInputStream ois = new ObjectInputStream(is);
            Object data = ois.readObject();
            EnderLiquidTagLink = (Map<EnderLinkTag, EnderFluidContainer>) data;
        } catch (IOException | ClassNotFoundException ignored) {
            System.out.println("ENDER_LIQUID_TAG_LINK LOAD FAILED");
            System.out.println(ignored);
        }

        try {
            byte[] ba = nbtTagCompound.getByteArray(ENDER_LIQUID_TANK_LINK);
            InputStream is = new ByteArrayInputStream(ba);
            ObjectInputStream ois = new ObjectInputStream(is);
            Object data = ois.readObject();
            EnderLiquidTankLink = (Map<EnderLinkTank, EnderLinkTag>) data;
        } catch (IOException | ClassNotFoundException ignored) {
            System.out.println("ENDER_LIQUID_TANK_LINK LOAD FAILED");
            System.out.println(ignored);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(EnderLiquidTagLink);
            oos.flush();
            byte[] data = bos.toByteArray();
            nbtTagCompound.setByteArray(ENDER_LIQUID_TAG_LINK, data);
        } catch (IOException ignored) {
            System.out.println("ENDER_LIQUID_TAG_LINK SAVE FAILED");
            System.out.println(ignored);
        }

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(EnderLiquidTankLink);
            oos.flush();
            byte[] data = bos.toByteArray();
            nbtTagCompound.setByteArray(ENDER_LIQUID_TANK_LINK, data);
        } catch (IOException ignored) {
            System.out.println("ENDER_LIQUID_TANK_LINK SAVE FAILED");
            System.out.println(ignored);
        }
    }

    private static void loadInstance() {
        if (INSTANCE == null) {
            MapStorage storage = DimensionManager.getWorld(0).mapStorage;
            INSTANCE = (EnderWorldSavedData) storage.loadData(EnderWorldSavedData.class, DATA_NAME);
            if (INSTANCE == null) {
                INSTANCE = new EnderWorldSavedData();
                storage.setData(DATA_NAME, INSTANCE);
            }
        }
        INSTANCE.markDirty();
    }

    private static Map<EnderLinkTag, EnderFluidContainer> getEnderLiquidLink() {
        loadInstance();
        return INSTANCE.EnderLiquidTagLink;
    }

    private static Map<EnderLinkTank, EnderLinkTag> getEnderLiquidTankLink() {
        loadInstance();
        return INSTANCE.EnderLiquidTankLink;
    }

    public static EnderFluidContainer getEnderFluidContainer(EnderLinkTag tag) {
        if (!getEnderLiquidLink().containsKey(tag)) {
            getEnderLiquidLink().put(tag, new EnderFluidContainer());
        }
        return getEnderLiquidLink().get(tag);
    }

    public static EnderLinkTag getEnderLinkTag(IFluidHandler handler) {
        EnderLinkTank tank = new EnderLinkTank(handler);
        if (!getEnderLiquidTankLink().containsKey(tank)) {
            getEnderLiquidTankLink().put(tank, DEFAULT_LINK_TAG);
        }
        return getEnderLiquidTankLink().get(tank);
    }

    public static void bindEnderLinkTag(IFluidHandler handler, EnderLinkTag tag) {
        EnderLinkTank tank = new EnderLinkTank(handler);
        getEnderLiquidTankLink().remove(tank);
        getEnderLiquidTankLink().put(tank, tag);
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        if (event.world.provider.dimensionId == 0) {
            INSTANCE = null;
        }
    }
}
