package gregtech.common.tileentities.machines.basic;

import static gregtech.api.enums.Mods.GregTech;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ProgressWidget;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.gtnewhorizon.gtnhlib.client.model.wavefront.WavefrontVBOBuilder;
import com.gtnewhorizon.gtnhlib.client.renderer.vao.IVertexArrayObject;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.HarvestTool;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MetaTileEntityIDs;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.objects.overclockdescriber.OverclockDescriber;
import gregtech.api.recipe.BasicUIProperties;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.singleblock.base.MTEBasicMachineBaseGui;
import gregtech.common.gui.modularui.util.MachineModularSlot;
import gregtech.common.modularui2.widget.GTProgressWidget;
import gregtech.common.render.IMTERenderer;

@IMetaTileEntity.SkipGenerateDescription
public class MTEIceCreamMachine extends MTEBasicMachine implements IMTERenderer, IItemRenderer {

    public MTEIceCreamMachine(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 1, "", 1, 1);
    }

    public MTEIceCreamMachine(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures, 1, 1);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEIceCreamMachine(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public String[] getDescription() {
        return new String[] { StatCollector.translateToLocal("gt.icecreammachine.tooltip.0") };
    }

    @Override
    public boolean isElectric() {
        return false;
    }

    @Override
    public long maxEUStore() {
        return 0;
    }

    @Override
    public long getMinimumStoredEU() {
        return 0;
    }

    @Override
    public long maxAmperesIn() {
        return 0;
    }

    @Override
    public long maxAmperesOut() {
        return 0;
    }

    @Override
    @Nullable
    public OverclockDescriber getOverclockDescriber() {
        return null;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.iceCreamMachineRecipes;
    }

    @Override
    public byte getTileEntityBaseType() {
        return HarvestTool.PickaxeLevel2.toTileEntityBaseType();
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GTCEU_LOOP_MIXER;
    }

    // Once per in-game day, 60% chance the machine works that day. Only rolled lazily when the GUI is opened,
    // never on a per-tick basis, and cached/persisted so it doesn't need re-rolling until the day changes.
    // When broken, there's a chance it'll also accept a repair item (in the special slot) to fix it early.
    private static final int WORK_CHANCE_PERCENT = 40;
    private static final int REPAIR_REQUEST_CHANCE_PERCENT = 60;
    private static final int BROKEN_TOOLTIP_COUNT = 4;
    private static final long TICKS_PER_DAY = 24000L;

    private static ItemStack[] sRepairItems;

    private static ItemStack[] getRepairItems() {
        if (sRepairItems == null) {
            sRepairItems = new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.screw, Materials.Iron, 1L),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.Steel, 1L),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Iron, 1L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 1L),
                ItemList.Electric_Motor_LV.get(1L) };
        }
        return sRepairItems;
    }

    private long mLastRolledDay = -1L;
    private boolean mBrokenToday = true;
    private int mRepairItemIndex = -1;
    private int mBrokenTooltipIndex = 0;

    public boolean isBrokenToday() {
        return mBrokenToday;
    }

    public boolean isRepairRequested() {
        return mBrokenToday && mRepairItemIndex >= 0;
    }

    public int getRepairItemIndex() {
        return mRepairItemIndex;
    }

    public int getBrokenTooltipIndex() {
        return mBrokenTooltipIndex;
    }

    private void rollDailyMalfunctionIfNeeded() {
        final long day = getBaseMetaTileEntity().getWorld()
            .getTotalWorldTime() / TICKS_PER_DAY;
        if (day == mLastRolledDay) return;
        mLastRolledDay = day;
        mBrokenToday = getBaseMetaTileEntity().getRandomNumber(100) >= WORK_CHANCE_PERCENT;
        mRepairItemIndex = (mBrokenToday
            && getBaseMetaTileEntity().getRandomNumber(100) < REPAIR_REQUEST_CHANCE_PERCENT)
                ? getBaseMetaTileEntity().getRandomNumber(getRepairItems().length)
                : -1;
        mBrokenTooltipIndex = getBaseMetaTileEntity().getRandomNumber(BROKEN_TOOLTIP_COUNT);
    }

    // Checks the special slot for the requested repair item; if present in enough quantity, consumes it and
    // clears the broken state early (instead of waiting for tomorrow's roll)
    private void tryRepair() {
        if (mRepairItemIndex < 0) return;
        final ItemStack special = getSpecialSlot();
        if (special == null) return;
        final ItemStack needed = getRepairItems()[mRepairItemIndex];
        if (special.getItem() != needed.getItem() || special.getItemDamage() != needed.getItemDamage()
            || special.stackSize < needed.stackSize) return;

        special.stackSize -= needed.stackSize;
        if (special.stackSize <= 0) mInventory[getSpecialSlotIndex()] = null;
        mBrokenToday = false;
        mRepairItemIndex = -1;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setLong("mLastRolledDay", mLastRolledDay);
        aNBT.setBoolean("mBrokenToday", mBrokenToday);
        aNBT.setInteger("mRepairItemIndex", mRepairItemIndex);
        aNBT.setInteger("mBrokenTooltipIndex", mBrokenTooltipIndex);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mLastRolledDay = aNBT.getLong("mLastRolledDay");
        mBrokenToday = aNBT.getBoolean("mBrokenToday");
        mRepairItemIndex = aNBT.getInteger("mRepairItemIndex");
        mBrokenTooltipIndex = aNBT.getInteger("mBrokenTooltipIndex");
    }

    @Override
    public void setItemNBT(NBTTagCompound aNBT) {
        super.setItemNBT(aNBT);
        aNBT.setLong("mLastRolledDay", mLastRolledDay);
        aNBT.setBoolean("mBrokenToday", mBrokenToday);
        aNBT.setInteger("mRepairItemIndex", mRepairItemIndex);
        aNBT.setInteger("mBrokenTooltipIndex", mBrokenTooltipIndex);
    }

    @Override
    public int checkRecipe() {
        if (mBrokenToday) {
            tryRepair();
            if (mBrokenToday) return DID_NOT_FIND_RECIPE;
        }
        if (getOutputAt(0) != null) {
            mOutputBlocked++;
            return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
        }
        final ItemStack input = getInputAt(0);
        if (GTUtility.isStackInvalid(input)) return DID_NOT_FIND_RECIPE;

        final List<GTRecipe> matches = getRecipeMap().findRecipeQuery()
            .items(getAllInputs())
            .findAll()
            .collect(Collectors.toList());
        if (matches.isEmpty()) return DID_NOT_FIND_RECIPE;

        final GTRecipe recipe = matches.get(getBaseMetaTileEntity().getRandomNumber(matches.size()));
        if (!canOutput(recipe)) {
            mOutputBlocked++;
            return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
        }

        input.stackSize -= recipe.mInputs[0].stackSize;
        mOutputItems[0] = recipe.getOutput(0)
            .copy();
        mEUt = 0;
        mMaxProgresstime = recipe.mDuration;
        return FOUND_AND_SUCCESSFULLY_USED_RECIPE;
    }

    @Override
    protected BasicUIProperties getUIProperties() {
        return super.getUIProperties().toBuilder()
            .progressBarTextureMUI2(GTGuiTextures.PROGRESSBAR_ARROW_STANDARD)
            .build();
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        // Server-authoritative, and runs before any widgets are built (so the special slot/tooltip below always
        // reflect today's just-rolled state, not yesterday's).
        if (!data.isClient()) rollDailyMalfunctionIfNeeded();
        return new MTEBasicMachineBaseGui<>(this, this.getUIProperties()) {

            @Override
            protected boolean supportsBottomLeftCornerFlow() {
                return false;
            }

            @Override
            protected boolean supportsChargerSlot() {
                return false;
            }

            @Override
            protected void initErrors(PanelSyncManager syncManager) {
                super.initErrors(syncManager);
                // One synced boolean per possible tooltip outcome (rather than picking the lang key from the raw
                // field at construction time), since only the synced value is guaranteed to reach the client -
                // the machine's own fields are never networked outside of this sync layer.
                for (int i = 0; i < getRepairItems().length; i++) {
                    final int idx = i;
                    final BooleanSyncValue repairSyncer = new BooleanSyncValue(
                        () -> machine.getRepairItemIndex() == idx);
                    syncManager.syncValue("icecreamRepair" + idx, repairSyncer);
                    errorMap.put(
                        repairSyncer,
                        machine.mTooltipCache.getData("gt.icecreammachine.repair." + idx + ".tooltip"));
                }
                for (int i = 0; i < BROKEN_TOOLTIP_COUNT; i++) {
                    final int idx = i;
                    final BooleanSyncValue brokenNoRepairSyncer = new BooleanSyncValue(
                        () -> machine.isBrokenToday() && machine.getRepairItemIndex() < 0
                            && machine.getBrokenTooltipIndex() == idx);
                    syncManager.syncValue("icecreamBrokenNoRepair" + idx, brokenNoRepairSyncer);
                    errorMap.put(
                        brokenNoRepairSyncer,
                        machine.mTooltipCache.getData("gt.icecreammachine.broken." + idx + ".tooltip"));
                }
            }

            // Special slot doubles as the repair-item slot, so it needs its own tooltip
            @Override
            protected ItemSlot createSpecialSlot() {
                return new ItemSlot().marginRight(SLOT_SIZE / 2)
                    .slot(
                        new MachineModularSlot(
                            machine.inventoryHandler,
                            machine.getSpecialSlotIndex(),
                            baseMetaTileEntity))
                    .backgroundOverlay(GTGuiTextures.SLOT_MAINTENANCE)
                    .tooltip(
                        t -> t.addLine(StatCollector.translateToLocal("gt.icecreammachine.repairslot.tooltip"))
                            .addLine(StatCollector.translateToLocal("gt.icecreammachine.repairslot.tooltip.1")))
                    .tooltipShowUpTimer(BaseTileEntity.TOOLTIP_DELAY);
            }

            // Does not use EU / tooltip to the generic one
            @Override
            protected ProgressWidget createProgressBar(ModularPanel panel, PanelSyncManager syncManager) {
                return new GTProgressWidget().neiTransferRect(properties.neiTransferRectId)
                    .value(new DoubleSyncValue(() -> (double) machine.mProgresstime / machine.mMaxProgresstime))
                    .texture(progressBarTexture, properties.progressBarWidthMUI2)
                    .size(properties.progressBarWidthMUI2, properties.progressBarHeightMUI2 / 2)
                    .direction(properties.progressBarDirectionMUI2)
                    .tooltipShowUpTimer(BaseTileEntity.TOOLTIP_DELAY);
            }
        }.useGregTechLogo(true)
            .build(data, syncManager, uiSettings);
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        // Hides machine casing cube / provided by renderTESR()
        return new ITexture[0];
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return item != null && item.getItemDamage() == MetaTileEntityIDs.ICE_CREAM_MACHINE.ID;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return switch (helper) {
            case ENTITY_ROTATION, ENTITY_BOBBING, EQUIPPED_BLOCK, INVENTORY_BLOCK -> true;
            default -> false;
        };
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        if (!modelInitialized) {
            initModel();
            if (!modelInitialized) return;
        }
        Minecraft.getMinecraft()
            .getTextureManager()
            .bindTexture(iceCreamMachineTexture);
        GL11.glPushMatrix();
        if (type != ItemRenderType.ENTITY) {
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        }
        if (type == ItemRenderType.INVENTORY) {
            GL11.glScalef(0.7F, 0.7F, 0.7F);
        } else {
            GL11.glScalef(0.9F, 0.9F, 0.9F);
        }
        GL11.glTranslatef(0F, 0.125F, 0F);
        iceCreamMachineModel.render();
        GL11.glPopMatrix();
    }

    private static boolean modelInitialized = false;
    private static ResourceLocation iceCreamMachineTexture;
    private static IVertexArrayObject iceCreamMachineModel;

    private static void initModel() {
        iceCreamMachineTexture = new ResourceLocation(GregTech.resourceDomain, "textures/model/ice_cream_machine.png");
        iceCreamMachineModel = WavefrontVBOBuilder
            .compileToVBO(new ResourceLocation(GregTech.resourceDomain, "textures/model/ice_cream_machine.obj"));
        modelInitialized = true;
    }

    @Override
    public void renderTESR(double x, double y, double z, float timeSinceLastTick) {
        if (!modelInitialized) {
            initModel();
            if (!modelInitialized) return;
        }
        Minecraft.getMinecraft()
            .getTextureManager()
            .bindTexture(iceCreamMachineTexture);
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y + 1.0, z + 0.5);
        GL11.glRotatef(getYRotationForFacing(mMainFacing), 0F, 1F, 0F);
        iceCreamMachineModel.render();
        GL11.glPopMatrix();
    }

    private static float getYRotationForFacing(ForgeDirection facing) {
        return switch (facing) {
            case NORTH -> 90F;
            case EAST -> 0F;
            case SOUTH -> 270F;
            default -> 180F;
        };
    }
}
