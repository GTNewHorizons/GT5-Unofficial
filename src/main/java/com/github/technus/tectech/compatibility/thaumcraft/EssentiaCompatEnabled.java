package com.github.technus.tectech.compatibility.thaumcraft;

import com.github.technus.tectech.compatibility.thaumcraft.definitions.iElementalAspect;
import com.github.technus.tectech.elementalMatter.core.stacks.cElementalInstanceStack;
import com.github.technus.tectech.elementalMatter.core.templates.cElementalDefinition;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import net.minecraft.tileentity.TileEntity;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.common.tiles.TileEssentiaReservoir;
import thaumcraft.common.tiles.TileJarFillable;

import static com.github.technus.tectech.compatibility.thaumcraft.definitions.AspectDefinitionCompat.aspectToDef;

/**
 * Created by Tec on 21.05.2017.
 */
public class EssentiaCompatEnabled extends EssentiaCompat {
    @Override
    public void run() {

    }

    @Override
    public boolean check(GT_MetaTileEntity_MultiblockBase_EM meta) {
        TileEntity tile =meta.getBaseMetaTileEntity().getTileEntityAtSide(meta.getBaseMetaTileEntity().getBackFacing());
        return tile!=null && tile instanceof TileEssentiaReservoir || tile instanceof TileJarFillable;
    }

    @Override
    public TileEntity getContainer(GT_MetaTileEntity_MultiblockBase_EM meta) {
        TileEntity tile =meta.getBaseMetaTileEntity().getTileEntityAtSide(meta.getBaseMetaTileEntity().getBackFacing());
        return tile!=null && !tile.isInvalid() && tile instanceof TileEssentiaReservoir || tile instanceof TileJarFillable ?tile:null;
    }

    @Override
    public boolean putElementalInstanceStack(TileEntity container,cElementalInstanceStack stack){
        if(container==null || container.isInvalid())return false;
        if(container instanceof IAspectContainer && stack.definition instanceof iElementalAspect){
            Aspect aspect=(Aspect) ((iElementalAspect) stack.definition).materializeIntoAspect();
            if(aspect!=null){
                ((IAspectContainer) container).addToContainer(aspect,1);
                return true;
            }
        }
        return false;
    }

    @Override
    public cElementalInstanceStack getFromContainer(TileEntity container){
        if(container==null || container.isInvalid())return null;
        if(container instanceof IAspectContainer){
            AspectList aspects=((IAspectContainer) container).getAspects();
            if(aspects!=null){
                Aspect[] aspectsArr= aspects.getAspects();
                if(aspectsArr!=null && aspectsArr[0]!=null){
                     if (((IAspectContainer) container).takeFromContainer(aspectsArr[0],1)){
                         cElementalDefinition def=aspectToDef.get(aspectsArr[0].getTag());
                         if(def!=null){
                             return new cElementalInstanceStack(def,1);
                         }
                     }
                }
            }
        }
        return null;
    }
}
