package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.auxiliary.Reference;
import com.github.technus.tectech.thing.metaTileEntity.iConstructible;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
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
public class GT_MetaTileEntity_TM_microwave extends GT_MetaTileEntity_MultiblockBase_EM  implements iConstructible {
    private int powerSetting = 1000;
    private int timerSetting = 0;
    private int timerValue = 0;

    //region Structure
    //use multi A energy inputs, use less power the longer it runs
    private static final String[][] shape = new String[][]{
            {"00000", "00000", "00.00", "0   0",},
            {"0C0", "0C0", "0C0", " 000 ",},
            {"0C0", "0C0", "0C0", " 000 ",},
            {"0C0", "0C0", "0C0", " 000 ",},
            {"00000", "00000", "00000", "0   0",},
    };
    private static final Block[] blockType = new Block[]{sBlockCasings4};
    private static final byte[] blockMeta = new byte[]{1};

    private static final String[] addingMethods = new String[]{"addClassicToMachineList"};
    private static final byte[] casingTextures = new byte[]{49};
    private static final Block[] blockTypeFallback = new Block[]{sBlockCasings4};
    private static final byte[] blockMetaFallback = new byte[]{1};
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
        }
        return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[49]};
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return aFacing >= 2;
    }

    @Override
    public boolean EM_checkMachine(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return EM_StructureCheckAdvanced(shape, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 2, 2, 0);
    }

    @Override
    public void construct(int qty) {
        StructureBuilder(shape, blockType, blockMeta,2, 2, 0, getBaseMetaTileEntity());
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.bassMark,
                "High Frequency Oven",
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "From live to done in seconds!",
                EnumChatFormatting.BLUE + "I said nuke the chinese, I meant microwave supper!",
        };
    }

    @Override
    public boolean EM_checkRecipe(ItemStack itemStack) {
        if(powerSetting<=300 || eParamsInStatus[0] == PARAM_TOO_HIGH || timerSetting<=0 || timerSetting>3000) return false;
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
    public void EM_outputFunction() {
        timerValue--;
        IGregTechTileEntity mte=getBaseMetaTileEntity();
        int xDirShift = ForgeDirection.getOrientation(mte.getBackFacing()).offsetX*2;
        int zDirShift = ForgeDirection.getOrientation(mte.getBackFacing()).offsetZ*2;
        int toMapTop=(256-mte.getYCoord())/3;
        float xPos=mte.getXCoord()+0.5f;
        float yPos=mte.getYCoord()+0.5f;
        float zPos=mte.getZCoord()+0.5f;
        ArrayList<ItemStack> itemsToOutput=new ArrayList<>();
        HashSet<Entity> tickedStuff=new HashSet<>();

        AxisAlignedBB aabb=AxisAlignedBB.getBoundingBox(xPos-1.5+xDirShift,yPos-.5,zPos-1.5+zDirShift,xPos+1.5+xDirShift,yPos+2.5,zPos+1.5+zDirShift);
        allAABB:
        for(int i=0;i<=toMapTop;i++) {
            for (Object entity : mte.getWorld().getEntitiesWithinAABBExcludingEntity(null, aabb)) {
                float damagingFactor=powerSetting >> (9+i);
                if(damagingFactor<=0) break allAABB;
                if (entity instanceof Entity) {
                    if(tickedStuff.add((Entity)entity)) {
                        if (i == 0 && entity instanceof EntityItem) {
                            GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sMicrowaveRecipes.findRecipe(
                                    mte, null, true, 128, null, null, new ItemStack[]{((EntityItem) entity).getEntityItem()});
                            if (tRecipe == null || tRecipe.mInputs[0].stackSize != 1) {
                                itemsToOutput.add(((EntityItem) entity).getEntityItem());
                            } else {
                                ItemStack newStuff = tRecipe.getOutput(0).copy();
                                newStuff.stackSize = ((EntityItem) entity).getEntityItem().stackSize;
                                itemsToOutput.add(newStuff);
                            }
                            ((EntityItem) entity).setDead();
                        } else if (entity instanceof EntityLiving) {
                            ((EntityLiving) entity).attackEntityFrom(microwaving, damagingFactor);
                        } else if (entity instanceof EntityPlayerMP) {
                            ((EntityPlayerMP) entity).attackEntityFrom(microwaving, damagingFactor);
                        }
                    }
                }
            }
            aabb.offset(0,3,0);
            aabb.minX-=.5f;
            aabb.maxX+=.5f;
            aabb.minZ-=.5f;
            aabb.maxZ+=.5f;
        }
        mOutputItems=itemsToOutput.toArray(new ItemStack[0]);
        if(timerValue<=0) {
            mte.getWorld().playSoundEffect(xPos,yPos,zPos, Reference.MODID+":microwave_ding", 1, 1);
            stopMachine();
        }
    }

    @Override
    public void EM_checkParams() {
        if (eParamsIn[0] <= 300)
            eParamsInStatus[0] = PARAM_TOO_LOW;
        else if (eParamsIn[0] < 1000)
            eParamsInStatus[0] = PARAM_LOW;
        else if (eParamsIn[0] == 1000)
            eParamsInStatus[0] = PARAM_OK;
        else if (eParamsIn[0] <= Integer.MAX_VALUE)
            eParamsInStatus[0] = PARAM_HIGH;
        else eParamsInStatus[0] = PARAM_TOO_HIGH;

        if (eParamsIn[10] <= 1)
            eParamsInStatus[10] = PARAM_TOO_LOW;
        else if (eParamsIn[10] <= 3000)
            eParamsInStatus[10] = PARAM_OK;
        else eParamsInStatus[10] = PARAM_TOO_HIGH;

        powerSetting = (int)eParamsIn[0];
        timerSetting = (int)eParamsIn[10];

        eParamsOut[0] = timerValue;

        if (eSafeVoid) {
            eSafeVoid=false;
            int timerValueBackup=timerValue;
            stopMachine();
            timerValue=timerValueBackup;
        }
    }

    @Override
    protected void EM_stopMachine() {
        timerValue=0;
    }

    @Override
    protected void EM_workJustGotDisabled() {
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
    public void doExplosion(long aExplosionPower) {
        explodeMultiblock();
    }//Redirecting to explodemultiblock
}
