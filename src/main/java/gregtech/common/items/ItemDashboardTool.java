package gregtech.common.items;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.items.GTGenericItem;
import gregtech.client.hud.CompositeWidget;
import gregtech.client.hud.HUDGui;
import gregtech.client.hud.HUDManager;
import gregtech.client.hud.elements.CheckboxElement;
import gregtech.client.hud.elements.DroplistElement;
import gregtech.client.hud.elements.GraphElement;
import gregtech.client.hud.elements.RectElement;
import gregtech.client.hud.elements.TextElement;
import gregtech.client.hud.elements.TextInputElement;

public class ItemDashboardTool extends GTGenericItem {

    public static boolean dashboardEnabled = false;

    public ItemDashboardTool(String unlocalized, String english, String tooltip) {
        super(unlocalized, english, tooltip);
        setMaxStackSize(1);

        HUDManager hud = HUDManager.getInstance();
        hud.register();
        hud.setEditGuiSupplier(() -> new HUDGui.GuiHUDEdit(hud));
    }

    @Override
    protected void addAdditionalToolTips(List<String> list, ItemStack stack, EntityPlayer player) {
        list.add("Author: §9Vortex");
        list.add("Right-click to toggle dashboard HUD");
        list.add("Press RSHIFT to toggle edit mode");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (world.isRemote) {
            dashboardEnabled = !dashboardEnabled;
            HUDManager hud = HUDManager.getInstance();
            hud.setEnabled(dashboardEnabled);

            if (dashboardEnabled && hud.getWidgets()
                .isEmpty()) {
                createWidgets();
            }
        }

        return stack;
    }

    private double lastPlayerY = 0.0;

    @SideOnly(Side.CLIENT)
    private void createWidgets() {
        HUDManager hud = HUDManager.getInstance();

        hud.addWidget(
            new CompositeWidget(10, 10).isConfigurable()
                .addElement(
                    new RectElement().size(140, 90)
                        .offset(-10, -10))
                .addElement(new GraphElement(() -> {
                    if (Minecraft.getMinecraft().thePlayer != null) {
                        lastPlayerY = Minecraft.getMinecraft().thePlayer.posY;
                    }
                    return lastPlayerY;
                }).size(120, 50))
                .addElement(
                    new RectElement().size(102, 18)
                        .offset(-5, 55))
                .addElement(
                    new TextElement(() -> "Player Y: " + String.format("%.5f", lastPlayerY)).offsetVertical(60)));

        hud.addWidget(
            new CompositeWidget(10, 130).isConfigurable()
                .addElement(new TextInputElement(text -> {
                    try {
                        int val = Integer.parseInt(text);
                        System.out.println("Input value: " + val);
                    } catch (NumberFormatException ignored) {}
                }).size(80, 18)));

        hud.addWidget(
            new CompositeWidget(10, 160).isConfigurable()
                .addElement(
                    new DroplistElement(
                        () -> Arrays.asList("Option 1", "Option 2", "Option 3"),
                        0,
                        selected -> System.out.println("Selected: " + selected)).size(100, 18)));

        hud.addWidget(
            new CompositeWidget(10, 190).isConfigurable()
                .addElement(
                    new CheckboxElement(
                        "Enable feature",
                        false,
                        checked -> System.out.println("Checkbox: " + checked))));
    }

}
