# Hold Attack / 长按冷却攻击

Minecraft **1.20.1**, **Forge 47.4.6–47.x**, **Java 17**. Client-only / 仅客户端。

## 中文
将 `holdattack-forge-1.20.1-1.0.0.jar` 放入客户端实例的 `mods` 目录；服务器无需安装。按住“攻击/破坏”键（支持重绑定），准星指向实体且武器冷却完全恢复时进行原版攻击，松开停止。不搜索目标，不增加距离、伤害或攻速，不改变方块挖掘。冷却不足的实体点击也会被阻止。

菜单、失焦、暂停、使用物品、挖掘、死亡、旁观及睡眠等状态会阻止自动攻击。以 `getAttackStrengthScale(0.0f)` 作保守整 tick 判定，每客户端 tick 最多一次，遵守 Forge 点击事件取消及挥手设置。服务端仍决定命中和伤害；网络延迟下不保证服务端收到攻击时恰好满冷却。遵守服务器规则；禁止自动输入的服务器请勿使用。避免同时安装其他自动攻击模组；不承诺反作弊或战斗模组兼容。

验证：17 项纯 Java 策略断言、静态 adapter 接线检查、生产 `clean build` / `reobfJar`。原工程此前在 Forge 47.4.12 做过构造阶段加载探针；本发布准备未启动客户端，未重跑该探针。未验证世界内长按、松开、换武器、挖掘及联机行为。编译基线为 Forge 47.4.6；其他允许版本不代表逐项实测。

## English
Place the JAR in your **client** instance's `mods` directory. No server installation is required. Hold the configured Attack/Destroy key to attack the entity under the crosshair only after full weapon cooldown; release to stop. No target search, extra reach, damage or attack speed. Block mining is unchanged. Entity clicks before full cooldown are also suppressed.

Automatic attacks are guarded against menus, lost focus, pause, item use, mining, death, spectator mode and sleep. The conservative whole-tick cooldown check permits at most one attack per client tick and respects Forge click cancellation/swing preferences. The server remains authoritative; latency may affect server-side cooldown timing. Follow server rules and do not use where automated input is prohibited. Other automatic-attack mods, combat mods and anti-cheat compatibility are not guaranteed.

Verified: 17 policy assertions, a static adapter wiring check, and a production clean build/reobfuscation. A previous-source construction-stage loading probe used Forge 47.4.12; it was not rerun for this preparation. No client was launched here. In-world holding/releasing, weapon switching, mining and multiplayer gameplay remain untested. Forge 47.4.6 is the compilation baseline, not a claim that every allowed version was tested.

## Build / 构建
Install JDK 17 and set `JAVA_HOME`. The wrapper uses Gradle 8.8; initial builds require Internet access for Gradle/Forge dependencies.

Windows:
```bat
gradlew.bat clean build reobfJar
gradlew.bat policyTest
python tools/test_adapter_contract.py
```
POSIX:
```sh
sh ./gradlew clean build reobfJar
sh ./gradlew policyTest
python3 tools/test_adapter_contract.py
```
Output: `build/libs/holdattack-forge-1.20.1-1.0.0.jar`. Do not install the source archive. The adapter check requires Python 3 and tests source wiring, not gameplay.

## License / 许可
Original mod source and resources: **BSD-3-Clause**, copyright **666nima**; see `LICENSE`. Gradle wrapper components retain their original **Apache-2.0** license and copyright; see `THIRD_PARTY_NOTICES.md` and `LICENSES/Apache-2.0.txt`. Build dependencies and Minecraft are not relicensed or bundled as mod source.
