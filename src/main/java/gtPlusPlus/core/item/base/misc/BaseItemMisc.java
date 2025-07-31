package gtPlusPlus.core.item.base.misc;

import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.StringUtils;
import gtPlusPlus.core.creative.AddToCreativeTab;

public class BaseItemMisc extends Item {

    public final String displayName;
    public final String unlocalName;
    public final MiscTypes miscType;
    public final int colorRGB;

    public BaseItemMisc(final String displayName, final int colorRGB, final int maxStackSize, final MiscTypes miscType,
        String[] description) {

        // Set-up the Misc Generic Item
        this.displayName = displayName;
        String unlocalName = StringUtils.sanitizeString(displayName);
        this.unlocalName = "item" + miscType.TYPE + unlocalName;
        this.miscType = miscType;
        this.setCreativeTab(AddToCreativeTab.tabMisc);
        this.setUnlocalizedName(this.unlocalName);
        this.setMaxStackSize(maxStackSize);
        // this.setTextureName(this.getCorrectTextures());
        this.colorRGB = colorRGB;
        if (description != null) {
            for (int i = 0; i < description.length; i++) {
                GTLanguageManager
                    .addStringLocalization("gtplusplus." + this.getUnlocalizedName() + ".tooltip." + i, description[i]);
            }
        }
        GameRegistry.registerItem(this, this.unlocalName);
        GTOreDictUnificator.registerOre(miscType.getOreDictPrefix() + unlocalName, new ItemStack(this));
    }

    private String getCorrectTextures() {
        return GTPlusPlus.ID + ":" + "item" + this.miscType.TYPE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return this.miscType == MiscTypes.DROP;
    }

    @Override
    public int getRenderPasses(int meta) {
        return (this.miscType == MiscTypes.DROP) ? 2 : 1;
    }

    @SideOnly(Side.CLIENT)
    private IIcon secondIcon;

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister) {
        if (this.miscType == MiscTypes.DROP && Forestry.isModLoaded()) {
            this.itemIcon = par1IconRegister.registerIcon("forestry:honeyDrop.0");
            this.secondIcon = par1IconRegister.registerIcon("forestry:honeyDrop.1");
        } else {
            this.itemIcon = par1IconRegister.registerIcon(getCorrectTextures());
        }
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        return (pass == 0) ? itemIcon : secondIcon;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list,
        final boolean bool) {
        for (int i = 0;; i++) {
            String tooltip = GTLanguageManager
                .getTranslation("gtplusplus." + this.getUnlocalizedName() + ".tooltip" + "." + i);
            if (!("gtplusplus." + this.getUnlocalizedName() + ".tooltip" + "." + i).equals(tooltip)) {
                list.add(tooltip);
            } else break;
        }
    }

    @Override
    public int getColorFromItemStack(final ItemStack stack, final int colorRGB) {
        return colorRGB;
    }

    public enum MiscTypes {

        POTION("Potion", " Potion", "potion"),
        KEY("Key", " Key", "key"),
        BIGKEY("KeyBig", " Big Key", "bosskey"),
        BOTTLE("Bottle", " Bottle", "bottle"),
        GEM("Gem", " Gemstone", "gem"),
        DROP("Droplet", " Droplet", "droplet"),
        MUSHROOM("Mushroom", " Mushroom", "mushroom");

        private final String TYPE;
        private final String DISPLAY_NAME_SUFFIX;
        private final String OREDICT_PREFIX;

        MiscTypes(final String LocalName, final String DisplayNameSuffix, final String OreDictPrefix) {
            this.TYPE = LocalName;
            this.DISPLAY_NAME_SUFFIX = DisplayNameSuffix;
            this.OREDICT_PREFIX = OreDictPrefix;
        }

        public String getType() {
            return this.TYPE;
        }

        public String getName() {
            return this.DISPLAY_NAME_SUFFIX;
        }

        public String getOreDictPrefix() {
            return this.OREDICT_PREFIX;
        }
    }
}
