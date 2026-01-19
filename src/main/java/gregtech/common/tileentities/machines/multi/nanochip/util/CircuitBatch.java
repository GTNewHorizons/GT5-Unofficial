package gregtech.common.tileentities.machines.multi.nanochip.util;

import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.BATCH_SIZE;

public class CircuitBatch {

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
        return (primitives + crystals + wetwares + bios + exotics + opticals + cosmics + temporals + specials)
            >= BATCH_SIZE;
    }

    public void add(byte type, int amount) {
        switch (type) {
            case 1 -> primitives += amount;
            case 2 -> crystals += amount;
            case 3 -> wetwares += amount;
            case 4 -> bios += amount;
            case 5 -> opticals += amount;
            case 6 -> exotics += amount;
            case 7 -> cosmics += amount;
            case 8 -> temporals += amount;
            case 64 -> specials += amount;
        }
    }

}
