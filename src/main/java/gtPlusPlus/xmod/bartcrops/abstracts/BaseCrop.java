package gtPlusPlus.xmod.bartcrops.abstracts;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import ic2.api.crops.CropCard;
import ic2.api.crops.ICropTile;
import speiger.src.crops.api.ICropCardInfo;

public abstract class BaseCrop extends CropCard implements ICropCardInfo {

    @Override
    @SideOnly(Side.CLIENT)
    public void registerSprites(IIconRegister iconRegister) {
        this.textures = new IIcon[this.maxSize()];
        for (int i = 1; i <= this.textures.length; ++i) {
            this.textures[i - 1] = iconRegister
                .registerIcon(GTPlusPlus.ID + ":crop/blockCrop." + this.name() + "." + i);
        }
    }

    @Override
    public float dropGainChance() {
        return (float) GTUtility.powInt(0.95D, this.tier());
    }

    @Override
    public boolean canCross(ICropTile crop) {
        return crop.getSize() == this.maxSize();
    }

    @Override
    public int getrootslength(ICropTile crop) {
        return 3;
    }

    @Override
    public String discoveredBy() {
        return "Alkalus";
    }

    @Override
    public String owner() {
        return "Gtplusplus";
    }

    @Override
    public List<String> getCropInformation() {
        List<String> ret = new ArrayList<>();
        ret.add(Arrays.toString(this.attributes()));
        return ret;
    }

    public ItemStack getDisplayItem(CropCard card) {
        return ItemUtils.getItemStackOfAmountFromOreDict("crop" + this.name(), 0);
    }
}
