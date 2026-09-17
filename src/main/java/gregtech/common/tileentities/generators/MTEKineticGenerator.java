package gregtech.common.tileentities.generators;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAYS_ENERGY_OUT;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IKineticProducer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTSplit;
import gregtech.common.gui.modularui.singleblock.kinetic.MTEKineticGeneratorGui;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEKineticGenerator extends MTETieredMachineBlock {

    private IKineticProducer producerKU;
    private boolean hasProducer;
    private long kuIn = 0;

    public MTEKineticGenerator(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 4, "hello");
    }

    @Override
    public String[] getDescription() {
        return Stream
            .concat(
                Arrays.stream(GTSplit.splitLocalized("gt.blockmachines.kinetic.generator.tooltip")),
                Arrays.stream(
                    new String[] { String.format(
                        StatCollector.translateToLocal("gt.blockmachines.kinetic.generator.ku_info"),
                        String.format("%,d", maxKU())) }))
            .toArray(String[]::new);
    }

    public MTEKineticGenerator(String aName, int aTier, int aInvSlotCount, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public long maxKU() {
        return GTValues.V[mTier] << 2;
    }

    private boolean checkForProducer() {
        IGregTechTileEntity thisTile = getBaseMetaTileEntity();
        if (thisTile == null) return false;

        TileEntity tileAtInput = thisTile.getTileEntityAtSide(getKUInputSide());
        if (tileAtInput instanceof IGregTechTileEntity adjTile
            && adjTile.getMetaTileEntity() instanceof IKineticProducer adjProducer
            && adjProducer.getKUOutSide()
                .getOpposite() == getKUInputSide()) {
            producerKU = adjProducer;
            return true;
        }
        producerKU = null;
        return false;
    }

    private long getEUOut() {
        return kuIn >> 2;
    }

    public long getKuIn() {
        return kuIn;
    }

    protected ForgeDirection getKUInputSide() {
        return getBaseMetaTileEntity().getFrontFacing();
    }

    protected ForgeDirection getEnergyOutSide() {
        return getBaseMetaTileEntity().getBackFacing();
    }

    @Override
    public int rechargerSlotCount() {
        return 4;
    }

    @Override
    public long maxEUStore() {
        return GTValues.V[mTier] * 10000;
    }

    @Override
    public long maxEUOutput() {
        return GTValues.V[mTier];
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return new ITexture[0][][];
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEKineticGenerator(
            this.mName,
            this.mTier,
            this.mInventory.length,
            this.mDescriptionArray,
            this.mTextures);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setLong("kuIn", kuIn);
        aNBT.setBoolean("hasProducer", hasProducer);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        kuIn = aNBT.getLong("kuIn");
        hasProducer = aNBT.getBoolean("hasProducer");
    }

    @Override
    public NBTTagCompound getDescriptionData() {
        NBTTagCompound data = super.getDescriptionData();
        if (data == null) data = new NBTTagCompound();

        data.setLong("kuIn", kuIn);
        data.setBoolean("hasProducer", hasProducer);

        return data;
    }

    @Override
    public void onDescriptionPacket(NBTTagCompound data) {
        super.onDescriptionPacket(data);
        kuIn = data.getLong("kuIn");
        hasProducer = data.getBoolean("hasProducer");

    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (aTick % 20 == 0) {
                hasProducer = checkForProducer();
                if (hasProducer) {
                    if (producerKU.currentKU() > maxKU()) aBaseMetaTileEntity.doExplosion(maxKU());
                    kuIn = producerKU.currentKU();
                } else {
                    kuIn = 0;
                }
                aBaseMetaTileEntity.issueTileUpdate();
            }
            aBaseMetaTileEntity.increaseStoredEnergyUnits(getEUOut(), false);
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        hasProducer = checkForProducer();
        super.onFirstTick(aBaseMetaTileEntity);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        IGregTechTileEntity baseTile = this.getBaseMetaTileEntity();
        tag.setLong("storedEU", baseTile.getStoredEU());
        tag.setLong("maxEU", baseTile.getEUCapacity());
        tag.setLong("euProduced", this.getEUOut());
        tag.setBoolean("hasProducer", hasProducer);
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        final NBTTagCompound tag = accessor.getNBTData();
        if (tag.hasKey("euProduced") && tag.getLong("euProduced") > 0) {
            currenttip.add(EnumChatFormatting.GREEN + StatCollector.translateToLocal("GT5U.waila.generating.on"));
        } else {
            currenttip.add(EnumChatFormatting.RED + StatCollector.translateToLocal("GT5U.waila.generating.off"));
        }
        if (tag.hasKey("hasProducer") && tag.getBoolean("hasProducer")) {
            if (tag.hasKey("euProduced")) currenttip.add(
                StatCollector.translateToLocalFormatted(
                    "GT5U.waila.energy.produce",
                    tag.getLong("euProduced"),
                    GTValues.TIER_COLORS[mTier] + GTValues.VN[mTier] + EnumChatFormatting.GRAY));
        } else {
            currenttip.add(EnumChatFormatting.RED + StatCollector.translateToLocal("GT5U.waila.kinetic.no_producer"));
        }
        if (tag.hasKey("storedEU") && tag.hasKey("maxEU")) currenttip.add(
            StatCollector.translateToLocalFormatted(
                "GT5U.waila.energy.stored",
                String.format("%,d", tag.getLong("storedEU")),
                String.format("%,d", tag.getLong("maxEU"))));
        super.getWailaBody(itemStack, currenttip, accessor, config);
    }

    @Override
    public boolean isEnetOutput() {
        return true;
    }

    @Override
    public boolean willExplodeInRain() {
        return false;
    }

    @Override
    public boolean isFacingValid(ForgeDirection side) {
        return (side != ForgeDirection.UP && side != ForgeDirection.DOWN);
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return side == getEnergyOutSide();
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
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][colorIndex + 1],
                TextureFactory.of(Textures.BlockIcons.OVERLAY_KINETIC_TRANSFER) };
        } else if (side == facing.getOpposite()) {
            return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][colorIndex + 1],
                OVERLAYS_ENERGY_OUT[mTier + 1] };
        }
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][colorIndex + 1] };
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side,
        float aX, float aY, float aZ) {
        openGui(aPlayer);
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEKineticGeneratorGui(this).build(guiData, syncManager, uiSettings);
    }
}
