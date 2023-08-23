package goodgenerator.main;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

import cpw.mods.fml.relauncher.FMLInjectionData;

public class GG_Config_Loader {

    public static final Configuration GG_Config = new Configuration(
            new File(new File((File) FMLInjectionData.data()[6], "config"), "GoodGenerator.cfg"));

    public static int LiquidAirConsumptionPerSecond = 2400;
    public static int[] NaquadahFuelVoltage = new int[] { 12960, 2200, 32400, 975000, 2300000, 9511000, 88540000,
            399576000 };
    public static int[] NaquadahFuelTime = new int[] { 100, 500, 150, 20, 20, 80, 100, 160 };
    public static int[] CoolantEfficiency = new int[] { 500, 275, 150, 105 };
    public static int[] ExcitedLiquidCoe = new int[] { 64, 16, 4, 3, 2 };
    public static boolean EnableNaquadahRework = true;

    public static void run() {
        loadCategory();
        loadProperty();
    }

    private static void loadProperty() {
        NaquadahFuelVoltage[0] = GG_Config
                .get("LargeNaquadahReactor", "Uranium Based Liquid Fuel Basic Output Voltage", NaquadahFuelVoltage[0])
                .getInt();
        NaquadahFuelTime[0] = GG_Config
                .get("LargeNaquadahReactor", "Uranium Based Liquid Fuel Burning Time", NaquadahFuelTime[0]).getInt();
        NaquadahFuelVoltage[1] = GG_Config
                .get("LargeNaquadahReactor", "Thorium Based Liquid Fuel Basic Output Voltage", NaquadahFuelVoltage[1])
                .getInt();
        NaquadahFuelTime[1] = GG_Config
                .get("LargeNaquadahReactor", "Thorium Based Liquid Fuel Burning Time", NaquadahFuelTime[1]).getInt();
        NaquadahFuelVoltage[2] = GG_Config
                .get("LargeNaquadahReactor", "Plutonium Based Liquid Fuel Basic Output Voltage", NaquadahFuelVoltage[2])
                .getInt();
        NaquadahFuelTime[2] = GG_Config
                .get("LargeNaquadahReactor", "Plutonium Based Liquid Fuel Burning Time", NaquadahFuelTime[2]).getInt();
        NaquadahFuelVoltage[3] = GG_Config
                .get("LargeNaquadahReactor", "Naquadah Fuel MkI Basic Output Voltage", NaquadahFuelVoltage[3]).getInt();
        NaquadahFuelTime[3] = GG_Config
                .get("LargeNaquadahReactor", "Naquadah Fuel MkI Burning Time", NaquadahFuelTime[3]).getInt();
        NaquadahFuelVoltage[4] = GG_Config
                .get("LargeNaquadahReactor", "Naquadah Fuel MkII Basic Output Voltage", NaquadahFuelVoltage[4])
                .getInt();
        NaquadahFuelTime[4] = GG_Config
                .get("LargeNaquadahReactor", "Naquadah Fuel MkII Burning Time", NaquadahFuelTime[4]).getInt();
        NaquadahFuelVoltage[5] = GG_Config
                .get("LargeNaquadahReactor", "Naquadah Fuel MkIII Basic Output Voltage", NaquadahFuelVoltage[5])
                .getInt();
        NaquadahFuelTime[5] = GG_Config
                .get("LargeNaquadahReactor", "Naquadah Fuel MkIII Burning Time", NaquadahFuelTime[5]).getInt();
        NaquadahFuelVoltage[6] = GG_Config
                .get("LargeNaquadahReactor", "Naquadah Fuel MkIV Basic Output Voltage", NaquadahFuelVoltage[6])
                .getInt();
        NaquadahFuelTime[6] = GG_Config
                .get("LargeNaquadahReactor", "Naquadah Fuel MkIV Burning Time", NaquadahFuelTime[6]).getInt();
        NaquadahFuelVoltage[7] = GG_Config
                .get("LargeNaquadahReactor", "Naquadah Fuel MkV Basic Output Voltage", NaquadahFuelVoltage[7]).getInt();
        NaquadahFuelTime[7] = GG_Config
                .get("LargeNaquadahReactor", "Naquadah Fuel MkV Burning Time", NaquadahFuelTime[7]).getInt();
        CoolantEfficiency[0] = GG_Config.get("LargeNaquadahReactor", "Tachyon Fluid Efficiency", CoolantEfficiency[0])
                .getInt();
        CoolantEfficiency[1] = GG_Config.get("LargeNaquadahReactor", "Cryotheum Efficiency", CoolantEfficiency[1])
                .getInt();
        CoolantEfficiency[2] = GG_Config.get("LargeNaquadahReactor", "Super Coolant Efficiency", CoolantEfficiency[2])
                .getInt();
        CoolantEfficiency[3] = GG_Config.get("LargeNaquadahReactor", "IC2 Coolant Efficiency", CoolantEfficiency[3])
                .getInt();
        ExcitedLiquidCoe[0] = GG_Config
                .get("LargeNaquadahReactor", "Spatially Enlarged Fluid Magnification", ExcitedLiquidCoe[0]).getInt();
        ExcitedLiquidCoe[1] = GG_Config
                .get("LargeNaquadahReactor", "Atomic Separation Catalyst Magnification", ExcitedLiquidCoe[1]).getInt();
        ExcitedLiquidCoe[2] = GG_Config.get("LargeNaquadahReactor", "Naquadah Magnification", ExcitedLiquidCoe[2])
                .getInt();
        ExcitedLiquidCoe[3] = GG_Config.get("LargeNaquadahReactor", "Uranium-235 Magnification", ExcitedLiquidCoe[3])
                .getInt();
        ExcitedLiquidCoe[4] = GG_Config.get("LargeNaquadahReactor", "Caesium Magnification", ExcitedLiquidCoe[4])
                .getInt();
        LiquidAirConsumptionPerSecond = Math.max(
                GG_Config
                        .get("LargeNaquadahReactor", "Liquid Air Consumption Per Second", LiquidAirConsumptionPerSecond)
                        .getInt(),
                0);

        EnableNaquadahRework = GG_Config.get("NaquadahRework", "Enable Naquadah Rework", EnableNaquadahRework)
                .getBoolean();

        if (GG_Config.hasChanged()) GG_Config.save();
    }

    private static void loadCategory() {
        GG_Config.addCustomCategoryComment(
                "LargeNaquadahReactor",
                "Set fuel value, coolant or excited liquid property.");
        GG_Config.addCustomCategoryComment("NaquadahRework", "About the naquadah line");
    }
}
