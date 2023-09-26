package gregtech.loaders.postload;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_ModHandler;

public class GT_FakeRecipeLoader implements Runnable {

    @Override
    public void run() {
        GT_Values.RA.addIC2ReactorBreederCell(
            GT_ModHandler.getIC2Item("reactorLithiumCell", 1),
            GT_ModHandler.getIC2Item("TritiumCell", 1),
            true,
            3000,
            1,
            10000);
    }
}
