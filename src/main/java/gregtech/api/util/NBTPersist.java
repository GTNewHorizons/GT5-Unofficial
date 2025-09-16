package gregtech.api.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class NBTPersist {

    /**
     * Converts an nbt tag to json.
     * Does not preserve the specific types of the tags, but the returned data will be sane and generally correct.
     * Compatible with Gson.
     */
    @SuppressWarnings("unchecked")
    public static JsonElement toJsonObject(NBTBase nbt) {
        if (nbt == null) return null;

        if (nbt instanceof NBTTagCompound compound) {
            // NBTTagCompound
            final Map<String, NBTBase> tagMap = (Map<String, NBTBase>) compound.tagMap;

            JsonObject root = new JsonObject();

            for (Map.Entry<String, NBTBase> nbtEntry : tagMap.entrySet()) {
                root.add(nbtEntry.getKey(), toJsonObject(nbtEntry.getValue()));
            }

            return root;
        } else if (nbt instanceof NBTTagByte) {
            // Number (byte)
            return new JsonPrimitive(((NBTTagByte) nbt).func_150290_f());
        } else if (nbt instanceof NBTTagShort) {
            // Number (short)
            return new JsonPrimitive(((NBTTagShort) nbt).func_150289_e());
        } else if (nbt instanceof NBTTagInt) {
            // Number (int)
            return new JsonPrimitive(((NBTTagInt) nbt).func_150287_d());
        } else if (nbt instanceof NBTTagLong) {
            // Number (long)
            return new JsonPrimitive(((NBTTagLong) nbt).func_150291_c());
        } else if (nbt instanceof NBTTagFloat) {
            // Number (float)
            return new JsonPrimitive(((NBTTagFloat) nbt).func_150288_h());
        } else if (nbt instanceof NBTTagDouble) {
            // Number (double)
            return new JsonPrimitive(((NBTTagDouble) nbt).func_150286_g());
        } else if (nbt instanceof NBTBase.NBTPrimitive) {
            // Number
            return new JsonPrimitive(((NBTBase.NBTPrimitive) nbt).func_150286_g());
        } else if (nbt instanceof NBTTagString) {
            // String
            return new JsonPrimitive(((NBTTagString) nbt).func_150285_a_());
        } else if (nbt instanceof NBTTagList list) {
            // Tag List

            JsonArray arr = new JsonArray();
            list.tagList.forEach(c -> arr.add(toJsonObject((NBTBase) c)));
            return arr;
        } else if (nbt instanceof NBTTagIntArray list) {
            // Int Array

            JsonArray arr = new JsonArray();

            for (int i : list.func_150302_c()) {
                arr.add(new JsonPrimitive(i));
            }

            return arr;
        } else if (nbt instanceof NBTTagByteArray list) {
            // Byte Array

            JsonArray arr = new JsonArray();

            for (byte i : list.func_150292_c()) {
                arr.add(new JsonPrimitive(i));
            }

            return arr;
        } else {
            throw new IllegalArgumentException("Unsupported NBT Tag: " + NBTBase.NBTTypes[nbt.getId()] + " - " + nbt);
        }
    }

    /**
     * The opposite of {@link #toJsonObject(NBTBase)}
     */
    public static NBTBase toNbt(JsonElement jsonElement) {
        if (jsonElement == null || jsonElement == JsonNull.INSTANCE) {
            return null;
        }

        if (jsonElement instanceof JsonPrimitive jsonPrimitive) {

            if (jsonPrimitive.isNumber()) {
                if (jsonPrimitive.getAsBigDecimal()
                    .remainder(BigDecimal.ONE)
                    .equals(BigDecimal.ZERO)) {
                    long lval = jsonPrimitive.getAsLong();

                    if (lval >= Byte.MIN_VALUE && lval <= Byte.MAX_VALUE) {
                        return new NBTTagByte((byte) lval);
                    }

                    if (lval >= Short.MIN_VALUE && lval <= Short.MAX_VALUE) {
                        return new NBTTagShort((short) lval);
                    }

                    if (lval >= Integer.MIN_VALUE && lval <= Integer.MAX_VALUE) {
                        return new NBTTagInt((int) lval);
                    }

                    return new NBTTagLong(lval);
                } else {
                    double dval = jsonPrimitive.getAsDouble();
                    float fval = (float) dval;

                    if (Math.abs(dval - fval) < 0.0001) {
                        return new NBTTagFloat(fval);
                    }

                    return new NBTTagDouble(dval);
                }
            } else {
                return new NBTTagString(jsonPrimitive.getAsString());
            }
        } else if (jsonElement instanceof JsonArray jsonArray) {
            final List<NBTBase> nbtList = new ArrayList<>();

            int type = -1;

            for (JsonElement element : jsonArray) {
                if (element == null || element == JsonNull.INSTANCE) continue;

                NBTBase tag = toNbt(element);

                if (tag == null) continue;

                if (type == -1) type = tag.getId();
                if (type != tag.getId())
                    throw new IllegalArgumentException("NBT lists cannot contain tags of varying types");

                nbtList.add(tag);
            }

            // spotless:off
            if (type == Constants.NBT.TAG_INT) {
                return new NBTTagIntArray(nbtList.stream().mapToInt(i -> ((NBTTagInt) i).func_150287_d()).toArray());
            } else if (type == Constants.NBT.TAG_BYTE) {
                final byte[] abyte = new byte[nbtList.size()];

                for (int i = 0; i < nbtList.size(); i++) {
                    abyte[i] = ((NBTTagByte) nbtList.get(i)).func_150290_f();
                }

                return new NBTTagByteArray(abyte);
            } else {
                NBTTagList nbtTagList = new NBTTagList();
                nbtList.forEach(nbtTagList::appendTag);

                return nbtTagList;
            }
            // spotless:on
        } else if (jsonElement instanceof JsonObject jsonObject) {
            NBTTagCompound nbtTagCompound = new NBTTagCompound();

            for (Map.Entry<String, JsonElement> jsonEntry : jsonObject.entrySet()) {
                if (jsonEntry.getValue() == JsonNull.INSTANCE) continue;

                nbtTagCompound.setTag(jsonEntry.getKey(), toNbt(jsonEntry.getValue()));
            }

            return nbtTagCompound;
        }

        throw new IllegalArgumentException("Unhandled element " + jsonElement);
    }

    /**
     * Converts an nbt tag to json.
     * Preserves types exactly. Not compatible with gson loading.
     */
    @SuppressWarnings("unchecked")
    public static JsonElement toJsonObjectExact(NBTBase nbt) {
        if (nbt == null) {
            return null;
        }

        if (nbt instanceof NBTTagCompound compound) {
            final Map<String, NBTBase> tagMap = (Map<String, NBTBase>) compound.tagMap;

            JsonObject root = new JsonObject();

            for (Map.Entry<String, NBTBase> nbtEntry : tagMap.entrySet()) {
                root.add(nbtEntry.getKey(), toJsonObjectExact(nbtEntry.getValue()));
            }

            return root;
        } else if (nbt instanceof NBTTagByte b) {
            return new JsonPrimitive("b" + b.func_150290_f());
        } else if (nbt instanceof NBTTagShort half) {
            return new JsonPrimitive("h" + half.func_150289_e());
        } else if (nbt instanceof NBTTagInt i) {
            return new JsonPrimitive("i" + Integer.toUnsignedString(i.func_150287_d(), 16));
        } else if (nbt instanceof NBTTagLong l) {
            return new JsonPrimitive("l" + Long.toUnsignedString(l.func_150291_c(), 16));
        } else if (nbt instanceof NBTTagFloat f) {
            return new JsonPrimitive("f" + Long.toUnsignedString(Float.floatToIntBits(f.func_150288_h()), 16));
        } else if (nbt instanceof NBTTagDouble d) {
            return new JsonPrimitive("d" + Long.toUnsignedString(Double.doubleToLongBits(d.func_150286_g()), 16));
        } else if (nbt instanceof NBTBase.NBTPrimitive other) {
            return new JsonPrimitive("d" + Long.toUnsignedString(Double.doubleToLongBits(other.func_150286_g()), 16));
        } else if (nbt instanceof NBTTagString s) {
            return new JsonPrimitive("s" + s.func_150285_a_());
        } else if (nbt instanceof NBTTagList l) {
            JsonArray arr = new JsonArray();

            l.tagList.forEach(c -> arr.add(toJsonObjectExact((NBTBase) c)));

            return arr;
        } else if (nbt instanceof NBTTagIntArray a) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);

            for (int i : a.func_150302_c()) {
                try {
                    dos.writeInt(i);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            return new JsonPrimitive(
                "1" + Base64.getEncoder()
                    .encodeToString(baos.toByteArray()));
        } else if (nbt instanceof NBTTagByteArray a) {
            return new JsonPrimitive(
                "2" + Base64.getEncoder()
                    .encodeToString(a.func_150292_c()));
        } else {
            throw new IllegalArgumentException("Unsupported NBT Tag: " + NBTBase.NBTTypes[nbt.getId()] + " - " + nbt);
        }
    }

    /**
     * The opposite of {@link #toJsonObjectExact(NBTBase)}
     */
    public static NBTBase toNbtExact(JsonElement jsonElement) throws JsonParseException {
        if (jsonElement == null) return null;

        if (jsonElement instanceof JsonPrimitive primitive) {
            if (!primitive.isString())
                throw new JsonParseException("expected json primitive to be string: '" + primitive + "'");

            String data = primitive.getAsString();

            if (data.length() < 2) throw new JsonParseException("illegal json primitive string: '" + data + "'");

            char prefix = data.charAt(0);
            data = data.substring(1);

            try {
                switch (prefix) {
                    case 'b' -> {
                        return new NBTTagByte(Byte.parseByte(data));
                    }
                    case 'h' -> {
                        return new NBTTagShort(Short.parseShort(data));
                    }
                    case 'i' -> {
                        return new NBTTagInt(Integer.parseUnsignedInt(data, 16));
                    }
                    case 'l' -> {
                        return new NBTTagLong(Long.parseUnsignedLong(data, 16));
                    }
                    case 'f' -> {
                        return new NBTTagFloat(Float.intBitsToFloat((int) Long.parseUnsignedLong(data, 16)));
                    }
                    case 'd' -> {
                        return new NBTTagDouble(Double.longBitsToDouble(Long.parseUnsignedLong(data, 16)));
                    }
                    case 's' -> {
                        return new NBTTagString(data);
                    }
                    case '1' -> {
                        ByteArrayInputStream bais = new ByteArrayInputStream(
                            Base64.getDecoder()
                                .decode(data));
                        DataInputStream dis = new DataInputStream(bais);

                        int count = bais.available() / 4;

                        int[] array = new int[count];

                        for (int i = 0; i < count; i++) {
                            try {
                                array[i] = dis.readInt();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        return new NBTTagIntArray(array);
                    }
                    case '2' -> {
                        return new NBTTagByteArray(
                            Base64.getDecoder()
                                .decode(data));
                    }
                }
            } catch (NumberFormatException e) {
                throw new JsonParseException("illegal number: " + primitive, e);
            }
        } else if (jsonElement instanceof JsonArray array) {
            NBTTagList list = new NBTTagList();

            for (JsonElement e : array) {
                list.appendTag(toNbtExact(e));
            }

            return list;
        } else if (jsonElement instanceof JsonObject obj) {
            NBTTagCompound tag = new NBTTagCompound();

            for (Map.Entry<String, JsonElement> jsonEntry : obj.entrySet()) {
                tag.setTag(jsonEntry.getKey(), toNbtExact(jsonEntry.getValue()));
            }

            return tag;
        }

        throw new IllegalArgumentException("Unhandled element " + jsonElement);
    }

    public static class ExactNBTTypeAdapter implements JsonSerializer<NBTBase>, JsonDeserializer<NBTBase> {

        @Override
        public NBTBase deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
            return toNbtExact(json);
        }

        @Override
        public JsonElement serialize(NBTBase src, Type typeOfSrc, JsonSerializationContext context) {
            return toJsonObjectExact(src);
        }
    }
}
