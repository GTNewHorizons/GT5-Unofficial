package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.Reference;
import com.github.technus.tectech.thing.metaTileEntity.IConstructable;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_Container_MultiMachineEM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_GUIContainer_MultiMachineEM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedTexture;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
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

import java.util.ArrayList;
import java.util.HashSet;

import static com.github.technus.tectech.Util.StructureBuilderExtreme;
import static com.github.technus.tectech.loader.MainLoader.microwaving;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.*;
import static gregtech.api.GregTech_API.sBlockCasings4;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_TM_microwave extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {
    public static final int POWER_SETTING_DEFAULT=1000, TIMER_SETTING_DEFAULT=360;
    private int powerSetting,timerSetting,timerValue;
    private boolean hasBeenPausedThisCycle=false;

    //region Structure
    //use multi A energy inputs, use less power the longer it runs
    private static final String[][] shape = new String[][]{
            {"00000", "00000", "00.00", "0   0",},
            {"0---0", "0---0", "0---0", " 000 ",},
            {"0---0", "0---0", "0---0", " 000 ",},
            {"0---0", "0---0", "0---0", " 000 ",},
            {"00000", "00000", "00000", "0   0",},
    };
    private static final Block[] blockType = new Block[]{sBlockCasings4};
    private static final byte[] blockMeta = new byte[]{1};

    private static final String[] addingMethods = new String[]{"addClassicToMachineList"};
    private static final short[] casingTextures = new short[]{49};
    private static final Block[] blockTypeFallback = new Block[]{sBlockCasings4};
    private static final byte[] blockMetaFallback = new byte[]{1};
    private static final String[] description = new String[]{
            EnumChatFormatting.AQUA+"Hint Details:",
            "1 - Classic Hatches or Clean Stainless Steel Casing",
            "Also acts like a hopper so give it an Output Bus",
    };
    //endregion

    public GT_MetaTileEntity_TM_microwave(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_TM_microwave(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_TM_microwave(mName);
    }

    @Override
    public void onRemoval(){}//Literally stops this machine from exploding if you break it with some power left, it doesn't deal with any EM ffs

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[49], new TT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE)};
        }else if(aSide == GT_Utility.getOppositeSide(aFacing)) {
            return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[49], aActive ? Textures.BlockIcons.CASING_BLOCKS[52] : Textures.BlockIcons.CASING_BLOCKS[53]};
        }
        return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[49]};
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_MultiMachineEM(aPlayerInventory, aBaseMetaTileEntity, false, true, true);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachineEM(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "EMDisplay.png", false, true, true);//todo texture
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return structureCheck_EM(shape, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 2, 2, 0);
    }

    @Override
    public void construct(int stackSize, boolean hintsOnly) {
        StructureBuilderExtreme(shape, blockType, blockMeta, 2, 2, 0, getBaseMetaTileEntity(),this, hintsOnly);
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
    public boolean checkRecipe_EM(ItemStack itemStack) {
        hasBeenPausedThisCycle =false;
        if(powerSetting<300 || timerSetting<=0 || timerSetting>3000) {
            return false;
        }
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
        if(hasBeenPausedThisCycle) {
            return;//skip timer and actions if paused
        }
        timerValue--;
        IGregTechTileEntity mte=getBaseMetaTileEntity();
        double[] xyzOffsets= getTranslatedOffsets(0,-1,2);
        double xPos=mte.getXCoord()+0.5f+xyzOffsets[0];
        double yPos=mte.getYCoord()+0.5f+xyzOffsets[1];
        double zPos=mte.getZCoord()+0.5f+xyzOffsets[2];
        AxisAlignedBB aabb=getBoundingBox(-2,-2,-2,2,2,2).offset(xPos,yPos,zPos);
        xyzOffsets= getTranslatedOffsets(0,-4,0);
        double[] xyzExpansion= getTranslatedOffsets(1.5,0,1.5);
        for(int i=0;i<3;i++){//gets ABS from translated to get expansion values
            if(xyzExpansion[i]<0)xyzExpansion[i]=-xyzExpansion[i];
        }

        int damagingFactor =
                Math.min(powerSetting >> 6,8)+
                Math.min(powerSetting >> 8,24)+
                Math.min(powerSetting >> 12,48)+
                        (powerSetting >> 18);

        ArrayList<ItemStack> itemsToOutput=new ArrayList<>();
        HashSet<Entity> tickedStuff=new HashSet<>();

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
                            if(!GT_Utility.isWearingFullElectroHazmat((EntityLivingBase) entity)) {
                                ((EntityLivingBase) entity).attackEntityFrom(microwaving, damagingFactor);
                            }
                        }
                    }
                }
            }
            aabb.offset(xyzOffsets[0],xyzOffsets[1],xyzOffsets[2]);
            aabb=aabb.expand(xyzExpansion[0],xyzExpansion[1],xyzExpansion[2]);
            inside=false;
            damagingFactor>>=1;
        } while(damagingFactor>0);

        mOutputItems= itemsToOutput.toArray(new ItemStack[itemsToOutput.size()]);

        if(timerValue<=0) {
            mte.getWorld().playSoundEffect(xPos,yPos,zPos, Reference.MODID+":microwave_ding", 1, 1);
            stopMachine();
        }
    }

    @Override
    protected void parametersLoadDefault_EM() {
        powerSetting = POWER_SETTING_DEFAULT;
        timerSetting = TIMER_SETTING_DEFAULT;
        setParameterPairIn_ClearOut(0,false, POWER_SETTING_DEFAULT, TIMER_SETTING_DEFAULT);
    }

    @Override
    protected void parametersInRead_EM() {
        powerSetting = (int) getParameterIn(0, 0);
        timerSetting = (int) getParameterIn(0, 1);
    }

    @Override
    public void parametersOutAndStatusesWrite_EM(boolean machineBusy) {
        double powerParameter = getParameterIn(0, 0);
        if (powerParameter < 300) {
            setStatusOfParameterIn(0, 0, STATUS_TOO_LOW);
        } else if (powerParameter < 1000) {
            setStatusOfParameterIn(0, 0, STATUS_LOW);
        } else if (powerParameter == 1000) {
            setStatusOfParameterIn(0, 0, STATUS_OK);
        } else if (powerParameter == Double.POSITIVE_INFINITY) {
            setStatusOfParameterIn(0, 0, STATUS_TOO_HIGH);
        } else if (Double.isNaN(powerParameter)) {
            setStatusOfParameterIn(0, 0, STATUS_WRONG);
        } else {
            setStatusOfParameterOut(0, 0, STATUS_HIGH);
        }

        double timerParameter = getParameterIn(0, 1);
        if (timerParameter <= 1) {
            setStatusOfParameterIn(0, 1, STATUS_TOO_LOW);
        } else if (timerParameter <= 3000) {
            setStatusOfParameterIn(0, 1, STATUS_OK);
        } else if (Double.isNaN(timerParameter)) {
            setStatusOfParameterIn(0, 1, STATUS_WRONG);
        } else {
            setStatusOfParameterIn(0, 1, STATUS_TOO_HIGH);
        }

        setParameterOut(0, 0, timerValue);
        setParameterOut(0, 1, timerSetting - timerValue);
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if(eSafeVoid) {
            hasBeenPausedThisCycle = true;
        }
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
