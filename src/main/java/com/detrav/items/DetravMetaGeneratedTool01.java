package com.detrav.items;

import java.util.List;

import com.detrav.DetravScannerMod;
import com.detrav.enums.DetravToolDictNames;
import com.detrav.items.tools.DetravProPick;
import com.detrav.items.tools.DetravToolElectricProPick;

import cpw.mods.fml.common.Loader;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TC_Aspects;
import gregtech.api.interfaces.IToolStats;
import gregtech.api.items.GT_MetaGenerated_Tool;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;

import static com.detrav.DetravScannerMod.DEBUGBUILD;

/**
 * Created by wital_000 on 19.03.2016.
 */
public class DetravMetaGeneratedTool01 extends GT_MetaGenerated_Tool {
    public static DetravMetaGeneratedTool01 INSTANCE;

    public DetravMetaGeneratedTool01() {
        super("detrav.metatool.01");
        INSTANCE = this;
        addTool(0,"Prospector's Scanner(ULV)","", new DetravProPick(0), new Object[]{DetravToolDictNames.craftingToolProPick, new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 4L)}, new TC_Aspects.TC_AspectStack(TC_Aspects.PERFODIO, 4L));
        addTool(2,"Prospector's Scanner(LV)","", new DetravProPick(1), new Object[]{DetravToolDictNames.craftingToolProPick, new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 4L)}, new TC_Aspects.TC_AspectStack(TC_Aspects.PERFODIO, 4L));
        addTool(4,"Prospector's Scanner(MV)","", new DetravProPick(2), new Object[]{DetravToolDictNames.craftingToolProPick, new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 4L)}, new TC_Aspects.TC_AspectStack(TC_Aspects.PERFODIO, 4L));
        addTool(6,"Prospector's Scanner(HV)","", new DetravProPick(3), new Object[]{DetravToolDictNames.craftingToolProPick, new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 4L)}, new TC_Aspects.TC_AspectStack(TC_Aspects.PERFODIO, 4L));
        addTool(8,"Prospector's Scanner(EV)","", new DetravProPick(4), new Object[]{DetravToolDictNames.craftingToolProPick, new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 4L)}, new TC_Aspects.TC_AspectStack(TC_Aspects.PERFODIO, 4L));
        addTool(10,"Prospector's Scanner(IV)","", new DetravProPick(5), new Object[]{DetravToolDictNames.craftingToolProPick, new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 4L)}, new TC_Aspects.TC_AspectStack(TC_Aspects.PERFODIO, 4L));
        addTool(12,"Prospector's Scanner(LuV)","", new DetravProPick(6), new Object[]{DetravToolDictNames.craftingToolProPick, new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 4L)}, new TC_Aspects.TC_AspectStack(TC_Aspects.PERFODIO, 4L));
        addTool(14,"Prospector's Scanner(ZPM)","", new DetravProPick(7), new Object[]{DetravToolDictNames.craftingToolProPick, new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 4L)}, new TC_Aspects.TC_AspectStack(TC_Aspects.PERFODIO, 4L));
        addTool(16,"Prospector's Scanner(UV)","", new DetravProPick(8), new Object[]{DetravToolDictNames.craftingToolProPick, new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 4L)}, new TC_Aspects.TC_AspectStack(TC_Aspects.PERFODIO, 4L));
        addTool(18,"Prospector's Scanner(UHV)","", new DetravProPick(9), new Object[]{DetravToolDictNames.craftingToolProPick, new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 4L)}, new TC_Aspects.TC_AspectStack(TC_Aspects.PERFODIO, 4L));

        addTool(100, "Electric Prospector's Scanner (LuV)", "", new DetravToolElectricProPick(6), new Object[]{DetravToolDictNames.craftingToolElectricProPick, new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 4L)}, new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 4L));
        addTool(102, "Electric Prospector's Scanner (ZPM)", "", new DetravToolElectricProPick(7), new Object[]{DetravToolDictNames.craftingToolElectricProPick, new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 4L)}, new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 4L));
        addTool(104, "Electric Prospector's Scanner (UV)", "", new DetravToolElectricProPick(8), new Object[]{DetravToolDictNames.craftingToolElectricProPick, new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 4L)}, new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 4L));
        addTool(106, "Electric Prospector's Scanner (UHV)", "", new DetravToolElectricProPick(9), new Object[]{DetravToolDictNames.craftingToolElectricProPick, new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L), new TC_Aspects.TC_AspectStack(TC_Aspects.METALLUM, 4L)}, new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 4L));
        setCreativeTab(DetravScannerMod.TAB_DETRAV);
    }

    public void addAdditionalToolTips(List aList, ItemStack aStack, EntityPlayer aPlayer) {
        //getElectricStats()
        //super.addAdditionalToolTips();
        long tMaxDamage = getToolMaxDamage(aStack);
        Materials tMaterial = getPrimaryMaterial(aStack);
        IToolStats tStats = getToolStats(aStack);
        int tOffset = aList.size(); 
		//getElectricStats(aStack) != null ? 2 : 1;
        if (tStats != null) {
            String name = aStack.getUnlocalizedName();
            String num = name.substring("gt.detrav.metatool.01.".length());
            int meta = Integer.parseInt(num);
            int range = getHarvestLevel(aStack, "")/2+(meta/4);
            if ((range % 2) == 0 ) {
                range += 1;
            }
            if (meta<100) {
                    aList.add(tOffset + 0, EnumChatFormatting.WHITE + "Durability: " + EnumChatFormatting.GREEN + Long.toString(tMaxDamage - getToolDamage(aStack)) + " / " + Long.toString(tMaxDamage) + EnumChatFormatting.GRAY);
                    aList.add(tOffset + 1, EnumChatFormatting.WHITE + tMaterial.mDefaultLocalName + EnumChatFormatting.GRAY);
                    aList.add(tOffset + 2, EnumChatFormatting.WHITE + "Chunks: " + 
                        Integer.toString(range) + 
                        "x"+
                        Integer.toString(range) + 
                        EnumChatFormatting.GRAY);
                    aList.add(tOffset + 3, EnumChatFormatting.ITALIC+ "Right click on rock for prospecting current chunk!" + EnumChatFormatting.GRAY);
                    aList.add(tOffset + 4, EnumChatFormatting.ITALIC+ "Right click on bedrock for prospecting oil!" + EnumChatFormatting.GRAY);
                    aList.add(tOffset + 5, EnumChatFormatting.ITALIC+ "Chance of a successful scan: "+EnumChatFormatting.RESET+Integer.toString(((((1+meta)*8) <= 100)? ((1+meta)*8) : 100))+EnumChatFormatting.GRAY+"%");
                    aList.add(tOffset + 6, EnumChatFormatting.ITALIC+ "next to you (0 chunks away), close to you (1-2)");
                    aList.add(tOffset + 7, EnumChatFormatting.ITALIC+ "at medium range (3-5), at long range (6-8), far away (9+)");

                    /*aList.add(tOffset + 4, "Traces: 1-9");
                    aList.add(tOffset + 5, "Small: 10-29");
                    aList.add(tOffset + 6, "Medium: 30-59");
                    aList.add(tOffset + 7, "Large: 60-99");
                    aList.add(tOffset + 8, "Very large: 100-***");*/
            }else if (meta >=100 && meta<200) {
                    aList.add(tOffset + 0, EnumChatFormatting.WHITE + "Durability: " + EnumChatFormatting.GREEN + (tMaxDamage - getToolDamage(aStack)) + " / " + tMaxDamage + EnumChatFormatting.GRAY);
                    aList.add(tOffset + 1, EnumChatFormatting.WHITE + tMaterial.mDefaultLocalName + EnumChatFormatting.GRAY);
                    aList.add(tOffset + 2, EnumChatFormatting.WHITE + "Chunks: " + EnumChatFormatting.YELLOW + (getHarvestLevel(aStack, "") * 2 + 1) + "x" + (getHarvestLevel(aStack, "") * 2 + 1) + EnumChatFormatting.GRAY);
                    aList.add(tOffset + 3, EnumChatFormatting.ITALIC+ "Right click on rock for prospecting current chunk!");
                    aList.add(tOffset + 4, EnumChatFormatting.ITALIC+ "Right click on bedrock for prospecting oil!");
                    aList.add(tOffset + 5, EnumChatFormatting.ITALIC+ "Right click for scanning!");
            }
            
                 /* unused
                case 106:
                case 107:
                    aList.add(tOffset + 0, EnumChatFormatting.WHITE + "Loss/Tick EU: " + EnumChatFormatting.GREEN + getElectricStatsLoss(aStack) + EnumChatFormatting.GRAY);
                    aList.add(tOffset + 1, EnumChatFormatting.WHITE + "Durability: " + EnumChatFormatting.GREEN + (tMaxDamage - getToolDamage(aStack)) + " / " + tMaxDamage + EnumChatFormatting.GRAY);
                    aList.add(tOffset + 2, "Can use as normal battery");
                    aList.add(tOffset + 3, "x4 charge speed for tools");
                    aList.add(tOffset + 4, "Right click to open GUI");
                    break;
                case 108:
                    aList.add(tOffset + 0, EnumChatFormatting.WHITE + "It can configure any programmed circuit" + EnumChatFormatting.GRAY);
                    break;
                case 200:
                    int count;
                    count = ((int)(getLevel(aStack,0)*100)); if(count > 0) {aList.add(tOffset, "Bonus 0 level: +" + count + "%");tOffset++;}
                    count = ((int)(getLevel(aStack,1)*100)); if(count > 0) {aList.add(tOffset, "Bonus 1 level: +" + count + "%");tOffset++;}
                    count = ((int)(getLevel(aStack,2)*100)); if(count > 0) {aList.add(tOffset, "Bonus 2 level: +" + count + "%");tOffset++;}
                    count = ((int)(getLevel(aStack,3)*100)); if(count > 0) {aList.add(tOffset, "Bonus 3 level: +" + count + "%");tOffset++;}
                    count = ((int)(getLevel(aStack,4)*100)); if(count > 0) {aList.add(tOffset, "Bonus 4 level: +" + count + "%");tOffset++;}
                    count = ((int)(getLevel(aStack,5)*100)); if(count > 0) {aList.add(tOffset, "Bonus 5 level: +" + count + "%");tOffset++;}
                    count = ((int)(getLevel(aStack,6)*100)); if(count > 0) {aList.add(tOffset, "Bonus 6 level: +" + count + "%");tOffset++;}
                    count = ((int)(getLevel(aStack,7)*100)); if(count > 0) {aList.add(tOffset, "Bonus 7 level: +" + count + "%");tOffset++;}
                    count = ((int)(getLevel(aStack,8)*100)); if(count > 0) {aList.add(tOffset, "Bonus 8 level: +" + count + "%");tOffset++;}
                    break;
                case 220:
                    aList.add(tOffset + 0, EnumChatFormatting.WHITE + "Durability: " + EnumChatFormatting.GREEN + (tMaxDamage - getToolDamage(aStack)) + " / " + tMaxDamage + EnumChatFormatting.GRAY);
                    aList.add(tOffset + 1, EnumChatFormatting.WHITE + tMaterial.mDefaultLocalName + EnumChatFormatting.YELLOW + " lvl " + getHarvestLevel(aStack, "") + EnumChatFormatting.GRAY);
                    aList.add(tOffset + 2, "It can suck in fluid");
                    FluidStack stack = getFluidStackFromDetravData(aStack);
                    if(stack!=null && stack.amount >0)
                    {
                        aList.add(tOffset +3, "Fluid: "+stack.getLocalizedName()+" : "+stack.amount);
                    }
                    else
                    {
                        aList.add(tOffset +3, "Fluid: empty");
                    }
                    break;
                   
            		}
             */
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

    public final ItemStack getToolWithStatsPlus(int aToolID, int aAmount, Materials aPrimaryMaterial, Materials aSecondaryMaterial, long[] aElectricArray, long aLoss) {
        return getToolWithStatsPlus(aToolID, aAmount, aPrimaryMaterial, aSecondaryMaterial, aElectricArray, aLoss, 10000L);
    }

    public final ItemStack getToolWithStatsPlus(int aToolID, int aAmount, Materials aPrimaryMaterial, Materials aSecondaryMaterial, long[] aElectricArray, long aLoss, long durability) {
        ItemStack result = getToolWithStats(aToolID, aAmount, aPrimaryMaterial, aSecondaryMaterial, aElectricArray);
        NBTTagCompound aNBT = result.getTagCompound();
        if (aNBT != null) {
            aNBT = aNBT.getCompoundTag("GT.ToolStats");
            if (aNBT != null && aNBT.getBoolean("Electric")) {
                aNBT.setLong("Loss", aLoss);
            }
            aNBT.setLong("MaxDamage", durability);
        }
        return result;
    }

    public Long getToolGTDetravData(ItemStack aStack) {
        NBTTagCompound aNBT = aStack.getTagCompound();
        if (aNBT != null) {
            aNBT = aNBT.getCompoundTag("GT.ToolStats");
            if (aNBT != null)
                return aNBT.getLong("DetravData");
        }
        return 0L;
    }

    public boolean setToolGTDetravData(ItemStack aStack, long data) {
        NBTTagCompound aNBT = aStack.getTagCompound();
        if (aNBT != null) {
            aNBT = aNBT.getCompoundTag("GT.ToolStats");
            if (aNBT != null) {
                aNBT.setLong("DetravData", data);
                return true;
            }
        }
        return false;
    }

    public void setLevelToItemStack(ItemStack aStack, int level, float percent)
    {
        if(aStack == null) return;
        NBTTagCompound aNBT = aStack.getTagCompound();
        if(aNBT == null) {
            aNBT = new NBTTagCompound();
            NBTTagCompound detravLevel = new NBTTagCompound();
            aNBT.setTag("DetravLevel", detravLevel);
            aStack.setTagCompound(aNBT);
        }
        {
            NBTTagCompound detravLevel = aNBT.getCompoundTag("DetravLevel");
            if (detravLevel == null || hasnolevel(detravLevel)) {
                detravLevel = new NBTTagCompound();
                aNBT.setTag("DetravLevel", detravLevel);
            }
            detravLevel.setFloat("level"+Integer.toString(level),percent);
        }
    }


    private boolean hasnolevel(NBTTagCompound detravLevel)
    {
        for(int i=0;i<9;i++)
        {
            if(detravLevel.hasKey("level"+Integer.toString(i)))
              return false;
        }
        return true;
    }

    public float getLevel(ItemStack aStack, int level)
    {
        if(aStack == null) return 0;
        NBTTagCompound aNBT = aStack.getTagCompound();
        if(aNBT ==null) return 0;
        NBTTagCompound detravLevel = aNBT.getCompoundTag("DetravLevel");
        if(detravLevel == null) return 0;
        return detravLevel.getFloat("level"+Integer.toString(level));
    }

    public boolean setItemStackToDetravData(ItemStack aStack, ItemStack what)
    {
        if(aStack == null) return false;
        NBTTagCompound aNBT = aStack.getTagCompound();
        if(aNBT == null) {
            aNBT = new NBTTagCompound();
            NBTTagCompound detravData = new NBTTagCompound();
            aNBT.setTag("DetravData", detravData);
            aStack.setTagCompound(aNBT);
        }
        {
            NBTTagCompound detravData = aNBT.getCompoundTag("DetravData");
            if (detravData == null || detravData.getShort("id") == 0) {
                detravData = new NBTTagCompound();
                aNBT.setTag("DetravData", detravData);
            }
            if (what == null)
                aNBT.removeTag("DetravData");
            else
                what.writeToNBT(detravData);
            return true;
        }
    }

    public ItemStack getItemStackFromDetravData(ItemStack aStack)
    {
        if(aStack == null) return null;
        NBTTagCompound aNBT = aStack.getTagCompound();
        if(aNBT ==null) return null;
        NBTTagCompound detravData = aNBT.getCompoundTag("DetravData");
        if(detravData == null) return null;
        return ItemStack.loadItemStackFromNBT(detravData);
    }


    public boolean setFluidStackToDetravData(ItemStack aStack, FluidStack what)
    {
        if(aStack == null) return false;
        NBTTagCompound aNBT = aStack.getTagCompound();
        if(aNBT == null) {
            aNBT = new NBTTagCompound();
            NBTTagCompound detravData = new NBTTagCompound();
            aNBT.setTag("DetravData", detravData);
            aStack.setTagCompound(aNBT);
        }
        {
            NBTTagCompound detravData = aNBT.getCompoundTag("DetravData");
            if (detravData == null || detravData.getShort("id") == 0) {
                detravData = new NBTTagCompound();
                aNBT.setTag("DetravData", detravData);
            }
            if (what == null)
                aNBT.removeTag("DetravData");
            else
                what.writeToNBT(detravData);
            return true;
        }
    }

    public FluidStack getFluidStackFromDetravData(ItemStack aStack)
    {
        if(aStack == null) return null;
        NBTTagCompound aNBT = aStack.getTagCompound();
        if(aNBT ==null) return null;
        NBTTagCompound detravData = aNBT.getCompoundTag("DetravData");
        if(detravData == null) return null;
        return FluidStack.loadFluidStackFromNBT(detravData);
    }

    //SubItems TODO
    public void getDetravSubItems(Item item, CreativeTabs detravCreativeTab, List list) {

        ItemStack dStack;
        if (Loader.isModLoaded("dreamcraft")) {
            //Materials at tiers
            dStack = getToolWithStats(0, 1, Materials.Polycaprolactam, Materials.Polycaprolactam, null);
            list.add(dStack);
            dStack = getToolWithStats(2, 1, Materials.Steel, Materials.Steel, null);
            list.add(dStack);
            dStack = getToolWithStats(2, 1, Materials.Bronze, Materials.Steel, null);
            list.add(dStack);
            dStack = getToolWithStats(4, 1, Materials.Manyullyn, Materials.Aluminium, null);
            list.add(dStack);
            dStack = getToolWithStats(6, 1, Materials.DamascusSteel, Materials.DamascusSteel, null);
            list.add(dStack);
            dStack = getToolWithStats(8, 1, Materials.Titanium, Materials.Titanium, null);
            list.add(dStack);
            dStack = getToolWithStats(10, 1, Materials.TungstenSteel, Materials.TungstenSteel, null);
            list.add(dStack);
            dStack = getToolWithStats(12, 1, Materials.Iridium, Materials.Iridium, null);
            list.add(dStack);
            dStack = getToolWithStats(12, 1, Materials.Osmium, Materials.Osmium, null);
            list.add(dStack);
            dStack = getToolWithStats(14, 1, Materials.Neutronium, Materials.Neutronium, null);
            list.add(dStack);

            dStack = getToolWithStats(16, 1, Materials.InfinityCatalyst, Materials.InfinityCatalyst, null);
            list.add(dStack);
            dStack = getToolWithStats(18, 1, Materials.Infinity, Materials.Infinity, null);
            list.add(dStack);
        }
        
        //Steel for comparison
        dStack = getToolWithStats(0,1,Materials.Steel,Materials.Steel, null);
        list.add(dStack);
        dStack = getToolWithStats(2,1,Materials.Steel,Materials.Steel, null);
        list.add(dStack);
        dStack = getToolWithStats(4,1,Materials.Steel,Materials.Steel, null);
        list.add(dStack);
        dStack = getToolWithStats(6,1,Materials.Steel,Materials.Steel, null);
        list.add(dStack);
        dStack = getToolWithStats(8,1,Materials.Steel,Materials.Steel, null);
        list.add(dStack);
        dStack = getToolWithStats(10,1,Materials.Steel,Materials.Steel, null);
        list.add(dStack);
        dStack = getToolWithStats(12,1,Materials.Steel,Materials.Steel, null);
        list.add(dStack);
        dStack = getToolWithStats(14,1,Materials.Steel,Materials.Steel, null);
        list.add(dStack);
        dStack = getToolWithStats(16,1,Materials.Steel,Materials.Steel, null);
        list.add(dStack);
        dStack = getToolWithStats(18,1,Materials.Steel,Materials.Steel, null);
        list.add(dStack);
        
        //Electric Scanners 
        dStack = getToolWithStats(100, 1, Materials.Iridium, Materials.TungstenSteel, new long[]{102400000L, GT_Values.V[6], 6L, -1L});
        setCharge(dStack,102400000L);
        list.add(dStack);
        dStack = getToolWithStats(102, 1, Materials.Neutronium, Materials.TungstenSteel, new long[]{409600000L, GT_Values.V[7], 7L, -1L});
        setCharge(dStack,409600000L);
        list.add(dStack);

        if (Loader.isModLoaded("dreamcraft")) {
            dStack = getToolWithStats(104, 1, Materials.InfinityCatalyst, Materials.TungstenSteel, new long[]{1638400000L, GT_Values.V[8], 8L, -1L});
            setCharge(dStack, 1638400000L);
            list.add(dStack);
            dStack = getToolWithStats(106, 1, Materials.Infinity, Materials.TungstenSteel, new long[]{6553600000L, GT_Values.V[9], 9L, -1L});
            setCharge(dStack, 6553600000L);
            list.add(dStack);
        } else {
            dStack = getToolWithStats(106, 1, Materials.Neutronium, Materials.TungstenSteel, new long[]{6553600000L, GT_Values.V[9], 9L, -1L});
            setCharge(dStack, 6553600000L);
            list.add(dStack);
        }
    }
}
