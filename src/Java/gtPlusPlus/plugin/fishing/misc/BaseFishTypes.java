package gtPlusPlus.plugin.fishing.misc;
import java.util.Map;

import com.google.common.collect.Maps;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import gtPlusPlus.plugin.fishing.item.BaseFish;

public enum BaseFishTypes{
	
        COD(0, "cod", 2, 0.1F, 5, 0.6F),
        SALMON(1, "salmon", 2, 0.1F, 6, 0.8F),
        CLOWNFISH(2, "clownfish", 1, 0.1F),
        PUFFERFISH(3, "pufferfish", 1, 0.1F);
	
	
	
	
        private static final Map<Integer, BaseFishTypes> mFishMap = Maps.newHashMap();
        private final int mID;
        private final String mFishName;
        @SideOnly(Side.CLIENT)
        private IIcon iicon;
        @SideOnly(Side.CLIENT)
        private IIcon iicon2;
        private final int field_150991_j;
        private final float field_150992_k;
        private final int field_150989_l;
        private final float field_150990_m;
        private boolean isCooked = false;

        private BaseFishTypes(int p_i45336_3_, String p_i45336_4_, int p_i45336_5_, float p_i45336_6_, int p_i45336_7_, float p_i45336_8_)
        {
            this.mID = p_i45336_3_;
            this.mFishName = p_i45336_4_;
            this.field_150991_j = p_i45336_5_;
            this.field_150992_k = p_i45336_6_;
            this.field_150989_l = p_i45336_7_;
            this.field_150990_m = p_i45336_8_;
            this.isCooked = true;
        }

        private BaseFishTypes(int p_i45337_3_, String p_i45337_4_, int p_i45337_5_, float p_i45337_6_)
        {
            this.mID = p_i45337_3_;
            this.mFishName = p_i45337_4_;
            this.field_150991_j = p_i45337_5_;
            this.field_150992_k = p_i45337_6_;
            this.field_150989_l = 0;
            this.field_150990_m = 0.0F;
            this.isCooked = false;
        }

        public int getFishID()
        {
            return this.mID;
        }

        public String getFishName()
        {
            return this.mFishName;
        }

        public int func_150975_c()
        {
            return this.field_150991_j;
        }

        public float func_150967_d()
        {
            return this.field_150992_k;
        }

        public int func_150970_e()
        {
            return this.field_150989_l;
        }

        public float func_150977_f()
        {
            return this.field_150990_m;
        }

        @SideOnly(Side.CLIENT)
        public void func_150968_a(IIconRegister p_150968_1_)
        {
            this.iicon = p_150968_1_.registerIcon("fish_" + this.mFishName + "_raw");

            if (this.isCooked)
            {
                this.iicon2 = p_150968_1_.registerIcon("fish_" + this.mFishName + "_cooked");
            }
        }

        @SideOnly(Side.CLIENT)
        public IIcon func_150971_g()
        {
            return this.iicon;
        }

        @SideOnly(Side.CLIENT)
        public IIcon func_150979_h()
        {
            return this.iicon2;
        }

        public boolean isCooked()
        {
            return this.isCooked;
        }

        public static BaseFishTypes getFishTypeFromDamageValue(int dmg)
        {
            BaseFishTypes fishtype = (BaseFishTypes)mFishMap.get(Integer.valueOf(dmg));
            return fishtype == null ? COD : fishtype;
        }

        public static BaseFishTypes getFishTypeFromStackDamage(ItemStack fish)
        {
            return fish.getItem() instanceof BaseFish ? getFishTypeFromDamageValue(fish.getItemDamage()) : COD;
        }

        static
        {
            BaseFishTypes[] var0 = values();
            int var1 = var0.length;

            for (int var2 = 0; var2 < var1; ++var2)
            {
                BaseFishTypes var3 = var0[var2];
                mFishMap.put(Integer.valueOf(var3.getFishID()), var3);
            }
        }
    }