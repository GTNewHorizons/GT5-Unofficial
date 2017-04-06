package com.github.technus.tectech.thing.metaTileEntity.hatch;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.thing.machineTT;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_Container_2by2;
import gregtech.api.gui.GT_GUIContainer_2by2;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.TreeMap;

import static com.github.technus.tectech.CommonValues.multiCheckAt;

/**
 * Created by Tec on 03.04.2017.
 */
public class GT_MetaTileEntity_Hatch_Rack extends GT_MetaTileEntity_Hatch implements machineTT {
    private static Textures.BlockIcons.CustomIcon EM_R;
    private static Textures.BlockIcons.CustomIcon EM_R_ACTIVE;
    public int heat=0;
    private float overclock=1;
    private static TreeMap<String,component> componentBinds=new TreeMap<>();

    public GT_MetaTileEntity_Hatch_Rack(int aID, String aName, String aNameRegional, int aTier, String descr) {
        super(aID, aName, aNameRegional, aTier, 4, descr);
    }

    public GT_MetaTileEntity_Hatch_Rack(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 4, aDescription, aTextures);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("eHeat",heat);
        aNBT.setFloat("eOverclock",overclock);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        heat=aNBT.getInteger("eHeat");
        overclock=aNBT.getFloat("eOverclock");
    }

    @Override
    public void registerIcons(IIconRegister aBlockIconRegister) {
        super.registerIcons(aBlockIconRegister);
        EM_R_ACTIVE = new Textures.BlockIcons.CustomIcon("iconsets/EM_RACK_ACTIVE");
        EM_R = new Textures.BlockIcons.CustomIcon("iconsets/EM_RACK");
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, new GT_RenderedTexture(EM_R_ACTIVE)};
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, new GT_RenderedTexture(EM_R)};
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_Rack(mName,mTier,mDescription,mTextures);
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return aFacing>=2;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return true;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        if(aBaseMetaTileEntity.isActive() || heat>0) return false;
        return aSide == aBaseMetaTileEntity.getFrontFacing();
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        if(aBaseMetaTileEntity.isActive() || heat>0) return false;
        return aSide == aBaseMetaTileEntity.getFrontFacing();
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_2by2(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_2by2(aPlayerInventory, aBaseMetaTileEntity, "Computer Rack");
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) return true;
        if(aBaseMetaTileEntity.isActive())
            aPlayer.addChatComponentMessage(new ChatComponentText("It is still active..."));
        else if(heat>0)
            aPlayer.addChatComponentMessage(new ChatComponentText("It is still warm..."));
        else
            aBaseMetaTileEntity.openGUI(aPlayer);
        return true;
    }

    //TODO implement: glitches with OC, random component burning with OC
    private int getComputationPower(float overclock,boolean heatProcess){
        float computation=0,heat=0;
        for(ItemStack is:mInventory){
            if(is==null || is.stackSize!=1) continue;
            component comp=componentBinds.get(is.getUnlocalizedName());
            if(comp==null) continue;
            if(heatProcess){
                if(this.heat>comp.maxHeat){
                    is.stackSize=0;
                    continue;
                }
                if(comp.subZero || this.heat>=0)
                    heat+=comp.heat>0?comp.heat*overclock*overclock:comp.heat;
            }
            computation+=comp.computation;
        }
        if(heatProcess){
            this.heat+=Math.ceil(heat);
        }
        return (int)Math.ceil(computation*overclock);
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    public int tickComponents(float oc) {
        overclock=oc;
        if(overclock>10) getBaseMetaTileEntity().setToFire();
        return getComputationPower(overclock,true);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if(aBaseMetaTileEntity.isServerSide()){
            if(aTick%20==multiCheckAt){
                if(heat>0)heat-=Math.max(heat/1000,1);
                else if(heat<0)heat-=Math.min(heat/1000,-1);

                if(heat>9000) aBaseMetaTileEntity.setOnFire();
                else if(heat>10000) aBaseMetaTileEntity.setToFire();
                else if(heat<-20000)this.heat=-20000;
            }
        }
    }

    @Override
    public void onRemoval() {
        if(mInventory!=null && (heat>0 || (getBaseMetaTileEntity()!=null && getBaseMetaTileEntity().isActive())))
            for(int i=0;i<mInventory.length;i++)
                mInventory[i]=null;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.tecMark,
                mDescription,
                EnumChatFormatting.AQUA + "Holds Computer Components"
        };
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        return new String[]{
                "Base computation: "+ EnumChatFormatting.AQUA +getComputationPower(1,false),
                "After overclocking: "+ EnumChatFormatting.AQUA +getComputationPower(overclock,false),
                "Heat Accumulated: "+ EnumChatFormatting.RED + ((heat+99)/100) +EnumChatFormatting.RESET+" %"};
                                                                //heat==0? --> ((heat+9)/10) = 0
                                                                //Heat==1-10? -->  1
    }

    public static void run(){
        new component(ItemList.Circuit_Elite.get(1),4,32,5000,true);
        new component(ItemList.Circuit_Advanced.get(1),1,2,2000,true);
        new component(ItemList.Circuit_Basic.get(1),2,1,1000,true);
    }

    public static class component implements Comparable<component>{
        private final String unlocalizedName;
        private final float heat,computation,maxHeat;
        private final boolean subZero;

        component(ItemStack is,float heat,float computation,float maxHeat, boolean subZero){
            unlocalizedName=is.getUnlocalizedName();
            this.heat=heat;
            this.computation=computation;
            this.maxHeat=maxHeat;
            this.subZero=subZero;
            componentBinds.put(unlocalizedName,this);
        }

        @Override
        public int compareTo(component o) {
            return unlocalizedName.compareTo(o.unlocalizedName);
        }
    }
}


