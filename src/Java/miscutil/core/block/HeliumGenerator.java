package miscutil.core.block;

import java.util.Random;

import miscutil.MiscUtils;
import miscutil.core.creative.AddToCreativeTab;
import miscutil.core.lib.CORE;
import miscutil.core.tileentities.TileEntityHeliumGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class HeliumGenerator extends BlockContainer {

    private IIcon top, sides, front;
    private Random randy = new Random();
    
    public HeliumGenerator(){
        super(Material.iron);
        setStepSound(Block.soundTypeMetal);
        setHardness(20.0F);
        setBlockName("helium_collector");
        setHarvestLevel("pickaxe", 3);
        setCreativeTab(AddToCreativeTab.tabMachines);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons (IIconRegister iconRegister)
    {
        this.top = iconRegister.registerIcon(CORE.MODID+":blockDefault");
        this.sides = iconRegister.registerIcon(CORE.MODID+":blockDefault");
        this.front = iconRegister.registerIcon(CORE.MODID+":blockDefault");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side){
        if(side == 1)
            return top;
        int facing = 2;
        TileEntityHeliumGenerator machine = (TileEntityHeliumGenerator)world.getTileEntity(x, y, z);
        if(machine != null)
            facing = machine.getFacing();
        if(side == facing)
            return front;
        else
            return sides;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon (int side, int metadata)
    {
        if(side == 1)
            return top;
        if(side == 3)
            return front;
        return sides;
    }

    @Override
    public boolean onBlockActivated (World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        if (world.isRemote)
        {
            return true;
        }
		player.openGui(MiscUtils.instance, 2, world, x, y, z);
		return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityHeliumGenerator();
    }

    @Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack item)
    {
        TileEntity tile = world.getTileEntity(x, y, z);
        if(tile instanceof TileEntityHeliumGenerator) {
            TileEntityHeliumGenerator machine = (TileEntityHeliumGenerator)tile;
            int l = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

            if (l == 0)
                machine.setFacing((short) 2);

            if (l == 1)
                machine.setFacing((short) 5);

            if (l == 2)
                machine.setFacing((short) 3);

            if (l == 3)
                machine.setFacing((short) 4);
        }

    }

    @Override
	public void breakBlock(World world, int x, int y, int z, Block block, int wut)
    {
            TileEntityHeliumGenerator collector = (TileEntityHeliumGenerator)world.getTileEntity(x, y, z);

            if (collector != null)
            {
                ItemStack itemstack = collector.getStackInSlot(0);

                if (itemstack != null)
                {
                    float f = this.randy.nextFloat() * 0.8F + 0.1F;
                    float f1 = this.randy.nextFloat() * 0.8F + 0.1F;
                    float f2 = this.randy.nextFloat() * 0.8F + 0.1F;

                    while (itemstack.stackSize > 0)
                    {
                        int j1 = this.randy.nextInt(21) + 10;

                        if (j1 > itemstack.stackSize)
                        {
                            j1 = itemstack.stackSize;
                        }

                        itemstack.stackSize -= j1;
                        EntityItem entityitem = new EntityItem(world, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

                        if (itemstack.hasTagCompound())
                        {
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                        }

                        float f3 = 0.05F;
                        entityitem.motionX = (double)((float)this.randy.nextGaussian() * f3);
                        entityitem.motionY = (double)((float)this.randy.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = (double)((float)this.randy.nextGaussian() * f3);
                        world.spawnEntityInWorld(entityitem);
                    }
                }

                world.func_147453_f(x, y, z, block);
            }

        super.breakBlock(world, x, y, z, block, wut);
    }
}
