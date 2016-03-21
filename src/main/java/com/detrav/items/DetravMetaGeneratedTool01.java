package com.detrav.items;

import com.detrav.DetravScannerMod;
import com.detrav.enums.DetravToolDictNames;
import com.detrav.items.tools.DetravToolHVElectricProPick;
import com.detrav.items.tools.DetravToolLVElectricProPick;
import com.detrav.items.tools.DetravToolMVElectricProPick;
import com.detrav.items.tools.DetravToolProPick;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TC_Aspects;
import gregtech.api.interfaces.IToolStats;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_ModHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

/**
 * Created by wital_000 on 19.03.2016.
 */
public class DetravMetaGeneratedTool01 extends GT_MetaGenerated_Tool {
    public static DetravMetaGeneratedTool01 INSTANCE;

    public DetravMetaGeneratedTool01() {
        super("detrav.metatool.01");
        INSTANCE = this;
        addTool(0, "Prospector's Pick", "", new DetravToolProPick(), new Object[]{DetravToolDictNames.craftingToolProPick, new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 4L)});
        addTool(100, "Electric Prospector's Scanner (LV)", "", new DetravToolLVElectricProPick(), new Object[]{DetravToolDictNames.craftingToolElectricProPick, new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 4L)}, new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 4L));
        addTool(102, "Electric Prospector's Scanner (MV)", "", new DetravToolMVElectricProPick(), new Object[]{DetravToolDictNames.craftingToolElectricProPick, new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 4L)}, new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 4L));
        addTool(104, "Electric Prospector's Scanner (HV)", "", new DetravToolHVElectricProPick(), new Object[]{DetravToolDictNames.craftingToolElectricProPick, new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 4L)}, new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 4L));
        setCreativeTab(DetravScannerMod.TAB_DETRAV);
        //addItemBehavior(0,new BehaviourDetravToolProPick());
        if (GT_Values.GT.isClientSide()) {
            //new Information

        }
    }


    /*public void addDetravInformation(ItemStack aStack, EntityPlayer aPlayer, List aList, boolean aF3_H) {
        if (getMaxDamage() > 0 && !getHasSubtypes())
            aList.add((aStack.getMaxDamage() - getDamage(aStack)) + " / " + aStack.getMaxDamage());
        String mTooltip = getUnlocalizedName(aStack) + ".tooltip";
        if (mTooltip != null) {
            String lTooltip = GT_LanguageManager.getTranslation(mTooltip);
            String[] lTooltips = lTooltip.split("|");
            if(lTooltips!=null)
            for(String str : lTooltips) {
                aList.add(aList.size(),str);
            }
        }
        //if (GT_ModHandler.isElectricItem(aStack)) aList.add("Tier: " + getTier(aStack));
        addAdditionalToolTips(aList, aStack, aPlayer);
    }*/


    public void addAdditionalToolTips(List aList, ItemStack aStack) {
        //super.addAdditionalToolTips();
        long tMaxDamage = getToolMaxDamage(aStack);
        Materials tMaterial = getPrimaryMaterial(aStack);
        IToolStats tStats = getToolStats(aStack);
        int tOffset = aList.size(); //getElectricStats(aStack) != null ? 2 : 1;
        if (tStats != null) {
            String name = aStack.getUnlocalizedName();
            //if (name.equals("gt.detrav.metatool.01.0")) {

            String num = name.substring("gt.detrav.metatool.01.".length());
            int meta = Integer.parseInt(num);
            if (meta < 100) {
                aList.add(tOffset + 0, EnumChatFormatting.WHITE + "Durability: " + EnumChatFormatting.GREEN + (tMaxDamage - getToolDamage(aStack)) + " / " + tMaxDamage + EnumChatFormatting.GRAY);
                aList.add(tOffset + 1, EnumChatFormatting.WHITE + tMaterial.mDefaultLocalName + EnumChatFormatting.YELLOW + " lvl " + getHarvestLevel(aStack, "") + EnumChatFormatting.GRAY);
                aList.add(tOffset + 2, "Right click on rock for prospecting current chunk!");
            } else {
                aList.add(tOffset + 0, EnumChatFormatting.WHITE + "Durability: " + EnumChatFormatting.GREEN + (tMaxDamage - getToolDamage(aStack)) + " / " + tMaxDamage + EnumChatFormatting.GRAY);
                aList.add(tOffset + 1, EnumChatFormatting.WHITE + tMaterial.mDefaultLocalName + EnumChatFormatting.GRAY);
                aList.add(tOffset + 2, EnumChatFormatting.WHITE + "Chunks: " + EnumChatFormatting.YELLOW + (getHarvestLevel(aStack, "")*2+1) + "x" + (getHarvestLevel(aStack, "")*2+1) + EnumChatFormatting.GRAY);
                aList.add(tOffset + 3, "Right click on rock for prospecting current chunk!");
                aList.add(tOffset + 4, "Right click for scanning!");
            }
            //Right click on rock for prospecting current chunk!|Right click for scanning!
            //aList.add(tOffset + 2, EnumChatFormatting.WHITE + "Attack Damage: " + EnumChatFormatting.BLUE + getToolCombatDamage(aStack) + EnumChatFormatting.GRAY);
            //aList.add(tOffset + 3, EnumChatFormatting.WHITE + "Mining Speed: " + EnumChatFormatting.LIGHT_PURPLE + Math.max(Float.MIN_NORMAL, tStats.getSpeedMultiplier() * getPrimaryMaterial(aStack).mToolSpeed) + EnumChatFormatting.GRAY);
                /*NBTTagCompound aNBT = aStack.getTagCompound();
                if (aNBT != null) {
                    aNBT = aNBT.getCompoundTag("GT.ToolStats");
                    if (aNBT != null && aNBT.hasKey("Heat")){
                        int tHeat = aNBT.getInteger("Heat");
                        long tWorldTime = aPlayer.getEntityWorld().getWorldTime();
                        if(aNBT.hasKey("HeatTime")){
                            long tHeatTime = aNBT.getLong("HeatTime");
                            if(tWorldTime>(tHeatTime+10)){
                                tHeat = (int) (tHeat - ((tWorldTime-tHeatTime)/10));
                                if(tHeat<300&&tHeat>-10000)tHeat=300;
                            }
                            aNBT.setLong("HeatTime", tWorldTime);
                            if(tHeat>-10000)aNBT.setInteger("Heat", tHeat);
                        }

                        aList.add(tOffset + 3, EnumChatFormatting.RED + "Heat: " + aNBT.getInteger("Heat")+" K" + EnumChatFormatting.GRAY);
                    }
                }*/
            // }
        }
    }
}