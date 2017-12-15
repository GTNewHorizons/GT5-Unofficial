package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.elementalMatter.core.cElementalInstanceStackMap;
import com.github.technus.tectech.elementalMatter.core.interfaces.iExchangeInfo;
import com.github.technus.tectech.elementalMatter.core.interfaces.iHasElementalDefinition;
import com.github.technus.tectech.elementalMatter.core.stacks.cElementalInstanceStack;
import com.github.technus.tectech.elementalMatter.core.transformations.aOredictDequantizationInfo;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.metaTileEntity.IConstructable;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_InputElemental;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;

import static com.github.technus.tectech.Util.StructureBuilder;
import static com.github.technus.tectech.Util.V;
import static com.github.technus.tectech.elementalMatter.core.templates.iElementalDefinition.STABLE_RAW_LIFE_TIME;
import static com.github.technus.tectech.elementalMatter.definitions.complex.atom.dAtomDefinition.refMass;
import static com.github.technus.tectech.elementalMatter.definitions.complex.atom.dAtomDefinition.refUnstableMass;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_dequantizer extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {

    //region Structure
    //use multi A energy inputs, use less power the longer it runs
    private static final String[][] shape = new String[][]{
            {"   ", " . ", "   ",},
            {"010", "111", "010",},
            {"\"\"\"", "\"1\"", "\"\"\"",},
            {"121", "2!2", "121",},
    };
    private static final Block[] blockType = new Block[]{sBlockCasingsTT, sBlockCasingsTT, QuantumGlassBlock.INSTANCE};
    private static final byte[] blockMeta = new byte[]{0, 4, 0};
    private static final String[] addingMethods = new String[]{"addClassicToMachineList", "addElementalInputToMachineList", "addElementalMufflerToMachineList"};
    private static final short[] casingTextures = new short[]{textureOffset, textureOffset + 4, textureOffset + 4};
    private static final Block[] blockTypeFallback = new Block[]{sBlockCasingsTT, sBlockCasingsTT, sBlockCasingsTT};
    private static final byte[] blockMetaFallback = new byte[]{0, 4, 4};
    private static final String[] description = new String[]{
            EnumChatFormatting.AQUA + "Hint Details:",
            "1 - Classic Hatches or High Power Casing",
            "2 - Elemental Input Hatch",
            "3 - Elemental Overflow Hatches or Molecular Casing",
    };
    //endregion

    public GT_MetaTileEntity_EM_dequantizer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_dequantizer(String aName) {
        super(aName);
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_dequantizer(this.mName);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return structureCheck_EM(shape, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 1, 1, 0);
    }

    @Override
    public void construct(int stackSize, boolean hintsOnly) {
        StructureBuilder(shape, blockType, blockMeta, 1, 1, 0, getBaseMetaTileEntity(), hintsOnly);
    }

    @Override
    public String[] getStructureDescription(int stackSize) {
        return description;
    }

    @Override
    public boolean checkRecipe_EM(ItemStack itemStack) {
        for (GT_MetaTileEntity_Hatch_InputElemental in : eInputHatches) {
            cElementalInstanceStackMap map = in.getContainerHandler();
            for (cElementalInstanceStack stack : map.values()) {
                iExchangeInfo info = stack.getDefinition().someAmountIntoFluidStack();
                if (info != null) {
                    if (map.removeAllAmounts(false, (iHasElementalDefinition) info.input())) {
                        mOutputFluids = new FluidStack[]{(FluidStack) info.output()};
                        startRecipe((iHasElementalDefinition) info.input(),stack.getEnergy());
                        return true;
                    }
                }

                info = stack.getDefinition().someAmountIntoItemsStack();
                if (info != null) {
                    if (map.removeAllAmounts(false, (iHasElementalDefinition) info.input())) {
                        mOutputItems = new ItemStack[]{(ItemStack) info.output()};
                        startRecipe((iHasElementalDefinition) info.input(),stack.getEnergy());
                        return true;
                    }
                }

                info = stack.getDefinition().someAmountIntoOredictStack();
                if (info != null) {
                    if (map.removeAllAmounts(false, (iHasElementalDefinition) info.input())) {
                        ArrayList<ItemStack> items = OreDictionary.getOres(((aOredictDequantizationInfo) info).out);
                        if (items != null && items.size() > 0) {
                            mOutputItems = new ItemStack[]{items.get(0)};
                            startRecipe((iHasElementalDefinition) info.input(),stack.getEnergy());
                            return true;
                        }
                    }
                }
            }
        }
        mEfficiencyIncrease = 0;
        mMaxProgresstime = 0;
        return false;
    }

    private void startRecipe(iHasElementalDefinition from, long energy) {
        mMaxProgresstime = 20;
        mEfficiencyIncrease = 10000;
        float mass = from.getMass();
        float euMult = mass / refMass;
        eAmpereFlow = (int) Math.ceil(euMult);
        if (mass > refUnstableMass || from.getDefinition().getRawTimeSpan(energy) < STABLE_RAW_LIFE_TIME) {
            mEUt = (int) -V[10];
        } else {
            mEUt = (int) -V[8];
        }
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_EM,
                "Transform quantum form back to...",
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "regular one, but why?"
        };
    }
}
