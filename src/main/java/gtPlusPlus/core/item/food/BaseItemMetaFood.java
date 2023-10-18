package gtPlusPlus.core.item.food;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.EntityUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class BaseItemMetaFood extends ItemFood {

    private static final HashMap<Integer, IIcon> mIconMap = new HashMap<>();
    private static int mTotalMetaItems = 0;

    /*
     * 0 - Raw Human Meat 1 - Cooked Human Meat 2 - Raw Horse Meat 3 - Cooked Horse Meat 4 - Raw Wolf Meat 5 - Cooked
     * Wolf Meat 6 - Raw Ocelot Meat 7 - Cooked Ocelot Meat 8 - Blaze Flesh
     */

    // listAllmeatraw
    // listAllmeatcooked

    public static void registerMetaFoods() {
        registerNewMetaFood(
                0,
                "I wouldn't eat this unless I was starving",
                2,
                0,
                64,
                getPotionEffectPackage(new EffectWeaknessModerate(80), new EffectSlownessModerate(80)),
                getOreDictNamesAsArrayList("listAllmeatraw"));
        registerNewMetaFood(
                1,
                "Doesn't look any better cooked",
                4,
                1,
                64,
                getPotionEffectPackage(new EffectWeaknessBasic(50), new EffectSlownessBasic(50)),
                getOreDictNamesAsArrayList("listAllmeatcooked"));
        registerNewMetaFood(
                2,
                "",
                2,
                0,
                64,
                getPotionEffectPackage(new EffectWeaknessBasic(30), new EffectSlownessBasic(30)),
                getOreDictNamesAsArrayList("listAllmeatraw"));
        registerNewMetaFood(3, "", 4, 1, 64, getOreDictNamesAsArrayList("listAllmeatcooked"));
        registerNewMetaFood(
                4,
                "",
                2,
                0,
                64,
                getPotionEffectPackage(new EffectWeaknessBasic(25), new EffectSlownessBasic(30)),
                getOreDictNamesAsArrayList("listAllmeatraw"));
        registerNewMetaFood(5, "", 4, 1, 64, getOreDictNamesAsArrayList("listAllmeatcooked"));
        registerNewMetaFood(
                6,
                "",
                2,
                0,
                64,
                getPotionEffectPackage(new EffectWeaknessBasic(30), new EffectSlownessBasic(25)),
                getOreDictNamesAsArrayList("listAllmeatraw"));
        registerNewMetaFood(7, "", 4, 1, 64, getOreDictNamesAsArrayList("listAllmeatcooked"));
        registerNewMetaFood(
                8,
                "Warm to the touch",
                EnumRarity.uncommon,
                4,
                1,
                64,
                new AutoMap<>(),
                new setOnFire(),
                getOreDictNamesAsArrayList("listAllmeatcooked"));
    }

    private static final HashMap<Integer, Integer> mMaxStackSizeMap = new HashMap<>();
    private static final HashMap<Integer, String> mTooltipMap = new HashMap<>();
    private static final HashMap<Integer, EnumRarity> mRarityMap = new HashMap<>();
    private static final HashMap<Integer, Integer> mHealAmountMap = new HashMap<>();
    private static final HashMap<Integer, Float> mSaturationAmountMap = new HashMap<>();
    private static final HashMap<Integer, AutoMap<PotionEffectPackage>> mPotionEffectsMap = new HashMap<>();
    private static final HashMap<Integer, Boolean> mHasSpecialBehaviourMap = new HashMap<>();
    private static final HashMap<Integer, SpecialFoodBehaviour> mSpecialBehaviourMap = new HashMap<>();
    private static final HashMap<Integer, ArrayList<String>> mOreDictNames = new HashMap<>();

    public static void registerNewMetaFood(final int aMetaID, String aTooltip, final int aHealAmount,
            final float aSaturationModifier, final int aMaxStacksize) {
        registerNewMetaFood(
                aMetaID,
                aTooltip,
                EnumRarity.common,
                aHealAmount,
                aSaturationModifier,
                aMaxStacksize,
                new AutoMap<>(),
                null,
                new ArrayList<>());
    }

    public static void registerNewMetaFood(final int aMetaID, String aTooltip, final int aHealAmount,
            final float aSaturationModifier, final int aMaxStacksize, final ArrayList<String> aOreDictNames) {
        registerNewMetaFood(
                aMetaID,
                aTooltip,
                EnumRarity.common,
                aHealAmount,
                aSaturationModifier,
                aMaxStacksize,
                new AutoMap<>(),
                null,
                aOreDictNames);
    }

    public static void registerNewMetaFood(final int aMetaID, String aTooltip, final int aHealAmount,
            final float aSaturationModifier, final int aMaxStacksize,
            final AutoMap<PotionEffectPackage> aPotionEffects) {
        registerNewMetaFood(
                aMetaID,
                aTooltip,
                EnumRarity.common,
                aHealAmount,
                aSaturationModifier,
                aMaxStacksize,
                new AutoMap<>(),
                null,
                new ArrayList<>());
    }

    public static void registerNewMetaFood(final int aMetaID, String aTooltip, final int aHealAmount,
            final float aSaturationModifier, final int aMaxStacksize, final AutoMap<PotionEffectPackage> aPotionEffects,
            final ArrayList<String> aOreDictNames) {
        registerNewMetaFood(
                aMetaID,
                aTooltip,
                EnumRarity.common,
                aHealAmount,
                aSaturationModifier,
                aMaxStacksize,
                aPotionEffects,
                null,
                aOreDictNames);
    }

    public static void registerNewMetaFood(final int aMetaID, String aTooltip, EnumRarity aRarity,
            final int aHealAmount, final float aSaturationModifier, final int aMaxStacksize,
            final AutoMap<PotionEffectPackage> aPotionEffects, final SpecialFoodBehaviour aSpecialBehaviour) {
        registerNewMetaFood(
                aMetaID,
                aTooltip,
                EnumRarity.common,
                aHealAmount,
                aSaturationModifier,
                aMaxStacksize,
                aPotionEffects,
                null,
                new ArrayList<>());
    }

    public static void registerNewMetaFood(final int aMetaID, String aTooltip, EnumRarity aRarity,
            final int aHealAmount, final float aSaturationModifier, final int aMaxStacksize,
            final AutoMap<PotionEffectPackage> aPotionEffects, final SpecialFoodBehaviour aSpecialBehaviour,
            final ArrayList<String> aOreDictNames) {
        mTotalMetaItems++;
        mMaxStackSizeMap.put(aMetaID, aMaxStacksize);
        mTooltipMap.put(aMetaID, aTooltip);
        mRarityMap.put(aMetaID, aRarity);
        mHealAmountMap.put(aMetaID, aHealAmount);
        mSaturationAmountMap.put(aMetaID, aSaturationModifier);
        mPotionEffectsMap.put(aMetaID, aPotionEffects);
        mHasSpecialBehaviourMap.put(aMetaID, (aSpecialBehaviour != null));
        if (aSpecialBehaviour != null) {
            mSpecialBehaviourMap.put(aMetaID, aSpecialBehaviour);
        }
        mOreDictNames.put(aMetaID, aOreDictNames);
    }

    public static void registerFoodsToOreDict() {
        for (int aMetaID = 0; aMetaID < mTotalMetaItems; aMetaID++) {
            ArrayList<String> aOreDictNames = mOreDictNames.get(aMetaID);
            if (aOreDictNames != null && !aOreDictNames.isEmpty()) {
                ItemStack aFoodStack = ItemUtils.simpleMetaStack(ModItems.itemMetaFood, aMetaID, 1);
                for (String aOreName : aOreDictNames) {
                    ItemUtils.addItemToOreDictionary(aFoodStack, aOreName);
                }
            }
        }
    }

    public BaseItemMetaFood() {
        super(0, 0, false);
        this.setHasSubtypes(true);
        this.setNoRepair();
        this.setMaxStackSize(64);
        this.setMaxDamage(0);
        this.setUnlocalizedName("BasicMetaFood");
        this.setCreativeTab(AddToCreativeTab.tabMisc);
        GameRegistry.registerItem(this, this.getUnlocalizedName());
        BaseItemMetaFood.registerMetaFoods();
    }

    private static int getMetaKey(ItemStack aStack) {
        return aStack.getItemDamage();
    }

    // Heal Amount
    @Override
    public int func_150905_g(ItemStack aStack) {
        return mHealAmountMap.get(getMetaKey(aStack));
    }

    // Saturation Amount
    @Override
    public float func_150906_h(ItemStack aStack) {
        return mSaturationAmountMap.get(getMetaKey(aStack));
    }

    // Whether wolves like this food, sadly doesn't support meta items
    @Override
    public boolean isWolfsFavoriteMeat() {
        return false;
    }

    @Override
    protected void onFoodEaten(final ItemStack aStack, final World aWorld, final EntityPlayer aPlayer) {
        // super.onFoodEaten(stack, world, player);
        AutoMap<PotionEffectPackage> aPotionEffects = mPotionEffectsMap.get(getMetaKey(aStack));
        if (!aWorld.isRemote && aPotionEffects != null && aPotionEffects.size() > 0) {
            for (PotionEffectPackage aFoodEffect : aPotionEffects) {
                if (MathUtils.randInt(0, 100) <= aFoodEffect.getChance() || aFoodEffect.getChance() == 100) {
                    PotionEffect aEffect = aFoodEffect.getEffect();
                    if (aEffect != null && aEffect.getPotionID() > 0) {
                        aPlayer.addPotionEffect(
                                new PotionEffect(
                                        aEffect.getPotionID(),
                                        aEffect.getDuration() * 20,
                                        aEffect.getAmplifier(),
                                        aEffect.getIsAmbient()));
                    }
                }
            }
        }

        boolean aHasEpcialBehaviour = mHasSpecialBehaviourMap.get(getMetaKey(aStack));
        if (!aWorld.isRemote && aHasEpcialBehaviour) {
            SpecialFoodBehaviour aBehaviour = mSpecialBehaviourMap.get(getMetaKey(aStack));
            if (aBehaviour != null) {
                aBehaviour.doBehaviour(aPlayer);
            }
        }
    }

    @Override
    public ItemStack onEaten(ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
        return super.onEaten(aStack, aWorld, aPlayer);
    }

    @Override
    public int getItemStackLimit(ItemStack aStack) {
        return mMaxStackSizeMap.get(getMetaKey(aStack));
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
        super.onUpdate(p_77663_1_, p_77663_2_, p_77663_3_, p_77663_4_, p_77663_5_);
    }

    @Override
    public String getItemStackDisplayName(ItemStack aStack) {
        return super.getItemStackDisplayName(aStack);
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer p_77624_2_, List aList, boolean p_77624_4_) {
        try {
            String aTooltip = mTooltipMap.get(getMetaKey(aStack));
            if (aTooltip != null && aTooltip.length() > 0) {
                aList.add(aTooltip);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        super.addInformation(aStack, p_77624_2_, aList, p_77624_4_);
    }

    @Override
    public EnumRarity getRarity(ItemStack aStack) {
        return mRarityMap.get(getMetaKey(aStack));
    }

    @Override
    public boolean requiresMultipleRenderPasses() {
        return false;
    }

    @Override
    public void getSubItems(Item aItem, CreativeTabs p_150895_2_, List aList) {
        for (int i = 0; i < mIconMap.size(); i++) {
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
        for (int i = 0; i < mTotalMetaItems; i++) {
            String aPath = GTPlusPlus.ID + ":" + "food/MetaItem1/" + i;
            mIconMap.put(i, u.registerIcon(aPath));
        }
    }

    @Override
    public IIcon getIconFromDamageForRenderPass(final int damage, final int pass) {
        return mIconMap.get(damage);
    }

    @Override
    public IIcon getIconFromDamage(int damage) {
        return mIconMap.get(damage);
    }

    @Override
    public IIcon getIcon(ItemStack aStack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
        return mIconMap.get(getMetaKey(aStack));
    }

    @Override
    public IIcon getIcon(ItemStack aStack, int pass) {
        return mIconMap.get(getMetaKey(aStack));
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "." + stack.getItemDamage();
    }

    private static class PotionEffectPackage {

        private final PotionEffect mEffect;
        private final int mChance;

        private PotionEffectPackage(PotionEffect aEffect, int aChance) {
            mEffect = aEffect;
            mChance = aChance;
        }

        public PotionEffect getEffect() {
            return this.mEffect;
        }

        public int getChance() {
            return this.mChance;
        }
    }

    private static AutoMap<PotionEffectPackage> getPotionEffectPackage(PotionEffectPackage... aEffects) {
        AutoMap<PotionEffectPackage> aPackage = new AutoMap<>();
        if (aEffects != null && aEffects.length > 0) {
            for (PotionEffectPackage aEffect : aEffects) {
                aPackage.put(aEffect);
            }
        }
        return aPackage;
    }

    private static ArrayList<String> getOreDictNamesAsArrayList(String... aOreDictNames) {
        ArrayList<String> aPackage = new ArrayList<>();
        if (aOreDictNames != null && aOreDictNames.length > 0) {
            for (String aEffect : aOreDictNames) {
                aPackage.add(aEffect);
            }
        }
        return aPackage;
    }

    private static class EffectWeaknessBasic extends PotionEffectPackage {

        protected EffectWeaknessBasic(int aChance) {
            super(new PotionEffect(Potion.weakness.getId(), 1, 20), aChance);
        }
    }

    private static class EffectWeaknessModerate extends PotionEffectPackage {

        protected EffectWeaknessModerate(int aChance) {
            super(new PotionEffect(Potion.weakness.getId(), 2, 40), aChance);
        }
    }

    private static class EffectWeaknessSevere extends PotionEffectPackage {

        protected EffectWeaknessSevere(int aChance) {
            super(new PotionEffect(Potion.weakness.getId(), 3, 60), aChance);
        }
    }

    private static class EffectSlownessBasic extends PotionEffectPackage {

        protected EffectSlownessBasic(int aChance) {
            super(new PotionEffect(Potion.moveSlowdown.getId(), 1, 20), aChance);
        }
    }

    private static class EffectSlownessModerate extends PotionEffectPackage {

        protected EffectSlownessModerate(int aChance) {
            super(new PotionEffect(Potion.moveSlowdown.getId(), 2, 40), aChance);
        }
    }

    private static class EffectSlownessSevere extends PotionEffectPackage {

        protected EffectSlownessSevere(int aChance) {
            super(new PotionEffect(Potion.moveSlowdown.getId(), 3, 60), aChance);
        }
    }

    private abstract static class SpecialFoodBehaviour {

        protected final int mChance;

        public SpecialFoodBehaviour(int aChance) {
            mChance = aChance;
        }

        public final void doBehaviour(EntityPlayer aPlayer) {
            if (aPlayer != null && !aPlayer.worldObj.isRemote) {
                if (MathUtils.randInt(0, 100) < mChance || mChance == 100) {
                    behaviour(aPlayer);
                }
            }
        }

        protected abstract void behaviour(EntityPlayer aPlayer);
    }

    private static class setOnFire extends SpecialFoodBehaviour {

        public setOnFire() {
            super(100);
        }

        @Override
        public void behaviour(EntityPlayer aPlayer) {
            EntityUtils.setEntityOnFire(aPlayer, 5);
        }
    }
}
