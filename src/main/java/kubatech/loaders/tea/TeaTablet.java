package kubatech.loaders.tea;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.factory.GuiFactories;
import com.cleanroommc.modularui.factory.PlayerInventoryGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.TextWidget;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class TeaTablet extends Item implements IGuiHolder<PlayerInventoryGuiData> {

    public TeaTablet(){

    }

    @Override
    public void registerIcons(IIconRegister register) {
        this.itemIcon = register.registerIcon("kubatech:tea/tea_tablet");
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack);
    }

    @Override
    public String getItemStackDisplayName(ItemStack p_77653_1_) {
        return super.getItemStackDisplayName(p_77653_1_);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer player) {
        if (!worldIn.isRemote) {
            GuiFactories.playerInventory().openFromMainHand(player);
        }
        return itemStackIn;
    }

    @Override
    public ModularPanel buildUI(PlayerInventoryGuiData data, PanelSyncManager syncManager, UISettings settings) {
        ItemStack stack = data.getUsedItemStack();
        ModularPanel panel = ModularPanel.defaultPanel("tea_tablet");
        panel.child(new TextWidget<>(IKey.dynamic()));
        panel.bindPlayerInventory();
        return panel;
    }
}
