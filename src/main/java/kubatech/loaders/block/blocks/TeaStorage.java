package kubatech.loaders.block.blocks;

import static kubatech.api.Variables.numberFormat;

import java.util.List;

import kubatech.loaders.block.BlockProxy;
import kubatech.loaders.block.IProxyTileEntityProvider;
import kubatech.tileentity.TeaStorageTile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class TeaStorage extends BlockProxy implements IProxyTileEntityProvider {

    public TeaStorage() {
        super("tea_storage", "tea_storage");
    }

    @Override
    public TileEntity createTileEntity(World world) {
        return new TeaStorageTile();
    }

    @Override
    public void onBlockPlaced(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
        if (world.isRemote) return;
        if (!(player instanceof EntityPlayerMP)) return;
        ((TeaStorageTile) world.getTileEntity(x, y, z)).setTeaOwner(player.getPersistentID());
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer entity, List<String> tooltipList, boolean showDebugInfo) {
        tooltipList.add("Extends Tea Storage by " + EnumChatFormatting.RED + numberFormat.format(Long.MAX_VALUE));
    }

    @Override
    public float getResistance() {
        return 999999999999.f;
    }
}
