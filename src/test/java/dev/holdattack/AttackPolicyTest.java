package dev.holdattack;

import java.lang.reflect.Method;

/** Dependency-free tests of the exact policy used by the Forge adapter. */
public final class AttackPolicyTest {
    private static int passed;
    private static Method allow;
    private static void check(boolean condition, String name) {
        if (!condition) throw new AssertionError(name);
        passed++;
    }
    private static boolean allows(float strength, int blockers, long tick, long last) throws Exception {
        return (boolean) allow.invoke(null, strength, blockers, tick, last);
    }
    public static void main(String[] args) throws Exception {
        try {
            allow = Class.forName("dev.holdattack.AttackPolicy")
                .getDeclaredMethod("allows", float.class, int.class, long.class, long.class);
            allow.setAccessible(true);
        } catch (ClassNotFoundException e) {
            throw new AssertionError("Missing full-cooldown attack policy", e);
        }
        check(allows(1.0f, 0, 20, 19), "held entity at full cooldown attacks");
        check(!allows(Math.nextDown(1.0f), 0, 20, 19), "not even almost-full cooldown may attack");
        for (int i = 0; i < 11; i++) {
            check(!allows(1.0f, 1 << i, 20, 19), "guard bit " + i + " blocks attacks");
        }
        check(!allows(1.0f, 0, 20, 20), "one attack per client tick even with instant weapon");
        check(!allows(Float.NaN, 0, 20, 19), "invalid cooldown does not attack");
        check(!allows(0.0f, 0, 20, 19), "reset cooldown does not attack");
        check(allows(1.0f, 0, 21, 20), "no fixed interval after next tick full recharge");
        System.out.println("PASS: " + passed + " policy assertions");
    }
}
