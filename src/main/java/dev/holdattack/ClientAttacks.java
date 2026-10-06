package dev.holdattack;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Uses the current vanilla crosshair result, never searches for targets. */
@Mod.EventBusSubscriber(modid = HoldAttack.MOD_ID, value = Dist.CLIENT)
public final class ClientAttacks {
    private static long clientTick;
    private static long lastAttackTick = Long.MIN_VALUE;
    private ClientAttacks() {}

    // Each bit is a separate reason to leave normal gameplay alone.
    private static int blockers(Minecraft mc) {
        int result = 0;
        if (mc.player == null || mc.level == null || mc.gameMode == null) return 1;
        if (!mc.options.keyAttack.isDown()) result |= 1 << 1;
        if (mc.screen != null || mc.getOverlay() != null) result |= 1 << 2;
        if (!mc.isWindowActive()) result |= 1 << 3;
        if (mc.isPaused()) result |= 1 << 4;
        if (!mc.player.isAlive()) result |= 1 << 5;
        if (mc.player.isSpectator()) result |= 1 << 6;
        if (mc.player.isUsingItem() || mc.player.isHandsBusy()
                || !mc.player.getMainHandItem().isItemEnabled(mc.level.enabledFeatures())) result |= 1 << 7;
        if (mc.gameMode.isDestroying()) result |= 1 << 8;
        if (!(mc.hitResult instanceof EntityHitResult hit)
                || !hit.getEntity().isAlive() || hit.getEntity().isRemoved()) result |= 1 << 9;
        if (mc.player.isSleeping()) result |= 1 << 10;
        return result;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public static void onInteraction(InputEvent.InteractionKeyMappingTriggered event) {
        Minecraft mc = Minecraft.getInstance();
        // Block mining uses this same hook. Do not touch block/miss/use/pick events.
        if (!event.isAttack() || !(mc.hitResult instanceof EntityHitResult) || event.isCanceled()) return;
        int blocked = blockers(mc);
        float strength = mc.player == null ? 0.0f : mc.player.getAttackStrengthScale(0.0f);
        if (!AttackPolicy.allows(strength, blocked, clientTick, lastAttackTick)) {
            event.setCanceled(true);
            event.setSwingHand(false);
            return;
        }
        // Reserve the tick for vanilla's click path or our synthetic click hook.
        // Normal MultiPlayerGameMode.attack resets the cooldown itself.
        lastAttackTick = clientTick;
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            clientTick++;
            return;
        }
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getInstance();
        if (blockers(mc) != 0 || !AttackPolicy.allows(mc.player.getAttackStrengthScale(0.0f),
                0, clientTick, lastAttackTick)) return;
        EntityHitResult target = (EntityHitResult) mc.hitResult;
        // Respect other mods' click cancellation and swing preference too.
        InputEvent.InteractionKeyMappingTriggered input = ForgeHooksClient.onClickInput(
                0, mc.options.keyAttack, InteractionHand.MAIN_HAND);
        if (input.isCanceled()) return;
        // Listeners may change screens, target, held key, or cooldown.
        if (blockers(mc) != 0 || mc.hitResult != target
                || mc.player.getAttackStrengthScale(0.0f) < 1.0f) return;
        lastAttackTick = clientTick;
        mc.gameMode.attack(mc.player, target.getEntity());
        if (input.shouldSwingHand()) mc.player.swing(InteractionHand.MAIN_HAND);
    }
}
