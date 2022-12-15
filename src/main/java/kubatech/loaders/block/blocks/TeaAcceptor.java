package kubatech.loaders.block.blocks;

import java.util.List;
import kubatech.loaders.block.BlockProxy;
import kubatech.loaders.block.IProxyTileEntityProvider;
import kubatech.tileentity.TeaAcceptorTile;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TeaAcceptor extends BlockProxy implements IProxyTileEntityProvider {

    public TeaAcceptor() {
        super("tea_acceptor", "tea_acceptor");
    }

    @Override
    public TileEntity createTileEntity(World world) {
        return new TeaAcceptorTile();
    }

    @Override
    public void onBlockPlaced(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
        if (world.isRemote) return;
        if (!(player instanceof EntityPlayerMP)) return;
        ((TeaAcceptorTile) world.getTileEntity(x, y, z)).setTeaOwner(player.getCommandSenderName());
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer entity, List<String> tooltipList, boolean showDebugInfo) {
        tooltipList.add("Accepts Tea items and adds them to your network");
        tooltipList.add("Can accept up to 10 stacks per tick");
    }

    @Override
    public float getResistance() {
        return 999999999999.f;
    }
}
