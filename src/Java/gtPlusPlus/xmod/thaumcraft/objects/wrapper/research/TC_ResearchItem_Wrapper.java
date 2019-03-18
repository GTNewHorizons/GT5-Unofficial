package gtPlusPlus.xmod.thaumcraft.objects.wrapper.research;

import gtPlusPlus.xmod.thaumcraft.objects.wrapper.aspect.TC_AspectList_Wrapper;
import gtPlusPlus.xmod.thaumcraft.objects.wrapper.aspect.TC_Aspect_Wrapper;
import gtPlusPlus.xmod.thaumcraft.util.ThaumcraftUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class TC_ResearchItem_Wrapper {

	public final String key;
	public final String category;
	public final TC_AspectList_Wrapper tags;
	public String[] parents = null;
	public String[] parentsHidden = null;
	public String[] siblings = null;
	public final int displayColumn;
	public final int displayRow;
	public final ItemStack icon_item;
	public final ResourceLocation icon_resource;
	private int complexity;
	private boolean isSpecial;
	private boolean isSecondary;
	private boolean isRound;
	private boolean isStub;
	private boolean isVirtual;
	private boolean isConcealed;
	private boolean isHidden;
	private boolean isLost;
	private boolean isAutoUnlock;
	private ItemStack[] itemTriggers;
	private String[] entityTriggers;
	private TC_Aspect_Wrapper[] aspectTriggers;
	private Object[] pages = null;

	public TC_ResearchItem_Wrapper(String key, String category) {
		this.key = key;
		this.category = category;
		this.tags = new TC_AspectList_Wrapper();
		this.icon_resource = null;
		this.icon_item = null;
		this.displayColumn = 0;
		this.displayRow = 0;
		this.setVirtual();
	}

	public TC_ResearchItem_Wrapper(String key, String category, TC_AspectList_Wrapper tags, int col, int row, int complex,
			ResourceLocation icon) {
		this.key = key;
		this.category = category;
		this.tags = tags;
		this.icon_resource = icon;
		this.icon_item = null;
		this.displayColumn = col;
		this.displayRow = row;
		this.complexity = complex;
		if (this.complexity < 1) {
			this.complexity = 1;
		}

		if (this.complexity > 3) {
			this.complexity = 3;
		}

	}

	public TC_ResearchItem_Wrapper(String key, String category, TC_AspectList_Wrapper tags, int col, int row, int complex, ItemStack icon) {
		this.key = key;
		this.category = category;
		this.tags = tags;
		this.icon_item = icon;
		this.icon_resource = null;
		this.displayColumn = col;
		this.displayRow = row;
		this.complexity = complex;
		if (this.complexity < 1) {
			this.complexity = 1;
		}

		if (this.complexity > 3) {
			this.complexity = 3;
		}

	}
	
	public TC_ResearchItem_Wrapper setSpecial() {
		this.isSpecial = true;
		return this;
	}

	public TC_ResearchItem_Wrapper setStub() {
		this.isStub = true;
		return this;
	}

	public TC_ResearchItem_Wrapper setLost() {
		this.isLost = true;
		return this;
	}

	public TC_ResearchItem_Wrapper setConcealed() {
		this.isConcealed = true;
		return this;
	}

	public TC_ResearchItem_Wrapper setHidden() {
		this.isHidden = true;
		return this;
	}

	public TC_ResearchItem_Wrapper setVirtual() {
		this.isVirtual = true;
		return this;
	}

	public TC_ResearchItem_Wrapper setParents(String... par) {
		this.parents = par;
		return this;
	}

	public TC_ResearchItem_Wrapper setParentsHidden(String... par) {
		this.parentsHidden = par;
		return this;
	}

	public TC_ResearchItem_Wrapper setSiblings(String... sib) {
		this.siblings = sib;
		return this;
	}

	public TC_ResearchItem_Wrapper setPages(Object... par) {
		this.pages = par;
		return this;
	}

	public Object[] getPages() {
		return this.pages;
	}

	public TC_ResearchItem_Wrapper setItemTriggers(ItemStack... par) {
		this.itemTriggers = par;
		return this;
	}

	public TC_ResearchItem_Wrapper setEntityTriggers(String... par) {
		this.entityTriggers = par;
		return this;
	}

	public TC_ResearchItem_Wrapper setAspectTriggers(TC_Aspect_Wrapper... par) {
		this.aspectTriggers = par;
		return this;
	}

	public ItemStack[] getItemTriggers() {
		return this.itemTriggers;
	}

	public String[] getEntityTriggers() {
		return this.entityTriggers;
	}

	public TC_Aspect_Wrapper[] getAspectTriggers() {
		return this.aspectTriggers;
	}

	public TC_ResearchItem_Wrapper registerResearchItem() {
		ThaumcraftUtils.addResearch(this);
		return this;
	}

	public String getName() {
		return StatCollector.translateToLocal("tc.research_name." + this.key);
	}

	public String getText() {
		return StatCollector.translateToLocal("tc.research_text." + this.key);
	}

	public boolean isSpecial() {
		return this.isSpecial;
	}

	public boolean isStub() {
		return this.isStub;
	}

	public boolean isLost() {
		return this.isLost;
	}

	public boolean isConcealed() {
		return this.isConcealed;
	}

	public boolean isHidden() {
		return this.isHidden;
	}

	public boolean isVirtual() {
		return this.isVirtual;
	}

	public boolean isAutoUnlock() {
		return this.isAutoUnlock;
	}

	public TC_ResearchItem_Wrapper setAutoUnlock() {
		this.isAutoUnlock = true;
		return this;
	}

	public boolean isRound() {
		return this.isRound;
	}

	public TC_ResearchItem_Wrapper setRound() {
		this.isRound = true;
		return this;
	}

	public boolean isSecondary() {
		return this.isSecondary;
	}

	public TC_ResearchItem_Wrapper setSecondary() {
		this.isSecondary = true;
		return this;
	}

	public int getComplexity() {
		return this.complexity;
	}

	public TC_ResearchItem_Wrapper setComplexity(int complexity) {
		this.complexity = complexity;
		return this;
	}

	public TC_Aspect_Wrapper getResearchPrimaryTag() {
		//TODO
		return null;
	}
	
}
