package org.dementhium.tools.funstuff;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class AutoSinger {

    private Robot robot;

    public static void main(String[] args) {
        new AutoSinger();
    }

    public AutoSinger() {
        try {
            new Scanner(System.in).nextLine();
            robot = new Robot();

            List<String> lyrics = readLyrics("data/randomshit/lyrics.txt");

            for (String string : lyrics) {
                string = string.toUpperCase();
                for (char c : string.toCharArray()) {
                    try {
                        boolean upper = Character.isUpperCase(c);
                        KeyStroke keycode = KeyStroke.getKeyStroke(c, upper ? 1 : 0);
                        robot.keyPress(keycode.getKeyCode());
                        robot.keyRelease(keycode.getKeyCode());
                        
                    } catch (java.lang.IllegalArgumentException ex) {
                        continue;
                    }
                }
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);
                Thread.sleep(2000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> readLyrics(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        LinkedList<String> list = new LinkedList<String>();
        String string;
        while ((string = reader.readLine()) != null) {
            if (string.length() > 0) {
                list.add(string.trim());
            }
        }
        return list;
    }

}
