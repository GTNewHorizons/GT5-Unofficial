// package goodgenerator.items.nuclear;
//
// import com.github.bartimaeusnek.bartworks.util.Pair;
// import cpw.mods.fml.relauncher.Side;
// import cpw.mods.fml.relauncher.SideOnly;
// import goodgenerator.main.GoodGenerator;
// import gregtech.api.interfaces.IIconContainer;
// import gregtech.api.items.GT_MetaGenerated_Item;
// import gregtech.api.util.GT_LanguageManager;
// import gregtech.api.util.GT_OreDictUnificator;
// import net.minecraft.creativetab.CreativeTabs;
// import net.minecraft.item.Item;
// import net.minecraft.item.ItemStack;
// import net.minecraft.util.EnumChatFormatting;
// import net.minecraft.util.IIcon;
//
// import java.util.List;
//
// import static goodgenerator.items.nuclear.IsotopeMaterial.mIDMap;
// import static goodgenerator.items.nuclear.IsotopeMaterial.mIsotopeMaterial;
//
// public class NuclearMetaItemGenerator extends GT_MetaGenerated_Item {
//
//    /**
//     *  <p>Full ingot - 1000
//     *  <p>Tiny ingot - 2000
//     *  <p>Full-Oxide - 3000
//     *  <p>Tiny-Oxide - 4000
//     */
//    public static final Pair<Integer, String>[] TYPE_OFFSET = new Pair[] {
//            new Pair<>(1000, "%s"),
//            new Pair<>(2000, "Tiny of %s"),
//            new Pair<>(3000, "%s Oxide"),
//            new Pair<>(4000, "Tiny of %s Oxide"),
//    };
//    public static final Pair<Integer, String>[] OREPREFIX = new Pair[] {
//            new Pair<>(1000, "item%s"),
//            new Pair<>(2000, "itemTiny%s"),
//            new Pair<>(3000, "item%sOxide"),
//            new Pair<>(4000, "itemTiny%sOxide"),
//    };
//
//    public NuclearMetaItemGenerator() {
//        super("nuclearIsotopeMaterial", (short) 32766, (short) 0);
//        this.setCreativeTab(GoodGenerator.GG);
//        for (IsotopeMaterial tIsotope : mIsotopeMaterial) {
//            for (Pair<Integer, String> tType : TYPE_OFFSET) {
//                int tOffset = tType.getKey();
//                String tOreName = tType.getValue();
//                ItemStack tStack = new ItemStack(this, 1, tIsotope.mID + tOffset);
//                GT_LanguageManager.addStringLocalization(this.getUnlocalizedName(tStack) + ".name",
// String.format(tOreName, tIsotope.mLocalizedName));
//                GT_LanguageManager.addStringLocalization(this.getUnlocalizedName(tStack) + ".tooltip",
// EnumChatFormatting.AQUA + String.format("%s's Isotope.", tIsotope.mMaterialName) + EnumChatFormatting.RESET);
//            }
//            for (Pair<Integer, String> tOreDict : OREPREFIX) {
//                int tOffset = tOreDict.getKey();
//                String tOreName = tOreDict.getValue();
//                ItemStack tStack = new ItemStack(this, 1, tIsotope.mID + tOffset);
//                GT_OreDictUnificator.registerOre(String.format(tOreName, tIsotope.mName), tStack);
//            }
//        }
//    }
//
//    @Override
//    public String getItemStackDisplayName(ItemStack aStack) {
//        return GT_LanguageManager.getTranslation(this.getUnlocalizedName(aStack) + ".name");
//    }
//
//    @Override
//    public IIconContainer getIconContainer(int aMetaData) {
//        int tID = aMetaData % 1000;
//        int tType = aMetaData / 1000 - 1;
//        IsotopeMaterial tMaterial = mIDMap.get(tID);
//        if (tMaterial != null) {
//            return tMaterial.mTexture.mTextures[tType];
//        }
//        return null;
//    }
//
//    @Override
//    @SideOnly(Side.CLIENT)
//    public void getSubItems(Item var1, CreativeTabs aCreativeTab, List aList) {
//        for (IsotopeMaterial tIsotope : mIsotopeMaterial) {
//            for (int i = 1; i <= 4; i ++) {
//                ItemStack tStack = new ItemStack(this, 1, tIsotope.mID + i * 1000);
//                aList.add(tStack);
//            }
//        }
//    }
//
//    @Override
//    public short[] getRGBa(ItemStack aStack) {
//        int tID = aStack.getItemDamage() % 1000;
//        int tType = aStack.getItemDamage() / 1000;
//        IsotopeMaterial tMaterial = mIDMap.get(tID);
//        if (tMaterial != null) {
//            if (tType == 1 || tType == 2)
//                return tMaterial.mRGB;
//            else
//                return tMaterial.mRGBO;
//        }
//        return null;
//    }
//
//    @Override
//    public final IIcon getIconFromDamage(int aMetaData) {
//        return this.getIconContainer(aMetaData).getIcon();
//    }
// }
