package com.github.technus.tectech.thing.metaTileEntity.multi.base;

public interface NameFunction<T extends GT_MetaTileEntity_MultiblockBase_EM>{
    String apply(T t, Parameters.IParameter iParameter);
}
