package org.dementhium.util;

import org.dementhium.model.Container;
import org.dementhium.model.player.Player;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * @author Lumby <lumbyjr@hotmail.com>
 */
public class Logger {

    public static void writeTradeLog(Player trader1, Player trader2, Container traderItems1, Container traderItems2) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("./data/trade_logs.txt", true));
            bw.write("\n---------------------------------------------------------");
            bw.write("\n " + trader1.getUsername() + " is trading with " + trader2.getUsername());
            for (int i = 0; i < traderItems1.size(); i++) {
                bw.write("\n Item_" + i + " Id: " + traderItems1.get(i).getDefinition().getId() + " Name: " + traderItems1.get(i).getDefinition().getName());
                bw.write("\n Item_" + i + " Amount: " + traderItems1.get(i).getAmount());
            }
            for (int i = 0; i < traderItems2.size(); i++) {
                bw.write("\n Item_" + i + " Id: " + traderItems2.get(i).getDefinition().getId() + " Name: " + traderItems2.get(i).getDefinition().getName());
                bw.write("\n Item_" + i + " Amount: " + traderItems2.get(i).getAmount());
            }
            bw.write("\n");
            bw.write("\n");
            bw.flush();
            bw.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}
