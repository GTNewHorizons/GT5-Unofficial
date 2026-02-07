package gregtech.common.tileentities.machines.multi.drone;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
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
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchMaintenance;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.render.TextureFactory;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEHatchDroneDownLink extends MTEHatchMaintenance {

    private Vec3Impl downlinkCoord;
    private MTEDroneCentre center;
    private final Set<DroneConnection> connections = new HashSet<>();
    private final Set<MTEMultiBlockBase> machines = new HashSet<>();

    private static final IIconContainer moduleActive = Textures.BlockIcons
        .custom("iconsets/OVERLAY_DRONE_MODULE_ACTIVE");

    public MTEHatchDroneDownLink(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public MTEHatchDroneDownLink(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures, false);
    }

    public void registerMachineController(MTEMultiBlockBase machine) {
        machines.add(machine);
        addConnection(machine);
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
        return new MTEHatchDroneDownLink(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        downlinkCoord = new Vec3Impl(
            getBaseMetaTileEntity().getXCoord(),
            getBaseMetaTileEntity().getYCoord(),
            getBaseMetaTileEntity().getZCoord());

        tryFindDroneCenter();
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            // validate that all connections to the center and the machines are still valid every 4s
            if (aTick % 80 == 0) {
                validateConnections();
            }

            // if we don't have a connection to a center, search for one every 10s
            if (center == null && aTick % 200 == 0) {
                tryFindDroneCenter();
            }

            if (hasConnection() && center.getBaseMetaTileEntity()
                .isActive()) {
                doNormalMaintain();
            }
        }
    }

    private void validateConnections() {
        if (center != null && (!center.isValid() || !center.getBaseMetaTileEntity()
            .isActive())) {
            center.getConnectionList()
                .removeAll(connections);
            connections.clear();
            center = null;
        }

        for (MTEMultiBlockBase machine : new ArrayList<>(machines)) {
            if (!machine.isValid()) {
                machines.remove(machine);

                DroneConnection connection = findConnection(machine);
                connections.remove(connection);
                center.getConnectionList()
                    .remove(connection);
            }
        }
    }

    private void doNormalMaintain() {
        this.mWrench = this.mScrewdriver = this.mSoftMallet = this.mHardHammer = this.mCrowbar = this.mSolderingTool = true;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side,
        float aX, float aY, float aZ) {
        if (aBaseMetaTileEntity.isClientSide()) return true;
        if (side == aBaseMetaTileEntity.getFrontFacing()) {
            if (aPlayer instanceof FakePlayer) return false;
            if (!hasConnection()) {
                aPlayer.addChatComponentMessage(new ChatComponentTranslation("GT5U.machines.dronecentre.noconnection"));
                return true;
            }
            openGui(aPlayer);
            return true;
        }
        return false;
    }

    @Override
    public void onMaintenancePerformed(MTEMultiBlockBase aMaintenanceTarget) {
        if (mMaintenanceSound == null) {
            setMaintenanceSound(SoundResource.GT_MAINTENANCE_DRONE_DOWNLINK_HATCH, 1.0F, 1.0F);
        }

        super.onMaintenancePerformed(aMaintenanceTarget);
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
        if (center == null) return;
        center.getConnectionList()
            .removeAll(connections);
    }

    private boolean hasConnection() {
        validateConnections();
        return !connections.isEmpty();
    }

    /**
     * Find a drone center. This will search for all DC in the same dimension, then find one in range.
     */
    private void tryFindDroneCenter() {
        if (MTEDroneCentre.getCentreMap()
            .containsKey(getBaseMetaTileEntity().getWorld().provider.dimensionId)) {
            List<MTEDroneCentre> target = MTEDroneCentre.getCentreMap()
                .get(getBaseMetaTileEntity().getWorld().provider.dimensionId)
                .stream()
                .collect(Collectors.toList());
            for (MTEDroneCentre centre : target) {
                if (centre.getCoords()
                    .withinDistance(this.downlinkCoord, centre.getRange())
                    && centre.getBaseMetaTileEntity()
                        .isActive()) {

                    connections.clear();
                    this.center = centre;

                    for (MTEMultiBlockBase machine : machines) {
                        addConnection(machine);
                    }

                    return;
                }
            }
        }
    }

    private void addConnection(MTEMultiBlockBase machine) {
        if (center == null || findConnection(machine) != null) {
            return;
        }

        DroneConnection connection = new DroneConnection(machine, center);
        connections.add(connection);
        center.getConnectionList()
            .add(connection);
    }

    private DroneConnection findConnection(MTEMultiBlockBase machine) {
        return connections.stream()
            .filter(c -> c.machine == machine)
            .findFirst()
            .orElse(null);
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
        builder.setBackground(GTUITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
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
                new TextFieldWidget()
                    .setGetter(() -> getFirstConnection() == null ? "" : getFirstConnection().getCustomName(false))
                    .setSetter(var -> connections.forEach(c -> c.setCustomName(var)))
                    .setTextAlignment(Alignment.CenterLeft)
                    .setTextColor(Color.WHITE.dark(1))
                    .setFocusOnGuiOpen(true)
                    .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD_LIGHT_GRAY.withOffset(-1, -1, 2, 2))
                    .setPos(10, 16)
                    .setSize(130, 16))
            .build();
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);

        tag.setBoolean("connected", hasConnection());
        if (hasConnection()) {
            tag.setInteger(
                "x",
                center.getCoords()
                    .get0());
            tag.setInteger(
                "y",
                center.getCoords()
                    .get1());
            tag.setInteger(
                "z",
                center.getCoords()
                    .get2());

            DroneConnection firstConnection = getFirstConnection();
            if (firstConnection != null && firstConnection.customName != null) {
                tag.setString("name", firstConnection.customName);
            }
        }
    }

    private DroneConnection getFirstConnection() {
        return connections.stream()
            .findFirst()
            .orElse(null);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        NBTTagCompound tag = accessor.getNBTData();
        if (tag.getBoolean("connected")) {
            currenttip.add(
                EnumChatFormatting.AQUA + StatCollector.translateToLocalFormatted(
                    "GT5U.waila.drone_downlink.connection",
                    tag.getInteger("x"),
                    tag.getInteger("y"),
                    tag.getInteger("z")));

            if (tag.hasKey("name")) {
                currenttip.add(EnumChatFormatting.YELLOW + tag.getString("name"));
            }
        } else {
            currenttip
                .add(EnumChatFormatting.RED + StatCollector.translateToLocal("GT5U.waila.drone_downlink.noConnection"));
        }
        super.getWailaBody(itemStack, currenttip, accessor, config);
    }
}
