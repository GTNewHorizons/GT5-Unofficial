package com.detrav.items;

import com.detrav.DetravScannerMod;
import com.detrav.enums.DetravToolDictNames;
import com.detrav.items.tools.*;
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
        addTool(106, "Portable Charger","", new DetravToolPortableCharger(), new Object[]{DetravToolDictNames.craftingToolPortableCharger, new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.AURAM, 4L)}, new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 4L));
        setCreativeTab(DetravScannerMod.TAB_DETRAV);
    }


    public void addAdditionalToolTips(List aList, ItemStack aStack, EntityPlayer aPlayer) {
        //getElectricStats()
        //super.addAdditionalToolTips();
        long tMaxDamage = getToolMaxDamage(aStack);
        Materials tMaterial = getPrimaryMaterial(aStack);
        IToolStats tStats = getToolStats(aStack);
        int tOffset = aList.size(); //getElectricStats(aStack) != null ? 2 : 1;
        if (tStats != null) {
            String name = aStack.getUnlocalizedName();
            String num = name.substring("gt.detrav.metatool.01.".length());
            int meta = Integer.parseInt(num);
            switch (meta) {
                case 0:
                    aList.add(tOffset + 0, EnumChatFormatting.WHITE + "Durability: " + EnumChatFormatting.GREEN + (tMaxDamage - getToolDamage(aStack)) + " / " + tMaxDamage + EnumChatFormatting.GRAY);
                    aList.add(tOffset + 1, EnumChatFormatting.WHITE + tMaterial.mDefaultLocalName + EnumChatFormatting.YELLOW + " lvl " + getHarvestLevel(aStack, "") + EnumChatFormatting.GRAY);
                    aList.add(tOffset + 2, "Right click on rock for prospecting current chunk!");
                    aList.add(tOffset + 3, "Right click on bedrock for prospecting oil!");
                    aList.add(tOffset + 4, "Traces: 1-9");
                    aList.add(tOffset + 5, "Small: 10-29");
                    aList.add(tOffset + 6, "Medium: 30-59");
                    aList.add(tOffset + 7, "Large: 60-99");
                    aList.add(tOffset + 8, "Very large: 100-***");
                    break;
                case 100:
                case 101:
                case 102:
                case 103:
                case 104:
                case 105:
                    aList.add(tOffset + 0, EnumChatFormatting.WHITE + "Durability: " + EnumChatFormatting.GREEN + (tMaxDamage - getToolDamage(aStack)) + " / " + tMaxDamage + EnumChatFormatting.GRAY);
                    aList.add(tOffset + 1, EnumChatFormatting.WHITE + tMaterial.mDefaultLocalName + EnumChatFormatting.GRAY);
                    aList.add(tOffset + 2, EnumChatFormatting.WHITE + "Chunks: " + EnumChatFormatting.YELLOW + (getHarvestLevel(aStack, "") * 2 + 1) + "x" + (getHarvestLevel(aStack, "") * 2 + 1) + EnumChatFormatting.GRAY);
                    aList.add(tOffset + 3, "Right click on rock for prospecting current chunk!");
                    aList.add(tOffset + 4, "Right click on bedrock for prospecting oil!");
                    aList.add(tOffset + 5, "Right click for scanning!");
                    break;
                case 106:
                case 107:
                    aList.add(tOffset + 0, EnumChatFormatting.WHITE + "Loss energy: " + EnumChatFormatting.GREEN + getElectricStatsLoss(aStack) + EnumChatFormatting.GRAY);
                    aList.add(tOffset + 1, "Right click to open GUI");
                    break;
            }
        }
    }

    public Long getElectricStatsLoss(ItemStack aStack) {
        NBTTagCompound aNBT = aStack.getTagCompound();
        if (aNBT != null) {
            aNBT = aNBT.getCompoundTag("GT.ToolStats");
            if (aNBT != null && aNBT.getBoolean("Electric"))
                return aNBT.getLong("Loss");
        }
        return 0L;
    }

    public final ItemStack getToolWithStatsPlus(int aToolID, int aAmount, Materials aPrimaryMaterial, Materials aSecondaryMaterial, long[] aElectricArray,long aLoss) {
        ItemStack result = getToolWithStats(aToolID,aAmount,aPrimaryMaterial,aSecondaryMaterial,aElectricArray);
        NBTTagCompound aNBT = result.getTagCompound();
        if (aNBT != null) {
            aNBT = aNBT.getCompoundTag("GT.ToolStats");
            if (aNBT != null && aNBT.getBoolean("Electric"))
                aNBT.setLong("Loss",aLoss);
        }
        return result;
    }
}