package gregtech.common.tileentities.machines.multi.compressor;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.GTAuthors.Ollie;
import static gregtech.api.enums.HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_COMPRESSOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_COMPRESSOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_COMPRESSOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_COMPRESSOR_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

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
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.metadata.CompressionTierKey;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings10;
import gregtech.common.misc.GTStructureChannels;

public class MTEIndustrialCompressor extends MTEExtendedPowerMultiBlockBase<MTEIndustrialCompressor>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTEIndustrialCompressor> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEIndustrialCompressor>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            // spotless:off
            transpose(new String[][]{
                {"       ","  DDD  "," DDDDD "," DDDDD "," DDDDD ","  DDD  ","       "},
                {"  BBB  "," BBBBB ","BBBBBBB","BBBBBBB","BBBBBBB"," BBBBB ","  BBB  "},
                {"  DDD  "," B   B ","B     B","B     B","B     B"," B   B ","  DDD  "},
                {"  DAD  "," B   B ","C     C","C     C","C     C"," B   B ","  DAD  "},
                {"  DAD  "," B   B ","C     C","C     C","C     C"," B   B ","  DAD  "},
                {"  DAD  "," B   B ","C     C","C     C","C     C"," B   B ","  DAD  "},
                {"  DDD  "," B   B ","B     B","B     B","B     B"," B   B ","  DDD  "},
                {"  B~B  "," BBBBB ","BBBBBBB","BBBBBBB","BBBBBBB"," BBBBB ","  BBB  "}
            }))
            //spotless:on
        .addElement(
            'C',
            buildHatchAdder(MTEIndustrialCompressor.class).atLeast(InputBus, OutputBus, InputHatch)
                .casingIndex(((BlockCasings10) GregTechAPI.sBlockCasings10).getTextureIndex(5))
                .hint(2)
                .buildAndChain(
                    onElementPass(MTEIndustrialCompressor::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings10, 5))))
        .addElement(
            'B',
            buildHatchAdder(MTEIndustrialCompressor.class).atLeast(Maintenance, Energy)
                .casingIndex(((BlockCasings10) GregTechAPI.sBlockCasings10).getTextureIndex(4))
                .hint(1)
                .buildAndChain(
                    onElementPass(MTEIndustrialCompressor::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings10, 4))))
        .addElement('A', chainAllGlasses())
        .addElement('D', ofBlock(GregTechAPI.sBlockCasings10, 5))
        .build();

    public MTEIndustrialCompressor(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEIndustrialCompressor(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTEIndustrialCompressor> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialCompressor(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings10, 4)),
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
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings10, 4)),
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
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings10, 4)) };
        }
        return rTexture;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Compressor, LEC")
            .addBulkMachineInfo(2, 2f, 0.9f)
            .beginStructureBlock(7, 8, 7, true)
            .addController("Front Center")
            .addCasingInfoMin("Electric Compressor Casing", 95, false)
            .addCasingInfoMin("Compressor Pipe Casing", 45, false)
            .addCasingInfoExactly("Any Tiered Glass", 6, false)
            .addInputBus("Pipe Casings on Side", 2)
            .addInputHatch("Pipe Casings on Side", 2)
            .addOutputBus("Pipe Casings on Side", 2)
            .addEnergyHatch("Any Electric Compressor Casing", 1)
            .addMaintenanceHatch("Any Electric Compressor Casing", 1)
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .toolTipFinisher(Ollie);
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 3, 7, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 3, 7, 0, elementBudget, env, false, true);
    }

    private int mCasingAmount;

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasingAmount = 0;
        return checkPiece(STRUCTURE_PIECE_MAIN, 3, 7, 0) && mCasingAmount >= 95;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                if (recipe.getMetadataOrDefault(CompressionTierKey.INSTANCE, 0) > 0)
                    return CheckRecipeResultRegistry.NO_RECIPE;
                return super.validateRecipe(recipe);
            }
        }.noRecipeCaching()
            .setSpeedBonus(1F / 2F)
            .setMaxParallelSupplier(this::getTrueParallel)
            .setEuModifier(0.9F);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (2 * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.compressorRecipes;
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
