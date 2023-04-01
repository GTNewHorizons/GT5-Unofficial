package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.Textures.BlockIcons.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;

import gregtech.GT_Mod;
import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.interfaces.IConfigurationCircuitSupport;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_ClientPreference;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.extensions.ArrayExt;

public class GT_MetaTileEntity_Hatch_InputBus extends GT_MetaTileEntity_Hatch
        implements IConfigurationCircuitSupport, IAddUIWidgets {

    public GT_Recipe_Map mRecipeMap = null;
    public boolean disableSort;
    public boolean disableFilter = true;
    public boolean disableLimited = true;

    public GT_MetaTileEntity_Hatch_InputBus(int id, String name, String nameRegional, int tier) {
        this(id, name, nameRegional, tier, getSlots(tier) + 1);
    }

    protected GT_MetaTileEntity_Hatch_InputBus(int id, String name, String nameRegional, int tier, int slots,
            String[] description) {
        super(id, name, nameRegional, tier, slots, description);
    }

    public GT_MetaTileEntity_Hatch_InputBus(int id, String name, String nameRegional, int tier, int slots) {
        super(
                id,
                name,
                nameRegional,
                tier,
                slots,
                ArrayExt.of(
                        "Item Input for Multiblocks",
                        "Shift + right click with screwdriver to turn Sort mode on/off",
                        "Capacity: " + getSlots(tier) + " stack" + (getSlots(tier) >= 2 ? "s" : "")));
    }

    @Deprecated
    // having too many constructors is bad, don't be so lazy, use GT_MetaTileEntity_Hatch_InputBus(String, int,
    // String[], ITexture[][][])
    public GT_MetaTileEntity_Hatch_InputBus(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        this(aName, aTier, ArrayExt.of(aDescription), aTextures);
    }

    public GT_MetaTileEntity_Hatch_InputBus(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        this(aName, aTier, getSlots(aTier) + 1, aDescription, aTextures);
    }

    public GT_MetaTileEntity_Hatch_InputBus(String aName, int aTier, int aSlots, String[] aDescription,
            ITexture[][][] aTextures) {
        super(aName, aTier, aSlots, aDescription, aTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return GT_Mod.gregtechproxy.mRenderIndicatorsOnHatch
                ? new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_IN), TextureFactory.of(ITEM_IN_SIGN) }
                : new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_IN) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return GT_Mod.gregtechproxy.mRenderIndicatorsOnHatch
                ? new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_IN), TextureFactory.of(ITEM_IN_SIGN) }
                : new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_IN) };
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
        return aIndex != getCircuitSlot();
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_InputBus(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        GT_UIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
        return true;
    }

    @Override
    public int getCircuitSlotX() {
        return 153;
    }

    @Override
    public int getCircuitSlotY() {
        return 63;
    }

    @Override
    public void initDefaultModes(NBTTagCompound aNBT) {
        if (!getBaseMetaTileEntity().getWorld().isRemote) {
            GT_ClientPreference tPreference = GT_Mod.gregtechproxy.getClientPreference(
                    getBaseMetaTileEntity().getOwnerUuid());
            if (tPreference != null) disableFilter = !tPreference.isInputBusInitialFilterEnabled();
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.hasInventoryBeenModified()) {
            updateSlots();
        }
    }

    public void updateSlots() {
        for (int i = 0; i < mInventory.length - 1; i++)
            if (mInventory[i] != null && mInventory[i].stackSize <= 0) mInventory[i] = null;
        if (!disableSort) fillStacksIntoFirstSlots();
    }

    protected void fillStacksIntoFirstSlots() {
        final int L = mInventory.length - 1;
        HashMap<GT_Utility.ItemId, Integer> slots = new HashMap<>(L);
        HashMap<GT_Utility.ItemId, ItemStack> stacks = new HashMap<>(L);
        List<GT_Utility.ItemId> order = new ArrayList<>(L);
        List<Integer> validSlots = new ArrayList<>(L);
        for (int i = 0; i < L; i++) {
            if (!isValidSlot(i)) continue;
            validSlots.add(i);
            ItemStack s = mInventory[i];
            if (s == null) continue;
            GT_Utility.ItemId sID = GT_Utility.ItemId.createNoCopy(s);
            slots.merge(sID, s.stackSize, Integer::sum);
            if (!stacks.containsKey(sID)) stacks.put(sID, s);
            order.add(sID);
            mInventory[i] = null;
        }
        int slotindex = 0;
        for (GT_Utility.ItemId sID : order) {
            int toSet = slots.get(sID);
            if (toSet == 0) continue;
            int slot = validSlots.get(slotindex);
            slotindex++;
            mInventory[slot] = stacks.get(sID)
                                     .copy();
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
        if (mRecipeMap != null) aNBT.setString("recipeMap", mRecipeMap.mUniqueIdentifier);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        disableSort = aNBT.getBoolean("disableSort");
        disableFilter = aNBT.getBoolean("disableFilter");
        if (aNBT.hasKey("disableLimited")) disableLimited = aNBT.getBoolean("disableLimited");
        mRecipeMap = GT_Recipe_Map.sIndexedMappings.getOrDefault(aNBT.getString("recipeMap"), null);
    }

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (!getBaseMetaTileEntity().getCoverInfoAtSide(aSide)
                                    .isGUIClickable())
            return;
        if (aPlayer.isSneaking()) {
            if (disableSort) {
                disableSort = false;
            } else {
                if (disableLimited) {
                    disableLimited = false;
                } else {
                    disableSort = true;
                    disableLimited = true;
                }
            }
            GT_Utility.sendChatToPlayer(
                    aPlayer,
                    StatCollector.translateToLocal("GT5U.hatch.disableSort." + disableSort) + "   "
                            + StatCollector.translateToLocal("GT5U.hatch.disableLimited." + disableLimited));
        } else {
            disableFilter = !disableFilter;
            GT_Utility.sendChatToPlayer(
                    aPlayer,
                    StatCollector.translateToLocal("GT5U.hatch.disableFilter." + disableFilter));
        }
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        if (aIndex == getCircuitSlot()) return false;
        return aSide == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return aSide == getBaseMetaTileEntity().getFrontFacing() && aIndex != getCircuitSlot()
                && (mRecipeMap == null || disableFilter || mRecipeMap.containsInput(aStack))
                && (disableLimited || limitedAllowPutStack(aIndex, aStack));
    }

    protected boolean limitedAllowPutStack(int aIndex, ItemStack aStack) {
        for (int i = 0; i < getSizeInventory(); i++)
            if (GT_Utility.areStacksEqual(GT_OreDictUnificator.get_nocopy(aStack), mInventory[i])) return i == aIndex;
        return mInventory[aIndex] == null;
    }

    public void startRecipeProcessing() {}

    public void endRecipeProcessing() {}

    @Override
    public boolean allowSelectCircuit() {
        return true;
    }

    @Override
    public int getCircuitSlot() {
        return getSlots(mTier);
    }

    @Override
    public boolean useModularUI() {
        return true;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        switch (mTier) {
            case 0:
                getBaseMetaTileEntity().add1by1Slot(builder);
                break;
            case 1:
                getBaseMetaTileEntity().add2by2Slots(builder);
                break;
            case 2:
                getBaseMetaTileEntity().add3by3Slots(builder);
                break;
            default:
                getBaseMetaTileEntity().add4by4Slots(builder);
                break;
        }
    }
}
