package tectech.thing.metaTileEntity.multi.godforge_modules;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTUtility.formatNumbers;
import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;
import static gregtech.common.misc.WirelessNetworkManager.getUserEU;
import static net.minecraft.util.EnumChatFormatting.GREEN;
import static net.minecraft.util.EnumChatFormatting.RED;
import static net.minecraft.util.EnumChatFormatting.RESET;
import static net.minecraft.util.EnumChatFormatting.YELLOW;
import static net.minecraft.util.StatCollector.translateToLocal;
import static tectech.loader.recipe.Godforge.exoticModuleMagmatterItemMap;
import static tectech.loader.recipe.Godforge.exoticModulePlasmaFluidMap;
import static tectech.loader.recipe.Godforge.exoticModulePlasmaItemMap;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.fluids.FluidTanksHandler;
import com.gtnewhorizons.modularui.api.fluids.IFluidTanksHandler;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.api.widget.IWidgetBuilder;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.FluidSlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.TierEU;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBuilder;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import tectech.recipe.TecTechRecipeMaps;
import tectech.thing.gui.TecTechUITextures;
import tectech.util.CommonValues;
import tectech.util.GodforgeMath;

public class MTEExoticModule extends MTEBaseModule {

    private int numberOfFluids = 0;
    private int numberOfItems = 0;
    private long ticker = 0;
    private long EUt = 0;
    private long actualParallel = 0;
    private boolean recipeInProgress = false;
    private boolean recipeRegenerated = false;
    private boolean magmatterMode = false;
    private FluidStack[] randomizedFluidInput = new FluidStack[] {};
    private ItemStack[] randomizedItemInput = new ItemStack[] {};
    List<FluidStack> inputPlasmas = new ArrayList<>();
    private GTRecipe plasmaRecipe = null;
    private BigInteger powerForRecipe = BigInteger.ZERO;
    private static RecipeMap<RecipeMapBackend> tempRecipeMap = RecipeMapBuilder.of("godforgeExoticTempRecipeMap")
        .maxIO(0, 0, 7, 2)
        .disableRegisterNEI()
        .build();
    private static final int NUMBER_OF_INPUTS = 7;
    private static final int INPUT_LIST_WINDOW_ID = 10;

    public MTEExoticModule(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEExoticModule(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEExoticModule(mName);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected Stream<GTRecipe> findRecipeMatches(@Nullable RecipeMap<?> map) {
                if (!recipeInProgress) {

                    if (magmatterMode) {
                        plasmaRecipe = generateMagmatterRecipe();
                    } else {
                        plasmaRecipe = generateQuarkGluonRecipe();
                    }

                    tempRecipeMap.add(plasmaRecipe);
                }
                return tempRecipeMap.getAllRecipes()
                    .parallelStream();
            }

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@Nonnull GTRecipe recipe) {
                if (!recipeInProgress || recipeRegenerated) {
                    powerForRecipe = BigInteger.valueOf(getProcessingVoltage())
                        .multiply(BigInteger.valueOf(recipe.mDuration * actualParallel));
                    if (getUserEU(userUUID).compareTo(powerForRecipe) < 0) {
                        tempRecipeMap.getBackend()
                            .clearRecipes();
                        return CheckRecipeResultRegistry.insufficientStartupPower(powerForRecipe);
                    }

                    if (numberOfFluids != 0) {
                        for (FluidStack fluidStack : randomizedFluidInput) {
                            dumpFluid(
                                mOutputHatches,
                                new FluidStack(fluidStack.getFluid(), fluidStack.amount / 1000),
                                false);
                        }
                    }

                    if (numberOfItems != 0) {
                        for (ItemStack itemStack : randomizedItemInput) {
                            addOutput(itemStack);
                        }
                    }

                    recipeInProgress = true;
                    recipeRegenerated = false;
                }
                if (new HashSet<>(Arrays.asList(inputFluids)).containsAll(inputPlasmas)) {
                    return CheckRecipeResultRegistry.SUCCESSFUL;
                }
                return SimpleCheckRecipeResult.ofFailure("waiting_for_inputs");
            }

            @NotNull
            @Override
            protected CheckRecipeResult onRecipeStart(@Nonnull GTRecipe recipe) {
                EUt = calculatedEut;
                powerForRecipe = BigInteger.valueOf(EUt)
                    .multiply(BigInteger.valueOf(duration * actualParallel));

                if (!addEUToGlobalEnergyMap(userUUID, powerForRecipe.negate())) {
                    return CheckRecipeResultRegistry.insufficientStartupPower(powerForRecipe);
                }

                addToPowerTally(powerForRecipe);
                addToRecipeTally(calculatedParallels);
                setCalculatedEut(0);
                tempRecipeMap.getBackend()
                    .clearRecipes();
                recipeInProgress = false;
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @Nonnull
            @Override
            protected OverclockCalculator createOverclockCalculator(@Nonnull GTRecipe recipe) {
                return super.createOverclockCalculator(recipe).setEUt(getProcessingVoltage())
                    .setDurationDecreasePerOC(getOverclockTimeFactor());
            }

        };
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(Long.MAX_VALUE);
        logic.setAvailableAmperage(Integer.MAX_VALUE);
        logic.setAmperageOC(false);
        logic.setSpeedBonus(getSpeedBonus());
        logic.setEuModifier(getEnergyDiscount());
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return TecTechRecipeMaps.godforgeExoticMatterRecipes;
    }

    private GTRecipe generateQuarkGluonRecipe() {
        actualParallel = getMaxParallel();
        tempRecipeMap.getBackend()
            .clearRecipes();
        numberOfFluids = GodforgeMath.getRandomIntInRange(0, NUMBER_OF_INPUTS);
        numberOfItems = NUMBER_OF_INPUTS - numberOfFluids;
        randomizedFluidInput = getRandomFluidInputs(exoticModulePlasmaFluidMap, numberOfFluids);
        randomizedItemInput = getRandomItemInputs(exoticModulePlasmaItemMap, numberOfItems);

        if (numberOfFluids != 0) {
            for (FluidStack fluidStack : randomizedFluidInput) {
                fluidStack.amount = 1000 * GodforgeMath.getRandomIntInRange(1, 64);
            }
        }

        if (numberOfItems != 0) {
            for (ItemStack itemStack : randomizedItemInput) {
                itemStack.stackSize = 9 * GodforgeMath.getRandomIntInRange(1, 7);
            }
        }

        inputPlasmas = new ArrayList<>(Arrays.asList(convertItemToPlasma(randomizedItemInput, 1)));
        inputPlasmas.addAll(Arrays.asList(convertFluidToPlasma(randomizedFluidInput, 1)));

        return new GTRecipe(
            false,
            null,
            null,
            null,
            null,
            inputPlasmas.toArray(new FluidStack[0]),
            new FluidStack[] { MaterialsUEVplus.QuarkGluonPlasma.getFluid(1000 * actualParallel) },
            10 * SECONDS,
            (int) TierEU.RECIPE_MAX,
            0);
    }

    private GTRecipe generateMagmatterRecipe() {
        actualParallel = getMaxParallel();
        tempRecipeMap.getBackend()
            .clearRecipes();
        randomizedItemInput = getRandomItemInputs(exoticModuleMagmatterItemMap, 1);
        numberOfItems = 1;
        numberOfFluids = 2;
        int timeAmount = GodforgeMath.getRandomIntInRange(1, 50);
        int spaceAmount = GodforgeMath.getRandomIntInRange(51, 100);
        randomizedFluidInput = new FluidStack[] { MaterialsUEVplus.Time.getMolten(timeAmount * 1000L),
            MaterialsUEVplus.Space.getMolten(spaceAmount * 1000L) };
        inputPlasmas = new ArrayList<>(
            Arrays.asList(convertItemToPlasma(randomizedItemInput, spaceAmount - timeAmount)));
        inputPlasmas.add(MaterialsUEVplus.Time.getMolten(timeAmount));
        inputPlasmas.add(MaterialsUEVplus.Space.getMolten(spaceAmount));

        return new GTRecipe(
            false,
            null,
            null,
            null,
            null,
            inputPlasmas.toArray(new FluidStack[0]),
            new FluidStack[] { MaterialsUEVplus.MagMatter.getMolten(576 * actualParallel) },
            10 * SECONDS,
            (int) TierEU.RECIPE_MAX,
            0);
    }

    private FluidStack[] getRandomFluidInputs(HashMap<FluidStack, Integer> fluidMap, int numberOfFluids) {
        int cumulativeWeight = 0;

        List<Map.Entry<FluidStack, Integer>> fluidEntryList = new ArrayList<>(fluidMap.entrySet());

        List<Integer> cumulativeWeights = new ArrayList<>();
        for (Map.Entry<FluidStack, Integer> entry : fluidEntryList) {
            cumulativeWeight += entry.getValue();
            cumulativeWeights.add(cumulativeWeight);
        }

        List<FluidStack> pickedFluids = new ArrayList<>();
        for (int i = 0; i < numberOfFluids; i++) {
            int randomWeight = GodforgeMath.getRandomIntInRange(1, cumulativeWeight);
            // Find the corresponding FluidStack based on randomWeight
            for (int j = 0; j < cumulativeWeights.size(); j++) {
                if (randomWeight <= cumulativeWeights.get(j)) {
                    FluidStack pickedFluid = fluidEntryList.get(j)
                        .getKey();
                    // prevent duplicates
                    if (pickedFluids.contains(pickedFluid)) {
                        i--;
                        break;
                    }
                    pickedFluids.add(pickedFluid);
                    break;
                }
            }
        }

        return pickedFluids.toArray(new FluidStack[0]);

    }

    private ItemStack[] getRandomItemInputs(HashMap<ItemStack, Integer> itemMap, int numberOfItems) {
        int cumulativeWeight = 0;

        List<Map.Entry<ItemStack, Integer>> itemEntryList = new ArrayList<>(itemMap.entrySet());

        List<Integer> cumulativeWeights = new ArrayList<>();
        for (Map.Entry<ItemStack, Integer> entry : itemEntryList) {
            cumulativeWeight += entry.getValue();
            cumulativeWeights.add(cumulativeWeight);
        }

        List<ItemStack> pickedItems = new ArrayList<>();
        for (int i = 0; i < numberOfItems; i++) {
            int randomWeight = GodforgeMath.getRandomIntInRange(1, cumulativeWeight);
            // Find the corresponding ItemStack based on randomWeight
            for (int j = 0; j < cumulativeWeights.size(); j++) {
                if (randomWeight <= cumulativeWeights.get(j)) {
                    ItemStack pickedItem = itemEntryList.get(j)
                        .getKey();
                    // prevent duplicates
                    if (pickedItems.contains(pickedItem)) {
                        i--;
                        break;
                    }
                    pickedItems.add(pickedItem);
                    break;
                }
            }
        }
        return pickedItems.toArray(new ItemStack[0]);

    }

    private FluidStack[] convertItemToPlasma(ItemStack[] items, long multiplier) {
        List<FluidStack> plasmas = new ArrayList<>();

        for (ItemStack itemStack : items) {
            String dict = OreDictionary.getOreName(OreDictionary.getOreIDs(itemStack)[0]);
            // substring 8 because dustTiny is 8 characters long and there is no other possible oreDict
            String strippedOreDict = dict.substring(8);
            plasmas.add(
                FluidRegistry.getFluidStack(
                    "plasma." + strippedOreDict.toLowerCase(),
                    (int) (INGOTS * multiplier * itemStack.stackSize)));
        }

        return plasmas.toArray(new FluidStack[0]);
    }

    private FluidStack[] convertFluidToPlasma(FluidStack[] fluids, long multiplier) {
        List<FluidStack> plasmas = new ArrayList<>();

        for (FluidStack fluidStack : fluids) {
            String[] fluidName = fluidStack.getUnlocalizedName()
                .split("\\.");
            plasmas.add(
                FluidRegistry.getFluidStack(
                    "plasma." + fluidName[fluidName.length - 1],
                    (int) (multiplier * fluidStack.amount)));
        }

        return plasmas.toArray(new FluidStack[0]);
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    @Override
    public boolean supportsBatchMode() {
        return false;
    }

    @Override
    public void saveNBTData(NBTTagCompound NBT) {

        NBT.setBoolean("recipeInProgress", recipeInProgress);
        NBT.setBoolean("magmatterMode", magmatterMode);
        NBT.setLong("maxParallel", actualParallel);

        // Store damage values/stack sizes of input plasmas
        NBTTagCompound fluidStackListNBTTag = new NBTTagCompound();
        fluidStackListNBTTag.setLong("numberOfPlasmas", inputPlasmas.size());

        int indexFluids = 0;
        for (FluidStack fluidStack : inputPlasmas) {
            // Save fluid amount to NBT
            fluidStackListNBTTag.setLong(indexFluids + "fluidAmount", fluidStack.amount);

            // Save FluidStack to NBT
            NBT.setTag(indexFluids + "fluidStack", fluidStack.writeToNBT(new NBTTagCompound()));

            indexFluids++;
        }

        NBT.setTag("inputPlasmas", fluidStackListNBTTag);
        super.saveNBTData(NBT);
    }

    @Override
    public void loadNBTData(final NBTTagCompound NBT) {

        recipeInProgress = NBT.getBoolean("recipeInProgress");
        magmatterMode = NBT.getBoolean("magmatterMode");
        actualParallel = NBT.getLong("maxParallel");

        // Load damage values/fluid amounts of input plasmas and convert back to fluids
        NBTTagCompound tempFluidTag = NBT.getCompoundTag("inputPlasmas");

        // Iterate over all stored fluids
        for (int indexFluids = 0; indexFluids < tempFluidTag.getLong("numberOfPlasmas"); indexFluids++) {

            // Load fluid amount from NBT
            int fluidAmount = tempFluidTag.getInteger(indexFluids + "fluidAmount");

            // Load FluidStack from NBT
            FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(NBT.getCompoundTag(indexFluids + "fluidStack"));

            inputPlasmas.add(new FluidStack(fluidStack, fluidAmount));
        }
        FluidStack outputFluid = MaterialsUEVplus.QuarkGluonPlasma.getFluid(1000L * actualParallel);

        if (magmatterMode) {
            outputFluid = MaterialsUEVplus.MagMatter.getMolten(576L * actualParallel);
        }

        tempRecipeMap.add(
            new GTRecipe(
                false,
                null,
                null,
                null,
                null,
                inputPlasmas.toArray(new FluidStack[0]),
                new FluidStack[] { outputFluid },
                10 * SECONDS,
                (int) TierEU.RECIPE_MAX,
                0));

        super.loadNBTData(NBT);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        ticker++;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
            new DrawableWidget().setPos(8, 69)
                .setSize(16, 16)
                .addTooltip(translateToLocal("fog.button.exoticinputs.tooltip"))
                .setTooltipShowUpDelay(TOOLTIP_DELAY));
        super.addUIWidgets(builder, buildContext);
        buildContext.addSyncedWindow(INPUT_LIST_WINDOW_ID, this::createInputListWindow);
        builder.widget(magmatterSwitch(builder));
        builder.widget(createExpectedInputsButton());
        builder.widget(
            new DrawableWidget().setDrawable(ModularUITextures.ICON_INFO)
                .setPos(8, 69)
                .setSize(16, 16));

    }

    protected ModularWindow createInputListWindow(final EntityPlayer player) {
        final int WIDTH = 100;
        final int HEIGHT = 60;
        final int PARENT_WIDTH = getGUIWidth();
        final int PARENT_HEIGHT = getGUIHeight();
        final Pos2d[] slotPositions = new Pos2d[] { new Pos2d(23, 35), new Pos2d(41, 35), new Pos2d(59, 35),
            new Pos2d(14, 17), new Pos2d(32, 17), new Pos2d(50, 17), new Pos2d(68, 17) };
        ModularWindow.Builder builder = ModularWindow.builder(WIDTH, HEIGHT);
        builder.setBackground(GTUITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.setGuiTint(getGUIColorization());
        builder.setDraggable(true);
        builder.setPos(
            (size, window) -> Alignment.Center.getAlignedPos(size, new Size(PARENT_WIDTH, PARENT_HEIGHT))
                .add(Alignment.TopLeft.getAlignedPos(new Size(PARENT_WIDTH, PARENT_HEIGHT), new Size(WIDTH, HEIGHT)))
                .subtract(100, -47));

        IFluidTanksHandler tanksHandler = new FluidTanksHandler(7, 128000);
        for (int i = 0; i < 7; i++) {
            if (i < inputPlasmas.size()) {
                FluidStack plasma = inputPlasmas.get(i);
                tanksHandler.setFluidInTank(i, plasma.getFluid(), plasma.amount);
            }
            builder.widget(
                new DrawableWidget().setDrawable(ModularUITextures.FLUID_SLOT)
                    .setSize(18, 18)
                    .setPos(slotPositions[i]))
                .widget(
                    new FluidSlotWidget(tanksHandler, i).setInteraction(false, false)
                        .setSize(18, 18)
                        .setPos(slotPositions[i])
                        .attachSyncer(
                            new FakeSyncWidget.BooleanSyncer(() -> recipeInProgress, val -> recipeInProgress = val),
                            builder,
                            (widget, val) -> widget.checkNeedsRebuild()));
        }

        builder.widget(
            new TextWidget(translateToLocal("gt.blockmachines.multimachine.FOG.expectedinputs"))
                .setDefaultColor(EnumChatFormatting.BLACK)
                .setTextAlignment(Alignment.Center)
                .setSize(100, 9)
                .setPos(0, 6));

        builder.widget(new ButtonWidget().setOnClick((clickData, widget) -> {
            if (!widget.isClient() && ticker > 1200) {

                if (magmatterMode) {
                    plasmaRecipe = generateMagmatterRecipe();
                } else {
                    plasmaRecipe = generateQuarkGluonRecipe();
                }
                recipeRegenerated = true;
                tempRecipeMap.add(plasmaRecipe);

                for (int i = 0; i < 7; i++) {
                    if (i < inputPlasmas.size()) {
                        FluidStack plasma = inputPlasmas.get(i);
                        tanksHandler.setFluidInTank(i, plasma.getFluid(), plasma.amount);
                    }
                }
                ticker = 0;
                widget.getContext()
                    .closeWindow(INPUT_LIST_WINDOW_ID);
                widget.getContext()
                    .openSyncedWindow(INPUT_LIST_WINDOW_ID);
            }
        })
            .setPlayClickSound(true)
            .setBackground(TecTechUITextures.OVERLAY_CYCLIC_BLUE)
            .dynamicTooltip(this::refreshTooltip)
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setSize(16, 16)
            .setPos(5, 37)
            .attachSyncer(
                new FakeSyncWidget.LongSyncer(() -> ticker, val -> ticker = val),
                builder,
                (widget, val) -> widget.notifyTooltipChange()));

        return builder.build();
    }

    private Widget createExpectedInputsButton() {
        return new ButtonWidget().setOnClick((clickData, widget) -> {
            if (!widget.isClient()) {
                widget.getContext()
                    .openSyncedWindow(INPUT_LIST_WINDOW_ID);
            }
        })
            .setPlayClickSound(true)
            .setSize(16, 16)
            .setPos(8, 69);
    }

    protected ButtonWidget magmatterSwitch(IWidgetBuilder<?> builder) {
        Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
            if (isMagmatterCapable) {
                magmatterMode = !magmatterMode;
            }
        })
            .setPlayClickSound(isMagmatterCapable)
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                if (isMagmatterModeOn()) {
                    ret.add(GTUITextures.BUTTON_STANDARD_PRESSED);
                    if (isMagmatterCapable) {
                        ret.add(GTUITextures.OVERLAY_BUTTON_CHECKMARK);
                    } else {
                        ret.add(GTUITextures.OVERLAY_BUTTON_DISABLE);
                    }
                } else {
                    ret.add(GTUITextures.BUTTON_STANDARD);
                    if (isMagmatterCapable) {
                        ret.add(GTUITextures.OVERLAY_BUTTON_CROSS);
                    } else {
                        ret.add(GTUITextures.OVERLAY_BUTTON_DISABLE);
                    }
                }
                if (!isMagmatterCapable) {
                    ret.add(GTUITextures.OVERLAY_BUTTON_DISABLE);
                }
                return ret.toArray(new IDrawable[0]);
            })
            .attachSyncer(new FakeSyncWidget.BooleanSyncer(this::isMagmatterModeOn, this::setMagmatterMode), builder)
            .addTooltip(translateToLocal("fog.button.magmattermode.tooltip.01"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(174, 91)
            .setSize(16, 16)
            .attachSyncer(
                new FakeSyncWidget.BooleanSyncer(() -> isMagmatterCapable, this::setMagmatterCapable),
                builder);
        if (!isMagmatterCapable) {
            button.addTooltip(EnumChatFormatting.GRAY + translateToLocal("fog.button.magmattermode.tooltip.02"));
        }
        return (ButtonWidget) button;
    }

    private List<String> refreshTooltip() {
        if (ticker > 1200) {
            return ImmutableList.of(translateToLocal("fog.button.reciperefresh.tooltip"));
        }

        return ImmutableList.of(
            translateToLocal("fog.button.refreshtimer.tooltip") + " "
                + (int) Math.ceil((1200 - ticker) / 20d)
                + " "
                + translateToLocal("fog.button.seconds"));
    }

    public boolean isMagmatterModeOn() {
        return magmatterMode;
    }

    private void setMagmatterMode(boolean enabled) {
        magmatterMode = enabled;
    }

    @Override
    public MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Exotic Matter Producer")
            .addInfo("Controller block for the Heliofusion Exoticizer, a module of the Godforge.")
            .addInfo("Must be part of a Godforge to function.")
            .addInfo("Used for ultra high temperature matter degeneration.")
            .addInfo(TOOLTIP_BAR)
            .addInfo("The fourth and final module of the Godforge, this module breaks apart the very")
            .addInfo("building blocks of matter, producing exotic mixtures in the process. Quark-Gluon Plasma")
            .addInfo("can be manufactured right away, but production of Magnetic Monopole Matter (Magmatter)")
            .addInfo("requires a fully upgraded Godforge.")
            .addInfo("This module is specialized towards acquisition of unique materials.")
            .addInfo(TOOLTIP_BAR)
            .beginStructureBlock(7, 7, 13, false)
            .addStructureInfo("The structure is too complex! See schematic for details.")
            .addStructureInfo(
                EnumChatFormatting.GOLD + "20"
                    + EnumChatFormatting.GRAY
                    + " Singularity Reinforced Stellar Shielding Casing")
            .addStructureInfo(
                EnumChatFormatting.GOLD + "20"
                    + EnumChatFormatting.GRAY
                    + " Boundless Gravitationally Severed Structure Casing")
            .addStructureInfo(
                EnumChatFormatting.GOLD + "5" + EnumChatFormatting.GRAY + " Harmonic Phonon Transmission Conduit")
            .addStructureInfo(
                EnumChatFormatting.GOLD + "5" + EnumChatFormatting.GRAY + " Celestial Matter Guidance Casing")
            .addStructureInfo(EnumChatFormatting.GOLD + "1" + EnumChatFormatting.GRAY + " Stellar Energy Siphon Casing")
            .addStructureInfo("Requires " + EnumChatFormatting.GOLD + 1 + EnumChatFormatting.GRAY + " Output Hatch")
            .addStructureInfo("Requires " + EnumChatFormatting.GOLD + 1 + EnumChatFormatting.GRAY + " Output Bus")
            .toolTipFinisher(CommonValues.GODFORGE_MARK);
        return tt;
    }

    @Override
    public String[] getInfoData() {
        ArrayList<String> str = new ArrayList<>();
        str.add(
            "Progress: " + GREEN
                + formatNumbers(mProgresstime / 20)
                + RESET
                + " s / "
                + YELLOW
                + formatNumbers(mMaxProgresstime / 20)
                + RESET
                + " s");
        str.add(
            "Currently using: " + RED
                + (getBaseMetaTileEntity().isActive() ? formatNumbers(EUt * actualParallel) : "0")
                + RESET
                + " EU/t");
        str.add(YELLOW + "Max Parallel: " + RESET + formatNumbers(getMaxParallel()));
        str.add(
            YELLOW + "Current Parallel: "
                + RESET
                + (getBaseMetaTileEntity().isActive() ? formatNumbers(getMaxParallel()) : "0"));
        str.add(YELLOW + "Recipe time multiplier: " + RESET + formatNumbers(getSpeedBonus()));
        str.add(YELLOW + "Energy multiplier: " + RESET + formatNumbers(getEnergyDiscount()));
        str.add(YELLOW + "Recipe time divisor per non-perfect OC: " + RESET + formatNumbers(getOverclockTimeFactor()));
        return str.toArray(new String[0]);
    }

}
