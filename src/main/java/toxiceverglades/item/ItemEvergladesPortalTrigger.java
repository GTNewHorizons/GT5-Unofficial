package toxiceverglades.item;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.util.Utils;
import toxiceverglades.dimension.DimensionEverglades;

public class ItemEvergladesPortalTrigger extends Item {

    public ItemEvergladesPortalTrigger() {
        super();
        this.maxStackSize = 1;
        setMaxDamage(64);
        setCreativeTab(CreativeTabs.tabTools);
        this.setTextureName(GTPlusPlus.ID + ":" + "itemAlkalusDisk");
    }

    @Override
    public Item setMaxStackSize(int int1) {
        return super.setMaxStackSize(1);
    }

    @Override
    public EnumRarity getRarity(ItemStack thisItem) {
        return EnumRarity.epic;
    }

    @Override
    public boolean hasEffect(ItemStack par1ItemStack, int pass) {
        return true;
    }

    @Override
    public String getItemStackDisplayName(final ItemStack p_77653_1_) {
        return StatCollector.translateToLocal("item.everglades.trigger.name");
    }

    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer player, final List<String> tooltip,
        final boolean adv) {
        tooltip.add(EnumChatFormatting.GREEN + StatCollector.translateToLocal("gtpp.tooltip.alkalus_disk.0"));
        tooltip.add(EnumChatFormatting.GREEN + StatCollector.translateToLocal("gtpp.tooltip.alkalus_disk.1"));
        super.addInformation(stack, player, tooltip, adv);
    }

    @Override
    public int getColorFromItemStack(final ItemStack stack, int HEX_OxFFFFFF) {
        return Utils.rgbtoHexValue(255, 128, 0);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
        float hitX, float hitY, float hitZ) {

        switch (side) {
            case 0 -> y--;
            case 1 -> y++;
            case 2 -> z--;
            case 3 -> z++;
            case 4 -> x--;
            case 5 -> x++;
            default -> {
                return false;
            }
        }

        if (!player.canPlayerEdit(x, y, z, side, stack) || !world.isAirBlock(x, y, z)) {
            return false;
        }

        if (world.isRemote) {
            return true;
        }

        world.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, "fire.ignite", 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);

        if (!DimensionEverglades.portalBlock.tryToCreatePortal(world, x, y, z)) {
            world.setBlock(x, y, z, ModBlocks.blockHellfire, 0, 3);
        }

        stack.damageItem(1, player);
        return true;
    }
}
