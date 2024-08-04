package gregtech.common.tileentities.machines.multi.drone;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.util.Vec3Impl;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.TextFieldWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IMachineBlockUpdateable;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Maintenance;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.render.TextureFactory;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class GT_MetaTileEntity_Hatch_DroneDownLink extends GT_MetaTileEntity_Hatch_Maintenance {

    private Vec3Impl downlinkCoord;
    private DroneConnection connection;
    // This has to be existed for doing random damage.
    private GT_MetaTileEntity_MultiBlockBase machine;
    private static final IIconContainer moduleActive = new Textures.BlockIcons.CustomIcon(
        "iconsets/OVERLAY_DRONE_MODULE_ACTIVE");

    public GT_MetaTileEntity_Hatch_DroneDownLink(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public GT_MetaTileEntity_Hatch_DroneDownLink(String aName, int aTier, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures, false);
    }

    @Override
    public String[] getDescription() {
        return new String[] { "Built-in powerful navigation beacon!" };
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(moduleActive) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(moduleActive) };
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_DroneDownLink(
            this.mName,
            this.mTier,
            this.mDescriptionArray,
            this.mTextures);
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        downlinkCoord = new Vec3Impl(
            getBaseMetaTileEntity().getXCoord(),
            getBaseMetaTileEntity().getYCoord(),
            getBaseMetaTileEntity().getZCoord());
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (hasConnection()) {
                if (connection.centre.getBaseMetaTileEntity()
                    .isActive()) {
                    doNormalMaintain();
                } else {
                    // Centre offline? ...do nothing.
                    // machine.causeMaintenanceIssue();
                }
            } else {
                // If the connection invalid, set it to null.
                // Find connection every 10 second
                if (aTick % 200 == 0) {
                    connection = null;
                    tryFindConnection();
                    // Let's have some "surprise". Sorry, surprise party is over.
                    // if (this.machine != null && this.machine.isValid()) {
                    // machine.causeMaintenanceIssue();
                }
            }
        }
    }

    private void doNormalMaintain() {
        this.mWrench = this.mScrewdriver = this.mSoftHammer = this.mHardHammer = this.mCrowbar = this.mSolderingTool = true;
        connection.machine.mWrench = connection.machine.mScrewdriver = connection.machine.mSoftHammer = connection.machine.mHardHammer = connection.machine.mCrowbar = connection.machine.mSolderingTool = true;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side,
        float aX, float aY, float aZ) {
        if (aBaseMetaTileEntity.isClientSide()) return true;
        if (side == aBaseMetaTileEntity.getFrontFacing()) {
            if (aPlayer instanceof FakePlayer) return false;
            if (connection == null || !connection.isValid()) {
                aPlayer.addChatComponentMessage(new ChatComponentTranslation("GT5U.machines.dronecentre.noconnection"));
                return false;
            }
            GT_UIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
            return true;
        }
        return false;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public void onRemoval() {
        if (hasConnection()) connection.machine = null;
    }

    private boolean hasConnection() {
        if (connection == null) return false;
        if (connection.isValid()) return true;
        return connection.reCheckConnection();
    }

    /**
     * Find a drone connection. This will search for all DC in the same dimension, then find one in range.
     */
    private void tryFindConnection() {
        if (GT_MetaTileEntity_DroneCentre.getCentreMap()
            .containsKey(getBaseMetaTileEntity().getWorld().provider.dimensionId)) {
            List<GT_MetaTileEntity_DroneCentre> target = GT_MetaTileEntity_DroneCentre.getCentreMap()
                .get(getBaseMetaTileEntity().getWorld().provider.dimensionId)
                .stream()
                .collect(Collectors.toList());
            for (GT_MetaTileEntity_DroneCentre centre : target) {
                if (centre.getCoords()
                    .withinDistance(this.downlinkCoord, centre.getRange())
                    && centre.getBaseMetaTileEntity()
                        .isActive()) {
                    GT_MetaTileEntity_MultiBlockBase machine = tryFindCoreGTMultiBlock();
                    if (machine != null && machine.isValid()) {
                        this.machine = machine;
                        connection = new DroneConnection(machine, centre);
                        connection.centre.getConnectionList()
                            .add(connection);
                        return;
                    }
                }
            }
        }
    }

    // Find mainframe. Mainly from a method in GT_API——This will cause performance issue! Do not call it frequently.
    private GT_MetaTileEntity_MultiBlockBase tryFindCoreGTMultiBlock() {
        Queue<ChunkCoordinates> tQueue = new LinkedList<>();
        Set<ChunkCoordinates> visited = new HashSet<>(80);
        tQueue.add(
            this.getBaseMetaTileEntity()
                .getCoords());
        World world = this.getBaseMetaTileEntity()
            .getWorld();
        while (!tQueue.isEmpty()) {
            final ChunkCoordinates aCoords = tQueue.poll();
            final TileEntity tTileEntity;
            final boolean isMachineBlock;
            tTileEntity = world.getTileEntity(aCoords.posX, aCoords.posY, aCoords.posZ);
            Block block = world.getBlock(aCoords.posX, aCoords.posY, aCoords.posZ);
            // Plascrete block isn't registered as machineBlock, therefore we have to check it manually so that drone
            // can work with cleanroom.
            // Todo: loading cleanroom's config for other blocks
            isMachineBlock = GregTech_API
                .isMachineBlock(block, world.getBlockMetadata(aCoords.posX, aCoords.posY, aCoords.posZ))
                || (block == GregTech_API.sBlockReinforced
                    && world.getBlockMetadata(aCoords.posX, aCoords.posY, aCoords.posZ) == 2);
            // See if the block itself is MultiBlock, also the one we need.
            if (tTileEntity instanceof IGregTechTileEntity te
                && te.getMetaTileEntity() instanceof GT_MetaTileEntity_MultiBlockBase mte)
                if (mte.mMaintenanceHatches.contains(this)) return mte;

            // Now see if we should add the nearby blocks to the queue:
            // 1) If we've visited less than 5 blocks, then yes
            // 2) If the tile says we should recursively update (pipes don't, machine blocks do)
            // 3) If the block at the coordinates is marked as a machine block
            if (visited.size() < 5
                || (tTileEntity instanceof IMachineBlockUpdateable
                    && ((IMachineBlockUpdateable) tTileEntity).isMachineBlockUpdateRecursive())
                || isMachineBlock) {
                ChunkCoordinates tCoords;

                if (visited.add(tCoords = new ChunkCoordinates(aCoords.posX + 1, aCoords.posY, aCoords.posZ)))
                    tQueue.add(tCoords);
                if (visited.add(tCoords = new ChunkCoordinates(aCoords.posX - 1, aCoords.posY, aCoords.posZ)))
                    tQueue.add(tCoords);
                if (visited.add(tCoords = new ChunkCoordinates(aCoords.posX, aCoords.posY + 1, aCoords.posZ)))
                    tQueue.add(tCoords);
                if (visited.add(tCoords = new ChunkCoordinates(aCoords.posX, aCoords.posY - 1, aCoords.posZ)))
                    tQueue.add(tCoords);
                if (visited.add(tCoords = new ChunkCoordinates(aCoords.posX, aCoords.posY, aCoords.posZ + 1)))
                    tQueue.add(tCoords);
                if (visited.add(tCoords = new ChunkCoordinates(aCoords.posX, aCoords.posY, aCoords.posZ - 1)))
                    tQueue.add(tCoords);
            }
        }
        return null;
    }

    @Override
    public boolean doesBindPlayerInventory() {
        return false;
    }

    @Override
    public int getGUIWidth() {
        return 150;
    }

    @Override
    public int getGUIHeight() {
        return 40;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.setBackground(GT_UITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.setGuiTint(getGUIColorization());
        builder.widget(
            ButtonWidget.closeWindowButton(true)
                .setPos(135, 3))
            .widget(
                new TextWidget(StatCollector.translateToLocal("GT5U.gui.text.drone_custom_name"))
                    .setTextAlignment(Alignment.Center)
                    .setPos(0, 5)
                    .setSize(150, 8))
            .widget(
                new TextFieldWidget().setGetter(() -> connection == null ? "" : connection.getCustomName(false))
                    .setSetter(var -> { if (connection != null) connection.setCustomName(var); })
                    .setTextAlignment(Alignment.CenterLeft)
                    .setTextColor(Color.WHITE.dark(1))
                    .setFocusOnGuiOpen(true)
                    .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD_LIGHT_GRAY.withOffset(-1, -1, 2, 2))
                    .setPos(10, 16)
                    .setSize(130, 16))
            .build();
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setBoolean("connection", connection == null);
        if (connection != null) {
            tag.setInteger("x", connection.centreCoord.posX);
            tag.setInteger("y", connection.centreCoord.posY);
            tag.setInteger("z", connection.centreCoord.posZ);
            tag.setString("name", connection.customName);
        }
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        NBTTagCompound tag = accessor.getNBTData();
        if (tag.getBoolean("connection")) {
            currenttip
                .add(EnumChatFormatting.RED + StatCollector.translateToLocal("GT5U.waila.drone_downlink.noConnection"));
        } else {
            currenttip.add(
                EnumChatFormatting.AQUA + StatCollector.translateToLocal("GT5U.waila.drone_downlink.connection")
                    + tag.getInteger("x")
                    + " "
                    + tag.getInteger("y")
                    + " "
                    + tag.getInteger("z"));
            currenttip.add(EnumChatFormatting.YELLOW + tag.getString("name"));
        }
        super.getWailaBody(itemStack, currenttip, accessor, config);
    }
}
