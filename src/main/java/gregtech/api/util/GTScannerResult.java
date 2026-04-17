package gregtech.api.util;

import net.minecraft.item.ItemStack;

import gregtech.api.interfaces.IGTScannerHandler;

public class GTScannerResult {

    public final static GTScannerResult NOT_FOUND = null;
    public final static GTScannerResult NOT_MET = new GTScannerResult(0, 0, 0, 0, 0, null);

    public int eut;
    public int duration;
    public int inputConsume;
    public int specialConsume;
    public int fluidConsume;
    public ItemStack output;
    public IGTScannerHandler handler;

    public GTScannerResult(int eut, int duration, int inputConsume, int specialConsume, int fluidConsume,
        ItemStack output) {
        this.eut = eut;
        this.duration = duration;
        this.inputConsume = inputConsume;
        this.specialConsume = specialConsume;
        this.fluidConsume = fluidConsume;
        this.output = output;
        this.handler = null;
    }

    public boolean isNotMet() {
        return this.eut <= 0 || this.duration <= 0;
    }

    public static class ALScannerResult extends GTScannerResult {

        public GTRecipe.RecipeAssemblyLine alRecipe;

        public ALScannerResult(int eut, int duration, int inputConsume, int specialConsume, int fluidConsume,
            ItemStack output, GTRecipe.RecipeAssemblyLine alRecipe) {
            super(eut, duration, inputConsume, specialConsume, fluidConsume, output);
            this.alRecipe = alRecipe;
        }
    }
}
