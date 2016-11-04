package gtPlusPlus.core.block.machine;

import java.util.ArrayList;

import gtPlusPlus.GTplusplus;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.tileentities.machines.TileEntityCharger;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import scala.util.Random;

public class Machine_Charger extends BlockContainer
{
    private static final String name = "Charging Machine";
 
    private final Random rand = new Random();
 
    public Machine_Charger(String unlocalizedName)
    {
        super(Material.iron);
        //GameRegistry.registerBlock(this, unlocalizedName);
        this.setBlockName(unlocalizedName);
        this.setBlockTextureName(CORE.MODID + ":" + unlocalizedName);
        this.setCreativeTab(AddToCreativeTab.tabMachines);
    }
 
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float lx, float ly, float lz)
    {
        if (world.isRemote) return true;
 
        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof TileEntityCharger)
        {
            player.openGui(GTplusplus.instance, 1, world, x, y, z);
            return true;
        }
        return false;
    }
 
    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int par6)
    {
        if (world.isRemote) return;
 
        ArrayList drops = new ArrayList();
 
        TileEntity teRaw = world.getTileEntity(x, y, z);
 
        if (teRaw != null && teRaw instanceof TileEntityCharger)
        {
        	TileEntityCharger te = (TileEntityCharger) teRaw;
 
            for (int i = 0; i < te.getSizeInventory(); i++)
            {
                ItemStack stack = te.getStackInSlot(i);
 
                if (stack != null) drops.add(stack.copy());
            }
        }
 
        for (int i = 0;i < drops.size();i++)
        {
            EntityItem item = new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, (ItemStack) drops.get(i));
            item.setVelocity((rand.nextDouble() - 0.5) * 0.25, rand.nextDouble() * 0.5 * 0.25, (rand.nextDouble() - 0.5) * 0.25);
            world.spawnEntityInWorld(item);
        }
    }
 
    @Override
	public TileEntity createNewTileEntity(World world, int par2)
    {
        return new TileEntityCharger();
    }
}