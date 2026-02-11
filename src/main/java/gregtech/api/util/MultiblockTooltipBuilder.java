package gregtech.api.util;

import static gregtech.api.util.tooltip.TooltipHelper.percentageFormat;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
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
 * Use addStructureInfo for any comments on nonstandard structure info wherever needed <br>
 * toolTipFinisher goes at the very end<br>
 * <br>
 * Originally created by kekzdealer
 */
public class MultiblockTooltipBuilder {

    private static final String TAB = "   ";
    private static final String COLON = ": ";
    private static final String SEPARATOR = ", ";
    private static final String TT_machineType = StatCollector.translateToLocal("GT5U.MBTT.MachineType");
    private static final String TT_StaticParallels = StatCollector.translateToLocal("GT5U.MBTT.Parallel.Base");
    private static final String TT_StaticSpeed = StatCollector.translateToLocal("GT5U.MBTT.Speed.Base");
    private static final String TT_StaticEuEff = StatCollector.translateToLocal("GT5U.MBTT.EuDiscount.Base");
    private static final String TT_DynamicParallels = StatCollector.translateToLocal("GT5U.MBTT.Parallel.Additional");
    private static final String TT_SingularParallel = StatCollector.translateToLocal("GT5U.MBTT.Parallel.Singular");
    private static final String TT_DynamicSpeedBonus = StatCollector.translateToLocal("GT5U.MBTT.Speed.Additional");
    private static final String TT_DynamicSpeed = StatCollector.translateToLocal("GT5U.MBTT.Speed.Absolute");
    private static final String TT_DynamicEuEff = StatCollector.translateToLocal("GT5U.MBTT.EuDiscount.Additional");
    private static final String TT_Steam_StaticSteamEff = StatCollector
        .translateToLocal("GT5U.MBTT.SteamDiscount.Base");

    private static final String TT_dimensions = StatCollector.translateToLocal("GT5U.MBTT.Dimensions");
    private static final String TT_hollow = StatCollector.translateToLocal("GT5U.MBTT.Hollow");
    private static final String TT_structure = StatCollector.translateToLocal("GT5U.MBTT.Structure");
    private static final String TT_controller = StatCollector.translateToLocal("GT5U.MBTT.Controller");
    private static final String TT_minimum = StatCollector.translateToLocal("GT5U.MBTT.Minimum");
    private static final String TT_tiered = StatCollector.translateToLocal("GT5U.MBTT.Tiered");
    private static final String TT_maintenancehatch = StatCollector.translateToLocal("GT5U.MBTT.MaintenanceHatch");
    private static final String TT_energyhatch = StatCollector.translateToLocal("GT5U.MBTT.EnergyHatch");
    private static final String TT_dynamohatch = StatCollector.translateToLocal("GT5U.MBTT.DynamoHatch");
    private static final String TT_mufflerhatch = StatCollector.translateToLocal("GT5U.MBTT.MufflerHatch");
    private static final String TT_inputbus = StatCollector.translateToLocal("GT5U.MBTT.InputBus");
    private static final String TT_inputhatch = StatCollector.translateToLocal("GT5U.MBTT.InputHatch");
    private static final String TT_outputbus = StatCollector.translateToLocal("GT5U.MBTT.OutputBus");
    private static final String TT_outputhatch = StatCollector.translateToLocal("GT5U.MBTT.OutputHatch");
    private static final String TT_tectechhatch = StatCollector.translateToLocal("GT5U.MBTT.TecTechHatch");
    private static final String TT_steaminputbus = StatCollector.translateToLocal("GTPP.MBTT.SteamInputBus");
    private static final String TT_steamoutputbus = StatCollector.translateToLocal("GTPP.MBTT.SteamOutputBus");
    private static final String TT_steamhatch = StatCollector.translateToLocal("GTPP.MBTT.SteamHatch");
    private static final String TT_causes = StatCollector.translateToLocal("GT5U.MBTT.Causes");
    private static final String TT_produces = StatCollector.translateToLocal("GT5U.MBTT.Produces");
    private static final String TT_pps = StatCollector.translateToLocal("GT5U.MBTT.PPS");
    private static final String TT_hold = StatCollector.translateToLocal("GT5U.MBTT.Hold");
    private static final String TT_todisplay = StatCollector.translateToLocal("GT5U.MBTT.Display");
    private static final String TT_structurehint = StatCollector.translateToLocal("GT5U.MBTT.StructureHint");
    private static final String TT_addedBy = StatCollector.translateToLocal("GT5U.MBTT.Mod");
    private static final String TT_air = StatCollector.translateToLocal("GT5U.MBTT.Air");
    private static final String TT_SeeStructure1 = StatCollector.translateToLocal("GT5U.MBTT.Structure.SeeStructure1");
    private static final String TT_SeeStructure2 = StatCollector.translateToLocal("GT5U.MBTT.Structure.SeeStructure2");
    private static final String TT_PerfectOC = StatCollector.translateToLocal("GT5U.MBTT.PerfectOC");
    private static final String[] TT_dots = IntStream.range(0, 16)
        .mapToObj(i -> StatCollector.translateToLocal("structurelib.blockhint." + i + ".name"))
        .toArray(String[]::new);
    private static final String TT_StructureAuthor = StatCollector.translateToLocal("GT5U.MBTT.StructureBy");

    private List<String> iLines;
    private List<String> sLines;
    private List<String> hLines;
    private List<String> authors;
    private List<String> structureAuthors;
    private SetMultimap<Integer, String> hBlocks;

    private String[] iArray;
    private String[] sArray;
    private String[] hArray;

    public MultiblockTooltipBuilder() {
        iLines = new LinkedList<>();
        sLines = new LinkedList<>();
        hLines = new LinkedList<>();
        authors = new LinkedList<>();
        structureAuthors = new LinkedList<>();
        hBlocks = Multimaps.newSetMultimap(new HashMap<>(), HashSet::new);
        hBlocks.put(StructureLibAPI.HINT_BLOCK_META_AIR, TT_air);
    }

    /**
     * Add a line telling you what the machine type is. Usually, this will be the name of a SB version.<br>
     * Machine Type: machine
     *
     * @param machine Name of the machine type
     *
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addMachineType(String machine) {
        iLines.add(TT_machineType + COLON + EnumChatFormatting.YELLOW + machine + EnumChatFormatting.RESET);
        return this;
    }

    /**
     * Add a basic line of information about this structure
     *
     * @param info The line to be added.
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addInfo(String info) {
        iLines.add(info);
        return this;
    }

    /**
     * Add a line for static parallel count Processes up to {parallels} recipes at once
     *
     * @param parallels Maximum parallels
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addStaticParallelInfo(Integer parallels) {
        iLines.add(String.format(TT_StaticParallels, TooltipHelper.parallelText(parallels)));
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
        iLines.add(
            String.format(
                parallels == 1 ? TT_SingularParallel : TT_DynamicParallels,
                TooltipHelper.parallelText(parallels),
                tier.getValue()));
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
        iLines.add(
            String.format(TT_DynamicParallels, TooltipHelper.parallelText(factor.toString() + "x"), tier.getValue()));
        return this;
    }

    /**
     * Add a line of information about Speed bonus
     *
     * @param speed Speed as defined in ProcessingLogic. e.g (1F / 3.5F = 350% Speed
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addStaticSpeedInfo(float speed) {

        iLines.add(String.format(TT_StaticSpeed, TooltipHelper.speedText(speed)));
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
        iLines.add(
            String.format(
                TT_DynamicSpeedBonus,
                TooltipHelper.speedText("+" + percentageFormat.format(speed)),
                tier.getValue()));
        return this;
    }

    public MultiblockTooltipBuilder addDynamicSpeedInfo(float speed, TooltipTier tier) {
        iLines.add(
            String.format(TT_DynamicSpeed, TooltipHelper.speedText(percentageFormat.format(speed)), tier.getValue()));
        return this;
    }

    /**
     * Add a line of information about Eu Discount bonus relative to SB machines
     *
     * @param euEff euEff as defined in ProcessingLogic
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addStaticEuEffInfo(float euEff) {
        iLines.add(String.format(TT_StaticEuEff, TooltipHelper.effText(euEff)));
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
        iLines.add(
            String
                .format(TT_DynamicEuEff, TooltipHelper.effText("-" + percentageFormat.format(euEff)), tier.getValue()));
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
        iLines.add(String.format(TT_Steam_StaticSteamEff, TooltipHelper.effText(percentageFormat.format(steamEff))));
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
     */
    public MultiblockTooltipBuilder addInfoAll(String... infoStrings) {
        iLines.addAll(Arrays.asList(infoStrings));
        return this;
    }

    /**
     * Add a separator line
     *
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addSeparator() {
        return addSeparator(EnumChatFormatting.GRAY, 41);
    }

    /**
     * Add a colored separator line
     *
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addSeparator(EnumChatFormatting color) {
        return addSeparator(color, 41);
    }

    /**
     * Add a colored separator line with specified length
     *
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addSeparator(EnumChatFormatting color, int length) {
        switch (GTMod.proxy.separatorStyle) {
            case 0 -> iLines.add(" ");
            case 1 -> iLines.add(color + StringUtils.getRepetitionOf('-', length));
            default -> iLines
                .add(color.toString() + EnumChatFormatting.STRIKETHROUGH + StringUtils.getRepetitionOf('-', length));
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
        iLines.add("" + EnumChatFormatting.DARK_PURPLE + pollution + " " + EnumChatFormatting.GRAY + TT_pps);
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
        sLines.add(
            EnumChatFormatting.WHITE + TT_dimensions
                + COLON
                + EnumChatFormatting.GOLD
                + w
                + EnumChatFormatting.GRAY
                + "x"
                + EnumChatFormatting.GOLD
                + h
                + EnumChatFormatting.GRAY
                + "x"
                + EnumChatFormatting.GOLD
                + l
                + EnumChatFormatting.GRAY
                + " ("
                + EnumChatFormatting.GOLD
                + "W"
                + EnumChatFormatting.GRAY
                + "x"
                + EnumChatFormatting.GOLD
                + "H"
                + EnumChatFormatting.GRAY
                + "x"
                + EnumChatFormatting.GOLD
                + "L"
                + EnumChatFormatting.GRAY
                + ") "
                + (hollow ? EnumChatFormatting.RED + TT_hollow : ""));
        sLines.add(EnumChatFormatting.WHITE + TT_structure + COLON);
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
        sLines.add(
            EnumChatFormatting.WHITE + TT_dimensions
                + COLON
                + EnumChatFormatting.GOLD
                + wmin
                + (wmin != wmax ? "-" + wmax : "")
                + EnumChatFormatting.GRAY
                + "x"
                + EnumChatFormatting.GOLD
                + hmin
                + (hmin != hmax ? "-" + hmax : "")
                + EnumChatFormatting.GRAY
                + "x"
                + EnumChatFormatting.GOLD
                + lmin
                + (lmin != lmax ? "-" + lmax : "")
                + EnumChatFormatting.GRAY
                + " ("
                + EnumChatFormatting.GOLD
                + "W"
                + EnumChatFormatting.GRAY
                + "x"
                + EnumChatFormatting.GOLD
                + "H"
                + EnumChatFormatting.GRAY
                + "x"
                + EnumChatFormatting.GOLD
                + "L"
                + EnumChatFormatting.GRAY
                + ") "
                + (hollow ? EnumChatFormatting.RED + TT_hollow : ""));
        sLines.add(EnumChatFormatting.WHITE + TT_structure + COLON);
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)Controller: info
     *
     * @param info Positional information.
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addController(String info) {
        sLines.add(TAB + EnumChatFormatting.WHITE + TT_controller + COLON + EnumChatFormatting.GRAY + info);
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
        sLines.add(
            countColor + TAB
                + count
                + "x "
                + EnumChatFormatting.RESET
                + textColor
                + casingName
                + (isTiered ? " " + TT_tiered : ""));
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)minCountx casingName (minimum) (tiered)
     *
     * @param casingName Name of the Casing.
     * @param minCount   Minimum needed for valid structure check.
     * @param isTiered   Flag if this casing accepts multiple tiers (e.g. coils)
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
        sLines.add(
            countColor + TAB
                + minCount
                + "x "
                + EnumChatFormatting.RESET
                + textColor
                + casingName
                + " "
                + TT_minimum
                + (isTiered ? " " + TT_tiered : ""));
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
        sLines.add(
            countColor + TAB
                + minCount
                + "x"
                + EnumChatFormatting.GRAY
                + " - "
                + countColor
                + maxCount
                + "x "
                + EnumChatFormatting.RESET
                + textColor
                + casingName
                + (isTiered ? " " + TT_tiered : ""));
        return this;
    }

    /**
     * Use this method to add a structural part that isn't covered by the other methods.<br>
     * (indent)name: info
     *
     * @param name Name of the hatch or other component.
     * @param info Positional information.
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addOtherStructurePart(String name, String info) {
        sLines.add(EnumChatFormatting.WHITE + TAB + name + COLON + EnumChatFormatting.GRAY + info);
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)Maintenance Hatch: info
     *
     * @param info Positional information.
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addMaintenanceHatch(String info) {
        sLines.add(EnumChatFormatting.WHITE + TAB + TT_maintenancehatch + COLON + EnumChatFormatting.GRAY + info);
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)Muffler Hatch: info
     *
     * @param info Location where the hatch goes
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addMufflerHatch(String info) {
        sLines.add(EnumChatFormatting.WHITE + TAB + TT_mufflerhatch + COLON + EnumChatFormatting.GRAY + info);
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)Energy Hatch: info
     *
     * @param info Positional information.
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addEnergyHatch(String info) {
        sLines.add(EnumChatFormatting.WHITE + TAB + TT_energyhatch + COLON + EnumChatFormatting.GRAY + info);
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)Dynamo Hatch: info
     *
     * @param info Positional information.
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addDynamoHatch(String info) {
        sLines.add(EnumChatFormatting.WHITE + TAB + TT_dynamohatch + COLON + EnumChatFormatting.GRAY + info);
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)Input Bus: info
     *
     * @param info Location where the bus goes
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addInputBus(String info) {
        sLines.add(EnumChatFormatting.WHITE + TAB + TT_inputbus + COLON + EnumChatFormatting.GRAY + info);
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)Input Bus (Steam): info
     *
     * @param info Location where the bus goes
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addSteamInputBus(String info) {
        sLines.add(EnumChatFormatting.WHITE + TAB + TT_steaminputbus + COLON + EnumChatFormatting.GRAY + info);
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)Input Hatch: info
     *
     * @param info Location where the hatch goes
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addInputHatch(String info) {
        sLines.add(EnumChatFormatting.WHITE + TAB + TT_inputhatch + COLON + EnumChatFormatting.GRAY + info);
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)Output Bus: info
     *
     * @param info Location where the bus goes
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addOutputBus(String info) {
        sLines.add(EnumChatFormatting.WHITE + TAB + TT_outputbus + COLON + EnumChatFormatting.GRAY + info);
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)Output Bus (Steam): info
     *
     * @param info Location where the bus goes
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addSteamOutputBus(String info) {
        sLines.add(EnumChatFormatting.WHITE + TAB + TT_steamoutputbus + COLON + EnumChatFormatting.GRAY + info);
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)Output Hatch: info
     *
     * @param info Location where the bus goes
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addOutputHatch(String info) {
        sLines.add(EnumChatFormatting.WHITE + TAB + TT_outputhatch + COLON + EnumChatFormatting.GRAY + info);
        return this;
    }

    /**
     * Use this method to add a structural part that isn't covered by the other methods.<br>
     * (indent)name: info
     *
     * @param localizedName Name of the hatch or other component. This entry should be localized, otherwise the
     *                      structure hints sent to the chat can't be localized.
     * @param info          Positional information.
     * @param dots          The valid locations for this part when asked to display hints
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addOtherStructurePart(String localizedName, String info, int... dots) {
        sLines.add(EnumChatFormatting.WHITE + TAB + localizedName + COLON + EnumChatFormatting.GRAY + info);
        for (int dot : dots) hBlocks.put(dot, localizedName);
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * Supports TecTech Multi-Amp Hatches!
     *
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addMultiAmpHatchInfo() {
        iLines.add(EnumChatFormatting.GREEN + GTUtility.translate("GT5U.MBTT.TecTechMultiAmp"));
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * Supports TecTech Multi-Amp and Laser Hatches!
     *
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addTecTechHatchInfo() {
        iLines.add(EnumChatFormatting.GREEN + TT_tectechhatch);
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * Machine does not lose efficiency when overclocked
     *
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addPerfectOCInfo() {
        iLines.add(EnumChatFormatting.AQUA + TT_PerfectOC);
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
        iLines.add(
            GTValues.TIER_COLORS[t] + GTValues.VN[t]
                + EnumChatFormatting.GRAY
                + StatCollector.translateToLocal("GT5U.MBTT.Structure.MinGlassForLaser"));
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * Energy Hatch limited by Glass tier
     *
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addGlassEnergyLimitInfo() {
        iLines.add(StatCollector.translateToLocal("GT5U.MBTT.Structure.GlassEnergyLimit"));
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
        iLines.add(
            StatCollector.translateToLocal("GT5U.MBTT.Structure.GlassEnergyLimit") + ", "
                + GTValues.TIER_COLORS[t]
                + GTValues.VN[t]
                + EnumChatFormatting.GRAY
                + StatCollector.translateToLocal("GT5U.MBTT.Structure.GlassEnergyLimitTier"));
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * This machine can run recipes regardless of tier, if given enough energy
     *
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addNoTierSkips() {
        iLines.add(StatCollector.translateToLocal("GT5U.MBTT.Structure.NoTierSkips"));
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * This machine can run recipes regardless of tier, if given enough energy
     *
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addUnlimitedTierSkips() {
        iLines.add(StatCollector.translateToLocal("GT5U.MBTT.Structure.UnlimitedTierSkips"));
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
        iLines.add(StatCollector.translateToLocalFormatted("GT5U.MBTT.Structure.MaxTierSkips", n));
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)Maintenance Hatch: info
     *
     * @param info Positional information.
     * @param dots The valid locations for this part when asked to display hints
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addMaintenanceHatch(String info, int... dots) {
        sLines.add(EnumChatFormatting.WHITE + TAB + TT_maintenancehatch + COLON + EnumChatFormatting.GRAY + info);
        for (int dot : dots) hBlocks.put(dot, TT_maintenancehatch);
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)Muffler Hatch: info
     *
     * @param info Location where the hatch goes
     * @param dots The valid locations for this part when asked to display hints
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addMufflerHatch(String info, int... dots) {
        sLines.add(EnumChatFormatting.WHITE + TAB + TT_mufflerhatch + COLON + EnumChatFormatting.GRAY + info);
        for (int dot : dots) hBlocks.put(dot, TT_mufflerhatch);
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)Energy Hatch: info
     *
     * @param info Positional information.
     * @param dots The valid locations for this part when asked to display hints
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addEnergyHatch(String info, int... dots) {
        sLines.add(EnumChatFormatting.WHITE + TAB + TT_energyhatch + COLON + EnumChatFormatting.GRAY + info);
        for (int dot : dots) hBlocks.put(dot, TT_energyhatch);
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)Dynamo Hatch: info
     *
     * @param info Positional information.
     * @param dots The valid locations for this part when asked to display hints
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addDynamoHatch(String info, int... dots) {
        sLines.add(EnumChatFormatting.WHITE + TAB + TT_dynamohatch + COLON + EnumChatFormatting.GRAY + info);
        for (int dot : dots) hBlocks.put(dot, TT_dynamohatch);
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)Input Bus: info
     *
     * @param info Location where the bus goes
     * @param dots The valid locations for this part when asked to display hints
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addInputBus(String info, int... dots) {
        sLines.add(EnumChatFormatting.WHITE + TAB + TT_inputbus + COLON + EnumChatFormatting.GRAY + info);
        for (int dot : dots) hBlocks.put(dot, TT_inputbus);
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)Input Bus (Steam): info
     *
     * @param info Location where the bus goes
     * @param dots The valid locations for this part when asked to display hints
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addSteamInputBus(String info, int... dots) {
        sLines.add(EnumChatFormatting.WHITE + TAB + TT_steaminputbus + COLON + EnumChatFormatting.GRAY + info);
        for (int dot : dots) hBlocks.put(dot, TT_steaminputbus);
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)Input Hatch: info
     *
     * @param info Location where the hatch goes
     * @param dots The valid locations for this part when asked to display hints
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addInputHatch(String info, int... dots) {
        sLines.add(EnumChatFormatting.WHITE + TAB + TT_inputhatch + COLON + EnumChatFormatting.GRAY + info);
        for (int dot : dots) hBlocks.put(dot, TT_inputhatch);
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)Output Bus: info
     *
     * @param info Location where the bus goes
     * @param dots The valid locations for this part when asked to display hints
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addOutputBus(String info, int... dots) {
        sLines.add(EnumChatFormatting.WHITE + TAB + TT_outputbus + COLON + EnumChatFormatting.GRAY + info);
        for (int dot : dots) hBlocks.put(dot, TT_outputbus);
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)Output Bus (Steam): info
     *
     * @param info Location where the bus goes
     * @param dots The valid locations for this part when asked to display hints
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addSteamOutputBus(String info, int... dots) {
        sLines.add(EnumChatFormatting.WHITE + TAB + TT_steamoutputbus + COLON + EnumChatFormatting.GRAY + info);
        for (int dot : dots) hBlocks.put(dot, TT_steamoutputbus);
        return this;
    }

    /**
     * Add a line of information about the structure:<br>
     * (indent)Output Hatch: info
     *
     * @param info Location where the bus goes
     * @param dots The valid locations for this part when asked to display hints
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addOutputHatch(String info, int... dots) {
        sLines.add(EnumChatFormatting.WHITE + TAB + TT_outputhatch + COLON + EnumChatFormatting.GRAY + info);
        for (int dot : dots) hBlocks.put(dot, TT_outputhatch);
        return this;
    }

    /**
     * Use this method to add non-standard structural info.<br>
     * (indent)info
     *
     * @param info The line to be added.
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addStructureInfo(String info) {
        sLines.add(TAB + info);
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
                case 1 -> sLines.add(TAB + " ");
                case 2 -> sLines.add(TAB + color + StringUtils.getRepetitionOf('-', length));
                default -> sLines.add(
                    TAB + color.toString()
                        + EnumChatFormatting.STRIKETHROUGH
                        + StringUtils.getRepetitionOf('-', length));
            }
        } else {
            switch (GTMod.proxy.separatorStyle) {
                case 0 -> sLines.add(TAB + " ");
                case 1 -> sLines.add(TAB + color + StringUtils.getRepetitionOf('-', length));
                default -> sLines
                    .add(TAB + color + EnumChatFormatting.STRIKETHROUGH + StringUtils.getRepetitionOf('-', length));
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
        return addStructureInfoSeparator(EnumChatFormatting.GRAY, 30, false);
    }

    /**
     * @deprecated use overload that accepts {@link IStructureChannels} instead
     */
    @Deprecated
    public MultiblockTooltipBuilder addSubChannelUsage(String channel, String purpose) {
        sLines.add(TAB + StatCollector.translateToLocalFormatted("GT5U.MBTT.subchannel", channel, purpose));
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
        sLines.add(TAB + StatCollector.translateToLocalFormatted("GT5U.MBTT.subchannel", channel.get(), purpose));
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
        sLines.add(
            TAB + StatCollector
                .translateToLocalFormatted("GT5U.MBTT.subchannel", channel.get(), channel.getDefaultTooltip()));
        return this;
    }

    /**
     * Use this method to add non-standard structural hint. This info will appear before the standard structural hint.
     *
     * @param info The line to be added. This should be an entry into minecraft's localization system.
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addStructureHint(String info) {
        hLines.add(StatCollector.translateToLocal(info));
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
        for (int dot : dots) hBlocks.put(dot, StatCollector.translateToLocal(nameKey));
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
     * Adds the given list of authors to the contributor list's author list, to be displayed at the end of the tooltip
     *
     * @param authors list of authors to add to tooltip
     * @return Instance this method was called on.
     */
    public MultiblockTooltipBuilder addAuthors(String... authors) {
        Collections.addAll(this.authors, authors);
        return this;
    }

    /**
     * Adds the given list of structure authors to the contributor list's structure author list,
     * to be displayed at the end of the tooltip
     *
     * @param structureAuthors list of structure authors to add to tooltip
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
    public MultiblockTooltipBuilder toolTipFinisher(@Nullable String... authors) {
        return toolTipFinisher(EnumChatFormatting.GRAY, 41, authors);
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
        return toolTipFinisher(separatorColor, separatorLength);
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
     */

    public MultiblockTooltipBuilder toolTipFinisher(EnumChatFormatting separatorColor, int separatorLength) {

        switch (GTMod.proxy.tooltipFinisherStyle) {
            case 0 -> {}
            case 1 -> iLines.add(" ");
            case 2 -> iLines.add(separatorColor + StringUtils.getRepetitionOf('-', separatorLength));
            default -> iLines.add(
                separatorColor.toString() + EnumChatFormatting.STRIKETHROUGH
                    + StringUtils.getRepetitionOf('-', separatorLength));
        }

        iLines.add(
            TT_hold + " "
                + EnumChatFormatting.BOLD
                + "[LSHIFT]"
                + EnumChatFormatting.RESET
                + EnumChatFormatting.GRAY
                + " "
                + TT_todisplay);

        final StringBuilder sb = new StringBuilder();
        if (!authors.isEmpty()) {
            final String authorTag = "Author: ";
            sb.append(TT_addedBy);
            sb.append(COLON);
            for (int i = 0; i < authors.size(); i++) {
                String author = authors.get(i);
                if (author.startsWith(authorTag)) {
                    // to support all the values in GTValues
                    // that already have Author at the start
                    sb.append(author.substring(authorTag.length()));
                } else {
                    sb.append(author);
                }
                if (i != authors.size() - 1) {
                    sb.append(EnumChatFormatting.RESET);
                    sb.append(EnumChatFormatting.GRAY);
                    sb.append(" & ");
                    sb.append(EnumChatFormatting.GREEN);
                }
            }
            if (!structureAuthors.isEmpty()) sb.append(EnumChatFormatting.RESET)
                .append(EnumChatFormatting.GRAY)
                .append(", ");
        }
        if (!structureAuthors.isEmpty()) {
            sb.append(TT_StructureAuthor);
            sb.append(COLON);
            for (int i = 0; i < structureAuthors.size(); i++) {
                String builder = structureAuthors.get(i);
                sb.append(builder);
                if (i != structureAuthors.size() - 1) {
                    sb.append(EnumChatFormatting.RESET);
                    sb.append(EnumChatFormatting.GRAY);
                    sb.append(" & ");
                    sb.append(EnumChatFormatting.GREEN);
                }
            }
        }
        if (sb.length() > 0) iLines.add(sb.toString());

        hLines.add(TT_structurehint);
        this.addStructureInfoSeparator(EnumChatFormatting.GRAY, 30, true);
        sLines.add(
            EnumChatFormatting.WHITE + TT_SeeStructure1
                + EnumChatFormatting.BLUE
                + " Structure"
                + EnumChatFormatting.DARK_BLUE
                + "Lib "
                + EnumChatFormatting.RESET
                + EnumChatFormatting.WHITE
                + TT_SeeStructure2);
        // create the final arrays
        iArray = iLines.toArray(new String[0]);
        sArray = sLines.toArray(new String[0]);
        // e.getKey() - 1 because 1 hint is meta 0.
        hArray = Stream.concat(
            hLines.stream(),
            hBlocks.asMap()
                .entrySet()
                .stream()
                .map(e -> TT_dots[e.getKey() - 1] + COLON + String.join(SEPARATOR, e.getValue())))
            .toArray(String[]::new);
        // free memory
        iLines = null;
        sLines = null;
        hLines = null;
        authors = null;
        structureAuthors = null;
        hBlocks = null;
        return this;
    }

    public String[] getInformation() {
        return iArray;
    }

    public String[] getStructureInformation() {
        return sArray;
    }

    public String[] getStructureHint() {
        return hArray;
    }

}
