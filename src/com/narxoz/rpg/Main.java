package com.narxoz.rpg;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.floor.BossFloor;
import com.narxoz.rpg.floor.CombatFloor;
import com.narxoz.rpg.floor.RestFloor;
import com.narxoz.rpg.floor.TowerFloor;
import com.narxoz.rpg.floor.TrapFloor;
import com.narxoz.rpg.state.NormalState;
import com.narxoz.rpg.state.PoisonedState;
import com.narxoz.rpg.tower.TowerRunResult;
import com.narxoz.rpg.tower.TowerRunner;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Hero hero1 = new Hero("Arlan", 80, 18, 6, new NormalState());
        Hero hero2 = new Hero("Selene", 75, 16, 5, new PoisonedState(2));

        List<Hero> party = new ArrayList<>();
        party.add(hero1);
        party.add(hero2);

        List<TowerFloor> floors = new ArrayList<>();
        floors.add(new TrapFloor("Poison Hall"));
        floors.add(new CombatFloor("Goblin Chamber"));
        floors.add(new RestFloor("Sanctuary Room"));
        floors.add(new BossFloor("Throne of Shadows"));

        TowerRunner runner = new TowerRunner(floors);

        System.out.println("=== THE HAUNTED TOWER BEGINS ===");
        System.out.println("Starting party:");
        for (Hero hero : party) {
            hero.printStatus();
        }

        TowerRunResult result = runner.run(party);

        System.out.println();
        System.out.println("=== FINAL RESULT ===");
        System.out.println("Floors cleared: " + result.getFloorsCleared());
        System.out.println("Heroes surviving: " + result.getHeroesSurviving());
        System.out.println("Reached top: " + result.isReachedTop());

        System.out.println();
        System.out.println("Final party status:");
        for (Hero hero : party) {
            hero.printStatus();
        }
    }
}
