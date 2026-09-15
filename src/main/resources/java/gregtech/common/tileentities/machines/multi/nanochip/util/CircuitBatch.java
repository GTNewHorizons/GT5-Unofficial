package gregtech.common.tileentities.machines.multi.nanochip.util;

import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.BATCH_SIZE;

public class CircuitBatch {

    public int total;
    public int primitives;
    public int crystals;
    public int wetwares;
    public int bios;
    public int opticals;
    public int exotics;
    public int cosmics;
    public int temporals;
    public int specials;

    public boolean isFull() {
        return total >= BATCH_SIZE;
    }

    // Returns leftover if total exceeds BATCH_SIZE
    public int add(CircuitCalibration circuitType, int amount) {
        int leftover = 0;
        if (total + amount > BATCH_SIZE) {
            leftover = total + amount - BATCH_SIZE;
            amount = BATCH_SIZE - total;
        }
        total += amount;
        switch (circuitType) {
            case PRIMITIVE -> primitives += amount;
            case CRYSTAL -> crystals += amount;
            case WETWARE -> wetwares += amount;
            case BIO -> bios += amount;
            case OPTICAL -> opticals += amount;
            case EXOTIC -> exotics += amount;
            case COSMIC -> cosmics += amount;
            case TEMPORAL -> temporals += amount;
            case SPECIAL -> specials += amount;
        }
        return leftover;
    }

    public int[] writeToIntArray() {
        return new int[] { primitives, crystals, wetwares, bios, opticals, exotics, cosmics, temporals, specials };
    }

    public CircuitBatch() {}

    public CircuitBatch(int[] history) {
        if (history.length == 0) return;
        primitives = history[0];
        crystals = history[1];
        wetwares = history[2];
        bios = history[3];
        opticals = history[4];
        exotics = history[5];
        cosmics = history[6];
        temporals = history[7];
        specials = history[8];

    }
}
