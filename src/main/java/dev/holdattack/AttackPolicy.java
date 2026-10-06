package dev.holdattack;

/** Pure eligibility policy; adapter supplies vanilla cooldown and environment. */
final class AttackPolicy {
    private AttackPolicy() {}
    static boolean allows(float strength, int blockers, long tick, long lastAttackTick) {
        return strength >= 1.0f && blockers == 0 && tick != lastAttackTick;
    }
}
