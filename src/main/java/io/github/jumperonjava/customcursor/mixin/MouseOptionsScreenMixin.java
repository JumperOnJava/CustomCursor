package io.github.jumperonjava.customcursor.mixin;

import io.github.jumperonjava.customcursor.editor.CursorEditScreen;
import io.github.jumperonjava.customcursor.util.VF;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.options.controls.ControlsScreen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ControlsScreen.class)
public abstract class MouseOptionsScreenMixin extends OptionsSubScreen {

    protected MouseOptionsScreenMixin() {
        super(null,null,null);
    }
        @Inject(method = "addOptions", at = @At("TAIL"))
        public void addOptions(CallbackInfo ci) {
            this.list.addSmall
                            (
                                    Button.builder(Component.translatable("customcursor.openbutton"),
                                            (buttonWidget) -> VF.setScreen(CursorEditScreen.createCursorEditScreen(this))).build(),
                                    Button.builder(Component.translatable("customcursor.openbutton"),
                                            (buttonWidget) -> VF.setScreen(CursorEditScreen.createCursorEditScreen(this))).bounds(0,0,0,0).build()
                            );
        }
}
