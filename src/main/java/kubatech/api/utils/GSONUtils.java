package kubatech.api.utils;

import com.google.gson.*;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;

public class GSONUtils {
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface SkipGSON {}

    private static final ExclusionStrategy GSONStrategy = new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getAnnotation(SkipGSON.class) != null;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    };

    private static final JsonSerializer<NBTTagCompound> NBTTagCompoundSerializer =
            new JsonSerializer<NBTTagCompound>() {

                @Override
                public JsonElement serialize(NBTTagCompound src, Type typeOfSrc, JsonSerializationContext context) {
                    try {
                        JsonArray array = new JsonArray();
                        for (byte b : CompressedStreamTools.compress(src)) {
                            array.add(new JsonPrimitive(b));
                        }
                        return array;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            };

    private static final JsonDeserializer<NBTTagCompound> NBTTagCompoundDeserializer =
            new JsonDeserializer<NBTTagCompound>() {
                @Override
                public NBTTagCompound deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                        throws JsonParseException {
                    try {
                        if (!(json instanceof JsonArray)) return null;
                        byte[] bytes = new byte[((JsonArray) json).size()];
                        for (int i = 0; i < bytes.length; i++)
                            bytes[i] = ((JsonArray) json).get(i).getAsByte();
                        return CompressedStreamTools.func_152457_a(bytes, new NBTSizeTracker(2097152L));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            };

    public static final GsonBuilder GSON_BUILDER = new GsonBuilder()
            .addSerializationExclusionStrategy(GSONStrategy)
            .addDeserializationExclusionStrategy(GSONStrategy)
            .registerTypeAdapter(NBTTagCompound.class, NBTTagCompoundDeserializer)
            .registerTypeAdapter(NBTTagCompound.class, NBTTagCompoundSerializer);
}
