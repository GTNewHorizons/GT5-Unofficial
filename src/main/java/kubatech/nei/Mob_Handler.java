/*
 * KubaTech - Gregtech Addon Copyright (C) 2022 - 2023 kuba6000 This library is free software; you can redistribute it
 * and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later version. This library is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details. You should have
 * received a copy of the GNU Lesser General Public License along with this library. If not, see
 * <https://www.gnu.org/licenses/>.
 */

package kubatech.nei;

import static kubatech.nei.Mob_Handler.Translations.*;

import java.awt.*;
import java.lang.reflect.Field;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import kubatech.Tags;
import kubatech.api.LoaderReference;
import kubatech.api.helpers.InfernalHelper;
import kubatech.api.mobhandler.MobDrop;
import kubatech.api.utils.FastRandom;
import kubatech.api.utils.MobUtils;
import kubatech.api.utils.ModUtils;
import kubatech.config.Config;
import kubatech.kubatech;
import kubatech.loaders.MobRecipeLoader;
import kubatech.tileentity.gregtech.multiblock.GT_MetaTileEntity_ExtremeExterminationChamber;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.*;
import cpw.mods.fml.common.event.FMLInterModComms;
import crazypants.enderio.EnderIO;
import crazypants.enderio.machine.spawner.BlockPoweredSpawner;
import gregtech.api.util.GT_Utility;

public class Mob_Handler extends TemplateRecipeHandler {

    enum Translations {

        NORMAL_DROPS,
        RARE_DROPS,
        ADDITIONAL_DROPS,
        INFERNAL_DROPS,
        INFERNAL_CANNOT,
        INFERNAL_CAN,
        INFERNAL_ALWAYS,
        CANNOT_USE_VIAL,
        CHANCE,
        AVERAGE_REMINDER,
        MOD,
        MAX_HEALTH,
        BOSS,
        LOOTABLE,
        PLAYER_ONLY,
        EEC_CHANCE,;

        final String key;

        Translations() {
            key = "mobhandler." + this.name()
                .toLowerCase();
        }

        public String get() {
            return StatCollector.translateToLocal(key);
        }

        public String get(Object... args) {
            return StatCollector.translateToLocalFormatted(key, args);
        }

        public String getKey() {
            return key;
        }

        @Override
        public String toString() {
            return get();
        }
    }

    private static final Logger LOG = LogManager.getLogger(Tags.MODID + "[Mob Handler]");
    private static final Mob_Handler instance = new Mob_Handler();
    private static final List<MobCachedRecipe> cachedRecipes = new ArrayList<>();
    public static int cycleTicksStatic = Math.abs((int) System.currentTimeMillis());
    private static final int itemsPerRow = 8, itemXShift = 18, itemYShift = 18, nextRowYShift = 35;

    public static void addRecipe(EntityLiving e, List<MobDrop> drop) {
        List<MobPositionedStack> positionedStacks = new ArrayList<>();
        int xorigin = 7, xoffset = xorigin, yoffset = 95, normaldrops = 0, raredrops = 0, additionaldrops = 0,
            infernaldrops = 0;
        MobDrop.DropType i = null;
        for (MobDrop d : drop) {
            if (i == d.type) {
                xoffset += itemXShift;
                if (xoffset >= xorigin + (itemXShift * itemsPerRow)) {
                    xoffset = xorigin;
                    yoffset += itemYShift;
                }
            }
            if (i != null && i != d.type) {
                xoffset = xorigin;
                yoffset += nextRowYShift;
            }
            i = d.type;
            if (d.type == MobDrop.DropType.Normal) normaldrops++;
            else if (d.type == MobDrop.DropType.Rare) raredrops++;
            else if (d.type == MobDrop.DropType.Additional) additionaldrops++;
            else if (d.type == MobDrop.DropType.Infernal) break; // dont render infernal drops
            positionedStacks.add(
                new MobPositionedStack(
                    d.stack.copy(),
                    xoffset,
                    yoffset,
                    d.type,
                    d.chance,
                    d.enchantable,
                    d.damages != null ? new ArrayList<>(d.damages.keySet()) : null,
                    d.lootable,
                    d.playerOnly));
        }
        instance.addRecipeInt(e, positionedStacks, normaldrops, raredrops, additionaldrops, infernaldrops);
    }

    private void addRecipeInt(EntityLiving e, List<Mob_Handler.MobPositionedStack> l, int normaldrops, int raredrops,
        int additionaldrops, int infernalDrops) {
        cachedRecipes.add(new MobCachedRecipe(e, l, normaldrops, raredrops, additionaldrops, infernalDrops));
    }

    public static void clearRecipes() {
        cachedRecipes.clear();
    }

    public static void sortCachedRecipes() {
        cachedRecipes.sort((o1, o2) -> {
            boolean m1 = o1.mod.equals("Minecraft");
            boolean m2 = o2.mod.equals("Minecraft");
            if (m1 && !m2) return -1;
            else if (!m1 && m2) return 1;
            if (!o1.mod.equals(o2.mod)) return o1.mod.compareTo(o2.mod);
            else return o1.localizedName.compareTo(o2.localizedName);
        });
    }

    public Mob_Handler() {
        this.transferRects.add(new RecipeTransferRect(new Rectangle(7, 62, 16, 16), getOverlayIdentifier()));
        if (!NEI_Config.isAdded) {
            FMLInterModComms.sendRuntimeMessage(
                kubatech.instance,
                "NEIPlugins",
                "register-crafting-handler",
                "kubatech@" + getRecipeName() + "@" + getOverlayIdentifier());
            GuiCraftingRecipe.craftinghandlers.add(this);
            GuiUsageRecipe.usagehandlers.add(this);
        }
    }

    @Override
    public TemplateRecipeHandler newInstance() {
        return new Mob_Handler();
    }

    @Override
    public String getOverlayIdentifier() {
        return "kubatech.mobhandler";
    }

    @Override
    public String getGuiTexture() {
        return "kubatech:textures/gui/MobHandler.png";
    }

    private static final Field mainmodelfield;

    static {
        try {
            mainmodelfield = RendererLivingEntity.class
                .getDeclaredField(ModUtils.isDeobfuscatedEnvironment ? "mainModel" : "field_77045_g");
            mainmodelfield.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void drawBackground(int recipe) {
        GL11.glColor4f(1f, 1f, 1f, 1f);
        GuiDraw.changeTexture(getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, 0, 0, 168, 192);

        MobCachedRecipe currentrecipe = ((MobCachedRecipe) arecipes.get(recipe));

        {
            int x = 6, y = 94, yshift = nextRowYShift;
            if (currentrecipe.normalOutputsCount > 0) {
                for (int i = 0; i < ((currentrecipe.normalOutputsCount - 1) / itemsPerRow) + 1; i++) {
                    GuiDraw.drawTexturedModalRect(x, y + (18 * i), 0, 192, 144, 18);
                    if (i > 0) GuiDraw.drawTexturedModalRect(x, y + ((18 * i) - 1), 0, 193, 144, 2);
                }
                y += yshift + ((currentrecipe.normalOutputsCount - 1) / itemsPerRow) * 18;
            }
            if (currentrecipe.rareOutputsCount > 0) {
                for (int i = 0; i < ((currentrecipe.rareOutputsCount - 1) / itemsPerRow) + 1; i++) {
                    GuiDraw.drawTexturedModalRect(x, y + (18 * i), 0, 192, 144, 18);
                    if (i > 0) GuiDraw.drawTexturedModalRect(x, y + ((18 * i) - 1), 0, 193, 144, 2);
                }
                y += yshift + ((currentrecipe.rareOutputsCount - 1) / itemsPerRow) * 18;
            }
            if (currentrecipe.additionalOutputsCount > 0) {
                for (int i = 0; i < ((currentrecipe.additionalOutputsCount - 1) / itemsPerRow) + 1; i++) {
                    GuiDraw.drawTexturedModalRect(x, y + (18 * i), 0, 192, 144, 18);
                    if (i > 0) GuiDraw.drawTexturedModalRect(x, y + ((18 * i) - 1), 0, 193, 144, 2);
                }
                y += yshift + ((currentrecipe.additionalOutputsCount - 1) / itemsPerRow) * 18;
            }
            if (currentrecipe.infernalOutputsCount > 0) {
                for (int i = 0; i < ((currentrecipe.infernalOutputsCount - 1) / itemsPerRow) + 1; i++) {
                    GuiDraw.drawTexturedModalRect(x, y + (18 * i), 0, 192, 144, 18);
                    if (i > 0) GuiDraw.drawTexturedModalRect(x, y + ((18 * i) - 1), 0, 193, 144, 2);
                }
            }
        }

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glColor4f(1f, 1f, 1f, 1f);

        Minecraft mc = Minecraft.getMinecraft();

        ScaledResolution scale = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);

        int width = scale.getScaledWidth();
        int height = scale.getScaledHeight();
        int mouseX = Mouse.getX() * width / mc.displayWidth;
        int mouseZ = height - Mouse.getY() * height / mc.displayHeight - 1;

        // Get current x,y from matrix
        FloatBuffer buf = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, buf);
        float x = buf.get(12);
        float y = buf.get(13);

        int stackdepth = GL11.glGetInteger(GL11.GL_MODELVIEW_STACK_DEPTH);

        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);

        GL11.glPushMatrix();

        float healthScale = BossStatus.healthScale;
        int statusBarTime = BossStatus.statusBarTime;
        String bossName = BossStatus.bossName;
        boolean hasColorModifier = BossStatus.hasColorModifier;

        BossStatus.statusBarTime = 0;

        try {
            EntityLiving e = currentrecipe.mob;

            float eheight = MobUtils.getMobHeight(e);
            float scaled = MobUtils.getDesiredScale(eheight, 27);
            //
            // int maxwidth = 15;
            // scaled = (int) Math.min(scaled, maxwidth / ewidth);

            int mobx = 30, moby = 50;
            e.setPosition(mc.thePlayer.posX + 5, mc.thePlayer.posY, mc.thePlayer.posZ);

            // ARGS: x, y, scale, rot, rot, entity
            GuiInventory.func_147046_a(
                mobx,
                moby,
                Math.round(scaled),
                (x + mobx) - mouseX,
                y + moby - eheight * scaled - mouseZ,
                e);
        } catch (Throwable ex) {
            Tessellator tes = Tessellator.instance;
            try {
                tes.draw();
            } catch (Exception ignored) {}
        }

        if (BossStatus.statusBarTime > 0 && currentrecipe.isBoss.isEmpty()) currentrecipe.isBoss = BossStatus.bossName;

        BossStatus.healthScale = healthScale;
        BossStatus.statusBarTime = statusBarTime;
        BossStatus.bossName = bossName;
        BossStatus.hasColorModifier = hasColorModifier;

        GL11.glMatrixMode(GL11.GL_MODELVIEW_MATRIX);
        stackdepth -= GL11.glGetInteger(GL11.GL_MODELVIEW_STACK_DEPTH);
        if (stackdepth < 0) for (; stackdepth < 0; stackdepth++) GL11.glPopMatrix();
        if (stackdepth > 0) for (; stackdepth > 0; stackdepth--) GL11.glPushMatrix();

        GL11.glPopAttrib();

        int err;
        while ((err = GL11.glGetError()) != GL11.GL_NO_ERROR) if (Config.Debug.showRenderErrors)
            LOG.error(currentrecipe.mobname + " | GL ERROR: " + err + " / " + GLU.gluErrorString(err));

        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }

    @Override
    public void drawForeground(int recipe) {
        MobCachedRecipe currentrecipe = ((MobCachedRecipe) arecipes.get(recipe));
        int y = 7, yshift = 10, x = 57;
        GuiDraw.drawString(currentrecipe.localizedName, x, y, 0xFF555555, false);
        if (Minecraft.getMinecraft().gameSettings.advancedItemTooltips && NEIClientUtils.shiftKey())
            GuiDraw.drawString(currentrecipe.mobname, x, y += yshift, 0xFF555555, false);
        GuiDraw.drawString(MOD.get() + currentrecipe.mod, x, y += yshift, 0xFF555555, false);
        GuiDraw.drawString(MAX_HEALTH.get() + currentrecipe.maxHealth, x, y += yshift, 0xFF555555, false);
        switch (currentrecipe.infernaltype) {
            case -1:
                break;
            case 0:
                GuiDraw.drawString(INFERNAL_CANNOT.get(), x, y += yshift, 0xFF555555, false);
                break;
            case 1:
                GuiDraw.drawString(INFERNAL_CAN.get(), x, y += yshift, 0xFFFF0000, false);
                break;
            case 2:
                GuiDraw.drawString(INFERNAL_ALWAYS.get(), x, y += yshift, 0xFFFF0000, false);
                break;
        }

        if (!currentrecipe.isBoss.isEmpty())
            GuiDraw.drawString(EnumChatFormatting.BOLD + "" + BOSS.get(), x, y += yshift, 0xFFD68F00, false);

        MobRecipeLoader.MobRecipe MBRecipe = GT_MetaTileEntity_ExtremeExterminationChamber.MobNameToRecipeMap
            .get(currentrecipe.mobname);
        if (MBRecipe != null) {
            if (MBRecipe.isUsable) {
                GuiDraw.drawString(
                    GT_Utility.trans("153", "Usage: ") + GT_Utility.formatNumbers(MBRecipe.mEUt) + " EU/t",
                    x,
                    y += yshift,
                    0xFF555555,
                    false);
                GuiDraw.drawString(
                    GT_Utility.trans("158", "Time: ") + GT_Utility.formatNumbers(MBRecipe.mDuration / 20d) + " secs",
                    x,
                    y += yshift,
                    0xFF555555,
                    false);
            } else {
                GuiDraw.drawString(CANNOT_USE_VIAL.get(), x, y += yshift, 0xFF555555, false);
            }
        }

        x = 6;
        y = 83;
        yshift = nextRowYShift;
        if (currentrecipe.normalOutputsCount > 0) {
            GuiDraw.drawString(NORMAL_DROPS.get(), x, y, 0xFF555555, false);
            y += yshift + ((currentrecipe.normalOutputsCount - 1) / itemsPerRow) * 18;
        }
        if (currentrecipe.rareOutputsCount > 0) {
            GuiDraw.drawString(RARE_DROPS.get(), x, y, 0xFF555555, false);
            y += yshift + ((currentrecipe.rareOutputsCount - 1) / itemsPerRow) * 18;
        }
        if (currentrecipe.additionalOutputsCount > 0) {
            GuiDraw.drawString(ADDITIONAL_DROPS.get(), x, y, 0xFF555555, false);
            y += yshift + ((currentrecipe.additionalOutputsCount - 1) / itemsPerRow) * 18;
        }
        if (currentrecipe.infernalOutputsCount > 0) {
            GuiDraw.drawString(INFERNAL_DROPS.get(), x, y, 0xFF555555, false);
            y += yshift + ((currentrecipe.additionalOutputsCount - 1) / itemsPerRow) * 18;
        }
        yshift = 10;
    }

    @Override
    public String getRecipeName() {
        return "Mob Drops";
    }

    @Override
    public IUsageHandler getUsageAndCatalystHandler(String inputId, Object... ingredients) {
        if (inputId.equals("item")) {
            TemplateRecipeHandler handler = newInstance();
            ItemStack candidate = (ItemStack) ingredients[0];
            if (RecipeCatalysts.containsCatalyst(handler, candidate)) {
                handler.loadCraftingRecipes(getOverlayIdentifier(), (Object) null);
                return handler;
            }
        }
        return this.getUsageHandler(inputId, ingredients);
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(getOverlayIdentifier())) {
            arecipes.addAll(cachedRecipes);
            return;
        }
        super.loadCraftingRecipes(outputId, results);
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        for (MobCachedRecipe r : cachedRecipes) if (r.contains(r.mOutputs, result)) arecipes.add(r);
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        if (LoaderReference.EnderIO && ingredient.getItem() == Item.getItemFromBlock(EnderIO.blockPoweredSpawner)) {
            if (!ingredient.hasTagCompound() || !ingredient.getTagCompound()
                .hasKey("mobType")) {
                loadCraftingRecipes(getOverlayIdentifier(), (Object) null);
                return;
            }
            for (MobCachedRecipe r : cachedRecipes) if (r.mInput.stream()
                .anyMatch(
                    s -> s.getItem() == ingredient.getItem() && Objects.equals(
                        s.getTagCompound()
                            .getString("mobType"),
                        ingredient.getTagCompound()
                            .getString("mobType"))))
                arecipes.add(r);
        } else for (MobCachedRecipe r : cachedRecipes) if (r.mInput.stream()
            .anyMatch(ingredient::isItemEqual)) arecipes.add(r);
    }

    @Override
    public void onUpdate() {
        cycleTicksStatic++;
    }

    @Override
    public List<String> handleItemTooltip(GuiRecipe<?> gui, ItemStack stack, List<String> currenttip, int recipe) {
        MobCachedRecipe currentrecipe = ((MobCachedRecipe) arecipes.get(recipe));
        PositionedStack positionedStack = currentrecipe.mOutputs.stream()
            .filter(ps -> ps.item == stack)
            .findFirst()
            .orElse(null);
        if (positionedStack instanceof MobPositionedStack)
            currenttip.addAll(((MobPositionedStack) positionedStack).extraTooltip);
        return currenttip;
    }

    public static class MobPositionedStack extends PositionedStack {

        public final MobDrop.DropType type;
        public final int chance;
        public final boolean enchantable;
        public final boolean randomdamage;
        public final List<Integer> damages;
        public final int enchantmentLevel;
        private final Random rand;
        public final List<String> extraTooltip;

        public MobPositionedStack(Object object, int x, int y, MobDrop.DropType type, int chance, Integer enchantable,
            List<Integer> damages, boolean lootable, boolean isPlayerOnly) {
            super(object, x, y, false);
            rand = new FastRandom();
            this.type = type;
            this.chance = chance;
            this.enchantable = enchantable != null;
            if (this.enchantable) enchantmentLevel = enchantable;
            else enchantmentLevel = 0;
            this.randomdamage = damages != null;
            if (this.randomdamage) this.damages = damages;
            else this.damages = null;
            extraTooltip = new ArrayList<>();

            if (chance != 10000) extraTooltip.add(EnumChatFormatting.RESET + CHANCE.get((double) chance / 100d));
            if (lootable) extraTooltip.add(EnumChatFormatting.RESET + LOOTABLE.get());
            if (isPlayerOnly) {
                extraTooltip.add(EnumChatFormatting.RESET + PLAYER_ONLY.get());
                extraTooltip.add(
                    EnumChatFormatting.RESET
                        + EEC_CHANCE.get(((double) chance / 100d) * Config.MobHandler.playerOnlyDropsModifier));
            }
            extraTooltip.add(EnumChatFormatting.RESET + AVERAGE_REMINDER.get());

            setPermutationToRender(0);
        }

        @Override
        public void setPermutationToRender(int index) {
            if (this.item == null) this.item = this.items[0].copy();
            if (enchantable) {
                if (this.item.getItem() == Items.enchanted_book) this.item = this.items[0].copy();
                if (this.item.hasTagCompound()) this.item.getTagCompound()
                    .removeTag("ench");
                EnchantmentHelper.addRandomEnchantment(rand, this.item, enchantmentLevel);
            }
            if (randomdamage) this.item.setItemDamage(damages.get(rand.nextInt(damages.size())));
        }
    }

    private class MobCachedRecipe extends TemplateRecipeHandler.CachedRecipe {

        public final EntityLiving mob;
        public final List<PositionedStack> mOutputs;
        public final List<ItemStack> mInput;
        public final String mobname;
        public final int infernaltype;
        public final PositionedStack ingredient;
        public final String localizedName;
        public final String mod;
        public final float maxHealth;
        public final int normalOutputsCount;
        public final int rareOutputsCount;
        public final int additionalOutputsCount;
        public final int infernalOutputsCount;
        public String isBoss = "";

        public MobCachedRecipe(EntityLiving mob, List<MobPositionedStack> mOutputs, int normalOutputsCount,
            int rareOutputsCount, int additionalOutputsCount, int infernalOutputsCount) {
            super();
            String classname = mob.getClass()
                .getName();
            this.mod = ModUtils.getModNameFromClassName(classname);
            this.mob = mob;
            this.maxHealth = mob.getMaxHealth();
            this.mOutputs = new ArrayList<>(mOutputs.size());
            this.mOutputs.addAll(mOutputs);
            this.normalOutputsCount = normalOutputsCount;
            this.rareOutputsCount = rareOutputsCount;
            this.additionalOutputsCount = additionalOutputsCount;
            this.infernalOutputsCount = infernalOutputsCount;
            this.mInput = new ArrayList<>();
            int id = EntityList.getEntityID(mob);
            mobname = EntityList.getEntityString(mob);
            localizedName = mobname.equals("Skeleton") && ((EntitySkeleton) mob).getSkeletonType() == 1
                ? "Wither Skeleton"
                : StatCollector.translateToLocal("entity." + mobname + ".name");
            if (id != 0) {
                this.mInput.add(new ItemStack(Items.spawn_egg, 1, id));
                this.mInput.add(new ItemStack(Blocks.mob_spawner, 1, id));
            }
            if (LoaderReference.EnderIO) {
                ItemStack s = new ItemStack(EnderIO.blockPoweredSpawner, 1);
                NBTTagCompound nbt = new NBTTagCompound();
                BlockPoweredSpawner.writeMobTypeToNBT(nbt, mobname);
                s.setTagCompound(nbt);
                this.mInput.add(0, s);
            } else if (id == 0) this.mInput.add(new ItemStack(Items.spawn_egg, 1, 0)); // ???
            ingredient = new PositionedStack(this.mInput.get(0), 38, 44, false);

            if (!LoaderReference.InfernalMobs) infernaltype = -1; // not supported
            else {
                if (!InfernalHelper.isClassAllowed(mob)) infernaltype = 0; // not allowed
                else if (InfernalHelper.checkEntityClassForced(mob)) infernaltype = 2; // forced
                else infernaltype = 1; // normal
            }
        }

        @Override
        public PositionedStack getIngredient() {
            return ingredient;
        }

        @Override
        public PositionedStack getResult() {
            return null;
        }

        @Override
        public List<PositionedStack> getOtherStacks() {
            if (cycleTicksStatic % 10 == 0) mOutputs.forEach(p -> p.setPermutationToRender(0));
            return mOutputs;
        }
    }
}
