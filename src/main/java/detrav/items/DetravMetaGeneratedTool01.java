package detrav.items;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import detrav.DetravScannerMod;
import detrav.enums.DetravToolDictNames;
import detrav.items.tools.DetravProspector;
import detrav.items.tools.DetravToolElectricProspector;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.TCAspects;
import gregtech.api.interfaces.IToolStats;
import gregtech.api.items.MetaGeneratedTool;

/**
 * Created by wital_000 on 19.03.2016.
 */
public class DetravMetaGeneratedTool01 extends MetaGeneratedTool {

    public static DetravMetaGeneratedTool01 INSTANCE;

    public DetravMetaGeneratedTool01() {
        super("detrav.metatool.01");
        INSTANCE = this;
        addTool(
            0,
            "Prospector's Scanner(ULV)",
            "",
            new DetravProspector(0),
            DetravToolDictNames.craftingToolProspector.toString(),
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L),
            new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 4L));
        addTool(
            2,
            "Prospector's Scanner(LV)",
            "",
            new DetravProspector(1),
            DetravToolDictNames.craftingToolProspector.toString(),
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L),
            new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 4L));
        addTool(
            4,
            "Prospector's Scanner(MV)",
            "",
            new DetravProspector(2),
            DetravToolDictNames.craftingToolProspector.toString(),
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L),
            new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 4L));
        addTool(
            6,
            "Prospector's Scanner(HV)",
            "",
            new DetravProspector(3),
            DetravToolDictNames.craftingToolProspector.toString(),
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L),
            new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 4L));
        addTool(
            8,
            "Prospector's Scanner(EV)",
            "",
            new DetravProspector(4),
            DetravToolDictNames.craftingToolProspector.toString(),
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L),
            new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 4L));
        addTool(
            10,
            "Prospector's Scanner(IV)",
            "",
            new DetravProspector(5),
            DetravToolDictNames.craftingToolProspector.toString(),
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L),
            new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 4L));
        addTool(
            12,
            "Prospector's Scanner(LuV)",
            "",
            new DetravProspector(6),
            DetravToolDictNames.craftingToolProspector.toString(),
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L),
            new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 4L));
        addTool(
            14,
            "Prospector's Scanner(ZPM)",
            "",
            new DetravProspector(7),
            DetravToolDictNames.craftingToolProspector.toString(),
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L),
            new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 4L));
        addTool(
            16,
            "Prospector's Scanner(UV)",
            "",
            new DetravProspector(8),
            DetravToolDictNames.craftingToolProspector.toString(),
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L),
            new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 4L));
        addTool(
            18,
            "Prospector's Scanner(UHV)",
            "",
            new DetravProspector(9),
            DetravToolDictNames.craftingToolProspector.toString(),
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L),
            new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 4L));

        addTool(
            100,
            "Electric Prospector's Scanner (LuV)",
            "",
            new DetravToolElectricProspector(6),
            DetravToolDictNames.craftingToolElectricProspector,
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L),
            new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4L));
        addTool(
            102,
            "Electric Prospector's Scanner (ZPM)",
            "",
            new DetravToolElectricProspector(7),
            DetravToolDictNames.craftingToolElectricProspector,
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L),
            new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4L));
        addTool(
            104,
            "Electric Prospector's Scanner (UV)",
            "",
            new DetravToolElectricProspector(8),
            DetravToolDictNames.craftingToolElectricProspector,
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L),
            new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4L));
        addTool(
            106,
            "Electric Prospector's Scanner (UHV)",
            "",
            new DetravToolElectricProspector(9),
            DetravToolDictNames.craftingToolElectricProspector,
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L),
            new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4L));
        setCreativeTab(DetravScannerMod.TAB_DETRAV);
    }

    @SuppressWarnings("unchecked")
    public void addAdditionalToolTips(List aList, ItemStack aStack, EntityPlayer aPlayer) {
        long tMaxDamage = getToolMaxDamage(aStack);
        Materials tMaterial = getPrimaryMaterial(aStack);
        IToolStats tStats = getToolStats(aStack);
        int tOffset = aList.size();
        if (tStats == null) return;

        String name = aStack.getUnlocalizedName();
        String num = name.substring("gt.detrav.metatool.01.".length());
        int meta = Integer.parseInt(num);
        int range = getHarvestLevel(aStack, "") / 2 + (meta / 4);
        if ((range % 2) == 0) {
            range += 1;
        }
        if (meta < 100) {
            aList.add(
                tOffset + 0,
                EnumChatFormatting.WHITE + StatCollector.translateToLocal("tooltip.detrav.scanner.durability")
                    + EnumChatFormatting.GREEN
                    + Long.toString(tMaxDamage - getToolDamage(aStack))
                    + " / "
                    + Long.toString(tMaxDamage)
                    + EnumChatFormatting.GRAY);
            aList.add(
                tOffset + 1,
                EnumChatFormatting.WHITE + tMaterial.getLocalizedNameForItem("%material") + EnumChatFormatting.GRAY);
            aList.add(
                tOffset + 2,
                EnumChatFormatting.WHITE + StatCollector.translateToLocal("tooltip.detrav.scanner.range")
                    + Integer.toString(range)
                    + "x"
                    + Integer.toString(range)
                    + EnumChatFormatting.GRAY);
            aList.add(
                tOffset + 3,
                EnumChatFormatting.ITALIC + StatCollector.translateToLocal("tooltip.detrav.scanner.usage.0")
                    + EnumChatFormatting.GRAY);
            aList.add(
                tOffset + 4,
                EnumChatFormatting.ITALIC + StatCollector.translateToLocal("tooltip.detrav.scanner.usage.1")
                    + EnumChatFormatting.GRAY);
            aList.add(
                tOffset + 5,
                EnumChatFormatting.ITALIC + StatCollector.translateToLocal("tooltip.detrav.scanner.success.chance")
                    + EnumChatFormatting.RESET
                    + Integer.toString(((((1 + meta) * 8) <= 100) ? ((1 + meta) * 8) : 100))
                    + EnumChatFormatting.GRAY
                    + "%");
            aList.add(
                tOffset + 6,
                EnumChatFormatting.ITALIC + StatCollector.translateToLocal("tooltip.detrav.scanner.distance.0"));
            aList.add(
                tOffset + 7,
                EnumChatFormatting.ITALIC + StatCollector.translateToLocal("tooltip.detrav.scanner.distance.1"));

        } else if (meta >= 100 && meta < 200) {
            aList.add(
                tOffset + 0,
                EnumChatFormatting.WHITE + StatCollector.translateToLocal("tooltip.detrav.scanner.durability")
                    + EnumChatFormatting.GREEN
                    + (tMaxDamage - getToolDamage(aStack))
                    + " / "
                    + tMaxDamage
                    + EnumChatFormatting.GRAY);
            aList.add(
                tOffset + 1,
                EnumChatFormatting.WHITE + tMaterial.getLocalizedNameForItem("%material") + EnumChatFormatting.GRAY);
            aList.add(
                tOffset + 2,
                EnumChatFormatting.WHITE + StatCollector.translateToLocal("tooltip.detrav.scanner.range")
                    + EnumChatFormatting.YELLOW
                    + (getHarvestLevel(aStack, "") * 2 + 1)
                    + "x"
                    + (getHarvestLevel(aStack, "") * 2 + 1)
                    + EnumChatFormatting.GRAY);
            aList.add(
                tOffset + 3,
                EnumChatFormatting.ITALIC + StatCollector.translateToLocal("tooltip.detrav.scanner.usage.0"));
            aList.add(
                tOffset + 4,
                EnumChatFormatting.ITALIC + StatCollector.translateToLocal("tooltip.detrav.scanner.usage.1"));
            aList.add(
                tOffset + 5,
                EnumChatFormatting.ITALIC + StatCollector.translateToLocal("tooltip.detrav.scanner.usage.2"));
            aList.add(
                tOffset + 6,
                EnumChatFormatting.ITALIC + StatCollector.translateToLocal("tooltip.detrav.scanner.usage.3"));
            aList.add(
                tOffset + 7,
                EnumChatFormatting.ITALIC + StatCollector.translateToLocal("tooltip.detrav.scanner.usage.4"));
        }

    }

    public Long getToolGTDetravData(ItemStack aStack) {
        NBTTagCompound aNBT = aStack.getTagCompound();
        if (aNBT != null) {
            aNBT = aNBT.getCompoundTag("GT.ToolStats");
            if (aNBT != null) return aNBT.getLong("DetravData");
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

    @SuppressWarnings("unchecked")
    public void getDetravSubItems(Item item, CreativeTabs detravCreativeTab, List list) {

        ItemStack dStack;
        if (Mods.NewHorizonsCoreMod.isModLoaded()) {
            // Materials at tiers
            list.add(getToolWithStats(0, 1, Materials.Polycaprolactam, Materials.Polycaprolactam, null));
            list.add(getToolWithStats(2, 1, Materials.Steel, Materials.Steel, null));
            list.add(getToolWithStats(2, 1, Materials.Bronze, Materials.Steel, null));
            list.add(getToolWithStats(4, 1, Materials.Manyullyn, Materials.Aluminium, null));
            list.add(getToolWithStats(6, 1, Materials.DamascusSteel, Materials.DamascusSteel, null));
            list.add(getToolWithStats(8, 1, Materials.Titanium, Materials.Titanium, null));
            list.add(getToolWithStats(10, 1, Materials.TungstenSteel, Materials.TungstenSteel, null));
            list.add(getToolWithStats(12, 1, Materials.Iridium, Materials.Iridium, null));
            list.add(getToolWithStats(12, 1, Materials.Osmium, Materials.Osmium, null));
            list.add(getToolWithStats(14, 1, Materials.Neutronium, Materials.Neutronium, null));
            list.add(getToolWithStats(16, 1, Materials.InfinityCatalyst, Materials.InfinityCatalyst, null));
            list.add(getToolWithStats(18, 1, Materials.Infinity, Materials.Infinity, null));
        }

        // Steel for comparison
        list.add(getToolWithStats(0, 1, Materials.Steel, Materials.Steel, null));
        list.add(getToolWithStats(2, 1, Materials.Steel, Materials.Steel, null));
        list.add(getToolWithStats(4, 1, Materials.Steel, Materials.Steel, null));
        list.add(getToolWithStats(6, 1, Materials.Steel, Materials.Steel, null));
        list.add(getToolWithStats(8, 1, Materials.Steel, Materials.Steel, null));
        list.add(getToolWithStats(10, 1, Materials.Steel, Materials.Steel, null));
        list.add(getToolWithStats(12, 1, Materials.Steel, Materials.Steel, null));
        list.add(getToolWithStats(14, 1, Materials.Steel, Materials.Steel, null));
        list.add(getToolWithStats(16, 1, Materials.Steel, Materials.Steel, null));
        list.add(getToolWithStats(18, 1, Materials.Steel, Materials.Steel, null));

        // Electric Scanners
        dStack = getToolWithStats(
            100,
            1,
            Materials.Iridium,
            Materials.TungstenSteel,
            new long[] { 102400000L, GTValues.V[6], 6L, -1L });
        setCharge(dStack, 102400000L);
        list.add(dStack);

        dStack = getToolWithStats(
            102,
            1,
            Materials.Neutronium,
            Materials.TungstenSteel,
            new long[] { 409600000L, GTValues.V[7], 7L, -1L });
        setCharge(dStack, 409600000L);
        list.add(dStack);

        if (Mods.NewHorizonsCoreMod.isModLoaded()) {
            dStack = getToolWithStats(
                104,
                1,
                Materials.InfinityCatalyst,
                Materials.TungstenSteel,
                new long[] { 1638400000L, GTValues.V[8], 8L, -1L });
            setCharge(dStack, 1638400000L);
            list.add(dStack);

            dStack = getToolWithStats(
                106,
                1,
                Materials.Infinity,
                Materials.TungstenSteel,
                new long[] { 6553600000L, GTValues.V[9], 9L, -1L });
            setCharge(dStack, 6553600000L);
            list.add(dStack);
        } else {
            dStack = getToolWithStats(
                106,
                1,
                Materials.Neutronium,
                Materials.TungstenSteel,
                new long[] { 6553600000L, GTValues.V[9], 9L, -1L });
            setCharge(dStack, 6553600000L);
            list.add(dStack);
        }
    }
}
