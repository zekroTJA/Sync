package utils;

import commands.Debug;

/**
 * Created by zekro on 15.10.2017 / 15:55
 * MCCore.utils
 * dev.zekro.de - github.zekro.de
 * © zekro 2017
 */

public class Dbg {

    public static void out(String content) {
        if (Debug.debug)
            System.out.println(content);
    }

}
