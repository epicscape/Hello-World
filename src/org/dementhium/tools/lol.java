package org.dementhium.tools;

import java.io.IOException;
import java.util.LinkedList;

import org.dementhium.io.FileUtilities;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 * @author Steve <golden_32@live.com>
 * @author Lumby <lumbyjr@hotmail.com>
 * @author Emperor <black_dragon_686@hotmail.com>
 */
public class lol {
	
	public static void main(String[] args) throws IOException {
		LinkedList<String> content = FileUtilities.readFile("data/randomshit/content.txt");
		for (String s : content) {
			if (s.contains("[img]")) {
				s = s.replace("[img]", "");
				s = s.replace("[/img]", "");
				System.out.print(s + " ");
			}
		}
	}
}
