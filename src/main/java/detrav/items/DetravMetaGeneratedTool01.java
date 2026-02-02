package detrav.items;

import static detrav.enums.IDDetraveMetaGeneratedTool01.ElectricProspectorScannerLuV;
import static detrav.enums.IDDetraveMetaGeneratedTool01.ElectricProspectorScannerUHV;
import static detrav.enums.IDDetraveMetaGeneratedTool01.ElectricProspectorScannerUV;
import static detrav.enums.IDDetraveMetaGeneratedTool01.ElectricProspectorScannerZPM;
import static detrav.enums.IDDetraveMetaGeneratedTool01.ProspectorScannerEV;
import static detrav.enums.IDDetraveMetaGeneratedTool01.ProspectorScannerHV;
import static detrav.enums.IDDetraveMetaGeneratedTool01.ProspectorScannerIV;
import static detrav.enums.IDDetraveMetaGeneratedTool01.ProspectorScannerLV;
import static detrav.enums.IDDetraveMetaGeneratedTool01.ProspectorScannerLuV;
import static detrav.enums.IDDetraveMetaGeneratedTool01.ProspectorScannerMV;
import static detrav.enums.IDDetraveMetaGeneratedTool01.ProspectorScannerUHV;
import static detrav.enums.IDDetraveMetaGeneratedTool01.ProspectorScannerUV;
import static detrav.enums.IDDetraveMetaGeneratedTool01.ProspectorScannerZPM;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
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
            ProspectorScannerLV.ID,
            "Prospector's Scanner(LV)",
            "",
            new DetravProspector(1),
            DetravToolDictNames.craftingToolProspector.toString(),
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L),
            new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 4L));
        addTool(
            ProspectorScannerMV.ID,
            "Prospector's Scanner(MV)",
            "",
            new DetravProspector(2),
            DetravToolDictNames.craftingToolProspector.toString(),
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L),
            new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 4L));
        addTool(
            ProspectorScannerHV.ID,
            "Prospector's Scanner(HV)",
            "",
            new DetravProspector(3),
            DetravToolDictNames.craftingToolProspector.toString(),
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L),
            new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 4L));
        addTool(
            ProspectorScannerEV.ID,
            "Prospector's Scanner(EV)",
            "",
            new DetravProspector(4),
            DetravToolDictNames.craftingToolProspector.toString(),
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L),
            new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 4L));
        addTool(
            ProspectorScannerIV.ID,
            "Prospector's Scanner(IV)",
            "",
            new DetravProspector(5),
            DetravToolDictNames.craftingToolProspector.toString(),
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L),
            new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 4L));
        addTool(
            ProspectorScannerLuV.ID,
            "Prospector's Scanner(LuV)",
            "",
            new DetravProspector(6),
            DetravToolDictNames.craftingToolProspector.toString(),
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L),
            new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 4L));
        addTool(
            ProspectorScannerZPM.ID,
            "Prospector's Scanner(ZPM)",
            "",
            new DetravProspector(7),
            DetravToolDictNames.craftingToolProspector.toString(),
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L),
            new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 4L));
        addTool(
            ProspectorScannerUV.ID,
            "Prospector's Scanner(UV)",
            "",
            new DetravProspector(8),
            DetravToolDictNames.craftingToolProspector.toString(),
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L),
            new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 4L));
        addTool(
            ProspectorScannerUHV.ID,
            "Prospector's Scanner(UHV)",
            "",
            new DetravProspector(9),
            DetravToolDictNames.craftingToolProspector.toString(),
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L),
            new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 4L));

        addTool(
            ElectricProspectorScannerLuV.ID,
            "Electric Prospector's Scanner (LuV)",
            "",
            new DetravToolElectricProspector(6),
            DetravToolDictNames.craftingToolElectricProspector,
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L),
            new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4L));
        addTool(
            ElectricProspectorScannerZPM.ID,
            "Electric Prospector's Scanner (ZPM)",
            "",
            new DetravToolElectricProspector(7),
            DetravToolDictNames.craftingToolElectricProspector,
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L),
            new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4L));
        addTool(
            ElectricProspectorScannerUV.ID,
            "Electric Prospector's Scanner (UV)",
            "",
            new DetravToolElectricProspector(8),
            DetravToolDictNames.craftingToolElectricProspector,
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4L),
            new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4L));
        addTool(
            ElectricProspectorScannerUHV.ID,
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

        int meta = Items.feather.getDamage(aStack);

        // range in chunks
        int range = getHarvestLevel(aStack, "") / 2 + (meta / 4);
        if ((range % 2) == 0) {
            range += 1;
        }

        if (meta < 100) {
            aList.add(
                tOffset,
                EnumChatFormatting.WHITE + StatCollector.translateToLocal("tooltip.detrav.scanner.durability")
                    + EnumChatFormatting.GREEN
                    + (tMaxDamage - getToolDamage(aStack))
                    + " / "
                    + tMaxDamage
                    + EnumChatFormatting.GRAY);
            aList.add(tOffset + 1, EnumChatFormatting.WHITE + tMaterial.getLocalizedName() + EnumChatFormatting.GRAY);
            aList.add(
                tOffset + 2,
                EnumChatFormatting.WHITE + StatCollector
                    .translateToLocal("tooltip.detrav.scanner.range") + range + "x" + range + EnumChatFormatting.GRAY);
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
                    + (Math.min(((1 + meta) * 8), 100))
                    + EnumChatFormatting.GRAY
                    + "%");
            aList.add(
                tOffset + 6,
                EnumChatFormatting.ITALIC + StatCollector.translateToLocal("tooltip.detrav.scanner.distance.0"));
            aList.add(
                tOffset + 7,
                EnumChatFormatting.ITALIC + StatCollector.translateToLocal("tooltip.detrav.scanner.distance.1"));
            return;

        }

        // from here, it's for the electric prospector scanners
        aList.add(
            tOffset + 0,
            EnumChatFormatting.WHITE + StatCollector.translateToLocal("tooltip.detrav.scanner.durability")
                + EnumChatFormatting.GREEN
                + (tMaxDamage - getToolDamage(aStack))
                + " / "
                + tMaxDamage
                + EnumChatFormatting.GRAY);
        aList.add(tOffset + 1, EnumChatFormatting.WHITE + tMaterial.getLocalizedName() + EnumChatFormatting.GRAY);
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

    public static final int MODE_BIG_ORES = 0;
    public static final int MODE_ALL_ORES = 1;
    public static final int MODE_FLUIDS = 2;
    public static final int MODE_POLLUTION = 3;

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
        if (NewHorizonsCoreMod.isModLoaded()) {
            // Materials at tiers
            list.add(getToolWithStats(ProspectorScannerLV.ID, 1, Materials.Steel, Materials.Steel, null));
            list.add(getToolWithStats(ProspectorScannerLV.ID, 1, Materials.Bronze, Materials.Steel, null));
            list.add(getToolWithStats(ProspectorScannerMV.ID, 1, Materials.Manyullyn, Materials.Aluminium, null));
            list.add(
                getToolWithStats(ProspectorScannerHV.ID, 1, Materials.DamascusSteel, Materials.DamascusSteel, null));
            list.add(getToolWithStats(ProspectorScannerEV.ID, 1, Materials.Titanium, Materials.Titanium, null));
            list.add(
                getToolWithStats(ProspectorScannerIV.ID, 1, Materials.TungstenSteel, Materials.TungstenSteel, null));
            list.add(getToolWithStats(ProspectorScannerLuV.ID, 1, Materials.Iridium, Materials.Iridium, null));
            list.add(getToolWithStats(ProspectorScannerLuV.ID, 1, Materials.Osmium, Materials.Osmium, null));
            list.add(getToolWithStats(ProspectorScannerZPM.ID, 1, Materials.Neutronium, Materials.Neutronium, null));
            list.add(
                getToolWithStats(
                    ProspectorScannerUV.ID,
                    1,
                    Materials.InfinityCatalyst,
                    Materials.InfinityCatalyst,
                    null));
            list.add(getToolWithStats(ProspectorScannerUHV.ID, 1, Materials.Infinity, Materials.Infinity, null));

            // electric prospector scanners:
            dStack = getToolWithStats(
                ElectricProspectorScannerUV.ID,
                1,
                Materials.InfinityCatalyst,
                Materials.TungstenSteel,
                new long[] { 1638400000L, GTValues.V[8], 8L, -1L });
            setCharge(dStack, 1638400000L);
            list.add(dStack);

            dStack = getToolWithStats(
                ElectricProspectorScannerUHV.ID,
                1,
                Materials.Infinity,
                Materials.TungstenSteel,
                new long[] { 6553600000L, GTValues.V[9], 9L, -1L });
            setCharge(dStack, 6553600000L);
            list.add(dStack);
        }

        // Steel for comparison
        list.add(getToolWithStats(ProspectorScannerLV.ID, 1, Materials.Steel, Materials.Steel, null));
        list.add(getToolWithStats(ProspectorScannerMV.ID, 1, Materials.Steel, Materials.Steel, null));
        list.add(getToolWithStats(ProspectorScannerHV.ID, 1, Materials.Steel, Materials.Steel, null));
        list.add(getToolWithStats(ProspectorScannerEV.ID, 1, Materials.Steel, Materials.Steel, null));
        list.add(getToolWithStats(ProspectorScannerIV.ID, 1, Materials.Steel, Materials.Steel, null));
        list.add(getToolWithStats(ProspectorScannerLuV.ID, 1, Materials.Steel, Materials.Steel, null));
        list.add(getToolWithStats(ProspectorScannerZPM.ID, 1, Materials.Steel, Materials.Steel, null));
        list.add(getToolWithStats(ProspectorScannerUV.ID, 1, Materials.Steel, Materials.Steel, null));
        list.add(getToolWithStats(ProspectorScannerUHV.ID, 1, Materials.Steel, Materials.Steel, null));

        // Electric Scanners
        dStack = getToolWithStats(
            ElectricProspectorScannerLuV.ID,
            1,
            Materials.Iridium,
            Materials.TungstenSteel,
            new long[] { 102400000L, GTValues.V[6], 6L, -1L });
        setCharge(dStack, 102400000L);
        list.add(dStack);

        dStack = getToolWithStats(
            ElectricProspectorScannerZPM.ID,
            1,
            Materials.Neutronium,
            Materials.TungstenSteel,
            new long[] { 409600000L, GTValues.V[7], 7L, -1L });
        setCharge(dStack, 409600000L);
        list.add(dStack);

        dStack = getToolWithStats(
            ElectricProspectorScannerUHV.ID,
            1,
            Materials.Neutronium,
            Materials.TungstenSteel,
            new long[] { 6553600000L, GTValues.V[9], 9L, -1L });
        setCharge(dStack, 6553600000L);
        list.add(dStack);
    }
}
