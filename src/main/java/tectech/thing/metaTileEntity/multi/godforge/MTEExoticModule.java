package tectech.thing.metaTileEntity.multi.godforge;

import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.ItemDrawable;
import com.gtnewhorizons.modularui.api.fluids.FluidTanksHandler;
import com.gtnewhorizons.modularui.api.fluids.IFluidTanksHandler;
import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
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
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTStreamUtil;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import tectech.TecTech;
import tectech.recipe.TecTechRecipeMaps;
import tectech.thing.CustomItemList;
import tectech.thing.gui.TecTechUITextures;
import tectech.thing.metaTileEntity.multi.godforge.util.ForgeOfGodsUI;
import tectech.thing.metaTileEntity.multi.godforge.util.GodforgeMath;

public class MTEExoticModule extends MTEBaseModule {

    private int numberOfFluids = 0;
    private int numberOfItems = 0;
    private long ticker = 0;
    private long EUt = 0;
    private long actualParallel = 0;
    private boolean recipeInProgress = false;
    private boolean recipeRegenerated = false;
    private boolean magmatterMode = false;
    private FluidStack[] randomizedFluidInput = GTValues.emptyFluidStackArray;
    private ItemStack[] randomizedItemInput = GTValues.emptyItemStackArray;
    private GTRecipe plasmaRecipe = null;
    private BigInteger powerForRecipe = BigInteger.ZERO;
    private static final int NUMBER_OF_INPUTS = 7;
    private static final int INPUT_LIST_WINDOW_ID = 11;
    private static final int POSSIBLE_INPUTS_WINDOW_ID = 12;

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
                }
                return GTStreamUtil.ofNullable(plasmaRecipe);
            }

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                if (!recipeInProgress || recipeRegenerated) {
                    powerForRecipe = BigInteger.valueOf(getSafeProcessingVoltage())
                        .multiply(BigInteger.valueOf(recipe.mDuration * actualParallel));
                    if (getUserEU(userUUID).compareTo(powerForRecipe) < 0) {
                        plasmaRecipe = null;
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

                for (FluidStack stack : recipe.mFluidInputs) {
                    if (!ArrayUtils.contains(inputFluids, stack)
                        || inputFluids[ArrayUtils.indexOf(inputFluids, stack)].amount != stack.amount) {
                        return SimpleCheckRecipeResult.ofFailure("waiting_for_inputs");
                    }
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @NotNull
            @Override
            protected CheckRecipeResult onRecipeStart(@NotNull GTRecipe recipe) {
                EUt = calculatedEut;
                powerForRecipe = BigInteger.valueOf(EUt)
                    .multiply(BigInteger.valueOf(duration * actualParallel));

                if (!addEUToGlobalEnergyMap(userUUID, powerForRecipe.negate())) {
                    return CheckRecipeResultRegistry.insufficientStartupPower(powerForRecipe);
                }

                addToPowerTally(powerForRecipe);
                addToRecipeTally(calculatedParallels);
                overwriteCalculatedEut(0);
                plasmaRecipe = null;
                recipeInProgress = false;
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @NotNull
            @Override
            protected OverclockCalculator createOverclockCalculator(@NotNull GTRecipe recipe) {
                return super.createOverclockCalculator(recipe).setEUt(getSafeProcessingVoltage())
                    .setDurationDecreasePerOC(getOverclockTimeFactor());
            }

        };
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(Long.MAX_VALUE);
        logic.setAvailableAmperage(Integer.MAX_VALUE);
        logic.setAmperageOC(false);
        logic.setUnlimitedTierSkips();
        logic.setSpeedBonus(getSpeedBonus());
        logic.setEuModifier(getEnergyDiscount());
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return TecTechRecipeMaps.godforgeExoticMatterRecipes;
    }

    private GTRecipe generateQuarkGluonRecipe() {
        actualParallel = getActualParallel();
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
                itemStack.stackSize = GodforgeMath.getRandomIntInRange(1, 7);
            }
        }

        return new GTRecipe(
            false,
            null,
            null,
            null,
            null,
            ArrayUtils
                .addAll(convertItemToPlasma(randomizedItemInput, 9), convertFluidToPlasma(randomizedFluidInput, 1)),
            new FluidStack[] { MaterialsUEVplus.QuarkGluonPlasma.getFluid(1000 * actualParallel) },
            10 * SECONDS,
            (int) TierEU.RECIPE_MAX,
            0);
    }

    private GTRecipe generateMagmatterRecipe() {
        actualParallel = getActualParallel();
        randomizedItemInput = getRandomItemInputs(exoticModuleMagmatterItemMap, 1);
        numberOfItems = 1;
        numberOfFluids = 2;
        int timeAmount = GodforgeMath.getRandomIntInRange(1, 50);
        int spaceAmount = GodforgeMath.getRandomIntInRange(51, 100);
        randomizedFluidInput = new FluidStack[] { MaterialsUEVplus.Time.getMolten(timeAmount * 1000L),
            MaterialsUEVplus.Space.getMolten(spaceAmount * 1000L) };

        return new GTRecipe(
            false,
            null,
            null,
            null,
            null,
            ArrayUtils.addAll(
                convertItemToPlasma(randomizedItemInput, spaceAmount - timeAmount),
                MaterialsUEVplus.Time.getMolten(timeAmount),
                MaterialsUEVplus.Space.getMolten(spaceAmount)),
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
            // substring 4 because dust is 4 characters long and there is no other possible oreDict
            String strippedOreDict = dict.substring(4);
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
    public void saveNBTData(NBTTagCompound NBT) {

        NBT.setBoolean("recipeInProgress", recipeInProgress);
        NBT.setBoolean("magmatterMode", magmatterMode);
        NBT.setLong("maxParallel", actualParallel);

        // Store damage values/stack sizes of input plasmas
        NBTTagCompound fluidStackListNBTTag = new NBTTagCompound();

        if (plasmaRecipe != null) {
            fluidStackListNBTTag.setLong("numberOfPlasmas", plasmaRecipe.mFluidInputs.length);

            int index = 0;
            for (FluidStack stack : plasmaRecipe.mFluidInputs) {
                // Save fluid amount to NBT
                fluidStackListNBTTag.setLong(index + "fluidAmount", stack.amount);

                // Save FluidStack to NBT
                NBT.setTag(index + "fluidStack", stack.writeToNBT(new NBTTagCompound()));

                index++;
            }
        } else {
            fluidStackListNBTTag.setLong("numberOfPlasmas", 0);
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

        long numberOfPlasmas = tempFluidTag.getLong("numberOfPlasmas");
        if (numberOfPlasmas > 0) {

            FluidStack[] stacks = new FluidStack[(int) numberOfPlasmas];
            for (int i = 0; i < numberOfPlasmas; i++) {
                // Load fluid amount from NBT
                int amount = tempFluidTag.getInteger(i + "fluidAmount");

                // Load FluidStack from NBT
                FluidStack stack = FluidStack.loadFluidStackFromNBT(NBT.getCompoundTag(i + "fluidStack"));

                stacks[i] = new FluidStack(stack, amount);
            }

            FluidStack outputFluid;
            if (magmatterMode) {
                outputFluid = MaterialsUEVplus.MagMatter.getMolten(actualParallel * 4 * INGOTS);
            } else {
                outputFluid = MaterialsUEVplus.QuarkGluonPlasma.getFluid(1000L * actualParallel);
            }

            plasmaRecipe = new GTRecipe(
                false,
                null,
                null,
                null,
                null,
                stacks,
                new FluidStack[] { outputFluid },
                10 * SECONDS,
                (int) TierEU.RECIPE_MAX,
                0);
        }

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
        buildContext.addSyncedWindow(POSSIBLE_INPUTS_WINDOW_ID, this::createPossibleInputsWindow);
        builder.widget(createMagmatterSwitch(builder));
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
            if (plasmaRecipe != null && i < plasmaRecipe.mFluidInputs.length) {
                FluidStack plasma = plasmaRecipe.mFluidInputs[i];
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

                for (int i = 0; i < 7; i++) {
                    if (i < plasmaRecipe.mFluidInputs.length) {
                        FluidStack plasma = plasmaRecipe.mFluidInputs[i];
                        tanksHandler.setFluidInTank(i, plasma.getFluid(), plasma.amount);
                    }
                }
                ticker = 0;
                ForgeOfGodsUI.reopenWindow(widget, INPUT_LIST_WINDOW_ID);
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

        if (NewHorizonsCoreMod.isModLoaded()) {
            builder.widget(createPossibleInputsButton());
        }

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

    protected ModularWindow createPossibleInputsWindow(final EntityPlayer player) {
        final int WIDTH = 300;
        final int HEIGHT = 143;
        ModularWindow.Builder builder = ModularWindow.builder(WIDTH, HEIGHT);
        builder.setBackground(GTUITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.setGuiTint(getGUIColorization());
        builder.setDraggable(true);
        builder.widget(
            ButtonWidget.closeWindowButton(true)
                .setPos(284, 4));

        int QGPItemMapSize = exoticModulePlasmaItemMap.size();
        int QGPFluidMapSize = exoticModulePlasmaFluidMap.size();

        IItemHandlerModifiable QGPItemHandler = new ItemStackHandler(QGPItemMapSize);
        IFluidTanksHandler QGPFluidHandler = new FluidTanksHandler(QGPFluidMapSize, 128000);

        List<Map.Entry<ItemStack, Integer>> itemEntryList = new ArrayList<>(exoticModulePlasmaItemMap.entrySet());
        List<Map.Entry<FluidStack, Integer>> fluidEntryList = new ArrayList<>(exoticModulePlasmaFluidMap.entrySet());

        int slotYQGP = 0;

        for (int i = 0; i < QGPItemMapSize + QGPFluidMapSize; i++) {

            int slotXQGP = 6 + i % 16 * 18;
            slotYQGP = 17 + i / 16 * 18;

            if (i < QGPItemMapSize) {
                QGPItemHandler.setStackInSlot(
                    i,
                    GTUtility.copyAmount(
                        1,
                        itemEntryList.get(i)
                            .getKey()));
                builder.widget(
                    new DrawableWidget().setDrawable(GTUITextures.BUTTON_STANDARD_PRESSED)
                        .setPos(slotXQGP, slotYQGP)
                        .setSize(18, 18));
                builder.widget(
                    new SlotWidget(QGPItemHandler, i).setAccess(false, false)
                        .setRenderStackSize(false)
                        .disableInteraction()
                        .setPos(slotXQGP, slotYQGP)
                        .setSize(18, 18));
            } else {
                QGPFluidHandler.setFluidInTank(
                    i - QGPItemMapSize,
                    fluidEntryList.get(i - QGPItemMapSize)
                        .getKey()
                        .getFluid(),
                    1);
                builder.widget(
                    new DrawableWidget().setDrawable(ModularUITextures.FLUID_SLOT)
                        .setSize(18, 18)
                        .setPos(slotXQGP, slotYQGP))
                    .widget(
                        new FluidSlotWidget(QGPFluidHandler, i - QGPItemMapSize).setInteraction(false, false)
                            .setSize(18, 18)
                            .setPos(slotXQGP, slotYQGP));
            }
        }

        int magMatterItemMapSize = exoticModuleMagmatterItemMap.size();

        IItemHandlerModifiable magMatterItemHandler = new ItemStackHandler(magMatterItemMapSize);
        IFluidTanksHandler magMatterFluidHandler = new FluidTanksHandler(2, 128000);
        magMatterFluidHandler.setFluidInTank(
            0,
            MaterialsUEVplus.Space.getMolten(1)
                .getFluid(),
            1);
        magMatterFluidHandler.setFluidInTank(
            1,
            MaterialsUEVplus.Time.getMolten(1)
                .getFluid(),
            1);

        List<Map.Entry<ItemStack, Integer>> magMatteritemEntryList = new ArrayList<>(
            exoticModuleMagmatterItemMap.entrySet());

        int slotXMagmatter;
        int slotYMagmatter;

        for (int i = 0; i < magMatterItemMapSize + 2; i++) {
            slotXMagmatter = 6 + i % 16 * 18;
            slotYMagmatter = slotYQGP + 30 + i / 16 * 18;

            if (i < magMatterItemMapSize) {
                magMatterItemHandler.setStackInSlot(
                    i,
                    GTUtility.copyAmount(
                        1,
                        magMatteritemEntryList.get(i)
                            .getKey()));
                builder.widget(
                    new DrawableWidget().setDrawable(GTUITextures.BUTTON_STANDARD_PRESSED)
                        .setPos(slotXMagmatter, slotYMagmatter)
                        .setSize(18, 18));
                builder.widget(
                    new SlotWidget(magMatterItemHandler, i).setAccess(false, false)
                        .setRenderStackSize(false)
                        .disableInteraction()
                        .setPos(slotXMagmatter, slotYMagmatter)
                        .setSize(18, 18));
            } else {
                builder.widget(
                    new DrawableWidget().setDrawable(ModularUITextures.FLUID_SLOT)
                        .setSize(18, 18)
                        .setPos(slotXMagmatter, slotYMagmatter))
                    .widget(
                        new FluidSlotWidget(magMatterFluidHandler, i - magMatterItemMapSize)
                            .setInteraction(false, false)
                            .setSize(18, 18)
                            .setPos(slotXMagmatter, slotYMagmatter));
            }
        }

        builder.widget(
            new TextWidget(translateToLocal("gt.blockmachines.multimachine.FOG.possibleinputsqgp"))
                .setDefaultColor(EnumChatFormatting.BLACK)
                .setTextAlignment(Alignment.Center)
                .setSize(300, 9)
                .setPos(0, 6));

        builder.widget(
            new TextWidget(translateToLocal("gt.blockmachines.multimachine.FOG.possibleinputsmagmatter"))
                .setDefaultColor(EnumChatFormatting.BLACK)
                .setTextAlignment(Alignment.Center)
                .setSize(300, 9)
                .setPos(0, slotYQGP + 21));

        return builder.build();
    }

    private Widget createPossibleInputsButton() {
        return new ButtonWidget().setOnClick((clickData, widget) -> {
            if (!widget.isClient()) {
                widget.getContext()
                    .openSyncedWindow(POSSIBLE_INPUTS_WINDOW_ID);
            }
        })
            .setPlayClickSound(true)
            .setBackground(ModularUITextures.ICON_INFO)
            .addTooltip(translateToLocal("fog.button.possibleexoticinputs.tooltip"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setSize(16, 16)
            .setPos(79, 37);
    }

    protected ButtonWidget createMagmatterSwitch(IWidgetBuilder<?> builder) {
        Widget button = new ButtonWidget().setOnClick((clickData, widget) -> {
            if (isMagmatterCapable) {
                TecTech.proxy.playSound(getBaseMetaTileEntity(), "fx_click");
                magmatterMode = !magmatterMode;
                widget.notifyTooltipChange();
            }
        })
            .setPlayClickSound(false)
            .setBackground(
                () -> new IDrawable[] { TecTechUITextures.BUTTON_CELESTIAL_32x32,
                    new ItemDrawable(
                        isMagmatterModeOn() ? GTOreDictUnificator.get(OrePrefixes.dust, MaterialsUEVplus.MagMatter, 1)
                            : CustomItemList.Godforge_FakeItemQGP.get(1))

                })
            .attachSyncer(new FakeSyncWidget.BooleanSyncer(this::isMagmatterModeOn, this::setMagmatterMode), builder)
            .dynamicTooltip(() -> {
                List<String> ret = new ArrayList<>();
                if (!isMagmatterModeOn()) {
                    ret.add(translateToLocal("fog.button.magmattermode.tooltip.01"));
                }
                if (isMagmatterCapable && isMagmatterModeOn()) {
                    ret.add(translateToLocal("fog.button.magmattermode.tooltip.02"));
                }
                if (!isMagmatterCapable) {
                    ret.add(EnumChatFormatting.GRAY + translateToLocal("fog.button.magmattermode.tooltip.03"));
                }
                return ret;
            })
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setSize(16, 16)
            .setPos(174, 91)
            .attachSyncer(
                new FakeSyncWidget.BooleanSyncer(() -> isMagmatterCapable, this::setMagmatterCapable),
                builder);
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
    protected boolean filtersFluid() {
        return false;
    }

    @Override
    public MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Exotic Matter Producer")
            .addInfo("This is a module of the Godforge.")
            .addInfo("Must be part of a Godforge to function.")
            .addInfo("Used for ultra high temperature matter degeneration.")
            .addSeparator(EnumChatFormatting.AQUA, 75)
            .addInfo("The fourth and final module of the Godforge, this module breaks apart the very")
            .addInfo("building blocks of matter, producing exotic mixtures in the process. Quark-Gluon Plasma")
            .addInfo("can be manufactured right away, but production of Magnetic Monopole Matter (Magmatter)")
            .addInfo("requires a fully upgraded Godforge.")
            .addInfo("This module is specialized towards acquisition of unique materials.")
            .beginStructureBlock(7, 7, 13, false)
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
            .toolTipFinisher(EnumChatFormatting.AQUA, 75);
        return tt;
    }

    @Override
    public String[] getInfoData() {
        ArrayList<String> str = new ArrayList<>();
        str.add(
            StatCollector.translateToLocalFormatted(
                "GT5U.infodata.progress",
                GREEN + formatNumbers(mProgresstime / 20) + RESET,
                YELLOW + formatNumbers(mMaxProgresstime / 20) + RESET));
        str.add(
            StatCollector.translateToLocalFormatted(
                "tt.infodata.multi.currently_using",
                RED + (getBaseMetaTileEntity().isActive() ? formatNumbers(EUt * actualParallel) : "0") + RESET));
        str.add(
            YELLOW + StatCollector.translateToLocalFormatted(
                "tt.infodata.multi.max_parallel",
                RESET + formatNumbers(getActualParallel())));
        str.add(
            YELLOW + StatCollector.translateToLocalFormatted(
                "GT5U.infodata.parallel.current",
                RESET + (getBaseMetaTileEntity().isActive() ? formatNumbers(getActualParallel()) : "0")));
        str.add(
            YELLOW + StatCollector.translateToLocalFormatted(
                "tt.infodata.multi.multiplier.recipe_time",
                RESET + formatNumbers(getSpeedBonus())));
        str.add(
            YELLOW + StatCollector.translateToLocalFormatted(
                "tt.infodata.multi.multiplier.energy",
                RESET + formatNumbers(getEnergyDiscount())));
        str.add(
            YELLOW + StatCollector.translateToLocalFormatted(
                "tt.infodata.multi.divisor.recipe_time.non_perfect_oc",
                RESET + formatNumbers(getOverclockTimeFactor())));
        return str.toArray(new String[0]);
    }

}
