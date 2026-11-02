package gregtech.api.recipe.maps;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;

import codechicken.nei.PositionedStack;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;
import gregtech.nei.GTNEIDefaultHandler;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class NACMatrixFrontend extends AssemblyLineFrontend {

    public NACMatrixFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    public ModularWindow.Builder createNEITemplate(GTNEIDefaultHandler.NEITemplateContext ctx) {
        return super.createNEITemplate(ctx).widget(new DrawableWidget().setDrawable(() -> {
            GTNEIDefaultHandler.CachedDefaultRecipe recipe = ctx.recipeSupplier.get();
            return recipe != null && recipe.mOutputs.size() > 1 ? ModularUITextures.ITEM_SLOT : IDrawable.EMPTY;
        })
            .setSize(18, 18)
            .setPos(new Pos2d(142, 26).add(ctx.windowOffset)));
    }

    @Override
    public void prepareRecipe(GTNEIDefaultHandler.CachedDefaultRecipe recipe) {
        super.prepareRecipe(recipe);

        for (int i = 0, outputCount = recipe.mOutputs.size(); i < outputCount; i++) {
            PositionedStack neiStack = recipe.mOutputs.get(i);
            ItemStack stack = neiStack.item;
            CircuitComponent cc = CircuitComponent.tryGetFromFakeStack(stack);
            if (cc == null || cc.xorResult == null) continue;
            if (neiStack instanceof GTNEIDefaultHandler.FixedPositionedStack fixed) {
                fixed.setChance(cc.xorSuccessChance);
                fixed.setCustomBadge(
                    String.format("%d%%", Math.floorDiv(cc.xorSuccessChance, 100)),
                    StatCollector.translateToLocalFormatted(
                        "GT5U.recipes.chance.unorganized",
                        Math.floorDiv(10000 - cc.xorSuccessChance, 100)));
                recipe.mOutputs.add(
                    new GTNEIDefaultHandler.FixedPositionedStack(
                        recipe,
                        cc.xorResult.getFakeStack(fixed.realStackSize),
                        neiProperties.renderRealStackSizes,
                        neiStack.relx,
                        neiStack.rely + 18,
                        10000 - cc.xorSuccessChance,
                        false,
                        false));
            }
        }
    }
}
