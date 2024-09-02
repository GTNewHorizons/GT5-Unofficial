package tectech.thing.metaTileEntity.single;

import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.reflect.FieldUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gregtech.api.objects.GTRenderedTexture;
import gregtech.api.util.GTUtility;
import tectech.TecTech;
import tectech.util.CommonValues;
import tectech.util.TTUtility;

/**
 * Created by Tec on 23.03.2017.
 */
public class MTEOwnerDetector extends MTETieredMachineBlock {

    private static GTRenderedTexture OWNER_ONLINE, OWNER_OFFLINE;
    private String uuid;
    private boolean interdimensional = true;

    public MTEOwnerDetector(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            0,
            new String[] { CommonValues.TEC_MARK_GENERAL,
                translateToLocal("gt.blockmachines.machine.tt.ownerdetector.desc.0"),
                EnumChatFormatting.BLUE + translateToLocal("gt.blockmachines.machine.tt.ownerdetector.desc.1"),
                EnumChatFormatting.BLUE + translateToLocal("gt.blockmachines.machine.tt.ownerdetector.desc.2") });
        TTUtility.setTier(aTier, this);
    }

    public MTEOwnerDetector(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
        TTUtility.setTier(aTier, this);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEOwnerDetector(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        super.registerIcons(aBlockIconRegister);
        OWNER_ONLINE = new GTRenderedTexture(new Textures.BlockIcons.CustomIcon("iconsets/OWNER_ONLINE"));
        OWNER_OFFLINE = new GTRenderedTexture(new Textures.BlockIcons.CustomIcon("iconsets/OWNER_OFFLINE"));
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        return new ITexture[] { tectech.thing.metaTileEntity.Textures.MACHINE_CASINGS_TT[mTier][colorIndex + 1],
            aActive ? OWNER_ONLINE : OWNER_OFFLINE };
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return null;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity iGregTechTileEntity, int i, ForgeDirection side,
        ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity iGregTechTileEntity, int i, ForgeDirection side,
        ItemStack itemStack) {
        return false;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setString("eUUID", uuid);
        aNBT.setBoolean("eInterDim", interdimensional);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        uuid = aNBT.getString("eUUID");
        interdimensional = aNBT.getBoolean("eInterDim");
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (uuid == null || uuid.length() == 0) {
                String name = aBaseMetaTileEntity.getOwnerName();
                if (!("Player".equals(name))) {
                    uuid = TecTech.proxy.getUUID(name);
                }
            }
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide() && aTick % 20 == CommonValues.RECIPE_AT) {
            boolean detected = TecTech.proxy.isOnlineUUID(uuid) || (uuid != null && uuid.length() > 0
                && TecTech.proxy.isOnlineName(aBaseMetaTileEntity.getOwnerName()));
            aBaseMetaTileEntity.setActive(detected);
            aBaseMetaTileEntity.setGenericRedstoneOutput(detected);
            byte value = (byte) (detected ? 15 : 0);
            for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                aBaseMetaTileEntity.setStrongOutputRedstoneSignal(side, value);
            }
        }
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        String clientLocale;
        try {
            EntityPlayerMP player = (EntityPlayerMP) aPlayer;
            clientLocale = (String) FieldUtils.readField(player, "translator", true);
        } catch (Exception e) {
            clientLocale = "en_US";
        }
        interdimensional ^= true;
        GTUtility.sendChatToPlayer(
            aPlayer,
            interdimensional ? translateToLocalFormatted("tt.keyphrase.Running_interdimensional_scan", clientLocale)
                : translateToLocalFormatted("tt.keyphrase.Running_local_dimension_scan", clientLocale));
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
    public boolean isElectric() {
        return false;
    }

    @Override
    public boolean isEnetOutput() {
        return false;
    }

    @Override
    public boolean isEnetInput() {
        return false;
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
        return false;
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return false;
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
    public long maxEUInput() {
        return Integer.MAX_VALUE;
    }

    @Override
    public long maxEUOutput() {
        return 0;
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
    public int getProgresstime() {
        return interdimensional ? 1 : 0;
    }

    @Override
    public int maxProgresstime() {
        return 1;
    }

    @Override
    public boolean hasSidedRedstoneOutputBehavior() {
        return true;
    }

    @Override
    public boolean allowGeneralRedstoneOutput() {
        return true;
    }
}
