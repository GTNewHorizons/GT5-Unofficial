package gregtech.common.items.tools;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IToolStats;
import gregtech.api.util.GTUtility;
import gregtech.api.util.TurbineStatCalculator;

/**
 * A standalone turbine rotor: the thing a turbine multiblock spins, not a tool anybody swings.
 * <p/>
 * Four of these exist, one per rotor size, and they differ only in their {@link IToolStats} -- the speed and
 * durability multipliers and the base efficiency that {@link TurbineStatCalculator} reads. Everything a machine needs
 * to know about a rotor it gets by testing for this class and asking {@link #getTurbineSize()}, which replaces the
 * metadata arithmetic the old meta-item forced on every one of them.
 * <p/>
 * Rotors wear differently from every other tool. A player never uses one: a turbine wears it by an amount
 * proportional to the power it is producing, which for a small turbine on weak steam is a small fraction of a
 * durability point per wear event. {@link ToolItemBase#doMachineWear} is what keeps that faithful, by banking the
 * fraction on the stack rather than rounding it away.
 */
public class ToolTurbineItem extends ToolItemBase {

    private final int size;

    /**
     * @param unlocalizedName   appended to {@code gt.}; becomes both the registry name and the localization key root.
     * @param toolStats         the generic stats, reused verbatim from the old tool registry, so that efficiency and
     *                          optimal flow come out exactly as they did before.
     * @param englishNameFormat the default display name, where {@code %material} is replaced by the material name.
     * @param englishTooltip    the default tooltip.
     * @param size              the rotor size, 1 for small through 4 for huge. Machines that scale with rotor size
     *                          read this rather than deriving it from the metadata.
     */
    public ToolTurbineItem(String unlocalizedName, IToolStats toolStats, String englishNameFormat,
        String englishTooltip, int size) {
        super(unlocalizedName, toolStats, englishNameFormat, englishTooltip, null);
        this.size = size;
    }

    /**
     * @return the rotor size, 1 for small through 4 for huge.
     */
    public int getTurbineSize() {
        return size;
    }

    /**
     * @return whether this stack is a turbine rotor of any size. This is what replaces the old "is it the tool
     *         meta-item, and is its metadata in the turbine band" test that every turbine machine used to make.
     */
    public static boolean isTurbineRotor(ItemStack stack) {
        return stack != null && stack.stackSize > 0 && stack.getItem() instanceof ToolTurbineItem;
    }

    /**
     * @return the rotor size of this stack, 1 for small through 4 for huge, or 0 if it is not a rotor.
     */
    public static int getTurbineSize(ItemStack stack) {
        return stack != null && stack.getItem() instanceof ToolTurbineItem rotor ? rotor.getTurbineSize() : 0;
    }

    /**
     * A rotor is worn by the machine holding it, not by anybody using it, and the two places that ask for whole
     * points at a time -- GT++'s Atmospheric Reconditioner among them -- mean exactly as many points as they ask
     * for. So unlike an ordinary tool, where every action costs the same single point, this spends what it is told.
     */
    @Override
    public boolean doDamageToItem(ItemStack stack, int vanillaDamage) {
        return vanillaDamage <= 0 || doDamage(stack, vanillaDamage);
    }

    /* ---------- DISPLAY ---------- */

    /**
     * A rotor's tooltip says nothing about mining or melee, and everything about what it does in a turbine. Ported
     * from the turbine branch of {@link gregtech.api.items.MetaGeneratedTool#addAdditionalToolTips} unchanged.
     */
    @Override
    protected void addAdditionalToolTips(List<String> list, ItemStack stack, EntityPlayer player) {
        Materials material = getToolMaterial(stack);
        if (material == Materials._NULL) return;
        TurbineStatCalculator turbine = new TurbineStatCalculator(this, stack);
        list.add(
            EnumChatFormatting.GRAY
                + translateToLocalFormatted(
                    "gt.item.desc.durability",
                    EnumChatFormatting.GREEN + formatNumber(turbine.getCurrentDurability()) + " ",
                    " " + formatNumber(turbine.getMaxDurability()))
                + EnumChatFormatting.GRAY);
        list.add(
            EnumChatFormatting.GRAY
                + translateToLocalFormatted(
                    "gt.item.desc.tier",
                    material.getLocalizedName() + ":" + EnumChatFormatting.YELLOW,
                    "" + getHarvestLevel(stack, ""))
                + EnumChatFormatting.GRAY);
        list.add(
            EnumChatFormatting.WHITE
                + translateToLocalFormatted(
                    "gt.item.desc.base_eff",
                    "" + EnumChatFormatting.BLUE + (int) Math.ceil(turbine.getBaseEfficiency() * 100))
                + "%"
                + EnumChatFormatting.GRAY);
        list.add(EnumChatFormatting.GRAY + translateToLocal("gt.item.desc.fuel_eff"));
        list.add(
            flowLine(
                EnumChatFormatting.WHITE,
                "GT5U.tootlip.tool.turbine.steam",
                "L/t",
                turbine.getOptimalSteamFlow(),
                turbine.getOptimalSteamEUt(),
                turbine.getSteamEfficiency()));
        list.add(
            flowLine(
                EnumChatFormatting.WHITE,
                "GT5U.tootlip.tool.turbine.loose",
                "L/t",
                turbine.getOptimalLooseSteamFlow(),
                turbine.getOptimalLooseSteamEUt(),
                turbine.getLooseSteamEfficiency()));
        list.add(
            EnumChatFormatting.DARK_GRAY + String.format("  %s", translateToLocal("GT5U.tootlip.tool.turbine.super")));
        list.add(
            flowLine(
                EnumChatFormatting.AQUA,
                "GT5U.tootlip.tool.turbine.gas",
                "EU/t",
                turbine.getOptimalGasFlow(),
                turbine.getOptimalGasEUt(),
                turbine.getGasEfficiency()));
        list.add(
            flowLine(
                EnumChatFormatting.AQUA,
                "GT5U.tootlip.tool.turbine.loose",
                "EU/t",
                turbine.getOptimalLooseGasFlow(),
                turbine.getOptimalLooseGasEUt(),
                turbine.getLooseGasEfficiency()));
        list.add(
            flowLine(
                EnumChatFormatting.LIGHT_PURPLE,
                "GT5U.tootlip.tool.turbine.plasma",
                "EU/t",
                turbine.getOptimalPlasmaFlow(),
                turbine.getOptimalPlasmaEUt(),
                turbine.getPlasmaEfficiency()));
        list.add(
            flowLine(
                EnumChatFormatting.LIGHT_PURPLE,
                "GT5U.tootlip.tool.turbine.loose",
                "EU/t",
                turbine.getOptimalLoosePlasmaFlow(),
                turbine.getOptimalLoosePlasmaEUt(),
                turbine.getLoosePlasmaEfficiency()));
        list.add(
            EnumChatFormatting.LIGHT_PURPLE + translateToLocalFormatted(
                "gt.item.desc.eff_tier",
                "" + EnumChatFormatting.GOLD + turbine.getOverflowEfficiency() + EnumChatFormatting.GRAY));
    }

    /** One "fuel > power | efficiency" line of the fuel efficiency block. */
    private static String flowLine(EnumChatFormatting colour, String nameKey, String flowUnit, float flow, float eut,
        float efficiency) {
        return colour + String.format("  %s ", translateToLocal(nameKey))
            + EnumChatFormatting.GRAY
            + " | "
            + String.format(
                "%s %s > %s EU/t | %s",
                EnumChatFormatting.GOLD + formatNumber(GTUtility.safeInt((long) flow)) + EnumChatFormatting.GRAY,
                flowUnit,
                EnumChatFormatting.DARK_GREEN + formatNumber(GTUtility.safeInt((long) eut)) + EnumChatFormatting.GRAY,
                "" + EnumChatFormatting.BLUE + (int) (efficiency * 100) + "%" + EnumChatFormatting.GRAY);
    }
}
