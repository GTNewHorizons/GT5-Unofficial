package tectech.thing.metaTileEntity.hatch;

import static gregtech.api.enums.Dyes.MACHINE_METAL;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.reflect.FieldUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.objects.GTRenderedTexture;
import tectech.mechanics.dataTransport.DataPacket;
import tectech.mechanics.pipe.IConnectsToDataPipe;
import tectech.util.CommonValues;
import tectech.util.TTUtility;

/**
 * Created by danie_000 on 11.12.2016.
 */
public abstract class MTEHatchDataConnector<T extends DataPacket> extends MTEHatch implements IConnectsToDataPipe {

    public static Textures.BlockIcons.CustomIcon EM_D_SIDES;
    public static Textures.BlockIcons.CustomIcon EM_D_ACTIVE;
    public static Textures.BlockIcons.CustomIcon EM_D_CONN;

    private String clientLocale = "en_US";

    public T q;

    public short id = -1;

    protected MTEHatchDataConnector(int aID, String aName, String aNameRegional, int aTier, String[] descr) {
        super(aID, aName, aNameRegional, aTier, 0, descr);
        TTUtility.setTier(aTier, this);
    }

    protected MTEHatchDataConnector(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        super.registerIcons(aBlockIconRegister);
        EM_D_ACTIVE = new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_EM_D_ACTIVE");
        EM_D_SIDES = new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_EM_D_SIDES");
        EM_D_CONN = new Textures.BlockIcons.CustomIcon("iconsets/EM_DATA_CONN");
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture,
            new GTRenderedTexture(
                EM_D_ACTIVE,
                Dyes.getModulation(getBaseMetaTileEntity().getColorization(), MACHINE_METAL.getRGBA())),
            new GTRenderedTexture(EM_D_CONN) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture,
            new GTRenderedTexture(
                EM_D_SIDES,
                Dyes.getModulation(getBaseMetaTileEntity().getColorization(), MACHINE_METAL.getRGBA())),
            new GTRenderedTexture(EM_D_CONN) };
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setShort("eID", id);
        if (q != null) {
            aNBT.setTag("eDATA", q.toNbt());
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        id = aNBT.getShort("eID");
        if (aNBT.hasKey("eDATA")) {
            q = loadPacketFromNBT(aNBT.getCompoundTag("eDATA"));
        }
    }

    protected abstract T loadPacketFromNBT(NBTTagCompound nbt);

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (CommonValues.MOVE_AT == aTick % 20) {
                if (q == null) {
                    getBaseMetaTileEntity().setActive(false);
                } else {
                    getBaseMetaTileEntity().setActive(true);
                    moveAround(aBaseMetaTileEntity);
                }
            }
        }
    }

    public abstract void moveAround(IGregTechTileEntity aBaseMetaTileEntity);

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) {
            return true;
        }
        try {
            EntityPlayerMP player = (EntityPlayerMP) aPlayer;
            clientLocale = (String) FieldUtils.readField(player, "translator", true);
        } catch (Exception e) {
            clientLocale = "en_US";
        }
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
    public boolean isLiquidInput(ForgeDirection side) {
        return false;
    }

    @Override
    public boolean isFluidInputAllowed(FluidStack aFluid) {
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
    public boolean isValidSlot(int aIndex) {
        return false;
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        if (id > 0) {
            return new String[] {
                translateToLocalFormatted("tt.keyword.ID", clientLocale) + ": " + EnumChatFormatting.AQUA + id,
                translateToLocalFormatted("tt.keyword.Content", clientLocale) + ": "
                    + EnumChatFormatting.AQUA
                    + (q != null ? q.getContentString() : 0),
                translateToLocalFormatted("tt.keyword.PacketHistory", clientLocale) + ": "
                    + EnumChatFormatting.RED
                    + (q != null ? q.getTraceSize() : 0), };
        }
        return new String[] {
            translateToLocalFormatted("tt.keyword.Content", clientLocale) + ": "
                + EnumChatFormatting.AQUA
                + (q != null ? q.getContentString() : 0),
            translateToLocalFormatted("tt.keyword.PacketHistory", clientLocale) + ": "
                + EnumChatFormatting.RED
                + (q != null ? q.getTraceSize() : 0), };
    }

    @Override
    public byte getColorization() {
        return getBaseMetaTileEntity().getColorization();
    }
}
