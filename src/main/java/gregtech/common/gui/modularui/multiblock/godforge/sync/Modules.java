package gregtech.common.gui.modularui.multiblock.godforge.sync;

import tectech.thing.metaTileEntity.multi.godforge.MTEBaseModule;
import tectech.thing.metaTileEntity.multi.godforge.MTEExoticModule;
import tectech.thing.metaTileEntity.multi.godforge.MTEMoltenModule;
import tectech.thing.metaTileEntity.multi.godforge.MTEPlasmaModule;
import tectech.thing.metaTileEntity.multi.godforge.MTESmeltingModule;

public final class Modules<T extends MTEBaseModule> {

    // spotless:off

    public static final Modules<MTEBaseModule> CORE = new Modules<>("", null, null);
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
}
