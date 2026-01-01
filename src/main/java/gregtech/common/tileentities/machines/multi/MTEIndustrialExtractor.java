package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_INDUSTRIAL_EXTRACTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_INDUSTRIAL_EXTRACTOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_INDUSTRIAL_EXTRACTOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_INDUSTRIAL_EXTRACTOR_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;

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
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.tooltip.TooltipTier;
import gregtech.common.blocks.BlockCasings4;
import gregtech.common.misc.GTStructureChannels;

public class MTEIndustrialExtractor extends MTEExtendedPowerMultiBlockBase<MTEIndustrialExtractor>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTEIndustrialExtractor> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEIndustrialExtractor>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            (new String[][] { { "CCCCC", "C   C", "C   C", "C   C", "CC~CC" },
                { "CCCCC", " BBB ", " AAA ", " BBB ", "CCCCC" }, { "CCCCC", " BBB ", " ABA ", " BBB ", "CCCCC" },
                { "CCCCC", " BBB ", " AAA ", " BBB ", "CCCCC" }, { "CCCCC", "C   C", "C   C", "C   C", "CCCCC" } }))
        .addElement(
            'C',
            buildHatchAdder(MTEIndustrialExtractor.class).atLeast(InputBus, OutputBus, Maintenance, Energy)
                .casingIndex(((BlockCasings4) GregTechAPI.sBlockCasings4).getTextureIndex(1))
                .hint(1)
                .buildAndChain(
                    onElementPass(MTEIndustrialExtractor::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings4, 1))))
        .addElement(
            'B',
            GTStructureChannels.ITEM_PIPE_CASING.use(
                ofBlocksTiered(
                    MTEIndustrialExtractor::getItemPipeTierFromMeta,
                    ImmutableList.of(
                        Pair.of(GregTechAPI.sBlockCasings11, 0),
                        Pair.of(GregTechAPI.sBlockCasings11, 1),
                        Pair.of(GregTechAPI.sBlockCasings11, 2),
                        Pair.of(GregTechAPI.sBlockCasings11, 3),
                        Pair.of(GregTechAPI.sBlockCasings11, 4),
                        Pair.of(GregTechAPI.sBlockCasings11, 5),
                        Pair.of(GregTechAPI.sBlockCasings11, 6),
                        Pair.of(GregTechAPI.sBlockCasings11, 7)),
                    -1,
                    MTEIndustrialExtractor::setItemPipeTier,
                    MTEIndustrialExtractor::getItemPipeTier)))
        .addElement('A', chainAllGlasses())
        .build();

    private int itemPipeTier = -1;

    @Nullable
    private static Integer getItemPipeTierFromMeta(Block block, Integer metaID) {
        if (block != GregTechAPI.sBlockCasings11) return null;
        if (metaID < 0 || metaID > 7) return null;
        return metaID + 1;
    }

    private void setItemPipeTier(int tier) {
        itemPipeTier = tier;
    }

    private int getItemPipeTier() {
        return itemPipeTier;
    }

    public MTEIndustrialExtractor(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEIndustrialExtractor(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTEIndustrialExtractor> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialExtractor(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings4, 1)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_INDUSTRIAL_EXTRACTOR_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_INDUSTRIAL_EXTRACTOR_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings4, 1)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_INDUSTRIAL_EXTRACTOR)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_INDUSTRIAL_EXTRACTOR_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings4, 1)) };
        }
        return rTexture;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Extractor")
            .addDynamicParallelInfo(8, TooltipTier.ITEM_PIPE_CASING)
            .addStaticSpeedInfo(3F)
            .addStaticEuEffInfo(0.85F)
            .beginStructureBlock(5, 5, 5, false)
            .addController("Front Center")
            .addCasingInfoMin("Stainless Steel Machine Casing", 45, false)
            .addCasingInfoExactly("Item Pipe Casing", 19, true)
            .addCasingInfoExactly("Any Tiered Glass", 8, false)
            .addInputBus("Any Stainless Steel Casing", 1)
            .addOutputBus("Any Stainless Steel Casing", 1)
            .addEnergyHatch("Any Stainless Steel Casing", 1)
            .addMaintenanceHatch("Any Stainless Steel Casing", 1)
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .addSubChannelUsage(GTStructureChannels.ITEM_PIPE_CASING)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 2, 4, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 2, 4, 0, elementBudget, env, false, true);
    }

    private int mCasingAmount;

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasingAmount = 0;
        itemPipeTier = -1;

        if (!checkPiece(STRUCTURE_PIECE_MAIN, 2, 4, 0)) return false;
        return mCasingAmount >= 45;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().noRecipeCaching()
            .setSpeedBonus(1F / 3F)
            .setMaxParallelSupplier(this::getTrueParallel)
            .setEuModifier(0.85F);
    }

    @Override
    public int getMaxParallelRecipes() {
        // Max call to prevent seeing -16 parallels in waila for unformed multi
        return Math.max(8 * itemPipeTier, 0);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.extractorRecipes;
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -10;
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
