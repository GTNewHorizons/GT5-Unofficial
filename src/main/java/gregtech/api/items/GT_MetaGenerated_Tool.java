package gregtech.api.items;

import static gregtech.api.util.GT_Utility.formatNumbers;
import static gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_LargeTurbine_Steam.calculateLooseFlow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;

import buildcraft.api.tools.IToolWrench;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import crazypants.enderio.api.tool.ITool;
import forestry.api.arboriculture.IToolGrafter;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enchants.Enchantment_Radioactivity;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TC_Aspects.TC_AspectStack;
import gregtech.api.interfaces.IDamagableItem;
import gregtech.api.interfaces.IToolStats;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.common.tools.GT_Tool_Turbine;
import mods.railcraft.api.core.items.IToolCrowbar;
import mrtjp.projectred.api.IScrewdriver;

/**
 * This is an example on how you can create a Tool ItemStack, in this case a Bismuth Wrench:
 * GT_MetaGenerated_Tool.sInstances.get("gt.metatool.01").getToolWithStats(GT_MetaGenerated_Tool_01.WRENCH, 1,
 * Materials.Bismuth, Materials.Bismuth, null);
 */
@Optional.InterfaceList(
    value = {
        @Optional.Interface(iface = "forestry.api.arboriculture.IToolGrafter", modid = "ForestryAPI|arboriculture"),
        @Optional.Interface(iface = "mods.railcraft.api.core.items.IToolCrowbar", modid = "RailcraftAPI|items"),
        @Optional.Interface(iface = "buildcraft.api.tools.IToolWrench", modid = "BuildCraftAPI|tools"),
        @Optional.Interface(iface = "crazypants.enderio.api.tool.ITool", modid = "EnderIOAPI|Tools"),
        @Optional.Interface(iface = "mrtjp.projectred.api.IScrewdriver", modid = "ProjRed|Core"), })
public abstract class GT_MetaGenerated_Tool extends GT_MetaBase_Item
    implements IDamagableItem, IToolGrafter, IToolCrowbar, IToolWrench, ITool, IScrewdriver {

    /**
     * All instances of this Item Class are listed here. This gets used to register the Renderer to all Items of this
     * Type, if useStandardMetaItemRenderer() returns true.
     * <p/>
     * You can also use the unlocalized Name gotten from getUnlocalizedName() as Key if you want to get a specific Item.
     */
    public static final ConcurrentHashMap<String, GT_MetaGenerated_Tool> sInstances = new ConcurrentHashMap<>();

    /* ---------- CONSTRUCTOR AND MEMBER VARIABLES ---------- */

    public final ConcurrentHashMap<Short, IToolStats> mToolStats = new ConcurrentHashMap<>();

    /**
     * Creates the Item using these Parameters.
     *
     * @param aUnlocalized The Unlocalized Name of this Item.
     */
    public GT_MetaGenerated_Tool(String aUnlocalized) {
        super(aUnlocalized);
        setCreativeTab(GregTech_API.TAB_GREGTECH);
        setMaxStackSize(1);
        sInstances.put(getUnlocalizedName(), this);
    }

    /* ---------- FOR ADDING CUSTOM ITEMS INTO THE REMAINING 766 RANGE ---------- */

    public static final Materials getPrimaryMaterial(ItemStack aStack) {
        NBTTagCompound aNBT = aStack.getTagCompound();
        if (aNBT != null) {
            aNBT = aNBT.getCompoundTag("GT.ToolStats");
            if (aNBT != null) return Materials.getRealMaterial(aNBT.getString("PrimaryMaterial"));
        }
        return Materials._NULL;
    }

    public static final Materials getSecondaryMaterial(ItemStack aStack) {
        NBTTagCompound aNBT = aStack.getTagCompound();
        if (aNBT != null) {
            aNBT = aNBT.getCompoundTag("GT.ToolStats");
            if (aNBT != null) return Materials.getRealMaterial(aNBT.getString("SecondaryMaterial"));
        }
        return Materials._NULL;
    }

    /* ---------- INTERNAL OVERRIDES ---------- */

    public static final long getToolMaxDamage(ItemStack aStack) {
        NBTTagCompound aNBT = aStack.getTagCompound();
        if (aNBT != null) {
            aNBT = aNBT.getCompoundTag("GT.ToolStats");
            if (aNBT != null) return aNBT.getLong("MaxDamage");
        }
        return 0;
    }

    public static final long getToolDamage(ItemStack aStack) {
        NBTTagCompound aNBT = aStack.getTagCompound();
        if (aNBT != null) {
            aNBT = aNBT.getCompoundTag("GT.ToolStats");
            if (aNBT != null) return aNBT.getLong("Damage");
        }
        return 0;
    }

    public static final boolean setToolDamage(ItemStack aStack, long aDamage) {
        NBTTagCompound aNBT = aStack.getTagCompound();
        if (aNBT != null) {
            aNBT = aNBT.getCompoundTag("GT.ToolStats");
            if (aNBT != null) {
                aNBT.setLong("Damage", aDamage);
                return true;
            }
        }
        return false;
    }

    public static final boolean setToolMode(ItemStack aStack, byte aMode) {
        NBTTagCompound aNBT = aStack.getTagCompound();
        if (aNBT != null) {
            aNBT = aNBT.getCompoundTag("GT.ToolStats");
            if (aNBT != null) {
                aNBT.setByte("Mode", aMode);
                return true;
            }
        }
        return false;
    }

    public static final byte getToolMode(ItemStack aStack) {
        NBTTagCompound aNBT = aStack.getTagCompound();
        if (aNBT != null) {
            aNBT = aNBT.getCompoundTag("GT.ToolStats");
            if (aNBT != null) return aNBT.getByte("Mode");
        }
        return 0;
    }

    /**
     * This adds a Custom Item to the ending Range.
     *
     * @param aID                     The Id of the assigned Tool Class [0 - 32765] (only even Numbers allowed! Uneven
     *                                ID's are empty electric Items)
     * @param aEnglish                The Default Localized Name of the created Item
     * @param aToolTip                The Default ToolTip of the created Item, you can also insert null for having no
     *                                ToolTip
     * @param aToolStats              The Food Value of this Item. Can be null as well.
     * @param aOreDictNamesAndAspects The OreDict Names you want to give the Item. Also used to assign Thaumcraft
     *                                Aspects.
     * @return An ItemStack containing the newly created Item, but without specific Stats.
     */
    public final ItemStack addTool(int aID, String aEnglish, String aToolTip, IToolStats aToolStats,
        Object... aOreDictNamesAndAspects) {
        if (aToolTip == null) aToolTip = "";
        if (aID >= 0 && aID < 32766 && aID % 2 == 0) {
            GT_LanguageManager.addStringLocalization(getUnlocalizedName() + "." + aID + ".name", aEnglish);
            GT_LanguageManager.addStringLocalization(getUnlocalizedName() + "." + aID + ".tooltip", aToolTip);
            GT_LanguageManager
                .addStringLocalization(getUnlocalizedName() + "." + (aID + 1) + ".name", aEnglish + " (Empty)");
            GT_LanguageManager
                .addStringLocalization(getUnlocalizedName() + "." + (aID + 1) + ".tooltip", "You need to recharge it");
            mToolStats.put((short) aID, aToolStats);
            mToolStats.put((short) (aID + 1), aToolStats);
            aToolStats.onStatsAddedToTool(this, aID);
            ItemStack rStack = new ItemStack(this, 1, aID);
            List<TC_AspectStack> tAspects = new ArrayList<>();
            for (Object tOreDictNameOrAspect : aOreDictNamesAndAspects) {
                if (tOreDictNameOrAspect instanceof TC_AspectStack)
                    ((TC_AspectStack) tOreDictNameOrAspect).addToAspectList(tAspects);
                else GT_OreDictUnificator.registerOre(tOreDictNameOrAspect, rStack);
            }
            if (GregTech_API.sThaumcraftCompat != null)
                GregTech_API.sThaumcraftCompat.registerThaumcraftAspectsToItem(rStack, tAspects, false);
            GT_ModHandler.registerBoxableItemToToolBox(rStack);
            return rStack;
        }
        return null;
    }

    /**
     * This Function gets an ItemStack Version of this Tool
     *
     * @param aToolID            the ID of the Tool Class
     * @param aAmount            Amount of Items (well normally you only need 1)
     * @param aPrimaryMaterial   Primary Material of this Tool
     * @param aSecondaryMaterial Secondary (Rod/Handle) Material of this Tool
     * @param aElectricArray     The Electric Stats of this Tool (or null if not electric)
     */
    public final ItemStack getToolWithStats(int aToolID, int aAmount, Materials aPrimaryMaterial,
        Materials aSecondaryMaterial, long[] aElectricArray) {
        ItemStack rStack = new ItemStack(this, aAmount, aToolID);
        IToolStats tToolStats = getToolStats(rStack);
        if (tToolStats != null) {
            NBTTagCompound tMainNBT = new NBTTagCompound(), tToolNBT = new NBTTagCompound();
            tToolNBT.setByte("Mode", (byte) 0);
            if (aPrimaryMaterial != null) {
                tToolNBT.setString("PrimaryMaterial", aPrimaryMaterial.mName);
                tToolNBT.setLong(
                    "MaxDamage",
                    100L * (long) (aPrimaryMaterial.mDurability * tToolStats.getMaxDurabilityMultiplier()));
            }
            if (aSecondaryMaterial != null) tToolNBT.setString("SecondaryMaterial", aSecondaryMaterial.mName);

            if (aElectricArray != null) {
                tToolNBT.setBoolean("Electric", true);
                tToolNBT.setLong("MaxCharge", aElectricArray[0]);
                tToolNBT.setLong("Voltage", aElectricArray[1]);
                tToolNBT.setLong("Tier", aElectricArray[2]);
                tToolNBT.setLong("SpecialData", aElectricArray[3]);
            }

            tMainNBT.setTag("GT.ToolStats", tToolNBT);
            rStack.setTagCompound(tMainNBT);
        }
        isItemStackUsable(rStack);
        return rStack;
    }

    /**
     * Called by the Block Harvesting Event within the GT_Proxy
     */
    @Mod.EventHandler
    public void onHarvestBlockEvent(ArrayList<ItemStack> aDrops, ItemStack aStack, EntityPlayer aPlayer, Block aBlock,
        int aX, int aY, int aZ, byte aMetaData, int aFortune, boolean aSilkTouch, BlockEvent.HarvestDropsEvent aEvent) {
        IToolStats tStats = getToolStats(aStack);
        if (isItemStackUsable(aStack) && getDigSpeed(aStack, aBlock, aMetaData) > 0.0F) doDamage(
            aStack,
            (long) tStats
                .convertBlockDrops(aDrops, aStack, aPlayer, aBlock, aX, aY, aZ, aMetaData, aFortune, aSilkTouch, aEvent)
                * tStats.getToolDamagePerDropConversion());
    }

    @Mod.EventHandler
    public float onBlockBreakSpeedEvent(float aDefault, ItemStack aStack, EntityPlayer aPlayer, Block aBlock, int aX,
        int aY, int aZ, byte aMetaData, PlayerEvent.BreakSpeed aEvent) {
        IToolStats tStats = getToolStats(aStack);
        return tStats == null ? aDefault
            : tStats.getMiningSpeed(aBlock, aMetaData, aDefault, aPlayer, aPlayer.worldObj, aX, aY, aZ);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack aStack, int aX, int aY, int aZ, EntityPlayer aPlayer) {
        if (aPlayer.worldObj.isRemote) {
            return false;
        }
        IToolStats tStats = getToolStats(aStack);
        Block aBlock = aPlayer.worldObj.getBlock(aX, aY, aZ);
        if (tStats.isChainsaw() && (aBlock instanceof IShearable target)) {
            if ((target.isShearable(aStack, aPlayer.worldObj, aX, aY, aZ))) {
                ArrayList<ItemStack> drops = target.onSheared(
                    aStack,
                    aPlayer.worldObj,
                    aX,
                    aY,
                    aZ,
                    EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, aStack));
                for (ItemStack stack : drops) {
                    float f = 0.7F;
                    double d = itemRand.nextFloat() * f + (1.0F - f) * 0.5D;
                    double d1 = itemRand.nextFloat() * f + (1.0F - f) * 0.5D;
                    double d2 = itemRand.nextFloat() * f + (1.0F - f) * 0.5D;
                    EntityItem entityitem = new EntityItem(aPlayer.worldObj, aX + d, aY + d1, aZ + d2, stack);
                    entityitem.delayBeforeCanPickup = 10;
                    aPlayer.worldObj.spawnEntityInWorld(entityitem);
                }
                aPlayer.addStat(net.minecraft.stats.StatList.mineBlockStatArray[Block.getIdFromBlock(aBlock)], 1);
                onBlockDestroyed(aStack, aPlayer.worldObj, aBlock, aX, aY, aZ, aPlayer);
            }
            return false;
        }
        return super.onBlockStartBreak(aStack, aX, aY, aZ, aPlayer);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack aStack, EntityPlayer aPlayer, Entity aEntity) {
        IToolStats tStats = getToolStats(aStack);
        if (tStats == null || !isItemStackUsable(aStack)) return true;
        GT_Utility.doSoundAtClient(tStats.getEntityHitSound(), 1, 1.0F);
        if (super.onLeftClickEntity(aStack, aPlayer, aEntity)) return true;
        if (aEntity.canAttackWithItem() && !aEntity.hitByEntity(aPlayer)) {
            float tMagicDamage = tStats.getMagicDamageAgainstEntity(
                aEntity instanceof EntityLivingBase
                    ? EnchantmentHelper.getEnchantmentModifierLiving(aPlayer, (EntityLivingBase) aEntity)
                    : 0.0F,
                aEntity,
                aStack,
                aPlayer),
                tDamage = tStats.getNormalDamageAgainstEntity(
                    (float) aPlayer.getEntityAttribute(SharedMonsterAttributes.attackDamage)
                        .getAttributeValue() + getToolCombatDamage(aStack),
                    aEntity,
                    aStack,
                    aPlayer);
            if (tDamage + tMagicDamage > 0.0F) {
                boolean tCriticalHit = aPlayer.fallDistance > 0.0F && !aPlayer.onGround
                    && !aPlayer.isOnLadder()
                    && !aPlayer.isInWater()
                    && !aPlayer.isPotionActive(Potion.blindness)
                    && aPlayer.ridingEntity == null
                    && aEntity instanceof EntityLivingBase;
                if (tCriticalHit && tDamage > 0.0F) tDamage *= 1.5F;
                tDamage += tMagicDamage;
                if (aEntity.attackEntityFrom(tStats.getDamageSource(aPlayer, aEntity), tDamage)) {
                    if (aEntity instanceof EntityLivingBase)
                        aEntity.setFire(EnchantmentHelper.getFireAspectModifier(aPlayer) * 4);
                    int tKnockcack = (aPlayer.isSprinting() ? 1 : 0) + (aEntity instanceof EntityLivingBase
                        ? EnchantmentHelper.getKnockbackModifier(aPlayer, (EntityLivingBase) aEntity)
                        : 0);
                    if (tKnockcack > 0) {
                        aEntity.addVelocity(
                            -MathHelper.sin(aPlayer.rotationYaw * (float) Math.PI / 180.0F) * tKnockcack * 0.5F,
                            0.1D,
                            MathHelper.cos(aPlayer.rotationYaw * (float) Math.PI / 180.0F) * tKnockcack * 0.5F);
                        aPlayer.motionX *= 0.6D;
                        aPlayer.motionZ *= 0.6D;
                        aPlayer.setSprinting(false);
                    }
                    if (tCriticalHit) aPlayer.onCriticalHit(aEntity);
                    if (tMagicDamage > 0.0F) aPlayer.onEnchantmentCritical(aEntity);
                    if (tDamage >= 18.0F) aPlayer.triggerAchievement(AchievementList.overkill);
                    aPlayer.setLastAttacker(aEntity);
                    if (aEntity instanceof EntityLivingBase)
                        EnchantmentHelper.func_151384_a((EntityLivingBase) aEntity, aPlayer);
                    EnchantmentHelper.func_151385_b(aPlayer, aEntity);
                    if (aEntity instanceof EntityLivingBase)
                        aPlayer.addStat(StatList.damageDealtStat, Math.round(tDamage * 10.0F));
                    aEntity.hurtResistantTime = Math
                        .max(1, tStats.getHurtResistanceTime(aEntity.hurtResistantTime, aEntity));
                    aPlayer.addExhaustion(0.3F);
                    doDamage(aStack, tStats.getToolDamagePerEntityAttack());
                }
            }
        }
        if (aStack.stackSize <= 0) aPlayer.destroyCurrentEquippedItem();
        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
        IToolStats tStats = getToolStats(aStack);
        if (tStats != null && tStats.canBlock()) aPlayer.setItemInUse(aStack, 72000);
        return super.onItemRightClick(aStack, aWorld, aPlayer);
    }

    @Override
    public final int getMaxItemUseDuration(ItemStack aStack) {
        return 72000;
    }

    @Override
    public final EnumAction getItemUseAction(ItemStack aStack) {
        IToolStats tStats = getToolStats(aStack);
        if (tStats != null && tStats.canBlock()) return EnumAction.block;
        return EnumAction.none;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public final void getSubItems(Item aItem, CreativeTabs aCreativeTab, List<ItemStack> aList) {
        for (int i = 0; i < 32766; i += 2) {
            if (getToolStats(new ItemStack(this, 1, i)) != null) {
                ItemStack tStack = new ItemStack(this, 1, i);
                isItemStackUsable(tStack);
                aList.add(tStack);
                aList.add(getToolWithStats(i, 1, Materials.Neutronium, Materials.Neutronium, null));
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public final void registerIcons(IIconRegister aIconRegister) {
        //
    }

    @Override
    public final IIcon getIconFromDamage(int aMetaData) {
        return null;
    }

    @Override
    public void addAdditionalToolTips(List<String> aList, ItemStack aStack, EntityPlayer aPlayer) {
        long tMaxDamage = getToolMaxDamage(aStack);
        Materials tMaterial = getPrimaryMaterial(aStack);
        IToolStats tStats = getToolStats(aStack);
        int tOffset = getElectricStats(aStack) != null ? 2 : 1;
        if (tStats != null) {
            if (tStats instanceof GT_Tool_Turbine) {

                // Durability -> toolMaxDamage
                // % Efficiency -> toolCombatDamage -> toolQuality
                // Optimal Flow -> toolSpeed
                // EU/t -> toolCombatDamage, toolSpeed
                // Overflow Tier -> toolQuality
                float aBaseEff = (5f + getToolCombatDamage(aStack)) * 1000f;

                // It was noted by IntelliJ that replacing ((GT_MetaGenerated_Tool) aStack.getItem()) with
                // GT_MetaGenerated_Tool can have side effects. This refactoring will need tests.
                @SuppressWarnings("AccessStaticViaInstance")
                float aOptFlow = (Math.max(
                    Float.MIN_NORMAL,
                    ((GT_MetaGenerated_Tool) aStack.getItem()).getToolStats(aStack)
                        .getSpeedMultiplier()
                        * ((GT_MetaGenerated_Tool) aStack.getItem()).getPrimaryMaterial(aStack).mToolSpeed
                        * 50F));
                aList.add(
                    tOffset + 0,
                    EnumChatFormatting.GRAY + String.format(
                        transItem("001", "Durability: %s/%s"),
                        "" + EnumChatFormatting.GREEN + formatNumbers(tMaxDamage - getToolDamage(aStack)) + " ",
                        " " + formatNumbers(tMaxDamage)) + EnumChatFormatting.GRAY);
                aList.add(
                    tOffset + 1,
                    EnumChatFormatting.GRAY + String.format(
                        transItem("002", "%s lvl %s"),
                        tMaterial.mLocalizedName + EnumChatFormatting.YELLOW,
                        "" + getHarvestLevel(aStack, "")) + EnumChatFormatting.GRAY);
                aList.add(
                    tOffset + 2,
                    EnumChatFormatting.WHITE
                        + String.format(
                            transItem("005", "Turbine Efficiency: %s"),
                            "" + EnumChatFormatting.BLUE + (50.0F + (10.0F * getToolCombatDamage(aStack))))
                        + "%"
                        + EnumChatFormatting.GRAY);
                aList.add(
                    tOffset + 3,
                    EnumChatFormatting.WHITE + String.format(
                        transItem("006", "Optimal Steam flow: %s L/t"),
                        "" + EnumChatFormatting.GOLD
                            + formatNumbers(
                                GT_Utility.safeInt(
                                    (long) (Math.max(
                                        Float.MIN_NORMAL,
                                        tStats.getSpeedMultiplier() * getPrimaryMaterial(aStack).mToolSpeed
                                            * (1000 * getPrimaryMaterial(aStack).mSteamMultiplier / 20)))))
                            + EnumChatFormatting.GRAY));
                aList.add(
                    tOffset + 4,
                    EnumChatFormatting.WHITE + String.format(
                        transItem("900", "Energy from Optimal Steam Flow: %s EU/t"),
                        "" + EnumChatFormatting.GOLD
                            + formatNumbers(
                                GT_Utility.safeInt(
                                    (long) (Math.max(
                                        Float.MIN_NORMAL,
                                        tStats.getSpeedMultiplier() * getPrimaryMaterial(aStack).mToolSpeed
                                            * (1000 * getPrimaryMaterial(aStack).mSteamMultiplier / 20))
                                        * (50.0F + (10.0F * getToolCombatDamage(aStack)))
                                        / 200)))
                            + EnumChatFormatting.GRAY));
                {
                    float[] calculatedFlow = calculateLooseFlow(aOptFlow, aBaseEff);
                    float aOptFlowLoose = calculatedFlow[0];
                    float aBaseEffLoose = calculatedFlow[1];

                    aList.add(
                        tOffset + 5,
                        EnumChatFormatting.AQUA + String.format(
                            transItem("500", "Turbine Efficiency (Loose): %s"),
                            "" + EnumChatFormatting.BLUE + (long) aBaseEffLoose / 100 + "%" + EnumChatFormatting.GRAY));
                    aList.add(
                        tOffset + 6,
                        EnumChatFormatting.AQUA + String.format(
                            transItem("501", "Optimal Steam flow (Loose): %s L/t"),
                            "" + EnumChatFormatting.GOLD
                                + formatNumbers(((long) aOptFlowLoose * getPrimaryMaterial(aStack).mSteamMultiplier))
                                + EnumChatFormatting.GRAY));
                    aList.add(
                        tOffset + 7,
                        EnumChatFormatting.AQUA + String.format(
                            transItem("901", "Energy from Optimal Steam Flow (Loose): %s EU/t"),
                            "" + EnumChatFormatting.GOLD
                                + formatNumbers(
                                    ((long) aOptFlowLoose * getPrimaryMaterial(aStack).mSteamMultiplier / 10000)
                                        * ((long) aBaseEffLoose / 2))
                                + EnumChatFormatting.GRAY));
                    aList.add(
                        tOffset + 8,
                        EnumChatFormatting.GRAY + "(Superheated Steam EU values are 2x those of Steam)");
                }
                aList.add(
                    tOffset + 9,
                    EnumChatFormatting.LIGHT_PURPLE + String.format(
                        transItem("007", "Energy from Optimal Gas Flow: %s EU/t"),
                        "" + EnumChatFormatting.GOLD
                            + formatNumbers(
                                GT_Utility.safeInt(
                                    (long) (Math.max(
                                        Float.MIN_NORMAL,
                                        tStats.getSpeedMultiplier() * getPrimaryMaterial(aStack).mToolSpeed
                                            * 50
                                            * getPrimaryMaterial(aStack).mGasMultiplier)
                                        * (50.0F + (10.0F * getToolCombatDamage(aStack)))
                                        / 100)))
                            + EnumChatFormatting.GRAY));
                aList.add(
                    tOffset + 10,
                    EnumChatFormatting.LIGHT_PURPLE + String.format(
                        transItem("008", "Energy from Optimal Plasma Flow: %s EU/t"),
                        "" + EnumChatFormatting.GOLD
                            + formatNumbers(
                                GT_Utility.safeInt(
                                    (long) (Math.max(
                                        Float.MIN_NORMAL,
                                        tStats.getSpeedMultiplier() * getPrimaryMaterial(aStack).mToolSpeed
                                            * 2000
                                            * getPrimaryMaterial(aStack).mPlasmaMultiplier)
                                        * (50.0F + (10.0F * getToolCombatDamage(aStack)))
                                        * (1.05 / 100))))
                            + EnumChatFormatting.GRAY));
                aList.add(
                    tOffset + 12,
                    EnumChatFormatting.GRAY + "(EU/t values include efficiency and are not 100% accurate)");
                int toolQualityLevel = GT_MetaGenerated_Tool.getPrimaryMaterial(aStack).mToolQuality;
                int overflowMultiplier = 0;
                if (toolQualityLevel >= 6) {
                    overflowMultiplier = 3;
                } else if (toolQualityLevel >= 3) {
                    overflowMultiplier = 2;
                } else {
                    overflowMultiplier = 1;
                }
                aList.add(
                    tOffset + 11,
                    EnumChatFormatting.LIGHT_PURPLE + String.format(
                        transItem("502", "Overflow Efficiency Tier: %s"),
                        "" + EnumChatFormatting.GOLD + overflowMultiplier + EnumChatFormatting.GRAY));

            } else {
                aList.add(
                    tOffset,
                    EnumChatFormatting.WHITE + String.format(
                        transItem("001", "Durability: %s/%s"),
                        "" + EnumChatFormatting.GREEN + formatNumbers(tMaxDamage - getToolDamage(aStack)) + " ",
                        " " + formatNumbers(tMaxDamage)) + EnumChatFormatting.GRAY);
                aList.add(
                    tOffset + 1,
                    EnumChatFormatting.WHITE + String.format(
                        transItem("002", "%s lvl %s"),
                        tMaterial.mLocalizedName + EnumChatFormatting.YELLOW,
                        "" + getHarvestLevel(aStack, "")) + EnumChatFormatting.GRAY);
                aList.add(
                    tOffset + 2,
                    EnumChatFormatting.WHITE + String.format(
                        transItem("003", "Attack Damage: %s"),
                        "" + EnumChatFormatting.BLUE + getToolCombatDamage(aStack)) + EnumChatFormatting.GRAY);
                aList.add(
                    tOffset + 3,
                    EnumChatFormatting.WHITE
                        + String.format(
                            transItem("004", "Mining Speed: %s"),
                            "" + EnumChatFormatting.GOLD
                                + Math.max(
                                    Float.MIN_NORMAL,
                                    tStats.getSpeedMultiplier() * getPrimaryMaterial(aStack).mToolSpeed))
                        + EnumChatFormatting.GRAY);
                NBTTagCompound aNBT = aStack.getTagCompound();
                if (aNBT != null) {
                    aNBT = aNBT.getCompoundTag("GT.ToolStats");
                    if (aNBT != null && aNBT.hasKey("Heat")) {
                        int tHeat = aNBT.getInteger("Heat");
                        long tWorldTime = aPlayer.getEntityWorld()
                            .getWorldTime();
                        if (aNBT.hasKey("HeatTime")) {
                            long tHeatTime = aNBT.getLong("HeatTime");
                            if (tWorldTime > (tHeatTime + 10)) {
                                tHeat = (int) (tHeat - ((tWorldTime - tHeatTime) / 10));
                                if (tHeat < 300 && tHeat > -10000) tHeat = 300;
                            }
                            aNBT.setLong("HeatTime", tWorldTime);
                            if (tHeat > -10000) aNBT.setInteger("Heat", tHeat);
                        }

                        aList.add(
                            tOffset + 3,
                            EnumChatFormatting.RED + "Heat: "
                                + aNBT.getInteger("Heat")
                                + " K"
                                + EnumChatFormatting.GRAY);
                    }
                }
            }
        }
    }

    @Override
    public Long[] getFluidContainerStats(ItemStack aStack) {
        return null;
    }

    @Override
    public Long[] getElectricStats(ItemStack aStack) {
        NBTTagCompound aNBT = aStack.getTagCompound();
        if (aNBT != null) {
            aNBT = aNBT.getCompoundTag("GT.ToolStats");
            if (aNBT != null && aNBT.getBoolean("Electric")) return new Long[] { aNBT.getLong("MaxCharge"),
                aNBT.getLong("Voltage"), aNBT.getLong("Tier"), aNBT.getLong("SpecialData") };
        }
        return null;
    }

    public float getToolCombatDamage(ItemStack aStack) {
        IToolStats tStats = getToolStats(aStack);
        if (tStats == null) return 0;
        return tStats.getBaseDamage() + getPrimaryMaterial(aStack).mToolQuality;
    }

    @Override
    public final boolean doDamageToItem(ItemStack aStack, int aVanillaDamage) {
        return doDamage(aStack, aVanillaDamage * 100L);
    }

    public final boolean doDamage(ItemStack aStack, long aAmount) {
        if (!isItemStackUsable(aStack)) return false;
        Long[] tElectric = getElectricStats(aStack);
        if (tElectric == null) {
            long tNewDamage = getToolDamage(aStack) + aAmount;
            setToolDamage(aStack, tNewDamage);
            if (tNewDamage >= getToolMaxDamage(aStack)) {
                IToolStats tStats = getToolStats(aStack);
                if (tStats == null || GT_Utility.setStack(aStack, tStats.getBrokenItem(aStack)) == null) {
                    if (tStats != null) GT_Utility.doSoundAtClient(tStats.getBreakingSound(), 1, 1.0F);
                    if (aStack.stackSize > 0) aStack.stackSize--;
                }
            }
            return true;
        }
        if (use(aStack, (int) aAmount, null)) {
            if (java.util.concurrent.ThreadLocalRandom.current()
                .nextInt(0, 25) == 0) {
                long tNewDamage = getToolDamage(aStack) + aAmount;
                setToolDamage(aStack, tNewDamage);
                if (tNewDamage >= getToolMaxDamage(aStack)) {
                    IToolStats tStats = getToolStats(aStack);
                    if (tStats == null || GT_Utility.setStack(aStack, tStats.getBrokenItem(aStack)) == null) {
                        if (tStats != null) GT_Utility.doSoundAtClient(tStats.getBreakingSound(), 1, 1.0F);
                        if (aStack.stackSize > 0) aStack.stackSize--;
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public float getDigSpeed(ItemStack aStack, Block aBlock, int aMetaData) {
        if (!isItemStackUsable(aStack)) return 0.0F;
        IToolStats tStats = getToolStats(aStack);
        if (tStats == null || Math.max(0, getHarvestLevel(aStack, "")) < aBlock.getHarvestLevel(aMetaData)) return 0.0F;
        return tStats.isMinableBlock(aBlock, (byte) aMetaData)
            ? Math.max(Float.MIN_NORMAL, tStats.getSpeedMultiplier() * getPrimaryMaterial(aStack).mToolSpeed)
            : 1.0F;
    }

    @Override
    public final boolean canHarvestBlock(Block aBlock, ItemStack aStack) {
        return getDigSpeed(aStack, aBlock, (byte) 0) > 0.0F;
    }

    @Override
    public final int getHarvestLevel(ItemStack aStack, String aToolClass) {
        IToolStats tStats = getToolStats(aStack);
        return tStats == null ? -1 : tStats.getBaseQuality() + getPrimaryMaterial(aStack).mToolQuality;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack aStack, World aWorld, Block aBlock, int aX, int aY, int aZ,
        EntityLivingBase aPlayer) {
        if (!isItemStackUsable(aStack)) return false;
        IToolStats tStats = getToolStats(aStack);
        if (tStats == null) return false;
        GT_Utility.doSoundAtClient(tStats.getMiningSound(), 1, 1.0F);
        doDamage(
            aStack,
            (int) Math.max(1, aBlock.getBlockHardness(aWorld, aX, aY, aZ) * tStats.getToolDamagePerBlockBreak()));
        return getDigSpeed(aStack, aBlock, aWorld.getBlockMetadata(aX, aY, aZ)) > 0.0F;
    }

    @Override
    public final ItemStack getContainerItem(ItemStack aStack) {
        return getContainerItem(aStack, true);
    }

    @Override
    public final boolean hasContainerItem(ItemStack aStack) {
        return getContainerItem(aStack, false) != null;
    }

    private ItemStack getContainerItem(ItemStack aStack, boolean playSound) {
        if (!isItemStackUsable(aStack)) return null;
        aStack = GT_Utility.copyAmount(1, aStack);
        IToolStats tStats = getToolStats(aStack);
        if (tStats == null) return null;
        doDamage(aStack, tStats.getToolDamagePerContainerCraft());
        aStack = aStack.stackSize > 0 ? aStack : null;
        if (playSound && GT_Mod.gregtechproxy.mTicksUntilNextCraftSound <= 0) {
            GT_Mod.gregtechproxy.mTicksUntilNextCraftSound = 10;
            String sound = (aStack == null) ? tStats.getBreakingSound() : tStats.getCraftingSound();
            GT_Utility.doSoundAtClient(sound, 1, 1.0F);
        }
        return aStack;
    }

    public IToolStats getToolStats(ItemStack aStack) {
        isItemStackUsable(aStack);
        return getToolStatsInternal(aStack);
    }

    public byte getToolMaxMode(ItemStack aStack) {
        IToolStats stats = getToolStats(aStack);
        if (stats != null) {
            return stats.getMaxMode();
        }
        return 1;
    }

    private IToolStats getToolStatsInternal(ItemStack aStack) {
        return aStack == null ? null : mToolStats.get((short) aStack.getItemDamage());
    }

    @Override
    public float getSaplingModifier(ItemStack aStack, World aWorld, EntityPlayer aPlayer, int aX, int aY, int aZ) {
        IToolStats tStats = getToolStats(aStack);
        return tStats != null && tStats.isGrafter() ? Math.min(100.0F, (1 + getHarvestLevel(aStack, "")) * 20.0F)
            : 0.0F;
    }

    @Override
    public boolean canWhack(EntityPlayer aPlayer, ItemStack aStack, int aX, int aY, int aZ) {
        if (!isItemStackUsable(aStack)) return false;
        IToolStats tStats = getToolStats(aStack);
        return tStats != null && tStats.isCrowbar();
    }

    @Override
    public void onWhack(EntityPlayer aPlayer, ItemStack aStack, int aX, int aY, int aZ) {
        IToolStats tStats = getToolStats(aStack);
        if (tStats != null) doDamage(aStack, tStats.getToolDamagePerEntityAttack());
    }

    @Override
    public boolean canWrench(EntityPlayer player, int x, int y, int z) {
        if (player == null) return false;
        if (player.getCurrentEquippedItem() == null) return false;
        if (!isItemStackUsable(player.getCurrentEquippedItem())) return false;
        IToolStats tStats = getToolStats(player.getCurrentEquippedItem());
        return tStats != null && tStats.isWrench();
    }

    @Override
    public void wrenchUsed(EntityPlayer player, int x, int y, int z) {
        if (player == null) return;
        if (player.getCurrentEquippedItem() == null) return;
        IToolStats tStats = getToolStats(player.getCurrentEquippedItem());
        if (tStats != null) doDamage(player.getCurrentEquippedItem(), tStats.getToolDamagePerEntityAttack());
    }

    @Override
    public boolean canUse(ItemStack stack, EntityPlayer player, int x, int y, int z) {
        return canWrench(player, x, y, z);
    }

    // ProjectRed screwdriver
    @Override
    public boolean canUse(EntityPlayer player, ItemStack stack) {
        if (player == null) return false;
        if (GT_Utility.isStackInvalid(stack) || !isItemStackUsable(stack)) return false;
        IToolStats tStats = getToolStats(stack);
        return tStats != null && tStats.isScrewdriver();
    }

    @Override
    public void damageScrewdriver(EntityPlayer player, ItemStack stack) {
        if (player == null) return;
        if (GT_Utility.isStackInvalid(stack) || !isItemStackUsable(stack)) return;
        IToolStats tStats = getToolStats(stack);
        if (tStats != null) doDamage(stack, tStats.getToolDamagePerEntityAttack());
    }

    @Override
    public void used(ItemStack stack, EntityPlayer player, int x, int y, int z) {
        wrenchUsed(player, x, y, z);
    }

    @Override
    public boolean shouldHideFacades(ItemStack stack, EntityPlayer player) {
        if (player == null) return false;
        if (player.getCurrentEquippedItem() == null) return false;
        if (!isItemStackUsable(player.getCurrentEquippedItem())) return false;
        IToolStats tStats = getToolStats(player.getCurrentEquippedItem());
        return tStats.isWrench();
    }

    @Override
    public boolean canLink(EntityPlayer aPlayer, ItemStack aStack, EntityMinecart cart) {
        if (!isItemStackUsable(aStack)) return false;
        IToolStats tStats = getToolStats(aStack);
        return tStats != null && tStats.isCrowbar();
    }

    @Override
    public void onLink(EntityPlayer aPlayer, ItemStack aStack, EntityMinecart cart) {
        IToolStats tStats = getToolStats(aStack);
        if (tStats != null) doDamage(aStack, tStats.getToolDamagePerEntityAttack());
    }

    @Override
    public boolean canBoost(EntityPlayer aPlayer, ItemStack aStack, EntityMinecart cart) {
        if (!isItemStackUsable(aStack)) return false;
        IToolStats tStats = getToolStats(aStack);
        return tStats != null && tStats.isCrowbar();
    }

    @Override
    public void onBoost(EntityPlayer aPlayer, ItemStack aStack, EntityMinecart cart) {
        IToolStats tStats = getToolStats(aStack);
        if (tStats != null) doDamage(aStack, tStats.getToolDamagePerEntityAttack());
    }

    @Override
    public void onCreated(ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
        IToolStats tStats = getToolStats(aStack);
        if (tStats != null && aPlayer != null) tStats.onToolCrafted(aStack, aPlayer);
        super.onCreated(aStack, aWorld, aPlayer);
    }

    @Override
    public final boolean doesContainerItemLeaveCraftingGrid(ItemStack aStack) {
        return false;
    }

    @Override
    public final int getItemStackLimit(ItemStack aStack) {
        return 1;
    }

    @Override
    public boolean isFull3D() {
        return true;
    }

    @Override
    public boolean isItemStackUsable(ItemStack aStack) {
        IToolStats tStats = getToolStatsInternal(aStack);
        if (aStack.getItemDamage() % 2 != 0 || tStats == null) {
            NBTTagCompound aNBT = aStack.getTagCompound();
            if (aNBT != null) aNBT.removeTag("ench");
            return false;
        }
        Materials aMaterial = getPrimaryMaterial(aStack);
        HashMap<Integer, Integer> tMap = new HashMap<>(), tResult = new HashMap<>();
        if (aMaterial.mEnchantmentTools != null) {
            tMap.put(aMaterial.mEnchantmentTools.effectId, (int) aMaterial.mEnchantmentToolsLevel);
            if (aMaterial.mEnchantmentTools == Enchantment.fortune)
                tMap.put(Enchantment.looting.effectId, (int) aMaterial.mEnchantmentToolsLevel);
            if (aMaterial.mEnchantmentTools == Enchantment.knockback)
                tMap.put(Enchantment.power.effectId, (int) aMaterial.mEnchantmentToolsLevel);
            if (aMaterial.mEnchantmentTools == Enchantment.fireAspect)
                tMap.put(Enchantment.flame.effectId, (int) aMaterial.mEnchantmentToolsLevel);
        }
        Enchantment[] tEnchants = tStats.getEnchantments(aStack);
        int[] tLevels = tStats.getEnchantmentLevels(aStack);
        for (int i = 0; i < tEnchants.length; i++) if (tLevels[i] > 0) {
            Integer tLevel = tMap.get(tEnchants[i].effectId);
            tMap.put(
                tEnchants[i].effectId,
                tLevel == null ? tLevels[i] : tLevel == tLevels[i] ? tLevel + 1 : Math.max(tLevel, tLevels[i]));
        }
        for (Entry<Integer, Integer> tEntry : tMap.entrySet()) {
            if (tEntry.getKey() == 33 || (tEntry.getKey() == 20 && tEntry.getValue() > 2)
                || tEntry.getKey() == Enchantment_Radioactivity.INSTANCE.effectId)
                tResult.put(tEntry.getKey(), tEntry.getValue());
            else switch (Enchantment.enchantmentsList[tEntry.getKey()].type) {
                case weapon:
                    if (tStats.isWeapon()) tResult.put(tEntry.getKey(), tEntry.getValue());
                    break;
                case all:
                    tResult.put(tEntry.getKey(), tEntry.getValue());
                    break;
                case armor:
                case armor_feet:
                case armor_head:
                case armor_legs:
                case armor_torso:
                    break;
                case bow:
                    if (tStats.isRangedWeapon()) tResult.put(tEntry.getKey(), tEntry.getValue());
                    break;
                case breakable:
                    break;
                case fishing_rod:
                    break;
                case digger:
                    if (tStats.isMiningTool()) tResult.put(tEntry.getKey(), tEntry.getValue());
                    break;
            }
        }
        EnchantmentHelper.setEnchantments(tResult, aStack);
        return true;
    }

    @Override
    public String getItemStackDisplayName(ItemStack aStack) {

        String result = super.getItemStackDisplayName(aStack);
        IToolStats toolStats = getToolStats(aStack);
        if (toolStats != null) {
            String toolName = toolStats.getToolTypeName();
            if (toolName == null) return result;

            String key = "gt." + toolName + ".mode." + getToolMode(aStack);
            if (StatCollector.canTranslate(key)) {
                result += " (" + StatCollector.translateToLocal(key) + ")";
            }
        }
        return result;

    }

    @Override
    public short getChargedMetaData(ItemStack aStack) {
        return (short) (aStack.getItemDamage() - (aStack.getItemDamage() % 2));
    }

    @Override
    public short getEmptyMetaData(ItemStack aStack) {
        NBTTagCompound aNBT = aStack.getTagCompound();
        if (aNBT != null) aNBT.removeTag("ench");
        return (short) (aStack.getItemDamage() + 1 - (aStack.getItemDamage() % 2));
    }

    @Override
    public int getItemEnchantability() {
        return 0;
    }

    @Override
    public boolean isBookEnchantable(ItemStack aStack, ItemStack aBook) {
        return false;
    }

    @Override
    public boolean getIsRepairable(ItemStack aStack, ItemStack aMaterial) {
        return false;
    }
}
