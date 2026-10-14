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

    //? if < 1.21 {
    /*@Inject(method = "init",at = @At("HEAD"),locals = LocalCapture.CAPTURE_FAILHARD)
    void inject(CallbackInfo ci){
        int k = this.height / 6 - 12 + 24 * 3;
        var cursorEditScreenButton = new Button.Builder(Component.translatable("customcursor.openbutton"),(buttonWidget)->{
            Minecraft.getInstance().setScreen(CursorEditScreen.createCursorEditScreen(this));
        }).bounds(width / 2 - 155, k, 310, 20).build();
        addRenderableWidget(cursorEditScreenButton);

    }
    @ModifyArg(method = "init",
            index = 1,
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/client/Options;getOperatorItemsTab()Lnet/minecraft/client/OptionInstance;"
                    ),
                    to = @At("TAIL")
            ),
            at = @At(
                    value = "INVOKE",
                    slice = "fd",
                    target = "Lnet/minecraft/client/gui/components/Button$Builder;bounds(IIII)Lnet/minecraft/client/gui/components/Button$Builder;"))
    int injected(int k){
        return k+24;
    }
    *///?} else {
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
    //?}
}
