package com.narxoz.rpg.floor;

import com.narxoz.rpg.combatant.Hero;

import java.util.List;

public class RestFloor extends TowerFloor {

    private final String floorName;

    public RestFloor(String floorName) {
        this.floorName = floorName;
    }

    @Override
    protected void announce() {
        System.out.println("\n--- Entering " + getFloorName() + " ---");
        System.out.println("A peaceful aura fills the chamber.");
    }

    @Override
    protected void setup(List<Hero> party) {
        System.out.println("Setup: Warm light surrounds the heroes.");
    }

    @Override
    protected FloorResult resolveChallenge(List<Hero> party) {
        System.out.println("Challenge: There is no battle here. The party rests.");

        int healedCount = 0;

        for (Hero hero : party) {
            if (hero.isAlive()) {
                hero.heal(15);
                healedCount++;
            }
        }

        return new FloorResult(
                true,
                0,
                "The party rested safely. Heroes healed: " + healedCount
        );
    }

    @Override
    protected boolean shouldAwardLoot(FloorResult result) {
        return false;
    }

    @Override
    protected void awardLoot(List<Hero> party, FloorResult result) {
    }

    @Override
    protected void cleanup(List<Hero> party) {
        System.out.println("Cleanup: The blessing of the chamber fades.");
    }

    @Override
    protected String getFloorName() {
        return floorName;
    }
}