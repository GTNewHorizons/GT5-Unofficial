package gregtech.api.interfaces.metatileentity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.utils.item.IItemHandlerModifiable;
import com.gtnewhorizon.gtnhlib.capability.CapabilityProvider;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IGetGUITextureSet;
import gregtech.api.interfaces.tileentity.IGregTechDeviceInformation;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IGregtechWailaProvider;
import gregtech.api.interfaces.tileentity.IMachineBlockUpdateable;
import gregtech.api.render.ISBRInventoryContext;
import gregtech.api.render.ISBRWorldContext;

/**
 * Warning, this Interface has just been made to be able to add multiple kinds of MetaTileEntities (Cables, Pipes,
 * Transformers, but not the regular Blocks)
 * <p/>
 * Don't implement this yourself and expect it to work. Extend @MetaTileEntity itself.
 */
public interface IMetaTileEntity extends ISidedInventory, IFluidTank, IFluidHandler, IMachineBlockUpdateable,
    IGregtechWailaProvider, IGetGUITextureSet, IGregTechDeviceInformation, CapabilityProvider, IGuiHolder<PosGuiData> {

    /**
     * This determines the BaseMetaTileEntity belonging to this MetaTileEntity by using the Meta ID of the Block itself.
     * <p/>
     * // spotless:off
     * 0 = BaseMetaTileEntity, Wrench lvl 0 to dismantle
     * 1 = BaseMetaTileEntity, Wrench lvl 1 to dismantle
     * 2 = BaseMetaTileEntity, Wrench lvl 2 to dismantle
     * 3 = BaseMetaTileEntity, Wrench lvl 3 to dismantle
     * 4 = BaseMetaPipeEntity, Wrench lvl 0 to dismantle
     * 5 = BaseMetaPipeEntity, Wrench lvl 1 to dismantle
     * 6 = BaseMetaPipeEntity, Wrench lvl 2 to dismantle
     * 7 = BaseMetaPipeEntity, Wrench lvl 3 to dismantle
     * 8 = BaseMetaPipeEntity, Cutter lvl 0 to dismantle
     * 9 = BaseMetaPipeEntity, Cutter lvl 1 to dismantle
     * 10 = BaseMetaPipeEntity, Cutter lvl 2 to dismantle
     * 11 = BaseMetaPipeEntity, Cutter lvl 3 to dismantle
     * 12 = GT++ 13 = GT++ 14 = GT++ 15 = GT++
     * // spotless:on
     */
    byte getTileEntityBaseType();

    /**
     * @param aTileEntity is just because the internal Variable "mBaseMetaTileEntity" is set after this Call.
     * @return a newly created and ready MetaTileEntity
     */
    IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity);

    /**
     * @return an ItemStack representing this MetaTileEntity.
     */
    ItemStack getStackForm(long aAmount);

    /**
     * new getter for the BaseMetaTileEntity, which restricts usage to certain Functions.
     */
    IGregTechTileEntity getBaseMetaTileEntity();

    /**
     * Sets the BaseMetaTileEntity of this
     */
    void setBaseMetaTileEntity(IGregTechTileEntity aBaseMetaTileEntity);

    /**
     * when placing a Machine in World, to initialize default Modes. aNBT can be null!
     */
    void initDefaultModes(NBTTagCompound aNBT);

    /**
     * ^= writeToNBT
     */
    void saveNBTData(NBTTagCompound aNBT);

    /**
     * ^= readFromNBT
     */
    void loadNBTData(NBTTagCompound aNBT);

    /**
     * Adds the NBT-Information to the ItemStack, when being dismantled properly Used to store Machine specific Upgrade
     * Data.
     */
    void setItemNBT(NBTTagCompound aNBT);

    /**
     * Called in the registered MetaTileEntity when the Server starts, to reset static variables
     */
    void onServerStart();

    /**
     * Called to set Configuration values for this MetaTileEntity. Use aConfig.get(ConfigCategories.machineconfig,
     * "MetaTileEntityName.Ability", DEFAULT_VALUE); to set the Values.
     */
    void onConfigLoad();

    /**
     * If a Cover of that Type can be placed on this Side. Also Called when the Facing of the Block Changes and a Cover
     * is on said Side.
     */
    boolean allowCoverOnSide(ForgeDirection side, ItemStack coverItem);

    /**
     * When a Player right-clicks the Facing with a Screwdriver.
     */
    void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool);

    /**
     * When a Player right-clicks the Facing with a Wrench.
     */
    boolean onWrenchRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer entityPlayer, float aX,
        float aY, float aZ, ItemStack aTool);

    /**
     * When a Player right-clicks the Facing with a wire cutter.
     */
    boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer entityPlayer,
        float aX, float aY, float aZ, ItemStack aTool);

    /**
     * When a Player right-clicks the Facing with a soldering iron.
     */
    boolean onSolderingToolRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer entityPlayer,
        float aX, float aY, float aZ, ItemStack aTool);

    /**
     * Called right before this Machine explodes
     */
    void onExplosion();

    /**
     * The First processed Tick which was passed to this MetaTileEntity
     */
    void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity);

    /**
     * The Tick before all the generic handling happens, what gives a slightly faster reaction speed. Don't use this if
     * you really don't need to. @onPostTick is better suited for ticks. This happens still after the Cover handling.
     */
    void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick);

    /**
     * The Tick after all the generic handling happened. Recommended to use this like updateEntity.
     */
    void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick);

    /**
     * Called when this MetaTileEntity gets (intentionally) disconnected from the BaseMetaTileEntity. Doesn't get called
     * when this thing is moved by Frames or similar hacks.
     */
    void inValidate();

    /**
     * Called when the BaseMetaTileEntity gets invalidated, what happens right before the @inValidate above gets called
     */
    void onRemoval();

    /**
     * Called when the BaseMetaTileEntity gets unloaded (chunk or world)
     */
    default void onUnload() {}

    /**
     * @param facing the facing direction to check
     * @return if aFacing would be a valid Facing for this Device. Used for wrenching.
     */
    boolean isFacingValid(ForgeDirection facing);

    /**
     * From new ISidedInventory
     */
    boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side, ItemStack aStack);

    /**
     * From new ISidedInventory
     */
    boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side, ItemStack aStack);

    /**
     * @return if aIndex is a valid Slot. false for things like HoloSlots. Is used for determining if an Item is dropped
     *         upon Block destruction and for Inventory Access Management
     */
    boolean isValidSlot(int aIndex);

    /// Checks if the slot is an I/O slot that contributes to [GTItemSink#getStoredItemsInSink(ItemStackPredicate)].
    /// Also controls which slots block AE pattern pushes for blocking mode.
    default boolean isIOSlot(int slot) {
        return true;
    }

    /**
     * Gets the max stack size limit for a slot and a stack.
     *
     * @param slot  The slot, or -1 for a general 'lowest slot' query.
     * @param stack The stack, or null for a general 'any standard stack' query (getMaxStackSize() == 64).
     */
    default int getStackSizeLimit(int slot, @Nullable ItemStack stack) {
        return Math.min(getInventoryStackLimit(), stack == null ? 64 : stack.getMaxStackSize());
    }

    /**
     * Check if the item at the specified index should be dropped
     *
     * @param index Index that will be checked
     * @return True if the item at the index should be dropped, else false
     */
    boolean shouldDropItemAt(int index);

    /**
     * Override to change which items are dropped when block is broken.
     */
    ArrayList<ItemStack> getDroppedItem();

    /**
     * @return if aIndex can be set to Zero stackSize, when being removed.
     */
    boolean setStackToZeroInsteadOfNull(int aIndex);

    /**
     * If this Side can connect to inputting pipes
     */
    boolean isLiquidInput(ForgeDirection side);

    /**
     * If this Side can connect to outputting pipes
     */
    boolean isLiquidOutput(ForgeDirection side);

    /**
     * Just an Accessor for the Name variable.
     */
    String getMetaName();

    /**
     * @return true if the Machine can be accessed
     */
    boolean isAccessAllowed(EntityPlayer aPlayer);

    /**
     * a Player right-clicks the Machine Sneaky right clicks are not getting passed to this!
     *
     * @return mostly {@code false}. Probably is left for compatibility.
     */
    boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side, float aX,
        float aY, float aZ);

    /**
     * a Player leftclicks the Machine Sneaky leftclicks are getting passed to this unlike with the rightclicks.
     */
    void onLeftclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer);

    /**
     * Called Clientside with the Data got from @getUpdateData
     */
    void onValueUpdate(byte aValue);

    /**
     * return a small bit of Data, like a secondary Facing for example with this Function, for the Client. The
     * BaseMetaTileEntity detects changes to this Value and will then send an Update. This is only for Information,
     * which is visible as Texture to the outside.
     * <p/>
     * If you just want to have an Active/Redstone State then set the Active State inside the BaseMetaTileEntity
     * instead.
     */
    byte getUpdateData();

    /**
     * For the rare case you need this Function
     */
    void receiveClientEvent(byte aEventID, byte aValue);

    /**
     * Called to actually play the sound on client side. Client/Server check is already done.
     */
    void doSound(byte aIndex, double aX, double aY, double aZ);

    void startSoundLoop(byte aIndex, double aX, double aY, double aZ);

    void stopSoundLoop(byte aValue, double aX, double aY, double aZ);

    /**
     * Sends the Event for the Sound Triggers, only usable Server Side!
     */
    void sendSound(byte aIndex);

    /**
     * Sends the Event for the Sound Triggers, only usable Server Side!
     */
    void sendLoopStart(byte aIndex);

    /**
     * Sends the Event for the Sound Triggers, only usable Server Side!
     */
    void sendLoopEnd(byte aIndex);

    /**
     * Called when the Machine explodes. Override the Explosion code here.
     *
     * @param aExplosionPower explosion power
     */
    void doExplosion(long aExplosionPower);

    /**
     * If there should be a Lag Warning if something laggy happens during this Tick.
     * <p/>
     * The Advanced Pump uses this to not cause the Lag Message, while it scans for all close Fluids. The Item Pipes and
     * Retrievers neither send this Message, when scanning for Pipes.
     */
    boolean doTickProfilingMessageDuringThisTick();

    /**
     * returns the DebugLog
     */
    ArrayList<String> getSpecialDebugInfo(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, int aLogLevel,
        ArrayList<String> aList);

    /**
     * get a small Description
     */
    String[] getDescription();

    /**
     * In case the Output Voltage varies.
     */
    String getSpecialVoltageToolTip();

    /**
     * Icon of the Texture.
     *
     * @param side          is the Side of the Block
     * @param facing        is the direction the Block is facing
     * @param colorIndex    The Minecraft Color the Block is having
     * @param active        if the Machine is currently active (use this instead of calling
     *                      {@code mBaseMetaTileEntity.mActive)}. Note: In case of Pipes this means if this Side is
     *                      connected to something or not.
     * @param redstoneLevel if the Machine is currently outputting a RedstoneSignal (use this instead of calling
     *                      {@code mBaseMetaTileEntity.mRedstone} !!!)
     */
    ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel);

    /**
     * Register Icons here. This gets called when the Icons get initialized by the Base Block Best is you put your Icons
     * in a static Array for quick and easy access without relying on the MetaTileList.
     *
     * @param aBlockIconRegister The Block Icon Register
     */
    @SideOnly(Side.CLIENT)
    void registerIcons(IIconRegister aBlockIconRegister);

    /**
     * @return true if you override the Rendering.
     */
    @SideOnly(Side.CLIENT)
    boolean renderInInventory(ISBRInventoryContext ctx);

    /**
     * @return true if you override the Rendering.
     */
    @SideOnly(Side.CLIENT)
    boolean renderInWorld(ISBRWorldContext ctx);

    /**
     * Gets the Output for the comparator on the given Side
     */
    byte getComparatorValue(ForgeDirection side);

    float getExplosionResistance(ForgeDirection side);

    ItemStack[] getRealInventory();

    boolean connectsToItemPipe(ForgeDirection side);

    void onColorChangeServer(byte aColor);

    void onColorChangeClient(byte aColor);

    default NBTTagCompound getDescriptionData() {
        return null;
    }

    default void onDescriptionPacket(NBTTagCompound data) {

    }

    /**
     * @return Actual color shown on GUI
     */
    default int getGUIColorization() {
        if (getBaseMetaTileEntity() != null) {
            return getBaseMetaTileEntity().getGUIColorization();
        } else {
            return Dyes.MACHINE_METAL.toInt();
        }
    }

    int getLightOpacity();

    boolean allowGeneralRedstoneOutput();

    void addCollisionBoxesToList(World aWorld, int aX, int aY, int aZ, AxisAlignedBB inputAABB,
        List<AxisAlignedBB> outputAABB, Entity collider);

    AxisAlignedBB getCollisionBoundingBoxFromPool(World aWorld, int aX, int aY, int aZ);

    void onEntityCollidedWithBlock(World aWorld, int aX, int aY, int aZ, Entity collider);

    /**
     * The onCreated Function of the Item Class redirects here
     */
    void onCreated(ItemStack aStack, World aWorld, EntityPlayer aPlayer);

    boolean hasAlternativeModeText();

    String getAlternativeModeText();

    boolean shouldJoinIc2Enet();

    /**
     * The Machine Update, which is called when the Machine needs an Update of its Parts. I suggest to wait 1-5 seconds
     * before actually checking the Machine Parts. RP-Frames could for example cause Problems when you instacheck the
     * Machine Parts.
     * <p>
     * just do stuff since we are already in meta tile...
     */
    @Override
    void onMachineBlockUpdate();

    /**
     * just return in should recurse since we are already in meta tile...
     */
    @Override
    default boolean isMachineBlockUpdateRecursive() {
        return true;
    }

    /**
     * A randomly called display update to be able to add particles or other items for display
     *
     * @param aBaseMetaTileEntity The entity that will handle the {@see Block#randomDisplayTick}
     */
    @SideOnly(Side.CLIENT)
    default void onRandomDisplayTick(IGregTechTileEntity aBaseMetaTileEntity) {
        /* do nothing */
    }

    default int getGUIWidth() {
        return 176;
    }

    default int getGUIHeight() {
        return 166;
    }

    /*
     * ModularUI Support
     */
    default IItemHandlerModifiable getInventoryHandler() {
        return null;
    }

    default String getLocalName() {
        return StatCollector.translateToLocal("GT5U.gui.title.unknown");
    }

    default boolean doesBindPlayerInventory() {
        return true;
    }

    default int getTextColorOrDefault(String textType, int defaultColor) {
        return defaultColor;
    }

    /**
     * Called before block is destroyed. This is before inventory dropping code has executed.
     */
    default void onBlockDestroyed() {}

    /**
     * Allows to add additional data to the tooltip, which is specific to an instance of the machine
     *
     * @param stack   Item stack of this MTE
     * @param tooltip Tooltip to which can be added
     */
    default void addAdditionalTooltipInformation(ItemStack stack, List<String> tooltip) {}

    /**
     * Gets items to be displayed for HoloInventory mod.
     *
     * @return null if default implementation should be used, i.e. {@link IInventory#getStackInSlot}. Otherwise, a list
     *         of items to be displayed. Null element may be contained.
     */
    @Nullable
    default List<ItemStack> getItemsForHoloGlasses() {
        return null;
    }

    /**
     * Returns GUI ID used for resource packs as a distinguishable id to customize UI elements in MUI2.
     */
    String getGuiId();

    default void onTextureUpdate() {}
}
