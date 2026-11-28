package gregtech.api.ec.component.multiblock;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.ComponentKey;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public interface IOnStructureStateChangeComponent extends IMultiblockComponent {

    ComponentKey.ListKey<IOnStructureStateChangeComponent> KEY = ComponentKey.list();

    default void onStructureFormed(@NotNull IGregTechTileEntity igte) {}

    default void onStructureBroken(@NotNull IGregTechTileEntity igte) {}
}
