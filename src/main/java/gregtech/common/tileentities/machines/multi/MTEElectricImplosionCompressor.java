package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_IMPLOSION_COMPRESSOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_IMPLOSION_COMPRESSOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_IMPLOSION_COMPRESSOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_IMPLOSION_COMPRESSOR_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;

import bartworks.API.recipe.BartWorksRecipeMaps;
import fox.spiteful.avaritia.blocks.LudicrousBlocks;
import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.ErrorType;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrorRegistry;
import gregtech.api.structure.error.StructureErrors;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.misc.GTStructureChannels;

public class MTEElectricImplosionCompressor extends MTEExtendedPowerMultiBlockBase<MTEElectricImplosionCompressor>
    implements ISurvivalConstructable {

    private int pistonTier = 0;
    private int casingAmount;
    private static final int OFFSET_X = 7;
    private static final int OFFSET_Y = 5;
    private static final int OFFSET_Z = 1;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private int glassTier = -1;

    public MTEElectricImplosionCompressor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEElectricImplosionCompressor(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MTEElectricImplosionCompressor(this.mName);
    }

    public static ImmutableList<Pair<Block, Integer>> getTierBlockList() {
        ImmutableList.Builder<Pair<Block, Integer>> builder = ImmutableList.builder();

        builder.add(Pair.of(GregTechAPI.sBlockMetal5, 2));

        if (Mods.Avaritia.isModLoaded()) {
            builder.add(Pair.of(LudicrousBlocks.resource_block, 1));
        }

        builder.add(Pair.of(GregTechAPI.sBlockMetal9, 4));
        builder.add(Pair.of(GregTechAPI.sBlockMetal9, 3));
        builder.add(Pair.of(GregTechAPI.sBlockMetal9, 8));

        return builder.build();
    }

    @Nullable
    public static Integer getTierBlock(Block block, int meta) {
        if (block == null) return null;
        if (block == GregTechAPI.sBlockMetal5 && meta == 2) return 1;

        if (Mods.Avaritia.isModLoaded()) {
            if (block == LudicrousBlocks.resource_block && meta == 1) return 2;
            if (block == GregTechAPI.sBlockMetal9 && meta == 4) return 3;
            if (block == GregTechAPI.sBlockMetal9 && meta == 3) return 4;
            if (block == GregTechAPI.sBlockMetal9 && meta == 8) return 5;
        } else {
            if (block == GregTechAPI.sBlockMetal9 && meta == 4) return 2;
            if (block == GregTechAPI.sBlockMetal9 && meta == 3) return 3;
            if (block == GregTechAPI.sBlockMetal9 && meta == 8) return 4;
        }
        return null;
    }

    // Lazy allocation needed to fix piston tiers not appearing in NEI viewer
    private static IStructureDefinition<MTEElectricImplosionCompressor> STRUCTURE_DEFINITION = null;

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Implosion Compressor, EIC")
            .addInfo("Explosions are fun!")
            .addInfo("Uses electricity instead of Explosives")
            .addInfo(
                EnumChatFormatting.GOLD + "Parallels"
                    + EnumChatFormatting.GRAY
                    + " are determined by "
                    + EnumChatFormatting.WHITE
                    + "Containment Block"
                    + EnumChatFormatting.GRAY
                    + " Tier")
            .addInfo(createParallelText(EnumChatFormatting.WHITE, "Neutronium", 1))
            .addInfo(createParallelText(EnumChatFormatting.RED, "Infinity", 4))
            .addInfo(createParallelText(EnumChatFormatting.DARK_GRAY, "Transcendent Metal", 16))
            .addInfo(createParallelText(EnumChatFormatting.LIGHT_PURPLE, "Spacetime", 64))
            .addInfo(createParallelText(EnumChatFormatting.DARK_AQUA, "Universium", 256))
            .addInfo("Energy Hatch Tier is limited by Glass Tier")
            .addInfo(
                GTUtility.getColoredTierNameFromTier((byte) 12) + EnumChatFormatting.GRAY
                    + "-glass unlocks all above energy tiers")
            .addMaxTierSkips(1)
            .addTecTechHatchInfo()
            .beginStructureBlock(15, 7, 7, false)
            .addController("Front bottom center")
            .addCasingInfoMin("Naquadah Reinforced Block", 230, false)
            .addCasingInfoExactly("Naquadah Frame Box", 2, false)
            .addCasingInfoExactly("PTFE Pipe Casing", 20, false)
            .addCasingInfoExactly("Any Tiered Glass", 22, true)
            .addCasingInfoExactly("Robust Tungstensteel Machine Casing", 36, false)
            .addCasingInfoExactly("Containment Blocks", 24, true)
            .addMaintenanceHatch("Any Naquadah Reinforced Block", 1)
            .addInputBus("Any Naquadah Reinforced Block", 1)
            .addInputHatch("Any Naquadah Reinforced Block", 1)
            .addOutputBus("Any Naquadah Reinforced Block", 1)
            .addEnergyHatch("Any Naquadah Reinforced Block", 1)
            .addSubChannelUsage(GTStructureChannels.EIC_PISTON)
            .addStructureAuthors(EnumChatFormatting.GOLD + "Pix3lated")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<MTEElectricImplosionCompressor> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEElectricImplosionCompressor>builder()
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    new String[][] {
                        { "               ", "F F         F F", "F F         F F", "F F         F F", "F F         F F",
                            "F F         F F", "               " },
                        { "F F         F F", "F F   FFF   F F", "FBF  FFAFF  FBF", "FBF  AAAAA  FBF", "FBF  FFAFF  FBF",
                            "F F   F~F   F F", "F F         F F" },
                        { "F F         F F", "FBF  FFCFF  FBF", "F FFFEEEEEFFF F", "F FAA     AAF F", "F FFFEEEEEFFF F",
                            "FBF  FFCFF  FBF", "F F         F F" },
                        { "F F         F F", "FBFFFFCCCFFFFBF", "F FBBBEDEBBBF F", "F F         F F", "F FBBBEDEBBBF F",
                            "FBFFFFCCCFFFFBF", "F F         F F" },
                        { "F F         F F", "FBF  FFCFF  FBF", "F FFFEEEEEFFF F", "F FAA     AAF F", "F FFFEEEEEFFF F",
                            "FBF  FFCFF  FBF", "F F         F F" },
                        { "F F         F F", "F F   FFF   F F", "FBF  FFAFF  FBF", "FBF  AAAAA  FBF", "FBF  FFAFF  FBF",
                            "F F   FFF   F F", "F F         F F" },
                        { "               ", "F F         F F", "F F         F F", "F F         F F", "F F         F F",
                            "F F         F F", "               " } })
                .addElement('A', chainAllGlasses(-1, (te, t) -> te.glassTier = t, te -> te.glassTier))
                .addElement('B', Casings.RobustTungstenSteelMachineCasing.asElement())
                .addElement('C', Casings.PTFEPipeCasing.asElement())
                .addElement('D', ofFrame(Materials.Naquadah))
                .addElement(
                    'E',
                    GTStructureChannels.EIC_PISTON.use(
                        StructureUtility.ofBlocksTiered(
                            MTEElectricImplosionCompressor::getTierBlock,
                            getTierBlockList(),
                            -1,
                            (t, m) -> t.pistonTier = m,
                            t -> t.pistonTier)))
                .addElement(
                    'F',
                    buildHatchAdder(MTEElectricImplosionCompressor.class)
                        .atLeast(Energy.or(ExoticEnergy), InputBus, OutputBus, Maintenance, InputHatch, OutputHatch)
                        .casingIndex(Casings.NaquadahReinforcedBlock.textureId)
                        .hint(1)
                        .buildAndChain(
                            onElementPass(x -> ++x.casingAmount, Casings.NaquadahReinforcedBlock.asElement())))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return BartWorksRecipeMaps.electricImplosionCompressorRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (int) GTUtility.powInt(4, Math.max(pistonTier - 1, 0));
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack itemStack,
        List<StructureError> errors) {
        int mMaxHatchTier = 0;
        casingAmount = 0;
        pistonTier = -1;
        glassTier = -1;

        if (!checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z, errors)) return;

        List<MTEHatch> energyHatches = getExoticAndNormalEnergyHatchList();
        for (MTEHatch hatch : energyHatches) {
            if (glassTier < VoltageIndex.UMV && hatch.mTier > glassTier) {
                errors.add(StructureErrorRegistry.ENERGY_TIER_EXCEED_GLASS);
                break;
            }
            mMaxHatchTier = Math.max(mMaxHatchTier, hatch.mTier);
        }
        if (energyHatches.isEmpty()) {
            errors.add(StructureErrors.hatchCount(ErrorType.TOO_FEW, Energy, 0, 1));
        }
        checkCasingMin(errors, casingAmount, 230);
        checkHasAnyInput(errors);
        checkHasAnyOutput(errors);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int aColorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Casings.NaquadahReinforcedBlock.getCasingTexture(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_IMPLOSION_COMPRESSOR_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_IMPLOSION_COMPRESSOR_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Casings.NaquadahReinforcedBlock.getCasingTexture(), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_IMPLOSION_COMPRESSOR)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_IMPLOSION_COMPRESSOR_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Casings.NaquadahReinforcedBlock.getCasingTexture() };
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        buildPiece(STRUCTURE_PIECE_MAIN, itemStack, b, OFFSET_X, OFFSET_Y, OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            OFFSET_X,
            OFFSET_Y,
            OFFSET_Z,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    private String createParallelText(EnumChatFormatting blockColor, String block, int parallels) {
        return String.format(
            "%s%s%s : %s%d%s %s",
            blockColor,
            block,
            EnumChatFormatting.GRAY,
            EnumChatFormatting.GOLD,
            parallels,
            EnumChatFormatting.GRAY,
            parallels == 1 ? "Parallel" : "Parallels");
    }
}
