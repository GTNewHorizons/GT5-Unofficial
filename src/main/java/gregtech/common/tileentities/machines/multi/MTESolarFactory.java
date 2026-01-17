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
import static net.minecraft.util.EnumChatFormatting.GREEN;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
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

import goodgenerator.loader.Loaders;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
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
import gregtech.api.recipe.metadata.SolarFactoryRecipeDataKey;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.ParallelHelper;
import gregtech.api.util.recipe.SolarFactoryRecipeData;
import gregtech.common.misc.GTStructureChannels;

public class MTESolarFactory extends MTEExtendedPowerMultiBlockBase<MTESolarFactory>
    implements IConstructable, ISurvivalConstructable {

    private static final int CASING_T1_INDEX = 49;
    private static final int CASING_T2_INDEX = 48;
    private static final int CASING_T3_INDEX = 183;
    int mTier;
    int casingAmount;
    int casingTier;
    boolean hasEnoughCasings;

    int outputMultiplierCap = 2;
    double outputMultiplierSlope;

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

    private static final String STRUCTURE_TIER_1 = "t1";
    private static final String STRUCTURE_TIER_2 = "t2";
    private static final String STRUCTURE_TIER_3 = "t3";
    private static final IStructureDefinition<MTESolarFactory> STRUCTURE_DEFINITION = StructureDefinition
        .<MTESolarFactory>builder()
        .addShape(
            STRUCTURE_TIER_1,
            transpose(
                new String[][] { { "EAAAE", "AAAAA", "AAAAA", "AAAAA", "EAAAE" },
                    { "E   E", " GGG ", " G G ", " GGG ", "E   E" }, { "E   E", " GGG ", " G G ", " GGG ", "E   E" },
                    { "E   E", " GGG ", " G G ", " GGG ", "E   E" }, { "EA~AE", "AAAAA", "AAAAA", "AAAAA", "EAAAE" } }))
        .addShape(
            STRUCTURE_TIER_2,
            transpose(
                new String[][] {
                    { " F     F ", "FFF   FFF", " FFF FFF ", "  FBBBF  ", "  FBBBF  ", " FFFFFFF ", "FFF   FFF",
                        " F     F " },
                    { "BBB   BBB", "BBB   BBB", "BB     BB", "   BBB   ", "   BBB   ", "BB     BB", "BBB   BBB",
                        "BBB   BBB" },
                    { "BF     FB", "FBGGGGGBF", " GGGGGGG ", " GGGGGGG ", " GGGGGGG ", " GGGGGGG ", "FBGGGGGBF",
                        "BF     FB" },
                    { "BF     FB", "FBGGGGGBF", " G     G ", " G     G ", " G     G ", " G     G ", "FBGGGGGBF",
                        "BF     FB" },
                    { "BF     FB", "FBGGGGGBF", " G     G ", " G     G ", " G     G ", " G     G ", "FBGGGGGBF",
                        "BF     FB" },
                    { "BBBB~BBBB", "BBFFFFFBB", "BFPPPPPFB", "BFPPPPPFB", "BFPPPPPFB", "BFPPPPPFB", "BBFFFFFBB",
                        "BBBBBBBBB" } }))
        .addShape(
            STRUCTURE_TIER_3,
            transpose(
                new String[][] {
                    { "   CCC   ", "  CCPCC  ", " CCPPPCC ", "CCPPPPPCC", " CCPPPCC ", "  CCPCC  ", "   CCC   " },
                    { "   GGG   ", "  C   C  ", " G     G ", "C       C", " G     G ", "  C   C  ", "   GGG   " },
                    { "   CCC   ", "  CCCCC  ", " CCCCCCC ", "CCCCHCCCC", " CCCCCCC ", "  CCCCC  ", "   CCC   " },
                    { "         ", "  FGGGF  ", "  G   G  ", "F G H G F", "  G   G  ", "  FGGGF  ", "         " },
                    { "         ", "  FGGGF  ", "  G   G  ", "F G H G F", "  G   G  ", "  FGGGF  ", "         " },
                    { "         ", "  FGGGF  ", "  G   G  ", "F G H G F", "  G   G  ", "  FGGGF  ", "         " },
                    { "         ", "  FGGGF  ", "  G   G  ", "F G H G F", "  G   G  ", "  FGGGF  ", "         " },
                    { "   CCC   ", "  CCCCC  ", " CCCCCCC ", "CCCCHCCCC", " CCCCCCC ", "  CCCCC  ", "   CCC   " },
                    { "   G~G   ", "  C   C  ", " G     G ", "C       C", " G     G ", "  C   C  ", "   GGG   " },
                    { "   CCC   ", "  CCPCC  ", " CCPPPCC ", "CCPPPPPCC", " CCPPPCC ", "  CCPCC  ", "   CCC   " } }))
        // Clean stainless steel
        .addElement(
            'A',
            buildHatchAdder(MTESolarFactory.class).atLeast(InputHatch, InputBus, OutputBus, Maintenance, Energy)
                .casingIndex(CASING_T1_INDEX)
                .hint(1)
                .buildAndChain(onElementPass(MTESolarFactory::onCasingAdded, ofBlock(sBlockCasings4, 1))))
        // Tungstensteel
        .addElement(
            'B',
            buildHatchAdder(MTESolarFactory.class)
                .atLeast(InputHatch, InputBus, OutputBus, Maintenance, Energy, MultiAmpEnergy)
                .casingIndex(CASING_T2_INDEX)
                .hint(1)
                .buildAndChain(onElementPass(MTESolarFactory::onCasingAdded, ofBlock(sBlockCasings4, 0))))
        // Advanced iridium
        .addElement(
            'C',
            buildHatchAdder(MTESolarFactory.class)
                .atLeast(InputHatch, InputBus, OutputBus, Maintenance, Energy, MultiAmpEnergy, ExoticEnergy)
                .casingIndex(CASING_T3_INDEX)
                .hint(1)
                .buildAndChain(onElementPass(MTESolarFactory::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings8, 7))))
        .addElement('E', ofFrame(Materials.DamascusSteel))
        .addElement('F', ofFrame(Materials.Tungsten))
        // G for Glass ^-^
        .addElement('G', chainAllGlasses())
        // Black plutonium item pipe
        .addElement('H', ofBlock(GregTechAPI.sBlockCasings11, 7))
        // P for Precise Electronic Unit Casing ^-^
        .addElement(
            'P',
            GTStructureChannels.PRASS_UNIT_CASING.use(
                ofBlocksTiered(
                    (block, meta) -> block == Loaders.preciseUnitCasing ? meta : null,
                    ImmutableList.of(
                        Pair.of(Loaders.preciseUnitCasing, 0),
                        Pair.of(Loaders.preciseUnitCasing, 1),
                        Pair.of(Loaders.preciseUnitCasing, 2),
                        Pair.of(Loaders.preciseUnitCasing, 3)),
                    -3,
                    MTESolarFactory::setCasingTier,
                    MTESolarFactory::getCasingTier)))
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
    public void loadNBTData(NBTTagCompound aNBT) {
        casingTier = aNBT.getInteger("casingTier");
        mTier = aNBT.getInteger("multiTier");
        super.loadNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("casingTier", casingTier);
        aNBT.setInteger("multiTier", mTier);
        super.saveNBTData(aNBT);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        casingAmount = 0;
        hasEnoughCasings = false;
        casingTier = -3;
        mTier = 0;
        if (checkPiece(STRUCTURE_TIER_1, 2, 4, 0)) {
            mTier = 1;
            hasEnoughCasings = casingAmount >= 15;
        } else if (checkPiece(STRUCTURE_TIER_2, 4, 5, 0)) {
            mTier = 2;
            hasEnoughCasings = casingAmount >= 35;
        } else if (checkPiece(STRUCTURE_TIER_3, 4, 8, 0)) {
            mTier = 3;
            hasEnoughCasings = casingAmount >= 50;
        }
        getBaseMetaTileEntity().sendBlockEvent(GregTechTileClientEvents.CHANGE_CUSTOM_DATA, getUpdateData());
        return mTier > 0 && hasEnoughCasings && (mTier == 1 || casingTier >= -1);
    }

    @Override
    public void construct(ItemStack holoStack, boolean hintsOnly) {
        if (holoStack.stackSize == 1) {
            buildPiece(STRUCTURE_TIER_1, holoStack, hintsOnly, 2, 4, 0);
        }
        if (holoStack.stackSize == 2) {
            buildPiece(STRUCTURE_TIER_2, holoStack, hintsOnly, 4, 5, 0);
        }
        if (holoStack.stackSize >= 3) {
            buildPiece(STRUCTURE_TIER_3, holoStack, hintsOnly, 4, 8, 0);
        }
    }

    @Override
    public int survivalConstruct(ItemStack holoStack, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        if (holoStack.stackSize == 1) {
            return survivalBuildPiece(STRUCTURE_TIER_1, holoStack, 2, 4, 0, elementBudget, env, false, true);
        }
        if (holoStack.stackSize == 2) {
            return survivalBuildPiece(STRUCTURE_TIER_2, holoStack, 4, 5, 0, elementBudget, env, false, true);
        }
        if (holoStack.stackSize >= 3) {
            return survivalBuildPiece(STRUCTURE_TIER_3, holoStack, 4, 8, 0, elementBudget, env, false, true);
        }
        return 0;
    }

    protected ItemStack[] calculateNewOutput(ItemStack currentOutput, int seed) {
        outputMultiplierSlope = mTier >= 3 ? 0.50 : 0.25;
        double calculatedMultiplier = Math.min((outputMultiplierSlope * seed) + 1, outputMultiplierCap);
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

            private void clearVars() {
                foundWaferStack = null;
                waferAmountInRecipe = 0;
                foundWaferTier = 0;
                minimumTierForRecipe = 0;
                shouldMultiplyOutputs = true;
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
                SolarFactoryRecipeData data = recipe.getMetadata(SolarFactoryRecipeDataKey.INSTANCE);
                if (data == null || data.minimumWaferTier == 0) {
                    shouldMultiplyOutputs = false;
                    return CheckRecipeResultRegistry.SUCCESSFUL;
                }
                if (mTier < data.tierRequired) {
                    return CheckRecipeResultRegistry.insufficientMachineTier(data.tierRequired);
                }
                if (mTier < 2) shouldMultiplyOutputs = false;
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
                if (foundWaferStack != null) {
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
        if (mTier <= 1) return 1;
        return (int) GTUtility.powInt(2, 1 + (casingTier + 2));
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        // The recipes for this map are added in NewHorizonsCoreMod
        return RecipeMaps.solarFactoryRecipes;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("machtype.solar_factory")
            .addInfo(
                "gt.solar_factory.tips",
                tieredTextLine("Mk-I", "MK-II", "MK-III", "MK-IV"),
                tieredTextLine("8", "16", "32", "64"))
            .beginStructureBlock(7, 10, 9, false)
            .addStructureInfo("gt.solar_factory.tier_head", "1")
            .addCasingInfoRange(
                ItemList.Casing_CleanStainlessSteel.get(1)
                    .getDisplayName(),
                15,
                41,
                false)
            .addCasingInfoExactly("GT5U.MBTT.AnyGlass", 24, true)
            .addCasingInfoExactly(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.DamascusSteel, 1)
                    .getDisplayName(),
                20,
                false)
            .addStructureInfo("gt.solar_factory.tier_head", "2")
            .addCasingInfoRange(
                ItemList.Casing_RobustTungstenSteel.get(1)
                    .getDisplayName(),
                35,
                101,
                false)
            .addCasingInfoExactly("GT5U.MBTT.AnyGlass", 74, true)
            .addCasingInfoExactly(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Tungsten, 1)
                    .getDisplayName(),
                75,
                false)
            .addCasingInfoExactly("GT5U.tooltip.structure.peuc", 20, true)
            .addStructureInfo("gt.solar_factory.tier_head", "3")
            .addCasingInfoRange(
                ItemList.Casing_Advanced_Iridium.get(1)
                    .getDisplayName(),
                50,
                140,
                false)
            .addCasingInfoExactly("GT5U.MBTT.AnyGlass", 67, true)
            .addCasingInfoExactly(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Tungsten, 1)
                    .getDisplayName(),
                24,
                false)
            .addCasingInfoExactly("GT5U.tooltip.structure.peuc", 26, true)
            .addCasingInfoExactly(
                ItemList.Casing_Item_Pipe_Black_Plutonium.get(1)
                    .getDisplayName(),
                6,
                false)
            .addStructureInfo("gt.solar_factory.tier_head.all")
            .addStructureInfo("")
            .addInputHatch("<casing>")
            .addInputBus("<casing>")
            .addOutputBus("<casing>")
            .addEnergyHatch("<casing>")
            .addMaintenanceHatch("<casing>")
            .addSubChannelUsage(GTStructureChannels.PRASS_UNIT_CASING)
            .toolTipFinisher(GTValues.AuthorPureBluez);
        return tt;
    }

    private int getIndex(int tier) {
        if (tier <= 1) return CASING_T1_INDEX;
        if (tier == 2) return CASING_T2_INDEX;
        return CASING_T3_INDEX;
    }

    @Override
    public byte getUpdateData() {
        return (byte) mTier;
    }

    @Override
    public void receiveClientEvent(byte aEventID, byte aValue) {
        super.receiveClientEvent(aEventID, aValue);
        if (aEventID == GregTechTileClientEvents.CHANGE_CUSTOM_DATA && ((aValue & 0x80) == 0 || aValue == -1)) {
            mTier = aValue;
        }
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(getIndex(mTier)),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_SOLAR_FACTORY_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_SOLAR_FACTORY_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(getIndex(mTier)), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_SOLAR_FACTORY_INACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_SOLAR_FACTORY_INACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(getIndex(mTier)) };
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    private String tieredTextLine(String mk1, String mk2, String mk3, String mk4) {
        return GREEN + mk1
            + EnumChatFormatting.GRAY
            + "/"
            + EnumChatFormatting.BLUE
            + mk2
            + EnumChatFormatting.GRAY
            + "/"
            + EnumChatFormatting.LIGHT_PURPLE
            + mk3
            + EnumChatFormatting.GRAY
            + "/"
            + EnumChatFormatting.GOLD
            + mk4
            + EnumChatFormatting.GRAY;
    }
}
