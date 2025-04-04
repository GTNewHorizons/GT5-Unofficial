package gregtech.mixin.mixins.late.coremod;

import java.util.HashSet;
import java.util.function.Function;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.dreammaster.recipes.RecipeRemover;

import gregtech.api.util.GTShapedRecipe;
import gregtech.api.util.GTShapelessRecipe;
import gregtech.api.util.GTUtility;
import gregtech.loaders.postload.recipes.FullPackSteamRecipes;

@Mixin(RecipeRemover.class)
public class MixinRecipeRemover {

    @Shadow(remap = false)
    private static void addToBuffer(HashSet<GTUtility.ItemId> outputs, Function<IRecipe, Boolean> whenToRemove) {}

    @Shadow(remap = false)
    private static HashSet<GTUtility.ItemId> getItemsHashed(Object item, boolean includeWildcardVariants) {
        return null;
    }

    /**
     * @author serenibyss
     * @reason because coremod wants to remove my custom recipes
     */
    @Overwrite(remap = false)
    private static void removeRecipeByOutputDelayed(Object aOutput) {
        addToBuffer(getItemsHashed(aOutput, false), r -> {
            if (r instanceof GTShapedRecipe gtShaped) {
                return gtShaped.isRemovableByCoremod();
            } else if (r instanceof GTShapelessRecipe gtShapeless) {
                return gtShapeless.isRemovableByCoremod();
            } else return !(r instanceof FullPackSteamRecipes.UpgradeBackpackRecipe);
        });
    }

    /**
     * @author serenibyss
     * @reason because coremod wants to remove my custom recipes
     */
    @Overwrite(remap = false)
    private static void removeRecipeShapedDelayed(Object aOutput) {
        addToBuffer(getItemsHashed(aOutput, false), r -> {
            if (r instanceof GTShapedRecipe gtShaped) {
                return gtShaped.isRemovableByCoremod();
            } else return r instanceof ShapedOreRecipe || r instanceof ShapedRecipes;
        });
    }
}
