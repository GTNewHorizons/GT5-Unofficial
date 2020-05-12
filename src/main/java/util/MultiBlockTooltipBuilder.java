package util;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

/**
 * Have you ever felt like your tooltips just aren't enterprise enough? Use this!
 * 
 * @author kekzdealer
 *
 */
public class MultiBlockTooltipBuilder {
	
	private static final String TAB = "   ";
	private static final String COLON = ": ";
	
	private final List<String> iLines;
	private final List<String> sLines;
	
	private String[] iArray;
	private String[] sArray;

	// Localized keywords
	private static final String KW_dimensions = StatCollector.translateToLocal("mbdesc.beginStructureBlock.Dimensions");
	private static final String KW_structure = StatCollector.translateToLocal("mbdesc.beginStructureBlock.Structure");
	private static final String KW_controller = StatCollector.translateToLocal("mbdesc.addController.Controller");
	private static final String KW_atleast = StatCollector.translateToLocal("mbdesc.addCasing.atleast");
	private static final String KW_energyhatch = StatCollector.translateToLocal("mbdesc.addEnergyHatch.EnergyHatch");
	private static final String KW_dynamohatch = StatCollector.translateToLocal("mbdesc.addDynamoHatch.DynamoHatch");
	private static final String KW_maintenancehatch = StatCollector.translateToLocal("mbdesc.addMaintenanceHatch.MaintenanceHatch");
	private static final String KW_iohatches = StatCollector.translateToLocal("mbdesc.addIOHatches.IOHatches");
	private static final String KW_inputbus = StatCollector.translateToLocal("mbdesc.addInputBus.InputBus");
	private static final String KW_inputhatch = StatCollector.translateToLocal("mbdesc.addInputHatch.InputHatch");
	private static final String KW_outputbus = StatCollector.translateToLocal("mbdesc.addOutputBus.OutputBus");
	private static final String KW_outputhatch = StatCollector.translateToLocal("mbdesc.addOutputHatch.OutputHatch");
	private static final String KW_hold = StatCollector.translateToLocal("mbdesc.signAndFinalize.Hold");
	private static final String KW_todisplay = StatCollector.translateToLocal("mbdesc.signAndFinalize.todisplay");
	private static final String KW_createdby = StatCollector.translateToLocal("mbdesc.signAndFinalize.createdby");

	public MultiBlockTooltipBuilder() {
		iLines = new LinkedList<>();
		sLines = new LinkedList<>();


	}
	
	/**
	 * Add a basic line of information about this structure
	 * 
	 * @param info
	 * 				The line to be added.
	 * @return Instance this method was called on.
	 */
	public MultiBlockTooltipBuilder addInfo(String info) {
		iLines.add(info);
		return this;
	}
	
	/**
	 * Add a separator line like this:<br>
	 * -------------------------------
	 * 
	 * @return Instance this method was called on.
	 */
	public MultiBlockTooltipBuilder addSeparator() {
		iLines.add("-----------------------------------------");
		return this;
	}
	
	/**
	 * Begin adding structural information by adding a line about the structure's dimensions
	 * and then inserting a "Structure:" line.
	 * 
	 * @param w
	 * 			Structure width.
	 * @param h
	 * 			Structure height.
	 * @param l
	 * 			Structure depth/length.
	 * @return Instance this method was called on.
	 */
	public MultiBlockTooltipBuilder beginStructureBlock(int w, int h, int l) {
		sLines.add(KW_dimensions + COLON + w + "x" + h + "x" + l + " (WxHxL)");
		sLines.add(KW_structure + COLON);
		return this;
	}
	
	public MultiBlockTooltipBuilder addController(String info) {
		sLines.add(TAB + KW_controller + COLON + info);
		return this;
	}
	
	public MultiBlockTooltipBuilder addCasingInfo(String casingName, int minCount) {
		sLines.add(TAB + minCount +"x " + casingName + " " + KW_atleast);
		return this;
	}
	
	public MultiBlockTooltipBuilder addEnergyHatch(String info) {
		sLines.add(TAB + KW_energyhatch + COLON + info);
		return this;
	}
	
	public MultiBlockTooltipBuilder addDynamoHatch(String info) {
		sLines.add(TAB + KW_dynamohatch + COLON + info);
		return this;
	}
	
	public MultiBlockTooltipBuilder addMaintenanceHatch(String info) {
		sLines.add(TAB + KW_maintenancehatch + COLON + info);
		return this;
	}
	
	public MultiBlockTooltipBuilder addIOHatches(String info) {
		sLines.add(TAB + KW_iohatches + COLON + info);
		return this;
	}
	
	public MultiBlockTooltipBuilder addInputBus(String info) {
		sLines.add(TAB + KW_inputbus + COLON + info);
		return this;
	}
	
	public MultiBlockTooltipBuilder addInputHatch(String info) {
		sLines.add(TAB + KW_inputhatch + COLON + info);
		return this;
	}
	
	public MultiBlockTooltipBuilder addOutputBus(String info) {
		sLines.add(TAB + KW_outputbus + COLON + info);
		return this;
	}
	
	public MultiBlockTooltipBuilder addOutputHatch(String info) {
		sLines.add(TAB + KW_outputhatch + COLON + info);
		return this;
	}
	
	/**
	 * Use this method to add a structural part that isn't covered by the builders capabilities.
	 * 
	 * @param name
	 * 				Name of the hatch or other component.
	 * @param info
	 * 				Positional information.
	 * @return Instance this method was called on.
	 */
	public MultiBlockTooltipBuilder addOtherStructurePart(String name, String info) {
		sLines.add(TAB + name + COLON + info);
		return this;
	}
	
	/**
	 * Call at the very end.<br>
	 * Adds a final line with the authors name and information on how to display the structure guidelines.<br>
	 * Ends the building process.
	 * 
	 * @param author
	 * 				Name of the creator of this Machine
	 */
	public void signAndFinalize(String author) {
		iLines.add(KW_hold + " " + EnumChatFormatting.BOLD + "[LSHIFT]" + EnumChatFormatting.RESET + EnumChatFormatting.GRAY + " " + KW_todisplay);
		iLines.add(KW_createdby + " " + author);
		iArray = new String[iLines.size()];
		sArray = new String[sLines.size()];
		iLines.toArray(iArray);
		sLines.toArray(sArray);
	}
	
	public String[] getInformation() {
		return iArray;
	}
	
	public String[] getStructureInformation() {
		return sArray;
	}
}
