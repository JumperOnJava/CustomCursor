package io.github.jumperonjava.customcursor.editor;


//? if <= 1.21.5 {
import com.mojang.blaze3d.systems.RenderSystem;
//?}
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.regex.Pattern;

public class TextureFolder {
    public final Path path;
    public final String namespace;
    private List<Identifier> textures = new ArrayList<>();

    /**
     * Initializes texture folder
     * DOES NOT SCAN THIS FOLDER AUTOMATICALLY but creates it to prevent some crashes
     * Use redefineTextures() to scan and update textures from folder
     */
    public TextureFolder(Path path,String namespace){
        this.path = path;
        this.namespace = namespace;
        if(Files.isRegularFile(path)){
            throw new RuntimeException("Path %s is not a directory".formatted(path));
        }
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * redefines textures in given folder
     */
    public void redefineTextures(){
        redefineTextures(()->{});
    }
    public void redefineTextures(Runnable onFinishedCallback){
        List<Path> l = new ArrayList<>();
        reregisterTextures(onFinishedCallback,getAllTexturesRecursive(path));
    }
    private List<Path> getAllTexturesRecursive(Path path){
        try{
            var l = new ArrayList<Path>();
            var objects = Files.list(path);
            for(var object : objects.toList()){
                if(Files.isDirectory(object)){
                    l.addAll(getAllTexturesRecursive(object));
                }
                if(Files.isRegularFile(object) && object.toString().toLowerCase().endsWith(".png")){
                    l.add(object);
                }
            }
            return l;
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private void reregisterTextures(Runnable onFinishedCallback, List<Path> toRegister) {
        // Move the RenderSystem.recordRenderCall out of the forEach loop

        //? if < 1.21.5
        RenderSystem.recordRenderCall(() -> {
            ONLY_RENDERTHREAD_register(onFinishedCallback, toRegister);
        //? if < 1.21.5
        });
    }

    private void ONLY_RENDERTHREAD_register(Runnable onFinishedCallback, List<Path> toRegister) {
        var tman = MinecraftClient.getInstance().getTextureManager();
        textures.forEach(tman::destroyTexture);
        textures.clear();
        Map<Identifier, NativeImage> textureMap = new HashMap<>();

        toRegister.forEach((p) -> {
            try {
                var stream = Files.newInputStream(p, StandardOpenOption.READ);
                var nativeImage = NativeImage.read(stream);
                stream.close();
                var name = getIdentifierFor(p);
                textureMap.put(name, nativeImage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        textureMap.forEach((identifier, nativeImage) -> {
            //? if < 1.21.5 {
            var texture = new NativeImageBackedTexture(nativeImage);
            //?} else {
            /*var texture = new NativeImageBackedTexture(identifier::toString,nativeImage);
            *///?}
            tman.registerTexture(identifier, texture);
            textures.add(identifier);
        });

        onFinishedCallback.run();
    }

    private Identifier getIdentifierFor(Path p) {

        //in some versions if identifier is incorrect it throws
        //in some it just sends null, so we throw an exception manually
        var s = p.toAbsolutePath().toString().replaceAll(Pattern.quote(path.toAbsolutePath().toString()),"").replace("\\","/").toLowerCase().substring(1);
        try{
            var name =  Identifier.of(namespace,s);
            if(name != null) return name;
            else throw new InvalidIdentifierException(namespace+":"+s);
        }
        catch (InvalidIdentifierException e){
            return Identifier.of(namespace,"wrongfilename" + s.hashCode());
        }
    }

    /*
     * return COPY of textures list, empty if no scan was done
     */
    public List<Identifier> getTextures(){
        return new ArrayList(this.textures);
    }
}
