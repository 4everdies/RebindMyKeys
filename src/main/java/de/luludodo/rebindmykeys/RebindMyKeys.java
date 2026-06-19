package de.luludodo.rebindmykeys;

import de.luludodo.rebindmykeys.keyBindings.CustomKeyBinding;
import de.luludodo.rebindmykeys.keyBindings.Type;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.lwjgl.input.Keyboard;

@Mod(modid = "rebindmykeys", name = "RebindMyKeys", version = "3.0", clientSideOnly = true)
public class RebindMyKeys {
    public static CustomKeyBinding escapeKey;
    public static CustomKeyBinding toggleHudKey;
    public static CustomKeyBinding debugKey;
    public static CustomKeyBinding reloadChunksKey;
    public static CustomKeyBinding showHitboxKey;
    public static CustomKeyBinding clearChatKey;
    public static CustomKeyBinding toggleChunkBordersKey;
    public static CustomKeyBinding toggleAdvancedTooltipsKey;
        @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        escapeKey = new CustomKeyBinding("rebindmykeys.key.escape", Keyboard.KEY_ESCAPE, Type.EVERYWHERE);
        ClientRegistry.registerKeyBinding(escapeKey);

        toggleHudKey = new CustomKeyBinding("rebindmykeys.key.toggle-hud", Keyboard.KEY_F1, Type.GAME);
        ClientRegistry.registerKeyBinding(toggleHudKey);

        debugKey = new CustomKeyBinding("rebindmykeys.key.debug", Keyboard.KEY_F3, Type.GAME);
        ClientRegistry.registerKeyBinding(debugKey);

        reloadChunksKey = new CustomKeyBinding("rebindmykeys.key.reload-chunks", Keyboard.KEY_A, Type.DEBUG_COMBO);
        ClientRegistry.registerKeyBinding(reloadChunksKey);

        showHitboxKey = new CustomKeyBinding("rebindmykeys.key.show-hitbox", Keyboard.KEY_B, Type.DEBUG_COMBO);
        ClientRegistry.registerKeyBinding(showHitboxKey);

        clearChatKey = new CustomKeyBinding("rebindmykeys.key.clear-chat", Keyboard.KEY_D, Type.DEBUG_COMBO);
        ClientRegistry.registerKeyBinding(clearChatKey);

        toggleChunkBordersKey = new CustomKeyBinding("rebindmykeys.key.toggle-chunk-borders", Keyboard.KEY_G, Type.DEBUG_COMBO);
        ClientRegistry.registerKeyBinding(toggleChunkBordersKey);

        toggleAdvancedTooltipsKey = new CustomKeyBinding("rebindmykeys.key.toggle-advanced-tooltips", Keyboard.KEY_H, Type.DEBUG_COMBO);
        ClientRegistry.registerKeyBinding(toggleAdvancedTooltipsKey);

    }
}
