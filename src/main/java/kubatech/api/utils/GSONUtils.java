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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;
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

    private static final JsonSerializer<NBTTagCompound> NBTTagCompoundSerializer = (src, typeOfSrc,
        context) -> new JsonPrimitive(src.toString());

    private static final JsonDeserializer<NBTTagCompound> NBTTagCompoundDeserializer = (json, typeOfT, context) -> {
        try {
            if (!(json instanceof JsonPrimitive)) return null;
            if (!((JsonPrimitive) json).isString()) return null;
            return (NBTTagCompound) JsonToNBT.func_150315_a(json.getAsString());
        } catch (NBTException e) {
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
