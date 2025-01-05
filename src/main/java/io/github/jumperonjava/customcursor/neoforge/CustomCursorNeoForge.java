//? if neoforge {
/*package io.github.jumperonjava.customcursor.neoforge;

import io.github.jumperonjava.customcursor.CursorEditScreen;
import io.github.jumperonjava.customcursor.CustomCursorInit;
import net.minecraft.client.gui.screen.Screen;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
//? if <1.21 {
import net.neoforged.neoforge.client.ConfigScreenHandler;
//?} else {
/^import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
^///?}
@Mod("customcursor")
public class CustomCursorNeoForge {
	public CustomCursorNeoForge() {
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