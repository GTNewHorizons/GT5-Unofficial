package com.github.technus.tectech.auxiliary;

import eu.usrv.yamcore.config.ConfigManager;

import java.io.File;

public class TecTechConfig extends ConfigManager {
	public TecTechConfig(File pConfigBaseDirectory, String pModCollectionDirectory, String pModID) {
		super(pConfigBaseDirectory, pModCollectionDirectory, pModID);
	}

	public boolean ModAdminErrorLogs_Enabled;

	public boolean DEBUG_MODE;
	public boolean BOOM_ENABLE;

	@Override
	protected void PreInit() {
		ModAdminErrorLogs_Enabled = false;
		DEBUG_MODE=false;
		BOOM_ENABLE=true;
	}

	@Override
	protected void Init() {
		ModAdminErrorLogs_Enabled = _mainConfig.getBoolean("AdminErrorLog", "Modules", ModAdminErrorLogs_Enabled,
				"If set to true, every op/admin will receive all errors occoured during the startup phase as ingame message on join");
		DEBUG_MODE=_mainConfig.getBoolean("DebugPrint", "Debug", DEBUG_MODE,
				"Enables logging into System.out");
		BOOM_ENABLE=_mainConfig.getBoolean("BoomEnable","Features", BOOM_ENABLE,
				"Set to false to disable explosions on everything bad that you can do (this will not be available after release)");
	}

	@Override
	protected void PostInit() {

	}
}