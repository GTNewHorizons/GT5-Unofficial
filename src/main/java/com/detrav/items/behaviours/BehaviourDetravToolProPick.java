package com.detrav.items.behaviours;

import com.detrav.items.DetravMetaGeneratedTool01;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.util.GT_LanguageManager;
import gregtech.common.GT_Proxy;
import gregtech.common.blocks.GT_TileEntity_Ores;
import gregtech.common.items.behaviors.Behaviour_None;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by wital_000 on 19.03.2016.
 */
public class BehaviourDetravToolProPick extends Behaviour_None {

    static final String[] foundTexts = new String[]{
            "Found nothing of interest",        //0
            "Found Traces of ",                 //1-9
            "Found a small sample of ",         //10-29
            "Found a medium sample of ",        //30-59
            "Found a large sample of ",         //60-99
            "Found a very large sample of ",    //100-**
            "Found "
    };
    protected final int mCosts;

    public BehaviourDetravToolProPick(int aCosts) {
        mCosts = aCosts;
    }

    public boolean onItemUse(GT_MetaBase_Item aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int aSide, float hitX, float hitY, float hitZ) {

        if(aWorld.getBlock(aX,aY,aZ) == Blocks.bedrock)
        {
            if (!aWorld.isRemote) {
                FluidStack fStack = getUndergroundOil(aWorld,aX,aZ);
                addChatMassageByValue(aPlayer,fStack.amount/5000,fStack.getLocalizedName());
                if (!aPlayer.capabilities.isCreativeMode)
                    ((DetravMetaGeneratedTool01)aItem).doDamage(aStack, this.mCosts);
            }
            return true;
        }
        if (aWorld.getBlock(aX, aY, aZ).getMaterial() == Material.rock || aWorld.getBlock(aX, aY, aZ) == GregTech_API.sBlockOres1) {
            if (!aWorld.isRemote) {
                processOreProspecting((DetravMetaGeneratedTool01) aItem, aStack, aPlayer, aWorld.getChunkFromBlockCoords(aX, aZ), aWorld.getTileEntity(aX, aY, aZ), new Random(aWorld.getSeed() + 3547 * aX + 1327 * aZ + 9973 * aY));
                return true;
            }
            return true;
        }
        return false;
    }

    private void processOreProspecting(DetravMetaGeneratedTool01 aItem, ItemStack aStack, EntityPlayer aPlayer, Chunk aChunk, TileEntity aTileEntity, Random aRandom)//TileEntity aTileEntity)
    {
        aRandom.nextInt();
        if (aTileEntity != null) {
            if (aTileEntity instanceof GT_TileEntity_Ores) {
                GT_TileEntity_Ores gt_entity = (GT_TileEntity_Ores) aTileEntity;
                String name = GT_LanguageManager.getTranslation("gt.blockores." + gt_entity.getMetaData() + ".name");
                addChatMassageByValue(aPlayer, -1, name);
                if (!aPlayer.capabilities.isCreativeMode)
                    aItem.doDamage(aStack, this.mCosts);
                return;
            }
        } else if (aRandom.nextInt(10) < 4) {
            int data = DetravMetaGeneratedTool01.INSTANCE.getToolGTDetravData(aStack).intValue();
            HashMap<String, Integer> ores = new HashMap<String, Integer>();
            for (int x = 0; x < 16; x++)
                for (int z = 0; z < 16; z++) {
                    int ySize = aChunk.getHeightValue(x, z);
                    for (int y = 1; y < ySize; y++) {
                        Block b = aChunk.getBlock(x, y, z);
                        if (b == GregTech_API.sBlockOres1) {
                            TileEntity entity = aChunk.getTileEntityUnsafe(x, y, z);
                            if (entity != null) {
                                GT_TileEntity_Ores gt_entity = (GT_TileEntity_Ores) entity;
                                String name = GT_LanguageManager.getTranslation(
                                        b.getUnlocalizedName() + "." + gt_entity.getMetaData() + ".name");
                                if (name.startsWith("Small")) if(data!=1) continue;
                                if (!ores.containsKey(name))
                                    ores.put(name, 1);
                                else {
                                    int val = ores.get(name);
                                    ores.put(name, val + 1);
                                }
                            }
                        }
                    }
                }
            int total = 0;
            for (String key : ores.keySet()) {
                int value = ores.get(key);
                total+=value;
               addChatMassageByValue(aPlayer,value,key);
            }
            addChatMassageByValue(aPlayer,total,"Total");
            if (!aPlayer.capabilities.isCreativeMode)
                aItem.doDamage(aStack, this.mCosts);
            return;
        }
        addChatMassageByValue(aPlayer,0,null);
    }

    void addChatMassageByValue(EntityPlayer aPlayer, int value, String name) {
        if(name == "Total") return;
        if (value < 0) {
            aPlayer.addChatMessage(new ChatComponentText(foundTexts[6] + name));
        } else if (value < 1) {
            aPlayer.addChatMessage(new ChatComponentText(foundTexts[0]));
        } else if (value < 10)
            aPlayer.addChatMessage(new ChatComponentText(foundTexts[1] + name));
        else if (value < 30)
            aPlayer.addChatMessage(new ChatComponentText(foundTexts[2] + name));
        else if (value < 60)
            aPlayer.addChatMessage(new ChatComponentText(foundTexts[3] + name));
        else if (value < 100)
            aPlayer.addChatMessage(new ChatComponentText(foundTexts[4] + name));
        else
            aPlayer.addChatMessage(new ChatComponentText(foundTexts[5] + name));
    }

    public static FluidStack getUndergroundOil(World aWorld, int aX, int aZ) {
        Random tRandom = new Random((aWorld.getSeed() + (aX / 96) + (7 * (aZ / 96))));
        int oil = tRandom.nextInt(3);
        double amount = tRandom.nextInt(50) + tRandom.nextDouble();
        oil = tRandom.nextInt(4);
//		System.out.println("Oil: "+(aX/96)+" "+(aZ/96)+" "+oil+" "+amount);
//		amount = 40;
        Fluid tFluid = null;
        switch(oil) {
            case 0:
                tFluid = Materials.NatruralGas.mGas;
                break;
            case 1:
                tFluid = Materials.OilLight.mFluid;
                break;
            case 2:
                tFluid = Materials.OilMedium.mFluid;
                break;
            case 3:
                tFluid = Materials.OilHeavy.mFluid;
                break;
            default:
                tFluid = Materials.Oil.mFluid;
        }

        int tAmount = (int)(Math.pow(amount, 5.0D) / 100.0D);
        ChunkPosition tPos = new ChunkPosition(aX / 16, 1, aZ / 16);
        if(GT_Proxy.chunkData.containsKey(tPos)) {
            int[] tInts = (int[])GT_Proxy.chunkData.get(tPos);
            if(tInts.length > 0 && tInts[0] > 0) {
                tAmount = tInts[0];
            }
        }
        tAmount -= 5;

        return new FluidStack(tFluid, tAmount);
    }
}