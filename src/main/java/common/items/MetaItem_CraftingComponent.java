package common.items;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import kekztech.KekzCore;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class MetaItem_CraftingComponent extends Item {
	
	private static final MetaItem_CraftingComponent INSTANCE = new MetaItem_CraftingComponent();
	private final IIcon[] icons = new IIcon[16];
	
	private MetaItem_CraftingComponent() { }
	
	public static MetaItem_CraftingComponent getInstance() {
		return INSTANCE;
	}
	
	public void registerItem() {
		super.setHasSubtypes(true);
		final String unlocalizedName = "kekztech_crafting_item";
		super.setUnlocalizedName(unlocalizedName);
		super.setCreativeTab(CreativeTabs.tabMisc);
		super.setMaxStackSize(64);
		GameRegistry.registerItem(getInstance(), unlocalizedName);
	}
	
	@Override
	public void registerIcons(IIconRegister reg) {
		int counter = 9;
		// Raw heat pipes
		/*icons[counter++] = reg.registerIcon(KekzCore.MODID + ":" + "CopperHeatPipe");
		icons[counter++] = reg.registerIcon(KekzCore.MODID + ":" + "SilverHeatPipe");
		icons[counter++] = reg.registerIcon(KekzCore.MODID + ":" + "BoronArsenideHeatPipe");
		icons[counter++] = reg.registerIcon(KekzCore.MODID + ":" + "DiamondHeatPipe");
		// Dust
		icons[counter++] = reg.registerIcon(KekzCore.MODID + ":" + "BoronArsenideDust");
		icons[counter++] = reg.registerIcon(KekzCore.MODID + ":" + "IsotopicallyPureDiamondDust");
		icons[counter++] = reg.registerIcon(KekzCore.MODID + ":" + "AmineCarbamateDust");
		// Crystal
		icons[counter++] = reg.registerIcon(KekzCore.MODID + ":" + "BoronArsenideCrystal");
		icons[counter++] = reg.registerIcon(KekzCore.MODID + ":" + "IsotopicallyPureDiamondCrystal");*/
		// Ceramics
		icons[counter++] = reg.registerIcon(KekzCore.MODID + ":" + "YSZCeramicDust");
		icons[counter++] = reg.registerIcon(KekzCore.MODID + ":" + "GDCCeramicDust");
		icons[counter++] = reg.registerIcon(KekzCore.MODID + ":" + "YttriaDust");
		icons[counter++] = reg.registerIcon(KekzCore.MODID + ":" + "ZirconiaDust");
		icons[counter++] = reg.registerIcon(KekzCore.MODID + ":" + "CeriaDust");
		icons[counter++] = reg.registerIcon(KekzCore.MODID + ":" + "YSZCeramicPlate");
		icons[counter++] = reg.registerIcon(KekzCore.MODID + ":" + "GDCCeramicPlate");
	}
	
	@Override
	public IIcon getIconFromDamage(int meta) {
		return icons[meta];
	}
	
	@SuppressWarnings({"unchecked"})
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for(int i = 9; i < icons.length; i++) {
			list.add(new ItemStack(item, 1, i));
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName() + "." + stack.getItemDamage();
	}
	
	@SuppressWarnings({"unchecked"})
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) {
		list.add("Crafting component for KekzTech things");
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return 0.0d;
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return false;
	}
	
	public ItemStack getStackFromDamage(int meta) {
		return new ItemStack(getInstance(), 1, meta);
	}
	
	public ItemStack getStackOfAmountFromDamage(int meta, int amount) {
		return new ItemStack(getInstance(), amount, meta);
	}
	
}
