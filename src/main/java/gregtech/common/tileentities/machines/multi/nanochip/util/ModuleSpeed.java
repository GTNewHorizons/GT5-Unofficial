package gregtech.common.tileentities.machines.multi.nanochip.util;

public class ModuleSpeed {

    public final int baseParallel;

    public ModuleSpeed(int parallel) {
        this.baseParallel = parallel;
    }

    public static final ModuleSpeed Fast = new ModuleSpeed(4);
    public static final ModuleSpeed Medium = new ModuleSpeed(2);
    public static final ModuleSpeed Slow = new ModuleSpeed(1);
}
