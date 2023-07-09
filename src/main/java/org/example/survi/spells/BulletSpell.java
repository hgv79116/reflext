package org.example.survi.spells;

import org.example.survi.utils.ContinuousCoordinate;

public class BulletSpell extends Spell {
    private final ContinuousCoordinate sourceCoordinate;
    private final ContinuousCoordinate velocity;
    private final int damage;
    private final double speed;

    public BulletSpell(String name,
                       ContinuousCoordinate sourceCoordinate,
                       ContinuousCoordinate velocity,
                       int damage,
                       double speed) {
        super(name);
        this.sourceCoordinate = sourceCoordinate;
        this.velocity = velocity;
        this.damage = damage;
        this.speed = speed;
    }
}
