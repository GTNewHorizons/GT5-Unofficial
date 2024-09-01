package tectech.thing.metaTileEntity.multi.base;

public interface IStatusFunction<T extends GT_MetaTileEntity_MultiblockBase_EM> {

    LedStatus apply(T t, Parameters.IParameter iParameter);
}
