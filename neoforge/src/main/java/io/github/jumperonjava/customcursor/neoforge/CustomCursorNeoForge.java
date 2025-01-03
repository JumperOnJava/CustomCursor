package io.github.jumperonjava.customcursor.neoforge;

import io.github.jumperonjava.customcursor.CustomCursorInit;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import org.slf4j.LoggerFactory;

@Mod("customcursor")
public class CustomCursorNeoForge {
	public CustomCursorNeoForge() {
		LoggerFactory.getLogger("customcursor").info("CustomCursorNeoForge created");
		CustomCursorInit.entrypoint((s)-> ModList.get().isLoaded(s));
	}
}
