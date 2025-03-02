package gregtech.common.items;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.factory.GuiData;
import com.cleanroommc.modularui.factory.GuiFactories;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.SyncHandlers;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widget.ScrollWidget;
import com.cleanroommc.modularui.widget.scroll.VerticalScrollData;
import com.cleanroommc.modularui.widgets.*;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;
import gregtech.api.GregTechAPI;
import gregtech.api.items.GTGenericItem;
import gregtech.common.covers.redstone.CoverAdvancedWirelessRedstoneBase.CoverData;
import gregtech.common.misc.spaceprojects.SpaceProjectManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class ItemSniffer extends GTGenericItem implements IGuiHolder<GuiData> {
    private static final Map<Integer, String> regularWirelessLabels = new HashMap<>();
    private static final Map<String, Map<Integer, Map<CoverData, String>>> advWirelessLabels = new HashMap<>();
    private final ItemStackHandler stackHandler = new ItemStackHandler(1);
    private String uuid;

    public ItemSniffer(String aUnlocalized, String aEnglish, String aEnglishTooltip) {
        super(aUnlocalized, aEnglish, aEnglishTooltip);
        ;
    }

    public Map<CoverData, String> getCoversOnFrequency(String uuid, int frequency){
        Map<Integer, Map<CoverData, String>> publicCovers = advWirelessLabels.getOrDefault(uuid, new HashMap<>());
        return publicCovers.getOrDefault(frequency, new HashMap<>());
    }



    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

        if (!world.isRemote) {
            this.uuid = String.valueOf(player.getUniqueID());

            //this.stack = stack;
            loadFromNBT(stack.getTagCompound());
            GuiFactories.item().open(player);
//            NBTTagCompound tag = serializeMapToNBT(new NBTTagCompound(), advWirelessLabels);
//            System.out.println(tag);
//            stack.setTagCompound(tag);

        }
        return super.onItemRightClick(stack,world,player);
    }
    public static NBTTagCompound serializeRegularToNBT(NBTTagCompound base){
        NBTTagCompound regularWireless = new NBTTagCompound();
        regularWirelessLabels.forEach((frequency, label) -> {
            regularWireless.setString(String.valueOf(frequency), label);
        });
        base.setTag("Regular", regularWireless);
        return base;
    }
    public static <K, V> NBTTagCompound serializeAdvancedToNBT(NBTTagCompound base, Map<K, V> map) {
        for(Map.Entry<K, V> entry : map.entrySet()) {
            if(entry.getValue() instanceof Map mapEntry) {
                base.setTag(entry.getKey().toString(), serializeAdvancedToNBT(new NBTTagCompound(), mapEntry));
            } else if(entry.getKey() instanceof CoverData data){
                NBTTagCompound coverTag = new NBTTagCompound();
                coverTag.setInteger("x", data.x);
                coverTag.setInteger("y", data.y);
                coverTag.setInteger("z", data.z);
                coverTag.setInteger("dim", data.dim);
                coverTag.setInteger("side", data.side);
                coverTag.setString("label", (String) entry.getValue());
                base.setTag(String.valueOf(data.hashCode()), coverTag);
            } else if(entry.getValue() instanceof String stringEntry) {
                base.setString(entry.getKey().toString(), stringEntry);
            } else if(entry.getValue() instanceof Integer intEntry) {
                base.setInteger(entry.getKey().toString(), intEntry);
            }
        }
        return base;
    }

    public void loadFromNBT(NBTTagCompound tag){
        System.out.println(tag);
        NBTTagCompound regular = tag.getCompoundTag("Regular");
        if (!regular.hasNoTags()){
            regular.tagMap.keySet().forEach(frequency -> {
                regularWirelessLabels.put(Integer.valueOf((String) frequency), regular.getString(String.valueOf(frequency)));
            });
        }
        String[] owners = {"Public", this.uuid};
        for(String owner : owners){
            if (tag.hasKey(owner)){
                NBTTagCompound frequencyMap = tag.getCompoundTag(owner);
                if(frequencyMap.hasNoTags()) continue;

                frequencyMap.tagMap.keySet().forEach(frequency -> {
                    NBTTagCompound coverMap = frequencyMap.getCompoundTag((String) frequency);
                    if (coverMap.hasNoTags()) return;

                    coverMap.tagMap.keySet().forEach(cover -> {
                        NBTTagCompound data = coverMap.getCompoundTag((String) cover);
                        int x = data.getInteger("x");
                        int y = data.getInteger("y");
                        int z = data.getInteger("z");
                        int dim = data.getInteger("dim");
                        int side = data.getInteger("side");
                        CoverData coverData = new CoverData(x, y, z, dim, side);
                        Map<Integer, Map<CoverData,String>> frequencies = advWirelessLabels.computeIfAbsent(owner, k -> new HashMap<>());
                        Map<CoverData,String> covers = frequencies.computeIfAbsent(Integer.valueOf((String) frequency), k -> new HashMap<>());
                        System.out.println(data.getString("label"));
                        covers.put(coverData, data.getString("label"));
                    });

                });

            }
        }
        System.out.println(tag);
    }
    @Override
    public ModularPanel buildUI(GuiData guiData, PanelSyncManager guiSyncManager) {
        PagedWidget.Controller controller = new PagedWidget.Controller();





        ModularPanel panel = ModularPanel.defaultPanel("redstone_sniffer");
        panel.flex()
                .sizeRel(0.5f, 0.5f)
                .align(Alignment.Center);

        PagedWidget data = new PagedWidget();
        data.sizeRel(1,0.75f);
        data.controller(controller);



        ScrollWidget wirelessPage = new ScrollWidget(new VerticalScrollData());
        wirelessPage.sizeRel(1, 0.8f);
        wirelessPage.getScrollArea().getScrollY().setScrollSize(35*GregTechAPI.sWirelessRedstone.size());

        AtomicInteger i = new AtomicInteger(0);

        //Process regular wireless redstone frequencies

        GregTechAPI.sWirelessRedstone.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry ->{
            int freq = entry.getKey();
            boolean isPrivate = freq > 65535;
            int displayFreq = isPrivate ? freq - 65536 : freq;
            wirelessPage.child(
                new Row()
                .sizeRel(1f,0.2f)
                .pos(0,i.get()*35)
                .child(new TextWidget(String.valueOf(displayFreq)).widthRel(1.0f/3.0f).alignment(Alignment.Center))
                .child(new TextWidget(isPrivate ? "Yes" : "No").widthRel(1.0f/3.0f).alignment(Alignment.Center))
                .child(new TextFieldWidget()
                    .align(Alignment.CenterRight)
                    .sizeRel(1.0f/3.25f, 1)
                    .value(SyncHandlers.string(() -> regularWirelessLabels.getOrDefault(freq, "Description"),
                        label -> {
                        regularWirelessLabels.put(freq, label);
                        if(!guiSyncManager.isClient()){
                            NBTTagCompound tag = serializeRegularToNBT(guiSyncManager.getPlayer().getHeldItem().getTagCompound());
                            System.out.println(tag);
                            guiSyncManager.getPlayer().getHeldItem().setTagCompound(tag);
                        }
                        }))
                    ));
            i.getAndIncrement();
        });


        ParentWidget regularWireless = new ParentWidget();
        regularWireless.sizeRel(1);
        regularWireless.child(new Column()
            .child(new Row()
                .margin(5)
                .heightRel(0.1f)
                .child(new TextWidget("Frequency").widthRel(1.0f/3.0f).alignment(Alignment.Center))
                .child(new TextWidget("Private").widthRel(1.0f/3.0f).alignment(Alignment.Center))
                .child(new TextWidget("Label").widthRel(1.0f/3.0f).alignment(Alignment.Center)))
            .child(wirelessPage)
        );
        data.addPage(regularWireless);



        //Process advanced wireless redstone frequencies
        Map<Integer, Map<CoverData, Byte>> publicFreqs = GregTechAPI.sAdvancedWirelessRedstone.getOrDefault("null", new ConcurrentHashMap<>());
        Map<Integer, Map<CoverData, Byte>> privateFreqs = GregTechAPI.sAdvancedWirelessRedstone.getOrDefault(this.uuid, new ConcurrentHashMap<>());

        int advFreqSize = publicFreqs.size() + privateFreqs.size();
        ScrollWidget advWirelessPage = new ScrollWidget(new VerticalScrollData());
        advWirelessPage.sizeRel(1, 0.9f);
        advWirelessPage.getScrollArea().getScrollY().setScrollSize(35*(advFreqSize+1));

        i.set(0);

        publicFreqs.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(frequencyEntry ->{
            frequencyEntry.getValue().forEach((cover,useless) ->{
                int freq = frequencyEntry.getKey();
                advWirelessPage.child(new Row()
                    .sizeRel(1f,0.2f)
                    .pos(0,i.get()*35)
                    .child(new TextWidget("Public")
                        .widthRel(0.2f)
                        .alignment(Alignment.Center))
                    .child(new TextWidget(String.valueOf(freq)).widthRel(0.2f).alignment(Alignment.Center))
                    .child(new TextWidget(cover.getInfo()).widthRel(0.25f).alignment(Alignment.Center))
                    .child(new TextFieldWidget()
                        .sizeRel(0.2f, 1)
                        .value(SyncHandlers.string(() -> {
                            Map<CoverData, String> coversOnFrequency = getCoversOnFrequency("Public", freq);
                            return coversOnFrequency.getOrDefault(cover, "Description");
                        }, label -> {
                            if(!guiSyncManager.isClient()){
                                Map<Integer, Map<CoverData,String>> frequencies = advWirelessLabels.computeIfAbsent("Public",k -> new HashMap<>());
                                Map<CoverData, String> covers = frequencies.computeIfAbsent(freq, k -> new HashMap<>());
                                covers.put(cover, label);
                                NBTTagCompound tag = serializeAdvancedToNBT(new NBTTagCompound(), advWirelessLabels);
                                System.out.println(tag);
                                guiSyncManager.getPlayer().getHeldItem().setTagCompound(tag);
                            }

                        }))
                    )
                );
                i.getAndIncrement();
            });
        });
        privateFreqs.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(frequencyEntry ->{
            frequencyEntry.getValue().forEach((cover,useless) ->{
                int freq = frequencyEntry.getKey();
                advWirelessPage.child(new Row()
                    .sizeRel(1f,0.2f)
                    .pos(0,i.get()*35)
                    .child(new TextWidget(SpaceProjectManager.getPlayerNameFromUUID(UUID.fromString(this.uuid)))
                        .widthRel(0.2f)
                        .alignment(Alignment.Center))
                    .child(new TextWidget(String.valueOf(freq)).widthRel(0.2f).alignment(Alignment.Center))
                    .child(new TextWidget(cover.getInfo()).widthRel(0.25f).alignment(Alignment.Center))
                    .child(new TextFieldWidget()
                        .sizeRel(0.2f, 1)
                        .value(SyncHandlers.string(() -> {
                            Map<CoverData, String> coversOnFrequency = getCoversOnFrequency(this.uuid, freq);
                            return coversOnFrequency.getOrDefault(cover, "Description");
                        }, label -> {
                            if(!guiSyncManager.isClient()){
                                Map<Integer, Map<CoverData,String>> frequencies = advWirelessLabels.computeIfAbsent(this.uuid,k -> new HashMap<>());
                                Map<CoverData, String> covers = frequencies.computeIfAbsent(freq, k -> new HashMap<>());
                                covers.put(cover, label);
                                NBTTagCompound tag = serializeAdvancedToNBT(new NBTTagCompound(), advWirelessLabels);
                                System.out.println(tag);
                                guiSyncManager.getPlayer().getHeldItem().setTagCompound(tag);
                            }

                        }))
                        .setValidator(s -> s)
                    )
                );
                i.getAndIncrement();
            });
        });

        ParentWidget advWireless = new ParentWidget();
        advWireless.sizeRel(1);
        advWireless.child(new Column()
            .child(new Row()
                .margin(5)
                .heightRel(0.1f)
                .child(new TextWidget("Owner").widthRel(0.2f).alignment(Alignment.Center))
                .child(new TextWidget("Frequency").widthRel(0.2f).alignment(Alignment.Center))
                .child(new TextWidget("Coords").widthRel(0.25f).alignment(Alignment.Center))
                .child(new TextWidget("Label").widthRel(0.2f).alignment(Alignment.Center))
                .child(new TextWidget("Action?").widthRel(0.15f).alignment(Alignment.Center))
            )
            .child(advWirelessPage)
        );
        data.addPage(advWireless);



        panel.child(new Column()
            .child(new Row()
                .heightRel(0.1f)
                .margin(10)
                .child(new PageButton(0, controller)
                    .widthRel(0.5f)
                    .align(Alignment.CenterLeft)
                    .overlay(IKey.dynamic(() -> "Regular Wireless")))
                .child(new PageButton(1, controller)
                    .widthRel(0.5f)
                    .align(Alignment.CenterRight)
                    .overlay(IKey.dynamic(() -> "Advanced Wireless"))))
            .child(data)
            .margin(10));
        System.out.println(wirelessPage.getChildren().size());

        return panel;
    }


    public void processAdvancedFrequencies(Map<Integer, Map<CoverData, Byte>> frequencyMap, String owner, ScrollWidget widget, AtomicInteger i, ItemStack itemStack){

    }

}
