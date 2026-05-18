package gregtech.api.materials.bec;

import java.util.EnumMap;

import javax.annotation.Nullable;

import gregtech.api.enums.OrePrefixes;

/// A subset of the normal ore prefixes, for use in [EnumMap]s.
public enum BECPartOrePrefix {

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

    public static final BECPartOrePrefix[] VALUES = values();

    public final OrePrefixes prefix;

    BECPartOrePrefix(OrePrefixes prefix) {
        this.prefix = prefix;
    }

    public static @Nullable BECPartOrePrefix fromPrefix(OrePrefixes prefix) {
        return switch (prefix.getName()) {
            case "plate" -> BECPartOrePrefix.plate;
            case "foil" -> BECPartOrePrefix.foil;
            case "stickLong" -> BECPartOrePrefix.stickLong;
            case "stick" -> BECPartOrePrefix.stick;
            case "bolt" -> BECPartOrePrefix.bolt;
            case "ring" -> BECPartOrePrefix.ring;
            case "wireFine" -> BECPartOrePrefix.wireFine;
            case "lens" -> BECPartOrePrefix.lens;
            default -> null;
        };
    }

    public static class BECPrefixMap<T> extends EnumMap<BECPartOrePrefix, T> {

        public BECPrefixMap() {
            super(BECPartOrePrefix.class);
        }

        public T get(OrePrefixes prefix) {
            return super.get(BECPartOrePrefix.fromPrefix(prefix));
        }

        public T getOrDefault(OrePrefixes prefix, T defaultValue) {
            return super.getOrDefault(BECPartOrePrefix.fromPrefix(prefix), defaultValue);
        }
    }
}
