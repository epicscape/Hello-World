package org.dementhium.event.impl.interfaces;

import org.dementhium.event.EventListener;
import org.dementhium.event.EventManager;
import org.dementhium.model.player.Player;
import org.dementhium.util.InterfaceSettings;

/**
 * The character design screen action button listener.
 * @author Lumby <lumbyjr@hotmail.com>
 * @author Emperor
 */
public class CharacterDesignListener extends EventListener {

	@Override
	public void register(EventManager manager) {
		manager.registerInterfaceListener(1028, this);
	}

	@Override
	public boolean interfaceOption(final Player player, int interfaceId, int buttonId, int buttonId2, int itemId, int opcode) {
		System.out.println("[Character Deisgn] ButtonId: "+buttonId+" buttonId2: "+buttonId2+" itemId: "+itemId);
		switch(buttonId){
		case 95:
			player.setAttribute("charCustom", 1);
			break;
		case 96:
			player.setAttribute("charCustom", 2);
			break;
		case 97:
			player.setAttribute("charCustom", 3);
			break;
		case 98:
			player.setAttribute("charCustom", 4);
			break;
		case 99:
			player.setAttribute("charCustom", 5);
			break;
		case 100:
			player.setAttribute("charCustom", 6);
			break;
		case 107:
			if(player.getAttribute("charCustom", 2) == 2){
				if(buttonId2 == 0){
					player.getAppearance().getLook()[0] = 5;
				}else if(buttonId2 == 1){
					player.getAppearance().getLook()[0] = 6;
				}else if(buttonId2 == 2){
					player.getAppearance().getLook()[0] = 93;
				}else if(buttonId2 == 3){
					player.getAppearance().getLook()[0] = 96;
				}else if(buttonId2 == 4){
					player.getAppearance().getLook()[0] = 92;
				}else if(buttonId2 == 5){
					player.getAppearance().getLook()[0] = 268;
				}else if(buttonId2 == 6){
					player.getAppearance().getLook()[0] = 265;
				}else if(buttonId2 == 7){
					player.getAppearance().getLook()[0] = 264;
				}else if(buttonId2 == 8){
					player.getAppearance().getLook()[0] = 267;
				}else if(buttonId2 == 9){
					player.getAppearance().getLook()[0] = 315;
				}else if(buttonId2 == 10){
					player.getAppearance().getLook()[0] = 94;
				}else if(buttonId2 == 11){
					player.getAppearance().getLook()[0] = 263;
				}else if(buttonId2 == 12){
					player.getAppearance().getLook()[0] = 312;
				}else if(buttonId2 == 13){
					player.getAppearance().getLook()[0] = 313;
				}else if(buttonId2 == 14){
					player.getAppearance().getLook()[0] = 311;
				}else if(buttonId2 == 15){
					player.getAppearance().getLook()[0] = 314;
				}else if(buttonId2 == 16){
					player.getAppearance().getLook()[0] = 261;
				}else if(buttonId2 == 17){
					player.getAppearance().getLook()[0] = 310;
				}else if(buttonId2 == 18){
					player.getAppearance().getLook()[0] = 1;
				}else if(buttonId2 == 19){
					player.getAppearance().getLook()[0] = 0;
				}else if(buttonId2 == 20){
					player.getAppearance().getLook()[0] = 97;
				}else if(buttonId2 == 21){
					player.getAppearance().getLook()[0] = 95;
				}else if(buttonId2 == 22){
					player.getAppearance().getLook()[0] = 262;
				}else if(buttonId2 == 23){
					player.getAppearance().getLook()[0] = 316;
				}else if(buttonId2 == 24){
					player.getAppearance().getLook()[0] = 309;
				}else if(buttonId2 == 25){
					player.getAppearance().getLook()[0] = 3;
				}else if(buttonId2 == 26){
					player.getAppearance().getLook()[0] = 91;
				}else if(buttonId2 == 27){
					player.getAppearance().getLook()[0] = 4;
				}
			}else if(player.getAttribute("charCustom", 3) == 3){
				if(buttonId2 == 0){
					player.getAppearance().getLook()[2] = 457;
					player.getAppearance().getLook()[3] = 588;
					player.getAppearance().getLook()[4] = 364;
				}else if(buttonId2 == 1){
					player.getAppearance().getLook()[2] = 445;
					player.getAppearance().getLook()[4] = 366;
				}else if(buttonId2 == 2){
					player.getAppearance().getLook()[2] = 459;
					player.getAppearance().getLook()[3] = 591;
					player.getAppearance().getLook()[4] = 367;
				}else if(buttonId2 == 3){
					player.getAppearance().getLook()[2] = 460;
					player.getAppearance().getLook()[3] = 592;
					player.getAppearance().getLook()[4] = 368;
				}else if(buttonId2 == 4){
					player.getAppearance().getLook()[2] = 461;
					player.getAppearance().getLook()[3] = 593;
					player.getAppearance().getLook()[4] = 369;
				}else if(buttonId2 == 5){
					player.getAppearance().getLook()[2] = 462;
					player.getAppearance().getLook()[3] = 594;
					player.getAppearance().getLook()[4] = 370;
				}else if(buttonId2 == 6){
					player.getAppearance().getLook()[2] = 452;
					player.getAppearance().getLook()[4] = 371;
				}else if(buttonId2 == 7){
					player.getAppearance().getLook()[2] = 463;
					player.getAppearance().getLook()[3] = 596;
					player.getAppearance().getLook()[4] = 372;
				}else if(buttonId2 == 8){
					player.getAppearance().getLook()[2] = 464;
					player.getAppearance().getLook()[3] = 597;
					player.getAppearance().getLook()[4] = 373;
				}else if(buttonId2 == 9){
					player.getAppearance().getLook()[2] = 446;
					player.getAppearance().getLook()[4] = 374;
				}else if(buttonId2 == 10){
					player.getAppearance().getLook()[2] = 465;
					player.getAppearance().getLook()[3] = 599;
					player.getAppearance().getLook()[4] = 375;
				}else if(buttonId2 == 11){
					player.getAppearance().getLook()[2] = 466;
					player.getAppearance().getLook()[3] = 600;
					player.getAppearance().getLook()[4] = 376;
				}else if(buttonId2 == 12){
					player.getAppearance().getLook()[2] = 467;
					player.getAppearance().getLook()[3] = 601;
					player.getAppearance().getLook()[4] = 377;
				}else if(buttonId2 == 13){
					player.getAppearance().getLook()[2] = 451;
					player.getAppearance().getLook()[4] = 378;
				}else if(buttonId2 == 14){
					player.getAppearance().getLook()[2] = 468;
					player.getAppearance().getLook()[3] = 603;
					player.getAppearance().getLook()[4] = 379;
				}else if(buttonId2 == 15){
					player.getAppearance().getLook()[2] = 453;
					player.getAppearance().getLook()[4] = 380;
				}else if(buttonId2 == 16){
					player.getAppearance().getLook()[2] = 454;
					player.getAppearance().getLook()[4] = 381;
				}else if(buttonId2 == 17){
					player.getAppearance().getLook()[2] = 455;
					player.getAppearance().getLook()[4] = 382;
				}else if(buttonId2 == 18){
					player.getAppearance().getLook()[2] = 469;
					player.getAppearance().getLook()[3] = 607;
					player.getAppearance().getLook()[4] = 383;
				}else if(buttonId2 == 19){
					player.getAppearance().getLook()[2] = 470;
					player.getAppearance().getLook()[3] = 608;
					player.getAppearance().getLook()[4] = 384;
				}else if(buttonId2 == 20){
					player.getAppearance().getLook()[2] = 450;
					player.getAppearance().getLook()[4] = 385;
				}else if(buttonId2 == 21){
					player.getAppearance().getLook()[2] = 458;
					player.getAppearance().getLook()[3] = 589;
					player.getAppearance().getLook()[4] = 365;
				}else if(buttonId2 == 22){
					player.getAppearance().getLook()[2] = 447;
					player.getAppearance().getLook()[4] = 386;
				}else if(buttonId2 == 23){
					player.getAppearance().getLook()[2] = 448;
					player.getAppearance().getLook()[4] = 387;
				}else if(buttonId2 == 24){
					player.getAppearance().getLook()[2] = 449;
					player.getAppearance().getLook()[4] = 388;
				}else if(buttonId2 == 25){
					player.getAppearance().getLook()[2] = 471;
					player.getAppearance().getLook()[3] = 613;
					player.getAppearance().getLook()[4] = 389;
				}else if(buttonId2 == 26){
					player.getAppearance().getLook()[2] = 443;
					player.getAppearance().getLook()[4] = 390;
				}else if(buttonId2 == 27){
					player.getAppearance().getLook()[2] = 472;
					player.getAppearance().getLook()[3] = 615;
					player.getAppearance().getLook()[4] = 391;
				}else if(buttonId2 == 28){
					player.getAppearance().getLook()[2] = 473;
					player.getAppearance().getLook()[3] = 616;
					player.getAppearance().getLook()[4] = 392;
				}else if(buttonId2 == 29){
					player.getAppearance().getLook()[2] = 444;
					player.getAppearance().getLook()[4] = 393;
				}else if(buttonId2 == 30){
					player.getAppearance().getLook()[2] = 474;
					player.getAppearance().getLook()[3] = 618;
					player.getAppearance().getLook()[4] = 394;
				}else if(buttonId2 == 31){
					player.getAppearance().getLook()[2] = 456;
					player.getAppearance().getLook()[4] = 9;
				}
			}if(player.getAttribute("charCustom", 4) == 4){
				if(buttonId2 == 0){
					player.getAppearance().getLook()[5] = 620;
				}else if(buttonId2 == 1){
					player.getAppearance().getLook()[5] = 622;
				}else if(buttonId2 == 2){
					player.getAppearance().getLook()[5] = 623;
				}else if(buttonId2 == 3){
					player.getAppearance().getLook()[5] = 624;
				}else if(buttonId2 == 4){
					player.getAppearance().getLook()[5] = 625;
				}else if(buttonId2 == 5){
					player.getAppearance().getLook()[5] = 626;
				}else if(buttonId2 == 6){
					player.getAppearance().getLook()[5] = 627;
				}else if(buttonId2 == 7){
					player.getAppearance().getLook()[5] = 628;
				}else if(buttonId2 == 8){
					player.getAppearance().getLook()[5] = 629;
				}else if(buttonId2 == 9){
					player.getAppearance().getLook()[5] = 630;
				}else if(buttonId2 == 10){
					player.getAppearance().getLook()[5] = 631;
				}else if(buttonId2 == 11){
					player.getAppearance().getLook()[5] = 632;
				}else if(buttonId2 == 12){
					player.getAppearance().getLook()[5] = 633;
				}else if(buttonId2 == 13){
					player.getAppearance().getLook()[5] = 634;
				}else if(buttonId2 == 14){
					player.getAppearance().getLook()[5] = 635;
				}else if(buttonId2 == 15){
					player.getAppearance().getLook()[5] = 636;
				}else if(buttonId2 == 16){
					player.getAppearance().getLook()[5] = 637;
				}else if(buttonId2 == 17){
					player.getAppearance().getLook()[5] = 638;
				}else if(buttonId2 == 18){
					player.getAppearance().getLook()[5] = 639;
				}else if(buttonId2 == 19){
					player.getAppearance().getLook()[5] = 640;
				}else if(buttonId2 == 20){
					player.getAppearance().getLook()[5] = 641;
				}else if(buttonId2 == 21){
					player.getAppearance().getLook()[5] = 621;
				}else if(buttonId2 == 22){
					player.getAppearance().getLook()[5] = 642;
				}else if(buttonId2 == 23){
					player.getAppearance().getLook()[5] = 643;
				}else if(buttonId2 == 24){
					player.getAppearance().getLook()[5] = 644;
				}else if(buttonId2 == 25){
					player.getAppearance().getLook()[5] = 645;
				}else if(buttonId2 == 26){
					player.getAppearance().getLook()[5] = 646;
				}else if(buttonId2 == 27){
					player.getAppearance().getLook()[5] = 647;
				}else if(buttonId2 == 28){
					player.getAppearance().getLook()[5] = 648;
				}else if(buttonId2 == 29){
					player.getAppearance().getLook()[5] = 649;
				}else if(buttonId2 == 30){
					player.getAppearance().getLook()[5] = 650;
				}else if(buttonId2 == 31){
					player.getAppearance().getLook()[5] = 651;
				}
			}else{

			}
			break;
		case 45://Skin color Main screen
			if(buttonId2 == 0){
				player.getAppearance().getColour()[4] = 9;
			}else if(buttonId2 == 1){
				player.getAppearance().getColour()[4] = 8;
			}else if(buttonId2 == 2){
				player.getAppearance().getColour()[4] = 7;
			}else if(buttonId2 == 3){
				player.getAppearance().getColour()[4] = 0;
			}else if(buttonId2 == 4){
				player.getAppearance().getColour()[4] = 1;
			}else if(buttonId2 == 5){
				player.getAppearance().getColour()[4] = 2;
			}else if(buttonId2 == 6){
				player.getAppearance().getColour()[4] = 3;
			}else if(buttonId2 == 7){
				player.getAppearance().getColour()[4] = 4;
			}else if(buttonId2 == 8){
				player.getAppearance().getColour()[4] = 5;
			}else if(buttonId2 == 9){
				player.getAppearance().getColour()[4] = 6;
			}else if(buttonId2 == 10){
				player.getAppearance().getColour()[4] = 10;
			}else if(buttonId2 == 11){
				player.getAppearance().getColour()[4] = 11;
			}
			break;
		case 111://Skin Color modify further screen
			if(player.getAttribute("charCustom", 2) == 2){
				if(buttonId2 == 0){
					player.getAppearance().getColour()[0] = 20;
				}else if(buttonId2 == 1){
					player.getAppearance().getColour()[0] = 19;
				}else if(buttonId2 == 2){
					player.getAppearance().getColour()[0] = 10;
				}else if(buttonId2 == 3){
					player.getAppearance().getColour()[0] = 18;
				}else if(buttonId2 == 4){
					player.getAppearance().getColour()[0] = 4;
				}else if(buttonId2 == 5){
					player.getAppearance().getColour()[0] = 5;
				}else if(buttonId2 == 6){
					player.getAppearance().getColour()[0] = 15;
				}else if(buttonId2 == 7){
					player.getAppearance().getColour()[0] = 7;
				}else if(buttonId2 == 8){
					player.getAppearance().getColour()[0] = 0;
				}else if(buttonId2 == 9){
					player.getAppearance().getColour()[0] = 6;
				}else if(buttonId2 == 10){
					player.getAppearance().getColour()[0] = 21;
				}else if(buttonId2 == 11){
					player.getAppearance().getColour()[0] = 9;
				}else if(buttonId2 == 12){
					player.getAppearance().getColour()[0] = 22;
				}else if(buttonId2 == 13){
					player.getAppearance().getColour()[0] = 17;
				}else if(buttonId2 == 14){
					player.getAppearance().getColour()[0] = 8;
				}else if(buttonId2 == 15){
					player.getAppearance().getColour()[0] = 16;
				}else if(buttonId2 == 16){
					player.getAppearance().getColour()[0] = 24;
				}else if(buttonId2 == 17){
					player.getAppearance().getColour()[0] = 11;
				}else if(buttonId2 == 18){
					player.getAppearance().getColour()[0] = 23;
				}else if(buttonId2 == 19){
					player.getAppearance().getColour()[0] = 3;
				}else if(buttonId2 == 20){
					player.getAppearance().getColour()[0] = 2;
				}else if(buttonId2 == 21){
					player.getAppearance().getColour()[0] = 1;
				}else if(buttonId2 == 22){
					player.getAppearance().getColour()[0] = 14;
				}else if(buttonId2 == 23){
					player.getAppearance().getColour()[0] = 13;
				}else if(buttonId2 == 24){
					player.getAppearance().getColour()[0] = 12;
				}
			}else if(player.getAttribute("charCustom", 3) == 3){
				if(buttonId2 == 0){
					player.getAppearance().getColour()[1] = 32;
				}else if(buttonId2 == 1){
					player.getAppearance().getColour()[1] = 101;
				}else if(buttonId2 == 2){
					player.getAppearance().getColour()[1] = 48;
				}else if(buttonId2 == 3){
					player.getAppearance().getColour()[1] = 56;
				}else if(buttonId2 == 4){
					player.getAppearance().getColour()[1] = 165;
				}else if(buttonId2 == 5){
					player.getAppearance().getColour()[1] = 103;
				}else if(buttonId2 == 6){
					player.getAppearance().getColour()[1] = 167;
				}else if(buttonId2 == 7){
					player.getAppearance().getColour()[1] = 106;
				}else if(buttonId2 == 8){
					player.getAppearance().getColour()[1] = 54;
				}else if(buttonId2 == 9){
					player.getAppearance().getColour()[1] = 198;
				}else if(buttonId2 == 10){
					player.getAppearance().getColour()[1] = 199;
				}else if(buttonId2 == 11){
					player.getAppearance().getColour()[1] = 200;
				}else if(buttonId2 == 12){
					player.getAppearance().getColour()[1] = 225;
				}else if(buttonId2 == 13){
					player.getAppearance().getColour()[1] = 35;
				}else if(buttonId2 == 14){
					player.getAppearance().getColour()[1] = 39;
				}else if(buttonId2 == 15){
					player.getAppearance().getColour()[1] = 53;
				}else if(buttonId2 == 16){
					player.getAppearance().getColour()[1] = 42;
				}else if(buttonId2 == 17){
					player.getAppearance().getColour()[1] = 46;
				}else if(buttonId2 == 18){
					player.getAppearance().getColour()[1] = 29;
				}else if(buttonId2 == 19){
					player.getAppearance().getColour()[1] = 91;
				}else if(buttonId2 == 20){
					player.getAppearance().getColour()[1] = 57;
				}else if(buttonId2 == 21){
					player.getAppearance().getColour()[1] = 90;
				}else if(buttonId2 == 22){
					player.getAppearance().getColour()[1] = 34;
				}else if(buttonId2 == 23){
					player.getAppearance().getColour()[1] = 102;
				}else if(buttonId2 == 24){
					player.getAppearance().getColour()[1] = 104;
				}else if(buttonId2 == 25){
					player.getAppearance().getColour()[1] = 105;
				}else if(buttonId2 == 26){
					player.getAppearance().getColour()[1] = 107;
				}else if(buttonId2 == 27){
					player.getAppearance().getColour()[1] = 173;
				}else if(buttonId2 == 28){
					player.getAppearance().getColour()[1] = 137;
				}else if(buttonId2 == 29){
					player.getAppearance().getColour()[1] = 201;
				}else if(buttonId2 == 30){
					player.getAppearance().getColour()[1] = 204;
				}else if(buttonId2 == 31){
					player.getAppearance().getColour()[1] = 211;
				}else if(buttonId2 == 32){
					player.getAppearance().getColour()[1] = 197;
				}else if(buttonId2 == 33){
					player.getAppearance().getColour()[1] = 108;
				}else if(buttonId2 == 34){
					player.getAppearance().getColour()[1] = 217;
				}else if(buttonId2 == 35){
					player.getAppearance().getColour()[1] = 220;
				}else if(buttonId2 == 36){
					player.getAppearance().getColour()[1] = 221;
				}else if(buttonId2 == 37){
					player.getAppearance().getColour()[1] = 226;
				}else if(buttonId2 == 38){
					player.getAppearance().getColour()[1] = 227;
				}else if(buttonId2 == 39){
					player.getAppearance().getColour()[1] = 215;
				}else if(buttonId2 == 40){
					player.getAppearance().getColour()[1] = 222;
				}else if(buttonId2 == 41){
					player.getAppearance().getColour()[1] = 166;
				}else if(buttonId2 == 42){
					player.getAppearance().getColour()[1] = 212;
				}else if(buttonId2 == 43){
					player.getAppearance().getColour()[1] = 174;
				}else if(buttonId2 == 44){
					player.getAppearance().getColour()[1] = 175;
				}else if(buttonId2 == 45){
					player.getAppearance().getColour()[1] = 169;
				}else if(buttonId2 == 46){
					player.getAppearance().getColour()[1] = 144;
				}else if(buttonId2 == 47){
					player.getAppearance().getColour()[1] = 135;
				}else if(buttonId2 == 48){
					player.getAppearance().getColour()[1] = 136;
				}else if(buttonId2 == 49){
					player.getAppearance().getColour()[1] = 133;
				}else if(buttonId2 == 50){
					player.getAppearance().getColour()[1] = 123;
				}else if(buttonId2 == 51){
					player.getAppearance().getColour()[1] = 119;
				}else if(buttonId2 == 52){
					player.getAppearance().getColour()[1] = 192;
				}else if(buttonId2 == 53){
					player.getAppearance().getColour()[1] = 194;
				}else if(buttonId2 == 54){
					player.getAppearance().getColour()[1] = 117;
				}else if(buttonId2 == 55){
					player.getAppearance().getColour()[1] = 115;
				}else if(buttonId2 == 56){
					player.getAppearance().getColour()[1] = 111;
				}else if(buttonId2 == 57){
					player.getAppearance().getColour()[1] = 141;
				}else if(buttonId2 == 58){
					player.getAppearance().getColour()[1] = 45;
				}else if(buttonId2 == 59){
					player.getAppearance().getColour()[1] = 49;
				}else if(buttonId2 == 60){
					player.getAppearance().getColour()[1] = 84;
				}else if(buttonId2 == 61){
					player.getAppearance().getColour()[1] = 77;
				}else if(buttonId2 == 62){
					player.getAppearance().getColour()[1] = 118;
				}else if(buttonId2 == 63){
					player.getAppearance().getColour()[1] = 88;
				}else if(buttonId2 == 64){
					player.getAppearance().getColour()[1] = 85;
				}else if(buttonId2 == 65){
					player.getAppearance().getColour()[1] = 138;
				}else if(buttonId2 == 66){
					player.getAppearance().getColour()[1] = 51;
				}else if(buttonId2 == 67){
					player.getAppearance().getColour()[1] = 92;
				}else if(buttonId2 == 68){
					player.getAppearance().getColour()[1] = 112;
				}else if(buttonId2 == 69){
					player.getAppearance().getColour()[1] = 145;
				}else if(buttonId2 == 70){
					player.getAppearance().getColour()[1] = 179;
				}else if(buttonId2 == 71){
					player.getAppearance().getColour()[1] = 143;
				}else if(buttonId2 == 72){
					player.getAppearance().getColour()[1] = 149;
				}else if(buttonId2 == 73){
					player.getAppearance().getColour()[1] = 151;
				}else if(buttonId2 == 74){
					player.getAppearance().getColour()[1] = 153;
				}else if(buttonId2 == 75){
					player.getAppearance().getColour()[1] = 44;
				}else if(buttonId2 == 76){
					player.getAppearance().getColour()[1] = 154;
				}else if(buttonId2 == 77){
					player.getAppearance().getColour()[1] = 155;
				}else if(buttonId2 == 78){
					player.getAppearance().getColour()[1] = 86;
				}else if(buttonId2 == 79){
					player.getAppearance().getColour()[1] = 89;
				}else if(buttonId2 == 80){
					player.getAppearance().getColour()[1] = 72;
				}else if(buttonId2 == 81){
					player.getAppearance().getColour()[1] = 66;
				}else if(buttonId2 == 82){
					player.getAppearance().getColour()[1] = 33;
				}else if(buttonId2 == 83){
					player.getAppearance().getColour()[1] = 206;
				}else if(buttonId2 == 84){
					player.getAppearance().getColour()[1] = 109;
				}else if(buttonId2 == 85){
					player.getAppearance().getColour()[1] = 110;
				}else if(buttonId2 == 86){
					player.getAppearance().getColour()[1] = 114;
				}else if(buttonId2 == 87){
					player.getAppearance().getColour()[1] = 116;
				}else if(buttonId2 == 88){
					player.getAppearance().getColour()[1] = 184;
				}else if(buttonId2 == 89){
					player.getAppearance().getColour()[1] = 170;
				}else if(buttonId2 == 90){
					player.getAppearance().getColour()[1] = 120;
				}else if(buttonId2 == 91){
					player.getAppearance().getColour()[1] = 113;
				}else if(buttonId2 == 92){
					player.getAppearance().getColour()[1] = 150;
				}else if(buttonId2 == 93){
					player.getAppearance().getColour()[1] = 205;
				}else if(buttonId2 == 94){
					player.getAppearance().getColour()[1] = 210;
				}else if(buttonId2 == 95){
					player.getAppearance().getColour()[1] = 207;
				}else if(buttonId2 == 96){
					player.getAppearance().getColour()[1] = 209;
				}else if(buttonId2 == 97){
					player.getAppearance().getColour()[1] = 193;
				}else if(buttonId2 == 98){
					player.getAppearance().getColour()[1] = 152;
				}else if(buttonId2 == 99){
					player.getAppearance().getColour()[1] = 156;
				}else if(buttonId2 == 100){
					player.getAppearance().getColour()[1] = 183;
				}else if(buttonId2 == 101){
					player.getAppearance().getColour()[1] = 161;
				}else if(buttonId2 == 102){
					player.getAppearance().getColour()[1] = 159;
				}else if(buttonId2 == 103){
					player.getAppearance().getColour()[1] = 160;
				}else if(buttonId2 == 104){
					player.getAppearance().getColour()[1] = 73;
				}else if(buttonId2 == 105){
					player.getAppearance().getColour()[1] = 75;
				}else if(buttonId2 == 106){
					player.getAppearance().getColour()[1] = 181;
				}else if(buttonId2 == 107){
					player.getAppearance().getColour()[1] = 185;
				}else if(buttonId2 == 108){
					player.getAppearance().getColour()[1] = 208;
				}else if(buttonId2 == 109){
					player.getAppearance().getColour()[1] = 74;
				}else if(buttonId2 == 110){
					player.getAppearance().getColour()[1] = 36;
				}else if(buttonId2 == 111){
					player.getAppearance().getColour()[1] = 37;
				}else if(buttonId2 == 112){
					player.getAppearance().getColour()[1] = 43;
				}else if(buttonId2 == 113){
					player.getAppearance().getColour()[1] = 50;
				}else if(buttonId2 == 114){
					player.getAppearance().getColour()[1] = 58;
				}else if(buttonId2 == 115){
					player.getAppearance().getColour()[1] = 55;
				}else if(buttonId2 == 116){
					player.getAppearance().getColour()[1] = 139;
				}else if(buttonId2 == 117){
					player.getAppearance().getColour()[1] = 148;
				}else if(buttonId2 == 118){
					player.getAppearance().getColour()[1] = 147;
				}else if(buttonId2 == 119){
					player.getAppearance().getColour()[1] = 64;
				}else if(buttonId2 == 120){
					player.getAppearance().getColour()[1] = 69;
				}else if(buttonId2 == 121){
					player.getAppearance().getColour()[1] = 70;
				}else if(buttonId2 == 122){
					player.getAppearance().getColour()[1] = 71;
				}else if(buttonId2 == 123){
					player.getAppearance().getColour()[1] = 68;
				}else if(buttonId2 == 124){
					player.getAppearance().getColour()[1] = 93;
				}else if(buttonId2 == 125){
					player.getAppearance().getColour()[1] = 94;
				}else if(buttonId2 == 126){
					player.getAppearance().getColour()[1] = 95;
				}else if(buttonId2 == 127){
					player.getAppearance().getColour()[1] = 124;
				}else if(buttonId2 == 128){
					player.getAppearance().getColour()[1] = 182;
				}else if(buttonId2 == 129){
					player.getAppearance().getColour()[1] = 96;
				}else if(buttonId2 == 130){
					player.getAppearance().getColour()[1] = 97;
				}else if(buttonId2 == 131){
					player.getAppearance().getColour()[1] = 219;
				}else if(buttonId2 == 132){
					player.getAppearance().getColour()[1] = 63;
				}else if(buttonId2 == 133){
					player.getAppearance().getColour()[1] = 228;
				}else if(buttonId2 == 134){
					player.getAppearance().getColour()[1] = 79;
				}else if(buttonId2 == 135){
					player.getAppearance().getColour()[1] = 82;
				}else if(buttonId2 == 136){
					player.getAppearance().getColour()[1] = 98;
				}else if(buttonId2 == 137){
					player.getAppearance().getColour()[1] = 99;
				}else if(buttonId2 == 138){
					player.getAppearance().getColour()[1] = 100;
				}else if(buttonId2 == 139){
					player.getAppearance().getColour()[1] = 125;
				}else if(buttonId2 == 140){
					player.getAppearance().getColour()[1] = 126;
				}else if(buttonId2 == 141){
					player.getAppearance().getColour()[1] = 127;
				}else if(buttonId2 == 142){
					player.getAppearance().getColour()[1] = 40;
				}else if(buttonId2 == 143){
					player.getAppearance().getColour()[1] = 128;
				}else if(buttonId2 == 144){
					player.getAppearance().getColour()[1] = 129;
				}else if(buttonId2 == 145){
					player.getAppearance().getColour()[1] = 188;
				}else if(buttonId2 == 146){
					player.getAppearance().getColour()[1] = 130;
				}else if(buttonId2 == 147){
					player.getAppearance().getColour()[1] = 131;
				}else if(buttonId2 == 148){
					player.getAppearance().getColour()[1] = 186;
				}else if(buttonId2 == 149){
					player.getAppearance().getColour()[1] = 132;
				}else if(buttonId2 == 150){
					player.getAppearance().getColour()[1] = 164;
				}else if(buttonId2 == 151){
					player.getAppearance().getColour()[1] = 157;
				}else if(buttonId2 == 152){
					player.getAppearance().getColour()[1] = 180;
				}else if(buttonId2 == 153){
					player.getAppearance().getColour()[1] = 187;
				}else if(buttonId2 == 154){
					player.getAppearance().getColour()[1] = 31;
				}else if(buttonId2 == 155){
					player.getAppearance().getColour()[1] = 162;
				}else if(buttonId2 == 156){
					player.getAppearance().getColour()[1] = 168;
				}else if(buttonId2 == 157){
					player.getAppearance().getColour()[1] = 52;
				}else if(buttonId2 == 158){
					player.getAppearance().getColour()[1] = 163;
				}else if(buttonId2 == 159){
					player.getAppearance().getColour()[1] = 158;
				}else if(buttonId2 == 160){
					player.getAppearance().getColour()[1] = 196;
				}else if(buttonId2 == 161){
					player.getAppearance().getColour()[1] = 59;
				}else if(buttonId2 == 162){
					player.getAppearance().getColour()[1] = 60;
				}else if(buttonId2 == 163){
					player.getAppearance().getColour()[1] = 87;
				}else if(buttonId2 == 164){
					player.getAppearance().getColour()[1] = 78;
				}else if(buttonId2 == 165){
					player.getAppearance().getColour()[1] = 61;
				}else if(buttonId2 == 166){
					player.getAppearance().getColour()[1] = 76;
				}else if(buttonId2 == 167){
					player.getAppearance().getColour()[1] = 80;
				}else if(buttonId2 == 168){
					player.getAppearance().getColour()[1] = 171;
				}else if(buttonId2 == 169){
					player.getAppearance().getColour()[1] = 172;
				}else if(buttonId2 == 170){
					player.getAppearance().getColour()[1] = 176;
				}else if(buttonId2 == 171){
					player.getAppearance().getColour()[1] = 177;
				}else if(buttonId2 == 172){
					player.getAppearance().getColour()[1] = 178;
				}else if(buttonId2 == 173){
					player.getAppearance().getColour()[1] = 38;
				}else if(buttonId2 == 174){
					player.getAppearance().getColour()[1] = 41;
				}else if(buttonId2 == 175){
					player.getAppearance().getColour()[1] = 47;
				}else if(buttonId2 == 176){
					player.getAppearance().getColour()[1] = 62;
				}else if(buttonId2 == 177){
					player.getAppearance().getColour()[1] = 65;
				}else if(buttonId2 == 178){
					player.getAppearance().getColour()[1] = 67;
				}else if(buttonId2 == 179){
					player.getAppearance().getColour()[1] = 81;
				}else if(buttonId2 == 180){
					player.getAppearance().getColour()[1] = 83;
				}else if(buttonId2 == 181){
					player.getAppearance().getColour()[1] = 121;
				}else if(buttonId2 == 182){
					player.getAppearance().getColour()[1] = 122;
				}else if(buttonId2 == 183){
					player.getAppearance().getColour()[1] = 134;
				}else if(buttonId2 == 184){
					player.getAppearance().getColour()[1] = 140;
				}else if(buttonId2 == 185){
					player.getAppearance().getColour()[1] = 142;
				}else if(buttonId2 == 186){
					player.getAppearance().getColour()[1] = 146;
				}else if(buttonId2 == 187){
					player.getAppearance().getColour()[1] = 189;
				}else if(buttonId2 == 188){
					player.getAppearance().getColour()[1] = 190;
				}else if(buttonId2 == 189){
					player.getAppearance().getColour()[1] = 191;
				}else if(buttonId2 == 190){
					player.getAppearance().getColour()[1] = 195;
				}else if(buttonId2 == 191){
					player.getAppearance().getColour()[1] = 202;
				}else if(buttonId2 == 192){
					player.getAppearance().getColour()[1] = 203;
				}else if(buttonId2 == 193){
					player.getAppearance().getColour()[1] = 213;
				}else if(buttonId2 == 194){
					player.getAppearance().getColour()[1] = 214;
				}else if(buttonId2 == 195){
					player.getAppearance().getColour()[1] = 216;
				}else if(buttonId2 == 196){
					player.getAppearance().getColour()[1] = 218;
				}else if(buttonId2 == 197){
					player.getAppearance().getColour()[1] = 223;
				}else if(buttonId2 == 198){
					player.getAppearance().getColour()[1] = 224;
				}
			}else if(player.getAttribute("charCustom", 4) == 4){
				if(buttonId2 == 0){
					player.getAppearance().getColour()[2] = 32;
				}else if(buttonId2 == 1){
					player.getAppearance().getColour()[2] = 101;
				}else if(buttonId2 == 2){
					player.getAppearance().getColour()[2] = 48;
				}else if(buttonId2 == 3){
					player.getAppearance().getColour()[2] = 56;
				}else if(buttonId2 == 4){
					player.getAppearance().getColour()[2] = 165;
				}else if(buttonId2 == 5){
					player.getAppearance().getColour()[2] = 103;
				}else if(buttonId2 == 6){
					player.getAppearance().getColour()[2] = 167;
				}else if(buttonId2 == 7){
					player.getAppearance().getColour()[2] = 106;
				}else if(buttonId2 == 8){
					player.getAppearance().getColour()[2] = 54;
				}else if(buttonId2 == 9){
					player.getAppearance().getColour()[2] = 198;
				}else if(buttonId2 == 10){
					player.getAppearance().getColour()[2] = 199;
				}else if(buttonId2 == 11){
					player.getAppearance().getColour()[2] = 200;
				}else if(buttonId2 == 12){
					player.getAppearance().getColour()[2] = 225;
				}else if(buttonId2 == 13){
					player.getAppearance().getColour()[2] = 35;
				}else if(buttonId2 == 14){
					player.getAppearance().getColour()[2] = 39;
				}else if(buttonId2 == 15){
					player.getAppearance().getColour()[2] = 53;
				}else if(buttonId2 == 16){
					player.getAppearance().getColour()[2] = 42;
				}else if(buttonId2 == 17){
					player.getAppearance().getColour()[2] = 46;
				}else if(buttonId2 == 18){
					player.getAppearance().getColour()[2] = 29;
				}else if(buttonId2 == 19){
					player.getAppearance().getColour()[2] = 91;
				}else if(buttonId2 == 20){
					player.getAppearance().getColour()[2] = 57;
				}else if(buttonId2 == 21){
					player.getAppearance().getColour()[2] = 90;
				}else if(buttonId2 == 22){
					player.getAppearance().getColour()[2] = 34;
				}else if(buttonId2 == 23){
					player.getAppearance().getColour()[2] = 102;
				}else if(buttonId2 == 24){
					player.getAppearance().getColour()[2] = 104;
				}else if(buttonId2 == 25){
					player.getAppearance().getColour()[2] = 105;
				}else if(buttonId2 == 26){
					player.getAppearance().getColour()[2] = 107;
				}else if(buttonId2 == 27){
					player.getAppearance().getColour()[2] = 173;
				}else if(buttonId2 == 28){
					player.getAppearance().getColour()[2] = 137;
				}else if(buttonId2 == 29){
					player.getAppearance().getColour()[2] = 201;
				}else if(buttonId2 == 30){
					player.getAppearance().getColour()[2] = 204;
				}else if(buttonId2 == 31){
					player.getAppearance().getColour()[2] = 211;
				}else if(buttonId2 == 32){
					player.getAppearance().getColour()[2] = 197;
				}else if(buttonId2 == 33){
					player.getAppearance().getColour()[2] = 108;
				}else if(buttonId2 == 34){
					player.getAppearance().getColour()[2] = 217;
				}else if(buttonId2 == 35){
					player.getAppearance().getColour()[2] = 220;
				}else if(buttonId2 == 36){
					player.getAppearance().getColour()[2] = 221;
				}else if(buttonId2 == 37){
					player.getAppearance().getColour()[2] = 226;
				}else if(buttonId2 == 38){
					player.getAppearance().getColour()[2] = 227;
				}else if(buttonId2 == 39){
					player.getAppearance().getColour()[2] = 215;
				}else if(buttonId2 == 40){
					player.getAppearance().getColour()[2] = 222;
				}else if(buttonId2 == 41){
					player.getAppearance().getColour()[2] = 166;
				}else if(buttonId2 == 42){
					player.getAppearance().getColour()[2] = 212;
				}else if(buttonId2 == 43){
					player.getAppearance().getColour()[2] = 174;
				}else if(buttonId2 == 44){
					player.getAppearance().getColour()[2] = 175;
				}else if(buttonId2 == 45){
					player.getAppearance().getColour()[2] = 169;
				}else if(buttonId2 == 46){
					player.getAppearance().getColour()[2] = 144;
				}else if(buttonId2 == 47){
					player.getAppearance().getColour()[2] = 135;
				}else if(buttonId2 == 48){
					player.getAppearance().getColour()[2] = 136;
				}else if(buttonId2 == 49){
					player.getAppearance().getColour()[2] = 133;
				}else if(buttonId2 == 50){
					player.getAppearance().getColour()[2] = 123;
				}else if(buttonId2 == 51){
					player.getAppearance().getColour()[2] = 119;
				}else if(buttonId2 == 52){
					player.getAppearance().getColour()[2] = 192;
				}else if(buttonId2 == 53){
					player.getAppearance().getColour()[2] = 194;
				}else if(buttonId2 == 54){
					player.getAppearance().getColour()[2] = 117;
				}else if(buttonId2 == 55){
					player.getAppearance().getColour()[2] = 115;
				}else if(buttonId2 == 56){
					player.getAppearance().getColour()[2] = 111;
				}else if(buttonId2 == 57){
					player.getAppearance().getColour()[2] = 141;
				}else if(buttonId2 == 58){
					player.getAppearance().getColour()[2] = 45;
				}else if(buttonId2 == 59){
					player.getAppearance().getColour()[2] = 49;
				}else if(buttonId2 == 60){
					player.getAppearance().getColour()[2] = 84;
				}else if(buttonId2 == 61){
					player.getAppearance().getColour()[2] = 77;
				}else if(buttonId2 == 62){
					player.getAppearance().getColour()[2] = 118;
				}else if(buttonId2 == 63){
					player.getAppearance().getColour()[2] = 88;
				}else if(buttonId2 == 64){
					player.getAppearance().getColour()[2] = 85;
				}else if(buttonId2 == 65){
					player.getAppearance().getColour()[2] = 138;
				}else if(buttonId2 == 66){
					player.getAppearance().getColour()[2] = 51;
				}else if(buttonId2 == 67){
					player.getAppearance().getColour()[2] = 92;
				}else if(buttonId2 == 68){
					player.getAppearance().getColour()[2] = 112;
				}else if(buttonId2 == 69){
					player.getAppearance().getColour()[2] = 145;
				}else if(buttonId2 == 70){
					player.getAppearance().getColour()[2] = 179;
				}else if(buttonId2 == 71){
					player.getAppearance().getColour()[2] = 143;
				}else if(buttonId2 == 72){
					player.getAppearance().getColour()[2] = 149;
				}else if(buttonId2 == 73){
					player.getAppearance().getColour()[2] = 151;
				}else if(buttonId2 == 74){
					player.getAppearance().getColour()[2] = 153;
				}else if(buttonId2 == 75){
					player.getAppearance().getColour()[2] = 44;
				}else if(buttonId2 == 76){
					player.getAppearance().getColour()[2] = 154;
				}else if(buttonId2 == 77){
					player.getAppearance().getColour()[2] = 155;
				}else if(buttonId2 == 78){
					player.getAppearance().getColour()[2] = 86;
				}else if(buttonId2 == 79){
					player.getAppearance().getColour()[2] = 89;
				}else if(buttonId2 == 80){
					player.getAppearance().getColour()[2] = 72;
				}else if(buttonId2 == 81){
					player.getAppearance().getColour()[2] = 66;
				}else if(buttonId2 == 82){
					player.getAppearance().getColour()[2] = 33;
				}else if(buttonId2 == 83){
					player.getAppearance().getColour()[2] = 206;
				}else if(buttonId2 == 84){
					player.getAppearance().getColour()[2] = 109;
				}else if(buttonId2 == 85){
					player.getAppearance().getColour()[2] = 110;
				}else if(buttonId2 == 86){
					player.getAppearance().getColour()[2] = 114;
				}else if(buttonId2 == 87){
					player.getAppearance().getColour()[2] = 116;
				}else if(buttonId2 == 88){
					player.getAppearance().getColour()[2] = 184;
				}else if(buttonId2 == 89){
					player.getAppearance().getColour()[2] = 170;
				}else if(buttonId2 == 90){
					player.getAppearance().getColour()[2] = 120;
				}else if(buttonId2 == 91){
					player.getAppearance().getColour()[2] = 113;
				}else if(buttonId2 == 92){
					player.getAppearance().getColour()[2] = 150;
				}else if(buttonId2 == 93){
					player.getAppearance().getColour()[2] = 205;
				}else if(buttonId2 == 94){
					player.getAppearance().getColour()[2] = 210;
				}else if(buttonId2 == 95){
					player.getAppearance().getColour()[2] = 207;
				}else if(buttonId2 == 96){
					player.getAppearance().getColour()[2] = 209;
				}else if(buttonId2 == 97){
					player.getAppearance().getColour()[2] = 193;
				}else if(buttonId2 == 98){
					player.getAppearance().getColour()[2] = 152;
				}else if(buttonId2 == 99){
					player.getAppearance().getColour()[2] = 156;
				}else if(buttonId2 == 100){
					player.getAppearance().getColour()[2] = 183;
				}else if(buttonId2 == 101){
					player.getAppearance().getColour()[2] = 161;
				}else if(buttonId2 == 102){
					player.getAppearance().getColour()[2] = 159;
				}else if(buttonId2 == 103){
					player.getAppearance().getColour()[2] = 160;
				}else if(buttonId2 == 104){
					player.getAppearance().getColour()[2] = 73;
				}else if(buttonId2 == 105){
					player.getAppearance().getColour()[2] = 75;
				}else if(buttonId2 == 106){
					player.getAppearance().getColour()[2] = 181;
				}else if(buttonId2 == 107){
					player.getAppearance().getColour()[2] = 185;
				}else if(buttonId2 == 108){
					player.getAppearance().getColour()[2] = 208;
				}else if(buttonId2 == 109){
					player.getAppearance().getColour()[2] = 74;
				}else if(buttonId2 == 110){
					player.getAppearance().getColour()[2] = 36;
				}else if(buttonId2 == 111){
					player.getAppearance().getColour()[2] = 37;
				}else if(buttonId2 == 112){
					player.getAppearance().getColour()[2] = 43;
				}else if(buttonId2 == 113){
					player.getAppearance().getColour()[2] = 50;
				}else if(buttonId2 == 114){
					player.getAppearance().getColour()[2] = 58;
				}else if(buttonId2 == 115){
					player.getAppearance().getColour()[2] = 55;
				}else if(buttonId2 == 116){
					player.getAppearance().getColour()[2] = 139;
				}else if(buttonId2 == 117){
					player.getAppearance().getColour()[2] = 148;
				}else if(buttonId2 == 118){
					player.getAppearance().getColour()[2] = 147;
				}else if(buttonId2 == 119){
					player.getAppearance().getColour()[2] = 64;
				}else if(buttonId2 == 120){
					player.getAppearance().getColour()[2] = 69;
				}else if(buttonId2 == 121){
					player.getAppearance().getColour()[2] = 70;
				}else if(buttonId2 == 122){
					player.getAppearance().getColour()[2] = 71;
				}else if(buttonId2 == 123){
					player.getAppearance().getColour()[2] = 68;
				}else if(buttonId2 == 124){
					player.getAppearance().getColour()[2] = 93;
				}else if(buttonId2 == 125){
					player.getAppearance().getColour()[2] = 94;
				}else if(buttonId2 == 126){
					player.getAppearance().getColour()[2] = 95;
				}else if(buttonId2 == 127){
					player.getAppearance().getColour()[2] = 124;
				}else if(buttonId2 == 128){
					player.getAppearance().getColour()[2] = 182;
				}else if(buttonId2 == 129){
					player.getAppearance().getColour()[2] = 96;
				}else if(buttonId2 == 130){
					player.getAppearance().getColour()[2] = 97;
				}else if(buttonId2 == 131){
					player.getAppearance().getColour()[2] = 219;
				}else if(buttonId2 == 132){
					player.getAppearance().getColour()[2] = 63;
				}else if(buttonId2 == 133){
					player.getAppearance().getColour()[2] = 228;
				}else if(buttonId2 == 134){
					player.getAppearance().getColour()[2] = 79;
				}else if(buttonId2 == 135){
					player.getAppearance().getColour()[2] = 82;
				}else if(buttonId2 == 136){
					player.getAppearance().getColour()[2] = 98;
				}else if(buttonId2 == 137){
					player.getAppearance().getColour()[2] = 99;
				}else if(buttonId2 == 138){
					player.getAppearance().getColour()[2] = 100;
				}else if(buttonId2 == 139){
					player.getAppearance().getColour()[2] = 125;
				}else if(buttonId2 == 140){
					player.getAppearance().getColour()[2] = 126;
				}else if(buttonId2 == 141){
					player.getAppearance().getColour()[2] = 127;
				}else if(buttonId2 == 142){
					player.getAppearance().getColour()[2] = 40;
				}else if(buttonId2 == 143){
					player.getAppearance().getColour()[2] = 128;
				}else if(buttonId2 == 144){
					player.getAppearance().getColour()[2] = 129;
				}else if(buttonId2 == 145){
					player.getAppearance().getColour()[2] = 188;
				}else if(buttonId2 == 146){
					player.getAppearance().getColour()[2] = 130;
				}else if(buttonId2 == 147){
					player.getAppearance().getColour()[2] = 131;
				}else if(buttonId2 == 148){
					player.getAppearance().getColour()[2] = 186;
				}else if(buttonId2 == 149){
					player.getAppearance().getColour()[2] = 132;
				}else if(buttonId2 == 150){
					player.getAppearance().getColour()[2] = 164;
				}else if(buttonId2 == 151){
					player.getAppearance().getColour()[2] = 157;
				}else if(buttonId2 == 152){
					player.getAppearance().getColour()[2] = 180;
				}else if(buttonId2 == 153){
					player.getAppearance().getColour()[2] = 187;
				}else if(buttonId2 == 154){
					player.getAppearance().getColour()[2] = 31;
				}else if(buttonId2 == 155){
					player.getAppearance().getColour()[2] = 162;
				}else if(buttonId2 == 156){
					player.getAppearance().getColour()[2] = 168;
				}else if(buttonId2 == 157){
					player.getAppearance().getColour()[2] = 52;
				}else if(buttonId2 == 158){
					player.getAppearance().getColour()[2] = 163;
				}else if(buttonId2 == 159){
					player.getAppearance().getColour()[2] = 158;
				}else if(buttonId2 == 160){
					player.getAppearance().getColour()[2] = 196;
				}else if(buttonId2 == 161){
					player.getAppearance().getColour()[2] = 59;
				}else if(buttonId2 == 162){
					player.getAppearance().getColour()[2] = 60;
				}else if(buttonId2 == 163){
					player.getAppearance().getColour()[2] = 87;
				}else if(buttonId2 == 164){
					player.getAppearance().getColour()[2] = 78;
				}else if(buttonId2 == 165){
					player.getAppearance().getColour()[2] = 61;
				}else if(buttonId2 == 166){
					player.getAppearance().getColour()[2] = 76;
				}else if(buttonId2 == 167){
					player.getAppearance().getColour()[2] = 80;
				}else if(buttonId2 == 168){
					player.getAppearance().getColour()[2] = 171;
				}else if(buttonId2 == 169){
					player.getAppearance().getColour()[2] = 172;
				}else if(buttonId2 == 170){
					player.getAppearance().getColour()[2] = 176;
				}else if(buttonId2 == 171){
					player.getAppearance().getColour()[2] = 177;
				}else if(buttonId2 == 172){
					player.getAppearance().getColour()[2] = 178;
				}else if(buttonId2 == 173){
					player.getAppearance().getColour()[2] = 38;
				}else if(buttonId2 == 174){
					player.getAppearance().getColour()[2] = 41;
				}else if(buttonId2 == 175){
					player.getAppearance().getColour()[2] = 47;
				}else if(buttonId2 == 176){
					player.getAppearance().getColour()[2] = 62;
				}else if(buttonId2 == 177){
					player.getAppearance().getColour()[2] = 65;
				}else if(buttonId2 == 178){
					player.getAppearance().getColour()[2] = 67;
				}else if(buttonId2 == 179){
					player.getAppearance().getColour()[2] = 81;
				}else if(buttonId2 == 180){
					player.getAppearance().getColour()[2] = 83;
				}else if(buttonId2 == 181){
					player.getAppearance().getColour()[2] = 121;
				}else if(buttonId2 == 182){
					player.getAppearance().getColour()[2] = 122;
				}else if(buttonId2 == 183){
					player.getAppearance().getColour()[2] = 134;
				}else if(buttonId2 == 184){
					player.getAppearance().getColour()[2] = 140;
				}else if(buttonId2 == 185){
					player.getAppearance().getColour()[2] = 142;
				}else if(buttonId2 == 186){
					player.getAppearance().getColour()[2] = 146;
				}else if(buttonId2 == 187){
					player.getAppearance().getColour()[2] = 189;
				}else if(buttonId2 == 188){
					player.getAppearance().getColour()[2] = 190;
				}else if(buttonId2 == 189){
					player.getAppearance().getColour()[2] = 191;
				}else if(buttonId2 == 190){
					player.getAppearance().getColour()[2] = 195;
				}else if(buttonId2 == 191){
					player.getAppearance().getColour()[2] = 202;
				}else if(buttonId2 == 192){
					player.getAppearance().getColour()[2] = 203;
				}else if(buttonId2 == 193){
					player.getAppearance().getColour()[2] = 213;
				}else if(buttonId2 == 194){
					player.getAppearance().getColour()[2] = 214;
				}else if(buttonId2 == 195){
					player.getAppearance().getColour()[2] = 216;
				}else if(buttonId2 == 196){
					player.getAppearance().getColour()[2] = 218;
				}else if(buttonId2 == 197){
					player.getAppearance().getColour()[2] = 223;
				}else if(buttonId2 == 198){
					player.getAppearance().getColour()[2] = 224;
				}
			}else{
				if(buttonId2 == 0){
					player.getAppearance().getColour()[4] = 9;
				}else if(buttonId2 == 1){
					player.getAppearance().getColour()[4] = 8;
				}else if(buttonId2 == 2){
					player.getAppearance().getColour()[4] = 7;
				}else if(buttonId2 == 3){
					player.getAppearance().getColour()[4] = 0;
				}else if(buttonId2 == 4){
					player.getAppearance().getColour()[4] = 1;
				}else if(buttonId2 == 5){
					player.getAppearance().getColour()[4] = 2;
				}else if(buttonId2 == 6){
					player.getAppearance().getColour()[4] = 3;
				}else if(buttonId2 == 7){
					player.getAppearance().getColour()[4] = 4;
				}else if(buttonId2 == 8){
					player.getAppearance().getColour()[4] = 5;
				}else if(buttonId2 == 9){
					player.getAppearance().getColour()[4] = 6;
				}else if(buttonId2 == 10){
					player.getAppearance().getColour()[4] = 10;
				}else if(buttonId2 == 11){
					player.getAppearance().getColour()[4] = 11;
				}
			}
			break;
		case 38:
			player.getAppearance().setGender((byte)0);
			player.getAppearance().getLook()[0] = 3; // Hair
			player.getAppearance().getLook()[1] = 14; // Beard
			break;
		case 39:
			player.getAppearance().setGender((byte)1);
			player.getAppearance().getLook()[0] = 48; // Hair
			player.getAppearance().getLook()[1] = 1000; // Beard
			break;
		case 48:
			player.setAttribute("charStage", 1);
			if(player.getAppearance().getGender() == 0){
				player.getAppearance().getLook()[2] = 471;
				player.getAppearance().getLook()[3] = 613;
				player.getAppearance().getLook()[4] = 389;
				player.getAppearance().getLook()[5] = 645;
				player.getAppearance().getLook()[6] = 439;
				player.getAppearance().getColour()[1] = 37;
				player.getAppearance().getColour()[2] = 213;
				player.getAppearance().getColour()[3] = 4;
			}else{
				player.getAppearance().getLook()[2] = 583;
				player.getAppearance().getLook()[3] = 420;
				player.getAppearance().getLook()[4] = 532;
				player.getAppearance().getLook()[5] = 500;
				player.getAppearance().getLook()[6] = 551;
				player.getAppearance().getColour()[1] = 45;
				player.getAppearance().getColour()[2] = 221;
				player.getAppearance().getColour()[3] = 15;
			}
			break;
		case 83:
			if(player.getAttribute("charStage", 1) == 1){
				if(player.getAppearance().getGender() == 0){
					player.getAppearance().getLook()[2] = 471;
					player.getAppearance().getLook()[3] = 613;
					player.getAppearance().getLook()[4] = 389;
					player.getAppearance().getLook()[5] = 645;
					player.getAppearance().getLook()[6] = 439;
					player.getAppearance().getColour()[1] = 37;
					player.getAppearance().getColour()[2] = 213;
					player.getAppearance().getColour()[3] = 4;
				}else{
					player.getAppearance().getLook()[2] = 583;
					player.getAppearance().getLook()[3] = 420;
					player.getAppearance().getLook()[4] = 532;
					player.getAppearance().getLook()[5] = 500;
					player.getAppearance().getLook()[6] = 551;
					player.getAppearance().getColour()[1] = 45;
					player.getAppearance().getColour()[2] = 221;
					player.getAppearance().getColour()[3] = 15;
				}
			}else if(player.getAttribute("charStage", 2) == 2){
				if(player.getAppearance().getGender() == 0){
					player.getAppearance().getLook()[2] = 453;
					player.getAppearance().getLook()[4] = 380;
					player.getAppearance().getLook()[5] = 636;
					player.getAppearance().getLook()[6] = 429;
					player.getAppearance().getColour()[1] = 197;
					player.getAppearance().getColour()[2] = 202;
					player.getAppearance().getColour()[3] = 4;
				}else{
					player.getAppearance().getLook()[2] = 562;
					player.getAppearance().getLook()[3] = 425;
					player.getAppearance().getLook()[4] = 523;
					player.getAppearance().getLook()[5] = 491;
					player.getAppearance().getLook()[6] = 551;
					player.getAppearance().getColour()[1] = 197;
					player.getAppearance().getColour()[2] = 202;
					player.getAppearance().getColour()[3] = 4;
				}
			}else if(player.getAttribute("charStage", 3) == 3){
				if(player.getAppearance().getGender() == 0){
					player.getAppearance().getLook()[2] = 447;
					player.getAppearance().getLook()[4] = 386;
					player.getAppearance().getLook()[5] = 642;
					player.getAppearance().getLook()[6] = 429;
					player.getAppearance().getColour()[1] = 125;
					player.getAppearance().getColour()[2] = 125;
					player.getAppearance().getColour()[3] = 4;
				}else{
					player.getAppearance().getLook()[2] = 581;
					player.getAppearance().getLook()[3] = 417;
					player.getAppearance().getLook()[4] = 529;
					player.getAppearance().getLook()[5] = 497;
					player.getAppearance().getLook()[6] = 551;
					player.getAppearance().getColour()[1] = 125;
					player.getAppearance().getColour()[2] = 125;
					player.getAppearance().getColour()[3] = 4;
				}
			}else if(player.getAttribute("charStage", 4) == 4){
				if(player.getAppearance().getGender() == 0){
					player.getAppearance().getLook()[2] = 469;
					player.getAppearance().getLook()[3] = 607;
					player.getAppearance().getLook()[4] = 383;
					player.getAppearance().getLook()[5] = 639;
					player.getAppearance().getLook()[6] = 431;
					player.getAppearance().getColour()[1] = 149;
					player.getAppearance().getColour()[2] = 150;
					player.getAppearance().getColour()[3] = 4;
				}else{
					player.getAppearance().getLook()[2] = 579;
					player.getAppearance().getLook()[3] = 414;
					player.getAppearance().getLook()[4] = 526;
					player.getAppearance().getLook()[5] = 494;
					player.getAppearance().getLook()[6] = 551;
					player.getAppearance().getColour()[1] = 149;
					player.getAppearance().getColour()[2] = 149;
					player.getAppearance().getColour()[3] = 4;
				}
			}else if(player.getAttribute("charStage", 5) == 5){
				if(player.getAppearance().getGender() == 0){
					player.getAppearance().getLook()[2] = 451;
					player.getAppearance().getLook()[4] = 378;
					player.getAppearance().getLook()[5] = 634;
					player.getAppearance().getLook()[6] = 429;
					player.getAppearance().getColour()[1] = 165;
					player.getAppearance().getColour()[2] = 165;
					player.getAppearance().getColour()[3] = 4;
				}else{
					player.getAppearance().getLook()[2] = 560;
					player.getAppearance().getLook()[3] = 416;
					player.getAppearance().getLook()[4] = 521;
					player.getAppearance().getLook()[5] = 489;
					player.getAppearance().getLook()[6] = 551;
					player.getAppearance().getColour()[1] = 165;
					player.getAppearance().getColour()[2] = 166;
					player.getAppearance().getColour()[3] = 142;
				}
			}else if(player.getAttribute("charStage", 6) == 6){
				if(player.getAppearance().getGender() == 0){
					player.getAppearance().getLook()[2] = 452;
					player.getAppearance().getLook()[3] = 592;
					player.getAppearance().getLook()[4] = 371;
					player.getAppearance().getLook()[5] = 627;
					player.getAppearance().getLook()[6] = 434;
					player.getAppearance().getColour()[1] = 189;
					player.getAppearance().getColour()[2] = 189;
					player.getAppearance().getColour()[3] = 39;
				}else{
					player.getAppearance().getLook()[2] = 561;
					player.getAppearance().getLook()[3] = 416;
					player.getAppearance().getLook()[4] = 514;
					player.getAppearance().getLook()[5] = 482;
					player.getAppearance().getLook()[6] = 545;
					player.getAppearance().getColour()[1] = 189;
					player.getAppearance().getColour()[2] = 189;
					player.getAppearance().getColour()[3] = 39;
				}
			}else if(player.getAttribute("charStage", 7) == 7){
				if(player.getAppearance().getGender() == 0){
					player.getAppearance().getLook()[2] = 464;
					player.getAppearance().getLook()[3] = 597;
					player.getAppearance().getLook()[4] = 373;
					player.getAppearance().getLook()[5] = 629;
					player.getAppearance().getLook()[6] = 435;
					player.getAppearance().getColour()[1] = 29;
					player.getAppearance().getColour()[2] = 30;
					player.getAppearance().getColour()[3] = 0;
				}else{
					player.getAppearance().getLook()[2] = 573;
					player.getAppearance().getLook()[3] = 404;
					player.getAppearance().getLook()[4] = 516;
					player.getAppearance().getLook()[5] = 484;
					player.getAppearance().getLook()[6] = 547;
					player.getAppearance().getColour()[1] = 29;
					player.getAppearance().getColour()[2] = 30;
					player.getAppearance().getColour()[3] = 0;
				}
			}else if(player.getAttribute("charStage", 8) == 8){
				if(player.getAppearance().getGender() == 0){
					player.getAppearance().getLook()[2] = 468;
					player.getAppearance().getLook()[3] = 603;
					player.getAppearance().getLook()[4] = 379;
					player.getAppearance().getLook()[5] = 635;
					player.getAppearance().getLook()[6] = 429;
					player.getAppearance().getColour()[1] = 117;
					player.getAppearance().getColour()[2] = 118;
					player.getAppearance().getColour()[3] = 4;
				}else{
					player.getAppearance().getLook()[2] = 578;
					player.getAppearance().getLook()[3] = 410;
					player.getAppearance().getLook()[4] = 522;
					player.getAppearance().getLook()[5] = 490;
					player.getAppearance().getLook()[6] = 550;
					player.getAppearance().getColour()[1] = 117;
					player.getAppearance().getColour()[2] = 118;
					player.getAppearance().getColour()[3] = 4;
				}
			}else if(player.getAttribute("charStage", 9) == 9){
				if(player.getAppearance().getGender() == 0){
					player.getAppearance().getLook()[2] = 458;
					player.getAppearance().getLook()[3] = 589;
					player.getAppearance().getLook()[4] = 365;
					player.getAppearance().getLook()[5] = 621;
					player.getAppearance().getLook()[6] = 428;
					player.getAppearance().getColour()[1] = 69;
					player.getAppearance().getColour()[2] = 69;
					player.getAppearance().getColour()[3] = 154;
				}else{
					player.getAppearance().getLook()[2] = 566;
					player.getAppearance().getLook()[3] = 396;
					player.getAppearance().getLook()[4] = 508;
					player.getAppearance().getLook()[5] = 476;
					player.getAppearance().getLook()[6] = 540;
					player.getAppearance().getColour()[1] = 69;
					player.getAppearance().getColour()[2] = 69;
					player.getAppearance().getColour()[3] = 154;
				}
			}else if(player.getAttribute("charStage", 10) == 10){
				if(player.getAppearance().getGender() == 0){
					player.getAppearance().getLook()[2] = 465;
					player.getAppearance().getLook()[3] = 599;
					player.getAppearance().getLook()[4] = 375;
					player.getAppearance().getLook()[5] = 631;
					player.getAppearance().getLook()[6] = 436;
					player.getAppearance().getColour()[1] = 205;
					player.getAppearance().getColour()[2] = 206;
					player.getAppearance().getColour()[3] = 4;
				}else{
					player.getAppearance().getLook()[2] = 575;
					player.getAppearance().getLook()[3] = 406;
					player.getAppearance().getLook()[4] = 518;
					player.getAppearance().getLook()[5] = 486;
					player.getAppearance().getLook()[6] = 551;
					player.getAppearance().getColour()[1] = 205;
					player.getAppearance().getColour()[2] = 206;
					player.getAppearance().getColour()[3] = 4;
				};
				player.getAppearance().getColour()[3] = 4;
			}else if(player.getAttribute("charStage", 11) == 11){
				if(player.getAppearance().getGender() == 0){
					player.getAppearance().getLook()[2] = 460;
					player.getAppearance().getLook()[3] = 592;
					player.getAppearance().getLook()[4] = 368;
					player.getAppearance().getLook()[5] = 624;
					player.getAppearance().getLook()[6] = 431;
					player.getAppearance().getColour()[1] = 93;
					player.getAppearance().getColour()[2] = 94;
					player.getAppearance().getColour()[3] = 4;
				}else{
					player.getAppearance().getLook()[2] = 569;
					player.getAppearance().getLook()[3] = 399;
					player.getAppearance().getLook()[4] = 511;
					player.getAppearance().getLook()[5] = 479;
					player.getAppearance().getLook()[6] = 551;
					player.getAppearance().getColour()[1] = 93;
					player.getAppearance().getColour()[2] = 94;
					player.getAppearance().getColour()[3] = 4;
				}
			}else if(player.getAttribute("charStage", 12) == 12){
				if(player.getAppearance().getGender() == 0){
					player.getAppearance().getLook()[2] = 456;
					player.getAppearance().getLook()[3] = 589;
					player.getAppearance().getLook()[4] = 9;
					player.getAppearance().getLook()[5] = 651;
					player.getAppearance().getLook()[6] = 442;
					player.getAppearance().getColour()[1] = 141;
					player.getAppearance().getColour()[2] = 141;
					player.getAppearance().getColour()[3] = 118;
				}else{
					player.getAppearance().getLook()[2] = 558;
					player.getAppearance().getLook()[3] = 399;
					player.getAppearance().getLook()[4] = 538;
					player.getAppearance().getLook()[5] = 506;
					player.getAppearance().getLook()[6] = 555;
					player.getAppearance().getColour()[1] = 141;
					player.getAppearance().getColour()[2] = 141;
					player.getAppearance().getColour()[3] = 118;
				}
			}else if(player.getAttribute("charStage", 13) == 13){
				if(player.getAppearance().getGender() == 0){
					player.getAppearance().getLook()[2] = 459;
					player.getAppearance().getLook()[3] = 591;
					player.getAppearance().getLook()[4] = 367;
					player.getAppearance().getLook()[5] = 623;
					player.getAppearance().getLook()[6] = 430;
					player.getAppearance().getColour()[1] = 133;
					player.getAppearance().getColour()[2] = 134;
					player.getAppearance().getColour()[3] = 4;
				}else{
					player.getAppearance().getLook()[2] = 568;
					player.getAppearance().getLook()[3] = 398;
					player.getAppearance().getLook()[4] = 510;
					player.getAppearance().getLook()[5] = 478;
					player.getAppearance().getLook()[6] = 542;
					player.getAppearance().getColour()[1] = 133;
					player.getAppearance().getColour()[2] = 134;
					player.getAppearance().getColour()[3] = 4;
				}
			}else if(player.getAttribute("charStage", 14) == 14){
				if(player.getAppearance().getGender() == 0){
					player.getAppearance().getLook()[2] = 462;
					player.getAppearance().getLook()[3] = 594;
					player.getAppearance().getLook()[4] = 370;
					player.getAppearance().getLook()[5] = 626;
					player.getAppearance().getLook()[6] = 433;
					player.getAppearance().getColour()[1] = 85;
					player.getAppearance().getColour()[2] = 85;
					player.getAppearance().getColour()[3] = 69;
				}else{
					player.getAppearance().getLook()[2] = 571;
					player.getAppearance().getLook()[3] = 401;
					player.getAppearance().getLook()[4] = 513;
					player.getAppearance().getLook()[5] = 481;
					player.getAppearance().getLook()[6] = 544;
					player.getAppearance().getColour()[1] = 85;
					player.getAppearance().getColour()[2] = 86;
					player.getAppearance().getColour()[3] = 69;
				}
			}else if(player.getAttribute("charStage", 15) == 15){
				if(player.getAppearance().getGender() == 0){
					player.getAppearance().getLook()[2] = 467;
					player.getAppearance().getLook()[3] = 601;
					player.getAppearance().getLook()[4] = 377;
					player.getAppearance().getLook()[5] = 633;
					player.getAppearance().getLook()[6] = 438;
					player.getAppearance().getColour()[1] = 173;
					player.getAppearance().getColour()[2] = 174;
					player.getAppearance().getColour()[3] = 4;
				}else{
					player.getAppearance().getLook()[2] = 577;
					player.getAppearance().getLook()[3] = 408;
					player.getAppearance().getLook()[4] = 520;
					player.getAppearance().getLook()[5] = 488;
					player.getAppearance().getLook()[6] = 549;
					player.getAppearance().getColour()[1] = 173;
					player.getAppearance().getColour()[2] = 174;
					player.getAppearance().getColour()[3] = 4;
				}
			}else if(player.getAttribute("charStage", 16) == 16){
				if(player.getAppearance().getGender() == 0){
					player.getAppearance().getLook()[2] = 461;
					player.getAppearance().getLook()[3] = 593;
					player.getAppearance().getLook()[4] = 369;
					player.getAppearance().getLook()[5] = 625;
					player.getAppearance().getLook()[6] = 432;
					player.getAppearance().getColour()[1] = 77;
					player.getAppearance().getColour()[2] = 78;
					player.getAppearance().getColour()[3] = 4;
				}else{
					player.getAppearance().getLook()[2] = 570;
					player.getAppearance().getLook()[3] = 400;
					player.getAppearance().getLook()[4] = 512;
					player.getAppearance().getLook()[5] = 480;
					player.getAppearance().getLook()[6] = 543;
					player.getAppearance().getColour()[1] = 77;
					player.getAppearance().getColour()[2] = 78;
					player.getAppearance().getColour()[3] = 4;
				}
			}else if(player.getAttribute("charStage", 17) == 17){
				if(player.getAppearance().getGender() == 0){
					player.getAppearance().getLook()[2] = 445;
					player.getAppearance().getLook()[3] = 589;
					player.getAppearance().getLook()[4] = 366;
					player.getAppearance().getLook()[5] = 622;
					player.getAppearance().getLook()[6] = 429;
					player.getAppearance().getColour()[1] = 101;
					player.getAppearance().getColour()[2] = 102;
					player.getAppearance().getColour()[3] = 4;
				}else{
					player.getAppearance().getLook()[2] = 567;
					player.getAppearance().getLook()[3] = 397;
					player.getAppearance().getLook()[4] = 509;
					player.getAppearance().getLook()[5] = 477;
					player.getAppearance().getLook()[6] = 541;
					player.getAppearance().getColour()[1] = 101;
					player.getAppearance().getColour()[2] = 102;
					player.getAppearance().getColour()[3] = 4;
				}
			}else if(player.getAttribute("charStage", 18) == 18){
				if(player.getAppearance().getGender() == 0){
					player.getAppearance().getLook()[2] = 446;
					player.getAppearance().getLook()[3] = 599;
					player.getAppearance().getLook()[4] = 374;
					player.getAppearance().getLook()[5] = 630;
					player.getAppearance().getLook()[6] = 429;
					player.getAppearance().getColour()[1] = 109;
					player.getAppearance().getColour()[2] = 109;
					player.getAppearance().getColour()[3] = 4;
				}else{
					player.getAppearance().getLook()[2] = 574;
					player.getAppearance().getLook()[3] = 405;
					player.getAppearance().getLook()[4] = 517;
					player.getAppearance().getLook()[5] = 485;
					player.getAppearance().getLook()[6] = 548;
					player.getAppearance().getColour()[1] = 109;
					player.getAppearance().getColour()[2] = 109;
					player.getAppearance().getColour()[3] = 4;
				}
			}else if(player.getAttribute("charStage", 19) == 19){
				if(player.getAppearance().getGender() == 0){
					player.getAppearance().getLook()[2] = 466;
					player.getAppearance().getLook()[3] = 600;
					player.getAppearance().getLook()[4] = 376;
					player.getAppearance().getLook()[5] = 632;
					player.getAppearance().getLook()[6] = 437;
					player.getAppearance().getColour()[1] = 181;
					player.getAppearance().getColour()[2] = 181;
					player.getAppearance().getColour()[3] = 56;
				}else{
					player.getAppearance().getLook()[2] = 576;
					player.getAppearance().getLook()[3] = 407;
					player.getAppearance().getLook()[4] = 519;
					player.getAppearance().getLook()[5] = 487;
					player.getAppearance().getLook()[6] = 553;
					player.getAppearance().getColour()[1] = 181;
					player.getAppearance().getColour()[2] = 181;
					player.getAppearance().getColour()[3] = 158;
				}
			}else if(player.getAttribute("charStage", 20) == 20){
				if(player.getAppearance().getGender() == 0){
					player.getAppearance().getLook()[2] = 463;
					player.getAppearance().getLook()[3] = 596;
					player.getAppearance().getLook()[4] = 372;
					player.getAppearance().getLook()[5] = 628;
					player.getAppearance().getLook()[6] = 429;
					player.getAppearance().getColour()[1] = 157;
					player.getAppearance().getColour()[2] = 157;
					player.getAppearance().getColour()[3] = 134;
				}else{
					player.getAppearance().getLook()[2] = 572;
					player.getAppearance().getLook()[3] = 403;
					player.getAppearance().getLook()[4] = 515;
					player.getAppearance().getLook()[5] = 483;
					player.getAppearance().getLook()[6] = 546;
					player.getAppearance().getColour()[1] = 157;
					player.getAppearance().getColour()[2] = 157;
					player.getAppearance().getColour()[3] = 54;
				}
			}
			break;
		case 84:
			if(player.getAttribute("charStage", 1) == 1){
				if(player.getAppearance().getGender() == 0){
					player.getAppearance().getLook()[2] = 443;
					player.getAppearance().getLook()[3] = 614;
					player.getAppearance().getLook()[4] = 390;
					player.getAppearance().getLook()[5] = 646;
					player.getAppearance().getLook()[6] = 440;
					player.getAppearance().getColour()[1] = 37;
					player.getAppearance().getColour()[2] = 213;
					player.getAppearance().getColour()[3] = 4;
				}else{
					player.getAppearance().getLook()[2] = 584;
					player.getAppearance().getLook()[3] = 421;
					player.getAppearance().getLook()[4] = 533;
					player.getAppearance().getLook()[5] = 501;
					player.getAppearance().getLook()[6] = 553;
					player.getAppearance().getColour()[1] = 45;
					player.getAppearance().getColour()[2] = 221;
					player.getAppearance().getColour()[3] = 4;
				}
			}else if(player.getAttribute("charStage", 2) == 2){
				if(player.getAppearance().getGender() == 0){
					player.getAppearance().getLook()[2] = 454;
					player.getAppearance().getLook()[4] = 381;
					player.getAppearance().getLook()[5] = 637;
					player.getAppearance().getLook()[6] = 429;
					player.getAppearance().getColour()[1] = 197;
					player.getAppearance().getColour()[2] = 202;
					player.getAppearance().getColour()[3] = 4;
				}else{
					player.getAppearance().getLook()[2] = 563;
					player.getAppearance().getLook()[3] = 425;
					player.getAppearance().getLook()[4] = 524;
					player.getAppearance().getLook()[5] = 492;
					player.getAppearance().getLook()[6] = 551;
					player.getAppearance().getColour()[1] = 197;
					player.getAppearance().getColour()[2] = 202;
					player.getAppearance().getColour()[3] = 4;
				}
			}else if(player.getAttribute("charStage", 3) == 3){
				if(player.getAppearance().getGender() == 0){
					player.getAppearance().getLook()[2] = 448;
					player.getAppearance().getLook()[4] = 387;
					player.getAppearance().getLook()[5] = 643;
					player.getAppearance().getLook()[6] = 429;
					player.getAppearance().getColour()[1] = 125;
					player.getAppearance().getColour()[2] = 125;
					player.getAppearance().getColour()[3] = 4;
				}else{
					player.getAppearance().getLook()[2] = 582;
					player.getAppearance().getLook()[3] = 418;
					player.getAppearance().getLook()[4] = 530;
					player.getAppearance().getLook()[5] = 498;
					player.getAppearance().getLook()[6] = 551;
					player.getAppearance().getColour()[1] = 125;
					player.getAppearance().getColour()[2] = 129;
					player.getAppearance().getColour()[3] = 4;
				}
			}else if(player.getAttribute("charStage", 4) == 4){
				if(player.getAppearance().getGender() == 0){
					player.getAppearance().getLook()[2] = 470;
					player.getAppearance().getLook()[3] = 608;
					player.getAppearance().getLook()[4] = 384;
					player.getAppearance().getLook()[5] = 640;
					player.getAppearance().getLook()[6] = 429;
					player.getAppearance().getColour()[1] = 149;
					player.getAppearance().getColour()[2] = 150;
					player.getAppearance().getColour()[3] = 4;
				}else{
					player.getAppearance().getLook()[2] = 559;
					player.getAppearance().getLook()[3] = 415;
					player.getAppearance().getLook()[4] = 527;
					player.getAppearance().getLook()[5] = 495;
					player.getAppearance().getLook()[6] = 551;
					player.getAppearance().getColour()[1] = 149;
					player.getAppearance().getColour()[2] = 150;
					player.getAppearance().getColour()[3] = 4;
				}
			}else if(player.getAttribute("charStage", 15) == 15){
				if(player.getAppearance().getGender() == 0){
					player.getAppearance().getLook()[2] = 457;
					player.getAppearance().getLook()[3] = 588;
					player.getAppearance().getLook()[4] = 364;
					player.getAppearance().getLook()[5] = 620;
					player.getAppearance().getLook()[6] = 427;
					player.getAppearance().getColour()[1] = 61;
					player.getAppearance().getColour()[2] = 61;
					player.getAppearance().getColour()[3] = 4;
				}else{
					player.getAppearance().getLook()[2] = 565;
					player.getAppearance().getLook()[3] = 395;
					player.getAppearance().getLook()[4] = 507;
					player.getAppearance().getLook()[5] = 475;
					player.getAppearance().getLook()[6] = 539;
					player.getAppearance().getColour()[1] = 61;
					player.getAppearance().getColour()[2] = 61;
					player.getAppearance().getColour()[3] = 4;
				}
			}
			break;
		case 85:
			if(player.getAttribute("charStage", 1) == 1){
				if(player.getAppearance().getGender() == 0){
					player.getAppearance().getLook()[2] = 472;
					player.getAppearance().getLook()[3] = 615;
					player.getAppearance().getLook()[4] = 391;
					player.getAppearance().getLook()[5] = 647;
					player.getAppearance().getLook()[6] = 441;
					player.getAppearance().getColour()[1] = 37;
					player.getAppearance().getColour()[2] = 213;
					player.getAppearance().getColour()[3] = 4;
				}else{
					player.getAppearance().getLook()[2] = 585;
					player.getAppearance().getLook()[3] = 422;
					player.getAppearance().getLook()[4] = 534;
					player.getAppearance().getLook()[5] = 502;
					player.getAppearance().getLook()[6] = 554;
					player.getAppearance().getColour()[1] = 45;
					player.getAppearance().getColour()[2] = 221;
					player.getAppearance().getColour()[3] = 4;
				}
			}else if(player.getAttribute("charStage", 2) == 2){
				if(player.getAppearance().getGender() == 0){
					player.getAppearance().getLook()[2] = 455;
					player.getAppearance().getLook()[4] = 382;
					player.getAppearance().getLook()[5] = 638;
					player.getAppearance().getLook()[6] = 429;
					player.getAppearance().getColour()[1] = 197;
					player.getAppearance().getColour()[2] = 202;
					player.getAppearance().getColour()[3] = 4;
				}else{
					player.getAppearance().getLook()[2] = 564;
					player.getAppearance().getLook()[3] = 425;
					player.getAppearance().getLook()[4] = 525;
					player.getAppearance().getLook()[5] = 493;
					player.getAppearance().getLook()[6] = 551;
					player.getAppearance().getColour()[1] = 197;
					player.getAppearance().getColour()[2] = 202;
					player.getAppearance().getColour()[3] = 4;
				}
			}else if(player.getAttribute("charStage", 3) == 3){
				if(player.getAppearance().getGender() == 0){
					player.getAppearance().getLook()[2] = 449;
					player.getAppearance().getLook()[4] = 388;
					player.getAppearance().getLook()[5] = 644;
					player.getAppearance().getLook()[6] = 429;
					player.getAppearance().getColour()[1] = 125;
					player.getAppearance().getColour()[2] = 125;
					player.getAppearance().getColour()[3] = 4;
				}else{
					player.getAppearance().getLook()[2] = 557;
					player.getAppearance().getLook()[3] = 419;
					player.getAppearance().getLook()[4] = 531;
					player.getAppearance().getLook()[5] = 499;
					player.getAppearance().getLook()[6] = 551;
					player.getAppearance().getColour()[1] = 125;
					player.getAppearance().getColour()[2] = 125;
					player.getAppearance().getColour()[3] = 4;
				}
			}else if(player.getAttribute("charStage", 4) == 4){
				if(player.getAppearance().getGender() == 0){
					player.getAppearance().getLook()[2] = 450;
					player.getAppearance().getLook()[3] = 609;
					player.getAppearance().getLook()[4] = 385;
					player.getAppearance().getLook()[5] = 641;
					player.getAppearance().getLook()[6] = 429;
					player.getAppearance().getColour()[1] = 149;
					player.getAppearance().getColour()[2] = 150;
					player.getAppearance().getColour()[3] = 4;
				}else{
					player.getAppearance().getLook()[2] = 580;
					player.getAppearance().getLook()[3] = 416;
					player.getAppearance().getLook()[4] = 528;
					player.getAppearance().getLook()[5] = 496;
					player.getAppearance().getLook()[6] = 552;
					player.getAppearance().getColour()[1] = 149;
					player.getAppearance().getColour()[2] = 150;
					player.getAppearance().getColour()[3] = 4;
				}
			}
			break;
		case 86:
			if(player.getAttribute("charStage", 1) == 1){
				if(player.getAppearance().getGender() == 0){
					player.getAppearance().getLook()[2] = 473;
					player.getAppearance().getLook()[3] = 616;
					player.getAppearance().getLook()[4] = 392;
					player.getAppearance().getLook()[5] = 648;
					player.getAppearance().getLook()[6] = 441;
					player.getAppearance().getColour()[1] = 37;
					player.getAppearance().getColour()[2] = 213;
					player.getAppearance().getColour()[3] = 4;
				}else{
					player.getAppearance().getLook()[2] = 586;
					player.getAppearance().getLook()[3] = 423;
					player.getAppearance().getLook()[4] = 535;
					player.getAppearance().getLook()[5] = 503;
					player.getAppearance().getLook()[6] = 553;
					player.getAppearance().getColour()[1] = 53;
					player.getAppearance().getColour()[2] = 53;
					player.getAppearance().getColour()[3] = 155;
				}
			}
			break;
		case 87:
			if(player.getAttribute("charStage", 1) == 1){
				if(player.getAppearance().getGender() == 0){
					player.getAppearance().getLook()[2] = 444;
					player.getAppearance().getLook()[3] = 617;
					player.getAppearance().getLook()[4] = 393;
					player.getAppearance().getLook()[5] = 649;
					player.getAppearance().getLook()[6] = 441;
					player.getAppearance().getColour()[1] = 37;
					player.getAppearance().getColour()[2] = 213;
					player.getAppearance().getColour()[3] = 4;
				}else{
					player.getAppearance().getLook()[2] = 556;
					player.getAppearance().getLook()[3] = 424;
					player.getAppearance().getLook()[4] = 536;
					player.getAppearance().getLook()[5] = 504;
					player.getAppearance().getLook()[6] = 554;
					player.getAppearance().getColour()[1] = 45;
					player.getAppearance().getColour()[2] = 221;
					player.getAppearance().getColour()[3] = 4;
				}
			}
			break;
		case 88:
			if(player.getAttribute("charStage", 1) == 1){
				if(player.getAppearance().getGender() == 0){
					player.getAppearance().getLook()[2] = 474;
					player.getAppearance().getLook()[3] = 618;
					player.getAppearance().getLook()[4] = 394;
					player.getAppearance().getLook()[5] = 650;
					player.getAppearance().getLook()[6] = 441;
					player.getAppearance().getColour()[1] = 37;
					player.getAppearance().getColour()[2] = 213;
					player.getAppearance().getColour()[3] = 4;
				}else{
					player.getAppearance().getLook()[2] = 587;
					player.getAppearance().getLook()[3] = 425;
					player.getAppearance().getLook()[4] = 537;
					player.getAppearance().getLook()[5] = 505;
					player.getAppearance().getLook()[6] = 551;
					player.getAppearance().getColour()[1] = 53;
					player.getAppearance().getColour()[2] = 53;
					player.getAppearance().getColour()[3] = 4;
				}
			}
			break;
		case 49:
			player.setAttribute("charStage", 2);
			if(player.getAppearance().getGender() == 0){
				player.getAppearance().getLook()[2] = 453;
				player.getAppearance().getLook()[3] = 619;
				player.getAppearance().getLook()[4] = 380;
				player.getAppearance().getLook()[5] = 636;
				player.getAppearance().getLook()[6] = 429;
				player.getAppearance().getColour()[1] = 197;
				player.getAppearance().getColour()[2] = 202;
				player.getAppearance().getColour()[3] = 4;
			}else{
				player.getAppearance().getLook()[2] = 562;
				player.getAppearance().getLook()[3] = 425;
				player.getAppearance().getLook()[4] = 523;
				player.getAppearance().getLook()[5] = 491;
				player.getAppearance().getLook()[6] = 551;
				player.getAppearance().getColour()[1] = 197;
				player.getAppearance().getColour()[2] = 202;
				player.getAppearance().getColour()[3] = 4;
			}
			break;
		case 50:
			player.setAttribute("charStage", 3);
			if(player.getAppearance().getGender() == 0){
				player.getAppearance().getLook()[2] = 447;
				player.getAppearance().getLook()[4] = 386;
				player.getAppearance().getLook()[5] = 642;
				player.getAppearance().getLook()[6] = 429;
				player.getAppearance().getColour()[1] = 125;
				player.getAppearance().getColour()[2] = 125;
				player.getAppearance().getColour()[3] = 4;
			}else{
				player.getAppearance().getLook()[2] = 581;
				player.getAppearance().getLook()[3] = 417;
				player.getAppearance().getLook()[4] = 529;
				player.getAppearance().getLook()[5] = 497;
				player.getAppearance().getLook()[6] = 551;
				player.getAppearance().getColour()[1] = 125;
				player.getAppearance().getColour()[2] = 125;
				player.getAppearance().getColour()[3] = 4;
			}
			break;
		case 51:
			player.setAttribute("charStage", 4);
			if(player.getAppearance().getGender() == 0){
				player.getAppearance().getLook()[2] = 469;
				player.getAppearance().getLook()[3] = 607;
				player.getAppearance().getLook()[4] = 383;
				player.getAppearance().getLook()[5] = 639;
				player.getAppearance().getLook()[6] = 431;
				player.getAppearance().getColour()[1] = 149;
				player.getAppearance().getColour()[2] = 150;
				player.getAppearance().getColour()[3] = 4;
			}else{
				player.getAppearance().getLook()[2] = 579;
				player.getAppearance().getLook()[3] = 414;
				player.getAppearance().getLook()[4] = 526;
				player.getAppearance().getLook()[5] = 494;
				player.getAppearance().getLook()[6] = 551;
				player.getAppearance().getColour()[1] = 149;
				player.getAppearance().getColour()[2] = 149;
				player.getAppearance().getColour()[3] = 4;
			}
			break;
		case 52:
			player.setAttribute("charStage", 5);
			if(player.getAppearance().getGender() == 0){
				player.getAppearance().getLook()[2] = 451;
				player.getAppearance().getLook()[3] = 592;
				player.getAppearance().getLook()[4] = 378;
				player.getAppearance().getLook()[5] = 634;
				player.getAppearance().getLook()[6] = 429;
				player.getAppearance().getColour()[1] = 165;
				player.getAppearance().getColour()[2] = 165;
				player.getAppearance().getColour()[3] = 4;
			}else{
				player.getAppearance().getLook()[2] = 560;
				player.getAppearance().getLook()[3] = 416;
				player.getAppearance().getLook()[4] = 521;
				player.getAppearance().getLook()[5] = 489;
				player.getAppearance().getLook()[6] = 551;
				player.getAppearance().getColour()[1] = 165;
				player.getAppearance().getColour()[2] = 166;
				player.getAppearance().getColour()[3] = 142;
			}
			break;
		case 53:
			player.setAttribute("charStage", 6);
			if(player.getAppearance().getGender() == 0){
				player.getAppearance().getLook()[2] = 452;
				player.getAppearance().getLook()[3] = 592;
				player.getAppearance().getLook()[4] = 371;
				player.getAppearance().getLook()[5] = 627;
				player.getAppearance().getLook()[6] = 434;
				player.getAppearance().getColour()[1] = 189;
				player.getAppearance().getColour()[2] = 189;
				player.getAppearance().getColour()[3] = 39;
			}else{
				player.getAppearance().getLook()[2] = 561;
				player.getAppearance().getLook()[3] = 416;
				player.getAppearance().getLook()[4] = 514;
				player.getAppearance().getLook()[5] = 482;
				player.getAppearance().getLook()[6] = 545;
				player.getAppearance().getColour()[1] = 189;
				player.getAppearance().getColour()[2] = 189;
				player.getAppearance().getColour()[3] = 39;
			}
			break;
		case 54:
			player.setAttribute("charStage", 7);
			if(player.getAppearance().getGender() == 0){
				player.getAppearance().getLook()[2] = 464;
				player.getAppearance().getLook()[3] = 597;
				player.getAppearance().getLook()[4] = 373;
				player.getAppearance().getLook()[5] = 629;
				player.getAppearance().getLook()[6] = 435;
				player.getAppearance().getColour()[1] = 29;
				player.getAppearance().getColour()[2] = 30;
				player.getAppearance().getColour()[3] = 0;
			}else{
				player.getAppearance().getLook()[2] = 573;
				player.getAppearance().getLook()[3] = 404;
				player.getAppearance().getLook()[4] = 516;
				player.getAppearance().getLook()[5] = 484;
				player.getAppearance().getLook()[6] = 547;
				player.getAppearance().getColour()[1] = 29;
				player.getAppearance().getColour()[2] = 30;
				player.getAppearance().getColour()[3] = 0;
			}
			break;
		case 55:
			player.setAttribute("charStage", 8);
			if(player.getAppearance().getGender() == 0){
				player.getAppearance().getLook()[2] = 468;
				player.getAppearance().getLook()[3] = 603;
				player.getAppearance().getLook()[4] = 379;
				player.getAppearance().getLook()[5] = 635;
				player.getAppearance().getLook()[6] = 429;
				player.getAppearance().getColour()[1] = 117;
				player.getAppearance().getColour()[2] = 118;
				player.getAppearance().getColour()[3] = 4;
			}else{
				player.getAppearance().getLook()[2] = 578;
				player.getAppearance().getLook()[3] = 410;
				player.getAppearance().getLook()[4] = 522;
				player.getAppearance().getLook()[5] = 490;
				player.getAppearance().getLook()[6] = 550;
				player.getAppearance().getColour()[1] = 117;
				player.getAppearance().getColour()[2] = 118;
				player.getAppearance().getColour()[3] = 4;
			}
			break;
		case 56:
			player.setAttribute("charStage", 9);
			if(player.getAppearance().getGender() == 0){
				player.getAppearance().getLook()[2] = 458;
				player.getAppearance().getLook()[3] = 589;
				player.getAppearance().getLook()[4] = 365;
				player.getAppearance().getLook()[5] = 621;
				player.getAppearance().getLook()[6] = 428;
				player.getAppearance().getColour()[1] = 69;
				player.getAppearance().getColour()[2] = 69;
				player.getAppearance().getColour()[3] = 154;
			}else{
				player.getAppearance().getLook()[2] = 566;
				player.getAppearance().getLook()[3] = 396;
				player.getAppearance().getLook()[4] = 508;
				player.getAppearance().getLook()[5] = 476;
				player.getAppearance().getLook()[6] = 540;
				player.getAppearance().getColour()[1] = 69;
				player.getAppearance().getColour()[2] = 69;
				player.getAppearance().getColour()[3] = 154;
			}
			break;
		case 57:
			player.setAttribute("charStage", 10);
			if(player.getAppearance().getGender() == 0){
				player.getAppearance().getLook()[2] = 465;
				player.getAppearance().getLook()[3] = 599;
				player.getAppearance().getLook()[4] = 375;
				player.getAppearance().getLook()[5] = 631;
				player.getAppearance().getLook()[6] = 436;
				player.getAppearance().getColour()[1] = 205;
				player.getAppearance().getColour()[2] = 206;
				player.getAppearance().getColour()[3] = 4;
			}else{
				player.getAppearance().getLook()[2] = 575;
				player.getAppearance().getLook()[3] = 406;
				player.getAppearance().getLook()[4] = 518;
				player.getAppearance().getLook()[5] = 486;
				player.getAppearance().getLook()[6] = 551;
				player.getAppearance().getColour()[1] = 205;
				player.getAppearance().getColour()[2] = 206;
				player.getAppearance().getColour()[3] = 4;
			}
			break;
		case 58:
			player.setAttribute("charStage", 11);
			if(player.getAppearance().getGender() == 0){
				player.getAppearance().getLook()[2] = 460;
				player.getAppearance().getLook()[3] = 592;
				player.getAppearance().getLook()[4] = 368;
				player.getAppearance().getLook()[5] = 624;
				player.getAppearance().getLook()[6] = 431;
				player.getAppearance().getColour()[1] = 93;
				player.getAppearance().getColour()[2] = 94;
				player.getAppearance().getColour()[3] = 4;
			}else{
				player.getAppearance().getLook()[2] = 569;
				player.getAppearance().getLook()[3] = 399;
				player.getAppearance().getLook()[4] = 511;
				player.getAppearance().getLook()[5] = 479;
				player.getAppearance().getLook()[6] = 551;
				player.getAppearance().getColour()[1] = 93;
				player.getAppearance().getColour()[2] = 94;
				player.getAppearance().getColour()[3] = 4;
			}
			break;
		case 59:
			player.setAttribute("charStage", 12);
			if(player.getAppearance().getGender() == 0){
				player.getAppearance().getLook()[2] = 456;
				player.getAppearance().getLook()[3] = 589;
				player.getAppearance().getLook()[4] = 9;
				player.getAppearance().getLook()[5] = 651;
				player.getAppearance().getLook()[6] = 442;
				player.getAppearance().getColour()[1] = 141;
				player.getAppearance().getColour()[2] = 141;
				player.getAppearance().getColour()[3] = 118;
			}else{
				player.getAppearance().getLook()[2] = 558;
				player.getAppearance().getLook()[3] = 399;
				player.getAppearance().getLook()[4] = 538;
				player.getAppearance().getLook()[5] = 506;
				player.getAppearance().getLook()[6] = 555;
				player.getAppearance().getColour()[1] = 141;
				player.getAppearance().getColour()[2] = 141;
				player.getAppearance().getColour()[3] = 118;
			}
			break;
		case 60:
			player.setAttribute("charStage", 13);
			if(player.getAppearance().getGender() == 0){
				player.getAppearance().getLook()[2] = 459;
				player.getAppearance().getLook()[3] = 591;
				player.getAppearance().getLook()[4] = 367;
				player.getAppearance().getLook()[5] = 623;
				player.getAppearance().getLook()[6] = 430;
				player.getAppearance().getColour()[1] = 133;
				player.getAppearance().getColour()[2] = 134;
				player.getAppearance().getColour()[3] = 4;
			}else{
				player.getAppearance().getLook()[2] = 568;
				player.getAppearance().getLook()[3] = 398;
				player.getAppearance().getLook()[4] = 510;
				player.getAppearance().getLook()[5] = 478;
				player.getAppearance().getLook()[6] = 542;
				player.getAppearance().getColour()[1] = 133;
				player.getAppearance().getColour()[2] = 134;
				player.getAppearance().getColour()[3] = 4;
			}
			break;
		case 61:
			player.setAttribute("charStage", 14);
			if(player.getAppearance().getGender() == 0){
				player.getAppearance().getLook()[2] = 462;
				player.getAppearance().getLook()[3] = 594;
				player.getAppearance().getLook()[4] = 370;
				player.getAppearance().getLook()[5] = 626;
				player.getAppearance().getLook()[6] = 433;
				player.getAppearance().getColour()[1] = 85;
				player.getAppearance().getColour()[2] = 85;
				player.getAppearance().getColour()[3] = 69;
			}else{
				player.getAppearance().getLook()[2] = 571;
				player.getAppearance().getLook()[3] = 401;
				player.getAppearance().getLook()[4] = 513;
				player.getAppearance().getLook()[5] = 481;
				player.getAppearance().getLook()[6] = 544;
				player.getAppearance().getColour()[1] = 85;
				player.getAppearance().getColour()[2] = 86;
				player.getAppearance().getColour()[3] = 69;
			}
			break;
		case 62:
			player.setAttribute("charStage", 15);
			if(player.getAppearance().getGender() == 0){
				player.getAppearance().getLook()[2] = 467;
				player.getAppearance().getLook()[3] = 601;
				player.getAppearance().getLook()[4] = 377;
				player.getAppearance().getLook()[5] = 633;
				player.getAppearance().getLook()[6] = 438;
				player.getAppearance().getColour()[1] = 173;
				player.getAppearance().getColour()[2] = 174;
				player.getAppearance().getColour()[3] = 4;
			}else{
				player.getAppearance().getLook()[2] = 577;
				player.getAppearance().getLook()[3] = 408;
				player.getAppearance().getLook()[4] = 520;
				player.getAppearance().getLook()[5] = 488;
				player.getAppearance().getLook()[6] = 549;
				player.getAppearance().getColour()[1] = 173;
				player.getAppearance().getColour()[2] = 174;
				player.getAppearance().getColour()[3] = 4;
			}
			break;
		case 63:
			player.setAttribute("charStage", 16);
			if(player.getAppearance().getGender() == 0){
				player.getAppearance().getLook()[2] = 461;
				player.getAppearance().getLook()[3] = 593;
				player.getAppearance().getLook()[4] = 369;
				player.getAppearance().getLook()[5] = 625;
				player.getAppearance().getLook()[6] = 432;
				player.getAppearance().getColour()[1] = 77;
				player.getAppearance().getColour()[2] = 78;
				player.getAppearance().getColour()[3] = 4;
			}else{
				player.getAppearance().getLook()[2] = 570;
				player.getAppearance().getLook()[3] = 400;
				player.getAppearance().getLook()[4] = 512;
				player.getAppearance().getLook()[5] = 480;
				player.getAppearance().getLook()[6] = 543;
				player.getAppearance().getColour()[1] = 77;
				player.getAppearance().getColour()[2] = 78;
				player.getAppearance().getColour()[3] = 4;
			}
			break;
		case 64:
			player.setAttribute("charStage", 17);
			if(player.getAppearance().getGender() == 0){
				player.getAppearance().getLook()[2] = 445;
				player.getAppearance().getLook()[3] = 589;
				player.getAppearance().getLook()[4] = 366;
				player.getAppearance().getLook()[5] = 622;
				player.getAppearance().getLook()[6] = 429;
				player.getAppearance().getColour()[1] = 101;
				player.getAppearance().getColour()[2] = 102;
				player.getAppearance().getColour()[3] = 4;
			}else{
				player.getAppearance().getLook()[2] = 567;
				player.getAppearance().getLook()[3] = 397;
				player.getAppearance().getLook()[4] = 509;
				player.getAppearance().getLook()[5] = 477;
				player.getAppearance().getLook()[6] = 541;
				player.getAppearance().getColour()[1] = 101;
				player.getAppearance().getColour()[2] = 102;
				player.getAppearance().getColour()[3] = 4;
			}
			break;
		case 65:
			player.setAttribute("charStage", 18);
			if(player.getAppearance().getGender() == 0){
				player.getAppearance().getLook()[2] = 446;
				player.getAppearance().getLook()[3] = 599;
				player.getAppearance().getLook()[4] = 374;
				player.getAppearance().getLook()[5] = 630;
				player.getAppearance().getLook()[6] = 429;
				player.getAppearance().getColour()[1] = 109;
				player.getAppearance().getColour()[2] = 109;
				player.getAppearance().getColour()[3] = 4;
			}else{
				player.getAppearance().getLook()[2] = 574;
				player.getAppearance().getLook()[3] = 405;
				player.getAppearance().getLook()[4] = 517;
				player.getAppearance().getLook()[5] = 485;
				player.getAppearance().getLook()[6] = 548;
				player.getAppearance().getColour()[1] = 109;
				player.getAppearance().getColour()[2] = 109;
				player.getAppearance().getColour()[3] = 4;
			}
			break;
		case 66:
			player.setAttribute("charStage", 19);
			if(player.getAppearance().getGender() == 0){
				player.getAppearance().getLook()[2] = 466;
				player.getAppearance().getLook()[3] = 600;
				player.getAppearance().getLook()[4] = 376;
				player.getAppearance().getLook()[5] = 632;
				player.getAppearance().getLook()[6] = 437;
				player.getAppearance().getColour()[1] = 181;
				player.getAppearance().getColour()[2] = 181;
				player.getAppearance().getColour()[3] = 56;
			}else{
				player.getAppearance().getLook()[2] = 576;
				player.getAppearance().getLook()[3] = 407;
				player.getAppearance().getLook()[4] = 519;
				player.getAppearance().getLook()[5] = 487;
				player.getAppearance().getLook()[6] = 553;
				player.getAppearance().getColour()[1] = 181;
				player.getAppearance().getColour()[2] = 181;
				player.getAppearance().getColour()[3] = 158;
			}
			break;
		case 67:
			player.setAttribute("charStage", 20);
			if(player.getAppearance().getGender() == 0){
				player.getAppearance().getLook()[2] = 463;
				player.getAppearance().getLook()[3] = 596;
				player.getAppearance().getLook()[4] = 372;
				player.getAppearance().getLook()[5] = 628;
				player.getAppearance().getLook()[6] = 429;
				player.getAppearance().getColour()[1] = 157;
				player.getAppearance().getColour()[2] = 157;
				player.getAppearance().getColour()[3] = 134;
			}else{
				player.getAppearance().getLook()[2] = 572;
				player.getAppearance().getLook()[3] = 403;
				player.getAppearance().getLook()[4] = 515;
				player.getAppearance().getLook()[5] = 483;
				player.getAppearance().getLook()[6] = 546;
				player.getAppearance().getColour()[1] = 157;
				player.getAppearance().getColour()[2] = 157;
				player.getAppearance().getColour()[3] = 54;
			}
			break;
		case 117:
			InterfaceSettings.sendInterfaces(player);
			player.getMask().setApperanceUpdate(true);
			break;
		}
		return false;
	}

}
