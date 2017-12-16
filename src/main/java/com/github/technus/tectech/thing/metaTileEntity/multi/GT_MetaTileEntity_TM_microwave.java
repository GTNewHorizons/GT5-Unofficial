package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.auxiliary.Reference;
import com.github.technus.tectech.thing.metaTileEntity.IConstructable;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_Container_MultiMachineEM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_GUIContainer_MultiMachineEM;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.HashSet;

import static com.github.technus.tectech.Util.StructureBuilder;
import static com.github.technus.tectech.loader.MainLoader.microwaving;
import static gregtech.api.GregTech_API.sBlockCasings4;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_TM_microwave extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {
    public final static int POWER_SETTING_DEFAULT=1000, TIMER_SETTING_DEFAULT=360;
    private int powerSetting = POWER_SETTING_DEFAULT;
    private int timerSetting = TIMER_SETTING_DEFAULT;
    private int timerValue = 0;
    private boolean hasBeenPausedThisCycle=false;
    private boolean flipped=false;

    //region Structure
    //use multi A energy inputs, use less power the longer it runs
    private static final String[][] shape = new String[][]{
            {"00000", "00000", "00.00", "0   0",},
            {"0---0", "0---0", "0---0", " 000 ",},
            {"0---0", "0---0", "0---0", " 000 ",},
            {"0---0", "0---0", "0---0", " 000 ",},
            {"00000", "00000", "00000", "0   0",},
    };
    private static final String[][] shapeFlipped = new String[][]{
            {shape[0][3],shape[0][2],shape[0][1],shape[0][0],},
            {shape[1][3],shape[1][2],shape[1][1],shape[1][0],},
            {shape[2][3],shape[2][2],shape[2][1],shape[2][0],},
            {shape[3][3],shape[3][2],shape[3][1],shape[3][0],},
            {shape[4][3],shape[4][2],shape[4][1],shape[4][0],},
    };
    private static final Block[] blockType = new Block[]{sBlockCasings4};
    private static final byte[] blockMeta = new byte[]{1};

    private static final String[] addingMethods = new String[]{"addClassicToMachineList"};
    private static final short[] casingTextures = new short[]{49};
    private static final Block[] blockTypeFallback = new Block[]{sBlockCasings4};
    private static final byte[] blockMetaFallback = new byte[]{1};
    private static final String[] description = new String[]{
            EnumChatFormatting.AQUA+"Hint Details:",
            "1 - Classic Hatches or High Power Casing",
            "Also acts like a hopper so give it an Output Bus",
    };
    //endregion

    public GT_MetaTileEntity_TM_microwave(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_TM_microwave(String aName) {
        super(aName);
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_TM_microwave(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[49], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE)};
        }else if(aSide == GT_Utility.getOppositeSide(aFacing)) {
            return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[49], aActive ? Textures.BlockIcons.CASING_BLOCKS[52] : Textures.BlockIcons.CASING_BLOCKS[53]};
        }
        return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[49]};
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_MultiMachineEM(aPlayerInventory, aBaseMetaTileEntity, false, false, true);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachineEM(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), "EMDisplay.png", false, false, true);//todo texture
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return aFacing >= 2;
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        if(flipped){//some optimization
            if(structureCheck_EM(shapeFlipped, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 2, 1, 0)){
                flipped=true;
                return true;
            }
            if(structureCheck_EM(shape, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 2, 2, 0)){
                flipped=false;
                return true;
            }
        }else{
            if(structureCheck_EM(shape, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 2, 2, 0)){
                flipped=false;
                return true;
            }
            if(structureCheck_EM(shapeFlipped, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 2, 1, 0)){
                flipped=true;
                return true;
            }
        }
        return false;
    }

    @Override
    public void construct(int stackSize, boolean hintsOnly) {
        if((stackSize &0x1)==1) StructureBuilder(shape, blockType, blockMeta,2, 2, 0, getBaseMetaTileEntity(),hintsOnly);
        else StructureBuilder(shapeFlipped, blockType, blockMeta,2, 1, 0, getBaseMetaTileEntity(),hintsOnly);
    }

    @Override
    public String[] getStructureDescription(int stackSize) {
        return description;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.BASS_MARK,
                "High Frequency Oven",
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "From live to done in seconds!",
                EnumChatFormatting.BLUE + "I said nuke the... I meant microwave supper!",
        };
    }

    @Override
    public boolean checkRecipe_EM(ItemStack itemStack, boolean noParametrizers) {
        hasBeenPausedThisCycle =false;
        if(noParametrizers){
            powerSetting=POWER_SETTING_DEFAULT;
            timerSetting=TIMER_SETTING_DEFAULT;
        }
        if(powerSetting<300 || timerSetting<=0 || timerSetting>3000) return false;
        if (timerValue <= 0) {
            timerValue=timerSetting;
        }
        mEUt = -(powerSetting >> 1);
        eAmpereFlow = 1;
        mMaxProgresstime = 20;
        mEfficiencyIncrease = 10000;
        return true;
    }

    @Override
    public void outputAfterRecipe_EM() {
        if(hasBeenPausedThisCycle) return;//skip timer and actions if paused
        timerValue--;
        IGregTechTileEntity mte=getBaseMetaTileEntity();
        int xDirShift = ForgeDirection.getOrientation(mte.getBackFacing()).offsetX*2;
        int zDirShift = ForgeDirection.getOrientation(mte.getBackFacing()).offsetZ*2;
        float xPos=mte.getXCoord()+0.5f;
        float yPos=mte.getYCoord()+0.5f;
        float zPos=mte.getZCoord()+0.5f;
        ArrayList<ItemStack> itemsToOutput=new ArrayList<>();
        HashSet<Entity> tickedStuff=new HashSet<>();

        AxisAlignedBB aabb=AxisAlignedBB.getBoundingBox(
                xPos-1.5+xDirShift,
                yPos-(flipped?2.5:.5),
                zPos-1.5+zDirShift,
                xPos+1.5+xDirShift,
                yPos+(flipped?.5:2.5),
                zPos+1.5+zDirShift);

        int damagingFactor =
                Math.min(powerSetting >> 6,8)+
                Math.min(powerSetting >> 8,24)+
                Math.min(powerSetting >> 12,48)+
                        (powerSetting >> 18);

        boolean inside=true;
        do {
            for (Object entity : mte.getWorld().getEntitiesWithinAABBExcludingEntity(null, aabb)) {
                if (entity instanceof Entity) {
                    if(tickedStuff.add((Entity)entity)) {
                        if (inside && entity instanceof EntityItem) {
                            GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sMicrowaveRecipes.findRecipe(
                                    mte, null, true, 128, null, null, new ItemStack[]{((EntityItem) entity).getEntityItem()});
                            if (tRecipe == null || tRecipe.mInputs[0].stackSize != 1) {
                                itemsToOutput.add(((EntityItem) entity).getEntityItem());
                            } else {
                                ItemStack newStuff = tRecipe.getOutput(0).copy();
                                newStuff.stackSize = ((EntityItem) entity).getEntityItem().stackSize;
                                itemsToOutput.add(newStuff);
                            }
                            ((EntityItem) entity).delayBeforeCanPickup=2;
                            ((EntityItem) entity).setDead();
                        } else if (entity instanceof EntityLivingBase) {
                            if(!GT_Utility.isWearingFullElectroHazmat((EntityLivingBase) entity))
                                ((EntityLivingBase) entity).attackEntityFrom(microwaving, damagingFactor);
                        }
                    }
                }
            }
            aabb.offset(0,flipped?-3:3,0);
            aabb.minX-=.5f;
            aabb.maxX+=.5f;
            aabb.minZ-=.5f;
            aabb.maxZ+=.5f;
            inside=false;
            damagingFactor>>=1;
        } while(damagingFactor>0);

        mOutputItems=itemsToOutput.toArray(new ItemStack[0]);

        if(timerValue<=0) {
            mte.getWorld().playSoundEffect(xPos,yPos,zPos, Reference.MODID+":microwave_ding", 1, 1);
            stopMachine();
        }
    }

    @Override
    public void updateParameters_EM(boolean machineIsBusy) {
        double powerParameter= getParameterInSafely(0,0);
        if (powerParameter < 300)
            setStatusOfParameterIn(0,0, STATUS_TOO_LOW);
        else if (powerParameter < 1000)
            setStatusOfParameterIn(0,0, STATUS_LOW);
        else if (powerParameter == 1000)
            setStatusOfParameterIn(0,0, STATUS_OK);
        else if (powerParameter==Double.POSITIVE_INFINITY)
            setStatusOfParameterIn(0,0, STATUS_TOO_HIGH);
        else if (Double.isNaN(powerParameter))
            setStatusOfParameterIn(0,0, STATUS_WRONG);
        else setStatusOfParameterOut(0,0,STATUS_HIGH);

        double timerParameter= getParameterInSafely(0,1);
        if (timerParameter <= 1)
            setStatusOfParameterIn(0,1,STATUS_TOO_LOW);
        else if (timerParameter <= 3000)
            setStatusOfParameterIn(0,0,STATUS_OK);
        else if (Double.isNaN(timerParameter))
            setStatusOfParameterIn(0,1,STATUS_WRONG);
        else setStatusOfParameterIn(0,1,STATUS_TOO_HIGH);

        setParameterOutSafely(0,0,timerValue);
        setParameterOutSafely(0,1,timerSetting-timerValue);

        if(machineIsBusy) return;

        powerSetting = (int)powerParameter;
        timerSetting = (int)timerParameter;
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if(eSafeVoid) hasBeenPausedThisCycle =true;
        return hasBeenPausedThisCycle || super.onRunningTick(aStack);//consume eu and other resources if not paused
    }

    @Override
    public void stopMachine() {
        super.stopMachine();
        timerValue=0;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("eTimerVal", timerValue);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        timerValue = aNBT.getInteger("eTimerVal");
    }

    @Override
    public byte getTileEntityBaseType() {
        return 1;
    }
}
