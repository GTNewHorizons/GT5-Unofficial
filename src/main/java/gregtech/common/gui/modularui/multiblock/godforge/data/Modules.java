package gregtech.common.gui.modularui.multiblock.godforge.data;

import tectech.thing.metaTileEntity.multi.godforge.MTEBaseModule;
import tectech.thing.metaTileEntity.multi.godforge.MTEExoticModule;
import tectech.thing.metaTileEntity.multi.godforge.MTEMoltenModule;
import tectech.thing.metaTileEntity.multi.godforge.MTEPlasmaModule;
import tectech.thing.metaTileEntity.multi.godforge.MTESmeltingModule;

public final class Modules<T extends MTEBaseModule> {

    // spotless:off

    // Effectively a substitute for "any module" in a few special cases
    public static final Modules<MTEBaseModule> BASE = new Modules<>(null, MTEBaseModule.class);

    public static final Modules<MTESmeltingModule> SMELTING = new Modules<>(Panels.MAIN_SMELTING, MTESmeltingModule.class);
    public static final Modules<MTEMoltenModule> MOLTEN = new Modules<>(Panels.MAIN_MOLTEN, MTEMoltenModule.class);
    public static final Modules<MTEPlasmaModule> PLASMA = new Modules<>(Panels.MAIN_PLASMA, MTEPlasmaModule.class);
    public static final Modules<MTEExoticModule> EXOTIC = new Modules<>(Panels.MAIN_EXOTIC, MTEExoticModule.class);

    // spotless:on

    private final Panels mainPanel;
    private final Class<T> moduleClass;

    private Modules(Panels mainPanel, Class<T> moduleClass) {
        this.mainPanel = mainPanel;
        this.moduleClass = moduleClass;
    }

    public Panels getMainPanel() {
        return this.mainPanel;
    }

    public T cast(MTEBaseModule module) {
        return moduleClass.cast(module);
    }
}
