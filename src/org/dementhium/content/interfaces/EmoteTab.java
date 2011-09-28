package org.dementhium.content.interfaces;

import org.dementhium.model.World;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.mask.Graphic;
import org.dementhium.model.player.Player;
import org.dementhium.tickable.Tick;


/**
 * @author Lumby <lumbyjr@hotmail.com>
 */
public class EmoteTab {
	
	

    /**
     * Animation Id for emote
     */
    private static Animation[] animation = {Animation.create(0x357), Animation.create(0x358), Animation.create(0x35A), Animation.create(0x35B),
            Animation.create(0x359), Animation.create(0x35F), Animation.create(0x841), Animation.create(0x35E),
            Animation.create(0x360), Animation.create(0x83D), Animation.create(0x35D), Animation.create(0x83F),
            Animation.create(0x362), Animation.create(0x83A), Animation.create(0x83B), Animation.create(0x83C),
            Animation.create(0x35C), Animation.create(0x55E), Animation.create(0x839), Animation.create(0x83E),
            Animation.create(0x361), Animation.create(0x840), Animation.create(0x84F), Animation.create(0x850),
            Animation.create(0x46B), Animation.create(0x46A), Animation.create(0x469), Animation.create(0x468),
            Animation.create(0x10B3), Animation.create(0x6D1), Animation.create(0x10B8), Animation.create(0x10B4),
            Animation.create(0xDD8), Animation.create(0xDD7), Animation.create(0x1C68), Animation.create(0xB14),
            Animation.create(0x17DF), null, Animation.create(0x1D6B), Animation.create(0x96E), Animation.create(0x2242),
            Animation.create(0x2706), Animation.create(0x2922), Animation.create(0x2B24), Animation.create(0x357),
            Animation.create(0x2D16), Animation.create(0x3172), null, Animation.create(0x3A15), Animation.create(0x3ABA), null};
    /**
     * Graphic Id's for emote
     */
    private static Graphic[] graphic = {Graphic.create(1702), Graphic.create(1244), Graphic.create(1537), Graphic.create(1553), Graphic.create(1734),
            Graphic.create(1864), Graphic.create(1973), Graphic.create(2037), Graphic.create(2837), Graphic.create(2930), Graphic.create(780)};

    /**
     * Holds the skillcape emote information as (Item id, emote, gfx)
     */
    public static int[][] skillcape = {
            {9747, 4959, 823}, {10639, 4959, 823}, {9748, 4959, 823},//Attack
            {9753, 4961, 824}, {10641, 4961, 824}, {9754, 4961, 824},//Defense
            {9750, 4981, 828}, {10640, 4981, 828}, {9751, 4981, 828},//Strength
            {9768, 14242, 2745}, {10647, 14242, 2745}, {9769, 14242, 2745},//Constitution
            {9756, 4973, 832}, {10642, 4973, 832}, {9757, 4973, 832},//Ranged
            {9762, 4939, 813}, {10644, 4939, 813}, {9763, 4939, 813},//Magic
            {9759, 4979, 829}, {10643, 4979, 829}, {9760, 4979, 829},//Prayer
            {9801, 4955, 821}, {10658, 4955, 821}, {9802, 4955, 821},//Cooking
            {9807, 4957, 822}, {10660, 4957, 822}, {9808, 4957, 822},//Woodcutting
            {9783, 4937, 812}, {10652, 4937, 812}, {9784, 4937, 812},//Fletching
            {9798, 4951, 819}, {10657, 4951, 819}, {9799, 4951, 819},//Fishing
            {9804, 4975, 831}, {10659, 4975, 831}, {9805, 4975, 831},//Firemaking
            {9780, 4949, 818}, {10651, 4949, 818}, {9781, 4949, 818},//Crafting
            {9795, 4943, 815}, {10656, 4943, 815}, {9796, 4943, 815},//Smithing
            {9792, 4941, 814}, {10655, 4941, 814}, {9793, 4941, 814},//Mining
            {9774, 4969, 835}, {10649, 4969, 835}, {9775, 4969, 835},//Herblore
            {9771, 4977, 830}, {10648, 4977, 830}, {9772, 4977, 830},//Agility
            {9777, 4965, 826}, {10650, 4965, 826}, {9778, 4965, 826},//Thieving
            {9786, 4967, 1656}, {10653, 4967, 1656}, {9787, 4967, 1656},//Slayer
            {9810, 4963, 825}, {10661, 4963, 825}, {9811, 4963, 825},//Farming
            {9765, 4947, 817}, {10645, 4947, 817}, {9766, 4947, 817},//Runecrafting
            {9789, 4953, 820}, {10654, 4953, 820}, {9790, 4953, 820},//Construction
            {12524, 8525, 1515}, {12169, 8525, 1515}, {12170, 8525, 1515},//Summoning
            {9948, 5158, 907}, {10646, 5158, 907}, {9949, 5158, 907},//Hunter
            {9813, 4945, 816}, {10662, 4945, 816}};

    private static void doskillcapeEmote(Player p) {
        for (int i = 0; i < skillcape.length; i++) {
            if (p.getEquipment().getSlot(1) == skillcape[i][0]) {
                p.animate(Animation.create(skillcape[i][1]));
                p.graphics(Graphic.create(skillcape[i][2]));
            }
        }
    }

    public static void handleButton(final Player p, final int buttonId, int buttonId2, int buttonId3) {
        if (buttonId < 0 || buttonId > 52) {
            return;
        }
        if (buttonId == 39) {
            doskillcapeEmote(p);
            if (p.getEquipment().getSlot(1) == 15706 || p.getEquipment().getSlot(1) == 18508 || p.getEquipment().getSlot(1) == 18509 || p.getEquipment().getSlot(1) == 19709 || p.getEquipment().getSlot(1) == 19710) {
                p.animate(Animation.create(13190));
                p.graphics(Graphic.create(2442));
                p.setAttribute("cantMove", Boolean.TRUE);
                final int rand = (int) (Math.random() * (2 + 1));
                World.getWorld().submit(new Tick(2) {

                    public void execute() {
                        p.getAppearance().setNpcType((short) (rand == 0 ? 11227 : (rand == 1 ? 11228 : 11229)));
                        p.getMask().setApperanceUpdate(true);
                        p.animate(Animation.create(rand == 0 ? 13192 : (rand == 1 ? 13193 : 13194)));
                        this.stop();
                    }
                });
                World.getWorld().submit(new Tick(7) {

                    public void execute() {
                        p.getAppearance().setNpcType((short) -1);
                        p.removeAttribute("cantMove");
                        p.getMask().setApperanceUpdate(true);
                        this.stop();
                    }
                });

            }
        } else if (buttonId == 46) {
            p.animate(Animation.create(10994));
            p.graphics(Graphic.create(86));
            p.setAttribute("cantMove", Boolean.TRUE);
            World.getWorld().submit(new Tick(1) {

                public void execute() {
                    p.animate(Animation.create(10996));
                    p.getAppearance().setNpcType((short) 8499);
                    p.getMask().setApperanceUpdate(true);
                    this.stop();
                }
            });
            World.getWorld().submit(new Tick(8) {

                public void execute() {
                    p.animate(Animation.create(10995));
                    p.graphics(Graphic.create(86));
                    p.removeAttribute("cantMove");
                    p.getAppearance().setNpcType((short) -1);
                    p.getAppearance().resetAppearence();
                    p.getMask().setApperanceUpdate(true);
                    this.stop();
                }
            });
            //p.sendMessage("Turkey Emote coming soon!");
        } else if(buttonId == 52){
        	p.animate(Animation.create(15104));
            p.graphics(Graphic.create(1287));
            p.setAttribute("cantMove", Boolean.TRUE);
            final int rand = (int) (Math.random() * (2 + 1));
            World.getWorld().submit(new Tick(2) {

                public void execute() {
                    p.animate(Animation.create(15106));
                    p.getAppearance().setNpcType((short) (rand == 0 ? 13255 : (rand == 1 ? 13256 : 13257)));
                    p.getMask().setApperanceUpdate(true);
                    this.stop();
                }
            });
            World.getWorld().submit(new Tick(4) {

                public void execute() {
                    p.animate(Animation.create(15108));
                    this.stop();
                }
            });
            World.getWorld().submit(new Tick(7) {

                public void execute() {
                	 p.animate(Animation.create(15105));
                     p.graphics(Graphic.create(1287));
                     p.removeAttribute("cantMove");
                     p.getAppearance().setNpcType((short) -1);
                     p.getAppearance().resetAppearence();
                     p.getMask().setApperanceUpdate(true);
                    this.stop();
                }
            });
        } else {
            /*if(!doingEmote){
                   World.getWorld().submit(new Tick(tickAmount[buttonId-2]){
                       public void execute(){
                           doingEmote = false;
                           stop();
                       }
                   });*/
            p.animate(animation[buttonId - 2]);
            if (buttonId == 19)
                p.graphics(graphic[0]);
            else if (buttonId == 36)
                p.graphics(graphic[1]);
            else if (buttonId == 41)
                p.graphics(graphic[2]);
            else if (buttonId == 42)
                p.graphics(graphic[3]);
            else if (buttonId == 43)
                p.graphics(graphic[4]);
            else if (buttonId == 44)
                p.graphics(graphic[5]);
            else if (buttonId == 45)
                p.graphics(graphic[6]);
            else if (buttonId == 47)
                p.graphics(graphic[7]);
            else if (buttonId == 48)
                p.graphics(graphic[10]);
            else if (buttonId == 50)
                p.graphics(graphic[8]);
            else if (buttonId == 51)
                p.graphics(graphic[9]);
            /*	}else{
                   p.sendMessage("You are already doing an emote!");
               }*/
        }
    }


}
