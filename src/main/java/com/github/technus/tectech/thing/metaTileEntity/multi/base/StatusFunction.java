package com.github.technus.tectech.thing.metaTileEntity.multi.base;

import java.util.function.BiFunction;

public interface StatusFunction<T extends GT_MetaTileEntity_MultiblockBase_EM> extends BiFunction<T, Parameters.IParameter, LedStatus> {
    @Override
    LedStatus apply(T t, Parameters.IParameter iParameter);
}
