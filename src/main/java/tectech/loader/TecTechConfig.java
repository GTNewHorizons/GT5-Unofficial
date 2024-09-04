package tectech.loader;

import java.io.File;

import net.minecraft.launchwrapper.Launch;

import eu.usrv.yamcore.config.ConfigManager;

public class TecTechConfig extends ConfigManager {

    public TecTechConfig(File pConfigBaseDirectory, String pModCollectionDirectory, String pModID) {
        super(pConfigBaseDirectory, pModCollectionDirectory, pModID);
    }

    // final static to allow compiler to remove the debug code when this is false
    public static boolean DEBUG_MODE = false;
    public static boolean POWERLESS_MODE = false;
    /**
     * Not complete; enabled by default only in dev env
     */
    public boolean ENABLE_GOD_FORGE;

    public boolean BOOM_ENABLE;
    public boolean DISABLE_BLOCK_HARDNESS_NERF;
    public boolean EASY_SCAN;
    public boolean NERF_FUSION;
    public boolean ENABLE_TURRET_EXPLOSIONS;
    public float TURRET_DAMAGE_FACTOR;
    public float TURRET_EXPLOSION_FACTOR;

    public boolean MOD_ADMIN_ERROR_LOGS;

    public boolean TESLA_MULTI_GAS_OUTPUT;
    public float TESLA_MULTI_LOSS_FACTOR_OVERDRIVE;
    public int TESLA_MULTI_LOSS_PER_BLOCK_T0;
    public int TESLA_MULTI_LOSS_PER_BLOCK_T1;
    public int TESLA_MULTI_LOSS_PER_BLOCK_T2;
    public int TESLA_MULTI_PLASMA_PER_SECOND_T1_HELIUM;
    public int TESLA_MULTI_PLASMA_PER_SECOND_T1_NITROGEN;
    public int TESLA_MULTI_PLASMA_PER_SECOND_T2_RADON;
    public int TESLA_MULTI_RANGE_COEFFICIENT_PLASMA_T1;
    public int TESLA_MULTI_RANGE_COEFFICIENT_PLASMA_T2;
    public int TESLA_MULTI_RANGE_COVER;
    public int TESLA_MULTI_RANGE_TOWER;
    public int TESLA_MULTI_RANGE_TRANSCEIVER;
    public float TESLA_SINGLE_LOSS_FACTOR_OVERDRIVE;
    public int TESLA_SINGLE_LOSS_PER_BLOCK;
    public int TESLA_SINGLE_RANGE;
    public boolean TESLA_VISUAL_EFFECT;

    /**
     * This loading phases do not correspond to mod loading phases!
     */
    @Override
    protected void PreInit() {

        BOOM_ENABLE = true;
        DISABLE_BLOCK_HARDNESS_NERF = false;
        EASY_SCAN = false;
        NERF_FUSION = false;
        ENABLE_TURRET_EXPLOSIONS = true;
        TURRET_DAMAGE_FACTOR = 10;
        TURRET_EXPLOSION_FACTOR = 1;

        MOD_ADMIN_ERROR_LOGS = false;

        TESLA_MULTI_GAS_OUTPUT = false;
        TESLA_MULTI_LOSS_FACTOR_OVERDRIVE = 0.25F;
        TESLA_MULTI_LOSS_PER_BLOCK_T0 = 1;
        TESLA_MULTI_LOSS_PER_BLOCK_T1 = 1;
        TESLA_MULTI_LOSS_PER_BLOCK_T2 = 1;
        TESLA_MULTI_PLASMA_PER_SECOND_T1_HELIUM = 100;
        TESLA_MULTI_PLASMA_PER_SECOND_T1_NITROGEN = 50;
        TESLA_MULTI_PLASMA_PER_SECOND_T2_RADON = 50;
        TESLA_MULTI_RANGE_COEFFICIENT_PLASMA_T1 = 2;
        TESLA_MULTI_RANGE_COEFFICIENT_PLASMA_T2 = 4;
        TESLA_MULTI_RANGE_COVER = 16;
        TESLA_MULTI_RANGE_TOWER = 32;
        TESLA_MULTI_RANGE_TRANSCEIVER = 16;
        TESLA_SINGLE_LOSS_FACTOR_OVERDRIVE = 0.25F;
        TESLA_SINGLE_LOSS_PER_BLOCK = 1;
        TESLA_SINGLE_RANGE = 20;
        TESLA_VISUAL_EFFECT = true;

        ENABLE_GOD_FORGE = (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
    }

    /**
     * This loading phases do not correspond to mod loading phases!
     */
    @Override
    protected void Init() {
        DEBUG_MODE = _mainConfig
            .getBoolean("DebugMode", "debug", DEBUG_MODE, "Enables logging and other purely debug features");
        POWERLESS_MODE = _mainConfig
            .getBoolean("PowerlessMode", "debug", POWERLESS_MODE, "Enables 0EU/t multi block machinery");
        ENABLE_GOD_FORGE = _mainConfig.getBoolean(
            "EnableGodForge",
            "debug",
            ENABLE_GOD_FORGE,
            "Enables the in progress God Forge; enabled automatically in dev env");

        BOOM_ENABLE = _mainConfig.getBoolean(
            "BoomEnable",
            "features",
            BOOM_ENABLE,
            "Set to false to disable explosions on everything bad that you can do");
        DISABLE_BLOCK_HARDNESS_NERF = _mainConfig.getBoolean(
            "DisableBlockHardnessNerf",
            "features",
            DISABLE_BLOCK_HARDNESS_NERF,
            "Set to true to disable the block hardness nerf");
        EASY_SCAN = _mainConfig.getBoolean(
            "EasyScan",
            "features",
            EASY_SCAN,
            "Enables tricorder to scan EM i/o hatches directly, too CHEEKY");
        NERF_FUSION = _mainConfig.getBoolean(
            "NerfFusion",
            "features",
            NERF_FUSION,
            "Set to true to enable removal of plasmas heavier than Fe and other weird ones");
        ENABLE_TURRET_EXPLOSIONS = _mainConfig.getBoolean(
            "TurretBoomEnable",
            "features",
            ENABLE_TURRET_EXPLOSIONS,
            "Set to false to disable explosions caused by EM turrets");
        TURRET_DAMAGE_FACTOR = _mainConfig.getFloat(
            "TurretDamageFactor",
            "features",
            TURRET_DAMAGE_FACTOR,
            0,
            Short.MAX_VALUE,
            "Damage is multiplied by this number");
        TURRET_EXPLOSION_FACTOR = _mainConfig.getFloat(
            "TurretExplosionFactor",
            "features",
            TURRET_EXPLOSION_FACTOR,
            0,
            Short.MAX_VALUE,
            "Explosion strength is multiplied by this number");

        MOD_ADMIN_ERROR_LOGS = _mainConfig.getBoolean(
            "AdminErrorLog",
            "modules",
            MOD_ADMIN_ERROR_LOGS,
            "If set to true, every op/admin will receive all errors occurred during the startup phase as in game message on join");

        TESLA_MULTI_GAS_OUTPUT = _mainConfig.getBoolean(
            "TeslaMultiGasOutput",
            "tesla_tweaks",
            TESLA_MULTI_GAS_OUTPUT,
            "Set to true to enable outputting plasmas as gasses from the tesla tower with a 1:1 ratio");
        TESLA_MULTI_LOSS_FACTOR_OVERDRIVE = _mainConfig.getFloat(
            "TeslaMultiLossFactorOverdrive",
            "tesla_tweaks",
            TESLA_MULTI_LOSS_FACTOR_OVERDRIVE,
            0,
            1,
            "Additional Tesla Tower power loss per amp as a factor of the tier voltage");
        TESLA_MULTI_LOSS_PER_BLOCK_T0 = _mainConfig.getInt(
            "TeslaMultiLossPerBlockT0",
            "tesla_tweaks",
            TESLA_MULTI_LOSS_PER_BLOCK_T0,
            0,
            Integer.MAX_VALUE,
            "Tesla Tower power transmission loss per block per amp using no plasmas");
        TESLA_MULTI_LOSS_PER_BLOCK_T1 = _mainConfig.getInt(
            "TeslaMultiLossPerBlockT1",
            "tesla_tweaks",
            TESLA_MULTI_LOSS_PER_BLOCK_T1,
            0,
            Integer.MAX_VALUE,
            "Tesla Tower power transmission loss per block per amp using helium or nitrogen plasma");
        TESLA_MULTI_LOSS_PER_BLOCK_T2 = _mainConfig.getInt(
            "TeslaMultiLossPerBlockT2",
            "tesla_tweaks",
            TESLA_MULTI_LOSS_PER_BLOCK_T2,
            0,
            Integer.MAX_VALUE,
            "Tesla Tower power transmission loss per block per amp using radon plasma");
        TESLA_MULTI_PLASMA_PER_SECOND_T1_HELIUM = _mainConfig.getInt(
            "TeslaMultiPlasmaPerSecondT1Helium",
            "tesla_tweaks",
            TESLA_MULTI_PLASMA_PER_SECOND_T1_HELIUM,
            0,
            Integer.MAX_VALUE,
            "Tesla Tower helium plasma consumed each second the tesla tower is active");
        TESLA_MULTI_PLASMA_PER_SECOND_T1_NITROGEN = _mainConfig.getInt(
            "TeslaMultiPlasmaPerSecondT1Nitrogen",
            "tesla_tweaks",
            TESLA_MULTI_PLASMA_PER_SECOND_T1_NITROGEN,
            0,
            Integer.MAX_VALUE,
            "Tesla Tower nitrogen plasma consumed each second the tesla tower is active");
        TESLA_MULTI_PLASMA_PER_SECOND_T2_RADON = _mainConfig.getInt(
            "TeslaMultiPlasmaPerSecondT2Radon",
            "tesla_tweaks",
            TESLA_MULTI_PLASMA_PER_SECOND_T2_RADON,
            0,
            Integer.MAX_VALUE,
            "Tesla Tower radon plasma consumed each second the tesla tower is active");
        TESLA_MULTI_RANGE_COEFFICIENT_PLASMA_T1 = _mainConfig.getInt(
            "TeslaMultiRangeCoefficientPlasmaT1",
            "tesla_tweaks",
            TESLA_MULTI_RANGE_COEFFICIENT_PLASMA_T1,
            0,
            Integer.MAX_VALUE,
            "Tesla Tower T1 Plasmas Range Multiplier");
        TESLA_MULTI_RANGE_COEFFICIENT_PLASMA_T2 = _mainConfig.getInt(
            "TeslaMultiRangeCoefficientPlasmaT2",
            "tesla_tweaks",
            TESLA_MULTI_RANGE_COEFFICIENT_PLASMA_T2,
            0,
            Integer.MAX_VALUE,
            "Tesla Tower T2 Plasmas Range Multiplier");
        TESLA_MULTI_RANGE_COVER = _mainConfig.getInt(
            "TeslaMultiRangeCover",
            "tesla_tweaks",
            TESLA_MULTI_RANGE_COVER,
            0,
            Integer.MAX_VALUE,
            "Tesla Tower to Tesla Coil Rich Edition Cover max range");
        TESLA_MULTI_RANGE_TOWER = _mainConfig.getInt(
            "TeslaMultiRangeTower",
            "tesla_tweaks",
            TESLA_MULTI_RANGE_TOWER,
            0,
            Integer.MAX_VALUE,
            "Tesla Tower to Tower max range");
        TESLA_MULTI_RANGE_TRANSCEIVER = _mainConfig.getInt(
            "TeslaMultiRangeTransceiver",
            "tesla_tweaks",
            TESLA_MULTI_RANGE_TRANSCEIVER,
            0,
            Integer.MAX_VALUE,
            "Tesla Tower to Transceiver max range");
        TESLA_SINGLE_LOSS_FACTOR_OVERDRIVE = _mainConfig.getFloat(
            "TeslaSingleLossFactorOverdrive",
            "tesla_tweaks",
            TESLA_SINGLE_LOSS_FACTOR_OVERDRIVE,
            0,
            1,
            "Additional Tesla Transceiver power loss per amp as a factor of the tier voltage");
        TESLA_SINGLE_LOSS_PER_BLOCK = _mainConfig.getInt(
            "TeslaSingleLossPerBlock",
            "tesla_tweaks",
            TESLA_SINGLE_LOSS_PER_BLOCK,
            0,
            Integer.MAX_VALUE,
            "Tesla Transceiver power transmission loss per block per amp");
        TESLA_SINGLE_RANGE = _mainConfig.getInt(
            "TeslaSingleRange",
            "tesla_tweaks",
            TESLA_SINGLE_RANGE,
            0,
            Integer.MAX_VALUE,
            "Tesla Transceiver to max range");
        TESLA_VISUAL_EFFECT = _mainConfig.getBoolean(
            "EnableTeslaVisualEffect",
            "tesla_tweaks",
            TESLA_VISUAL_EFFECT,
            "Set true to enable the cool visual effect when tesla tower running.");
    }

    /**
     * This loading phases do not correspond to mod loading phases!
     */
    @Override
    protected void PostInit() {}
}
