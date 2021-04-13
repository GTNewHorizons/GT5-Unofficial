package common.items;

import java.util.List;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.registry.GameRegistry;
import kekztech.KekzCore;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import reactor.items.CoolantCell;
import reactor.items.FuelRod;
import reactor.items.HeatExchanger;
import reactor.items.HeatVent;
import reactor.items.NeutronReflector;

public class MetaItem_ReactorComponent extends Item {
	
	private static final MetaItem_ReactorComponent INSTANCE = new MetaItem_ReactorComponent();
	private final IIcon[] icons = new IIcon[50];
	
	private MetaItem_ReactorComponent() { }
	
	public static MetaItem_ReactorComponent getInstance() {
		return INSTANCE;
	}
	
	public void registerItem() {
		super.setHasSubtypes(true);
		final String unlocalizedName = "kekztech_reactor_item";
		super.setUnlocalizedName(unlocalizedName);
		super.setCreativeTab(CreativeTabs.tabMisc);
		super.setMaxStackSize(1);
		//GameRegistry.registerItem(getInstance(), unlocalizedName);
	}
	
	@Override
	public void registerIcons(IIconRegister reg) {
		int counter = 0;
		for(String s : HeatVent.RESOURCE_NAMES) {
			icons[counter++] = reg.registerIcon(KekzCore.MODID + ":" + s);
		}
		for(String s : HeatExchanger.RESOURCE_NAME) {
			icons[counter++] = reg.registerIcon(KekzCore.MODID + ":" + s);
		}
		for(String s : FuelRod.RESOURCE_NAME) {
			icons[counter++] = reg.registerIcon(KekzCore.MODID + ":" + s);
		}
		for(String s : FuelRod.RESOURCE_NAME_DEPLETED) {
			icons[counter++] = reg.registerIcon(KekzCore.MODID + ":" + s);
		}
		for(String s : NeutronReflector.RESOURCE_NAME) {
			icons[counter++] = reg.registerIcon(KekzCore.MODID + ":" + s);
		}
		for(String s : CoolantCell.RESOURCE_NAME) {
			icons[counter++] = reg.registerIcon(KekzCore.MODID + ":" + s);
		}
	}
	
	@Override
	public IIcon getIconFromDamage(int meta) {
		return icons[meta];
	}
	
	@SuppressWarnings({"unchecked"})
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for(int i = 0; i < icons.length; i++) {
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
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			list.add("Property = 1");
		} else {
			list.add("Part for the Modular Nuclear Reactor");
			list.add("Hold " + EnumChatFormatting.BOLD + "[LSHIFT]" + EnumChatFormatting.RESET + EnumChatFormatting.GRAY + " to display properties");
		}
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		NBTTagCompound nbt = (stack.getTagCompound() == null) ? new NBTTagCompound() : stack.getTagCompound();
		if(nbt.getInteger("HEALTH") != 0 && nbt.getInteger("MAXHEALTH") != 0) {
			return 1 - (double) (nbt.getInteger("HEALTH") / nbt.getInteger("MAXHEALTH"));
		} else {
			return 0.0d;
		}
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		final int meta = stack.getItemDamage();
		return meta < 31 || meta > 45;
	}
	
	public ItemStack getStackFromDamage(int meta) {
		return new ItemStack(getInstance(), 1, meta);
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
