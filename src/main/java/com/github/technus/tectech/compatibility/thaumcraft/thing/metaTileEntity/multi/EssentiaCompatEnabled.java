package com.github.technus.tectech.compatibility.thaumcraft.thing.metaTileEntity.multi;

import com.github.technus.tectech.mechanics.elementalMatter.core.templates.iElementalDefinition;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import net.minecraft.tileentity.TileEntity;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.common.tiles.TileEssentiaReservoir;
import thaumcraft.common.tiles.TileJarFillable;

import static com.github.technus.tectech.compatibility.thaumcraft.elementalMatter.transformations.AspectDefinitionCompat.aspectDefinitionCompat;

/**
 * Created by Tec on 21.05.2017.
 */
public class EssentiaCompatEnabled extends EssentiaCompat {
    @Override
    public boolean check(GT_MetaTileEntity_MultiblockBase_EM meta) {
        TileEntity tile =meta.getBaseMetaTileEntity().getTileEntityAtSide(meta.getBaseMetaTileEntity().getBackFacing());
        return tile instanceof TileEssentiaReservoir || tile instanceof TileJarFillable;
    }

    @Override
    public TileEntity getContainer(GT_MetaTileEntity_MultiblockBase_EM meta) {
        TileEntity tile =meta.getBaseMetaTileEntity().getTileEntityAtSide(meta.getBaseMetaTileEntity().getBackFacing());
        return tile!=null && !tile.isInvalid() && tile instanceof TileEssentiaReservoir || tile instanceof TileJarFillable ?tile:null;
    }

    @Override
    public String getEssentiaName(iElementalDefinition stack) {
        return aspectDefinitionCompat.defToAspect.get(stack);
    }

    @Override
    public boolean putInContainer(TileEntity container, String aspectName) {
        if (container == null || container.isInvalid() || aspectName == null || aspectName.isEmpty()) {
            return false;
        }
        if (container instanceof IAspectContainer) {
            Aspect aspect = Aspect.getAspect(aspectName);
            if(aspect==null){
                return false;
            }
            int remaining=((IAspectContainer) container).addToContainer(aspect, 1);
            return remaining==0;
        }
        return false;
    }

    @Override
    public iElementalDefinition getFromContainer(TileEntity container){
        if(container==null || container.isInvalid()) {
            return null;
        }
        if(container instanceof IAspectContainer){
            AspectList aspects=((IAspectContainer) container).getAspects();
            if(aspects!=null){
                Aspect[] aspectsArr= aspects.getAspects();
                if(aspectsArr!=null && aspectsArr[0]!=null){
                     if (((IAspectContainer) container).takeFromContainer(aspectsArr[0],1)){
                         return aspectDefinitionCompat.aspectToDef.get(aspectsArr[0].getTag());
                     }
                }
            }
        }
        return null;
    }
}
