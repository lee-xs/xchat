package cn.lixinblog.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtils {

    public static String parseDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/M/d h:m:s");
        return sdf.format(new Date());
    }

}
