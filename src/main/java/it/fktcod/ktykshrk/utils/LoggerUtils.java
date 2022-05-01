package it.fktcod.ktykshrk.utils;

import org.apache.logging.log4j.LogManager;

public class LoggerUtils{

    public static void info(String string){
        LogManager.getLogger("ensemble").info(string);
    }

}
