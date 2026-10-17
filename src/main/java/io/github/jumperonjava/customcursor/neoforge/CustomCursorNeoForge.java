//? if neoforge {
/*package io.github.jumperonjava.customcursor.neoforge;

import io.github.jumperonjava.customcursor.editor.CursorEditScreen;
import io.github.jumperonjava.customcursor.CustomCursorInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;

import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;

import net.neoforged.neoforge.client.event.lifecycle.ClientStartedEvent;
@Mod("customcursor")
public class CustomCursorNeoForge {

    public CustomCursorNeoForge(IEventBus bus) {
        NeoForge.EVENT_BUS.addListener((ClientStartedEvent event)->{this.entrypoint();});
    }

	public void entrypoint() {
		CustomCursorInit.entrypoint((s)-> ModList.get().isLoaded(s));
        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                () -> (client, parent) -> CursorEditScreen.createCursorEditScreen(parent)
        );
	}
}
*///?}