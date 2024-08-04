package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.Muffler;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_Values.AuthorVolence;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_LATHE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_LATHE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_LATHE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_LATHE_GLOW;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofCoil;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.GregTechTileClientEvents;
import gregtech.common.blocks.GT_Block_Casings4;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.multitileentity.multiblock.casing.Glasses;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Casings2;

public class GT_MetaTileEntity_MultiAutoclave extends
    GT_MetaTileEntity_EnhancedMultiBlockBase<GT_MetaTileEntity_MultiAutoclave> implements ISurvivalConstructable {

    public GT_MetaTileEntity_MultiAutoclave(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_MultiAutoclave(String aName) {
        super(aName);
    }

    private HeatingCoilLevel mHeatingCapacity = HeatingCoilLevel.None;

    private static final String STRUCTURE_PIECE_MAIN = "main";

    protected int itemPipeTier = 0;
    protected int fluidPipeTier = 0;

    private static Integer getItemPipeTierFromMeta(Block block, Integer metaID) {
        if (block != GregTech_API.sBlockCasings11) return -1;
        if (metaID < 0 || metaID > 7) return -1;
        return metaID;
    }

    private void setItemPipeTier(int tier) {
        itemPipeTier = tier;
    }

    private int getItemPipeTier() {
        return itemPipeTier;
    }

    private static Integer getFluidTierFromMeta(Block block, Integer metaID) {
        if (block != GregTech_API.sBlockCasings2) return -1;
        if (metaID < 12 || metaID > 15) return -1;
        return metaID - 12;
    }

    private void setFluidPipeTier(int tier) {
        fluidPipeTier = tier;
    }

    private int getFluidPipeTier() {
        return fluidPipeTier;
    }

    public HeatingCoilLevel getCoilLevel() {
        return mHeatingCapacity;
    }

    public void setCoilLevel(HeatingCoilLevel aCoilLevel) {
        mHeatingCapacity = aCoilLevel;
    }

    private static final IStructureDefinition<GT_MetaTileEntity_MultiAutoclave> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_MultiAutoclave>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(
                new String[][] {
                    { "  AAA  ", "  AFA  ", "  AFA  ", "  AFA  ", "  AFA  ", "  AFA  ", "  AFA  ", "  AFA  ",
                        "  AAA  " },
                    { " ABBBA ", " A   A ", " A   A ", " A   A ", " A   A ", " A   A ", " A   A ", " A   A ",
                        " ABBBA ", },
                    { "ABBBBBA", "A C C A", "A C C A", "A C C A", "A C C A", "A C C A", "A C C A", "A C C A",
                        "ABBBBBA", },
                    { "ABBBBBA", "ACDEDCA", "ACDEDCA", "ACDEDCA", "ACDEDCA", "ACDEDCA", "ACDEDCA", "ACDEDCA",
                        "ABBBBBA", },
                    { "ABBBBBA", "A C C A", "A C C A", "A C C A", "A C C A", "A C C A", "A C C A", "A C C A",
                        "ABBBBBA", },
                    { "AABBBAA", " A   A ", " A   A ", " A   A ", " A   A ", " A   A ", " A   A ", " A   A ",
                        "AABBBAA", },
                    { "A A~A A", "  AAA  ", "  AAA  ", "  AAA  ", "  AAA  ", "  AAA  ", "  AAA  ", "  AAA  ",
                        "A AAA A" } }))
        .addElement(
            'A',
            buildHatchAdder(GT_MetaTileEntity_MultiAutoclave.class)
                .atLeast(InputBus, OutputBus, InputHatch, Maintenance, Muffler, Energy)
                .casingIndex(((GT_Block_Casings4) GregTech_API.sBlockCasings4).getTextureIndex(1))
                .dot(1)
                .buildAndChain(
                    onElementPass(
                        GT_MetaTileEntity_MultiAutoclave::onCasingAdded,
                        ofBlock(GregTech_API.sBlockCasings4, 1))))
        .addElement('B', Glasses.chainAllGlasses()) // Steel Casings
        .addElement('C', ofFrame(Materials.Polytetrafluoroethylene)) // PTFE Frame
        .addElement(
            'D',
            ofBlocksTiered(
                GT_MetaTileEntity_MultiAutoclave::getItemPipeTierFromMeta,
                ImmutableList.of(
                    Pair.of(GregTech_API.sBlockCasings11, 0),
                    Pair.of(GregTech_API.sBlockCasings11, 1),
                    Pair.of(GregTech_API.sBlockCasings11, 2),
                    Pair.of(GregTech_API.sBlockCasings11, 3),
                    Pair.of(GregTech_API.sBlockCasings11, 4),
                    Pair.of(GregTech_API.sBlockCasings11, 5),
                    Pair.of(GregTech_API.sBlockCasings11, 6),
                    Pair.of(GregTech_API.sBlockCasings11, 7)),
                -2,
                GT_MetaTileEntity_MultiAutoclave::setItemPipeTier,
                GT_MetaTileEntity_MultiAutoclave::getItemPipeTier))
        .addElement(
            'E',
            ofBlocksTiered(
                GT_MetaTileEntity_MultiAutoclave::getFluidTierFromMeta,
                ImmutableList.of(
                    Pair.of(GregTech_API.sBlockCasings2, 12),
                    Pair.of(GregTech_API.sBlockCasings2, 13),
                    Pair.of(GregTech_API.sBlockCasings2, 14),
                    Pair.of(GregTech_API.sBlockCasings2, 15)),
                -2,
                GT_MetaTileEntity_MultiAutoclave::setFluidPipeTier,
                GT_MetaTileEntity_MultiAutoclave::getFluidPipeTier))
        .addElement(
            'F',
            ofCoil(GT_MetaTileEntity_MultiAutoclave::setCoilLevel, GT_MetaTileEntity_MultiAutoclave::getCoilLevel))
        .build();

    @Override
    public IStructureDefinition<GT_MetaTileEntity_MultiAutoclave> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Lathe")
            .addInfo("Controller Block for the Industrial Precision Lathe")
            .addInfo("Gains 2 parallels per voltage tier,")
            .addInfo("and 4 parallels per pipe casing tier (16 for Black Plutonium)")
            .addInfo("Better pipe casings increase speed")
            .addInfo(AuthorVolence)
            .addSeparator()
            .beginStructureBlock(7, 5, 5, true)
            .addController("Front Center")
            .addCasingInfoMin("Solid Steel Machine Casing", 36, false)
            .addCasingInfoExactly("Steel Pipe Casing", 8, false)
            .addInputBus("Any of the 9 Solid Steel Casing at Each End", 1)
            .addOutputBus("Any of the 9 Solid Steel Casing at Each End", 1)
            .addEnergyHatch("Any Solid Steel Casing", 1)
            .addMaintenanceHatch("Any Solid Steel Casing", 1)
            .addMufflerHatch("Any Solid Steel Casing", 1)
            .addOtherStructurePart("4 Item Pipe Casings", "Center of the glass", 4)
            .toolTipFinisher("GregTech");
        return tt;
    }

    private int mCasingAmount;

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasingAmount = 0;
        mEnergyHatches.clear();
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 3, 6, 0)) return false;
        // if (mCasingAmount < 8) return false;
        getBaseMetaTileEntity().sendBlockEvent(GregTechTileClientEvents.CHANGE_CUSTOM_DATA, getUpdateData());
        return this.mMaintenanceHatches.size() == 1
            && mEnergyHatches.size() >= 1
            && mMufflerHatches.size() == 1;
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
                        .addIcon(OVERLAY_FRONT_MULTI_LATHE_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_LATHE_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings2, 0)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_LATHE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_LATHE_GLOW)
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
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 3, 6, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int build = survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 3, 6, 0, elementBudget, env, false, true);
        return build;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_MultiAutoclave(this.mName);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic()
            .setSpeedBonus(1F / 2F)
            .setEuModifier(0.8F)
            .setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    public int getMaxParallelRecipes() {
        return 16;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.autoclaveRecipes;
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
        return true;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }
}
