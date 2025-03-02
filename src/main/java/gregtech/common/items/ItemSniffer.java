package gregtech.common.items;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.factory.GuiData;
import com.cleanroommc.modularui.factory.GuiFactories;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
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
import net.minecraft.world.World;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class ItemSniffer extends GTGenericItem implements IGuiHolder<GuiData> {
    private static final Map<Integer, String> regularWirelessLabels = new ConcurrentHashMap<>();
    private static final Map<String, Map<Integer, Map<CoverData, String>>> advWirelessLabels = new ConcurrentHashMap<>();
    private String uuid;

    public ItemSniffer(String aUnlocalized, String aEnglish, String aEnglishTooltip) {
        super(aUnlocalized, aEnglish, aEnglishTooltip);
    }

    public Map<CoverData, String> getCoversOnFrequency(String uuid, int frequency){
        Map<Integer, Map<CoverData, String>> publicCovers = advWirelessLabels.getOrDefault(uuid, new ConcurrentHashMap<>());
        return publicCovers.getOrDefault(frequency, new ConcurrentHashMap<>());
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {

        if (!world.isRemote) {
            this.uuid = String.valueOf(player.getUniqueID());

            GuiFactories.item().open(player);
        }
        return super.onItemRightClick(stack,world,player);
    }


    @Override
    public ModularPanel buildUI(GuiData guiData, PanelSyncManager guiSyncManager) {
        PagedWidget.Controller controller = new PagedWidget.Controller();





        ModularPanel panel = ModularPanel.defaultPanel("redstone_sniffer");
        panel.flex()
                .sizeRel(0.5f, 0.5f)
                .align(Alignment.Center);

        PagedWidget data = new PagedWidget();
        data.sizeRel(1,0.65f);
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
                        label -> regularWirelessLabels.put(freq, label)))
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
        advWirelessPage.sizeRel(1, 0.8f);
        advWirelessPage.getScrollArea().getScrollY().setScrollSize(35*(advFreqSize+1));

        i.set(0);
        processAdvancedFrequencies(publicFreqs, "Public", advWirelessPage, i);
        processAdvancedFrequencies(privateFreqs, this.uuid, advWirelessPage, i);

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

    public void processAdvancedFrequencies(Map<Integer, Map<CoverData, Byte>> frequencyMap, String owner, ScrollWidget widget, AtomicInteger i){
        frequencyMap.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(frequencyEntry ->{
            frequencyEntry.getValue().forEach((cover,useless) ->{
                int freq = frequencyEntry.getKey();
                widget.child(new Row()
                    .sizeRel(1f,0.2f)
                    .pos(0,i.get()*35)
                    .child(new TextWidget(owner.equals("Public") ? "Public" : SpaceProjectManager.getPlayerNameFromUUID(UUID.fromString(owner)))
                        .widthRel(0.2f)
                        .alignment(Alignment.Center))
                    .child(new TextWidget(String.valueOf(freq)).widthRel(0.2f).alignment(Alignment.Center))
                    .child(new TextWidget(cover.getInfo()).widthRel(0.25f).alignment(Alignment.Center))
                    .child(new TextFieldWidget()
                        .sizeRel(0.2f, 1)
                        .value(SyncHandlers.string(() -> {
                            Map<CoverData, String> coversOnFrequency = getCoversOnFrequency(owner, freq);
                            return coversOnFrequency.getOrDefault(cover, "Description");
                        }, label -> {
                            Map<Integer, Map<CoverData,String>> frequencies = this.advWirelessLabels.computeIfAbsent(owner,k -> new ConcurrentHashMap<>());
                            Map<CoverData, String> covers = frequencies.computeIfAbsent(freq, k -> new ConcurrentHashMap<>());
                            covers.put(cover, label);
                        }))
                    )
                );
                i.getAndIncrement();
            });
        });
    }

}
