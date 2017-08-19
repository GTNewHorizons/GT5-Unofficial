package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.elementalMatter.classes.cElementalInstanceStack;
import com.github.technus.tectech.elementalMatter.classes.cElementalInstanceStackMap;
import com.github.technus.tectech.recipe.TT_recipe;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.metaTileEntity.IConstructable;
import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import static com.github.technus.tectech.Util.StructureBuilder;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_scanner extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {
    TT_recipe.TT_EMRecipe.TT_EMRecipe eRecipe;
    private String machineType;
    private long computationRemaining,computationRequired;

    //region structure
    private static final String[][] shape = new String[][]{
            {"     ", " 222 ", " 2.2 ", " 222 ", "     ",},
            {"00000", "00000", "00000", "00000", "00000",},
            {"00100", "01110", "11111", "01110", "00100",},
            {"01110", "1---1", "1---1", "1---1", "01110",},
            {"01110", "1---1", "1-A-1", "1---1", "01110",},
            {"01110", "1---1", "1---1", "1---1", "01110",},
            {"00100", "01110", "11\"11", "01110", "00100",},
            {"#####", "#000#", "#0!0#", "#000#", "#####",},
    };
    private static final Block[] blockType = new Block[]{sBlockCasingsTT, QuantumGlassBlock.INSTANCE, sBlockCasingsTT};
    private static final byte[] blockMeta = new byte[]{4, 0, 0};
    private static final String[] addingMethods = new String[]{
            "addClassicToMachineList",
            "addElementalInputToMachineList",
            "addElementalOutputToMachineList",
            "addElementalMufflerToMachineList"};
    private static final short[] casingTextures = new short[]{textureOffset, textureOffset + 4, textureOffset + 4, textureOffset + 4};
    private static final Block[] blockTypeFallback = new Block[]{sBlockCasingsTT, sBlockCasingsTT, sBlockCasingsTT, sBlockCasingsTT};
    private static final byte[] blockMetaFallback = new byte[]{0, 4, 4, 4};
    private static final String[] description = new String[]{
            EnumChatFormatting.AQUA+"Hint Details:",
            "1 - Classic Hatches or High Power Casing",
            "2 - Elemental Input Hatches or Molecular Casing",
            "3 - Elemental Output Hatches or Molecular Casing",
            "4 - Elemental Overflow Hatches or Molecular Casing",
    };
    //endregion

    public GT_MetaTileEntity_EM_scanner(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_scanner(String aName) {
        super(aName);
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_scanner(this.mName);
    }

    @Override
    public boolean EM_checkMachine(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        if (!EM_StructureCheckAdvanced(shape, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 2, 2, 0))
            return false;
        return eInputHatches.size() != 1 || eOutputHatches.size() != 1 && eOutputHatches.get(0).getBaseMetaTileEntity().getFrontFacing() == iGregTechTileEntity.getFrontFacing();
    }

    @Override
    public void construct(int stackSize, boolean hintsOnly) {
        StructureBuilder(shape, blockType, blockMeta,2, 2, 0, getBaseMetaTileEntity(),hintsOnly);
    }

    @Override
    public String[] getStructureDescription(int stackSize) {
        return description;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.tecMark,
                "What is existing here?",
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "I HAVE NO IDEA (yet)!"
        };
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if(aBaseMetaTileEntity.isActive() && (aTick & 0x2)==0 && aBaseMetaTileEntity.isClientSide()){
            int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX*3;
            int yDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetY*3;
            int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ*3;
            aBaseMetaTileEntity.getWorld().markBlockRangeForRenderUpdate(xDir,yDir,zDir,xDir,yDir,zDir);
        }
    }

    @Override
    public boolean EM_checkRecipe(ItemStack itemStack) {
        eRecipe=null;
        if(!eInputHatches.isEmpty() && eInputHatches.get(0).getContainerHandler().hasStacks() && !eOutputHatches.isEmpty()) {
            cElementalInstanceStackMap researchEM = eInputHatches.get(0).getContainerHandler();
            if(ItemList.Tool_DataOrb.isStackEqual(itemStack, false, true)) {
                for(cElementalInstanceStack stackEM:researchEM.values()){
                    eRecipe = TT_recipe.TT_Recipe_Map_EM.sMachineRecipesEM.findRecipe(stackEM.definition);
                    if(eRecipe!=null) {
                        machineType=GT_MetaTileEntity_EM_machine.machine;
                        break;
                    }
                    eRecipe = TT_recipe.TT_Recipe_Map_EM.sCrafterRecipesEM.findRecipe(stackEM.definition);
                    if(eRecipe!=null) {
                        machineType=GT_MetaTileEntity_EM_crafter.crafter;
                        break;
                    }
                }
                if(eRecipe!=null){
                    computationRequired = computationRemaining = eRecipe.mDuration * 20L;
                    mMaxProgresstime = 20;
                    mEfficiencyIncrease = 10000;
                    eRequiredData = (short) (eRecipe.mSpecialValue >>> 16);
                    eAmpereFlow = (short) (eRecipe.mSpecialValue & 0xFFFF);
                    mEUt = eRecipe.mEUt;
                    eOutputHatches.get(0).getBaseMetaTileEntity().setActive(true);
                    return true;
                }
            }
        }
        computationRequired=computationRemaining=0;
        mMaxProgresstime=0;
        mEfficiencyIncrease = 0;
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }
}
