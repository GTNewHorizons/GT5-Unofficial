package gregtech.api.metatileentity.implementations;

import gregtech.GT_Mod;
import gregtech.api.gui.*;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_ClientPreference;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.extensions.ArrayExt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gregtech.api.enums.Textures.BlockIcons.*;

public class GT_MetaTileEntity_Hatch_InputBus extends GT_MetaTileEntity_Hatch {
    public GT_Recipe_Map mRecipeMap = null;
    public boolean disableSort;
    public boolean disableFilter = false;
    public boolean disableLimited = true;

    public GT_MetaTileEntity_Hatch_InputBus(int id, String name, String nameRegional, int tier) {
        this(id, name, nameRegional, tier, getSlots(tier));
    }

    public GT_MetaTileEntity_Hatch_InputBus(int id, String name, String nameRegional, int tier, int slots) {
        super(id, name, nameRegional, tier, slots, ArrayExt.of(
                "Item Input for Multiblocks",
                "Shift + right click with screwdriver to turn Sort mode on/off",
                "Capacity: " + slots + " stack" + (slots >= 2 ? "s" : "")));
    }

    @Deprecated
    // having too many constructors is bad, don't be so lazy, use GT_MetaTileEntity_Hatch_InputBus(String, int, String[], ITexture[][][])
    public GT_MetaTileEntity_Hatch_InputBus(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        this(aName, aTier, ArrayExt.of(aDescription), aTextures);
    }

    public GT_MetaTileEntity_Hatch_InputBus(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        this(aName, aTier, getSlots(aTier), aDescription, aTextures);
    }

    public GT_MetaTileEntity_Hatch_InputBus(String aName, int aTier, int aSlots, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aSlots, aDescription, aTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return GT_Mod.gregtechproxy.mRenderIndicatorsOnHatch ?
                new ITexture[]{aBaseTexture, TextureFactory.of(OVERLAY_PIPE_IN), TextureFactory.of(ITEM_IN_SIGN)} :
                new ITexture[]{aBaseTexture, TextureFactory.of(OVERLAY_PIPE_IN)};
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return GT_Mod.gregtechproxy.mRenderIndicatorsOnHatch ?
                new ITexture[]{aBaseTexture, TextureFactory.of(OVERLAY_PIPE_IN), TextureFactory.of(ITEM_IN_SIGN)} :
                new ITexture[]{aBaseTexture, TextureFactory.of(OVERLAY_PIPE_IN)};
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return true;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_InputBus(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) return true;
        aBaseMetaTileEntity.openGUI(aPlayer);
        return true;
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        switch (mTier) {
            case 0:
                return new GT_Container_1by1(aPlayerInventory, aBaseMetaTileEntity);
            case 1:
                return new GT_Container_2by2(aPlayerInventory, aBaseMetaTileEntity);
            case 2:
                return new GT_Container_3by3(aPlayerInventory, aBaseMetaTileEntity);
            default:
                return new GT_Container_4by4(aPlayerInventory, aBaseMetaTileEntity);
        }
    }

    @Override
    public void initDefaultModes(NBTTagCompound aNBT) {
        if (!getBaseMetaTileEntity().getWorld().isRemote) {
            GT_ClientPreference tPreference = GT_Mod.gregtechproxy.getClientPreference(getBaseMetaTileEntity().getOwnerUuid());
            if (tPreference != null)
                disableFilter = !tPreference.isInputBusInitialFilterEnabled();
        }
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        switch (mInventory.length) {
            case 1:
                return new GT_GUIContainer_1by1(aPlayerInventory, aBaseMetaTileEntity, "Input Bus");
            case 4:
                return new GT_GUIContainer_2by2(aPlayerInventory, aBaseMetaTileEntity, "Input Bus");
            case 9:
                return new GT_GUIContainer_3by3(aPlayerInventory, aBaseMetaTileEntity, "Input Bus");
            case 16:
                return new GT_GUIContainer_4by4(aPlayerInventory, aBaseMetaTileEntity, "Input Bus");
            default:
                return new GT_GUIContainer_4by4(aPlayerInventory, aBaseMetaTileEntity, "Input Bus");
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.hasInventoryBeenModified()) {
            updateSlots();
        }
    }

    public void updateSlots() {
        for (int i = 0; i < mInventory.length; i++)
            if (mInventory[i] != null && mInventory[i].stackSize <= 0) mInventory[i] = null;
        if(!disableSort)
            fillStacksIntoFirstSlots();
    }

    protected void fillStacksIntoFirstSlots() {
        HashMap<GT_Utility.ItemId, Integer> slots = new HashMap<>(mInventory.length);
        HashMap<GT_Utility.ItemId, ItemStack> stacks = new HashMap<>(mInventory.length);
        List<GT_Utility.ItemId> order = new ArrayList<>(mInventory.length);
        List<Integer> validSlots = new ArrayList<>(mInventory.length);
        for (int i = 0; i < mInventory.length; i++) {
            if (!isValidSlot(i))
                continue;
            validSlots.add(i);
            ItemStack s = mInventory[i];
            if(s == null)
                continue;
            GT_Utility.ItemId sID = GT_Utility.ItemId.createNoCopy(s);
            slots.merge(sID, s.stackSize, Integer::sum);
            if(!stacks.containsKey(sID))
                stacks.put(sID, s);
            order.add(sID);
            mInventory[i] = null;
        }
        int slotindex = 0;
        for (GT_Utility.ItemId sID : order) {
            int toSet = slots.get(sID);
            if (toSet == 0)
                continue;
            int slot = validSlots.get(slotindex);
            slotindex++;
            mInventory[slot] = stacks.get(sID).copy();
            toSet = Math.min(toSet, mInventory[slot].getMaxStackSize());
            mInventory[slot].stackSize = toSet;
            slots.merge(sID, toSet, (a, b) -> a - b);
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("disableSort", disableSort);
        aNBT.setBoolean("disableFilter", disableFilter);
        aNBT.setBoolean("disableLimited", disableLimited);
        if (mRecipeMap != null)
            aNBT.setString("recipeMap", mRecipeMap.mUniqueIdentifier);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        disableSort = aNBT.getBoolean("disableSort");
        disableFilter = aNBT.getBoolean("disableFilter");
        if(aNBT.hasKey("disableLimited"))
            disableLimited = aNBT.getBoolean("disableLimited");
        mRecipeMap = GT_Recipe_Map.sIndexedMappings.getOrDefault(aNBT.getString("recipeMap"), null);
    }

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (!getBaseMetaTileEntity().getCoverBehaviorAtSideNew(aSide).isGUIClickable(aSide, getBaseMetaTileEntity().getCoverIDAtSide(aSide), getBaseMetaTileEntity().getComplexCoverDataAtSide(aSide), getBaseMetaTileEntity()))
            return;
        if (aPlayer.isSneaking()) {
            if(disableSort) {
                disableSort = false;
            } else {
                if(disableLimited) {
                    disableLimited = false;
                } else {
                    disableSort = true;
                    disableLimited = true;
                }
            }
            GT_Utility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("GT5U.hatch.disableSort." + disableSort) + "   " +
                    StatCollector.translateToLocal("GT5U.hatch.disableLimited." + disableLimited));
        } else {
            disableFilter = !disableFilter;
            GT_Utility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("GT5U.hatch.disableFilter." + disableFilter));
        }
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return aSide == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return aSide == getBaseMetaTileEntity().getFrontFacing()
                && (mRecipeMap == null || disableFilter || mRecipeMap.containsInput(aStack))
                && (disableLimited || limitedAllowPutStack(aIndex, aStack));
    }

    protected boolean limitedAllowPutStack(int aIndex, ItemStack aStack) {
        for (int i = 0; i < getSizeInventory(); i++)
            if (GT_Utility.areStacksEqual(GT_OreDictUnificator.get_nocopy(aStack), mInventory[i]))
                return i == aIndex;
        return mInventory[aIndex] == null;
    }
}
