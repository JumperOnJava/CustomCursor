//? if neoforge {
/*package io.github.jumperonjava.customcursor.neoforge;

import io.github.jumperonjava.customcursor.editor.CursorEditScreen;
import io.github.jumperonjava.customcursor.CustomCursorInit;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
//? if <1.21 {
import net.neoforged.neoforge.client.ConfigScreenHandler;
//?} else {
/^import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;

^///?}

//? if >= 1.21.6 {

/^import net.neoforged.neoforge.client.event.lifecycle.ClientStartedEvent;

^///?}
@Mod("customcursor")
public class CustomCursorNeoForge {

    //? if >= 1.21.6 {

    /^public CustomCursorNeoForge(IEventBus bus) {
        NeoForge.EVENT_BUS.addListener((ClientStartedEvent event)->{this.entrypoint();});
    }

    ^///?} else {

    public CustomCursorNeoForge() {
       this.entrypoint();
    }

    //?}




	public void entrypoint() {
		CustomCursorInit.entrypoint((s)-> ModList.get().isLoaded(s));
        ModLoadingContext.get().registerExtensionPoint(
                //? if <1.21 {
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        ((client, parent) -> CursorEditScreen.createCursorEditScreen(parent))
                )
                //?} else {
                /^IConfigScreenFactory.class,
                () -> (client, parent) -> CursorEditScreen.createCursorEditScreen(parent)
                ^///?}
        );
	}
}
*///?}