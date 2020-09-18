package com.powernode.util;

import java.util.Collection;

/**
 * @Author AlanLin
 * @Description
 * @Date 2020/8/31
 */
public class JudgeUtils {
    public static boolean collectionIsNull(Collection collection){
        return collection==null||collection.size()==0;
    }

    public static boolean conditionJudge(Object obj){
        if (obj!=null&&!"".equals(obj)){
            return true;
        }
        return false;
    }
}
