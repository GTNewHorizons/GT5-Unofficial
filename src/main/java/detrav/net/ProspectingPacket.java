package detrav.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.gtnhlib.util.CoordinatePacker;

import cpw.mods.fml.common.network.ByteBufUtils;
import detrav.DetravScannerMod;
import detrav.gui.DetravScannerGUI;
import detrav.gui.textures.DetravMapTexture;
import detrav.items.DetravMetaGeneratedTool01;
import detrav.utils.FluidColors;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.common.ores.OreManager;
import it.unimi.dsi.fastutil.longs.Long2ShortOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ShortOpenHashMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;

/**
 * Created by wital_000 on 20.03.2016.
 */
public class ProspectingPacket extends DetravPacket {

    public static abstract class BasicInfo {

        public String name;
        public int rgba;
    }

    public static class BlockInfo extends BasicInfo {

        public ItemStack stack;
        /** Used to display an icon. */
        public String internalName;

        public BlockInfo(ItemStack stack) {
            this.stack = stack;
            this.name = stack.getDisplayName();
            IOreMaterial mat = OreManager.getMaterial(stack);
            short[] rgba1 = mat == null ? new short[] { 125, 125, 125, 255 } : mat.getRGBA();
            this.rgba = rgba(rgba1);
            this.internalName = mat == null ? "" : mat.getInternalName();
        }
    }

    public static class FluidInfo extends BasicInfo {

        public FluidStack stack;

        public FluidInfo(FluidStack stack) {
            this.stack = stack;
            this.name = stack.getLocalizedName();
            this.rgba = rgba(FluidColors.getColor(stack.getFluidID()));
        }
    }

    public final int chunkX;
    public final int chunkZ;
    public final int posX;
    public final int posZ;
    public final int size;
    public final int ptype;
    /**
     * {packed x,y,z: object id}
     * <br>
     * if `ptype` is ore: the value is the object id.
     * <br>
     * If `ptype` is pollution:
     * if y == 1, the value is the lower 16 bits of the pollution quantity;
     * if y == 2, the value is the upper 16 bits of the pollution quantity.
     * <br>
     * if `ptype` is fluid:
     * if y == 0, the value is the object id;
     * if y == 1, the value is the lower 16 bits of the fluid amount;
     * if y == 2, the value is the upper 16 bits of the fluid amount.
     */
    public final Long2ShortOpenHashMap map = new Long2ShortOpenHashMap();
    /**
     * {object id: (item stack, localized name, object rgba, mat internal name)}
     */
    public final Short2ObjectOpenHashMap<BlockInfo> items = new Short2ObjectOpenHashMap<>();
    /**
     * {object id: (fluid stack, localized name, object rgba)}
     */
    public final Short2ObjectOpenHashMap<FluidInfo> fluids = new Short2ObjectOpenHashMap<>();
    /**
     * {object name: object id}
     */
    private final Object2ShortOpenHashMap<String> nameLookup = new Object2ShortOpenHashMap<>();

    public ProspectingPacket(int chunkX, int chunkZ, int posX, int posZ, int size, int ptype) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.posX = posX;
        this.posZ = posZ;
        this.size = size;
        this.ptype = ptype;
    }

    private static int rgba(short[] rgba) {
        return (0xFF << 24) | ((rgba[0] & 0xFF) << 16) + ((rgba[1] & 0xFF) << 8) + ((rgba[2] & 0xFF));
    }

    public static Object decode(PacketBuffer aData) throws IOException {
        ProspectingPacket packet = new ProspectingPacket(
            aData.readInt(),
            aData.readInt(),
            aData.readInt(),
            aData.readInt(),
            aData.readInt(),
            aData.readInt());

        int itemCount = aData.readInt();
        packet.items.ensureCapacity(itemCount);
        for (int i = 0; i < itemCount; i++) {
            short objectId = aData.readShort();
            ItemStack item = ByteBufUtils.readItemStack(aData);
            packet.items.put(objectId, new BlockInfo(item));
        }

        int fluidCount = aData.readInt();
        packet.fluids.ensureCapacity(fluidCount);
        for (int i = 0; i < fluidCount; i++) {
            short objectId = aData.readShort();
            NBTTagCompound nbt = aData.readNBTTagCompoundFromBuffer();
            FluidStack fluid = FluidStack.loadFluidStackFromNBT(nbt);
            packet.fluids.put(objectId, new FluidInfo(fluid));
        }

        int instanceCount = aData.readInt();
        packet.map.ensureCapacity(instanceCount);

        for (int i = 0; i < instanceCount; i++) {
            long coord = aData.readLong();
            short objectId = aData.readShort();

            packet.map.put(coord, objectId);
        }

        return packet;
    }

    @Override
    public int getPacketID() {
        return 0;
    }

    @Override
    public void encode(PacketBuffer tOut) throws IOException {
        tOut.writeInt(chunkX);
        tOut.writeInt(chunkZ);
        tOut.writeInt(posX);
        tOut.writeInt(posZ);
        tOut.writeInt(size);
        tOut.writeInt(ptype);

        tOut.writeInt(items.size());
        for (var obj : items.short2ObjectEntrySet()) {
            tOut.writeShort(obj.getShortKey());
            ByteBufUtils.writeItemStack(tOut, obj.getValue().stack);
        }
        tOut.writeInt(fluids.size());
        for (var obj : fluids.short2ObjectEntrySet()) {
            tOut.writeShort(obj.getShortKey());
            var nbt = obj.getValue().stack.writeToNBT(new NBTTagCompound());
            tOut.writeNBTTagCompoundToBuffer(nbt);
        }

        tOut.writeInt(map.size());

        for (var instance : map.long2ShortEntrySet()) {
            tOut.writeLong(instance.getLongKey());
            tOut.writeShort(instance.getShortValue());
        }
    }

    @Override
    public void process() {
        DetravScannerGUI.newMap(new DetravMapTexture(this));
        DetravScannerMod.proxy.openProspectorGUI();
    }

    private short nextId = 0;

    public void addBlock(int x, int y, int z, Block block, int meta) {
        int aX = x - (chunkX - size) * 16;
        int aZ = z - (chunkZ - size) * 16;

        ItemStack stack = new ItemStack(block, 1, meta);

        String stackName = stack.getDisplayName();

        short objectId;

        if (nameLookup.containsKey(stackName)) {
            objectId = nameLookup.getShort(stackName);
        } else {
            objectId = nextId++;

            nameLookup.put(stackName, objectId);
            items.put(objectId, new BlockInfo(stack));
        }

        map.put(CoordinatePacker.pack(aX, y, aZ), objectId);
    }

    public void addFluid(int cX, int cZ, FluidStack fluid) {
        int aX = cX - (chunkX - size);
        int aZ = cZ - (chunkZ - size);

        if (fluid == null || fluid.getFluid() == null) return;

        String stackName = fluid.getLocalizedName();

        short objectId;

        if (nameLookup.containsKey(stackName)) {
            objectId = nameLookup.getShort(stackName);
        } else {
            objectId = nextId++;

            nameLookup.put(stackName, objectId);
            fluids.put(objectId, new FluidInfo(fluid));
        }

        int lower = fluid.amount & 0xFFFF;
        int upper = (fluid.amount >> 16) & 0xFFFF;

        map.put(CoordinatePacker.pack(aX, 0, aZ), objectId);
        map.put(CoordinatePacker.pack(aX, 1, aZ), (short) lower);
        map.put(CoordinatePacker.pack(aX, 2, aZ), (short) upper);
    }

    public int getAmount(int absChunkX, int absChunkZ) {
        int lower = Short.toUnsignedInt(map.get(CoordinatePacker.pack(absChunkX, 1, absChunkZ)));
        int upper = Short.toUnsignedInt(map.get(CoordinatePacker.pack(absChunkX, 2, absChunkZ)));

        return (upper << 16) | lower;
    }

    public void addPollution(int cX, int cZ, int amount) {
        int aX = cX - (chunkX - size);
        int aZ = cZ - (chunkZ - size);

        int lower = amount & 0xFFFF;
        int upper = (amount >> 16) & 0xFFFF;

        map.put(CoordinatePacker.pack(aX, 1, aZ), (short) lower);
        map.put(CoordinatePacker.pack(aX, 2, aZ), (short) upper);
    }

    public int getSize() {
        return (size * 2 + 1) * 16;
    }

    public List<String> getAllKeys() {
        switch (this.ptype) {
            case DetravMetaGeneratedTool01.MODE_ALL_ORES, DetravMetaGeneratedTool01.MODE_BIG_ORES, DetravMetaGeneratedTool01.MODE_FLUIDS -> {
                List<String> allKeys = this.basicInfo()
                    .short2ObjectEntrySet()
                    .stream()
                    .map(e -> e.getValue().name)
                    .sorted()
                    .collect(Collectors.toList());
                if (allKeys.size() > 1) {
                    allKeys.addFirst(StatCollector.translateToLocal("gui.detrav.scanner.all"));
                }
                return allKeys;
            }
            case DetravMetaGeneratedTool01.MODE_POLLUTION -> {
                List<String> allKeys = new ArrayList<>();
                allKeys.add(StatCollector.translateToLocal("gui.detrav.scanner.pollution"));
                return allKeys;
            }
        }
        return new ArrayList<>();
    }

    public Short2ObjectOpenHashMap<? extends BasicInfo> basicInfo() {
        switch (this.ptype) {
            case DetravMetaGeneratedTool01.MODE_BIG_ORES, DetravMetaGeneratedTool01.MODE_ALL_ORES -> {
                return this.items;
            }
            case DetravMetaGeneratedTool01.MODE_FLUIDS -> {
                return this.fluids;
            }
        }
        return new Short2ObjectOpenHashMap<>();
    }

    public Object2IntOpenHashMap<String> getColorMap() {
        var colors = new Object2IntOpenHashMap<String>();
        for (var e : this.basicInfo()
            .short2ObjectEntrySet()) {
            var item = e.getValue();
            colors.put(item.name, item.rgba);
        }
        return colors;
    }

}
