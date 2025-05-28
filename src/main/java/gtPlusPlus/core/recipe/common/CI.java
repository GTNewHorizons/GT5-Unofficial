package gtPlusPlus.core.recipe.common;

import gregtech.api.enums.Mods;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.ExternalMaterials;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.recipe.LoaderMachineComponents;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import ic2.core.Ic2Items;

import static gregtech.api.util.GTModHandler.getModItem;

public class CI {

    // null
    public static ItemStack _NULL = ItemUtils.getErrorStack(1);

    // bits
    public static long bits = GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.REVERSIBLE
        | GTModHandler.RecipeBits.BUFFERED;
    public static long bitsd = GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
        | GTModHandler.RecipeBits.REVERSIBLE
        | GTModHandler.RecipeBits.BUFFERED;

    // Circuits
    public static Object circuitPrimitive;
    public static Object circuitTier1;
    public static Object circuitTier2;
    public static Object circuitTier3;
    public static Object circuitTier4;
    public static Object circuitTier5;
    public static Object circuitTier6;
    public static Object circuitTier7;
    public static Object circuitTier8;
    public static Object circuitTier9;

    // Machine Components
    public static ItemStack electricMotor_LV;
    public static ItemStack electricMotor_MV;
    public static ItemStack electricMotor_HV;
    public static ItemStack electricMotor_EV;
    public static ItemStack electricMotor_IV;
    public static ItemStack electricMotor_LuV;
    public static ItemStack electricMotor_ZPM;
    public static ItemStack electricMotor_UV;
    public static ItemStack electricMotor_UHV;
    public static ItemStack electricPump_LV;
    public static ItemStack electricPump_MV;
    public static ItemStack electricPump_HV;
    public static ItemStack electricPump_EV;
    public static ItemStack electricPump_IV;
    public static ItemStack electricPump_LuV;
    public static ItemStack electricPump_ZPM;
    public static ItemStack electricPump_UV;
    public static ItemStack electricPump_UHV;
    public static ItemStack electricPiston_LV;
    public static ItemStack electricPiston_MV;
    public static ItemStack electricPiston_HV;
    public static ItemStack electricPiston_EV;
    public static ItemStack electricPiston_IV;
    public static ItemStack electricPiston_LuV;
    public static ItemStack electricPiston_ZPM;
    public static ItemStack electricPiston_UV;
    public static ItemStack electricPiston_UHV;
    public static ItemStack robotArm_LV;
    public static ItemStack robotArm_MV;
    public static ItemStack robotArm_HV;
    public static ItemStack robotArm_EV;
    public static ItemStack robotArm_IV;
    public static ItemStack robotArm_LuV;
    public static ItemStack robotArm_ZPM;
    public static ItemStack robotArm_UV;
    public static ItemStack robotArm_UHV;
    public static ItemStack conveyorModule_LV;
    public static ItemStack conveyorModule_MV;
    public static ItemStack conveyorModule_HV;
    public static ItemStack conveyorModule_EV;
    public static ItemStack conveyorModule_IV;
    public static ItemStack conveyorModule_LuV;
    public static ItemStack conveyorModule_ZPM;
    public static ItemStack conveyorModule_UV;
    public static ItemStack conveyorModule_UHV;
    public static ItemStack emitter_LV;
    public static ItemStack emitter_MV;
    public static ItemStack emitter_HV;
    public static ItemStack emitter_EV;
    public static ItemStack emitter_IV;
    public static ItemStack emitter_LuV;
    public static ItemStack emitter_ZPM;
    public static ItemStack emitter_UV;
    public static ItemStack emitter_UHV;
    public static ItemStack fieldGenerator_LV;
    public static ItemStack fieldGenerator_MV;
    public static ItemStack fieldGenerator_HV;
    public static ItemStack fieldGenerator_EV;
    public static ItemStack fieldGenerator_IV;
    public static ItemStack fieldGenerator_LuV;
    public static ItemStack fieldGenerator_ZPM;
    public static ItemStack fieldGenerator_UV;
    public static ItemStack fieldGenerator_UHV;
    public static ItemStack sensor_LV;
    public static ItemStack sensor_MV;
    public static ItemStack sensor_HV;
    public static ItemStack sensor_EV;
    public static ItemStack sensor_IV;
    public static ItemStack sensor_LuV;
    public static ItemStack sensor_ZPM;
    public static ItemStack sensor_UV;
    public static ItemStack sensor_UHV;

    public static ItemStack fluidRegulator_LV;
    public static ItemStack fluidRegulator_MV;
    public static ItemStack fluidRegulator_HV;
    public static ItemStack fluidRegulator_EV;
    public static ItemStack fluidRegulator_IV;
    public static ItemStack fluidRegulator_LuV;
    public static ItemStack fluidRegulator_ZPM;
    public static ItemStack fluidRegulator_UV;

    // Machine Casings
    public static ItemStack machineCasing_ULV;
    public static ItemStack machineCasing_LV;
    public static ItemStack machineCasing_MV;
    public static ItemStack machineCasing_HV;
    public static ItemStack machineCasing_EV;
    public static ItemStack machineCasing_IV;
    public static ItemStack machineCasing_LuV;
    public static ItemStack machineCasing_ZPM;
    public static ItemStack machineCasing_UV;
    public static ItemStack machineCasing_UHV;

    // Machine Hulls
    public static ItemStack machineHull_ULV;
    public static ItemStack machineHull_LV;
    public static ItemStack machineHull_MV;
    public static ItemStack machineHull_HV;
    public static ItemStack machineHull_EV;
    public static ItemStack machineHull_IV;
    public static ItemStack machineHull_LuV;
    public static ItemStack machineHull_ZPM;
    public static ItemStack machineHull_UV;
    public static ItemStack machineHull_UHV;

    // Gearbox Casings
    public static ItemStack gearboxCasing_Tier_1;
    public static ItemStack gearboxCasing_Tier_2;
    public static ItemStack gearboxCasing_Tier_3;
    public static ItemStack gearboxCasing_Tier_4;

    public static String[] component_Plate;
    public static String[] component_Rod;
    public static String[] component_Ingot;

    // Crafting Tools
    public static String craftingToolWrench = "craftingToolWrench";
    public static String craftingToolHammer_Hard = "craftingToolHardHammer";
    public static String craftingToolScrewdriver = "craftingToolScrewdriver";
    public static String craftingToolFile = "craftingToolFile";
    public static String craftingToolMortar = "craftingToolMortar";
    public static String craftingToolSaw = "craftingToolSaw";
    public static String craftingToolWireCutter = "craftingToolWirecutter";
    public static String craftingToolSolderingIron = "craftingToolSolderingIron";

    // Explosives
    public static ItemStack explosivePowderKeg;
    public static ItemStack explosiveTNT;
    public static ItemStack explosiveITNT;

    public static Materials[] tieredMaterials = new Materials[] { Materials.Iron, Materials.Steel, Materials.Aluminium,
        Materials.StainlessSteel, Materials.Titanium, Materials.TungstenSteel,
        ExternalMaterials.getRhodiumPlatedPalladium(), Materials.Iridium, Materials.Osmium, Materials.Neutronium };

    public static void preInit() {

        // Tiered Components
        component_Plate = new String[] { getTieredComponent(OrePrefixes.plate, 0),
            getTieredComponent(OrePrefixes.plate, 1), getTieredComponent(OrePrefixes.plate, 2),
            getTieredComponent(OrePrefixes.plate, 3), getTieredComponent(OrePrefixes.plate, 4),
            getTieredComponent(OrePrefixes.plate, 5), getTieredComponent(OrePrefixes.plate, 6),
            getTieredComponent(OrePrefixes.plate, 7), getTieredComponent(OrePrefixes.plate, 8),
            getTieredComponent(OrePrefixes.plate, 9), getTieredComponent(OrePrefixes.plate, 10),
            getTieredComponent(OrePrefixes.plate, 11) };
        component_Rod = new String[] { getTieredComponent(OrePrefixes.stick, 0),
            getTieredComponent(OrePrefixes.stick, 1), getTieredComponent(OrePrefixes.stick, 2),
            getTieredComponent(OrePrefixes.stick, 3), getTieredComponent(OrePrefixes.stick, 4),
            getTieredComponent(OrePrefixes.stick, 5), getTieredComponent(OrePrefixes.stick, 6),
            getTieredComponent(OrePrefixes.stick, 7), getTieredComponent(OrePrefixes.stick, 8),
            getTieredComponent(OrePrefixes.stick, 9), getTieredComponent(OrePrefixes.stick, 10),
            getTieredComponent(OrePrefixes.stick, 11) };
        component_Ingot = new String[] { getTieredComponent(OrePrefixes.ingot, 0),
            getTieredComponent(OrePrefixes.ingot, 1), getTieredComponent(OrePrefixes.ingot, 2),
            getTieredComponent(OrePrefixes.ingot, 3), getTieredComponent(OrePrefixes.ingot, 4),
            getTieredComponent(OrePrefixes.ingot, 5), getTieredComponent(OrePrefixes.ingot, 6),
            getTieredComponent(OrePrefixes.ingot, 7), getTieredComponent(OrePrefixes.ingot, 8),
            getTieredComponent(OrePrefixes.ingot, 9), getTieredComponent(OrePrefixes.ingot, 10),
            getTieredComponent(OrePrefixes.ingot, 11) };

        // Circuits
        circuitPrimitive = getTieredCircuit(0);
        circuitTier1 = getTieredCircuit(1);
        circuitTier2 = getTieredCircuit(2);
        circuitTier3 = getTieredCircuit(3);
        circuitTier4 = getTieredCircuit(4);
        circuitTier5 = getTieredCircuit(5);
        circuitTier6 = getTieredCircuit(6);
        circuitTier7 = getTieredCircuit(7);
        circuitTier8 = getTieredCircuit(8);
        circuitTier9 = getTieredCircuit(9);
    }

    public static Object getTieredCircuit(int tier) {
        return getTieredCircuitOreDictName(tier);
    }

    public static String getTieredCircuitOreDictName(int tier) {
        return switch(tier) {
            case 0 -> "circuitPrimitive";
            case 1 -> "circuitBasic";
            case 2 -> "circuitGood";
            case 3 -> "circuitAdvanced";
            case 4 -> "circuitData";
            case 5 -> "circuitElite";
            case 6 -> "circuitMaster";
            case 7 -> "circuitUltimate";
            case 8 -> "circuitSuperconductor";
            case 9 -> "circuitInfinite";
            case 10 -> "circuitQuantum";
            default -> "circuitPrimitive";
        };
    }

    private static Object getMaterialFromTier(int tier) {
        return switch (tier){
            case 0 -> Materials.Wood;
            case 1 -> Materials.Lead;
            case 2 -> Materials.Bronze;
            case 3 -> Materials.Steel;
            case 4 -> MaterialsAlloy.EGLIN_STEEL;
            case 5 -> Materials.Aluminium;
            case 6 -> MaterialsAlloy.MARAGING250;
            case 7 -> MaterialsAlloy.TANTALLOY_61;
            case 8 -> MaterialsAlloy.INCONEL_792;
            case 9 -> MaterialsAlloy.ZERON_100;
            case 10 -> Materials.NaquadahEnriched;
            case 11 -> Materials.Neutronium;
            default -> Materials._NULL;
        };
    }

    public static String getTieredComponent(OrePrefixes type, int tier) {
        Object material = getMaterialFromTier(tier);
        if (material != null) {
            String materialName;
            if (material instanceof Materials) {
                materialName = ((Materials) material).mDefaultLocalName;
            } else {
                materialName = (Utils.sanitizeString(((Material) material).getLocalizedName()));
            }
            Logger.INFO("Searching for a component named " + type.name() + materialName);
            return (type.name() + materialName);
        }
        Logger.INFO("[Components] Failed getting a tiered component. " + type.name() + " | " + tier);
        return null;
    }

    public static ItemStack getDataOrb() {
        return ItemList.Tool_DataOrb.get(1);
    }

    public static ItemStack getDataStick() {
        return ItemList.Tool_DataStick.get(1);
    }

    public static ItemStack getTieredMachineHull(int tier) {
        return switch (tier){
            case 0 -> machineHull_ULV;
            case 1 -> machineHull_LV;
            case 2 -> machineHull_MV;
            case 3 -> machineHull_HV;
            case 4 -> machineHull_EV;
            case 5 -> machineHull_IV;
            case 6 -> machineHull_LuV;
            case 7 -> machineHull_ZPM;
            case 8 -> machineHull_UV;
            case 9 -> machineHull_UHV;
            default -> GregtechItemList.Casing_Multi_Use.get(1);
        };
    }

    public static ItemStack getTieredMachineCasing(int tier) {
        return switch (tier){
            case 0 -> machineCasing_ULV;
            case 1 -> machineCasing_LV;
            case 2 -> machineCasing_MV;
            case 3 -> machineCasing_HV;
            case 4 -> machineCasing_EV;
            case 5 -> machineCasing_IV;
            case 6 -> machineCasing_LuV;
            case 7 -> machineCasing_ZPM;
            case 8 -> machineCasing_UV;
            case 9 -> machineCasing_UHV;
            default -> GregtechItemList.Casing_Multi_Use.get(1);
        };
    }

    public static void init() {
        // Set Explosives
        explosivePowderKeg = ItemList.Block_Powderbarrel.get(1);
        explosiveTNT = ItemUtils.getSimpleStack(Blocks.tnt)
            .copy();
        explosiveITNT = Ic2Items.industrialTnt.copy();

        // Machine Casings
        machineCasing_ULV = ItemList.Casing_ULV.get(1);
        machineCasing_LV = ItemList.Casing_LV.get(1);
        machineCasing_MV = ItemList.Casing_MV.get(1);
        machineCasing_HV = ItemList.Casing_HV.get(1);
        machineCasing_EV = ItemList.Casing_EV.get(1);
        machineCasing_IV = ItemList.Casing_IV.get(1);
        machineCasing_LuV = ItemList.Casing_LuV.get(1);
        machineCasing_ZPM = ItemList.Casing_ZPM.get(1);
        machineCasing_UV = ItemList.Casing_UV.get(1);
        machineCasing_UHV = ItemList.Casing_MAX.get(1);

        // Machine Hulls
        machineHull_ULV = ItemList.Hull_ULV.get(1);
        machineHull_LV = ItemList.Hull_LV.get(1);
        machineHull_MV = ItemList.Hull_MV.get(1);
        machineHull_HV = ItemList.Hull_HV.get(1);
        machineHull_EV = ItemList.Hull_EV.get(1);
        machineHull_IV = ItemList.Hull_IV.get(1);
        machineHull_LuV = ItemList.Hull_LuV.get(1);
        machineHull_ZPM = ItemList.Hull_ZPM.get(1);
        machineHull_UV = ItemList.Hull_UV.get(1);
        machineHull_UHV = ItemList.Hull_MAX.get(1);

        // Gear box Casings
        gearboxCasing_Tier_1 = ItemList.Casing_Gearbox_Bronze.get(1);
        gearboxCasing_Tier_2 = ItemList.Casing_Gearbox_Steel.get(1);
        gearboxCasing_Tier_3 = ItemList.Casing_Gearbox_Titanium.get(1);
        gearboxCasing_Tier_4 = ItemList.Casing_Gearbox_TungstenSteel.get(1);

        // Machine Components
        LoaderMachineComponents.initialise();
    }

    public static ItemStack emptyCells(int i) {
        return ItemUtils.getEmptyCell(i);
    }

    private static final Material[] aMaterial_Main = new Material[] { MaterialsAlloy.POTIN, MaterialsAlloy.TUMBAGA,
        MaterialsAlloy.EGLIN_STEEL, MaterialsAlloy.TANTALUM_CARBIDE, MaterialsAlloy.INCOLOY_DS,
        MaterialsAlloy.INCONEL_625, MaterialsAlloy.ZERON_100, MaterialsAlloy.PIKYONIUM,
        MaterialsElements.STANDALONE.ADVANCED_NITINOL, MaterialsAlloy.ABYSSAL, MaterialsAlloy.QUANTUM,
        MaterialsElements.STANDALONE.HYPOGEN };

    private static final Material[] aMaterial_Secondary = new Material[] { MaterialsAlloy.STEEL,
        MaterialsAlloy.SILICON_CARBIDE, MaterialsAlloy.BLOODSTEEL, MaterialsAlloy.TANTALUM_CARBIDE,
        MaterialsAlloy.INCONEL_792, MaterialsAlloy.ARCANITE, MaterialsAlloy.LAFIUM, MaterialsAlloy.CINOBITE,
        MaterialsAlloy.TITANSTEEL, MaterialsAlloy.OCTIRON, MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN,
        MaterialsElements.STANDALONE.HYPOGEN };

    private static final Material[] aMaterial_Tertiary = new Material[] { MaterialsElements.getInstance().LEAD,
        MaterialsElements.getInstance().ALUMINIUM, MaterialsElements.STANDALONE.BLACK_METAL,
        MaterialsElements.getInstance().TITANIUM, MaterialsAlloy.HASTELLOY_N, MaterialsAlloy.ENERGYCRYSTAL,
        MaterialsAlloy.TRINIUM_NAQUADAH_CARBON, MaterialsAlloy.TRINIUM_REINFORCED_STEEL, // Arceus
        MaterialsAlloy.TITANSTEEL, MaterialsElements.STANDALONE.ASTRAL_TITANIUM,
        MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN, MaterialsElements.STANDALONE.HYPOGEN };

    private static final Materials[] aMaterial_Cables = new Materials[] { Materials.Tin, Materials.Cobalt,
        Materials.AnnealedCopper, Materials.Gold, Materials.Titanium, Materials.Nichrome, Materials.Platinum,
        Materials.YttriumBariumCuprate, Materials.Naquadah, Materials.Duranium, Materials.SuperconductorUHV, };

    private static final Materials[] aMaterial_Circuits = new Materials[] { Materials.ULV, Materials.LV, Materials.MV,
        Materials.HV, Materials.EV, Materials.IV, Materials.LuV, Materials.ZPM, Materials.UV, Materials.UHV,
        Materials.UEV, };

    private static final Material[][] aMaster = new Material[][] { aMaterial_Main, aMaterial_Secondary,
        aMaterial_Tertiary };

    public static FluidStack getTieredFluid(int aTier, int aAmount) {
        return getTieredFluid(aTier, aAmount, 0);
    }

    public static FluidStack getAlternativeTieredFluid(int aTier, int aAmount) {
        return getTieredFluid(aTier, aAmount, 1);
    }

    public static FluidStack getTertiaryTieredFluid(int aTier, int aAmount) {
        return getTieredFluid(aTier, aAmount, 2);
    }

    public static FluidStack getTieredFluid(int aTier, int aAmount, int aType) {
        FluidStack a = aMaster[aType][aTier].getFluidStack(aAmount);
        if (a == null) {
            ItemStack aCell = getTieredComponent(OrePrefixes.liquid, aTier, 1);
            if (aCell != null) {
                a = GTUtility.getFluidForFilledItem(aCell, true);
                a.amount = aAmount;
            }
        }
        return a;
    }

    public static ItemStack getEnergyCore(int aTier, int aAmount) {
        ItemStack[] aOutput = new ItemStack[] {
            getModItem(Mods.GTPlusPlus.ID,"item.itemBufferCore" + "1", 1),
            getModItem(Mods.GTPlusPlus.ID,"item.itemBufferCore" + "2", 1),
            getModItem(Mods.GTPlusPlus.ID,"item.itemBufferCore" + "3", 1),
            getModItem(Mods.GTPlusPlus.ID,"item.itemBufferCore" + "4", 1),
            getModItem(Mods.GTPlusPlus.ID,"item.itemBufferCore" + "5", 1),
            getModItem(Mods.GTPlusPlus.ID,"item.itemBufferCore" + "6", 1),
            getModItem(Mods.GTPlusPlus.ID,"item.itemBufferCore" + "7", 1),
            getModItem(Mods.GTPlusPlus.ID,"item.itemBufferCore" + "8", 1),
            getModItem(Mods.GTPlusPlus.ID,"item.itemBufferCore" + "9", 1),
            getModItem(Mods.GTPlusPlus.ID,"item.itemBufferCore" + "10", 1) };
        return ItemUtils.getSimpleStack(aOutput[MathUtils.balance(aTier, 0, 9)], aAmount);
    }

    public static ItemStack getPlate(int aTier, int aAmount) {
        return getTieredComponent(OrePrefixes.plate, aTier, aAmount);
    }

    public static ItemStack getDoublePlate(int aTier, int aAmount) {
        return getTieredComponent(OrePrefixes.plateDouble, aTier, aAmount);
    }

    public static ItemStack getGear(int aTier, int aAmount) {
        return getTieredComponent(OrePrefixes.gearGt, aTier, aAmount);
    }

    public static ItemStack getIngot(int aTier, int aAmount) {
        return getTieredComponent(OrePrefixes.ingot, aTier, aAmount);
    }

    public static ItemStack getBolt(int aTier, int aAmount) {
        return getTieredComponent(OrePrefixes.bolt, aTier, aAmount);
    }

    public static ItemStack getScrew(int aTier, int aAmount) {
        return getTieredComponent(OrePrefixes.screw, aTier, aAmount);
    }

    public static ItemStack getCircuit(int aTier, int aAmount) {
        return getTieredComponent(OrePrefixes.circuit, aTier, aAmount);
    }

    public static ItemStack getTieredComponent(OrePrefixes aPrefix, int aTier, int aAmount) {
        aTier = Math.max(0, aTier);

        Material m;

        if (aPrefix == OrePrefixes.liquid) {
            int aMatID = (aTier == 0 || aTier == 2 || aTier == 5 || aTier == 8 ? 0
                : (aTier == 1 || aTier == 3 || aTier == 6 || aTier == 9 ? 1 : 2));
            return aMaster[aMatID][aTier].getCell(aAmount);
        }

        if (aPrefix == OrePrefixes.circuit) {
            return GTOreDictUnificator.get(OrePrefixes.circuit, aMaterial_Circuits[aTier], aAmount);
        }

        // Check for Cables first, catch SuperConductor case and swap to wire.
        if (aPrefix == OrePrefixes.cableGt01 || aPrefix == OrePrefixes.cableGt02
            || aPrefix == OrePrefixes.cableGt04
            || aPrefix == OrePrefixes.cableGt08
            || aPrefix == OrePrefixes.cableGt12) {
            // Special Handler
            if (aTier == 10) {
                if (aPrefix == OrePrefixes.cableGt01) {
                    aPrefix = OrePrefixes.wireGt02;
                } else if (aPrefix == OrePrefixes.cableGt02) {
                    aPrefix = OrePrefixes.wireGt04;
                } else if (aPrefix == OrePrefixes.cableGt04) {
                    aPrefix = OrePrefixes.wireGt08;
                } else if (aPrefix == OrePrefixes.cableGt08) {
                    aPrefix = OrePrefixes.wireGt12;
                } else if (aPrefix == OrePrefixes.cableGt12) {
                    aPrefix = OrePrefixes.wireGt16;
                }
            } else {
                return ItemUtils.getOrePrefixStack(aPrefix, aMaterial_Cables[aTier], aAmount);
            }
        }
        if (aPrefix == OrePrefixes.wireGt01 || aPrefix == OrePrefixes.wireGt02
            || aPrefix == OrePrefixes.wireGt04
            || aPrefix == OrePrefixes.wireGt08
            || aPrefix == OrePrefixes.wireGt12
            || aPrefix == OrePrefixes.wireGt16) {
            return ItemUtils.getOrePrefixStack(aPrefix, aMaterial_Cables[aTier], aAmount);
        }

        if (aPrefix == OrePrefixes.pipeTiny || aPrefix == OrePrefixes.pipeSmall
            || aPrefix == OrePrefixes.pipe
            || aPrefix == OrePrefixes.pipeMedium
            || aPrefix == OrePrefixes.pipeLarge
            || aPrefix == OrePrefixes.pipeHuge) {

            if (aPrefix == OrePrefixes.pipe) {
                aPrefix = OrePrefixes.pipeMedium;
            }

            if (aTier == 0) {
                return ItemUtils.getOrePrefixStack(aPrefix, Materials.Lead, aAmount);
            } else if (aTier == 1) {
                return ItemUtils.getOrePrefixStack(aPrefix, Materials.Steel, aAmount);
            } else if (aTier == 2) {
                return ItemUtils.getOrePrefixStack(aPrefix, Materials.StainlessSteel, aAmount);
            } else if (aTier == 3) {
                return ItemUtils.getOrePrefixStack(aPrefix, Materials.Tungsten, aAmount);
            } else if (aTier == 4) {
                return ItemUtils.getOrePrefixStack(aPrefix, Materials.TungstenSteel, aAmount);
            } else if (aTier == 5) {
                return ItemUtils.getOrePrefixStack(aPrefix, MaterialsAlloy.MARAGING350, aAmount);
            } else if (aTier == 6) {
                return ItemUtils.getOrePrefixStack(aPrefix, MaterialsAlloy.STABALLOY, aAmount);
            } else if (aTier == 7) {
                return ItemUtils.getOrePrefixStack(aPrefix, MaterialsAlloy.HASTELLOY_X, aAmount);
            } else if (aTier == 8) {
                return ItemUtils.getOrePrefixStack(aPrefix, Materials.Ultimate, aAmount);
            } else if (aTier == 9) {
                return ItemUtils.getOrePrefixStack(OrePrefixes.pipeMedium, Materials.SuperconductorUHV, aAmount);
            } else if (aTier == 10) {
                return ItemUtils.getOrePrefixStack(aPrefix, Materials.Europium, aAmount);
            } else {
                return ItemUtils.getOrePrefixStack(aPrefix, Materials.Titanium, aAmount);
            }
        }

        if (aPrefix == OrePrefixes.rod) {
            aPrefix = OrePrefixes.stick;
        }

        if (aPrefix == OrePrefixes.gear || aPrefix == OrePrefixes.gearGt) {
            m = aMaster[0][aTier];
        } else if (aPrefix == OrePrefixes.rod || aPrefix == OrePrefixes.stick) {
            m = aMaster[0][aTier];
        } else if (aPrefix == OrePrefixes.stickLong) {
            m = aMaster[1][aTier];
        } else if (aPrefix == OrePrefixes.bolt) {
            m = aMaster[2][aTier];
        } else if (aPrefix == OrePrefixes.screw) {
            m = aMaster[0][aTier];
        } else if (aPrefix == OrePrefixes.rotor) {
            m = aMaster[1][aTier];
        } else if (aPrefix == OrePrefixes.frame || aPrefix == OrePrefixes.frameGt) {
            m = aMaster[2][aTier];
        } else if (aPrefix == OrePrefixes.ingot) {
            m = aMaster[1][aTier];
        } else if (aPrefix == OrePrefixes.plate) {
            m = aMaster[0][aTier];
        } else if (aPrefix == OrePrefixes.plateDouble) {
            m = aMaster[0][aTier];
        } else if (aPrefix == OrePrefixes.ring) {
            m = aMaster[2][aTier];
        } else if (aPrefix == OrePrefixes.cell) {
            m = aMaster[1][aTier];
        } else {
            m = aMaterial_Main[aTier];
        }

        ItemStack aReturn = ItemUtils.getOrePrefixStack(aPrefix, m, aAmount);

        // If Invalid, Try First Material
        if (!ItemUtils.checkForInvalidItems(aReturn)) {
            m = aMaster[0][aTier];
            aReturn = ItemUtils.getOrePrefixStack(aPrefix, m, aAmount);

            // If Invalid, Try Second Material
            if (!ItemUtils.checkForInvalidItems(aReturn)) {
                m = aMaster[1][aTier];
                aReturn = ItemUtils.getOrePrefixStack(aPrefix, m, aAmount);

                // If Invalid, Try Third Material
                if (!ItemUtils.checkForInvalidItems(aReturn)) {
                    m = aMaster[2][aTier];
                    aReturn = ItemUtils.getOrePrefixStack(aPrefix, m, aAmount);

                    // All Invalid?
                    // Let's add a special error ingot.
                    if (!ItemUtils.checkForInvalidItems(aReturn)) {
                        aReturn = ItemUtils.getErrorStack(1, (aPrefix + m.getLocalizedName() + " x" + aAmount));
                    }
                }
            }
        }

        return aReturn;
    }

    public static ItemStack getElectricMotor(int tier, int size) {
        return switch (tier){
            case 1 -> GTUtility.copyAmount(size, CI.electricMotor_LV);
            case 2 -> GTUtility.copyAmount(size, CI.electricMotor_MV);
            case 3 -> GTUtility.copyAmount(size, CI.electricMotor_HV);
            case 4 -> GTUtility.copyAmount(size, CI.electricMotor_EV);
            case 5 -> GTUtility.copyAmount(size, CI.electricMotor_IV);
            case 6 -> GTUtility.copyAmount(size, CI.electricMotor_LuV);
            case 7 -> GTUtility.copyAmount(size, CI.electricMotor_ZPM);
            case 8 -> GTUtility.copyAmount(size, CI.electricMotor_UV);
            case 9 -> GTUtility.copyAmount(size, CI.electricMotor_UHV);
            default -> throw new IllegalArgumentException();
        };
    }

    public static ItemStack getFluidRegulator(int aTier, int aSize) {
        return switch(aTier){
            case 0 -> GTUtility.copyAmount(aSize, CI.fluidRegulator_LV);
            case 1 -> GTUtility.copyAmount(aSize, CI.fluidRegulator_LV);
            case 2 -> GTUtility.copyAmount(aSize, CI.fluidRegulator_MV);
            case 3 -> GTUtility.copyAmount(aSize, CI.fluidRegulator_HV);
            case 4 -> GTUtility.copyAmount(aSize, CI.fluidRegulator_EV);
            case 5 -> GTUtility.copyAmount(aSize, CI.fluidRegulator_IV);
            case 6 -> GTUtility.copyAmount(aSize, CI.fluidRegulator_LuV);
            case 7 -> GTUtility.copyAmount(aSize, CI.fluidRegulator_ZPM);
            case 8 -> GTUtility.copyAmount(aSize, CI.fluidRegulator_UV);
            case 9 -> GTUtility.copyAmount(aSize, CI.fluidRegulator_UV);
            default -> throw new IllegalArgumentException();
        };
    }

    public static ItemStack getElectricPiston(int tier, int size) {
        return switch (tier){
            case 1 -> GTUtility.copyAmount(size, CI.electricPiston_LV);
            case 2 -> GTUtility.copyAmount(size, CI.electricPiston_MV);
            case 3 -> GTUtility.copyAmount(size, CI.electricPiston_HV);
            case 4 -> GTUtility.copyAmount(size, CI.electricPiston_EV);
            case 5 -> GTUtility.copyAmount(size, CI.electricPiston_IV);
            case 6 -> GTUtility.copyAmount(size, CI.electricPiston_LuV);
            case 7 -> GTUtility.copyAmount(size, CI.electricPiston_ZPM);
            case 8 -> GTUtility.copyAmount(size, CI.electricPiston_UV);
            case 9 -> GTUtility.copyAmount(size, CI.electricPiston_UHV);
            default -> throw new IllegalArgumentException();
        };
    }

    public static ItemStack getElectricPump(int tier, int size) {
        return switch (tier){
            case 1 -> GTUtility.copyAmount(size, CI.electricPump_LV);
            case 2 -> GTUtility.copyAmount(size, CI.electricPump_MV);
            case 3 -> GTUtility.copyAmount(size, CI.electricPump_HV);
            case 4 -> GTUtility.copyAmount(size, CI.electricPump_EV);
            case 5 -> GTUtility.copyAmount(size, CI.electricPump_IV);
            case 6 -> GTUtility.copyAmount(size, CI.electricPump_LuV);
            case 7 -> GTUtility.copyAmount(size, CI.electricPump_ZPM);
            case 8 -> GTUtility.copyAmount(size, CI.electricPump_UV);
            case 9 -> GTUtility.copyAmount(size, CI.electricPump_UHV);
            default -> throw new IllegalArgumentException();
        };
    }

    public static ItemStack getRobotArm(int tier, int size) {
        return switch (tier){
            case 1 -> GTUtility.copyAmount(size, CI.robotArm_LV);
            case 2 -> GTUtility.copyAmount(size, CI.robotArm_MV);
            case 3 -> GTUtility.copyAmount(size, CI.robotArm_HV);
            case 4 -> GTUtility.copyAmount(size, CI.robotArm_EV);
            case 5 -> GTUtility.copyAmount(size, CI.robotArm_IV);
            case 6 -> GTUtility.copyAmount(size, CI.robotArm_LuV);
            case 7 -> GTUtility.copyAmount(size, CI.robotArm_ZPM);
            case 8 -> GTUtility.copyAmount(size, CI.robotArm_UV);
            case 9 -> GTUtility.copyAmount(size, CI.robotArm_UHV);
            default -> throw new IllegalArgumentException();
        };
    }

    public static ItemStack getConveyor(int tier, int size) {
        return switch (tier){
            case 1 -> GTUtility.copyAmount(size, CI.conveyorModule_LV);
            case 2 -> GTUtility.copyAmount(size, CI.conveyorModule_MV);
            case 3 -> GTUtility.copyAmount(size, CI.conveyorModule_HV);
            case 4 -> GTUtility.copyAmount(size, CI.conveyorModule_EV);
            case 5 -> GTUtility.copyAmount(size, CI.conveyorModule_IV);
            case 6 -> GTUtility.copyAmount(size, CI.conveyorModule_LuV);
            case 7 -> GTUtility.copyAmount(size, CI.conveyorModule_ZPM);
            case 8 -> GTUtility.copyAmount(size, CI.conveyorModule_UV);
            case 9 -> GTUtility.copyAmount(size, CI.conveyorModule_UHV);
            default -> throw new IllegalArgumentException();
        };
    }

    public static ItemStack getEmitter(int tier, int size) {
        return switch (tier){
            case 1 -> GTUtility.copyAmount(size, CI.emitter_LV);
            case 2 -> GTUtility.copyAmount(size, CI.emitter_MV);
            case 3 -> GTUtility.copyAmount(size, CI.emitter_HV);
            case 4 -> GTUtility.copyAmount(size, CI.emitter_EV);
            case 5 -> GTUtility.copyAmount(size, CI.emitter_IV);
            case 6 -> GTUtility.copyAmount(size, CI.emitter_LuV);
            case 7 -> GTUtility.copyAmount(size, CI.emitter_ZPM);
            case 8 -> GTUtility.copyAmount(size, CI.emitter_UV);
            case 9 -> GTUtility.copyAmount(size, CI.emitter_UHV);
            default -> throw new IllegalArgumentException();
        };
    }

    public static ItemStack getSensor(int tier, int size) {
        return switch (tier){
            case 1 -> GTUtility.copyAmount(size, CI.electricMotor_LV);
            case 2 -> GTUtility.copyAmount(size, CI.electricMotor_MV);
            case 3 -> GTUtility.copyAmount(size, CI.electricMotor_HV);
            case 4 -> GTUtility.copyAmount(size, CI.electricMotor_EV);
            case 5 -> GTUtility.copyAmount(size, CI.electricMotor_IV);
            case 6 -> GTUtility.copyAmount(size, CI.electricMotor_LuV);
            case 7 -> GTUtility.copyAmount(size, CI.electricMotor_ZPM);
            case 8 -> GTUtility.copyAmount(size, CI.electricMotor_UV);
            case 9 -> GTUtility.copyAmount(size, CI.electricMotor_UHV);
            default -> throw new IllegalArgumentException();
        };
    }

    public static ItemStack getFieldGenerator(int tier, int size) {
        return switch (tier){
            case 1 -> GTUtility.copyAmount(size, CI.fieldGenerator_LV);
            case 2 -> GTUtility.copyAmount(size, CI.fieldGenerator_MV);
            case 3 -> GTUtility.copyAmount(size, CI.fieldGenerator_HV);
            case 4 -> GTUtility.copyAmount(size, CI.fieldGenerator_EV);
            case 5 -> GTUtility.copyAmount(size, CI.fieldGenerator_IV);
            case 6 -> GTUtility.copyAmount(size, CI.fieldGenerator_LuV);
            case 7 -> GTUtility.copyAmount(size, CI.fieldGenerator_ZPM);
            case 8 -> GTUtility.copyAmount(size, CI.fieldGenerator_UV);
            case 9 -> GTUtility.copyAmount(size, CI.fieldGenerator_UHV);
            default -> throw new IllegalArgumentException();
        };
    }

    public static ItemStack getTieredMachineHull(int tier, int size) {
        return switch (tier){
            case 0 -> GTUtility.copyAmount(size, CI.machineHull_ULV);
            case 1 -> GTUtility.copyAmount(size, CI.machineHull_LV);
            case 2 -> GTUtility.copyAmount(size, CI.machineHull_MV);
            case 3 -> GTUtility.copyAmount(size, CI.machineHull_HV);
            case 4 -> GTUtility.copyAmount(size, CI.machineHull_EV);
            case 5 -> GTUtility.copyAmount(size, CI.machineHull_IV);
            case 6 -> GTUtility.copyAmount(size, CI.machineHull_LuV);
            case 7 -> GTUtility.copyAmount(size, CI.machineHull_ZPM);
            case 8 -> GTUtility.copyAmount(size, CI.machineHull_UV);
            case 9 -> GTUtility.copyAmount(size, CI.machineHull_UHV);
            default -> throw new IllegalArgumentException();
        };
    }

    public static ItemStack getTieredGTPPMachineCasing(int aTier, int aAmount) {
        GregtechItemList[] aHulls = new GregtechItemList[] { GregtechItemList.GTPP_Casing_ULV,
            GregtechItemList.GTPP_Casing_LV, GregtechItemList.GTPP_Casing_MV, GregtechItemList.GTPP_Casing_HV,
            GregtechItemList.GTPP_Casing_EV, GregtechItemList.GTPP_Casing_IV, GregtechItemList.GTPP_Casing_LuV,
            GregtechItemList.GTPP_Casing_ZPM, GregtechItemList.GTPP_Casing_UV, GregtechItemList.GTPP_Casing_UHV };
        return aHulls[aTier].get(aAmount);
    }

    public static ItemStack getTieredComponentOfMaterial(Materials aMaterial, OrePrefixes aPrefix, int aAmount) {
        return ItemUtils.getOrePrefixStack(aPrefix, aMaterial, aAmount);
    }

    public static ItemStack getTransmissionComponent(int aTier, int aAmount) {
        GregtechItemList[] aTransParts = new GregtechItemList[] { null, GregtechItemList.TransmissionComponent_LV,
            GregtechItemList.TransmissionComponent_MV, GregtechItemList.TransmissionComponent_HV,
            GregtechItemList.TransmissionComponent_EV, GregtechItemList.TransmissionComponent_IV,
            GregtechItemList.TransmissionComponent_LuV, GregtechItemList.TransmissionComponent_ZPM,
            GregtechItemList.TransmissionComponent_UV, GregtechItemList.TransmissionComponent_UHV, };
        return aTransParts[aTier].get(aAmount);
    }

    public static ItemStack getEmptyCatalyst(int aAmount) {
        return GregtechItemList.EmptyCatalystCarrier.get(aAmount);
    }

    /**
     * Aluminium + Silver Catalyst
     *
     * @param aAmount - Stacksize
     * @return - A Catalyst stack of given size
     */
    public static ItemStack getGreenCatalyst(int aAmount) {
        return GregtechItemList.GreenMetalCatalyst.get(aAmount);
    }

    /**
     * Iridium + Ruthenium Catalyst
     *
     * @param aAmount - Stacksize
     * @return - A Catalyst stack of given size
     */
    public static ItemStack getPurpleCatalyst(int aAmount) {
        return GregtechItemList.PurpleMetalCatalyst.get(aAmount);
    }

    /**
     * Platinum + Rhodium Catalyst
     *
     * @param aAmount - Stacksize
     * @return - A Catalyst stack of given size
     */
    public static ItemStack getPinkCatalyst(int aAmount) {
        return GregtechItemList.PinkMetalCatalyst.get(aAmount);
    }
}
