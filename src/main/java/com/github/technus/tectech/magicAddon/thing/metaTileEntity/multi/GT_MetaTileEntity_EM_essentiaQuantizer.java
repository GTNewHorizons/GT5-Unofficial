package com.github.technus.tectech.magicAddon.thing.metaTileEntity.multi;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.elementalMatter.classes.cElementalInstanceStack;
import com.github.technus.tectech.elementalMatter.classes.cElementalInstanceStackMap;
import com.github.technus.tectech.magicAddon.definitions.ePrimalAspectDefinition;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.metaTileEntity.iConstructible;
import com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_MultiblockBase_EM;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;

import static com.github.technus.tectech.Util.StructureBuilder;
import static com.github.technus.tectech.magicAddon.EssentiaCompat.essentiaContainerCompat;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static gregtech.api.enums.GT_Values.E;
import static gregtech.api.enums.GT_Values.V;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_essentiaQuantizer extends GT_MetaTileEntity_MultiblockBase_EM implements iConstructible {
    private TileEntity container;

    //region Structure
    //use multi A energy inputs, use less power the longer it runs
    private static final String[][] shape = new String[][]{
            {"!!!","!.!","!!!",},
            {"0A0",E,"0A0",},
            {"121","232","121",},
            {"\"\"\"","\"1\"","\"\"\"",},
            {"010","1 1","010",},
    };
    private static final Block[] blockType = new Block[]{QuantumGlassBlock.INSTANCE, sBlockCasingsTT, sBlockCasingsTT, sBlockCasingsTT};
    private static final byte[] blockMeta = new byte[]{0,4,0,10};
    private static final String[] addingMethods = new String[]{"addElementalOutputToMachineList", "addClassicToMachineList", "addElementalMufflerToMachineList"};
    private static final short[] casingTextures = new short[]{textureOffset + 4, textureOffset, textureOffset + 4};
    private static final Block[] blockTypeFallback = new Block[]{sBlockCasingsTT, sBlockCasingsTT, sBlockCasingsTT};
    private static final byte[] blockMetaFallback = new byte[]{4, 0, 4};
    //endregion

    public GT_MetaTileEntity_EM_essentiaQuantizer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_essentiaQuantizer(String aName) {
        super(aName);
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_essentiaQuantizer(this.mName);
    }

    @Override
    public boolean EM_checkMachine(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return essentiaContainerCompat.check(this) && EM_StructureCheckAdvanced(shape, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 1, 1, 0);
    }

    @Override
    public void construct(int qty, boolean hintsOnly) {
        StructureBuilder(shape, blockType, blockMeta,1, 1, 0, getBaseMetaTileEntity(),hintsOnly);
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.tecMark,
                "Conveniently convert regular stuff into quantum form.",
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "To make it more inconvenient."
        };
    }

    @Override
    public boolean EM_checkRecipe(ItemStack itemStack) {
        container=essentiaContainerCompat.getContainer(this);
        cElementalInstanceStack newStack=essentiaContainerCompat.getFromContainer(container);
        if(newStack!=null){
            mMaxProgresstime = 20;
            mEfficiencyIncrease = 10000;
            eAmpereFlow=1;
            outputEM = new cElementalInstanceStackMap[]{
                    new cElementalInstanceStackMap(newStack)
            };
            if (newStack.definition instanceof ePrimalAspectDefinition) {
                mEUt = (int) -V[8];
            } else {
                mEUt = (int) -V[10];
            }
            return true;
        }
        mEfficiencyIncrease = 0;
        mMaxProgresstime = 0;
        return false;
    }

    @Override
    public void EM_outputFunction() {
        if (eOutputHatches.size() < 1) {
            stopMachine();
            return;
        }
        eOutputHatches.get(0).getContainerHandler().putUnifyAll(outputEM[0]);
        outputEM=null;
    }
}
