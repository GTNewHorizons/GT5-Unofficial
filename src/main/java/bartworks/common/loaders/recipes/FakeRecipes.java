package bartworks.common.loaders.recipes;

import bartworks.common.tileentities.multis.MTEHighTempGasCooledReactor;

public class FakeRecipes implements Runnable {

    @Override
    public void run() {
        MTEHighTempGasCooledReactor.HTGRMaterials.register_fake_THR_Recipes();
    }
}
