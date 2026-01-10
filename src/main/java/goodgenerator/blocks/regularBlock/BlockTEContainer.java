package goodgenerator.blocks.regularBlock;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import goodgenerator.blocks.tileEntity.MTEEssentiaOutputHatch;
import goodgenerator.blocks.tileEntity.MTEEssentiaOutputHatchME;
import goodgenerator.main.GoodGenerator;
import gregtech.api.GregTechAPI;
import gregtech.api.util.GTUtility;

public class BlockTEContainer extends BlockContainer {

    @SideOnly(Side.CLIENT)
    protected IIcon[] texture;

    String[] textureNames;
    protected String name;

    // Index 0: Formerly Essentia Hatch, now removed
    // Index 1: Essentia Output Hatch
    // Index 2: Essentia Output Hatch (ME)
    // Index 3+: Unused
    protected int index;

    public BlockTEContainer(String name, String[] texture, CreativeTabs Tab) {
        super(Material.iron);
        this.setHardness(9.0F);
        this.setResistance(5.0F);
        this.name = name;
        this.textureNames = texture;
        this.setHarvestLevel("wrench", 2);
        this.setCreativeTab(GoodGenerator.GG);
        GregTechAPI.registerMachineBlock(this, -1);
    }

    public BlockTEContainer(String name, String[] texture, int index) {
        super(Material.iron);
        this.setHardness(9.0F);
        this.setResistance(5.0F);
        this.name = name;
        this.textureNames = texture;
        this.setHarvestLevel("wrench", 2);
        this.index = index;
        this.setCreativeTab(GoodGenerator.GG);
        GregTechAPI.registerMachineBlock(this, -1);
    }

    public BlockTEContainer(String name, String[] texture, Material material) {
        super(material);
        this.setHardness(9.0F);
        this.setResistance(5.0F);
        this.name = name;
        this.textureNames = texture;
        this.setHarvestLevel("wrench", 2);
        this.setCreativeTab(GoodGenerator.GG);
        GregTechAPI.registerMachineBlock(this, -1);
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
        if (GregTechAPI.isMachineBlock(this, aWorld.getBlockMetadata(aX, aY, aZ))) {
            GregTechAPI.causeMachineUpdate(aWorld, aX, aY, aZ);
        }
    }

    @Override
    public void breakBlock(World aWorld, int aX, int aY, int aZ, Block aBlock, int aMetaData) {
        if (GregTechAPI.isMachineBlock(this, aMetaData)) {
            GregTechAPI.causeMachineUpdate(aWorld, aX, aY, aZ);
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
            case 2:
                return new MTEEssentiaOutputHatch();
            case 3:
                return new MTEEssentiaOutputHatchME();
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
            if (index == 2) {
                if (tile instanceof MTEEssentiaOutputHatch && player.isSneaking()) {
                    ItemStack tItemStack = player.getHeldItem();
                    if (tItemStack == null) {
                        ((MTEEssentiaOutputHatch) tile).clear();
                        GTUtility.sendChatTrans(player, "essentiaoutputhatch.chat.0");
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

    @Override
    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn) {
        super.onBlockPlacedBy(worldIn, x, y, z, placer, itemIn);
        TileEntity te = worldIn.getTileEntity(x, y, z);
        if (te instanceof MTEEssentiaOutputHatchME hatchME) {
            hatchME.getProxy()
                .setOwner((EntityPlayer) placer);
        }

    }
}
