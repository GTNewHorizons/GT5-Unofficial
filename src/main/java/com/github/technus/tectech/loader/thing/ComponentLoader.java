package com.github.technus.tectech.loader.thing;

import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IMaterialHandler;

public class ComponentLoader implements IMaterialHandler {
    public ComponentLoader(){
        Materials.add(this);
    }

    @Override
    public void onComponentInit() {
        //example
        //OrePrefixes.ring.enableComponent(Materials.RedAlloy);
    }

    @Override
    public void onComponentIteration(Materials materials) {

    }

    @Override
    public void onMaterialsInit() {

    }
}
