package com.github.technus.tectech.mechanics.alignment.enumerable;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import static java.lang.Math.abs;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

public enum Rotation {
    NORMAL(2, "normal"),
    CLOCKWISE(3, "clockwise"),
    UPSIDE_DOWN(0, "upside down"),
    COUNTER_CLOCKWISE(1, "counter clockwise");

    private final int opposite;
    private final String name;

    public static final Rotation[] VALUES = values();
    private static final Map<String, Rotation> NAME_LOOKUP = stream(VALUES).collect(toMap(Rotation::getName2, (rotation) -> rotation));

    Rotation(int oppositeIn, String nameIn) {
        this.opposite = oppositeIn;
        this.name = nameIn;
    }

    public int getIndex(){
        return ordinal();
    }

    public Rotation getOpposite() {
        return VALUES[opposite];
    }

    public String getName2() {
        return this.name;
    }

    public static Rotation byName(String name) {
        return name == null ? null : NAME_LOOKUP.get(name.toLowerCase(Locale.ROOT));
    }

    public static Rotation byIndex(int index) {
        return VALUES[abs(index % VALUES.length)];
    }

    public static Rotation random(@Nonnull Random rand) {
        return VALUES[rand.nextInt(VALUES.length)];
    }

    public String toString() {
        return this.name;
    }

    public String getName() {
        return this.name;
    }

    public boolean isNotRotated(){
        return this==NORMAL;
    }

    public boolean isClockwise(){
        return this==CLOCKWISE;
    }

    public boolean isCounterClockwise(){
        return this==COUNTER_CLOCKWISE;
    }

    public boolean isUpsideDown(){
        return this==UPSIDE_DOWN;
    }
}
