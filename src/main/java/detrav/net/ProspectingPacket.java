package detrav.net;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.google.common.base.Objects;

import bartworks.system.material.Werkstoff;
import detrav.DetravScannerMod;
import detrav.gui.DetravScannerGUI;
import detrav.gui.textures.DetravMapTexture;
import detrav.utils.GTppHelper;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.util.GTLanguageManager;

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
    public final HashMap<Byte, Short>[][] map;
    public final HashMap<String, Integer> ores;
    public final HashMap<Short, String> metaMap;
    public static final HashMap<Integer, short[]> fluidColors = new HashMap<>();

    public int level = -1;

    public ProspectingPacket(int chunkX, int chunkZ, int posX, int posZ, int size, int ptype) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.posX = posX;
        this.posZ = posZ;
        this.size = size;
        this.ptype = ptype;
        this.map = new HashMap[(size * 2 + 1) * 16][(size * 2 + 1) * 16];
        this.ores = new HashMap<>();
        this.metaMap = new HashMap<>();
    }

    private static void addOre(ProspectingPacket packet, byte y, int i, int j, short meta) {
        final String name;
        short[] rgba;

        try {
            if (packet.ptype == 0 || packet.ptype == 1) {
                // Ore or Small Ore
                if (meta < 7000 || meta > 7500) {
                    if (meta > 0) {
                        Materials tMaterial = GregTechAPI.sGeneratedMaterials[meta % 1000];
                        rgba = tMaterial.getRGBA();
                        name = tMaterial.getLocalizedNameForItem(
                            GTLanguageManager.getTranslation("gt.blockores." + meta + ".name"));
                    } else {
                        final Werkstoff werkstoff = Werkstoff.werkstoffHashMap.getOrDefault((short) (meta * -1), null);
                        String translated = GTLanguageManager.getTranslation("bw.blocktype.ore");
                        name = translated.replace("%material", werkstoff.getLocalizedName());
                        rgba = werkstoff != null ? werkstoff.getRGBA() : new short[] { 0, 0, 0, 0 };
                    }
                } else {
                    gtPlusPlus.core.material.Material pMaterial = GTppHelper.decodeoresGTpp.get((short) (meta - 7000));
                    rgba = pMaterial.getRGBA();
                    name = pMaterial.getLocalizedName() + " Ore";
                }
            } else if (packet.ptype == 2) {
                // Fluid
                rgba = fluidColors.get((int) meta);
                if (rgba == null) {
                    DetravScannerMod.proxy
                        .sendPlayerExeption("Unknown fluid ID = " + meta + " Please add to FluidColors.java!");
                    rgba = new short[] { 0, 0, 0, 0 };
                }

                name = Objects.firstNonNull(
                    FluidRegistry.getFluid(meta)
                        .getLocalizedName(new FluidStack(FluidRegistry.getFluid(meta), 0)),
                    StatCollector.translateToLocal("gui.detrav.scanner.unknown_fluid"));
            } else if (packet.ptype == 3) {
                // Pollution
                name = StatCollector.translateToLocal("gui.detrav.scanner.pollution");
                rgba = new short[] { 125, 123, 118, 0 };
            } else {
                return;
            }
        } catch (Exception ignored) {
            return;
        }
        packet.ores.put(name, ((rgba[0] & 0xFF) << 16) + ((rgba[1] & 0xFF) << 8) + ((rgba[2] & 0xFF)));
        packet.metaMap.put(meta, name);
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
        packet.level = aData.readInt();

        int aSize = (packet.size * 2 + 1) * 16;
        int checkOut = 0;
        for (int i = 0; i < aSize; i++) for (int j = 0; j < aSize; j++) {
            byte kSize = aData.readByte();
            if (kSize == 0) continue;
            packet.map[i][j] = new HashMap<>();
            for (int k = 0; k < kSize; k++) {
                final byte y = aData.readByte();
                final short meta = aData.readShort();
                packet.map[i][j].put(y, meta);
                if (packet.ptype != 2 || y == 1) addOre(packet, y, i, j, meta);
                checkOut++;
            }
        }
        int checkOut2 = aData.readInt();
        if (checkOut != checkOut2) return null;
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
        tOut.writeInt(level);

        int aSize = (size * 2 + 1) * 16;
        int checkOut = 0;
        for (int i = 0; i < aSize; i++) for (int j = 0; j < aSize; j++) {
            HashMap<Byte, Short> data = map[i][j];
            if (data == null) tOut.writeByte(0);
            else {
                tOut.writeByte(
                    data.keySet()
                        .size());
                for (byte key : data.keySet()) {
                    tOut.writeByte(key);
                    tOut.writeShort(data.get(key));
                    checkOut++;
                }
            }
        }
        tOut.writeInt(checkOut);
        tOut.close();
    }

    @Override
    public void process() {
        DetravScannerGUI.newMap(new DetravMapTexture(this));
        DetravScannerMod.proxy.openProspectorGUI();
    }

    public void addBlock(int x, int y, int z, short metaData) {
        int aX = x - (chunkX - size) * 16;
        int aZ = z - (chunkZ - size) * 16;
        if (map[aX][aZ] == null) map[aX][aZ] = new HashMap<>();
        map[aX][aZ].put((byte) y, metaData);
    }

    public int getSize() {
        return (size * 2 + 1) * 16;
    }
}
