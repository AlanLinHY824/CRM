package com.powernode.workbench.Comparator;

import com.powernode.workbench.pojo.TblDicValue;

import java.util.Comparator;

/**
 * @Author AlanLin
 * @Description
 * @Date 2020/9/4
 */
public class DicValueComparator implements Comparator<TblDicValue> {
    /**
     * TreeSet中，新加入的元素为o1，保持本方法返回值与实际大小情况一致，即为顺序存储
     * @param o1
     * @param o2
     * @return
     */
    @Override
    public int compare(TblDicValue o1, TblDicValue o2) {
        return o1.getOrderno().compareTo(o2.getOrderno());
    }
}
