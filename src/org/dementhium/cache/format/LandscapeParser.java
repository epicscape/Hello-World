package org.dementhium.cache.format;

import org.dementhium.cache.CacheContainer;
import org.dementhium.cache.CacheManager;
import org.dementhium.cache.stream.ByteInputStream;
import org.dementhium.cache.stream.RSInputStream;
import org.dementhium.model.map.Region;
import org.dementhium.util.XTEA;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class LandscapeParser {

    private static Map<Integer, Boolean> loaded = new HashMap<Integer, Boolean>();
    private static Map<Integer, Boolean> broken = new HashMap<Integer, Boolean>();

    public static boolean parseLandscape(final int area, final int[] keys) {
        if (broken.containsKey(area)) {
            return false;
        }
        if (loaded.get(area) == Boolean.TRUE) {
            return true;
        }
        loaded.put(area, Boolean.TRUE);
        int regionX = (area >> 8);
        int regionY = (area & 0xFF);
        try {
            byte[] landscapeMap = CacheManager.getByName(5, "m" + regionX + "_" + regionY);
            byte[] objectMap = CacheManager.getByName(5, "l" + regionX + "_" + regionY);
            if (landscapeMap == null && objectMap == null) {
                System.out.println("Map [" + (regionX << 6) + ", " 	+ (regionY << 6) + "] was not found in the cache!");
                return true;
            }
            RSInputStream str2 = null;
            ByteInputStream landStream = null;
            if (landscapeMap != null) {
                str2 = new RSInputStream(new ByteArrayInputStream(new CacheContainer(landscapeMap).decompress()));
            }
            if (objectMap != null) {
                if (keys != null) {
                    objectMap = XTEA.decrypt(keys, objectMap, 5, objectMap.length);
                }
                landStream = new ByteInputStream(new CacheContainer(objectMap).decompress());
            }
            int x = regionX << 6;
            int y = regionY << 6;
            byte[][][] landscapeData = new byte[4][64][64];
            if (str2 != null) {
                for (int z = 0; z < 4; z++) {
                    for (int localX = 0; localX < 64; localX++) {
                        for (int localY = 0; localY < 64; localY++) {
                            while (true) {
                                int v = str2.readByte() & 0xff;
                                if (v == 0) {
                                    break;
                                } else if (v == 1) {
                                    str2.readByte();
                                    break;
                                } else if (v <= 49) {
                                    str2.readByte();
                                } else if (v <= 81) {
                                    landscapeData[z][localX][localY] = (byte) (v - 49);
                                }
                            }
                        }
                    }
                }
                for (int z = 0; z < 4; z++) {
                    for (int localX = 0; localX < 64; localX++) {
                        for (int localY = 0; localY < 64; localY++) {
                            if ((landscapeData[z][localX][localY] & 1) == 1) {
                                int height = z;
                                if ((landscapeData[1][localX][localY] & 2) == 2) {
                                    height--;
                                }
                                if (height >= 0 && height <= 3) {
                                    Region.addClipping(x + localX, y + localY, height, 0x200000);
                                }
                            }
                        }
                    }
                }
            }
            if (landStream != null) {
                int objectId = -1;
                int incr;
                if (!(landStream.buffer.length>=1))
                	return true;
                while ((incr = landStream.readSmart2()) != 0) {
                    objectId += incr;
                    int location = 0;
                    int incr2;
                    while ((incr2 = landStream.readUnsignedSmart()) != 0) {
                        location += incr2 - 1;
                        int localX = location >> 6 & 0x3f;
                        int localY = location & 0x3f;
                        int height = location >> 12;
                        int objectData = landStream.readUnsignedByte();
                        int type = objectData >> 2;
                        int rotation = objectData & 0x3;
                        if (localX < 0 || localX >= 64 || localY < 0 || localY >= 64) {
                            continue;
                        }
                        if ((landscapeData[1][localX][localY] & 2) == 2) {
                            height--;
                        }
                        if (height >= 0 && height <= 3) {
                            Region.addObject(objectId, x + localX, y + localY, height, type, rotation, true);
                        }
                    }
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println("Error while loading region " + area + ", " + e.getMessage() + ", " + e.getCause() + ", " + e.toString());
            e.printStackTrace();
            broken.put(area, Boolean.TRUE);
            return false;
        }
    }

    private static Map<Integer, Boolean> dynamicLoaded = new HashMap<Integer, Boolean>();
    private static Map<Integer, Boolean> dynamicBroken = new HashMap<Integer, Boolean>();

    public static boolean parseDynamicLandscape(final int baseX, final int baseY, final int area, final int[] keys) {
        if (dynamicBroken.containsKey(area)) {
            return false;
        }
        if (dynamicLoaded.get(area) == Boolean.TRUE) {
            return true;
        }
        dynamicLoaded.put(area, Boolean.TRUE);
        int regionX = area >> 8;
        int regionY = area & 0xFF;
        try {
            byte[] landscapeMap = CacheManager.getByName(5, "m" + regionX + "_" + regionY);
            byte[] objectMap = CacheManager.getByName(5, "l" + regionX + "_" + regionY);
            if (landscapeMap == null && objectMap == null) {
                return true;
            }
            RSInputStream str2 = null;
            ByteInputStream str1 = null;
            if (landscapeMap != null) {
                str2 = new RSInputStream(new ByteArrayInputStream(new CacheContainer(landscapeMap).decompress()));
            }
            if (objectMap != null) {
                if (keys != null) {
                    objectMap = XTEA.decrypt(keys, objectMap, 5, objectMap.length);
                }
                str1 = new ByteInputStream(new CacheContainer(objectMap).decompress());
            }
            byte[][][] landscapeData = new byte[4][64][64];
            if (str2 != null) {
                for (int z = 0; z < 4; z++) {
                    for (int localX = 0; localX < 64; localX++) {
                        for (int localY = 0; localY < 64; localY++) {
                            while (true) {
                                int v = str2.readByte() & 0xff;
                                if (v == 0) {
                                    break;
                                } else if (v == 1) {
                                    str2.readByte();
                                    break;
                                } else if (v <= 49) {
                                    str2.readByte();
                                } else if (v <= 81) {
                                    landscapeData[z][localX][localY] = (byte) (v - 49);
                                }
                            }
                        }
                    }
                }
                for (int z = 0; z < 4; z++) {
                    for (int localX = 0; localX < 64; localX++) {
                        for (int localY = 0; localY < 64; localY++) {
                            if ((landscapeData[z][localX][localY] & 1) == 1) {
                                int height = z;
                                if ((landscapeData[1][localX][localY] & 2) == 2) {
                                    height--;
                                }
                                if (height >= 0 && height <= 3) {
                                    Region.addClipping(baseX + localX, baseY + localY, height, 0x200000);
                                }
                            }
                        }
                    }
                }
            }
            if (str1 != null) {
                int objectId = -1;
                int incr;
                while ((incr = str1.readSmart2()) != 0) {
                    objectId += incr;
                    int location = 0;
                    int incr2;
                    while ((incr2 = str1.readSmart()) != 0) {
                        location += incr2 - 1;
                        int localX = location >> 6 & 0x3f;
                        int localY = location & 0x3f;
                        int height = location >> 12;
                        int objectData = str1.readUByte();
                        int type = objectData >> 2;
                        int rotation = objectData & 0x3;
                        if (localX < 0 || localX >= 64 || localY < 0 || localY >= 64) {
                            continue;
                        }
                        if ((landscapeData[1][localX][localY] & 2) == 2) {
                            height--;
                        }
                        if (height >= 0 && height <= 3) {
                            Region.addObject(objectId, baseX + localX, baseY + localY, height, type, rotation, true);
                        }
                    }
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println("Error while loading region " + area + ", " + e.getMessage() + ", " + e.getCause() + ", " + e.toString());
            broken.put(area, Boolean.TRUE);
            return false;
        }
    }
}
