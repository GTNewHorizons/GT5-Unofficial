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
    public float TURRET_DAMAGE_FACTOR;
    public float TURRET_EXPLOSION_FACTOR;

    /**
     * This loading phases do not correspond to mod loading phases!
     */
    @Override
    protected void PreInit() {
        modAdminErrorLogs = false;
        EASY_SCAN = false;
        BOOM_ENABLE = true;
        NERF_FUSION = false;
        ENABLE_TURRET_EXPLOSIONS = true;
        TURRET_DAMAGE_FACTOR = 10;
        TURRET_EXPLOSION_FACTOR = 1;
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
    }

    /**
     * This loading phases do not correspond to mod loading phases!
     */
    @Override
    protected void PostInit() {

    }
}
