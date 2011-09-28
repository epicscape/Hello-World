package org.dementhium.content;

import org.dementhium.util.MapXTEA;

import java.util.ArrayList;
import java.util.List;

public class AreaManager {

    private List<Integer> available = new ArrayList<Integer>();
    private List<Integer> inUse = new ArrayList<Integer>();
    //private final Map<Integer, Integer[]> map = new HashMap<Integer, Integer[]>();

    public AreaManager() {
        for (int x = 0; x < 1200; x++) { //finds 96*96 empty spaced regions
            for (int y = 0; y < 1000; y++) {
            	int region = y | x << 8;
                if (!regionExists(region)) {
                    //boolean add = true;
                    /*for (int xx = 0; xx < 13; xx++) {
                        if (!add) break;
                        for (int yy = 0; yy < 13; yy++) {
                            int region2 = (x * (8 * xx) >> 3) | (y * (8 * yy) >> 3) << 8;
                            if (regionExists(region2)) {
                                add = false;
                                break;
                            }
                        }
                    */
                    //if (add) {
                        available.add(region);
                    //}
                }
            }
        }
        /*
		if(available.size() < 1) return;
		for(int i = 0; i < available.size(); i++) { //cleans up overlaying areas
			int region = available.get(i);
			if(region < 1) continue;
			int x = region >> 8;
			int y = region & 0xff;
			for(int xx = 0; xx < 13; xx++) {
				for(int yy = 0; yy < 13; yy++) {
					int region2 = (x*(8 * xx) >> 3) | (y*(8 * yy) >> 3) << 8;
					if(available.contains(region2)) {
						available.remove(region2);
					}
				}
			}
		}
        */
        System.out.println("Number of available regions - " + available.size());
    }

    private boolean regionExists(int region) {
        int[] keys = MapXTEA.getKey(region);
        if (keys == null) {
            return false;
        }
        return true;
    }

    public int getArea(int size) {
        int area = available.get(0);
        if (area == -1) {
            return -1;
        }
        inUse.add(area);
        available.remove(area);
        return area;
    }

    public void recycleArea(int area) {
        inUse.remove(area);
        available.add(area);
    }

}
