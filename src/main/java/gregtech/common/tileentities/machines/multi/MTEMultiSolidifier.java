package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static gregtech.api.enums.GTValues.AuthorFourIsTheNumber;
import static gregtech.api.enums.GTValues.AuthorOmdaCZ;
import static gregtech.api.enums.GTValues.authorBaps;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER_GLOW;
import static gregtech.api.util.GTUtility.filterValidMTEs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.GregTechTileClientEvents;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.metatileentity.implementations.MTEHatchMaintenance;
import gregtech.api.metatileentity.implementations.MTEHatchMuffler;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.metatileentity.implementations.MTEHatchOutputBus;
import gregtech.api.multitileentity.multiblock.casing.Glasses;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.HatchElementBuilder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings1;
import gregtech.common.blocks.BlockCasings10;
import gregtech.common.tileentities.machines.IDualInputHatch;
import gregtech.common.tileentities.machines.IDualInputInventory;
import gregtech.common.tileentities.machines.MTEHatchCraftingInputME;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchSolidifier;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyMulti;

public class MTEMultiSolidifier extends MTEExtendedPowerMultiBlockBase<MTEMultiSolidifier>
    implements ISurvivalConstructable {

    protected final String MS_LEFT_MID = mName + "leftmid";
    protected final String MS_RIGHT_MID = mName + "rightmid";
    protected final String MS_END = mName + "end";

    protected int casingAmount = 0;
    protected int pipeCasingAmount = 0;
    protected int Width = 0;
    protected int eV = 0, mCeil = 0, mFloor = 0;
    private int casingTier = -1;
    private int pipeCasingTier = -1;
    private int pipeMeta = -1;
    private int machineTier = 0;
    private static final int SOLIDIFIER_CASING_INDEX = ((BlockCasings10) GregTechAPI.sBlockCasings10)
        .getTextureIndex(13);
    private static final int DTPF_CASING_INDEX = ((BlockCasings1) GregTechAPI.sBlockCasings1).getTextureIndex(12);

    private final Map<Integer, Pair<Block, Integer>> tieredFluidSolidifierCasings = new HashMap<>() {

        {
            // Solidifier Casing
            put(13, Pair.of(GregTechAPI.sBlockCasings10, 0));
            // Laurenium Casing
            put(2, Pair.of(ModBlocks.blockCustomMachineCasings, 1));
            // Dimensionally Transcendent Casing
            put(12, Pair.of(GregTechAPI.sBlockCasings1, 2));

        }
    };

    private final Map<Integer, Pair<Block, Integer>> tieredPipeCasings = new HashMap<>() {

        {
            // Solidifier Radiator
            put(14, Pair.of(GregTechAPI.sBlockCasings10, 0));
            // Cinobite Pipe Casing
            put(13, Pair.of(ModBlocks.blockCustomPipeGearCasings, 1));
            // Abyssal Pipe Casing
            put(15, Pair.of(ModBlocks.blockCustomPipeGearCasings, 2));

        }
    };

    private final List<Integer> casingIndices = new ArrayList<>(
        Arrays.asList(SOLIDIFIER_CASING_INDEX, 84, DTPF_CASING_INDEX));

    private final String STRUCTURE_PIECE_MAIN = "main";
    private final IStructureDefinition<MTEMultiSolidifier> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEMultiSolidifier>builder()
        .addShape(
            MS_LEFT_MID,
            (transpose(
                new String[][] { { "  ", "BB", "BB", "BB", }, { "  ", "AA", "D ", "AA", }, { "  ", "AA", "  ", "AA", },
                    { "  ", "CC", "FC", "CC", }, { "  ", "BB", "BB", "BB", } })))
        .addShape(
            MS_RIGHT_MID,
            (transpose(
                new String[][] { { "  ", "BB", "BB", "BB" }, { "  ", "AA", " D", "AA" }, { "  ", "AA", "  ", "AA" },
                    { "  ", "CC", "CF", "CC" }, { "  ", "BB", "BB", "BB" } })))
        .addShape(
            MS_END,
            (transpose(
                new String[][] { { "B", "B", "B", "B", "B" }, { "B", "B", "B", "B", "B" }, { "B", "B", "B", "B", "B" },
                    { "B", "B", "B", "B", "B" }, { "B", "B", "B", "B", "B" } })))
        .addShape(
            STRUCTURE_PIECE_MAIN,
            (transpose(
                new String[][] { { "       ", "BBBBBBB", "BBBBBBB", "BBBBBBB", "       " },
                    { "BBBBBBB", "       ", "D D D D", "       ", "AAAAAAA" },
                    { "AAAAAAA", "       ", "       ", "       ", "AAAAAAA" },
                    { "CCCBCCC", "       ", "F F F F", "       ", "CCCCCCC" },
                    { "BBB~BBB", "BBBBBBB", "BBBBBBB", "BBBBBBB", "BBBBBBB" } })))
        .addElement('A', Glasses.chainAllGlasses())
        .addElement(
            'B',
            HatchElementBuilder.<MTEMultiSolidifier>builder()
                .atLeast(InputBus, OutputBus, Maintenance, Energy, InputHatch)
                .adder(MTEMultiSolidifier::addToSolidifierList)
                .casingIndex(SOLIDIFIER_CASING_INDEX)
                .dot(1)
                .buildAndChain(
                    withChannel(
                        "casing",
                        onElementPass(
                            x -> x.casingAmount++,
                            ofBlocksTiered(
                                this::casingTierExtractor,
                                ImmutableList.of(
                                    Pair.of(GregTechAPI.sBlockCasings10, 13),
                                    Pair.of(ModBlocks.blockCustomMachineCasings, 2),
                                    Pair.of(GregTechAPI.sBlockCasings1, 12)),
                                -1,
                                MTEMultiSolidifier::setCasingTier,
                                MTEMultiSolidifier::getCasingTier)))))
        .addElement(
            'C',
            onElementPass(
                x -> x.pipeCasingAmount++,
                ofBlocksTiered(
                    this::pipeTierExtractor,
                    ImmutableList.of(
                        Pair.of(GregTechAPI.sBlockCasings10, 14),
                        Pair.of(ModBlocks.blockCustomPipeGearCasings, 13),
                        Pair.of(ModBlocks.blockCustomPipeGearCasings, 15)),
                    -1,
                    MTEMultiSolidifier::setPipeCasingTier,
                    MTEMultiSolidifier::getPipeCasingTier)))
        .addElement('F', ofBlock(GregTechAPI.sBlockCasings1, 11))
        /*
         * TinkerConstruct.isModLoaded()// maybe temporary if someone makes textures for new special decorative block
         * ? ofChain(ofBlock(Block.getBlockFromName("TConstruct:SearedBlock"), 0))
         * : ofChain(ofBlock(Blocks.cauldron, 0)))
         */
        .addElement('D', ofBlock(GregTechAPI.sBlockCasings4, 1))
        /*
         * BuildCraftFactory.isModLoaded()// maybe temporary if someone makes textures for new special decorative block
         * ? ofChain(ofBlock(Block.getBlockFromName("BuildCraft|Factory:blockHopper"), 10))
         * : ofChain(ofBlock(Blocks.hopper, 0)))
         */
        .build();

    public MTEMultiSolidifier(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEMultiSolidifier(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEMultiSolidifier(this.mName);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        int casingIndex = casingTier > -1 ? casingIndices.get(casingTier) : SOLIDIFIER_CASING_INDEX;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] { Textures.BlockIcons.getCasingTextureForId(casingIndex),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_CANNER_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_CANNER_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] { Textures.BlockIcons.getCasingTextureForId(casingIndex),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_CANNER)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_CANNER_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings10, 13)) };
        }
        return rTexture;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Fluid Solidifier")
            .addInfo("Controller Block for the Fluid Shaper")
            .addInfo("Speeds up to a maximum of 300% faster than singleblock machines while running")
            .addInfo("Has 4 parallels by default")
            .addInfo("Gains an additional 10 parallels per width expansion")
            .addInfo(EnumChatFormatting.BLUE + "Pretty Ⱄⱁⰾⰻⰴ, isn't it")
            .addInfo(
                AuthorOmdaCZ + "with help of "
                    + AuthorFourIsTheNumber
                    + EnumChatFormatting.AQUA
                    + ", GDCloud "
                    + "&"
                    + authorBaps)
            .addSeparator()
            .beginVariableStructureBlock(17, 33, 5, 5, 5, 5, true)
            .addController("Front Center bottom")
            .addCasingInfoMin("Tier 1: Solidifier Casing", 146, true)
            .addCasingInfoMin("Tier 2: Laurenium Casing", 146, true)
            .addCasingInfoMin("Tier 3: DTPF Casing", 146, true)
            .addCasingInfoMin("Tier 1: Radiator Casing", 18, true)
            .addCasingInfoMin("Tier 2: Cinobite Pipe Casing", 18, true)
            .addCasingInfoMin("Tier 3: Abyssal Alloy Pipe Casing", 18, true)
            .addCasingInfoMin("Heat Proof Casing", 4, false)
            .addCasingInfoMin("Solid Steel Casing", 4, false)
            .addInfo("Casings/Pipe Casings limit maximal width 2; 4; 6")
            .addInputBus("Any Tiered Casing", 1)
            .addOutputBus("Any Tiered Casing", 1)
            .addInputHatch("Any Tiered Casing", 1)
            .addEnergyHatch("Any Tiered Casing", 1)
            .addMaintenanceHatch("Any Tiered Casing", 1)
            .toolTipFinisher("GregTech");
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 3, 4, 0);
        // max Width, minimal mid pieces to build on each side
        int tTotalWidth = Math.min(6, stackSize.stackSize + 3);
        for (int i = 1; i < tTotalWidth - 1; i++) {
            // horizontal offset 3 from controller and number of pieces times width of each piece
            buildPiece(MS_LEFT_MID, stackSize, hintsOnly, 3 + 2 * i, 4, 0);
            // the same but on other side of controller, for some reason -2 works right but -3 is weird
            buildPiece(MS_RIGHT_MID, stackSize, hintsOnly, -2 - 2 * i, 4, 0);
        }
        // trial and error numbers that work
        buildPiece(MS_END, stackSize, hintsOnly, (tTotalWidth + 2) * 2 - 4, 4, 0);
        buildPiece(MS_END, stackSize, hintsOnly, (-tTotalWidth - 2) * 2 + 4, 4, 0);
    }

    protected final List<List<MTEHatchOutput>> mOutputHatchesByLayer = new ArrayList<>();
    protected int mWidth;
    protected int nWidth;

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        mWidth = 0;
        nWidth = 0;
        int built = survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 3, 4, 0, elementBudget, env, false, true);
        if (built >= 0) return built;
        int tTotalWidth = Math.min(3 + getCasingTier(), stackSize.stackSize + 3);
        for (int i = 1; i < tTotalWidth - 1; i++) {
            mWidth = i;
            nWidth = i;
            built = survivialBuildPiece(MS_LEFT_MID, stackSize, 3 + 2 * i, 4, 0, elementBudget, env, false, true);
            if (built >= 0) return built;
            built = survivialBuildPiece(MS_RIGHT_MID, stackSize, -2 - 2 * i, 4, 0, elementBudget, env, false, true);
            if (built >= 0) return built;
        }
        if (mWidth == tTotalWidth - 2) return survivialBuildPiece(
            MS_END,
            stackSize,
            (2 + tTotalWidth) * 2 - 4,
            4,
            0,
            elementBudget,
            env,
            false,
            true);
        else return survivialBuildPiece(
            MS_END,
            stackSize,
            (-2 - tTotalWidth) * 2 + 4,
            4,
            0,
            elementBudget,
            env,
            false,
            true);
    }

    private int mCasingAmount;

    int mTier;
    {
        if (casingTier > pipeCasingTier) {
            mTier = pipeCasingTier;
        } else if (casingTier <= pipeCasingTier) {
            mTier = casingTier;
        }

    }

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public IStructureDefinition<MTEMultiSolidifier> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    protected boolean mTopLayerFound;
    protected int mCasing;

    protected void onCasingFound() {
        mCasing++;
    }

    protected void onTopLayerFound(boolean aIsCasing) {
        mTopLayerFound = true;
        if (aIsCasing) onCasingFound();
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mWidth = 0;
        casingTier = -1;
        pipeCasingTier = -1;
        if (checkPiece(STRUCTURE_PIECE_MAIN, 3, 4, 0)) {
            while (mWidth < (6)) {
                if (checkPiece(MS_RIGHT_MID, (-2 * (mWidth + 1)) - 2, 4, 0)
                    && checkPiece(MS_LEFT_MID, (2 * (mWidth + 1)) + 3, 4, 0)) {
                    mWidth++;
                } else break;
            }
        } else return false;
        if (!checkPiece(MS_END, (-2 * mWidth) - 4, 4, 0) || !checkPiece(MS_END, (mWidth * 2) + 4, 4, 0)) {
            return false;
        }
        if (casingAmount < (100 + mWidth * 23)) {
            casingAmount = 0;
            return false;
        } else casingAmount = 0;
        machineTier = Math.min(pipeCasingTier, casingTier);
        if (mWidth > (2 * (machineTier + 1)))
        {
            return false;
        }
        if (casingTier > -1) {
            updateHatchTextures(casingIndices.get(casingTier));
            getBaseMetaTileEntity().sendBlockEvent(GregTechTileClientEvents.CHANGE_CUSTOM_DATA, getUpdateData());
        }
        return true;

    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                setSpeedBonus(1F / speedup);
                return super.validateRecipe(recipe);
            }
        }.setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    private float speedup = 1;

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aTick % 20 == 0) {
            if (this.maxProgresstime() != 0 && speedup <= 3) {
                speedup += 0.2F;
            } else speedup = 1;
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    public int getMaxParallelRecipes() {
        return 4 + (mWidth * 10);
    }

    private void setCasingTier(int tier) {
        casingTier = tier;
    }

    private int getCasingTier() {
        return casingTier;
    }

    private void setPipeCasingTier(int tier) {
        pipeCasingTier = tier;
    }

    private int getPipeCasingTier() {
        return pipeCasingTier;
    }

    @Override
    public byte getUpdateData() {
        return (byte) casingTier;
    }

    @Override
    public void receiveClientEvent(byte aEventID, byte aValue) {
        super.receiveClientEvent(aEventID, aValue);
        if (aEventID == GregTechTileClientEvents.CHANGE_CUSTOM_DATA) {
            casingTier = aValue;
        }
    }

    private int casingTierExtractor(Block block, int meta) {
        if (!tieredFluidSolidifierCasings.containsKey(meta) || !(tieredFluidSolidifierCasings.get(meta)
            .getLeft() == block)) {
            return -1;
        }
        return tieredFluidSolidifierCasings.get(meta)
            .getRight();
    }

    private int pipeTierExtractor(Block block, int meta) {
        if (!tieredPipeCasings.containsKey(meta) || !(tieredPipeCasings.get(meta)
            .getLeft() == block)) {
            return -1;
        }
        return tieredPipeCasings.get(meta)
            .getRight();
    }

    private boolean addToSolidifierList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity instanceof MTEHatchInput hatch) {
            return mInputHatches.add(hatch);
        }
        if (aMetaTileEntity instanceof IDualInputHatch hatch) {
            return mDualInputHatches.add(hatch);
        }
        if (aMetaTileEntity instanceof MTEHatchInputBus hatch) {
            return mInputBusses.add(hatch);
        }
        if (aMetaTileEntity instanceof MTEHatchOutput hatch) {
            return mOutputHatches.add(hatch);
        }
        if (aMetaTileEntity instanceof MTEHatchOutputBus hatch) {
            return mOutputBusses.add(hatch);
        }
        if (aMetaTileEntity instanceof MTEHatchEnergy hatch) {
            return mEnergyHatches.add(hatch);
        }
        if (aMetaTileEntity instanceof MTEHatchMaintenance hatch) {
            return mMaintenanceHatches.add(hatch);
        }
        if (aMetaTileEntity instanceof MTEHatchMuffler hatch) {
            return mMufflerHatches.add(hatch);
        }
        if (aMetaTileEntity instanceof MTEHatchEnergyMulti hatch) {
            return mExoticEnergyHatches.add(hatch);
        }
        return false;
    }

    private void updateHatchTextures(int texture) {
        for (IDualInputHatch hatch : mDualInputHatches) {
            if (((MetaTileEntity) hatch).isValid()) {
                hatch.updateTexture(texture);
            }
        }
        for (MTEHatch hatch : filterValidMTEs(mInputHatches)) {
            hatch.updateTexture(texture);
        }
        for (MTEHatch hatch : filterValidMTEs(mInputBusses)) {
            hatch.updateTexture(texture);
        }
        for (MTEHatch hatch : filterValidMTEs(mOutputHatches)) {
            hatch.updateTexture(texture);
        }
        for (MTEHatch hatch : filterValidMTEs(mOutputBusses)) {
            hatch.updateTexture(texture);
        }
        for (MTEHatch hatch : filterValidMTEs(mEnergyHatches)) {
            hatch.updateTexture(texture);
        }
        for (MTEHatch hatch : filterValidMTEs(mMaintenanceHatches)) {
            hatch.updateTexture(texture);
        }
        for (MTEHatch hatch : filterValidMTEs(mMufflerHatches)) {
            hatch.updateTexture(texture);
        }
        for (MTEHatch hatch : filterValidMTEs(mExoticEnergyHatches)) {
            hatch.updateTexture(texture);
        }
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.fluidSolidifierRecipes;
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -10;
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
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
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @NotNull
    @Override
    protected CheckRecipeResult doCheckRecipe() {
        CheckRecipeResult result = CheckRecipeResultRegistry.NO_RECIPE;

        // Copied all of this from LPF. Surely it works fine
        // check crafting input hatches first
        if (supportsCraftingMEBuffer()) {
            for (IDualInputHatch dualInputHatch : mDualInputHatches) {
                for (var it = dualInputHatch.inventories(); it.hasNext();) {
                    IDualInputInventory slot = it.next();
                    processingLogic.setInputItems(slot.getItemInputs());
                    processingLogic.setInputFluids(slot.getFluidInputs());
                    CheckRecipeResult foundResult = processingLogic.process();
                    if (foundResult.wasSuccessful()) {
                        return foundResult;
                    }
                    if (foundResult != CheckRecipeResultRegistry.NO_RECIPE) {
                        // Recipe failed in interesting way, so remember that and continue searching
                        result = foundResult;
                    }
                }
            }
        }

        // Logic for GT_MetaTileEntity_Hatch_Solidifier
        for (MTEHatchInput solidifierHatch : mInputHatches) {
            if (solidifierHatch instanceof MTEHatchSolidifier hatch) {
                ItemStack mold = hatch.getMold();
                FluidStack fluid = solidifierHatch.getFluid();

                if (mold != null && fluid != null) {
                    List<ItemStack> inputItems = new ArrayList<>();
                    inputItems.add(mold);

                    processingLogic.setInputItems(inputItems.toArray(new ItemStack[0]));
                    processingLogic.setInputFluids(fluid);

                    CheckRecipeResult foundResult = processingLogic.process();
                    if (foundResult.wasSuccessful()) {
                        return foundResult;
                    }
                    if (foundResult != CheckRecipeResultRegistry.NO_RECIPE) {
                        // Recipe failed in interesting way, so remember that and continue searching
                        result = foundResult;
                    }
                }
            }
        }
        processingLogic.clear();
        processingLogic.setInputFluids(getStoredFluids());
        // Default logic
        for (MTEHatchInputBus bus : mInputBusses) {
            if (bus instanceof MTEHatchCraftingInputME) {
                continue;
            }
            List<ItemStack> inputItems = new ArrayList<>();
            for (int i = bus.getSizeInventory() - 1; i >= 0; i--) {
                ItemStack stored = bus.getStackInSlot(i);
                if (stored != null) {
                    inputItems.add(stored);
                }
            }
            if (canUseControllerSlotForRecipe() && getControllerSlot() != null) {
                inputItems.add(getControllerSlot());
            }
            processingLogic.setInputItems(inputItems.toArray(new ItemStack[0]));
            CheckRecipeResult foundResult = processingLogic.process();
            if (foundResult.wasSuccessful()) {
                return foundResult;
            }
            if (foundResult != CheckRecipeResultRegistry.NO_RECIPE) {
                // Recipe failed in interesting way, so remember that and continue searching
                result = foundResult;
            }
        }
        return result;
    }

    @Override
    public boolean isInputSeparationEnabled() {
        return true;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }
}
