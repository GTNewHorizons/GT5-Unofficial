/*
 * spotless:off
 * KubaTech - Gregtech Addon
 * Copyright (C) 2022 - 2023  kuba6000
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <https://www.gnu.org/licenses/>.
 * spotless:on
 */

package kubatech.loaders;

import static kubatech.api.utils.ModUtils.isClientSided;
import static kubatech.api.utils.ModUtils.isDeobfuscatedEnvironment;
import static kubatech.tileentity.gregtech.multiblock.GT_MetaTileEntity_ExtremeExterminationChamber.DIAMOND_SPIKES_DAMAGE;
import static kubatech.tileentity.gregtech.multiblock.GT_MetaTileEntity_ExtremeExterminationChamber.MOB_SPAWN_INTERVAL;
import static kubatech.tileentity.gregtech.multiblock.GT_MetaTileEntity_ExtremeExterminationChamber.MobNameToRecipeMap;

import java.io.File;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.IMob;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.io.Files;
import com.google.gson.Gson;

import atomicstryker.infernalmobs.common.InfernalMobsCore;
import atomicstryker.infernalmobs.common.MobModifier;
import atomicstryker.infernalmobs.common.mods.api.ModifierLoader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_DummyWorld;
import kubatech.Tags;
import kubatech.api.LoaderReference;
import kubatech.api.helpers.EnderIOHelper;
import kubatech.api.helpers.InfernalHelper;
import kubatech.api.helpers.ProgressBarWrapper;
import kubatech.api.mobhandler.MobDrop;
import kubatech.api.network.LoadConfigPacket;
import kubatech.api.utils.GSONUtils;
import kubatech.api.utils.ModUtils;
import kubatech.config.Config;
import kubatech.config.OverridesConfig;
import kubatech.nei.Mob_Handler;
import kubatech.tileentity.gregtech.multiblock.GT_MetaTileEntity_ExtremeExterminationChamber;
import minetweaker.MineTweakerAPI;
import minetweaker.api.entity.IEntityDefinition;
import minetweaker.api.item.IItemStack;
import minetweaker.mc1710.item.MCItemStack;
import stanhebben.zenscript.value.IntRange;
import thaumcraft.common.items.wands.ItemWandCasting;

public class MobRecipeLoader {

    private static final Logger LOG = LogManager.getLogger(Tags.MODID + "[Mob Recipe Loader]");

    private static final String dropFewItemsName = isDeobfuscatedEnvironment ? "dropFewItems" : "func_70628_a";
    private static final String dropRareDropName = isDeobfuscatedEnvironment ? "dropRareDrop" : "func_70600_l";
    private static final String setSlimeSizeName = isDeobfuscatedEnvironment ? "setSlimeSize" : "func_70799_a";
    private static final String addRandomArmorName = isDeobfuscatedEnvironment ? "addRandomArmor" : "func_82164_bB";
    private static final String enchantEquipmentName = isDeobfuscatedEnvironment ? "enchantEquipment" : "func_82162_bC";
    private static final String randName = isDeobfuscatedEnvironment ? "rand" : "field_70146_Z";

    private static boolean alreadyGenerated = false;
    public static boolean isInGenerationProcess = false;
    public static final String randomEnchantmentDetectedString = "RandomEnchantmentDetected";

    public static class MobRecipe {

        public final ArrayList<MobDrop> mOutputs;
        public final int mEUt = 2000;
        public final int mDuration;
        public int mMaxDamageChance;
        public final boolean infernalityAllowed;
        public final boolean alwaysinfernal;
        public static droplist infernaldrops;
        public final boolean isPeacefulAllowed;
        public final EntityLiving entity;
        public final float maxEntityHealth;
        public final boolean isUsable;

        @SuppressWarnings("unchecked")
        public MobRecipe copy() {
            return new MobRecipe(
                (ArrayList<MobDrop>) mOutputs.clone(),
                mDuration,
                mMaxDamageChance,
                infernalityAllowed,
                alwaysinfernal,
                isPeacefulAllowed,
                entity,
                maxEntityHealth,
                isUsable);
        }

        private MobRecipe(ArrayList<MobDrop> mOutputs, int mDuration, int mMaxDamageChance, boolean infernalityAllowed,
            boolean alwaysinfernal, boolean isPeacefulAllowed, EntityLiving entity, float maxEntityHealth,
            boolean isUsable) {
            this.mOutputs = mOutputs;
            this.mDuration = mDuration;
            this.mMaxDamageChance = mMaxDamageChance;
            this.infernalityAllowed = infernalityAllowed;
            this.alwaysinfernal = alwaysinfernal;
            this.isPeacefulAllowed = isPeacefulAllowed;
            this.entity = entity;
            this.maxEntityHealth = maxEntityHealth;
            this.isUsable = isUsable;
        }

        public static MobRecipe generateMobRecipe(EntityLiving e, String entityID, ArrayList<MobDrop> outputs) {
            return new MobRecipe(e, entityID, outputs);
        }

        @SuppressWarnings("unchecked")
        private MobRecipe(EntityLiving e, String entityID, ArrayList<MobDrop> outputs) {
            if (infernaldrops == null && LoaderReference.InfernalMobs) {
                infernaldrops = new droplist();
                LOG.info("Generating Infernal drops");
                ArrayList<ModifierLoader<?>> modifierLoaders = (ArrayList<ModifierLoader<?>>) InfernalHelper
                    .getModifierLoaders()
                    .clone();
                int i = 0;
                for (ModifierLoader<?> modifierLoader : modifierLoaders) {
                    MobModifier nextMod = modifierLoader.make(null);
                    if (nextMod.getBlackListMobClasses() != null)
                        for (Class<?> cl : nextMod.getBlackListMobClasses()) if (e.getClass()
                            .isAssignableFrom(cl)) break;
                    i++;
                }
                if (i > 0) {
                    double chance = InfernalHelper.checkEntityClassForced(e) ? 1d
                        : (1d / InfernalHelper.getEliteRarity());
                    ArrayList<ItemStack> elitelist = InfernalHelper.getDropIdListElite();
                    for (ItemStack stack : elitelist) {
                        dropinstance instance = infernaldrops
                            .add(new dropinstance(stack.copy(), infernaldrops), chance / elitelist.size());
                        instance.isEnchatmentRandomized = true;
                        // noinspection ConstantConditions
                        instance.enchantmentLevel = stack.getItem()
                            .getItemEnchantability();
                    }
                    ArrayList<ItemStack> ultralist = InfernalHelper.getDropIdListUltra();
                    chance *= 1d / InfernalHelper.getUltraRarity();
                    for (ItemStack stack : ultralist) {
                        dropinstance instance = infernaldrops
                            .add(new dropinstance(stack.copy(), infernaldrops), chance / ultralist.size());
                        instance.isEnchatmentRandomized = true;
                        // noinspection ConstantConditions
                        instance.enchantmentLevel = stack.getItem()
                            .getItemEnchantability();
                    }
                    ArrayList<ItemStack> infernallist = InfernalHelper.getDropIdListInfernal();
                    chance *= 1d / InfernalHelper.getInfernoRarity();
                    for (ItemStack stack : infernallist) {
                        dropinstance instance = infernaldrops
                            .add(new dropinstance(stack.copy(), infernaldrops), chance / infernallist.size());
                        instance.isEnchatmentRandomized = true;
                        // noinspection ConstantConditions
                        instance.enchantmentLevel = stack.getItem()
                            .getItemEnchantability();
                    }
                }
            } else if (infernaldrops == null) infernaldrops = new droplist();

            infernalityAllowed = InfernalHelper.isClassAllowed(e);
            alwaysinfernal = InfernalHelper.checkEntityClassForced(e);
            isPeacefulAllowed = !(e instanceof IMob);

            mOutputs = (ArrayList<MobDrop>) outputs.clone();
            int maxdamagechance = 0;
            for (MobDrop o : mOutputs) if (o.damages != null) for (int v : o.damages.values()) maxdamagechance += v;
            mMaxDamageChance = maxdamagechance;
            maxEntityHealth = e.getMaxHealth();
            mDuration = Math.max(MOB_SPAWN_INTERVAL, (int) ((maxEntityHealth / DIAMOND_SPIKES_DAMAGE) * 10d));
            entity = e;
            isUsable = EnderIOHelper.canEntityBeCapturedWithSoulVial(e, entityID);
        }

        public void refresh() {
            int maxdamagechance = 0;
            for (MobDrop o : mOutputs) if (o.damages != null) for (int v : o.damages.values()) maxdamagechance += v;
            mMaxDamageChance = maxdamagechance;
        }

        public ItemStack[] generateOutputs(Random rnd, GT_MetaTileEntity_ExtremeExterminationChamber MTE,
            double attackDamage, int lootinglevel, boolean preferInfernalDrops) {
            MTE.lEUt = mEUt;
            MTE.mMaxProgresstime = Math.max(MOB_SPAWN_INTERVAL, (int) ((maxEntityHealth / attackDamage) * 10d));
            ArrayList<ItemStack> stacks = new ArrayList<>(mOutputs.size());
            for (MobDrop o : mOutputs) {
                int chance = o.chance;
                if (o.playerOnly) {
                    chance = (int) ((double) chance * Config.MobHandler.playerOnlyDropsModifier);
                    if (chance < 1) chance = 1;
                }
                int amount = o.stack.stackSize;
                if (o.lootable && lootinglevel > 0) {
                    chance += lootinglevel * 5000;
                    if (chance > 10000) {
                        int div = (int) Math.ceil(chance / 10000d);
                        amount *= div;
                        chance /= div;
                    }
                }
                if (chance == 10000 || rnd.nextInt(10000) < chance) {
                    ItemStack s = o.stack.copy();
                    s.stackSize = amount;
                    if (o.enchantable != null) EnchantmentHelper.addRandomEnchantment(rnd, s, o.enchantable);
                    if (o.damages != null) {
                        int rChance = rnd.nextInt(mMaxDamageChance);
                        int cChance = 0;
                        for (Map.Entry<Integer, Integer> damage : o.damages.entrySet()) {
                            cChance += damage.getValue();
                            if (rChance <= cChance) {
                                s.setItemDamage(damage.getKey());
                                break;
                            }
                        }
                    }
                    stacks.add(s);
                }
            }

            if (infernalityAllowed && mEUt * 8 < MTE.getMaxInputVoltage()
                && !InfernalHelper.getDimensionBlackList()
                    .contains(
                        MTE.getBaseMetaTileEntity()
                            .getWorld().provider.dimensionId)) {
                int p = 0;
                int mods = 0;
                if (alwaysinfernal || (preferInfernalDrops && rnd.nextInt(InfernalHelper.getEliteRarity()) == 0)) {
                    p = 1;
                    if (rnd.nextInt(InfernalHelper.getUltraRarity()) == 0) {
                        p = 2;
                        if (rnd.nextInt(InfernalHelper.getInfernoRarity()) == 0) p = 3;
                    }
                }
                ArrayList<ItemStack> infernalstacks = null;
                if (p > 0) if (p == 1) {
                    infernalstacks = InfernalHelper.getDropIdListElite();
                    mods = InfernalHelper.getMinEliteModifiers();
                } else if (p == 2) {
                    infernalstacks = InfernalHelper.getDropIdListUltra();
                    mods = InfernalHelper.getMinUltraModifiers();
                } else {
                    infernalstacks = InfernalHelper.getDropIdListInfernal();
                    mods = InfernalHelper.getMinInfernoModifiers();
                }
                if (infernalstacks != null) {
                    ItemStack infernalstack = infernalstacks.get(rnd.nextInt(infernalstacks.size()))
                        .copy();
                    // noinspection ConstantConditions
                    EnchantmentHelper.addRandomEnchantment(
                        rnd,
                        infernalstack,
                        infernalstack.getItem()
                            .getItemEnchantability());
                    stacks.add(infernalstack);
                    MTE.lEUt *= 8L;
                    MTE.mMaxProgresstime *= mods * InfernalMobsCore.instance()
                        .getMobModHealthFactor();
                }
            }

            return stacks.toArray(new ItemStack[0]);
        }
    }

    public static class fakeRand extends Random {

        private static class nexter {

            private final int type;
            private final int bound;
            private int next;

            public nexter(int type, int bound) {
                this.next = 0;
                this.bound = bound;
                this.type = type;
            }

            private int getType() {
                return type;
            }

            private boolean getBoolean() {
                return next == 1;
            }

            private int getInt() {
                return next;
            }

            private float getFloat() {
                return next * 0.1f;
            }

            private boolean next() {
                next++;
                return next >= bound;
            }
        }

        private final ArrayList<nexter> nexts = new ArrayList<>();
        private int walkCounter = 0;
        private double chance;
        private boolean exceptionOnEnchantTry = false;
        private int maxWalkCount = -1;
        private float forceFloatValue = -1.f;

        @Override
        public int nextInt(int bound) {
            if (exceptionOnEnchantTry && bound == Enchantment.enchantmentsBookList.length) return -1;
            if (nexts.size() <= walkCounter) { // new call
                if (maxWalkCount == walkCounter) {
                    return 0;
                }
                nexts.add(new nexter(0, bound));
                walkCounter++;
                chance /= bound;
                return 0;
            }
            chance /= bound;
            return nexts.get(walkCounter++)
                .getInt();
        }

        @Override
        public float nextFloat() {
            if (forceFloatValue != -1f) return forceFloatValue;
            if (nexts.size() <= walkCounter) { // new call
                if (maxWalkCount == walkCounter) {
                    return 0f;
                }
                nexts.add(new nexter(2, 10));
                walkCounter++;
                chance /= 10;
                return 0f;
            }
            chance /= 10;
            return nexts.get(walkCounter++)
                .getFloat();
        }

        @Override
        public boolean nextBoolean() {
            if (nexts.size() <= walkCounter) { // new call
                if (maxWalkCount == walkCounter) {
                    return false;
                }
                nexts.add(new nexter(1, 2));
                walkCounter++;
                chance /= 2;
                return false;
            }
            chance /= 2;
            return nexts.get(walkCounter++)
                .getBoolean();
        }

        public void newRound() {
            walkCounter = 0;
            nexts.clear();
            chance = 1d;
            maxWalkCount = -1;
            exceptionOnEnchantTry = false;
            forceFloatValue = -1f;
        }

        public boolean nextRound() {
            walkCounter = 0;
            chance = 1d;
            while (nexts.size() > 0 && nexts.get(nexts.size() - 1)
                .next()) nexts.remove(nexts.size() - 1);
            return nexts.size() > 0;
        }
    }

    private static class dropinstance {

        public boolean isDamageRandomized = false;
        public HashMap<Integer, Integer> damagesPossible = new HashMap<>();
        public boolean isEnchatmentRandomized = false;
        public int enchantmentLevel = 0;
        public final ItemStack stack;
        public final GT_Utility.ItemId itemId;
        private double dropchance = 0d;
        private int dropcount = 1;
        private final droplist owner;

        public dropinstance(ItemStack s, droplist owner) {
            this.owner = owner;
            stack = s;
            itemId = GT_Utility.ItemId.createNoCopy(stack);
        }

        public int getchance(int chancemodifier) {
            dropchance = (double) Math.round(dropchance * 100000) / 100000d;
            return (int) (dropchance * chancemodifier);
        }

        @Override
        public int hashCode() {
            return itemId.hashCode();
        }
    }

    public static class droplist {

        private final ArrayList<dropinstance> drops = new ArrayList<>();
        private final HashMap<GT_Utility.ItemId, Integer> dropschecker = new HashMap<>();

        public dropinstance add(dropinstance i, double chance) {
            if (contains(i)) {
                int ssize = i.stack.stackSize;
                i = get(dropschecker.get(i.itemId));
                i.dropchance += chance * ssize;
                i.dropcount += ssize;
                return i;
            }
            drops.add(i);
            i.dropchance += chance * i.stack.stackSize;
            i.dropcount += i.stack.stackSize - 1;
            i.stack.stackSize = 1;
            dropschecker.put(i.itemId, drops.size() - 1);
            return i;
        }

        public dropinstance get(int index) {
            return drops.get(index);
        }

        public dropinstance get(dropinstance i) {
            if (!contains(i)) return null;
            return get(dropschecker.get(i.itemId));
        }

        public boolean contains(dropinstance i) {
            return dropschecker.containsKey(i.itemId);
        }

        public boolean contains(ItemStack stack) {
            return dropschecker.containsKey(GT_Utility.ItemId.createNoCopy(stack));
        }

        public boolean isEmpty() {
            return drops.isEmpty();
        }

        public int size() {
            return drops.size();
        }

        public int indexOf(dropinstance i) {
            if (!contains(i)) return -1;
            return dropschecker.get(i.itemId);
        }
    }

    private static class dropCollector {

        final HashMap<GT_Utility.ItemId, Integer> damagableChecker = new HashMap<>();
        private boolean booksAlwaysRandomlyEnchanted = false;

        public void addDrop(droplist fdrops, ArrayList<EntityItem> listToParse, double chance) {
            for (EntityItem entityItem : listToParse) {
                ItemStack ostack = entityItem.getEntityItem();
                if (ostack == null) continue;
                dropinstance drop;
                boolean randomchomenchantdetected = ostack.hasTagCompound()
                    && ostack.stackTagCompound.hasKey(randomEnchantmentDetectedString);
                int randomenchantmentlevel = 0;
                if (randomchomenchantdetected) {
                    randomenchantmentlevel = ostack.stackTagCompound.getInteger(randomEnchantmentDetectedString);
                    ostack.stackTagCompound.removeTag("ench");
                    ostack.stackTagCompound.setInteger(randomEnchantmentDetectedString, 0);
                }
                if ((booksAlwaysRandomlyEnchanted || randomchomenchantdetected)
                    && Items.enchanted_book == ostack.getItem()) {
                    NBTTagCompound tagCompound = (NBTTagCompound) ostack.stackTagCompound.copy();
                    tagCompound.removeTag("StoredEnchantments");
                    ostack = new ItemStack(Items.book, ostack.stackSize, 0);
                    if (!tagCompound.hasNoTags()) ostack.stackTagCompound = tagCompound;
                    if (randomenchantmentlevel == 0) randomenchantmentlevel = 1;
                    randomchomenchantdetected = true;
                }
                boolean randomdamagedetected = false;
                int newdamage = -1;
                if (ostack.isItemStackDamageable()) {
                    int odamage = ostack.getItemDamage();
                    ostack.setItemDamage(1);
                    GT_Utility.ItemId id = GT_Utility.ItemId.createNoCopy(ostack);
                    damagableChecker.putIfAbsent(id, odamage);
                    int check = damagableChecker.get(id);
                    if (check != odamage) {
                        randomdamagedetected = true;
                        newdamage = odamage;
                        ostack.setItemDamage(check);
                    } else ostack.setItemDamage(odamage);
                }
                drop = fdrops.add(new dropinstance(ostack.copy(), fdrops), chance);
                if (!drop.isEnchatmentRandomized && randomchomenchantdetected) {
                    drop.isEnchatmentRandomized = true;
                    drop.enchantmentLevel = randomenchantmentlevel;
                }
                if (drop.isDamageRandomized && !randomdamagedetected) {
                    drop.damagesPossible.merge(drop.stack.getItemDamage(), 1, Integer::sum);
                }
                if (randomdamagedetected) {
                    if (!drop.isDamageRandomized) {
                        drop.isDamageRandomized = true;
                        drop.damagesPossible.merge(drop.stack.getItemDamage(), drop.dropcount - 1, Integer::sum);
                    }
                    if (newdamage == -1) newdamage = drop.stack.getItemDamage();
                    drop.damagesPossible.merge(newdamage, 1, Integer::sum);
                }
            }

            listToParse.clear();
        }

        public void newRound() {
            damagableChecker.clear();
            booksAlwaysRandomlyEnchanted = false;
        }
    }

    public static class GeneralMappedMob {

        public final EntityLiving mob;
        public final MobRecipe recipe;
        public final ArrayList<MobDrop> drops;

        public GeneralMappedMob(EntityLiving mob, MobRecipe recipe, ArrayList<MobDrop> drops) {
            this.mob = mob;
            this.recipe = recipe;
            this.drops = drops;
        }
    }

    public static final HashMap<String, GeneralMappedMob> GeneralMobList = new HashMap<>();

    private static class MobRecipeLoaderCacheStructure {

        String version;
        Map<String, ArrayList<MobDrop>> moblist;
    }

    @SuppressWarnings({ "unchecked", "UnstableApiUsage" })
    public static void generateMobRecipeMap() {

        if (alreadyGenerated) return;
        alreadyGenerated = true;
        if (!Config.MobHandler.mobHandlerEnabled) return;

        World f = new GT_DummyWorld() {

            @Override
            public boolean blockExists(int p_72899_1_, int p_72899_2_, int p_72899_3_) {
                return false;
            }

            @SuppressWarnings("rawtypes")
            @Override
            public List getEntitiesWithinAABB(Class p_72872_1_, AxisAlignedBB p_72872_2_) {
                return new ArrayList();
            }

            @Override
            public Block getBlock(int aX, int aY, int aZ) {
                if (LoaderReference.TwilightForest && new Throwable().getStackTrace()[1].getClassName()
                    .equals("twilightforest.client.renderer.entity.RenderTFSnowQueenIceShield"))
                    return Blocks.packed_ice;
                return super.getBlock(aX, aY, aZ);
            }
        };
        f.isRemote = true; // quick hack to get around achievements

        fakeRand frand = new fakeRand();
        f.rand = frand;

        File cache = Config.getConfigFile("MobRecipeLoader.cache");
        Gson gson = GSONUtils.GSON_BUILDER.create();

        String modlistversion;
        if (Config.MobHandler.regenerationTrigger == Config.MobHandler._CacheRegenerationTrigger.ModAdditionRemoval)
            modlistversion = ModUtils.getModListVersionIgnoringModVersions();
        else modlistversion = ModUtils.getModListVersion();

        if (Config.MobHandler.regenerationTrigger != Config.MobHandler._CacheRegenerationTrigger.Always
            && cache.exists()) {
            LOG.info("Parsing Cached map");
            Reader reader = null;
            try {
                reader = Files.newReader(cache, StandardCharsets.UTF_8);
                MobRecipeLoaderCacheStructure s = gson.fromJson(reader, MobRecipeLoaderCacheStructure.class);
                if (Config.MobHandler.regenerationTrigger == Config.MobHandler._CacheRegenerationTrigger.Never
                    || s.version.equals(modlistversion)) {
                    ProgressBarWrapper bar = new ProgressBarWrapper("Parsing cached Mob Recipe Map", s.moblist.size());
                    for (Map.Entry<String, ArrayList<MobDrop>> entry : s.moblist.entrySet()) {
                        bar.step(entry.getKey());
                        try {
                            EntityLiving e;
                            String mobName = entry.getKey();
                            if (mobName.equals("witherSkeleton")
                                && !EntityList.stringToClassMapping.containsKey("witherSkeleton")) {
                                e = new EntitySkeleton(f);
                                ((EntitySkeleton) e).setSkeletonType(1);
                            } else e = (EntityLiving) ((Class<?>) EntityList.stringToClassMapping.get(mobName))
                                .getConstructor(new Class[] { World.class })
                                .newInstance(new Object[] { f });
                            ArrayList<MobDrop> drops = entry.getValue();
                            drops.forEach(MobDrop::reconstructStack);
                            GeneralMobList.put(
                                mobName,
                                new GeneralMappedMob(e, MobRecipe.generateMobRecipe(e, mobName, drops), drops));
                        } catch (Exception ignored) {}
                    }
                    bar.end();
                    LOG.info("Parsed cached map, skipping generation");
                    return;
                } else {
                    LOG.info("Cached map version mismatch, generating a new one");
                }
            } catch (Exception ignored) {
                LOG.warn("There was an exception while parsing cached map, generating a new one");
            } finally {
                if (reader != null) try {
                    reader.close();
                } catch (Exception ignored) {}
            }
        } else {
            LOG.info("Cached map doesn't exist or config option forced, generating a new one");
        }

        isInGenerationProcess = true;

        LOG.info("Generating Recipe Map for Mob Handler and EEC");

        long time = System.currentTimeMillis();

        Method setSlimeSize;
        Method dropFewItems;
        Method dropRareDrop;
        Method addRandomArmor;
        Method enchantEquipment;
        Field rand;

        try {
            setSlimeSize = EntitySlime.class.getDeclaredMethod(setSlimeSizeName, int.class);
            setSlimeSize.setAccessible(true);
            dropFewItems = EntityLivingBase.class.getDeclaredMethod(dropFewItemsName, boolean.class, int.class);
            dropFewItems.setAccessible(true);
            dropRareDrop = EntityLivingBase.class.getDeclaredMethod(dropRareDropName, int.class);
            dropRareDrop.setAccessible(true);
            addRandomArmor = EntityLiving.class.getDeclaredMethod(addRandomArmorName);
            addRandomArmor.setAccessible(true);
            enchantEquipment = EntityLiving.class.getDeclaredMethod(enchantEquipmentName);
            enchantEquipment.setAccessible(true);
            rand = Entity.class.getDeclaredField(randName);
            rand.setAccessible(true);
        } catch (Exception ex) {
            LOG.error("Failed to obtain methods");
            isInGenerationProcess = false;
            return;
        }

        dropCollector collector = new dropCollector();

        // Stupid MC code, I need to cast myself
        Map<String, Class<? extends Entity>> stringToClassMapping = (Map<String, Class<? extends Entity>>) EntityList.stringToClassMapping;
        boolean registeringWitherSkeleton = !stringToClassMapping.containsKey("witherSkeleton");
        if (registeringWitherSkeleton) stringToClassMapping.put("witherSkeleton", EntitySkeleton.class);
        ProgressBarWrapper bar = new ProgressBarWrapper("Generating Mob Recipe Map", stringToClassMapping.size());
        stringToClassMapping.forEach((k, v) -> {
            bar.step(k);
            if (v == null) return;

            if (Modifier.isAbstract(v.getModifiers())) {
                LOG.info("Entity " + k + " is abstract, skipping");
                return;
            }

            EntityLiving e;
            try {
                e = (EntityLiving) v.getConstructor(new Class[] { World.class })
                    .newInstance(new Object[] { f });
            } catch (ClassCastException ex) {
                // not a EntityLiving
                LOG.info("Entity " + k + " is not a LivingEntity, skipping");
                return;
            } catch (NoSuchMethodException ex) {
                // No constructor ?
                LOG.info("Entity " + k + " doesn't have constructor, skipping");
                return;
            } catch (NoClassDefFoundError ex) {
                // Its using classes from Client ? Then it's not important to include
                LOG.info("Entity " + k + " is using undefined classes, skipping");
                return;
            } catch (Throwable ex) {
                ex.printStackTrace();
                return;
            }

            if (registeringWitherSkeleton && e instanceof EntitySkeleton && k.equals("witherSkeleton"))
                ((EntitySkeleton) e).setSkeletonType(1);
            else if (StatCollector.translateToLocal("entity." + k + ".name")
                .equals("entity." + k + ".name")) {
                    LOG.info("Entity " + k + " does't have localized name, skipping");
                    return;
                }

            // POWERFULL GENERATION

            e.captureDrops = true;

            if (e instanceof EntitySlime) try {
                setSlimeSize.invoke(e, 1);
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }

            try {
                rand.set(e, frand);
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }

            droplist drops = new droplist();
            droplist raredrops = new droplist();
            droplist superraredrops = new droplist();
            droplist additionaldrops = new droplist();
            droplist dropslooting = new droplist();

            frand.newRound();
            collector.newRound();

            if (v.getName()
                .startsWith("com.emoniph.witchery")) {
                try {
                    dropFewItems.invoke(e, true, 0);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return;
                }
                frand.newRound();
                frand.exceptionOnEnchantTry = true;
                boolean enchantmentDetected = false;
                try {
                    dropFewItems.invoke(e, true, 0);
                } catch (Exception ex) {
                    enchantmentDetected = true;
                }
                int w = frand.walkCounter;
                frand.newRound();
                if (enchantmentDetected) {
                    frand.maxWalkCount = w;
                    collector.booksAlwaysRandomlyEnchanted = true;
                }
                e.capturedDrops.clear();
            }

            boolean second = false;
            do {
                try {
                    dropFewItems.invoke(e, true, 0);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return;
                }
                collector.addDrop(drops, e.capturedDrops, frand.chance);

                if (second && frand.chance < 0.0000001d) {
                    LOG.warn("Skipping " + k + " normal dropmap because it's too randomized");
                    break;
                }
                second = true;

            } while (frand.nextRound());

            frand.newRound();
            collector.newRound();

            if (v.getName()
                .startsWith("com.emoniph.witchery")) {
                try {
                    dropFewItems.invoke(e, true, 0);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return;
                }
                frand.newRound();
                frand.exceptionOnEnchantTry = true;
                boolean enchantmentDetected = false;
                try {
                    dropFewItems.invoke(e, true, 0);
                } catch (Exception ex) {
                    enchantmentDetected = true;
                }
                int w = frand.walkCounter;
                frand.newRound();
                if (enchantmentDetected) {
                    frand.maxWalkCount = w;
                    collector.booksAlwaysRandomlyEnchanted = true;
                }
                e.capturedDrops.clear();
            }

            second = false;
            do {
                try {
                    dropFewItems.invoke(e, true, 1);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return;
                }
                collector.addDrop(dropslooting, e.capturedDrops, frand.chance);

                if (second && frand.chance < 0.0000001d) {
                    LOG.warn("Skipping " + k + " normal dropmap because it's too randomized");
                    break;
                }
                second = true;

            } while (frand.nextRound());

            frand.newRound();
            collector.newRound();

            second = false;
            do {
                try {
                    dropRareDrop.invoke(e, 0);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return;
                }
                collector.addDrop(raredrops, e.capturedDrops, frand.chance);

                if (second && frand.chance < 0.0000001d) {
                    LOG.warn("Skipping " + k + " rare dropmap because it's too randomized");
                    break;
                }
                second = true;

            } while (frand.nextRound());

            frand.newRound();
            collector.newRound();

            second = false;
            do {
                try {
                    dropRareDrop.invoke(e, 1);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return;
                }
                collector.addDrop(superraredrops, e.capturedDrops, frand.chance);

                if (second && frand.chance < 0.0000001d) {
                    LOG.warn("Skipping " + k + " rare dropmap because it's too randomized");
                    break;
                }
                second = true;

            } while (frand.nextRound());

            frand.newRound();
            collector.newRound();

            if (registeringWitherSkeleton && e instanceof EntitySkeleton && k.equals("witherSkeleton")) {
                dropinstance i = new dropinstance(new ItemStack(Items.stone_sword), additionaldrops);
                i.isDamageRandomized = true;
                int maxdamage = i.stack.getMaxDamage();
                int max = Math.max(maxdamage - 25, 1);
                for (int d = Math.min(max, 25); d <= max; d++) i.damagesPossible.put(d, 1);
                additionaldrops.add(i, 1d);
            } else try {
                Class<?> cl = e.getClass();
                boolean detectedException;
                do {
                    detectedException = false;
                    try {
                        cl.getDeclaredMethod(addRandomArmorName);
                    } catch (Exception ex) {
                        detectedException = true;
                        cl = cl.getSuperclass();
                    }
                } while (detectedException && !cl.equals(Entity.class));
                if (cl.equals(EntityLiving.class) || cl.equals(Entity.class)) throw new Exception();
                cl = e.getClass();
                do {
                    detectedException = false;
                    try {
                        cl.getDeclaredMethod(enchantEquipmentName);
                    } catch (Exception ex) {
                        detectedException = true;
                        cl = cl.getSuperclass();
                    }
                } while (detectedException && !cl.equals(EntityLiving.class));
                boolean usingVanillaEnchantingMethod = cl.equals(EntityLiving.class);
                double chanceModifierLocal = 1f;
                if (v.getName()
                    .startsWith("twilightforest.entity")) {
                    frand.forceFloatValue = 0f;
                    chanceModifierLocal = 0.25f;
                }
                second = false;
                do {
                    addRandomArmor.invoke(e);
                    if (!usingVanillaEnchantingMethod) enchantEquipment.invoke(e);
                    ItemStack[] lastActiveItems = e.getLastActiveItems();
                    for (int j = 0, lastActiveItemsLength = lastActiveItems.length; j < lastActiveItemsLength; j++) {
                        ItemStack stack = lastActiveItems[j];
                        if (stack != null) {
                            if (LoaderReference.Thaumcraft) if (stack.getItem() instanceof ItemWandCasting) continue; // crashes
                                                                                                                      // the
                                                                                                                      // game
                                                                                                                      // when
                                                                                                                      // rendering
                                                                                                                      // in
                                                                                                                      // GUI

                            int randomenchant = -1;
                            if (stack.hasTagCompound()
                                && stack.stackTagCompound.hasKey(randomEnchantmentDetectedString)) {
                                randomenchant = stack.stackTagCompound.getInteger(randomEnchantmentDetectedString);
                                stack.stackTagCompound.removeTag("ench");
                            }
                            dropinstance i = additionaldrops.add(
                                new dropinstance(stack.copy(), additionaldrops),
                                frand.chance * chanceModifierLocal
                                    * (usingVanillaEnchantingMethod ? (j == 0 ? 0.75d : 0.5d) : 1d));
                            if (!i.isDamageRandomized && i.stack.isItemStackDamageable()) {
                                i.isDamageRandomized = true;
                                int maxdamage = i.stack.getMaxDamage();
                                int max = Math.max(maxdamage - 25, 1);
                                for (int d = Math.min(max, 25); d <= max; d++) i.damagesPossible.put(d, 1);
                            }
                            if (!i.isEnchatmentRandomized && randomenchant != -1) {
                                i.isEnchatmentRandomized = true;
                                i.enchantmentLevel = randomenchant;
                            }
                            if (usingVanillaEnchantingMethod) {
                                if (!stack.hasTagCompound()) stack.stackTagCompound = new NBTTagCompound();
                                stack.stackTagCompound.setInteger(randomEnchantmentDetectedString, 14);
                                dropinstance newdrop = additionaldrops.add(
                                    new dropinstance(stack.copy(), additionaldrops),
                                    frand.chance * chanceModifierLocal * (j == 0 ? 0.25d : 0.5d));
                                newdrop.isEnchatmentRandomized = true;
                                newdrop.enchantmentLevel = 14;
                                newdrop.isDamageRandomized = i.isDamageRandomized;
                                newdrop.damagesPossible = (HashMap<Integer, Integer>) i.damagesPossible.clone();
                            }
                        }
                    }
                    Arrays.fill(e.getLastActiveItems(), null);

                    if (second && frand.chance < 0.0000001d) {
                        LOG.warn("Skipping " + k + " additional dropmap because it's too randomized");
                        break;
                    }
                    second = true;

                } while (frand.nextRound());
            } catch (Exception ignored) {}

            frand.newRound();
            collector.newRound();

            if (drops.isEmpty() && raredrops.isEmpty() && additionaldrops.isEmpty()) {
                ArrayList<MobDrop> arr = new ArrayList<>();
                GeneralMobList.put(k, new GeneralMappedMob(e, MobRecipe.generateMobRecipe(e, k, arr), arr));
                LOG.info("Mapped " + k);
                return;
            }

            ArrayList<MobDrop> moboutputs = new ArrayList<>(drops.size() + raredrops.size() + additionaldrops.size());

            for (dropinstance drop : drops.drops) {
                ItemStack stack = drop.stack;
                if (stack.hasTagCompound()) stack.stackTagCompound.removeTag(randomEnchantmentDetectedString);
                int chance = drop.getchance(10000);
                if (chance > 10000) {
                    int div = (int) Math.ceil(chance / 10000d);
                    stack.stackSize *= div;
                    chance /= div;
                }
                if (chance == 0) {
                    LOG.warn("Detected 0% loot, setting to 0.01%");
                    chance = 1;
                }
                dropinstance dlooting = dropslooting.get(drop);
                moboutputs.add(
                    new MobDrop(
                        stack,
                        MobDrop.DropType.Normal,
                        chance,
                        drop.isEnchatmentRandomized ? drop.enchantmentLevel : null,
                        drop.isDamageRandomized ? drop.damagesPossible : null,
                        dlooting != null && dlooting.dropcount > drop.dropcount,
                        false));
            }
            for (dropinstance drop : raredrops.drops) {
                ItemStack stack = drop.stack;
                if (stack.hasTagCompound()) stack.stackTagCompound.removeTag(randomEnchantmentDetectedString);
                int chance = drop.getchance(250);
                if (chance > 10000) {
                    int div = (int) Math.ceil(chance / 10000d);
                    stack.stackSize *= div;
                    chance /= div;
                }
                if (chance == 0) {
                    LOG.warn("Detected 0% loot, setting to 0.01%");
                    chance = 1;
                }
                moboutputs.add(
                    new MobDrop(
                        stack,
                        MobDrop.DropType.Rare,
                        chance,
                        drop.isEnchatmentRandomized ? drop.enchantmentLevel : null,
                        drop.isDamageRandomized ? drop.damagesPossible : null,
                        false,
                        false));
            }
            for (dropinstance drop : superraredrops.drops) {
                if (raredrops.contains(drop)) continue;
                ItemStack stack = drop.stack;
                if (stack.hasTagCompound()) stack.stackTagCompound.removeTag(randomEnchantmentDetectedString);
                int chance = drop.getchance(50);
                if (chance > 10000) {
                    int div = (int) Math.ceil(chance / 10000d);
                    stack.stackSize *= div;
                    chance /= div;
                }
                if (chance == 0) {
                    LOG.warn("Detected 0% loot, setting to 0.01%");
                    chance = 1;
                }
                moboutputs.add(
                    new MobDrop(
                        stack,
                        MobDrop.DropType.Rare,
                        chance,
                        drop.isEnchatmentRandomized ? drop.enchantmentLevel : null,
                        drop.isDamageRandomized ? drop.damagesPossible : null,
                        false,
                        false));
            }
            for (dropinstance drop : additionaldrops.drops) {
                ItemStack stack = drop.stack;
                if (stack.hasTagCompound()) stack.stackTagCompound.removeTag(randomEnchantmentDetectedString);
                int chance = drop.getchance(850);
                if (chance > 10000) {
                    int div = (int) Math.ceil(chance / 10000d);
                    stack.stackSize *= div;
                    chance /= div;
                }
                if (chance == 0) {
                    LOG.warn("Detected 0% loot, setting to 0.01%");
                    chance = 1;
                }
                moboutputs.add(
                    new MobDrop(
                        stack,
                        MobDrop.DropType.Additional,
                        chance,
                        drop.isEnchatmentRandomized ? drop.enchantmentLevel : null,
                        drop.isDamageRandomized ? drop.damagesPossible : null,
                        false,
                        false));
            }

            GeneralMobList.put(k, new GeneralMappedMob(e, MobRecipe.generateMobRecipe(e, k, moboutputs), moboutputs));

            LOG.info("Mapped " + k);
        });

        if (registeringWitherSkeleton) stringToClassMapping.remove("witherSkeleton");

        time -= System.currentTimeMillis();
        time = -time;

        LOG.info("Recipe map generated ! It took " + time + "ms");

        bar.end();

        isInGenerationProcess = false;

        LOG.info("Saving generated map to file");
        MobRecipeLoaderCacheStructure s = new MobRecipeLoaderCacheStructure();
        s.version = modlistversion;
        s.moblist = new HashMap<>();
        GeneralMobList.forEach((k, v) -> s.moblist.put(k, v.drops));
        Writer writer = null;
        try {
            writer = Files.newWriter(cache, StandardCharsets.UTF_8);
            gson.toJson(s, writer);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) try {
                writer.close();
            } catch (Exception ignored) {}
        }
    }

    public static void processMobRecipeMap() {
        LOG.info("Loading config");

        OverridesConfig.LoadConfig();

        if (isClientSided) Mob_Handler.clearRecipes();
        MobNameToRecipeMap.clear();
        LoadConfigPacket.instance.mobsToLoad.clear();
        LoadConfigPacket.instance.mobsOverrides.clear();
        for (Map.Entry<String, GeneralMappedMob> entry : GeneralMobList.entrySet()) {
            String k = entry.getKey();
            GeneralMappedMob v = entry.getValue();
            if (Arrays.asList(Config.MobHandler.mobBlacklist)
                .contains(k)) {
                LOG.info("Entity " + k + " is blacklisted, skipping");
                continue;
            }

            MobRecipe recipe = v.recipe;
            recipe = recipe.copy();
            @SuppressWarnings("unchecked")
            ArrayList<MobDrop> drops = (ArrayList<MobDrop>) v.drops.clone();

            // MT Scripts should already be loaded here
            if (LoaderReference.MineTweaker) {
                Optionals.parseMTAdditions(k, drops, recipe);
            }

            OverridesConfig.MobOverride override;
            if ((override = OverridesConfig.overrides.get(k)) != null) {
                if (override.removeAll) {
                    drops.clear();
                    recipe.mOutputs.clear();
                } else for (OverridesConfig.MobDropSimplified removal : override.removals) {
                    drops.removeIf(removal::isMatching);
                    recipe.mOutputs.removeIf(removal::isMatching);
                }
                drops.addAll(override.additions);
                recipe.mOutputs.addAll(override.additions);
                LoadConfigPacket.instance.mobsOverrides.put(k, override);
            }
            recipe.refresh();

            if (drops.isEmpty()) {
                LOG.info("Entity " + k + " doesn't drop any items, skipping EEC map");
                if (!Config.MobHandler.includeEmptyMobs) continue;
                LoadConfigPacket.instance.mobsToLoad.add(k);
                LOG.info("Registered " + k);
                continue;
            }
            if (v.recipe.mOutputs.size() > 0) MobNameToRecipeMap.put(k, recipe);
            LoadConfigPacket.instance.mobsToLoad.add(k);
            LOG.info("Registered " + k);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void processMobRecipeMap(HashSet<String> mobs,
        HashMap<String, OverridesConfig.MobOverride> overrides) {
        if (isClientSided) Mob_Handler.clearRecipes();
        MobNameToRecipeMap.clear();
        mobs.forEach(k -> {
            GeneralMappedMob v = GeneralMobList.get(k);
            MobRecipe recipe = v.recipe;
            recipe = recipe.copy();
            @SuppressWarnings("unchecked")
            ArrayList<MobDrop> drops = (ArrayList<MobDrop>) v.drops.clone();

            // MT Scripts should already be loaded here
            if (LoaderReference.MineTweaker) {
                Optionals.parseMTAdditions(k, drops, recipe);
            }

            OverridesConfig.MobOverride override;
            if ((override = overrides.get(k)) != null) {
                if (override.removeAll) {
                    drops.clear();
                    recipe.mOutputs.clear();
                } else for (OverridesConfig.MobDropSimplified removal : override.removals) {
                    drops.removeIf(removal::isMatching);
                    recipe.mOutputs.removeIf(removal::isMatching);
                }
                drops.addAll(override.additions);
                recipe.mOutputs.addAll(override.additions);
                drops.sort(Comparator.comparing(d -> d.type)); // Fix GUI
            }
            recipe.refresh();

            Mob_Handler.addRecipe(v.mob, drops);
            if (recipe.mOutputs.size() > 0) MobNameToRecipeMap.put(k, recipe);
            LOG.info("Registered " + k);
        });
        LOG.info("Sorting NEI map");
        Mob_Handler.sortCachedRecipes();
    }

    private static class Optionals {

        private static void parseMTAdditions(String k, ArrayList<MobDrop> drops, MobRecipe recipe) {
            IEntityDefinition ie = MineTweakerAPI.game.getEntity(k);
            if (ie != null) {
                for (Map.Entry<IItemStack, IntRange> entry : ie.getDropsToAdd()
                    .entrySet()) {
                    IntRange r = entry.getValue();
                    // Get average chance
                    double chance;
                    if (r.getFrom() == 0 && r.getTo() == 0) chance = 1d;
                    else {
                        double a = r.getFrom();
                        double b = r.getTo();
                        chance = ((b * b) + b - (a * a) + a) / (2 * (b - a + 1));
                    }
                    ItemStack stack = ((ItemStack) entry.getKey()
                        .getInternal()).copy();
                    MobDrop drop = new MobDrop(
                        stack,
                        MobDrop.DropType.Normal,
                        (int) (chance * 10000),
                        null,
                        null,
                        false,
                        false);
                    drops.add(drop);
                    recipe.mOutputs.add(drop);
                }
                for (Map.Entry<IItemStack, IntRange> entry : ie.getDropsToAddPlayerOnly()
                    .entrySet()) {
                    IntRange r = entry.getValue();
                    // Get average chance
                    double chance;
                    if (r.getFrom() == 0 && r.getTo() == 0) chance = 1d;
                    else {
                        double a = r.getFrom();
                        double b = r.getTo();
                        chance = ((b * b) + b - (a * a) + a) / (2 * (b - a + 1));
                    }
                    ItemStack stack = ((ItemStack) entry.getKey()
                        .getInternal()).copy();
                    MobDrop drop = new MobDrop(
                        stack,
                        MobDrop.DropType.Normal,
                        (int) (chance * 10000),
                        null,
                        null,
                        false,
                        true);
                    drops.add(drop);
                }
                for (IItemStack istack : ie.getDropsToRemove()) {
                    List<MobDrop> toRemove = drops.stream()
                        .filter(d -> istack.matches(new MCItemStack(d.stack)))
                        .collect(Collectors.toList());
                    drops.removeAll(toRemove);
                    recipe.mOutputs.removeAll(toRemove);
                }
            }
        }
    }
}
