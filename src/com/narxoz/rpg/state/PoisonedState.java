package com.narxoz.rpg.state;

import com.narxoz.rpg.combatant.Hero;

public class PoisonedState implements HeroState {

    private int turnsRemaining;

    public PoisonedState(int turns) {
        this.turnsRemaining = turns;
    }

    @Override
    public String getName() {
        return "Poisoned";
    }

    @Override
    public int modifyOutgoingDamage(int basePower) {
        return (int)(basePower * 0.8);
    }

    @Override
    public int modifyIncomingDamage(int rawDamage) {
        return (int)(rawDamage * 1.2);
    }

    @Override
    public void onTurnStart(Hero hero) {
        int poisonDamage = 5;
        hero.takeDamage(poisonDamage);
        System.out.println(hero.getName() + " suffers poison damage!");
    }

    @Override
    public void onTurnEnd(Hero hero) {
        turnsRemaining--;

        if (turnsRemaining <= 0) {
            hero.setState(new NormalState());
            System.out.println(hero.getName() + " is no longer poisoned.");
        }
    }

    @Override
    public boolean canAct() {
        return true;
    }
}