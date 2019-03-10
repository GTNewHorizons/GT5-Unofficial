package com.github.technus.tectech.thing.metaTileEntity.multi.base;

import java.util.function.BiFunction;

public interface NameFunction<T extends GT_MetaTileEntity_MultiblockBase_EM> extends BiFunction<T, Parameters.IParameter, String> {
    @Override
    String apply(T t, Parameters.IParameter iParameter);
}
