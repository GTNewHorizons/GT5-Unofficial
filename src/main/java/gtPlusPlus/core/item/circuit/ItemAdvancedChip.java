package gtPlusPlus.core.item.circuit;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_GUIDialogSelectItem;
import gregtech.api.interfaces.INetworkUpdatableItem;
import gregtech.api.net.GT_Packet_UpdateItem;
import gregtech.api.objects.XSTR;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.FakePlayer;
import org.apache.commons.lang3.tuple.Pair;

public class ItemAdvancedChip extends Item implements INetworkUpdatableItem {
	private static final List<ItemStack> ALL_VARIANTS = new ArrayList<>();
	protected IIcon base;
	
	public ItemAdvancedChip() {
		this.setHasSubtypes(true);
		this.setNoRepair();
		this.setMaxStackSize(64);
		this.setMaxDamage(0);
		this.setUnlocalizedName("T3RecipeSelector");
		GameRegistry.registerItem(this, this.getUnlocalizedName());
		ALL_VARIANTS.add(new ItemStack(this, 0, 0));
		for (int i = 1; i <= 24; i++) {
			ItemStack aStack = new ItemStack(this, 0, i);
			ALL_VARIANTS.add(aStack);
		}
	}
	
	@Override
	public boolean isDamageable() {
		return false;
	}

	@Override
	public boolean shouldRotateAroundWhenRendering() {
		return super.shouldRotateAroundWhenRendering();
	}

	@Override
	public void onUpdate(ItemStack p_77663_1_, World p_77663_2_, Entity p_77663_3_, int p_77663_4_, boolean p_77663_5_) {
		super.onUpdate(p_77663_1_, p_77663_2_, p_77663_3_, p_77663_4_, p_77663_5_);
	}

	@Override
	public String getItemStackDisplayName(ItemStack aStack) {
		return super.getItemStackDisplayName(aStack);
	}

	@Override
	public void addInformation(ItemStack aStack, EntityPlayer p_77624_2_, List aList, boolean p_77624_4_) {
		try {
			aList.add("Configuration == "+aStack.getItemDamage());
			aList.add(GT_LanguageManager.addStringLocalization(new StringBuilder().append(getUnlocalizedName()).append(".tooltip.0").toString(), "Right click to reconfigure"));
			aList.add(GT_LanguageManager.addStringLocalization(new StringBuilder().append(getUnlocalizedName()).append(".tooltip.1").toString(), "Needs a screwdriver or circuit programming tool"));
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
		super.addInformation(aStack, p_77624_2_, aList, p_77624_4_);
	}

	@Override
	public EnumRarity getRarity(ItemStack p_77613_1_) {
		return EnumRarity.common;
	}

	@Override
	public void getSubItems(Item aItem, CreativeTabs p_150895_2_, List aList) {
			aList.add(ItemUtils.simpleMetaStack(aItem, 0, 1));		
	}

	@Override
	public boolean getIsRepairable(ItemStack p_82789_1_, ItemStack p_82789_2_) {
		return false;
	}

	@Override
	public boolean isRepairable() {
		return false;
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}

	@Override
	public int getDisplayDamage(ItemStack stack) {
		return stack.getItemDamage();
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return false;
	}

	@Override
	public int getItemEnchantability() {
		return 0;
	}

	@Override
	public int getItemEnchantability(ItemStack stack) {
		return 0;
	}

	@Override
	public void registerIcons(final IIconRegister u) {
		this.base = u.registerIcon(CORE.MODID + ":" + "science/general/AdvancedCircuit");				
	}
	
	@Override
	public IIcon getIconFromDamageForRenderPass(final int damage, final int pass) {
		return this.base;		
	}

	@Override
	public IIcon getIconFromDamage(int damage) {
		return this.base;	
	}

	@Override
	public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
		return this.base;
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return this.base;	
	}

	@Override
    public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName();
    }

	@Override
	public boolean receive(ItemStack stack, EntityPlayerMP player, NBTTagCompound tag) {
		int meta = tag.hasKey("meta", Constants.NBT.TAG_BYTE) ? tag.getByte("meta") : -1;
		if (meta < 0 || meta > 24)
			return true;

		if (!player.capabilities.isCreativeMode) {
			Pair<Integer, BiFunction<ItemStack, EntityPlayerMP, ItemStack>> toolIndex = findConfiguratorInInv(player);
			if (toolIndex == null) return true;

			ItemStack[] mainInventory = player.inventory.mainInventory;
			mainInventory[toolIndex.getKey()] = toolIndex.getValue().apply(mainInventory[toolIndex.getKey()], player);
		}
		stack.setItemDamage(meta);

		return true;
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xOffset, float yOffset, float zOffset) {
		// nothing on server side or fake player
		if (player instanceof FakePlayer || !world.isRemote) return false;
		// check if any screwdriver
		ItemStack configuratorStack;
		if (player.capabilities.isCreativeMode) {
			configuratorStack = null;
		} else {
			Pair<Integer, ?> configurator = findConfiguratorInInv(player);
			if (configurator == null) {
				int count;
				try {
					count = Integer.parseInt(StatCollector.translateToLocal("GT5U.item.programmed_circuit.no_screwdriver.count"));
				} catch (NumberFormatException e) {
					player.addChatComponentMessage(new ChatComponentText("Error in translation GT5U.item.programmed_circuit.no_screwdriver.count: " + e.getMessage()));
					count = 1;
				}
				player.addChatComponentMessage(new ChatComponentTranslation("GT5U.item.programmed_circuit.no_screwdriver." + XSTR.XSTR_INSTANCE.nextInt(count)));
				return false;
			}
			configuratorStack = player.inventory.mainInventory[configurator.getKey()];
		}
		openSelectorGui(configuratorStack, stack.getItemDamage());
		return true;
	}

	private void openSelectorGui(ItemStack configurator, int meta) {
		FMLCommonHandler.instance().showGuiScreen(new GT_GUIDialogSelectItem(
				StatCollector.translateToLocal("GT5U.item.programmed_circuit.select.header"),
				configurator,
				null,
				ItemAdvancedChip::onConfigured,
				ALL_VARIANTS,
				meta,
				true
		));
	}

	private static void onConfigured(ItemStack stack) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setByte("meta", (byte) stack.getItemDamage());
		GT_Values.NW.sendToServer(new GT_Packet_UpdateItem(tag));
	}

	private static Pair<Integer, BiFunction<ItemStack, EntityPlayerMP, ItemStack>> findConfiguratorInInv(EntityPlayer player) {
		ItemStack[] mainInventory = player.inventory.mainInventory;
		for (int j = 0, mainInventoryLength = mainInventory.length; j < mainInventoryLength; j++) {
			ItemStack toolStack = mainInventory[j];

			if (!GT_Utility.isStackValid(toolStack))
				continue;

			for (Map.Entry<Predicate<ItemStack>, BiFunction<ItemStack, EntityPlayerMP, ItemStack>> p : GregTech_API.sCircuitProgrammerList.entrySet())
				if (p.getKey().test(toolStack))
					return Pair.of(j, p.getValue());
		}
		return null;
	}
}
