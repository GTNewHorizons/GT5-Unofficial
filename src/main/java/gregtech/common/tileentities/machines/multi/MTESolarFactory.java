package gregtech.common.tileentities.machines.multi;

import static bartworks.util.BWTooltipReference.TT;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SOLAR_FACTORY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SOLAR_FACTORY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SOLAR_FACTORY_INACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SOLAR_FACTORY_INACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTUtility.copyAmountUnsafe;
import static net.minecraft.util.EnumChatFormatting.*;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;

import goodgenerator.loader.Loaders;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.GregTechTileClientEvents;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.multitileentity.multiblock.casing.Glasses;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.ParallelHelper;

// TODO:
// fix bugs if i find them
// get staffix to make sf recipes cuz she asked if she could :3

public class MTESolarFactory extends MTEExtendedPowerMultiBlockBase<MTESolarFactory>
    implements IConstructable, ISurvivalConstructable {

    protected IStructureDefinition<MTESolarFactory> multiDefinition = null;
    protected int casingAmount;
    protected int casingTier;
    ItemStack waferStack;
    int foundWaferTier;
    boolean didFindStack;
    int minimumWaferTierForRecipe = -1;
    private static final int CASING_INDEX = 16;

    // Left side of the pair is a valid wafer for input, right side is compared to the specialvalue to validate recipe
    // and then used in the output multiplier formula
    public static final ImmutableList<Pair<ItemStack, Integer>> validWafers = ImmutableList.of(
        Pair.of(ItemList.Circuit_Silicon_Wafer.get(1), 1),
        Pair.of(ItemList.Circuit_Silicon_Wafer2.get(1), 2),
        Pair.of(ItemList.Circuit_Silicon_Wafer3.get(1), 3),
        Pair.of(ItemList.Circuit_Silicon_Wafer4.get(1), 4),
        Pair.of(ItemList.Circuit_Silicon_Wafer5.get(1), 5),
        Pair.of(ItemList.Circuit_Silicon_Wafer6.get(1), 6),
        Pair.of(ItemList.Circuit_Silicon_Wafer7.get(1), 7));

    public MTESolarFactory(String aName) {
        super(aName);
    }

    public MTESolarFactory(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESolarFactory(mName);
    }

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTESolarFactory> STRUCTURE_DEFINITION = StructureDefinition
        .<MTESolarFactory>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(
                new String[][] {
                    { "   CCC   ", "  CCFCC  ", " CCFFFCC ", "CCFFFFFCC", " CCFFFCC ", "  CCFCC  ", "   CCC   " },
                    { "   BBB   ", "  C   C  ", " B     B ", "C       C", " B     B ", "  C   C  ", "   BBB   " },
                    { "   CCC   ", "  CCCCC  ", " CCCCCCC ", "CCCCCCCCC", " CCCCCCC ", "  CCCCC  ", "   CCC   " },
                    { "         ", "  EBBBE  ", "  B   B  ", "E B D B E", "  B   B  ", "  EBBBE  ", "         " },
                    { "         ", "  EBBBE  ", "  B   B  ", "E B D B E", "  B   B  ", "  EBBBE  ", "         " },
                    { "         ", "  EBBBE  ", "  B   B  ", "E B D B E", "  B   B  ", "  EBBBE  ", "         " },
                    { "         ", "  EBBBE  ", "  B   B  ", "E B D B E", "  B   B  ", "  EBBBE  ", "         " },
                    { "   CCC   ", "  CCCCC  ", " CCCCCCC ", "CCCCCCCCC", " CCCCCCC ", "  CCCCC  ", "   CCC   " },
                    { "   B~B   ", "  C   C  ", " B     B ", "C       C", " B     B ", "  C   C  ", "   BBB   " },
                    { "   CCC   ", "  CCFCC  ", " CCFFFCC ", "CCFFFFFCC", " CCFFFCC ", "  CCFCC  ", "   CCC   " } }))
        .addElement(
            'F',
            withChannel(
                "unit casing",
                StructureUtility.ofBlocksTiered(
                    (block, meta) -> block == Loaders.preciseUnitCasing ? meta : -2,
                    // ^ if block is preciseUnitCasing return meta, otherwise return -2 & fail checkMachine
                    ImmutableList.of(
                        Pair.of(Loaders.preciseUnitCasing, 0),
                        Pair.of(Loaders.preciseUnitCasing, 1),
                        Pair.of(Loaders.preciseUnitCasing, 2),
                        Pair.of(Loaders.preciseUnitCasing, 3)),
                    -3,
                    MTESolarFactory::setCasingTier,
                    MTESolarFactory::getCasingTier)))
        .addElement(
            'C',
            buildHatchAdder(MTESolarFactory.class)
                .atLeast(InputHatch, InputBus, OutputBus, Maintenance, Energy.or(ExoticEnergy))
                .casingIndex(CASING_INDEX)
                .dot(1)
                .buildAndChain(onElementPass(MTESolarFactory::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings2, 0))))
        .addElement('D', ofBlock(GregTechAPI.sBlockCasings2, 5))
        .addElement('B', Glasses.chainAllGlasses())
        .addElement('E', ofFrame(Materials.NiobiumTitanium))
        .build();

    public int getCasingTier() {
        return casingTier;
    }

    public void setCasingTier(int i) {
        casingTier = i;
    }

    private void onCasingAdded() {
        casingAmount++;
    }

    @Override
    public IStructureDefinition<MTESolarFactory> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.casingAmount = 0;
        this.casingTier = -3;
        if (checkPiece(STRUCTURE_PIECE_MAIN, 4, 8, 0)) {
            getBaseMetaTileEntity().sendBlockEvent(GregTechTileClientEvents.CHANGE_CUSTOM_DATA, getUpdateData());
            return casingAmount >= 8 && casingTier >= -1 && mMaintenanceHatches.size() == 1;
        }
        return false;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        casingTier = aNBT.getInteger("casingTier");
        super.loadNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("casingTier", casingTier);
        super.saveNBTData(aNBT);
    }

    @Override
    public byte getUpdateData() {
        return (byte) casingTier;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 4, 8, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 4, 8, 0, elementBudget, env, false, true);
    }

    protected int getMaxParallel() {
        return (int) Math.pow(2, 1 + (casingTier + 2));
    }

    protected ItemStack[] calculateOutput(ItemStack currentOutput, int seed) {
        double calculatedMultiplier = ((0.25 * seed) + 1);
        if (calculatedMultiplier > 2) calculatedMultiplier = 2;
        int outputSize = (int) Math.floor(currentOutput.stackSize * calculatedMultiplier);
        return new ItemStack[] { copyAmountUnsafe(outputSize, currentOutput) };
    }

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {
        setupProcessingLogic(processingLogic);

        CheckRecipeResult result = doCheckRecipe();
        result = postCheckRecipe(result, processingLogic);
        // inputs are consumed at this point
        updateSlots();
        if (!result.wasSuccessful()) return result;

        mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        mEfficiencyIncrease = 10000;
        mMaxProgresstime = processingLogic.getDuration();
        setEnergyUsage(processingLogic);

        mOutputItems = calculateOutput(
            processingLogic.getOutputItems()[0],
            (foundWaferTier - minimumWaferTierForRecipe));
        mOutputFluids = processingLogic.getOutputFluids();

        waferStack = null;
        foundWaferTier = 0;
        minimumWaferTierForRecipe = -1;
        didFindStack = false;

        return result;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            // validateRecipe is called before createParallelHelper or checkProcessing so checking for wafers and
            // overriding the variables will go there
            @NotNull
            @Override
            public CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                for (ItemStack items : getStoredInputs()) {
                    // iterate over the validWafers List
                    for (Pair<ItemStack, Integer> p : validWafers) {
                        if (items.isItemEqual(p.getLeft())) {
                            waferStack = p.getLeft();
                            foundWaferTier = p.getRight();
                            minimumWaferTierForRecipe = recipe.mSpecialValue;
                            didFindStack = true;
                            // items will never equal another entry in validWafers so break out of the loop
                            break;
                        }
                    }
                }
                if (!didFindStack) {
                    return SimpleCheckRecipeResult.ofFailure("no_wafer");
                }
                if (minimumWaferTierForRecipe > foundWaferTier) {
                    return SimpleCheckRecipeResult.ofFailure("low_wafer_tier");
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @Nonnull
            private GTRecipe recipeAfterAdjustments(@Nonnull GTRecipe recipe) {
                GTRecipe tRecipe = recipe.copy();
                tRecipe.mInputs = ArrayUtils.add(tRecipe.mInputs, waferStack);
                return tRecipe;
            }

            @NotNull
            @Override
            protected ParallelHelper createParallelHelper(@Nonnull GTRecipe recipe) {
                return super.createParallelHelper(recipeAfterAdjustments(recipe));
            }
        }.setMaxParallelSupplier(this::getMaxParallel);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.solarFactoryRecipes;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Solar Factory")
            .addInfo("Controller block for the Solar Factory")
            .addInfo("Produces solar panels in bulk")
            .addInfo("Receives a 25% bonus to output for each Wafer tier above the minimum required")
            .addInfo("The bonus to output occurs after parallels, and cannot be greater than 100%")
            .addInfo("The recipes shown in NEI display the minimum wafer tier required")
            .addInfo("Parallels are based on Precise Casing Tier")
            .addInfo("MK-I = 8x, MK-II = 16x, MK-III = 32x, MK-IV = 64x")
            .addInfo("Supports " + TT + " energy hatches")
            .beginStructureBlock(7, 10, 9, true)
            .addStructureInfo(BLUE + "Imprecise Unit Casings cannot be used")
            .addStructureInfo(BLUE + "26 " + DARK_AQUA + "Precise Electronic Unit Casing")
            .addStructureInfo(BLUE + "120+ " + DARK_AQUA + "Solid Steel Machine Casings")
            .addStructureInfo(BLUE + "24 " + DARK_AQUA + "Niobium-Titanium Frame Boxes")
            .addStructureInfo(BLUE + "67 " + DARK_AQUA + "HV+ Glass")
            .addStructureInfo(BLUE + "4 " + DARK_AQUA + "Assembling Line Casing")
            .addStructureInfo(BLUE + "1+ " + DARK_AQUA + "Input Hatch")
            .addStructureInfo(BLUE + "1+ " + DARK_AQUA + "Input Bus")
            .addStructureInfo(BLUE + "1+ " + DARK_AQUA + "Output Bus")
            .addStructureInfo(BLUE + "1+ " + DARK_AQUA + "Energy Hatch")
            .addStructureInfo(BLUE + "1 " + DARK_AQUA + "Maintenance Hatch")
            .toolTipFinisher(GTValues.AuthorPureBluez);
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { casingTexturePages[0][16], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_SOLAR_FACTORY_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_SOLAR_FACTORY_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { casingTexturePages[0][16], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_SOLAR_FACTORY_INACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_SOLAR_FACTORY_INACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { casingTexturePages[0][16] };
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    public boolean isInputSeparationEnabled() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

}
