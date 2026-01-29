package gtPlusPlus.plugin.agrichem.item.algae;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.List;

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
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.util.GTLog;
import gtPlusPlus.plugin.agrichem.AlgaeDefinition;
import gtPlusPlus.plugin.agrichem.IAlgalItem;
import gtPlusPlus.plugin.agrichem.logic.AlgaeGeneticData;

public class ItemAlgaeBase extends Item implements IAlgalItem {

    protected IIcon base;

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
    public void onUpdate(ItemStack p_77663_1_, World p_77663_2_, Entity p_77663_3_, int p_77663_4_,
        boolean p_77663_5_) {
        if (!p_77663_1_.hasTagCompound() || p_77663_1_.getTagCompound()
            .hasNoTags()) {
            p_77663_1_ = initNBT(p_77663_1_);
        }
        super.onUpdate(p_77663_1_, p_77663_2_, p_77663_3_, p_77663_4_, p_77663_5_);
    }

    @Override
    public String getItemStackDisplayName(ItemStack aStack) {
        return EnumChatFormatting.UNDERLINE + super.getItemStackDisplayName(aStack);
    }

    private String boolLoc(Boolean bool) {
        return StatCollector.translateToLocal(bool ? "GT5U.generic.true" : "GT5U.generic.false");
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer p_77624_2_, List aList, boolean p_77624_4_) {
        int aDam = aStack.getItemDamage();
        try {
            aList.add(
                StatCollector.translateToLocal(
                    "GTPP.algae." + AlgaeDefinition.getByIndex(aDam).mSimpleName.replace(" ", "_")
                        .toLowerCase() + ".name"));
            if (!aStack.hasTagCompound() || aStack.getTagCompound()
                .hasNoTags()) {
                aStack = initNBT(aStack);
            } else {
                NBTTagCompound aNBT = aStack.getTagCompound();
                boolean mRequiresLight = aNBT.getBoolean("mRequiresLight");
                boolean mSaltWater = aNBT.getBoolean("mSaltWater");
                boolean mFreshWater = aNBT.getBoolean("mFreshWater");
                byte mTempTolerance = aNBT.getByte("mTempTolerance");
                float mFertility = aNBT.getFloat("mFertility");
                float mProductionSpeed = aNBT.getFloat("mProductionSpeed");
                byte mLifespan = aNBT.getByte("mLifespan");
                int mGeneration = aNBT.getInteger("mGeneration");

                aList.add(
                    StatCollector.translateToLocalFormatted("GTPP.algae.tooltip.requires_light", boolLoc(mRequiresLight)));
                aList.add(StatCollector.translateToLocalFormatted("GTPP.algae.tooltip.salt_water", boolLoc(mSaltWater)));
                aList.add(StatCollector.translateToLocalFormatted("GTPP.algae.tooltip.fresh_water", boolLoc(mFreshWater)));
                aList.add(StatCollector.translateToLocalFormatted("GTPP.algae.tooltip.temp_tolerance", mTempTolerance));
                aList.add(StatCollector.translateToLocalFormatted("GTPP.algae.tooltip.growth", mFertility));
                aList.add(StatCollector.translateToLocalFormatted("GTPP.algae.tooltip.production", mProductionSpeed));
                aList.add(StatCollector.translateToLocalFormatted("GTPP.algae.tooltip.lifespan", mLifespan));
                aList.add(StatCollector.translateToLocalFormatted("GTPP.algae.tooltip.generation", mGeneration));
            }
        } catch (Exception e) {
            e.printStackTrace(GTLog.err);
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
        for (int i = 0; i < AlgaeDefinition.values().length; i++) {
            aList.add(new ItemStack(aItem, 1, i));
        }
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
    public boolean showDurabilityBar(ItemStack stack) {
        return false;
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
        return this.base;
    }

    @Override
    public void registerIcons(final IIconRegister i) {
        this.base = i.registerIcon(GTPlusPlus.ID + ":bioscience/BasicAlgae");
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
        if (!aStack.hasTagCompound() || aStack.getTagCompound()
            .hasNoTags()) {
            AlgaeGeneticData aGenes;
            if (aStack.getItemDamage() < 3 || aStack.getItemDamage() > 5) {
                aGenes = new AlgaeGeneticData();
            } else {
                byte aTemp, aLifespan;
                float aFert, aSpeed;

                int aDam = aStack.getItemDamage();
                aTemp = (byte) (aDam == 3 ? 0 : aDam == 4 ? 2 : 1);
                aLifespan = (byte) (aDam == 3 ? 1 : aDam == 4 ? 3f : 2f);
                aFert = aDam == 3 ? 2f : aDam == 4 ? 1f : 1.75f;
                aSpeed = aDam == 3 ? 1f : aDam == 4 ? 1.5f : 2f;

                aGenes = new AlgaeGeneticData(
                    true,
                    true,
                    AlgaeDefinition.getByIndex(aDam).mSaltWater,
                    AlgaeDefinition.getByIndex(aDam).mFreshWater,
                    aTemp,
                    aFert,
                    aSpeed,
                    aLifespan,
                    0);
            }
            aTag = aGenes.writeToNBT();
        } else {
            aTag = aStack.getTagCompound();
        }

        return new AlgaeGeneticData(aTag);
    }
}
