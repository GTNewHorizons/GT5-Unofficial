package gregtech.api.metatileentity;

import static gregtech.api.enums.GTValues.E;
import static gregtech.api.enums.GTValues.NW;
import static gregtech.api.util.GTLanguageManager.FACES;
import static gregtech.api.util.GTLanguageManager.getTranslation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.ItemDrawable;
import com.gtnewhorizons.modularui.api.math.MainAxisAlignment;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.Column;
import com.gtnewhorizons.modularui.common.widget.MultiChildWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
import gregtech.api.covers.CoverRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GUITextureSet;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregtechWailaProvider;
import gregtech.api.net.GTPacketRequestCoverData;
import gregtech.api.net.GTPacketSendCoverData;
import gregtech.api.objects.GTItemStack;
import gregtech.api.util.CoverBehaviorBase;
import gregtech.api.util.ISerializableObject;
import gregtech.common.GTClient;
import gregtech.common.covers.CoverInfo;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public abstract class CoverableTileEntity extends BaseTileEntity implements ICoverable, IGregtechWailaProvider {

    public static final String[] COVER_DATA_NBT_KEYS = Arrays.stream(ForgeDirection.VALID_DIRECTIONS)
        .mapToInt(Enum::ordinal)
        .mapToObj(i -> "mCoverData" + i)
        .toArray(String[]::new);
    private static final String[] COVER_DIRECTION_NAMES = new String[] { "GT5U.interface.coverTabs.down",
        "GT5U.interface.coverTabs.up", "GT5U.interface.coverTabs.north", "GT5U.interface.coverTabs.south",
        "GT5U.interface.coverTabs.west", "GT5U.interface.coverTabs.east" };

    // New Cover Information
    protected final CoverInfo[] coverInfos = new CoverInfo[] { null, null, null, null, null, null };
    private byte validCoversMask;

    protected byte[] mSidedRedstone = new byte[] { 15, 15, 15, 15, 15, 15 };
    protected boolean mRedstone = false;
    protected byte mStrongRedstone = 0;

    protected short mID = 0;
    public long mTickTimer = 0;

    protected void writeCoverNBT(NBTTagCompound aNBT, boolean isDrop) {
        final NBTTagList tList = new NBTTagList();

        for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            final CoverInfo coverInfo = getCoverInfoAtSide(side);
            if (!coverInfo.isValid()) continue;

            // Backwards compat, in case of a revert... for now
            tList.appendTag(coverInfo.writeToNBT(new NBTTagCompound()));
            aNBT.setTag(
                COVER_DATA_NBT_KEYS[side.ordinal()],
                coverInfo.getCoverData()
                    .saveDataToNBT());
        }
        if (tList.tagCount() > 0) {
            aNBT.setTag(GTValues.NBT.COVERS, tList);
        }

        if (mStrongRedstone > 0) aNBT.setByte("mStrongRedstone", mStrongRedstone);

        if (!isDrop) {
            aNBT.setByteArray("mRedstoneSided", mSidedRedstone);
            aNBT.setBoolean("mRedstone", mRedstone);
        }
    }

    protected void readCoverNBT(NBTTagCompound aNBT) {
        mRedstone = aNBT.getBoolean("mRedstone");
        mSidedRedstone = aNBT.hasKey("mRedstoneSided") ? aNBT.getByteArray("mRedstoneSided")
            : new byte[] { 15, 15, 15, 15, 15, 15 };
        mStrongRedstone = aNBT.getByte("mStrongRedstone");

        if (aNBT.hasKey(GTValues.NBT.COVERS)) {
            readCoverInfoNBT(aNBT);
        }
    }

    public void readCoverInfoNBT(NBTTagCompound aNBT) {
        final NBTTagList tList = aNBT.getTagList(GTValues.NBT.COVERS, 10);
        for (byte i = 0; i < tList.tagCount(); i++) {
            final NBTTagCompound tNBT = tList.getCompoundTagAt(i);
            final CoverInfo coverInfo = new CoverInfo(this, tNBT);
            this.setCoverInfoAtSide(coverInfo.getSide(), coverInfo);
            if (coverInfo.isDataNeededOnClient()) issueCoverUpdate(ForgeDirection.getOrientation(i));
        }
    }

    public abstract boolean isStillValid();

    protected boolean doCoverThings() {
        byte validCoversMask = this.validCoversMask;
        if (validCoversMask == 0) return true;

        for (int i = Integer.numberOfTrailingZeros(validCoversMask); i < 6; i++) {
            if (((validCoversMask >>> i) & 1) == 0) continue;
            if (!tickCoverAtSide(ForgeDirection.VALID_DIRECTIONS[i])) return false;
        }

        return true;
    }

    protected void onCoverUnload() {
        byte validCoversMask = this.validCoversMask;
        if (validCoversMask == 0) return;

        for (int i = Integer.numberOfTrailingZeros(validCoversMask); i < 6; i++) {
            if (((validCoversMask >>> i) & 1) == 0) continue;
            onCoverUnloadAtSide(ForgeDirection.VALID_DIRECTIONS[i]);
        }
    }

    public void onCoverUnloadAtSide(ForgeDirection side) {
        final CoverInfo coverInfo = getCoverInfoAtSide(side);
        if (!coverInfo.isValid()) return;
        coverInfo.onCoverUnload();
    }

    public boolean tickCoverAtSide(ForgeDirection side) {
        return tickCoverAtSide(side, mTickTimer);
    }

    /**
     * @return {@code false} if the tile is no longer valid after ticking the cover
     */
    public boolean tickCoverAtSide(ForgeDirection side, long aTickTimer) {
        final CoverInfo coverInfo = getCoverInfoAtSide(side);
        if (!coverInfo.isValid()) return true;
        final int tCoverTickRate = coverInfo.getTickRate();
        if (tCoverTickRate > 0 && aTickTimer % tCoverTickRate == 0) {
            final byte tRedstone = coverInfo.isRedstoneSensitive(aTickTimer) ? getInputRedstoneSignal(side) : 0;
            coverInfo.setCoverData(coverInfo.doCoverThings(aTickTimer, tRedstone));
            return isStillValid();
        }

        return true;
    }

    public abstract boolean allowCoverOnSide(ForgeDirection side, GTItemStack aCoverID);

    protected void checkDropCover() {
        for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            final int coverId = getCoverIDAtSide(side);
            if (coverId != 0 && !allowCoverOnSide(side, new GTItemStack(coverId))) dropCover(side, side, true);
        }
    }

    @Override
    public void issueCoverUpdate(ForgeDirection side) {
        final CoverInfo coverInfo = getCoverInfoAtSide(side);
        if (worldObj == null || (isServerSide() && coverInfo.isDataNeededOnClient())) coverInfo.setNeedsUpdate(true);
    }

    public final ITexture getCoverTexture(ForgeDirection side) {
        final CoverInfo coverInfo = getCoverInfoAtSide(side);
        if (!coverInfo.isValid()) return null;
        if (GTMod.instance.isClientSide() && (GTClient.hideValue & 0x1) != 0) {
            return Textures.BlockIcons.HIDDEN_TEXTURE[0]; // See through
        }
        final ITexture coverTexture = (!(this instanceof BaseMetaPipeEntity)) ? coverInfo.getSpecialCoverFGTexture()
            : coverInfo.getSpecialCoverTexture();

        return coverTexture != null ? coverTexture : CoverRegistry.getCoverTexture(getCoverIDAtSide(side));
    }

    protected void requestCoverDataIfNeeded() {
        if (worldObj == null || !worldObj.isRemote) return;
        for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            final CoverInfo coverInfo = getCoverInfoAtSide(side);
            if (coverInfo.isDataNeededOnClient()) NW.sendToServer(new GTPacketRequestCoverData(coverInfo, this));
        }
    }

    @Override
    public void setCoverIdAndDataAtSide(ForgeDirection side, int aId, ISerializableObject aData) {
        if (setCoverIDAtSideNoUpdate(side, aId, aData)) {
            issueCoverUpdate(side);
            issueBlockUpdate();
        }
    }

    @Override
    public void setCoverIDAtSide(ForgeDirection side, int aID) {
        setCoverIdAndDataAtSide(side, aID, null);
    }

    @Override
    public boolean setCoverIDAtSideNoUpdate(ForgeDirection side, int aID) {
        return setCoverIDAtSideNoUpdate(side, aID, null);
    }

    public boolean setCoverIDAtSideNoUpdate(ForgeDirection side, int aID, ISerializableObject aData) {
        final CoverInfo oldCoverInfo = getCoverInfoAtSide(side);
        if (side != ForgeDirection.UNKNOWN && oldCoverInfo.getCoverID() != aID) {
            if (aID == 0 && isClientSide()) oldCoverInfo.onDropped();
            setCoverInfoAtSide(side, new CoverInfo(side, aID, this, aData));
            return true;
        }
        return false;
    }

    @Override
    @Deprecated
    public void setCoverDataAtSide(ForgeDirection side, int aData) {
        final CoverInfo coverInfo = getCoverInfoAtSide(side);
        if (coverInfo.isValid() && coverInfo.getCoverData() instanceof ISerializableObject.LegacyCoverData)
            coverInfo.setCoverData(new ISerializableObject.LegacyCoverData(aData));
    }

    @Override
    public void setCoverDataAtSide(ForgeDirection side, ISerializableObject aData) {
        final CoverInfo coverInfo = getCoverInfoAtSide(side);
        if (coverInfo.isValid() && coverInfo.getCoverBehavior()
            .cast(aData) != null) coverInfo.setCoverData(aData);
    }

    @Override
    public void attachCover(EntityPlayer aPlayer, ItemStack aCover, ForgeDirection side) {
        CoverBehaviorBase<?> coverBehavior = CoverRegistry.getCoverBehaviorNew(aCover);
        coverBehavior.placeCover(side, aCover, this);
        coverBehavior.onPlayerAttach(aPlayer, aCover, this, side);
    }

    @Override
    public int getCoverIDAtSide(ForgeDirection side) {
        return getCoverInfoAtSide(side).getCoverID();
    }

    @Override
    public ItemStack getCoverItemAtSide(ForgeDirection side) {
        return getCoverInfoAtSide(side).getDisplayStack();
    }

    public final void setCoverInfoAtSide(ForgeDirection side, CoverInfo coverInfo) {
        if (side != ForgeDirection.UNKNOWN) {
            coverInfos[side.ordinal()] = coverInfo;

            validCoversMask &= (byte) ~side.flag;
            if (coverInfo.isValid()) validCoversMask |= side.flag;
        }
    }

    @Override
    public final CoverInfo getCoverInfoAtSide(ForgeDirection side) {
        final int ordinalSide = side.ordinal();
        if (side != ForgeDirection.UNKNOWN) {
            CoverInfo coverInfo = coverInfos[ordinalSide];
            if (coverInfo == null) coverInfo = (coverInfos[ordinalSide] = new CoverInfo(side, this));
            return coverInfo;
        }
        return CoverInfo.EMPTY_INFO;
    }

    public void clearCoverInfoAtSide(ForgeDirection side) {
        if (side != ForgeDirection.UNKNOWN) {
            setCoverIDAtSide(side, 0);
        }
    }

    @Override
    public boolean dropCover(ForgeDirection side, ForgeDirection droppedSide, boolean aForced) {
        final CoverInfo coverInfo = getCoverInfoAtSide(side);
        if (!coverInfo.isValid()) return false;
        if (!coverInfo.onCoverRemoval(aForced) && !aForced) return false;
        final ItemStack tStack = coverInfo.getDrop();
        if (tStack != null) {
            coverInfo.onDropped();
            final EntityItem tEntity = new EntityItem(
                worldObj,
                getOffsetX(droppedSide, 1) + 0.5,
                getOffsetY(droppedSide, 1) + 0.5,
                getOffsetZ(droppedSide, 1) + 0.5,
                tStack);
            tEntity.motionX = 0;
            tEntity.motionY = 0;
            tEntity.motionZ = 0;
            worldObj.spawnEntityInWorld(tEntity);
        }
        clearCoverInfoAtSide(side);
        updateOutputRedstoneSignal(side);

        return true;
    }

    protected void onBaseTEDestroyed() {
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            final CoverInfo coverInfo = getCoverInfoAtSide(side);
            if (coverInfo.isValid()) coverInfo.onBaseTEDestroyed();
        }
    }

    @Override
    public void setOutputRedstoneSignal(ForgeDirection side, byte strength) {
        final byte cappedStrength = (byte) Math.min(Math.max(0, strength), 15);
        if (side == ForgeDirection.UNKNOWN) return;

        final int ordinalSide = side.ordinal();
        if (mSidedRedstone[ordinalSide] != cappedStrength || (mStrongRedstone & (1 << ordinalSide)) > 0) {
            mSidedRedstone[ordinalSide] = cappedStrength;
            issueBlockUpdate();
        }
    }

    @Override
    public void setStrongOutputRedstoneSignal(ForgeDirection side, byte strength) {
        mStrongRedstone |= (byte) side.flag;
        setOutputRedstoneSignal(side, strength);
    }

    @Override
    public void setRedstoneOutputStrength(ForgeDirection side, boolean isStrong) {
        if (isStrong) {
            mStrongRedstone |= (byte) side.flag;
        } else {
            mStrongRedstone &= ~(byte) side.flag;
        }
        setOutputRedstoneSignal(side, mSidedRedstone[side.ordinal()]);
    }

    @Override
    public boolean getRedstoneOutputStrength(ForgeDirection side) {
        return (mStrongRedstone & side.flag) != 0;
    }

    @Override
    public void setInternalOutputRedstoneSignal(ForgeDirection side, byte aStrength) {
        if (!getCoverInfoAtSide(side).manipulatesSidedRedstoneOutput()) setOutputRedstoneSignal(side, aStrength);
    }

    @Override
    public boolean getRedstone() {
        return Arrays.stream(ForgeDirection.VALID_DIRECTIONS)
            .anyMatch(this::getRedstone);
    }

    @Override
    public boolean getRedstone(ForgeDirection side) {
        return getInternalInputRedstoneSignal(side) > 0;
    }

    @Override
    public byte getStrongestRedstone() {
        return Arrays.stream(ForgeDirection.VALID_DIRECTIONS)
            .map(this::getInternalInputRedstoneSignal)
            .max(Comparator.comparing(Byte::valueOf))
            .orElse((byte) 0);
    }

    @Override
    public byte getStrongOutputRedstoneSignal(ForgeDirection side) {
        final int ordinalSide = side.ordinal();
        return side != ForgeDirection.UNKNOWN && (mStrongRedstone & (1 << ordinalSide)) != 0
            ? (byte) (mSidedRedstone[ordinalSide] & 15)
            : 0;
    }

    @Override
    public void setGenericRedstoneOutput(boolean aOnOff) {
        mRedstone = aOnOff;
    }

    @Override
    public byte getInternalInputRedstoneSignal(ForgeDirection side) {
        return (byte) (getCoverInfoAtSide(side).getRedstoneInput(getInputRedstoneSignal(side)) & 15);
    }

    @Override
    public byte getInputRedstoneSignal(ForgeDirection side) {
        return (byte) (worldObj
            .getIndirectPowerLevelTo(getOffsetX(side, 1), getOffsetY(side, 1), getOffsetZ(side, 1), side.ordinal())
            & 15);
    }

    @Override
    public byte getOutputRedstoneSignal(ForgeDirection side) {
        return getCoverInfoAtSide(side).manipulatesSidedRedstoneOutput() ? mSidedRedstone[side.ordinal()]
            : getGeneralRS(side);
    }

    protected void updateOutputRedstoneSignal(ForgeDirection side) {
        setOutputRedstoneSignal(side, (byte) 0);
    }

    @Override
    public void receiveCoverData(ForgeDirection coverSide, int aCoverID, int aCoverData) {
        if (coverSide == ForgeDirection.UNKNOWN) return;
        final CoverInfo oldCoverInfo = getCoverInfoAtSide(coverSide);
        if (!oldCoverInfo.isValid()) return;

        setCoverIDAtSideNoUpdate(coverSide, aCoverID);
        setCoverDataAtSide(coverSide, aCoverData);
    }

    @Override
    public void receiveCoverData(ForgeDirection coverSide, int aCoverID, ISerializableObject aCoverData,
        EntityPlayerMP aPlayer) {
        if (coverSide == ForgeDirection.UNKNOWN) return;

        final CoverInfo oldCoverInfo = getCoverInfoAtSide(coverSide);

        if (!oldCoverInfo.isValid()) return;
        oldCoverInfo.preDataChanged(aCoverID, aCoverData);
        setCoverIDAtSideNoUpdate(coverSide, aCoverID, aCoverData);
        setCoverDataAtSide(coverSide, aCoverData);

        if (isClientSide()) {
            getCoverInfoAtSide(coverSide).onDataChanged();
        }
    }

    protected void sendCoverDataIfNeeded() {
        if (worldObj == null || worldObj.isRemote) return;
        for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            final CoverInfo coverInfo = getCoverInfoAtSide(side);
            if (coverInfo.needsUpdate()) {
                NW.sendPacketToAllPlayersInRange(worldObj, new GTPacketSendCoverData(coverInfo, this), xCoord, zCoord);
                coverInfo.setNeedsUpdate(false);
            }
        }
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        final NBTTagCompound tag = accessor.getNBTData();
        final ForgeDirection currentFacing = accessor.getSide();

        final NBTTagList tList = tag.getTagList(GTValues.NBT.COVERS, 10);
        for (byte i = 0; i < tList.tagCount(); i++) {
            final NBTTagCompound tNBT = tList.getCompoundTagAt(i);
            final CoverInfo coverInfo = new CoverInfo(this, tNBT);
            if (!coverInfo.isValid()) continue;

            final ItemStack coverStack = coverInfo.getDisplayStack();
            if (coverStack != null) {
                currentTip.add(
                    StatCollector.translateToLocalFormatted(
                        "GT5U.waila.cover",
                        currentFacing == coverInfo.getSide()
                            ? StatCollector.translateToLocal("GT5U.waila.cover.current_facing")
                            : StatCollector.translateToLocal(
                                "GT5U.interface.coverTabs." + coverInfo.getSide()
                                    .toString()
                                    .toLowerCase()),
                        coverStack.getDisplayName()));
                final String behaviorDesc = coverInfo.getBehaviorDescription();
                if (!Objects.equals(behaviorDesc, E)) currentTip.add(behaviorDesc);
            }
        }

        // No super implementation
        // super.getWailaBody(itemStack, currenttip, accessor, config);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        // No super implementation
        // super.getWailaNBTData(player, tile, tag, world, x, y, z);

        // While we have some cover data on the client (enough to render it); we don't have all the information we want,
        // such as details on the fluid filter, so send it all here.
        writeCoverNBT(tag, false);
    }

    /**
     * Add installed cover information, generally called from ItemBlock
     *
     * @param aNBT  - NBTTagCompound from the stack
     * @param aList - List to add the information to
     */
    public static void addInstalledCoversInformation(NBTTagCompound aNBT, List<String> aList) {
        if (aNBT == null || aList == null) return;
        final NBTTagList tList = aNBT.getTagList(GTValues.NBT.COVERS, 10);

        for (byte i = 0; i < tList.tagCount(); i++) {
            final NBTTagCompound tNBT = tList.getCompoundTagAt(i);
            final CoverInfo coverInfo = new CoverInfo(null, tNBT);
            if (!coverInfo.isValid()) continue;

            final ItemStack coverStack = coverInfo.getDisplayStack();
            if (coverStack != null) {
                aList.add(
                    String.format(
                        "Cover on %s side: %s",
                        getTranslation(
                            FACES[coverInfo.getSide()
                                .ordinal()]),
                        coverStack.getDisplayName()));
            }
        }

        byte strongRedstone = aNBT.getByte("mStrongRedstone");

        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            if ((strongRedstone & dir.flag) != 0) {
                aList.add(String.format("Emits a strong redstone signal on the %s side", switch (dir) {
                    case DOWN -> "bottom";
                    case UP -> "top";
                    case NORTH -> "north";
                    case SOUTH -> "south";
                    case WEST -> "west";
                    case EAST -> "east";
                    default -> "<unknown>";
                }));
            }
        }
    }

    protected ModularWindow createCoverWindow(EntityPlayer player, ForgeDirection side) {
        return getCoverInfoAtSide(side).createWindow(player);
    }

    protected static final int COVER_WINDOW_ID_START = 1;

    @Override
    public void addCoverTabs(ModularWindow.Builder builder, UIBuildContext buildContext) {
        final int COVER_TAB_LEFT = -16, COVER_TAB_TOP = 1, COVER_TAB_HEIGHT = 20, COVER_TAB_WIDTH = 18,
            COVER_TAB_SPACING = 2, ICON_SIZE = 16;
        final boolean flipHorizontally = GTMod.gregtechproxy.mCoverTabsFlipped;

        final Column columnWidget = new Column();
        builder.widget(columnWidget);
        final int xPos = flipHorizontally ? (getGUIWidth() - COVER_TAB_LEFT - COVER_TAB_WIDTH) : COVER_TAB_LEFT;
        if (GTMod.gregtechproxy.mCoverTabsVisible) {
            columnWidget.setPos(xPos, COVER_TAB_TOP)
                .setEnabled(
                    widget -> ((Column) widget).getChildren()
                        .stream()
                        .anyMatch(Widget::isEnabled));
        } else {
            columnWidget.setEnabled(false);
        }
        columnWidget.setAlignment(MainAxisAlignment.SPACE_BETWEEN)
            .setSpace(COVER_TAB_SPACING);

        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
            buildContext.addSyncedWindow(
                direction.ordinal() + COVER_WINDOW_ID_START,
                player -> createCoverWindow(player, direction));
            columnWidget.addChild(new MultiChildWidget().addChild(new ButtonWidget() {

                @Override
                public IDrawable[] getBackground() {
                    final List<IDrawable> backgrounds = new ArrayList<>();
                    final GUITextureSet tabIconSet = getGUITextureSet();

                    if (getCoverInfoAtSide(direction).hasCoverGUI()) {
                        if (isHovering()) {
                            backgrounds.add(
                                flipHorizontally ? tabIconSet.getCoverTabHighlightFlipped()
                                    : tabIconSet.getCoverTabHighlight());
                        } else {
                            backgrounds.add(
                                flipHorizontally ? tabIconSet.getCoverTabNormalFlipped()
                                    : tabIconSet.getCoverTabNormal());
                        }
                    } else {
                        backgrounds.add(
                            flipHorizontally ? tabIconSet.getCoverTabDisabledFlipped()
                                : tabIconSet.getCoverTabDisabled());
                    }
                    return backgrounds.toArray(new IDrawable[] {});
                }
            }.setOnClick((clickData, widget) -> onTabClicked(clickData, widget, direction))
                .dynamicTooltip(() -> getCoverTabTooltip(direction))
                .setSize(COVER_TAB_WIDTH, COVER_TAB_HEIGHT))
                .addChild(
                    new ItemDrawable(() -> getCoverItemAtSide(direction)).asWidget()
                        .setPos(
                            (COVER_TAB_WIDTH - ICON_SIZE) / 2 + (flipHorizontally ? -1 : 1),
                            (COVER_TAB_HEIGHT - ICON_SIZE) / 2))
                .setEnabled(widget -> getCoverItemAtSide(direction) != null));
        }
    }

    @SideOnly(Side.CLIENT)
    protected List<String> getCoverTabTooltip(ForgeDirection side) {
        final CoverInfo coverInfo = getCoverInfoAtSide(side);
        final ItemStack coverItem = coverInfo.getDisplayStack();
        if (coverItem == null) return Collections.emptyList();
        final boolean coverHasGUI = coverInfo.hasCoverGUI();

        final Minecraft mc = Minecraft.getMinecraft();
        final List<String> tooltip = coverItem.getTooltip(mc.thePlayer, mc.gameSettings.advancedItemTooltips);
        final ImmutableList.Builder<String> builder = ImmutableList.builder();
        builder.add(
            (coverHasGUI ? EnumChatFormatting.UNDERLINE : EnumChatFormatting.DARK_GRAY)
                + StatCollector.translateToLocal(COVER_DIRECTION_NAMES[side.ordinal()])
                + (coverHasGUI ? EnumChatFormatting.RESET + ": " : ": " + EnumChatFormatting.RESET)
                + tooltip.get(0));
        builder.addAll(coverInfo.getAdditionalTooltip(coverInfo.getCoverData()));
        builder.addAll(
            IntStream.range(1, tooltip.size())
                .mapToObj(index -> EnumChatFormatting.GRAY + tooltip.get(index))
                .iterator());
        return builder.build();
    }

    protected void onTabClicked(Widget.ClickData ignoredClickData, Widget widget, ForgeDirection side) {
        if (isClientSide()) return;
        widget.getContext()
            .openSyncedWindow(side.ordinal() + COVER_WINDOW_ID_START);
    }
}
