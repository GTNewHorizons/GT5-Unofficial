package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.elementalMatter.core.cElementalInstanceStackMap;
import com.github.technus.tectech.elementalMatter.core.stacks.cElementalDefinitionStack;
import com.github.technus.tectech.elementalMatter.core.stacks.cElementalInstanceStack;
import com.github.technus.tectech.elementalMatter.core.tElementalException;
import com.github.technus.tectech.recipe.TT_recipe;
import com.github.technus.tectech.thing.CustomItemList;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.block.QuantumStuffBlock;
import com.github.technus.tectech.thing.item.ElementalDefinitionScanStorage_EM;
import com.github.technus.tectech.thing.metaTileEntity.IConstructable;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Recipe;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import static com.github.technus.tectech.Util.*;
import static com.github.technus.tectech.auxiliary.TecTechConfig.DEBUG_MODE;
import static com.github.technus.tectech.elementalMatter.definitions.primitive.cPrimitiveDefinition.nbtE__;
import static com.github.technus.tectech.recipe.TT_recipe.E_RECIPE_ID;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_crafting.crafter;
import static com.github.technus.tectech.thing.metaTileEntity.multi.em_machine.GT_MetaTileEntity_EM_machine.machine;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_scanner extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {
    public static final int SCAN_DO_NOTHING=0,
            SCAN_GET_NOMENCLATURE=1,SCAN_GET_DEPTH_LEVEL=2,SCAN_GET_AMOUNT=4,SCAN_GET_CHARGE=8,
            SCAN_GET_MASS=16,SCAN_GET_ENERGY_LEVEL=32,SCAN_GET_TIMESPAN_INFO=64,SCAN_GET_ENERGY_STATES=128,
        SCAN_GET_COLOR=256,SCAN_GET_AGE=512,SCAN_GET_TIMESPAN_MULT=1024,SCAN_GET_CLASS_TYPE=2048;

    private TT_recipe.TT_EMRecipe.TT_EMRecipe eRecipe;
    private cElementalDefinitionStack objectResearched;
    private cElementalInstanceStackMap objectsScanned;
    private String machineType;
    private long totalComputationRemaining, totalComputationRequired;
    private int[] scanComplexity;

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
        eDismantleBoom=true;
    }

    public GT_MetaTileEntity_EM_scanner(String aName) {
        super(aName);
        eDismantleBoom=true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_scanner(mName);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        if (!structureCheck_EM(shape, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 2, 2, 0)) {
            return false;
        }
        return eInputHatches.size() == 1 && eOutputHatches.size() == 1 && eOutputHatches.get(0).getBaseMetaTileEntity().getFrontFacing() == iGregTechTileEntity.getFrontFacing();
    }

    @Override
    public void construct(int stackSize, boolean hintsOnly) {
        StructureBuilderExtreme(shape, blockType, blockMeta,2, 2, 0, getBaseMetaTileEntity(),this,hintsOnly);
    }

    @Override
    public String[] getStructureDescription(int stackSize) {
        return description;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_EM,
                "What is existing here?",
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "I HAVE NO IDEA (yet)!"
        };
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setLong("eComputationRemaining", totalComputationRemaining);
        aNBT.setLong("eComputationRequired", totalComputationRequired);
        if(objectResearched!=null) {
            aNBT.setTag("eObject", objectResearched.toNBT());
        } else {
            aNBT.removeTag("eObject");
        }
        if(scanComplexity!=null) {
            aNBT.setIntArray("eScanComplexity", scanComplexity);
        } else {
            aNBT.removeTag("eScanComplexity");
        }
        if(objectsScanned!=null) {
            aNBT.setTag("eScanObjects", objectsScanned.toNBT());
        } else {
            aNBT.removeTag("eScanObjects");
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        totalComputationRemaining =aNBT.getLong("eComputationRemaining");
        totalComputationRequired =aNBT.getLong("eComputationRequired");
        if(aNBT.hasKey("eObject")) {
            objectResearched = cElementalDefinitionStack.fromNBT(aNBT.getCompoundTag("eObject"));
            if(objectResearched.definition==nbtE__) {
                objectResearched = null;
            }
        }else {
            objectResearched = null;
        }
        if(aNBT.hasKey("eScanComplexity")) {
            scanComplexity = aNBT.getIntArray("eScanComplexity");
        } else {
            scanComplexity = null;
        }
        try {
            if (aNBT.hasKey("eScanObjects")) {
                objectsScanned = cElementalInstanceStackMap.fromNBT(aNBT.getCompoundTag("eScanObjects"));
            }
        }catch (tElementalException e){
            objectsScanned=new cElementalInstanceStackMap();
        }
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if(aBaseMetaTileEntity.isActive() && (aTick & 0x2)==0 && aBaseMetaTileEntity.isClientSide()){
            int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX*4+aBaseMetaTileEntity.getXCoord();
            int yDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetY*4+aBaseMetaTileEntity.getYCoord();
            int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ*4+aBaseMetaTileEntity.getZCoord();
            aBaseMetaTileEntity.getWorld().markBlockRangeForRenderUpdate(xDir,yDir,zDir,xDir,yDir,zDir);
        }
    }

    @Override
    public void onFirstTick_EM(IGregTechTileEntity aBaseMetaTileEntity) {
        if(aBaseMetaTileEntity.isServerSide()) {
            if (totalComputationRemaining > 0 && objectResearched!=null) {
                eRecipe = null;
                if (ItemList.Tool_DataOrb.isStackEqual(mInventory[1], false, true)) {
                    eRecipe = TT_recipe.TT_Recipe_Map_EM.sMachineRecipesEM.findRecipe(objectResearched.definition);
                    if (eRecipe != null) {
                        machineType = machine;
                    } else {
                        eRecipe = TT_recipe.TT_Recipe_Map_EM.sCrafterRecipesEM.findRecipe(objectResearched.definition);
                        if (eRecipe != null) {
                            machineType = crafter;
                        }
                    }
                }
                if (eRecipe == null) {
                    quantumStuff(false);
                    objectResearched = null;
                    eRequiredData=0;
                    totalComputationRequired = totalComputationRemaining = 0;
                    mMaxProgresstime = 0;
                    mEfficiencyIncrease = 0;
                } else {
                    quantumStuff(true);
                }
            }
        }
    }

    @Override
    public boolean checkRecipe_EM(ItemStack itemStack) {
        eRecipe=null;
        if(!eInputHatches.isEmpty() && eInputHatches.get(0).getContainerHandler().hasStacks() && !eOutputHatches.isEmpty()) {
            cElementalInstanceStackMap researchEM = eInputHatches.get(0).getContainerHandler();
            if(ItemList.Tool_DataOrb.isStackEqual(itemStack, false, true)) {
                GT_Recipe scannerRecipe=null;
                for(cElementalInstanceStack stackEM:researchEM.values()){
                    objectsScanned=null;
                    eRecipe = TT_recipe.TT_Recipe_Map_EM.sMachineRecipesEM.findRecipe(stackEM.definition);
                    if(eRecipe!=null) {
                        scannerRecipe=eRecipe.scannerRecipe;
                        machineType= machine;
                        objectResearched=new cElementalDefinitionStack(stackEM.definition,1);
                        //cleanMassEM_EM(objectResearched.getMass());
                        researchEM.remove(objectResearched.definition);
                        break;
                    }
                    eRecipe = TT_recipe.TT_Recipe_Map_EM.sCrafterRecipesEM.findRecipe(stackEM.definition);
                    if(eRecipe!=null) {
                        scannerRecipe=eRecipe.scannerRecipe;
                        machineType= crafter;
                        objectResearched=new cElementalDefinitionStack(stackEM.definition,1);
                        //cleanMassEM_EM(objectResearched.getMass());
                        researchEM.remove(objectResearched.definition);
                        break;
                    }
                    cleanStackEM_EM(stackEM);
                    researchEM.remove(stackEM.definition);
                }
                if(eRecipe!=null && scannerRecipe!=null){//make sure it werks
                    totalComputationRequired = totalComputationRemaining = scannerRecipe.mDuration * 20L;
                    mMaxProgresstime = 20;//const
                    mEfficiencyIncrease = 10000;
                    eRequiredData = (short) (scannerRecipe.mSpecialValue >>> 16);
                    eAmpereFlow = (short) (scannerRecipe.mSpecialValue & 0xFFFF);
                    mEUt = scannerRecipe.mEUt;
                    quantumStuff(true);
                    return true;
                }
            }else if(CustomItemList.scanContainer.isStackEqual(itemStack, false, true)) {
                eRecipe=null;
                if(researchEM.hasStacks()) {
                    objectsScanned = researchEM.takeAllToNewMap();
                    cleanMassEM_EM(objectsScanned.getMass());

                    totalComputationRequired =0;
                    eRequiredData=0;
                    eAmpereFlow=objectsScanned.size() + TecTech.Rnd.next(objectsScanned.size());
                    mEUt=-(int)V[8];

                    //get depth scan complexity array
                    {
                        int[] scanComplexityTemp = new int[20];
                        for (int i = 0; i < 10; i++) {
                            scanComplexityTemp[i] = getParameterInInt(i, 0);
                            scanComplexityTemp[i + 10] = getParameterInInt(i, 1);
                        }
                        int maxDepth = 0;
                        for (int i = 0; i < 20; i++) {
                            if (scanComplexityTemp[i] != SCAN_DO_NOTHING) {
                                maxDepth = i;
                                if(!DEBUG_MODE) {
                                    scanComplexityTemp[i] &= ~SCAN_GET_CLASS_TYPE;
                                }
                                addComputationRequirements(i+1,scanComplexityTemp[i]);
                            }
                        }
                        maxDepth+=1;//from index to len
                        scanComplexity = new int[maxDepth];
                        System.arraycopy(scanComplexityTemp,0,scanComplexity,0,maxDepth);
                    }

                    totalComputationRemaining = totalComputationRequired*=20;
                    mMaxProgresstime = 20;//const
                    mEfficiencyIncrease = 10000;
                    quantumStuff(true);
                    return true;
                }
            }
        }
        quantumStuff(false);
        objectResearched=null;
        totalComputationRemaining =0;
        return false;
    }

    private void addComputationRequirements(int depthPlus, int capabilities){
        if(areBitsSet(SCAN_GET_NOMENCLATURE,capabilities)){
            totalComputationRequired +=depthPlus*5L;
            eRequiredData+=depthPlus;
        }
        if(areBitsSet(SCAN_GET_DEPTH_LEVEL,capabilities)){
            totalComputationRequired +=depthPlus*10L;
            eRequiredData+=depthPlus;

        }
        if(areBitsSet(SCAN_GET_AMOUNT,capabilities)){
            totalComputationRequired +=depthPlus*64L;
            eRequiredData+=depthPlus*8L;

        }
        if(areBitsSet(SCAN_GET_CHARGE,capabilities)){
            totalComputationRequired +=depthPlus*128L;
            eRequiredData+=depthPlus*4L;

        }
        if(areBitsSet(SCAN_GET_MASS,capabilities)){
            totalComputationRequired +=depthPlus*256L;
            eRequiredData+=depthPlus*4L;

        }
        if(areBitsSet(SCAN_GET_ENERGY_LEVEL,capabilities)){
            totalComputationRequired +=depthPlus*512L;
            eRequiredData+=depthPlus*16L;

        }
        if(areBitsSet(SCAN_GET_TIMESPAN_INFO,capabilities)){
            totalComputationRequired +=depthPlus*1024L;
            eRequiredData+=depthPlus*32L;

        }
        if(areBitsSet(SCAN_GET_ENERGY_STATES,capabilities)){
            totalComputationRequired +=depthPlus*2048L;
            eRequiredData+=depthPlus*32L;

        }
        if(areBitsSet(SCAN_GET_COLOR,capabilities)){
            totalComputationRequired +=depthPlus*1024L;
            eRequiredData+=depthPlus*48L;

        }
        if(areBitsSet(SCAN_GET_AGE,capabilities)){
            totalComputationRequired +=depthPlus*2048L;
            eRequiredData+=depthPlus*64L;

        }
        if(areBitsSet(SCAN_GET_TIMESPAN_MULT,capabilities)){
            totalComputationRequired +=depthPlus*2048L;
            eRequiredData+=depthPlus*64L;

        }
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if(totalComputationRemaining <=0) {
            totalComputationRemaining =0;
            mProgresstime=mMaxProgresstime;
            return true;
        }else{
            totalComputationRemaining -=eAvailableData;
            mProgresstime=1;
            return super.onRunningTick(aStack);
        }
    }

    @Override
    public void outputAfterRecipe_EM() {
        if (eRecipe != null && ItemList.Tool_DataOrb.isStackEqual(mInventory[1], false, true)){

            mInventory[1].setStackDisplayName(GT_LanguageManager.getTranslation(eRecipe.mOutputs[0].getDisplayName()) + ' ' + machineType +" Construction Data");
            NBTTagCompound tNBT = mInventory[1].getTagCompound();//code above makes it not null

            tNBT.setString("eMachineType", machineType);
            tNBT.setTag(E_RECIPE_ID, objectResearched.toNBT());
            tNBT.setString("author", EnumChatFormatting.BLUE + "Tec" + EnumChatFormatting.DARK_BLUE + "Tech" + EnumChatFormatting.WHITE + ' ' + machineType+ " EM Recipe Generator");
        }else if(objectsScanned!=null && CustomItemList.scanContainer.isStackEqual(mInventory[1], false, true)){
            ElementalDefinitionScanStorage_EM.setContent(mInventory[1],objectsScanned,scanComplexity);
        }
        objectResearched=null;
        totalComputationRemaining =0;
        quantumStuff(false);
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public String[] getInfoData() {
        long storedEnergy = 0;
        long maxEnergy = 0;
        for (GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tHatch)) {
                storedEnergy += tHatch.getBaseMetaTileEntity().getStoredEU();
                maxEnergy += tHatch.getBaseMetaTileEntity().getEUCapacity();
            }
        }
        for (GT_MetaTileEntity_Hatch_EnergyMulti tHatch : eEnergyMulti) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tHatch)) {
                storedEnergy += tHatch.getBaseMetaTileEntity().getStoredEU();
                maxEnergy += tHatch.getBaseMetaTileEntity().getEUCapacity();
            }
        }

        return new String[]{
                "Energy Hatches:",
                EnumChatFormatting.GREEN + Long.toString(storedEnergy) + EnumChatFormatting.RESET + " EU / " +
                        EnumChatFormatting.YELLOW + Long.toString(maxEnergy) + EnumChatFormatting.RESET + " EU",
                (mEUt <= 0 ? "Probably uses: " : "Probably makes: ") +
                        EnumChatFormatting.RED + Integer.toString(Math.abs(mEUt)) + EnumChatFormatting.RESET + " EU/t at " +
                        EnumChatFormatting.RED + eAmpereFlow + EnumChatFormatting.RESET + " A",
                "Tier Rating: " + EnumChatFormatting.YELLOW + VN[getMaxEnergyInputTier_EM()] + EnumChatFormatting.RESET + " / " + EnumChatFormatting.GREEN + VN[getMinEnergyInputTier_EM()] + EnumChatFormatting.RESET +
                        " Amp Rating: " + EnumChatFormatting.GREEN + eMaxAmpereFlow + EnumChatFormatting.RESET + " A",
                "Problems: " + EnumChatFormatting.RED + (getIdealStatus() - getRepairStatus()) + EnumChatFormatting.RESET +
                        " Efficiency: " + EnumChatFormatting.YELLOW + Float.toString(mEfficiency / 100.0F) + EnumChatFormatting.RESET + " %",
                "PowerPass: " + EnumChatFormatting.BLUE + ePowerPass + EnumChatFormatting.RESET +
                        " SafeVoid: " + EnumChatFormatting.BLUE + eSafeVoid,
                "Computation Available: " + EnumChatFormatting.GREEN + eAvailableData +EnumChatFormatting.RESET+" / "+EnumChatFormatting.YELLOW + eRequiredData + EnumChatFormatting.RESET,
                "Computation Remaining:",
                EnumChatFormatting.GREEN + Long.toString(totalComputationRemaining / 20L) + EnumChatFormatting.RESET + " / " +
                        EnumChatFormatting.YELLOW + Long.toString(totalComputationRequired / 20L)
        };
    }

    @Override
    public void onRemoval() {
        quantumStuff(false);
        super.onRemoval();
    }

    @Override
    public void stopMachine() {
        quantumStuff(false);
        super.stopMachine();
        totalComputationRequired = totalComputationRemaining =0;
        objectResearched=null;

    }

    private void quantumStuff(boolean shouldExist){
        IGregTechTileEntity base=getBaseMetaTileEntity();
        if(base!=null && base.getWorld()!=null) {
            int xDir = ForgeDirection.getOrientation(base.getBackFacing()).offsetX * 4+base.getXCoord();
            int yDir = ForgeDirection.getOrientation(base.getBackFacing()).offsetY * 4+base.getYCoord();
            int zDir = ForgeDirection.getOrientation(base.getBackFacing()).offsetZ * 4+base.getZCoord();
            Block block = base.getWorld().getBlock(xDir, yDir, zDir);
            if (shouldExist) {
                if(block != null && block.getMaterial()== Material.air) {
                    base.getWorld().setBlock(xDir, yDir, zDir, QuantumStuffBlock.INSTANCE, 0, 2);
                }
            } else {
                if (block instanceof QuantumStuffBlock) {
                    base.getWorld().setBlock(xDir, yDir, zDir, Blocks.air, 0, 2);
                }
            }
        }
    }
}
