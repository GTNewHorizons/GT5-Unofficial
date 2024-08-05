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
import static gregtech.api.enums.GT_Values.Colen;
import static gregtech.api.enums.Mods.BuildCraftFactory;
import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.ProjectRedIllumination;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER_GLOW;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;

import com.github.technus.tectech.thing.casing.TT_Container_Casings;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.StructureUtility;
import cpw.mods.fml.client.config.GuiEditArrayEntries;
import goodgenerator.blocks.regularBlock.Casing;
import goodgenerator.loader.Loaders;
import goodgenerator.main.GoodGenerator;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.blocks.GregtechMetaSpecialMachineCasings;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.chemplant.GregtechMTE_ChemicalPlant;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

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
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import gregtech.api.multitileentity.multiblock.casing.Glasses;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Casings10;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import org.apache.commons.lang3.tuple.Pair;

public class GT_MetaTileEntity_MultiSolidifier extends
    GT_MetaTileEntity_ExtendedPowerMultiBlockBase<GT_MetaTileEntity_MultiSolidifier> implements ISurvivalConstructable {

    protected final String MS_LEFT_MID = mName + "leftmid";
    protected final String MS_RIGHT_MID = mName + "rightmid";
    protected final String MS_END = mName + "end";

    protected int casingAmount = 0;
    protected int Width = 0;
    protected int eV = 0, mCeil = 0, mFloor = 0;
    private int casingTier;
    private int spacetimeCompressionFieldMetadata = -1;


    public List fluidSolidifierCasings = new ArrayList((GregTech_API.sBlockCasings10, 3),(GTplusplus.) );

    private final String STRUCTURE_PIECE_MAIN = "main";
    private final IStructureDefinition<GT_MetaTileEntity_MultiSolidifier> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_MultiSolidifier>builder()
        .addShape(
            MS_LEFT_MID,
            (transpose(
                new String[][] { { "BB", "BB", "BB", "BB", "BB" }, { "AA", "  ", "D ", "  ", "AA" },
                    { "AA", "  ", "  ", "  ", "AA" }, { "CC", "  ", "F ", "  ", "CC" },
                    { "BB", "BB", "BB", "BB", "BB" } }))
            )
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

            buildHatchAdder(GT_MetaTileEntity_MultiSolidifier.class)
                .atLeast(InputBus, OutputBus, Maintenance, Energy, InputHatch)
                .casingIndex(((GT_Block_Casings10) GregTech_API.sBlockCasings10).getTextureIndex(3))
                .dot(1)
                .buildAndChain(
                    StructureUtility.<IStructureElement<GT_MetaTileEntity_MultiSolidifier>, GT_MetaTileEntity_MultiSolidifier>onElementPass(
                        GT_MetaTileEntity_MultiSolidifier::onCasingAdded,
                        /*
                        ofBlock(GregTech_API.sBlockCasings10, 3))))
                         */
                        withChannel(
                            "casing",
                            ofBlocksTiered(
                                (block, meta) -> block == TT_Container_Casings.SpacetimeCompressionFieldGenerators ? meta : null,
                                ImmutableList.of(
                                    Pair.of(GregTech_API.sBlockCasings10, 3),
                                    Pair.of(GregtechItemList.Casing_Machine_Custom_3, 1),
                                    Pair.of(TT_Container_Casings.SpacetimeCompressionFieldGenerators, 2),
                                    Pair.of(TT_Container_Casings.SpacetimeCompressionFieldGenerators, 3),
                                    Pair.of(TT_Container_Casings.SpacetimeCompressionFieldGenerators, 4),
                                    Pair.of(TT_Container_Casings.SpacetimeCompressionFieldGenerators, 5),
                                    Pair.of(TT_Container_Casings.SpacetimeCompressionFieldGenerators, 6),
                                    Pair.of(TT_Container_Casings.SpacetimeCompressionFieldGenerators, 7),
                                    Pair.of(TT_Container_Casings.SpacetimeCompressionFieldGenerators, 8)),
                                -1,
                                (t, meta) -> t.spacetimeCompressionFieldMetadata = meta,
                                t -> t.spacetimeCompressionFieldMetadata)))))
        .addElement('C', ofBlock(GregTech_API.sBlockCasings10, 4))
        .addElement('D', ofBlock(GregTech_API.sBlockCasings2, 13))
        .addElement(
            'E',
            buildHatchAdder(GT_MetaTileEntity_MultiSolidifier.class).atLeast(InputHatch)
                .casingIndex(((GT_Block_Casings10) GregTech_API.sBlockCasings10).getTextureIndex(3))
                .dot(1)
                .buildAndChain(
                    onElementPass(
                        GT_MetaTileEntity_MultiSolidifier::onCasingAdded,
                        ofBlock(GregTech_API.sBlockCasings10, 3))))
        .addElement('F', BuildCraftFactory.isModLoaded()
                ? ofChain(
        ofBlock(Block.getBlockFromName("BuildCraft|Factory:hopperBlock"), 10))
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
        if (checkPiece(STRUCTURE_PIECE_MAIN, 3, 4, 0)) {
            while (mWidth < 30) {
                if (checkPiece(MS_RIGHT_MID, (-2 * (mWidth + 1)) - 2, 4, 0) && checkPiece(MS_LEFT_MID, (2 * (mWidth + 1)) + 3, 4, 0)) {
                    mWidth++;
                } else break;
            }
        } else return false;
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
