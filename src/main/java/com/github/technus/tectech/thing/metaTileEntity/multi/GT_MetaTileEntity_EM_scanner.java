package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.elementalMatter.core.cElementalInstanceStackMap;
import com.github.technus.tectech.elementalMatter.core.containers.cElementalDefinitionStack;
import com.github.technus.tectech.elementalMatter.core.containers.cElementalInstanceStack;
import com.github.technus.tectech.recipe.TT_recipe;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.block.QuantumStuffBlock;
import com.github.technus.tectech.thing.metaTileEntity.IConstructable;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti;
import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Recipe;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import static com.github.technus.tectech.Util.StructureBuilder;
import static com.github.technus.tectech.Util.VN;
import static com.github.technus.tectech.elementalMatter.definitions.primitive.cPrimitiveDefinition.nbtE__;
import static com.github.technus.tectech.recipe.TT_recipe.E_RECIPE_ID;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_crafting.crafter;
import static com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_machine.machine;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_scanner extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {
    private TT_recipe.TT_EMRecipe.TT_EMRecipe eRecipe;
    private cElementalDefinitionStack objectResearched;
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
        eDismantleBoom=true;
    }

    public GT_MetaTileEntity_EM_scanner(String aName) {
        super(aName);
        eDismantleBoom=true;
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_scanner(this.mName);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        if (!structureCheck_EM(shape, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 2, 2, 0))
            return false;
        return eInputHatches.size() == 1 && eOutputHatches.size() == 1 && eOutputHatches.get(0).getBaseMetaTileEntity().getFrontFacing() == iGregTechTileEntity.getFrontFacing();
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
                CommonValues.TEC_MARK_EM,
                "What is existing here?",
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "I HAVE NO IDEA (yet)!"
        };
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setLong("eComputationRemaining",computationRemaining);
        aNBT.setLong("eComputationRequired",computationRequired);
        if(objectResearched!=null)
            aNBT.setTag("eObject",objectResearched.toNBT());
        else aNBT.removeTag("eObject");
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        computationRemaining=aNBT.getLong("eComputationRemaining");
        computationRequired=aNBT.getLong("eComputationRequired");
        if(aNBT.hasKey("eObject")) {
            objectResearched = cElementalDefinitionStack.fromNBT(aNBT.getCompoundTag("eObject"));
            if(objectResearched.definition==nbtE__) objectResearched=null;
        }else objectResearched=null;
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
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        if(aBaseMetaTileEntity.isServerSide()) {
            if (computationRemaining > 0) {
                eRecipe = null;
                if (objectResearched != null) {
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
                }
                if (eRecipe == null) {
                    quantumStuff(false);
                    objectResearched = null;
                    computationRequired = computationRemaining = 0;
                    mMaxProgresstime = 0;
                    mEfficiencyIncrease = 0;
                } else quantumStuff(true);
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
                    computationRequired = computationRemaining = scannerRecipe.mDuration * 20L;
                    mMaxProgresstime = 20;
                    mEfficiencyIncrease = 10000;
                    eRequiredData = (short) (scannerRecipe.mSpecialValue >>> 16);
                    eAmpereFlow = (short) (scannerRecipe.mSpecialValue & 0xFFFF);
                    mEUt = scannerRecipe.mEUt;
                    quantumStuff(true);
                    return true;
                }
            }//else{
            //todo implement molecular in depth info scan
            //}
        }
        quantumStuff(false);
        objectResearched=null;
        computationRequired=computationRemaining=0;
        mMaxProgresstime=0;
        mEfficiencyIncrease = 0;
        return false;
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if(computationRemaining<=0) {
            computationRemaining=0;
            mProgresstime=mMaxProgresstime;
            return true;
        }else{
            computationRemaining-=eAvailableData;
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
        }
        quantumStuff(false);
        objectResearched=null;
        computationRequired=computationRemaining=0;
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
            if (isValidMetaTileEntity(tHatch)) {
                storedEnergy += tHatch.getBaseMetaTileEntity().getStoredEU();
                maxEnergy += tHatch.getBaseMetaTileEntity().getEUCapacity();
            }
        }
        for (GT_MetaTileEntity_Hatch_EnergyMulti tHatch : eEnergyMulti) {
            if (isValidMetaTileEntity(tHatch)) {
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
                "Computation Available: " + EnumChatFormatting.GREEN + eAvailableData + EnumChatFormatting.RESET,
                "Computation Remaining:",
                EnumChatFormatting.GREEN + Long.toString(computationRemaining / 20L) + EnumChatFormatting.RESET + " / " +
                        EnumChatFormatting.YELLOW + Long.toString(computationRequired / 20L)
        };
    }

    @Override
    public void onRemoval() {
        super.onRemoval();
        quantumStuff(false);
    }

    @Override
    public void stopMachine() {
        super.stopMachine();
        computationRequired=computationRemaining=0;
        objectResearched=null;
        quantumStuff(false);

    }

    private void quantumStuff(boolean shouldExist){
        IGregTechTileEntity base=getBaseMetaTileEntity();
        if(base!=null && base.getWorld()!=null) {
            int xDir = ForgeDirection.getOrientation(base.getBackFacing()).offsetX * 4+base.getXCoord();
            int yDir = ForgeDirection.getOrientation(base.getBackFacing()).offsetY * 4+base.getYCoord();
            int zDir = ForgeDirection.getOrientation(base.getBackFacing()).offsetZ * 4+base.getZCoord();
            Block block = base.getWorld().getBlock(xDir, yDir, zDir);
            if (shouldExist) {
                if(block != null && block.getMaterial()== Material.air)
                base.getWorld().setBlock(xDir, yDir, zDir, QuantumStuffBlock.INSTANCE,0,2);
            } else {
                if (block instanceof QuantumStuffBlock)
                    base.getWorld().setBlock(xDir, yDir, zDir, Blocks.air,0,2);
            }
        }
    }
}
