package gtPlusPlus.core.item.general.spawn;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class ItemCustomSpawnEgg extends ItemMonsterPlacer {

    private static final HashMap<Integer, IIcon> mIconMap = new HashMap<>();
    private static int mTotalMetaItems = 0;

    private static final HashMap<Integer, Integer> mMaxStackSizeMap = new HashMap<>();
    private static final HashMap<Integer, EnumRarity> mRarityMap = new HashMap<>();
    private static final HashMap<Integer, ArrayList<String>> mOreDictNames = new HashMap<>();

    private static final HashMap<Integer, Integer> mColourBaseMap = new HashMap<>();
    private static final HashMap<Integer, Integer> mColourSpotsMap = new HashMap<>();
    private static final HashMap<Integer, String> mEntityNameMap = new HashMap<>();
    private static final HashMap<Integer, String> mEntityFullNameMap = new HashMap<>();

    private static final HashMap<String, Integer> mReverseEntityMap = new HashMap<>();

    protected EntityLiving entityToSpawn = null;

    public static void registerEntityForSpawnEgg(final int aMetaID, String parEntityToSpawnName, int aPrimaryColor,
            int aSecondaryColor) {
        registerEntityForSpawnEgg(
                aMetaID,
                parEntityToSpawnName,
                aPrimaryColor,
                aSecondaryColor,
                EnumRarity.common,
                new ArrayList<String>());
    }

    public static void registerEntityForSpawnEgg(final int aMetaID, String parEntityToSpawnName, int aPrimaryColor,
            int aSecondaryColor, EnumRarity aRarity, final ArrayList<String> aOreDictNames) {
        mTotalMetaItems++;
        mMaxStackSizeMap.put(aMetaID, 64);
        mRarityMap.put(aMetaID, aRarity);
        mOreDictNames.put(aMetaID, aOreDictNames);
        mColourBaseMap.put(aMetaID, aPrimaryColor);
        mColourSpotsMap.put(aMetaID, aSecondaryColor);
        mReverseEntityMap.put(parEntityToSpawnName, aMetaID);
        setEntityToSpawnName(aMetaID, parEntityToSpawnName);
    }

    public ItemCustomSpawnEgg() {
        super();
        this.setNoRepair();
        this.setMaxStackSize(64);
        this.setMaxDamage(0);
        this.setUnlocalizedName("BasicMetaSpawnEgg");
        GameRegistry.registerItem(this, this.getUnlocalizedName());
    }

    /**
     * Callback for item usage. If the item does something special on right clicking,
     *
     * he will have one of those. Return True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4,
            int par5, int par6, int par7, float par8, float par9, float par10) {
        if (par3World.isRemote) {
            return true;
        } else {
            Block block = par3World.getBlock(par4, par5, par6);
            par4 += Facing.offsetsXForSide[par7];
            par5 += Facing.offsetsYForSide[par7];
            par6 += Facing.offsetsZForSide[par7];
            double d0 = 0.0D;

            if (par7 == 1 && block.getRenderType() == 11) {
                d0 = 0.5D;
            }

            Entity entity = spawnEntity(par1ItemStack, par3World, par4 + 0.5D, par5 + d0, par6 + 0.5D);

            if (entity != null) {
                if (entity instanceof EntityLivingBase && par1ItemStack.hasDisplayName()) {
                    ((EntityLiving) entity).setCustomNameTag(par1ItemStack.getDisplayName());
                }

                if (!par2EntityPlayer.capabilities.isCreativeMode) {
                    --par1ItemStack.stackSize;
                }
            }

            return true;
        }
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed.
     *
     * Args: itemStack, world, entityPlayer
     */
    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        if (par2World.isRemote) {
            return par1ItemStack;
        } else {
            MovingObjectPosition movingobjectposition = getMovingObjectPositionFromPlayer(
                    par2World,
                    par3EntityPlayer,
                    true);

            if (movingobjectposition == null) {
                return par1ItemStack;
            } else {
                if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                    int i = movingobjectposition.blockX;
                    int j = movingobjectposition.blockY;
                    int k = movingobjectposition.blockZ;

                    if (!par2World.canMineBlock(par3EntityPlayer, i, j, k)) {
                        return par1ItemStack;
                    }

                    if (!par3EntityPlayer.canPlayerEdit(i, j, k, movingobjectposition.sideHit, par1ItemStack)) {

                        return par1ItemStack;
                    }

                    if (par2World.getBlock(i, j, k) instanceof BlockLiquid) {
                        Entity entity = spawnEntity(par1ItemStack, par2World, i, j, k);

                        if (entity != null) {
                            if (entity instanceof EntityLivingBase && par1ItemStack.hasDisplayName()) {

                                ((EntityLiving) entity).setCustomNameTag(par1ItemStack.getDisplayName());
                            }

                            if (!par3EntityPlayer.capabilities.isCreativeMode) {
                                --par1ItemStack.stackSize;
                            }
                        }
                    }
                }

                return par1ItemStack;
            }
        }
    }

    /**
     * Spawns the creature specified by the egg's type in the location specified by
     *
     * the last three parameters. Parameters: world, entityID, x, y, z.
     * 
     * @param par1ItemStack
     */
    public Entity spawnEntity(ItemStack par1ItemStack, World parWorld, double parX, double parY, double parZ) {

        if (!parWorld.isRemote) // never spawn entity on client side
        {
            int aDamage = par1ItemStack.getItemDamage();
            String entityToSpawnNameFull = mEntityFullNameMap.get(aDamage);
            String entityToSpawnName = mEntityNameMap.get(aDamage);
            // entityToSpawnNameFull = WildAnimals.MODID + "." + entityToSpawnName;
            if (EntityList.stringToClassMapping.containsKey(entityToSpawnNameFull)) {
                entityToSpawn = (EntityLiving) EntityList.createEntityByName(entityToSpawnNameFull, parWorld);
                entityToSpawn.setLocationAndAngles(
                        parX,
                        parY,
                        parZ,
                        MathHelper.wrapAngleTo180_float(parWorld.rand.nextFloat() * 360.0F),
                        0.0F);
                parWorld.spawnEntityInWorld(entityToSpawn);
                entityToSpawn.onSpawnWithEgg((IEntityLivingData) null);
                entityToSpawn.playLivingSound();
            } else {
                // DEBUG
                System.out.println("Entity not found " + entityToSpawnName);
            }
        }

        return entityToSpawn;
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item aItem, CreativeTabs p_150895_2_, List aList) {
        for (int aMeta : mReverseEntityMap.values()) {
            aList.add(ItemUtils.simpleMetaStack(aItem, aMeta, 1));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack par1ItemStack, int parColorType) {
        int aID = par1ItemStack.getItemDamage();
        return (parColorType == 0) ? mColourBaseMap.get(aID) : mColourSpotsMap.get(aID);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    // Doing this override means that there is no localization for language
    // unless you specifically check for localization here and convert
    public String getItemStackDisplayName(ItemStack par1ItemStack) {
        return StatCollector.translateToLocalFormatted(
                "item.ItemCustomSpawnEgg.name",
                StatCollector
                        .translateToLocal("entity." + mEntityNameMap.get(par1ItemStack.getItemDamage()) + ".name"));
    }

    @Override
    public void registerIcons(final IIconRegister u) {
        mIconMap.put(0, u.registerIcon(GTPlusPlus.ID + ":" + "spawn_egg"));
        mIconMap.put(1, u.registerIcon(GTPlusPlus.ID + ":" + "spawn_egg_overlay"));
    }

    @Override
    public IIcon getIconFromDamageForRenderPass(final int damage, final int renderPass) {
        return mIconMap.get(renderPass);
    }

    @Override
    public IIcon getIconFromDamage(int damage) {
        return getIconFromDamageForRenderPass(0, 0);
    }

    @Override
    public IIcon getIcon(ItemStack aStack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
        return getIconFromDamageForRenderPass(0, renderPass);
    }

    @Override
    public IIcon getIcon(ItemStack aStack, int renderPass) {
        return getIconFromDamageForRenderPass(0, renderPass);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "." + stack.getItemDamage();
    }

    public static void setEntityToSpawnName(int aMetaID, String parEntityToSpawnName) {
        mEntityNameMap.put(aMetaID, parEntityToSpawnName);
        mEntityFullNameMap.put(aMetaID, GTPlusPlus.ID + "." + parEntityToSpawnName);
    }
}
