package it.fktcod.ktykshrk.managers;

import it.fktcod.ktykshrk.Core;
import it.fktcod.ktykshrk.module.Module;
import it.fktcod.ktykshrk.value.Value;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class TranslationManager {
    public static String settingsTranslationPath = "C:\\settings.translation.ensemble";
    public static String modulesTranslationPath = "C:\\settings.translation.ensemble";

    public static void setValuesTranslation(){

        String Translation = read(settingsTranslationPath);
        String[] Translations = Translation.split("@hh=X=hh=X=x@");
        for (int i = 0; i < Translations.length ; i++) {
            String[] TranslationMVS = Translations[i].split(";");

            if (TranslationMVS.length == 3 ){
                if (TranslationMVS[0] != null){
                    Module m = Core.hackManager.getHack(TranslationMVS[0]);
                    if (TranslationMVS[1] != null){
                        Value value = m.getValueByName(TranslationMVS[1]);
                        if (TranslationMVS[2] != null){
                            if (value != null){
                                value.setChinese(TranslationMVS[2]);
                            }
                        }
                    }

                }

            }
        }

    }

    public static String read(String filePath) {

        try {
            File file = new File(filePath);
            if(file.isFile() && file.exists()) {
                InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String lineTxt = null;
                while ((lineTxt = br.readLine()) != null) {
                    return lineTxt;
                }
                br.close();
            } else {
                File Temp = new File(filePath);
                Temp.mkdir();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
