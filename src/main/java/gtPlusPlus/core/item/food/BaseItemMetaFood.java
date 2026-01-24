package gtPlusPlus.core.item.food;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.objects.ItemData;
import gregtech.api.objects.MaterialStack;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class BaseItemMetaFood extends ItemFood {

    private static final HashMap<Integer, IIcon> mIconMap = new HashMap<>();
    private static int mTotalMetaItems = 0;

    /*
     * 0 - Raw Human Meat 1 - Cooked Human Meat 2 - Raw Horse Meat 3 - Cooked Horse Meat 4 - Raw Wolf Meat 5 - Cooked
     * Wolf Meat 6 - Raw Ocelot Meat 7 - Cooked Ocelot Meat 8 - Blaze Flesh
     */

    // listAllmeatraw
    // listAllmeatcooked

    public void registerMetaFoods() {
        GregtechItemList.RawHumanMeat.set(
            registerNewMetaFood(
                0,
                StatCollector.translateToLocal("GTPP.tooltip.meta_food.unless_starving"),
                2,
                0,
                64,
                getPotionEffectPackage(new EffectWeaknessModerate(80), new EffectSlownessModerate(80)),
                getOreDictNamesAsArrayList("listAllmeatraw")));

        GregtechItemList.CookedHumanMeat.set(
            registerNewMetaFood(
                1,
                StatCollector.translateToLocal("GTPP.tooltip.meta_food.better_cooked"),
                4,
                1,
                64,
                getPotionEffectPackage(new EffectWeaknessBasic(50), new EffectSlownessBasic(50)),
                getOreDictNamesAsArrayList("listAllmeatcooked")));

        GregtechItemList.RawHorseMeat.set(
            registerNewMetaFood(
                2,
                "",
                2,
                0,
                64,
                getPotionEffectPackage(new EffectWeaknessBasic(30), new EffectSlownessBasic(30)),
                getOreDictNamesAsArrayList("listAllmeatraw")));

        GregtechItemList.CookedHorseMeat
            .set(registerNewMetaFood(3, "", 4, 1, 64, getOreDictNamesAsArrayList("listAllmeatcooked")));

        GregtechItemList.RawWolfMeat.set(
            registerNewMetaFood(
                4,
                "",
                2,
                0,
                64,
                getPotionEffectPackage(new EffectWeaknessBasic(25), new EffectSlownessBasic(30)),
                getOreDictNamesAsArrayList("listAllmeatraw")));

        GregtechItemList.CookedWolfMeat
            .set(registerNewMetaFood(5, "", 4, 1, 64, getOreDictNamesAsArrayList("listAllmeatcooked")));

        GregtechItemList.RawOcelotMeat.set(
            registerNewMetaFood(
                6,
                "",
                2,
                0,
                64,
                getPotionEffectPackage(new EffectWeaknessBasic(30), new EffectSlownessBasic(25)),
                getOreDictNamesAsArrayList("listAllmeatraw")));

        GregtechItemList.CookedOcelotMeat
            .set(registerNewMetaFood(7, "", 4, 1, 64, getOreDictNamesAsArrayList("listAllmeatcooked")));

        GregtechItemList.BlazeFlesh.set(
            registerNewMetaFood(
                8,
                StatCollector.translateToLocal("GTPP.tooltip.meta_food.warm_touch"),
                EnumRarity.uncommon,
                4,
                1,
                64,
                new ArrayList<>(),
                new setOnFire(),
                getOreDictNamesAsArrayList("listAllmeatcooked")));
    }

    private static final HashMap<Integer, Integer> mMaxStackSizeMap = new HashMap<>();
    private static final HashMap<Integer, String> mTooltipMap = new HashMap<>();
    private static final HashMap<Integer, EnumRarity> mRarityMap = new HashMap<>();
    private static final HashMap<Integer, Integer> mHealAmountMap = new HashMap<>();
    private static final HashMap<Integer, Float> mSaturationAmountMap = new HashMap<>();
    private static final HashMap<Integer, ArrayList<PotionEffectPackage>> mPotionEffectsMap = new HashMap<>();
    private static final HashMap<Integer, Boolean> mHasSpecialBehaviourMap = new HashMap<>();
    private static final HashMap<Integer, SpecialFoodBehaviour> mSpecialBehaviourMap = new HashMap<>();
    private static final HashMap<Integer, ArrayList<String>> mOreDictNames = new HashMap<>();

    public ItemStack registerNewMetaFood(final int aMetaID, String aTooltip, final int aHealAmount,
        final float aSaturationModifier, final int aMaxStacksize, final ArrayList<String> aOreDictNames) {
        return registerNewMetaFood(
            aMetaID,
            aTooltip,
            EnumRarity.common,
            aHealAmount,
            aSaturationModifier,
            aMaxStacksize,
            new ArrayList<>(),
            null,
            aOreDictNames);
    }

    public ItemStack registerNewMetaFood(final int aMetaID, String aTooltip, final int aHealAmount,
        final float aSaturationModifier, final int aMaxStacksize, final ArrayList<PotionEffectPackage> aPotionEffects,
        final ArrayList<String> aOreDictNames) {
        return registerNewMetaFood(
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

    public ItemStack registerNewMetaFood(final int aMetaID, String aTooltip, EnumRarity aRarity, final int aHealAmount,
        final float aSaturationModifier, final int aMaxStacksize, final ArrayList<PotionEffectPackage> aPotionEffects,
        final SpecialFoodBehaviour aSpecialBehaviour, final ArrayList<String> aOreDictNames) {
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
        if (aOreDictNames.contains("listAllmeatraw")) {
            GTOreDictUnificator.addItemData(
                new ItemStack(this, 1, aMetaID),
                new ItemData(Materials.MeatRaw, GTValues.M, new MaterialStack(Materials.Bone, GTValues.M / 9)));
        } else if (aOreDictNames.contains("listAllmeatcooked")) {
            GTOreDictUnificator.addItemData(
                new ItemStack(this, 1, aMetaID),
                new ItemData(Materials.MeatCooked, GTValues.M, new MaterialStack(Materials.Bone, GTValues.M / 9)));
        }
        return new ItemStack(this, 1, aMetaID);
    }

    public void registerFoodsToOreDict() {
        for (int aMetaID = 0; aMetaID < mTotalMetaItems; aMetaID++) {
            ArrayList<String> aOreDictNames = mOreDictNames.get(aMetaID);
            if (aOreDictNames != null && !aOreDictNames.isEmpty()) {
                ItemStack aFoodStack = new ItemStack(this, 1, aMetaID);
                for (String aOreName : aOreDictNames) {
                    OreDictionary.registerOre(aOreName, aFoodStack);
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
        ArrayList<PotionEffectPackage> aPotionEffects = mPotionEffectsMap.get(getMetaKey(aStack));
        if (!aWorld.isRemote && aPotionEffects != null && !aPotionEffects.isEmpty()) {
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
            if (aTooltip != null && !aTooltip.isEmpty()) {
                aList.add(aTooltip);
            }
        } catch (Exception t) {
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

    private static ArrayList<PotionEffectPackage> getPotionEffectPackage(PotionEffectPackage... aEffects) {
        ArrayList<PotionEffectPackage> aPackage = new ArrayList<>();
        if (aEffects != null && aEffects.length > 0) {
            Collections.addAll(aPackage, aEffects);
        }
        return aPackage;
    }

    private static ArrayList<String> getOreDictNamesAsArrayList(String... aOreDictNames) {
        ArrayList<String> aPackage = new ArrayList<>();
        if (aOreDictNames != null) {
            aPackage.addAll(Arrays.asList(aOreDictNames));
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
            if (!aPlayer.isImmuneToFire()) {
                aPlayer.setFire(5);
            }
        }
    }
}
