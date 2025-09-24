package gregtech.api.util;

import static gregtech.api.util.GTUtility.translate;
import static gregtech.api.util.tooltip.TooltipHelper.percentageFormat;
import static net.minecraft.util.StatCollector.translateToLocal;
import static org.apache.commons.lang3.StringUtils.removeEnd;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import net.minecraft.util.EnumChatFormatting;

import org.jetbrains.annotations.NotNull;

import com.github.bsideup.jabel.Desugar;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import com.google.common.primitives.Ints;
import com.gtnewhorizon.structurelib.StructureLibAPI;

import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.structure.IStructureChannels;
import gregtech.api.util.tooltip.TooltipHelper;
import gregtech.api.util.tooltip.TooltipTier;

/**
 * This makes it easier to build multi tooltips, with a standardized format. <br>
 * Info section order should be:<br>
 * addMachineType<br>
 * addInfo, for what it does, special notes, etc.<br>
 * addSeparator, if you need it<br>
 * addPollutionAmount<br>
 * <br>
 * Structure order should be:<br>
 * beginStructureBlock<br>
 * addController<br>
 * addCasingInfo<br>
 * addOtherStructurePart, for secondary structure block info (pipes, coils, etc)<br>
 * addEnergyHatch/addDynamoHatch<br>
 * addMaintenanceHatch<br>
 * addMufflerHatch<br>
 * addInputBus/addInputHatch/addOutputBus/addOutputHatch, in that order<br>
 * addShiftInfo adds a line as secondary (LSHIFT to show) tooltip<br>
 * Use addStructureInfo for any comments on nonstandard structure info wherever needed <br>
 * toolTipFinisher goes at the very end<br>
 * <br>
 * Originally created by kekzdealer
 * Refactored by ChromaPIE
 */
public class MultiblockTooltipBuilder {

    public static final String TAB = "   ";
    private static final String COLON = translateToLocal("gt.string.colon").equals(":") ? ": "
        : translateToLocal("gt.string.colon");
    private static final String SEPARATOR = translateToLocal("gt.string.separator").equals(",") ? ", "
        : translateToLocal("gt.string.separator");
    private static final String TT_StaticParallels = translateToLocal("GT5U.MBTT.Parallel.Base");
    private static final String TT_StaticSpeed = translateToLocal("GT5U.MBTT.Speed.Base");
    private static final String TT_StaticEuEff = translateToLocal("GT5U.MBTT.EuDiscount.Base");
    private static final String TT_DynamicParallels = translateToLocal("GT5U.MBTT.Parallel.Additional");
    private static final String TT_SingularParallel = translateToLocal("GT5U.MBTT.Parallel.Singular");
    private static final String TT_DynamicSpeed = translateToLocal("GT5U.MBTT.Speed.Additional");
    private static final String TT_DynamicEuEff = translateToLocal("GT5U.MBTT.EuDiscount.Additional");
    private static final String TT_Steam_StaticSteamEff = translateToLocal("GT5U.MBTT.SteamDiscount.Base");
    private static final String TT_structurehint = translateToLocal("GT5U.MBTT.StructureHint");
    private static final String TT_partinfohint = translateToLocal("GT5U.MBTT.PartInfoHint");
    private static final String TT_air = translateToLocal("GT5U.MBTT.Air");
    private static final String[] TT_dots = IntStream.range(0, 16)
        .mapToObj(i -> translateToLocal("structurelib.blockhint." + i + ".name"))
        .toArray(String[]::new);

    private final List<TooltipLine> iLines;
    private final List<TooltipLine> sLines;
    private List<String> hLines;
    private SetMultimap<Integer, String> hBlocks;

    private String[] hArray;

    public MultiblockTooltipBuilder() {
        iLines = new LinkedList<>();
        sLines = new LinkedList<>();
        hLines = new LinkedList<>();
        hBlocks = Multimaps.newSetMultimap(new HashMap<>(), HashSet::new);
        hBlocks.put(StructureLibAPI.HINT_BLOCK_META_AIR, TT_air);
    }

    /**
     * Add a line telling you what the machine type is. Usually, this will be the name of an SB version.<br>
     * Machine Type: machine<br>
     * Provide multiple params for multifunctional machines, divided by "|"<br>
     * Acronyms should NOT be made a separate param. It should be like<br>
     * Machine Type: Big Bad Machine, BBM | Furnace<br>
     * but not Machine Type: Big Bad Machine | BBM | Furnace
     *
     * @param machLocKeys Localization keys to machine types
     *
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addMachineType(String... machLocKeys) {
        String placeholder = "%s" + EnumChatFormatting.GRAY + " | " + EnumChatFormatting.YELLOW;

        addInfo(
            "%s" + removeEnd(
                Strings.repeat(placeholder, machLocKeys.length),
                EnumChatFormatting.GRAY + " | " + EnumChatFormatting.YELLOW),
            Stream.concat(Stream.of("GT5U.MBTT.MachineType"), Arrays.stream(machLocKeys))
                .toArray());
        return this;
    }

    /**
     * Add a basic line of information about this structure
     *
     * @param text The line to be added.
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addInfo(String text, Object... params) {
        iLines.add(new TooltipLine(text, params));
        return this;
    }

    public MultiblockTooltipBuilder addInfo(String text) {
        addInfo(text, new Object[0]);
        return this;
    }

    public MultiblockTooltipBuilder addShiftInfo(String text, Object... params) {
        sLines.add(new TooltipLine(text, params));
        return this;
    }

    public MultiblockTooltipBuilder addShiftInfo(String text) {
        addShiftInfo(text, new Object[0]);
        return this;
    }

    /**
     * Add a line for static parallel count processes up to {parallels} recipes at once
     *
     * @param parallels Maximum parallels
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addStaticParallelInfo(Integer parallels) {
        addInfo("GT5U.MBTT.Parallel.Base", TooltipHelper.parallelText(parallels));
        return this;
    }

    /**
     * Add a line of information about dynamic parallel count (tiered).
     * "Processes %s items per %s Tier."
     *
     * @param parallels Amount of parallels gained per tier
     * @param tier      Tiered object that determines bonus
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addDynamicParallelInfo(Integer parallels, TooltipTier tier) {
        addInfo(
            parallels == 1 ? TT_SingularParallel : TT_DynamicParallels,
            TooltipHelper.parallelText(parallels),
            tier.getValue());
        return this;
    }

    /**
     * Add a line of information about parallel count per Voltage Tier
     * "Processes %s items per Voltage Tier."
     *
     * @param parallels Amount of parallels gained per Voltage tier
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addVoltageParallelInfo(int parallels) {
        return addDynamicParallelInfo(parallels, TooltipTier.VOLTAGE);
    }

    /**
     * Add a line of information about multiplicative parallels per Tier
     * "%sx Parallels per %s Tier"
     *
     * @param factor parallel multiplier
     * @param tier   Tiered object that determines bonus
     * @return Instance this method was called on
     */
    public MultiblockTooltipBuilder addDynamicMultiplicativeParallelInfo(Integer factor, TooltipTier tier) {
        addInfo(TT_DynamicParallels, TooltipHelper.parallelText(factor.toString() + "x"), tier.getValue());
        return this;
    }

    /**
     * Add a line of information about Speed bonus
     *
     * @param speed Speed as defined in ProcessingLogic. e.g (1F / 3.5F = 350% Speed
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addStaticSpeedInfo(float speed) {

        addInfo(TT_StaticSpeed, TooltipHelper.speedText(speed));
        return this;
    }

    /**
     * Add a line of information about dynamic parallel count (tiered).
     *
     * @param speed Speed increment per tier
     * @param tier  Tiered object that determines bonus
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addDynamicSpeedInfo(float speed, TooltipTier tier) {
        addInfo(TT_DynamicSpeed, TooltipHelper.speedText("+" + percentageFormat.format(speed)), tier.getValue());
        return this;
    }

    /**
     * Add a line of information about Eu Discount bonus relative to SB machines
     *
     * @param euEff euEff as defined in ProcessingLogic
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addStaticEuEffInfo(float euEff) {
        addInfo(TT_StaticEuEff, TooltipHelper.effText(euEff));
        return this;
    }

    /**
     * Add a line of information about dynamic parallel count (tiered).
     *
     * @param euEff euEff Increment per tier
     * @param tier  Tiered object that determines bonus
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addDynamicEuEffInfo(float euEff, TooltipTier tier) {
        addInfo(TT_DynamicEuEff, TooltipHelper.effText("-" + percentageFormat.format(euEff)), tier.getValue());
        return this;
    }

    /**
     *
     * Add bulk information for parallels (voltageTier) , speed, euEff; in that order.
     *
     * @param parallels parallels per voltage tier
     * @param speed     Speed as defined in ProcessingLogic
     * @param euEff     EuEff as defined in ProcessingLogic
     */
    public MultiblockTooltipBuilder addBulkMachineInfo(int parallels, float speed, float euEff) {
        return addVoltageParallelInfo(parallels).addStaticSpeedInfo(speed)
            .addStaticEuEffInfo(euEff);
    }

    /**
     * For steam machines only.
     * Add a line of information about Steam Discount
     *
     * @param steamEff steamEff as defined in ProcessingLogic
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addStaticSteamEffInfo(float steamEff) {
        addInfo(TT_Steam_StaticSteamEff, TooltipHelper.effText(percentageFormat.format(steamEff)));
        return this;
    }

    /**
     * For steam machines only. Assumes static parallels / buffs.
     * Add bulk information for parallels (voltageTier) , speed, steamEff; in that order.
     *
     * @param parallels parallels
     * @param speed     Speed as defined in ProcessingLogic
     * @param steamEff  steamEff as defined in ProcessingLogic
     */
    public MultiblockTooltipBuilder addSteamBulkMachineInfo(int parallels, float speed, float steamEff) {
        return addStaticParallelInfo(parallels).addStaticSpeedInfo(speed)
            .addStaticSteamEffInfo(steamEff);
    }

    /**
     * Add a number of basic lines of information about this structure
     *
     * @param infoStrings The lines to be added.
     * @return Instance this method was called on.
     *
     * @deprecated Use {@link MultiblockTooltipBuilder#addInfo}<br>
     *             and use {@link GTUtility#YAP_SEPARATOR} ({@code \n}) in lang entries as separator.<br>
     *             Like {@code gt.a_multiblock.desc.1=Yaps a lot,\nso we have to separate it.}
     */
    @Deprecated
    public MultiblockTooltipBuilder addInfoAll(String... infoStrings) {
        for (String info : infoStrings) {
            addInfo(info);
        }
        return this;
    }

    /**
     * Add a separator line
     *
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addSeparator() {
        return addSeparator(EnumChatFormatting.GRAY, 0); // 0 is for backward compat, nothing else
    }

    /**
     * Add a colored separator line with specified length
     *
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addSeparator(EnumChatFormatting color, int length) {
        switch (GTMod.proxy.separatorStyle) {
            case 0 -> addInfo(" ");
            case 1 -> addInfo(color + "%SEPARATORLINE%");
            default -> addInfo("" + color + EnumChatFormatting.STRIKETHROUGH + "%SEPARATORLINE%");
        }
        return this;
    }

    /**
     * Add a line telling how much this machine pollutes.
     *
     * @param pollution Amount of pollution per second when active
     *
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addPollutionAmount(int pollution) {
        if (pollution == 0) return this;
        addInfo("GT5U.MBTT.CausesPollution", pollution);
        return this;
    }

    /**
     * Begin adding structural information by adding a line about the structure's dimensions and then inserting a
     * "Structure:" line.
     *
     * @param w      Structure width.
     * @param h      Structure height.
     * @param l      Structure depth/length.
     * @param hollow T/F, adds a (hollow) comment if true
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder beginStructureBlock(int w, int h, int l, boolean hollow) {
        addShiftInfo("GT5U.MBTT.Dimensions", w, h, l, (hollow ? "GT5U.MBTT.Hollow" : " "));
        addShiftInfo("GT5U.MBTT.Structure");
        return this;
    }

    /**
     * Begin adding structural information by adding a line about the structure's dimensions<br>
     * and then inserting a "Structure:" line. Variable version displays min and max
     *
     * @param wmin   Structure min width.
     * @param wmax   Structure max width.
     * @param hmin   Structure min height.
     * @param hmax   Structure max height.
     * @param lmin   Structure min depth/length.
     * @param lmax   Structure max depth/length.
     * @param hollow T/F, adds a (hollow) comment if true
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder beginVariableStructureBlock(int wmin, int wmax, int hmin, int hmax, int lmin,
        int lmax, boolean hollow) {
        addShiftInfo(
            "GT5U.MBTT.DimensionsVariable",
            wmin,
            wmax,
            hmin,
            hmax,
            lmin,
            lmax,
            (hollow ? "GT5U.MBTT.Hollow" : " "));
        addShiftInfo("GT5U.MBTT.Structure");
        return this;
    }

    /**
     * Add a line of structure info for where to set the controller block<br>
     * (indent)Controller: info
     *
     * @param info Lang key to positional information.<br>
     *             <b>Preset values:</b><br>
     *             {@code "fc"}: {@code Front center}, e.g. Oil Cracker<br>
     *             {@code "fbm"}: {@code Front bottom middle}, e.g. EBF
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addController(String info) {
        addStructurePart(
            "GT5U.MBTT.Controller",
            info.equals("fc") ? "gt.mb.corepos.fc" : info.equals("fbm") ? "gt.mb.corepos.fbm" : info);
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)countx casingName (tiered)
     *
     * @param casingName Name of the Casing.
     * @param isTiered   Flag if this casing accepts multiple tiers (e.g. coils)
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addCasingInfoExactly(String casingName, int count, boolean isTiered) {
        return addCasingInfoExactlyColored(
            casingName,
            EnumChatFormatting.GRAY,
            count,
            EnumChatFormatting.GOLD,
            isTiered);
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)countx casingName (tiered)
     *
     * @param casingName Name of the Casing.
     * @param isTiered   Flag if this casing accepts multiple tiers (e.g. coils)
     * @param countColor Color of the casing count text
     * @param textColor  Color of the casing name text
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addCasingInfoExactlyColored(String casingName, EnumChatFormatting textColor,
        int count, EnumChatFormatting countColor, boolean isTiered) {
        addStructureInfo(
            "" + countColor + count + "x " + textColor + "%s%s",
            translateToLocal(casingName),
            (isTiered ? "GT5U.MBTT.Tiered" : " "));
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)minCountx casingName (minimum) (tiered)
     *
     * @param casingName Name of the Casing.
     * @param minCount   Minimum needed for valid structure check.
     * @param isTiered   Flag if this casing accepts multiple tiers (e.g. coils), not specified = false
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addCasingInfoMin(String casingName, int minCount, boolean isTiered) {
        return addCasingInfoMinColored(
            casingName,
            EnumChatFormatting.GRAY,
            minCount,
            EnumChatFormatting.GOLD,
            isTiered);
    }

    public MultiblockTooltipBuilder addCasingInfoMin(String casingName, int minCount) {
        return addCasingInfoMin(casingName, minCount, false);
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)minCountx casingName (minimum) (tiered)
     *
     * @param casingName Name of the Casing.
     * @param minCount   Minimum needed for valid structure check.
     * @param isTiered   Flag if this casing accepts multiple tiers (e.g. coils)
     * @param countColor Color of the casing count text
     * @param textColor  Color of the casing name text
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addCasingInfoMinColored(String casingName, EnumChatFormatting textColor,
        int minCount, EnumChatFormatting countColor, boolean isTiered) {
        addStructureInfo(
            "" + countColor + minCount + "x " + textColor + "%s%s%s",
            translateToLocal(casingName),
            "GT5U.MBTT.Minimum",
            (isTiered ? "GT5U.MBTT.Tiered" : " "));
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)minCountx - maxCountx casingName (minimum) (tiered)
     *
     * @param casingName Name of the Casing.
     * @param minCount   Minimum needed for valid structure check.
     * @param maxCount   Maximum needed for valid structure check.
     * @param isTiered   Flag if this casing accepts multiple tiers (e.g. coils)
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addCasingInfoRange(String casingName, int minCount, int maxCount,
        boolean isTiered) {
        return addCasingInfoRangeColored(
            casingName,
            EnumChatFormatting.GRAY,
            minCount,
            maxCount,
            EnumChatFormatting.GOLD,
            isTiered);
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)minCountx - maxCountx casingName (minimum) (tiered)
     *
     * @param casingName Name of the Casing.
     * @param minCount   Minimum needed for valid structure check.
     * @param maxCount   Maximum needed for valid structure check.
     * @param isTiered   Flag if this casing accepts multiple tiers (e.g. coils)
     * @param countColor Color of the casing count text
     * @param textColor  Color of the casing name text
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addCasingInfoRangeColored(String casingName, EnumChatFormatting textColor,
        int minCount, int maxCount, EnumChatFormatting countColor, boolean isTiered) {
        addStructureInfo(
            "" + countColor
                + minCount
                + "x"
                + EnumChatFormatting.GRAY
                + " - "
                + countColor
                + maxCount
                + "x "
                + textColor
                + "%s%s",
            translateToLocal(casingName),
            (isTiered ? "GT5U.MBTT.Tiered" : " "));
        return this;
    }

    /**
     * Use this method to add a structural part that isn't covered by the other methods.<br>
     * (indent)name: info
     *
     * @param locKey Localization key of the hatch or other component.
     * @param info   Positional information.
     * @return Instance this method was called on.
     *
     * @deprecated Use {@link #addStructurePart(String, String)}
     */
    @Deprecated
    public MultiblockTooltipBuilder addOtherStructurePart(String locKey, String info) {
        addShiftInfo(TAB + "GT5U.MBTT.PartInfo", locKey, translateToLocal(info));
        return this;
    }

    /**
     * Use this method to add a structural part that isn't covered by the other methods.<br>
     * (indent)name: info
     *
     * @param locKey Localization key of the hatch or other component. This entry should be localized, otherwise the
     *               structure hints sent to the chat can't be localized.
     * @param info   Positional information.
     * @param dots   The valid locations for this part when asked to display hints
     * @return Instance this method was called on.
     *
     * @deprecated Use {@link #addStructurePartHinted(String, String, int...)}
     */
    @Deprecated
    public MultiblockTooltipBuilder addOtherStructurePart(String locKey, String info, int... dots) {
        addOtherStructurePart(locKey, info);
        for (int dot : dots) hBlocks.put(dot, translateToLocal(locKey));
        return this;
    }

    public MultiblockTooltipBuilder addStructurePart(String locKey, String info) {
        addStructureInfo("GT5U.MBTT.PartInfo", locKey, info);
        return this;
    }

    public MultiblockTooltipBuilder addStructurePartHinted(String locKey, String info, int... dots) {
        addStructurePart(locKey, hintLine(info, dots));
        for (int dot : dots) hBlocks.put(dot, translateToLocal(locKey));
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * Supports TecTech Multi-Amp Hatches!
     *
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addMultiAmpHatchInfo() {
        addInfo("GT5U.MBTT.TecTechMultiAmp");
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * Supports TecTech Multi-Amp and Laser Hatches!
     *
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addTecTechHatchInfo() {
        addInfo("GT5U.MBTT.TecTechHatch");
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * Machine does not lose efficiency when overclocked
     *
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addPerfectOCInfo() {
        addInfo("GT5U.MBTT.PerfectOC");
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * t-tier Glass required for TecTech Laser Hatches
     *
     * @param t Tier of glass that unlocks all energy hatches
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addMinGlassForLaser(int t) {
        addInfo("GT5U.MBTT.Structure.MinGlassForLaser", GTValues.TIER_COLORS[t], GTValues.VN[t]);
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * Energy Hatch limited by Glass tier
     *
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addGlassEnergyLimitInfo() {
        addInfo("GT5U.MBTT.Structure.GlassEnergyLimit");
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * Energy Hatch limited by Glass tier, t-tier Glass unlocks all
     *
     * @param t Tier of glass that unlocks all energy hatches
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addGlassEnergyLimitInfo(int t) {
        addInfo("GT5U.MBTT.Structure.GlassEnergyLimitTier", GTValues.TIER_COLORS[t], GTValues.VN[t]);
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * This machine can run recipes regardless of tier, if given enough energy
     *
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addNoTierSkips() {
        addInfo("GT5U.MBTT.Structure.NoTierSkips");
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * This machine can run recipes regardless of tier, if given enough energy
     *
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addUnlimitedTierSkips() {
        addInfo("GT5U.MBTT.Structure.UnlimitedTierSkips");
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * This machine can run recipes at most n tiers above the average energy hatch tier
     *
     * @param n The max amount of tier skips allowed
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addMaxTierSkips(int n) {
        addInfo("GT5U.MBTT.Structure.MaxTierSkips", n);
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)Maintenance Hatch: info<br>
     * {@code "<hint>"} as info to make it look like<br>
     * {@code (Hatch/Bus Type): Hint block with dot 1, 3, 5}
     *
     * @param info Positional information.
     * @param dots The valid locations for this part when asked to display hints
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addMaintenanceHatch(String info, int... dots) {
        addStructurePartHinted("GT5U.MBTT.MaintenanceHatch", info, dots);
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)Muffler Hatch: info<br>
     * {@code "<hint>"} as info to make it look like<br>
     * {@code (Hatch/Bus Type): Hint block with dot 1, 3, 5}
     *
     * @param info Location where the hatch goes
     * @param dots The valid locations for this part when asked to display hints
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addMufflerHatch(String info, int... dots) {
        addStructurePartHinted("GT5U.MBTT.MufflerHatch", info, dots);
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)Energy Hatch: info<br>
     * {@code "<hint>"} as info to make it look like<br>
     * {@code (Hatch/Bus Type): Hint block with dot 1, 3, 5}
     *
     * @param info Positional information.
     * @param dots The valid locations for this part when asked to display hints
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addEnergyHatch(String info, int... dots) {
        addStructurePartHinted("GT5U.MBTT.EnergyHatch", info, dots);
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)Dynamo Hatch: info<br>
     * {@code "<hint>"} as info to make it look like<br>
     * {@code (Hatch/Bus Type): Hint block with dot 1, 3, 5}
     *
     * @param info Positional information.
     * @param dots The valid locations for this part when asked to display hints
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addDynamoHatch(String info, int... dots) {
        addStructurePartHinted("GT5U.MBTT.DynamoHatch", info, dots);
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)Input Bus: info<br>
     * {@code "<hint>"} as info to make it look like<br>
     * {@code (Hatch/Bus Type): Hint block with dot 1, 3, 5}
     *
     * @param info Location where the bus goes
     * @param dots The valid locations for this part when asked to display hints
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addInputBus(String info, int... dots) {
        addStructurePartHinted("GT5U.MBTT.InputBus", info, dots);
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)Input Bus (Steam): info<br>
     * {@code "<hint>"} as info to make it look like<br>
     * {@code (Hatch/Bus Type): Hint block with dot 1, 3, 5}
     *
     * @param info Location where the bus goes
     * @param dots The valid locations for this part when asked to display hints
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addSteamInputBus(String info, int... dots) {
        addStructurePartHinted("GTPP.MBTT.SteamInputBus", info, dots);
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)Input Hatch: info<br>
     * {@code "<hint>"} as info to make it look like<br>
     * {@code (Hatch/Bus Type): Hint block with dot 1, 3, 5}
     *
     * @param info Location where the hatch goes
     * @param dots The valid locations for this part when asked to display hints
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addInputHatch(String info, int... dots) {
        addStructurePartHinted("GT5U.MBTT.InputHatch", info, dots);
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)Output Bus: info<br>
     * {@code "<hint>"} as info to make it look like<br>
     * {@code (Hatch/Bus Type): Hint block with dot 1, 3, 5}
     *
     * @param info Location where the bus goes
     * @param dots The valid locations for this part when asked to display hints
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addOutputBus(String info, int... dots) {
        addStructurePartHinted("GT5U.MBTT.OutputBus", info, dots);
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)Output Bus (Steam): info<br>
     * {@code "<hint>"} as info to make it look like<br>
     * {@code (Hatch/Bus Type): Hint block with dot 1, 3, 5}
     *
     * @param info Location where the bus goes
     * @param dots The valid locations for this part when asked to display hints
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addSteamOutputBus(String info, int... dots) {
        addStructurePartHinted("GTPP.MBTT.SteamOutputBus", info, dots);
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)Output Hatch: info<br>
     * {@code "<hint>"} as info to make it look like<br>
     * {@code (Hatch/Bus Type): Hint block with dot 1, 3, 5}
     *
     * @param info Location where the bus goes
     * @param dots The valid locations for this part when asked to display hints
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addOutputHatch(String info, int... dots) {
        addStructurePartHinted("GT5U.MBTT.OutputHatch", info, dots);
        return this;
    }

    /**
     * Use this method to add non-standard structural info.<br>
     * (indent)info<br>
     * Basically addShiftInfo with a TAB prepended
     *
     * @param info The line to be added.
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addStructureInfo(String info, Object... params) {
        addShiftInfo(TAB + info, params);
        return this;
    }

    public MultiblockTooltipBuilder addStructureInfo(String info) {
        addShiftInfo(TAB + info);
        return this;
    }

    /**
     * Add a colored separator line with specified length to structure info.<br>
     *
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addStructureInfoSeparator(EnumChatFormatting color, int length,
        boolean useFinisherConfig) {
        if (useFinisherConfig) {
            switch (GTMod.proxy.tooltipFinisherStyle) {
                case 0 -> {}
                case 1 -> addStructureInfo(" ");
                case 2 -> addStructureInfo(color + StringUtils.getRepetitionOf('-', length));
                default -> addStructureInfo(
                    color.toString() + EnumChatFormatting.STRIKETHROUGH + StringUtils.getRepetitionOf('-', length));
            }
        } else {
            switch (GTMod.proxy.separatorStyle) {
                case 0 -> addStructureInfo(" ");
                case 1 -> addStructureInfo(color + StringUtils.getRepetitionOf('-', length));
                default -> addStructureInfo(
                    "" + color + EnumChatFormatting.STRIKETHROUGH + StringUtils.getRepetitionOf('-', length));
            }
        }
        return this;
    }

    /**
     * Add a separator line to structure info.<br>
     *
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addStructureInfoSeparator() {
        return addStructureInfoSeparator(
            EnumChatFormatting.GRAY,
            (int) (translateToLocal("GT5U.MBTT.Structure.SeeStructure").replaceAll("§[0-9a-fk-or]", "")
                .length() * 0.8),
            false);
    }

    /**
     * @deprecated use overload that accepts {@link IStructureChannels} instead
     */
    @Deprecated
    public MultiblockTooltipBuilder addSubChannelUsage(String channel, String purpose) {
        addShiftInfo(TAB + translate("GT5U.MBTT.subchannel", channel, purpose));
        return this;
    }

    /**
     * Use this method to add non-standard structural info.<br>
     * (indent)info
     *
     * @param channel the name of subchannel
     * @param purpose the purpose of subchannel
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addSubChannelUsage(IStructureChannels channel, String purpose) {
        addShiftInfo(TAB + translate("GT5U.MBTT.subchannel", channel.get(), purpose));
        return this;
    }

    /**
     * Use this method to add non-standard structural info.<br>
     * (indent)info
     *
     * @param channel the name of subchannel
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addSubChannelUsage(IStructureChannels channel) {
        addShiftInfo(TAB + translate("GT5U.MBTT.subchannel", channel.get(), channel.getDefaultTooltip()));
        return this;
    }

    /**
     * Use this method to add non-standard structural hint. This info will appear before the standard structural hint.
     *
     * @param info The line to be added. This should be an entry into minecraft's localization system.
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addStructureHint(String info) {
        hLines.add(translateToLocal(info));
        return this;
    }

    /**
     * Use this method to add an entry to standard structural hint without creating a corresponding line in structure
     * information
     *
     * @param nameKey The name of block This should be an entry into minecraft's localization system.
     * @param dots    Possible locations of this block
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addStructureHint(String nameKey, int... dots) {
        for (int dot : dots) hBlocks.put(dot, translateToLocal(nameKey));
        return this;
    }

    /**
     * Useful for maintaining the flow when you need to run an arbitrary operation on the builder.
     *
     * @param fn The operation.
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder pipe(Consumer<MultiblockTooltipBuilder> fn) {
        fn.accept(this);
        return this;
    }

    /**
     * Call at the very end.<br>
     * Adds a line jump.<br>
     * Adds information on how to display the structure guidelines.<br>
     * Adds credit for creators of this multi, if any.<br>
     * <p>
     * Ends the building process.
     *
     * @param authors Formatted names of the creators of this multiblock machine - if any
     */
    public MultiblockTooltipBuilder toolTipFinisher(@Nullable String... authors) {
        return toolTipFinisher(EnumChatFormatting.GRAY, 0, authors);
    }

    /**
     * Call at the very end.<br>
     * Adds a line jump with configurable color and length.<br>
     * Adds information on how to display the structure guidelines.<br>
     * Adds credit for creators of this multi, if any.<br>
     * <p>
     * Ends the building process.
     *
     * @param separatorColor Color of the separator line
     * @param authors        Formatted names of the creators of this multiblock machine - if any
     */

    public MultiblockTooltipBuilder toolTipFinisher(EnumChatFormatting separatorColor, int length,
        @Nullable String... authors) {

        switch (GTMod.proxy.tooltipFinisherStyle) {
            case 0 -> {}
            case 1 -> addInfo(" ");
            case 2 -> addInfo(separatorColor + "%SEPARATORLINE%");
            default -> addInfo(separatorColor.toString() + EnumChatFormatting.STRIKETHROUGH + "%SEPARATORLINE%");
        }

        addInfo("GT5U.MBTT.HoldDisplay");
        if (authors != null && authors.length > 0) {
            String[] processedAuthors = Arrays.stream(authors)
                .map(author -> author.startsWith("Author: ") ? author.substring("Author: ".length()) : author)
                .toArray(String[]::new);

            addInfo(
                "GT5U.MBTT.Authors",
                String.join(EnumChatFormatting.GRAY + " & " + EnumChatFormatting.GREEN, processedAuthors));
        }
        hLines.add(TT_structurehint);
        this.addStructureInfoSeparator(
            EnumChatFormatting.GRAY,
            (int) (translateToLocal("GT5U.MBTT.Structure.SeeStructure").replaceAll("§[0-9a-fk-or]", "")
                .length() * 0.8),
            true);
        addShiftInfo("GT5U.MBTT.Structure.Complex");
        addShiftInfo("GT5U.MBTT.Structure.SeeStructure");
        // e.getKey() - 1 because 1 dot is meta 0.
        hArray = Stream.concat(
            hLines.stream(),
            hBlocks.asMap()
                .entrySet()
                .stream()
                .map(e -> TT_dots[e.getKey() - 1] + COLON + String.join(SEPARATOR, e.getValue())))
            .toArray(String[]::new);
        // free memory
        hLines = null;
        hBlocks = null;
        return this;
    }

    public String[] getInformation() {
        return getStrings(iLines);
    }

    public String[] getStructureInformation() {
        return getStrings(sLines);
    }

    public String[] getStructureHint() {
        return hArray;
    }

    private String hintLine(String info, int... dots) {
        return info.equals("<hint>") ? String.format(
            TT_partinfohint,
            Joiner.on(SEPARATOR)
                .join(Ints.asList(dots)))
            : info;
    }

    @Desugar
    private record TooltipLine(String text, Object... params) {}

    @NotNull
    private String[] getStrings(List<TooltipLine> xLines) {
        return xLines.stream()
            .map(line -> {
                if (line.params.length == 0) {
                    return line.text;
                } else {
                    StringBuilder sb = new StringBuilder(line.text);
                    for (Object param : line.params) {
                        sb.append(GTUtility.LOC_SEPARATOR)
                            .append(param.toString());
                    }
                    return sb.toString();
                }
            })
            .toArray(String[]::new);
    }
}
