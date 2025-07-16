package io.github.jumperonjava.customcursor.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FolderTextureAskList extends Screen {
    public static final int gap = 2;
    private final TextureFolder folder;
    private final Consumer<Identifier> onSuccess;

    ScrollListWidget list;

    protected Identifier EMPTY_TEXTURE = Identifier.of("minecraft", "empty");
    private List<Identifier> textures = new ArrayList<>();

    private Identifier selectedTexture = EMPTY_TEXTURE;

    private Screen parent;
    private String lastFilter = "";

    public FolderTextureAskList(TextureFolder folder, Consumer<Identifier> onSuccess) {
        super(Text.empty());
        this.onSuccess = onSuccess;
        this.folder = folder;
    }

    public static void ask(FolderTextureAskList askScreen) {
        var client = MinecraftClient.getInstance();
        var currentScreen = client.currentScreen;
        if (currentScreen == null) {
            client.setScreen(askScreen);
            return;
        }
        client.setScreen(askScreen);
        askScreen.parent = currentScreen;
    }
    @Override
    protected void init() {
        // Initializations
        list = new ScrollListWidget(client, width, height - 64, 0, 22, 40);
        var search = new TextFieldWidget(client.textRenderer, 0, 0, width, 20, Text.empty());
        var accept = new ButtonWidget.Builder(Text.translatable("customcursor.folder.accept"), b1 -> {
            if (selectedTexture != EMPTY_TEXTURE)
                success(selectedTexture);
        });
        var cancel1 = new ButtonWidget.Builder(Text.translatable("customcursor.folder.cancel"), b1 -> closeScreen());
        var cancel = new ButtonWidget.Builder(Text.translatable("customcursor.folder.openfolder"), b -> openCursorFolder());

        accept.dimensions(40 + gap, height - 20 - gap, 100, 20);
        cancel1.dimensions((int) (140 + gap * 1.5), height - 20 - gap, 100, 20);
        cancel.dimensions(240 + gap * 2, height - 20 - gap, 100, 20);

        // Adding Widgets
        refreshListByFilter("");
        addDrawableChild(list);
        search.setChangedListener(this::refreshListByFilter);
        addDrawableChild(search);
        addDrawableChild(accept.build());
        addDrawableChild(cancel1.build());
        addDrawableChild(cancel.build());
        addDrawable(((context, mouseX, mouseY, delta) ->
        {
            var texture = selectedTexture == EMPTY_TEXTURE ? Identifier.of("minecraft", "textures/item/barrier.png") : selectedTexture;
            TextureWidget.render(context,texture, gap / 2, height - 40 - gap / 2, 40, 40);
        }));

        setTexturesCallback(this::setTextures);
        addDrawable(((context, mouseX, mouseY, delta) -> context.drawText(
                textRenderer,
                Text.translatable("customcursor.folder.selected").append(": ").append(selectedTexture.toString()),
                45,
                height - 30 - 6 - gap / 2,
                0xFFFFFFFF,
                true)));
    }


    private void openCursorFolder() {
        Util.getOperatingSystem().open(folder.path.toFile());
    }

    public void setTexturesCallback(Consumer<List<Identifier>> consumer) {
        if (!this.textures.isEmpty())
            consumer.accept(textures);
        folder.redefineTextures(()->{
            this.textures = folder.getTextures();
            consumer.accept(this.textures);
        });

    }

    public void setTextures(List<Identifier> identifiers) {
        this.textures = identifiers;
        refreshListByFilter(lastFilter);
    }

    private void refreshListByFilter(String s) {
        lastFilter = s;
        list.children().clear();
        //? if < 1.21.4 {
        list.setScrollAmount(0);
         //?} else {
        /*list.setScrollY(0);
        *///?}
        for (var key : textures) {
            extracted(s, key);
        }
    }

    private void extracted(String s, Identifier key) {
        if (key == null) return;
        var id = key.toString();
        if (!id.toLowerCase().contains(s.toLowerCase()))
            return;
        Identifier finalKey = key;
        var entry = new ScrollListWidget.ScrollListEntry(key, () -> {
            this.selectedTexture = finalKey;
        });
        list.addEntry(entry);
    }

    //? if <= 1.20.1 {
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
    }
    //?}

    public void success(Identifier ret) {
        onSuccess.accept(ret);
        this.closeScreen();
    }

    private void closeScreen() {
        MinecraftClient.getInstance().setScreen(this.parent);
    }
}
