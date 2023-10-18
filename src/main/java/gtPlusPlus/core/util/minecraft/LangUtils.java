package gtPlusPlus.core.util.minecraft;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.common.registry.LanguageRegistry;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.reflect.ReflectionUtils;

public class LangUtils {

    @SuppressWarnings("unchecked")
    public static void rewriteEntryForLanguageRegistry(String aLang, String aKey, String aNewValue) {
        LanguageRegistry aInstance = LanguageRegistry.instance();
        Field aModLanguageData = ReflectionUtils.getField(LanguageRegistry.class, "modLanguageData");
        if (aModLanguageData != null) {
            Map<String, Properties> aProps = new HashMap<>();
            Object aInstanceProps;
            try {
                aInstanceProps = aModLanguageData.get(aInstance);
                if (aInstanceProps != null) {
                    aProps = (Map<String, Properties>) aInstanceProps;
                    Properties aLangProps = aProps.get(aLang);
                    if (aLangProps != null) {
                        if (aLangProps.containsKey(aKey)) {
                            aLangProps.remove(aKey);
                            aLangProps.put(aKey, aNewValue);
                        } else {
                            aLangProps.put(aKey, aNewValue);
                        }
                        aProps.remove(aLang);
                        aProps.put(aLang, aLangProps);
                        ReflectionUtils.setField(aInstance, aModLanguageData, aProps);
                    }
                }
            } catch (IllegalArgumentException | IllegalAccessException ignored) {

            }
        }
    }

    /**
     * Quick Block Name Lookup that is friendly to servers and locale.
     */
    private static final Map<String, String> mLocaleCache = new HashMap<>();

    public static String getLocalizedNameOfBlock(Block aBlock, int aMeta) {
        if (aBlock != null) {
            return getLocalizedNameOfItemStack(ItemUtils.simpleMetaStack(aBlock, aMeta, 1));
        }
        return "Bad Block Name";
    }

    public static String getLocalizedNameOfItemStack(ItemStack aStack) {
        String aUnlocalized;
        if (aStack != null) {
            aUnlocalized = ItemUtils.getUnlocalizedItemName(aStack) + "." + aStack.getItemDamage() + ".name";
            String mCacheKey = aUnlocalized;
            if (mLocaleCache.containsKey(mCacheKey)) {
                // Recache the key if it's invalid.
                if (mLocaleCache.get(mCacheKey).toLowerCase().contains(".name")
                        || mLocaleCache.get(mCacheKey).toLowerCase().contains("|")) {
                    mLocaleCache.remove(mCacheKey);
                    String mNew;
                    try {
                        mNew = (StatCollector
                                .translateToLocal(aStack.getItem().getUnlocalizedNameInefficiently(aStack) + ".name"))
                                        .trim();
                        if (aStack.hasTagCompound()) {
                            if (aStack.stackTagCompound != null && aStack.stackTagCompound.hasKey("display", 10)) {
                                NBTTagCompound nbttagcompound = aStack.stackTagCompound.getCompoundTag("display");

                                if (nbttagcompound.hasKey("Name", 8)) {
                                    mNew = nbttagcompound.getString("Name");
                                }
                            }
                        }
                    } catch (Throwable t) {
                        mNew = "ERROR - Empty Stack";
                    }
                    Logger.INFO("Re-caching " + mNew + " into locale cache. Key: " + mCacheKey);
                    mLocaleCache.put(mCacheKey, mNew);
                }
                Logger.INFO("Returning Cached Value. Key: " + mCacheKey);
                return mLocaleCache.get(mCacheKey);
            } else {
                String unlocalizedName = aStack.getItem().getUnlocalizedName(aStack);
                Logger.INFO("Cached New Value. UnlocalName: " + unlocalizedName);
                String blockName = StatCollector.translateToLocal(unlocalizedName + ".name");
                Logger.INFO("Cached New Value. TranslatedName: " + unlocalizedName);
                if (blockName.toLowerCase().contains(".name") || blockName.toLowerCase().contains("|")) {
                    try {
                        blockName = (StatCollector
                                .translateToLocal(aStack.getItem().getUnlocalizedNameInefficiently(aStack) + ".name"))
                                        .trim();
                        if (aStack.hasTagCompound()) {
                            if (aStack.stackTagCompound != null && aStack.stackTagCompound.hasKey("display", 10)) {
                                NBTTagCompound nbttagcompound = aStack.stackTagCompound.getCompoundTag("display");
                                if (nbttagcompound.hasKey("Name", 8)) {
                                    blockName = nbttagcompound.getString("Name");
                                }
                            }
                        }
                    } catch (Throwable t) {
                        blockName = "ERROR - Empty Stack";
                    }
                }
                mLocaleCache.put(mCacheKey, blockName);
                Logger.INFO("Cached New Value. Key: " + mCacheKey);
                return blockName;
            }
        }
        return "Bad ItemStack Name";
    }
}
