package gregtech.common.tileentities.machines.multi.drone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

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

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.gtnewhorizon.structurelib.util.Vec3Impl;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchMaintenance;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.render.TextureFactory;
import gregtech.common.gui.modularui.hatch.MTEHatchDroneDownLinkGui;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEHatchDroneDownLink extends MTEHatchMaintenance {

    private Vec3Impl downlinkCoord;
    private MTEDroneCentre centre;
    private final List<DroneConnection> connections = new ArrayList<>();
    private final List<MTEMultiBlockBase> unlinkedMachines = new ArrayList<>();
    private final HashMap<String, String> savedNameList = new HashMap<>();

    private static final IIconContainer moduleActive = new Textures.BlockIcons.CustomIcon(
        "iconsets/OVERLAY_DRONE_MODULE_ACTIVE");

    public MTEHatchDroneDownLink(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public MTEHatchDroneDownLink(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures, false);
    }

    public void registerMachineController(MTEMultiBlockBase machine) {
        if (!addConnection(machine)) unlinkedMachines.add(machine);
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
            aBaseMetaTileEntity.getXCoord(),
            aBaseMetaTileEntity.getYCoord(),
            aBaseMetaTileEntity.getZCoord());

        tryFindDroneCenter();
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            // validate that all connections to the center and the machines are still valid every 5s
            if (aTick % 100 == 0) {
                validateConnections();
            }

            // if we don't have a connection to a center, search for one every 10s
            if (aTick % 200 == 0) {
                if (centre == null) {
                    tryFindDroneCenter();
                    if (centre == null) return;
                }
                // In rare cases, this status may not refresh to the connection. Manually refresh it.
                for (DroneConnection conn : connections) {
                    conn.getLinkedMachine()
                        .ifPresent(mte -> conn.machineStatus = mte.isAllowedToWork());
                }
            }

            // Maintain
            if (hasConnection() && centre.getBaseMetaTileEntity() != null
                && centre.getBaseMetaTileEntity()
                    .isActive()) {
                doNormalMaintain();
            }
        }
    }

    private void validateConnections() {
        // If centre is offline, delete all connection.
        if (centre != null && (!centre.isValid() || !centre.getBaseMetaTileEntity()
            .isActive())) {
            centre.getConnectionList()
                .removeAll(connections);
            clearConnections();
            centre = null;
        }

        // Now we validate if the LinkedMachines are still valid.
        connections.removeIf(entry -> {
            boolean result = entry.isValid();
            if (!result) centre.getConnectionList()
                .remove(entry);
            return !result;
        });
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
        if (centre == null) return;
        centre.getConnectionList()
            .removeAll(connections);
    }

    private boolean hasConnection() {
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
                    .withinDistance(this.downlinkCoord, centre.getRange()) && centre.getBaseMetaTileEntity() != null
                    && centre.getBaseMetaTileEntity()
                        .isActive()) {

                    clearConnections();
                    this.centre = centre;

                    unlinkedMachines.removeIf(this::addConnection);

                    return;
                }
            }
        }
    }

    private boolean addConnection(MTEMultiBlockBase machine) {
        if (centre == null || findConnection(machine).isPresent()) {
            return false;
        }
        DroneConnection connection = new DroneConnection(machine, centre, savedNameList);
        connections.add(connection);
        centre.getConnectionList()
            .add(connection);
        return true;
    }

    private void clearConnections() {
        // save data first
        connections.removeIf(conn -> {
            conn.getLinkedMachine()
                .ifPresent(unlinkedMachines::add);
            savedNameList.put(conn.uuid.toString(), conn.getCustomName());
            return true;
        });
    }

    public List<DroneConnection> getConnections() {
        return connections;
    }

    public void setConnections(List<DroneConnection> connections) {
        this.connections.clear();
        this.connections.addAll(connections);
    }

    public Optional<DroneConnection> findConnection(MTEMultiBlockBase machine) {
        return connections.stream()
            .filter(
                connection -> connection.getLinkedMachine()
                    .filter(machine::equals)
                    .isPresent())
            .findFirst();
    }

    public Optional<DroneConnection> findConnection(UUID uuid1) {
        return connections.stream()
            .filter(connection -> connection.uuid.equals(uuid1))
            .findFirst();
    }

    @Nullable
    public MTEDroneCentre getCentre() {
        return centre;
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchDroneDownLinkGui(this).build(data, syncManager, uiSettings);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        NBTTagCompound nameList = aNBT.getCompoundTag("conList");
        for (String s : nameList.func_150296_c()) {
            savedNameList.put(s, nameList.getString(s));
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        NBTTagCompound conList = new NBTTagCompound();
        connections.forEach(conn -> conList.setString(conn.uuid.toString(), conn.getCustomName()));
        aNBT.setTag("conList", conList);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);

        tag.setBoolean("connected", hasConnection());
        if (hasConnection()) {
            tag.setInteger(
                "x",
                centre.getCoords()
                    .get0());
            tag.setInteger(
                "y",
                centre.getCoords()
                    .get1());
            tag.setInteger(
                "z",
                centre.getCoords()
                    .get2());

            int i = 0;
            for (DroneConnection connection : connections) {
                if (connection.customName != null) {
                    tag.setString("name" + i, connection.customName);
                    i++;
                }
            }
        }
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

            if (tag.hasKey("name0")) {
                int i = 0;
                while (tag.hasKey("name" + i)) {
                    currenttip.add(EnumChatFormatting.YELLOW + tag.getString("name" + i));
                    i++;
                }
            }
        } else {
            currenttip
                .add(EnumChatFormatting.RED + StatCollector.translateToLocal("GT5U.waila.drone_downlink.noConnection"));
        }
        super.getWailaBody(itemStack, currenttip, accessor, config);
    }
}
