package gregtech.api.enums;

import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;

public class TAE {

    // TAE stands for Texture Array Expansion.

    public static int gtPPLastUsedIndex = 64;
    public static int secondaryIndex = 0;

    private static final Int2ObjectOpenHashMap<ITexture> mTAE = new Int2ObjectOpenHashMap<>();
    private static final IntOpenHashSet mFreeSlots = new IntOpenHashSet(64);

    static {
        for (int i = 64; i < 128; i++) {
            mFreeSlots.add(i);
        }
        Logger.INFO("Initialising TAE.");
    }

    /**
     *
     * @param aPage   - The Texture page (0-3)
     * @param aID     - The ID on the specified page (0-15)
     * @param texture - The Texture to register
     * @return - Did it register correctly?
     */
    public static boolean registerTexture(int aPage, int aID, ITexture texture) {
        int aRealID = aID + (aPage * 16);
        return registerTexture(64 + aRealID, texture);
    }

    public static boolean registerTexture(int aID, ITexture texture) {
        if (mFreeSlots.contains(aID)) {
            mFreeSlots.remove(aID);
            mTAE.put(aID, texture);
            return true;
        }

        Logger.ERROR("Tried to register texture with ID " + aID + " to TAE, but it is already in use.");
        throw new IllegalStateException();
    }

    public static void finalizeTAE() {
        StringBuilder aFreeSpaces = new StringBuilder();
        StringBuilder aPageAndSlotFree = new StringBuilder();
        int[] aTemp = mFreeSlots.toArray(new int[0]);
        for (int i = 0; i < mFreeSlots.size(); i++) {
            int j = aTemp[i];
            aFreeSpaces.append(j);
            aPageAndSlotFree.append(getPageFromIndex(j));
            if (i != (mFreeSlots.size() - 1)) {
                aFreeSpaces.append(", ");
                aPageAndSlotFree.append(", ");
            }
        }
        Logger.INFO("Free Indexes within TAE: " + aFreeSpaces);
        Logger.INFO("Free Page slots within TAE: " + aPageAndSlotFree);
        Logger.INFO("Filling them with ERROR textures.");
        for (int aFreeSlot : aTemp) {
            registerTexture(aFreeSlot, TextureFactory.of(ModBlocks.blockCasingsTieredGTPP, 15));
        }
        Logger.INFO("Finalising TAE.");
        for (Int2ObjectMap.Entry<ITexture> entry : mTAE.int2ObjectEntrySet()) {
            Textures.BlockIcons.setCasingTextureForId(entry.getIntKey(), entry.getValue());
        }
        Logger.INFO("Finalised TAE.");
    }

    public static ITexture getTexture(int index) {
        if (gtPPLastUsedIndex >= 128) {
            return Textures.BlockIcons.getCasingTextureForId(((64 * 128) + index));
        }
        return Textures.BlockIcons.getCasingTextureForId((64 + index));
    }

    public static int GTPP_INDEX(int ID) {
        if (ID >= 64) {
            if (gtPPLastUsedIndex >= 128) {
                return (128 + ID);
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
        int aAdjustedIndex = aIndex > 64 ? (aIndex - 64) : aIndex;
        int aPage = aAdjustedIndex / 16;
        int aSlot = aAdjustedIndex - (16 * aPage);
        return "[" + aIndex + " | " + aPage + ", " + aSlot + "]";
    }
}
