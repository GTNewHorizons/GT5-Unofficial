package gregtech.api.ec.component.multiblock;

import java.util.List;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.component.IUniqueComponent;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.structure.error.StructureError;

public interface IStructureLogicComponent extends IMultiblockComponent, IUniqueComponent {

    ComponentKey.UniqueKey<IStructureLogicComponent> KEY = ComponentKey.unique();

    boolean isFormed();

    void markStructureDirty();

    void scheduleCheckNextTick();

    boolean checkStructure(IGregTechTileEntity te);

    List<StructureError> getStructureErrors();

    int getStructureUpdateTime();

    void setStructureUpdateTime(int time);

    int getStartUpCheckTimer();
}
