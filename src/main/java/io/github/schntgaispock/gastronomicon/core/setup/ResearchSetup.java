package io.github.schntgaispock.gastronomicon.core.setup;

import io.github.schntgaispock.gastronomicon.core.slimefun.GastroResearch;
import io.github.schntgaispock.gastronomicon.core.slimefun.GastroStacks;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResearchSetup {

    public static void setup() {
        GastroResearch.WOODEN_TOOLS
            .addItems(
                GastroStacks.ROLLING_PIN.item().clone(),
                GastroStacks.MORTAR_AND_PESTLE.item().clone())
            .register();
        GastroResearch.STEEL_TOOLS
            .addItems(
                GastroStacks.KITCHEN_KNIFE.item().clone(),
                GastroStacks.BLENDER.item().clone(),
                GastroStacks.PEELER.item().clone(),
                GastroStacks.BAKING_TRAY.item().clone(),
                GastroStacks.FRYING_PAN.item().clone(),
                GastroStacks.STEEL_POT.item().clone(),
                GastroStacks.STEEL_BOWL.item().clone(),
                GastroStacks.WHISK.item().clone())
            .register();
        GastroResearch.CULINARY_WORKBENCH.addItems(GastroStacks.CULINARY_WORKBENCH.item().clone()).register();
        GastroResearch.MULTI_STOVE.addItems(GastroStacks.MULTI_STOVE.item().clone()).register();
        GastroResearch.GRAIN_MILL.addItems(GastroStacks.MILL.item().clone()).register();
        GastroResearch.REFRIGERATOR.addItems(GastroStacks.REFRIGERATOR.item().clone()).register();
        GastroResearch.FERMENTER.addItems(GastroStacks.FERMENTER.item().clone(), GastroStacks.LARGE_FERMENTER.item().clone()).register();
        GastroResearch.CHEFS_HAT.addItems(GastroStacks.CHEFS_HAT.item().clone()).register();
        GastroResearch.TRAPS
            .addItems(
                GastroStacks.STEEL_WIRE.item().clone(),
                GastroStacks.STEEL_SPRING.item().clone(),
                GastroStacks.CRAB_TRAP.item().clone(),
                GastroStacks.HUNTING_TRAP.item().clone())
            .register();
        GastroResearch.FISHING_NETS
            .addItems(
                GastroStacks.FISHING_NET_I.item().clone(),
                GastroStacks.FISHING_NET_II.item().clone(),
                GastroStacks.FISHING_NET_III.item().clone())
            .register();
        GastroResearch.CHEF_ANDROID
            .addItems(
                GastroStacks.CHEF_ANDROID.item().clone(),
                GastroStacks.CHEF_ANDROID_TRAINER.item().clone())
            .register();
        GastroResearch.ELECTRIC_KITCHEN
            .addItems(
                GastroStacks.ELECTRIC_KITCHEN_I.item().clone(),
                GastroStacks.ELECTRIC_KITCHEN_II.item().clone(),
                GastroStacks.ELECTRIC_KITCHEN_III.item().clone())
            .register();
        GastroResearch.SICKLES
            .addItems(
                GastroStacks.WOODEN_SICKLE.item().clone(),
                GastroStacks.STEEL_SICKLE.item().clone(),
                GastroStacks.REINFORCED_SICKLE.item().clone())
            .register();
        GastroResearch.RAW_INGREDIENTS.register();
        GastroResearch.PROCESSED_INGREDIENTS.register();
        GastroResearch.FOOD.register();
    }

}
