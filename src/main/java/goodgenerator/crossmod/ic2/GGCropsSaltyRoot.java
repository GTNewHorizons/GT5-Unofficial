package goodgenerator.crossmod.ic2;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import goodgenerator.main.GoodGenerator;
import goodgenerator.util.ItemRefer;
import ic2.api.crops.CropCard;
import ic2.api.crops.ICropTile;
import speiger.src.crops.api.ICropCardInfo;

public class GGCropsSaltyRoot extends CropCard implements ICropCardInfo {

    private final String cropName;

    public GGCropsSaltyRoot(String name) {
        this.cropName = name;
    }

    @Override
    public String name() {
        return cropName;
    }

    @Override
    public int tier() {
        return 4;
    }

    @Override
    public int stat(int n) {
        return 0;
    }

    @Override
    public String[] attributes() {
        return new String[] { "Salt", "Gray", "Root", "Hydrophobic" };
    }

    @Override
    public int maxSize() {
        return 3;
    }

    @Override
    public boolean canGrow(ICropTile crop) {
        return crop.getSize() < maxSize();
    }

    @Override
    public int getOptimalHavestSize(ICropTile crop) {
        return 3;
    }

    @Override
    public boolean canBeHarvested(ICropTile crop) {
        return crop.getSize() == 3;
    }

    @Override
    public ItemStack getGain(ICropTile crop) {
        return ItemRefer.Salty_Root.get(1);
    }

    @Override
    public String discoveredBy() {
        return "GlodBlock";
    }

    @Override
    public String owner() {
        return GoodGenerator.MOD_ID;
    }

    @Override
    public String displayName() {
        return StatCollector.translateToLocal("crops." + cropName.toLowerCase().replace(" ", ""));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerSprites(IIconRegister iconRegister) {
        textures = new IIcon[maxSize()];
        for (int i = 1; i <= textures.length; i++) {
            textures[i - 1] = iconRegister.registerIcon(GoodGenerator.MOD_ID + ":crops/" + name() + "." + i);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getSprite(ICropTile crop) {
        if (crop.getSize() <= 0 || crop.getSize() > textures.length) return null;
        return textures[crop.getSize() - 1];
    }

    @Override
    public boolean canCross(ICropTile crop) {
        return crop.getSize() > 1;
    }

    @Override
    public float dropGainChance() {
        return 4.0F;
    }

    @Override
    public int weightInfluences(ICropTile crop, float humidity, float nutrients, float air) {
        return (int) (-humidity + nutrients * 2 + air);
    }

    @Override
    public List<String> getCropInformation() {
        return Arrays.asList("It prefers dry environment.", "Inedible.");
    }

    @Override
    public ItemStack getDisplayItem() {
        return ItemRefer.Salty_Root.get(1);
    }
}
