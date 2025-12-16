package tectech.mechanics.enderStorage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
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
    private static final String ENDER_LIQUID_TAG_VERSION = DATA_NAME + "_Version";

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
        int version = nbtTagCompound.getInteger(ENDER_LIQUID_TAG_VERSION);

        switch (version) {
            case 0: {
                try {
                    byte[] bytes = nbtTagCompound.getByteArray(ENDER_LIQUID_TAG_LINK);

                    try (InputStream is = new ByteArrayInputStream(bytes);
                        ObjectInputStream objectStream = new MigratingObjectInputStream(is);) {
                        EnderLiquidTagLink = (Map<EnderLinkTag, EnderFluidContainer>) objectStream.readObject();
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("ENDER_LIQUID_TAG_LINK LOAD FAILED");
                    e.printStackTrace();
                }

                try {
                    byte[] bytes = nbtTagCompound.getByteArray(ENDER_LIQUID_TANK_LINK);
                    try (InputStream is = new ByteArrayInputStream(bytes);
                        ObjectInputStream objectStream = new MigratingObjectInputStream(is);) {
                        EnderLiquidTankLink = (Map<EnderLinkTank, EnderLinkTag>) objectStream.readObject();
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("ENDER_LIQUID_TANK_LINK LOAD FAILED");
                    e.printStackTrace();
                }
                break;
            }
            case 1: {
                List<NBTTagCompound> tags = ((NBTTagList) nbtTagCompound.getTag(ENDER_LIQUID_TAG_LINK)).tagList;

                EnderLiquidTagLink = new HashMap<>();

                for (NBTTagCompound tagLink : tags) {
                    EnderLinkTag tag = EnderLinkTag.load((NBTTagCompound) tagLink.getTag("k"));
                    EnderFluidContainer container = EnderFluidContainer.load((NBTTagCompound) tagLink.getTag("v"));
                    EnderLiquidTagLink.put(tag, container);
                }

                List<NBTTagCompound> tanks = ((NBTTagList) nbtTagCompound.getTag(ENDER_LIQUID_TANK_LINK)).tagList;

                EnderLiquidTankLink = new HashMap<>();

                for (NBTTagCompound tankLink : tanks) {
                    EnderLinkTank tank = EnderLinkTank.load((NBTTagCompound) tankLink.getTag("k"));
                    EnderLinkTag tag = EnderLinkTag.load((NBTTagCompound) tankLink.getTag("v"));
                    EnderLiquidTankLink.put(tank, tag);
                }

                break;
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        nbtTagCompound.setInteger(ENDER_LIQUID_TAG_VERSION, 1);

        HashSet<EnderLinkTag> usedTags = new HashSet<>();

        NBTTagList tanks = new NBTTagList();

        EnderLiquidTankLink.forEach((tank, tag) -> {
            if (tank.shouldSave()) {
                usedTags.add(tag);
                NBTTagCompound entry = new NBTTagCompound();
                entry.setTag("k", tank.save());
                entry.setTag("v", tag.save());
                tanks.appendTag(entry);
            }
        });

        nbtTagCompound.setTag(ENDER_LIQUID_TANK_LINK, tanks);
        NBTTagList tags = new NBTTagList();

        EnderLiquidTagLink.forEach((tag, container) -> {
            if (usedTags.contains(tag)) {
                NBTTagCompound entry = new NBTTagCompound();
                entry.setTag("k", tag.save());
                entry.setTag("v", container.save());
                tags.appendTag(entry);
            }
        });

        nbtTagCompound.setTag(ENDER_LIQUID_TAG_LINK, tags);
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
        EnderLinkTag tag = getEnderLiquidTankLink().get(tank);

        if (tag != null && Objects.equals(tag.getFrequency(), "")) {
            tag = null;
        }

        return tag;
    }

    public static void bindEnderLinkTag(IFluidHandler handler, EnderLinkTag tag) {
        EnderLinkTank tank = new EnderLinkTank(handler);

        if (!tag.equals(getEnderLiquidTankLink().get(tank))) {
            unbindTank(handler);

            getEnderLiquidTankLink().remove(tank);
            getEnderLiquidTankLink().put(tank, tag);
        }
    }

    public static void unbindTank(IFluidHandler handler) {
        EnderLinkTank tank = new EnderLinkTank(handler);
        EnderLinkTag oldTag = getEnderLiquidTankLink().remove(tank);

        if (oldTag != null) {
            boolean isReferenced = getEnderLiquidTankLink().values()
                .contains(oldTag);

            if (!isReferenced) {
                getEnderLiquidLink().remove(oldTag);
            }
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        if (event.world.provider.dimensionId == 0) {
            INSTANCE = null;
        }
    }

    private static class MigratingObjectInputStream extends ObjectInputStream {

        public MigratingObjectInputStream(InputStream in) throws IOException {
            super(in);
        }

        @Override
        protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
            ObjectStreamClass resultClassDescriptor = super.readClassDescriptor();

            String className = resultClassDescriptor.getName();

            if (className.startsWith("com.github.technus.")) {
                resultClassDescriptor = ObjectStreamClass
                    .lookup(Class.forName(className.replace("com.github.technus.", "")));
            }

            return resultClassDescriptor;
        }
    }
}
