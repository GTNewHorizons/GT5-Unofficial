package bartworks.common.loaders.recipes;

import bartworks.common.tileentities.multis.GT_TileEntity_HTGR;

public class FakeRecipes implements Runnable {

    @Override
    public void run() {
        GT_TileEntity_HTGR.HTGRMaterials.register_fake_THR_Recipes();
    }
}
