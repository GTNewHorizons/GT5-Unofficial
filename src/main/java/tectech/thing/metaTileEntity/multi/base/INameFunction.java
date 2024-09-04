package tectech.thing.metaTileEntity.multi.base;

public interface INameFunction<T extends TTMultiblockBase> {

    String apply(T t, Parameters.IParameter iParameter);
}
