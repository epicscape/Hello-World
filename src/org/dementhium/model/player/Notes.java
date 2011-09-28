package org.dementhium.model.player;

import org.dementhium.net.ActionSender;

import java.util.ArrayList;

/**
 * @author Steve <golden_32@live.com>
 * @author Lumby <lumbyjr@hotmail.com>
 */
public class Notes {


    public class Note {

        private int color = 0;
        private String text = "";

        public Note(int color, String text) {
            this.setColor(color);
            this.setText(text);
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public int getColor() {
            return color;
        }
    }

    private ArrayList<Note> notes = new ArrayList<Note>(30);
    private Player player;

    public Notes(Player p) {
        this.player = p;
    }

    public void addNote(String text) {
        if (text.length() > 50) {
            player.sendMessage("You can only enter notes up to 50 characters!");
            return;
        }
        if (notes.size() < 30) {
            notes.add(new Note(0, text));
        } else {
            player.sendMessage("You cannot add more then 30 notes!");
            return;
        }
        int NoteId = notes.size() - 1;
        ActionSender.sendConfig(player, 1439, NoteId);
        player.setAttribute("selectedNote", NoteId);
        refreshNotes(false);
    }

    public void addNote(String text, int color) {
        notes.add(new Note(color, text));
    }

    public void loadNotes() {
        ActionSender.sendAMask(player, 2621470, 34, 9, 0, 29);
        ActionSender.sendString(player, "Loading notes<br>Please wait...", 34, 13);
        ActionSender.sendInterfaceConfig(player, 34, 13, true);
        ActionSender.sendInterfaceConfig(player, 34, 3, true);
        ActionSender.sendConfig(player, 1439, -1);
        refreshNotes(true);
    }

    public void refreshNotes(boolean sendStartConfigs) {
        for (int i = 0; i < 30; i++) {
            ActionSender.sendSpecialString(player, 149 + i, i < notes.size() ? notes.get(i).getText() : "");
        }
        if (sendStartConfigs) {
            for (int i = 1430; i < 1450; i++)
                ActionSender.sendConfig(player, i, i);
        }
        ActionSender.sendConfig(player, 1440, getFirstTotalColorValue());
        ActionSender.sendConfig(player, 1441, getSecondTotalColorValue());
    }


    public int intColorValue(int color, int noteId) {
        return (int) (Math.pow(4, noteId) * color);
    }

    public int getFirstTotalColorValue() {
        int Color = 0;
        for (int i = 0; i < 15; i++) {
            if (notes.size() > i)
                Color += intColorValue(notes.get(i).getColor(), i);
        }
        return Color;
    }

    public int getSecondTotalColorValue() {
        int color = 0;
        for (int i = 0; i < 15; i++) {
            if (notes.size() > (i + 16))
                color += intColorValue(notes.get(i + 16).getColor(), i);
        }
        return color;
    }

    public void deleteSelectedNote() {
        if (player.getAttribute("selectedNote", -1) > -1) {
            int slot = player.getAttribute("selectedNote", -1);
            notes.remove(slot);
            player.setAttribute("selectedNote", -1);
            ActionSender.sendConfig(player, 1439, -1);
            refreshNotes(false);
        }
    }

    public void clear() {
        notes.clear();
        refreshNotes(false);
    }

    public void editNote(String string, Integer attribute) {
        notes.get(attribute).setText(string);
        refreshNotes(false);
    }

    public void setColor(int color, Integer attribute) {
        notes.get(attribute).setColor(color);
        refreshNotes(false);

    }

    public void deleteNote(int slot) {
        notes.remove(slot);
        refreshNotes(false);

    }

    public void setNotes(ArrayList<Note> setNotes) {
        notes = setNotes;
        refreshNotes(false);
    }

    public ArrayList<Note> getList() {
        return notes;
    }


}
