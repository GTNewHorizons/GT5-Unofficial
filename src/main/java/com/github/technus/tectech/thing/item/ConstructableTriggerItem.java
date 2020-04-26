package com.github.technus.tectech.thing.item;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.mechanics.alignment.IAlignment;
import com.github.technus.tectech.mechanics.alignment.enumerable.ExtendedFacing;
import com.github.technus.tectech.mechanics.constructable.IConstructable;
import com.github.technus.tectech.mechanics.constructable.IMultiblockInfoContainer;
import com.github.technus.tectech.util.CommonValues;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

import static com.github.technus.tectech.Reference.MODID;
import static com.github.technus.tectech.loader.gui.CreativeTabTecTech.creativeTabTecTech;
import static net.minecraft.util.StatCollector.translateToLocal;

/**
 * Created by Tec on 15.03.2017.
 */
public final class ConstructableTriggerItem extends Item {
    public static ConstructableTriggerItem INSTANCE;

    private ConstructableTriggerItem() {
        setUnlocalizedName("em.constructable");
        setTextureName(MODID + ":itemConstructable");
        setCreativeTab(creativeTabTecTech);
    }

    @Override
    public boolean onItemUseFirst(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int aSide, float hitX, float hitY, float hitZ) {
        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if(tTileEntity==null || aPlayer instanceof FakePlayer) {
            return aPlayer instanceof EntityPlayerMP;
        }
        if (aPlayer instanceof EntityPlayerMP) {
            //struct gen
            if (aPlayer.isSneaking() && aPlayer.capabilities.isCreativeMode) {
                if (tTileEntity instanceof IGregTechTileEntity) {
                    IMetaTileEntity metaTE = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity();
                    if (metaTE instanceof IConstructable) {
                        ((IConstructable) metaTE).construct(aStack, false);
                    } else if (IMultiblockInfoContainer.contains(metaTE.getClass())) {
                        IMultiblockInfoContainer<IMetaTileEntity> iMultiblockInfoContainer =IMultiblockInfoContainer.get(metaTE.getClass());
                        if(metaTE instanceof IAlignment){
                            iMultiblockInfoContainer.construct(aStack, false, metaTE, (
                                    (IAlignment) metaTE).getExtendedFacing());
                        }else {
                            iMultiblockInfoContainer.construct(aStack, false, metaTE,
                                    ExtendedFacing.of(ForgeDirection.getOrientation(((IGregTechTileEntity) tTileEntity).getFrontFacing())));
                        }
                    }
                } else if (tTileEntity instanceof IConstructable) {
                    ((IConstructable) tTileEntity).construct(aStack, false);
                } else if (IMultiblockInfoContainer.contains(tTileEntity.getClass())) {
                    IMultiblockInfoContainer<TileEntity> iMultiblockInfoContainer =IMultiblockInfoContainer.get(tTileEntity.getClass());
                    if(tTileEntity instanceof IAlignment){
                        iMultiblockInfoContainer.construct(aStack, false, tTileEntity,
                                ((IAlignment) tTileEntity).getExtendedFacing());
                    }else {
                        iMultiblockInfoContainer.construct(aStack, false, tTileEntity,
                                ExtendedFacing.of(ForgeDirection.getOrientation(aSide)));
                    }
                }
            }
            return true;
        }else if (TecTech.proxy.isThePlayer(aPlayer)){//particles and text client side
            //if ((!aPlayer.isSneaking() || !aPlayer.capabilities.isCreativeMode)) {
                if(tTileEntity instanceof IGregTechTileEntity) {
                    IMetaTileEntity metaTE = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity();
                    if (metaTE instanceof IConstructable) {
                        ((IConstructable) metaTE).construct(aStack, true);
                        TecTech.proxy.printInchat(((IConstructable) metaTE).getStructureDescription(aStack));
                        return false;
                    } else if(IMultiblockInfoContainer.contains(metaTE.getClass())){
                        IMultiblockInfoContainer<IMetaTileEntity> iMultiblockInfoContainer =IMultiblockInfoContainer.get(metaTE.getClass());
                        if(metaTE instanceof IAlignment){
                            iMultiblockInfoContainer.construct(aStack, true, metaTE,
                                    ((IAlignment) metaTE).getExtendedFacing());
                        }else {
                            iMultiblockInfoContainer.construct(aStack, true, metaTE,
                                    ExtendedFacing.of(ForgeDirection.getOrientation(((IGregTechTileEntity) tTileEntity).getFrontFacing())));
                        }
                        TecTech.proxy.printInchat(IMultiblockInfoContainer.get(metaTE.getClass()).getDescription(aStack));
                        return false;
                    }
                } else if(tTileEntity instanceof IConstructable){
                    ((IConstructable) tTileEntity).construct(aStack,true);
                    TecTech.proxy.printInchat(((IConstructable) tTileEntity).getStructureDescription(aStack));
                    return false;
                } else if(IMultiblockInfoContainer.contains(tTileEntity.getClass())){
                    IMultiblockInfoContainer<TileEntity> iMultiblockInfoContainer = IMultiblockInfoContainer.get(tTileEntity.getClass());
                    if(tTileEntity instanceof IAlignment){
                        iMultiblockInfoContainer.construct(aStack, true, tTileEntity,
                                ((IAlignment) tTileEntity).getExtendedFacing());
                    }else {
                        iMultiblockInfoContainer.construct(aStack, true, tTileEntity,
                                ExtendedFacing.of(ForgeDirection.getOrientation(aSide)));
                    }
                    TecTech.proxy.printInchat(IMultiblockInfoContainer.get(tTileEntity.getClass()).getDescription(aStack));
                    return false;
                }
            //} else {
            //    if(tTileEntity instanceof IGregTechTileEntity) {
            //        IMetaTileEntity metaTE = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity();
            //        if (metaTE instanceof IConstructable) {
            //            TecTech.proxy.printInchat(((IConstructable) metaTE).getStructureDescription(aStack.stackSize));
            //            return false;
            //        } else if(multiblockMap.containsKey(metaTE.getClass().getCanonicalName())){
            //            TecTech.proxy.printInchat(multiblockMap.get(metaTE.getClass().getCanonicalName()).getDescription(aStack.stackSize));
            //            return false;
            //        }
            //    } else if(tTileEntity instanceof IConstructable){
            //        TecTech.proxy.printInchat(((IConstructable) tTileEntity).getStructureDescription(aStack.stackSize));
            //        return false;
            //    } else if(multiblockMap.containsKey(tTileEntity.getClass().getCanonicalName())){
            //        TecTech.proxy.printInchat(multiblockMap.get(tTileEntity.getClass().getCanonicalName()).getDescription(aStack.stackSize));
            //        return false;
            //    }
            //}
        }
        return false;
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer ep, List aList, boolean boo) {
        aList.add(CommonValues.TEC_MARK_GENERAL);
        aList.add(translateToLocal("item.em.constructable.desc.0"));//Triggers Constructable Interface
        aList.add(EnumChatFormatting.BLUE + translateToLocal("item.em.constructable.desc.1"));//Shows multiblock construction details,
        aList.add(EnumChatFormatting.BLUE + translateToLocal("item.em.constructable.desc.2"));//just Use on a multiblock controller.
        aList.add(EnumChatFormatting.BLUE + translateToLocal("item.em.constructable.desc.3"));//(Sneak Use in creative to build)
        aList.add(EnumChatFormatting.BLUE + translateToLocal("item.em.constructable.desc.4"));//Quantity affects tier/mode/type
    }

    public static void run() {
        INSTANCE = new ConstructableTriggerItem();
        GameRegistry.registerItem(INSTANCE, INSTANCE.getUnlocalizedName());
    }
}
