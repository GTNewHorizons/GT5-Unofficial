/*
 * KubaTech - Gregtech Addon
 * Copyright (C) 2022  kuba6000
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
 *
 */

package kubatech.loaders.item.items;

import java.util.LinkedList;
import java.util.List;
import kubatech.api.utils.ModUtils;
import kubatech.loaders.ItemLoader;
import kubatech.loaders.item.ItemProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.Achievement;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.AchievementPage;

public class Tea extends ItemProxy {
    protected static TeaPage teapage;
    protected static LinkedList<Achievement> achievements;
    protected Achievement achievement;
    private final String achievementname;

    public Tea(String unlocalizedName) {
        super("teacollection." + unlocalizedName, "teacollection/" + unlocalizedName);
        achievementname = "teacollection." + unlocalizedName;
    }

    private static final int[][] achievement_poses = new int[][] {
        {0, 0},
        {2, 0},
        {3, 1},
        {4, 2},
        {4, 4},
        {3, 5},
        {2, 6},
        {0, 6},
        {-1, 5},
        {-2, 4},
        {-2, 2},
        {-1, 1},
        {1, 3}
    };

    @Override
    public void ItemInit(int index) {
        super.ItemInit(index);
        if (teapage == null) {
            teapage = new TeaPage();
            AchievementPage.registerAchievementPage(teapage);
            achievements = teapage.getAchievementsOriginal();
        }
        achievements.add(
                achievement = new Achievement(
                                achievementname,
                                achievementname,
                                achievement_poses[index][0],
                                achievement_poses[index][1],
                                new ItemStack(ItemLoader.kubaitems, 1, index),
                                null)
                        .registerStat());
    }

    @Override
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {
        if (p_77624_1_.stackTagCompound != null
                && p_77624_1_.stackTagCompound.hasKey("TeaOwner")
                && !p_77624_1_
                        .stackTagCompound
                        .getString("TeaOwner")
                        .equals(p_77624_2_.getUniqueID().toString())) {
            p_77624_3_.add(EnumChatFormatting.GRAY + "" + EnumChatFormatting.BOLD + "" + EnumChatFormatting.ITALIC
                    + StatCollector.translateToLocal("item.notyours"));
            return;
        }
        p_77624_3_.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("item.fromcollection"));
        p_77624_3_.add(EnumChatFormatting.GRAY + "" + EnumChatFormatting.BOLD + "" + EnumChatFormatting.ITALIC + ""
                + EnumChatFormatting.UNDERLINE + StatCollector.translateToLocal("item.teacollection"));
    }

    @Override
    public EnumAction getItemUseAction(ItemStack p_77661_1_) {
        return EnumAction.drink;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
        if (p_77659_1_.stackTagCompound == null || !p_77659_1_.stackTagCompound.hasKey("TeaOwner")) return p_77659_1_;
        if (!p_77659_1_
                .stackTagCompound
                .getString("TeaOwner")
                .equals(p_77659_3_.getUniqueID().toString())) return p_77659_1_;
        p_77659_3_.setItemInUse(p_77659_1_, 32);
        return p_77659_1_;
    }

    @Override
    public ItemStack onEaten(ItemStack stack, World p_77654_2_, EntityPlayer p_77654_3_) {
        if (p_77654_2_.isRemote) return stack;
        if (!(p_77654_3_ instanceof EntityPlayerMP)) return stack;
        if (stack.stackTagCompound == null || !stack.stackTagCompound.hasKey("TeaOwner")) return stack;
        if (!stack.stackTagCompound
                .getString("TeaOwner")
                .equals(p_77654_3_.getUniqueID().toString())) return stack;
        p_77654_3_.addChatComponentMessage(new ChatComponentText(
                EnumChatFormatting.GREEN + StatCollector.translateToLocal("item.teacollection.mmm")));
        p_77654_3_.triggerAchievement(achievement);
        return stack;
    }

    @Override
    public int getMaxItemUseDuration() {
        return 32;
    }

    @Override
    public String getDisplayName(ItemStack stack) {
        if (!ModUtils.isClientSided) return super.getDisplayName(stack);
        if (stack.stackTagCompound == null
                || (!stack.stackTagCompound.hasKey("TeaOwner")
                        || stack.stackTagCompound
                                .getString("TeaOwner")
                                .equals(Minecraft.getMinecraft()
                                        .thePlayer
                                        .getUniqueID()
                                        .toString()))) return super.getDisplayName(stack);
        return EnumChatFormatting.GOLD + "" + EnumChatFormatting.BOLD + "" + EnumChatFormatting.ITALIC + "???????";
    }

    @Override
    public void onUpdate(
            ItemStack p_77663_1_, World p_77663_2_, Entity p_77663_3_, int p_77663_4_, boolean p_77663_5_) {
        if (p_77663_2_.isRemote) return;
        if (!(p_77663_3_ instanceof EntityPlayerMP)) return;
        NBTTagCompound tag = p_77663_1_.stackTagCompound;
        if (tag == null) tag = p_77663_1_.stackTagCompound = new NBTTagCompound();
        if (tag.hasKey("display")) tag.removeTag("display");
        if (tag.hasKey("TeaOwner")) return;
        tag.setString("TeaOwner", p_77663_3_.getPersistentID().toString());
    }

    private static class TeaPage extends AchievementPage {

        public TeaPage() {
            super("Tea");
        }

        LinkedList<Achievement> unlockedAchievements = new LinkedList<>();

        @Override
        public List<Achievement> getAchievements() {
            if (!ModUtils.isClientSided) return super.getAchievements();

            if (new Throwable().getStackTrace()[1].getMethodName().equals("isAchievementInPages"))
                return super.getAchievements(); // 5HEAD FIX

            EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
            unlockedAchievements.clear();
            for (Achievement achievement : achievements)
                if (player.getStatFileWriter().hasAchievementUnlocked(achievement))
                    unlockedAchievements.add(achievement);
            return unlockedAchievements;
        }

        private LinkedList<Achievement> getAchievementsOriginal() {
            return (LinkedList<Achievement>) super.getAchievements();
        }
    }
}
