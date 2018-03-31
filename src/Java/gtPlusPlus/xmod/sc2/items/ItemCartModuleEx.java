package gtPlusPlus.xmod.sc2.items;

import vswe.stevescarts.Modules.ModuleBase;
import net.minecraft.nbt.NBTTagCompound;
import vswe.stevescarts.TileEntities.TileEntityCartAssembler;
import net.minecraft.entity.player.EntityPlayer;
import java.util.List;
import java.util.Iterator;
import net.minecraft.client.renderer.texture.IIconRegister;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import vswe.stevescarts.ModuleData.ModuleData;
import vswe.stevescarts.ModuleData.ModuleDataLoader;

import net.minecraft.item.ItemStack;
import net.minecraft.creativetab.CreativeTabs;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.Items.ItemCartModule;

import net.minecraft.util.IIcon;
import net.minecraft.item.Item;

public class ItemCartModuleEx extends ItemCartModule {
	IIcon unknownIcon;

	public ItemCartModuleEx() {
		
	}

	public String getName(final ItemStack par1ItemStack) {
		final ModuleData data = this.getModuleData(par1ItemStack, true);
		if (data == null) {
			return "Unknown SC2 module";
		}
		return data.getName();
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(final int dmg) {
		final ModuleData data = ModuleData.getList().get((byte) dmg);
		if (data != null) {
			return data.getIcon();
		}
		return this.unknownIcon;
	}

	@SideOnly(Side.CLIENT)
	public void registerIcons(final IIconRegister register) {
		for (final ModuleData module : ModuleData.getList().values()) {
			module.createIcon(register);
		}
		final StringBuilder sb = new StringBuilder();
		StevesCarts.instance.getClass();
		this.unknownIcon = register
				.registerIcon(sb.append("stevescarts").append(":").append("unknown_icon").toString());
	}

	public String getUnlocalizedName() {
		return "item.SC2:unknownmodule";
	}

	public String getUnlocalizedName(final ItemStack item) {
		final ModuleData data = this.getModuleData(item, true);
		if (data != null) {
			return "item.SC2:" + data.getRawName();
		}
		return this.getUnlocalizedName();
	}

	@SideOnly(Side.CLIENT)
	public void getSubItems(final Item item, final CreativeTabs par2CreativeTabs, final List par3List) {
		for (final ModuleData module : ModuleDataLoader.getList().values()) {
			if (module.getIsValid()) {
				par3List.add(module.getItemStack());
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(final ItemStack par1ItemStack, final EntityPlayer par2EntityPlayer, final List par3List,
			final boolean par4) {
		final ModuleData module = this.getModuleData(par1ItemStack, true);
		if (module != null) {
			module.addInformation(par3List, par1ItemStack.getTagCompound());
		} else if (par1ItemStack != null && par1ItemStack.getItem() instanceof ItemCartModuleEx) {
			par3List.add("Module id " + par1ItemStack.getItemDamage());
		} else {
			par3List.add("Unknown module id");
		}
	}

	public ModuleData getModuleData(final ItemStack itemstack, final boolean ignoreSize) {
		if (itemstack != null && itemstack.getItem() instanceof ItemCartModuleEx
				&& (ignoreSize || itemstack.stackSize != TileEntityCartAssembler.getRemovedSize())) {
			return ModuleData.getList().get((byte) itemstack.getItemDamage());
		}
		return null;
	}

}