package com.github.bartimaeusnek.bartworks.common.loaders;

public class LoaderRegistry implements Runnable {
    public ItemRegistry items = new ItemRegistry();
    public RecipeLoader recipes = new RecipeLoader();

    @Override
    public void run() {
        items.run();
        recipes.run();
    }


}
