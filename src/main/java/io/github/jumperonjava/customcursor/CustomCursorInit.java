package io.github.jumperonjava.customcursor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.blaze3d.platform.cursor.CursorType;
import com.mojang.blaze3d.platform.cursor.CursorTypes;
import io.github.jumperonjava.customcursor.util.FileReadWrite;
import io.github.jumperonjava.customcursor.editor.TextureFolder;
import io.github.jumperonjava.customcursor.util.WindowingBackend;
import net.minecraft.client.Minecraft;

//? if >= 26.3{
/*import org.lwjgl.sdl.SDLMouse;
*///?}

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class CustomCursorInit {
    public static final String MOD_ID = "customcursorcomm";
    public static final Logger LOGGER = LoggerFactory.getLogger("CustomCursor");
    private static TextureFolder TEXTURE_FOLDER;

    private static CursorConfigStorage config;
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void entrypoint(Function<String, Boolean> isModLoaded) {
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

    public static TextureFolder getTextureFolder() {
        if (TEXTURE_FOLDER == null) {
            TEXTURE_FOLDER = new TextureFolder(
                    Minecraft.getInstance().gameDirectory.toPath().resolve("cursors"),
                    "cursorfolder"
            );
        }
        return TEXTURE_FOLDER;
    }

    public static void updateCursors() {
        var list = new ArrayList<>(CursorSettings.cursors().values());
        list.add(CursorType.DEFAULT);
        list.forEach((cursorType -> {
            if(config.settings.enabled){
                var sprite = config.settings.cursorToSprite(cursorType);
                var rendered = CursorRenderer.renderState(config.settings.size,sprite.x,sprite.y,sprite.rotation,sprite.mirroredX,sprite.mirroredY,sprite.identifier);
                var handle = WindowingBackend.INSTANCE.getCursorHandle(rendered);
                cursorType.handle = handle;
                if(handle == 0){
                    LOGGER.warn("Cursortype {} handle is 0",cursorType.toString());
                    cursorType.handle = vanillaHandles.get(cursorType);
                }
            } else{
                cursorType.handle = vanillaHandles.get(cursorType);
            }
        }));

        //? if >= 26.3 {
        /*CursorTypes.ARROW.select();
         *///?} else {
        CursorTypes.ARROW.select(Minecraft.getInstance().getWindow());
        //?}
    }

    public static Map<CursorType, Long> vanillaHandles = new HashMap<>();
    public static void backupVanillaHandles() {
        //? if >= 26.3 {
        /*vanillaHandles.put(CursorType.DEFAULT, CursorType.createStandardCursor(SDLMouse.SDL_SYSTEM_CURSOR_DEFAULT, "arrow", CursorType.DEFAULT).handle);
        vanillaHandles.put(CursorTypes.ARROW, CursorType.createStandardCursor(SDLMouse.SDL_SYSTEM_CURSOR_DEFAULT, "arrow", CursorType.DEFAULT).handle);
        vanillaHandles.put(CursorTypes.IBEAM, CursorType.createStandardCursor(SDLMouse.SDL_SYSTEM_CURSOR_TEXT, "ibeam", CursorType.DEFAULT).handle);
        vanillaHandles.put(CursorTypes.CROSSHAIR, CursorType.createStandardCursor(SDLMouse.SDL_SYSTEM_CURSOR_CROSSHAIR, "crosshair", CursorType.DEFAULT).handle);
        vanillaHandles.put(CursorTypes.POINTING_HAND, CursorType.createStandardCursor(SDLMouse.SDL_SYSTEM_CURSOR_POINTER, "pointing_hand", CursorType.DEFAULT).handle);
        vanillaHandles.put(CursorTypes.RESIZE_NS, CursorType.createStandardCursor(SDLMouse.SDL_SYSTEM_CURSOR_NS_RESIZE, "resize_ns", CursorType.DEFAULT).handle);
        vanillaHandles.put(CursorTypes.RESIZE_EW, CursorType.createStandardCursor(SDLMouse.SDL_SYSTEM_CURSOR_EW_RESIZE, "resize_ew", CursorType.DEFAULT).handle);
        vanillaHandles.put(CursorTypes.RESIZE_ALL, CursorType.createStandardCursor(SDLMouse.SDL_SYSTEM_CURSOR_MOVE, "resize_all", CursorType.DEFAULT).handle);
        vanillaHandles.put(CursorTypes.NOT_ALLOWED, CursorType.createStandardCursor(SDLMouse.SDL_SYSTEM_CURSOR_NOT_ALLOWED, "not_allowed", CursorType.DEFAULT).handle);
        *///?} else {
        vanillaHandles.put(CursorType.DEFAULT, 0L);
        vanillaHandles.put(CursorTypes.ARROW, CursorType.createStandardCursor(221185, "arrow", CursorType.DEFAULT).handle);
        vanillaHandles.put(CursorTypes.IBEAM, CursorType.createStandardCursor(221186, "ibeam", CursorType.DEFAULT).handle);
        vanillaHandles.put(CursorTypes.CROSSHAIR, CursorType.createStandardCursor(221187, "crosshair", CursorType.DEFAULT).handle);
        vanillaHandles.put(CursorTypes.POINTING_HAND, CursorType.createStandardCursor(221188, "pointing_hand", CursorType.DEFAULT).handle);
        vanillaHandles.put(CursorTypes.RESIZE_NS, CursorType.createStandardCursor(221190, "resize_ns", CursorType.DEFAULT).handle);
        vanillaHandles.put(CursorTypes.RESIZE_EW, CursorType.createStandardCursor(221189, "resize_ew", CursorType.DEFAULT).handle);
        vanillaHandles.put(CursorTypes.RESIZE_ALL, CursorType.createStandardCursor(221193, "resize_all", CursorType.DEFAULT).handle);
        vanillaHandles.put(CursorTypes.NOT_ALLOWED, CursorType.createStandardCursor(221194, "not_allowed", CursorType.DEFAULT).handle);
        //?}
    }
}