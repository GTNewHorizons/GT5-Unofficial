package GoodGenerator.Blocks.TEs.MetaTE;

import GoodGenerator.Client.GUI.NeutronSensorGUIClient;
import GoodGenerator.Common.Container.NeutronSensorGUIContainer;
import GoodGenerator.Main.GoodGenerator;
import GoodGenerator.Network.MessageOpenNeutronSensorGUI;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Log;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class NeutronSensor extends GT_MetaTileEntity_Hatch {

    private static final IIconContainer textureFont = new Textures.BlockIcons.CustomIcon("icons/NeutronSensorFont");
    private static final IIconContainer textureFont_Glow = new Textures.BlockIcons.CustomIcon("icons/NeutronSensorFont_GLOW");

    protected String texts = "";

    public NeutronSensor(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, "Detect Neutron Kinetic Energy.");
    }

    public NeutronSensor(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Can be installed in Neutron Activator.",
                "Output Redstone Signal according to the Neutron Kinetic Energy.",
                "Right click to open the GUI and setting."
        };
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        texts = aNBT.getString("mBoxContext");
        GT_Log.out.print(texts + "\n");
        super.loadNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setString("mBoxContext", texts);
        super.saveNBTData(aNBT);
    }

    @Override
    public void initDefaultModes(NBTTagCompound aNBT) {
        getBaseMetaTileEntity().setActive(true);
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return false;
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new NeutronSensorGUIClient(aPlayerInventory, aBaseMetaTileEntity, GoodGenerator.MOD_ID + ":textures/gui/NeutronSensorGUI.png", this.texts);
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new NeutronSensorGUIContainer(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, byte aSide, float aX, float aY, float aZ) {
        if (aBaseMetaTileEntity.isClientSide()) return true;
        if (aSide == aBaseMetaTileEntity.getFrontFacing() && aPlayer instanceof EntityPlayerMP) {
            GoodGenerator.CHANNEL.sendTo(new MessageOpenNeutronSensorGUI(aBaseMetaTileEntity, texts), (EntityPlayerMP) aPlayer);
            return true;
        }
        return false;
    }

    public void setText(String text) {
        texts = text == null ? "" : text;
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] {
                aBaseTexture,
                TextureFactory.of(textureFont),
                TextureFactory.builder().addIcon(textureFont_Glow).glow().build()
        };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] {
                aBaseTexture,
                TextureFactory.of(textureFont)
        };
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new NeutronSensor(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        //if (aTick % 100 == 0) GT_Log.out.print(texts + "\n");
    }
}
