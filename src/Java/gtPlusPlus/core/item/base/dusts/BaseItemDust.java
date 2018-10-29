package gtPlusPlus.core.item.base.dusts;

import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;

public class BaseItemDust extends BaseItemComponent {



	private Material dustInfo;
	private BaseItemComponent[] mSizedDusts = new BaseItemComponent[2];

	public BaseItemDust(Material aMat) {
		this(aMat, true);
	}

	public BaseItemDust(Material aMat, boolean generateSmallDusts) {
		super(aMat, ComponentTypes.DUST);
		if (generateSmallDusts) {
			mSizedDusts[0] = new BaseItemComponent(aMat, ComponentTypes.DUSTSMALL);
			mSizedDusts[1] = new BaseItemComponent(aMat, ComponentTypes.DUSTTINY);
		}
	}

	public BaseItemDust(DustState aState, Material aMat) {
		super(aMat, ComponentTypes.DUST);		
		if (aState.generatesSmallDust()) {
			mSizedDusts[0] = new BaseItemComponent(aMat, ComponentTypes.DUSTSMALL);			
		}
		if (aState.generatesTinyDust()) {
			mSizedDusts[1] = new BaseItemComponent(aMat, ComponentTypes.DUSTTINY);			
		}
	}

	private BaseItemDust(final String unlocalizedName, final String materialName, final Material matInfo, final int colour, final String pileSize, final int tier){
		this(unlocalizedName, materialName, matInfo, colour, pileSize, tier, true);
	}

	private BaseItemDust(String unlocalizedName, String materialName, Material matInfo, int colour, String pileSize, int tier, boolean addRecipes) {
		super(matInfo, ComponentTypes.DUST);

		try {/*
			this.setUnlocalizedName(unlocalizedName);
			this.setMaxStackSize(64);

			this.setCreativeTab(tabMisc);
			this.colour = colour;
			this.mTier = tier;
			this.materialName = materialName;
			this.dustInfo = matInfo;
			this.setTextureName(this.getCorrectTexture(pileSize));
			GameRegistry.registerItem(this, unlocalizedName);

			String temp = "";
			Logger.WARNING("Unlocalized name for OreDict nameGen: "+this.getUnlocalizedName());
			if (this.getUnlocalizedName().contains("item.")){
				temp = this.getUnlocalizedName().replace("item.", "");
				Logger.WARNING("Generating OreDict Name: "+temp);
			}
			else {
				temp = this.getUnlocalizedName();
			}
			if (temp.contains("DustTiny")){
				temp = temp.replace("itemD", "d");
				Logger.WARNING("Generating OreDict Name: "+temp);
			}
			else if (temp.contains("DustSmall")){
				temp = temp.replace("itemD", "d");
				Logger.WARNING("Generating OreDict Name: "+temp);
			}
			else {
				temp = temp.replace("itemD", "d");
				Logger.WARNING("Generating OreDict Name: "+temp);
			}
			if ((temp != null) && !temp.equals("")){
				GT_OreDictUnificator.registerOre(temp, ItemUtils.getSimpleStack(this));
			}
			if (addRecipes){
				this.addFurnaceRecipe();
				this.addMacerationRecipe();
			}		
		 */}
		catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private String getCorrectTexture(final String pileSize){

		if (!CORE.ConfigSwitches.useGregtechTextures || this.dustInfo.getTextureSet() == null){
			if ((pileSize == "dust") || (pileSize == "Dust")){
				this.setTextureName(CORE.MODID + ":" + "dust");}
			else{
				this.setTextureName(CORE.MODID + ":" + "dust"+pileSize);
			}
		}


		if (pileSize.toLowerCase().contains("small")){
			return "gregtech" + ":" + "materialicons/"+this.dustInfo.getTextureSet().mSetName+"/dustSmall";
		}
		else if (pileSize.toLowerCase().contains("tiny")){
			return "gregtech" + ":" + "materialicons/"+this.dustInfo.getTextureSet().mSetName+"/dustTiny";
		}
		return "gregtech" + ":" + "materialicons/"+this.dustInfo.getTextureSet().mSetName+"/dust";
	}

	/*	@Override
	public String getItemStackDisplayName(final ItemStack iStack) {

		String unlocal = super.getItemStackDisplayName(iStack);
		if (!unlocal.toLowerCase().contains(".name")) {
			return unlocal;
		}
		else {
			return unlocal;
		}

	}*/

	/*	@Override
	public void onUpdate(final ItemStack iStack, final World world, final Entity entityHolding, final int p_77663_4_, final boolean p_77663_5_) {
		try {
			if (this.dustInfo != null){
				if (entityHolding instanceof EntityPlayer){
					if (!((EntityPlayer) entityHolding).capabilities.isCreativeMode){
						EntityUtils.applyRadiationDamageToEntity(iStack.stackSize, this.dustInfo.vRadiationLevel, world, entityHolding);	
					}
				}
			}		
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
	}*/

	/*@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {

		if (stack.getDisplayName().toLowerCase().contains("fluorite")){
			list.add("Mined from Sandstone and Limestone.");
		}
		if (this.dustInfo != null){
			list.add(this.dustInfo.vChemicalFormula);
		}
		if (this.dustInfo.vRadiationLevel > 0){
			list.add(CORE.GT_Tooltip_Radioactive);
		}


		//}
		super.addInformation(stack, aPlayer, list, bool);
	}*/

	public static class DustState {
		static final int NORMAL = (1);
		static final int SMALL = (10);
		static final int TINY = (100);
		final int MIXTURE;	
		final boolean[] doesThings = new boolean[3];

		public DustState (boolean genDust, boolean genSmallDust, boolean genDustTiny){
			int aTotal = 0;
			if (genDust) {
				aTotal += NORMAL;
				doesThings[0] = true;
			}
			else {
				doesThings[0] = false;				
			}
			if (genSmallDust) {
				aTotal += SMALL;
				doesThings[1] = true;
			}
			else {
				doesThings[1] = false;				
			}
			if (genDustTiny) {
				aTotal += TINY;
				doesThings[2] = true;
			}
			else {
				doesThings[2] = false;				
			}
			MIXTURE = aTotal;
		}

		public boolean generatesDust() {
			return doesThings[0];
		}
		public boolean generatesSmallDust() {
			return doesThings[1];
		}
		public boolean generatesTinyDust() {
			return doesThings[2];
		}

		private DustState(int amount) {
			
			if (amount == 1) {
				doesThings[0] = true;
				doesThings[1] = false;
				doesThings[2] = false;
				
			}
			else if (amount == 10) {
				doesThings[0] = false;
				doesThings[1] = true;
				doesThings[2] = false;
				
			}
			else if (amount == 100) {
				doesThings[0] = false;
				doesThings[1] = false;
				doesThings[2] = true;
				
			}
			else if (amount == 11) {
				doesThings[0] = true;
				doesThings[1] = true;
				doesThings[2] = false;
				
			}
			else if (amount == 101) {
				doesThings[0] = true;
				doesThings[1] = false;
				doesThings[2] = true;
				
			}
			else if (amount == 110) {
				doesThings[0] = false;
				doesThings[1] = true;
				doesThings[2] = true;
				
			}
			else if (amount == 111) {
				doesThings[0] = true;
				doesThings[1] = true;
				doesThings[2] = true;				
			}
			else {
				doesThings[0] = false;
				doesThings[1] = false;
				doesThings[2] = false;
			}
			MIXTURE = amount;			
		}

		public DustState get(int a) {
			if (a == 1) {
				return new DustState(NORMAL);
			}
			else if (a == 10) {
				return new DustState(SMALL);
			}
			else if (a == 100) {
				return new DustState(TINY);
			}
			else {
				return new DustState(MIXTURE);
			}
		}
	}

}
