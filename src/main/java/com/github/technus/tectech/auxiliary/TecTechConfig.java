package com.github.technus.tectech.auxiliary;

import eu.usrv.yamcore.config.ConfigManager;

import java.io.File;

public class TecTechConfig extends ConfigManager {
    public TecTechConfig(File pConfigBaseDirectory, String pModCollectionDirectory, String pModID) {
        super(pConfigBaseDirectory, pModCollectionDirectory, pModID);
    }

    public boolean ModAdminErrorLogs_Enabled;

    //final static to allow compiler to remove the debug code when this is false
    public static final boolean DEBUG_MODE = false;
    public boolean BOOM_ENABLE;
    public boolean NERF_FUSION;
    public boolean ENABLE_TURRET_EXPLOSIONS;
    public float TURRET_DAMAGE_FACTOR;
    public float TURRET_EXPLOSION_FACTOR;

    @Override
    protected void PreInit() {
        ModAdminErrorLogs_Enabled = false;
        BOOM_ENABLE = true;
        NERF_FUSION = false;
        ENABLE_TURRET_EXPLOSIONS = true;
        TURRET_DAMAGE_FACTOR=10;
        TURRET_EXPLOSION_FACTOR=1;
    }

    @Override
    protected void Init() {
        ModAdminErrorLogs_Enabled = _mainConfig.getBoolean("AdminErrorLog", "Modules", ModAdminErrorLogs_Enabled,
                "If set to true, every op/admin will receive all errors occoured during the startup phase as ingame message on join");
        BOOM_ENABLE = _mainConfig.getBoolean("BoomEnable", "Features", BOOM_ENABLE,
                "Set to false to disable explosions on everything bad that you can do");
        NERF_FUSION = _mainConfig.getBoolean("NerfFusion", "Features", NERF_FUSION,
                "Set to true to enable removal of plasmas heavier than Fe");
        ENABLE_TURRET_EXPLOSIONS = _mainConfig.getBoolean("TurretBoomEnable", "Features", ENABLE_TURRET_EXPLOSIONS,
                "Set to false to disable explosions caused by EM turrets");
        TURRET_DAMAGE_FACTOR = _mainConfig.getFloat("TurretDamageFactor", "Features", TURRET_DAMAGE_FACTOR, 0, Short.MAX_VALUE,
                "Damage is multiplied by this number");
        TURRET_EXPLOSION_FACTOR = _mainConfig.getFloat("TurretExplosionFactor", "Features", TURRET_EXPLOSION_FACTOR, 0, Short.MAX_VALUE,
                "Explosion strength is multiplied by this number");
    }

    @Override
    protected void PostInit() {

    }
}