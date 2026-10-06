"""Static adapter wiring contract, NOT gameplay simulation."""
from pathlib import Path
import unittest
ROOT = Path(__file__).resolve().parents[1]
class AdapterContract(unittest.TestCase):
    def test_forge_adapter_has_guarded_entity_only_hook_and_normal_attack(self):
        p = ROOT/'src/main/java/dev/holdattack/ClientAttacks.java'
        self.assertTrue(p.exists(), 'client Forge adapter is missing')
        s = p.read_text(encoding='utf-8')
        for required in ['EntityHitResult', 'getAttackStrengthScale(0.0f)',
                         'setSwingHand(false)', 'ForgeHooksClient.onClickInput',
                         'event.isCanceled()', 'input.isCanceled()',
                         'gameMode.attack', 'TickEvent.Phase.END',
                         'isDestroying()', 'isWindowActive()', 'isUsingItem()',
                         'isSpectator()', 'isHandsBusy()', 'isItemEnabled(', 'lastAttackTick', 'keyAttack.isDown()']:
            self.assertIn(required, s)
if __name__ == '__main__': unittest.main()
