package gregtech.api.recipe;

import static gregtech.api.enums.Mods.GregTech;

import java.awt.Rectangle;
import java.util.Collections;
import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.cleanroommc.modularui.widgets.ProgressWidget;
import com.gtnewhorizons.modularui.api.drawable.FallbackableUITexture;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;

import codechicken.nei.recipe.HandlerInfo;
import gregtech.api.gui.modularui.FallbackableSteamTexture;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.gui.modularui.SteamTexture;
import gregtech.api.objects.overclockdescriber.OverclockDescriber;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.formatter.INEISpecialInfoFormatter;

// spotless:off spotless likes formatting @code to &#64;code
/**
 * Builder class for constructing {@link RecipeMap}. Instantiate this class and call {@link #build}
 * to retrieve RecipeMap. Smallest example:
 *
 * <pre>
 * {@code
 *     RecipeMap<RecipeMapBackend> exampleRecipes = RecipeMapBuilder.of("example")
 *         .maxIO(9, 4, 1, 1)
 *         .build();
 * }
 * </pre>
 *
 * Note that {@link #maxIO} is required to build.
 */
// spotless:on
@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class RecipeMapBuilder<B extends RecipeMapBackend> {

    private final String unlocalizedName;
    private final RecipeMapBackendPropertiesBuilder backendPropertiesBuilder = RecipeMapBackendProperties.builder();
    private final RecipeMapBackend.BackendCreator<B> backendCreator;
    private final BasicUIPropertiesBuilder uiPropertiesBuilder;
    private final NEIRecipePropertiesBuilder neiPropertiesBuilder = NEIRecipeProperties.builder();
    private RecipeMapFrontend.FrontendCreator frontendCreator = RecipeMapFrontend::new;

    /**
     * Constructs builder object for {@link RecipeMap} with given backend logic. For custom frontend, call
     * {@link #frontend} for the created builder object.
     *
     * @param unlocalizedName Unique identifier for the recipemap. This is also used as translation key for NEI recipe
     *                        GUI header, so add localization for it if needed.
     * @return New builder object.
     */
    public static <B extends RecipeMapBackend> RecipeMapBuilder<B> of(String unlocalizedName,
        RecipeMapBackend.BackendCreator<B> backendCreator) {
        return new RecipeMapBuilder<>(unlocalizedName, backendCreator);
    }

    /**
     * Constructs builder object for {@link RecipeMap}.
     *
     * @param unlocalizedName Unique identifier for the recipemap. This is also used as translation key for NEI recipe
     *                        GUI header, so add localization for it if needed.
     * @return New builder object.
     */
    public static RecipeMapBuilder<RecipeMapBackend> of(String unlocalizedName) {
        return new RecipeMapBuilder<>(unlocalizedName, RecipeMapBackend::new);
    }

    private RecipeMapBuilder(String unlocalizedName, RecipeMapBackend.BackendCreator<B> backendCreator) {
        this.unlocalizedName = unlocalizedName;
        this.backendCreator = backendCreator;
        this.uiPropertiesBuilder = BasicUIProperties.builder()
            .progressBarTexture(GTUITextures.fallbackableProgressbar(unlocalizedName, GTUITextures.PROGRESSBAR_ARROW))
            .neiTransferRectId(unlocalizedName);
    }

    // region backend

    /**
     * Sets minimum amount of inputs required for the recipes.
     */
    public RecipeMapBuilder<B> minInputs(int minItemInputs, int minFluidInputs) {
        backendPropertiesBuilder.minItemInputs(minItemInputs)
            .minFluidInputs(minFluidInputs);
        return this;
    }

    /**
     * Whether this recipemap should check for equality of special slot when searching recipe.
     */
    public RecipeMapBuilder<B> specialSlotSensitive() {
        backendPropertiesBuilder.specialSlotSensitive();
        return this;
    }

    /**
     * Transformer which allows you to modify the recipe builder before it emits recipes. <br>
     * Allows modification of the builder to modify this recipe, or adding recipes to other places based on the builder.
     */
    public RecipeMapBuilder<B> builderTransformer(Consumer<? super GTRecipeBuilder> builderTransformer) {
        backendPropertiesBuilder.builderTransformer(builderTransformer);
        return this;
    }

    /**
     * Changes how recipes are emitted by a particular recipe builder. Can emit multiple recipe per builder.
     */
    public RecipeMapBuilder<B> recipeEmitter(
        Function<? super GTRecipeBuilder, ? extends Iterable<? extends GTRecipe>> recipeEmitter) {
        backendPropertiesBuilder.recipeEmitter(recipeEmitter);
        return this;
    }

    /**
     * Changes how recipes are emitted by a particular recipe builder. Should not return null.
     * <p>
     * Recipes added via one of the overloads of addRecipe will NOT be affected by this function.
     */
    public RecipeMapBuilder<B> recipeEmitterSingle(
        Function<? super GTRecipeBuilder, ? extends GTRecipe> recipeEmitter) {
        return recipeEmitter(recipeEmitter.andThen(Collections::singletonList));
    }

    /**
     * Changes how recipes are emitted by a particular recipe builder. Can emit multiple recipe per builder.
     * <p>
     * Recipes added via one of the overloads of addRecipe will NOT be affected by this function.
     * <p>
     * Unlike {@link #recipeEmitter(Function)}, this one does not clear the existing recipe being emitted, if any
     */
    public RecipeMapBuilder<B> combineRecipeEmitter(
        Function<? super GTRecipeBuilder, ? extends Iterable<? extends GTRecipe>> recipeEmitter) {
        backendPropertiesBuilder.combineRecipeEmitter(recipeEmitter);
        return this;
    }

    /**
     * Changes how recipes are emitted by a particular recipe builder. Effectively add a new recipe per recipe added.
     * func must not return null.
     * <p>
     * Recipes added via one of the overloads of addRecipe will NOT be affected by this function.
     * <p>
     * Unlike {@link #recipeEmitter(Function)}, this one does not clear the existing recipe being emitted, if any
     */
    public RecipeMapBuilder<B> combineRecipeEmitterSingle(
        Function<? super GTRecipeBuilder, ? extends GTRecipe> recipeEmitter) {
        return combineRecipeEmitter(recipeEmitter.andThen(Collections::singletonList));
    }

    /**
     * Transformer which allows for modification of a recipe after it is "finalized" but before it is added to the map.
     * <br>
     * Allows modification of the recipe to change this map's recipe, or add this recipe copied and/or edited elsewhere.
     */
    public RecipeMapBuilder<B> recipeTransformer(Consumer<? super GTRecipe> recipeTransformer) {
        backendPropertiesBuilder.recipeTransformer(recipeTransformer);
        return this;
    }

    // endregion

    // region frontend UI properties

    /**
     * Sets how many item/fluid inputs/outputs does this recipemap usually has at most. It does not actually restrict
     * the number of items that can be used in recipes.
     */
    public RecipeMapBuilder<B> maxIO(int maxItemInputs, int maxItemOutputs, int maxFluidInputs, int maxFluidOutputs) {
        uiPropertiesBuilder.maxItemInputs(maxItemInputs)
            .maxItemOutputs(maxItemOutputs)
            .maxFluidInputs(maxFluidInputs)
            .maxFluidOutputs(maxFluidOutputs);
        return this;
    }

    /**
     * Sets function to get overlay for slots.
     */
    public RecipeMapBuilder<B> slotOverlays(BasicUIProperties.SlotOverlayGetter<IDrawable> slotOverlays) {
        uiPropertiesBuilder.slotOverlays(slotOverlays);
        return this;
    }

    /**
     * Sets function to get overlay for slots of steam machines.
     */
    public RecipeMapBuilder<B> slotOverlaysSteam(BasicUIProperties.SlotOverlayGetter<SteamTexture> slotOverlaysSteam) {
        uiPropertiesBuilder.slotOverlaysSteam(slotOverlaysSteam);
        return this;
    }

    // ensures the value input is never null
    public RecipeMapBuilder<B> slotOverlaysMUI2(
        BasicUIProperties.SlotOverlayGetter<com.cleanroommc.modularui.api.drawable.IDrawable> slotOverlaysMUI2) {
        uiPropertiesBuilder.slotOverlaysMUI2((index, isFluid, isOutput, isSpecial) -> {
            com.cleanroommc.modularui.api.drawable.IDrawable drawable = slotOverlaysMUI2
                .apply(index, isFluid, isOutput, isSpecial);
            return drawable == null ? com.cleanroommc.modularui.api.drawable.IDrawable.NONE : drawable;
        });
        return this;
    }

    /**
     * Sets texture and animation direction of the progressbar.
     * <p>
     * Unless specified, size should be (20, 36), consisting of two parts; First is (20, 18) size of "empty" image at
     * the top, Second is (20, 18) size of "filled" image at the bottom.
     * <p>
     * By default, it's set to {@code GTGuiTextures.PROGRESSBAR_ARROW, ProgressWidget.Direction.RIGHT}.
     */
    public RecipeMapBuilder<B> progressBarMUI2(com.cleanroommc.modularui.drawable.UITexture progressBarTextureMUI2,
        ProgressWidget.Direction progressBarDirectionMUI2) {
        uiPropertiesBuilder.progressBarTextureMUI2(progressBarTextureMUI2)
            .progressBarDirectionMUI2(progressBarDirectionMUI2);
        return this;
    }

    /**
     * Sets progressbar texture with right direction.
     * <p>
     * Unless specified, size should be (20, 36), consisting of two parts; First is (20, 18) size of "empty" image at
     * the top, Second is (20, 18) size of "filled" image at the bottom.
     */
    public RecipeMapBuilder<B> progressBarMUI2(com.cleanroommc.modularui.drawable.UITexture progressBarTextureMUI2) {
        return progressBarMUI2(progressBarTextureMUI2, ProgressWidget.Direction.RIGHT);
    }

    public RecipeMapBuilder<B> progressBarSizeMUI2(int progressBarWidthMUI2, int progressBarHeightMUI2) {
        uiPropertiesBuilder.progressBarSizeMUI2(progressBarWidthMUI2, progressBarHeightMUI2);
        return this;
    }

    /**
     * Sets texture and animation direction of the progressbar.
     * <p>
     * Unless specified, size should be (20, 36), consisting of two parts; First is (20, 18) size of "empty" image at
     * the top, Second is (20, 18) size of "filled" image at the bottom.
     * <p>
     * By default, it's set to {@code GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT}.
     */
    public RecipeMapBuilder<B> progressBar(UITexture texture, ProgressBar.Direction direction) {
        return progressBarWithFallback(GTUITextures.fallbackableProgressbar(unlocalizedName, texture), direction);
    }

    /**
     * Sets progressbar texture with right direction.
     * <p>
     * Unless specified, size should be (20, 36), consisting of two parts; First is (20, 18) size of "empty" image at
     * the top, Second is (20, 18) size of "filled" image at the bottom.
     */
    public RecipeMapBuilder<B> progressBar(UITexture texture) {
        return progressBar(texture, ProgressBar.Direction.RIGHT);
    }

    /**
     * Some resource packs want to use custom progress bar textures even for plain arrow. This method allows them to add
     * unique textures, yet other packs don't need to make textures for every recipemap.
     */
    private RecipeMapBuilder<B> progressBarWithFallback(FallbackableUITexture texture,
        ProgressBar.Direction direction) {
        uiPropertiesBuilder.progressBarTexture(texture)
            .progressBarDirection(direction);
        return this;
    }

    /**
     * Sets progressbar texture for steam machines.
     * <p>
     * Unless specified, size should be (20, 36), consisting of two parts; First is (20, 18) size of "empty" image at
     * the top, Second is (20, 18) size of "filled" image at the bottom.
     */
    public RecipeMapBuilder<B> progressBarSteam(SteamTexture texture) {
        return progressBarSteamWithFallback(
            new FallbackableSteamTexture(
                SteamTexture.fullImage(GregTech.ID, "gui/progressbar/" + unlocalizedName + "_%s"),
                texture));
    }

    private RecipeMapBuilder<B> progressBarSteamWithFallback(FallbackableSteamTexture texture) {
        uiPropertiesBuilder.progressBarTextureSteam(texture);
        return this;
    }

    /**
     * Sets size of the progressbar. (20, 36) by default.
     */
    public RecipeMapBuilder<B> progressBarSize(int x, int y) {
        uiPropertiesBuilder.progressBarSize(new Size(x, y));
        return this;
    }

    /**
     * Sets position of the progressbar. (78, 24) by default.
     */
    public RecipeMapBuilder<B> progressBarPos(int x, int y) {
        uiPropertiesBuilder.progressBarPos(new Pos2d(x, y));
        return this;
    }

    /**
     * Stops adding progressbar to the UI.
     */
    public RecipeMapBuilder<B> dontUseProgressBar() {
        uiPropertiesBuilder.useProgressBar(false);
        return this;
    }

    /**
     * Configures this recipemap to use special slot. This means special slot shows up on NEI and tooltip for special
     * slot on basic machine GUI indicates it has actual usage.
     */
    public RecipeMapBuilder<B> useSpecialSlot() {
        uiPropertiesBuilder.useSpecialSlot(true);
        return this;
    }

    /**
     * Adds GUI area where clicking shows up all the recipes available.
     *
     * @see codechicken.nei.recipe.TemplateRecipeHandler.RecipeTransferRect
     */
    public RecipeMapBuilder<B> neiTransferRect(int x, int y, int width, int height) {
        uiPropertiesBuilder.addNEITransferRect(new Rectangle(x, y, width, height));
        return this;
    }

    /**
     * Sets ID used to open NEI recipe GUI when progressbar is clicked.
     */
    public RecipeMapBuilder<B> neiTransferRectId(String neiTransferRectId) {
        uiPropertiesBuilder.neiTransferRectId(neiTransferRectId);
        return this;
    }

    /**
     * Adds additional textures shown on GUI.
     */
    public RecipeMapBuilder<B> addSpecialTexture(int x, int y, int width, int height, IDrawable texture) {
        uiPropertiesBuilder.addSpecialTexture(new Size(width, height), new Pos2d(x, y), texture);
        return this;
    }

    /**
     * Adds additional textures shown on steam machine GUI.
     */
    public RecipeMapBuilder<B> addSpecialTextureSteam(int x, int y, int width, int height, SteamTexture texture) {
        uiPropertiesBuilder.addSpecialTextureSteam(new Size(width, height), new Pos2d(x, y), texture);
        return this;
    }

    /**
     * Sets logo shown on GUI. GregTech logo by default.
     */
    public RecipeMapBuilder<B> logo(IDrawable logo) {
        uiPropertiesBuilder.logo(logo);
        return this;
    }

    /**
     * Sets size of logo. (17, 17) by default.
     */
    public RecipeMapBuilder<B> logoSize(int width, int height) {
        uiPropertiesBuilder.logoSize(new Size(width, height));
        return this;
    }

    /**
     * Sets position of logo. (152, 63) by default.
     */
    public RecipeMapBuilder<B> logoPos(int x, int y) {
        uiPropertiesBuilder.logoPos(new Pos2d(x, y));
        return this;
    }

    /**
     * Sets amperage for the recipemap.
     */
    public RecipeMapBuilder<B> amperage(int amperage) {
        uiPropertiesBuilder.amperage(amperage);
        return this;
    }

    // endregion

    // region frontend NEI properties

    /**
     * Stops adding dedicated NEI recipe page for this recipemap. This does not prevent adding transferrect for the
     * machine GUI.
     */
    public RecipeMapBuilder<B> disableRegisterNEI() {
        neiPropertiesBuilder.disableRegisterNEI();
        return this;
    }

    /**
     * Sets properties of NEI handler info this recipemap belongs to. You can specify icon shown on recipe tab, handler
     * height, number of recipes per page, etc. Either use supplied template or return newly constructed one.
     * <p>
     * Invocation of the builder creator is delayed until the actual registration (FMLLoadCompleteEvent), so you can
     * safely use itemstack that doesn't exist as of recipemap initialization.
     * <p>
     * If this method is not used, handler icon will be inferred from recipe catalysts associated with this recipemap.
     * <p>
     * Precisely, what's registered to NEI is {@link RecipeCategory}, not RecipeMap. However, handler info supplied by
     * this method will be used for default category where most of the recipes belong to.
     */
    public RecipeMapBuilder<B> neiHandlerInfo(UnaryOperator<HandlerInfo.Builder> handlerInfoCreator) {
        neiPropertiesBuilder.handlerInfoCreator(handlerInfoCreator);
        return this;
    }

    /**
     * Sets offset of background shown on NEI.
     */
    public RecipeMapBuilder<B> neiRecipeBackgroundSize(int width, int height) {
        neiPropertiesBuilder.recipeBackgroundSize(new Size(width, height));
        return this;
    }

    /**
     * Sets size of background shown on NEI.
     */
    public RecipeMapBuilder<B> neiRecipeBackgroundOffset(int x, int y) {
        neiPropertiesBuilder.recipeBackgroundOffset(new Pos2d(x, y));
        return this;
    }

    /**
     * Sets formatter for special description for the recipe, mainly {@link GTRecipe#mSpecialValue}.
     */
    public RecipeMapBuilder<B> neiSpecialInfoFormatter(INEISpecialInfoFormatter neiSpecialInfoFormatter) {
        neiPropertiesBuilder.neiSpecialInfoFormatter(neiSpecialInfoFormatter);
        return this;
    }

    /**
     * Sets whether to show oredict equivalent item outputs on NEI.
     */
    public RecipeMapBuilder<B> unificateOutputNEI(boolean unificateOutputNEI) {
        neiPropertiesBuilder.unificateOutput(unificateOutputNEI);
        return this;
    }

    /**
     * Sets NEI recipe handler to use a custom filter method {@link OverclockDescriber#canHandle} to limit the shown
     * recipes when searching recipes with recipe catalyst. Without calling this method, the voltage of the recipe is
     * the only factor to filter recipes by default.
     * <p>
     * This method on its own doesn't do anything. You need to bind custom {@link OverclockDescriber} object to machines
     * that will be shown as recipe catalysts for this recipemap by implementing
     * {@link gregtech.api.interfaces.tileentity.IOverclockDescriptionProvider}.
     */
    public RecipeMapBuilder<B> useCustomFilterForNEI() {
        neiPropertiesBuilder.useCustomFilter();
        return this;
    }

    /**
     * Stops rendering the actual stack size of items on NEI.
     */
    public RecipeMapBuilder<B> disableRenderRealStackSizes() {
        neiPropertiesBuilder.disableRenderRealStackSizes();
        return this;
    }

    /**
     * Allows modifying what item inputs get displayed on NEI, without affecting GTRecipe object on backend.
     */
    public RecipeMapBuilder<B> neiItemInputsGetter(Function<GTRecipe, ItemStack[]> itemInputsGetter) {
        neiPropertiesBuilder.itemInputsGetter(itemInputsGetter);
        return this;
    }

    /**
     * Allows modifying what fluid inputs get displayed on NEI, without affecting GTRecipe object on backend.
     */
    public RecipeMapBuilder<B> neiFluidInputsGetter(Function<GTRecipe, FluidStack[]> fluidInputsGetter) {
        neiPropertiesBuilder.fluidInputsGetter(fluidInputsGetter);
        return this;
    }

    /**
     * Allows modifying what item outputs get displayed on NEI, without affecting GTRecipe object on backend.
     */
    public RecipeMapBuilder<B> neiItemOutputsGetter(Function<GTRecipe, ItemStack[]> itemOutputsGetter) {
        neiPropertiesBuilder.itemOutputsGetter(itemOutputsGetter);
        return this;
    }

    /**
     * Allows modifying what fluid outputs get displayed on NEI, without affecting GTRecipe object on backend.
     */
    public RecipeMapBuilder<B> neiFluidOutputsGetter(Function<GTRecipe, FluidStack[]> fluidOutputsGetter) {
        neiPropertiesBuilder.fluidOutputsGetter(fluidOutputsGetter);
        return this;
    }

    /**
     * Sets custom comparator for NEI recipe sort.
     */
    public RecipeMapBuilder<B> neiRecipeComparator(Comparator<GTRecipe> comparator) {
        neiPropertiesBuilder.recipeComparator(comparator);
        return this;
    }

    // endregion

    /**
     * Sets custom frontend logic. For custom backend, pass it to {@link #of(String, RecipeMapBackend.BackendCreator)}.
     */
    public RecipeMapBuilder<B> frontend(RecipeMapFrontend.FrontendCreator frontendCreator) {
        this.frontendCreator = frontendCreator;
        return this;
    }

    /**
     * Builds new recipemap.
     *
     * @return Recipemap object with backend type parameter, which is {@code RecipeMapFrontend} unless specified.
     */
    public RecipeMap<B> build() {
        return new RecipeMap<>(
            unlocalizedName,
            backendCreator.create(backendPropertiesBuilder),
            frontendCreator.create(uiPropertiesBuilder, neiPropertiesBuilder));
    }

    private static <T> Function<? super T, ? extends T> withIdentityReturn(Consumer<T> func) {
        return r -> {
            func.accept(r);
            return r;
        };
    }
}
