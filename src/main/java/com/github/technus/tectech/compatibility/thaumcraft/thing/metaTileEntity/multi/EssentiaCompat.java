package com.github.technus.tectech.compatibility.thaumcraft.thing.metaTileEntity.multi;

import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.IEMDefinition;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Tec on 21.05.2017.
 */
public class EssentiaCompat {
    public static EssentiaCompat essentiaContainerCompat;

    public boolean check(GT_MetaTileEntity_MultiblockBase_EM meta){
        return false;
    }

    public TileEntity getContainer(GT_MetaTileEntity_MultiblockBase_EM meta){
        return null;
    }

    public String getEssentiaName(IEMDefinition stack){
        return null;
    }

    public boolean putInContainer(TileEntity container, String name){
        return false;
    }

    public IEMDefinition getFromContainer(TileEntity container){
        return null;
    }
}
