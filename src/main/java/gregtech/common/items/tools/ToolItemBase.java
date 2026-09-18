package gregtech.common.items.tools;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static net.minecraft.util.StatCollector.canTranslate;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.oredict.OreDictionary;

import com.gtnewhorizon.gtnhlib.item.ItemStackNBT;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTLoggers;
import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enchants.EnchantmentRadioactivity;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TCAspects.TC_AspectStack;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.interfaces.IDamagableItem;
import gregtech.api.interfaces.IGTTool;
import gregtech.api.interfaces.IToolStats;
import gregtech.api.items.GTGenericItem;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.objects.GTHashSet;
import gregtech.api.objects.GTItemStack;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * The shared half of a standalone, single-purpose tool item.
 * <p/>
 * Unlike {@link MetaGeneratedTool}, where the metadata is the tool type and the material lives in NBT, these items'
 * metadata <em>is</em> the crafting material's {@link Materials#mMetaItemSubID}. That makes every material a distinct
 * item+meta pair, which is what NEI keys its recipe lookup on, so a Steel Wrench and a Diamond Wrench are finally
 * different things as far as the recipe view is concerned.
 * <p/>
 * Everything here is true of any such tool: how a material is declared, where durability and mode live, and how the
 * stack mines, fights, wears out and describes itself. A subclass adds what makes its tool that tool -- the cross-mod
 * interfaces it honours, what right-clicking a block does, and the lines that say so in the tooltip. Tool types move
 * across from {@link gregtech.common.items.MetaGeneratedTool01} one at a time; see {@link GTToolItems}.
 */
public abstract class ToolItemBase extends GTGenericItem implements IGTTool, IDamagableItem {

    /** Where the accumulated durability damage is kept, counted in whole durability points. */
    protected static final String DAMAGE_KEY = "GT.ToolDamage";
    /** Where the selected tool mode is kept. */
    protected static final String MODE_KEY = "GT.ToolMode";

    protected final IToolStats toolStats;
    private final String nameKey;
    private final String englishNameFormat;
    private final ToolDictNames[] oreDictNames;
    /** The metadata values this tool has actually been registered for, in registration order. */
    private final IntArrayList validMetas = new IntArrayList();

    /**
     * @param unlocalizedName   appended to {@code gt.}; becomes both the registry name and the localization key root.
     * @param toolStats         the generic stats for this tool, reused verbatim from the old tool registry.
     * @param englishNameFormat the default display name, where {@code %material} is replaced by the material name.
     * @param englishTooltip    the default tooltip, or an empty string for none.
     * @param toolList          the {@link GregTechAPI} list machines check to recognise this tool, or null if the tool
     *                          is not one machines respond to.
     * @param oreDictNames      the ore dictionary names every material of this tool is registered under. A few tools
     *                          answer to more than one -- a knife is both a blade and a knife.
     */
    protected ToolItemBase(String unlocalizedName, IToolStats toolStats, String englishNameFormat,
        String englishTooltip, GTHashSet toolList, ToolDictNames... oreDictNames) {
        super(unlocalizedName, null, englishTooltip);
        this.toolStats = toolStats;
        this.englishNameFormat = englishNameFormat;
        this.oreDictNames = oreDictNames;
        this.nameKey = getUnlocalizedName() + ".name";
        // The name and the tooltip live in the asset lang file, where translators can reach them; the English text
        // passed in is only what the game falls back to when a key is missing, so nothing is written into
        // GregTech.lang here.
        setHasSubtypes(true);
        setMaxDamage(0);
        setMaxStackSize(1);
        // One wildcard entry is enough: GTUtility.isStackInList falls back to a wildcard lookup.
        if (toolList != null) toolList.add(new GTItemStack(this, 1, OreDictionary.WILDCARD_VALUE));
        GregTechAPI.sToolList.add(new GTItemStack(this, 1, OreDictionary.WILDCARD_VALUE));
    }

    /* ---------- MATERIAL REGISTRATION ---------- */

    /**
     * Declares that this tool can be made from the given material, which registers the resulting stack with the ore
     * dictionary, the Thaumcraft aspect list and the IC2 toolbox, and makes it show up in the creative tab.
     *
     * @param aspects Thaumcraft aspects for the resulting stack, or none.
     * @return the registered stack, or null if the material has no metadata slot (see {@link #getMaterialMeta}).
     */
    public ItemStack registerMaterial(Materials material, TC_AspectStack... aspects) {
        return registerMaterial(material, -1, aspects);
    }

    /**
     * As {@link #registerMaterial(Materials, TC_AspectStack...)}, but for a material that needs a metadata outside the
     * ordinary sub id band -- see {@link ToolMaterialIndex}.
     */
    public ItemStack registerMaterial(Materials material, int meta, TC_AspectStack... aspects) {
        meta = ToolMaterialIndex.assign(material, meta);
        if (meta < 0) {
            // No metadata slot means no tool for this material, which would quietly drop a recipe -- say so.
            GTLoggers.GT_FML_LOGGER.warn(
                "No metadata slot for material {}, so {} gets no recipe for it",
                material == null ? "null" : material.mName,
                getUnlocalizedName());
            return null;
        }
        // Registering a material twice would duplicate its ore dictionary and aspect entries.
        if (validMetas.contains(meta)) return new ItemStack(this, 1, meta);
        validMetas.add(meta);
        ItemStack stack = new ItemStack(this, 1, meta);
        // Deferred, because materials are declared from inside the ore dictionary handler: see
        // GTToolItems.registerOreDictEntry.
        for (ToolDictNames oreDictName : oreDictNames) {
            GTToolItems.registerOreDictEntry(() -> GTOreDictUnificator.registerOre(oreDictName, stack));
        }
        if (GregTechAPI.sThaumcraftCompat != null && aspects.length > 0) {
            List<TC_AspectStack> aspectList = new ArrayList<>();
            for (TC_AspectStack aspect : aspects) aspect.addToAspectList(aspectList);
            GregTechAPI.sThaumcraftCompat.registerThaumcraftAspectsToItem(stack, aspectList, false);
        }
        GTModHandler.registerBoxableItemToToolBox(stack);
        return stack;
    }

    /**
     * @return the metadata this material's tool uses, or -1 if the material has no metadata slot at all. The mapping
     *         is shared by every standalone tool item, so one material is the same metadata on all of them.
     */
    public static int getMaterialMeta(Materials material) {
        return ToolMaterialIndex.getMeta(material);
    }

    /**
     * @return a freshly crafted tool of this material, or null if the material has no metadata slot.
     */
    public ItemStack getToolWithMaterial(Materials material) {
        int meta = getMaterialMeta(material);
        if (meta < 0) return null;
        ItemStack stack = new ItemStack(this, 1, meta);
        isItemStackUsable(stack);
        return stack;
    }

    /**
     * A stack of whichever material was registered first, for UI lists that mean "any tool of this kind" -- the tree
     * farm's list of tools it accepts, for instance. The metadata-based tools could show a material-less stack for
     * this; a standalone tool cannot, because a metadata with no material behind it has no stats and renders as
     * nothing at all.
     *
     * @return the stack, or null if no material has been registered for this tool.
     */
    public ItemStack getDisplayStack() {
        if (validMetas.isEmpty()) return null;
        return new ItemStack(this, 1, validMetas.getInt(0));
    }

    /* ---------- IGTTool ---------- */

    /**
     * @return the generic stats shared by every tool of this kind, independent of any particular stack.
     */
    public IToolStats getToolStats() {
        return toolStats;
    }

    @Override
    public IToolStats getToolStats(ItemStack stack) {
        return getToolMaterial(stack) == Materials._NULL ? null : toolStats;
    }

    @Override
    public Materials getToolMaterial(ItemStack stack) {
        if (stack == null) return Materials._NULL;
        return ToolMaterialIndex.getMaterial(stack.getItemDamage());
    }

    /**
     * The handle is not stored anywhere: every recipe that ever built one of these tools used the head material's
     * {@link Materials#mHandleMaterial}, so it follows from the metadata like everything else.
     */
    @Override
    public Materials getToolHandleMaterial(ItemStack stack) {
        Materials material = getToolMaterial(stack);
        return material == Materials._NULL ? Materials._NULL : material.mHandleMaterial;
    }

    @Override
    public long getStoredDamage(ItemStack stack) {
        return ItemStackNBT.getLong(stack, DAMAGE_KEY);
    }

    @Override
    public long getMaxStoredDamage(ItemStack stack) {
        Materials material = getToolMaterial(stack);
        if (material == Materials._NULL) return 0;
        // The material's durability times the tool type's multiplier, counted in whole points: one action, one point,
        // so this number is also how many times the tool can be used.
        return (long) (material.mDurability * toolStats.getMaxDurabilityMultiplier());
    }

    /**
     * Spends one action's worth of this tool and reports whether it could be paid for. Every action costs the same --
     * breaking a block, hitting something, being consumed by a recipe, or whatever the tool type does on a click --
     * which is one durability point, or, for a tool that runs on energy instead, {@link #getEnergyCostPerUse()}.
     */
    @Override
    public boolean spendOneUse(ItemStack stack) {
        final long energy = getEnergyCostPerUse();
        return doDamage(stack, energy > 0 ? energy : 1);
    }

    /**
     * @return what one action costs a tool that stores energy rather than durability, in EU, or 0 for a tool that
     *         wears out. Electric tools set this per type, since a drill's block and a file's recipe were never worth
     *         the same.
     */
    public long getEnergyCostPerUse() {
        return 0;
    }

    @Override
    public boolean doDamage(ItemStack stack, long amount) {
        if (stack == null || stack.stackSize <= 0) return false;
        long maxDamage = getMaxStoredDamage(stack);
        if (maxDamage <= 0) return false;
        long newDamage = getStoredDamage(stack) + amount;
        ItemStackNBT.setLong(stack, DAMAGE_KEY, newDamage);
        if (newDamage >= maxDamage) breakTool(stack);
        return true;
    }

    protected void breakTool(ItemStack stack) {
        if (GTUtility.setStack(stack, toolStats.getBrokenItem(stack)) == null) {
            if (MetaGeneratedTool.playSound) GTUtility.doSoundAtClient(toolStats.getBreakingSound(), 1, 1.0F);
            if (stack.stackSize > 0) stack.stackSize--;
        }
    }

    @Override
    public long getStoredCharge(ItemStack stack) {
        return 0;
    }

    @Override
    public long getMaxStoredCharge(ItemStack stack) {
        return 0;
    }

    @Override
    public byte getMode(ItemStack stack) {
        if (stack == null) return 0;
        return (byte) ItemStackNBT.getInteger(stack, MODE_KEY);
    }

    @Override
    public boolean setMode(ItemStack stack, byte mode) {
        if (stack == null) return false;
        ItemStackNBT.setInteger(stack, MODE_KEY, mode);
        return true;
    }

    @Override
    public byte getMaxMode(ItemStack stack) {
        return toolStats.getMaxMode();
    }

    /**
     * One call, one action: the vanilla damage figure is ignored, since every action costs the same now.
     */
    @Override
    public boolean doDamageToItem(ItemStack stack, int vanillaDamage) {
        return spendOneUse(stack);
    }

    /* ---------- MINING ---------- */

    @Override
    public float getDigSpeed(ItemStack stack, Block block, int metaData) {
        Materials material = getToolMaterial(stack);
        if (material == Materials._NULL) return 0.0F;
        if (Math.max(0, getHarvestLevel(stack, "")) < block.getHarvestLevel(metaData)) return 0.0F;
        return toolStats.isMinableBlock(block, metaData)
            ? Math.max(Float.MIN_NORMAL, toolStats.getSpeedMultiplier() * material.mToolSpeed)
            : 0.0F;
    }

    @Override
    public boolean canHarvestBlock(Block block, ItemStack stack) {
        return getDigSpeed(stack, block, 0) > 0.0F;
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass) {
        Materials material = getToolMaterial(stack);
        if (material == Materials._NULL) return -1;
        return toolStats.getBaseQuality() + material.mToolQuality;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z,
        EntityLivingBase player) {
        if (getToolMaterial(stack) == Materials._NULL) return false;
        GTUtility.doSoundAtClient(toolStats.getMiningSound(), 1, 1.0F);
        // A block is a block: no scaling by hardness any more.
        spendOneUse(stack);
        return getDigSpeed(stack, block, world.getBlockMetadata(x, y, z)) > 0.0F;
    }

    @Override
    public float onBlockBreakSpeedEvent(float defaultSpeed, ItemStack stack, EntityPlayer player, Block block, int x,
        int y, int z, int metaData, PlayerEvent.BreakSpeed event) {
        return toolStats.getMiningSpeed(block, metaData, defaultSpeed, player, player.worldObj, x, y, z);
    }

    @Override
    public void onHarvestBlockEvent(ArrayList<ItemStack> drops, ItemStack stack, EntityPlayer player, Block block,
        int x, int y, int z, int metaData, int fortune, boolean silkTouch, BlockEvent.HarvestDropsEvent event) {
        if (getToolMaterial(stack) == Materials._NULL || getDigSpeed(stack, block, metaData) <= 0.0F) return;
        // One point for the conversion, however many drops it turned over.
        if (toolStats.convertBlockDrops(drops, stack, player, block, x, y, z, metaData, fortune, silkTouch, event) > 0)
            spendOneUse(stack);
    }

    @Override
    public float getBlockStrength(ItemStack stack, Block block, EntityPlayer player, World world, int x, int y, int z,
        float defaultBlockStrength) {
        if (player == null) return defaultBlockStrength;
        return toolStats.getBlockStrength(stack, block, player, world, x, y, z, defaultBlockStrength);
    }

    /* ---------- USE ---------- */

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        if (getToolMaterial(stack) == Materials._NULL) return true;
        GTUtility.doSoundAtClient(toolStats.getEntityHitSound(), 1, 1.0F);
        if (super.onLeftClickEntity(stack, player, entity)) return true;
        if (entity.canAttackWithItem() && !entity.hitByEntity(player)) {
            float magicDamage = toolStats.getMagicDamageAgainstEntity(
                entity instanceof EntityLivingBase
                    ? EnchantmentHelper.getEnchantmentModifierLiving(player, (EntityLivingBase) entity)
                    : 0.0F,
                entity,
                stack,
                player);
            float damage = toolStats.getNormalDamageAgainstEntity(
                (float) player.getEntityAttribute(SharedMonsterAttributes.attackDamage)
                    .getAttributeValue() + getToolCombatDamage(stack),
                entity,
                stack,
                player);
            if (damage + magicDamage > 0.0F) {
                boolean criticalHit = player.fallDistance > 0.0F && !player.onGround
                    && !player.isOnLadder()
                    && !player.isInWater()
                    && !player.isPotionActive(Potion.blindness)
                    && player.ridingEntity == null
                    && entity instanceof EntityLivingBase;
                if (criticalHit && damage > 0.0F) damage *= 1.5F;
                damage += magicDamage;
                if (entity.attackEntityFrom(toolStats.getDamageSource(player, entity), damage)) {
                    if (entity instanceof EntityLivingBase)
                        entity.setFire(EnchantmentHelper.getFireAspectModifier(player) * 4);
                    int knockback = (player.isSprinting() ? 1 : 0) + (entity instanceof EntityLivingBase
                        ? EnchantmentHelper.getKnockbackModifier(player, (EntityLivingBase) entity)
                        : 0);
                    if (knockback > 0) {
                        entity.addVelocity(
                            -MathHelper.sin(player.rotationYaw * (float) Math.PI / 180.0F) * knockback * 0.5F,
                            0.1D,
                            MathHelper.cos(player.rotationYaw * (float) Math.PI / 180.0F) * knockback * 0.5F);
                        player.motionX *= 0.6D;
                        player.motionZ *= 0.6D;
                        player.setSprinting(false);
                    }
                    if (criticalHit) player.onCriticalHit(entity);
                    if (magicDamage > 0.0F) player.onEnchantmentCritical(entity);
                    if (damage >= 18.0F) player.triggerAchievement(AchievementList.overkill);
                    player.setLastAttacker(entity);
                    if (entity instanceof EntityLivingBase)
                        EnchantmentHelper.func_151384_a((EntityLivingBase) entity, player);
                    EnchantmentHelper.func_151385_b(player, entity);
                    if (entity instanceof EntityLivingBase)
                        player.addStat(StatList.damageDealtStat, Math.round(damage * 10.0F));
                    entity.hurtResistantTime = Math
                        .max(1, toolStats.getHurtResistanceTime(entity.hurtResistantTime, entity));
                    player.addExhaustion(0.3F);
                    spendOneUse(stack);
                }
            }
        }
        if (stack.stackSize <= 0) GTUtility.destroyCurrentItem(player);
        return true;
    }

    public float getToolCombatDamage(ItemStack stack) {
        Materials material = getToolMaterial(stack);
        if (material == Materials._NULL) return 0;
        return toolStats.getBaseDamage() + material.mToolQuality;
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        if (getToolMaterial(stack) == Materials._NULL) return null;
        ItemStack result = GTUtility.copyAmount(1, stack);
        spendOneUse(result);
        return result != null && result.stackSize > 0 ? result : null;
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return getToolMaterial(stack) != Materials._NULL;
    }

    @Override
    public boolean doesContainerItemLeaveCraftingGrid(ItemStack stack) {
        return false;
    }

    @Override
    public void onCreated(ItemStack stack, World world, EntityPlayer player) {
        if (player != null && getToolMaterial(stack) != Materials._NULL) toolStats.onToolCrafted(stack, player);
        isItemStackUsable(stack);
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 1;
    }

    @Override
    public boolean isFull3D() {
        return true;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    /* ---------- DISPLAY ---------- */

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String format = canTranslate(nameKey) ? translateToLocal(nameKey) : englishNameFormat;
        Materials material = getToolMaterial(stack);
        String name = material == Materials._NULL ? format.replace("%material", "")
            .trim() : material.getLocalizedNameForItem(format);
        String mode = getToolModeName(stack);
        return mode == null ? name : name + " (" + mode + ")";
    }

    public String getToolModeName(ItemStack stack) {
        String toolName = toolStats.getToolTypeName();
        if (toolName == null) return null;
        String key = "gt." + toolName + ".mode." + getMode(stack);
        return canTranslate(key) ? translateToLocal(key) : null;
    }

    @Override
    protected void addAdditionalToolTips(List<String> list, ItemStack stack, EntityPlayer player) {
        Materials material = getToolMaterial(stack);
        if (material == Materials._NULL) return;
        long maxDamage = getMaxStoredDamage(stack);
        if (maxDamage > 0) {
            list.add(
                EnumChatFormatting.WHITE
                    + translateToLocalFormatted(
                        "gt.item.desc.durability",
                        EnumChatFormatting.GREEN + formatNumber(maxDamage - getStoredDamage(stack)) + " ",
                        " " + formatNumber(maxDamage))
                    + EnumChatFormatting.GRAY);
        }
        list.add(
            EnumChatFormatting.WHITE
                + translateToLocalFormatted(
                    "gt.item.desc.level",
                    material.getLocalizedName() + EnumChatFormatting.YELLOW,
                    formatNumber(getHarvestLevel(stack, "")))
                + EnumChatFormatting.GRAY);
        list.add(
            EnumChatFormatting.WHITE
                + translateToLocalFormatted(
                    "gt.item.desc.damage",
                    EnumChatFormatting.BLUE + formatNumber(getToolCombatDamage(stack)))
                + EnumChatFormatting.GRAY);
        list.add(
            EnumChatFormatting.WHITE
                + translateToLocalFormatted(
                    "gt.item.desc.mine_speed",
                    EnumChatFormatting.GOLD + formatNumber(
                        Math.max(Float.MIN_NORMAL, toolStats.getSpeedMultiplier() * material.mToolSpeed)))
                + EnumChatFormatting.GRAY);
        addBehaviourToolTips(list, stack);
        if (getMaxMode(stack) > 1) {
            list.add(
                EnumChatFormatting.DARK_GRAY + translateToLocalFormatted(
                    "gt.behaviour.switch_mode.tooltip",
                    GameSettings.getKeyDisplayString(GTMod.proxy.TOOL_MODE_SWITCH_KEYBIND.getKeyCode())));
        }
    }

    /**
     * Describes what this tool does when it is used on a block, which is the one part of the tooltip that is specific
     * to the tool rather than to its material. Called after the material lines and before the mode-switch hint.
     */
    protected void addBehaviourToolTips(List<String> list, ItemStack stack) {}

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creativeTab, List list) {
        for (int i = 0; i < validMetas.size(); i++) {
            ItemStack stack = new ItemStack(this, 1, validMetas.getInt(i));
            isItemStackUsable(stack);
            list.add(stack);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        // Rendered from the material's icon set by MetaGeneratedToolRenderer, so there is no icon of our own.
    }

    @Override
    public IIcon getIconFromDamage(int metaData) {
        return null;
    }

    /* ---------- ENCHANTMENTS ---------- */

    /**
     * Keeps the stack's enchantments in sync with the ones its material and tool type grant. Mirrors
     * {@link MetaGeneratedTool#isItemStackUsable(ItemStack)}, minus the charged/discharged metadata handling that the
     * old tool item needed.
     */
    @Override
    public boolean isItemStackUsable(ItemStack stack) {
        if (stack == null) return false;
        Materials material = getToolMaterial(stack);
        if (material == Materials._NULL) {
            ItemStackNBT.removeTag(stack, "ench");
            return false;
        }
        HashMap<Integer, Integer> wanted = new HashMap<>(), result = new HashMap<>();
        if (material.mToolEnchantment != null) {
            wanted.put(material.mToolEnchantment.effectId, (int) material.mToolEnchantmentLevel);
            if (material.mToolEnchantment == Enchantment.fortune)
                wanted.put(Enchantment.looting.effectId, (int) material.mToolEnchantmentLevel);
            if (material.mToolEnchantment == Enchantment.knockback)
                wanted.put(Enchantment.power.effectId, (int) material.mToolEnchantmentLevel);
            if (material.mToolEnchantment == Enchantment.fireAspect)
                wanted.put(Enchantment.flame.effectId, (int) material.mToolEnchantmentLevel);
        }
        Enchantment[] enchants = toolStats.getEnchantments(stack);
        int[] levels = toolStats.getEnchantmentLevels(stack);
        for (int i = 0; i < enchants.length; i++) if (levels[i] > 0) {
            Integer level = wanted.get(enchants[i].effectId);
            wanted.put(
                enchants[i].effectId,
                level == null ? levels[i] : level == levels[i] ? level + 1 : Math.max(level, levels[i]));
        }
        for (Entry<Integer, Integer> entry : wanted.entrySet()) {
            if (entry.getKey() == 33 || (entry.getKey() == 20 && entry.getValue() > 2)
                || entry.getKey() == EnchantmentRadioactivity.INSTANCE.effectId) {
                result.put(entry.getKey(), entry.getValue());
            } else {
                switch (Enchantment.enchantmentsList[entry.getKey()].type) {
                    case weapon -> {
                        if (toolStats.isWeapon()) result.put(entry.getKey(), entry.getValue());
                    }
                    case all -> result.put(entry.getKey(), entry.getValue());
                    case armor, armor_feet, armor_head, armor_legs, armor_torso, breakable, fishing_rod -> {}
                    case bow -> {
                        if (toolStats.isRangedWeapon()) result.put(entry.getKey(), entry.getValue());
                    }
                    case digger -> {
                        if (toolStats.isMiningTool()) result.put(entry.getKey(), entry.getValue());
                    }
                }
            }
        }
        EnchantmentHelper.setEnchantments(result, stack);
        return true;
    }
}
