package com.detrav.items.behaviours;

import java.util.HashMap;
import java.util.Random;

import com.detrav.items.DetravMetaGeneratedTool01;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.common.GT_Pollution;
import gregtech.common.GT_UndergroundOil;
import gregtech.common.blocks.GT_Block_Ores_Abstract;
import gregtech.common.blocks.GT_TileEntity_Ores;
import gregtech.common.items.behaviors.Behaviour_None;
import gtPlusPlus.core.block.base.BlockBaseOre;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fluids.FluidStack;

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
                FluidStack fStack = GT_UndergroundOil.undergroundOil(aWorld.getChunkFromBlockCoords(aX, aZ), -1);
                addChatMassageByValue(aPlayer,fStack.amount/5000,fStack.getLocalizedName());
                if (!aPlayer.capabilities.isCreativeMode)
                    ((DetravMetaGeneratedTool01)aItem).doDamage(aStack, this.mCosts);
            }
            return true;
        }
        if (aWorld.getBlock(aX, aY, aZ).getMaterial() == Material.rock || aWorld.getBlock(aX, aY, aZ) == GregTech_API.sBlockOres1) {
            if (!aWorld.isRemote) {
                processOreProspecting((DetravMetaGeneratedTool01) aItem, aStack, aPlayer, aWorld.getChunkFromBlockCoords(aX, aZ), aWorld.getTileEntity(aX, aY, aZ),GT_OreDictUnificator.getAssociation(new ItemStack(aWorld.getBlock(aX, aY, aZ), 1, aWorld.getBlockMetadata(aX, aY, aZ))), new Random(aWorld.getSeed() + 3547 * aX + 1327 * aZ + 9973 * aY),40);
                return true;
            }
            return true;
        }
        return false;
    }

    protected void processOreProspecting(DetravMetaGeneratedTool01 aItem, ItemStack aStack, EntityPlayer aPlayer, Chunk aChunk, TileEntity aTileEntity, ItemData tAssotiation, Random aRandom, int chance)//TileEntity aTileEntity)
    {
        aRandom.nextInt();
        if (aTileEntity != null) {
            if (aTileEntity instanceof GT_TileEntity_Ores) {
                GT_TileEntity_Ores gt_entity = (GT_TileEntity_Ores) aTileEntity;
                short meta = gt_entity.getMetaData();
                String name = Materials.getLocalizedNameForItem(GT_LanguageManager.getTranslation("gt.blockores." + meta + ".name"), meta%1000);
                addChatMassageByValue(aPlayer, -1, name);
                if (!aPlayer.capabilities.isCreativeMode)
                    aItem.doDamage(aStack, this.mCosts);
                return;
            }
        } else if (tAssotiation!=null)
        {
            //if (aTileEntity instanceof GT_TileEntity_Ores) {
            try {
                GT_TileEntity_Ores gt_entity = (GT_TileEntity_Ores) aTileEntity;
                String name = tAssotiation.toString();
                addChatMassageByValue(aPlayer, -1, name);
                if (!aPlayer.capabilities.isCreativeMode)
                    aItem.doDamage(aStack, this.mCosts);
                return;
            }
            catch (Exception e)
            {
                addChatMassageByValue(aPlayer, -1, "ERROR, lol ^_^");
            }
            //}
        }else if (aRandom.nextInt(100) < chance) {
            int data = DetravMetaGeneratedTool01.INSTANCE.getToolGTDetravData(aStack).intValue();
            HashMap<String, Integer> ores = new HashMap<String, Integer>();
            for (int x = 0; x < 16; x++)
                for (int z = 0; z < 16; z++) {
                    int ySize = aChunk.getHeightValue(x, z);
                    for (int y = 1; y < ySize; y++) {

                        Block tBlock = aChunk.getBlock(x,y,z);
                        short tMetaID = (short)aChunk.getBlockMetadata(x,y,z);
                        if (tBlock instanceof GT_Block_Ores_Abstract) {
                            TileEntity tTileEntity = aChunk.getTileEntityUnsafe(x,y,z);
                            if ((tTileEntity!=null)
                                    && (tTileEntity instanceof GT_TileEntity_Ores)
                                    && ((GT_TileEntity_Ores) tTileEntity).mNatural == true) {
                                tMetaID = (short)((GT_TileEntity_Ores) tTileEntity).getMetaData();
                                try {
                                    String name = Materials.getLocalizedNameForItem(
                                    		GT_LanguageManager.getTranslation(tBlock.getUnlocalizedName() + "." + tMetaID + ".name"), tMetaID%1000);
                                    if (name.startsWith("Small")) if (data != 1) continue;
                                    if (name.startsWith("Small")) if(data!=1) continue;
                                    if (!ores.containsKey(name))
                                        ores.put(name, 1);
                                    else {
                                        int val = ores.get(name);
                                        ores.put(name, val + 1);
                                    }
                                }
                                catch(Exception e) {
                                    String name = tBlock.getUnlocalizedName() + ".";
                                    if (name.contains(".small.")) if (data != 1) continue;
                                    if (name.startsWith("Small")) if(data!=1) continue;
                                    if (!ores.containsKey(name))
                                        ores.put(name, 1);
                                    else {
                                        int val = ores.get(name);
                                        ores.put(name, val + 1);
                                    }
                                }
                            }
                        } else if (tBlock instanceof BlockBaseOre) {
                        	String name = tBlock.getLocalizedName();
                        	if (!ores.containsKey(name))
                                ores.put(name, 1);
                            else {
                                int val = ores.get(name);
                                ores.put(name, val + 1);
                            }
                        	
                        } else if (data == 1) {
                            tAssotiation = GT_OreDictUnificator.getAssociation(new ItemStack(tBlock, 1, tMetaID));
                            if ((tAssotiation != null) && (tAssotiation.mPrefix.toString().startsWith("ore"))) {
                                try {
                                    try {
                                        tMetaID = (short)tAssotiation.mMaterial.mMaterial.mMetaItemSubID;
                                        
                                        String name = Materials.getLocalizedNameForItem(GT_LanguageManager.getTranslation(
                                                "gt.blockores." + tMetaID + ".name"), tMetaID%1000);
                                        if (!ores.containsKey(name))
                                            ores.put(name, 1);
                                        else {
                                            int val = ores.get(name);
                                            ores.put(name, val + 1);
                                        }
                                    } catch (Exception e1) {
                                        String name = tAssotiation.toString();
                                        if (!ores.containsKey(name))
                                            ores.put(name, 1);
                                        else {
                                            int val = ores.get(name);
                                            ores.put(name, val + 1);
                                        }
                                    }
                                }
                                catch (Exception e)
                                {

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
            if(total==0)
            {
                addChatMassageByValue(aPlayer,0,null);
            }
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

    public static int getPolution(World aWorld, int aX, int aZ)
    {
        return GT_Pollution.getPollution(aWorld.getChunkFromBlockCoords(aX, aZ));
    }
}