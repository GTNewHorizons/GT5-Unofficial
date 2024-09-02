package tectech.thing.item;

import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.util.GTUtility;
import tectech.Reference;
import tectech.thing.CustomItemList;
import tectech.util.CommonValues;

public final class ItemTeslaStaff extends Item {

    public static ItemTeslaStaff INSTANCE;

    private ItemTeslaStaff() {
        setUnlocalizedName("tm.teslaStaff");
        setTextureName(Reference.MODID + ":itemTeslaStaff");
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer ep, List<String> aList, boolean boo) {
        aList.add(CommonValues.THETA_MOVEMENT);
        aList.add(translateToLocal("item.tm.teslaStaff.desc"));
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer aPlayer, Entity entity) {
        GTUtility.sendChatToPlayer(aPlayer, "Zapperoni!");
        if (!(aPlayer instanceof EntityPlayerMP)) {
            double aX = aPlayer.posX;
            double aY = aPlayer.posY;
            double aZ = aPlayer.posZ;
            GTUtility.doSoundAtClient(new ResourceLocation(Reference.MODID, "fx_scan"), 1, 1.0F, aX, aY, aZ);
        }
        return false;
    }

    public static void run() {
        INSTANCE = new ItemTeslaStaff();
        GameRegistry.registerItem(INSTANCE, INSTANCE.getUnlocalizedName());
        CustomItemList.teslaStaff.set(INSTANCE);
    }
}
