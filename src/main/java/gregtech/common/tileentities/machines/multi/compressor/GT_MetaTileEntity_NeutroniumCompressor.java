package gregtech.common.tileentities.machines.multi.compressor;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_Values.AuthorFourIsTheNumber;
import static gregtech.api.enums.GT_Values.Ollie;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_COMPRESSOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_COMPRESSOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_COMPRESSOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_COMPRESSOR_GLOW;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.multitileentity.multiblock.casing.Glasses;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Casings10;

public class GT_MetaTileEntity_NeutroniumCompressor
    extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<GT_MetaTileEntity_NeutroniumCompressor>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<GT_MetaTileEntity_NeutroniumCompressor> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_NeutroniumCompressor>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            // spotless:off
            transpose(new String[][]{
                {"  CEEEEEC  "," CE     EC ","CE       EC","E         E","E         E","E         E","E         E","E         E","CE       EC"," CE     EC ","  CEEEEEC  "},
                {" CE     EC ","C  BBBBB  C","E BBBBBBB E"," BBCCCCCBB "," BBCBBBCBB "," BBCCCCCBB "," BBCBBBCBB "," BBCCCCCBB ","E BBBBBBB E","C  BBBBB  C"," CE     EC "},
                {"CE       EC","E BBBBBBB E"," B       B "," D       D "," B       B "," B       B "," D       D "," B       B "," B       B ","E BDDDBDB E","CE       EC"},
                {"E         E"," BBAAAAABB "," B       B "," D       D "," B       B "," D       D "," D       D "," B       B "," B       B "," BBDBBBDBB ","E         E"},
                {"E         E"," BBAAAAABB "," B       B "," D       D "," B       B "," B       B "," D       D "," D       D "," D       D "," BDDBDDDDB ","E         E"},
                {"E         E"," BBAAAAABB "," D       D "," D       D "," D       D "," B       B "," B       B "," B       B "," B       B "," BBDBBBBDB ","E         E"},
                {"E         E"," BBAAAAABB "," B       B "," B       B "," D       D "," B       B "," B       B "," D       D "," B       B "," BBDBBDBBB ","E         E"},
                {"E         E"," BBAAAAABB "," B       B "," D       D "," D       D "," B       B "," D       D "," D       D "," D       D "," BBBBBDDDB ","E         E"},
                {"CE       EC","E BBBBBBB E"," B       B "," D       D "," B       B "," B       B "," D       D "," B       B "," B       B ","E BDDBBBB E","CE       EC"},
                {" CE     EC ","C  BB~BB  C","E BBBBBBB E"," BBBBBBBBB "," BBBBBBBBB "," BBBBBBBBB "," BBBBBBBBB "," BBBBBBBBB ","E BBBBBBB E","C  BBBBB  C"," CE     EC "},
                {"  CEEEEEC  "," CE     EC ","CE       EC","E         E","E         E","E         E","E         E","E         E","CE       EC"," CE     EC ","  CEEEEEC  "}
            }))
            //spotless:on
        .addElement('A', Glasses.chainAllGlasses())
        .addElement(
            'B',
            buildHatchAdder(GT_MetaTileEntity_NeutroniumCompressor.class)
                .atLeast(InputBus, OutputBus, Maintenance, Energy)
                .casingIndex(((GT_Block_Casings10) GregTech_API.sBlockCasings10).getTextureIndex(6))
                .dot(1)
                .buildAndChain(
                    onElementPass(
                        GT_MetaTileEntity_NeutroniumCompressor::onCasingAdded,
                        ofBlock(GregTech_API.sBlockCasings10, 6))))
        .addElement('C', ofBlock(GregTech_API.sBlockCasings10, 8))
        .addElement('D', ofBlock(GregTech_API.sBlockCasings10, 7))
        .addElement('E', ofFrame(Materials.NaquadahAlloy))
        .build();

    public GT_MetaTileEntity_NeutroniumCompressor(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_NeutroniumCompressor(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_NeutroniumCompressor> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_NeutroniumCompressor(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings10, 6)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_COMPRESSOR_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_COMPRESSOR_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings10, 6)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_COMPRESSOR)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_COMPRESSOR_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings10, 6)) };
        }
        return rTexture;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Neutronium Compressor")
            .addInfo("Controller Block for the Neutronium Compressor")
            .addInfo("Has a static 8 parallels")
            .addInfo("Capable of compressing matter into " + EnumChatFormatting.GOLD + "singularities")
            .addInfo("More advanced singularities will require even stronger compression...")
            .addInfo(AuthorFourIsTheNumber + EnumChatFormatting.RESET + " & " + Ollie)
            .addSeparator()
            .beginStructureBlock(11, 11, 11, true)
            .addController("Front Center")
            .addCasingInfoMin("Neutronium Casing", 220, false)
            .addCasingInfoExactly("Active Neutronium Casing", 63, false)
            .addCasingInfoExactly("Any Glass", 25, false)
            .addCasingInfoExactly("Naquadah Alloy Frame Box", 108, false)
            .addCasingInfoExactly("Neutronium Stabilization Casing", 67, false)
            .addInputBus("Any Neutronium Casing", 1)
            .addOutputBus("Any Neutronium Casing", 1)
            .addEnergyHatch("Any Neutronium Casing", 1)
            .addMaintenanceHatch("Any Neutronium Casing", 1)
            .toolTipFinisher("GregTech");
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 5, 9, 1);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 5, 9, 1, elementBudget, env, false, true);
    }

    private int mCasingAmount;

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasingAmount = 0;

        return (checkPiece(STRUCTURE_PIECE_MAIN, 5, 9, 1)) && mCasingAmount >= 220;
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(GT_Utility.roundUpVoltage(this.getMaxInputVoltage()));
        logic.setAvailableAmperage(1L);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {

        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GT_Recipe recipe) {
                if (recipe.mSpecialValue > 0) {
                    return CheckRecipeResultRegistry.NO_BLACK_HOLE;
                }
                return super.validateRecipe(recipe);
            }
        }.setMaxParallel(8);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.neutroniumCompressorRecipes;
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
