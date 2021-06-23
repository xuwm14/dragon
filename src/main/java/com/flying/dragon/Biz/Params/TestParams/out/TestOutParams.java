package com.flying.dragon.Biz.Params.TestParams.out;

import com.flying.dragon.Biz.Params.CommonOutParams;

import java.util.List;
import java.util.Map;

/**
 * @描述 出参测试，出参的格式非常灵活，所有可以被JSON序列化的对象都可以直接用作出参
 **/
public class TestOutParams extends CommonOutParams {
    /** 普通类型的出参 */
    private int num;
    /** 数组出参 */
    private double[] money;
    /** 列表出参 */
    private List<String> names;
    /** map出参 */
    private Map<String, Integer> studentId;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double[] getMoney() {
        return money;
    }

    public void setMoney(double[] money) {
        this.money = money;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public Map<String, Integer> getStudentId() {
        return studentId;
    }

    public void setStudentId(Map<String, Integer> studentId) {
        this.studentId = studentId;
    }
}
