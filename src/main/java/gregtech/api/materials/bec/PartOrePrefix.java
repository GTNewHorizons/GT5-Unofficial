package gregtech.api.materials.bec;

import java.util.EnumMap;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import gregtech.api.enums.OrePrefixes;

/** A subset of the normal ore prefixes for use in EnumMaps. */
public enum PartOrePrefix {

    plate(OrePrefixes.plate),
    foil(OrePrefixes.foil),
    stickLong(OrePrefixes.stickLong),
    stick(OrePrefixes.stick),
    bolt(OrePrefixes.bolt),
    ring(OrePrefixes.ring),
    wireFine(OrePrefixes.wireFine),
    lens(OrePrefixes.lens),
    //
    ;

    public static final List<PartOrePrefix> VALUES = ImmutableList.copyOf(values());

    public final OrePrefixes prefix;

    PartOrePrefix(OrePrefixes prefix) {
        this.prefix = prefix;
    }

    public static @Nullable PartOrePrefix fromPrefix(OrePrefixes prefix) {
        return switch (prefix.getName()) {
            case "plate" -> PartOrePrefix.plate;
            case "foil" -> PartOrePrefix.foil;
            case "stickLong" -> PartOrePrefix.stickLong;
            case "stick" -> PartOrePrefix.stick;
            case "bolt" -> PartOrePrefix.bolt;
            case "ring" -> PartOrePrefix.ring;
            case "wireFine" -> PartOrePrefix.wireFine;
            case "lens" -> PartOrePrefix.lens;
            default -> null;
        };
    }

    public static class PrefixMap<T> extends EnumMap<PartOrePrefix, T> {

        public PrefixMap() {
            super(PartOrePrefix.class);
        }

        public T get(OrePrefixes prefix) {
            return super.get(PartOrePrefix.fromPrefix(prefix));
        }

        public T getOrDefault(OrePrefixes prefix, T defaultValue) {
            return super.getOrDefault(PartOrePrefix.fromPrefix(prefix), defaultValue);
        }
    }
}
