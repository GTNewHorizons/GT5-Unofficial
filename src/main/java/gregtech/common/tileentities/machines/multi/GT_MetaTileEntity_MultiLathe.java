package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.enums.GT_Values.AuthorVolence;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_LATHE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_LATHE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_LATHE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_LATHE_GLOW;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import gregtech.api.enums.GT_HatchElement;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.multitileentity.multiblock.casing.Glasses;
import gregtech.api.util.GT_OreDictUnificator;
import kubatech.loaders.BlockLoader;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
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
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Casings2;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;

public class GT_MetaTileEntity_MultiLathe extends GT_MetaTileEntity_EnhancedMultiBlockBase<GT_MetaTileEntity_MultiLathe>
    implements ISurvivalConstructable {
    private static final String STRUCTURE_PIECE_MAIN = "main";

    public enum PipeTiers {
        Platinum(8, 0.8F, 1F),
        Osmium(24, 0.75F, 1.25F),
        Quantium(48, 0.7F, 1.5F),
        FluxedElectrum(96, 0.6F, 2F),
        BlackPlutonium(256, 0.5F, 2.5F);

        final int maxParallel;
        final float euModifier, speedBoost;

        PipeTiers(int maxParallel, float euModifier, float speedBoost) {
            this.maxParallel = maxParallel;
            this.euModifier = euModifier;
            this.speedBoost = 1F / speedBoost;
        }
    }
    private static final Block PIPE_BLOCK_BASE = Block.getBlockFromName("gregtech:gt.blockmachines");
    private int mPipeTier = 0;

    private static final List<Pair<Block, Integer>> pipeTiers = Arrays.asList(
        Pair.of(PIPE_BLOCK_BASE, 5622),
        Pair.of(PIPE_BLOCK_BASE, 5632)
    );

    // get tier from block meta
    private static Integer getTierFromMeta(Block b, Integer metaID) {
        switch (metaID) {
            case 5622:  // Platinum Pipe
                return 1;
            case 5632:  // Osmium Pipe
                return 2;
            default:
                return -2;
        }
    }

    private void setPipeTier(int tier) {
        mPipeTier = tier;
    }

    private int getPipeTier() {
        return mPipeTier;
    }

    // Plat Small: 5621
    // Osmium Small: 5631
    // Quantium Small: 5731
    // Fluxed Electrum Small: 5651
    // Black Plutonium Small: 5661

    private static final IStructureDefinition<GT_MetaTileEntity_MultiLathe> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_MultiLathe>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            (transpose(
                new String[][] {
                    { "  A  ", "  A  ", "  A  ", "  A  ", "  A  ", "  A  ", "  A  " },
                    { " AAA ", " BBB ", " CCC ", " CCC ", " CCC ", " CCC ", " AAA " },
                    { "AA~AA", "ABEBA", " CFC ", " CFC ", " CFC ", " CFC ", " ADA " },
                    { " AAA ", " BBB ", " CCC ", " CCC ", " CCC ", " CCC ", " AAA " },
                    { "  A  ", "  A  ", "  A  ", "  A  ", "  A  ", "  A  ", "  A  " }
                }
            ))
        )
        .addElement(
            'A',
            buildHatchAdder(GT_MetaTileEntity_MultiLathe.class)
                .atLeast(InputBus, OutputBus, Maintenance, Muffler, Energy)
                .casingIndex(((GT_Block_Casings2) GregTech_API.sBlockCasings2).getTextureIndex(0))
                .dot(1)
                .buildAndChain(
                    onElementPass(
                        GT_MetaTileEntity_MultiLathe::onCasingAdded,
                        ofBlock(GregTech_API.sBlockCasings2, 0)
                    )
                )
        )
        .addElement('B', ofBlock(GregTech_API.sBlockCasings3, 10)) // Steel Casings
        .addElement('C', Glasses.chainAllGlasses()) // Glass
        .addElement('D', OutputBus.newAny(16, 4, ForgeDirection.SOUTH)) // Output Bus
        .addElement('E', InputBus.newAny(16, 5, ForgeDirection.SOUTH)) // Input Bus
        .addElement(
            'F',
            ofBlocksTiered(
                GT_MetaTileEntity_MultiLathe::getTierFromMeta,
                pipeTiers,
                -1,
                GT_MetaTileEntity_MultiLathe::setPipeTier,
                GT_MetaTileEntity_MultiLathe::getPipeTier
            )
        )
        .build();

    public GT_MetaTileEntity_MultiLathe(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_MultiLathe(String aName) {
        super(aName);
    }

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
            .addInfo("250% the speed of single block machines of the same voltage")
            .addInfo("Gains 8 parallels per voltage tier")
            .addInfo(AuthorVolence)
            .addSeparator()
            .beginStructureBlock(5, 5, 7, true)
            .addController("Front Center")
            .addCasingInfoMin("Solid Steel Machine Casing", 85, false)
            .addCasingInfoExactly("Steel Pipe Casing", 24, false)
            .addInputBus("Center Block Attached to Pipe Opposite of Output Bus, Any Other Solid Steel Casing", 1)
            .addOutputBus("Back Center Casing", 1)
            .addEnergyHatch("Any Solid Steel Casing", 1)
            .addMaintenanceHatch("Any Solid Steel Casing", 1)
            .addMufflerHatch("Any Solid Steel Casing")
            .addOtherStructurePart("Pipes", "Center of the glass, connecting the input and output bus", 4)
            .toolTipFinisher("GregTech");
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 2, 2, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 2, 2, 0, elementBudget, env, false, true);
    }

    private int mCasingAmount;

    private void onCasingAdded() {
        mCasingAmount++;
    }

    private int mPipeBlockAmount;

    private void onPipeBlockAdded() {
        mPipeBlockAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasingAmount = 0;
        mEnergyHatches.clear();
//        mPipeBlock = null;

        if (!checkPiece(STRUCTURE_PIECE_MAIN, 2, 2, 0)) return false;
        if (mCasingAmount < 8) return false;

        // All checks passed!
        return true;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setSpeedBonus(1F / 2F)
            .setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    public int getMaxParallelRecipes() {
        return (8 * GT_Utility.getTier(this.getMaxInputVoltage()));
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

}
