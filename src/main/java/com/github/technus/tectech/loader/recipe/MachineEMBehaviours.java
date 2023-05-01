package com.github.technus.tectech.loader.recipe;

import static com.github.technus.tectech.loader.recipe.BaseRecipeLoader.getItemContainer;

import com.github.technus.tectech.thing.metaTileEntity.multi.em_machine.Behaviour_Centrifuge;
import com.github.technus.tectech.thing.metaTileEntity.multi.em_machine.Behaviour_ElectromagneticSeparator;
import com.github.technus.tectech.thing.metaTileEntity.multi.em_machine.Behaviour_Recycler;
import com.github.technus.tectech.thing.metaTileEntity.multi.em_machine.GT_MetaTileEntity_EM_machine;

import gregtech.api.enums.ItemList;

public class MachineEMBehaviours implements Runnable {

    @Override
    public void run() {
        registerMachineEMBehaviours();
    }

    private void registerMachineEMBehaviours() {
        GT_MetaTileEntity_EM_machine
                .registerBehaviour(() -> new Behaviour_Centrifuge(5), ItemList.Machine_IV_Centrifuge.get(1));
        GT_MetaTileEntity_EM_machine
                .registerBehaviour(() -> new Behaviour_Centrifuge(6), getItemContainer("CentrifugeLuV").get(1));
        GT_MetaTileEntity_EM_machine
                .registerBehaviour(() -> new Behaviour_Centrifuge(7), getItemContainer("CentrifugeZPM").get(1));
        GT_MetaTileEntity_EM_machine
                .registerBehaviour(() -> new Behaviour_Centrifuge(8), getItemContainer("CentrifugeUV").get(1));
        GT_MetaTileEntity_EM_machine
                .registerBehaviour(() -> new Behaviour_Centrifuge(9), getItemContainer("CentrifugeUHV").get(1));
        GT_MetaTileEntity_EM_machine
                .registerBehaviour(() -> new Behaviour_Centrifuge(10), getItemContainer("CentrifugeUEV").get(1));
        GT_MetaTileEntity_EM_machine
                .registerBehaviour(() -> new Behaviour_Centrifuge(11), getItemContainer("CentrifugeUIV").get(1));
        GT_MetaTileEntity_EM_machine
                .registerBehaviour(() -> new Behaviour_Centrifuge(12), getItemContainer("CentrifugeUMV").get(1));

        GT_MetaTileEntity_EM_machine.registerBehaviour(
                () -> new Behaviour_ElectromagneticSeparator(5),
                ItemList.Machine_IV_ElectromagneticSeparator.get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(
                () -> new Behaviour_ElectromagneticSeparator(6),
                getItemContainer("ElectromagneticSeparatorLuV").get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(
                () -> new Behaviour_ElectromagneticSeparator(7),
                getItemContainer("ElectromagneticSeparatorZPM").get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(
                () -> new Behaviour_ElectromagneticSeparator(8),
                getItemContainer("ElectromagneticSeparatorUV").get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(
                () -> new Behaviour_ElectromagneticSeparator(9),
                getItemContainer("ElectromagneticSeparatorUHV").get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(
                () -> new Behaviour_ElectromagneticSeparator(10),
                getItemContainer("ElectromagneticSeparatorUEV").get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(
                () -> new Behaviour_ElectromagneticSeparator(11),
                getItemContainer("ElectromagneticSeparatorUIV").get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(
                () -> new Behaviour_ElectromagneticSeparator(12),
                getItemContainer("ElectromagneticSeparatorUMV").get(1));

        GT_MetaTileEntity_EM_machine
                .registerBehaviour(() -> new Behaviour_Recycler(5), ItemList.Machine_IV_Recycler.get(1));
        GT_MetaTileEntity_EM_machine
                .registerBehaviour(() -> new Behaviour_Recycler(6), getItemContainer("RecyclerLuV").get(1));
        GT_MetaTileEntity_EM_machine
                .registerBehaviour(() -> new Behaviour_Recycler(7), getItemContainer("RecyclerZPM").get(1));
        GT_MetaTileEntity_EM_machine
                .registerBehaviour(() -> new Behaviour_Recycler(8), getItemContainer("RecyclerUV").get(1));
        GT_MetaTileEntity_EM_machine
                .registerBehaviour(() -> new Behaviour_Recycler(9), getItemContainer("RecyclerUHV").get(1));
        GT_MetaTileEntity_EM_machine
                .registerBehaviour(() -> new Behaviour_Recycler(10), getItemContainer("RecyclerUEV").get(1));
        GT_MetaTileEntity_EM_machine
                .registerBehaviour(() -> new Behaviour_Recycler(11), getItemContainer("RecyclerUIV").get(1));
        GT_MetaTileEntity_EM_machine
                .registerBehaviour(() -> new Behaviour_Recycler(12), getItemContainer("RecyclerUMV").get(1));
    }
}
