package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.state.PoisonedState;
import com.narxoz.rpg.state.StunnedState;

import java.util.List;

public class TrapFloor extends TowerFloor {

    private final String floorName;

    public TrapFloor(String floorName) {
        this.floorName = floorName;
    }

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("Setup: Hidden mechanisms click inside the walls...");
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("Challenge: The trap is triggered!");

        int totalDamageTaken = 0;

        for (int i = 0; i < party.size(); i++) {
            Hero hero = party.get(i);

            if (!hero.isAlive()) {
                continue;
            }

            int beforeHp = hero.getHp();
            hero.takeDamage(8);
            totalDamageTaken += Math.max(0, beforeHp - hero.getHp());

            if (!hero.isAlive()) {
                System.out.println(hero.getName() + " fell to the trap.");
                continue;
            }

            if (i % 2 == 0) {
                hero.setState(new PoisonedState(2));
                System.out.println(hero.getName() + " has been poisoned!");
            } else {
                hero.setState(new StunnedState(1));
                System.out.println(hero.getName() + " has been stunned!");
            }
        }

        boolean cleared = hasAliveHeroes(party);
        String summary = cleared
                ? "The party survived a dangerous trap floor."
                : "The trap wiped out the party.";

        return new FloorResult(cleared, totalDamageTaken, summary);
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
        if (!result.isCleared()) {
            System.out.println("Loot: No rewards. The trap defeated the party.");
            return;
        }

        System.out.println("Loot: The party finds a small healing potion cache. Surviving heroes heal 5 HP.");
        for (Hero hero : party) {
            if (hero.isAlive()) {
                hero.heal(5);
            }
        }
    }

    @Override
    protected void cleanup(List<Hero> party) {
        System.out.println("Cleanup: Poisonous smoke slowly disappears.");
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