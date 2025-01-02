package forge.io.github.jumperonjava.customcursor;

import io.github.jumperonjava.customcursor.CustomCursorInit;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod("customcursor")
public class CustomCursorForge {
	public CustomCursorForge() {
		CustomCursorInit.entrypoint((s)-> ModList.get().isLoaded(s));
	}
}
