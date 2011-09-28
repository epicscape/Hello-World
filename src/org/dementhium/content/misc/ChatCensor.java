package org.dementhium.content.misc;

import org.dementhium.model.player.Player;

/**
 * @author Lumby <lumbyjr@hotmail.com>
 */
public class ChatCensor {
	
	private static String[] profanityWords = {"fuck", "cock", "asshole","ass hole","nigger"};
	
	private static String[] censoredWords = {".com",".org",".net",".biz",".info", "ziotic", "lethium"};
	
	public static String checkMessage(Player player, String message){
		for(int i = 0; i < profanityWords.length; i++){
			if(message.contains(profanityWords[i])){
				message = message.replace(profanityWords[i], getStars(profanityWords[i].length()));
			}
		}
		for(int i = 0; i < censoredWords.length; i++){
			if(message.contains(censoredWords[i])){
				message = message.replace(censoredWords[i], getStars(censoredWords[i].length()));
			}
		}
		return message;
	}
	
	private static String getStars(int number){
		String stars = "";
		for(int i = 0; i < number; i++)
			stars += "*";
		return stars;
	}

}
