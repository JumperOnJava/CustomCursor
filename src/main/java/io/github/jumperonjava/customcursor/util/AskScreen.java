package io.github.jumperonjava.customcursor.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public abstract class AskScreen<T> extends Screen {
    private final Consumer<T> onSuccess;
    private final Runnable onFail;
    private Screen parent;

    public AskScreen(Consumer<T> onSuccess, Runnable onFail){
        super(Text.empty());
        this.onSuccess = onSuccess;
        this.onFail = onFail;
    }

    public void success(T ret){
        onSuccess.accept(ret);
        this.initClose();
    }
    public void fail(){
        onFail.run();
        this.initClose();
    }
    public void initClose(){
        closeScreen(this);
    }
    @Override
    public void close(){
        fail();
    }
    public static <T> void ask(AskScreen<T> askScreen){
        var client = MinecraftClient.getInstance();
        var currentScreen = client.currentScreen;
        if(currentScreen==null){
            client.setScreen(askScreen);
            return;
        }
        client.setScreen(askScreen);
        askScreen.parent = currentScreen;
    }

    private static <T extends AskScreen<?>> void closeScreen(AskScreen screen) {
        MinecraftClient.getInstance().setScreen(screen.parent);
    }
}
