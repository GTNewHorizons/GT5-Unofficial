package com.github.technus.tectech.thing.metaTileEntity.multi.base;


import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

import java.util.function.BiFunction;

public interface HatchAdder extends BiFunction<IGregTechTileEntity, Short,Boolean> {}
