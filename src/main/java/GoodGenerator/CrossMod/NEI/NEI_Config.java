package GoodGenerator.CrossMod.NEI;

import GoodGenerator.Main.GoodGenerator;
import GoodGenerator.util.MyRecipeAdder;
import codechicken.nei.api.IConfigureNEI;

public class NEI_Config implements IConfigureNEI {
    public static boolean isAdded = true;

    @Override
    public void loadConfig() {
        NEI_Config.isAdded = false;
        new NeutronActivatorHandler(MyRecipeAdder.instance.NA);
        NEI_Config.isAdded = true;
    }

    @Override
    public String getName() {
        return "Good Generator NEI Plugin";
    }

    @Override
    public String getVersion() {
        return GoodGenerator.VERSION;
    }
}
