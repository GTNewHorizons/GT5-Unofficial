package detrav.net;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.gtnhlib.util.CoordinatePacker;

import detrav.DetravScannerMod;
import detrav.gui.DetravScannerGUI;
import detrav.gui.textures.DetravMapTexture;
import detrav.utils.FluidColors;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.common.ores.OreManager;
import it.unimi.dsi.fastutil.longs.Long2ShortOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ShortOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIntPair;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;

/**
 * Created by wital_000 on 20.03.2016.
 */
public class ProspectingPacket extends DetravPacket {

    public final int chunkX;
    public final int chunkZ;
    public final int posX;
    public final int posZ;
    public final int size;
    public final int ptype;
    /** {packed x,y,z: object id} */
    public final Long2ShortOpenHashMap map = new Long2ShortOpenHashMap();
    /** {object id: (object name, object rgba)} */
    public final Short2ObjectOpenHashMap<ObjectIntPair<String>> objects = new Short2ObjectOpenHashMap<>();
    /** {object name: object id} */
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

    public static Object decode(InputStream in) throws IOException {
        DataInput aData = new DataInputStream(new GZIPInputStream(in));
        ProspectingPacket packet = new ProspectingPacket(
            aData.readInt(),
            aData.readInt(),
            aData.readInt(),
            aData.readInt(),
            aData.readInt(),
            aData.readInt());

        int objectCount = aData.readInt();
        packet.objects.ensureCapacity(objectCount);

        for (int i = 0; i < objectCount; i++) {
            short objectId = aData.readShort();
            String name = aData.readUTF();
            int rgba = aData.readInt();

            packet.objects.put(objectId, ObjectIntPair.of(name, rgba));
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
    public void encode(OutputStream out) throws IOException {
        DataOutputStream tOut = new DataOutputStream(new GZIPOutputStream(out));
        tOut.writeInt(chunkX);
        tOut.writeInt(chunkZ);
        tOut.writeInt(posX);
        tOut.writeInt(posZ);
        tOut.writeInt(size);
        tOut.writeInt(ptype);

        tOut.writeInt(objects.size());

        for (var obj : objects.short2ObjectEntrySet()) {
            tOut.writeShort(obj.getShortKey());
            tOut.writeUTF(
                obj.getValue()
                    .left());
            tOut.writeInt(
                obj.getValue()
                    .rightInt());
        }

        tOut.writeInt(map.size());

        for (var instance : map.long2ShortEntrySet()) {
            tOut.writeLong(instance.getLongKey());
            tOut.writeShort(instance.getShortValue());
        }

        tOut.close();
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

            IOreMaterial mat = OreManager.getMaterial(block, meta);

            short[] rgba = mat == null ? new short[] { 125, 125, 125, 255 } : mat.getRGBA();

            nameLookup.put(stackName, objectId);
            objects.put(objectId, ObjectIntPair.of(stackName, rgba(rgba)));
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
            objects.put(objectId, ObjectIntPair.of(stackName, rgba(FluidColors.getColor(fluid.getFluidID()))));
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
}
