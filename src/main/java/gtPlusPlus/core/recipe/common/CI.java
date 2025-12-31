package gtPlusPlus.core.recipe.common;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class CI {

    // bits
    public static long bitsd = GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
        | GTModHandler.RecipeBits.REVERSIBLE
        | GTModHandler.RecipeBits.BUFFERED;

    public static ItemStack getDataOrb() {
        return ItemList.Tool_DataOrb.get(1);
    }

    public static ItemStack getDataStick() {
        return ItemList.Tool_DataStick.get(1);
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
        FluidStack a = aMaster[0][aTier].getFluidStack(aAmount);
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
                switch (aPrefix.getName()) {
                    case "cableGt01" -> aPrefix = OrePrefixes.wireGt02;
                    case "cableGt02" -> aPrefix = OrePrefixes.wireGt04;
                    case "cableGt04" -> aPrefix = OrePrefixes.wireGt08;
                    case "cableGt08" -> aPrefix = OrePrefixes.wireGt12;
                    case "cableGt12" -> aPrefix = OrePrefixes.wireGt16;
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

        switch (aPrefix.getName()) {
            case "gear", "gearGt", "rod", "stick", "screw", "plate", "plateDouble" -> m = aMaster[0][aTier];
            case "stickLong", "ingot", "rotor", "cell" -> m = aMaster[1][aTier];
            case "bolt", "ring", "frame", "frameGt" -> m = aMaster[2][aTier];
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

    public static ItemStack getEmptyCatalyst(int aAmount) {
        return GregtechItemList.EmptyCatalystCarrier.get(aAmount);
    }
}
