/*
 * spotless:off
 * KubaTech - Gregtech Addon
 * Copyright (C) 2022 - 2023  kuba6000
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <https://www.gnu.org/licenses/>.
 * spotless:on
 */

package kubatech.loaders.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.gtnewhorizons.modularui.api.screen.ModularUIContext;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.builder.UIBuilder;
import com.gtnewhorizons.modularui.common.builder.UIInfo;
import com.gtnewhorizons.modularui.common.internal.wrapper.ModularGui;
import com.gtnewhorizons.modularui.common.internal.wrapper.ModularUIContainer;

import kubatech.Tags;

public class ItemProxy {

    private static final UIInfo<?, ?> HeldItemUIInfo = UIBuilder.of()
        .container((player, w, x, y, z) -> {
            ItemStack stack = player.getHeldItem();
            ItemProxy proxy = KubaItems.getItemProxy(stack);
            if (!(proxy instanceof IItemProxyGUI)) return null;
            UIBuildContext context = new UIBuildContext(player);
            ModularWindow window = ((IItemProxyGUI) proxy).createWindow(stack, player);
            return new ModularUIContainer(
                new ModularUIContext(context, () -> player.inventoryContainer.detectAndSendChanges()),
                window);
        })
        .gui((player, w, x, y, z) -> {
            ItemStack stack = player.getHeldItem();
            ItemProxy proxy = KubaItems.getItemProxy(stack);
            if (!(proxy instanceof IItemProxyGUI)) return null;
            UIBuildContext context = new UIBuildContext(player);
            ModularWindow window = ((IItemProxyGUI) proxy).createWindow(stack, player);
            return new ModularGui(new ModularUIContainer(new ModularUIContext(context, null), window));
        })
        .build();
    private final String unlocalizedName;
    private final String texturepath;
    private IIcon icon;

    public ItemProxy(String unlocalizedName, String texture) {
        this.unlocalizedName = "kubaitem." + unlocalizedName;
        texturepath = Tags.MODID + ":" + texture;
    }

    public ItemProxy(String unlocalizedNameAndTexture) {
        this(unlocalizedNameAndTexture, unlocalizedNameAndTexture);
    }

    public void ItemInit(int index) {}

    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    public String getDisplayName(ItemStack stack) {
        return StatCollector.translateToLocal(this.unlocalizedName + ".name")
            .trim();
    }

    public void registerIcon(IIconRegister iconRegister) {
        icon = iconRegister.registerIcon(texturepath);
    }

    public IIcon getIcon() {
        return icon;
    }

    public void addInformation(ItemStack stack, EntityPlayer entity, List<String> tooltipList, boolean showDebugInfo) {}

    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isCurrentItem) {}

    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.none;
    }

    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer entity) {
        return stack;
    }

    public ItemStack onEaten(ItemStack stack, World world, EntityPlayer entity) {
        return stack;
    }

    public int getMaxItemUseDuration() {
        return 0;
    }

    public static void openHeldItemGUI(EntityPlayer player) {
        ItemStack stack = player.getHeldItem();
        if (KubaItems.getItemProxy(stack) instanceof IItemProxyGUI) HeldItemUIInfo.open(player);
    }
}
