package org.dementhium.model.misc;

import org.dementhium.model.Mob;
import org.dementhium.model.World;
import org.dementhium.model.misc.DamageManager.DamageType;
import org.dementhium.net.ActionSender;
import org.dementhium.tickable.Tick;

/**
 * @author Steve <golden_32@live.com>
 */
public class PoisonManager {

    private static final int POISON_TIME = 30;
    private Mob mob;
    private int amount;
    private Mob poisoner;
    private int cycles = 0;
    private boolean canBe = true;

    public PoisonManager(Mob mob) {
        this.mob = mob;
    }

    public void poison(Mob attacker, int initialAmount) {
        if (!isPoisoned() && canBe) {
            this.poisoner = attacker;
            this.amount = initialAmount;
            startDamage();
            if (mob.isPlayer()) {
                ActionSender.sendConfig(mob.getPlayer(), 102, 1);
                mob.getPlayer().sendMessage("You have been poisoned!");
            }
        }
    }

    public void continuePoison(int initialAmount) {
        if (initialAmount > 0) {
            if (!isPoisoned()) {
                this.amount = initialAmount;
                startDamage();
                if (mob.isPlayer()) {
                    World.getWorld().submit(new Tick(2) {
                        public void execute() {
                            if (mob.getPlayer().isOnline() && isPoisoned()) {
                                ActionSender.sendConfig(mob.getPlayer(), 102, 1);
                                stop();
                            } else if (mob.getPlayer().isOnline() && !isPoisoned()) {
                                stop();
                            }
                        }
                    });
                }
            }
        }
    }

    private void startDamage() {
        World.getWorld().submit(new Tick(POISON_TIME) {

            @Override
            public void execute() {
                if (amount == 0 || amount < 10 || cycles > 7) {
                    removePoison();
                    this.stop();
                } else {
                    mob.getDamageManager().damage(poisoner, amount, 1000, DamageType.POSION);
                    amount -= 2;
                    cycles++;
                }

            }

        });

    }

    public void removePoison() {
        if (isPoisoned()) {
            if (mob.isPlayer()) {
                ActionSender.sendConfig(mob.getPlayer(), 102, 0);
                mob.getPlayer().sendMessage("The poison has worn off.");
            }
            amount = 0;
            cycles = 0;
        }
    }


    public Mob getPoisoner() {
        return poisoner;
    }

    public void setPoisoner(Mob poisoner) {
        this.poisoner = poisoner;
    }

    public boolean isPoisoned() {
        return amount > 10;
    }

    public int getCurrentPoisonAmount() {
        return amount;
    }

    public boolean canBePoisoned() {
        return canBe;
    }

    public void setCanBePoisoned(boolean b) {
        this.canBe = true;

    }

}
