package gregtech.common.gui.modularui.multiblock.godforge.sync;

import gregtech.common.gui.modularui.multiblock.godforge.MTEBaseModuleGui;
import gregtech.common.gui.modularui.multiblock.godforge.MTEExoticModuleGui;
import gregtech.common.gui.modularui.multiblock.godforge.MTEMoltenModuleGui;
import gregtech.common.gui.modularui.multiblock.godforge.MTEPlasmaModuleGui;
import gregtech.common.gui.modularui.multiblock.godforge.MTESmeltingModuleGui;
import tectech.thing.metaTileEntity.multi.godforge.MTEBaseModule;
import tectech.thing.metaTileEntity.multi.godforge.MTEExoticModule;
import tectech.thing.metaTileEntity.multi.godforge.MTEMoltenModule;
import tectech.thing.metaTileEntity.multi.godforge.MTEPlasmaModule;
import tectech.thing.metaTileEntity.multi.godforge.MTESmeltingModule;

public final class Modules<T extends MTEBaseModule> {

    // spotless:off

    public static final Modules<MTEBaseModule> CORE = new Modules<>("core", null, null);
    public static final Modules<MTEBaseModule> ANY = new Modules<>("any", null, MTEBaseModule.class);

    public static final Modules<MTESmeltingModule> SMELTING = new Modules<>("smelting", Panels.MAIN_SMELTING, MTESmeltingModule.class);
    public static final Modules<MTEMoltenModule> MOLTEN = new Modules<>("molten", Panels.MAIN_MOLTEN, MTEMoltenModule.class);
    public static final Modules<MTEPlasmaModule> PLASMA = new Modules<>("plasma", Panels.MAIN_PLASMA, MTEPlasmaModule.class);
    public static final Modules<MTEExoticModule> EXOTIC = new Modules<>("exotic", Panels.MAIN_EXOTIC, MTEExoticModule.class);

    // spotless:on

    private final String moduleId;
    private final Panels mainPanel;
    private final Class<T> moduleClass;

    private Modules(String moduleId, Panels mainPanel, Class<T> moduleClass) {
        this.moduleId = moduleId;
        this.mainPanel = mainPanel;
        this.moduleClass = moduleClass;
    }

    public Panels getMainPanel() {
        return this.mainPanel;
    }

    public T cast(MTEBaseModule module) {
        return moduleClass.cast(module);
    }

    public String getModuleId() {
        return "fog.module." + this.moduleId;
    }

    public static Modules<?> getModule(MTEBaseModule multiblock) {
        if (multiblock instanceof MTESmeltingModule) return SMELTING;
        if (multiblock instanceof MTEMoltenModule) return MOLTEN;
        if (multiblock instanceof MTEPlasmaModule) return PLASMA;
        if (multiblock instanceof MTEExoticModule) return EXOTIC;
        return null;
    }

    public static MTEBaseModuleGui<?> createSubpanelGui(MTEBaseModule multiblock, SyncHypervisor hypervisor) {
        if (multiblock instanceof MTESmeltingModule smelting) return new MTESmeltingModuleGui(smelting, hypervisor);
        if (multiblock instanceof MTEMoltenModule molten) return new MTEMoltenModuleGui(molten, hypervisor);
        if (multiblock instanceof MTEPlasmaModule plasma) return new MTEPlasmaModuleGui(plasma, hypervisor);
        if (multiblock instanceof MTEExoticModule exotic) return new MTEExoticModuleGui(exotic, hypervisor);
        return null;
    }
}
