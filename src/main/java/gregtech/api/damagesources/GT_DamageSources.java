package gregtech.api.damagesources;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class GT_DamageSources {

    public static DamageSource getElectricDamage() {
        return ic2.api.info.Info.DMG_ELECTRIC;
    }

    public static DamageSource getRadioactiveDamage() {
        return ic2.api.info.Info.DMG_RADIATION;
    }

    public static DamageSource getNukeExplosionDamage() {
        return ic2.api.info.Info.DMG_NUKE_EXPLOSION;
    }

    public static DamageSource getExplodingDamage() {
        return new DamageSourceExploding();
    }

    public static DamageSource getCombatDamage(String aType, EntityLivingBase aPlayer, IChatComponent aDeathMessage) {
        return new DamageSourceCombat(aType, aPlayer, aDeathMessage);
    }

    public static DamageSource getHeatDamage() {
        return new DamageSourceHeat();
    }

    public static DamageSource getFrostDamage() {
        return new DamageSourceFrost();
    }

    private static class DamageSourceCombat extends EntityDamageSource {

        private final IChatComponent mDeathMessage;

        public DamageSourceCombat(String aType, EntityLivingBase aPlayer, IChatComponent aDeathMessage) {
            super(aType, aPlayer);
            mDeathMessage = aDeathMessage;
        }

        @Override
        public IChatComponent func_151519_b(EntityLivingBase aTarget) {
            return mDeathMessage == null ? super.func_151519_b(aTarget) : mDeathMessage;
        }
    }

    private static class DamageSourceFrost extends DamageSource {

        public DamageSourceFrost() {
            super("frost");
            setDifficultyScaled();
        }

        @Override
        public IChatComponent func_151519_b(EntityLivingBase aTarget) {
            return new ChatComponentText(
                EnumChatFormatting.RED + aTarget.getCommandSenderName() + EnumChatFormatting.WHITE + " got frozen");
        }
    }

    private static class DamageSourceHeat extends DamageSource {

        public DamageSourceHeat() {
            super("steam");
            setFireDamage();
            setDifficultyScaled();
        }

        @Override
        public IChatComponent func_151519_b(EntityLivingBase aTarget) {
            return new ChatComponentText(
                EnumChatFormatting.RED + aTarget.getCommandSenderName()
                    + EnumChatFormatting.WHITE
                    + " was boiled alive");
        }
    }

    public static class DamageSourceHotItem extends DamageSourceHeat {

        @Nullable
        private final ItemStack stack;

        public DamageSourceHotItem(@Nullable ItemStack cause) {
            this.stack = cause;
        }

        @Nullable
        public ItemStack getDamagingStack() {
            return stack;
        }
    }

    public static class DamageSourceExploding extends DamageSource {

        public DamageSourceExploding() {
            super("exploded");
            setDamageAllowedInCreativeMode();
            setDamageBypassesArmor();
            setDamageIsAbsolute();
        }

        @Override
        public IChatComponent func_151519_b(EntityLivingBase aTarget) {
            return new ChatComponentText(
                EnumChatFormatting.RED + aTarget.getCommandSenderName() + EnumChatFormatting.WHITE + " exploded");
        }
    }
}
