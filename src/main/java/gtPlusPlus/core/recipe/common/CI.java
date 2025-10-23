package gtPlusPlus.core.recipe.common;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.ExternalMaterials;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.api.util.StringUtils;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import ic2.core.Ic2Items;

public class CI {

    // bits
    public static long bits = GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.REVERSIBLE
        | GTModHandler.RecipeBits.BUFFERED;
    public static long bitsd = GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
        | GTModHandler.RecipeBits.REVERSIBLE
        | GTModHandler.RecipeBits.BUFFERED;

    // Gearbox Casings
    public static ItemStack gearboxCasing_Tier_1;
    public static ItemStack gearboxCasing_Tier_2;
    public static ItemStack gearboxCasing_Tier_3;
    public static ItemStack gearboxCasing_Tier_4;

    public static String[] component_Plate;
    public static String[] component_Rod;
    public static String[] component_Ingot;

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
    }

    public static final String[] circuits = new String[] { "circuitPrimitive", // ULV
        "circuitBasic", // LV
        "circuitGood", // MV
        "circuitAdvanced", // HV
        "circuitData", // EV
        "circuitElite", // IV
        "circuitMaster", // LUV
        "circuitUltimate", // ZPM
        "circuitSuperconductor", // UV
        "circuitInfinite", // UHV
        "circuitQuantum"// UEV
    };

    private static Object getMaterialFromTier(int tier) {
        return switch (tier) {
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
                materialName = (StringUtils.sanitizeString(((Material) material).getLocalizedName()));
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
        return switch (tier) {
            case 0 -> ItemList.Hull_ULV.get(1);
            case 1 -> ItemList.Hull_LV.get(1);
            case 2 -> ItemList.Hull_MV.get(1);
            case 3 -> ItemList.Hull_HV.get(1);
            case 4 -> ItemList.Hull_EV.get(1);
            case 5 -> ItemList.Hull_IV.get(1);
            case 6 -> ItemList.Hull_LuV.get(1);
            case 7 -> ItemList.Hull_ZPM.get(1);
            case 8 -> ItemList.Hull_UV.get(1);
            case 9 -> ItemList.Hull_MAX.get(1);
            default -> GregtechItemList.Casing_Multi_Use.get(1);
        };
    }

    public static ItemStack getTieredMachineCasing(int tier) {
        return switch (tier) {
            case 0 -> ItemList.Casing_ULV.get(1);
            case 1 -> ItemList.Casing_LV.get(1);
            case 2 -> ItemList.Casing_MV.get(1);
            case 3 -> ItemList.Casing_HV.get(1);
            case 4 -> ItemList.Casing_EV.get(1);
            case 5 -> ItemList.Casing_IV.get(1);
            case 6 -> ItemList.Casing_LuV.get(1);
            case 7 -> ItemList.Casing_ZPM.get(1);
            case 8 -> ItemList.Casing_UV.get(1);
            case 9 -> ItemList.Casing_MAX.get(1);
            default -> GregtechItemList.Casing_Multi_Use.get(1);
        };
    }

    public static void init() {
        // Set Explosives
        explosivePowderKeg = ItemList.Block_Powderbarrel.get(1);
        explosiveTNT = new ItemStack(Blocks.tnt).copy();
        explosiveITNT = Ic2Items.industrialTnt.copy();

        // Gear box Casings
        gearboxCasing_Tier_1 = ItemList.Casing_Gearbox_Bronze.get(1);
        gearboxCasing_Tier_2 = ItemList.Casing_Gearbox_Steel.get(1);
        gearboxCasing_Tier_3 = ItemList.Casing_Gearbox_Titanium.get(1);
        gearboxCasing_Tier_4 = ItemList.Casing_Gearbox_TungstenSteel.get(1);
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
        ItemStack[] aOutput = new ItemStack[] { GregtechItemList.Energy_Core_ULV.get(1),
            GregtechItemList.Energy_Core_LV.get(1), GregtechItemList.Energy_Core_MV.get(1),
            GregtechItemList.Energy_Core_HV.get(1), GregtechItemList.Energy_Core_EV.get(1),
            GregtechItemList.Energy_Core_IV.get(1), GregtechItemList.Energy_Core_LuV.get(1),
            GregtechItemList.Energy_Core_ZPM.get(1), GregtechItemList.Energy_Core_UV.get(1),
            GregtechItemList.Energy_Core_UHV.get(1), };
        return GTUtility.copyAmount(aAmount, aOutput[MathUtils.balance(aTier, 0, 9)]);
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
                switch (aPrefix) {
                    case cableGt01 -> aPrefix = OrePrefixes.wireGt02;
                    case cableGt02 -> aPrefix = OrePrefixes.wireGt04;
                    case cableGt04 -> aPrefix = OrePrefixes.wireGt08;
                    case cableGt08 -> aPrefix = OrePrefixes.wireGt12;
                    case cableGt12 -> aPrefix = OrePrefixes.wireGt16;
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
            return switch (aTier) {
                case 0 -> ItemUtils.getOrePrefixStack(aPrefix, Materials.Lead, aAmount);
                case 1 -> ItemUtils.getOrePrefixStack(aPrefix, Materials.Steel, aAmount);
                case 2 -> ItemUtils.getOrePrefixStack(aPrefix, Materials.StainlessSteel, aAmount);
                case 3 -> ItemUtils.getOrePrefixStack(aPrefix, Materials.Tungsten, aAmount);
                case 4 -> ItemUtils.getOrePrefixStack(aPrefix, Materials.TungstenSteel, aAmount);
                case 5 -> ItemUtils.getOrePrefixStack(aPrefix, MaterialsAlloy.MARAGING350, aAmount);
                case 6 -> ItemUtils.getOrePrefixStack(aPrefix, MaterialsAlloy.STABALLOY, aAmount);
                case 7 -> ItemUtils.getOrePrefixStack(aPrefix, MaterialsAlloy.HASTELLOY_X, aAmount);
                case 8 -> ItemUtils.getOrePrefixStack(aPrefix, Materials.ZPM, aAmount);
                case 9 -> ItemUtils.getOrePrefixStack(OrePrefixes.pipeMedium, Materials.SuperconductorUHV, aAmount);
                case 10 -> ItemUtils.getOrePrefixStack(aPrefix, Materials.Europium, aAmount);
                default -> ItemUtils.getOrePrefixStack(aPrefix, Materials.Titanium, aAmount);
            };
        }

        if (aPrefix == OrePrefixes.rod) {
            aPrefix = OrePrefixes.stick;
        }

        switch (aPrefix) {
            case gear, gearGt, rod, stick, screw, plate, plateDouble -> m = aMaster[0][aTier];
            case stickLong, ingot, rotor, cell -> m = aMaster[1][aTier];
            case bolt, ring, frame, frameGt -> m = aMaster[2][aTier];
            default -> m = aMaterial_Main[aTier];
        }

        ItemStack aReturn = ItemUtils.getOrePrefixStack(aPrefix, m, aAmount);
        // If valid, return
        if (aReturn != null) return aReturn;

        // If Invalid, Try First Material

        m = aMaster[0][aTier];
        aReturn = ItemUtils.getOrePrefixStack(aPrefix, m, aAmount);

        if (aReturn != null) return aReturn;

        // If Invalid, Try Second Materialqcv v
        m = aMaster[1][aTier];
        aReturn = ItemUtils.getOrePrefixStack(aPrefix, m, aAmount);

        if (aReturn != null) return aReturn;

        // If Invalid, Try Third Material

        m = aMaster[2][aTier];
        aReturn = ItemUtils.getOrePrefixStack(aPrefix, m, aAmount);

        // All Invalid?
        // Let's add a special error ingot.
        if (aReturn == null) {
            aReturn = null;
        }

        return aReturn;
    }

    public static ItemStack getElectricMotor(int tier, int size) {
        return switch (tier) {
            case 1 -> ItemList.Electric_Motor_LV.get(size);
            case 2 -> ItemList.Electric_Motor_MV.get(size);
            case 3 -> ItemList.Electric_Motor_HV.get(size);
            case 4 -> ItemList.Electric_Motor_EV.get(size);
            case 5 -> ItemList.Electric_Motor_IV.get(size);
            case 6 -> ItemList.Electric_Motor_LuV.get(size);
            case 7 -> ItemList.Electric_Motor_ZPM.get(size);
            case 8 -> ItemList.Electric_Motor_UV.get(size);
            case 9 -> ItemList.Electric_Motor_UHV.get(size);
            default -> throw new IllegalArgumentException();
        };
    }

    public static ItemStack getFluidRegulator(int tier, int size) {
        return switch (tier) {
            case 0 -> ItemList.FluidRegulator_LV.get(size);
            case 1 -> ItemList.FluidRegulator_LV.get(size);
            case 2 -> ItemList.FluidRegulator_MV.get(size);
            case 3 -> ItemList.FluidRegulator_HV.get(size);
            case 4 -> ItemList.FluidRegulator_EV.get(size);
            case 5 -> ItemList.FluidRegulator_IV.get(size);
            case 6 -> ItemList.FluidRegulator_LuV.get(size);
            case 7 -> ItemList.FluidRegulator_ZPM.get(size);
            case 8 -> ItemList.FluidRegulator_UV.get(size);
            case 9 -> ItemList.FluidRegulator_UV.get(size);
            default -> throw new IllegalArgumentException();
        };
    }

    public static ItemStack getElectricPiston(int tier, int size) {
        return switch (tier) {
            case 1 -> ItemList.Electric_Piston_LV.get(size);
            case 2 -> ItemList.Electric_Piston_MV.get(size);
            case 3 -> ItemList.Electric_Piston_HV.get(size);
            case 4 -> ItemList.Electric_Piston_EV.get(size);
            case 5 -> ItemList.Electric_Piston_IV.get(size);
            case 6 -> ItemList.Electric_Piston_LuV.get(size);
            case 7 -> ItemList.Electric_Piston_ZPM.get(size);
            case 8 -> ItemList.Electric_Piston_UV.get(size);
            case 9 -> ItemList.Electric_Piston_UHV.get(size);
            default -> throw new IllegalArgumentException();
        };
    }

    public static ItemStack getElectricPump(int tier, int size) {
        return switch (tier) {
            case 1 -> ItemList.Electric_Pump_LV.get(size);
            case 2 -> ItemList.Electric_Pump_MV.get(size);
            case 3 -> ItemList.Electric_Pump_HV.get(size);
            case 4 -> ItemList.Electric_Pump_EV.get(size);
            case 5 -> ItemList.Electric_Pump_IV.get(size);
            case 6 -> ItemList.Electric_Pump_LuV.get(size);
            case 7 -> ItemList.Electric_Pump_ZPM.get(size);
            case 8 -> ItemList.Electric_Pump_UV.get(size);
            case 9 -> ItemList.Electric_Pump_UHV.get(size);
            default -> throw new IllegalArgumentException();
        };
    }

    public static ItemStack getRobotArm(int tier, int size) {
        return switch (tier) {
            case 1 -> ItemList.Robot_Arm_LV.get(size);
            case 2 -> ItemList.Robot_Arm_MV.get(size);
            case 3 -> ItemList.Robot_Arm_HV.get(size);
            case 4 -> ItemList.Robot_Arm_EV.get(size);
            case 5 -> ItemList.Robot_Arm_IV.get(size);
            case 6 -> ItemList.Robot_Arm_LuV.get(size);
            case 7 -> ItemList.Robot_Arm_ZPM.get(size);
            case 8 -> ItemList.Robot_Arm_UV.get(size);
            case 9 -> ItemList.Robot_Arm_UHV.get(size);
            default -> throw new IllegalArgumentException();
        };
    }

    public static ItemStack getConveyor(int tier, int size) {
        return switch (tier) {
            case 1 -> ItemList.Conveyor_Module_LV.get(size);
            case 2 -> ItemList.Conveyor_Module_MV.get(size);
            case 3 -> ItemList.Conveyor_Module_HV.get(size);
            case 4 -> ItemList.Conveyor_Module_EV.get(size);
            case 5 -> ItemList.Conveyor_Module_IV.get(size);
            case 6 -> ItemList.Conveyor_Module_LuV.get(size);
            case 7 -> ItemList.Conveyor_Module_ZPM.get(size);
            case 8 -> ItemList.Conveyor_Module_UV.get(size);
            case 9 -> ItemList.Conveyor_Module_UHV.get(size);
            default -> throw new IllegalArgumentException();
        };
    }

    public static ItemStack getEmitter(int tier, int size) {
        return switch (tier) {
            case 1 -> ItemList.Emitter_LV.get(size);
            case 2 -> ItemList.Emitter_MV.get(size);
            case 3 -> ItemList.Emitter_HV.get(size);
            case 4 -> ItemList.Emitter_EV.get(size);
            case 5 -> ItemList.Emitter_IV.get(size);
            case 6 -> ItemList.Emitter_LuV.get(size);
            case 7 -> ItemList.Emitter_ZPM.get(size);
            case 8 -> ItemList.Emitter_UV.get(size);
            case 9 -> ItemList.Emitter_UHV.get(size);
            default -> throw new IllegalArgumentException();
        };
    }

    public static ItemStack getSensor(int tier, int size) {
        return switch (tier) {
            case 1 -> ItemList.Sensor_LV.get(size);
            case 2 -> ItemList.Sensor_MV.get(size);
            case 3 -> ItemList.Sensor_HV.get(size);
            case 4 -> ItemList.Sensor_EV.get(size);
            case 5 -> ItemList.Sensor_IV.get(size);
            case 6 -> ItemList.Sensor_LuV.get(size);
            case 7 -> ItemList.Sensor_ZPM.get(size);
            case 8 -> ItemList.Sensor_UV.get(size);
            case 9 -> ItemList.Sensor_UHV.get(size);
            default -> throw new IllegalArgumentException();
        };
    }

    public static ItemStack getFieldGenerator(int tier, int size) {
        return switch (tier) {
            case 1 -> ItemList.Field_Generator_LV.get(size);
            case 2 -> ItemList.Field_Generator_MV.get(size);
            case 3 -> ItemList.Field_Generator_HV.get(size);
            case 4 -> ItemList.Field_Generator_EV.get(size);
            case 5 -> ItemList.Field_Generator_IV.get(size);
            case 6 -> ItemList.Field_Generator_LuV.get(size);
            case 7 -> ItemList.Field_Generator_ZPM.get(size);
            case 8 -> ItemList.Field_Generator_UV.get(size);
            case 9 -> ItemList.Field_Generator_UHV.get(size);
            default -> throw new IllegalArgumentException();
        };
    }

    public static ItemStack getTieredMachineHull(int tier, int size) {
        return switch (tier) {
            case 0 -> GTUtility.copyAmount(size, ItemList.Hull_ULV.get(1));
            case 1 -> GTUtility.copyAmount(size, ItemList.Hull_LV.get(1));
            case 2 -> GTUtility.copyAmount(size, ItemList.Hull_MV.get(1));
            case 3 -> GTUtility.copyAmount(size, ItemList.Hull_HV.get(1));
            case 4 -> GTUtility.copyAmount(size, ItemList.Hull_EV.get(1));
            case 5 -> GTUtility.copyAmount(size, ItemList.Hull_IV.get(1));
            case 6 -> GTUtility.copyAmount(size, ItemList.Hull_LuV.get(1));
            case 7 -> GTUtility.copyAmount(size, ItemList.Hull_ZPM.get(1));
            case 8 -> GTUtility.copyAmount(size, ItemList.Hull_UV.get(1));
            case 9 -> GTUtility.copyAmount(size, ItemList.Hull_MAX.get(1));
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
