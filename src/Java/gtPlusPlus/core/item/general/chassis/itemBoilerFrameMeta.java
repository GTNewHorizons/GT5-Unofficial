public class itemBoilerChassis extends Item {

	public IIcon[] icon = new IIcon[1];

	public itemBoilerChassis() {
		super();
		this.setHasSubtypes(true);
		String unlocalizedName = "itemBoilerChassis";
		this.setUnlocalizedName(unlocalizedName);
		this.setCreativeTab(AddToCreativeTab.tabMisc);
		GameRegistry.registerItem(this, unlocalizedName);
	}

	@Override
	public void registerIcons(IIconRegister reg) {
		this.icons[0] = reg.registerIcon(CORE.MODID + ":" + "itemBoilerChassis");
	}

	@Override
	public IIcon getIconFromDamage(int meta) {
		return this.icons[0];
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < 3; i ++) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return this.getUnlocalizedName() + "_" + stack.getItemDamage();
	}

	@Override
	public String getItemStackDisplayName(final ItemStack tItem) {
		String itemName = "Advanced Boiler Chassis";
		String suffixName = "";
		if (tItem.getItemDamage() == 0){
			suffixName = " [T1]";
		}
		else if (tItem.getItemDamage() == 1){
			suffixName = " [T2]";
		}
		else if (tItem.getItemDamage() == 2){
			suffixName = " [T3]";
		}
		return (itemName+suffixName);
		
	}

	@Override //TODO
	public int getColorFromItemStack(final ItemStack stack, int HEX_OxFFFFFF) {
		int meta = stack.getItemDamage();
		if (meta == 0){
			HEX_OxFFFFFF = Utils.rgbToHex(10,110,30);
		}
		else if (meta == 1){
			HEX_OxFFFFFF = Utils.rgbToHex(150,180,35);
		}	
		else if (meta == 2){
			HEX_OxFFFFFF = Utils.rgbToHex(200,85,40);
		}
		else if (meta == 3){
			HEX_OxFFFFFF = Utils.rgbToHex(255,100,50);
		}
		return HEX_OxFFFFFF;
	}

}