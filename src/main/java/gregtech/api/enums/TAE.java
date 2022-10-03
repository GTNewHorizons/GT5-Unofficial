package gregtech.api.enums;

import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import net.minecraftforge.common.util.ForgeDirection;

public class TAE {

    // TAE stands for Texture Array Expansion.

    public static int gtPPLastUsedIndex = 64;
    public static int secondaryIndex = 0;

    public static HashMap<Integer, ITexture> mTAE = new HashMap<Integer, ITexture>();
    private static final HashSet<Integer> mFreeSlots = new HashSet<Integer>(64);

    static {
        for (int i = 64; i < 128; i++) {
            mFreeSlots.add(i);
        }
        Logger.INFO("Initialising TAE.");
    }

    /**
     *
     * @param aPage - The Texture page (0-3)
     * @param aID - The ID on the specified page (0-15)
     * @param iTexture - The Texture to register
     * @return - Did it register correctly?
     */
    public static boolean registerTexture(int aPage, int aID, ITexture iTexture) {
        int aRealID = aID + (aPage * 16);
        return registerTexture(64 + aRealID, iTexture);
    }

    public static boolean registerTexture(int aID, ITexture iTexture) {
        if (mFreeSlots.contains(aID)) {
            mFreeSlots.remove(aID);
            mTAE.put(aID, iTexture);
            return true;
        } else {
            CORE.crash("Tried to register texture with ID " + aID + " to TAE, but it is already in use.");
            return false; // Dead Code
        }
    }

    public static void finalizeTAE() {
        String aFreeSpaces = "";
        String aPageAndSlotFree = "";
        AutoMap<Integer> aTemp = new AutoMap<Integer>(mFreeSlots);
        for (int i = 0; i < mFreeSlots.size(); i++) {
            int j = aTemp.get(i);
            aFreeSpaces += j;
            aPageAndSlotFree += getPageFromIndex(j);
            if (i != (mFreeSlots.size() - 1)) {
                aFreeSpaces += ", ";
                aPageAndSlotFree += ", ";
            }
        }
        Logger.INFO("Free Indexes within TAE: " + aFreeSpaces);
        Logger.INFO("Free Page slots within TAE: " + aPageAndSlotFree);
        Logger.INFO("Filling them with ERROR textures.");
        for (int aFreeSlot : aTemp.values()) {
            registerTexture(aFreeSlot, TextureFactory.of(ModBlocks.blockCasingsTieredGTPP, 15, ForgeDirection.UP));
        }
        Logger.INFO("Finalising TAE.");
        for (int aKeyTae : mTAE.keySet()) {
            Textures.BlockIcons.setCasingTextureForId(aKeyTae, mTAE.get(aKeyTae));
        }
        Logger.INFO("Finalised TAE.");
    }

    private static boolean registerTextures(ITexture iTexture) {
        try {
            // Handle page 2.
            Logger.INFO("[TAE} Registering Texture, Last used casing ID is " + gtPPLastUsedIndex + ".");
            if (gtPPLastUsedIndex >= 128) {
                if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK && Utils.getGregtechSubVersion() > 30) {
                    Field x = ReflectionUtils.getField(Textures.BlockIcons.class, "casingTexturePages");
                    if (x != null) {
                        ITexture[][] h = (ITexture[][]) x.get(null);
                        if (h != null) {
                            h[64][secondaryIndex++] = iTexture;
                            x.set(null, h);
                            Logger.INFO("[TAE} Registered Texture with ID " + (secondaryIndex - 1)
                                    + " in secondary index.");
                            return true;
                        }
                    }
                }
            }

            // set to page 1.
            else {
                Textures.BlockIcons.setCasingTextureForId(gtPPLastUsedIndex, iTexture);
                Logger.INFO("[TAE} Registered Texture with ID " + (gtPPLastUsedIndex) + " in main index.");
                gtPPLastUsedIndex++;
                return true;
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        Logger.INFO("[TAE} Failed to register texture, Last used casing ID is " + gtPPLastUsedIndex + ".");
        return false;
    }

    public static ITexture getTexture(int index) {
        if (gtPPLastUsedIndex >= 128) {
            if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK && Utils.getGregtechSubVersion() > 30) {
                return Textures.BlockIcons.getCasingTextureForId(((64 * 128) + index));
            }
        }
        return Textures.BlockIcons.getCasingTextureForId((64 + index));
    }

    public static int GTPP_INDEX(int ID) {

        if (ID >= 64) {
            if (gtPPLastUsedIndex >= 128) {
                if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK && Utils.getGregtechSubVersion() > 30) {
                    return (128 + ID);
                }
            }
        }
        return (64 + ID);
    }

    public static int getIndexFromPage(int page, int blockMeta) {
        int id = 64;
        id += (page == 0 ? 0 : page == 1 ? 16 : page == 2 ? 32 : page == 3 ? 48 : page == 4 ? 64 : 0);
        id += blockMeta;
        return id;
    }

    public static String getPageFromIndex(int aIndex) {
        int aPage = 0;
        int aSlot = 0;
        int aAdjustedIndex = aIndex > 64 ? (aIndex - 64) : aIndex;
        aPage = aAdjustedIndex / 16;
        aSlot = aAdjustedIndex - (16 * aPage);
        return "[" + aIndex + " | " + aPage + ", " + aSlot + "]";
    }
}
