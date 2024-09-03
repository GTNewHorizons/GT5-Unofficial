package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.Textures.BlockIcons.ITEM_IN_SIGN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_IN;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.CycleButtonWidget;

import gregtech.GTMod;
import gregtech.api.gui.modularui.GTUIInfos;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.IConfigurationCircuitSupport;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTClientPreference;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTTooltipDataCache;
import gregtech.api.util.GTUtility;
import gregtech.api.util.extensions.ArrayExt;

public class MTEHatchInputBus extends MTEHatch implements IConfigurationCircuitSupport, IAddUIWidgets {

    private static final String SORTING_MODE_TOOLTIP = "GT5U.machines.sorting_mode.tooltip";
    private static final String ONE_STACK_LIMIT_TOOLTIP = "GT5U.machines.one_stack_limit.tooltip";
    private static final int BUTTON_SIZE = 18;

    public RecipeMap<?> mRecipeMap = null;
    public boolean disableSort;
    public boolean disableFilter = true;
    public boolean disableLimited = true;
    private int uiButtonCount = 0;

    public MTEHatchInputBus(int id, String name, String nameRegional, int tier) {
        this(id, name, nameRegional, tier, getSlots(tier) + 1);
    }

    protected MTEHatchInputBus(int id, String name, String nameRegional, int tier, int slots, String[] description) {
        super(id, name, nameRegional, tier, slots, description);
    }

    public MTEHatchInputBus(int id, String name, String nameRegional, int tier, int slots) {
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

    public MTEHatchInputBus(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        this(aName, aTier, getSlots(aTier) + 1, aDescription, aTextures);
    }

    public MTEHatchInputBus(String aName, int aTier, int aSlots, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aSlots, aDescription, aTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return GTMod.gregtechproxy.mRenderIndicatorsOnHatch
            ? new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_IN), TextureFactory.of(ITEM_IN_SIGN) }
            : new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_IN) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return GTMod.gregtechproxy.mRenderIndicatorsOnHatch
            ? new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_IN), TextureFactory.of(ITEM_IN_SIGN) }
            : new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_IN) };
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
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
        return new MTEHatchInputBus(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        GTUIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
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
            GTClientPreference tPreference = GTMod.gregtechproxy
                .getClientPreference(getBaseMetaTileEntity().getOwnerUuid());
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
        HashMap<GTUtility.ItemId, Integer> slots = new HashMap<>(L);
        HashMap<GTUtility.ItemId, ItemStack> stacks = new HashMap<>(L);
        List<GTUtility.ItemId> order = new ArrayList<>(L);
        List<Integer> validSlots = new ArrayList<>(L);
        for (int i = 0; i < L; i++) {
            if (!isValidSlot(i)) continue;
            validSlots.add(i);
            ItemStack s = mInventory[i];
            if (s == null) continue;
            GTUtility.ItemId sID = GTUtility.ItemId.createNoCopy(s);
            slots.merge(sID, s.stackSize, Integer::sum);
            if (!stacks.containsKey(sID)) stacks.put(sID, s);
            order.add(sID);
            mInventory[i] = null;
        }
        int slotindex = 0;
        for (GTUtility.ItemId sID : order) {
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
        if (mRecipeMap != null) {
            aNBT.setString("recipeMap", mRecipeMap.unlocalizedName);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        disableSort = aNBT.getBoolean("disableSort");
        disableFilter = aNBT.getBoolean("disableFilter");
        if (aNBT.hasKey("disableLimited")) {
            disableLimited = aNBT.getBoolean("disableLimited");
        }
        mRecipeMap = RecipeMap.getFromOldIdentifier(aNBT.getString("recipeMap"));
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (!getBaseMetaTileEntity().getCoverInfoAtSide(side)
            .isGUIClickable()) return;
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
            GTUtility.sendChatToPlayer(
                aPlayer,
                StatCollector.translateToLocal("GT5U.hatch.disableSort." + disableSort) + "   "
                    + StatCollector.translateToLocal("GT5U.hatch.disableLimited." + disableLimited));
        } else {
            disableFilter = !disableFilter;
            GTUtility
                .sendChatToPlayer(aPlayer, StatCollector.translateToLocal("GT5U.hatch.disableFilter." + disableFilter));
        }
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        if (aIndex == getCircuitSlot()) return false;
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return side == getBaseMetaTileEntity().getFrontFacing() && aIndex != getCircuitSlot()
            && (mRecipeMap == null || disableFilter || mRecipeMap.containsInput(aStack))
            && (disableLimited || limitedAllowPutStack(aIndex, aStack));
    }

    protected boolean limitedAllowPutStack(int aIndex, ItemStack aStack) {
        for (int i = 0; i < getSizeInventory(); i++)
            if (GTUtility.areStacksEqual(GTOreDictUnificator.get_nocopy(aStack), mInventory[i])) return i == aIndex;
        return mInventory[aIndex] == null;
    }

    @Override
    public boolean allowSelectCircuit() {
        return true;
    }

    @Override
    public int getCircuitSlot() {
        return getSlots(mTier);
    }

    private void addSortStacksButton(ModularWindow.Builder builder) {
        builder.widget(
            createToggleButton(
                () -> !disableSort,
                val -> disableSort = !val,
                GTUITextures.OVERLAY_BUTTON_SORTING_MODE,
                () -> mTooltipCache.getData(SORTING_MODE_TOOLTIP)));
    }

    private void addOneStackLimitButton(ModularWindow.Builder builder) {
        builder.widget(createToggleButton(() -> !disableLimited, val -> {
            disableLimited = !val;
            updateSlots();
        }, GTUITextures.OVERLAY_BUTTON_ONE_STACK_LIMIT, () -> mTooltipCache.getData(ONE_STACK_LIMIT_TOOLTIP)));
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        buildContext.addCloseListener(() -> uiButtonCount = 0);
        addSortStacksButton(builder);
        addOneStackLimitButton(builder);
        // Remove one for ghost circuit slot
        int slotCount = getSizeInventory();
        if (allowSelectCircuit()) {
            slotCount = slotCount - 1;
        }
        // We do this to decouple slot count from tier in here, since there is no reason to do so.
        switch (slotCount) {
            case 1 -> getBaseMetaTileEntity().add1by1Slot(builder);
            case 4 -> getBaseMetaTileEntity().add2by2Slots(builder);
            case 9 -> getBaseMetaTileEntity().add3by3Slots(builder);
            case 16 -> getBaseMetaTileEntity().add4by4Slots(builder);
            default -> {}
        }
    }

    private Widget createToggleButton(Supplier<Boolean> getter, Consumer<Boolean> setter, UITexture picture,
        Supplier<GTTooltipDataCache.TooltipData> tooltipDataSupplier) {
        return new CycleButtonWidget().setToggle(getter, setter)
            .setStaticTexture(picture)
            .setVariableBackground(GTUITextures.BUTTON_STANDARD_TOGGLE)
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(7 + (uiButtonCount++ * BUTTON_SIZE), 62)
            .setSize(BUTTON_SIZE, BUTTON_SIZE)
            .setGTTooltip(tooltipDataSupplier);
    }
}
