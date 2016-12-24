package com.detrav.items.behaviours;

import com.detrav.items.DetravMetaGeneratedTool01;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicTank;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.behaviors.Behaviour_None;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

/**
 * Created by Detrav on 11.12.2016.
 */
public class BehaviourDetravToolSmartPlunger extends Behaviour_None {

    protected final int mCosts;
    protected final int mFluidSpace;

    public BehaviourDetravToolSmartPlunger(int aCosts,int fluidSpace) {
        mCosts = aCosts;
        mFluidSpace = fluidSpace;
    }

    public boolean onItemUseFirst(GT_MetaBase_Item aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int aSide, float hitX, float hitY, float hitZ) {
        if (aWorld.isRemote) {
            return false;
        }
        FluidStack fs =  DetravMetaGeneratedTool01.INSTANCE.getFluidStackFromDetravData(aStack);
        boolean containts = fs!=null && fs.amount >0;
        if(containts) {
            TileEntity aTileEntity = aWorld.getTileEntity(aX, aY, aZ);
            if ((aTileEntity instanceof IFluidHandler)) {
                for (ForgeDirection tDirection : ForgeDirection.VALID_DIRECTIONS) {
                    if (((IFluidHandler) aTileEntity).fill(tDirection, fs, false) > 0) {
                        if ((aPlayer.capabilities.isCreativeMode) || (((GT_MetaGenerated_Tool) aItem).doDamage(aStack, this.mCosts))) {
                            int amount = ((IFluidHandler) aTileEntity).fill(tDirection, fs, true);
                            //fs = ((IFluidHandler) aTileEntity).drain(tDirection, 1000, true);
                            fs.amount -= amount;
                            GT_Utility.sendSoundToPlayers(aWorld, (String) GregTech_API.sSoundList.get(Integer.valueOf(101)), 1.0F, -1.0F, aX, aY, aZ);
                            if (fs.amount > 0)
                                DetravMetaGeneratedTool01.INSTANCE.setFluidStackToDetravData(aStack, fs);
                            else
                                DetravMetaGeneratedTool01.INSTANCE.setFluidStackToDetravData(aStack, null);
                            return true;
                        }
                    }
                }
            }
        }
        else {
            TileEntity aTileEntity = aWorld.getTileEntity(aX, aY, aZ);
            if ((aTileEntity instanceof IFluidHandler)) {
                for (ForgeDirection tDirection : ForgeDirection.VALID_DIRECTIONS) {
                    if (((IFluidHandler) aTileEntity).drain(tDirection, mFluidSpace, false) != null) {
                        if ((aPlayer.capabilities.isCreativeMode) || (((GT_MetaGenerated_Tool) aItem).doDamage(aStack, this.mCosts))) {
                            fs = ((IFluidHandler) aTileEntity).drain(tDirection, mFluidSpace, true);
                            GT_Utility.sendSoundToPlayers(aWorld, (String) GregTech_API.sSoundList.get(Integer.valueOf(101)), 1.0F, -1.0F, aX, aY, aZ);
                            DetravMetaGeneratedTool01.INSTANCE.setFluidStackToDetravData(aStack,fs);
                            return true;
                        }
                    }
                }
            }
            if (aTileEntity instanceof IGregTechTileEntity) {
                IGregTechTileEntity tTileEntity = (IGregTechTileEntity) aTileEntity;
                IMetaTileEntity mTileEntity = tTileEntity.getMetaTileEntity();
                if (mTileEntity instanceof GT_MetaTileEntity_BasicTank) {
                    GT_MetaTileEntity_BasicTank machine = (GT_MetaTileEntity_BasicTank) mTileEntity;
                    if (machine.mFluid != null && machine.mFluid.amount > 0) {
                        fs = machine.mFluid.copy();
                        if (fs.amount > mFluidSpace) fs.amount = mFluidSpace;
                        machine.mFluid.amount = machine.mFluid.amount - Math.min(machine.mFluid.amount, mFluidSpace);
                        DetravMetaGeneratedTool01.INSTANCE.setFluidStackToDetravData(aStack,fs);
                    }
                    return true;
                }
            }
        }
        return false;
    }
}