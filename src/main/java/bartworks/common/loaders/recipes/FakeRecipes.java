package bartworks.common.loaders.recipes;

import kubatech.tileentity.gregtech.multiblock.MTEHighTempGasCooledReactor;

public class FakeRecipes implements Runnable {

    @Override
    public void run() {
        MTEHighTempGasCooledReactor.HTGRMaterials.register_fake_THR_Recipes();
    }
}
