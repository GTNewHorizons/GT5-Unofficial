package gtPlusPlus.core.item.crafting;

import java.util.LinkedHashMap;
import java.util.Map;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.general.ItemGenericToken;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class ItemDummyResearch extends ItemGenericToken {
	
	public static enum ASSEMBLY_LINE_RESEARCH {
		
		
		RESEARCH_1_CONTAINMENT("Containment Fields", "Advanced scientific study"),
		RESEARCH_2_BASIC_CHEM("Basic Chemistry", "Time to start at the beginning"),
		RESEARCH_3_ADV_CHEM("Advanced Chemistry", "Best learn more than chemical equations"),
		RESEARCH_4_BASIC_PHYSICS("Basic Physics", "Fundamental laws of motion"),
		RESEARCH_5_ADV_PHYSICS("Advanced Physics", "Advanced knowledge!"),
		RESEARCH_6_BASIC_METALLURGY("Basic Metallurgy", "Information about material smelting"),
		RESEARCH_7_ADV_METALLURGY("Advanced Metallurgy", "Advanced Material Sciences!"),
		RESEARCH_8_TURBINE_AUTOMATION("Turbine Automation", "You really don't want to share this with anyone!"), 
		RESEARCH_9_CLOAKING("Cloaking Technologies", "Sneaking around like a mouse");	
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		private String mName;
		private String mDesc;
		
		private ASSEMBLY_LINE_RESEARCH(String aName, String aDesc) {
			mName = aName;
			mDesc = aDesc;
			ModItems.itemDummyResearch.register(mName, mDesc);
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	private static Map<String, Integer> mInternalNameToIdMap = new LinkedHashMap<String, Integer>();
	
	public static ItemStack getResearchStack(ASSEMBLY_LINE_RESEARCH aResearchName, int aStacksize) {
		Integer aMeta = mInternalNameToIdMap.get(Utils.sanitizeString(aResearchName.mName));
		if (aMeta == null) {
			aMeta = 0;
		}		
		return ItemUtils.simpleMetaStack(ModItems.itemDummyResearch, aMeta, aStacksize);
	}
	
	private int aID = 0;
	public ItemDummyResearch() {
		super("dummyResearch", "Research", new String[] {"This object requires some further study"}, "research");
		

		
	}
	
	/**
	 * 
	 * @param aResearchType - What is the research for?
	 * @param aDescriptThe - tooltip for this research
	 * @return - Did we register a custom research item?
	 */
	public boolean register(String aResearchType, String aDescript) {
		int aNewID = aID++;
		mInternalNameToIdMap.put(Utils.sanitizeString(aResearchType), aNewID);
		return register(aNewID, "Research on "+aResearchType, 1, aDescript);
	}
	
	@Override
	public boolean register(int id, String aLocalName, int aMaxStack, String aDescript) {		
		return register(id, aLocalName, 1, new String[] {aDescript, EnumChatFormatting.DARK_GRAY+"Used to further your knowledge"}, EnumRarity.common, EnumChatFormatting.LIGHT_PURPLE);
	}	
	
	@Override
	@SideOnly(Side.CLIENT)
	public final void registerIcons(final IIconRegister aIconRegister) {
		for (int i = 0, j = mLocalNames.size(); i < j; i++) {
			mIcons.put(i, aIconRegister.registerIcon(CORE.MODID + ":" + "research" + "/" + "note"));
		}
	}
	
}
