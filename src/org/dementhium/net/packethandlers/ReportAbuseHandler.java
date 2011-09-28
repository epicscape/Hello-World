package org.dementhium.net.packethandlers;

import org.dementhium.model.player.Player;
import org.dementhium.net.PacketHandler;
import org.dementhium.net.message.Message;

/**
 *
 * @author Lumby <lumbyjr@hotmail.com>
 */
public class ReportAbuseHandler extends PacketHandler {

	@Override
	public void handlePacket(Player player, Message packet) {
		//int reportLocation = packet.readByte();
		String offender = packet.readRS2String();
		int offense = packet.readByte();
		packet.readByte();
		packet.readRS2String();
		System.out.println("Report: Offender: "+offender+" Offense: "+offense+" Reported By: "+player.getUsername());
		
		switch(offense){
		case 6://Buying or selling account
		case 9://encourage rule breaking
		case 5://staff impersonation
		case 7://macroing/use of bots
		case 15://scamming
		case 4://Exploiting a bug
		case 16://seriously offensive language
		case 17://solicitation
		case 18://Disruptive behaviour
		case 19://offensive account name
		case 20://real life threats
		case 13://asking for real life info
		case 21://breaking real world laws
		case 11://advertising websites
			break;
		}
	}

}
