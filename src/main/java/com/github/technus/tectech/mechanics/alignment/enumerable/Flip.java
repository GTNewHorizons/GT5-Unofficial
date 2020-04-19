package com.github.technus.tectech.mechanics.alignment.enumerable;

import javax.annotation.Nonnull;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import static java.lang.Math.abs;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

public enum Flip {
    NONE(3, "none"),
    HORIZONTAL(2, "horizontal"),
    VERTICAL(1, "vertical"),
    BOTH(0, "both");

    private final int opposite;
    private final String name;

    public static final Flip[] VALUES = values();
    private static final Map<String, Flip> NAME_LOOKUP = stream(VALUES).collect(toMap(Flip::getName2, (flip) -> flip));

    Flip(int oppositeIn, String nameIn) {
        this.opposite = oppositeIn;
        this.name = nameIn;
    }

    public int getIndex(){
        return ordinal();
    }

    public Flip getOpposite() {
        return VALUES[opposite];
    }

    public String getName2() {
        return this.name;
    }

    public static Flip byName(String name) {
        return name == null ? null : NAME_LOOKUP.get(name.toLowerCase(Locale.ROOT));
    }

    public static Flip byIndex(int index) {
        return VALUES[abs(index % VALUES.length)];
    }

    public static Flip random(@Nonnull Random rand) {
        return VALUES[rand.nextInt(VALUES.length)];
    }

    public String toString() {
        return this.name;
    }

    public String getName() {
        return this.name;
    }

    public boolean isNotFlipped(){
        return this==NONE;
    }

    public boolean isBothFlipped(){
        return this==BOTH;
    }

    public boolean isHorizontallyFlipped() {
        return this==HORIZONTAL || isBothFlipped();
    }

    public boolean isVerticallyFliped() {
        return this==VERTICAL || isBothFlipped();
    }
}
