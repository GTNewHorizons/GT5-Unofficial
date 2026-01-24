package tectech.thing.metaTileEntity.multi.godforge;

import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTUtility.formatNumbers;
import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;
import static gregtech.common.misc.WirelessNetworkManager.getUserEU;
import static net.minecraft.util.EnumChatFormatting.GREEN;
import static net.minecraft.util.EnumChatFormatting.RED;
import static net.minecraft.util.EnumChatFormatting.RESET;
import static net.minecraft.util.EnumChatFormatting.YELLOW;
import static tectech.loader.recipe.Godforge.exoticModuleMagmatterItemMap;
import static tectech.loader.recipe.Godforge.exoticModulePlasmaFluidMap;
import static tectech.loader.recipe.Godforge.exoticModulePlasmaItemMap;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

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

import com.cleanroommc.modularui.utils.fluid.FluidTanksHandler;
import com.cleanroommc.modularui.utils.fluid.IFluidTanksHandler;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTStreamUtil;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.gui.modularui.multiblock.godforge.MTEExoticModuleGui;
import tectech.recipe.TecTechRecipeMaps;
import tectech.thing.metaTileEntity.multi.godforge.util.GodforgeMath;

public class MTEExoticModule extends MTEBaseModule {

    public static final int RECIPE_REFRESH_LIMIT = 60 * SECONDS;
    public static final int NUMBER_OF_INPUTS = 7;

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
    public final IFluidTanksHandler tankHandler = new FluidTanksHandler(NUMBER_OF_INPUTS, 128000);
    private BigInteger powerForRecipe = BigInteger.ZERO;

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
                        setPlasmaRecipe(generateMagmatterRecipe());
                    } else {
                        setPlasmaRecipe(generateQuarkGluonRecipe());
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
                        setPlasmaRecipe(null);
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
                        addItemOutputs(randomizedItemInput);
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
                setPlasmaRecipe(null);
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
            new FluidStack[] { Materials.QuarkGluonPlasma.getFluid(1000 * actualParallel) },
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
        randomizedFluidInput = new FluidStack[] { Materials.Time.getMolten(timeAmount * 1000L),
            Materials.Space.getMolten(spaceAmount * 1000L) };

        return new GTRecipe(
            false,
            null,
            null,
            null,
            null,
            ArrayUtils.addAll(
                convertItemToPlasma(randomizedItemInput, spaceAmount - timeAmount),
                Materials.Time.getMolten(timeAmount),
                Materials.Space.getMolten(spaceAmount)),
            new FluidStack[] { Materials.MagMatter.getMolten(576 * actualParallel) },
            10 * SECONDS,
            (int) TierEU.RECIPE_MAX,
            0);
    }

    private void setPlasmaRecipe(GTRecipe plasmaRecipe) {
        this.plasmaRecipe = plasmaRecipe;

        for (int i = 0; i < NUMBER_OF_INPUTS; i++) {
            if (plasmaRecipe == null) {
                tankHandler.setFluidInTank(i, null, 0);
                continue;
            }

            if (i < plasmaRecipe.mFluidInputs.length) {
                FluidStack plasma = plasmaRecipe.mFluidInputs[i];
                tankHandler.setFluidInTank(i, plasma.getFluid(), plasma.amount);
            } else {
                tankHandler.setFluidInTank(i, null, 0);
            }
        }
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
                outputFluid = Materials.MagMatter.getMolten(actualParallel * 4 * INGOTS);
            } else {
                outputFluid = Materials.QuarkGluonPlasma.getFluid(1000L * actualParallel);
            }

            setPlasmaRecipe(
                new GTRecipe(
                    false,
                    null,
                    null,
                    null,
                    null,
                    stacks,
                    new FluidStack[] { outputFluid },
                    10 * SECONDS,
                    (int) TierEU.RECIPE_MAX,
                    0));
        }

        super.loadNBTData(NBT);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        ticker++;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTEExoticModuleGui(this);
    }

    public void refreshRecipe() {
        if (ticker <= RECIPE_REFRESH_LIMIT) return;

        if (magmatterMode) {
            setPlasmaRecipe(generateMagmatterRecipe());
        } else {
            setPlasmaRecipe(generateQuarkGluonRecipe());
        }
        recipeRegenerated = true;
        ticker = 0;
    }

    public boolean isMagmatterModeOn() {
        return magmatterMode;
    }

    public void setMagmatterMode(boolean enabled) {
        magmatterMode = enabled;
    }

    public long getTicker() {
        return ticker;
    }

    public void setTicker(long val) {
        ticker = val;
    }

    @Override
    protected boolean filtersFluid() {
        return false;
    }

    @Override
    public MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Exotic Matter Producer")
            .addInfo("This is a module of the Godforge")
            .addInfo("Must be part of a Godforge to function")
            .addInfo("Used for ultra high temperature matter degeneration")
            .addSeparator(EnumChatFormatting.AQUA, 75)
            .addInfo("The fourth and final module of the Godforge, this module breaks apart the very")
            .addInfo("building blocks of matter, producing exotic mixtures in the process. Quark-Gluon Plasma")
            .addInfo("can be manufactured right away, but production of Magnetic Monopole Matter (Magmatter)")
            .addInfo("requires a fully upgraded Godforge")
            .addInfo("This module is specialized towards acquisition of unique materials")
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
    public void getExtraInfoData(ArrayList<String> info) {
        info.add(
            YELLOW + StatCollector.translateToLocalFormatted(
                "tt.infodata.multi.max_parallel",
                RESET + formatNumbers(getActualParallel())));
        info.add(
            YELLOW + StatCollector.translateToLocalFormatted(
                "GT5U.infodata.parallel.current",
                RESET + (getBaseMetaTileEntity().isActive() ? formatNumbers(getActualParallel()) : "0")));
        info.add(
            YELLOW + StatCollector.translateToLocalFormatted(
                "tt.infodata.multi.multiplier.recipe_time",
                RESET + formatNumbers(getSpeedBonus())));
        info.add(
            YELLOW + StatCollector.translateToLocalFormatted(
                "tt.infodata.multi.multiplier.energy",
                RESET + formatNumbers(getEnergyDiscount())));
        info.add(
            YELLOW + StatCollector.translateToLocalFormatted(
                "tt.infodata.multi.divisor.recipe_time.non_perfect_oc",
                RESET + formatNumbers(getOverclockTimeFactor())));
    }

}
