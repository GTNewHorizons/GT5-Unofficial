package kubatech.api.gui;

import static gregtech.api.enums.Mods.KubaTech;
import static kubatech.api.gui.KubaTechUITextures.PICTURE_KUBATECH_LOGO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.modularui.api.drawable.FallbackableUITexture;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;

import gregtech.api.enums.Materials;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.nei.RecipeDisplayInfo;
import kubatech.loaders.HTGRLoader;
import kubatech.loaders.item.htgritem.HTGRItem;

public class HighTemperatureGasCooledReactorRecipeMapFrontend extends RecipeMapFrontend {

    public static final UITexture PROGRESSBAR = UITexture.fullImage(KubaTech.ID, "gui/reactor_chart");

    public HighTemperatureGasCooledReactorRecipeMapFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(
            uiPropertiesBuilder.progressBarTexture(new FallbackableUITexture(PROGRESSBAR))
                .progressBarPos(new Pos2d(3, 3))
                .progressBarDirection(ProgressBar.Direction.DOWN)
                .logoPos(new Pos2d(145, 237))
                .logo(PICTURE_KUBATECH_LOGO)
                .logoSize(new Size(13, 15))
                .maxItemInputs(7)
                .maxItemOutputs(16),
            neiPropertiesBuilder.recipeBackgroundSize(new Size(170, 245))
                .itemInputsGetter(recipe -> {
                    ArrayList<ItemStack> inputs = new ArrayList<>();
                    // main item
                    inputs.add(recipe.mInputs[0]);
                    // shell
                    inputs.addAll(Arrays.asList(recipe.getMetadata(HTGRLoader.SHELL)));
                    // fuel
                    for (Pair<ItemStack, Integer> fuel : recipe.getMetadata(HTGRLoader.FUEL)) {
                        inputs.add(fuel.getLeft());
                    }
                    return inputs.toArray(new ItemStack[0]);
                })
                .itemOutputsGetter(recipe -> {
                    ArrayList<ItemStack> outputs = new ArrayList<>();
                    Materials material = GTOreDictUnificator.getAssociation(recipe.mInputs[0]).mMaterial.mMaterial;
                    outputs.add(HTGRItem.createTRISOMixture(material));
                    outputs.add(HTGRItem.createIncompleteBISOFuel(material));
                    outputs.add(HTGRItem.createIncompleteTRISOFuel(material));
                    ItemStack ofuel = HTGRItem.createTRISOFuel(material);
                    outputs.add(ofuel);
                    outputs.add(ofuel.copy());
                    outputs.add(HTGRItem.createBurnedTRISOFuel(material));
                    Collections.addAll(outputs, recipe.mOutputs);
                    // recycling
                    for (Pair<ItemStack, Integer> fuel : recipe.getMetadata(HTGRLoader.FUEL)) {
                        ItemStack stack = fuel.getLeft()
                            .copy();
                        stack.stackSize = fuel.getRight();
                        if (stack.stackSize > 0) outputs.add(stack);
                    }
                    outputs.addAll(Arrays.asList(recipe.getMetadata(HTGRLoader.SHELL)));
                    return outputs.toArray(new ItemStack[0]);
                }));
    }

    @Override
    public void addProgressBar(ModularWindow.Builder builder, @NotNull Supplier<Float> progressSupplier,
        @NotNull Pos2d windowOffset) {
        assert uiProperties.progressBarTexture != null;
        builder.widget(
            new ProgressBar().setTexture(uiProperties.progressBarTexture.get(), 170)
                .setDirection(uiProperties.progressBarDirection)
                .setProgress(() -> (0.31f + progressSupplier.get() * 0.26f)) // start 76 = 0.31 end 138 = 0.57 d = 0.26
                .setSynced(false, false)
                .setPos(uiProperties.progressBarPos.add(windowOffset))
                .setSize(new Size(170, 245)));
    }

    @Override
    public @NotNull List<Pos2d> getItemInputPositions(int itemInputCount) {
        ArrayList<Pos2d> positions = new ArrayList<>();
        // main item
        positions.add(new Pos2d(7, 25));
        // shell
        positions.add(new Pos2d(7, 80));
        positions.add(new Pos2d(7, 118));
        positions.add(new Pos2d(7, 157));
        // fuel
        int x = 30, y = 7;
        for (int i = 0; i < itemInputCount - 4; i++) {
            positions.add(new Pos2d(x, y));
            y += 18;
        }
        return positions;
    }

    @Override
    public @NotNull List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        ArrayList<Pos2d> positions = new ArrayList<>();

        // outputs
        positions.add(new Pos2d(79, 67));
        positions.add(new Pos2d(79, 99));
        positions.add(new Pos2d(79, 131));
        positions.add(new Pos2d(79, 163));
        positions.add(new Pos2d(136, 25));
        positions.add(new Pos2d(136, 163));
        // recycling

        int x = 7, y = 197;
        for (int i = 0; i < itemOutputCount - 6; i++) {
            positions.add(new Pos2d(x, y));
            x += 18;
            if (x >= 75 + 18) {
                x = 7;
                y += 18;
            }
        }
        return positions;
    }

    @Override
    public void drawDescription(RecipeDisplayInfo recipeInfo) {
        drawRecipeOwnerInfo(recipeInfo);
    }
}
