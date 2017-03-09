package com.github.technus.tectech.elementalMatter.machine;

import com.github.technus.tectech.casing.GT_Container_CasingsTT;
import com.github.technus.tectech.blocks.QuantumGlass;
import com.github.technus.tectech.elementalMatter.classes.cElementalDefinitionStack;
import com.github.technus.tectech.elementalMatter.classes.cElementalInstanceStack;
import com.github.technus.tectech.elementalMatter.classes.cElementalInstanceStackTree;
import com.github.technus.tectech.elementalMatter.commonValues;
import com.github.technus.tectech.elementalMatter.definitions.dHadronDefinition;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import java.util.HashMap;

import static com.github.technus.tectech.elementalMatter.commonValues.DEBUGMODE;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EMquantifier extends GT_MetaTileEntity_MultiblockBase_Elemental  {
    public static HashMap<Item,cElementalDefinitionStack> itemBinds=new HashMap<>(200);
    public static HashMap<Fluid,cElementalDefinitionStack> fluidBind=new HashMap<>(200);

    public GT_MetaTileEntity_EMquantifier(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EMquantifier(String aName) {
        super(aName);
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EMquantifier(this.mName);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        int xDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetX;
        int zDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetZ;
        if (iGregTechTileEntity.getBlockOffset(xDir, 0, zDir)!= QuantumGlass.INSTANCE) {
            return false;
        }
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int h = -1; h < 2; h++) {
                    if ((h != 0) || (((xDir + i != 0) || (zDir + j != 0)) && ((i != 0) || (j != 0)))) {
                        IGregTechTileEntity tTileEntity = iGregTechTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);
                        if (    (!addMaintenanceToMachineList(tTileEntity, 99)) &&
                                (!addClassicInputToMachineList(tTileEntity, 99)) &&
                                (!addElementalOutputToMachineList(tTileEntity, 99)) &&
                                (!addMufflerToMachineList(tTileEntity, 99)) &&
                                (!addEnergyIOToMachineList(tTileEntity, 99))) {
                            if (    iGregTechTileEntity.getBlockOffset(xDir + i, h, zDir + j) != GT_Container_CasingsTT.sBlockCasingsTT ||
                                    iGregTechTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 3) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                commonValues.tecMark,
                "Transform quantum form back to regular one...",
                EnumChatFormatting.AQUA.toString()+EnumChatFormatting.BOLD+"but why?"
        };
    }

    @Override
    public boolean EM_checkRecipe(ItemStack itemStack) {
        if(itemStack.getItem()==Items.iron_ingot){
            mEUt=-1;
            mMaxProgresstime=20;

            outputEM=new cElementalInstanceStackTree[1];
            outputEM[0]=new cElementalInstanceStackTree();
            outputEM[0].putReplace(new cElementalInstanceStack(dHadronDefinition.hadron_p,1000));

            return true;
        }
        return false;
    }

    @Override
    public void EM_outputFunction() {
        try{
            for(GT_MetaTileEntity_Hatch_OutputElemental hatch:eOutputHatches)
                hatch.getContainerHandler().putUnifyAll(outputEM[0]);
            //TODO FIX NPEs
        }catch (Exception e){
            if(DEBUGMODE)e.printStackTrace();
        }
    }

    public static void recipeInit(){
        itemBinds.put(Items.iron_ingot,new cElementalDefinitionStack(dHadronDefinition.hadron_p,1));
    }
}
