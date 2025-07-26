package gregtech.common.tileentities.machines.multi.drone;

import java.util.List;
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
    private DroneConnection connection;
    // This has to be existed for doing random damage.
    private MTEMultiBlockBase machine;
    private static final IIconContainer moduleActive = new Textures.BlockIcons.CustomIcon(
        "iconsets/OVERLAY_DRONE_MODULE_ACTIVE");

    public MTEHatchDroneDownLink(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public MTEHatchDroneDownLink(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
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
        return new MTEHatchDroneDownLink(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
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
                }
            } else {
                // Clear any connection that this downlink may have
                connection = null;
            }
        }
    }

    private void doNormalMaintain() {
        this.mWrench = this.mScrewdriver = this.mSoftMallet = this.mHardHammer = this.mCrowbar = this.mSolderingTool = true;
        connection.machine.mWrench = connection.machine.mScrewdriver = connection.machine.mSoftMallet = connection.machine.mHardHammer = connection.machine.mCrowbar = connection.machine.mSolderingTool = true;
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
            openGui(aPlayer);
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
     * Find and establish a drone controller connection. This will search for all DC in the same dimension, then find
     * one in range.
     */
    private void tryFindConnection() {
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
                    if (machine != null && machine.isValid()) {
                        connection = new DroneConnection(machine, centre);
                        connection.centre.getConnectionList()
                            .add(connection);
                        return;
                    }
                }
            }
        }
    }

    public void connectMultiBlockBase(MTEMultiBlockBase machine) {
        if (this.machine != machine) {
            this.machine = machine;
            tryFindConnection();
        }
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
                new TextFieldWidget().setGetter(() -> connection == null ? "" : connection.getCustomName(false))
                    .setSetter(var -> { if (connection != null) connection.setCustomName(var); })
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
                EnumChatFormatting.AQUA + StatCollector.translateToLocalFormatted(
                    "GT5U.waila.drone_downlink.connection",
                    tag.getInteger("x"),
                    tag.getInteger("y"),
                    tag.getInteger("z")));
            currenttip.add(EnumChatFormatting.YELLOW + tag.getString("name"));
        }
        super.getWailaBody(itemStack, currenttip, accessor, config);
    }
}
