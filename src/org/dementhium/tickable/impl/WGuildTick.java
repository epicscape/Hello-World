package org.dementhium.tickable.impl;

import org.dementhium.content.DialogueManager;
import org.dementhium.model.Location;
import org.dementhium.model.World;
import org.dementhium.model.map.GameObject;
import org.dementhium.model.map.ObjectManager;
import org.dementhium.model.map.Region;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.misc.DamageManager.DamageType;
import org.dementhium.model.misc.ProjectileManager;
import org.dementhium.model.player.Equipment;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.net.ActionSender;
import org.dementhium.tickable.Tick;
import org.dementhium.util.Misc;

/**
 * @author Steve <golden_32@live.com>
 */
public class WGuildTick extends Tick {

    public static final Location[] DUMMY_LOCS = new Location[]{
            Location.locate(2859, 3549, 0),
            Location.locate(2857, 3549, 0),
            Location.locate(2855, 3550, 0),
            Location.locate(2855, 3552, 0),
            Location.locate(2856, 3554, 0),
            Location.locate(2858, 3554, 0),
            Location.locate(2860, 3553, 0),
            Location.locate(2860, 3551, 0)};

    public static final int[] FACE_DIRECTIONS = new int[]{
            2,
            2,
            3,
            3,
            4,
            4,
            5,
            5};

    public static final int[] OLD_IDS = new int[]{
            15635,
            15636,
            15637,
            15631,
            15634,
            15631,
            15632,
            15633};

    public static final int[] NEW_IDS = new int[]{
            15628,
            15629,
            15630,
            15627,
            15624,
            15625,
            15626,
            15627};

    protected static final Animation[] SUCCESS_ANIMATIONS = new Animation[]{Animation.create(4169), Animation.create(4168), Animation.create(4171), Animation.create(4170)};

    //15624, 15630
    public WGuildTick(int cycles) {
        super(cycles);
    }

    boolean stageOne = true;
    private Location currentLocation;
    private int randomIndex;
    private GameObject currentObject;

    private boolean spawnBoth;

    private GameObject secondObject;

    private GameObject catapultObject;

    private int projectileType;

    private static Location catapultTarget = Location.locate(2842, 3545, 1);

    private Location fromLoc = Location.locate(2842, 3554, 1);

    private static long lastLaunch;

    @Override
    public void execute() {
        if (stageOne) {
            if (currentLocation != null)
                ObjectManager.replaceObject(currentLocation, OLD_IDS[randomIndex]);
            if (spawnBoth) {
                ObjectManager.replaceObject(secondObject.getLocation(), OLD_IDS[7]);
            }
            spawnBoth = false;
            int newIndex = Misc.random(DUMMY_LOCS.length - 1);
            randomIndex = newIndex == randomIndex ? (randomIndex == 2 ? 1 : 2) : newIndex;
            if (randomIndex == 3 || randomIndex == 7) {
                randomIndex = 3;
                spawnBoth = true;
            }
            currentLocation = DUMMY_LOCS[randomIndex];
            currentObject = new GameObject(NEW_IDS[randomIndex], currentLocation, 10, FACE_DIRECTIONS[randomIndex]);
            if (spawnBoth) {
                secondObject = new GameObject(NEW_IDS[7], DUMMY_LOCS[7], 10, FACE_DIRECTIONS[7]);
                secondObject = ObjectManager.addCustomObject(secondObject);
                secondObject.setHealthSet(true);
                Region.addClipping(secondObject.getLocation().getX(), secondObject.getLocation().getY(), secondObject.getLocation().getZ(), 256 | 0x20000 | 0x40000000);

            }
            currentObject = ObjectManager.addCustomObject(currentObject);
            currentObject.setHealthSet(true);
            Region.addClipping(currentLocation.getX(), currentLocation.getY(), currentLocation.getZ(), 256 | 0x20000 | 0x40000000);
            //15616 - 15620
            projectileType = Misc.random(3);
            ObjectManager.removeCustomObject(2840, 3552, 1, 10);
            catapultObject = ObjectManager.addCustomObject(15617 + projectileType, 2840, 3552, 1, 10, 4);
            ProjectileManager.sendProjectile(getProjectile(projectileType), fromLoc, catapultTarget, 85, 15, 25, 15, 16, 0, 2);
            lastLaunch = System.currentTimeMillis();
            stageOne = false;
        } else {
            World.getWorld().submit(new Tick(2) {
                public void execute() {
                    this.stop();
                    for (Player p : catapultTarget.getPlayers()) {
                        if (p.getEquipment().getSlot(Equipment.SLOT_SHIELD) == 8856 && p.getAttribute("disabledTabs", false)) {
                            if (getProjectileType(p.getAttribute("shieldStyle", -1)) == projectileType) {
                                p.animate(SUCCESS_ANIMATIONS[projectileType]);
                                p.getSettings().getTokens()[3] += 4;
                                p.getSkills().addExperience(Skills.DEFENCE, 10);
                                p.sendMessage("You successfully block the missle!");
                            } else {
                                p.animate(SUCCESS_ANIMATIONS[projectileType].getId() + 4);
                                p.getDamageManager().miscDamage(Misc.random(10, 40), DamageType.RED_DAMAGE);
                                p.sendMessage("You fail to block the missle!");
                            }
                            p.removeAttribute("shieldStyle");
                        } else {
                            p.teleport(2842, 3542, 1);
                            DialogueManager.sendDialogue(p, DialogueManager.TALKING_ALOT, 4287, -1, "Watch out!", "You need to be careful! Wear your Defensive shield next", "time you go up there! Talk to me if you need one!");
                        }
                    }
                }
            });
            World.getWorld().submit(new Tick(4) {
                public void execute() {
                    currentObject.setHealthSet(false); //disable hitting after lowering
                    if (spawnBoth) {
                        secondObject.setHealthSet(false);
                    }
                    for (Player p : Region.getLocalPlayers(currentLocation)) {
                        ActionSender.sendAnimateObject(p, currentObject, 4164);
                        if (spawnBoth) {
                            ActionSender.sendAnimateObject(p, secondObject, 4164);
                        }
                    }
                    this.stop();
                    stageOne = true;
                }
            });
        }

    }


    private int getProjectileType(int style) {
        switch (style) {
            case 0:
                return 0;
            case 3:
                return 1;
            case 1:
                return 2;
            case 2:
                return 3;
        }
        return -1;
    }

    private int getProjectile(int projectileType) {
        switch (projectileType) {
            case 0:
                return 679;
            case 1:
                return 682;
            case 2:
                return 680;
            case 3:
                return 681;
        }
        return 679;
    }

    public GameObject getCatapultObject() {
        return catapultObject;
    }

    public static Location getCatapultTarget() {
        return catapultTarget;
    }

    public static long getLastLaunch() {
        return lastLaunch;
    }


}
