package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_Values.AuthorOmdaCZ;
import static gregtech.api.enums.Mods.BuildCraftFactory;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER_GLOW;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_Utility.filterValidMTEs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;

import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Maintenance;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBus;
import gregtech.api.multitileentity.multiblock.casing.Glasses;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Casings1;
import gregtech.common.blocks.GT_Block_Casings10;
import gregtech.common.tileentities.machines.IDualInputHatch;
import gtPlusPlus.core.block.ModBlocks;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class GT_MetaTileEntity_MultiSolidifier extends
    GT_MetaTileEntity_ExtendedPowerMultiBlockBase<GT_MetaTileEntity_MultiSolidifier> implements ISurvivalConstructable {

    protected final String MS_LEFT_MID = mName + "leftmid";
    protected final String MS_RIGHT_MID = mName + "rightmid";
    protected final String MS_END = mName + "end";

    protected int casingAmount = 0;
    protected int Width = 0;
    protected int eV = 0, mCeil = 0, mFloor = 0;
    private int casingTier = -1;
    private int pipeMeta = -1;
    private int machineTier = 0;
    private static final int SOLIDIFIER_CASING_INDEX = ((GT_Block_Casings10) GregTech_API.sBlockCasings10)
        .getTextureIndex(3);
    private static final int DTPF_CASING_INDEX = ((GT_Block_Casings1) GregTech_API.sBlockCasings1).getTextureIndex(12);

    private final Map<Integer, Pair<Block, Integer>> tieredFluidSolidifierCasings = new HashMap<>() {

        {
            // Solidifier Casing
            put(3, Pair.of(GregTech_API.sBlockCasings10, 0));
            // Laurenium Casing
            put(2, Pair.of(ModBlocks.blockCustomMachineCasings, 1));
            // Dimensionally Transcendent Casing
            put(12, Pair.of(GregTech_API.sBlockCasings1, 2));

        }
    };

    private final List<Integer> casingIndices = new ArrayList<>(
        Arrays.asList(SOLIDIFIER_CASING_INDEX, 84, DTPF_CASING_INDEX));

    private final String STRUCTURE_PIECE_MAIN = "main";
    private final IStructureDefinition<GT_MetaTileEntity_MultiSolidifier> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_MultiSolidifier>builder()
        .addShape(
            MS_LEFT_MID,
            (transpose(
                new String[][] { { "BB", "BB", "BB", "BB", "BB" }, { "AA", "  ", "D ", "  ", "AA" },
                    { "AA", "  ", "  ", "  ", "AA" }, { "CC", "  ", "F ", "  ", "CC" },
                    { "BB", "BB", "BB", "BB", "BB" } })))
        .addShape(
            MS_RIGHT_MID,
            (transpose(
                new String[][] { { "BB", "BB", "BB", "BB", "BB" }, { "AA", "  ", " D", "  ", "AA" },
                    { "AA", "  ", "  ", "  ", "AA" }, { "CC", "  ", " F", "  ", "CC" },
                    { "BB", "BB", "BB", "BB", "BB" } })))
        .addShape(
            MS_END,
            (transpose(
                new String[][] { { "B", "B", "B", "B", "B" }, { "B", "B", "B", "B", "B" }, { "B", "B", "B", "B", "B" },
                    { "B", "B", "B", "B", "B" }, { "B", "B", "B", "B", "B" } })))
        .addShape(
            STRUCTURE_PIECE_MAIN,
            (transpose(
                new String[][] { { "BBBBBBB", "BBBBBBB", "BBBBBBB", "BBBBBBB", "BBBBBBB" },
                    { "AAAAAAA", "       ", "D D D D", "       ", "AAAAAAA" },
                    { "AAAAAAA", "       ", "       ", "       ", "AAAAAAA" },
                    { "CCCBCCC", "       ", "F F F F", "       ", "CCCECCC" },
                    { "BBB~BBB", "BBBBBBB", "BBBBBBB", "BBBBBBB", "BBBBBBB" } })))
        .addElement('A', Glasses.chainAllGlasses())
        .addElement(
            'B',

            GT_HatchElementBuilder.<GT_MetaTileEntity_MultiSolidifier>builder()
                .atLeast(InputBus, OutputBus, Maintenance, Energy, InputHatch)
                .adder(GT_MetaTileEntity_MultiSolidifier::addToSolidifierList)
                .casingIndex(SOLIDIFIER_CASING_INDEX)
                .dot(1)
                .buildAndChain(
                    withChannel(
                        "casing",
                        onElementPass(
                            x -> x.casingAmount++,
                            ofBlocksTiered(
                                this::tierExtractor,
                                ImmutableList.of(
                                    Pair.of(GregTech_API.sBlockCasings10, 3),
                                    Pair.of(ModBlocks.blockCustomMachineCasings, 2),
                                    Pair.of(GregTech_API.sBlockCasings1, 12)),
                                -1,
                                GT_MetaTileEntity_MultiSolidifier::setCasingTier,
                                GT_MetaTileEntity_MultiSolidifier::getCasingTier)))))
        .addElement('C', ofBlock(GregTech_API.sBlockCasings10, 4))
        .addElement(
            'D',
            withChannel(
                "pipe casing",
                ofBlocksTiered(
                    (block, meta) -> block == ModBlocks.blockCustomPipeGearCasings ? meta : null,
                    ImmutableList.of(
                        Pair.of(ModBlocks.blockCustomPipeGearCasings, 8),
                        Pair.of(ModBlocks.blockCustomPipeGearCasings, 13),
                        Pair.of(ModBlocks.blockCustomPipeGearCasings, 15)),
                    -1,
                    (t, meta) -> t.pipeMeta = meta,
                    t -> t.pipeMeta)))
        .addElement(
            'E',
            buildHatchAdder(GT_MetaTileEntity_MultiSolidifier.class).atLeast(InputHatch)
                .casingIndex(SOLIDIFIER_CASING_INDEX)
                .dot(1)
                .buildAndChain(
                    onElementPass(
                        GT_MetaTileEntity_MultiSolidifier::onCasingAdded,
                        ofBlock(GregTech_API.sBlockCasings10, 3))))
        .addElement(
            'F',
            BuildCraftFactory.isModLoaded()
                ? ofChain(ofBlock(Block.getBlockFromName("BuildCraft|Factory:hopperBlock"), 10))
                : ofChain(ofBlock(Blocks.hopper, 0)))
        .build();

    public GT_MetaTileEntity_MultiSolidifier(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_MultiSolidifier(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_MultiSolidifier(this.mName);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings2, 0)),
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
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings2, 0)),
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
                .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings2, 0)) };
        }
        return rTexture;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Fluid solidifier")
            .addInfo("Controller Block for the Fluid Shaper 2024")
            .addInfo("Can get speed bonus from coolants")
            .addInfo("Water: +20%")
            .addInfo("IC2 Coolant: +50%")
            .addInfo("Cryotheum: +100%")
            .addInfo("Super Coolant: +200%")
            .addInfo("Gains 2x mold parallels per width expansion")
            .addInfo(EnumChatFormatting.BLUE + "Pretty solid, isn't it")
            .addInfo(AuthorOmdaCZ)
            .addSeparator()
            .beginVariableStructureBlock(9, 65, 5, 5, 5, 5, true)
            .addController("Front Center bottom")
            .addCasingInfoMin("Solid Steel Machine Casing", 69, false)
            .addCasingInfoMin("Steel Pipe Casing", 18, false)
            .addInputBus("Any Solid Steel Casing", 1)
            .addOutputBus("Any Solid Steel Casing", 1)
            .addInputHatch("Any Solid Steel Casing", 1)
            .addInputHatch("Solid Steel Casing second layer in the back centre", 1)
            .addEnergyHatch("Any Solid Steel Casing", 1)
            .addMaintenanceHatch("Any Solid Steel Casing", 1)
            .toolTipFinisher("GregTech");
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 3, 4, 0);
        int tTotalWidth = Math.min(30, stackSize.stackSize + 3); // min 2 output layer, so at least 1 + 2 Width
        for (int i = 1; i < tTotalWidth - 1; i++) {
            buildPiece(MS_LEFT_MID, stackSize, hintsOnly, 3 + 2 * i, 4, 0);
            buildPiece(MS_RIGHT_MID, stackSize, hintsOnly, -2 - 2 * i, 4, 0);
        }
        buildPiece(MS_END, stackSize, hintsOnly, (tTotalWidth + 2) * 2 - 4, 4, 0);
        buildPiece(MS_END, stackSize, hintsOnly, (-tTotalWidth - 2) * 2 + 4, 4, 0);
    }

    protected final List<List<GT_MetaTileEntity_Hatch_Output>> mOutputHatchesByLayer = new ArrayList<>();
    protected int mWidth;
    protected int nWidth;

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        mWidth = 0;
        nWidth = 0;
        int built = survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 3, 4, 0, elementBudget, env, false, true);
        if (built >= 0) return built;
        int tTotalWidth = Math.min(30, stackSize.stackSize + 3); // min 2 output layer, so at least 1 + 2 Width
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

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_MultiSolidifier> getStructureDefinition() {
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
        if (checkPiece(STRUCTURE_PIECE_MAIN, 3, 4, 0)) {
            while (mWidth < 30) {
                if (checkPiece(MS_RIGHT_MID, (-2 * (mWidth + 1)) - 2, 4, 0)
                    && checkPiece(MS_LEFT_MID, (2 * (mWidth + 1)) + 3, 4, 0)) {
                    mWidth++;
                } else break;
            }
        } else return false;
        machineTier = Math.min(getPipeCasingTier(pipeMeta), casingTier);
        if (casingTier >= -1) {
            updateHatchTextures(casingIndices.get(casingTier));
        }
        return checkPiece(MS_END, (-2 * mWidth) - 4, 4, 0) && checkPiece(MS_END, (mWidth * 2) + 4, 4, 0);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setSpeedBonus(1F / 2F)
            .setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    private int moldParallel = 0;

    public int getMaxParallelRecipes() {
        return (moldParallel * (mWidth + 4) * 2);
    }

    private void setCasingTier(int tier) {
        casingTier = tier;
    }

    private int getCasingTier() {
        return casingTier;
    }

    private int tierExtractor(Block block, int meta) {
        if (!tieredFluidSolidifierCasings.containsKey(meta) || !(tieredFluidSolidifierCasings.get(meta)
            .getLeft() == block)) {
            return -1;
        }
        return tieredFluidSolidifierCasings.get(meta)
            .getRight();
    }

    private int getPipeCasingTier(int meta) {
        if (meta == 13) {
            return 1;
        }
        if (meta == 15) {
            return 2;
        }
        return 0;
    }

    private boolean addToSolidifierList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
            return mInputHatches.add((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof IDualInputHatch) {
            return mDualInputHatches.add((IDualInputHatch) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputBus) {
            return mInputBusses.add((GT_MetaTileEntity_Hatch_InputBus) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Output) {
            return mOutputHatches.add((GT_MetaTileEntity_Hatch_Output) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputBus) {
            return mOutputBusses.add((GT_MetaTileEntity_Hatch_OutputBus) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Energy) {
            return mEnergyHatches.add((GT_MetaTileEntity_Hatch_Energy) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Maintenance) {
            return mMaintenanceHatches.add((GT_MetaTileEntity_Hatch_Maintenance) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler) {
            return mMufflerHatches.add((GT_MetaTileEntity_Hatch_Muffler) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_EnergyMulti) {
            return mExoticEnergyHatches.add((GT_MetaTileEntity_Hatch_EnergyMulti) aMetaTileEntity);
        }
        return false;
    }

    private void updateHatchTextures(int texture) {
        for (IDualInputHatch hatch : mDualInputHatches) {
            if (((MetaTileEntity) hatch).isValid()) {
                hatch.updateTexture(texture);
            }
        }
        for (GT_MetaTileEntity_Hatch hatch : filterValidMTEs(mInputHatches)) {
            hatch.updateTexture(texture);
        }
        for (GT_MetaTileEntity_Hatch hatch : filterValidMTEs(mInputBusses)) {
            hatch.updateTexture(texture);
        }
        for (GT_MetaTileEntity_Hatch hatch : filterValidMTEs(mOutputHatches)) {
            hatch.updateTexture(texture);
        }
        for (GT_MetaTileEntity_Hatch hatch : filterValidMTEs(mOutputBusses)) {
            hatch.updateTexture(texture);
        }
        for (GT_MetaTileEntity_Hatch hatch : filterValidMTEs(mEnergyHatches)) {
            hatch.updateTexture(texture);
        }
        for (GT_MetaTileEntity_Hatch hatch : filterValidMTEs(mMaintenanceHatches)) {
            hatch.updateTexture(texture);
        }
        for (GT_MetaTileEntity_Hatch hatch : filterValidMTEs(mMufflerHatches)) {
            hatch.updateTexture(texture);
        }
        for (GT_MetaTileEntity_Hatch hatch : filterValidMTEs(mExoticEnergyHatches)) {
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
    public boolean supportsMachineModeSwitch() {
        return false;
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

    @Override
    public boolean supportsInputSeparation() {
        return false;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }
}
