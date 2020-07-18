package com.github.technus.tectech.loader;

import eu.usrv.yamcore.config.ConfigManager;

import java.io.File;

public class TecTechConfig extends ConfigManager {
    public TecTechConfig(File pConfigBaseDirectory, String pModCollectionDirectory, String pModID) {
        super(pConfigBaseDirectory, pModCollectionDirectory, pModID);
    }

    public boolean modAdminErrorLogs;

    //final static to allow compiler to remove the debug code when this is false
    public static boolean DEBUG_MODE = false;
    public boolean BOOM_ENABLE;
    public boolean EASY_SCAN;
    public boolean NERF_FUSION;
    public boolean ENABLE_TURRET_EXPLOSIONS;
    public boolean DISABLE_MATERIAL_LOADING_FFS;
    public boolean DISABLE_BLOCK_HARDNESS_NERF;
    public float TURRET_DAMAGE_FACTOR;
    public float TURRET_EXPLOSION_FACTOR;
    public int TESLA_MULTI_HELIUM_PLASMA_PER_SECOND;
    public int TESLA_MULTI_NITROGEN_PLASMA_PER_SECOND;
    public int TESLA_MULTI_RADON_PLASMA_PER_SECOND;
    public int TESLA_MULTI_LOSS_PER_BLOCK_T0;
    public int TESLA_MULTI_LOSS_PER_BLOCK_T1;
    public int TESLA_MULTI_LOSS_PER_BLOCK_T2;
    public float TESLA_MULTI_OVERDRIVE_LOSS_FACTOR;
    public int TESLA_MULTI_SCAN_RANGE;//TODO delete
    public boolean TESLA_MULTI_GAS_OUTPUT;
    public int TESLA_SINGLE_LOSS_PER_BLOCK;
    public float TESLA_SINGLE_OVERDRIVE_LOSS_FACTOR;

    /**
     * This loading phases do not correspond to mod loading phases!
     */
    @Override
    protected void PreInit() {
        modAdminErrorLogs = false;
        EASY_SCAN = false;
        BOOM_ENABLE = true;
        NERF_FUSION = false;
        DISABLE_BLOCK_HARDNESS_NERF = false;
        ENABLE_TURRET_EXPLOSIONS = true;
        DISABLE_MATERIAL_LOADING_FFS = false;
        TURRET_DAMAGE_FACTOR = 10;
        TURRET_EXPLOSION_FACTOR = 1;
        TESLA_MULTI_HELIUM_PLASMA_PER_SECOND = 100;
        TESLA_MULTI_NITROGEN_PLASMA_PER_SECOND = 50;
        TESLA_MULTI_RADON_PLASMA_PER_SECOND = 50;
        TESLA_MULTI_LOSS_PER_BLOCK_T0 = 1;
        TESLA_MULTI_LOSS_PER_BLOCK_T1 = 1;
        TESLA_MULTI_LOSS_PER_BLOCK_T2 = 1;
        TESLA_MULTI_OVERDRIVE_LOSS_FACTOR = 0.25F;
        TESLA_MULTI_SCAN_RANGE = 40;
        TESLA_MULTI_GAS_OUTPUT = false;
        TESLA_SINGLE_LOSS_PER_BLOCK = 1;
        TESLA_SINGLE_OVERDRIVE_LOSS_FACTOR = 0.25F;
    }

    /**
     * This loading phases do not correspond to mod loading phases!
     */
    @Override
    protected void Init() {
        modAdminErrorLogs = _mainConfig.getBoolean("AdminErrorLog", "Modules", modAdminErrorLogs,
                "If set to true, every op/admin will receive all errors occurred during the startup phase as in game message on join");
        DEBUG_MODE = _mainConfig.getBoolean("DebugMode", "Debug", DEBUG_MODE,
                "Enables logging and other purely debug features");
        EASY_SCAN = _mainConfig.getBoolean("EasyScan", "Features", EASY_SCAN,
                "Enables tricorder to scan EM i/o hatches directly, too CHEEKY");
        BOOM_ENABLE = _mainConfig.getBoolean("BoomEnable", "Features", BOOM_ENABLE,
                "Set to false to disable explosions on everything bad that you can do");
        NERF_FUSION = _mainConfig.getBoolean("NerfFusion", "Features", NERF_FUSION,
                "Set to true to enable removal of plasmas heavier than Fe and other weird ones");
        ENABLE_TURRET_EXPLOSIONS = _mainConfig.getBoolean("TurretBoomEnable", "Features", ENABLE_TURRET_EXPLOSIONS,
                "Set to false to disable explosions caused by EM turrets");
        TURRET_DAMAGE_FACTOR = _mainConfig.getFloat("TurretDamageFactor", "Features", TURRET_DAMAGE_FACTOR, 0, Short.MAX_VALUE,
                "Damage is multiplied by this number");
        TURRET_EXPLOSION_FACTOR = _mainConfig.getFloat("TurretExplosionFactor", "Features", TURRET_EXPLOSION_FACTOR, 0, Short.MAX_VALUE,
                "Explosion strength is multiplied by this number");
        DISABLE_BLOCK_HARDNESS_NERF = _mainConfig.getBoolean("DisableBlockHardnessNerf", "Features", DISABLE_BLOCK_HARDNESS_NERF,
                "Set to true to disable the block hardness nerf");
        DISABLE_MATERIAL_LOADING_FFS = _mainConfig.getBoolean("DisableMaterialLoading", "Debug", DISABLE_MATERIAL_LOADING_FFS,
                "Set to true to disable gregtech material processing");

        TESLA_MULTI_HELIUM_PLASMA_PER_SECOND = _mainConfig.getInt("TeslaMultiHeliumPlasmaPerSecond", "Balance Tweaks", TESLA_MULTI_HELIUM_PLASMA_PER_SECOND, 0, Integer.MAX_VALUE, "Tesla Tower helium plasma consumed each second the tesla tower is active");
        TESLA_MULTI_NITROGEN_PLASMA_PER_SECOND = _mainConfig.getInt("TeslaMultiNitrogenPlasmaPerSecond", "Balance Tweaks", TESLA_MULTI_NITROGEN_PLASMA_PER_SECOND, 0, Integer.MAX_VALUE, "Tesla Tower nitrogen plasma consumed each second the tesla tower is active");
        TESLA_MULTI_RADON_PLASMA_PER_SECOND = _mainConfig.getInt("TeslaMultiRadonPlasmaPerSecond", "Balance Tweaks", TESLA_MULTI_RADON_PLASMA_PER_SECOND, 0, Integer.MAX_VALUE, "Tesla Tower radon plasma consumed each second the tesla tower is active");
        TESLA_MULTI_LOSS_PER_BLOCK_T0 = _mainConfig.getInt("TeslaMultiLossPerBlockT0", "Balance Tweaks", TESLA_MULTI_LOSS_PER_BLOCK_T0, 0, Integer.MAX_VALUE, "Tesla Tower power transmission loss per block per amp using no plasmas");
        TESLA_MULTI_LOSS_PER_BLOCK_T1 = _mainConfig.getInt("TeslaMultiLossPerBlockT1", "Balance Tweaks", TESLA_MULTI_LOSS_PER_BLOCK_T1, 0, Integer.MAX_VALUE, "Tesla Tower power transmission loss per block per amp using helium or nitrogen plasma");
        TESLA_MULTI_LOSS_PER_BLOCK_T2 = _mainConfig.getInt("TeslaMultiLossPerBlockT1", "Balance Tweaks", TESLA_MULTI_LOSS_PER_BLOCK_T2, 0, Integer.MAX_VALUE, "Tesla Tower power transmission loss per block per amp using radon plasma");
        TESLA_MULTI_OVERDRIVE_LOSS_FACTOR = _mainConfig.getFloat("TeslaMultiOverdriveLossFactor", "Balance Tweaks", TESLA_MULTI_OVERDRIVE_LOSS_FACTOR, 0, 1, "Additional Tesla Tower power loss per amp as a factor of the tier voltage");
        TESLA_MULTI_SCAN_RANGE = _mainConfig.getInt("TeslaMultiScanRange", "Balance Tweaks", TESLA_MULTI_SCAN_RANGE, 4, 256, "The horizontal radius scanned by the Tesla Tower");
        TESLA_MULTI_GAS_OUTPUT = _mainConfig.getBoolean("TeslaMultiMoltenOutput", "Balance Tweaks", TESLA_MULTI_GAS_OUTPUT, "Set to true to enable outputting plasmas as gasses from the tesla tower with a 1:1 ratio");
        TESLA_SINGLE_LOSS_PER_BLOCK = _mainConfig.getInt("TeslaSingleLossPerBlock", "Balance Tweaks", TESLA_SINGLE_LOSS_PER_BLOCK, 0, Integer.MAX_VALUE, "Tesla Transceiver power transmission loss per block per amp");
        TESLA_SINGLE_OVERDRIVE_LOSS_FACTOR = _mainConfig.getFloat("TeslaSingleOverdriveLossFactor", "Balance Tweaks", TESLA_SINGLE_OVERDRIVE_LOSS_FACTOR, 0, 1, "Additional Tesla Transceiver power loss per amp as a factor of the tier voltage");
    }

    /**
     * This loading phases do not correspond to mod loading phases!
     */
    @Override
    protected void PostInit() {

    }
}
