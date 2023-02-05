package gregtech.api.interfaces.metatileentity;

public interface IMachineCallback<Machinetype extends IMetaTileEntity> {

    Machinetype getCallbackBase();

    void setCallbackBase(Machinetype callback);

    Class<?> getType();
}
