package gregtech.common.blocks;

import static gregtech.api.enums.Mods.NotEnoughItems;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.items.GTGenericBlock;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.render.GTRendererBlock;

public abstract class BlockOresAbstract extends GTGenericBlock implements ITileEntityProvider {

    private static final String DOT_NAME = ".name";
    private static final String DOT_TOOLTIP = ".tooltip";
    public static ThreadLocal<TileEntityOres> mTemporaryTileEntity = new ThreadLocal<>();
    public static boolean FUCKING_LOCK = false;
    public static boolean tHideOres;
    public static Set<Materials> aBlockedOres = new HashSet<>();

    protected BlockOresAbstract(String aUnlocalizedName, int aOreMetaCount, boolean aHideFirstMeta,
        Material aMaterial) {
        super(ItemOres.class, aUnlocalizedName, aMaterial);
        this.isBlockContainer = true;
        setStepSound(soundTypeStone);
        setCreativeTab(GregTechAPI.TAB_GREGTECH_ORES);
        tHideOres = NotEnoughItems.isModLoaded() && GTMod.gregtechproxy.mHideUnusedOres;
        if (aOreMetaCount > 8 || aOreMetaCount < 0) aOreMetaCount = 8;

        for (int i = 0; i < 16; i++) {
            GTModHandler.addValuableOre(this, i, 1);
        }
        for (int i = 1; i < GregTechAPI.sGeneratedMaterials.length; i++) {
            if (GregTechAPI.sGeneratedMaterials[i] != null) {
                for (int j = 0; j < aOreMetaCount; j++) {
                    if (!this.getEnabledMetas()[j]) continue;
                    GTLanguageManager.addStringLocalization(
                        getUnlocalizedName() + "." + (i + (j * 1000)) + DOT_NAME,
                        GTLanguageManager.i18nPlaceholder ? getLocalizedNameFormat(GregTechAPI.sGeneratedMaterials[i])
                            : getLocalizedName(GregTechAPI.sGeneratedMaterials[i]));
                    GTLanguageManager.addStringLocalization(
                        getUnlocalizedName() + "." + (i + (j * 1000)) + DOT_TOOLTIP,
                        GregTechAPI.sGeneratedMaterials[i].getToolTip());
                    GTLanguageManager.addStringLocalization(
                        getUnlocalizedName() + "." + ((i + 16000) + (j * 1000)) + DOT_NAME,
                        "Small " + (GTLanguageManager.i18nPlaceholder
                            ? getLocalizedNameFormat(GregTechAPI.sGeneratedMaterials[i])
                            : getLocalizedName(GregTechAPI.sGeneratedMaterials[i])));
                    GTLanguageManager.addStringLocalization(
                        getUnlocalizedName() + "." + ((i + 16000) + (j * 1000)) + DOT_TOOLTIP,
                        GregTechAPI.sGeneratedMaterials[i].getToolTip());
                    if ((GregTechAPI.sGeneratedMaterials[i].mTypes & 0x8) != 0
                        && !aBlockedOres.contains(GregTechAPI.sGeneratedMaterials[i])) {
                        GTOreDictUnificator.registerOre(
                            this.getProcessingPrefix()[j] != null
                                ? this.getProcessingPrefix()[j].get(GregTechAPI.sGeneratedMaterials[i])
                                : "",
                            new ItemStack(this, 1, i + (j * 1000)));
                        if (tHideOres) {
                            if (!(j == 0 && !aHideFirstMeta)) {
                                codechicken.nei.api.API.hideItem(new ItemStack(this, 1, i + (j * 1000)));
                            }
                            codechicken.nei.api.API.hideItem(new ItemStack(this, 1, (i + 16000) + (j * 1000)));
                        }
                    }
                }
            }
        }
    }

    public int getBaseBlockHarvestLevel(int aMeta) {
        return 0;
    }

    @Override
    public void onNeighborChange(IBlockAccess aWorld, int aX, int aY, int aZ, int aTileX, int aTileY, int aTileZ) {
        if (!FUCKING_LOCK) {
            FUCKING_LOCK = true;
            TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
            if ((tTileEntity instanceof TileEntityOres)) {
                ((TileEntityOres) tTileEntity).onUpdated();
            }
        }
        FUCKING_LOCK = false;
    }

    @Override
    public void onNeighborBlockChange(World aWorld, int aX, int aY, int aZ, Block aBlock) {
        if (!FUCKING_LOCK) {
            FUCKING_LOCK = true;
            TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
            if ((tTileEntity instanceof TileEntityOres)) {
                ((TileEntityOres) tTileEntity).onUpdated();
            }
        }
        FUCKING_LOCK = false;
    }

    public String getLocalizedNameFormat(Materials aMaterial) {
        return switch (aMaterial.mName) {
            case "InfusedAir", "InfusedDull", "InfusedEarth", "InfusedEntropy", "InfusedFire", "InfusedOrder", "InfusedVis", "InfusedWater" -> "%material Infused Stone";
            case "Vermiculite", "Bentonite", "Kaolinite", "Talc", "BasalticMineralSand", "GraniticMineralSand", "GlauconiteSand", "CassiteriteSand", "GarnetSand", "QuartzSand", "Pitchblende", "FullersEarth" -> "%material";
            default -> "%material" + OrePrefixes.ore.mLocalizedMaterialPost;
        };
    }

    public String getLocalizedName(Materials aMaterial) {
        return aMaterial.getDefaultLocalizedNameForItem(getLocalizedNameFormat(aMaterial));
    }

    @Override
    public boolean onBlockActivated(World aWorld, int aX, int aY, int aZ, EntityPlayer aPlayer, int ordinalSide,
        float aOffsetX, float aOffsetY, float aOffsetZ) {
        if (!aPlayer.isSneaking() || !aPlayer.capabilities.isCreativeMode) {
            return false;
        }

        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (!(tTileEntity instanceof TileEntityOres)) {
            return false;
        }

        boolean tNatural = (((TileEntityOres) tTileEntity).mNatural = !((TileEntityOres) tTileEntity).mNatural);
        GTUtility.sendChatToPlayer(aPlayer, "Ore \"mNatural\" flag set to: " + tNatural);
        return true;
    }

    @Override
    public boolean onBlockEventReceived(World p_149696_1_, int p_149696_2_, int p_149696_3_, int p_149696_4_,
        int p_149696_5_, int p_149696_6_) {
        super.onBlockEventReceived(p_149696_1_, p_149696_2_, p_149696_3_, p_149696_4_, p_149696_5_, p_149696_6_);
        TileEntity tileentity = p_149696_1_.getTileEntity(p_149696_2_, p_149696_3_, p_149696_4_);
        return tileentity != null && tileentity.receiveClientEvent(p_149696_5_, p_149696_6_);
    }

    @Override
    public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
        return (!(entity instanceof EntityDragon)) && (super.canEntityDestroy(world, x, y, z, entity));
    }

    @Override
    public String getHarvestTool(int aMeta) {
        return aMeta < 8 ? "pickaxe" : "shovel";
    }

    @Override
    public int getHarvestLevel(int aMeta) {
        return aMeta == 5 || aMeta == 6 ? 2 : aMeta % 8;
    }

    @Override
    public float getBlockHardness(World aWorld, int aX, int aY, int aZ) {
        return 1.0F + getHarvestLevel(aWorld.getBlockMetadata(aX, aY, aZ)) * 1.0F;
    }

    @Override
    public float getExplosionResistance(Entity entity, World aWorld, int aX, int aY, int aZ, double explosionX,
        double explosionY, double explosionZ) {
        return 1.0F + getHarvestLevel(aWorld.getBlockMetadata(aX, aY, aZ)) * 1.0F;
    }

    @Override
    protected boolean canSilkHarvest() {
        return false;
    }

    @Override
    public abstract String getUnlocalizedName();

    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal(getUnlocalizedName() + DOT_NAME);
    }

    @Override
    public int getRenderType() {
        if (GTRendererBlock.INSTANCE == null) {
            return super.getRenderType();
        }
        return GTRendererBlock.INSTANCE.mRenderID;
    }

    @Override
    public boolean canBeReplacedByLeaves(IBlockAccess aWorld, int aX, int aY, int aZ) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockAccess aWorld, int aX, int aY, int aZ) {
        return true;
    }

    @Override
    public boolean hasTileEntity(int aMeta) {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World aWorld, int aMeta) {
        return createTileEntity(aWorld, aMeta);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess aIBlockAccess, int aX, int aY, int aZ, int ordinalSide) {
        return Blocks.stone.getIcon(0, 0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return Blocks.stone.getIcon(0, 0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister aIconRegister) {}

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addHitEffects(World worldObj, MovingObjectPosition target, EffectRenderer effectRenderer) {
        GTRendererBlock
            .addHitEffects(effectRenderer, this, worldObj, target.blockX, target.blockY, target.blockZ, target.sideHit);
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer) {
        GTRendererBlock.addDestroyEffects(effectRenderer, this, world, x, y, z);
        return true;
    }

    @Override
    public int getDamageValue(World aWorld, int aX, int aY, int aZ) {
        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (((tTileEntity instanceof TileEntityOres))) {
            return ((TileEntityOres) tTileEntity).getMetaData();
        }
        return 0;
    }

    @Override
    public void breakBlock(World aWorld, int aX, int aY, int aZ, Block aBlock, int aMetadata) {
        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if ((tTileEntity instanceof TileEntityOres)) {
            mTemporaryTileEntity.set((TileEntityOres) tTileEntity);
        }
        super.breakBlock(aWorld, aX, aY, aZ, aBlock, aMetadata);
        aWorld.removeTileEntity(aX, aY, aZ);
    }

    public abstract OrePrefixes[] getProcessingPrefix(); // Must have 8 entries; an entry can be null to disable
                                                         // automatic recipes.

    public abstract boolean[] getEnabledMetas(); // Must have 8 entries.

    public abstract Block getDroppedBlock();

    public abstract Materials[] getDroppedDusts(); // Must have 8 entries; can be null.

    @Override
    public ArrayList<ItemStack> getDrops(World aWorld, int aX, int aY, int aZ, int aMeta, int aFortune) {
        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if ((tTileEntity instanceof TileEntityOres)) {
            return ((TileEntityOres) tTileEntity).getDrops(getDroppedBlock(), aFortune);
        }
        return mTemporaryTileEntity.get() == null ? new ArrayList<>()
            : mTemporaryTileEntity.get()
                .getDrops(getDroppedBlock(), aFortune);
    }

    @Override
    public TileEntity createTileEntity(World aWorld, int aMeta) {
        return new TileEntityOres();
    }

    public abstract ITexture[] getTextureSet(); // Must have 16 entries.

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item aItem, CreativeTabs aTab, List<ItemStack> aList) {
        for (int i = 0; i < GregTechAPI.sGeneratedMaterials.length; i++) {
            Materials tMaterial = GregTechAPI.sGeneratedMaterials[i];
            if ((tMaterial != null) && ((tMaterial.mTypes & 0x8) != 0) && !aBlockedOres.contains(tMaterial)) {
                for (int meta = i; meta < 23000 + i; meta += 1000) {
                    if (!(new ItemStack(aItem, 1, meta).getDisplayName()
                        .contains(DOT_NAME))) aList.add(new ItemStack(aItem, 1, meta));
                }
            }
        }
    }
}
