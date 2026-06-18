package gtnhintergalactic.config;

import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.gtnhlib.config.Config;

import gregtech.api.enums.Mods;
import gregtech.api.util.GTModHandler;
import gtnhintergalactic.GTNHIntergalactic;

@Config(modid = Mods.ModIDs.G_T_N_H_INTERGALACTIC, filename = "gtnhintergalactic")
public class IGConfig {

    public static SpaceElevator spaceElevator = new SpaceElevator();
    public static DysonSwarm dysonSwarm = new DysonSwarm();

    @Config.Comment("Space Elevator section")
    public static class SpaceElevator {

        @Config.Comment("If true, the Space Elevator will use it's fancy renderer, otherwise a simple block renderer")
        @Config.DefaultBoolean(true)
        public boolean isCableRenderingEnabled;
    }

    @Config.Comment("Dyson Swarm section")
    public static class DysonSwarm {

        @Config.Comment({ "Each hour, n of m modules are destroyed according to this formula:",
            " n = m * (2 * base_chance) / (exp(-a * (m - 1))+exp(b * cps)), where cps is computation per second.",
            "This sets the parameter a." })
        @Config.DefaultDouble(0.00005)
        @Config.RangeDouble(min = 0, max = 1)
        @Config.RequiresMcRestart
        public double destroyModuleA;
        @Config.Comment({ "Each hour, n of m modules are destroyed according to this formula:",
            " n = m * (2 * base_chance) / (exp(-a * (m - 1))+exp(b * cps)), where cps is computation per second.",
            "This sets the parameter b." })
        @Config.DefaultDouble(0.00003)
        @Config.RangeDouble(min = 0, max = 1)
        @Config.RequiresMcRestart
        public double destroyModuleB;
        @Config.Comment({ "Each hour, n of m modules are destroyed according to this formula:",
            " n = m * (2 * base_chance) / (exp(-a * (m - 1))+exp(b * cps)), where cps is computation per second.",
            "This sets the parameter base_chance." })
        @Config.DefaultDouble(0.066)
        @Config.RangeDouble(min = 0, max = 1)
        @Config.RequiresMcRestart
        public double destroyModuleChance;
        @Config.Comment("The maximum number of modules the dyson swarm can take")
        @Config.DefaultInt(10000)
        @Config.RangeInt(min = 1)
        @Config.RequiresMcRestart
        public int maxModules;
        @Config.Comment("The maximum computation per second that will help prevent modules from collision")
        @Config.DefaultDouble(100000.0)
        @Config.RangeDouble(min = 0)
        @Config.RequiresMcRestart
        public double destroyModuleMaxCPS;
        @Config.Comment("How much EU the Dyson Swarm Command Center produces per module per tick")
        @Config.DefaultInt(10000000)
        @Config.RangeInt(min = 1)
        @Config.RequiresMcRestart
        public int euPerModule;
        @Config.Comment({ "Define a power factor for each dimension ID.",
            "The total energy output of Dyson Swarm multiblocks is multiplied by these values.",
            "Format is \"DIMID:FACTOR\"", "DIMIDs for Space Stations are \"SS_unlocalizedNameOfBodyToOrbit\"",
            "DIMIDs for Utility Worlds dimensions are \"UW_Garden\", \"UW_Mining\" and \"UW_Void\"" })
        @Config.DefaultStringList({ "0:1.0", // Overworld
            "25:0.15", // Makemake
            "28:1.0", // Moon
            "29:0.81", // Mars
            "30:0.61", // Asteroids
            "31:2.28", // A Centauri Bb
            "32:2.31", // Barnarda C
            "33:0.16", // Kuiper Belt
            "35:0.44", // Europa
            "36:0.44", // Io
            "37:1.61", // Mercury
            "38:0.81", // Phobos
            "39:1.76", // Venus
            "40:0.81", // Deimos
            "41:0.32", // Enceladus
            "42:0.6", // Ceres
            "43:0.44", // Ganymede
            "44:0.32", // Titan
            "45:0.32", // Callisto
            "46:0.23", // Oberon
            "47:0.23", // Proteus
            "48:0.18", // Triton
            "49:0.16", // Pluto
            "63:1.12", // Ross128ba
            "64:1.12", // Ross128b
            "81:1.41", // Barnarda E
            "82:1.26", // Barnarda F
            "83:0.15", // Haumea
            "84:1.98", // Vega B
            "85:1.34", // T Ceti E
            "86:0.23", // Miranda
            "90:2.28", // Neper
            "91:1.98", // Maahes
            "92:1.81", // Anubis
            "93:3.37", // Horus
            "94:1.98", // Seth
            "95:2.11", // Mehen
            "SS_Overworld:1.1", // Earth Space Station
            "SS_planet.mars:0.89", // Mars Space Station
            "SS_planet.venus:1.94", // Venus Space Station
            "SS_planet.jupiter:0.48", // Jupiter Space Station
            "SS_planet.saturn:0.36", // Saturn Space Station
            "SS_planet.uranus:0.25", // Uranus Space Station
            "SS_planet.neptune:0.2", // Neptune Space Station
            "PS:0.01", // Personal Space dimensions
        })
        @Config.RequiresMcRestart
        public String[] powerFactors;
        @Config.Comment("If a power factor for a dimension is not set, this value will be used")
        @Config.RangeDouble(min = 0.0)
        @Config.RequiresMcRestart
        public double powerFactorDefault;
        @Config.Comment("How much coolant is consumed per hour")
        @Config.DefaultInt(3600000)
        @Config.RangeInt(min = 0)
        @Config.RequiresMcRestart
        public int coolantConsumption;
        @Config.Comment({ "Name of the coolant to use", "Will fallback to IC2 Coolant if this name is invalid" })
        @Config.DefaultString("cryotheum")
        @Config.RequiresMcRestart
        public String coolantFluid;
        @Config.Ignore
        private Fluid cachedCoolantFluid;

        public FluidStack getCoolantStack() {
            if (cachedCoolantFluid == null) {
                cachedCoolantFluid = FluidRegistry.getFluid(coolantFluid);
                if (cachedCoolantFluid == null) {
                    cachedCoolantFluid = GTModHandler.getIC2Coolant(0)
                        .getFluid(); // fallback
                }
            }
            return new FluidStack(cachedCoolantFluid, coolantConsumption);
        }

        @Config.Ignore
        private Map<String, Double> cachedPowerFactors;

        public double getPowerFactor(String dimID) {
            if (cachedPowerFactors == null) {
                cachedPowerFactors = new HashMap<>();
                for (String s : powerFactors) {
                    String[] parts = s.split(":");
                    try {
                        cachedPowerFactors.put(parts[0], Double.parseDouble(parts[1]));
                    } catch (Exception e) {
                        GTNHIntergalactic.LOG.error("Error while trying to parse \"" + s + "\"!");
                        e.printStackTrace();
                    }
                }
            }
            Double value = cachedPowerFactors.get(dimID);
            return value != null ? value : powerFactorDefault;
        }
    }
}
