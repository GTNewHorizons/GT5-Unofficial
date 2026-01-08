package gregtech.api.util;

import net.minecraft.item.ItemStack;

import com.github.bsideup.jabel.Desugar;

@Desugar
public record GTScannerResult(int eut, int duration, int inputConsume, int specialConsume, int fluidConsume,
    ItemStack output, boolean consumeInputsIfOCFail) {

    public final static GTScannerResult NOT_FOUND = null;
    public final static GTScannerResult NOT_MET = new GTScannerResult(0, 0, 0, 0, 0, null, false);

}
