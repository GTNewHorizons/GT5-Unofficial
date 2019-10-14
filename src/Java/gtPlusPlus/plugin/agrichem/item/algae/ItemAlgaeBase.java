package gtPlusPlus.plugin.agrichem.item.algae;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.plugin.agrichem.AlgaeDefinition;
import gtPlusPlus.plugin.agrichem.IAlgalItem;
import gtPlusPlus.plugin.agrichem.logic.AlgaeGeneticData;
import gtPlusPlus.plugin.agrichem.logic.AlgaeGrowthRequirement;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemAlgaeBase extends Item implements IAlgalItem {

	protected IIcon base;	
	protected IIcon overlay;	
	
	public ItemAlgaeBase() {
		this.setHasSubtypes(true);
		this.setMaxDamage(127);
		this.setNoRepair();
		this.setMaxStackSize(32);
		this.setUnlocalizedName("BasicAlgaeItem");
		GameRegistry.registerItem(this, this.getUnlocalizedName());
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
		if (!p_77663_1_.hasTagCompound() || p_77663_1_.getTagCompound().hasNoTags()) {
			p_77663_1_ = initNBT(p_77663_1_);
		}
		super.onUpdate(p_77663_1_, p_77663_2_, p_77663_3_, p_77663_4_, p_77663_5_);
	}

	@Override
	public String getItemStackDisplayName(ItemStack aStack) {
		return EnumChatFormatting.UNDERLINE+super.getItemStackDisplayName(aStack);
	}

	@Override
	public void addInformation(ItemStack aStack, EntityPlayer p_77624_2_, List aList, boolean p_77624_4_) {
		int aDam = aStack.getItemDamage();
		try {
		aList.add(AlgaeDefinition.getByIndex(aDam).mSimpleName);
		if (!aStack.hasTagCompound() || aStack.getTagCompound().hasNoTags()) {
			aStack = initNBT(aStack);
		}
		else {
			NBTTagCompound aNBT = aStack.getTagCompound();
			boolean mRequiresLight = aNBT.getBoolean("mRequiresLight");
			boolean mSaltWater = aNBT.getBoolean("mSaltWater");
			boolean mFreshWater = aNBT.getBoolean("mFreshWater");
			byte mTempTolerance = aNBT.getByte("mTempTolerance");
			float mFertility = aNBT.getFloat("mFertility");
			float mProductionSpeed = aNBT.getFloat("mProductionSpeed");
			byte mLifespan = aNBT.getByte("mLifespan");
			int mGeneration = aNBT.getInteger("mGeneration");			

			aList.add("Requires Light: "+mRequiresLight);
			aList.add("Salt Water: "+mSaltWater);
			aList.add("Fresh Water: "+mFreshWater);
			aList.add("Temp Tolerance: "+mTempTolerance);
			aList.add("Growth: "+mFertility);
			aList.add("Production: "+mProductionSpeed);
			aList.add("Lifespan in days: "+mLifespan);
			aList.add("Generation: "+mGeneration);			
		}		
		}
		catch (Throwable t) {
			t.printStackTrace();
		}
		super.addInformation(aStack, p_77624_2_, aList, p_77624_4_);
	}

	@Override
	public EnumRarity getRarity(ItemStack p_77613_1_) {
		return EnumRarity.uncommon;
	}

	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	@Override
	public void getSubItems(Item aItem, CreativeTabs p_150895_2_, List aList) {
		for (int i=0;i<AlgaeDefinition.values().length;i++) {
			aList.add(ItemUtils.simpleMetaStack(aItem, i, 1));
		}
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
	public int getColorFromItemStack(ItemStack aStack, int aMeta) {
		return AlgaeDefinition.getByIndex(aStack.getItemDamage()).mColour;
	}
	
	@Override
	public IIcon getIconFromDamageForRenderPass(final int damage, final int pass) {
		if(pass == 0) {
			return this.base;
		}
		return this.overlay;	
	}

	@Override
	public void registerIcons(final IIconRegister i) {
		this.base = i.registerIcon(CORE.MODID + ":" + "bioscience/BasicAlgae");
		this.overlay = i.registerIcon(CORE.MODID + ":" + "bioscience/BasicAlgae" + "_Overlay");		
	}
	
	public static ItemStack initNBT(ItemStack aFreshAlgae) {
		NBTTagCompound aNewTag = new NBTTagCompound();
		ItemAlgaeBase aItem;
		if (aFreshAlgae.getItem() instanceof ItemAlgaeBase) {
			aItem = (ItemAlgaeBase) aFreshAlgae.getItem();
			if (!aFreshAlgae.hasTagCompound()) {
				AlgaeGeneticData y = aItem.getSpeciesData(aFreshAlgae);
				aNewTag = y.writeToNBT();				
				aFreshAlgae.setTagCompound(aNewTag);
			}
			else {
				aNewTag = aFreshAlgae.getTagCompound();
			}	
		}			
		return aFreshAlgae;
	}

	@Override
	public AlgaeDefinition getAlgaeType(ItemStack aStack) {
		return AlgaeDefinition.getByIndex(aStack != null ? aStack.getItemDamage() : 3);
	}

	@Override
	public AlgaeGeneticData getSpeciesData(ItemStack aStack) {
		NBTTagCompound aTag;
		if (!aStack.hasTagCompound() || aStack.getTagCompound().hasNoTags()) {
			aTag = new NBTTagCompound();
			AlgaeGeneticData aGenes;
			if (aStack.getItemDamage() < 3 || aStack.getItemDamage() > 5) {
				aGenes = new AlgaeGeneticData();
				aTag = aGenes.writeToNBT();
			}
			else {
				byte aTemp, aLifespan;
				float aFert, aSpeed;
				
				int aDam = aStack.getItemDamage();
				aTemp = (byte) (aDam == 3 ? 0 : aDam == 4 ? 2 : 1);
				aLifespan = (byte) (aDam == 3 ? 1 : aDam == 4 ? 3f : 2f);
				aFert = (float) (aDam == 3 ? 2f : aDam == 4 ? 1f : 1.75f);
				aSpeed = (float) (aDam == 3 ? 1f : aDam == 4 ? 1.5f : 2f);
				
				aGenes = new AlgaeGeneticData(
						true, true,
						AlgaeDefinition.getByIndex(aDam).mSaltWater, AlgaeDefinition.getByIndex(aDam).mFreshWater,
						aTemp,
						aFert,
						aSpeed,
						aLifespan,
						0,
						new AutoMap<AlgaeGrowthRequirement>());
				aTag = aGenes.writeToNBT();
			}
		}
		else {
			aTag = aStack.getTagCompound();
		}
		
		
		
		
		return new AlgaeGeneticData(aTag);
	}
	
	
	

}
