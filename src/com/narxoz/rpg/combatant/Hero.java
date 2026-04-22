package com.narxoz.rpg.combatant;

import com.narxoz.rpg.state.HeroState;

public class Hero {

    private final String name;
    private int hp;
    private final int maxHp;
    private final int attackPower;
    private final int defense;

    private HeroState state;

    public Hero(String name, int hp, int attackPower, int defense, HeroState initialState) {
        if (initialState == null) {
            throw new IllegalArgumentException("Hero must have an initial state.");
        }

        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.attackPower = attackPower;
        this.defense = defense;
        this.state = initialState;
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public int getDefense() {
        return defense;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public HeroState getState() {
        return state;
    }

    public void setState(HeroState state) {
        if (state == null) {
            throw new IllegalArgumentException("Hero state cannot be null.");
        }

        System.out.println(name + " is now in state: " + state.getName());
        this.state = state;
    }

    public int attack() {
        if (!isAlive()) {
            return 0;
        }
        return state.modifyOutgoingDamage(attackPower);
    }

    public void takeDamage(int amount) {
        int safeAmount = Math.max(0, amount);
        int modified = Math.max(0, state.modifyIncomingDamage(safeAmount));
        hp = Math.max(0, hp - modified);

        System.out.println(name + " took " + modified + " damage. HP: " + hp + "/" + maxHp);
    }

    public void heal(int amount) {
        int safeAmount = Math.max(0, amount);
        hp = Math.min(maxHp, hp + safeAmount);

        System.out.println(name + " healed " + safeAmount + " HP. HP: " + hp + "/" + maxHp);
    }

    public void onTurnStart() {
        state.onTurnStart(this);
    }

    public void onTurnEnd() {
        state.onTurnEnd(this);
    }

    public boolean canAct() {
        return state.canAct();
    }

    public void printStatus() {
        System.out.println(name + " | HP: " + hp + "/" + maxHp + " | State: " + state.getName());
    }
}