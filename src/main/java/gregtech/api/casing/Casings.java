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
import gregtech.common.blocks.BlockCasings4;
import gregtech.common.blocks.BlockCasings8;
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

    TurbineShaft
        (() -> ModBlocks.blockSpecialMultiCasings,0,gtpp(2,1)),
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
    CleanStainlessSteelCasing
        (() -> GregTechAPI.sBlockCasings4, 1, ((BlockCasings4) GregTechAPI.sBlockCasings4).getTextureIndex(1)),
    MiningOsmiridiumCasing
        (() -> GregTechAPI.sBlockCasings4, 14, ((BlockCasings4) GregTechAPI.sBlockCasings4).getTextureIndex(14)),
    BoltedOsmiridiumCasing
        (() -> WerkstoffLoader.BWBlockCasings, 32083,32083),
    ReboltedOsmiridiumCasing
        (() -> WerkstoffLoader.BWBlockCasingsAdvanced, 32083, 32083),
    MiningBlackPlutoniumCasing
        (() -> GregTechAPI.sBlockCasings8, 3, ((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(3)),
    BoltedNaquadahAlloyCasing
        (() -> WerkstoffLoader.BWBlockCasings, 32091,32091),
    ReboltedNaquadahAlloyCasing
        (() -> WerkstoffLoader.BWBlockCasingsAdvanced, 32091, 32091),
    MiningNeutroniumCasing
        (() -> GregTechAPI.sBlockCasings8, 2, ((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(2)),
    BoltedIridiumCasing
        (() -> WerkstoffLoader.BWBlockCasings, 31850,31850),
    ReboltedIridiumCasing
        (() -> WerkstoffLoader.BWBlockCasingsAdvanced, 31850, 31850),

    SolidSteelMachineCasing
        (() -> GregTechAPI.sBlockCasings2, 0, 16),
    FrostProofMachineCasing
        (() -> GregTechAPI.sBlockCasings2, 1, 17),

    WireFactoryCasing
        (() -> ModBlocks.blockCasingsMisc, 6, gtpp(0, 6)),

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

            if (tier == 0) {
                return TextureFactory.builder().setFromBlock(this.getBlock(), 9).build();
            } else {
                return TextureFactory.builder().setFromBlock(this.getBlock(), 3).build();
            }
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
