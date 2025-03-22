package gregtech.api.casing;

import net.minecraft.block.Block;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.util.data.BlockSupplier;

import gregtech.api.GregTechAPI;
import gtPlusPlus.core.block.ModBlocks;
import tectech.thing.block.BlockQuantumGlass;
import tectech.thing.casing.BlockGTCasingsTT;
import tectech.thing.casing.TTCasingsContainer;

public enum Casings implements ICasing {

    // spotless:off
    // I know this indenting looks weird, but I think it makes it easier to read because everything is aligned

    // For some reason a couple of these blockCasings2Misc casings don't register their textures in the expected indices
    // Instead, block casings 5 uses these indices
    // Why? Idk, blame alk or something
    ThermalProcessingCasing
        (() -> ModBlocks.blockCasings2Misc, 0, gtpp(1, 0)),
    HastelloyNSealantBlock
        (() -> ModBlocks.blockCasings2Misc, 1, gtpp(1, 1)),
    HastelloyXStructuralBlock
        (() -> ModBlocks.blockCasings2Misc, 2, gtpp(1, 2)),
    IncoloyDSFluidContainmentBlock
        (() -> ModBlocks.blockCasings2Misc, 3, gtpp(1, 3)),
    WashPlantCasing
        (() -> ModBlocks.blockCasings2Misc, 4, -1),
    IndustrialSieveCasing
        (() -> ModBlocks.blockCasings2Misc, 5, gtpp(1, 5)),
    LargeSieveGrate
        (() -> ModBlocks.blockCasings2Misc, 6, gtpp(1, 6)),
    VanadiumRedoxPowerCellEV
        (() -> ModBlocks.blockCasings2Misc, 7, gtpp(1, 7)),
    SubStationExternalCasing
        (() -> ModBlocks.blockCasings2Misc, 8, gtpp(1, 8)),
    CyclotronCoil
        (() -> ModBlocks.blockCasings2Misc, 9, gtpp(1, 9)),
    CyclotronOuterCasing
        (() -> ModBlocks.blockCasings2Misc, 10, -1),
    ThermalContainmentCasing
        (() -> ModBlocks.blockCasings2Misc, 11, -1),
    BulkProductionFrame
        (() -> ModBlocks.blockCasings2Misc, 12, -1),
    CuttingFactoryFrame
        (() -> ModBlocks.blockCasings2Misc, 13, gtpp(1, 13)),
    SterileFarmCasing
        (() -> ModBlocks.blockCasings2Misc, 15, gtpp(1, 15)),

    TinItemPipeCasing
        (() -> GregTechAPI.sBlockCasings11, 0, gt(16, 64 + 0)),
    BrassItemPipeCasing
        (() -> GregTechAPI.sBlockCasings11, 1, gt(16, 64 + 1)),
    ElectrumItemPipeCasing
        (() -> GregTechAPI.sBlockCasings11, 2, gt(16, 64 + 2)),
    PlatinumItemPipeCasing
        (() -> GregTechAPI.sBlockCasings11, 3, gt(16, 64 + 3)),
    OsmiumItemPipeCasing
        (() -> GregTechAPI.sBlockCasings11, 4, gt(16, 64 + 4)),
    QuantiumItemPipeCasing
        (() -> GregTechAPI.sBlockCasings11, 5, gt(16, 64 + 5)),
    FluxedElectrumItemPipeCasing
        (() -> GregTechAPI.sBlockCasings11, 6, gt(16, 64 + 6)),
    BlackPlutoniumItemPipeCasing
        (() -> GregTechAPI.sBlockCasings11, 7, gt(16, 64 + 7)),

    ChemicalGradeGlass
        (() -> GregTechAPI.sBlockGlass1, 0, gt(16, 0)),
    ElectronPermeableNeutroniumCoatedGlass
        (() -> GregTechAPI.sBlockGlass1, 1, gt(16, 1)),
    OmniPurposeInfinityFusedGlass
        (() -> GregTechAPI.sBlockGlass1, 2, gt(16, 2)),
    NonPhotonicMatterExclusionGlass
        (() -> GregTechAPI.sBlockGlass1, 3, gt(16, 3)),
    HawkingRadiationRealignmentFocus
        (() -> GregTechAPI.sBlockGlass1, 4, gt(16, 4)),

    FusionMachineCasingMKIV
        (() -> ModBlocks.blockCasings6Misc, 0, gtpp(3, 4)),
    AdvancedFusionCoilII
        (() -> ModBlocks.blockCasings6Misc, 1, gtpp(3, 5)),

    QuantumGlass
        (() -> BlockQuantumGlass.INSTANCE, 0, -1),

    HighPowerCasing
        (() -> TTCasingsContainer.sBlockCasingsTT, 0, tt(0)),
    ComputerCasing
        (() -> TTCasingsContainer.sBlockCasingsTT, 1, tt(1)),
    ComputerHeatVent
        (() -> TTCasingsContainer.sBlockCasingsTT, 2, tt(2)),
    AdvancedComputerCasing
        (() -> TTCasingsContainer.sBlockCasingsTT, 3, tt(3)),
    MolecularCasing
        (() -> TTCasingsContainer.sBlockCasingsTT, 4, tt(4)),
    AdvancedMolecularCasing
        (() -> TTCasingsContainer.sBlockCasingsTT, 5, tt(5)),
    ContainmentFieldGenerator
        (() -> TTCasingsContainer.sBlockCasingsTT, 6, tt(6)),
    MolecularCoil
        (() -> TTCasingsContainer.sBlockCasingsTT, 7, tt(7)),
    HollowCasing
        (() -> TTCasingsContainer.sBlockCasingsTT, 8, tt(8)),
    SpacetimeAlteringCasing
        (() -> TTCasingsContainer.sBlockCasingsTT, 9, tt(9)),
    TeleportationCasing
        (() -> TTCasingsContainer.sBlockCasingsTT, 10, tt(10)),
    DimensionalBridgeGenerator
        (() -> TTCasingsContainer.sBlockCasingsTT, 11, tt(11)),
    UltimateMolecularCasing
        (() -> TTCasingsContainer.sBlockCasingsTT, 12, tt(12)),
    UltimateAdvancedMolecularCasing
        (() -> TTCasingsContainer.sBlockCasingsTT, 13, tt(13)),
    UltimateContainmentFieldGenerator
        (() -> TTCasingsContainer.sBlockCasingsTT, 14, tt(14)),
    ;
    // spotless:on

    public final BlockSupplier blockGetter;
    public final int meta;
    public final int textureId;

    Casings(BlockSupplier blockGetter, int meta, int textureId) {
        this.blockGetter = blockGetter;
        this.meta = meta;
        this.textureId = textureId;
    }

    @Override
    public @NotNull Block getBlock() {
        return blockGetter.get();
    }

    @Override
    public int getBlockMeta() {
        return meta;
    }

    @Override
    public int getTextureId() {
        if (textureId == -1) {
            throw new UnsupportedOperationException(
                "Casing " + name() + " does not have a casing texture; The result of getTextureId() is undefined.");
        }

        return textureId;
    }

    private static int gt(int page, int id) {
        return (page << 7) | id;
    }

    private static int gtpp(int page, int id) {
        int aRealID = id + (page * 16);
        return 64 + aRealID;
    }

    private static int tt(int id) {
        return BlockGTCasingsTT.textureOffset + id;
    }
}
