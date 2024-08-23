package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.Muffler;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_Values.AuthorVolence;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_LATHE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_LATHE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_LATHE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_LATHE_GLOW;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

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
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.GregTechTileClientEvents;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.multitileentity.multiblock.casing.Glasses;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Casings2;

public class GT_MetaTileEntity_MultiLathe extends GT_MetaTileEntity_EnhancedMultiBlockBase<GT_MetaTileEntity_MultiLathe>
    implements ISurvivalConstructable {

    public GT_MetaTileEntity_MultiLathe(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_MultiLathe(String aName) {
        super(aName);
    }

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String STRUCTURE_PIECE_BODY = "body";
    private static final String STRUCTURE_PIECE_BODY_ALT = "body_alt";

    protected int pipeTier = 0;

    public enum PipeTiers {

        Platinum(4, 1F),
        Osmium(8, 1.25F),
        Quantium(12, 1.5F),
        FluxedElectrum(16, 2F),
        BlackPlutonium(32, 2.5F);

        final int maxParallel;
        final float speedBoost;

        PipeTiers(int maxParallel, float speedBoost) {
            this.maxParallel = maxParallel;
            this.speedBoost = speedBoost;
        }
    }

    private PipeTiers getPipeData() {
        pipeTier = getPipeTier();
        return switch (pipeTier) {
            case 2 -> PipeTiers.Osmium;
            case 3 -> PipeTiers.Quantium;
            case 4 -> PipeTiers.FluxedElectrum;
            case 5 -> PipeTiers.BlackPlutonium;
            default -> PipeTiers.Platinum;
        };
    }

    // get tier from block meta
    private static Integer getTierFromMeta(Block block, Integer metaID) {
        if (block != GregTech_API.sBlockCasings11) return -1;
        if (metaID < 3 || metaID > 7) return -1;
        return (metaID - 2);
    }

    private void setPipeTier(int tier) {
        pipeTier = tier;
    }

    private int getPipeTier() {
        return pipeTier;
    }

    private static final IStructureDefinition<GT_MetaTileEntity_MultiLathe> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_MultiLathe>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(new String[][] { { "       " }, { "       " }, { "       " }, { "       " }, { "AAA~AAA" } }))
        .addShape(
            STRUCTURE_PIECE_BODY,
            (transpose(
                new String[][] { { "       ", "AAAAAAA", "       ", "       " },
                    { "DBCCCCD", "DBCCCCD", "DBCCCCD", "       " }, { "DBCCCCD", "DBFFFFD", "DBCCCCD", "       " },
                    { "DBCCCCD", "DBCCCCD", "DBCCCCD", "       " }, { "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA" } })))
        .addShape(
            STRUCTURE_PIECE_BODY_ALT,
            (transpose(
                new String[][] { { "       ", "AAAAAAA", "       ", "       " },
                    { "DCCCCBD", "DCCCCBD", "DCCCCBD", "       " }, { "DCCCCBD", "DFFFFBD", "DCCCCBD", "       " },
                    { "DCCCCBD", "DCCCCBD", "DCCCCBD", "       " }, { "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA" } })))
        .addElement(
            'A',
            buildHatchAdder(GT_MetaTileEntity_MultiLathe.class).atLeast(Maintenance, Muffler, Energy)
                .casingIndex(((GT_Block_Casings2) GregTech_API.sBlockCasings2).getTextureIndex(0))
                .dot(1)
                .buildAndChain(
                    onElementPass(
                        GT_MetaTileEntity_MultiLathe::onCasingAdded,
                        ofBlock(GregTech_API.sBlockCasings2, 0))))
        .addElement('B', ofBlock(GregTech_API.sBlockCasings3, 10)) // Steel Casings
        .addElement('C', Glasses.chainAllGlasses()) // Glass
        .addElement(
            'D',
            buildHatchAdder(GT_MetaTileEntity_MultiLathe.class)
                .atLeast(InputBus, OutputBus, Maintenance, Muffler, Energy)
                .casingIndex(((GT_Block_Casings2) GregTech_API.sBlockCasings2).getTextureIndex(0))
                .dot(1)
                .buildAndChain(
                    onElementPass(
                        GT_MetaTileEntity_MultiLathe::onCasingAdded,
                        ofBlock(GregTech_API.sBlockCasings2, 0))))
        .addElement(
            'F',
            ofBlocksTiered(
                GT_MetaTileEntity_MultiLathe::getTierFromMeta,
                ImmutableList.of(
                    Pair.of(GregTech_API.sBlockCasings11, 3),
                    Pair.of(GregTech_API.sBlockCasings11, 4),
                    Pair.of(GregTech_API.sBlockCasings11, 5),
                    Pair.of(GregTech_API.sBlockCasings11, 6),
                    Pair.of(GregTech_API.sBlockCasings11, 7)),
                -2,
                GT_MetaTileEntity_MultiLathe::setPipeTier,
                GT_MetaTileEntity_MultiLathe::getPipeTier))
        .build();

    @Override
    public IStructureDefinition<GT_MetaTileEntity_MultiLathe> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_MultiLathe(this.mName);
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

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 3, 4, 0);
        buildPiece(STRUCTURE_PIECE_BODY, stackSize, hintsOnly, 3, 4, -1);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int build = survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 3, 4, 0, elementBudget, env, false, true);
        if (build >= 0) return build;
        build = survivialBuildPiece(STRUCTURE_PIECE_BODY, stackSize, 3, 4, -1, elementBudget, env, false, true);
        if (build >= 0) return build;
        build = survivialBuildPiece(STRUCTURE_PIECE_BODY_ALT, stackSize, 3, 4, -1, elementBudget, env, false, true);
        return build;
    }

    private int mCasingAmount;

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        pipeTier = -2;
        mEnergyHatches.clear();
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 3, 4, 0)) return false;
        getBaseMetaTileEntity().sendBlockEvent(GregTechTileClientEvents.CHANGE_CUSTOM_DATA, getUpdateData());
        if (!checkPiece(STRUCTURE_PIECE_BODY, 3, 4, -1) && !checkPiece(STRUCTURE_PIECE_BODY_ALT, 3, 4, -1))
            return false;
        return this.mMaintenanceHatches.size() == 1 && pipeTier >= -1
            && mEnergyHatches.size() >= 1
            && mInputBusses.size() >= 1
            && mMufflerHatches.size() == 1;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic()
            .setSpeedBonus(1F / (getPipeData().speedBoost + GT_Utility.getTier(this.getMaxInputVoltage()) / 4F))
            .setEuModifier(0.8F)
            .setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    public int getMaxParallelRecipes() {
        return getPipeData().maxParallel + (GT_Utility.getTier(this.getMaxInputVoltage()) * 2);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.latheRecipes;
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

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(GT_Utility.roundUpVoltage(this.getMaxInputVoltage()));
        logic.setAvailableAmperage(1L);
    }
}
