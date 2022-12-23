package com.fourshape.numbermatch.puzzle_core.structure;

import com.fourshape.numbermatch.puzzle_core.GameLevel;
import com.fourshape.numbermatch.utils.MakeLog;
import com.fourshape.numbermatch.utils.RandNumber;

import java.util.ArrayList;

public class SetCollection {

    private static final String TAG = "SetSelection";

    private int setId, setIncrementLevel, gameLevel;
    private static final int TOTAL_SETS = 350;

    public SetCollection () {}

    public int getSetId() {
        return setId;
    }

    public int getSetIncrementLevel() {
        return setIncrementLevel;
    }

    public static int getTotalSets() {
        return TOTAL_SETS;
    }

    public void setGameLevel (int gameLevel) {
        this.gameLevel = gameLevel;
    }

    public void setLastSetId (int lastSetId) {
        this.setId = lastSetId;
    }

    public void setLastSetIncrementLevel (int lastSetIncrementLevel) {
        setIncrementLevel = lastSetIncrementLevel;
    }

    public int[] getSet () {

        int[] set;

        int itr = 0;

        do {

            if (setId > TOTAL_SETS) {
                setId = 0;
            } else if (setId < 0) {
                if (setIncrementLevel > 8) {
                    setIncrementLevel = 0;
                    setId++;
                } else {
                    setId++;
                    setIncrementLevel++;
                }
            } else {
                if (setIncrementLevel > 8) {
                    setIncrementLevel = 0;
                    setId++;
                } else {
                    setIncrementLevel++;
                }
            }

            set = getOriginalSet(setId);
            itr++;

            if (itr > 5)
                break;

            MakeLog.info(TAG, "Loop iterated.");

        } while (!isValidSet(getOriginalSet(setId)));

        if (set == null) {
            setId = 1;
            set = getOriginalSet(setId);
        }

        for (int i=0; i<set.length; i++) {
            set[i] = getOptimizedValue(set[i], setIncrementLevel);
        }

        /*
        Easy - remove 30%
        Medium - remove half
        Hard - remove 75%
         */

        Cell[][] tempSet = new Cell[3][9];
        int index = 0;

        // fit set into tempset
        for (int row=0; row<tempSet.length; row++) {
            for (int col=0; col<tempSet[row].length; col++) {
                tempSet[row][col] = new Cell(row, col, set[index], false);
                index++;
            }
        }

        int removeLimit = 0, totalRemoved = 0;

        // get data first
        ArrayList<CellRC> cellRCArrayList = new ArrayList<>();
        for (int row=0; row<tempSet.length; row++) {
            for (int col=0; col<tempSet[row].length; col++) {

                // Check for horizontal
                boolean hCheck = isValidRC(row, col+1, tempSet) &&
                        (tempSet[row][col+1].getValue() == tempSet[row][col].getValue() || tempSet[row][col+1].getValue()+tempSet[row][col].getValue()==10)
                        &&
                        (!tempSet[row][col+1].isSolved() && !tempSet[row][col].isSolved());

                if (hCheck) {

                    tempSet[row][col].setSolved(true);
                    tempSet[row][col+1].setSolved(true);
                    cellRCArrayList.add(new CellRC(row, col));

                }

                // Check for vertical
                boolean vCheck = isValidRC(row+1, col, tempSet) &&
                        (tempSet[row+1][col].getValue() == tempSet[row][col].getValue() || tempSet[row+1][col].getValue()+tempSet[row][col].getValue()==10)
                        &&
                        (!tempSet[row+1][col].isSolved() && !tempSet[row][col].isSolved());

                if (vCheck) {

                    tempSet[row][col].setSolved(true);
                    tempSet[row+1][col].setSolved(true);
                    cellRCArrayList.add(new CellRC(row, col));

                }

                // Check for diagonal - right-bottom
                boolean dRBCheck = isValidRC(row+1, col+1, tempSet) &&
                        (tempSet[row+1][col+1].getValue() == tempSet[row][col].getValue() || tempSet[row+1][col+1].getValue()+tempSet[row][col].getValue() == 10)
                        &&
                        (!tempSet[row+1][col+1].isSolved() && !tempSet[row][col].isSolved());

                if (dRBCheck) {
                    tempSet[row][col].setSolved(true);
                    tempSet[row+1][col+1].setSolved(true);
                    cellRCArrayList.add(new CellRC(row, col));
                }

                // Check for diagonal - right-top
                boolean dRTCheck = isValidRC(row-1, col+1, tempSet) &&
                        (tempSet[row-1][col+1].getValue() == tempSet[row][col].getValue() || tempSet[row-1][col+1].getValue()+tempSet[row][col].getValue() == 10)
                        &&
                        (!tempSet[row-1][col+1].isSolved() && !tempSet[row][col].isSolved());

                if (dRTCheck) {
                    tempSet[row][col].setSolved(true);
                    tempSet[row-1][col+1].setSolved(true);
                    cellRCArrayList.add(new CellRC(row, col));
                }

                // Check for diagonal - left-bottom
                boolean dLBCheck = isValidRC(row+1, col-1, tempSet) &&
                        (tempSet[row+1][col-1].getValue() == tempSet[row][col].getValue() || tempSet[row+1][col-1].getValue()+tempSet[row][col].getValue() == 10)
                        &&
                        (!tempSet[row+1][col-1].isSolved() && !tempSet[row][col].isSolved());

                if (dLBCheck) {
                    tempSet[row][col].setSolved(true);
                    tempSet[row+1][col-1].setSolved(true);
                    cellRCArrayList.add(new CellRC(row, col));
                }

                // Check for diagonal - left-top
                boolean dLTCheck = isValidRC(row-1, col-1, tempSet) &&
                        (tempSet[row-1][col-1].getValue() == tempSet[row][col].getValue() || tempSet[row-1][col-1].getValue()+tempSet[row][col].getValue() == 10)
                        &&
                        (!tempSet[row-1][col-1].isSolved() && !tempSet[row][col].isSolved());

                if (dLTCheck) {
                    tempSet[row][col].setSolved(true);
                    tempSet[row-1][col-1].setSolved(true);
                    cellRCArrayList.add(new CellRC(row, col));
                }

            }
        }

        if (gameLevel == GameLevel.EASY)
            removeLimit = (int) (cellRCArrayList.size() * 0.3f) + 1;
        else if (gameLevel == GameLevel.MEDIUM) {
            removeLimit = (int) (cellRCArrayList.size() * 0.5f) + 1;
        } else {
            removeLimit = (int) (cellRCArrayList.size() * 0.7f) + 1;
        }

        // convert data in tempSet cell
        for (int row=0; row< tempSet.length; row++) {
            for (int col=0; col<tempSet[row].length; col++) {

                if (totalRemoved < removeLimit) {
                    for (int i=0; i<cellRCArrayList.size(); i++) {
                        if (cellRCArrayList.get(i).getRow() == row && cellRCArrayList.get(i).getCol() == col) {

                            tempSet[row][col].setValue(getNumberExcludingMatchOne(RandNumber.get(1,9), tempSet[row][col].getValue()));
                            totalRemoved++;

                        }
                    }
                } else {
                    break;
                }

            }
        }

        // set data back into a main set.
        index = 0;

        for (int row=0; row<tempSet.length; row++) {
            for (int col=0; col<tempSet[row].length; col++) {
                set[index] = tempSet[row][col].getValue();
                index++;
            }
        }

        MakeLog.info(TAG, "Total Direct match: " + cellRCArrayList.size());
        MakeLog.info(TAG, "Total removed: " + totalRemoved);

        return set;

    }

    private boolean isValidSet (int[] set) {

        if (set != null) {
            if (set.length == 27) {
                for (int j : set) {
                    if (j < 0 || j > 9) {
                        return false;
                    }
                }
            } else {
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    private int getNumberExcludingMatchOne (int numberToFit, int numberToExclude) {

        if (numberToFit == numberToExclude) {
            return getValidNumber(numberToFit-1);
        } else {
            return getValidNumber(numberToFit);
        }

    }

    private int getValidNumber (int number) {

        if (number > 9)
            return number - 9;
        else if (number <= 0)
            return number + RandNumber.get(3,6);
        else
            return number;

    }

    private boolean isValidRC (int row, int col, Cell[][] refArr) {

        try {
            Cell a = refArr[row][col];
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    private int getOptimizedValue (int value,  int incrementLevel) {

        int number = 0;

        if (incrementLevel == 0) {
            return value;
        } else {
            int randNumber = RandNumber.get(1,9);
            if (randNumber + value <= 9) {
                number = randNumber + value;
            } else if (randNumber > value) {
                number = randNumber;
            } else if (randNumber < value) {
                number = value-randNumber;
            } else {
                number = 9 - randNumber;
            }
        }

        if (number > 9)
            number = 9-number;
        else if (number == 0) {
            number = RandNumber.get(1,3) + RandNumber.get(2,6);
        } else {
            if (getPositive(number) <= 9)
                return getPositive(number);
            else
                return getPositive(number)-9;
        }

        return getPositive(number);

    }

    private int getPositive (int value) {
        return (value < 0) ? value*-1 : value;
    }

    private int[] getOriginalSet (int setId) {

        switch (setId) {

            case 1:
            default:
                return SET_1;
            case 2:
                return SET_2;
            case 3:
                return SET_3;
            case 4:
                return SET_4;
            case 5:
                return SET_5;
            case 6:
                return SET_6;
            case 7:
                return SET_7;
            case 8:
                return SET_8;
            case 9:
                return SET_9;
            case 10:
                return SET_10;
            case 11:
                return SET_11;
            case 12:
                return SET_12;
            case 13:
                return SET_13;
            case 14:
                return SET_14;
            case 15:
                return SET_15;
            case 16:
                return SET_16;
            case 17:
                return SET_17;
            case 18:
                return SET_18;
            case 19:
                return SET_19;
            case 20:
                return SET_20;
            case 21:
                return SET_21;
            case 22:
                return SET_22;
            case 23:
                return SET_23;
            case 24:
                return SET_24;
            case 25:
                return SET_25;
            case 26:
                return SET_26;
            case 27:
                return SET_27;
            case 28:
                return SET_28;
            case 29:
                return SET_29;
            case 30:
                return SET_30;
            case 31:
                return SET_31;
            case 32:
                return SET_32;
            case 33:
                return SET_33;
            case 34:
                return SET_34;
            case 35:
                return SET_35;
            case 36:
                return SET_36;
            case 37:
                return SET_37;
            case 38:
                return SET_38;
            case 39:
                return SET_39;
            case 40:
                return SET_40;
            case 41:
                return SET_41;
            case 42:
                return SET_42;
            case 43:
                return SET_43;
            case 44:
                return SET_44;
            case 45:
                return SET_45;
            case 46:
                return SET_46;
            case 47:
                return SET_47;
            case 48:
                return SET_48;
            case 49:
                return SET_49;
            case 50:
                return SET_50;
            case 51:
                return SET_51;
            case 52:
                return SET_52;
            case 53:
                return SET_53;
            case 54:
                return SET_54;
            case 55:
                return SET_55;
            case 56:
                return SET_56;
            case 57:
                return SET_57;
            case 58:
                return SET_58;
            case 59:
                return SET_59;
            case 60:
                return SET_60;
            case 61:
                return SET_61;
            case 62:
                return SET_62;
            case 63:
                return SET_63;
            case 64:
                return SET_64;
            case 65:
                return SET_65;
            case 66:
                return SET_66;
            case 67:
                return SET_67;
            case 68:
                return SET_68;
            case 69:
                return SET_69;
            case 70:
                return SET_70;
            case 71:
                return SET_71;
            case 72:
                return SET_72;
            case 73:
                return SET_73;
            case 74:
                return SET_74;
            case 75:
                return SET_75;
            case 76:
                return SET_76;
            case 77:
                return SET_77;
            case 78:
                return SET_78;
            case 79:
                return SET_79;
            case 80:
                return SET_80;
            case 81:
                return SET_81;
            case 82:
                return SET_82;
            case 83:
                return SET_83;
            case 84:
                return SET_84;
            case 85:
                return SET_85;
            case 86:
                return SET_86;
            case 87:
                return SET_87;
            case 88:
                return SET_88;
            case 89:
                return SET_89;
            case 90:
                return SET_90;
            case 91:
                return SET_91;
            case 92:
                return SET_92;
            case 93:
                return SET_93;
            case 94:
                return SET_94;
            case 95:
                return SET_95;
            case 96:
                return SET_96;
            case 97:
                return SET_97;
            case 98:
                return SET_98;
            case 99:
                return SET_99;
            case 100:
                return SET_100;
            case 101:
                return SET_101;
            case 102:
                return SET_102;
            case 103:
                return SET_103;
            case 104:
                return SET_104;
            case 105:
                return SET_105;
            case 106:
                return SET_106;
            case 107:
                return SET_107;
            case 108:
                return SET_108;
            case 109:
                return SET_109;
            case 110:
                return SET_110;
            case 111:
                return SET_111;
            case 112:
                return SET_112;
            case 113:
                return SET_113;
            case 114:
                return SET_114;
            case 115:
                return SET_115;
            case 116:
                return SET_116;
            case 117:
                return SET_117;
            case 118:
                return SET_118;
            case 119:
                return SET_119;
            case 120:
                return SET_120;
            case 121:
                return SET_121;
            case 122:
                return SET_122;
            case 123:
                return SET_123;
            case 124:
                return SET_124;
            case 125:
                return SET_125;
            case 126:
                return SET_126;
            case 127:
                return SET_127;
            case 128:
                return SET_128;
            case 129:
                return SET_129;
            case 130:
                return SET_130;
            case 131:
                return SET_131;
            case 132:
                return SET_132;
            case 133:
                return SET_133;
            case 134:
                return SET_134;
            case 135:
                return SET_135;
            case 136:
                return SET_136;
            case 137:
                return SET_137;
            case 138:
                return SET_138;
            case 139:
                return SET_139;
            case 140:
                return SET_140;
            case 141:
                return SET_141;
            case 142:
                return SET_142;
            case 143:
                return SET_143;
            case 144:
                return SET_144;
            case 145:
                return SET_145;
            case 146:
                return SET_146;
            case 147:
                return SET_147;
            case 148:
                return SET_148;
            case 149:
                return SET_149;
            case 150:
                return SET_150;
            case 151:
                return SET_151;
            case 152:
                return SET_152;
            case 153:
                return SET_153;
            case 154:
                return SET_154;
            case 155:
                return SET_155;
            case 156:
                return SET_156;
            case 157:
                return SET_157;
            case 158:
                return SET_158;
            case 159:
                return SET_159;
            case 160:
                return SET_160;
            case 161:
                return SET_161;
            case 162:
                return SET_162;
            case 163:
                return SET_163;
            case 164:
                return SET_164;
            case 165:
                return SET_165;
            case 166:
                return SET_166;
            case 167:
                return SET_167;
            case 168:
                return SET_168;
            case 169:
                return SET_169;
            case 170:
                return SET_170;
            case 171:
                return SET_171;
            case 172:
                return SET_172;
            case 173:
                return SET_173;
            case 174:
                return SET_174;
            case 175:
                return SET_175;
            case 176:
                return SET_176;
            case 177:
                return SET_177;
            case 178:
                return SET_178;
            case 179:
                return SET_179;
            case 180:
                return SET_180;
            case 181:
                return SET_181;
            case 182:
                return SET_182;
            case 183:
                return SET_183;
            case 184:
                return SET_184;
            case 185:
                return SET_185;
            case 186:
                return SET_186;
            case 187:
                return SET_187;
            case 188:
                return SET_188;
            case 189:
                return SET_189;
            case 190:
                return SET_190;
            case 191:
                return SET_191;
            case 192:
                return SET_192;
            case 193:
                return SET_193;
            case 194:
                return SET_194;
            case 195:
                return SET_195;
            case 196:
                return SET_196;
            case 197:
                return SET_197;
            case 198:
                return SET_198;
            case 199:
                return SET_199;
            case 200:
                return SET_200;
            case 201:
                return SET_201;
            case 202:
                return SET_202;
            case 203:
                return SET_203;
            case 204:
                return SET_204;
            case 205:
                return SET_205;
            case 206:
                return SET_206;
            case 207:
                return SET_207;
            case 208:
                return SET_208;
            case 209:
                return SET_209;
            case 210:
                return SET_210;
            case 211:
                return SET_211;
            case 212:
                return SET_212;
            case 213:
                return SET_213;
            case 214:
                return SET_214;
            case 215:
                return SET_215;
            case 216:
                return SET_216;
            case 217:
                return SET_217;
            case 218:
                return SET_218;
            case 219:
                return SET_219;
            case 220:
                return SET_220;
            case 221:
                return SET_221;
            case 222:
                return SET_222;
            case 223:
                return SET_223;
            case 224:
                return SET_224;
            case 225:
                return SET_225;
            case 226:
                return SET_226;
            case 227:
                return SET_227;
            case 228:
                return SET_228;
            case 229:
                return SET_229;
            case 230:
                return SET_230;
            case 231:
                return SET_231;
            case 232:
                return SET_232;
            case 233:
                return SET_233;
            case 234:
                return SET_234;
            case 235:
                return SET_235;
            case 236:
                return SET_236;
            case 237:
                return SET_237;
            case 238:
                return SET_238;
            case 239:
                return SET_239;
            case 240:
                return SET_240;
            case 241:
                return SET_241;
            case 242:
                return SET_242;
            case 243:
                return SET_243;
            case 244:
                return SET_244;
            case 245:
                return SET_245;
            case 246:
                return SET_246;
            case 247:
                return SET_247;
            case 248:
                return SET_248;
            case 249:
                return SET_249;
            case 250:
                return SET_250;
            case 251:
                return SET_251;
            case 252:
                return SET_252;
            case 253:
                return SET_253;
            case 254:
                return SET_254;
            case 255:
                return SET_255;
            case 256:
                return SET_256;
            case 257:
                return SET_257;
            case 258:
                return SET_258;
            case 259:
                return SET_259;
            case 260:
                return SET_260;
            case 261:
                return SET_261;
            case 262:
                return SET_262;
            case 263:
                return SET_263;
            case 264:
                return SET_264;
            case 265:
                return SET_265;
            case 266:
                return SET_266;
            case 267:
                return SET_267;
            case 268:
                return SET_268;
            case 269:
                return SET_269;
            case 270:
                return SET_270;
            case 271:
                return SET_271;
            case 272:
                return SET_272;
            case 273:
                return SET_273;
            case 274:
                return SET_274;
            case 275:
                return SET_275;
            case 276:
                return SET_276;
            case 277:
                return SET_277;
            case 278:
                return SET_278;
            case 279:
                return SET_279;
            case 280:
                return SET_280;
            case 281:
                return SET_281;
            case 282:
                return SET_282;
            case 283:
                return SET_283;
            case 284:
                return SET_284;
            case 285:
                return SET_285;
            case 286:
                return SET_286;
            case 287:
                return SET_287;
            case 288:
                return SET_288;
            case 289:
                return SET_289;
            case 290:
                return SET_290;
            case 291:
                return SET_291;
            case 292:
                return SET_292;
            case 293:
                return SET_293;
            case 294:
                return SET_294;
            case 295:
                return SET_295;
            case 296:
                return SET_296;
            case 297:
                return SET_297;
            case 298:
                return SET_298;
            case 299:
                return SET_299;
            case 300:
                return SET_300;
            case 301:
                return SET_301;
            case 302:
                return SET_302;
            case 303:
                return SET_303;
            case 304:
                return SET_304;
            case 305:
                return SET_305;
            case 306:
                return SET_306;
            case 307:
                return SET_307;
            case 308:
                return SET_308;
            case 309:
                return SET_309;
            case 310:
                return SET_310;
            case 311:
                return SET_311;
            case 312:
                return SET_312;
            case 313:
                return SET_313;
            case 314:
                return SET_314;
            case 315:
                return SET_315;
            case 316:
                return SET_316;
            case 317:
                return SET_317;
            case 318:
                return SET_318;
            case 319:
                return SET_319;
            case 320:
                return SET_320;
            case 321:
                return SET_321;
            case 322:
                return SET_322;
            case 323:
                return SET_323;
            case 324:
                return SET_324;
            case 325:
                return SET_325;
            case 326:
                return SET_326;
            case 327:
                return SET_327;
            case 328:
                return SET_328;
            case 329:
                return SET_329;
            case 330:
                return SET_330;
            case 331:
                return SET_331;
            case 332:
                return SET_332;
            case 333:
                return SET_333;
            case 334:
                return SET_334;
            case 335:
                return SET_335;
            case 336:
                return SET_336;
            case 337:
                return SET_337;
            case 338:
                return SET_338;
            case 339:
                return SET_339;
            case 340:
                return SET_340;
            case 341:
                return SET_341;
            case 342:
                return SET_342;
            case 343:
                return SET_343;
            case 344:
                return SET_344;
            case 345:
                return SET_345;
            case 346:
                return SET_346;
            case 347:
                return SET_347;
            case 348:
                return SET_348;
            case 349:
                return SET_349;
            case 350:
                return SET_350;


        }

    }

    private static final int[] SET_350 = {
            5,7,3,4,5,8,3,5,4,
            2,4,5,9,7,5,1,2,3,
            9,7,2,6,2,1,9,9,8
    };

    private static final int[] SET_349 = {
            5,7,3,4,5,8,3,5,4,
            2,4,5,9,7,5,1,2,3,
            9,7,2,6,2,1,9,9,8
    };

    private static final int[] SET_348 = {
            3,6,3,6,8,1,3,7,2,
            5,2,7,1,3,7,9,5,6,
            3,4,1,4,8,4,2,3,9
    };

    private static final int[] SET_347 = {
            1,5,5,2,1,5,5,9,8,
            3,6,7,4,5,8,6,3,2,
            9,8,5,1,5,4,5,1,5
    };

    private static final int[] SET_346 = {
            3,2,7,4,1,4,7,8,4,
            5,4,1,9,8,5,1,4,6,
            8,9,4,3,6,3,2,4,1
    };

    private static final int[] SET_345 = {
            4,7,7,4,1,2,6,1,3,
            2,9,8,5,3,1,5,7,9,
            7,1,6,1,4,8,4,8,4
    };

    private static final int[] SET_344 = {
            6,5,4,3,5,4,5,1,2,
            4,2,8,9,8,3,9,7,8,
            9,3,4,3,4,2,5,4,1
    };

    private static final int[] SET_343 = {
            8,1,3,5,4,9,5,8,4,
            9,6,1,7,7,2,7,9,5,
            2,4,5,9,3,5,6,2,7
    };

    private static final int[] SET_342 = {
            7,4,7,8,9,3,6,1,8,
            7,1,5,3,5,2,7,5,6,
            8,9,2,9,4,6,1,2,7
    };

    private static final int[] SET_341 = {
            1,4,7,1,3,3,4,2,5,
            2,5,6,5,6,7,3,9,8,
            6,6,3,1,2,1,2,5,7
    };

    private static final int[] SET_340 = {
            6,7,2,4,8,7,2,7,1,
            7,7,7,9,5,9,6,5,8,
            3,7,8,4,2,3,7,4,9
    };

    private static final int[] SET_339 = {
            6,9,5,3,8,3,6,7,5,
            8,6,7,6,5,1,8,2,9,
            5,7,7,4,8,6,5,4,3
    };

    private static final int[] SET_338 = {
            4,7,9,8,4,6,2,5,8,
            3,2,6,5,1,5,3,4,9,
            8,4,4,4,3,6,9,5,8
    };

    private static final int[] SET_337 = {
            8,1,3,8,3,5,2,5,1,
            2,4,5,4,1,4,3,7,5,
            7,1,9,3,7,5,1,6,2
    };

    private static final int[] SET_336 = {
            4,5,5,8,3,8,9,5,1,
            7,2,1,6,2,1,8,6,7,
            2,4,3,5,3,6,7,9,8
    };

    private static final int[] SET_335 = {
            5,4,2,1,4,7,9,7,7,
            8,6,5,7,5,1,8,6,1,
            7,3,9,8,4,2,7,5,2
    };

    private static final int[] SET_334 = {
            8,3,8,7,9,9,8,1,4,
            1,5,6,6,8,5,6,7,9,
            2,6,2,1,4,9,5,8,5
    };

    private static final int[] SET_333 = {
            8,4,5,7,6,5,9,6,2,
            1,8,6,1,8,7,2,5,8,
            3,5,9,7,2,4,1,7,1
    };

    private static final int[] SET_332 = {
            5,9,8,8,1,9,6,9,3,
            8,4,5,6,3,7,5,8,6,
            7,1,8,9,2,4,1,4,2
    };

    private static final int[] SET_331 = {
            9,8,9,5,6,5,8,5,8,
            7,2,4,3,9,1,9,7,7,
            9,3,5,2,4,8,4,1,5
    };

    private static final int[] SET_330 = {
            8,7,3,5,4,9,5,1,6,
            9,6,8,9,6,3,2,3,8,
            5,3,5,1,5,8,5,3,5
    };

    private static final int[] SET_329 = {
            5,5,7,2,9,3,6,9,4,
            5,9,8,4,6,5,8,5,1,
            7,5,3,5,7,9,7,6,7
    };

    private static final int[] SET_328 = {
            7,9,2,6,3,8,5,2,1,
            7,5,3,5,7,1,5,6,3,
            6,9,4,8,2,7,3,2,9
    };

    private static final int[] SET_327 = {
            5,9,6,9,8,5,3,2,1,
            6,5,8,7,1,4,9,5,6,
            1,7,1,2,6,8,7,8,4
    };

    private static final int[] SET_326 = {
            3,7,4,2,7,7,8,1,5,
            9,5,8,5,4,5,6,3,9,
            5,7,1,3,8,1,8,5,4
    };

    private static final int[] SET_325 = {
            9,3,4,5,6,5,6,7,2,
            4,2,1,2,3,1,4,4,5,
            5,4,7,5,4,7,8,6,9
    };

    private static final int[] SET_324 = {
            2,1,8,6,5,4,1,5,3,
            8,4,7,5,1,8,7,8,4,
            7,5,3,6,2,6,1,6,9
    };

    private static final int[] SET_323 = {
            3,7,6,1,7,8,2,3,8,
            4,2,2,2,6,9,5,4,1,
            8,7,4,3,5,7,2,6,5
    };

    private static final int[] SET_322 = {
            1,2,1,3,1,4,8,4,8,
            9,7,6,4,7,5,1,3,5,
            4,5,8,5,8,6,1,9,1
    };

    private static final int[] SET_321 = {
            7,3,9,5,8,7,9,5,7,
            5,3,6,7,2,5,8,2,8,
            7,1,5,1,4,2,3,9,5
    };

    private static final int[] SET_320 = {
            7,3,5,7,1,7,4,8,3,
            2,6,2,6,5,3,2,2,6,
            8,1,3,9,2,9,8,3,1
    };

    private static final int[] SET_319 = {
            3,1,3,4,1,2,1,6,7,
            1,1,1,2,3,6,7,8,9,
            2,9,8,9,5,8,3,5,4
    };

    private static final int[] SET_318 = {
            6,7,5,9,8,6,6,1,9,
            5,9,2,4,3,1,7,2,3,
            6,8,7,9,6,2,5,4,6
    };

    private static final int[] SET_317 = {
            6,9,3,5,9,1,6,7,8,
            4,2,7,8,3,8,5,9,3,
            5,1,5,1,6,3,4,8,4
    };

    private static final int[] SET_316 = {
            5,1,2,6,6,7,1,8,4,
            7,6,5,1,3,5,3,5,6,
            1,2,7,4,2,3,1,9,7
    };

    private static final int[] SET_315 = {
            5,4,9,5,3,5,2,9,3,
            1,6,7,2,7,5,4,5,8,
            7,2,1,3,6,7,3,1,7
    };

    private static final int[] SET_314 = {
            8,1,6,4,3,1,3,7,1,
            6,3,7,1,5,8,9,6,2,
            9,4,5,8,3,6,7,5,1
    };

    private static final int[] SET_313 = {
            4,2,3,3,8,7,9,3,7,
            5,1,9,5,4,5,8,4,9,
            2,8,3,6,7,1,7,5,7
    };

    private static final int[] SET_312 = {
            3,5,4,2,7,1,2,8,1,
            6,1,7,2,6,3,6,7,6,
            2,8,6,9,4,5,9,5,2
    };

    private static final int[] SET_311 = {
            3,1,5,2,1,2,3,9,4,
            5,6,2,4,5,7,4,2,8,
            3,9,5,7,2,4,9,4,6
    };

    private static final int[] SET_310 = {
            7,9,5,4,5,3,7,9,4,
            2,4,8,3,9,1,3,2,5,
            9,6,1,3,5,4,5,3,4
    };

    private static final int[] SET_309 = {
            5,6,9,3,4,3,2,9,6,
            5,7,7,2,9,6,5,4,7,
            8,9,3,4,3,2,7,1,8
    };

    private static final int[] SET_308 = {
            9,7,4,5,2,4,8,9,7,
            2,3,2,1,7,6,5,2,4,
            9,7,3,6,5,1,3,9,6
    };

    private static final int[] SET_307 = {
            1,4,3,9,6,8,4,1,4,
            2,5,1,2,9,3,7,7,3,
            4,3,4,5,9,7,7,4,1
    };

    private static final int[] SET_306 = {
            5,7,5,5,1,8,6,4,4,
            9,4,1,6,3,5,3,4,7,
            5,7,8,4,9,6,1,8,2
    };

    private static final int[] SET_305 = {
            8,6,7,9,7,5,8,9,2,
            7,4,7,2,6,2,6,7,8,
            1,5,1,5,3,7,9,5,1
    };

    private static final int[] SET_304 = {
            1,6,8,5,2,3,2,2,6,
            5,2,9,3,4,9,5,3,5,
            7,1,4,7,6,2,6,8,6
    };

    private static final int[] SET_303 = {
            4,5,3,9,1,2,9,4,7,
            7,1,2,4,3,4,5,3,6,
            2,3,6,5,1,8,1,2,5
    };

    private static final int[] SET_302 = {
            1,4,2,5,4,3,1,3,7,
            8,5,2,7,1,5,2,5,5,
            3,1,6,5,9,8,6,7,4
    };

    private static final int[] SET_301 = {
            5,5,3,5,1,2,9,7,6,
            4,2,1,6,7,8,5,2,5,
            9,8,9,5,1,6,9,6,1
    };

    private static final int[] SET_300 = {
            4,9,2,2,7,3,9,7,4,
            8,5,6,1,4,5,8,1,8,
            6,3,9,5,7,9,2,5,6
    };

    private static final int[] SET_299 = {
            4,9,4,5,4,6,5,3,5,
            6,3,2,1,3,7,9,1,8,
            2,5,6,5,8,6,5,4,5
    };

    private static final int[] SET_298 = {
            1,5,6,1,6,1,6,8,5,
            9,2,3,2,5,7,5,1,3,
            5,5,9,8,1,4,3,4,9
    };

    private static final int[] SET_297 = {
           4,8,6,2,3,2,2,2,8,
           5,9,7,5,9,2,3,6,5,
           7,2,1,6,7,2,8,2,3
    };

    private static final int[] SET_296 = {
           9,9,5,3,6,4,7,8,1,
           8,4,7,1,8,5,1,4,9,
           3,1,8,4,2,4,8,7,8
    };

    private static final int[] SET_295 = {
            1,4,1,8,5,4,7,8,5,
            9,3,5,3,6,3,9,4,1,
            8,2,6,8,3,5,8,3,8
    };

    private static final int[] SET_294 = {
            1,6,2,6,8,9,4,2,2,
            3,5,1,5,3,5,7,8,2,
            8,8,4,8,4,6,9,4,4
    };

    private static final int[] SET_293 = {
            7,4,2,5,5,2,3,8,7,
            8,5,1,3,1,6,5,4,6,
            3,6,8,5,6,6,4,1,9
    };

    private static final int[] SET_292 = {
            7,1,6,1,8,4,1,2,9,
            5,6,3,7,6,3,5,6,5,
            8,9,2,1,2,9,7,3,3
    };

    private static final int[] SET_291 = {
            9,5,9,2,7,5,8,9,3,
            2,8,6,5,4,1,3,5,2,
            9,5,4,6,8,7,6,1,5
    };

    private static final int[] SET_290 = {
            1,4,3,7,6,7,1,9,2,
            3,8,4,8,4,5,2,6,5,
            6,1,3,5,2,3,1,3,2
    };

    private static final int[] SET_289 = {
            3,7,6,1,3,6,4,9,4,
            9,5,6,9,8,9,8,5,3,
            2,3,2,5,5,7,6,1,6
    };

    private static final int[] SET_288 = {
            6,3,5,1,8,2,6,7,9,
            8,2,9,4,1,3,5,2,4,
            3,1,7,5,5,8,4,7,1
    };

    private static final int[] SET_287 = {
            2,4,5,4,1,7,4,5,7,
            4,9,8,7,3,2,3,1,8,
            3,5,4,6,5,6,5,2,3
    };

    private static final int[] SET_286 = {
            7,1,5,6,2,9,7,2,4,
            6,9,7,9,3,4,8,3,4,
            2,5,4,5,8,5,9,6,9
    };

    private static final int[] SET_285 = {
            5,8,3,4,3,8,5,1,9,
            7,4,5,9,2,1,9,5,6,
            8,1,2,7,5,4,8,4,2
    };

    private static final int[] SET_284 = {
            4,5,6,1,8,4,9,4,3,
            1,7,8,5,3,3,8,5,7,
            9,4,6,2,6,5,1,6,1
    };

    private static final int[] SET_283 = {
            4,5,9,3,5,9,3,7,6,
            9,7,6,7,6,8,6,9,4,
            8,1,5,8,5,2,3,5,7
    };

    private static final int[] SET_282 = {
            2,4,8,4,7,9,3,1,9,
            3,5,3,1,2,4,2,6,3,
            5,2,6,5,7,3,8,9,7
    };

    private static final int[] SET_281 = {
            1,4,7,4,8,9,2,1,5,
            6,6,2,3,2,3,3,6,8,
            9,4,9,4,1,5,8,5,1
    };

    private static final int[] SET_280 = {
            8,8,6,2,9,2,3,6,9,
            2,2,5,3,4,5,1,5,2,
            1,6,7,2,6,2,3,4,8
    };

    private static final int[] SET_279 = {
            2,8,1,8,4,1,3,1,4,
            7,5,2,6,7,5,4,5,8,
            1,5,7,9,2,4,9,7,9
    };

    private static final int[] SET_278 = {
            9,2,4,8,4,3,5,5,6,
            5,7,9,5,9,2,6,1,8,
            2,4,2,5,1,7,3,5,4
    };

    private static final int[] SET_277 = {
            6,3,8,3,8,6,2,5,5,
            9,5,4,5,7,4,3,4,7,
            2,3,9,6,1,2,3,9,8
    };

    private static final int[] SET_276 = {
            1,7,8,3,8,9,4,8,9,
            2,5,7,7,5,2,6,4,3,
            4,1,4,7,3,1,7,9,2
    };

    private static final int[] SET_275 = {
            8,5,2,4,7,6,7,8,2,
            3,9,1,5,9,5,1,3,4,
            5,3,4,3,2,6,4,5,8
    };

    private static final int[] SET_274 = {
            6,4,7,8,9,6,4,4,7,
            8,4,1,5,3,8,5,2,4,
            5,3,4,8,6,9,1,7,9
    };

    private static final int[] SET_273 = {
            8,4,1,4,1,6,4,7,4,
            1,7,5,7,8,7,5,3,2,
            6,9,6,2,4,1,5,4,9
    };

    private static final int[] SET_272 = {
            2,1,3,5,5,7,5,4,8,
            4,5,2,4,1,2,4,3,5,
            8,3,1,5,6,5,7,9,5
    };

    private static final int[] SET_271 = {
            2,9,4,2,3,5,6,4,9,
            6,5,7,1,4,5,2,3,2,
            6,1,8,3,9,7,4,9,4
    };

    private static final int[] SET_270 = {
            9,3,5,4,8,1,4,5,8,
            6,8,6,3,5,9,3,2,5,
            1,7,5,2,8,4,5,9,6
    };

    private static final int[] SET_269 = {
            2,6,8,5,5,8,3,4,9,
            8,5,5,7,1,4,9,7,6,
            7,6,8,6,8,5,8,5,9
    };

    private static final int[] SET_268 = {
            3,8,2,4,9,8,3,7,4,
            1,5,3,5,7,5,5,6,6,
            4,8,9,4,2,1,2,3,9
    };

    private static final int[] SET_267 = {
            5,4,8,2,3,8,9,5,3,
            8,7,8,7,4,5,3,4,6,
            1,9,8,5,9,8,1,2,5
    };

    private static final int[] SET_266 = {
            4,1,8,7,1,8,9,5,1,
            8,9,5,9,3,5,6,3,4,
            5,6,4,2,4,8,2,5,8
    };

    private static final int[] SET_265 = {
            7,1,3,6,7,3,3,8,6,
            5,8,5,4,9,7,5,9,3,
            4,1,3,8,1,4,6,2,5
    };

    private static final int[] SET_264 = {
            7,3,6,9,8,6,8,1,3,
            8,1,5,4,3,5,2,6,5,
            7,6,7,9,6,4,5,9,7
    };

    private static final int[] SET_263 = {
            7,7,7,9,2,9,2,1,7,
            7,9,2,3,4,5,4,5,8,
            2,6,5,9,8,7,2,6,3
    };

    private static final int[] SET_262 = {
            1,6,5,2,6,4,9,8,1,
            3,9,3,9,8,5,5,7,5,
            4,5,6,5,4,9,6,5,2
    };

    private static final int[] SET_261 = {
            5,1,2,9,4,6,2,7,4,
            5,7,4,3,2,5,9,5,8,
            2,9,5,4,7,6,3,4,1
    };

    private static final int[] SET_260 = {
            3,5,4,1,3,7,2,7,4,
            4,8,9,2,6,5,4,9,5,
            7,5,7,5,4,3,1,3,8
    };

    private static final int[] SET_259 = {
            4,5,3,6,5,4,1,2,4,
            7,8,2,9,1,2,6,3,9,
            4,3,5,7,4,8,5,2,5
    };

    private static final int[] SET_258 = {
            9,3,5,5,1,9,7,6,3,
            2,4,2,7,4,2,5,2,8,
            7,5,9,6,1,8,4,1,5
    };

    private static final int[] SET_257 = {
            7,3,1,7,8,1,5,8,6,
            8,6,5,2,5,7,6,3,1,
            5,7,7,9,3,4,1,2,6
    };

    private static final int[] SET_256 = {
            4,4,8,3,5,9,8,9,8,
            5,1,5,6,8,3,4,5,7,
            3,8,4,1,5,2,1,4,1
    };

    private static final int[] SET_255 = {
            2,4,9,7,9,6,5,1,1,
            6,5,8,5,2,7,2,3,7,
            1,9,7,1,4,9,9,6,9
    };

    private static final int[] SET_254 = {
            7,5,1,3,7,5,9,8,3,
            9,8,6,5,6,4,1,4,2,
            7,6,9,7,9,2,5,3,9
    };

    private static final int[] SET_253 = {
            1,4,8,3,1,4,6,1,2,
            5,9,7,3,5,8,3,5,7,
            6,5,6,1,7,1,6,8,6
    };

    private static final int[] SET_252 = {
            5,6,5,4,1,4,1,7,8,
            2,3,2,3,5,1,9,4,5,
            9,6,5,6,1,9,8,1,9
    };

    private static final int[] SET_251 = {
            6,5,9,4,9,4,2,9,1,
            8,3,1,8,2,7,5,4,7,
            6,5,6,7,1,9,3,1,8
    };

    private static final int[] SET_250 = {
            2,3,6,5,6,1,6,7,4,
            8,5,4,9,7,8,3,9,6,
            7,1,5,2,6,9,4,8,3
    };

    private static final int[] SET_249 = {
            1,9,5,9,7,3,8,5,6,
            5,7,8,2,4,6,1,3,2,
            1,2,8,1,3,2,5,6,1
    };

    private static final int[] SET_248 = {
            3,1,5,7,4,7,1,6,5,
            7,4,6,5,8,5,2,3,1,
            5,2,8,9,1,4,1,4,2
    };

    private static final int[] SET_247 = {
            3,1,6,5,3,1,8,5,3,
            2,4,2,9,6,9,5,9,2,
            9,5,7,3,2,7,6,3,8
    };

    private static final int[] SET_246 = {
            6,2,4,9,2,4,4,8,3,
            5,7,8,5,6,5,1,5,2,
            1,4,9,7,3,4,8,4,1
    };

    private static final int[] SET_245 = {
            9,2,1,4,5,7,7,1,6,
            5,3,5,2,1,2,5,8,1,
            2,7,9,4,3,9,3,4,6
    };

    private static final int[] SET_244 = {
            8,5,2,7,2,3,5,7,6,
            9,3,6,4,9,8,2,2,9,
            2,5,7,5,7,1,4,3,5
    };

    private static final int[] SET_243 = {
            4,5,7,6,8,9,4,1,3,
            4,2,1,4,4,7,5,2,6,
            7,8,5,6,1,5,4,3,5
    };

    private static final int[] SET_242 = {
            1,8,5,3,1,2,5,8,9,
            2,4,7,2,6,7,4,7,6,
            3,1,9,8,1,2,5,5,2
    };

    private static final int[] SET_241 = {
            6,7,4,7,3,1,8,5,2,
            5,8,4,5,4,5,4,7,9,
            1,7,9,3,6,7,6,8,4
    };

    private static final int[] SET_240 = {
            1,3,7,6,5,1,3,6,3,
            2,5,9,2,7,9,5,5,8,
            4,9,6,7,6,2,3,4,7
    };

    private static final int[] SET_239 = {
            8,6,5,5,4,9,3,6,8,
            6,7,8,7,2,5,8,9,4,
            2,9,4,5,4,3,1,9,9
    };

    private static final int[] SET_238 = {
            8,6,9,8,5,4,1,4,1,
            9,2,5,6,7,2,7,5,8,
            5,3,9,4,2,2,2,1,3
    };

    private static final int[] SET_237 = {
            9,4,5,4,3,7,8,6,9,
            5,7,1,8,1,8,2,7,4,
            8,6,5,6,9,6,2,9,2
    };

    private static final int[] SET_236 = {
            7,2,4,5,2,5,7,2,2,
            3,5,9,3,6,1,8,8,5,
            1,4,8,4,2,7,6,2,8
    };

    private static final int[] SET_235 = {
            3,6,7,5,6,9,4,2,5,
            1,2,4,1,2,5,6,8,5,
            6,3,5,3,4,8,9,7,9
    };

    private static final int[] SET_234 = {
            5,9,7,8,9,5,5,8,4,
            1,9,4,5,6,8,6,7,5,
            6,9,8,2,3,1,4,8,1
    };

    private static final int[] SET_233 = {
            1,2,8,1,1,9,8,2,5,
            7,4,1,5,6,7,4,3,9,
            8,5,3,8,8,5,9,2,6
    };

    private static final int[] SET_232 = {
            4,2,3,4,9,4,7,4,9,
            5,1,5,5,3,6,5,8,5,
            3,8,3,1,8,1,5,5,2
    };

    private static final int[] SET_231 = {
            8,6,9,7,8,7,7,6,3,
            2,7,1,4,1,7,3,9,2,
            1,6,8,7,5,8,6,1,7
    };

    private static final int[] SET_230 = {
            9,4,9,5,6,2,6,7,2,
            5,6,7,8,6,7,8,4,5,
            1,3,5,9,5,9,1,7,9
    };

    private static final int[] SET_229 = {
            3,5,6,3,8,4,8,1,8,
            7,2,5,1,9,3,5,4,3,
            4,9,7,2,3,2,1,6,5
    };

    private static final int[] SET_228 = {
            8,6,2,4,1,8,3,5,9,
            8,5,9,5,5,2,1,6,2,
            4,6,8,1,7,4,7,5,9
    };

    private static final int[] SET_227 = {
            4,5,2,8,9,1,6,9,6,
            8,1,3,1,5,7,5,2,7,
            5,9,6,9,3,1,6,9,5
    };

    private static final int[] SET_226 = {
            9,5,6,2,6,3,1,9,2,
            4,8,1,5,8,4,5,4,7,
            3,5,7,5,1,7,2,9,8
    };

    private static final int[] SET_225 = {
            2,9,4,7,9,7,9,7,4,
            3,5,3,2,4,4,6,2,1,
            6,8,4,5,6,7,3,5,8
    };

    private static final int[] SET_224 = {
            9,8,6,4,3,2,6,4,7,
            5,7,1,2,5,9,5,8,6,
            8,6,5,9,8,7,5,1,3
    };

    private static final int[] SET_223 = {
            9,7,5,7,1,4,6,6,7,
            4,2,6,8,6,2,6,4,3,
            1,9,5,3,5,7,9,5,9
    };

    private static final int[] SET_222 = {
            7,3,5,9,2,3,1,3,5,
            6,7,5,7,4,8,5,4,9,
            5,1,7,8,3,1,7,2,3
    };

    private static final int[] SET_221 = {
            6,9,4,2,7,7,8,9,6,
            2,5,3,5,1,6,2,3,8,
            4,9,8,9,4,5,7,5,9
    };

    private static final int[] SET_220 = {
            9,8,7,6,2,2,2,4,5,
            3,4,3,1,2,8,8,7,6,
            8,1,2,6,7,1,6,9,2
    };

    private static final int[] SET_219 = {
            7,4,8,6,8,3,6,2,5,
            5,9,5,4,5,1,5,7,1,
            3,7,1,3,2,4,2,9,6
    };

    private static final int[] SET_218 = {
            6,5,4,4,7,8,5,1,8,
            4,7,9,2,5,6,3,6,7,
            9,5,6,3,7,6,5,5,2
    };

    private static final int[] SET_217 = {
            8,5,4,2,1,8,3,4,1,
            7,5,3,9,7,6,9,7,8,
            3,4,8,5,8,5,8,4,2
    };

    private static final int[] SET_216 = {
            3,4,9,4,8,5,1,3,9,
            2,5,7,1,3,5,8,5,1,
            8,9,6,8,6,9,7,2,6
    };

    private static final int[] SET_215 = {
            4,9,9,4,9,7,1,4,9,
            6,7,8,5,2,5,6,2,3,
            8,9,5,9,6,3,9,1,6
    };

    private static final int[] SET_214 = {
            3,9,6,1,4,7,2,7,5,
            4,5,8,7,5,1,5,1,5,
            2,6,3,9,2,8,3,9,4
    };

    private static final int[] SET_213 = {
            4,7,4,8,5,4,2,3,2,
            1,2,2,1,2,9,9,6,5,
            7,6,7,3,6,7,8,9,1
    };

    private static final int[] SET_212 = {
            7,4,3,7,4,7,5,3,5,
            2,1,2,5,9,2,6,1,4,
            8,3,7,4,7,8,5,7,6
    };

    private static final int[] SET_211 = {
            6,7,1,9,7,6,9,5,9,
            8,2,6,4,5,2,5,7,6,
            3,9,7,9,8,6,1,2,1
    };

    private static final int[] SET_210 = {
            3,1,7,5,7,6,8,2,8,
            4,9,5,8,9,5,7,8,8,
            2,3,4,7,6,8,6,5,7
    };

    private static final int[] SET_209 = {
            6,4,7,9,3,9,7,8,1,
            3,4,5,1,4,2,3,6,3,
            2,4,5,3,5,9,5,8,9
    };

    private static final int[] SET_208 = {
            3,7,6,9,2,6,3,1,7,
            4,1,2,6,4,4,5,4,8,
            2,7,5,3,6,3,9,3,1
    };

    private static final int[] SET_207 = {
            3,2,9,3,2,7,1,3,4,
            5,9,3,1,7,7,5,8,5,
            6,8,6,2,9,3,4,1,4
    };

    private static final int[] SET_206 = {
            3,8,9,6,1,7,5,7,5,
            1,4,5,8,5,3,9,8,6,
            5,9,3,5,5,4,5,2,1
    };

    private static final int[] SET_205 = {
            8,1,9,8,2,3,1,4,1,
            6,7,5,6,9,2,5,3,8,
            8,9,3,2,3,8,1,4,1
    };

    private static final int[] SET_204 = {
            1,3,2,1,9,6,3,7,6,
            8,4,5,3,5,2,1,2,9,
            6,7,8,7,8,3,6,7,4
    };

    private static final int[] SET_203 = {
            5,9,4,9,1,7,6,7,5,
            7,8,2,5,2,5,9,2,6,
            9,3,9,6,9,7,5,3,8
    };

    private static final int[] SET_202 = {
            4,6,2,8,7,4,2,7,9,
            7,9,5,5,9,5,1,4,8,
            4,8,7,2,8,3,2,3,7
    };

    private static final int[] SET_201 = {
            4,2,3,8,5,2,9,7,9,
            3,5,1,4,1,7,6,5,6,
            9,8,9,9,8,5,8,4,7
    };

    private static final int[] SET_200 = {
            6,8,7,2,8,7,6,1,7,
            5,1,6,5,1,4,5,8,4,
            5,2,3,8,3,9,8,7,1
    };

    private static final int[] SET_199 = {
            6,7,9,8,7,6,8,1,7,
            4,5,5,6,2,6,7,6,5,
            1,8,3,8,2,5,8,9,7
    };

    private static final int[] SET_198 = {
            8,7,5,7,3,9,7,4,9,
            5,2,4,2,6,5,5,1,2,
            9,7,5,9,5,5,7,4,7
    };

    private static final int[] SET_197 = {
            8,1,2,8,3,7,4,1,5,
            7,5,3,7,8,7,8,7,2,
            4,9,6,9,8,7,4,5,6
    };

    private static final int[] SET_196 = {
            3,1,1,9,3,4,1,8,2,
            3,7,3,2,1,5,7,4,3,
            4,9,6,5,7,8,1,5,8
    };

    private static final int[] SET_195 = {
            9,8,1,9,5,5,4,3,2,
            7,6,1,9,2,1,5,1,6,
            2,5,5,6,7,6,8,5,3
    };

    private static final int[] SET_194 = {
            3,8,7,8,9,5,7,3,5,
            6,5,1,6,3,4,9,7,1,
            1,4,3,2,5,1,7,4,5
    };

    private static final int[] SET_193 = {
            8,3,6,2,7,4,1,7,2,
            6,5,1,5,3,3,2,9,8,
            1,7,6,2,8,1,7,4,3
    };

    private static final int[] SET_192 = {
            3,5,5,8,5,4,7,8,7,
            6,8,9,4,2,9,1,4,5,
            4,5,2,5,7,6,3,8,3
    };

    private static final int[] SET_191 = {
            8,8,1,8,6,5,1,1,8,
            5,6,3,4,2,1,9,6,5,
            9,8,9,5,7,4,2,7,2
    };

    private static final int[] SET_190 = {
            3,1,3,2,7,8,5,5,2,
            8,6,5,9,1,4,9,7,9,
            3,4,8,4,3,6,2,6,8
    };

    private static final int[] SET_189 = {
            9,6,1,2,5,4,5,8,1,
            7,4,9,7,4,2,3,6,5,
            3,5,5,8,9,5,1,8,7
    };

    private static final int[] SET_188 = {
            4,6,1,3,6,4,8,4,9,
            1,8,5,2,9,5,1,5,2,
            4,7,9,4,4,7,3,4,1
    };

    private static final int[] SET_187 = {
            1,2,8,6,7,6,5,4,5,
            3,6,2,5,9,9,1,1,2,
            5,2,7,4,8,5,9,5,9
    };

    private static final int[] SET_186 = {
            5,2,5,1,8,4,6,2,3,
            6,1,6,6,3,7,5,1,8,
            7,9,2,8,6,8,7,4,5
    };

    private static final int[] SET_185 = {
            1,7,9,8,1,8,4,7,4,
            6,3,4,2,3,4,9,2,9,
            2,5,1,7,1,2,3,6,5
    };

    private static final int[] SET_184 = {
            2,1,2,6,9,7,3,9,3,
            4,7,5,3,7,7,8,5,4,
            6,2,6,2,9,3,9,4,9
    };

    private static final int[] SET_183 = {
            2,6,3,7,4,2,5,7,5,
            7,9,2,9,5,1,4,1,4,
            5,5,6,3,6,8,1,9,6
    };

    private static final int[] SET_182 = {
            2,3,3,6,8,6,4,1,4,
            9,6,7,3,5,3,2,5,3,
            5,1,5,2,1,6,9,6,3
    };

    private static final int[] SET_181 = {
            8,9,1,5,2,1,3,1,5,
            6,7,8,4,7,8,6,3,6,
            8,1,7,9,5,9,7,3,8
    };

    private static final int[] SET_180 = {
            2,3,7,9,8,5,2,7,1,
            3,3,5,4,7,9,7,6,5,
            1,4,9,5,2,4,5,1,4
    };

    private static final int[] SET_179 = {
            3,2,3,2,8,9,6,3,9,
            5,6,5,1,7,5,1,8,5,
            4,8,7,6,3,8,3,4,5
    };

    private static final int[] SET_178 = {
            2,8,9,1,9,7,1,4,9,
            3,9,4,1,9,8,6,7,8,
            4,5,7,5,4,5,9,5,9
    };

    private static final int[] SET_177 = {
            7,1,4,5,7,1,2,8,6,
            3,4,2,5,8,5,6,5,7,
            5,1,3,4,9,3,4,8,6
    };

    private static final int[] SET_176 = {
            7,2,1,3,9,3,4,8,9,
            1,5,4,5,7,8,7,5,4,
            8,7,8,6,1,2,6,2,9
    };

    private static final int[] SET_175 = {
            5,3,2,6,3,1,9,2,5,
            9,1,9,8,5,6,3,6,7,
            4,1,4,7,1,2,9,4,1
    };

    private static final int[] SET_174 = {
            3,6,2,1,8,1,9,3,9,
            8,4,4,3,7,4,8,6,5,
            3,9,6,8,4,1,7,9,2
    };

    private static final int[] SET_173 = {
            2,8,4,7,5,5,8,6,9,
            7,1,5,3,4,5,3,5,8,
            5,6,2,1,5,2,3,1,7
    };

    private static final int[] SET_172 = {
            7,9,8,9,3,8,3,1,7,
            6,2,4,2,5,4,5,2,6,
            1,5,8,3,1,9,2,2,8
    };

    private static final int[] SET_171 = {
            8,4,8,1,7,9,2,8,4,
            1,9,6,5,4,5,7,1,5,
            5,2,3,2,4,1,8,4,8
    };

    private static final int[] SET_170 = {
            8,3,9,5,2,9,2,7,8,
            4,7,1,7,9,4,5,4,1,
            6,5,2,4,8,2,9,7,5
    };

    private static final int[] SET_169 = {
            9,8,4,7,1,7,8,9,7,
            5,6,1,2,6,4,2,3,2,
            2,3,5,3,5,1,5,1,4
    };

    private static final int[] SET_168 = {
            3,8,2,4,5,4,8,7,6,
            7,9,7,1,8,3,1,9,4,
            5,4,8,4,8,4,8,7,5
    };

    private static final int[] SET_167 = {
            6,9,4,9,7,9,2,7,5,
            4,5,3,8,9,1,5,9,2,
            1,2,4,5,2,3,8,7,5
    };

    private static final int[] SET_166 = {
            1,6,2,4,8,5,4,5,3,
            7,5,7,1,7,1,3,2,9,
            6,4,9,5,4,5,5,3,4
    };


    private static final int[] SET_1 = {
            8,5,8,7,9,6,5,3,4,
            7,4,1,4,2,8,9,8,7,
            2,9,7,1,9,7,6,7,7
    };

    private static final int[] SET_2 = {
            1,7,8,3,2,5,4,1,3,
            5,7,5,4,1,5,8,9,8,
            6,2,1,2,6,7,3,6,7
    };

    private static final int[] SET_3 = {
            1,3,8,9,6,1,3,5,6,
            5,2,4,5,2,7,3,3,2,
            7,3,1,7,1,6,1,8,9
    };

    private static final int[] SET_4 = {
            4,5,7,1,6,2,2,8,6,
            3,9,5,2,5,3,7,2,1,
            6,1,6,9,4,2,4,5,1
    };

    private static final int[] SET_5 = {
            6,2,7,5,3,6,2,5,7,
            1,2,6,1,2,1,7,4,5,
            3,8,2,9,7,4,8,6,9
    };

    private static final int[] SET_6 = {
            3,9,5,8,7,5,5,4,1,
            5,1,7,6,3,6,2,3,5,
            7,2,2,9,8,5,8,6,8
    };

    private static final int[] SET_7 = {
            2,4,7,4,7,2,8,6,3,
            7,5,1,5,7,4,9,5,9,
            6,4,3,2,1,3,7,6,2
    };

    private static final int[] SET_8 = {
            2,9,7,8,1,7,3,1,5,
            7,6,2,9,4,5,6,5,3,
            5,9,4,7,2,1,2,1,2
    };

    private static final int[] SET_9 = {
            2,4,9,2,9,3,8,9,2,
            1,8,7,3,5,6,5,1,7,
            1,5,6,2,4,7,8,4,2
    };

    private static final int[] SET_10 = {
            5,5,9,2,1,6,1,2,1,
            8,3,4,3,5,7,8,3,6,
            1,5,2,1,8,2,3,5,7
    };

    private static final int[] SET_11 = {
            5,3,2,1,6,7,9,2,4,
            5,6,2,5,3,5,8,1,3,
            1,3,1,4,2,1,6,5,4
    };

    private static final int[] SET_12 = {
            3,4,4,4,9,3,4,7,4,
            5,6,9,7,9,1,8,5,1,
            3,8,5,4,1,2,4,3,2
    };

    private static final int[] SET_13 = {
            3,5,3,5,1,9,6,7,2,
            4,8,1,7,4,3,8,3,6,
            3,5,4,8,5,6,1,2,9
    };

    private static final int[] SET_14 = {
            3,6,4,7,3,4,3,7,2,
            5,1,5,9,5,1,8,6,9,
            2,7,8,2,4,7,5,3,3
    };

    private static final int[] SET_15 = {
            2,4,7,2,5,9,3,2,9,
            8,6,6,1,6,6,5,6,5,
            7,5,4,9,5,2,7,8,3
    };

    private static final int[] SET_16 = {
            6,1,4,1,6,7,3,4,3,
            8,9,3,8,5,1,6,2,1,
            4,5,7,1,6,5,3,5,6
    };

    private static final int[] SET_17 = {
            5,7,1,7,5,5,6,1,6,
            3,8,6,8,1,3,8,9,8,
            5,1,3,5,6,4,5,6,2
    };

    private static final int[] SET_18 = {
            2,4,3,2,6,2,7,3,1,
            6,9,5,9,5,1,8,6,8,
            1,3,2,6,4,7,5,9,3
    };

    private static final int[] SET_19 = {
            3,8,8,2,4,3,1,4,8,
            4,8,6,2,2,5,2,9,7,
            3,1,5,1,7,4,7,5,4
    };

    private static final int[] SET_20 = {
            7,2,5,8,4,8,9,1,3,
            4,9,6,9,6,7,5,2,5,
            6,3,7,2,5,9,4,6,3
    };

    private static final int[] SET_21 = {
            7,1,9,5,8,3,8,6,9,
            9,2,8,4,9,5,1,5,3,
            7,7,3,5,7,6,8,4,8
    };

    private static final int[] SET_22 = {
            6,4,5,6,8,5,4,5,1,
            3,9,7,6,9,3,7,9,2,
            8,6,8,5,2,4,5,4,7
    };

    private static final int[] SET_23 = {
            1,9,9,7,8,5,7,1,8,
            9,9,4,1,6,9,6,2,6,
            2,3,2,3,4,8,7,9,3
    };

    private static final int[] SET_24 = {
            6,4,6,7,2,1,5,1,2,
            1,3,4,5,4,3,2,1,8,
            5,2,9,2,9,5,1,9,7
    };

    private static final int[] SET_25 = {
            7,7,3,7,4,5,7,8,9,
            4,3,5,1,3,2,9,7,6,
            8,1,8,9,4,5,4,5,1
    };

    private static final int[] SET_26 = {
            7,4,9,3,2,7,8,9,7,
            9,4,1,1,9,4,5,6,8,
            5,6,4,3,2,7,9,2,1
    };

    private static final int[] SET_27 = {
            2,6,2,7,2,7,3,3,1,
            7,5,9,5,6,4,3,3,7,
            6,2,3,2,1,9,8,4,5
    };

    private static final int[] SET_28 = {
            3,7,6,2,5,7,8,6,9,
            4,9,5,1,4,9,5,7,3,
            6,7,8,4,8,1,8,1,6
    };

    private static final int[] SET_29 = {
            7,4,1,2,1,3,9,6,5,
            2,5,7,4,5,4,8,4,7,
            6,6,6,3,2,1,7,6,4
    };

    private static final int[] SET_30 = {
            1,3,1,8,9,1,5,4,7,
            9,4,5,6,3,1,9,2,5,
            3,8,1,7,8,6,1,1,4
    };

    private static final int[] SET_31 = {
            8,9,7,8,2,2,3,2,1,
            5,4,5,3,8,4,5,4,7,
            2,1,2,7,3,6,9,2,5
    };

    private static final int[] SET_32 = {
            3,9,6,7,6,1,4,7,6,
            2,5,8,7,8,9,2,9,3,
            8,6,9,6,9,1,7,5,8
    };

    private static final int[] SET_33 = {
            1,7,1,9,5,3,9,3,4,
            5,6,5,3,4,5,8,5,9,
            3,8,7,9,9,3,6,4,2,
    };

    private static final int[] SET_34 = {
            5,5,6,7,3,9,3,6,1,
            2,4,9,2,6,2,5,7,8,
            9,5,7,5,9,7,9,6,2
    };

    private static final int[] SET_35 = {
            9,5,5,9,3,2,3,6,1,
            7,6,4,1,6,1,5,8,7,
            2,8,5,3,5,3,2,4,5
    };

    private static final int[] SET_36 = {
            1,3,5,9,4,1,4,9,6,
            7,2,8,3,7,8,3,2,5,
            1,5,9,5,6,1,4,3,1
    };

    private static final int[] SET_37 = {
            2,3,5,9,5,3,6,9,7,
            6,8,1,4,7,1,3,8,4,
            9,3,5,2,3,2,7,3,9
    };

    private static final int[] SET_38 = {
            7,3,9,4,9,4,2,2,2,
            1,6,5,3,5,8,3,9,3,
            8,4,9,4,1,6,5,2,2
    };

    private static final int[] SET_39 = {
            9,5,3,1,3,2,5,6,4,
            7,8,6,5,8,9,6,4,9,
            4,5,1,7,6,1,4,6,7
    };

    private static final int[] SET_40 = {
            5,4,3,2,9,5,6,9,7,
            5,8,1,5,7,8,7,2,5,
            7,9,8,6,4,5,6,1,8
    };

    private static final int[] SET_41 = {
            8,4,2,6,5,9,1,7,6,
            1,6,1,4,1,7,4,5,2,
            5,7,8,3,1,1,9,7,6
    };

    private static final int[] SET_42 = {
            2,4,5,9,6,3,7,9,4,
            9,4,2,3,8,5,1,8,2,
            4,6,1,6,9,6,7,5,9
    };

    private static final int[] SET_43 = {
            5,7,5,4,2,4,9,2,9,
            8,6,9,8,1,2,3,5,4,
            3,5,1,5,2,7,6,2,8
    };

    private static final int[] SET_44 = {
            8,5,2,6,5,3,2,1,8,
            6,9,7,9,7,8,4,5,9,
            3,5,4,7,4,9,5,7,8
    };

    private static final int[] SET_45 = {
            8,5,2,6,5,3,2,1,8,
            6,9,7,9,7,8,4,5,9,
            3,5,4,7,4,9,5,7,8
    };

    private static final int[] SET_46 = {
            2,6,8,3,6,5,8,7,1,
            5,3,5,9,8,7,9,4,2,
            7,4,5,7,6,5,6,1,8
    };

    private static final int[] SET_47 = {
            9,6,9,6,2,1,5,6,8,
            7,4,1,7,1,4,3,2,4,
            5,2,5,2,5,8,5,1,7
    };

    private static final int[] SET_48 = {
            3,3,7,2,7,6,5,6,8,
            3,4,5,6,4,9,6,1,3,
            1,2,1,4,7,2,3,5,2
    };

    private static final int[] SET_49 = {
            6,8,6,5,2,5,3,3,6,
            5,1,3,9,4,5,7,7,2,
            9,8,5,2,3,2,8,4,5
    };

    private static final int[] SET_50 = {
            4,4,1,3,5,4,7,1,4,
            8,3,5,6,2,9,6,5,2,
            2,6,8,3,5,2,3,9,8
    };

    private static final int[] SET_51 = {
            3,1,6,1,8,3,8,9,5,
            5,2,3,4,4,9,7,4,7,
            1,4,1,5,4,5,2,8,1
    };

    private static final int[] SET_52 = {
            8,1,4,8,4,2,8,7,7,
            4,3,5,7,9,6,1,5,7,
            9,8,4,2,5,8,8,6,3
    };

    private static final int[] SET_53 = {
            3,6,3,8,7,8,9,8,7,
            5,2,5,6,4,3,5,4,5,
            8,9,1,2,5,1,6,9,7
    };

    private static final int[] SET_54 = {
            7,5,9,8,8,4,1,9,7,
            6,2,3,1,3,5,2,5,4,
            8,5,6,8,4,7,9,7,8
    };

    private static final int[] SET_55 = {
            3,6,7,6,3,4,9,3,4,
            1,4,5,2,8,5,1,2,5,
            2,3,5,7,3,6,7,6,9
    };

    private static final int[] SET_56 = {
            5,6,2,9,5,3,4,7,9,
            4,9,1,4,1,8,5,2,8,
            5,5,3,5,3,4,3,9,6
    };

    private static final int[] SET_57 = {
            8,7,5,7,5,3,5,7,6,
            9,6,9,1,2,6,1,1,4,
            5,8,5,8,3,1,7,5,1
    };

    private static final int[] SET_58 = {
            5,2,3,8,4,2,3,4,9,
            6,1,6,1,7,5,1,5,5,
            2,5,4,3,2,9,2,4,6
    };

    private static final int[] SET_59 = {
            4,1,8,7,1,2,3,6,1,
            8,3,9,5,6,7,7,5,7,
            4,6,3,2,9,5,3,8,4
    };

    private static final int[] SET_60 = {
            9,7,2,7,1,3,4,5,8,
            5,6,9,4,5,8,6,3,6,
            8,4,1,5,1,4,4,1,5
    };

    private static final int[] SET_61 = {
            2,1,6,5,6,1,5,2,5,
            4,7,9,8,7,2,6,9,3,
            6,8,5,2,6,3,5,2,1
    };

    private static final int[] SET_62 = {
            4,9,4,3,5,2,6,2,5,
            7,6,2,6,9,4,4,5,7,
            9,5,7,5,2,3,6,8,1
    };

    private static final int[] SET_63 = {
            6,2,2,8,9,3,5,7,3,
            9,8,8,4,5,8,6,2,9,
            5,7,2,1,3,1,5,1,7
    };

    private static final int[] SET_64 = {
            9,4,2,5,4,8,7,5,3,
            5,3,7,6,9,5,6,5,9,
            2,6,8,1,2,3,5,3,6
    };

    private static final int[] SET_65 = {
            3,1,3,8,8,6,1,8,9,
            8,5,6,4,9,7,5,4,5,
            3,2,3,2,5,2,6,7,3
    };

    private static final int[] SET_66 = {
            4,3,5,6,8,4,1,2,5,
            7,6,2,9,1,5,7,6,7,
            9,5,7,1,4,9,1,1,8
    };

    private static final int[] SET_67 = {
            5,5,8,1,2,1,4,3,4,
            8,7,6,3,5,3,2,1,8,
            9,3,8,4,9,8,4,3,9
    };

    private static final int[] SET_68 = {
            2,5,4,1,9,3,6,3,6,
            9,7,8,6,8,5,8,7,8,
            6,5,1,5,9,6,9,2,2
    };

    private static final int[] SET_69 = {
            3,8,4,5,9,2,7,4,9,
            2,5,9,7,5,1,5,8,5,
            7,5,4,8,4,2,6,3,6
    };

    private static final int[] SET_70 = {
            9,1,6,1,4,3,8,7,5,
            2,5,3,5,3,1,5,1,4,
            9,7,1,2,3,7,4,7,9
    };

    private static final int[] SET_71 = {
            3,5,2,4,2,2,4,9,4,
            9,2,6,9,8,1,7,2,7,
            6,3,5,2,3,5,3,1,5
    };

    private static final int[] SET_72 = {
            1,2,2,5,9,8,9,2,5,
            6,7,6,3,4,3,4,3,4,
            1,9,8,2,1,7,5,5,2
    };

    private static final int[] SET_73 = {
            3,4,7,4,3,4,3,6,4,
            9,2,5,8,1,8,1,8,9,
            1,1,4,3,1,2,3,6,3
    };

    private static final int[] SET_74 = {
            6,7,2,6,8,2,3,6,3,
            8,1,5,3,9,5,5,2,9,
            1,9,4,8,6,2,9,8,4
    };

    private static final int[] SET_75 = {
            8,9,2,6,5,4,8,9,8,
            1,4,7,9,7,5,3,6,5,
            8,6,3,2,4,8,2,1,2
    };

    private static final int[] SET_76 = {
            4,3,5,3,1,3,8,3,1,
            5,8,2,6,8,5,9,4,9,
            2,6,2,3,2,1,3,8,7
    };

    private static final int[] SET_77 = {
            6,3,6,8,5,9,3,6,3,
            1,9,5,7,4,5,9,2,7,
            2,4,8,1,2,5,5,6,5
    };

    private static final int[] SET_78 = {
            8,6,2,7,2,7,6,8,4,
            9,7,1,5,4,1,5,1,5,
            5,3,2,1,6,2,6,3,9
    };

    private static final int[] SET_79 = {
            7,8,3,1,7,7,4,6,3,
            4,9,6,2,5,9,6,8,9,
            3,5,3,1,7,6,7,3,1
    };

    private static final int[] SET_80 = {
            3,6,8,4,8,9,4,7,4,
            1,9,3,9,5,3,8,2,9,
            6,2,6,7,7,6,4,5,7
    };

    private static final int[] SET_81 = {
            9,6,2,1,8,4,5,4,1,
            2,1,1,5,3,9,8,7,2,
            4,3,6,2,9,7,4,7,9
    };

    private static final int[] SET_82 = {
            3,4,8,1,9,5,7,1,8,
            7,1,5,6,4,8,6,2,7,
            2,6,4,9,3,9,5,1,5
    };

    private static final int[] SET_83 = {
            1,5,2,4,6,6,7,1,5,
            4,4,6,4,8,1,6,9,2,
            3,1,3,5,7,5,8,3,4
    };

    private static final int[] SET_84 = {
            9,6,1,5,2,1,4,8,3,
            8,7,8,7,6,5,4,6,5,
            6,3,1,9,3,1,2,4,2
    };

    private static final int[] SET_85 = {
            8,1,4,6,2,7,9,4,7,
            5,3,7,9,5,4,5,2,5,
            8,7,3,8,2,3,2,1,6
    };

    private static final int[] SET_86 = {
            5,3,9,6,9,8,3,4,5,
            5,5,8,7,5,7,9,6,8,
            4,3,1,4,2,4,7,7,5
    };

    private static final int[] SET_87 = {
            5,2,8,8,9,2,5,7,3,
            4,3,9,2,4,3,6,8,4,
            9,6,5,7,5,5,1,7,1
    };

    private static final int[] SET_88 = {
            8,8,3,9,7,1,3,8,3,
            4,1,4,5,2,6,9,5,6,
            3,2,7,9,7,8,4,7,3
    };

    private static final int[] SET_89 = {
            9,7,2,1,9,8,2,3,8,
            6,5,3,8,7,6,5,9,5,
            3,1,4,1,5,2,3,1,8
    };

    private static final int[] SET_90 = {
            9,2,9,8,3,6,1,6,4,
            3,6,7,6,5,4,3,2,6,
            5,9,2,9,7,3,7,5,6
    };

    private static final int[] SET_91 = {
            3,5,3,6,8,9,3,7,1,
            6,3,9,4,3,7,2,8,5,
            9,5,3,8,7,9,7,6,3
    };

    private static final int[] SET_92 = {
            3,8,1,5,7,1,7,8,3,
            2,6,7,8,4,2,5,6,1,
            3,5,5,9,3,5,7,9,4
    };

    private static final int[] SET_93 = {
            6,3,6,1,5,3,4,9,2,
            1,2,5,2,9,6,5,4,6,
            5,4,7,4,8,3,1,4,4
    };

    private static final int[] SET_94 = {
            6,3,7,3,5,8,4,8,5,
            2,4,3,9,1,3,5,9,3,
            5,1,2,6,9,9,6,8,6
    };

    private static final int[] SET_95 = {
            6,4,7,5,4,3,4,5,2,
            3,7,9,3,8,1,5,7,1,
            8,4,5,5,4,7,4,2,4
    };

    private static final int[] SET_96 = {
            1,8,7,3,7,5,7,1,7,
            5,6,5,3,8,4,1,9,1,
            3,8,2,1,5,7,2,3,2
    };

    private static final int[] SET_97 = {
            5,7,5,7,9,1,1,1,8,
            6,9,2,6,5,8,3,1,1,
            2,7,5,8,7,9,4,1,1
    };

    private static final int[] SET_98 = {
            6,1,2,7,5,8,5,5,6,
            2,8,5,2,2,3,6,1,9,
            7,1,4,9,5,9,8,7,4
    };

    private static final int[] SET_99 = {
            3,5,8,5,4,7,9,7,5,
            9,4,6,6,5,8,5,2,6,
            8,2,1,9,7,4,3,1,7
    };

    private static final int[] SET_100 = {
            3,5,7,6,5,2,9,8,9,
            2,1,5,7,5,4,5,3,4,
            5,3,6,8,7,3,2,7,5
    };

    private static final int[] SET_101 = {
            5,6,1,6,3,9,2,7,8,
            2,4,5,8,5,6,1,5,4,
            4,7,2,1,7,8,3,8,9
    };

    private static final int[] SET_102 = {
            4,3,9,8,5,9,8,9,3,
            1,8,4,2,6,7,5,7,6,
            3,3,9,3,9,4,2,8,9
    };

    private static final int[] SET_103 = {
            9,3,8,7,6,9,5,2,9,
            2,4,9,4,2,1,3,6,4,
            6,8,7,5,3,5,2,5,3
    };

    private static final int[] SET_104 = {
            4,3,6,3,1,8,3,8,9,
            6,2,6,5,6,5,6,4,2,
            1,7,1,8,1,5,2,7,5
    };

    private static final int[] SET_105 = {
            6,9,7,8,5,4,8,2,8,
            5,2,5,4,1,2,3,9,6,
            2,4,9,2,6,7,5,8,7
    };

    private static final int[] SET_106 = {
           2,1,8,9,2,3,4,9,8,
           3,5,3,4,7,1,1,2,6,
           9,9,2,9,2,4,9,7,9
    };

    private static final int[] SET_107 = {
           1,5,8,9,8,5,3,7,5,
           4,6,3,4,3,5,2,9,6,
           3,1,2,5,5,1,7,5,4
    };

    private static final int[] SET_108 = {
            8,6,5,7,4,7,2,9,7,
            8,2,9,3,2,1,8,4,2,
            5,3,8,8,6,5,3,9,5
    };

    private static final int[] SET_109 = {
            4,2,7,2,7,5,2,1,8,
            5,1,4,5,9,7,8,6,7,
            3,2,5,7,6,5,9,4,1
    };

    private static final int[] SET_110 = {
            9,2,8,1,9,8,1,2,1,
            6,5,5,7,6,3,4,5,4,
            8,3,6,2,1,1,8,1,5
    };

    private static final int[] SET_111 = {
            5,9,6,1,7,3,9,6,1,
            3,8,3,2,8,1,5,8,7,
            6,5,6,1,5,7,6,9,3
    };

    private static final int[] SET_112 = {
            9,7,3,9,2,5,9,5,3,
            1,5,6,5,8,3,2,1,4,
            6,2,9,3,4,1,6,5,3
    };

    private static final int[] SET_113 = {
            6,5,4,1,8,4,1,5,1,
            2,3,2,3,2,7,9,8,9,
            4,1,1,5,9,9,4,5,6
    };

    private static final int[] SET_114 = {
            9,1,8,6,5,1,8,7,9,
            7,4,3,9,2,6,3,4,9,
            5,8,2,6,3,4,5,2,7
    };

    private static final int[] SET_115 = {
            2,7,8,1,2,3,9,7,4,
            1,6,5,3,9,8,5,6,9,
            2,9,8,4,5,4,9,8,2
    };

    private static final int[] SET_116 = {
            9,3,7,6,9,8,3,9,9,
            8,5,2,1,5,4,5,7,4,
            7,1,3,4,8,7,3,2,9
    };

    private static final int[] SET_117 = {
            6,5,3,5,7,1,6,2,3,
            1,4,1,9,8,5,5,7,5,
            2,9,5,7,6,1,6,9,2
    };

    private static final int[] SET_118 = {
            4,9,4,1,5,9,4,7,3,
            5,8,7,3,2,7,5,1,8,
            6,5,9,4,8,2,8,7,4
    };

    private static final int[] SET_119 = {
            3,6,9,6,7,2,5,6,4,
            2,5,8,5,1,2,6,7,6,
            3,9,3,2,3,5,3,1,5
    };

    private static final int[] SET_120 = {
            5,1,5,6,3,1,3,8,4,
            7,2,4,1,2,6,5,6,1,
            8,6,4,3,5,3,9,3,7
    };

    private static final int[] SET_121 = {
            5,7,5,5,8,5,4,2,7,
            9,6,8,1,6,7,3,6,1,
            1,3,5,7,5,1,8,5,9
    };

    private static final int[] SET_122 = {
            1,9,7,1,2,7,8,1,7,
            3,4,2,5,6,3,5,4,5,
            8,6,7,1,8,9,2,1,5
    };

    private static final int[] SET_123 = {
            7,1,7,3,1,8,1,8,9,
            3,9,8,3,7,4,7,5,7,
            2,5,6,1,5,8,2,4,9
    };

    private static final int[] SET_124 = {
            9,8,6,9,5,6,9,3,9,
            6,7,5,1,5,8,5,6,1,
            5,2,9,9,4,9,2,7,2
    };

    private static final int[] SET_125 = {
            6,6,5,4,5,9,3,6,9,
            3,6,9,7,1,6,5,8,5,
            4,7,5,2,7,2,9,4,9
    };

    private static final int[] SET_126 = {
            6,3,6,2,6,9,3,5,3,
            2,9,4,3,6,7,2,6,3,
            1,5,2,5,8,9,5,1,5
    };

    private static final int[] SET_127 = {
            1,6,3,3,9,2,7,9,8,
            9,7,8,4,7,4,5,6,3,
            2,6,5,1,2,9,6,4,5
    };

    private static final int[] SET_128 = {
            4,7,2,5,1,2,5,9,3,
            2,5,4,7,6,9,5,4,5,
            3,1,6,3,5,8,7,8,2
    };

    private static final int[] SET_129 = {
            6,3,9,7,2,5,2,8,4,
            2,1,4,5,4,8,9,3,2,
            7,2,7,1,7,5,4,5,2
    };

    private static final int[] SET_130 = {
            2,9,3,2,3,6,9,7,5,
            5,7,4,1,5,8,5,2,4,
            3,7,8,7,7,1,4,9,6
    };

    private static final int[] SET_131 = {
            2,5,9,1,7,1,4,6,5,
            9,4,8,5,8,4,8,7,4,
            7,3,9,7,6,4,1,5,1
    };

    private static final int[] SET_132 = {
            5,4,9,4,8,4,2,7,4,
            8,6,3,2,9,1,3,5,9,
            7,9,5,4,7,1,9,6,8
    };

    private static final int[] SET_133 = {
            7,5,4,9,7,5,1,7,5,
            1,8,6,2,9,8,9,6,9,
            2,3,1,8,4,7,5,2,3
    };

    private static final int[] SET_134 = {
            9,7,2,9,7,2,2,1,7,
            5,7,8,6,5,9,4,8,3,
            2,6,9,7,2,3,5,8,1
    };

    private static final int[] SET_135 = {
            9,7,5,1,8,1,3,2,6,
            2,4,3,5,7,5,6,8,1,
            7,9,2,6,5,1,7,5,9
    };

    private static final int[] SET_136 = {
            3,6,1,8,9,2,3,5,1,
            2,7,5,2,6,4,9,4,8,
            5,1,4,3,3,5,8,3,7
    };

    private static final int[] SET_137 = {
            7,9,6,6,3,5,2,7,8,
            6,3,5,9,5,1,4,1,5,
            8,5,6,2,7,6,3,8,6
    };

    private static final int[] SET_138 = {
            5,3,5,9,8,5,1,2,9,
            7,8,4,7,9,7,6,9,1,
            5,9,5,2,6,8,4,7,4
    };

    private static final int[] SET_139 = {
            1,4,1,1,9,7,1,4,1,
            5,8,9,3,5,6,8,7,5,
            8,8,2,6,9,2,1,6,1
    };

    private static final int[] SET_140 = {
            6,4,2,9,6,8,8,7,9,
            8,4,4,2,7,9,6,5,4,
            2,1,7,5,4,2,3,2,3
    };

    private static final int[] SET_141 = {
            4,9,3,2,8,5,2,7,4,
            2,5,4,5,3,6,1,5,6,
            8,3,2,1,2,9,3,6,6
    };

    private static final int[] SET_142 = {
            9,7,8,2,6,1,4,5,7,
            9,3,5,9,3,9,8,9,4,
            5,2,4,8,6,5,3,8,7
    };

    private static final int[] SET_143 = {
            2,6,9,2,5,8,2,7,6,
            1,4,5,7,1,9,1,5,2,
            3,7,9,6,8,5,3,9,7
    };

    private static final int[] SET_144 = {
            9,4,9,5,5,8,5,6,7,
            9,2,8,4,9,4,7,2,5,
            9,1,5,3,8,3,1,6,7
    };

    private static final int[] SET_145 = {
            7,1,9,2,7,9,8,4,5,
            4,8,5,6,8,6,7,1,5,
            3,7,2,3,9,5,8,6,8
    };

    private static final int[] SET_146 = {
            1,8,7,5,6,9,7,6,8,
            6,4,2,4,1,2,5,9,5,
            4,4,7,5,3,6,3,6,3
    };

    private static final int[] SET_147 = {
            9,3,9,5,8,9,5,3,2,
            5,7,4,7,1,6,9,5,6,
            9,8,1,5,2,8,3,1,2
    };

    private static final int[] SET_148 = {
            6,9,7,1,2,3,9,6,8,
            4,8,1,4,9,7,8,5,9,
            7,5,3,2,3,3,6,7,6
    };

    private static final int[] SET_149 = {
            6,1,8,9,8,7,4,6,2,
            9,5,4,5,5,9,8,5,9,
            3,6,9,7,5,5,7,4,8
    };

    private static final int[] SET_150 = {
            7,6,1,5,5,1,5,1,8,
            3,5,9,5,4,8,3,6,5,
            2,4,7,8,5,6,9,2,1
    };

    private static final int[] SET_151 = {
            4,8,1,3,6,5,4,1,5,
            1,3,9,4,7,6,7,8,3,
            6,2,5,8,5,9,3,4,9
    };

    private static final int[] SET_152 = {
            8,7,1,4,3,8,3,8,7,
            9,5,8,9,5,1,5,4,3,
            8,7,2,6,4,8,7,2,6
    };

    private static final int[] SET_153 = {
            5,7,2,4,5,6,1,3,5,
            9,4,3,9,8,4,5,8,2,
            6,2,5,4,5,7,7,1,5
    };

    private static final int[] SET_154 = {
            3,1,4,3,1,9,5,1,5,
            6,5,2,5,2,3,6,8,3,
            9,4,8,7,1,5,2,4,5
    };

    private static final int[] SET_155 = {
            2,7,3,1,7,8,3,8,7,
            3,9,2,3,5,9,5,6,9,
            4,6,5,1,8,3,7,8,3
    };

    private static final int[] SET_156 = {
            6,8,9,3,7,1,3,6,9,
            7,5,6,1,2,4,6,2,5,
            2,9,2,5,8,1,6,7,9
    };

    private static final int[] SET_157 = {
            2,4,9,9,4,3,4,3,6,
            1,7,2,3,1,8,5,9,8,
            6,9,5,6,7,9,2,3,4
    };

    private static final int[] SET_158 = {
            2,3,1,4,5,8,1,8,2,
            6,5,9,2,9,9,1,7,5,
            9,4,7,6,3,2,5,8,1
    };

    private static final int[] SET_159 = {
            1,3,9,2,6,4,7,1,8,
            7,4,5,7,5,1,6,4,7,
            5,2,9,2,6,7,9,5,5
    };

    private static final int[] SET_160 = {
            9,4,1,4,3,4,3,1,4,
            7,8,2,5,1,8,8,6,8,
            5,6,9,3,8,4,2,2,9
    };

    private static final int[] SET_161 = {
            5,3,3,2,4,2,3,8,5,
            6,5,5,5,1,7,5,4,7,
            8,1,8,6,8,6,9,1,8
    };

    private static final int[] SET_162 = {
            6,8,3,8,9,5,3,5,4,
            9,3,4,7,3,8,4,9,3,
            1,8,1,9,6,2,3,2,6
    };

    private static final int[] SET_163 = {
            6,1,5,6,1,5,4,8,3,
            9,2,7,9,2,8,7,9,6,
            7,6,5,8,5,2,4,3,8
    };

    private static final int[] SET_164 = {
            7,3,6,5,4,1,7,8,9,
            6,9,5,9,8,9,7,6,3,
            5,2,8,6,7,6,8,9,5
    };

    private static final int[] SET_165 = {
            9,2,7,6,1,2,4,7,6,
            5,3,7,7,5,7,8,8,9,
            2,1,4,8,2,6,9,6,8
    };

}
