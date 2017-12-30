package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.elementalMatter.core.cElementalInstanceStackMap;
import com.github.technus.tectech.elementalMatter.core.stacks.iHasElementalDefinition;
import com.github.technus.tectech.elementalMatter.core.stacks.cElementalInstanceStack;
import com.github.technus.tectech.elementalMatter.core.transformations.aFluidQuantizationInfo;
import com.github.technus.tectech.elementalMatter.core.transformations.aItemQuantizationInfo;
import com.github.technus.tectech.elementalMatter.core.transformations.aOredictQuantizationInfo;
import com.github.technus.tectech.elementalMatter.core.transformations.bTransformationInfo;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.metaTileEntity.IConstructable;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;

import static com.github.technus.tectech.Util.*;
import static com.github.technus.tectech.auxiliary.TecTechConfig.DEBUG_MODE;
import static com.github.technus.tectech.elementalMatter.core.templates.iElementalDefinition.DEFAULT_ENERGY_LEVEL;
import static com.github.technus.tectech.elementalMatter.definitions.complex.atom.dAtomDefinition.refMass;
import static com.github.technus.tectech.elementalMatter.definitions.complex.atom.dAtomDefinition.refUnstableMass;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_quantizer extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {
    //region Structure
    //use multi A energy inputs, use less power the longer it runs
    private static final String[][] shape = new String[][]{
            {"   ", " . ", "   ",},
            {"010", "101", "010",},
            {"\"\"\"", "\"0\"", "\"\"\"",},
            {"202", "0!0", "202",},
    };
    private static final Block[] blockType = new Block[]{sBlockCasingsTT, sBlockCasingsTT, QuantumGlassBlock.INSTANCE};
    private static final byte[] blockMeta = new byte[]{4, 0, 0};
    private static final String[] addingMethods = new String[]{"addClassicToMachineList", "addElementalOutputToMachineList", "addElementalMufflerToMachineList"};
    private static final short[] casingTextures = new short[]{textureOffset, textureOffset + 4, textureOffset + 4};
    private static final Block[] blockTypeFallback = new Block[]{sBlockCasingsTT, sBlockCasingsTT, sBlockCasingsTT};
    private static final byte[] blockMetaFallback = new byte[]{0, 4, 4};
    private static final String[] description = new String[]{
            EnumChatFormatting.AQUA+"Hint Details:",
            "1 - Classic Hatches or High Power Casing",
            "2 - Elemental Output Hatch",
            "3 - Elemental Overflow Hatches or Molecular Casing",
    };
    //endregion

    public GT_MetaTileEntity_EM_quantizer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_quantizer(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_quantizer(mName);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return structureCheck_EM(shape, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 1, 1, 0);
    }

    @Override
    public void construct(int stackSize, boolean hintsOnly) {
        StructureBuilder(shape, blockType, blockMeta, 1, 1, 0, getBaseMetaTileEntity(),hintsOnly);
    }

    @Override
    public String[] getStructureDescription(int stackSize) {
        return description;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_EM,
                "Conveniently convert regular stuff into quantum form.",
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "To make it more inconvenient."
        };
    }

    @Override
    public boolean checkRecipe_EM(ItemStack itemStack) {//TODO implement instance quantization
        if (GregTech_API.sPostloadFinished) {
            ArrayList<ItemStack> storedInputs = getStoredInputs();
            ItemStack[] inI = storedInputs.toArray(new ItemStack[storedInputs.size()]);
            if (inI.length > 0) {
                for (ItemStack is : inI) {
                    //ITEM STACK quantization
                    aItemQuantizationInfo aIQI = bTransformationInfo.itemQuantization.get(new aItemQuantizationInfo(is, false, null));
                    if (aIQI == null) {
                        aIQI = bTransformationInfo.itemQuantization.get(new aItemQuantizationInfo(is, true, null));//todo check if works?
                    }
                    if (aIQI == null) {
                        //ORE DICT quantization //todo fix for uranium?
                        int[] oreIDs = OreDictionary.getOreIDs(is);
                        for (int ID : oreIDs) {
                            if (DEBUG_MODE) {
                                TecTech.Logger.info("Quantifier-Ore-recipe " + is.getItem().getUnlocalizedName() + '.' + is.getItemDamage() + ' ' + OreDictionary.getOreName(ID));
                            }
                            aOredictQuantizationInfo aOQI = bTransformationInfo.oredictQuantization.get(ID);
                            if (aOQI == null) {
                                continue;
                            }
                            iHasElementalDefinition into = aOQI.output();
                            if (into != null && isInputEqual(true, false, GT_MetaTileEntity_MultiblockBase_EM.nothingF, new ItemStack[]{new ItemStack(is.getItem(), aOQI.amount, is.getItemDamage())}, null, inI)) {
                                startRecipe(into);
                                return true;
                            }
                        }
                    } else {
                        //Do ITEM STACK quantization
                        if (DEBUG_MODE) {
                            TecTech.Logger.info("Quantifier-Item-recipe " + is.getItem().getUnlocalizedName() + '.' + is.getItemDamage());
                        }
                        iHasElementalDefinition into = aIQI.output();
                        if (into != null && isInputEqual(true, false, GT_MetaTileEntity_MultiblockBase_EM.nothingF, new ItemStack[]{new ItemStack(is.getItem(), aIQI.input().stackSize, is.getItemDamage())}, null, inI)) {
                            startRecipe(into);
                            return true;
                        }
                    }
                }
            }
            ArrayList<FluidStack> storedFluids = getStoredFluids();
            FluidStack[] inF = storedFluids.toArray(new FluidStack[storedFluids.size()]);
            if (inF.length > 0) {
                for (FluidStack fs : inF) {
                    aFluidQuantizationInfo aFQI = bTransformationInfo.fluidQuantization.get(fs.getFluid().getID());
                    if (aFQI == null) {
                        continue;
                    }
                    iHasElementalDefinition into = aFQI.output();
                    if (into != null && fs.amount >= aFQI.input().amount && isInputEqual(true, false,
                            new FluidStack[]{aFQI.input()}, GT_MetaTileEntity_MultiblockBase_EM.nothingI, inF, (ItemStack[]) null)) {
                        startRecipe(into);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void startRecipe(iHasElementalDefinition into) {
        mMaxProgresstime = 20;
        mEfficiencyIncrease = 10000;
        float mass = into.getMass();
        float euMult = mass / refMass;
        eAmpereFlow = (int) Math.ceil(euMult);
        if (mass > refUnstableMass || into.getDefinition().getRawTimeSpan(DEFAULT_ENERGY_LEVEL) < 1.5e25f) {
            mEUt = (int) -V[10];
        } else {
            mEUt = (int) -V[8];
        }
        outputEM = new cElementalInstanceStackMap[]{
                into instanceof cElementalInstanceStack ?
                        new cElementalInstanceStackMap((cElementalInstanceStack) into) :
                        new cElementalInstanceStackMap(new cElementalInstanceStack(into.getDefinition(), into.getAmount()))
        };
    }

    @Override
    public void outputAfterRecipe_EM() {
        if (eOutputHatches.size() < 1) {
            stopMachine();
            return;
        }
        eOutputHatches.get(0).getContainerHandler().putUnifyAll(outputEM[0]);
        outputEM = null;
    }
}
