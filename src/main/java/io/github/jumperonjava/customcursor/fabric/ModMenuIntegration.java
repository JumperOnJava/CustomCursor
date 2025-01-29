//? if fabric {
package io.github.jumperonjava.customcursor.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.jumperonjava.customcursor.CursorEditScreen;

public class ModMenuIntegration implements ModMenuApi {
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return CursorEditScreen::createCursorEditScreen;
    }
}
//?}
