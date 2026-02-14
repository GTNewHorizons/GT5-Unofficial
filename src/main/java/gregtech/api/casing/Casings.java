package gregtech.api.casing;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import net.minecraft.block.Block;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.util.data.BlockSupplier;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.StructureUtility;

import bartworks.API.BorosilicateGlass;
import bartworks.system.material.WerkstoffLoader;
import cpw.mods.fml.common.registry.GameRegistry;
import goodgenerator.loader.Loaders;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Mods;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.IStructureProvider;
import gregtech.api.structure.ISuperChestAcceptor;
import gregtech.api.util.GTStructureUtility;
import gregtech.api.util.GTUtility;
import gregtech.common.blocks.BlockCasings12;
import gregtech.common.blocks.BlockGlass1;
import gregtech.common.tileentities.storage.MTEDigitalChestBase;
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

    IsamillGearBoxCasing
        (() -> ModBlocks.blockCasings5Misc, 2, gtpp(2,0)),

    // ------------------ Gregtech Casings -----------------

    //Block Casings 1
    ULVMachineCasing
        (() -> GregTechAPI.sBlockCasings1, 0, gt(0, 0, 0)),
    LVMachineCasing
        (() -> GregTechAPI.sBlockCasings1, 1, gt(0, 0, 1)),
    MVMachineCasing
        (() -> GregTechAPI.sBlockCasings1, 2, gt(0, 0, 2)),
    HVMachineCasing
        (() -> GregTechAPI.sBlockCasings1, 3, gt(0, 0, 3)),
    EVMachineCasing
        (() -> GregTechAPI.sBlockCasings1, 4, gt(0, 0, 4)),
    IVMachineCasing
        (() -> GregTechAPI.sBlockCasings1, 5, gt(0, 0, 5)),
    LuVMachineCasing
        (() -> GregTechAPI.sBlockCasings1, 6, gt(0, 0, 6)),
    ZPMMachineCasing
        (() -> GregTechAPI.sBlockCasings1, 7, gt(0, 0, 7)),
    UVMachineCasing
        (() -> GregTechAPI.sBlockCasings1, 8, gt(0, 0, 8)),
    UHVMachineCasing
        (() -> GregTechAPI.sBlockCasings1, 9, gt(0, 0, 9)),
    BronzePlatedBricks
        (() -> GregTechAPI.sBlockCasings1, 10, gt(0, 0, 10)),
    HeatProofMachineCasing
        (() -> GregTechAPI.sBlockCasings1, 11, gt(0, 0, 11)),
    DimensionallyTranscendentCasing
        (() -> GregTechAPI.sBlockCasings1, 12, gt(0, 0, 12)),
    DimensionalInjectionCasing
        (() -> GregTechAPI.sBlockCasings1, 13, gt(0, 0, 13)),
    DimensionalBridge
        (() -> GregTechAPI.sBlockCasings1, 14, gt(0, 0, 14)),
    SuperconductingCoilBlock
        (() -> GregTechAPI.sBlockCasings1, 15, gt(0, 0, 15)),
    // Block Casings 2
    SolidSteelMachineCasing
        (() -> GregTechAPI.sBlockCasings2, 0, gt(0, 1, 0)),
    FrostProofMachineCasing
        (() -> GregTechAPI.sBlockCasings2, 1, gt(0, 1, 1)),
    BronzeGearBoxCasing
        (() -> GregTechAPI.sBlockCasings2, 2, gt(0, 1, 2)),
    SteelGearBoxCasing
        (() -> GregTechAPI.sBlockCasings2, 3, gt(0, 1, 3)),
    TitaniumGearBoxCasing
        (() -> GregTechAPI.sBlockCasings2, 4, gt(0, 1, 4)),
    AssemblyLineCasing
        (() -> GregTechAPI.sBlockCasings2, 5, gt(0, 1, 5)),
    ProcessorMachineCasing
        (() -> GregTechAPI.sBlockCasings2, 6, gt(0, 1, 6)),
    DataDriveMachineCasing
        (() -> GregTechAPI.sBlockCasings2, 7, gt(0, 1, 7)),
    ContainmentFieldMachineCasing
        (() -> GregTechAPI.sBlockCasings2, 8, gt(0, 1, 8)),
    AssemblerMachineCasing
        (() -> GregTechAPI.sBlockCasings2, 9, gt(0, 1, 9)),
    PumpMachineCasing
        (() -> GregTechAPI.sBlockCasings2, 10, gt(0, 1, 10)),
    MotorMachineCasing
        (() -> GregTechAPI.sBlockCasings2, 11, gt(0, 1, 11)),
    BronzePipeCasing
        (() -> GregTechAPI.sBlockCasings2, 12, gt(0, 1, 12)),
    SteelPipeCasing
        (() -> GregTechAPI.sBlockCasings2, 13, gt(0, 1, 13)),
    TitaniumPipeCasing
        (() -> GregTechAPI.sBlockCasings2, 14, gt(0, 1, 14)),
    TungstensteelPipeCasing
        (() -> GregTechAPI.sBlockCasings2, 15, gt(0, 1, 15  )),
    // Block Casings 3
    YellowStripesBlockA
        (() -> GregTechAPI.sBlockCasings3, 0, gt(0, 2, 0)),
    YellowStripesBlockB
        (() -> GregTechAPI.sBlockCasings3, 1, gt(0, 2, 1)),
    RadioactiveHazardSignBlock
        (() -> GregTechAPI.sBlockCasings3, 2, gt(0, 2, 2)),
    BioHazardSignBlock
        (() -> GregTechAPI.sBlockCasings3, 3, gt(0, 2, 3)),
    ExplosionHazardSignBlock
        (() -> GregTechAPI.sBlockCasings3, 4, gt(0, 2, 4)),
    FireHazardSignBlock
        (() -> GregTechAPI.sBlockCasings3, 5, gt(0, 2, 5)),
    AcidHazardSignBlock
        (() -> GregTechAPI.sBlockCasings3, 6, gt(0, 2, 6)),
    MagicHazardSignBlock
        (() -> GregTechAPI.sBlockCasings3, 7, gt(0, 2, 7)),
    FrostHazardSignBlock
        (() -> GregTechAPI.sBlockCasings3, 8, gt(0, 2, 8)),
    NoiseHazardSignBlock
        (() -> GregTechAPI.sBlockCasings3, 9, gt(0, 2, 9)),
    GrateMachineCasing
        (() -> GregTechAPI.sBlockCasings3, 10, gt(0, 2, 10)),
    FilterMachineCasing
        (() -> GregTechAPI.sBlockCasings3, 11, gt(0, 2, 11)),
    RadiationProofMachineCasing
        (() -> GregTechAPI.sBlockCasings3, 12, gt(0, 2, 12)),
    BronzeFireboxCasing
        (() -> GregTechAPI.sBlockCasings3, 13, gt(0, 2, 13)),
    SteelFireboxCasing
        (() -> GregTechAPI.sBlockCasings3, 14, gt(0, 2, 14)),
    TungstensteelFireboxCasing
        (() -> GregTechAPI.sBlockCasings3, 15, gt(0, 2, 15)),
    // Block Casings 4
    RobustTungstenSteelMachineCasing
        (() -> GregTechAPI.sBlockCasings4, 0, gt(0, 3, 0)),
    CleanStainlessSteelMachineCasing
        (() -> GregTechAPI.sBlockCasings4, 1, gt(0,3,1)),
    StableTitaniumMachineCasing
        (() -> GregTechAPI.sBlockCasings4, 2, gt(0, 3, 2)),
    TitaniumFireboxCasing
        (() -> GregTechAPI.sBlockCasings4, 3, gt(0, 3, 3)),
    FusionMachineCasing
        (() -> GregTechAPI.sBlockCasings4, 6, gt(0, 3, 6)),
    FusionCoilBlock
        (() -> GregTechAPI.sBlockCasings4, 7, gt(0, 3, 7)),
    FusionMachineCasingMKII
        (() -> GregTechAPI.sBlockCasings4, 8, gt(0, 3, 8)),
    TurbineCasing
        (() -> GregTechAPI.sBlockCasings4, 9, gt(0, 3, 9)),
    StainlessSteelTurbineCasing
        (() -> GregTechAPI.sBlockCasings4, 10, gt(0, 3, 10)),
    TitaniumTurbineCasing
        (() -> GregTechAPI.sBlockCasings4, 11, gt(0, 3, 11)),
    TungstensteelTurbineCasing
        (() -> GregTechAPI.sBlockCasings4, 12, gt(0, 3, 12)),
    EngineIntakeCasing
        (() -> GregTechAPI.sBlockCasings4, 13, gt(0, 3, 13)),
    MiningOsmiridiumCasing
        (() -> GregTechAPI.sBlockCasings4, 14, gt(0, 3, 14)),
    Firebricks
        (() -> GregTechAPI.sBlockCasings4, 15, gt(0, 3, 15)),
    // Block Casings 5
    CupronickelCoilBlock
        (() -> GregTechAPI.sBlockCasings5, 0, gt(1, 0, 0)),
    KanthalCoilBlock
        (() -> GregTechAPI.sBlockCasings5, 1, gt(1,0,1)),
    NichromeCoilBlock
        (() -> GregTechAPI.sBlockCasings5, 2, gt(1, 0, 2)),
    TPVCoilBlock
        (() -> GregTechAPI.sBlockCasings5, 3, gt(1, 0, 3)),
    HSSGCoilBlock
        (() -> GregTechAPI.sBlockCasings5, 4, gt(1, 0, 4)),
    HSSSCoilBlock
        (() -> GregTechAPI.sBlockCasings5, 9, gt(1, 0, 9)),
    NaquadahCoilBlock
        (() -> GregTechAPI.sBlockCasings5, 5, gt(1, 0, 5)),
    NaquadahAlloyCoilBlock
        (() -> GregTechAPI.sBlockCasings5, 6, gt(1, 0, 6)),
    TriniumCoilBlock
        (() -> GregTechAPI.sBlockCasings5, 10, gt(1, 0, 10)),
    ElectrumFluxCoilBlock
        (() -> GregTechAPI.sBlockCasings5, 8, gt(1, 0, 8)),
    AwakenedDraconiumCoilBlock
        (() -> GregTechAPI.sBlockCasings5, 9, gt(1, 0, 9)),
    InfinityCoilBlock
        (() -> GregTechAPI.sBlockCasings5, 11, gt(1, 0, 11)),
    HypogenCoilBlock
        (() -> GregTechAPI.sBlockCasings5, 12, gt(1, 0, 12)),
    EternalCoilBlock
        (() -> GregTechAPI.sBlockCasings5, 13, gt(1, 0, 13)),
    // Block Casings 6
    HermeticCasing
        (() -> GregTechAPI.sBlockCasings6, 0, gt(8, 7, 0)),
    HermeticCasing1
        (() -> GregTechAPI.sBlockCasings6, 1, gt(8,7,1)),
    HermeticCasing2
        (() -> GregTechAPI.sBlockCasings6, 2, gt(8, 7, 2)),
    HermeticCasing3
        (() -> GregTechAPI.sBlockCasings6, 3, gt(8, 7, 3)),
    HermeticCasing4
        (() -> GregTechAPI.sBlockCasings6, 4, gt(8, 7, 4)),
    HermeticCasing5
        (() -> GregTechAPI.sBlockCasings6, 5, gt(8, 7, 5)),
    HermeticCasing6
        (() -> GregTechAPI.sBlockCasings6, 6, gt(8, 7, 6)),
    HermeticCasing7
        (() -> GregTechAPI.sBlockCasings6, 7, gt(8, 7, 7)),
    HermeticCasing8
        (() -> GregTechAPI.sBlockCasings6, 8, gt(8, 7, 8)),
    HermeticCasing9
        (() -> GregTechAPI.sBlockCasings6, 9, gt(8, 7, 9)),
    HermeticCasing10
        (() -> GregTechAPI.sBlockCasings6, 10, gt(8, 7, 10)),
    HermeticCasing11
        (() -> GregTechAPI.sBlockCasings6, 11, gt(8, 7, 11)),
    HermeticCasing12
        (() -> GregTechAPI.sBlockCasings6, 12, gt(8, 7, 12)),
    HermeticCasing13
        (() -> GregTechAPI.sBlockCasings6, 13, gt(8, 7, 13)),
    HermeticCasing14
        (() -> GregTechAPI.sBlockCasings6, 14, gt(8, 7, 14)),
    // Block Casings 8
    ChemicallyInertMachineCasing
        (() -> GregTechAPI.sBlockCasings8, 0, gt(1, 3, 0)),
    PTFEPipeCasing
        (() -> GregTechAPI.sBlockCasings8, 1, gt(1,3,1)),
    MiningNeutroniumCasing
        (() -> GregTechAPI.sBlockCasings8, 2, gt(1, 3, 2)),
    MiningBlackPlutoniumCasing
        (() -> GregTechAPI.sBlockCasings8, 3, gt(1, 3, 3)),
    ExtremeEngineIntakeCasing
        (() -> GregTechAPI.sBlockCasings8, 4, gt(1, 3, 4)),
    EuropiumReinforcedRadiationProofMachineCasing
        (() -> GregTechAPI.sBlockCasings8, 5, gt(1, 3, 5)),
    AdvancedRhodiumPlatedPalladiumMachineCasing
        (() -> GregTechAPI.sBlockCasings8, 6, gt(1, 3, 6)),
    AdvancedIridiumPlatedMachineCasing
        (() -> GregTechAPI.sBlockCasings8, 7, gt(1, 3, 7)),
    MagicalMachineCasing
        (() -> GregTechAPI.sBlockCasings8, 8, gt(1, 3, 8)),
    RadiantNaquadahAlloyCasing
        (() -> GregTechAPI.sBlockCasings8, 10, gt(1, 3, 10)),
    BasicPhotolithographicFrameworkCasing
        (() -> GregTechAPI.sBlockCasings8, 11, gt(1, 3, 11)),
    ReinforcedPhotolithographicFrameworkCasing
        (() -> GregTechAPI.sBlockCasings8, 12, gt(1, 3, 12)),
    RadiationProofPhotolithographicFrameworkCasing
        (() -> GregTechAPI.sBlockCasings8, 13, gt(1, 3, 13)),
    InfinityCooledCasing
        (() -> GregTechAPI.sBlockCasings8, 14, gt(1, 3, 14)),
    // Block Casings 9
    PBIPipeCasing
        (() -> GregTechAPI.sBlockCasings9, 0, gt(16, 1, 0)),
    AdvancedFilterCasing
        (() -> GregTechAPI.sBlockCasings9, 1, gt(16, 1, 1)),
    PrimitiveWoodenCasing
        (() -> GregTechAPI.sBlockCasings9, 2, gt(16, 1, 2)),
    SuperplasticizerTreatedHighStrengthConcrete
        (() -> GregTechAPI.sBlockCasings9, 3, gt(16, 1, 3)),
    SterileWaterPlantCasing
        (() -> GregTechAPI.sBlockCasings9, 4, gt(16, 1, 4)),
    ReinforcedSterileWaterPlantCasing
        (() -> GregTechAPI.sBlockCasings9, 5, gt(16, 1, 5)),
    SlickSterileFlocculationCasing
        (() -> GregTechAPI.sBlockCasings9, 6, gt(16, 1, 6)),
    StabilizedNaquadahWaterPlantCasing
        (() -> GregTechAPI.sBlockCasings9, 7, gt(16, 1, 7)),
    InertNeutralizationWaterPlantCasing
        (() -> GregTechAPI.sBlockCasings9, 8, gt(16, 1, 8)),
    ReactiveGasContainmentCasing
        (() -> GregTechAPI.sBlockCasings9, 9, gt(16, 1, 9)),
    InertFiltrationCasing
        (() -> GregTechAPI.sBlockCasings9, 10, gt(16, 1, 10)),
    HeatResistantTriniumPlatedCasing
        (() -> GregTechAPI.sBlockCasings9, 11, gt(16, 1, 11)),
    NaquadriaReinforcedWaterPlantCasing
        (() -> GregTechAPI.sBlockCasings9, 12, gt(16, 1, 12)),
    HighEnergyUltravioletEmitterCasing
        (() -> GregTechAPI.sBlockCasings9, 13, gt(16, 1, 13)),
    ParticleBeamGuidancePipeCasing
        (() -> GregTechAPI.sBlockCasings9, 14, gt(16, 1, 14)),
    FemtometerCalibratedParticleBeamCasing
        (() -> GregTechAPI.sBlockCasings9, 15, gt(16, 1, 15)),
    // Block Casings 10
    MagTechCasing
        (() -> GregTechAPI.sBlockCasings10, 0, gt(16, 3, 0)),
    LaserContainmentCasing
        (() -> GregTechAPI.sBlockCasings10, 1, gt(16, 3, 1)),
    QuarkExclusionCasing
        (() -> GregTechAPI.sBlockCasings10, 2, gt(16, 3, 2)),
    PressureContainmentCasing
        (() -> GregTechAPI.sBlockCasings10, 3, gt(16, 3, 3)),
    ElectricCompressorCasing
        (() -> GregTechAPI.sBlockCasings10, 4, gt(16, 3, 4)),
    CompressionPipeCasing
        (() -> GregTechAPI.sBlockCasings10, 5, gt(16, 3, 5)),
    NeutroniumCasing
        (() -> GregTechAPI.sBlockCasings10, 6, gt(16, 3, 6)),
    ActiveNeutroniumCasing
        (() -> GregTechAPI.sBlockCasings10, 7, gt(16, 3, 7)),
    NeutroniumStabilizationCasing
        (() -> GregTechAPI.sBlockCasings10, 8, gt(16, 3, 8)),
    CoolantDuct
        (() -> GregTechAPI.sBlockCasings10, 9, gt(16, 3, 9)),
    HeatingDuct
        (() -> GregTechAPI.sBlockCasings10, 10, gt(16, 3, 10)),
    ExtremeDensitySpaceBendingCasing
        (() -> GregTechAPI.sBlockCasings10, 11, gt(16, 3, 11)),
    BackgroundRadiationAbsorbentCasing
        (() -> GregTechAPI.sBlockCasings10, 12, gt(16, 3, 12)),
    SolidifierCasing
        (() -> GregTechAPI.sBlockCasings10, 13, gt(16, 3, 13)),
    SolidifierRadiator
        (() -> GregTechAPI.sBlockCasings10, 14, gt(16, 3, 14)),
    ReinforcedWoodenCasing
        (() -> GregTechAPI.sBlockCasings10, 15, gt(16, 3, 15)),
    // Block Casings 11
    TinItemPipeCasing
        (() -> GregTechAPI.sBlockCasings11, 0, gt(16, 4, 0)),
    BrassItemPipeCasing
        (() -> GregTechAPI.sBlockCasings11, 1, gt(16, 4, 1)),
    ElectrumItemPipeCasing
        (() -> GregTechAPI.sBlockCasings11, 2, gt(16, 4, 2)),
    PlatinumItemPipeCasing
        (() -> GregTechAPI.sBlockCasings11, 3, gt(16, 4, 3)),
    OsmiumItemPipeCasing
        (() -> GregTechAPI.sBlockCasings11, 4, gt(16, 4, 4)),
    QuantiumItemPipeCasing
        (() -> GregTechAPI.sBlockCasings11, 5, gt(16, 4, 5)),
    FluxedElectrumItemPipeCasing
        (() -> GregTechAPI.sBlockCasings11, 6, gt(16, 4, 6)),
    BlackPlutoniumItemPipeCasing
        (() -> GregTechAPI.sBlockCasings11, 7, gt(16, 4, 7)),
    // Block Casings 12
    CokeOvenBricks
        (() -> GregTechAPI.sBlockCasings12, 0, gt(16, 5, 0)),
    VibrationSafeCasing
        (() -> GregTechAPI.sBlockCasings12, 1, gt(16, 5, 1)),
    AlchemicallyResistantThaumiumCasing
        (() -> GregTechAPI.sBlockCasings12, 2, gt(16, 5, 2)),
    AlchemicallyInertVoidCasing
        (() -> GregTechAPI.sBlockCasings12, 3, gt(16, 5, 3)),
    AlchemicallyImmuneIchoriumCasing
        (() -> GregTechAPI.sBlockCasings12, 4, gt(16, 5, 4)),
    AlchemicalCasing(() -> GregTechAPI.sBlockCasings12, 10, gt(16, 5, 10)) {
        @Override
        public String getLocalizedName() {
            return GTUtility.translate("GT5U.MBTT.AlchemyCasingAny");
        }

        @Override
        public <T> int getTextureId(T t, CasingElementContext<T> context) {
            int tier = GTUtility.clamp(context.getInstance(t).getCasingTier(context.getGroup(), 0), 0, 2);

            return switch (tier) {
                case 1 -> gt(16, 80 + 11);
                case 2 -> gt(16, 80 + 12);
                default -> gt(16, 80 + 10);
            };
        }

        @Override
        public <T> IStructureElement<T> asElement(CasingElementContext<T> context) {
            return StructureUtility.ofBlocksTiered(
                (block, meta) -> {
                    if (block != GregTechAPI.sBlockCasings12) return null;

                    return switch (meta) {
                        case 10 -> 0;
                        case 11 -> 1;
                        case 12 -> 2;
                        default -> null;
                    };
                },
                Arrays.asList(
                    Pair.of(GregTechAPI.sBlockCasings12, 10),
                    Pair.of(GregTechAPI.sBlockCasings12, 11),
                    Pair.of(GregTechAPI.sBlockCasings12, 12)
                ),
                null,
                (T multi, Integer tier) -> {
                    ((IStructureProvider<?>)multi).getStructureInstance().setCasingTier(context.getGroup(), tier);
                },
                (T multi) -> {
                    int tier = ((IStructureProvider<?>)multi).getStructureInstance().getCasingTier(context.getGroup(), -1);

                    return tier == -1 ? null : tier;
                });
        }

        @Override
        public boolean isTiered() {
            return true;
        }
    },

    AlchemicalConstructTiered(() -> GameRegistry.findBlock(Mods.Thaumcraft.ID, "blockMetalDevice"), 9, -1) {
        @Override
        public String getLocalizedName() {
            return GTUtility.translate("GT5U.MBTT.AlchemicalConstructAny");
        }

        @Override
        public <T> ITexture getCasingTexture(T t, CasingElementContext<T> context) {
            int tier = GTUtility.clamp(context.getInstance(t).getCasingTier(context.getGroup(), 0), 0, 1);

            return TextureFactory.of(this.getBlock(), tier == 0 ? 9 : 3);
        }

        @Override
        public <T> IStructureElement<T> asElement(CasingElementContext<T> context) {
            return StructureUtility.ofBlocksTiered(
                (block, meta) -> {
                    if (block != this.getBlock()) return null;

                    return switch (meta) {
                        case 9 -> 0;
                        case 3 -> 1;
                        default -> null;
                    };
                },
                Arrays.asList(
                    Pair.of(this.getBlock(), 9),
                    Pair.of(this.getBlock(), 3)
                ),
                null,
                (T multi, Integer tier) -> {
                    ((IStructureProvider<?>)multi).getStructureInstance().setCasingTier(context.getGroup(), tier);
                },
                (T multi) -> {
                    int tier = ((IStructureProvider<?>)multi).getStructureInstance().getCasingTier(context.getGroup(), -1);

                    return tier == -1 ? null : tier;
                });
        }

        @Override
        public boolean isTiered() {
            return true;
        }
    },

    // Block Casings 13
    CableCasing
        (() -> GregTechAPI.sBlockCasings13, 0, gt(16, 6, 0)),
    GraphiteModeratorCasing
        (() -> GregTechAPI.sBlockCasings13, 1, gt(16, 6, 1)),
    InsulatedFluidPipeCasing
        (() -> GregTechAPI.sBlockCasings13, 2, gt(16, 6, 2)),
    BerylliumIntegratedReactorCasing
        (() -> GregTechAPI.sBlockCasings13, 3, gt(16, 6, 3)),
    RefinedGraphiteBlock
        (() -> GregTechAPI.sBlockCasings13, 4, gt(16, 6, 4)),
    PrecisionFieldSyncCasing
        (() -> GregTechAPI.sBlockCasings13, 5, gt(16, 6, 5)),
    MagneticAnchorCasing
        (() -> GregTechAPI.sBlockCasings13, 6, gt(16, 6, 6)),
    FieldEnergyAbsorberCasing
        (() -> GregTechAPI.sBlockCasings13, 7, gt(16, 6, 7)),
    LoadbearingDistributionCasing
        (() -> GregTechAPI.sBlockCasings13, 8, gt(16, 6, 8)),
    NaniteReplicationFramework
        (() -> GregTechAPI.sBlockCasings13, 9, gt(16, 6, 9)),
    // Block Casings Foundry
    PrimaryExoFoundryCasing
        (() -> GregTechAPI.sBlockCasingsFoundry, 0, gt(8, 5, 0)),
    InfiniteMagneticChassis
        (() -> GregTechAPI.sBlockCasingsFoundry, 1, gt(8, 5, 1)),
    EternalMagneticChassis
        (() -> GregTechAPI.sBlockCasingsFoundry, 2, gt(8, 5, 2)),
    CelestialMagneticChassis
        (() -> GregTechAPI.sBlockCasingsFoundry, 3, gt(8, 5, 3)),
    UniversalCollapserCasing
        (() -> GregTechAPI.sBlockCasingsFoundry, 4, gt(8, 5, 4)),
    SentientOverclockerCasing
        (() -> GregTechAPI.sBlockCasingsFoundry, 5, gt(8, 5, 5)),
    ProtoVoltStabilizerCasing
        (() -> GregTechAPI.sBlockCasingsFoundry, 6, gt(8, 5, 6)),
    HeliocastReinforcementCasing
        (() -> GregTechAPI.sBlockCasingsFoundry, 7, gt(8, 5, 7)),
    SuperdenseCastingBasinCasing
        (() -> GregTechAPI.sBlockCasingsFoundry, 8, gt(8, 5, 8)),
    HypercoolerCasing
        (() -> GregTechAPI.sBlockCasingsFoundry, 9, gt(8, 5, 9)),
    StreamlinedCastingCasing
        (() -> GregTechAPI.sBlockCasingsFoundry, 10, gt(8, 5, 10)),
    InnerExoFoundrySiphonCasing
        (() -> GregTechAPI.sBlockCasingsFoundry, 11, gt(8, 5, 11)),
    CentralCasingExoFoundryCasing
        (() -> GregTechAPI.sBlockCasingsFoundry, 12, gt(8, 5, 12)),

    // Block Casings NH
    AirFilterTurbineCasing
        (() -> GregTechAPI.sBlockCasingsNH, 0, gt(8, 4, 0)),
    AirFilterVentCasing
        (() -> GregTechAPI.sBlockCasingsNH, 1, gt(8, 4, 1)),
    PyrolyseOvenCasing
        (() -> GregTechAPI.sBlockCasingsNH, 2, gt(8, 4, 2)),
    AdvancedAirFilterTurbineCasing
        (() -> GregTechAPI.sBlockCasingsNH, 3, gt(8, 4, 3)),
    AdvancedAirFilterVentCasing
        (() -> GregTechAPI.sBlockCasingsNH, 4, gt(8, 4, 4)),
    SuperAirFilterTurbineCasing
        (() -> GregTechAPI.sBlockCasingsNH, 5, gt(8, 4, 5)),
    SuperAirFilterVentCasing
        (() -> GregTechAPI.sBlockCasingsNH, 6, gt(8, 4, 6)),
    UEVMachineCasing
        (() -> GregTechAPI.sBlockCasingsNH, 10, gt(8, 4, 10)),
    UIVMachineCasing
        (() -> GregTechAPI.sBlockCasingsNH, 11, gt(8, 4, 11)),
    UMVMachineCasing
        (() -> GregTechAPI.sBlockCasingsNH, 12, gt(8, 4, 12)),
    UXVMachineCasing
        (() -> GregTechAPI.sBlockCasingsNH, 13, gt(8, 4, 13)),
    MAXMachineCasing
        (() -> GregTechAPI.sBlockCasingsNH, 14, gt(8, 4, 14)),
    // Block Cyclotron Coils (Solenoids)
    MVSolenoidSuperconductorCoil
        (() -> GregTechAPI.sSolenoidCoilCasings, 0, gt(2, 0, 0)),
    HVSolenoidSuperconductorCoil
        (() -> GregTechAPI.sSolenoidCoilCasings, 1, gt(2, 0, 1)),
    EVSolenoidSuperconductorCoil
        (() -> GregTechAPI.sSolenoidCoilCasings, 2, gt(2, 0, 2)),
    IVSolenoidSuperconductorCoil
        (() -> GregTechAPI.sSolenoidCoilCasings, 3, gt(2, 0, 3)),
    LuVSolenoidSuperconductorCoil
        (() -> GregTechAPI.sSolenoidCoilCasings, 4, gt(2, 0, 4)),
    ZPMSolenoidSuperconductorCoil
        (() -> GregTechAPI.sSolenoidCoilCasings, 5, gt(2, 0, 5)),
    UVSolenoidSuperconductorCoil
        (() -> GregTechAPI.sSolenoidCoilCasings, 6, gt(2, 0, 6)),
    UHVSolenoidSuperconductorCoil
        (() -> GregTechAPI.sSolenoidCoilCasings, 7, gt(2, 0, 7)),
    UEVSolenoidSuperconductorCoil
        (() -> GregTechAPI.sSolenoidCoilCasings, 8, gt(2, 0, 8)),
    UIVSolenoidSuperconductorCoil
        (() -> GregTechAPI.sSolenoidCoilCasings, 9, gt(2, 0, 9)),
    UMVSolenoidSuperconductorCoil
        (() -> GregTechAPI.sSolenoidCoilCasings, 10, gt(2, 0, 10)),
    // Block Glass 1
    ChemicalGradeGlass
        (() -> GregTechAPI.sBlockGlass1, 0, gt(16, 0, 0)),
    ElectronPermeableNeutroniumCoatedGlass
        (() -> GregTechAPI.sBlockGlass1, 1, gt(16, 0, 1)),
    OmniPurposeInfinityFusedGlass
        (() -> GregTechAPI.sBlockGlass1, 2, gt(16, 0, 2)),
    NonPhotonicMatterExclusionGlass
        (() -> GregTechAPI.sBlockGlass1, 3, gt(16, 0, 3)),
    HawkingRadiationRealignmentFocus
        (() -> GregTechAPI.sBlockGlass1, 4, gt(16, 0, 4)),
    NaniteShieldingGlass
        (() -> GregTechAPI.sBlockGlass1, 5, gt(16, 0, 5)),
    ChamberGrate
        (() -> GregTechAPI.sBlockGlass1, 6, gt(16, 0, 6)),
    ExoFoundryContainmentGlass
        (() -> GregTechAPI.sBlockGlass1, 7, gt(16, 0, 7)),

    BoltedOsmiridiumCasing
        (() -> WerkstoffLoader.BWBlockCasings, 32083,32083),
    ReboltedOsmiridiumCasing
        (() -> WerkstoffLoader.BWBlockCasingsAdvanced, 32083, 32083),
    BoltedNaquadahAlloyCasing
        (() -> WerkstoffLoader.BWBlockCasings, 32091,32091),
    ReboltedNaquadahAlloyCasing
        (() -> WerkstoffLoader.BWBlockCasingsAdvanced, 32091, 32091),
    BoltedIridiumCasing
        (() -> WerkstoffLoader.BWBlockCasings, 31850,31850),
    ReboltedIridiumCasing
        (() -> WerkstoffLoader.BWBlockCasingsAdvanced, 31850, 31850),

    // GTPP casings

    // GregtechMetaCasingBlocks4 (gtplusplus.blockcasings.4)
    TriniumTitaniumCasing
        (() -> ModBlocks.blockCasings4Misc, 0, gtpp(3, 0)),
    TechCasing
        (() -> ModBlocks.blockCasings4Misc, 1, gtpp(3, 1)),
    OrganicPanelAGlowing
        (() -> ModBlocks.blockCasings4Misc, 2, gtpp(3, 2)),
    TemperedArcFurnaceCasing
        (() -> ModBlocks.blockCasings4Misc, 3, gtpp(3, 3)),
    QuantumForceTransformerCoilCasing
        (() -> ModBlocks.blockCasings4Misc, 4, gtpp(3, 4)),
    // 5–8,12–15 are skipped
    VacuumCasing
        (() -> ModBlocks.blockCasings4Misc, 10, gtpp(3, 10)),
    TurbodyneCasing
        (() -> ModBlocks.blockCasings4Misc, 11, gtpp(3, 11)),

    // GregtechMetaCasingBlocks (miscutils.blockcasings)
    CentrifugeCasing
        (() -> ModBlocks.blockCasingsMisc, 0, gtpp(0, 0)),
    StructuralCokeOvenCasing
        (() -> ModBlocks.blockCasingsMisc, 1, gtpp(0, 1)),
    HeatResistantCokeOvenCasing
        (() -> ModBlocks.blockCasingsMisc, 2, gtpp(0, 2)),
    HeatProofCokeOvenCasing
        (() -> ModBlocks.blockCasingsMisc, 3, gtpp(0, 3)),
    MaterialPressCasing
        (() -> ModBlocks.blockCasingsMisc, 4, gtpp(0, 4)),
    ElectrolyzerCasing
        (() -> ModBlocks.blockCasingsMisc, 5, gtpp(0, 5)),
    WireFactoryCasing
        (() -> ModBlocks.blockCasingsMisc, 6, gtpp(0, 6)),
    MacerationStackCasing
        (() -> ModBlocks.blockCasingsMisc, 7, gtpp(0, 7)),
    MatterGenerationCoil
        (() -> ModBlocks.blockCasingsMisc, 8, gtpp(0, 8)),
    MatterFabricatorCasing
        (() -> ModBlocks.blockCasingsMisc, 9, gtpp(0, 9)),
    IronPlatedBricks
        (() -> ModBlocks.blockCasingsMisc, 10, gtpp(0, 10)),
    MultitankExteriorCasing
        (() -> ModBlocks.blockCasingsMisc, 11, gtpp(0, 11)),
    HastelloyNReactorCasing
        (() -> ModBlocks.blockCasingsMisc, 12, gtpp(0, 12)),
    ReactorShieldCasing
        (() -> ModBlocks.blockCasingsMisc, 13, gtpp(0, 13)),
    BlastSmelterHeatContainmentCoil
        (() -> ModBlocks.blockCasingsMisc, 14, gtpp(0, 14)),
    BlastSmelterCasing
        (() -> ModBlocks.blockCasingsMisc, 15, gtpp(0, 15)),

    // GregtechMetaCasingBlocks3 (gtplusplus.blockcasings.3)
    AquaticCasing
        (() -> ModBlocks.blockCasings3Misc, 0, gtpp(2, 0)),
    InconelReinforcedCasing
        (() -> ModBlocks.blockCasings3Misc, 1, gtpp(2, 1)),
    MultiUseCasing
        (() -> ModBlocks.blockCasings3Misc, 2, gtpp(2, 2)),
    TriniumPlatedCasing
        (() -> ModBlocks.blockCasings3Misc, 3, gtpp(2, 3)),
    VanadiumRedoxPowerCellIV
        (() -> ModBlocks.blockCasings3Misc, 4, gtpp(2, 4)),
    VanadiumRedoxPowerCellLuV
        (() -> ModBlocks.blockCasings3Misc, 5, gtpp(2, 5)),
    VanadiumRedoxPowerCellZPM
        (() -> ModBlocks.blockCasings3Misc, 6, gtpp(2, 6)),
    VanadiumRedoxPowerCellUV
        (() -> ModBlocks.blockCasings3Misc, 7, gtpp(2, 7)),
    VanadiumRedoxPowerCellUHV
        (() -> ModBlocks.blockCasings3Misc, 8, gtpp(2, 8)),
    SupplyDepotCasing
        (() -> ModBlocks.blockCasings3Misc, 9, gtpp(2, 9)),
    AdvancedCryogenicCasing
        (() -> ModBlocks.blockCasings3Misc, 10, gtpp(2, 10)),
    VolcanusCasing
        (() -> ModBlocks.blockCasings3Misc, 11, gtpp(2, 11)),
    FusionMachineCasingMKIII
        (() -> ModBlocks.blockCasings3Misc, 12, gtpp(2, 12)),
    AdvancedFusionCoil
        (() -> ModBlocks.blockCasings3Misc, 13, gtpp(2, 13)),
    // blank
    ContainmentCasing
        (() -> ModBlocks.blockCasings3Misc, 15, gtpp(2, 15)),

    // GregtechMetaCasingBlocks6 (gtplusplus.blockcasings.6)
    FusionMachineCasingMKIV
        (() -> ModBlocks.blockCasings6Misc, 0, gtpp(3, 4)),
    AdvancedFusionCoilII
        (() -> ModBlocks.blockCasings6Misc, 1, gtpp(3, 5)),
    UnnamedCasing6_2
        (() -> ModBlocks.blockCasings6Misc, 2, gtpp(3, 6)),

    // GregtechMetaCasingBlocks5 (gtplusplus.blockcasings.5)
    IsaMillExteriorCasing
        (() -> ModBlocks.blockCasings5Misc, 0, gtpp(0, 2)),
    IsaMillPiping
        (() -> ModBlocks.blockCasings5Misc, 1, -1),
    IsaMillGearboxCasing
        (() -> ModBlocks.blockCasings5Misc, 2, -1),
    ElementalConfinementShell
        (() -> ModBlocks.blockCasings5Misc, 3, gtpp(0, 3)),
    SpargeTowerExteriorCasing
        (() -> ModBlocks.blockCasings5Misc, 4, gtpp(0, 4)),
    SturdyPrinterCasing
        (() -> ModBlocks.blockCasings5Misc, 5, gtpp(1, 10)),
    ForgeCasing
        (() -> ModBlocks.blockCasings5Misc, 6, gtpp(1, 11)),
    NeutronPulseManipulator
        (() -> ModBlocks.blockCasings5Misc, 7, -1),
    CosmicFabricManipulator
        (() -> ModBlocks.blockCasings5Misc, 8, -1),
    InfinityInfusedManipulator
        (() -> ModBlocks.blockCasings5Misc, 9, -1),
    SpaceTimeContinuumRipper
        (() -> ModBlocks.blockCasings5Misc, 10, -1),
    NeutronShieldingCore
        (() -> ModBlocks.blockCasings5Misc, 11, -1),
    CosmicFabricShieldingCore
        (() -> ModBlocks.blockCasings5Misc, 12, -1),
    InfinityInfusedShieldingCore
        (() -> ModBlocks.blockCasings5Misc, 13, -1),
    SpaceTimeBendingCore
        (() -> ModBlocks.blockCasings5Misc, 14, -1),
    ForceFieldGlass
        (() -> ModBlocks.blockCasings5Misc, 15, -1),

    // GregtechMetaTieredCasingBlocks1 — tiered Integral Encasement / Framework
    IntegralEncasementULV
        (() -> ModBlocks.blockCasingsTieredGTPP, 0, -1),
    IntegralEncasementLV
        (() -> ModBlocks.blockCasingsTieredGTPP, 1, -1),
    IntegralEncasementMV
        (() -> ModBlocks.blockCasingsTieredGTPP, 2, -1),
    IntegralEncasementHV
        (() -> ModBlocks.blockCasingsTieredGTPP, 3, -1),
    IntegralEncasementEV
        (() -> ModBlocks.blockCasingsTieredGTPP, 4, -1),
    IntegralFrameworkIV
        (() -> ModBlocks.blockCasingsTieredGTPP, 5, -1),
    IntegralFrameworkLuV
        (() -> ModBlocks.blockCasingsTieredGTPP, 6, -1),
    IntegralFrameworkZPM
        (() -> ModBlocks.blockCasingsTieredGTPP, 7, -1),
    IntegralFrameworkUV
        (() -> ModBlocks.blockCasingsTieredGTPP, 8, -1),
    IntegralFrameworkUHV
        (() -> ModBlocks.blockCasingsTieredGTPP, 9, -1),

    // GregtechMetaSpecialMultiCasings
    TurbineShaft
        (() -> ModBlocks.blockSpecialMultiCasings, 0, gtpp(1, 0)),
    ReinforcedSteamTurbineCasing
        (() -> ModBlocks.blockSpecialMultiCasings, 1, gtpp(1, 1)),
    ReinforcedHPSteamTurbineCasing
        (() -> ModBlocks.blockSpecialMultiCasings, 2, gtpp(1, 2)),
    ReinforcedGasTurbineCasing
        (() -> ModBlocks.blockSpecialMultiCasings, 3, gtpp(1, 3)),
    ReinforcedPlasmaTurbineCasing
        (() -> ModBlocks.blockSpecialMultiCasings, 4, gtpp(1, 4)),
    TeslaContainmentCasing
        (() -> ModBlocks.blockSpecialMultiCasings, 5, gtpp(1, 5)),
    StructuralSolarCasing
        (() -> ModBlocks.blockSpecialMultiCasings, 6, gtpp(1, 6)),
    SaltContainmentCasing
        (() -> ModBlocks.blockSpecialMultiCasings, 7, gtpp(1, 7)),
    ThermallyInsulatedCasing
        (() -> ModBlocks.blockSpecialMultiCasings, 8, gtpp(1, 8)),
    FlotationCellCasings
        (() -> ModBlocks.blockSpecialMultiCasings, 9, gtpp(1, 9)),
    ReinforcedEngineCasing
        (() -> ModBlocks.blockSpecialMultiCasings, 10, gtpp(1, 10)),
    MolecularContainmentCasing
        (() -> ModBlocks.blockSpecialMultiCasings, 11, gtpp(1, 11)),
    HighVoltageCurrentCapacitor
        (() -> ModBlocks.blockSpecialMultiCasings, 12, gtpp(1, 12)),
    ParticleContainmentCasing
        (() -> ModBlocks.blockSpecialMultiCasings, 13, gtpp(1, 13)),
    ReinforcedHeatExchangerCasing
        (() -> ModBlocks.blockSpecialMultiCasings, 14, gtpp(1, 14)),
    ReinforcedSCTurbineCasing
        (() -> ModBlocks.blockSpecialMultiCasings, 15, gtpp(1, 15)),

    MagicCasing
        (() -> Loaders.magicCasing, 0, -1),

    BorosilicateGlassAny(BorosilicateGlass::getGlassBlock, 0, -1) {
        @Override
        public String getLocalizedName() {
            return GTUtility.translate("GT5U.MBTT.BoroGlassAny");
        }

        @Override
        public <T> IStructureElement<T> asElement(CasingElementContext<T> context) {
            return BorosilicateGlass.ofBoroGlassAnyTier();
        }
    },

    BorosilicateGlassTiered(BorosilicateGlass::getGlassBlock, 0, -1) {
        @Override
        public String getLocalizedName() {
            return GTUtility.translate("GT5U.MBTT.BoroGlassTiered");
        }

        @Override
        public <T> IStructureElement<T> asElement(CasingElementContext<T> context) {
            return BorosilicateGlass.ofBoroGlass(
                (byte) -2,
                (T multi, Byte tier) -> context.getInstance(multi).setCasingTier(context.getGroup(), tier),
                (T multi) -> (byte) context.getInstance(multi).getCasingTier(context.getGroup(), -2));
        }

        @Override
        public boolean isTiered() {
            return true;
        }
    },

    WardedGlass
        (() -> GameRegistry.findBlock(Mods.Thaumcraft.ID, "blockCosmeticOpaque"), 2, -1),

    SuperChest(() -> GregTechAPI.sBlockMachines, 0, -1) {
        @Override
        public String getLocalizedName() {
            return GTUtility.translate("GT5U.MBTT.SuperChest");
        }

        @Override
        public boolean isTiered() {
            return true;
        }

        @Override
        public <T> IStructureElement<T> asElement(CasingElementContext<T> context) {
            List<MTEDigitalChestBase> tiers = GTStructureUtility.extractMTEs(
                MTEDigitalChestBase.class,
                ItemList.Super_Chest_LV.get(1),
                ItemList.Super_Chest_MV.get(1),
                ItemList.Super_Chest_HV.get(1),
                ItemList.Super_Chest_EV.get(1),
                ItemList.Super_Chest_IV.get(1),
                ItemList.Quantum_Chest_LV.get(1),
                ItemList.Quantum_Chest_MV.get(1),
                ItemList.Quantum_Chest_HV.get(1),
                ItemList.Quantum_Chest_EV.get(1),
                ItemList.Quantum_Chest_IV.get(1));

            return lazy(() -> GTStructureUtility.ofGenericMTETiered(
                MTEDigitalChestBase.class,
                (t, mte, tier) -> ((ISuperChestAcceptor) t).onSuperChestAdded(context.getGroup(), mte, tier),
                tiers));
        }
    },

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
    NanochipMeshInterfaceCasing(
        () -> GregTechAPI.sBlockCasings12, 1, ((BlockCasings12) GregTechAPI.sBlockCasings12).getTextureIndex(1)),
    NanochipReinforcementCasing(
        () -> GregTechAPI.sBlockCasings12, 2, ((BlockCasings12) GregTechAPI.sBlockCasings12).getTextureIndex(2)),
    NanochipComputationalMatrixCasing(
        () -> GregTechAPI.sBlockCasings12, 3, ((BlockCasings12) GregTechAPI.sBlockCasings12).getTextureIndex(3)),
    NanochipFirewallProjectionCasing(
        () -> GregTechAPI.sBlockCasings12, 4, ((BlockCasings12) GregTechAPI.sBlockCasings12).getTextureIndex(4)),
    NanochipComplexGlass(
        () -> GregTechAPI.sBlockGlass1, 8, ((BlockGlass1) GregTechAPI.sBlockGlass1).getTextureIndex(8)),

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
            block = Objects.requireNonNull(blockGetter.get(), "Block for casing " + name() + " was null");
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

    @Override
    public boolean isTiered() {
        return false;
    }

    private static int gt(int page, int id) {
        return page * 128 + id;
    }

    private static int gt(int page, int casing, int id) {
        return page * 128 + casing * 16 + id;
    }

    private static int gtpp(int page, int id) {
        int aRealID = id + (page * 16);
        return 64 + aRealID;
    }

    private static int tt(int id) {
        return BlockGTCasingsTT.textureOffset + id;
    }
}
