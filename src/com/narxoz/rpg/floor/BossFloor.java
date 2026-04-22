package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.combatant.Monster;
import com.narxoz.rpg.state.BerserkState;

import java.util.List;

public class BossFloor extends TowerFloor {

    private Monster boss;
    private final String floorName;

    public BossFloor(String floorName) {
        this.floorName = floorName;
    }

    @Override
    protected void announce() {
        System.out.println("\n=== BOSS FLOOR: " + getFloorName() + " ===");
        System.out.println("A terrifying presence shakes the tower...");
    }

    @Override
    protected void setup(List<Hero> party) {
        boss = new Monster("Haunted Knight", 90, 16);
        System.out.println("Setup: Boss appears -> " + boss.getName()
                + " (HP: " + boss.getHp() + ", ATK: " + boss.getAttackPower() + ")");
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("Challenge: Final battle begins!");

        int totalDamageTaken = 0;

        while (boss.isAlive() && hasAliveHeroes(party)) {

            for (Hero hero : party) {
                if (!hero.isAlive() || !boss.isAlive()) {
                    continue;
                }

                if (hero.getHp() <= hero.getMaxHp() / 3
                        && hero.getState().getName().equalsIgnoreCase("Normal")) {
                    hero.setState(new BerserkState());
                }

                hero.onTurnStart();

                if (!hero.isAlive()) {
                    continue;
                }

                if (!hero.canAct()) {
                    System.out.println(hero.getName() + " is unable to act!");
                    hero.onTurnEnd();
                    continue;
                }

                int damage = hero.attack();
                boss.takeDamage(damage);

                System.out.println(hero.getName() + " strikes " + boss.getName()
                        + " for " + damage + " damage. Boss HP: " + boss.getHp());

                hero.onTurnEnd();
            }

            if (!boss.isAlive()) {
                break;
            }

            for (Hero hero : party) {
                if (!hero.isAlive()) {
                    continue;
                }

                int beforeHp = hero.getHp();
                boss.attack(hero);
                int dealt = Math.max(0, beforeHp - hero.getHp());
                totalDamageTaken += dealt;

                System.out.println(boss.getName() + " hits " + hero.getName()
                        + ". " + hero.getName() + " HP: " + hero.getHp());

                if (!hero.isAlive()) {
                    System.out.println(hero.getName() + " has fallen in battle.");
                }
            }
        }

        boolean cleared = !boss.isAlive();
        String summary = cleared
                ? "The boss was defeated. The tower trembles in silence."
                : "The party was crushed by the boss.";

        return new FloorResult(cleared, totalDamageTaken, summary);
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        if (!result.isCleared()) {
            System.out.println("Loot: No boss reward.");
            return;
        }

        System.out.println("Loot: Surviving heroes restore 20 HP after the boss fight.");
        for (Hero hero : party) {
            if (hero.isAlive()) {
                hero.heal(20);
            }
        }
    }

    @Override
    protected void cleanup(List<Hero> party) {
        System.out.println("Cleanup: The dark aura of the boss floor fades away.");
    }

    @Override
    protected String getFloorName() {
        return floorName;
    }

    private boolean hasAliveHeroes(List<Hero> party) {
        for (Hero hero : party) {
            if (hero.isAlive()) {
                return true;
            }
        }
        return false;
    }
}