package goodgenerator.blocks.regularBlock;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import goodgenerator.blocks.tileEntity.EssentiaHatch;
import goodgenerator.blocks.tileEntity.EssentiaOutputHatch;
import goodgenerator.blocks.tileEntity.EssentiaOutputHatch_ME;
import goodgenerator.main.GoodGenerator;
import gregtech.api.GregTech_API;
import gregtech.api.util.GT_Utility;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IEssentiaContainerItem;

public class TEBlock extends BlockContainer {

    @SideOnly(Side.CLIENT)
    protected IIcon[] texture;

    String[] textureNames;
    protected String name;
    protected int index;

    public TEBlock(String name, String[] texture, CreativeTabs Tab) {
        super(Material.iron);
        this.setHardness(9.0F);
        this.setResistance(5.0F);
        this.name = name;
        this.textureNames = texture;
        this.setHarvestLevel("wrench", 2);
        this.setCreativeTab(GoodGenerator.GG);
        GregTech_API.registerMachineBlock(this, -1);
    }

    public TEBlock(String name, String[] texture, int index) {
        super(Material.iron);
        this.setHardness(9.0F);
        this.setResistance(5.0F);
        this.name = name;
        this.textureNames = texture;
        this.setHarvestLevel("wrench", 2);
        this.index = index;
        this.setCreativeTab(GoodGenerator.GG);
        GregTech_API.registerMachineBlock(this, -1);
    }

    public TEBlock(String name, String[] texture, Material material) {
        super(material);
        this.setHardness(9.0F);
        this.setResistance(5.0F);
        this.name = name;
        this.textureNames = texture;
        this.setHarvestLevel("wrench", 2);
        this.setCreativeTab(GoodGenerator.GG);
        GregTech_API.registerMachineBlock(this, -1);
    }

    public int getIndex() {
        return this.index;
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return meta < this.texture.length ? this.texture[meta] : this.texture[0];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        this.texture = new IIcon[this.textureNames.length];
        for (int i = 0; i < this.textureNames.length; i++) {
            this.texture[i] = par1IconRegister.registerIcon(this.textureNames[i]);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < this.textureNames.length; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public void onBlockAdded(World aWorld, int aX, int aY, int aZ) {
        if (GregTech_API.isMachineBlock(this, aWorld.getBlockMetadata(aX, aY, aZ))) {
            GregTech_API.causeMachineUpdate(aWorld, aX, aY, aZ);
        }
    }

    @Override
    public void breakBlock(World aWorld, int aX, int aY, int aZ, Block aBlock, int aMetaData) {
        if (GregTech_API.isMachineBlock(this, aMetaData)) {
            GregTech_API.causeMachineUpdate(aWorld, aX, aY, aZ);
        }
        aWorld.removeTileEntity(aX, aY, aZ);
    }

    @Override
    public String getUnlocalizedName() {
        return this.name;
    }

    @Override
    public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
        return false;
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public TileEntity createTileEntity(World world, int meta) {
        switch (index) {
            case 1:
                return new EssentiaHatch();
            case 2:
                return new EssentiaOutputHatch();
            case 3:
                return new EssentiaOutputHatch_ME();
            default:
                return null;
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7,
        float par8, float par9) {
        if (world.isRemote) {
            return false;
        } else {
            TileEntity tile = world.getTileEntity(x, y, z);
            if (index == 1) {
                if (tile instanceof EssentiaHatch) {
                    ItemStack tItemStack = player.getHeldItem();
                    if (tItemStack != null) {
                        Item tItem = tItemStack.getItem();
                        if (tItem instanceof IEssentiaContainerItem
                            && ((IEssentiaContainerItem) tItem).getAspects(player.getHeldItem()) != null
                            && ((IEssentiaContainerItem) tItem).getAspects(player.getHeldItem())
                                .size() > 0) {
                            Aspect tLocked = ((IEssentiaContainerItem) tItem).getAspects(player.getHeldItem())
                                .getAspects()[0];
                            ((EssentiaHatch) tile).setLockedAspect(tLocked);
                            GT_Utility.sendChatToPlayer(
                                player,
                                String.format(
                                    StatCollector.translateToLocal("essentiahatch.chat.0"),
                                    tLocked.getLocalizedDescription()));
                        }
                    } else {
                        ((EssentiaHatch) tile).setLockedAspect(null);
                        GT_Utility.sendChatToPlayer(player, StatCollector.translateToLocal("essentiahatch.chat.1"));
                    }
                    world.markBlockForUpdate(x, y, z);
                    return true;
                } else return false;
            } else if (index == 2) {
                if (tile instanceof EssentiaOutputHatch && player.isSneaking()) {
                    ItemStack tItemStack = player.getHeldItem();
                    if (tItemStack == null) {
                        ((EssentiaOutputHatch) tile).clear();
                        GT_Utility
                            .sendChatToPlayer(player, StatCollector.translateToLocal("essentiaoutputhatch.chat.0"));
                    }
                    return true;
                } else return false;
            } else return false;
        }
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return null;
    }
}
