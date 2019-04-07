package com.detrav.items.behaviours;

import com.detrav.DetravScannerMod;
import com.detrav.items.DetravMetaGeneratedTool01;
import com.detrav.utils.BartWorksHelper;
import com.detrav.utils.GTppHelper;
import cpw.mods.fml.common.Loader;
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
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fluids.FluidStack;

import java.util.HashMap;
import java.util.SplittableRandom;

/**
 * Created by wital_000 on 19.03.2016.
 */
public class BehaviourDetravToolProPick extends Behaviour_None {

    static final String[] foundTexts = new String[]{
            "Found nothing of interest",        //0
            " traces.",                 //1-9
            " small sample.",         //10-29
            " medium sample.",        //30-59
            " large sample.",         //60-99
            " very large sample.",    //100-**
            "Found "
    };

    static final String[] DISTANCETEXTS = new String[]{
            " next to you,",     // 0 chunks away
            " close to you,",    // 1-2 chunks aways
            " at medium range,", // 3 - 5 chunks away
            " at long range,",   // 6 -8 chunks away
            " far away,",        // 9 + chunks away
    };

    static final int[] DISTANCEINTS = new int[] {
            0,
            4,
            25,
            64,
    };
    int distTextIndex;

    HashMap<String, Integer> ores;
    int badluck;

    protected final int mCosts;

    public BehaviourDetravToolProPick(int aCosts) {
        mCosts = aCosts;
    }
    
    public boolean onItemUse(GT_MetaBase_Item aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int aSide, float hitX, float hitY, float hitZ) {
    	
    	SplittableRandom aRandom =new SplittableRandom();
        int chance = ((1+aStack.getItemDamage())*8) > 100 ? 100 :(1+aStack.getItemDamage())*8;
        
    	if (aWorld.isRemote)
    		 return false;
    	
        if(aWorld.getBlock(aX,aY,aZ) == Blocks.bedrock)
        {
            if (!aWorld.isRemote && aRandom.nextInt(100) < chance) {
                FluidStack fStack = GT_UndergroundOil.undergroundOil(aWorld.getChunkFromBlockCoords(aX, aZ), -1);
                addChatMassageByValue(aPlayer,fStack.amount/2,"a Fluid");//fStack.getLocalizedName());
            	/*boolean fluid = GT_UndergroundOil.undergroundOil(aWorld.getChunkFromBlockCoords(aX, aZ), -1)!=null &&GT_UndergroundOil.undergroundOil(aWorld.getChunkFromBlockCoords(aX, aZ), -1).getFluid()!=null;
            	if (fluid)
            		aPlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN+"You found some liquid."));
            	else
            		aPlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED+"You found no liquid."));*/
                if (!aPlayer.capabilities.isCreativeMode)
                    ((DetravMetaGeneratedTool01)aItem).doDamage(aStack, this.mCosts);
            }
            return true;
        }
        if (aWorld.getBlock(aX, aY, aZ).getMaterial() == Material.rock || aWorld.getBlock(aX, aY, aZ).getMaterial() == Material.ground || aWorld.getBlock(aX, aY, aZ) == GregTech_API.sBlockOres1) {
            if (!aWorld.isRemote) {
                prospectChunks( (DetravMetaGeneratedTool01) aItem, aStack, aPlayer, aWorld, aX, aY, aZ, aRandom, chance );
            }
            return true;
        }
        return false;
    }

    protected void prospectChunks(GT_MetaBase_Item aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, SplittableRandom aRandom, int chance)
    {
        int bX = aX;
        int bZ = aZ;
        
        badluck = 0;
        ores = new HashMap<String, Integer>();
        
        int range = ((DetravMetaGeneratedTool01)aItem).getHarvestLevel(aStack, "")/2+(aStack.getItemDamage()/4);
        if ((range % 2) == 0 ) {
            range += 1;   // kinda not needed here, divide takes it out, but we put it back in with the range+1 in the loop
        }
        range = range/2; // Convert range from diameter to radius
        
        aPlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD+"Prospecting at " + EnumChatFormatting.BLUE + "(" + bX + ", " + bZ + ")" ));
        for (int x = -(range); x<(range+1);++x){
            aX=bX+(x*16);
            for (int z = -(range); z<(range+1);++z) {
        
                aZ=bZ+(z*16);
                int dist = x*x + z*z;
        
                for( distTextIndex = 0; distTextIndex < DISTANCEINTS.length; distTextIndex++ ) {
                    if ( dist <= DISTANCEINTS[distTextIndex] ) {
                        break;
                    }
                }
                if (DetravScannerMod.DEBUGBUILD)
                    aPlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.YELLOW+"Chunk at "+ aX +"|"+aZ+" to "+(aX+16)+"|"+(aZ+16) + DISTANCETEXTS[distTextIndex]));
                processOreProspecting((DetravMetaGeneratedTool01) aItem, aStack, aPlayer, aWorld.getChunkFromBlockCoords(aX, aZ), aWorld.getTileEntity(aX, aY, aZ),GT_OreDictUnificator.getAssociation(new ItemStack(aWorld.getBlock(aX, aY, aZ), 1, aWorld.getBlockMetadata(aX, aY, aZ))), aRandom, chance);
            }
        }
        
        for (String key : ores.keySet()) {
            int value = ores.get(key);
           addChatMassageByValue(aPlayer,value,key);
        }
        
        if( badluck == 0) {
            aPlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.WHITE + "All chunks scanned successfully!"));
        } else {
            aPlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.WHITE + "Failed on " + badluck + " chunks. Better luck next time!"));
        }
    }

    // Used by Electric scanner when scanning the chunk whacked by the scanner. 100% chance find rate
    protected void prospectSingleChunk(GT_MetaBase_Item aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ )
    {
        ores = new HashMap<String, Integer>();
        aPlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GOLD+"Prospecting at " + EnumChatFormatting.BLUE + "(" + aX + ", " + aZ + ")" ));
        processOreProspecting((DetravMetaGeneratedTool01) aItem, aStack, aPlayer, aWorld.getChunkFromBlockCoords(aX, aZ), aWorld.getTileEntity(aX, aY, aZ),GT_OreDictUnificator.getAssociation(new ItemStack(aWorld.getBlock(aX, aY, aZ), 1, aWorld.getBlockMetadata(aX, aY, aZ))), new SplittableRandom(), 1000);
        
        for (String key : ores.keySet()) {
            int value = ores.get(key);
            addChatMassageByValue(aPlayer,value,key);
        }
    }

    protected void processOreProspecting(DetravMetaGeneratedTool01 aItem, ItemStack aStack, EntityPlayer aPlayer, Chunk aChunk, TileEntity aTileEntity, ItemData tAssotiation, SplittableRandom aRandom, int chance)//TileEntity aTileEntity)
    {
        if (aTileEntity != null) {
            if (aTileEntity instanceof GT_TileEntity_Ores) {
                GT_TileEntity_Ores gt_entity = (GT_TileEntity_Ores) aTileEntity;
                short meta = gt_entity.getMetaData();
                String name = Materials.getLocalizedNameForItem(GT_LanguageManager.getTranslation("gt.blockores." + meta + ".name"), meta%1000);
                addOreToHashMap(name, aPlayer);
                if (!aPlayer.capabilities.isCreativeMode)
                    aItem.doDamage(aStack, this.mCosts);
                return;
            }
        } else if (tAssotiation!=null){
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
        }else if (aRandom.nextInt(100) < chance) {
            int data = DetravMetaGeneratedTool01.INSTANCE.getToolGTDetravData(aStack).intValue();
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
                                    addOreToHashMap(name, aPlayer);
                                }
                                catch(Exception e) {
                                    String name = tBlock.getUnlocalizedName() + ".";
                                    if (name.contains(".small.")) if (data != 1) continue;
                                    if (name.startsWith("Small")) if(data!=1) continue;
                                    addOreToHashMap(name, aPlayer);
                                }
                            }
                        } else if (Loader.isModLoaded("miscutils") && GTppHelper.isGTppBlock(tBlock) ) {
                            String name = GTppHelper.getGTppVeinName(tBlock);
                            if (!name.isEmpty())
                                addOreToHashMap(name, aPlayer);
                        } else if (Loader.isModLoaded("bartworks") && BartWorksHelper.isOre(tBlock)){
                                addOreToHashMap(GT_LanguageManager.getTranslation("bw.blockores.01." + ((BartWorksHelper.getMetaFromBlock(aChunk,x,y,z,tBlock))*-1) + ".name"), aPlayer);
                        } else if (data == 1) {
                            tAssotiation = GT_OreDictUnificator.getAssociation(new ItemStack(tBlock, 1, tMetaID));
                            if ((tAssotiation != null) && (tAssotiation.mPrefix.toString().startsWith("ore"))) {
                                try {
                                    try {
                                        tMetaID = (short)tAssotiation.mMaterial.mMaterial.mMetaItemSubID;
                                        
                                        String name = Materials.getLocalizedNameForItem(GT_LanguageManager.getTranslation(
                                                "gt.blockores." + tMetaID + ".name"), tMetaID%1000);
                                        addOreToHashMap(name, aPlayer);
                                    } catch (Exception e1) {
                                        String name = tAssotiation.toString();
                                        addOreToHashMap(name, aPlayer);
                                    }
                                }
                                catch (Exception e)
                                {

                                }
                            }
                        }

                    }
                }

            if (!aPlayer.capabilities.isCreativeMode)
                aItem.doDamage(aStack, this.mCosts);

            return;
        }
        else  {
            if (DetravScannerMod.DEBUGBUILD)
                aPlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED+" Failed on this chunk"));
        	badluck++;
        	if (!aPlayer.capabilities.isCreativeMode)
        		aItem.doDamage(aStack, this.mCosts/4);
        }
       // addChatMassageByValue(aPlayer,0,null);
    }

    void addOreToHashMap(String orename, EntityPlayer aPlayer) {
        String oreDistance = orename + DISTANCETEXTS[distTextIndex]; // orename + the textual distance of the ore
        if (!ores.containsKey(oreDistance)) {
            if (DetravScannerMod.DEBUGBUILD)
                aPlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN+" Adding to oremap " + oreDistance));
            ores.put(oreDistance, 1);
        } else {
            int val = ores.get(oreDistance);
            ores.put(oreDistance, val + 1);
        }
    }

    void addChatMassageByValue(EntityPlayer aPlayer, int value, String name) {
        if (value < 0) {
            aPlayer.addChatMessage(new ChatComponentText(foundTexts[6] + name));
        } else if (value < 1) {
            aPlayer.addChatMessage(new ChatComponentText(foundTexts[0]));
        } else if (value < 10)
            aPlayer.addChatMessage(new ChatComponentText(name + foundTexts[1]));
        else if (value < 30)
            aPlayer.addChatMessage(new ChatComponentText(name + foundTexts[2]));
        else if (value < 60)
            aPlayer.addChatMessage(new ChatComponentText(name + foundTexts[3]));
        else if (value < 100)
            aPlayer.addChatMessage(new ChatComponentText(name + foundTexts[4]));
        else
            aPlayer.addChatMessage(new ChatComponentText(name + foundTexts[5]));
    }

    public static int getPolution(World aWorld, int aX, int aZ)
    {
        return GT_Pollution.getPollution(aWorld.getChunkFromBlockCoords(aX, aZ));
    }
}
