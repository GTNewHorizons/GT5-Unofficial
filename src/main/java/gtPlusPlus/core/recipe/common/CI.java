package gtPlusPlus.core.recipe.common;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.item.chemistry.AgriculturalChem;
import gtPlusPlus.core.item.chemistry.GenericChem;
import gtPlusPlus.core.material.ALLOY;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.recipe.LOADER_Machine_Components;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import ic2.core.Ic2Items;

public class CI {

    // null
    public static ItemStack _NULL = ItemUtils.getErrorStack(1);

    // bits
    public static long bits = GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE
            | GT_ModHandler.RecipeBits.BUFFERED;
    public static long bitsd = GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
            | GT_ModHandler.RecipeBits.REVERSIBLE
            | GT_ModHandler.RecipeBits.BUFFERED;

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
            Materials.StainlessSteel, Materials.Titanium, Materials.TungstenSteel, Materials.Chrome, Materials.Iridium,
            Materials.Osmium, Materials.Neutronium };

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
        if (tier == 0) {
            return "circuitPrimitive";
        } else if (tier == 1) {
            return "circuitBasic";
        } else if (tier == 2) {
            return "circuitGood";
        } else if (tier == 3) {
            return "circuitAdvanced";
        } else if (tier == 4) {
            return "circuitData";
        } else if (tier == 5) {
            return "circuitElite";
        } else if (tier == 6) {
            return "circuitMaster";
        } else if (tier == 7) {
            return "circuitUltimate";
        } else if (tier == 8) {
            return "circuitSuperconductor";
        } else if (tier == 9) {
            return "circuitInfinite";
        } else if (tier == 10) {
            return "circuitQuantum";
        } else {
            return "circuitPrimitive";
        }
    }

    public static ItemStack getNumberedCircuit(int Meta) {
        return ItemUtils.getGregtechCircuit(Meta);
    }

    private static Object getMaterialFromTier(int tier) {
        if (tier == 0) {
            return Materials.Wood;
        } else if (tier == 1) {
            return Materials.Lead;
        } else if (tier == 2) {
            return Materials.Bronze;
        } else if (tier == 3) {
            return Materials.Steel;
        } else if (tier == 4) {
            return ALLOY.EGLIN_STEEL;
        } else if (tier == 5) {
            return Materials.Aluminium;
        } else if (tier == 6) {
            return ALLOY.MARAGING250;
        } else if (tier == 7) {
            return ALLOY.TANTALLOY_61;
        } else if (tier == 8) {
            return ALLOY.INCONEL_792;
        } else if (tier == 9) {
            return ALLOY.ZERON_100;
        } else if (tier == 10) {
            return Materials.NaquadahEnriched;
        } else if (tier == 11) {
            return Materials.Neutronium;
        }
        return Materials._NULL;
    }

    public static String getTieredComponent(OrePrefixes type, int tier) {
        Object material = getMaterialFromTier(tier);
        if (material != null) {
            if (material instanceof Materials) {
                // return (ItemStack) type.get(material);
                String materialName = ((Materials) material).mDefaultLocalName;
                Logger.INFO("Searching for a component named " + type.name() + materialName);
                // return ItemUtils.getItemStackOfAmountFromOreDict(type.name()+materialName, 1);
                return (type.name() + materialName);
            } else {
                String materialName = (Utils.sanitizeString(((Material) material).getLocalizedName()));
                Logger.INFO("Searching for a component named " + type.name() + materialName);
                // return ItemUtils.getItemStackOfAmountFromOreDict(type.name()+materialName, 1);
                return (type.name() + materialName);
            }
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
        if (tier == 0) {
            return machineHull_ULV;
        } else if (tier == 1) {
            return machineHull_LV;
        } else if (tier == 2) {
            return machineHull_MV;
        } else if (tier == 3) {
            return machineHull_HV;
        } else if (tier == 4) {
            return machineHull_EV;
        } else if (tier == 5) {
            return machineHull_IV;
        } else if (tier == 6) {
            return machineHull_LuV;
        } else if (tier == 7) {
            return machineHull_ZPM;
        } else if (tier == 8) {
            return machineHull_UV;
        } else if (tier == 9) {
            return machineHull_UHV;
        } else {
            return GregtechItemList.Casing_Multi_Use.get(1);
        }
    }

    public static ItemStack getTieredMachineCasing(int tier) {
        if (tier == 0) {
            if (machineCasing_ULV == null) {
                machineCasing_ULV = ItemList.Casing_ULV.get(1);
            }
            return machineCasing_ULV;
        } else if (tier == 1) {
            return machineCasing_LV;
        } else if (tier == 2) {
            return machineCasing_MV;
        } else if (tier == 3) {
            return machineCasing_HV;
        } else if (tier == 4) {
            return machineCasing_EV;
        } else if (tier == 5) {
            return machineCasing_IV;
        } else if (tier == 6) {
            return machineCasing_LuV;
        } else if (tier == 7) {
            return machineCasing_ZPM;
        } else if (tier == 8) {
            return machineCasing_UV;
        } else if (tier == 9) {
            return machineCasing_UHV;
        } else {
            return GregtechItemList.Casing_Multi_Use.get(1);
        }
    }

    public static void init() {
        // Set Explosives
        explosivePowderKeg = ItemList.Block_Powderbarrel.get(1);
        explosiveTNT = ItemUtils.getSimpleStack(Blocks.tnt).copy();
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
        LOADER_Machine_Components.initialise();
    }

    public static ItemStack emptyCells(int i) {
        return ItemUtils.getEmptyCell(i);
    }

    private static final Material[] aMaterial_Main = new Material[] { ALLOY.POTIN, ALLOY.TUMBAGA, ALLOY.EGLIN_STEEL,
            ALLOY.INCONEL_625, ALLOY.INCOLOY_DS, ALLOY.NITINOL_60, ALLOY.ZERON_100, ALLOY.PIKYONIUM,
            ELEMENT.STANDALONE.ADVANCED_NITINOL, ALLOY.ABYSSAL, ALLOY.QUANTUM, ELEMENT.STANDALONE.HYPOGEN };

    private static final Material[] aMaterial_Secondary = new Material[] { ALLOY.STEEL, ALLOY.SILICON_CARBIDE,
            ALLOY.BLOODSTEEL, ALLOY.TANTALUM_CARBIDE, ALLOY.INCONEL_792, ALLOY.ARCANITE, ALLOY.LAFIUM, ALLOY.CINOBITE,
            ALLOY.TITANSTEEL, ALLOY.OCTIRON, ELEMENT.STANDALONE.CELESTIAL_TUNGSTEN, ELEMENT.STANDALONE.HYPOGEN };

    private static final Material[] aMaterial_Tertiary = new Material[] { ELEMENT.getInstance().LEAD,
            ELEMENT.getInstance().ALUMINIUM, ELEMENT.STANDALONE.BLACK_METAL, ELEMENT.getInstance().TITANIUM,
            ALLOY.HASTELLOY_N, ALLOY.ENERGYCRYSTAL, ALLOY.TRINIUM_NAQUADAH_CARBON, ALLOY.TRINIUM_REINFORCED_STEEL, // Arceus
            ALLOY.TITANSTEEL, ELEMENT.STANDALONE.ASTRAL_TITANIUM, ELEMENT.STANDALONE.CELESTIAL_TUNGSTEN,
            ELEMENT.STANDALONE.HYPOGEN };

    private static final Materials[] aMaterial_Cables = new Materials[] { Materials.Tin, Materials.Cobalt,
            Materials.AnnealedCopper, Materials.Gold, Materials.Titanium, Materials.Nichrome, Materials.Platinum,
            Materials.YttriumBariumCuprate, Materials.Naquadah, Materials.Duranium, Materials.SuperconductorUHV, };

    private static final Materials[] aMaterial_Circuits = new Materials[] { Materials.Primitive, Materials.Basic,
            Materials.Good, Materials.Advanced, Materials.Data, Materials.Data, Materials.Elite, Materials.Master,
            Materials.Ultimate, Materials.SuperconductorUHV, Materials.Infinite, };

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
        // Weird Legacy handling
        /*
         * ItemStack aCell = getTieredComponent(OrePrefixes.liquid, aTier, 1); FluidStack a =
         * GT_Utility.getFluidForFilledItem(aCell, true); if (a == null) { a = aMaster[aType][aTier].getFluid(aAmount);
         * }
         */

        // Modern Handling
        FluidStack a = aMaster[aType][aTier].getFluidStack(aAmount);
        if (a == null) {
            ItemStack aCell = getTieredComponent(OrePrefixes.liquid, aTier, 1);
            if (aCell != null) {
                a = GT_Utility.getFluidForFilledItem(aCell, true);
                a.amount = aAmount;
            }
        }
        return a;
    }

    public static ItemStack getEnergyCore(int aTier, int aAmount) {
        ItemStack[] aOutput = new ItemStack[] {
                ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore" + "1", 1),
                ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore" + "2", 1),
                ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore" + "3", 1),
                ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore" + "4", 1),
                ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore" + "5", 1),
                ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore" + "6", 1),
                ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore" + "7", 1),
                ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore" + "8", 1),
                ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore" + "9", 1),
                ItemUtils.getItemStackFromFQRN("miscutils:item.itemBufferCore" + "10", 1) };
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
            ItemStack aCell = aMaster[aMatID][aTier].getCell(aAmount);
            return aCell;
        }

        if (aPrefix == OrePrefixes.circuit) {
            /*
             * if (aTier == 4) { return ItemUtils.getSimpleStack(CI.getDataStick(), aAmount); } else if (aTier == 5) {
             * return ItemUtils.getSimpleStack(CI.getDataOrb(), aAmount); }
             */
            return ItemUtils.getOrePrefixStack(OrePrefixes.circuit, aMaterial_Circuits[aTier], aAmount);
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
                return ItemUtils.getOrePrefixStack(aPrefix, ALLOY.MARAGING350, aAmount);
            } else if (aTier == 6) {
                return ItemUtils.getOrePrefixStack(aPrefix, ALLOY.STABALLOY, aAmount);
            } else if (aTier == 7) {
                return ItemUtils.getOrePrefixStack(aPrefix, ALLOY.HASTELLOY_X, aAmount);
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

    public static ItemStack getElectricMotor(int aTier, int aSize) {
        ItemStack aType;
        int aLazyTier = 1;
        if (aTier == aLazyTier++) {
            aType = CI.electricMotor_LV;
        } else if (aTier == aLazyTier++) {
            aType = CI.electricMotor_MV;
        } else if (aTier == aLazyTier++) {
            aType = CI.electricMotor_HV;
        } else if (aTier == aLazyTier++) {
            aType = CI.electricMotor_EV;
        } else if (aTier == aLazyTier++) {
            aType = CI.electricMotor_IV;
        } else if (aTier == aLazyTier++) {
            aType = CI.electricMotor_LuV;
        } else if (aTier == aLazyTier++) {
            aType = CI.electricMotor_ZPM;
        } else if (aTier == aLazyTier++) {
            aType = CI.electricMotor_UV;
        } else if (aTier == aLazyTier) {
            aType = CI.electricMotor_UHV;
        } else {
            throw new IllegalArgumentException();
        }
        return ItemUtils.getSimpleStack(aType, aSize);
    }

    public static ItemStack getFluidRegulator(int aTier, int aSize) {
        ItemStack aType;
        int aLazyTier = 0;
        if (aTier == aLazyTier++) {
            aType = CI.fluidRegulator_LV;
        } else if (aTier == aLazyTier++) {
            aType = CI.fluidRegulator_LV;
        } else if (aTier == aLazyTier++) {
            aType = CI.fluidRegulator_MV;
        } else if (aTier == aLazyTier++) {
            aType = CI.fluidRegulator_HV;
        } else if (aTier == aLazyTier++) {
            aType = CI.fluidRegulator_EV;
        } else if (aTier == aLazyTier++) {
            aType = CI.fluidRegulator_IV;
        } else if (aTier == aLazyTier++) {
            aType = CI.fluidRegulator_LuV;
        } else if (aTier == aLazyTier++) {
            aType = CI.fluidRegulator_ZPM;
        } else if (aTier == aLazyTier++) {
            aType = CI.fluidRegulator_UV;
        } else if (aTier == aLazyTier) {
            aType = CI.fluidRegulator_UV;
        } else {
            throw new IllegalArgumentException();
        }
        return ItemUtils.getSimpleStack(aType, aSize);
    }

    public static ItemStack getElectricPiston(int aTier, int aSize) {
        ItemStack aType;
        int aLazyTier = 1;
        if (aTier == aLazyTier++) {
            aType = CI.electricPiston_LV;
        } else if (aTier == aLazyTier++) {
            aType = CI.electricPiston_MV;
        } else if (aTier == aLazyTier++) {
            aType = CI.electricPiston_HV;
        } else if (aTier == aLazyTier++) {
            aType = CI.electricPiston_EV;
        } else if (aTier == aLazyTier++) {
            aType = CI.electricPiston_IV;
        } else if (aTier == aLazyTier++) {
            aType = CI.electricPiston_LuV;
        } else if (aTier == aLazyTier++) {
            aType = CI.electricPiston_ZPM;
        } else if (aTier == aLazyTier++) {
            aType = CI.electricPiston_UV;
        } else if (aTier == aLazyTier) {
            aType = CI.electricPiston_UHV;
        } else {
            throw new IllegalArgumentException();
        }
        return ItemUtils.getSimpleStack(aType, aSize);
    }

    public static ItemStack getElectricPump(int aTier, int aSize) {
        ItemStack aType;
        int aLazyTier = 1;
        if (aTier == aLazyTier++) {
            aType = CI.electricPump_LV;
        } else if (aTier == aLazyTier++) {
            aType = CI.electricPump_MV;
        } else if (aTier == aLazyTier++) {
            aType = CI.electricPump_HV;
        } else if (aTier == aLazyTier++) {
            aType = CI.electricPump_EV;
        } else if (aTier == aLazyTier++) {
            aType = CI.electricPump_IV;
        } else if (aTier == aLazyTier++) {
            aType = CI.electricPump_LuV;
        } else if (aTier == aLazyTier++) {
            aType = CI.electricPump_ZPM;
        } else if (aTier == aLazyTier++) {
            aType = CI.electricPump_UV;
        } else if (aTier == aLazyTier) {
            aType = CI.electricPump_UHV;
        } else {
            throw new IllegalArgumentException();
        }
        return ItemUtils.getSimpleStack(aType, aSize);
    }

    public static ItemStack getRobotArm(int aTier, int aSize) {
        ItemStack aType;
        int aLazyTier = 1;
        if (aTier == aLazyTier++) {
            aType = CI.robotArm_LV;
        } else if (aTier == aLazyTier++) {
            aType = CI.robotArm_MV;
        } else if (aTier == aLazyTier++) {
            aType = CI.robotArm_HV;
        } else if (aTier == aLazyTier++) {
            aType = CI.robotArm_EV;
        } else if (aTier == aLazyTier++) {
            aType = CI.robotArm_IV;
        } else if (aTier == aLazyTier++) {
            aType = CI.robotArm_LuV;
        } else if (aTier == aLazyTier++) {
            aType = CI.robotArm_ZPM;
        } else if (aTier == aLazyTier++) {
            aType = CI.robotArm_UV;
        } else if (aTier == aLazyTier) {
            aType = CI.robotArm_UHV;
        } else {
            throw new IllegalArgumentException();
        }
        return ItemUtils.getSimpleStack(aType, aSize);
    }

    public static ItemStack getConveyor(int aTier, int aSize) {
        ItemStack aType;
        int aLazyTier = 1;
        if (aTier == aLazyTier++) {
            aType = CI.conveyorModule_LV;
        } else if (aTier == aLazyTier++) {
            aType = CI.conveyorModule_MV;
        } else if (aTier == aLazyTier++) {
            aType = CI.conveyorModule_HV;
        } else if (aTier == aLazyTier++) {
            aType = CI.conveyorModule_EV;
        } else if (aTier == aLazyTier++) {
            aType = CI.conveyorModule_IV;
        } else if (aTier == aLazyTier++) {
            aType = CI.conveyorModule_LuV;
        } else if (aTier == aLazyTier++) {
            aType = CI.conveyorModule_ZPM;
        } else if (aTier == aLazyTier++) {
            aType = CI.conveyorModule_UV;
        } else if (aTier == aLazyTier) {
            aType = CI.conveyorModule_UHV;
        } else {
            throw new IllegalArgumentException();
        }
        return ItemUtils.getSimpleStack(aType, aSize);
    }

    public static ItemStack getEmitter(int aTier, int aSize) {
        ItemStack aType;
        int aLazyTier = 1;
        if (aTier == aLazyTier++) {
            aType = CI.emitter_LV;
        } else if (aTier == aLazyTier++) {
            aType = CI.emitter_MV;
        } else if (aTier == aLazyTier++) {
            aType = CI.emitter_HV;
        } else if (aTier == aLazyTier++) {
            aType = CI.emitter_EV;
        } else if (aTier == aLazyTier++) {
            aType = CI.emitter_IV;
        } else if (aTier == aLazyTier++) {
            aType = CI.emitter_LuV;
        } else if (aTier == aLazyTier++) {
            aType = CI.emitter_ZPM;
        } else if (aTier == aLazyTier++) {
            aType = CI.emitter_UV;
        } else if (aTier == aLazyTier) {
            aType = CI.emitter_UHV;
        } else {
            throw new IllegalArgumentException();
        }
        return ItemUtils.getSimpleStack(aType, aSize);
    }

    public static ItemStack getSensor(int aTier, int aSize) {
        ItemStack aType;
        int aLazyTier = 1;
        if (aTier == aLazyTier++) {
            aType = CI.sensor_LV;
        } else if (aTier == aLazyTier++) {
            aType = CI.sensor_MV;
        } else if (aTier == aLazyTier++) {
            aType = CI.sensor_HV;
        } else if (aTier == aLazyTier++) {
            aType = CI.sensor_EV;
        } else if (aTier == aLazyTier++) {
            aType = CI.sensor_IV;
        } else if (aTier == aLazyTier++) {
            aType = CI.sensor_LuV;
        } else if (aTier == aLazyTier++) {
            aType = CI.sensor_ZPM;
        } else if (aTier == aLazyTier++) {
            aType = CI.sensor_UV;
        } else if (aTier == aLazyTier) {
            aType = CI.sensor_UHV;
        } else {
            throw new IllegalArgumentException();
        }
        return ItemUtils.getSimpleStack(aType, aSize);
    }

    public static ItemStack getFieldGenerator(int aTier, int aSize) {
        ItemStack aType;
        int aLazyTier = 1;
        if (aTier == aLazyTier++) {
            aType = CI.fieldGenerator_LV;
        } else if (aTier == aLazyTier++) {
            aType = CI.fieldGenerator_MV;
        } else if (aTier == aLazyTier++) {
            aType = CI.fieldGenerator_HV;
        } else if (aTier == aLazyTier++) {
            aType = CI.fieldGenerator_EV;
        } else if (aTier == aLazyTier++) {
            aType = CI.fieldGenerator_IV;
        } else if (aTier == aLazyTier++) {
            aType = CI.fieldGenerator_LuV;
        } else if (aTier == aLazyTier++) {
            aType = CI.fieldGenerator_ZPM;
        } else if (aTier == aLazyTier++) {
            aType = CI.fieldGenerator_UV;
        } else if (aTier == aLazyTier) {
            aType = CI.fieldGenerator_UHV;
        } else {
            throw new IllegalArgumentException();
        }
        return ItemUtils.getSimpleStack(aType, aSize);
    }

    public static ItemStack getTieredMachineHull(int aTier, int aSize) {
        ItemStack aType;
        int aLazyTier = 0;
        if (aTier == aLazyTier++) {
            aType = CI.machineHull_ULV;
        } else if (aTier == aLazyTier++) {
            aType = CI.machineHull_LV;
        } else if (aTier == aLazyTier++) {
            aType = CI.machineHull_MV;
        } else if (aTier == aLazyTier++) {
            aType = CI.machineHull_HV;
        } else if (aTier == aLazyTier++) {
            aType = CI.machineHull_EV;
        } else if (aTier == aLazyTier++) {
            aType = CI.machineHull_IV;
        } else if (aTier == aLazyTier++) {
            aType = CI.machineHull_LuV;
        } else if (aTier == aLazyTier++) {
            aType = CI.machineHull_ZPM;
        } else if (aTier == aLazyTier++) {
            aType = CI.machineHull_UV;
        } else if (aTier == aLazyTier) {
            aType = CI.machineHull_UHV;
        } else {
            throw new IllegalArgumentException();
        }
        return ItemUtils.getSimpleStack(aType, aSize);
    }

    public static ItemStack getHeatCoil(int i) {
        if (i > 8) {
            i = 8;
        }
        return ItemUtils.simpleMetaStack(GregTech_API.sBlockCasings5, i, 1);
    }

    public static ItemStack getNumberedBioCircuit(int i) {
        return ItemUtils.simpleMetaStack(AgriculturalChem.mBioCircuit, i, 0);
    }

    public static ItemStack getNumberedAdvancedCircuit(int i) {
        return ItemUtils.simpleMetaStack(GenericChem.mAdvancedCircuit, i, 0);
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
        return ItemUtils.simpleMetaStack(AgriculturalChem.mAgrichemItem1, 13, aAmount);
    }

    /**
     * Aluminium + Silver Catalyst
     * 
     * @param aAmount - Stacksize
     * @return - A Catalyst stack of given size
     */
    public static ItemStack getGreenCatalyst(int aAmount) {
        return ItemUtils.simpleMetaStack(AgriculturalChem.mAgrichemItem1, 14, aAmount);
    }

    /**
     * Iridium + Ruthenium Catalyst
     * 
     * @param aAmount - Stacksize
     * @return - A Catalyst stack of given size
     */
    public static ItemStack getPurpleCatalyst(int aAmount) {
        return ItemUtils.simpleMetaStack(GenericChem.mGenericChemItem1, 4, aAmount);
    }

    /**
     * Platinum + Rhodium Catalyst
     * 
     * @param aAmount - Stacksize
     * @return - A Catalyst stack of given size
     */
    public static ItemStack getPinkCatalyst(int aAmount) {
        return ItemUtils.simpleMetaStack(GenericChem.mGenericChemItem1, 6, aAmount);
    }
}
