package io.github.jumperonjava.customcursor.fabric;

import net.fabricmc.api.ModInitializer;
import io.github.jumperonjava.customcursor.CustomCursorInit;
import net.fabricmc.loader.api.FabricLoader;

public class CustomCursorFabric implements ModInitializer {
	@Override
	public void onInitialize() {
		CustomCursorInit.entrypoint((s)-> FabricLoader.getInstance().isModLoaded(s));
	}
}
