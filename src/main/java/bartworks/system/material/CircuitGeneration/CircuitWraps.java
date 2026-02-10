package bartworks.system.material.CircuitGeneration;

import static bartworks.system.material.CircuitGeneration.CircuitPartsItem.getCircuitParts;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.HALF_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import java.util.HashMap;

import net.minecraft.item.ItemStack;

import bartworks.MainMod;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTUtility;

public enum CircuitWraps {

    // ID 0-10000 RESERVED FOR IMPRINTS

    EngravedCrystalChips(32763, ItemList.Circuit_Parts_Crystal_Chip_Elite, ItemList.Wrap_EngravedCrystalChips),
    EngravedLapotrionChips(32762, ItemList.Circuit_Parts_Crystal_Chip_Master, ItemList.Wrap_EngravedLapotrionChips),
    CoatedCircuitBoards(32761, ItemList.Circuit_Board_Coated, ItemList.Wrap_CoatedCircuitBoards),
    CircuitBoards(32760, ItemList.Circuit_Board_Coated_Basic, ItemList.Wrap_CircuitBoards),
    PhenolicCircuitBoards(32759, ItemList.Circuit_Board_Phenolic, ItemList.Wrap_PhenolicCircuitBoards),
    GoodCircuitBoards(32758, ItemList.Circuit_Board_Phenolic_Good, ItemList.Wrap_GoodCircuitBoards),
    EpoxyCircuitBoards(32757, ItemList.Circuit_Board_Epoxy, ItemList.Wrap_EpoxyCircuitBoards),
    AdvancedCircuitBoards(32756, ItemList.Circuit_Board_Epoxy_Advanced, ItemList.Wrap_AdvancedCircuitBoards),
    FiberReinforcedCircuitBoards(32755, ItemList.Circuit_Board_Fiberglass, ItemList.Wrap_FiberReinforcedCircuitBoards),
    MoreAdvancedCircuitBoards(32754, ItemList.Circuit_Board_Fiberglass_Advanced,
        ItemList.Wrap_MoreAdvancedCircuitBoards),
    EliteCircuitBoards(32753, ItemList.Circuit_Board_Multifiberglass_Elite, ItemList.Wrap_EliteCircuitBoards),
    MultilayerFiberReinforcedCircuitBoards(32752, ItemList.Circuit_Board_Multifiberglass,
        ItemList.Wrap_MultilayerFiberReinforcedCircuitBoards),
    WetwareLifesupportCircuitBoards(32751, ItemList.Circuit_Board_Wetware,
        ItemList.Wrap_WetwareLifesupportCircuitBoards),
    ExtremeWetwareLifesupportCircuitBoards(32750, ItemList.Circuit_Board_Wetware_Extreme,
        ItemList.Wrap_ExtremeWetwareLifesupportCircuitBoards),
    PlasticCircuitBoards(32749, ItemList.Circuit_Board_Plastic, ItemList.Wrap_PlasticCircuitBoards),
    PlasticCircuitBoards2(32748, ItemList.Circuit_Board_Plastic_Advanced, ItemList.Wrap_PlasticCircuitBoards2),
    BioCircuitBoards(32747, ItemList.Circuit_Board_Bio, ItemList.Wrap_BioCircuitBoards),
    UltraBioMutatedCircuitBoards(32746, ItemList.Circuit_Board_Bio_Ultra, ItemList.Wrap_UltraBioMutatedCircuitBoards),
    SMDResistors(32745, ItemList.Circuit_Parts_ResistorSMD, ItemList.Wrap_SMDResistors),
    SMDInductors(32744, ItemList.Circuit_Parts_InductorSMD, ItemList.Wrap_SMDInductors),
    SMDDiodes(32743, ItemList.Circuit_Parts_DiodeSMD, ItemList.Wrap_SMDDiodes),
    SMDTransistors(32742, ItemList.Circuit_Parts_TransistorSMD, ItemList.Wrap_SMDTransistors),
    SMDCapacitors(32741, ItemList.Circuit_Parts_CapacitorSMD, ItemList.Wrap_SMDCapacitors),
    AdvancedSMDResistors(32740, ItemList.Circuit_Parts_ResistorASMD, ItemList.Wrap_AdvancedSMDResistors),
    AdvancedSMDDiodes(32739, ItemList.Circuit_Parts_DiodeASMD, ItemList.Wrap_AdvancedSMDDiodes),
    AdvancedSMDTransistors(32738, ItemList.Circuit_Parts_TransistorASMD, ItemList.Wrap_AdvancedSMDTransistors),
    AdvancedSMDCapacitors(32737, ItemList.Circuit_Parts_CapacitorASMD, ItemList.Wrap_AdvancedSMDCapacitors),
    IntegratedLogicCircuits(32736, ItemList.Circuit_Chip_ILC, ItemList.Wrap_IntegratedLogicCircuits),
    RandomAccessMemoryChips(32735, ItemList.Circuit_Chip_Ram, ItemList.Wrap_RandomAccessMemoryChips),
    NANDMemoryChips(32734, ItemList.Circuit_Chip_NAND, ItemList.Wrap_NANDMemoryChips),
    NORMemoryChips(32733, ItemList.Circuit_Chip_NOR, ItemList.Wrap_NORMemoryChips),
    CentralProcessingUnits(32732, ItemList.Circuit_Chip_CPU, ItemList.Wrap_CentralProcessingUnits),
    SoCs(32731, ItemList.Circuit_Chip_SoC, ItemList.Wrap_SoCs),
    ASoCs(32730, ItemList.Circuit_Chip_SoC2, ItemList.Wrap_ASoCs),
    PowerICs(32729, ItemList.Circuit_Chip_PIC, ItemList.Wrap_PowerICs),
    SimpleSOCs(32728, ItemList.Circuit_Chip_Simple_SoC, ItemList.Wrap_SimpleSOCs),
    HighPowerICs(32727, ItemList.Circuit_Chip_HPIC, ItemList.Wrap_HighPowerICs),
    UltraHighPowerICs(32726, ItemList.Circuit_Chip_UHPIC, ItemList.Wrap_UltraHighPowerICs),
    UltraLowPowerICs(32725, ItemList.Circuit_Chip_ULPIC, ItemList.Wrap_UltraLowPowerICs),
    LowPowerICs(32724, ItemList.Circuit_Chip_LPIC, ItemList.Wrap_LowPowerICs),
    NanoPowerICs(32723, ItemList.Circuit_Chip_NPIC, ItemList.Wrap_NanoPowerICs),
    PikoPowerICs(32722, ItemList.Circuit_Chip_PPIC, ItemList.Wrap_PikoPowerICs),
    QuantumPowerICs(32721, ItemList.Circuit_Chip_QPIC, ItemList.Wrap_QuantumPowerICs),
    NanocomponentCentralProcessingUnits(32720, ItemList.Circuit_Chip_NanoCPU,
        ItemList.Wrap_NanocomponentCentralProcessingUnits),
    QBitProcessingUnits(32719, ItemList.Circuit_Chip_QuantumCPU, ItemList.Wrap_QBitProcessingUnits),
    CrystalProcessingUnits(32718, ItemList.Circuit_Chip_CrystalCPU, ItemList.Wrap_CrystalProcessingUnits),
    CrystalSoCs(32717, ItemList.Circuit_Chip_CrystalSoC, ItemList.Wrap_CrystalSoCs),
    RawAdvancedCrystalChips(32716, ItemList.Circuit_Chip_CrystalSoC2, ItemList.Wrap_RawAdvancedCrystalChips),
    NeuroProcessingUnits(32715, ItemList.Circuit_Chip_NeuroCPU, ItemList.Wrap_NeuroProcessingUnits),
    BioProcessingUnits(32714, ItemList.Circuit_Chip_BioCPU, ItemList.Wrap_BioProcessingUnits),
    Stemcells(32713, ItemList.Circuit_Chip_Stemcell, ItemList.Wrap_Stemcells),
    Biocells(32712, ItemList.Circuit_Chip_Biocell, ItemList.Wrap_Biocells),
    OpticalSMDResistors(32711, ItemList.Circuit_Parts_ResistorXSMD, ItemList.Wrap_OpticalSMDResistors),
    OpticalSMDDiodes(32710, ItemList.Circuit_Parts_DiodeXSMD, ItemList.Wrap_OpticalSMDDiodes),
    OpticalSMDTransistors(32709, ItemList.Circuit_Parts_TransistorXSMD, ItemList.Wrap_OpticalSMDTransistors),
    OpticalSMDCapacitors(32708, ItemList.Circuit_Parts_CapacitorXSMD, ItemList.Wrap_OpticalSMDCapacitors),
    AdvancedSMDInductors(32707, ItemList.Circuit_Parts_InductorASMD, ItemList.Wrap_AdvancedSMDInductors),
    OpticalSMDInductors(32706, ItemList.Circuit_Parts_InductorXSMD, ItemList.Wrap_OpticalSMDInductors),
    RawExposedOpticalChips(32705, ItemList.Circuit_Chip_Optical, ItemList.Wrap_RawExposedOpticalChips),
    OpticalCircuitBoards(32704, ItemList.Circuit_Board_Optical, ItemList.Wrap_OpticalCircuitBoards),
    OpticallyPerfectedCPUs(32703, ItemList.Optically_Perfected_CPU, ItemList.Wrap_OpticallyPerfectedCPUs),
    OpticalCPUContainmentHousings(32702, ItemList.Optical_Cpu_Containment_Housing,
        ItemList.Wrap_OpticalCPUContainmentHousings),
    OpticallyCompatibleMemories(32701, ItemList.Optically_Compatible_Memory, ItemList.Wrap_OpticallyCompatibleMemories),
    LivingCrystalChips(32700, ItemList.Circuit_Parts_Crystal_Chip_Wetware, ItemList.Wrap_LivingCrystalChips),
    LivingBioChips(32699, ItemList.Circuit_Parts_Chip_Bioware, ItemList.Wrap_LivingBioChips),

    ;

    private static final HashMap<Integer, CircuitWraps> ID_MAP = new HashMap<>();

    public final int id;
    public final ItemList itemSingle;
    public final ItemList itemWrap;

    CircuitWraps(int id, ItemList itemSingle, ItemList itemWrap) {
        if (id < 10000) {
            throw new IllegalArgumentException(
                "ID for CircuitWraps must be abovce 10000, as IDs below that are reserved for imprints.");
        }
        this.id = id;
        this.itemSingle = itemSingle;
        this.itemWrap = itemWrap;
    }

    public void registerWrap() {
        if (!itemSingle.hasBeenSet()) {
            MainMod.LOGGER
                .error("Source item for wrap with ID: {} is registered later than the wrap, this is a bug.", id);
            return;
        }

        ItemStack itemStack = itemSingle.get(1);

        if (!GTUtility.isStackValid(itemStack)) {
            MainMod.LOGGER.error("Source item for wrap with ID: {} has an invalid stack, this is a bug.", id);
            return;
        }

        itemWrap.set(getCircuitParts().getWrapStack(id));

        ID_MAP.put(id, this);
    }

    public void registerWrapRecipe() {
        GTValues.RA.stdBuilder()
            .itemInputs(itemSingle.get(16), GTUtility.getIntegratedCircuit(16))
            .itemOutputs(getCircuitParts().getStack(id))
            .fluidInputs(Materials.Polyethylene.getMolten(1 * HALF_INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);
    }

    public static int getMinimalID() {
        int min = Short.MAX_VALUE - 1;

        for (CircuitWraps wrap : CircuitWraps.values()) {
            min = Math.min(min, wrap.id);
        }
        return min;
    }

    public static CircuitWraps getByID(int id) {
        return ID_MAP.get(id);
    }
}
