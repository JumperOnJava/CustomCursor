//? if forge {
/*package io.github.jumperonjava.customcursor.forge;

import io.github.jumperonjava.customcursor.CursorEditScreen;
import io.github.jumperonjava.customcursor.CustomCursorInit;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod("customcursor")
public class CustomCursorForge {
	public CustomCursorForge() {
		CustomCursorInit.entrypoint((s)-> ModList.get().isLoaded(s));
        MinecraftForge.registerConfigScreen(CursorEditScreen::createCursorEditScreen);
	}
}
*///?}