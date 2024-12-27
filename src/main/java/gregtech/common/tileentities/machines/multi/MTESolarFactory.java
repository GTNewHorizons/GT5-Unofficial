package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.GregTechAPI.sBlockCasings4;
import static gregtech.api.enums.HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SOLAR_FACTORY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SOLAR_FACTORY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SOLAR_FACTORY_INACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SOLAR_FACTORY_INACTIVE_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTUtility.copyAmount;
import static gregtech.api.util.GTUtility.copyAmountUnsafe;
import static net.minecraft.util.EnumChatFormatting.WHITE;

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
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.GregTechTileClientEvents;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.ParallelHelper;
import gregtech.api.util.recipe.SolarFactoryRecipeData;

public class MTESolarFactory extends MTEExtendedPowerMultiBlockBase<MTESolarFactory>
    implements IConstructable, ISurvivalConstructable {

    protected IStructureDefinition<MTESolarFactory> multiDefinition = null;
    private static final int CASING_INDEX = 48;
    int casingAmount;
    int casingTier;

    int outputMultiplierCap = 2;
    double outputMultiplierSlope = 0.25;

    // Left side represents the wafers to look for, right side represents their tier.
    public static ImmutableList<Pair<ItemStack, Integer>> validWafers = ImmutableList.of(
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
                .buildAndChain(onElementPass(MTESolarFactory::onCasingAdded, ofBlock(sBlockCasings4, 0))))
        .addElement('D', ofBlock(GregTechAPI.sBlockCasings2, 5))
        .addElement('B', chainAllGlasses())
        .addElement('E', ofFrame(Materials.Tungsten))
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

    protected ItemStack[] calculateNewOutput(ItemStack currentOutput, int seed) {
        double calculatedMultiplier = ((outputMultiplierSlope * seed) + 1) > outputMultiplierCap ? outputMultiplierCap
            : ((outputMultiplierSlope * seed) + 1);
        int outputSize = (int) Math.floor(currentOutput.stackSize * calculatedMultiplier);
        return new ItemStack[] { copyAmountUnsafe(outputSize, currentOutput) };
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            ItemStack foundWaferStack;
            int waferAmountInRecipe;
            int foundWaferTier;
            int minimumTierForRecipe;
            boolean shouldMultiplyOutputs = true;

            private void clearVars() {
                foundWaferStack = null;
                waferAmountInRecipe = 0;
                foundWaferTier = 0;
                minimumTierForRecipe = 0;
                shouldMultiplyOutputs = true;
            }

            private void findWaferStack() {
                for (ItemStack items : inputItems) {
                    for (Pair<ItemStack, Integer> pair : validWafers) {
                        if (items.isItemEqual(pair.getLeft())) {
                            foundWaferStack = items;
                            foundWaferTier = pair.getRight();
                            break;
                        }
                    }
                }
            }

            @Override
            protected @NotNull CheckRecipeResult applyRecipe(@NotNull GTRecipe recipe, @NotNull ParallelHelper helper,
                @NotNull OverclockCalculator calculator, @NotNull CheckRecipeResult result) {
                result = super.applyRecipe(recipe, helper, calculator, result);
                if (shouldMultiplyOutputs) {
                    // We multiply outputs here since its after parallels are calculated, however this is after void
                    // protection checks so void protection is not supported.
                    outputItems = calculateNewOutput(outputItems[0], (foundWaferTier - minimumTierForRecipe));
                }
                clearVars();
                return result;
            }

            @NotNull
            @Override
            public CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                SolarFactoryRecipeData data = recipe.getMetadata(GTRecipeConstants.SOLAR_FACTORY_RECIPE_DATA);
                if (data == null) {
                    shouldMultiplyOutputs = false;
                    return CheckRecipeResultRegistry.SUCCESSFUL;
                }
                minimumTierForRecipe = data.minimumWaferTier;
                waferAmountInRecipe = data.minimumWaferCount;
                findWaferStack();
                if (foundWaferStack == null) {
                    clearVars();
                    return SimpleCheckRecipeResult.ofFailure("no_wafer");
                }
                if (minimumTierForRecipe > foundWaferTier) {
                    clearVars();
                    return SimpleCheckRecipeResult.ofFailure("low_wafer_tier");
                }
                if (waferAmountInRecipe > foundWaferStack.stackSize) {
                    clearVars();
                    return SimpleCheckRecipeResult.ofFailure("not_enough_wafer");
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @Nonnull
            private GTRecipe adjustRecipe(@Nonnull GTRecipe recipe) {
                GTRecipe tRecipe = recipe.copy();
                if (shouldMultiplyOutputs) {
                    tRecipe.mInputs = ArrayUtils.add(tRecipe.mInputs, copyAmount(waferAmountInRecipe, foundWaferStack));
                }
                return tRecipe;
            }

            @NotNull
            @Override
            protected ParallelHelper createParallelHelper(@Nonnull GTRecipe recipe) {
                return super.createParallelHelper(adjustRecipe(recipe));
            }
        }.setMaxParallelSupplier(this::getMaxParallel);
    }

    // 2^(casingTier + 3)
    protected int getMaxParallel() {
        return (int) Math.pow(2, 1 + (casingTier + 2));
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        // The recipes for this map are added in NewHorizonsCoreMod
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
            .addTecTechHatchInfo()
            .beginStructureBlock(7, 10, 9, true)
            .addStructureInfo(WHITE + "Imprecise Unit Casings cannot be used")
            .addCasingInfoRange("Tungstensteel Machine Casing", 120, 142, false)
            .addCasingInfoExactly("Any Glass", 67, false)
            .addCasingInfoExactly("Precise Electronic Unit Casing", 26, true)
            .addCasingInfoExactly("Tungsten Frame Box", 24, false)
            .addCasingInfoExactly("Assembling Line Casing", 4, false)
            .addInputHatch("Any Tungstensteel Machine Casing")
            .addInputBus("Any Tungstensteel Machine Casing")
            .addOutputBus("Any Tungstensteel Machine Casing")
            .addEnergyHatch("Any Tungstensteel Machine Casing")
            .addMaintenanceHatch("Any Tungstensteel Machine Casing")
            .toolTipFinisher(GTValues.AuthorPureBluez);
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_SOLAR_FACTORY_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_SOLAR_FACTORY_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_SOLAR_FACTORY_INACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_SOLAR_FACTORY_INACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX) };
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
    public boolean supportsInputSeparation() {
        return true;
    }
}
