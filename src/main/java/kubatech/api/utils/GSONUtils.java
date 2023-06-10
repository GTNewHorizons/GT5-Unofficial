/*
 * spotless:off
 * KubaTech - Gregtech Addon
 * Copyright (C) 2022 - 2023  kuba6000
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <https://www.gnu.org/licenses/>.
 * spotless:on
 */

package kubatech.api.utils;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

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

    private static final JsonSerializer<NBTTagCompound> NBTTagCompoundSerializer = (src, typeOfSrc, context) -> {
        try {
            JsonArray array = new JsonArray();
            for (byte b : CompressedStreamTools.compress(src)) {
                array.add(new JsonPrimitive(b));
            }
            return array;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };

    private static final JsonDeserializer<NBTTagCompound> NBTTagCompoundDeserializer = (json, typeOfT, context) -> {
        try {
            if (!(json instanceof JsonArray)) return null;
            byte[] bytes = new byte[((JsonArray) json).size()];
            for (int i = 0; i < bytes.length; i++) bytes[i] = ((JsonArray) json).get(i)
                .getAsByte();
            return CompressedStreamTools.func_152457_a(bytes, new NBTSizeTracker(2097152L));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };

    public static final GsonBuilder GSON_BUILDER = new GsonBuilder().addSerializationExclusionStrategy(GSONStrategy)
        .addDeserializationExclusionStrategy(GSONStrategy)
        .registerTypeAdapter(NBTTagCompound.class, NBTTagCompoundDeserializer)
        .registerTypeAdapter(NBTTagCompound.class, NBTTagCompoundSerializer)
        .serializeNulls();
    public static final GsonBuilder GSON_BUILDER_PRETTY = new GsonBuilder()
        .addSerializationExclusionStrategy(GSONStrategy)
        .addDeserializationExclusionStrategy(GSONStrategy)
        .registerTypeAdapter(NBTTagCompound.class, NBTTagCompoundDeserializer)
        .registerTypeAdapter(NBTTagCompound.class, NBTTagCompoundSerializer)
        .serializeNulls()
        .setPrettyPrinting();
}
