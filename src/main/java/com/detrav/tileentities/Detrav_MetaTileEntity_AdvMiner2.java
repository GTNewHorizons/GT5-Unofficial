package com.detrav.tileentities;

/**
 * Created by Detrav on 13.12.2016.
 */
import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Ores_Abstract;
import gregtech.common.blocks.GT_TileEntity_Ores;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;

public class Detrav_MetaTileEntity_AdvMiner2 extends GT_MetaTileEntity_MultiBlockBase {

    private final ArrayList<ChunkPosition> mMineList = new ArrayList();

    public Detrav_MetaTileEntity_AdvMiner2(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public Detrav_MetaTileEntity_AdvMiner2(String aName) {
        super(aName);
    }

    public String[] getDescription() {
        return new String[]{
                "Controller Block for the Detrav Advanced Miner II",
                "Default size is one chunk, use circuit configuration",
                "to increase the size, {config}*2 + 1 chunks"
                "Size(WxHxD): 3x7x3, Controller (Front middle bottom)",
                "3x1x3 Base of Solid Steel Casings",
                "1x3x1 Solid Steel Casing pillar (Center of base)",
                "1x3x1 Steel Frame Boxes (Each Steel pillar side and on top)",
                "1x Input Hatch (Any bottom layer casing)",
                "1x Output Bus (Any bottom layer casing)",
                "1x Maintenance Hatch (Any bottom layer casing)",
                "1x MV+ Energy Hatch (Any bottom layer casing)"};
    }

    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[16], new GT_RenderedTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_ADVMINER2_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_ADVMINER2)};
        }
        return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[16]};
    }

    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "AdvMiner2.png");
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {
        
        if(!moveFirst())
            return false;
        
        if (mInputHatches == null || mInputHatches.get(0).mFluid == null || mInputHatches.get(0).mFluid.getFluid().getID() != ItemList.sDrillingFluid.getID()) {
            return false;
        }
        FluidStack tFluid = mInputHatches.get(0).mFluid.copy();
        if (tFluid == null) {
            return false;
        }
        if (tFluid.amount < 100) {
            return false;
        }
        tFluid.amount = 100;
        depleteInput(tFluid);
        long tVoltage = getMaxInputVoltage();

        if (getBaseMetaTileEntity().getRandomNumber(10) <= 4) {
            if (mMineList.isEmpty()) {
                int x = getXCurrent();
                int z = getZCurrent();
                for(int yLevel = getBaseMetaTileEntity().getYCoord() - 1; yLevel>1; yLevel --)
                {
                    Block tBlock = c.getBlock(x,yLevel,z);
                    int tMetaID = c.getBlockMetadata(x,yLevel,z);
                    if (tBlock instanceof GT_Block_Ores_Abstract) {
                        TileEntity tTileEntity = c.getTileEntityUnsafe(x,yLevel,z);
                        if ((tTileEntity!=null)
                                && (tTileEntity instanceof GT_TileEntity_Ores)
                                && ((GT_TileEntity_Ores) tTileEntity).mNatural == true
                                && !mMineList.contains(new ChunkPosition(tTileEntity.xCoord, tTileEntity.yCoord, tTileEntity.zCoord))) {
                            mMineList.add(new ChunkPosition(tTileEntity.xCoord, tTileEntity.yCoord, tTileEntity.zCoord));
                        }
                    }
                    else {
                        ItemData tAssotiation = GT_OreDictUnificator.getAssociation(new ItemStack(tBlock, 1, tMetaID));
                        if ((tAssotiation != null) && (tAssotiation.mPrefix.toString().startsWith("ore"))) {
                            ChunkPosition cp = new ChunkPosition(c.xPosition * 16 + x, yLevel, c.zPosition * 16 + z);
                            if (!mMineList.contains(cp)) {
                                mMineList.add(cp);
                            }
                        }
                    }
                }       
            }
        

            ArrayList<ItemStack> tDrops = new ArrayList();
            Block tMineBlock = null;
            ChunkPosition mle = null;;
            while ((tMineBlock==null || tMineBlock == Blocks.air) && !mMineList.isEmpty()) {
                mle = mMineList.get(0);
                mMineList.remove(0);
                tMineBlock = getBaseMetaTileEntity().getBlock(mle.chunkPosX, mle.chunkPosY, mle.chunkPosZ);
            }

            if (tMineBlock!=null && tMineBlock!=Blocks.air) {
                int metadata = getBaseMetaTileEntity().getMetaID(mle.chunkPosX, mle.chunkPosY, mle.chunkPosZ);
                boolean silkTouch = tMineBlock.canSilkHarvest(getBaseMetaTileEntity().getWorld(), null, mle.chunkPosX, mle.chunkPosY, mle.chunkPosZ, metadata);
                if (silkTouch){
                    ItemStack IS = new ItemStack(tMineBlock);
                    IS.setItemDamage(metadata);
                    IS.stackSize=1;
                    tDrops.add(IS);
                }
                else{
                    tDrops = tMineBlock.getDrops(getBaseMetaTileEntity().getWorld(), mle.chunkPosX, mle.chunkPosY, mle.chunkPosZ, metadata, 1);
                }

                getBaseMetaTileEntity().getWorld().setBlock(mle.chunkPosX, mle.chunkPosY , mle.chunkPosZ,Blocks.dirt);
                if (!tDrops.isEmpty()) {
                    ItemData tData = GT_OreDictUnificator.getItemData(tDrops.get(0).copy());
                    if (tData.mPrefix != OrePrefixes.crushed && tData.mMaterial.mMaterial != Materials.Oilsands) {

                        GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sMaceratorRecipes.findRecipe(getBaseMetaTileEntity(), false, tVoltage, null, tDrops.get(0).copy());
                        if (tRecipe != null) {
                            this.mOutputItems = new ItemStack[tRecipe.mOutputs.length];
                            for (int g = 0; g < mOutputItems.length; g++) {
                                mOutputItems[g] = tRecipe.mOutputs[g].copy();
                                if (getBaseMetaTileEntity().getRandomNumber(10000) < tRecipe.getOutputChance(g)) {
                                    mOutputItems[g].stackSize *= getBaseMetaTileEntity().getRandomNumber(4) + 1;
                                }
                            }
                        } else {
                            this.mOutputItems = new ItemStack[tDrops.size()];
                            for (int g = 0; g < mOutputItems.length; g++) {
                                mOutputItems[g] = tDrops.get(g).copy();
                            }
                        }
                    } else {
                        this.mOutputItems = null;
                        ItemStack[] tStack = new ItemStack[tDrops.size()];
                        for (int j = 0; j < tDrops.size(); j++) {
                            tStack[j] = tDrops.get(j).copy();
                            tStack[j].stackSize = tStack[j].stackSize * (getBaseMetaTileEntity().getRandomNumber(4) + 1);
                        }
                        mOutputItems = tStack;
                    }
                }
            }
            else
            {
                if (mEnergyHatches.size() > 0 && mEnergyHatches.get(0).getEUVar() > (512 + getMaxInputVoltage() * 4)) {
                    moveNext();
                }
            }
        }

        byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
        this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;
        int tEU = 48;
        int tDuration = 24;
        if (tEU <= 16) {
            this.mEUt = (tEU * (1 << tTier - 1) * (1 << tTier - 1));
            this.mMaxProgresstime = (tDuration / (1 << tTier - 1));
        } else {
            this.mEUt = tEU;
            this.mMaxProgresstime = tDuration;
            while (this.mEUt <= gregtech.api.enums.GT_Values.V[(tTier - 1)]) {
                this.mEUt *= 4;
                this.mMaxProgresstime /= 2;
            }
        }
        if (this.mEUt > 0) {
            this.mEUt = (-this.mEUt);
        }
        this.mMaxProgresstime = Math.max(1, this.mMaxProgresstime);
        return true;
    }

    private boolean moveFirst() {
        int circuit_config = 1;
        if (mInventory[1] == null && !mInventory[1].getUnlocalizedName().startsWith("gt.integrated_circuit")) return false;

        circuit_config = mInventory[1].getItemDamage();
        
        circuit_config *= 2;
        circuit_config++;
        //in here if circuit is empty set data to chunk
        boolean configurated = false;

        NBTTagCompound aNBT = aCircuit.getTagCompound();
        if(aNBT == null) {
            aCircuit = new NBTTagCompound();
            NBTTagCompound detravPosition = new NBTTagCompound();
            aNBT.setTag("DetravPosition", detravPosition);
            aStack.setTagCompound(aNBT);
        }
        
        NBTTagCompound detravPosition = aNBT.getCompoundTag("DetravPosition");
        if (detravPosition == null ) {
            detravPosition = new NBTTagCompound();
            aNBT.setTag("DetravPosition", detravPosition);
        }
        configurated = detravPosition.hasKey("Configurated") && detravPosition.get("Configurated");
        
        if(!configurated)
        {
            configurated = true;
            int x_from = getBaseMetaTileEntity().getXCoord() -circuit_config * 16;
            int x_to = getBaseMetaTileEntity().getXCoord() +circuit_config * 16;
            int x_current = x_from;
            int z_from = getBaseMetaTileEntity().getZCoord() - circuit_config * 16;
            int z_to = getBaseMetaTileEntity().getZCoord() + circuit_config * 16;
            int z_current = z_from;

            detravPosition.set("XFrom",x_from);
            detravPosition.set("XTo",x_to);
            detravPosition.set("XCurrent",x_current);
            detravPosition.set("ZFrom",z_from);
            detravPosition.set("ZTo",z_to);
            detravPosition.set("ZCurrent",z_current);
        }
        if(detravPosition.hasKey("Finished"))
            configurated = !detravPosition.get("Finished");
        return configurated;
    }

    private int getXCurrent()
    {
        int fakeResult = getBaseMetaTileEntity().getXCoord();
        if(mInventory[1] == null) return fakeResult;
        ItemStack aCircuit = mInventory[1];
        NBTTagCompound aNBT = aCircuit.getTagCompound();
        if(aNBT == null) return fakeResultfalse;
        NBTTagCompound detravPosition = aNBT.getCompoundTag("DetravPosition");
        if (detravPosition == null ) return fakeResult;

        if(detravPosition.hasKey("Finished"))
            if(detravPosition.get("Finished"))
                return fakeResult;

        return detravPosition.get("XCurrent");
    }

    private int getZCurrent()
    {
        int fakeResult = getBaseMetaTileEntity().getZCoord();
        if(mInventory[1] == null) return fakeResult;
        ItemStack aCircuit = mInventory[1];
        NBTTagCompound aNBT = aCircuit.getTagCompound();
        if(aNBT == null) return fakeResultfalse;
        NBTTagCompound detravPosition = aNBT.getCompoundTag("DetravPosition");
        if (detravPosition == null ) return fakeResult;

        if(detravPosition.hasKey("Finished"))
            if(detravPosition.get("Finished"))
                return fakeResult;

        return detravPosition.get("ZCurrent");
    }


    private boolean moveNext() {
        if(mInventory[1] == null) return false;
        ItemStack aCircuit = mInventory[1];
        NBTTagCompound aNBT = aCircuit.getTagCompound();
        if(aNBT == null) return false;
        NBTTagCompound detravPosition = aNBT.getCompoundTag("DetravPosition");
        if (detravPosition == null ) return false;

        if(detravPosition.hasKey("Finished"))
            if(detravPosition.get("Finished"))
                return false;
        
        int x_from = detravPosition.get("XFrom");
        int z_from = detravPosition.get("ZFrom");
        int x_to = detravPosition.get("XTo");
        int z_to = detravPosition.get("ZTo");
        int x_current = detravPosition.get("XCurrent");
        int z_current = detravPosition.get("ZCurrent");

        if(z_current < z_to)
            z_current++;
        else
        {
            if(x_current < x_to)
            {
                z_current = z_from;
                x_current++;
            }
            else
            {
                detravPosition.set("Finished",true);
                return false;
            }
        }

        detravPosition.set("XCurrent",x_current);
        detravPosition.get("ZCurrent",z_current);
        
        return true;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
        int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if ((xDir + i != 0) || (zDir + j != 0)) {
                    IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, 0, zDir + j);
                    if ((!addMaintenanceToMachineList(tTileEntity, 16)) && (!addInputToMachineList(tTileEntity, 16)) && (!addOutputToMachineList(tTileEntity, 16)) && (!addEnergyInputToMachineList(tTileEntity, 16))) {
                        if (aBaseMetaTileEntity.getBlockOffset(xDir + i, 0, zDir + j) != GregTech_API.sBlockCasings2) {
                            return false;
                        }
                        if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, 0, zDir + j) != 0) {
                            return false;
                        }
                    }
                }
            }
        }
        for (int y = 1; y < 4; y++) {
            if (aBaseMetaTileEntity.getBlockOffset(xDir, y, zDir) != GregTech_API.sBlockCasings2) {
                return false;
            }
            if (aBaseMetaTileEntity.getBlockOffset(xDir + 1, y, zDir) != GregTech_API.sBlockMachines) {
                return false;
            }
            if (aBaseMetaTileEntity.getBlockOffset(xDir - 1, y, zDir) != GregTech_API.sBlockMachines) {
                return false;
            }
            if (aBaseMetaTileEntity.getBlockOffset(xDir, y, zDir + 1) != GregTech_API.sBlockMachines) {
                return false;
            }
            if (aBaseMetaTileEntity.getBlockOffset(xDir, y, zDir - 1) != GregTech_API.sBlockMachines) {
                return false;
            }
            if (aBaseMetaTileEntity.getBlockOffset(xDir, y + 3, zDir) != GregTech_API.sBlockMachines) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerTick(ItemStack aStack) {
        return 0;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new Detrav_MetaTileEntity_AdvMiner2(this.mName);
    }

}
