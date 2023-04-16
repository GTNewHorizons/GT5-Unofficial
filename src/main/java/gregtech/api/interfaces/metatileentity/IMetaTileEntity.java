package gregtech.api.interfaces.metatileentity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;

import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IGetGUITextureSet;
import gregtech.api.interfaces.tileentity.IGearEnergyTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IGregtechWailaProvider;
import gregtech.api.interfaces.tileentity.IMachineBlockUpdateable;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.util.GT_Config;
import gregtech.api.util.GT_Util;
import gregtech.common.power.Power;

/**
 * Warning, this Interface has just been made to be able to add multiple kinds of MetaTileEntities (Cables, Pipes,
 * Transformers, but not the regular Blocks)
 * <p/>
 * Don't implement this yourself and expect it to work. Extend @MetaTileEntity itself.
 */
public interface IMetaTileEntity extends ISidedInventory, IFluidTank, IFluidHandler, IGearEnergyTileEntity,
    IMachineBlockUpdateable, IGregtechWailaProvider, IGetGUITextureSet {

    /**
     * This determines the BaseMetaTileEntity belonging to this MetaTileEntity by using the Meta ID of the Block itself.
     * <p/>
     * 0 = BaseMetaTileEntity, Wrench lvl 0 to dismantle 1 = BaseMetaTileEntity, Wrench lvl 1 to dismantle 2 =
     * BaseMetaTileEntity, Wrench lvl 2 to dismantle 3 = BaseMetaTileEntity, Wrench lvl 3 to dismantle 4 =
     * BaseMetaPipeEntity, Wrench lvl 0 to dismantle 5 = BaseMetaPipeEntity, Wrench lvl 1 to dismantle 6 =
     * BaseMetaPipeEntity, Wrench lvl 2 to dismantle 7 = BaseMetaPipeEntity, Wrench lvl 3 to dismantle 8 =
     * BaseMetaPipeEntity, Cutter lvl 0 to dismantle 9 = BaseMetaPipeEntity, Cutter lvl 1 to dismantle 10 =
     * BaseMetaPipeEntity, Cutter lvl 2 to dismantle 11 = BaseMetaPipeEntity, Cutter lvl 3 to dismantle 12 = GT++ 13 =
     * GT++ 14 = GT++ 15 = GT++
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
     * Adds the NBT-Information to the ItemStack, when being dismanteled properly Used to store Machine specific Upgrade
     * Data.
     */
    void setItemNBT(NBTTagCompound aNBT);

    /**
     * Called in the registered MetaTileEntity when the Server starts, to reset static variables
     */
    void onServerStart();

    /**
     * Called in the registered MetaTileEntity when the Server ticks a World the first time, to load things from the
     * World Save
     */
    void onWorldLoad(File aSaveDirectory);

    /**
     * Called in the registered MetaTileEntity when the Server stops, to save the Game.
     */
    void onWorldSave(File aSaveDirectory);

    /**
     * Called to set Configuration values for this MetaTileEntity. Use aConfig.get(ConfigCategories.machineconfig,
     * "MetaTileEntityName.Ability", DEFAULT_VALUE); to set the Values.
     */
    void onConfigLoad(GT_Config aConfig);

    /**
     * If a Cover of that Type can be placed on this Side. Also Called when the Facing of the Block Changes and a Cover
     * is on said Side.
     */
    boolean allowCoverOnSide(ForgeDirection aSide, GT_ItemStack aStack);

    /**
     * When a Player rightclicks the Facing with a Screwdriver.
     */
    void onScrewdriverRightClick(ForgeDirection aSide, EntityPlayer aPlayer, float aX, float aY, float aZ);

    /**
     * When a Player right-clicks the Facing with a Wrench.
     */
    boolean onWrenchRightClick(ForgeDirection sideDirection, ForgeDirection wrenchingSideDirection, EntityPlayer entityPlayer, float aX,
        float aY, float aZ);

    /**
     * When a Player right-clicks the Facing with a wire cutter.
     */
    boolean onWireCutterRightClick(ForgeDirection sideDirection, ForgeDirection wrenchingSideDirection, EntityPlayer entityPlayer, float aX,
        float aY, float aZ);

    /**
     * When a Player right-clicks the Facing with a soldering iron.
     */
    boolean onSolderingToolRightClick(ForgeDirection sideDirection, ForgeDirection wrenchingSideDirection, EntityPlayer entityPlayer,
        float aX, float aY, float aZ);

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
     * @param facingDirection to test
     * @return if aFacing would be a valid Facing for this Device. Used for wrenching.
     */
    boolean isFacingValid(ForgeDirection facingDirection);

    /**
     * @return the Server Side Container
     * @deprecated Use ModularUI
     */
    @Deprecated
    default Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        throw new UnsupportedOperationException();
    }

    /**
     * @return the Client Side GUI Container
     * @deprecated Use ModularUI
     */
    @Deprecated
    default Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        throw new UnsupportedOperationException();
    }

    /**
     * For back compatibility, you need to override this if this MetaTileEntity uses ModularUI.
     */
    default boolean useModularUI() {
        return false;
    }

    /**
     * From new ISidedInventory
     */
    boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection aSide, ItemStack aStack);

    /**
     * From new ISidedInventory
     */
    boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection aSide, ItemStack aStack);

    /**
     * @return if aIndex is a valid Slot. false for things like HoloSlots. Is used for determining if an Item is dropped
     *         upon Block destruction and for Inventory Access Management
     */
    boolean isValidSlot(int aIndex);

    /**
     * Check if the item at the specified index should be dropped
     *
     * @param index Index that will be checked
     * @return True if the item at the index should be dropped, else false
     */
    boolean shouldDropItemAt(int index);

    /**
     * @return if aIndex can be set to Zero stackSize, when being removed.
     */
    boolean setStackToZeroInsteadOfNull(int aIndex);

    /**
     * If this Side can connect to inputting pipes
     */
    boolean isLiquidInput(ForgeDirection aSide);

    /**
     * If this Side can connect to outputting pipes
     */
    boolean isLiquidOutput(ForgeDirection aSide);

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
     * @return
     */
    boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection aSide, float aX,
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
     * Called to actually play the Sound. Do not insert Client/Server checks. That is already done for you. Do not
     * use @playSoundEffect, Minecraft doesn't like that at all. Use @playSound instead.
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
     * Called when the Machine explodes, override Explosion Code here.
     *
     * @param aExplosionPower
     */
    void doExplosion(long aExplosionPower);

    /**
     * If this is just a simple Machine, which can be wrenched at 100%
     */
    boolean isSimpleMachine();

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
     * @return Power object used for displaying in NEI
     */
    default Power getPower() {
        return null;
    }

    /**
     * Icon of the Texture. If this returns null then it falls back to getTextureIndex.
     *
     * @param sideDirection     is the Side of the Block
     * @param facingDirection   is the direction the Block is facing
     * @param colorIndex        The Minecraft Color the Block is having
     * @param active            if the Machine is currently active (use this instead of calling
     *                          {@code mBaseMetaTileEntity.mActive)}. Note: In case of Pipes this means if this Side is
     *                          connected to something or not.
     * @param redstoneLevel     if the Machine is currently outputting a RedstoneSignal (use this instead of calling
     *                          {@code mBaseMetaTileEntity.mRedstone} !!!)
     */
    ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
                          ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel);

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
    boolean renderInInventory(Block aBlock, int aMeta, RenderBlocks aRenderer);

    /**
     * @return true if you override the Rendering.
     */
    @SideOnly(Side.CLIENT)
    boolean renderInWorld(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock, RenderBlocks aRenderer);

    /**
     * Gets the Output for the comparator on the given Side
     */
    byte getComparatorValue(ForgeDirection aSide);

    float getExplosionResistance(ForgeDirection aSide);

    String[] getInfoData();

    boolean isGivingInformation();

    ItemStack[] getRealInventory();

    boolean connectsToItemPipe(ForgeDirection aSide);

    void onColorChangeServer(byte aColor);

    void onColorChangeClient(byte aColor);

    /**
     * @return Actual color shown on GUI
     */
    default int getGUIColorization() {
        if (getBaseMetaTileEntity() != null) {
            return getBaseMetaTileEntity().getGUIColorization();
        } else {
            return GT_Util.getRGBInt(Dyes.MACHINE_METAL.getRGBA());
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
     *
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
    default ItemStackHandler getInventoryHandler() {
        return null;
    }

    default String getLocalName() {
        return "Unknown";
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
}
