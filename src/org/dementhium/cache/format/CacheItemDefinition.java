package org.dementhium.cache.format;

import org.dementhium.cache.CacheConstants;
import org.dementhium.cache.CacheManager;
import org.dementhium.util.BufferUtils;

import java.nio.ByteBuffer;
import java.util.HashMap;

public class CacheItemDefinition {

    private int id;
    private boolean loaded;

    private int interfaceModelId;
    private String name;

    private int modelZoom;
    private int modelRotation1;
    private int modelRotation2;
    private int modelOffset1;
    private int modelOffset2;

    private int stackable;
    private int value;
    private boolean membersOnly;

    private int maleWornModelId1 = -1;
    private int femaleWornModelId1;
    private int maleWornModelId2 = -1;
    private int femaleWornModelId2;

    private String[] groundOptions;
    private String[] inventoryOptions;

    private int[] originalModelColors;
    private int[] modifiedModelColors;
    private int[] textureColour1;
    private int[] textureColour2;
    private byte[] unknownArray1;
    private int[] unknownArray2;

    private boolean unnoted;

    private int colourEquip1;
    private int colourEquip2;
    private int certId;
    private int certTemplateId;
    private int[] stackIds;
    private int[] stackAmounts;
    private int teamId;
    private int lendId;
    private int lendTemplateId;

    private static HashMap<Integer, Object> clientScriptData;

    public static CacheItemDefinition getItemDefinition(int itemId) {
        CacheItemDefinition def = new CacheItemDefinition(itemId);
        def.loadItemDefinition();
        return def;
    }

    public CacheItemDefinition(int id) {
        this.id = id;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void loadItemDefinition() {
        setDefaultsVariableValules();
        setDefaultOptions();
        byte[] is = null;
        try {
            is = CacheManager.getData(CacheConstants.ITEMDEF_IDX_ID, id >>> 8, id & 0xFF);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Item " + id + " doesn't exist in the cache!");
        }
        if (is != null) {
            try {
                readOpcodeValues(ByteBuffer.wrap(is));
            } catch (Exception e) {
                System.out.println("Error while reading " + id);
            }
        }
        loaded = true;
    }

    public boolean canEquip() {
        return maleWornModelId1 >= 0 || maleWornModelId2 >= 0;
    }

    public boolean hasSpecialBar() {
        if (clientScriptData == null)
            return false;
        Object specialBar = clientScriptData.get(687);
        return specialBar instanceof Integer && (Integer) specialBar == 1;
    }

    public int getGroupId() {
        if (clientScriptData == null)
            return 0;
        Object specialBar = clientScriptData.get(686);
        if (specialBar instanceof Integer)
            return (Integer) specialBar;
        return 0;
    }

    public int getRenderAnimId() {
        if (clientScriptData == null)
            return 1426;
        Object animId = clientScriptData.get(644);
        if (animId instanceof Integer)
            return (Integer) animId;
        return 1426;
    }

    public int getQuestId() {
        if (clientScriptData == null)
            return -1;
        Object questId = clientScriptData.get(861);
        if (questId != null && questId instanceof Integer)
            return (Integer) questId;
        return -1;
    }

    public void test() {
        if (clientScriptData == null) {
            return;
        }
        for (Object o : clientScriptData.entrySet()) {
            System.out.println(o);
        }
    }

    public HashMap<Integer, Integer> getWearingSkillRequiriments() {
        if (clientScriptData == null)
            return null;
        HashMap<Integer, Integer> skills = new HashMap<Integer, Integer>();
        int nextLevel = -1;
        int nextSkill = -1;
        for (int key : clientScriptData.keySet()) {
            Object value = clientScriptData.get(key);
            if (value instanceof String)
                continue;
            if (key >= 749 && key < 797) {
                if (key % 2 == 0)
                    nextLevel = (Integer) value;
                else
                    nextSkill = (Integer) value;
                if (nextLevel != -1 && nextSkill != -1) {
                    skills.put(nextSkill, nextLevel);
                    nextLevel = -1;
                    nextSkill = -1;
                }
            }
        }
        return skills;
    }

    private void setDefaultsVariableValules() {

    }

    private void setDefaultOptions() {
        groundOptions = new String[]{null, null, "take", null, null};
        inventoryOptions = new String[]{null, null, null, null, "drop"};
    }

    private void readValues(ByteBuffer buffer, int opcode) {
        switch (opcode) {
            case 4:
                modelZoom = buffer.getShort() & 0xFFFF;
                break;
            case 5:
                modelRotation1 = buffer.getShort() & 0xFFFF;
                break;
            case 6:
                modelRotation2 = buffer.getShort() & 0xFFFF;
                break;
            case 11:
                stackable = 1;
                break;
            case 12:
                value = buffer.getInt();
                break;
            case 23:
                maleWornModelId1 = buffer.getShort() & 0xFFFF;
                break;
            case 25:
                maleWornModelId2 = buffer.getShort() & 0xFFFF;
                break;
            case 26:
                femaleWornModelId2 = buffer.getShort() & 0xFFFF;
                break;
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
                inventoryOptions[opcode - 35] = BufferUtils.readRS2String(buffer);
                break;
            case 40: {
                int length = buffer.get() & 0xFF;
                originalModelColors = new int[length];
                modifiedModelColors = new int[length];
                for (int index = 0; index < length; index++) {
                    originalModelColors[index] = buffer.getShort() & 0xFFFF;
                    modifiedModelColors[index] = buffer.getShort() & 0xFFFF;
                }
                break;
            }
            case 41: {
                int length = buffer.get() & 0xFF;
                textureColour1 = new int[length];
                textureColour2 = new int[length];
                for (int index = 0; index < length; index++) {
                    textureColour1[index] = buffer.getShort() & 0xFFFF;
                    textureColour2[index] = buffer.getShort() & 0xFFFF;
                }
                break;
            }
            case 42: {
                int length = buffer.get() & 0xFF;
                unknownArray1 = new byte[length];
                for (int index = 0; index < length; index++)
                    unknownArray1[index] = buffer.get();
                break;
            }
            case 65:
                unnoted = true;
                break;
            case 78:
                colourEquip1 = buffer.getShort() & 0xFFFF;
                break;
            case 79:
                colourEquip2 = buffer.getShort() & 0xFFFF;
                break;
            case 91:
                buffer.getShort();
                break;
            case 98:
                certTemplateId = buffer.getShort() & 0xFFFF;
                break;
            case 110:
                buffer.getShort();
                break;
            case 111:
                buffer.getShort();
                break;
            case 115:
                teamId = buffer.get() & 0xFF;
                break;
            case 122:
                lendTemplateId = buffer.getShort() & 0xFFFF;
                break;
            case 130:
                buffer.get();
                buffer.getShort();
                break;
            case 139:
                buffer.getShort();
                break;
            case 249: {
                int length = buffer.get() & 0xFF;
                if (clientScriptData == null) {
                    clientScriptData = new HashMap<Integer, Object>();
                }
                for (int index = 0; index < length; index++) {
                    boolean stringInstance = buffer.get() == 1;
                    int key = BufferUtils.getMediumInt(buffer);
                    Object value = stringInstance ? BufferUtils.readRS2String(buffer) : buffer.getInt();
                    clientScriptData.put(key, value);
                }
                break;
            }
            case 140:
                buffer.getShort();
                break;
            case 134:
                buffer.get();
                break;
            case 132: {
                int length = buffer.get() & 0xFF;
                unknownArray2 = new int[length];
                for (int index = 0; index < length; index++) {
                    unknownArray2[index] = buffer.getShort() & 0xFFFF;
                }
                break;
            }
            case 129:
            case 128:
            case 127:
                buffer.get();
                buffer.getShort();
                break;
            case 126:
            case 125:
                buffer.get();
                buffer.get();
                buffer.get();
                break;
            case 121:
                lendId = buffer.getShort() & 0xFFFF;
                break;
            case 114:
                buffer.get();
                break;
            case 113:
                buffer.get();
                break;
            case 112:
                buffer.getShort();
                break;
            case 100:
            case 101:
            case 102:
            case 103:
            case 104:
            case 105:
            case 106:
            case 107:
            case 108:
            case 109:
                if (stackIds == null) {
                    stackIds = new int[10];
                    stackAmounts = new int[10];
                }
                stackIds[opcode - 100] = buffer.getShort() & 0xFFFF;
                stackAmounts[opcode - 100] = buffer.getShort() & 0xFFFF;
                break;
            case 96:
                buffer.get();
                //certId = buffer.getShort();
                break;
            case 97:
                certTemplateId = buffer.getShort();
                break;
            case 95:
            case 93:
            case 92:
            case 90: //unknown
                buffer.getShort();
                break;
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
                groundOptions[opcode - 30] = BufferUtils.readRS2String(buffer);
                break;
            case 24:
                femaleWornModelId1 = buffer.getShort();
                break;
            case 18:
                buffer.getShort();
                break;
            case 16:
                membersOnly = true;
                break;
            case 8:
                modelOffset2 = buffer.getShort() & 0xFFFF;
                if (modelOffset2 > 32767)
                    modelOffset2 -= 65536;
                modelOffset2 <<= 0;
                break;
            case 7:
                modelOffset1 = buffer.getShort() & 0xFFFF;
                if (modelOffset1 > 32767)
                    modelOffset1 -= 65536;
                modelOffset1 <<= 0;
                break;
            case 2:
                name = BufferUtils.readRS2String(buffer);
                break;
            case 1:
                interfaceModelId = buffer.getShort() & 0xFFFF;
                break;
        }
    }

    private void readOpcodeValues(ByteBuffer buffer) {
        while (true) {
            int opcode = buffer.get() & 0xFF;
            if (opcode == 0)
                break;
            readValues(buffer, opcode);
        }
    }

    public int getId() {
        return id;
    }

    public int getInterfaceModelId() {
        return interfaceModelId;
    }

    public String getName() {
        return name;
    }

    public int getModelZoom() {
        return modelZoom;
    }

    public int getModelRotation1() {
        return modelRotation1;
    }

    public int getModelRotation2() {
        return modelRotation2;
    }

    public int getModelOffset1() {
        return modelOffset1;
    }

    public int getModelOffset2() {
        return modelOffset2;
    }

    public int getStackable() {
        return stackable;
    }

    public int getValue() {
        return value;
    }

    public boolean isMembersOnly() {
        return membersOnly;
    }

    public int getMaleWornModelId1() {
        return maleWornModelId1;
    }

    public int getFemaleWornModelId1() {
        return femaleWornModelId1;
    }

    public int getMaleWornModelId2() {
        return maleWornModelId2;
    }

    public int getFemaleWornModelId2() {
        return femaleWornModelId2;
    }

    public String[] getGroundOptions() {
        return groundOptions;
    }

    public String[] getInventoryOptions() {
        return inventoryOptions;
    }

    public int[] getOriginalModelColors() {
        return originalModelColors;
    }

    public int[] getModifiedModelColors() {
        return modifiedModelColors;
    }

    public int[] getTextureColour1() {
        return textureColour1;
    }

    public int[] getTextureColour2() {
        return textureColour2;
    }

    public byte[] getUnknownArray1() {
        return unknownArray1;
    }

    public int[] getUnknownArray2() {
        return unknownArray2;
    }

    public boolean isUnnoted() {
        return unnoted;
    }

    public int getColourEquip1() {
        return colourEquip1;
    }

    public int getColourEquip2() {
        return colourEquip2;
    }

    public int getCertId() {
        return certId;
    }

    public int getCertTemplateId() {
        return certTemplateId;
    }

    public int[] getStackIds() {
        return stackIds;
    }

    public int[] getStackAmounts() {
        return stackAmounts;
    }

    public int getTeamId() {
        return teamId;
    }

    public int getLendId() {
        return lendId;
    }

    public int getLendTemplateId() {
        return lendTemplateId;
    }
}
