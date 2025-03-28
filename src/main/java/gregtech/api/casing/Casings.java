package gregtech.api.casing;

import net.minecraft.block.Block;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.util.data.BlockSupplier;
import com.gtnewhorizon.structurelib.structure.IStructureElement;

import bartworks.API.BorosilicateGlass;
import cpw.mods.fml.common.registry.GameRegistry;
import goodgenerator.loader.Loaders;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Mods;
import gregtech.api.structure.IStructureProvider;
import gregtech.api.util.GTUtility;
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

    YellowStripesBlockA
        (() -> GregTechAPI.sBlockCasings3, 0, gt(0, 32 + 0)),
    YellowStripesBlockB
        (() -> GregTechAPI.sBlockCasings3, 1, gt(0, 32 + 1)),
    RadioactiveHazardSignBlock
        (() -> GregTechAPI.sBlockCasings3, 2, gt(0, 32 + 2)),
    BioHazardSignBlock
        (() -> GregTechAPI.sBlockCasings3, 3, gt(0, 32 + 3)),
    ExplosionHazardSignBlock
        (() -> GregTechAPI.sBlockCasings3, 4, gt(0, 32 + 4)),
    FireHazardSignBlock
        (() -> GregTechAPI.sBlockCasings3, 5, gt(0, 32 + 5)),
    AcidHazardSignBlock
        (() -> GregTechAPI.sBlockCasings3, 6, gt(0, 32 + 6)),
    MagicHazardSignBlock
        (() -> GregTechAPI.sBlockCasings3, 7, gt(0, 32 + 7)),
    FrostHazardSignBlock
        (() -> GregTechAPI.sBlockCasings3, 8, gt(0, 32 + 8)),
    NoiseHazardSignBlock
        (() -> GregTechAPI.sBlockCasings3, 9, gt(0, 32 + 9)),
    GrateMachineCasing
        (() -> GregTechAPI.sBlockCasings3, 10, gt(0, 32 + 10)),
    FilterMachineCasing
        (() -> GregTechAPI.sBlockCasings3, 11, gt(0, 32 + 11)),
    RadiationProofMachineCasing
        (() -> GregTechAPI.sBlockCasings3, 12, gt(0, 32 + 12)),
    BronzeFireboxCasing
        (() -> GregTechAPI.sBlockCasings3, 13, gt(0, 32 + 13)),
    SteelFireboxCasing
        (() -> GregTechAPI.sBlockCasings3, 14, gt(0, 32 + 14)),
    TungstensteelFireboxCasing
        (() -> GregTechAPI.sBlockCasings3, 15, gt(0, 32 + 15)),

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

    EntropyResistantCasing
        (() -> GregTechAPI.sBlockCasings12, 10, gt(16, 80 + 10)),

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

    MagicCasing
        (() -> Loaders.magicCasing, 0, -1),

    BorosilicateGlassAny(BorosilicateGlass::getGlassBlock, 0, -1) {
        @Override
        public String getLocalizedName() {
            return GTUtility.translate("GT5U.MBTT.BoroGlassAny");
        }

        @Override
        public <T> IStructureElement<T> asElement(CasingElementContext context) {
            return BorosilicateGlass.ofBoroGlassAnyTier();
        }
    },

    BorosilicateGlassTiered(BorosilicateGlass::getGlassBlock, 0, -1) {
        @Override
        public String getLocalizedName() {
            return GTUtility.translate("GT5U.MBTT.BoroGlassTiered");
        }

        @Override
        public <T> IStructureElement<T> asElement(CasingElementContext context) {
            return BorosilicateGlass.ofBoroGlass(
                (byte) -2,
                (T multi, Byte tier) -> ((IStructureProvider<?>)multi).getStructureInstance().setCasingTier(context.getGroup(), tier),
                (T multi) -> (byte) ((IStructureProvider<?>)multi).getStructureInstance().getCasingTier(context.getGroup(), -2));
        }

        @Override
        public boolean isTiered() {
            return true;
        }
    },

    WardedGlass
        (() -> GameRegistry.findBlock(Mods.Thaumcraft.ID, "blockCosmeticOpaque"), 2, -1),

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
    private volatile Block block;
    public final int meta;
    public final int textureId;

    Casings(BlockSupplier blockGetter, int meta, int textureId) {
        this.blockGetter = blockGetter;
        this.meta = meta;
        this.textureId = textureId;
    }

    @Override
    public @NotNull Block getBlock() {
        if (block == null) {
            block = blockGetter.get();
        }

        return block;
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
