package io.github.jumperonjava.customcursor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.blaze3d.platform.cursor.CursorType;
import io.github.jumperonjava.customcursor.util.FileReadWrite;
import io.github.jumperonjava.customcursor.editor.TextureFolder;
import io.github.jumperonjava.customcursor.util.WindowingBackend;
import net.minecraft.client.Minecraft;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.function.Function;

public class CustomCursorInit {
    public static final String MOD_ID = "customcursorcomm";
    public static final Logger LOGGER = LoggerFactory.getLogger("CustomCursor");
    private static TextureFolder TEXTURE_FOLDER;

    private static CursorConfigStorage config;
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void entrypoint(Function<String, Boolean> isModLoaded) {
        //WARNING: DUMB
        //? if neoforge && = 1.21.5 {
		/*new Thread(() -> {
			while(Minecraft.getInstance() == null){
				Thread.yield();
			}
			getConfig();
		}).start();
		if(1==1){
			return;
		}
		*///?}
        getConfig();
    }

    private static CursorConfigStorage loadConfig() {
        if (getConfigFile().exists()) {
            return gson.fromJson(FileReadWrite.read(getConfigFile()), CursorConfigStorage.class);
        } else {
            return new CursorConfigStorage();
        }
    }

    private static File getConfigFile() {
        return Minecraft.getInstance().gameDirectory.toPath().resolve("config/customcursor.json").toFile();
    }

    public static CursorConfigStorage getConfig() {
        if (config == null)
            setConfig(loadConfig());
        return config;
    }

    public static void setConfig(CursorConfigStorage config) {
        var json = gson.toJson(config);
        FileReadWrite.write(getConfigFile(), json);
        CustomCursorInit.config = config;
    }

    public static void updateCursors() {
        var list = new ArrayList<>(CursorSettings.cursors().values());
        list.add(CursorType.DEFAULT);
        list.forEach((cursorType -> {
            if(config.settings.enabled){
                var sprite = config.settings.cursorToSprite(cursorType);
                var rendered = CursorRenderer.renderState(config.settings.size,sprite.x,sprite.y,sprite.rotation,sprite.mirroredX,sprite.mirroredY,sprite.identifier);
                cursorType.handle = WindowingBackend.INSTANCE.getCursorHandle(rendered);
            } else{
                cursorType.handle = WindowingBackend.INSTANCE.getCursorVanillaHandle(cursorType);
            }
        }));
    }

    public static TextureFolder getTextureFolder() {
        if (TEXTURE_FOLDER == null) {
            TEXTURE_FOLDER = new TextureFolder(
                    Minecraft.getInstance().gameDirectory.toPath().resolve("cursors"),
                    "cursorfolder"
            );
        }
        return TEXTURE_FOLDER;
    }
}