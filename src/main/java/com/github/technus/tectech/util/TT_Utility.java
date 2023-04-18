package com.github.technus.tectech.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;
import java.util.function.Function;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.SaveHandler;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.StringUtils;

import com.github.technus.tectech.TecTech;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

/**
 * Created by Tec on 21.03.2017.
 */
public final class TT_Utility {

    private TT_Utility() {}

    private static final StringBuilder STRING_BUILDER = new StringBuilder();
    private static final Map<Locale, Formatter> FORMATTER_MAP = new HashMap<>();

    private static Formatter getFormatter() {
        STRING_BUILDER.setLength(0);
        return FORMATTER_MAP.computeIfAbsent(
                Locale.getDefault(Locale.Category.FORMAT),
                locale -> new Formatter(STRING_BUILDER, locale));
    }

    public static String formatNumberShortExp(double value) {
        return getFormatter().format("%.3E", value).toString();
    }

    public static String formatNumberExp(double value) {
        return getFormatter().format("%+.5E", value).toString();
    }

    public static String formatNumberIntHex(int value) {
        return getFormatter().format("%08X", value).toString();
    }

    @SuppressWarnings("ComparatorMethodParameterNotUsed")
    public static <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map) {
        SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<>((e1, e2) -> {
            int res = e1.getValue().compareTo(e2.getValue());
            return res != 0 ? res : 1; // Special fix to preserve items with equal values
        });
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }

    public static int bitStringToInt(String bits) {
        if (bits == null) {
            return 0;
        }
        if (bits.length() > 32) {
            throw new NumberFormatException("Too long!");
        }
        return Integer.parseInt(bits, 2);
    }

    public static int hexStringToInt(String hex) {
        if (hex == null) {
            return 0;
        }
        if (hex.length() > 8) {
            throw new NumberFormatException("Too long!");
        }
        return Integer.parseInt(hex, 16);
    }

    public static double stringToDouble(String str) {
        if (str == null) {
            return 0;
        }
        return Double.parseDouble(str);
    }

    private static final String SUPER_SCRIPT = "\u207D\u207E*\u207A,\u207B./\u2070\u00B9\u00B2\u00B3\u2074\u2075\u2076\u2077\u2078\u2079:;<\u207C";
    private static final String SUB_SCRIPT = "\u208D\u208E*\u208A,\u208B./\u2080\u2081\u2082\u2083\u2084\u2085\u2086\u2087\u2088\u2089:;<\u208C";

    public static String toSubscript(String s) {
        STRING_BUILDER.setLength(0);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= '(' && c <= '=') {
                STRING_BUILDER.append(SUB_SCRIPT.charAt(c - '('));
            } else {
                STRING_BUILDER.append(c);
            }
        }
        return STRING_BUILDER.toString();
    }

    public static String toSuperscript(String s) {
        STRING_BUILDER.setLength(0);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= '(' && c <= '=') {
                STRING_BUILDER.append(SUPER_SCRIPT.charAt(c - '('));
            } else {
                STRING_BUILDER.append(c);
            }
        }
        return STRING_BUILDER.toString();
    }

    public static double getValue(String in1) {
        String str = in1.toLowerCase();
        double val;
        try {
            if (str.contains("b")) {
                String[] split = str.split("b");
                val = TT_Utility.bitStringToInt(split[0].replaceAll("[^-]", "") + split[1].replaceAll("_", ""));
            } else if (str.contains("x")) {
                String[] split = str.split("x");
                val = TT_Utility.hexStringToInt(split[0].replaceAll("[^-]", "") + split[1].replaceAll("_", ""));
            } else {
                val = TT_Utility.stringToDouble(str);
            }
            return val;
        } catch (Exception e) {
            return 0;
        }
    }

    public static String intBitsToString(int number) {
        StringBuilder result = new StringBuilder(16);

        for (int i = 31; i >= 0; i--) {
            int mask = 1 << i;
            result.append((number & mask) != 0 ? "1" : "0");

            if (i % 8 == 0) {
                result.append(' ');
            }
        }
        result.replace(result.length() - 1, result.length(), "");

        return result.toString();
    }

    public static String intBitsToShortString(int number) {
        StringBuilder result = new StringBuilder(35);

        for (int i = 31; i >= 0; i--) {
            int mask = 1 << i;
            result.append((number & mask) != 0 ? ":" : ".");

            if (i % 8 == 0) {
                result.append('|');
            }
        }
        result.replace(result.length() - 1, result.length(), "");

        return result.toString();
    }

    public static String longBitsToShortString(long number) {
        StringBuilder result = new StringBuilder(71);

        for (int i = 63; i >= 0; i--) {
            long mask = 1L << i;
            result.append((number & mask) != 0 ? ":" : ".");

            if (i % 8 == 0) {
                result.append('|');
            }
        }
        result.replace(result.length() - 1, result.length(), "");

        return result.toString();
    }

    public static float map(float x, float in_min, float in_max, float out_min, float out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    public static boolean isInputEqual(boolean aDecreaseStacksizeBySuccess, boolean aDontCheckStackSizes,
            FluidStack[] requiredFluidInputs, ItemStack[] requiredInputs, FluidStack[] givenFluidInputs,
            ItemStack... givenInputs) {
        if (!GregTech_API.sPostloadFinished) {
            return false;
        }
        if (requiredFluidInputs.length > 0 && givenFluidInputs == null) {
            return false;
        }
        int amt;
        for (FluidStack tFluid : requiredFluidInputs) {
            if (tFluid != null) {
                boolean temp = true;
                amt = tFluid.amount;
                for (FluidStack aFluid : givenFluidInputs) {
                    if (aFluid != null && aFluid.isFluidEqual(tFluid)) {
                        if (aDontCheckStackSizes) {
                            temp = false;
                            break;
                        }
                        amt -= aFluid.amount;
                        if (amt < 1) {
                            temp = false;
                            break;
                        }
                    }
                }
                if (temp) {
                    return false;
                }
            }
        }

        if (requiredInputs.length > 0 && givenInputs == null) {
            return false;
        }
        for (ItemStack tStack : requiredInputs) {
            if (tStack != null) {
                amt = tStack.stackSize;
                boolean temp = true;
                for (ItemStack aStack : givenInputs) {
                    if (GT_Utility.areUnificationsEqual(aStack, tStack, true)
                            || GT_Utility.areUnificationsEqual(GT_OreDictUnificator.get(false, aStack), tStack, true)) {
                        if (aDontCheckStackSizes) {
                            temp = false;
                            break;
                        }
                        amt -= aStack.stackSize;
                        if (amt < 1) {
                            temp = false;
                            break;
                        }
                    }
                }
                if (temp) {
                    return false;
                }
            }
        }

        if (aDecreaseStacksizeBySuccess) {
            if (givenFluidInputs != null) {
                for (FluidStack tFluid : requiredFluidInputs) {
                    if (tFluid != null) {
                        amt = tFluid.amount;
                        for (FluidStack aFluid : givenFluidInputs) {
                            if (aFluid != null && aFluid.isFluidEqual(tFluid)) {
                                if (aDontCheckStackSizes) {
                                    aFluid.amount -= amt;
                                    break;
                                }
                                if (aFluid.amount < amt) {
                                    amt -= aFluid.amount;
                                    aFluid.amount = 0;
                                } else {
                                    aFluid.amount -= amt;
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            if (givenInputs != null) {
                for (ItemStack tStack : requiredInputs) {
                    if (tStack != null) {
                        amt = tStack.stackSize;
                        for (ItemStack aStack : givenInputs) {
                            if (GT_Utility.areUnificationsEqual(aStack, tStack, true) || GT_Utility
                                    .areUnificationsEqual(GT_OreDictUnificator.get(false, aStack), tStack, true)) {
                                if (aDontCheckStackSizes) {
                                    aStack.stackSize -= amt;
                                    break;
                                }
                                if (aStack.stackSize < amt) {
                                    amt -= aStack.stackSize;
                                    aStack.stackSize = 0;
                                } else {
                                    aStack.stackSize -= amt;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    public static String getUniqueIdentifier(ItemStack is) {
        return GameRegistry.findUniqueIdentifierFor(is.getItem()).modId + ':' + is.getUnlocalizedName();
    }

    public static byte getTier(long l) {
        byte b = -1;

        do {
            ++b;
            if (b >= CommonValues.V.length) {
                return b;
            }
        } while (l > CommonValues.V[b]);

        return b;
    }

    public static String[] splitButDifferent(String string, String delimiter) {
        String[] strings = new String[StringUtils.countMatches(string, delimiter) + 1];
        int lastEnd = 0;
        for (int i = 0; i < strings.length - 1; i++) {
            int nextEnd = string.indexOf(delimiter, lastEnd);
            strings[i] = string.substring(lastEnd, nextEnd);
            lastEnd = nextEnd + delimiter.length();
        }
        strings[strings.length - 1] = string.substring(lastEnd);
        return strings;
    }

    public static String[] unpackStrings(NBTTagCompound nbt) {
        String[] strings = new String[nbt.getInteger("i")];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = nbt.getString(Integer.toString(i));
        }
        return strings;
    }

    public static String getSomeString(NBTTagCompound nbt, int index) {
        return nbt.getString(Integer.toString(index % nbt.getInteger("i")));
    }

    public static NBTTagCompound packStrings(String... info) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("i", info.length);
        for (int i = 0; i < info.length; i++) {
            nbt.setString(Integer.toString(i), info[i]);
        }
        return nbt;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] unpackNBT(Class<T> tClass, Function<NBTTagCompound, T> converter, NBTTagCompound nbt) {
        T[] objects = (T[]) Array.newInstance(tClass, nbt.getInteger("i"));
        for (int i = 0; i < objects.length; i++) {
            objects[i] = converter.apply(nbt.getCompoundTag(Integer.toString(i)));
        }
        return objects;
    }

    @SafeVarargs
    public static <T> NBTTagCompound packNBT(Function<T, NBTTagCompound> converter, T... info) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("i", info.length);
        for (int i = 0; i < info.length; i++) {
            nbt.setTag(Integer.toString(i), converter.apply(info[i]));
        }
        return nbt;
    }

    public static boolean areBitsSet(int setBits, int testedValue) {
        return (testedValue & setBits) == setBits;
    }

    public static class ItemStack_NoNBT implements Comparable<ItemStack_NoNBT> {

        public final Item mItem;
        public final int mStackSize;
        public final int mMetaData;

        public ItemStack_NoNBT(Item aItem, long aStackSize, long aMetaData) {
            this.mItem = aItem;
            this.mStackSize = (byte) ((int) aStackSize);
            this.mMetaData = (short) ((int) aMetaData);
        }

        public ItemStack_NoNBT(ItemStack aStack) {
            if (aStack == null) {
                mItem = null;
                mStackSize = mMetaData = 0;
            } else {
                mItem = aStack.getItem();
                mStackSize = aStack.stackSize;
                mMetaData = Items.feather.getDamage(aStack);
            }
        }

        @Override
        public int compareTo(ItemStack_NoNBT o) {
            if (mMetaData > o.mMetaData) return 1;
            if (mMetaData < o.mMetaData) return -1;
            if (mStackSize > o.mStackSize) return 1;
            if (mStackSize < o.mStackSize) return -1;
            if (mItem != null && o.mItem != null)
                return mItem.getUnlocalizedName().compareTo(o.mItem.getUnlocalizedName());
            if (mItem == null && o.mItem == null) return 0;
            if (mItem != null) return 1;
            return -1;
        }

        @Override
        public boolean equals(Object aStack) {
            return aStack == this || (aStack instanceof ItemStack_NoNBT
                    && ((mItem == ((ItemStack_NoNBT) aStack).mItem) || ((ItemStack_NoNBT) aStack).mItem
                            .getUnlocalizedName().equals(this.mItem.getUnlocalizedName()))
                    && ((ItemStack_NoNBT) aStack).mStackSize == this.mStackSize
                    && ((ItemStack_NoNBT) aStack).mMetaData == this.mMetaData);
        }

        @Override
        public int hashCode() {
            return (mItem != null ? mItem.getUnlocalizedName().hashCode() : 0) ^ (mMetaData << 16) ^ (mStackSize << 24);
        }

        @Override
        public String toString() {
            return Integer.toString(hashCode()) + ' '
                    + (mItem == null ? "null" : mItem.getUnlocalizedName())
                    + ' '
                    + mMetaData
                    + ' '
                    + mStackSize;
        }
    }

    public static void setTier(int tier, Object me) {
        try {
            Field field = GT_MetaTileEntity_TieredMachineBlock.class.getField("mTier");
            field.setAccessible(true);
            field.set(me, (byte) tier);
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }

    public static StringBuilder receiveString(StringBuilder previousValue, int startIndex, int index, int value) {
        int sizeReq = index - startIndex;
        if (value == 0) {
            previousValue.setLength(Math.min(previousValue.length(), sizeReq));
        } else {
            previousValue.setLength(Math.max(previousValue.length(), sizeReq));
            previousValue.setCharAt(sizeReq, (char) value);
        }
        return previousValue;
    }

    @Deprecated
    public static double receiveDouble(double previousValue, int startIndex, int index, int value) {
        return Double.longBitsToDouble(receiveLong(Double.doubleToLongBits(previousValue), startIndex, index, value));
    }

    public static long receiveLong(long previousValue, int startIndex, int index, int value) {
        value &= 0xFFFF;
        switch (index - startIndex) {
            case 0:
                previousValue &= 0xFFFF_FFFF_FFFF_0000L;
                previousValue |= value;
                break;
            case 1:
                previousValue &= 0xFFFF_FFFF_0000_FFFFL;
                previousValue |= (long) value << 16;
                break;
            case 2:
                previousValue &= 0xFFFF_0000_FFFF_FFFFL;
                previousValue |= (long) value << 32;
                break;
            case 3:
                previousValue &= 0x0000_FFFF_FFFF_FFFFL;
                previousValue |= (long) value << 48;
                break;
        }
        return previousValue;
    }

    public static void sendString(StringBuilder string, Container container, ICrafting crafter, int startIndex) {
        for (int i = 0; i < string.length(); i++) {
            crafter.sendProgressBarUpdate(container, startIndex++, string.charAt(i));
        }
        crafter.sendProgressBarUpdate(container, startIndex, 0);
    }

    public static void sendDouble(double value, Container container, ICrafting crafter, int startIndex) {
        sendLong(Double.doubleToLongBits(value), container, crafter, startIndex);
    }

    public static void sendLong(long value, Container container, ICrafting crafter, int startIndex) {
        crafter.sendProgressBarUpdate(container, startIndex++, (int) (value & 0xFFFFL));
        crafter.sendProgressBarUpdate(container, startIndex++, (int) ((value & 0xFFFF0000L) >>> 16));
        crafter.sendProgressBarUpdate(container, startIndex++, (int) ((value & 0xFFFF00000000L) >>> 32));
        crafter.sendProgressBarUpdate(container, startIndex, (int) ((value & 0xFFFF000000000000L) >>> 48));
    }

    @Deprecated
    public static float receiveFloat(float previousValue, int startIndex, int index, int value) {
        return Float.intBitsToFloat(receiveInteger(Float.floatToIntBits(previousValue), startIndex, index, value));
    }

    public static int receiveInteger(int previousValue, int startIndex, int index, int value) {
        value &= 0xFFFF;
        switch (index - startIndex) {
            case 0:
                previousValue &= 0xFFFF_0000;
                previousValue |= value;
                break;
            case 1:
                previousValue &= 0x0000_FFFF;
                previousValue |= value << 16;
                break;
        }
        return previousValue;
    }

    public static void sendFloat(float value, Container container, ICrafting crafter, int startIndex) {
        sendInteger(Float.floatToIntBits(value), container, crafter, startIndex);
    }

    public static void sendInteger(int value, Container container, ICrafting crafter, int startIndex) {
        crafter.sendProgressBarUpdate(container, startIndex++, (int) (value & 0xFFFFL));
        crafter.sendProgressBarUpdate(container, startIndex, (value & 0xFFFF0000) >>> 16);
    }

    public static String doubleToString(double value) {
        if (value == (long) value) {
            return Long.toString((long) value);
        }
        return Double.toString(value);
    }

    public static boolean checkChunkExist(World world, ChunkCoordIntPair chunk) {
        int x = chunk.getCenterXPos();
        int z = chunk.getCenterZPosition();
        return world.checkChunksExist(x, 0, z, x, 0, z);
    }

    public static NBTTagCompound getPlayerData(UUID uuid1, UUID uuid2, String extension) {
        try {
            if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
                if (uuid1 != null && uuid2 != null) {
                    IPlayerFileData playerNBTManagerObj = MinecraftServer.getServer().worldServerForDimension(0)
                            .getSaveHandler().getSaveHandler();
                    SaveHandler sh = (SaveHandler) playerNBTManagerObj;
                    File dir = ObfuscationReflectionHelper.getPrivateValue(
                            SaveHandler.class,
                            sh,
                            new String[] { "playersDirectory", "field_75771_c" });
                    String id1 = uuid1.toString();
                    NBTTagCompound tagCompound = read(new File(dir, id1 + "." + extension));
                    if (tagCompound != null) {
                        return tagCompound;
                    }
                    tagCompound = readBackup(new File(dir, id1 + "." + extension + "_bak"));
                    if (tagCompound != null) {
                        return tagCompound;
                    }
                    String id2 = uuid2.toString();
                    tagCompound = read(new File(dir, id2 + "." + extension));
                    if (tagCompound != null) {
                        return tagCompound;
                    }
                    tagCompound = readBackup(new File(dir, id2 + "." + extension + "_bak"));
                    if (tagCompound != null) {
                        return tagCompound;
                    }
                }
            }
        } catch (Exception ignored) {}
        return new NBTTagCompound();
    }

    public static void savePlayerFile(EntityPlayer player, String extension, NBTTagCompound data) {
        try {
            if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
                if (player != null) {
                    IPlayerFileData playerNBTManagerObj = MinecraftServer.getServer().worldServerForDimension(0)
                            .getSaveHandler().getSaveHandler();
                    SaveHandler sh = (SaveHandler) playerNBTManagerObj;
                    File dir = ObfuscationReflectionHelper.getPrivateValue(
                            SaveHandler.class,
                            sh,
                            new String[] { "playersDirectory", "field_75771_c" });
                    String id1 = player.getUniqueID().toString();
                    write(new File(dir, id1 + "." + extension), data);
                    write(new File(dir, id1 + "." + extension + "_bak"), data);
                    String id2 = UUID.nameUUIDFromBytes(player.getCommandSenderName().getBytes(StandardCharsets.UTF_8))
                            .toString();
                    write(new File(dir, id2 + "." + extension), data);
                    write(new File(dir, id2 + "." + extension + "_bak"), data);
                }
            }
        } catch (Exception ignored) {}
    }

    private static NBTTagCompound read(File file) {
        if (file != null && file.exists()) {
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                return CompressedStreamTools.readCompressed(fileInputStream);
            } catch (Exception var9) {
                TecTech.LOGGER.error("Cannot read NBT File: " + file.getAbsolutePath());
            }
        }
        return null;
    }

    private static NBTTagCompound readBackup(File file) {
        if (file != null && file.exists()) {
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                return CompressedStreamTools.readCompressed(fileInputStream);
            } catch (Exception var9) {
                TecTech.LOGGER.error("Cannot read NBT File: " + file.getAbsolutePath());
                return new NBTTagCompound();
            }
        }
        return null;
    }

    private static void write(File file, NBTTagCompound tagCompound) {
        if (file != null) {
            if (tagCompound == null) {
                if (file.exists()) file.delete();
            } else {
                try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                    CompressedStreamTools.writeCompressed(tagCompound, fileOutputStream);
                } catch (Exception var9) {
                    TecTech.LOGGER.error("Cannot write NBT File: " + file.getAbsolutePath());
                }
            }
        }
    }

    public static AxisAlignedBB fromChunkCoordIntPair(ChunkCoordIntPair chunkCoordIntPair) {
        int x = chunkCoordIntPair.chunkXPos << 4;
        int z = chunkCoordIntPair.chunkZPos << 4;
        return AxisAlignedBB.getBoundingBox(x, -128, z, x + 16, 512, z + 16);
    }

    public static AxisAlignedBB fromChunk(Chunk chunk) {
        int x = chunk.xPosition << 4;
        int z = chunk.zPosition << 4;
        return AxisAlignedBB.getBoundingBox(x, -128, z, x + 16, 512, z + 16);
    }

    public static String getConcated(String[] strings, String separator) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : strings) {
            stringBuilder.append(string).append(separator);
        }
        int length = stringBuilder.length();
        if (length >= separator.length()) {
            stringBuilder.setLength(length - separator.length());
        }
        return stringBuilder.toString();
    }

    public static double getMagnitude3D(double[] in) {
        return Math.sqrt(in[0] * in[0] + in[1] * in[1] + in[2] * in[2]);
    }

    public static void normalize3D(double[] in, double[] out) {
        double mag = getMagnitude3D(in);
        out[0] = in[0] / mag;
        out[1] = in[1] / mag;
        out[2] = in[2] / mag;
    }

    public static void crossProduct3D(double[] inA, double[] inB, double[] out) {
        out[0] = inA[1] * inB[2] - inA[2] * inB[1];
        out[1] = inA[2] * inB[0] - inA[0] * inB[2];
        out[2] = inA[0] * inB[1] - inA[1] * inB[0];
    }
}
