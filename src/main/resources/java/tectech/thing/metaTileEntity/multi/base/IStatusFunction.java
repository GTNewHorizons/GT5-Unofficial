package tectech.thing.metaTileEntity.multi.base;

public interface IStatusFunction<T extends TTMultiblockBase> {

    LedStatus apply(T t, Parameters.IParameter iParameter);
}
