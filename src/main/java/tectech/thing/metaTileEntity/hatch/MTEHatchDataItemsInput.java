package tectech.thing.metaTileEntity.hatch;

import static gregtech.api.enums.Dyes.MACHINE_METAL;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;
import static tectech.thing.metaTileEntity.hatch.MTEHatchDataConnector.EM_D_ACTIVE;
import static tectech.thing.metaTileEntity.hatch.MTEHatchDataConnector.EM_D_CONN;
import static tectech.thing.metaTileEntity.hatch.MTEHatchDataConnector.EM_D_SIDES;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.reflect.FieldUtils;

import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchDataAccess;
import gregtech.api.objects.GTRenderedTexture;
import tectech.mechanics.dataTransport.InventoryDataPacket;
import tectech.mechanics.pipe.IConnectsToDataPipe;
import tectech.recipe.TTRecipeAdder;
import tectech.util.CommonValues;
import tectech.util.TTUtility;

public class MTEHatchDataItemsInput extends MTEHatchDataAccess implements IConnectsToDataPipe {

    public boolean delDelay = true;
    private ItemStack[] stacks;

    private String clientLocale = "en_US";

    public MTEHatchDataItemsInput(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
        TTUtility.setTier(aTier, this);
    }

    public MTEHatchDataItemsInput(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
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
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchDataItemsInput(this.mName, this.mTier, mDescriptionArray, this.mTextures);
    }

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
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return false;
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean isDataInputFacing(ForgeDirection side) {
        return isInputFacing(side);
    }

    @Override
    public boolean canConnectData(ForgeDirection side) {
        return isInputFacing(side);
    }

    @Override
    public IConnectsToDataPipe getNext(IConnectsToDataPipe source) {
        return null;
    }

    public void setContents(InventoryDataPacket iIn) {
        if (iIn == null) {
            stacks = null;
        } else {
            if (iIn.getContent().length > 0) {
                stacks = iIn.getContent();
                delDelay = true;
            } else {
                stacks = null;
            }
        }
    }

    @Override
    public void onRemoval() {
        stacks = null;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        NBTTagCompound stacksTag = new NBTTagCompound();
        if (stacks != null) {
            stacksTag.setInteger("count", stacks.length);
            for (int i = 0; i < stacks.length; i++) {
                stacksTag.setTag(Integer.toString(i), stacks[i].writeToNBT(new NBTTagCompound()));
            }
        }
        aNBT.setTag("data_stacks", stacksTag);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        NBTTagCompound stacksTag = aNBT.getCompoundTag("data_stacks");
        int count = stacksTag.getInteger("count");
        if (count > 0) {
            ArrayList<ItemStack> stacks = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                ItemStack stack = ItemStack.loadItemStackFromNBT(stacksTag.getCompoundTag(Integer.toString(i)));
                if (stack != null) {
                    stacks.add(stack);
                }
            }
            if (stacks.size() > 0) {
                this.stacks = stacks.toArray(TTRecipeAdder.nullItem);
            }
        }
    }

    @Override
    public int getSizeInventory() {
        return stacks != null ? stacks.length : 0;
    }

    @Override
    public ItemStack getStackInSlot(int aIndex) {
        return stacks != null && aIndex < stacks.length ? stacks[aIndex] : null;
    }

    @Override
    public List<ItemStack> getInventoryItems(Predicate<ItemStack> filter) {
        if (stacks == null) return Collections.emptyList();
        return Arrays.stream(stacks)
            .filter(stack -> stack != null && filter.test(stack))
            .collect(Collectors.toList());
    }

    @Override
    public boolean shouldDropItemAt(int index) {
        return false;
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        if (CommonValues.MOVE_AT == aTick % 20) {
            if (stacks == null) {
                getBaseMetaTileEntity().setActive(false);
            } else {
                getBaseMetaTileEntity().setActive(true);
                if (delDelay) {
                    delDelay = false;
                } else {
                    setContents(null);
                }
            }
        }
    }

    @Override
    public String[] getDescription() {
        return new String[] { CommonValues.TEC_MARK_EM, translateToLocal("gt.blockmachines.hatch.datainass.desc.0"),
            translateToLocal("gt.blockmachines.hatch.datainass.desc.1"),
            EnumChatFormatting.AQUA + translateToLocal("gt.blockmachines.hatch.datainass.desc.2") };
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        return new String[] { translateToLocalFormatted("tt.keyphrase.Content_Stack_Count", clientLocale) + ": "
            + (stacks == null ? 0 : stacks.length) };
    }

    @Override
    public byte getColorization() {
        return getBaseMetaTileEntity().getColorization();
    }
}
