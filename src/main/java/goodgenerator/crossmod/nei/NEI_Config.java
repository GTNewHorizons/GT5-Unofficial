package goodgenerator.crossmod.nei;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import goodgenerator.main.GoodGenerator;

public class NEI_Config implements IConfigureNEI {

    public static boolean isAdded = true;

    @Override
    public void loadConfig() {
        NEI_Config.isAdded = false;
        NEI_Config.isAdded = true;
    }

    public static void hide(Block aBlock) {
        API.hideItem(new ItemStack(aBlock, 1));
    }

    public static void hide(Item aItem) {
        API.hideItem(new ItemStack(aItem, 1));
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
