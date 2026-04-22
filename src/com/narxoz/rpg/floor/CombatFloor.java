package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.combatant.Monster;

import java.util.List;

public class CombatFloor extends TowerFloor {

    private Monster monster;
    private final String floorName;

    public CombatFloor(String floorName) {
        this.floorName = floorName;
    }

    @Override
    protected void setup(List<Hero> party) {
        monster = new Monster("Goblin Warrior", 50, 12);
        System.out.println("Setup: A monster appears -> " + monster.getName()
                + " (HP: " + monster.getHp() + ", ATK: " + monster.getAttackPower() + ")");
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("Challenge: Combat begins!");

        int totalDamageTaken = 0;

        while (monster.isAlive() && hasAliveHeroes(party)) {
            for (Hero hero : party) {
                if (!hero.isAlive() || !monster.isAlive()) {
                    continue;
                }

                hero.onTurnStart();

                if (!hero.isAlive()) {
                    hero.onTurnEnd();
                    continue;
                }

                if (!hero.canAct()) {
                    System.out.println(hero.getName() + " cannot act this turn.");
                    hero.onTurnEnd();
                    continue;
                }

                int damage = hero.attack();
                monster.takeDamage(damage);

                System.out.println(hero.getName() + " attacks " + monster.getName()
                        + " for " + damage + " damage. Monster HP: " + monster.getHp());

                hero.onTurnEnd();
            }

            if (!monster.isAlive()) {
                break;
            }

            Hero target = firstAliveHero(party);
            if (target != null) {
                int beforeHp = target.getHp();
                monster.attack(target);
                int dealt = Math.max(0, beforeHp - target.getHp());
                totalDamageTaken += dealt;

                System.out.println(monster.getName() + " attacks " + target.getName()
                        + ". " + target.getName() + " HP: " + target.getHp());
            }
        }

        boolean cleared = !monster.isAlive();
        String summary = cleared
                ? "The party defeated " + monster.getName() + "."
                : "The party was defeated by " + monster.getName() + ".";

        return new FloorResult(cleared, totalDamageTaken, summary);
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        if (!result.isCleared()) {
            System.out.println("Loot: No rewards. The floor was not cleared.");
            return;
        }

        System.out.println("Loot: Each surviving hero restores 10 HP.");
        for (Hero hero : party) {
            if (hero.isAlive()) {
                hero.heal(10);
            }
        }
    }

    @Override
    protected void cleanup(List<Hero> party) {
        System.out.println("Cleanup: The battlefield grows quiet.");
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

    private Hero firstAliveHero(List<Hero> party) {
        for (Hero hero : party) {
            if (hero.isAlive()) {
                return hero;
            }
        }
        return null;
    }
}