package gregtech.api.util;

import static gregtech.api.util.GTUtility.nestParams;
import static gregtech.api.util.tooltip.TooltipHelper.percentageFormat;
import static gregtech.api.util.tooltip.TooltipMarkupProcessor.FINISHER_MARK;
import static gregtech.api.util.tooltip.TooltipMarkupProcessor.INDENT_MARK;
import static gregtech.api.util.tooltip.TooltipMarkupProcessor.SEPARATOR_MARK;
import static gregtech.api.util.tooltip.TooltipMarkupProcessor.STRUCTURE_SEPARATOR_MARK;
import static org.apache.commons.lang3.StringUtils.removeEnd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import com.github.bsideup.jabel.Desugar;
import com.google.common.base.Strings;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import com.gtnewhorizon.structurelib.StructureLibAPI;

import gregtech.api.enums.GTAuthors;
import gregtech.api.enums.GTValues;
import gregtech.api.structure.IStructureChannels;
import gregtech.api.util.tooltip.DelayedTooltipLocalization;
import gregtech.api.util.tooltip.MarkdownTooltipLoader;
import gregtech.api.util.tooltip.TooltipHelper;
import gregtech.api.util.tooltip.TooltipTier;

/**
 * This makes it easier to build multiblock tooltips, with a standardized format. <br>
 * Info section order should be:<br>
 * addMachineType<br>
 * addInfo, for what it does, special notes, etc.<br>
 * addSeparator, if you need it<br>
 * addPollutionAmount<br>
 * <br>
 * Structure order should be:<br>
 * beginStructureBlock<br>
 * addController<br>
 * addCasing<br>
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

    private static final DelayedTooltipLocalization DELAYED_TOOLTIP_LOCALIZATION = new DelayedTooltipLocalization(
        (key, parameters) -> parameters == null || parameters.length == 0 ? StatCollector.translateToLocal(key)
            : StatCollector.translateToLocalFormatted(key, parameters));

    private final List<Object> iLines;
    private final List<Object> sLines;
    private List<TooltipLine> hLines;
    private List<String> authors;
    private List<String> structureAuthors;
    private SetMultimap<Integer, String> hBlocks;
    private List<TooltipLine> finalizedHintLines;
    private List<StructureHintEntry> finalizedHintBlocks;

    public MultiblockTooltipBuilder() {
        iLines = new LinkedList<>();
        sLines = new LinkedList<>();
        hLines = new LinkedList<>();
        authors = new LinkedList<>();
        structureAuthors = new LinkedList<>();
        hBlocks = Multimaps.newSetMultimap(new HashMap<>(), HashSet::new);
        hBlocks.put(StructureLibAPI.HINT_BLOCK_META_AIR, "GT5U.MBTT.Air");
    }

    /**
     * Add a line telling you what the machine type is. Usually, this will be the name of an SB version.<br>
     * Machine Type: machine<br>
     * Provide multiple params for multifunctional machines, divided by "|"<br>
     * Acronyms and aliases should NOT be made a separate param. It should be like<br>
     * Machine Type: Big Bad Machine, BBM | Furnace<br>
     * but not Machine Type: Big Bad Machine | BBM | Furnace
     * Check if the machine has multiple recipe groups in NEI
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
     * Add a basic line of information about this structure.
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
     * Add a deprecation line to the tooltip.
     * The line is prefixed with a dark red {@code "DEPRECATED - "} label
     * followed by the provided additional information.
     *
     * @param info additional explanation shown after the {@code DEPRECATED} label
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addDeprecatedLine(String info) {
        addInfo("GT5U.MBTT.Deprecated", info);
        return this;
    }

    /**
     * Add a line that states this multi will be deprecated in next major version.
     * Specifically for use with structure deprecation.
     *
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addStructureDeprecatedLine() {
        addDeprecatedLine("GT5U.MBTT.Deprecated.Removal");
        addInfo("GT5U.MBTT.Deprecated.NEI");
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
            parallels == 1 ? "GT5U.MBTT.Parallel.Singular" : "GT5U.MBTT.Parallel.Additional",
            TooltipHelper.parallelText(parallels),
            tier.getLocalizedValue());
        return this;
    }

    /**
     * Add a line of information about parallel count per Voltage Tier.
     * "Processes %s items per Voltage Tier."
     *
     * @param parallels Amount of parallels gained per Voltage tier
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addVoltageParallelInfo(int parallels) {
        return addDynamicParallelInfo(parallels, TooltipTier.VOLTAGE);
    }

    /**
     * Add a line of information about multiplicative parallels per Tier.
     * "%sx Parallels per %s Tier".
     *
     * @param factor parallel multiplier
     * @param tier   Tiered object that determines bonus
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addDynamicMultiplicativeParallelInfo(Integer factor, TooltipTier tier) {
        addInfo(
            "GT5U.MBTT.Parallel.Additional",
            TooltipHelper.parallelText(factor.toString() + "x"),
            tier.getLocalizedValue());
        return this;
    }

    /**
     * Add a line of information about Speed bonus.
     *
     * @param speed Speed as defined in ProcessingLogic (ie. 1F / 3.5F = 350% Speed)
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addStaticSpeedInfo(float speed) {

        addInfo("GT5U.MBTT.Speed.Base", TooltipHelper.speedText(speed));
        return this;
    }

    /**
     * Add a line of information about dynamic parallel count (tiered).
     *
     * @param speed Speed increment per tier
     * @param tier  Tiered object that determines bonus
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addDynamicSpeedBonusInfo(float speed, TooltipTier tier) {
        addInfo(
            "GT5U.MBTT.Speed.Additional",
            TooltipHelper.speedText("+" + percentageFormat.format(speed)),
            tier.getLocalizedValue());
        return this;
    }

    public MultiblockTooltipBuilder addDynamicSpeedInfo(float speed, TooltipTier tier) {
        addInfo(
            "GT5U.MBTT.Speed.Absolute",
            TooltipHelper.speedText(percentageFormat.format(speed)),
            tier.getLocalizedValue());
        return this;
    }

    /**
     * Add a line of information about EU Discount bonus relative to SB machines.
     *
     * @param euEff euEff as defined in ProcessingLogic
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addStaticEuEffInfo(float euEff) {
        addInfo("GT5U.MBTT.EuDiscount.Base", TooltipHelper.effText(euEff));
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
        addInfo(
            "GT5U.MBTT.EuDiscount.Additional",
            TooltipHelper.effText("-" + percentageFormat.format(euEff)),
            tier.getLocalizedValue());
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
     * Add a line of information about Steam Discount.
     *
     * @param steamEff steamEff as defined in ProcessingLogic
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addStaticSteamEffInfo(float steamEff) {
        addInfo("GT5U.MBTT.SteamDiscount.Base", TooltipHelper.effText(percentageFormat.format(steamEff)));
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
     * Add a number of basic lines of information about this structure.
     *
     * @param infoStrings The lines to be added
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addInfoAll(String... infoStrings) {
        for (String info : infoStrings) addInfo(info);
        return this;
    }

    public MultiblockTooltipBuilder addMarkdown(ResourceLocation loc) {
        return addMarkdown(loc, Collections.emptyMap());
    }

    public MultiblockTooltipBuilder addMarkdown(ResourceLocation loc, Map<String, Object> vars) {
        for (String line : MarkdownTooltipLoader.STANDARD.loadStandardPath(loc, vars)) addInfo(line);
        return this;
    }

    /**
     * Add a separator line.
     *
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addSeparator() {
        return addSeparator(EnumChatFormatting.GRAY, 41);
    }

    /**
     * Add a colored separator line.
     *
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addSeparator(EnumChatFormatting color) {
        return addSeparator(color, 41);
    }

    /// Tooltip separators are spaces
    public static final int TT_SEPARATOR_SPACES = 0;
    /// Tooltip separators are dashed lines
    public static final int TT_SEPARATOR_DASHES = 1;
    /// Tooltip separators are continuous lines
    public static final int TT_SEPARATOR_SOLID_LINE = 2;

    /**
     * Add a colored separator line with a specified length.
     *
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addSeparator(EnumChatFormatting color, int length) {
        addInfo(color + SEPARATOR_MARK);
        return this;
    }

    /**
     * Add a line telling how much this machine pollutes.
     *
     * @param pollution Amount of pollution per second when active
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
     * @param l      Structure length/depth
     * @param w      Structure width
     * @param h      Structure height
     * @param hollow Flag for the (Hollow) tag
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder beginStructureBlock(int l, int w, int h, boolean hollow) {
        addShiftInfo(
            "GT5U.MBTT.Dimensions",
            l,
            w,
            h,
            hollow ? "GT5U.MBTT.Hollow" : "");
        addShiftInfo("GT5U.MBTT.Structure");
        return this;
    }

    /**
     * Begin adding structural information by adding a line about the structure's dimensions<br>
     * and then inserting a "Structure:" line. Variable version displays min and max.
     *
     * @param lmin   Structure min length/depth
     * @param lmax   Structure max length/depth
     * @param wmin   Structure min width
     * @param wmax   Structure max width
     * @param hmin   Structure min height
     * @param hmax   Structure max height
     * @param hollow Flag for the (Hollow) tag
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder beginVariableStructureBlock(int lmin, int lmax, int wmin, int wmax, int hmin,
        int hmax, boolean hollow) {
        addShiftInfo(
            "GT5U.MBTT.Dimensions",
            formatRange(lmin, lmax),
            formatRange(wmin, wmax),
            formatRange(hmin, hmax),
            hollow ? "GT5U.MBTT.Hollow" : "");
        addShiftInfo("GT5U.MBTT.Structure");
        return this;
    }

    private static String formatRange(int min, int max) {
        return min == max ? Integer.toString(min) : min + "-" + max;
    }

    /**
     * Add a line of structure info for where to set the controller block<br>
     * (indent)Controller: info
     *
     * @param info Lang key to positional information.<br>
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addController(String info) {
        addStructurePart("GT5U.MBTT.Controller", new PrefixedFallbackText("gt.mbtt.structure.", info, info), false);
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)count name (Tiered)
     *
     * @param count    Number of casings
     * @param name     Name of casing
     * @param isTiered Flag for the (Tiered) tag
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addCasing(String count, String name, boolean isTiered) {
        return addCasingLine(name, EnumChatFormatting.WHITE, count, EnumChatFormatting.GOLD, false, isTiered);
    }

    /**
     * @deprecated Use addCasing() instead
     *             Add a line of information about the structure:<br>
     *             (indent)count name (Tiered)
     *
     * @param count    Number of casings
     * @param name     Name of casing
     * @param isTiered Flag for the (Tiered) tag
     * @return Instance this method was called on.
     */
    @Deprecated
    public MultiblockTooltipBuilder addCasingInfo(String count, String name, boolean isTiered) {
        return addCasing(count, name, isTiered);
    }

    public MultiblockTooltipBuilder addCasingInfoMin(String casingName, int minCount) {
        return addCasingInfoMin(casingName, minCount, false);
    }

    /**
     * @deprecated use addCasing() instead
     *             Add a line of information about the structure:<br>
     *             (indent)count name (Tiered)
     *
     * @param name     Name of casing
     * @param isTiered Flag for the (Tiered) tag
     * @return Instance this method was called on.
     */
    @Deprecated
    public MultiblockTooltipBuilder addCasingInfoExactly(String name, int count, boolean isTiered) {
        return addCasingInfoExactlyColored(name, EnumChatFormatting.WHITE, count, EnumChatFormatting.GOLD, isTiered);
    }

    /**
     * @deprecated use addCasing() instead
     *             Add a line of information about the structure:<br>
     *             (indent)count name (Tiered)
     *
     * @param name       Name of casing
     * @param isTiered   Flag for the (Tiered) tag
     * @param countColor Color of the count text
     * @param textColor  Color of the name text
     * @return Instance this method was called on.
     */
    @Deprecated
    public MultiblockTooltipBuilder addCasingInfoExactlyColored(String name, EnumChatFormatting textColor, int count,
        EnumChatFormatting countColor, boolean isTiered) {
        return addCasingLine(name, textColor, Integer.toString(count), countColor, false, isTiered);
    }

    /**
     * @deprecated use addCasing() instead
     *             Add a line of information about the structure:<br>
     *             (indent)minCount+ name (Tiered)
     *
     * @param name     Name of casing
     * @param minCount Minimum number of casings
     * @param isTiered Flag for the (Tiered) tag
     * @return Instance this method was called on.
     */
    @Deprecated
    public MultiblockTooltipBuilder addCasingInfoMin(String name, int minCount, boolean isTiered) {
        return addCasingLine(name, EnumChatFormatting.WHITE, minCount + "+", EnumChatFormatting.GOLD, false, isTiered);
    }

    /**
     * @deprecated use addCasing() instead
     *             Add a line of information about the structure:<br>
     *             (indent)minCount name (Minimum) (Tiered)
     *
     * @param name       Name of casing
     * @param minCount   Minimum number of casings
     * @param isTiered   Flag for the (Tiered) tag
     * @param countColor Color of the count text
     * @param textColor  Color of the name text
     * @return Instance this method was called on.
     */
    @Deprecated
    public MultiblockTooltipBuilder addCasingInfoMinColored(String name, EnumChatFormatting textColor, int minCount,
        EnumChatFormatting countColor, boolean isTiered) {
        return addCasingLine(name, textColor, Integer.toString(minCount), countColor, true, isTiered);
    }

    /**
     * @deprecated use addCasing() instead
     *             Add a line of information about the structure:<br>
     *             (indent)minCount-maxCount name (Tiered)
     *
     * @param name     Name of casing
     * @param minCount Minimum number of casings
     * @param maxCount Maximum number of casings
     * @param isTiered Flag for the (Tiered) tag
     * @return Instance this method was called on.
     */
    @Deprecated
    public MultiblockTooltipBuilder addCasingInfoRange(String name, int minCount, int maxCount, boolean isTiered) {
        return addCasingLine(
            name,
            EnumChatFormatting.WHITE,
            minCount + "-" + maxCount,
            EnumChatFormatting.GOLD,
            false,
            isTiered);
    }

    /**
     * @deprecated use addCasing() instead
     *             Add a line of information about the structure:<br>
     *             (indent)minCount-maxCount name (Minimum) (Tiered)
     *
     * @param name       Name of casing
     * @param minCount   Minimum number of casings
     * @param maxCount   Maximum number of casings
     * @param isTiered   Flag for the (Tiered) tag
     * @param countColor Color of the count text
     * @param textColor  Color of the name text
     * @return Instance this method was called on.
     */
    @Deprecated
    public MultiblockTooltipBuilder addCasingInfoRangeColored(String name, EnumChatFormatting textColor, int minCount,
        int maxCount, EnumChatFormatting countColor, boolean isTiered) {
        return addCasingLine(name, textColor, minCount + "-" + maxCount, countColor, false, isTiered);
    }

    private MultiblockTooltipBuilder addCasingLine(String name, EnumChatFormatting textColor, String count,
        EnumChatFormatting countColor, boolean isMinimum, boolean isTiered) {
        addShiftInfo(
            "GT5U.MBTT.Casing.Format",
            countColor,
            count,
            textColor,
            name,
            isMinimum ? new TooltipLine("GT5U.MBTT.Casing.MinimumSuffix", "GT5U.MBTT.Minimum") : "",
            isTiered ? new TooltipLine("GT5U.MBTT.Casing.TieredSuffix", "GT5U.MBTT.Tiered") : "");
        return this;
    }

    /**
     * @deprecated use method that includes count and hint dots instead.
     *             Use this method to add a structural part that isn't covered by the other methods.<br>
     *             (indent)name: info
     *
     * @param name Name of component
     * @param info Location description
     * @return Instance this method was called on.
     */
    @Deprecated
    public MultiblockTooltipBuilder addOtherStructurePart(String name, String info) {
        return addLegacyStructurePart(name, info);
    }

    /**
     * @deprecated use method that includes count and hint dots instead.
     *             Add a line of information about the structure:<br>
     *             (indent)Maintenance Hatch: info
     *
     * @param info Location description
     * @return Instance this method was called on.
     */
    @Deprecated
    public MultiblockTooltipBuilder addMaintenanceHatch(String info) {
        return addLegacyStructurePart("GT5U.MBTT.MaintenanceHatch", info);
    }

    /**
     * @deprecated use method that includes count and hint dots instead.
     *             Add a line of information about the structure:<br>
     *             (indent)Muffler Hatch: info
     *
     * @param info Location description
     * @return Instance this method was called on.
     */
    @Deprecated
    public MultiblockTooltipBuilder addMufflerHatch(String info) {
        return addLegacyStructurePart("GT5U.MBTT.MufflerHatch", info);
    }

    /**
     * @deprecated use method that includes count and hint dots instead.
     *             Add a line of information about the structure:<br>
     *             (indent)Energy Hatch: info
     *
     * @param info Location description
     * @return Instance this method was called on.
     */
    @Deprecated
    public MultiblockTooltipBuilder addEnergyHatch(String info) {
        return addLegacyStructurePart("GT5U.MBTT.EnergyHatch", info);
    }

    /**
     * @deprecated use method that includes count and hint dots instead.
     *             Add a line of information about the structure:<br>
     *             (indent)Dynamo Hatch: info
     *
     * @param info Location description
     * @return Instance this method was called on.
     */
    @Deprecated
    public MultiblockTooltipBuilder addDynamoHatch(String info) {
        return addLegacyStructurePart("GT5U.MBTT.DynamoHatch", info);
    }

    /**
     * @deprecated use method that includes count and hint dots instead.
     *             Add a line of information about the structure:<br>
     *             (indent)Input Bus: info
     *
     * @param info Location description
     * @return Instance this method was called on.
     */
    @Deprecated
    public MultiblockTooltipBuilder addInputBus(String info) {
        return addLegacyStructurePart("GT5U.MBTT.InputBus", info);
    }

    /**
     * @deprecated use method that includes count and hint dots instead.
     *             Add a line of information about the structure:<br>
     *             (indent)Steam Input Bus: info
     *
     * @param info Location description
     * @return Instance this method was called on.
     */
    @Deprecated
    public MultiblockTooltipBuilder addSteamInputBus(String info) {
        return addLegacyStructurePart("GTPP.MBTT.SteamInputBus", info);
    }

    /**
     * @deprecated use method that includes count and hint dots instead.
     *             Add a line of information about the structure:<br>
     *             (indent)Input Hatch: info
     *
     * @param info Location description
     * @return Instance this method was called on.
     */
    @Deprecated
    public MultiblockTooltipBuilder addInputHatch(String info) {
        return addLegacyStructurePart("GT5U.MBTT.InputHatch", info);
    }

    /**
     * @deprecated use method that includes count and hint dots instead.
     *             Add a line of information about the structure:<br>
     *             (indent)Output Bus: info
     *
     * @param info Location description
     * @return Instance this method was called on.
     */
    @Deprecated
    public MultiblockTooltipBuilder addOutputBus(String info) {
        return addLegacyStructurePart("GT5U.MBTT.OutputBus", info);
    }

    /**
     * @deprecated use method that includes count and hint dots instead.
     *             Add a line of information about the structure:<br>
     *             (indent)Steam Output Bus: info
     *
     * @param info Location description
     * @return Instance this method was called on.
     */
    @Deprecated
    public MultiblockTooltipBuilder addSteamOutputBus(String info) {
        return addLegacyStructurePart("GTPP.MBTT.SteamOutputBus", info);
    }

    /**
     * @deprecated use method that includes count and hint dots instead.
     *             Add a line of information about the structure:<br>
     *             (indent)Output Hatch: info
     *
     * @param info Location description
     * @return Instance this method was called on.
     */
    @Deprecated
    public MultiblockTooltipBuilder addOutputHatch(String info) {
        return addLegacyStructurePart("GT5U.MBTT.OutputHatch", info);
    }

    private MultiblockTooltipBuilder addLegacyStructurePart(String name, String info) {
        addStructureInfo("GT5U.MBTT.PartInfo", name, resolveStructureInfo(info));
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
     * @deprecated Use {@link #addStructurePart(String, String, int...)}
     */
    @Deprecated
    public MultiblockTooltipBuilder addOtherStructurePart(String locKey, String info, int... dots) {
        addOtherStructurePart(locKey, info);
        addStructureHint(locKey, dots);
        return this;
    }

    public MultiblockTooltipBuilder addStructurePart(String partLocKey, String info, boolean addHintInfo,
        int... hintDots) {
        return addStructurePart(partLocKey, (Object) info, addHintInfo, hintDots);
    }

    private MultiblockTooltipBuilder addStructurePart(String partLocKey, Object info, boolean addHintInfo,
        int... hintDots) {
        Object dotStr = hintDots.length == 0 ? "???" : joinHintDots(hintDots);

        if (info instanceof String text && text.equalsIgnoreCase("<hint>")) {
            addStructureInfo("GT5U.MBTT.PartInfo", partLocKey, new TooltipLine("GT5U.MBTT.PartHintDesc", dotStr));
        } else {
            Object resolvedInfo = resolveStructureInfo(info);
            if (addHintInfo) {
                addStructureInfo("GT5U.MBTT.PartInfoWithHint", partLocKey, resolvedInfo, dotStr);
            } else {
                addStructureInfo("GT5U.MBTT.PartInfo", partLocKey, resolvedInfo);
            }
        }

        if (hintDots.length > 0) {
            addStructureHint(partLocKey, hintDots);
        }

        return this;
    }

    private TooltipLine joinHintDots(int... hintDots) {
        StringBuilder format = new StringBuilder();
        Object[] params = new Object[hintDots.length * 2 - 1];
        for (int i = 0; i < hintDots.length; i++) {
            if (i > 0) {
                format.append("%s");
                params[i * 2 - 1] = "GT5U.MBTT.ListSeparator";
            }
            format.append("%s");
            params[i * 2] = hintDots[i];
        }
        return new TooltipLine(format.toString(), params);
    }

    private Object resolveStructureInfo(Object info) {
        if (!(info instanceof String text)) return info;

        return switch (text.toLowerCase(Locale.ROOT)) {
            case "<casing>", "any casing" -> "GT5U.MBTT.AnyCasing";
            case "<bottom casing>", "bottom casing" -> "GT5U.MBTT.AnyBottomCasing";
            case "<top casing>", "top casing" -> "GT5U.MBTT.AnyTopCasing";
            default -> text;
        };
    }

    public MultiblockTooltipBuilder addStructurePart(String partLocKey, String info, int... hintDots) {
        addStructurePart(partLocKey, info, false, hintDots);
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)count Energy Hatch: info
     *
     * @param count Number of hatches (ie. 1+)
     * @param info  Location description
     * @param dots  Hint block(s)
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addEnergyHatch(String count, String info, int... dots) {
        return addCountedStructurePart(count, "GT5U.MBTT.EnergyHatch", info, dots);
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)count Dynamo Hatch: info
     *
     * @param count Number of hatches (ie. 1)
     * @param info  Location description
     * @param dots  Hint block(s)
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addDynamoHatch(String count, String info, int... dots) {
        return addCountedStructurePart(count, "GT5U.MBTT.DynamoHatch", info, dots);
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)count Maintenance Hatch: info
     *
     * @param count Number of hatches (ie. 1)
     * @param info  Location description
     * @param dots  Hint block(s)
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addMaintenanceHatch(String count, String info, int... dots) {
        return addCountedStructurePart(count, "GT5U.MBTT.MaintenanceHatch", info, dots);
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)count Muffler Hatch: info
     *
     * @param count Number of buses (ie. 1)
     * @param info  Location description
     * @param dots  Hint block(s)
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addMufflerHatch(String count, String info, int... dots) {
        return addCountedStructurePart(count, "GT5U.MBTT.MufflerHatch", info, dots);
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)count Steam Input Bus: info
     *
     * @param count Number of buses (ie. 1+)
     * @param info  Location description
     * @param dots  Hint block(s)
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addSteamInputBus(String count, String info, int... dots) {
        return addCountedStructurePart(count, "GTPP.MBTT.SteamInputBus", info, dots);
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)count Input Bus: info
     *
     * @param count Number of buses (ie. 1+)
     * @param info  Location description
     * @param dots  Hint block(s)
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addInputBus(String count, String info, int... dots) {
        return addCountedStructurePart(count, "GT5U.MBTT.InputBus", info, dots);
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)count Input Hatch: info
     *
     * @param count Number of hatches (ie. 1+)
     * @param info  Location description
     * @param dots  Hint block(s)
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addInputHatch(String count, String info, int... dots) {
        return addCountedStructurePart(count, "GT5U.MBTT.InputHatch", info, dots);
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)count Input Bus/Hatch: info
     *
     * @param count Number of buses/hatches (ie. 1+)
     * @param info  Location description
     * @param dots  Hint block(s)
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addInputAny(String count, String info, int... dots) {
        return addCountedStructurePart(count, "GT5U.MBTT.InputAny", info, dots);
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)count Steam Hatch: info
     *
     * @param count Number of hatches (ie. 1+)
     * @param info  Location description
     * @param dots  Hint block(s)
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addSteamHatch(String count, String info, int... dots) {
        return addCountedStructurePart(count, "GTPP.MBTT.SteamHatch", info, dots);
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)count Steam Output Bus: info
     *
     * @param count Number of buses (ie. 1+)
     * @param info  Location description
     * @param dots  Hint block(s)
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addSteamOutputBus(String count, String info, int... dots) {
        return addCountedStructurePart(count, "GTPP.MBTT.SteamOutputBus", info, dots);
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)count Output Bus: info
     *
     * @param count Number of buses (ie. 1+)
     * @param info  Location description
     * @param dots  Hint block(s)
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addOutputBus(String count, String info, int... dots) {
        return addCountedStructurePart(count, "GT5U.MBTT.OutputBus", info, dots);
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)count Output Hatch: info
     *
     * @param count Number of hatches (ie. 1+)
     * @param info  Location description
     * @param dots  Hint block(s)
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addOutputHatch(String count, String info, int... dots) {
        return addCountedStructurePart(count, "GT5U.MBTT.OutputHatch", info, dots);
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)count Output Bus/Hatch: info
     *
     * @param count Number of buses/hatches (ie. 1+)
     * @param info  Location description
     * @param dots  Hint block(s)
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addOutputAny(String count, String info, int... dots) {
        return addCountedStructurePart(count, "GT5U.MBTT.OutputAny", info, dots);
    }

    private MultiblockTooltipBuilder addCountedStructurePart(String count, String name, String info, int... dots) {
        addShiftInfo(
            INDENT_MARK + EnumChatFormatting.YELLOW + "%s " + EnumChatFormatting.WHITE + "%s",
            count,
            new TooltipLine("GT5U.MBTT.PartInfo", name, resolveStructureInfo(info)));
        addStructureHint(name, dots);
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)count name: info
     *
     * @param count Number of hatches (ie. 1+)
     * @param name  Name of hatch
     * @param info  Location description
     * @param dots  Hint block(s)
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addMiscHatch(String count, String name, String info, int... dots) {
        return addCountedStructurePart(count, name, info, dots);
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)Mandatory Air: info
     *
     * @param info Location description
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addAir(String info) {
        return addStructurePart("GT5U.MBTT.Air", info);
    }

    /**
     * Add a line of information about the structure:<br>
     * Supports Multi-Amp Hatches!
     *
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addSupportMultiAmp() {
        addInfo("%s%s", EnumChatFormatting.GREEN, "GT5U.MBTT.SupportMultiAmp");
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * Supports Laser Hatches!
     *
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addSupportLaser() {
        addInfo("%s%s", EnumChatFormatting.GREEN, "GT5U.MBTT.SupportLaser");
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * Supports Multi-Amp and Laser Hatches!
     *
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addSupportAny() {
        addInfo("%s%s", EnumChatFormatting.GREEN, "GT5U.MBTT.SupportAny");
        return this;
    }

    /**
     * @deprecated use addSupportMultiAmp() instead.
     *             Add a line of information about the structure:<br>
     *             Supports Multi-Amp Hatches!
     *
     * @return Instance this method was called on.
     */
    @Deprecated
    public MultiblockTooltipBuilder addMultiAmpHatchInfo() {
        return addSupportMultiAmp();
    }

    /**
     * @deprecated use addSupportAny() instead.
     *             Add a line of information about the structure:<br>
     *             Supports Multi-Amp and Laser Hatches!
     *
     * @return Instance this method was called on.
     */
    @Deprecated
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
     * t-tier Glass required for Laser Hatches
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
    @Deprecated
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
     * @param n The max number of tier skips allowed
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addMaxTierSkips(int n) {
        addInfo("GT5U.MBTT.Structure.MaxTierSkips", n);
        return this;
    }

    /**
     * @deprecated use method that includes count and hint dots instead.
     *             Add a line of information about the structure:<br>
     *             (indent)Maintenance Hatch: info
     *
     * @param info Location description
     * @param dots Hint block(s)
     * @return Instance this method was called on.
     */
    @Deprecated
    public MultiblockTooltipBuilder addMaintenanceHatch(String info, int... dots) {
        addStructurePart("GT5U.MBTT.MaintenanceHatch", info, dots);
        return this;
    }

    /**
     * @deprecated use method that includes count and hint dots instead.
     *             Add a line of information about the structure:<br>
     *             (indent)Muffler Hatch: info
     *
     * @param info Location description
     * @param dots Hint block(s)
     * @return Instance this method was called on.
     */
    @Deprecated
    public MultiblockTooltipBuilder addMufflerHatch(String info, int... dots) {
        addStructurePart("GT5U.MBTT.MufflerHatch", info, dots);
        return this;
    }

    /**
     * @deprecated use method that includes count and hint dots instead.
     *             Add a line of information about the structure:<br>
     *             (indent)Energy Hatch: info
     *
     * @param info Location description
     * @param dots Hint block(s)
     * @return Instance this method was called on.
     */
    @Deprecated
    public MultiblockTooltipBuilder addEnergyHatch(String info, int... dots) {
        addStructurePart("GT5U.MBTT.EnergyHatch", info, dots);
        return this;
    }

    /**
     * @deprecated use method that includes count and hint dots instead.
     *             Add a line of information about the structure:<br>
     *             (indent)Dynamo Hatch: info
     *
     * @param info Location description
     * @param dots Hint block(s)
     * @return Instance this method was called on.
     */
    @Deprecated
    public MultiblockTooltipBuilder addDynamoHatch(String info, int... dots) {
        addStructurePart("GT5U.MBTT.DynamoHatch", info, dots);
        return this;
    }

    /**
     * @deprecated use method that includes count and hint dots instead.
     *             Add a line of information about the structure:<br>
     *             (indent)Input Bus: info
     *
     * @param info Location description
     * @param dots Hint block(s)
     * @return Instance this method was called on.
     */
    @Deprecated
    public MultiblockTooltipBuilder addInputBus(String info, int... dots) {
        addStructurePart("GT5U.MBTT.InputBus", info, dots);
        return this;
    }

    /**
     * @deprecated use method that includes count and hint dots instead.
     *             Add a line of information about the structure:<br>
     *             (indent)Steam Input Bus: info
     *
     * @param info Location description
     * @param dots Hint block(s)
     * @return Instance this method was called on.
     */
    @Deprecated
    public MultiblockTooltipBuilder addSteamInputBus(String info, int... dots) {
        addStructurePart("GTPP.MBTT.SteamInputBus", info, dots);
        return this;
    }

    /**
     * @deprecated use method that includes count and hint dots instead.
     *             Add a line of information about the structure:<br>
     *             (indent)Input Hatch: info
     *
     * @param info Location description
     * @param dots Hint block(s)
     * @return Instance this method was called on.
     */
    @Deprecated
    public MultiblockTooltipBuilder addInputHatch(String info, int... dots) {
        addStructurePart("GT5U.MBTT.InputHatch", info, dots);
        return this;
    }

    /**
     * @deprecated use method that includes count and hint dots instead.
     *             Add a line of information about the structure:<br>
     *             (indent)Output Bus: info
     *
     * @param info Location description
     * @param dots Hint block(s)
     * @return Instance this method was called on.
     */
    @Deprecated
    public MultiblockTooltipBuilder addOutputBus(String info, int... dots) {
        addStructurePart("GT5U.MBTT.OutputBus", info, dots);
        return this;
    }

    /**
     * @deprecated use method that includes count and hint dots instead.
     *             Add a line of information about the structure:<br>
     *             (indent)Steam Output Bus: info
     *
     * @param info Location description
     * @param dots Hint block(s)
     * @return Instance this method was called on.
     */
    @Deprecated
    public MultiblockTooltipBuilder addSteamOutputBus(String info, int... dots) {
        addStructurePart("GTPP.MBTT.SteamOutputBus", info, dots);
        return this;
    }

    /**
     * @deprecated use method that includes count and hint dots instead.
     *             Add a line of information about the structure:<br>
     *             (indent)Output Hatch: info
     *
     * @param info Location description
     * @param dots Hint block(s)
     * @return Instance this method was called on.
     */
    @Deprecated
    public MultiblockTooltipBuilder addOutputHatch(String info, int... dots) {
        addStructurePart("GT5U.MBTT.OutputHatch", info, dots);
        return this;
    }

    /**
     * Use this method to add non-standard structural info:<br>
     * (indent)info
     *
     * @param info The line to be added
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addStructureInfo(String info, Object... params) {
        addShiftInfo(INDENT_MARK + info, params);
        return this;
    }

    /**
     * Use this method to add non-standard structural info without indent:<br>
     * info
     *
     * @param info The line to be added
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addStructureFooter(String info) {
        addShiftInfo(info);
        return this;
    }

    /**
     * Add a colored separator line with specified length to structure info<br>
     *
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addStructureInfoSeparator(EnumChatFormatting color, int length,
        boolean useFinisherConfig) {
        addShiftInfo(color + (useFinisherConfig ? FINISHER_MARK : STRUCTURE_SEPARATOR_MARK));
        return this;
    }

    /**
     * Add a separator line to structure info<br>
     *
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addStructureInfoSeparator() {
        addStructureInfo(EnumChatFormatting.GRAY + STRUCTURE_SEPARATOR_MARK);
        return this;
    }

    /**
     * @deprecated use overload that accepts {@link IStructureChannels} instead
     */
    @Deprecated
    public MultiblockTooltipBuilder addSubChannelUsage(String channel, String purpose) {
        addStructureInfo("GT5U.MBTT.subchannel", channel, purpose);
        return this;
    }

    /**
     * Use this method to add non-standard structural info.<br>
     * (indent)info
     *
     * @param effect Effect of master channel
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addMasterChannel(String effect) {
        addShiftInfo("GT5U.MBTT.masterchannel", effect);
        return this;
    }

    /**
     * Use this method to add non-standard structural info:<br>
     * (indent)Subchannel [channel] determines [channel.tooltip]
     *
     * @param channel Name of subchannel
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addSubChannel(IStructureChannels channel) {
        addShiftInfo("GT5U.MBTT.subchannel", channel.get(), channel.getDefaultTooltip());
        return this;
    }

    /**
     * Use this method to add non-standard structural info:<br>
     * (indent)Subchannel [channel] determines [channel.tooltip]
     *
     * @param channel Name of subchannel
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addSubChannelUsage(IStructureChannels channel) {
        addStructureInfo(
            "GT5U.MBTT.subchannel",
            channel.get(),
            new PrefixedFallbackText("gt.channelfor.", channel.get(), channel.getDefaultTooltip()));
        return this;
    }

    /**
     * Use this method to add non-standard structural hint. This info will appear before the standard structural hint.
     *
     * @param info The line to be added. This should be an entry into minecraft's localization system.
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addStructureHint(String info) {
        hLines.add(new TooltipLine(info));
        return this;
    }

    /**
     * Use this method to add an entry to standard structural hint without creating a corresponding line in structure
     * information
     *
     * @param nameKey The name of block This should be an entry into minecraft's localization system.
     * @param dots    Hint block(s)
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addStructureHint(String nameKey, int... dots) {
        for (int dot : dots) hBlocks.put(dot, nameKey);
        return this;
    }

    /**
     * Useful for maintaining the flow when you need to run an arbitrary operation on the builder.
     *
     * @param fn The operation
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder pipe(Consumer<MultiblockTooltipBuilder> fn) {
        fn.accept(this);
        return this;
    }

    /**
     * Adds the given list of authors to the contributor list's author list, to be displayed at the end of the tooltip.
     *
     * @param authors List of authors to add to tooltip
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addAuthors(String... authors) {
        if (authors != null) Collections.addAll(this.authors, authors);
        return this;
    }

    /**
     * Adds the given list of structure authors to the contributor list's structure author list,
     * to be displayed at the end of the tooltip.
     *
     * @param structureAuthors List of structure authors to add to tooltip
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addStructureAuthors(String... structureAuthors) {
        Collections.addAll(this.structureAuthors, structureAuthors);
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
    public MultiblockTooltipBuilder toolTipFinisher(String... authors) {
        return toolTipFinisher(EnumChatFormatting.GRAY, authors);
    }

    /**
     * Call at the very end.<br>
     * Adds a line jump with configurable color.<br>
     * Adds information on how to display the structure guidelines.<br>
     * Adds credit for creators of this multi, if any.<br>
     * <p>
     * Ends the building process.
     *
     * @param separatorColor Color of the separator line
     * @param authors        Formatted names of the creators of this multiblock machine - if any
     */
    public MultiblockTooltipBuilder toolTipFinisher(EnumChatFormatting separatorColor, @Nullable String... authors) {
        this.addAuthors(authors);
        return toolTipFinisher(separatorColor);
    }

    /**
     * Call at the very end.<br>
     * Adds a line jump with configurable color and length.<br>
     * Adds information on how to display the structure guidelines.<br>
     * Adds credit for creators of this multi, if any.<br>
     * <p>
     * Ends the building process.
     *
     * @param separatorColor  Color of the separator line
     * @param separatorLength Length of the separator line
     * @param authors         Formatted names of the creators of this multiblock machine - if any
     */
    public MultiblockTooltipBuilder toolTipFinisher(EnumChatFormatting separatorColor, int separatorLength,
        @Nullable String... authors) {
        this.addAuthors(authors);
        // Separator length is intentionally calculated at render time from the resolved tooltip.
        return toolTipFinisher(separatorColor);
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
     */

    public MultiblockTooltipBuilder toolTipFinisher(EnumChatFormatting separatorColor) {

        addInfo(separatorColor + FINISHER_MARK);
        addInfo("GT5U.MBTT.HoldDisplay");

        if (!authors.isEmpty() && !structureAuthors.isEmpty()) {
            addInfo("GT5U.MBTT.AuthorsX", GTAuthors.formatAuthors(authors), GTAuthors.formatAuthors(structureAuthors));
        } else if (!this.authors.isEmpty()) {
            addInfo("GT5U.MBTT.Authors", GTAuthors.formatAuthors(authors));
        }

        addStructureInfo(EnumChatFormatting.GRAY + STRUCTURE_SEPARATOR_MARK);
        addShiftInfo("GT5U.MBTT.Structure.SeeStructure");
        addStructureHint("GT5U.MBTT.StructureHint");
        this.addStructureInfoSeparator(EnumChatFormatting.GRAY, 30, true);
        addShiftInfo("GT5U.MBTT.Structure.Projector");
        finalizedHintLines = new ArrayList<>(hLines);
        finalizedHintBlocks = hBlocks.asMap()
            .entrySet()
            .stream()
            .map(entry -> new StructureHintEntry(entry.getKey(), new ArrayList<>(entry.getValue())))
            .collect(java.util.stream.Collectors.toList());
        hLines = null;
        hBlocks = null;
        authors = null;
        structureAuthors = null;
        return this;
    }

    public String[] getInformation() {
        return getStrings(iLines);
    }

    public String[] getStructureInformation() {
        return getStrings(sLines);
    }

    public String[] getStructureHint() {
        if (finalizedHintLines == null || finalizedHintBlocks == null) return null;

        return Stream.concat(
            finalizedHintLines.stream()
                .map(this::resolveTooltipLine),
            finalizedHintBlocks.stream()
                // e.getKey() - 1 because 1 hint is meta 0.
                .map(
                    entry -> resolveLocalizedText("structurelib.blockhint." + (entry.dot - 1) + ".name")
                        + localizedSeparator("gt.string.colon", ":")
                        + entry.names.stream()
                            .map(this::resolveLocalizedText)
                            .distinct()
                            .collect(
                                java.util.stream.Collectors.joining(localizedSeparator("gt.string.separator", ",")))))
            .toArray(String[]::new);
    }

    @Desugar
    private record TooltipLine(String text, Object... params) {}

    @Desugar
    private record PrefixedFallbackText(String prefix, String value, String fallback) {}

    @Desugar
    private record StructureHintEntry(int dot, List<String> names) {}

    @NotNull
    private String[] getStrings(List<Object> xLines) {
        return xLines.stream()
            .map(line -> line instanceof TooltipLine tooltipLine ? serializeTooltipLine(tooltipLine) : line.toString())
            .toArray(String[]::new);
    }

    private String serializeTooltipLine(TooltipLine line) {
        if (line.params == null) return nestParams(line.text, (Object[]) null);

        return nestParams(
            line.text,
            Arrays.stream(line.params)
                .map(this::serializeParam)
                .toArray());
    }

    private Object serializeParam(Object param) {
        if (param instanceof TooltipLine tooltipLine) return serializeTooltipLine(tooltipLine);
        if (param instanceof PrefixedFallbackText fallback) {
            return DelayedTooltipLocalization
                .encode(fallback.prefix + fallback.value, fallback.fallback == null ? "" : fallback.fallback);
        }
        return param;
    }

    private String resolveTooltipLine(TooltipLine line) {
        return resolveLocalizedText(serializeTooltipLine(line));
    }

    private String resolveLocalizedText(String text) {
        if (text.startsWith("{\"k\":")) return DELAYED_TOOLTIP_LOCALIZATION.resolveJsonLocalization(text);

        String translated = StatCollector.translateToLocal(text);
        return translated.equals(text) ? text : translated;
    }

    private String localizedSeparator(String key, String fallback) {
        String separator = StatCollector.translateToLocal(key);
        if (separator.equals(key)) separator = fallback;
        return separator.equals(fallback) ? separator + " " : separator;
    }
}
