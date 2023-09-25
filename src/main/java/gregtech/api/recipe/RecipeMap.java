package gregtech.api.recipe;

import static gregtech.api.enums.Mods.*;
import static gregtech.api.recipe.check.FindRecipeResult.NOT_FOUND;
import static gregtech.api.recipe.check.FindRecipeResult.ofSuccess;
import static gregtech.api.util.GT_RecipeBuilder.handleRecipeCollision;
import static gregtech.api.util.GT_RecipeMapUtil.*;
import static gregtech.api.util.GT_Utility.*;
import static net.minecraft.util.EnumChatFormatting.GRAY;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Iterables;
import com.gtnewhorizons.modularui.api.GlStateManager;
import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.drawable.FallbackableUITexture;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import codechicken.nei.PositionedStack;
import gnu.trove.map.TByteObjectMap;
import gnu.trove.map.hash.TByteObjectHashMap;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.SteamVariant;
import gregtech.api.gui.GT_GUIColorOverride;
import gregtech.api.gui.modularui.FallbackableSteamTexture;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.gui.modularui.SteamTexture;
import gregtech.api.interfaces.IGT_RecipeMap;
import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.recipe.check.FindRecipeResult;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_RecipeBuilder;
import gregtech.api.util.GT_RecipeConstants;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.common.power.EUPower;
import gregtech.common.power.Power;
import gregtech.common.power.UnspecifiedEUPower;
import gregtech.nei.GT_NEI_DefaultHandler;
import gregtech.nei.INEISpecialInfoFormatter;
import gregtech.nei.NEIRecipeInfo;

public class RecipeMap implements IGT_RecipeMap {

    /**
     * Contains all Recipe Maps
     */
    public static final Collection<RecipeMap> sMappings = new ArrayList<>();
    /**
     * All recipe maps indexed by their {@link #mUniqueIdentifier}.
     */
    public static final Map<String, RecipeMap> sIndexedMappings = new HashMap<>();

    protected static final String TEXTURES_GUI_BASICMACHINES = "textures/gui/basicmachines";

    @Nullable
    public static RecipeMap findRecipeMap(@Nonnull String unlocalizedName) {
        return sMappings.stream()
            .filter(m -> unlocalizedName.equals(m.mUnlocalizedName))
            .findFirst()
            .orElse(null);
    }

    /**
     * HashMap of Recipes based on their Items
     */
    public final Map<GT_ItemStack, Collection<GT_Recipe>> mRecipeItemMap = new /* Concurrent */ HashMap<>();
    /**
     * HashMap of Recipes based on their Fluids
     */
    public final Map<String, Collection<GT_Recipe>> mRecipeFluidMap = new HashMap<>();

    public final HashSet<String> mRecipeFluidNameMap = new HashSet<>();
    /**
     * The List of all Recipes
     */
    public final Collection<GT_Recipe> mRecipeList;
    /**
     * String used as an unlocalised Name.
     */
    public final String mUnlocalizedName;
    /**
     * String used in NEI for the Recipe Lists. If null it will use the unlocalised Name instead
     */
    public final String mNEIName;
    /**
     * GUI used for NEI Display. Usually the GUI of the Machine itself
     */
    public final String mNEIGUIPath;

    public final String mNEISpecialValuePre, mNEISpecialValuePost;
    public final int mUsualInputCount, mUsualOutputCount, mNEISpecialValueMultiplier, mMinimalInputItems,
        mMinimalInputFluids, mAmperage;
    public final boolean mNEIAllowed, mShowVoltageAmperageInNEI;

    /**
     * Whether to show oredict equivalent outputs when NEI is queried to show recipe
     */
    public boolean mNEIUnificateOutput = true;

    /**
     * Unique identifier for this recipe map. Generated from aUnlocalizedName and a few other parameters. See
     * constructor for details.
     */
    public final String mUniqueIdentifier;

    /**
     * Whether this recipe map contains any fluid outputs.
     */
    private boolean mHasFluidOutputs = false;

    /**
     * Whether this recipe map contains special slot inputs.
     */
    private boolean mUsesSpecialSlot = false;

    /**
     * Whether this recipemap checks for equality of special slot when searching recipe.
     */
    private boolean isSpecialSlotSensitive = false;

    /**
     * How many fluid inputs does this recipemap has at most. Currently used only for NEI slot placements and does
     * not actually restrict number of fluids used in the recipe.
     */
    private int usualFluidInputCount;

    /**
     * How many fluid outputs does this recipemap has at most. Currently used only for NEI slot placements and does
     * not actually restrict number of fluids used in the recipe.
     */
    private int usualFluidOutputCount;

    /**
     * Overlays used for GUI. 1 = If it's fluid slot. 2 = If it's output slot. 4 = If it's first slot in the same
     * section, e.g. first slot in the item output slots 8 = If it's special item slot.
     */
    private final TByteObjectMap<IDrawable> slotOverlays = new TByteObjectHashMap<>();

    /**
     * Overlays used for GUI on steam machine. 1 = If it's fluid slot. 2 = If it's output slot. 4 = If it's first
     * slot in the same section, e.g. first slot in the item output slots 8 = If it's special item slot.
     */
    private final TByteObjectMap<SteamTexture> slotOverlaysSteam = new TByteObjectHashMap<>();

    /**
     * Progressbar used for BasicMachine GUI and/or NEI. Unless specified, size should be (20, 36), consisting of
     * two parts; First is (20, 18) size of "empty" image at the top, Second is (20, 18) size of "filled" image at
     * the bottom.
     */
    private FallbackableUITexture progressBarTexture;

    /**
     * Progressbar used for steam machine GUI and/or NEI. Unless specified, size should be (20, 36), consisting of
     * two parts; First is (20, 18) size of "empty" image at the top, Second is (20, 18) size of "filled" image at
     * the bottom.
     */
    private FallbackableSteamTexture progressBarTextureSteam;

    public ProgressBar.Direction progressBarDirection = ProgressBar.Direction.RIGHT;

    public Size progressBarSize = new Size(20, 18);

    public Pos2d progressBarPos = new Pos2d(78, 24);

    public Rectangle neiTransferRect = new Rectangle(
        progressBarPos.x - (16 / 2),
        progressBarPos.y,
        progressBarSize.width + 16,
        progressBarSize.height);

    /**
     * Image size in direction of progress. Used for non-smooth rendering.
     */
    private int progressBarImageSize;

    /**
     * Additional textures shown on GUI.
     */
    public final List<Pair<IDrawable, Pair<Size, Pos2d>>> specialTextures = new ArrayList<>();

    /**
     * Additional textures shown on steam machine GUI.
     */
    public final List<Pair<SteamTexture, Pair<Size, Pos2d>>> specialTexturesSteam = new ArrayList<>();

    public IDrawable logo = GT_UITextures.PICTURE_GT_LOGO_17x17_TRANSPARENT;

    public Pos2d logoPos = new Pos2d(152, 63);

    public Size logoSize = new Size(17, 17);

    public Pos2d neiBackgroundOffset = new Pos2d(2, 3);

    public Size neiBackgroundSize = new Size(172, 82);

    protected final GT_GUIColorOverride colorOverride;
    private int neiTextColorOverride = -1;

    private INEISpecialInfoFormatter neiSpecialInfoFormatter;

    private final boolean checkForCollision = true;
    private boolean disableOptimize = false;
    private Function<? super GT_RecipeBuilder, ? extends Iterable<? extends GT_Recipe>> recipeEmitter = this::defaultBuildRecipe;
    private Function<? super GT_Recipe, ? extends GT_Recipe> specialHandler;
    private String recipeConfigCategory;
    private Function<? super GT_Recipe, String> recipeConfigKeyConvertor;
    private final List<IGT_RecipeMap> downstreams = new ArrayList<>(0);

    /**
     * Flag if a comparator should be used to search the recipe in NEI (which is defined in {@link Power}). Else
     * only the voltage will be used to find recipes
     */
    public boolean useComparatorForNEI;

    /**
     * Whether to render the actual size of stacks or a size of 1.
     */
    public boolean renderRealStackSizes = true;

    /**
     * Initialises a new type of Recipe Handler.
     *
     * @param aRecipeList                a List you specify as Recipe List. Usually just an ArrayList with a
     *                                   pre-initialised Size.
     * @param aUnlocalizedName           the unlocalised Name of this Recipe Handler, used mainly for NEI.
     * @param aLocalName                 @deprecated the displayed Name inside the NEI Recipe GUI for optionally
     *                                   registering aUnlocalizedName
     *                                   with the language manager
     * @param aNEIGUIPath                the displayed GUI Texture, usually just a Machine GUI. Auto-Attaches ".png"
     *                                   if forgotten.
     * @param aUsualInputCount           the usual amount of Input Slots this Recipe Class has.
     * @param aUsualOutputCount          the usual amount of Output Slots this Recipe Class has.
     * @param aNEISpecialValuePre        the String in front of the Special Value in NEI.
     * @param aNEISpecialValueMultiplier the Value the Special Value is getting Multiplied with before displaying
     * @param aNEISpecialValuePost       the String after the Special Value. Usually for a Unit or something.
     * @param aNEIAllowed                if NEI is allowed to display this Recipe Handler in general.
     */
    public RecipeMap(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName, String aNEIName,
        String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount, int aMinimalInputItems,
        int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre, int aNEISpecialValueMultiplier,
        String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed) {
        sMappings.add(this);
        mNEIAllowed = aNEIAllowed;
        mShowVoltageAmperageInNEI = aShowVoltageAmperageInNEI;
        mRecipeList = aRecipeList;
        mNEIName = aNEIName == null ? aUnlocalizedName : aNEIName;
        mNEIGUIPath = aNEIGUIPath.endsWith(".png") ? aNEIGUIPath : aNEIGUIPath + ".png";
        mNEISpecialValuePre = aNEISpecialValuePre;
        mNEISpecialValueMultiplier = aNEISpecialValueMultiplier;
        mNEISpecialValuePost = aNEISpecialValuePost;
        mAmperage = aAmperage;
        mUsualInputCount = aUsualInputCount;
        mUsualOutputCount = aUsualOutputCount;
        mMinimalInputItems = aMinimalInputItems;
        mMinimalInputFluids = aMinimalInputFluids;
        GregTech_API.sItemStackMappings.add(mRecipeItemMap);
        mUnlocalizedName = aUnlocalizedName;
        if (aLocalName != null) {
            GT_LanguageManager.addStringLocalization(mUnlocalizedName, aLocalName);
        }
        mUniqueIdentifier = String.format(
            "%s_%d_%d_%d_%d_%d",
            aUnlocalizedName,
            aAmperage,
            aUsualInputCount,
            aUsualOutputCount,
            aMinimalInputFluids,
            aMinimalInputItems);
        progressBarTexture = new FallbackableUITexture(
            UITexture.fullImage(GregTech.ID, "gui/progressbar/" + mUnlocalizedName),
            GT_UITextures.PROGRESSBAR_ARROW);
        colorOverride = GT_GUIColorOverride.get(ModularUITextures.VANILLA_BACKGROUND.location);
        if (sIndexedMappings.put(mUniqueIdentifier, this) != null)
            throw new IllegalArgumentException("Duplicate recipe map registered: " + mUniqueIdentifier);
    }

    public RecipeMap setDisableOptimize(boolean disableOptimize) {
        this.disableOptimize = disableOptimize;
        return this;
    }

    public RecipeMap setSpecialSlotSensitive(boolean isSpecialSlotSensitive) {
        this.isSpecialSlotSensitive = isSpecialSlotSensitive;
        return this;
    }

    public RecipeMap setNEIUnificateOutput(boolean mNEIUnificateOutput) {
        this.mNEIUnificateOutput = mNEIUnificateOutput;
        return this;
    }

    public RecipeMap useComparatorForNEI(boolean use) {
        this.useComparatorForNEI = use;
        return this;
    }

    public RecipeMap setRenderRealStackSizes(boolean renderRealStackSizes) {
        this.renderRealStackSizes = renderRealStackSizes;
        return this;
    }

    public RecipeMap setSlotOverlay(boolean isFluid, boolean isOutput, boolean isFirst, boolean isSpecial,
        IDrawable slotOverlay) {
        this.slotOverlays.put(
            (byte) ((isFluid ? 1 : 0) + (isOutput ? 2 : 0) + (isFirst ? 4 : 0) + (isSpecial ? 8 : 0)),
            slotOverlay);
        return this;
    }

    public RecipeMap setSlotOverlay(boolean isFluid, boolean isOutput, boolean isFirst, IDrawable slotOverlay) {
        return setSlotOverlay(isFluid, isOutput, isFirst, false, slotOverlay);
    }

    public RecipeMap setSlotOverlay(boolean isFluid, boolean isOutput, IDrawable slotOverlay) {
        return setSlotOverlay(isFluid, isOutput, true, slotOverlay)
            .setSlotOverlay(isFluid, isOutput, false, slotOverlay);
    }

    public RecipeMap setSlotOverlaySteam(boolean isFluid, boolean isOutput, boolean isFirst, boolean isSpecial,
        SteamTexture slotOverlay) {
        this.slotOverlaysSteam.put(
            (byte) ((isFluid ? 1 : 0) + (isOutput ? 2 : 0) + (isFirst ? 4 : 0) + (isSpecial ? 8 : 0)),
            slotOverlay);
        return this;
    }

    public RecipeMap setSlotOverlaySteam(boolean isOutput, boolean isFirst, SteamTexture slotOverlay) {
        return setSlotOverlaySteam(false, isOutput, isFirst, false, slotOverlay);
    }

    public RecipeMap setSlotOverlaySteam(boolean isOutput, SteamTexture slotOverlay) {
        return setSlotOverlaySteam(false, isOutput, true, false, slotOverlay)
            .setSlotOverlaySteam(false, isOutput, false, false, slotOverlay);
    }

    public RecipeMap setProgressBar(UITexture progressBarTexture, ProgressBar.Direction progressBarDirection) {
        return setProgressBarWithFallback(
            new FallbackableUITexture(
                UITexture.fullImage(GregTech.ID, "gui/progressbar/" + mUnlocalizedName),
                progressBarTexture),
            progressBarDirection);
    }

    public RecipeMap setProgressBar(UITexture progressBarTexture) {
        return setProgressBar(progressBarTexture, ProgressBar.Direction.RIGHT);
    }

    /**
     * Some resource packs want to use custom progress bar textures even for plain arrow. This method allows them to
     * add unique textures, yet other packs don't need to make textures for every recipemap.
     */
    public RecipeMap setProgressBarWithFallback(FallbackableUITexture progressBarTexture,
        ProgressBar.Direction progressBarDirection) {
        this.progressBarTexture = progressBarTexture;
        this.progressBarDirection = progressBarDirection;
        return this;
    }

    public RecipeMap setProgressBarSteam(SteamTexture progressBarTexture) {
        return setProgressBarSteamWithFallback(
            new FallbackableSteamTexture(
                SteamTexture.fullImage(GregTech.ID, "gui/progressbar/" + mUnlocalizedName + "_%s"),
                progressBarTexture));
    }

    public RecipeMap setProgressBarSteamWithFallback(FallbackableSteamTexture progressBarTexture) {
        this.progressBarTextureSteam = progressBarTexture;
        return this;
    }

    public RecipeMap setProgressBarSize(int x, int y) {
        this.progressBarSize = new Size(x, y);
        return this;
    }

    public RecipeMap setProgressBarPos(int x, int y) {
        this.progressBarPos = new Pos2d(x, y);
        return this;
    }

    public RecipeMap setProgressBarImageSize(int progressBarImageSize) {
        this.progressBarImageSize = progressBarImageSize;
        return this;
    }

    public RecipeMap setNEITransferRect(Rectangle neiTransferRect) {
        this.neiTransferRect = neiTransferRect;
        return this;
    }

    public RecipeMap addSpecialTexture(int width, int height, int x, int y, IDrawable texture) {
        specialTextures
            .add(new ImmutablePair<>(texture, new ImmutablePair<>(new Size(width, height), new Pos2d(x, y))));
        return this;
    }

    public RecipeMap addSpecialTextureSteam(int width, int height, int x, int y, SteamTexture texture) {
        specialTexturesSteam
            .add(new ImmutablePair<>(texture, new ImmutablePair<>(new Size(width, height), new Pos2d(x, y))));
        return this;
    }

    public RecipeMap setUsualFluidInputCount(int usualFluidInputCount) {
        this.usualFluidInputCount = usualFluidInputCount;
        return this;
    }

    public RecipeMap setUsualFluidOutputCount(int usualFluidOutputCount) {
        this.usualFluidOutputCount = usualFluidOutputCount;
        return this;
    }

    public RecipeMap setLogo(IDrawable logo) {
        this.logo = logo;
        return this;
    }

    public RecipeMap setLogoPos(int x, int y) {
        this.logoPos = new Pos2d(x, y);
        return this;
    }

    public RecipeMap setLogoSize(int width, int height) {
        this.logoSize = new Size(width, height);
        return this;
    }

    public RecipeMap setNEIBackgroundOffset(int x, int y) {
        this.neiBackgroundOffset = new Pos2d(x, y);
        return this;
    }

    public RecipeMap setNEIBackgroundSize(int width, int height) {
        this.neiBackgroundSize = new Size(width, height);
        return this;
    }

    public RecipeMap setNEISpecialInfoFormatter(INEISpecialInfoFormatter neiSpecialInfoFormatter) {
        this.neiSpecialInfoFormatter = neiSpecialInfoFormatter;
        return this;
    }

    /**
     * Change how recipes are emitted by a particular recipe builder. Can emit multiple recipe per builder.
     */
    public RecipeMap setRecipeEmitter(
        Function<? super GT_RecipeBuilder, ? extends Iterable<? extends GT_Recipe>> func) {
        this.recipeEmitter = func;
        return this;
    }

    /**
     * Change how recipes are emitted by a particular recipe builder. Can emit multiple recipe per builder.
     * <p>
     * Unlike {@link #setRecipeEmitter(Function)}, this one does not clear the existing recipe being emitted, if any
     */
    public RecipeMap combineRecipeEmitter(
        Function<? super GT_RecipeBuilder, ? extends Iterable<? extends GT_Recipe>> func) {
        // move recipeEmitter to local variable, so lambda capture the function itself instead of this
        Function<? super GT_RecipeBuilder, ? extends Iterable<? extends GT_Recipe>> cur = recipeEmitter;
        this.recipeEmitter = b -> Iterables.concat(cur.apply(b), func.apply(b));
        return this;
    }

    /**
     * Change how recipes are emitted by a particular recipe builder. Should not return null.
     */
    public RecipeMap setRecipeEmitterSingle(Function<? super GT_RecipeBuilder, ? extends GT_Recipe> func) {
        return setRecipeEmitter(func.andThen(Collections::singletonList));
    }

    /**
     * Change how recipes are emitted by a particular recipe builder. Effectively add a new recipe per recipe added.
     * func must not return null.
     * <p>
     * Unlike {@link #setRecipeEmitter(Function)}, this one does not clear the existing recipe being emitted, if any
     */
    public RecipeMap combineRecipeEmitterSingle(Function<? super GT_RecipeBuilder, ? extends GT_Recipe> func) {
        return combineRecipeEmitter(func.andThen(Collections::singletonList));
    }

    private static <T> Function<? super T, ? extends T> withIdentityReturn(Consumer<T> func) {
        return r -> {
            func.accept(r);
            return r;
        };
    }

    /**
     * Run a custom hook on all recipes added <b>via builder</b>. For more complicated behavior subclass this, then
     * override {@link #doAdd(GT_RecipeBuilder)}
     * <p>
     * Recipes added via one of the overloads of addRecipe will NOT be affected by this function.
     */
    public RecipeMap setRecipeSpecialHandler(Function<? super GT_Recipe, ? extends GT_Recipe> func) {
        this.specialHandler = func;
        return this;
    }

    /**
     * Run a custom hook on all recipes added <b>via builder</b>. For more complicated behavior, create a subclass
     * and override {@link #doAdd(GT_RecipeBuilder)}
     * <p>
     * Recipes added via one of the overloads of addRecipe will NOT be affected by this function.
     */
    public RecipeMap setRecipeSpecialHandler(Consumer<GT_Recipe> func) {
        return setRecipeSpecialHandler(withIdentityReturn(func));
    }

    /**
     * Run a custom hook on all recipes added <b>via builder</b>. For more complicated behavior subclass this, then
     * override {@link #doAdd(GT_RecipeBuilder)}.
     * <p>
     * Recipes added via one of the overloads of addRecipe will NOT be affected by this function.
     * <p>
     * Unlike {@link #setRecipeSpecialHandler(Function)}, this one will not replace the existing special handler.
     * The supplied function will be given the output of existing handler when a recipe is added.
     */
    public RecipeMap chainRecipeSpecialHandler(Function<? super GT_Recipe, ? extends GT_Recipe> func) {
        this.specialHandler = specialHandler == null ? func : specialHandler.andThen(func);
        return this;
    }

    /**
     * Run a custom hook on all recipes added <b>via builder</b>. For more complicated behavior subclass this, then
     * override {@link #doAdd(GT_RecipeBuilder)}.
     * <p>
     * Recipes added via one of the overloads of addRecipe will NOT be affected by this function.
     * <p>
     * Unlike {@link #setRecipeSpecialHandler(Function)}, this one will not replace the existing special handler.
     * The supplied function will be given the output of existing handler when a recipe is added.
     */
    public RecipeMap chainRecipeSpecialHandler(Consumer<GT_Recipe> func) {
        return chainRecipeSpecialHandler(withIdentityReturn(func));
    }

    public RecipeMap setRecipeConfigFile(String category, Function<? super GT_Recipe, String> keyConvertor) {
        if (StringUtils.isBlank(category) || keyConvertor == null) throw new IllegalArgumentException();
        this.recipeConfigCategory = category;
        this.recipeConfigKeyConvertor = keyConvertor;
        return this;
    }

    @Override
    public void addDownstream(IGT_RecipeMap downstream) {
        this.downstreams.add(downstream);
    }

    public GT_Recipe addRecipe(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecial,
        int[] aOutputChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt,
        int aSpecialValue) {
        return addRecipe(
            new GT_Recipe(
                aOptimize,
                aInputs,
                aOutputs,
                aSpecial,
                aOutputChances,
                aFluidInputs,
                aFluidOutputs,
                aDuration,
                aEUt,
                aSpecialValue));
    }

    public GT_Recipe addRecipe(int[] aOutputChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs,
        int aDuration, int aEUt, int aSpecialValue) {
        return addRecipe(
            new GT_Recipe(
                false,
                null,
                null,
                null,
                aOutputChances,
                aFluidInputs,
                aFluidOutputs,
                aDuration,
                aEUt,
                aSpecialValue),
            false,
            false,
            false);
    }

    public GT_Recipe addRecipe(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecial,
        FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
        return addRecipe(
            new GT_Recipe(
                aOptimize,
                aInputs,
                aOutputs,
                aSpecial,
                null,
                aFluidInputs,
                aFluidOutputs,
                aDuration,
                aEUt,
                aSpecialValue));
    }

    public GT_Recipe addRecipe(GT_Recipe aRecipe) {
        return addRecipe(aRecipe, true, false, false);
    }

    protected GT_Recipe addRecipe(GT_Recipe aRecipe, boolean aCheckForCollisions, boolean aFakeRecipe,
        boolean aHidden) {
        aRecipe.mHidden = aHidden;
        aRecipe.mFakeRecipe = aFakeRecipe;
        if (aRecipe.mFluidInputs.length < mMinimalInputFluids && aRecipe.mInputs.length < mMinimalInputItems)
            return null;
        if (aCheckForCollisions
            && findRecipe(null, false, true, Long.MAX_VALUE, aRecipe.mFluidInputs, aRecipe.mInputs) != null)
            return null;
        return add(aRecipe);
    }

    /**
     * Only used for fake Recipe Handlers to show something in NEI, do not use this for adding actual Recipes!
     * findRecipe wont find fake Recipes, containsInput WILL find fake Recipes
     */
    public GT_Recipe addFakeRecipe(boolean aCheckForCollisions, ItemStack[] aInputs, ItemStack[] aOutputs,
        Object aSpecial, int[] aOutputChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration,
        int aEUt, int aSpecialValue) {
        return addFakeRecipe(
            aCheckForCollisions,
            new GT_Recipe(
                false,
                aInputs,
                aOutputs,
                aSpecial,
                aOutputChances,
                aFluidInputs,
                aFluidOutputs,
                aDuration,
                aEUt,
                aSpecialValue));
    }

    /**
     * Only used for fake Recipe Handlers to show something in NEI, do not use this for adding actual Recipes!
     * findRecipe wont find fake Recipes, containsInput WILL find fake Recipes
     */
    public GT_Recipe addFakeRecipe(boolean aCheckForCollisions, ItemStack[] aInputs, ItemStack[] aOutputs,
        Object aSpecial, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt,
        int aSpecialValue) {
        return addFakeRecipe(
            aCheckForCollisions,
            new GT_Recipe(
                false,
                aInputs,
                aOutputs,
                aSpecial,
                null,
                aFluidInputs,
                aFluidOutputs,
                aDuration,
                aEUt,
                aSpecialValue));
    }

    public GT_Recipe addFakeRecipe(boolean aCheckForCollisions, ItemStack[] aInputs, ItemStack[] aOutputs,
        Object aSpecial, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt,
        int aSpecialValue, boolean hidden) {
        return addFakeRecipe(
            aCheckForCollisions,
            new GT_Recipe(
                false,
                aInputs,
                aOutputs,
                aSpecial,
                null,
                aFluidInputs,
                aFluidOutputs,
                aDuration,
                aEUt,
                aSpecialValue),
            hidden);
    }

    public GT_Recipe addFakeRecipe(boolean aCheckForCollisions, ItemStack[] aInputs, ItemStack[] aOutputs,
        Object aSpecial, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt,
        int aSpecialValue, ItemStack[][] aAlt, boolean hidden) {
        return addFakeRecipe(
            aCheckForCollisions,
            new GT_Recipe.GT_Recipe_WithAlt(
                false,
                aInputs,
                aOutputs,
                aSpecial,
                null,
                aFluidInputs,
                aFluidOutputs,
                aDuration,
                aEUt,
                aSpecialValue,
                aAlt),
            hidden);
    }

    /**
     * Only used for fake Recipe Handlers to show something in NEI, do not use this for adding actual Recipes!
     * findRecipe wont find fake Recipes, containsInput WILL find fake Recipes
     */
    public GT_Recipe addFakeRecipe(boolean aCheckForCollisions, GT_Recipe aRecipe) {
        return addRecipe(aRecipe, aCheckForCollisions, true, false);
    }

    public GT_Recipe addFakeRecipe(boolean aCheckForCollisions, GT_Recipe aRecipe, boolean hidden) {
        return addRecipe(aRecipe, aCheckForCollisions, true, hidden);
    }

    @Nonnull
    @Override
    public Collection<GT_Recipe> doAdd(GT_RecipeBuilder builder) {
        Iterable<? extends GT_Recipe> recipes = recipeEmitter.apply(builder);
        Collection<GT_Recipe> ret = new ArrayList<>();
        for (GT_Recipe r : recipes) {
            if (recipeConfigCategory != null) {
                String configKey = recipeConfigKeyConvertor.apply(r);
                if (configKey != null
                    && (r.mDuration = GregTech_API.sRecipeFile.get(recipeConfigCategory, configKey, r.mDuration))
                        <= 0) {
                    continue;
                }
            }
            if (r.mFluidInputs.length < mMinimalInputFluids && r.mInputs.length < mMinimalInputItems) return null;
            if (r.mSpecialValue == 0) {
                // new style cleanroom/lowgrav handling
                int specialValue = 0;
                if (builder.getMetadata(GT_RecipeConstants.LOW_GRAVITY, false)) specialValue -= 100;
                if (builder.getMetadata(GT_RecipeConstants.CLEANROOM, false)) specialValue -= 200;
                for (GT_RecipeBuilder.MetadataIdentifier<Integer> ident : SPECIAL_VALUE_ALIASES) {
                    Integer metadata = builder.getMetadata(ident, null);
                    if (metadata != null) {
                        specialValue = metadata;
                        break;
                    }
                }
                r.mSpecialValue = specialValue;
            }
            if (specialHandler != null) r = specialHandler.apply(r);
            if (r == null) continue;
            if (checkForCollision && findRecipe(null, false, true, Long.MAX_VALUE, r.mFluidInputs, r.mInputs) != null) {
                StringBuilder errorInfo = new StringBuilder();
                boolean hasAnEntry = false;
                for (FluidStack fStack : r.mFluidInputs) {
                    if (fStack == null) {
                        continue;
                    }
                    String s = fStack.getLocalizedName();
                    if (s == null) {
                        continue;
                    }
                    if (hasAnEntry) {
                        errorInfo.append("+")
                            .append(s);
                    } else {
                        errorInfo.append(s);
                    }
                    hasAnEntry = true;
                }
                for (ItemStack iStack : r.mInputs) {
                    if (iStack == null) {
                        continue;
                    }
                    String s = iStack.getDisplayName();
                    if (hasAnEntry) {
                        errorInfo.append("+")
                            .append(s);
                    } else {
                        errorInfo.append(s);
                    }
                    hasAnEntry = true;
                }
                handleRecipeCollision(errorInfo.toString());
                continue;
            }
            ret.add(add(r));
        }
        if (!ret.isEmpty()) {
            builder.clearInvalid();
            for (IGT_RecipeMap downstream : downstreams) {
                downstream.doAdd(builder);
            }
        }
        return ret;
    }

    public final Iterable<? extends GT_Recipe> defaultBuildRecipe(GT_RecipeBuilder builder) {
        // TODO sensible validation
        GT_RecipeBuilder b = builder;
        if (disableOptimize && builder.isOptimize()) {
            b = copy(builder, b).noOptimize();
        }
        return buildOrEmpty(b);
    }

    private static GT_RecipeBuilder copy(GT_RecipeBuilder original, GT_RecipeBuilder b) {
        return b == original ? b.copy() : b;
    }

    public GT_Recipe add(GT_Recipe aRecipe) {
        mRecipeList.add(aRecipe);
        for (FluidStack aFluid : aRecipe.mFluidInputs) {
            if (aFluid != null) {
                Collection<GT_Recipe> tList = mRecipeFluidMap.computeIfAbsent(
                    aFluid.getFluid()
                        .getName(),
                    k -> new HashSet<>(1));
                tList.add(aRecipe);
                mRecipeFluidNameMap.add(
                    aFluid.getFluid()
                        .getName());
            }
        }
        if (aRecipe.mFluidOutputs.length != 0) {
            this.mHasFluidOutputs = true;
        }
        if (aRecipe.mSpecialItems != null) {
            this.mUsesSpecialSlot = true;
        }
        return addToItemMap(aRecipe);
    }

    public void reInit() {
        mRecipeItemMap.clear();
        for (GT_Recipe tRecipe : mRecipeList) {
            GT_OreDictUnificator.setStackArray(true, tRecipe.mInputs);
            GT_OreDictUnificator.setStackArray(true, tRecipe.mOutputs);
            addToItemMap(tRecipe);
        }
    }

    /**
     * @return if this Item is a valid Input for any for the Recipes
     */
    public boolean containsInput(ItemStack aStack) {
        return aStack != null && (mRecipeItemMap.containsKey(new GT_ItemStack(aStack))
            || mRecipeItemMap.containsKey(new GT_ItemStack(aStack, true)));
    }

    /**
     * @return if this Fluid is a valid Input for any for the Recipes
     */
    public boolean containsInput(FluidStack aFluid) {
        return aFluid != null && containsInput(aFluid.getFluid());
    }

    /**
     * @return if this Fluid is a valid Input for any for the Recipes
     */
    public boolean containsInput(Fluid aFluid) {
        return aFluid != null && mRecipeFluidNameMap.contains(aFluid.getName());
    }

    @Nullable
    public final GT_Recipe findRecipe(IHasWorldObjectAndCoords aTileEntity, boolean aNotUnificated, long aVoltage,
        FluidStack[] aFluids, ItemStack... aInputs) {
        return findRecipe(aTileEntity, null, aNotUnificated, aVoltage, aFluids, null, aInputs);
    }

    @Nullable
    public final GT_Recipe findRecipe(IHasWorldObjectAndCoords aTileEntity, boolean aNotUnificated,
        boolean aDontCheckStackSizes, long aVoltage, FluidStack[] aFluids, ItemStack... aInputs) {
        return findRecipe(aTileEntity, null, aNotUnificated, aDontCheckStackSizes, aVoltage, aFluids, null, aInputs);
    }

    @Nullable
    public final GT_Recipe findRecipe(IHasWorldObjectAndCoords aTileEntity, GT_Recipe aRecipe, boolean aNotUnificated,
        long aVoltage, FluidStack[] aFluids, ItemStack... aInputs) {
        return findRecipe(aTileEntity, aRecipe, aNotUnificated, aVoltage, aFluids, null, aInputs);
    }

    @Nullable
    public final GT_Recipe findRecipe(IHasWorldObjectAndCoords aTileEntity, GT_Recipe aRecipe, boolean aNotUnificated,
        boolean aDontCheckStackSizes, long aVoltage, FluidStack[] aFluids, ItemStack... aInputs) {
        return findRecipe(aTileEntity, aRecipe, aNotUnificated, aDontCheckStackSizes, aVoltage, aFluids, null, aInputs);
    }

    @Nullable
    public final GT_Recipe findRecipe(IHasWorldObjectAndCoords aTileEntity, GT_Recipe aRecipe, boolean aNotUnificated,
        long aVoltage, FluidStack[] aFluids, ItemStack aSpecialSlot, ItemStack... aInputs) {
        return findRecipe(aTileEntity, aRecipe, aNotUnificated, false, aVoltage, aFluids, aSpecialSlot, aInputs);
    }

    // TODO: make this final after migrating BW
    @SuppressWarnings("unused")
    @Nullable
    public GT_Recipe findRecipe(IHasWorldObjectAndCoords aTileEntity, GT_Recipe aRecipe, boolean aNotUnificated,
        boolean aDontCheckStackSizes, long aVoltage, FluidStack[] aFluids, ItemStack aSpecialSlot,
        ItemStack... aInputs) {
        FindRecipeResult result = findRecipeWithResult(
            aRecipe,
            aNotUnificated,
            aDontCheckStackSizes,
            aVoltage,
            aFluids,
            aSpecialSlot,
            aInputs);
        return result.isSuccessful() ? result.getRecipe() : null;
    }

    /**
     * finds a Recipe matching the aFluid and ItemStack Inputs.
     *
     * @param aRecipe              in case this is != null it will try to use this Recipe first when looking things
     *                             up.
     * @param aNotUnificated       if this is T the Recipe searcher will unificate the ItemStack Inputs
     * @param aDontCheckStackSizes if set to false will only return recipes that can be executed at least once with
     *                             the provided input
     * @param aVoltage             Voltage of the Machine or Long.MAX_VALUE if it has no Voltage
     * @param aFluids              the Fluid Inputs
     * @param aSpecialSlot         the content of the Special Slot, the regular Manager doesn't do anything with
     *                             this, but some custom ones do.
     * @param aInputs              the Item Inputs
     * @return Result of the recipe search
     */
    @Nonnull
    public final FindRecipeResult findRecipeWithResult(GT_Recipe aRecipe, boolean aNotUnificated,
        boolean aDontCheckStackSizes, long aVoltage, FluidStack[] aFluids, ItemStack aSpecialSlot,
        ItemStack... aInputs) {
        return findRecipeWithResult(
            aRecipe,
            recipe -> aVoltage * mAmperage >= recipe.mEUt,
            aNotUnificated,
            aDontCheckStackSizes,
            aVoltage,
            aFluids,
            aSpecialSlot,
            aInputs);
    }

    /**
     * finds a Recipe matching the aFluid and ItemStack Inputs.
     *
     * @param aRecipe              in case this is != null it will try to use this Recipe first when looking things
     *                             up.
     * @param aIsValidRecipe       predicate to help identify, if the recipe matches our machine
     * @param aNotUnificated       if this is T the Recipe searcher will unificate the ItemStack Inputs
     * @param aDontCheckStackSizes if set to false will only return recipes that can be executed at least once with
     *                             the provided input
     * @param aVoltage             Voltage of the Machine or Long.MAX_VALUE if it has no Voltage
     * @param aFluids              the Fluid Inputs
     * @param aSpecialSlot         the content of the Special Slot, the regular Manager doesn't do anything with
     *                             this, but some custom ones do.
     * @param aInputs              the Item Inputs
     * @return Result of the recipe search
     */
    @Nonnull
    public FindRecipeResult findRecipeWithResult(GT_Recipe aRecipe, Predicate<GT_Recipe> aIsValidRecipe,
        boolean aNotUnificated, boolean aDontCheckStackSizes, long aVoltage, FluidStack[] aFluids,
        ItemStack aSpecialSlot, ItemStack... aInputs) {
        // No Recipes? Well, nothing to be found then.
        if (mRecipeList.isEmpty()) return NOT_FOUND;

        // Some Recipe Classes require a certain amount of Inputs of certain kinds. Like "at least 1 Fluid + 1
        // Stack" or "at least 2 Stacks" before they start searching for Recipes.
        // This improves Performance massively, especially if people leave things like Circuits, Molds or Shapes in
        // their Machines to select Sub Recipes.
        if (GregTech_API.sPostloadFinished) {
            if (mMinimalInputFluids > 0) {
                if (aFluids == null) return NOT_FOUND;
                int tAmount = 0;
                for (FluidStack aFluid : aFluids) if (aFluid != null) tAmount++;
                if (tAmount < mMinimalInputFluids) return NOT_FOUND;
            }
            if (mMinimalInputItems > 0) {
                if (aInputs == null) return NOT_FOUND;
                int tAmount = 0;
                for (ItemStack aInput : aInputs) if (aInput != null) tAmount++;
                if (tAmount < mMinimalInputItems) return NOT_FOUND;
            }
        }

        // Unification happens here in case the Input isn't already unificated.
        if (aNotUnificated) aInputs = GT_OreDictUnificator.getStackArray(true, (Object[]) aInputs);

        // Check the Recipe which has been used last time in order to not have to search for it again, if possible.
        if (aRecipe != null) if (!aRecipe.mFakeRecipe && aRecipe.mCanBeBuffered
            && aRecipe.isRecipeInputEqual(false, aDontCheckStackSizes, aFluids, aInputs)) {
                if (!isSpecialSlotSensitive || areStacksEqualOrNull((ItemStack) aRecipe.mSpecialItems, aSpecialSlot)) {
                    if (aRecipe.mEnabled && aIsValidRecipe.test(aRecipe)) {
                        return ofSuccess(aRecipe);
                    }
                }
            }

        // Now look for the Recipes inside the Item HashMaps, but only when the Recipes usually have Items.
        if (mUsualInputCount > 0 && aInputs != null) for (ItemStack tStack : aInputs) if (tStack != null) {
            Collection<GT_Recipe> tRecipes = mRecipeItemMap.get(new GT_ItemStack(tStack));
            if (tRecipes != null) for (GT_Recipe tRecipe : tRecipes)
                if (!tRecipe.mFakeRecipe && tRecipe.isRecipeInputEqual(false, aDontCheckStackSizes, aFluids, aInputs)) {
                    if (!isSpecialSlotSensitive
                        || areStacksEqualOrNull((ItemStack) tRecipe.mSpecialItems, aSpecialSlot)) {
                        if (tRecipe.mEnabled && aIsValidRecipe.test(tRecipe)) {
                            return ofSuccess(tRecipe);
                        }
                    }
                }
            tRecipes = mRecipeItemMap.get(new GT_ItemStack(tStack, true));
            if (tRecipes != null) for (GT_Recipe tRecipe : tRecipes)
                if (!tRecipe.mFakeRecipe && tRecipe.isRecipeInputEqual(false, aDontCheckStackSizes, aFluids, aInputs)) {
                    if (!isSpecialSlotSensitive
                        || areStacksEqualOrNull((ItemStack) tRecipe.mSpecialItems, aSpecialSlot)) {
                        if (tRecipe.mEnabled && aIsValidRecipe.test(tRecipe)) {
                            return ofSuccess(tRecipe);
                        }
                    }
                }
        }

        // If the minimal Amount of Items for the Recipe is 0, then it could be a Fluid-Only Recipe, so check that
        // Map too.
        if (mMinimalInputItems == 0 && aFluids != null) for (FluidStack aFluid : aFluids) if (aFluid != null) {
            Collection<GT_Recipe> tRecipes = mRecipeFluidMap.get(
                aFluid.getFluid()
                    .getName());
            if (tRecipes != null) for (GT_Recipe tRecipe : tRecipes)
                if (!tRecipe.mFakeRecipe && tRecipe.isRecipeInputEqual(false, aDontCheckStackSizes, aFluids, aInputs)) {
                    if (!isSpecialSlotSensitive
                        || areStacksEqualOrNull((ItemStack) tRecipe.mSpecialItems, aSpecialSlot)) {
                        if (tRecipe.mEnabled && aIsValidRecipe.test(tRecipe)) {
                            return ofSuccess(tRecipe);
                        }
                    }
                }
        }

        // And nothing has been found.
        return NOT_FOUND;
    }

    protected GT_Recipe addToItemMap(GT_Recipe aRecipe) {
        for (ItemStack aStack : aRecipe.mInputs) if (aStack != null) {
            GT_ItemStack tStack = new GT_ItemStack(aStack);
            Collection<GT_Recipe> tList = mRecipeItemMap.computeIfAbsent(tStack, k -> new HashSet<>(1));
            tList.add(aRecipe);
        }
        return aRecipe;
    }

    /**
     * Whether this recipe map contains any fluid outputs.
     */
    public boolean hasFluidOutputs() {
        return mHasFluidOutputs;
    }

    /**
     * Whether this recipe map contains any fluid inputs.
     */
    public boolean hasFluidInputs() {
        return mRecipeFluidNameMap.size() != 0;
    }

    /**
     * Whether this recipe map contains special slot inputs.
     */
    public boolean usesSpecialSlot() {
        return mUsesSpecialSlot;
    }

    public int getUsualFluidInputCount() {
        return Math.max(usualFluidInputCount, hasFluidInputs() ? 1 : 0);
    }

    public int getUsualFluidOutputCount() {
        return Math.max(usualFluidOutputCount, hasFluidOutputs() ? 1 : 0);
    }

    @Nullable
    public IDrawable getOverlayForSlot(boolean isFluid, boolean isOutput, int index, boolean isSpecial) {
        byte overlayKey = (byte) ((isFluid ? 1 : 0) + (isOutput ? 2 : 0) + (index == 0 ? 4 : 0) + (isSpecial ? 8 : 0));
        if (slotOverlays.containsKey(overlayKey)) {
            return slotOverlays.get(overlayKey);
        }
        return null;
    }

    @Nullable
    public SteamTexture getOverlayForSlotSteam(boolean isFluid, boolean isOutput, int index, boolean isSpecial) {
        byte overlayKey = (byte) ((isFluid ? 1 : 0) + (isOutput ? 2 : 0) + (index == 0 ? 4 : 0) + (isSpecial ? 8 : 0));
        if (slotOverlaysSteam.containsKey(overlayKey)) {
            return slotOverlaysSteam.get(overlayKey);
        }
        return null;
    }

    @Nullable
    public SteamTexture getOverlayForSlotSteam(boolean isOutput, boolean isFirst) {
        byte overlayKey = (byte) ((isOutput ? 2 : 0) + (isFirst ? 4 : 0));
        if (slotOverlaysSteam.containsKey(overlayKey)) {
            return slotOverlaysSteam.get(overlayKey);
        }
        return null;
    }

    public UITexture getProgressBarTexture() {
        return progressBarTexture.get();
    }

    public FallbackableUITexture getProgressBarTextureRaw() {
        return progressBarTexture;
    }

    public UITexture getProgressBarTextureSteam(SteamVariant steamVariant) {
        return progressBarTextureSteam.get(steamVariant);
    }

    public int getProgressBarImageSize() {
        if (progressBarImageSize != 0) {
            return progressBarImageSize;
        }
        return switch (progressBarDirection) {
            case UP, DOWN -> progressBarSize.height;
            case CIRCULAR_CW -> Math.max(progressBarSize.width, progressBarSize.height);
            default -> progressBarSize.width;
        };
    }

    /**
     * Adds slot backgrounds, progressBar, etc.
     */
    public ModularWindow.Builder createNEITemplate(IItemHandlerModifiable itemInputsInventory,
        IItemHandlerModifiable itemOutputsInventory, IItemHandlerModifiable specialSlotInventory,
        IItemHandlerModifiable fluidInputsInventory, IItemHandlerModifiable fluidOutputsInventory,
        Supplier<Float> progressSupplier, Pos2d windowOffset) {
        ModularWindow.Builder builder = ModularWindow.builder(neiBackgroundSize)
            .setBackground(ModularUITextures.VANILLA_BACKGROUND);

        UIHelper.forEachSlots(
            (i, backgrounds, pos) -> builder.widget(
                SlotWidget.phantom(itemInputsInventory, i)
                    .setBackground(backgrounds)
                    .setPos(pos)
                    .setSize(18, 18)),
            (i, backgrounds, pos) -> builder.widget(
                SlotWidget.phantom(itemOutputsInventory, i)
                    .setBackground(backgrounds)
                    .setPos(pos)
                    .setSize(18, 18)),
            (i, backgrounds, pos) -> {
                if (usesSpecialSlot()) builder.widget(
                    SlotWidget.phantom(specialSlotInventory, 0)
                        .setBackground(backgrounds)
                        .setPos(pos)
                        .setSize(18, 18));
            },
            (i, backgrounds, pos) -> builder.widget(
                SlotWidget.phantom(fluidInputsInventory, i)
                    .setBackground(backgrounds)
                    .setPos(pos)
                    .setSize(18, 18)),
            (i, backgrounds, pos) -> builder.widget(
                SlotWidget.phantom(fluidOutputsInventory, i)
                    .setBackground(backgrounds)
                    .setPos(pos)
                    .setSize(18, 18)),
            ModularUITextures.ITEM_SLOT,
            ModularUITextures.FLUID_SLOT,
            this,
            mUsualInputCount,
            mUsualOutputCount,
            getUsualFluidInputCount(),
            getUsualFluidOutputCount(),
            SteamVariant.NONE,
            windowOffset);

        addProgressBarUI(builder, progressSupplier, windowOffset);
        addGregTechLogoUI(builder, windowOffset);

        for (Pair<IDrawable, Pair<Size, Pos2d>> specialTexture : specialTextures) {
            builder.widget(
                new DrawableWidget().setDrawable(specialTexture.getLeft())
                    .setSize(
                        specialTexture.getRight()
                            .getLeft())
                    .setPos(
                        specialTexture.getRight()
                            .getRight()
                            .add(windowOffset)));
        }

        return builder;
    }

    public void addProgressBarUI(ModularWindow.Builder builder, Supplier<Float> progressSupplier, Pos2d windowOffset) {
        builder.widget(
            new ProgressBar().setTexture(getProgressBarTexture(), 20)
                .setDirection(progressBarDirection)
                .setProgress(progressSupplier)
                .setSynced(false, false)
                .setPos(progressBarPos.add(windowOffset))
                .setSize(progressBarSize));
    }

    public void addGregTechLogoUI(ModularWindow.Builder builder, Pos2d windowOffset) {
        builder.widget(
            new DrawableWidget().setDrawable(logo)
                .setSize(logoSize)
                .setPos(logoPos.add(windowOffset)));
    }

    public void addRecipeSpecificDrawable(ModularWindow.Builder builder, Pos2d windowOffset,
        Supplier<IDrawable> supplier, Pos2d pos, Size size) {
        builder.widget(
            new DrawableWidget().setDrawable(supplier)
                .setSize(size)
                .setPos(pos.add(windowOffset)));
    }

    /**
     * Overriding this method allows custom NEI stack placement
     */
    public List<Pos2d> getItemInputPositions(int itemInputCount) {
        return UIHelper.getItemInputPositions(itemInputCount);
    }

    /**
     * Overriding this method allows custom NEI stack placement
     */
    public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        return UIHelper.getItemOutputPositions(itemOutputCount);
    }

    /**
     * Overriding this method allows custom NEI stack placement
     */
    public Pos2d getSpecialItemPosition() {
        return UIHelper.getSpecialItemPosition();
    }

    /**
     * Overriding this method allows custom NEI stack placement
     */
    public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        return UIHelper.getFluidInputPositions(fluidInputCount);
    }

    /**
     * Overriding this method allows custom NEI stack placement
     */
    public List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
        return UIHelper.getFluidOutputPositions(fluidOutputCount);
    }

    public void drawNEIDescription(NEIRecipeInfo recipeInfo) {
        drawNEIEnergyInfo(recipeInfo);
        drawNEIDurationInfo(recipeInfo);
        drawNEISpecialInfo(recipeInfo);
        drawNEIRecipeOwnerInfo(recipeInfo);
    }

    protected void drawNEIEnergyInfo(NEIRecipeInfo recipeInfo) {
        GT_Recipe recipe = recipeInfo.recipe;
        Power power = recipeInfo.power;
        if (power.getEuPerTick() > 0) {
            drawNEIText(recipeInfo, trans("152", "Total: ") + power.getTotalPowerString());

            String amperage = power.getAmperageString();
            String powerUsage = power.getPowerUsageString();
            if (amperage == null || amperage.equals("unspecified") || powerUsage.contains("(OC)")) {
                drawNEIText(recipeInfo, trans("153", "Usage: ") + powerUsage);
                if (GT_Mod.gregtechproxy.mNEIOriginalVoltage) {
                    Power originalPower = getPowerFromRecipeMap();
                    if (!(originalPower instanceof UnspecifiedEUPower)) {
                        originalPower.computePowerUsageAndDuration(recipe.mEUt, recipe.mDuration);
                        drawNEIText(recipeInfo, trans("275", "Original voltage: ") + originalPower.getVoltageString());
                    }
                }
                if (amperage != null && !amperage.equals("unspecified") && !amperage.equals("1")) {
                    drawNEIText(recipeInfo, trans("155", "Amperage: ") + amperage);
                }
            } else if (amperage.equals("1")) {
                drawNEIText(recipeInfo, trans("154", "Voltage: ") + power.getVoltageString());
            } else {
                drawNEIText(recipeInfo, trans("153", "Usage: ") + powerUsage);
                drawNEIText(recipeInfo, trans("154", "Voltage: ") + power.getVoltageString());
                drawNEIText(recipeInfo, trans("155", "Amperage: ") + amperage);
            }
        }
    }

    protected void drawNEIDurationInfo(NEIRecipeInfo recipeInfo) {
        Power power = recipeInfo.power;
        if (power.getDurationTicks() > 0) {
            String textToDraw = trans("158", "Time: ");
            if (GT_Mod.gregtechproxy.mNEIRecipeSecondMode) {
                textToDraw += power.getDurationStringSeconds();
                if (power.getDurationSeconds() <= 1.0d) {
                    textToDraw += String.format(" (%s)", power.getDurationStringTicks());
                }
            } else {
                textToDraw += power.getDurationStringTicks();
            }
            drawNEIText(recipeInfo, textToDraw);
        }
    }

    protected void drawNEISpecialInfo(NEIRecipeInfo recipeInfo) {
        String[] recipeDesc = recipeInfo.recipe.getNeiDesc();
        if (recipeDesc != null) {
            for (String s : recipeDesc) {
                drawOptionalNEIText(recipeInfo, s);
            }
        } else if (neiSpecialInfoFormatter != null) {
            drawNEITextMultipleLines(recipeInfo, neiSpecialInfoFormatter.format(recipeInfo, this::formatSpecialValue));
        } else {
            drawOptionalNEIText(recipeInfo, getNEISpecialInfo(recipeInfo.recipe.mSpecialValue));
        }
    }

    protected String getNEISpecialInfo(int specialValue) {
        if (specialValue == -100 && GT_Mod.gregtechproxy.mLowGravProcessing) {
            return trans("159", "Needs Low Gravity");
        } else if (specialValue == -200 && GT_Mod.gregtechproxy.mEnableCleanroom) {
            return trans("160", "Needs Cleanroom");
        } else if (specialValue == -201) {
            return trans("206", "Scan for Assembly Line");
        } else if (specialValue == -300 && GT_Mod.gregtechproxy.mEnableCleanroom) {
            return trans("160.1", "Needs Cleanroom & LowGrav");
        } else if (specialValue == -400) {
            return trans("216", "Deprecated Recipe");
        } else if (hasSpecialValueFormat()) {
            return formatSpecialValue(specialValue);
        }
        return null;
    }

    private boolean hasSpecialValueFormat() {
        return (isStringValid(mNEISpecialValuePre)) || (isStringValid(mNEISpecialValuePost));
    }

    protected String formatSpecialValue(int specialValue) {
        return mNEISpecialValuePre + formatNumbers((long) specialValue * mNEISpecialValueMultiplier)
            + mNEISpecialValuePost;
    }

    protected void drawNEIRecipeOwnerInfo(NEIRecipeInfo recipeInfo) {
        GT_Recipe recipe = recipeInfo.recipe;
        if (GT_Mod.gregtechproxy.mNEIRecipeOwner) {
            if (recipe.owners.size() > 1) {
                drawNEIText(
                    recipeInfo,
                    EnumChatFormatting.ITALIC + trans("273", "Original Recipe by: ")
                        + recipe.owners.get(0)
                            .getName());
                for (int i = 1; i < recipe.owners.size(); i++) {
                    drawNEIText(
                        recipeInfo,
                        EnumChatFormatting.ITALIC + trans("274", "Modified by: ")
                            + recipe.owners.get(i)
                                .getName());
                }
            } else if (recipe.owners.size() > 0) {
                drawNEIText(
                    recipeInfo,
                    EnumChatFormatting.ITALIC + trans("272", "Recipe by: ")
                        + recipe.owners.get(0)
                            .getName());
            }
        }
        if (GT_Mod.gregtechproxy.mNEIRecipeOwnerStackTrace && recipe.stackTraces != null
            && !recipe.stackTraces.isEmpty()) {
            drawNEIText(recipeInfo, "stackTrace:");
            // todo: good way to show all stacktraces
            for (StackTraceElement stackTrace : recipe.stackTraces.get(0)) {
                drawNEIText(recipeInfo, stackTrace.toString());
            }
        }
    }

    protected void drawNEIText(NEIRecipeInfo recipeInfo, String text) {
        drawNEIText(recipeInfo, text, 10);
    }

    /**
     * Draws text on NEI recipe.
     *
     * @param yShift y position to shift after this text
     */
    @SuppressWarnings("SameParameterValue")
    protected void drawNEIText(NEIRecipeInfo recipeInfo, String text, int yShift) {
        drawNEIText(recipeInfo, text, 10, yShift);
    }

    /**
     * Draws text on NEI recipe.
     *
     * @param xStart x position to start drawing
     * @param yShift y position to shift after this text
     */
    @SuppressWarnings("SameParameterValue")
    protected void drawNEIText(NEIRecipeInfo recipeInfo, String text, int xStart, int yShift) {
        net.minecraft.client.Minecraft.getMinecraft().fontRenderer
            .drawString(text, xStart, recipeInfo.yPos, neiTextColorOverride != -1 ? neiTextColorOverride : 0x000000);
        recipeInfo.yPos += yShift;
    }

    protected void drawOptionalNEIText(NEIRecipeInfo recipeInfo, String text) {
        if (isStringValid(text) && !text.equals("unspecified")) {
            drawNEIText(recipeInfo, text, 10);
        }
    }

    protected void drawNEITextMultipleLines(NEIRecipeInfo recipeInfo, List<String> texts) {
        for (String text : texts) {
            drawNEIText(recipeInfo, text, 10);
        }
    }

    public List<String> handleNEIItemTooltip(ItemStack stack, List<String> currentTip,
        GT_NEI_DefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        for (PositionedStack pStack : neiCachedRecipe.mInputs) {
            if (stack == pStack.item) {
                if (pStack instanceof GT_NEI_DefaultHandler.FixedPositionedStack) {
                    currentTip = handleNEIItemInputTooltip(
                        currentTip,
                        (GT_NEI_DefaultHandler.FixedPositionedStack) pStack);
                }
                break;
            }
        }
        for (PositionedStack pStack : neiCachedRecipe.mOutputs) {
            if (stack == pStack.item) {
                if (pStack instanceof GT_NEI_DefaultHandler.FixedPositionedStack) {
                    currentTip = handleNEIItemOutputTooltip(
                        currentTip,
                        (GT_NEI_DefaultHandler.FixedPositionedStack) pStack);
                }
                break;
            }
        }
        return currentTip;
    }

    protected List<String> handleNEIItemInputTooltip(List<String> currentTip,
        GT_NEI_DefaultHandler.FixedPositionedStack pStack) {
        if (pStack.isNotConsumed()) {
            currentTip.add(GRAY + trans("151", "Does not get consumed in the process"));
        }
        return currentTip;
    }

    protected List<String> handleNEIItemOutputTooltip(List<String> currentTip,
        GT_NEI_DefaultHandler.FixedPositionedStack pStack) {
        if (pStack.isChanceBased()) {
            currentTip.add(GRAY + trans("150", "Chance: ") + pStack.getChanceText());
        }
        return currentTip;
    }

    public void drawNEIOverlays(GT_NEI_DefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        for (PositionedStack stack : neiCachedRecipe.mInputs) {
            if (stack instanceof GT_NEI_DefaultHandler.FixedPositionedStack) {
                drawNEIOverlayForInput((GT_NEI_DefaultHandler.FixedPositionedStack) stack);
            }
        }
        for (PositionedStack stack : neiCachedRecipe.mOutputs) {
            if (stack instanceof GT_NEI_DefaultHandler.FixedPositionedStack) {
                drawNEIOverlayForOutput((GT_NEI_DefaultHandler.FixedPositionedStack) stack);
            }
        }
    }

    protected void drawNEIOverlayForInput(GT_NEI_DefaultHandler.FixedPositionedStack stack) {
        if (stack.isNotConsumed()) {
            drawNEIOverlayText("NC", stack);
        }
    }

    protected void drawNEIOverlayForOutput(GT_NEI_DefaultHandler.FixedPositionedStack stack) {
        if (stack.isChanceBased()) {
            drawNEIOverlayText(stack.getChanceText(), stack);
        }
    }

    @SuppressWarnings("SameParameterValue")
    protected void drawNEIOverlayText(String text, PositionedStack stack, int color, float scale, boolean shadow,
        Alignment alignment) {
        FontRenderer fontRenderer = net.minecraft.client.Minecraft.getMinecraft().fontRenderer;
        int width = fontRenderer.getStringWidth(text);
        int x = (int) ((stack.relx + 8 + 8 * alignment.x) / scale) - (width / 2 * (alignment.x + 1));
        int y = (int) ((stack.rely + 8 + 8 * alignment.y) / scale) - (fontRenderer.FONT_HEIGHT / 2 * (alignment.y + 1))
            - (alignment.y - 1) / 2;

        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1);
        fontRenderer.drawString(text, x, y, color, shadow);
        GlStateManager.popMatrix();
    }

    protected void drawNEIOverlayText(String text, PositionedStack stack) {
        drawNEIOverlayText(
            text,
            stack,
            colorOverride.getTextColorOrDefault("nei_overlay_yellow", 0xFDD835),
            0.5f,
            false,
            Alignment.TopLeft);
    }

    public void updateNEITextColorOverride() {
        neiTextColorOverride = colorOverride.getTextColorOrDefault("nei", -1);
    }

    public Power getPowerFromRecipeMap() {
        // By default, assume generic EU LV power with no overclocks
        Power power;
        if (mShowVoltageAmperageInNEI) {
            power = new EUPower((byte) 1, mAmperage);
        } else {
            power = new UnspecifiedEUPower((byte) 1, mAmperage);
        }
        return power;
    }

    public void addRecipe(Object o, FluidStack[] fluidInputArray, FluidStack[] fluidOutputArray) {}
}
