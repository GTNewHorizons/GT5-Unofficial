package gregtech.common.tileentities.machines.multi.nanochip.util;

public class ModuleRecipeInfo {

    public final int baseParallel;

    public ModuleRecipeInfo(int parallel) {
        this.baseParallel = parallel;
    }

    public static final ModuleRecipeInfo Fast = new ModuleRecipeInfo(4);
    public static final ModuleRecipeInfo Medium = new ModuleRecipeInfo(2);
    public static final ModuleRecipeInfo Slow = new ModuleRecipeInfo(1);
}
