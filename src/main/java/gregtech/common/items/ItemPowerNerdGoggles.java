package gregtech.common.items;

import appeng.api.util.DimensionalCoord;
import gregtech.api.gui.modularui.GTUIInfos;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.common.handlers.PowerGogglesEventHandler;
import gregtech.common.handlers.PowerGogglesHudHandler;
import kekztech.common.tileentities.MTELapotronicSuperCapacitor;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import gregtech.api.items.GTGenericItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class ItemPowerNerdGoggles extends GTGenericItem implements IBauble {

    private static int ticks = 0;

    public ItemPowerNerdGoggles(String aUnlocalized, String aEnglish, String aEnglishTooltip) {
        super(aUnlocalized, aEnglish, aEnglishTooltip);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z,
                                  int ordinalSide, float hitX, float hitY, float hitZ) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if(tileEntity instanceof IGregTechTileEntity bla){
            if (bla.getMetaTileEntity() instanceof MTELapotronicSuperCapacitor lsc){
                if(player instanceof EntityClientPlayerMP mPlayer){
                    PowerGogglesEventHandler.lscLink = new DimensionalCoord(tileEntity);
                } else{

                }
                player.addChatMessage(new ChatComponentText("\"Linking\" the LSC..."));
            } else{
                player.addChatMessage(new ChatComponentText("Dude... That's not an LSC. Are you dumb?"));
            }
            return true;
        }
        return super.onItemUseFirst(stack,player,world,x,y,z,ordinalSide,hitX,hitY,hitZ);
    }
    @Override
    public BaubleType getBaubleType(ItemStack itemstack) {
        return BaubleType.UNIVERSAL;
    }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase player) {}

    @Override
    public void onEquipped(ItemStack itemstack, EntityLivingBase player) {

    }

    @Override
    public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {

    }

    @Override
    public boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

    @Override
    public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
        return true;
    }

}
