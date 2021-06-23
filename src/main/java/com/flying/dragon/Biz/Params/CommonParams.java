package com.flying.dragon.Biz.Params;

import com.alibaba.fastjson.JSONObject;
import com.flying.dragon.Frame.Util.ParseUtil;

import java.lang.reflect.Field;

/**
 * @描述 通用参数，主要是封装一些通用操作
 **/
public abstract class CommonParams {
    /** 在转换为jsonObject对象前进行的操作 */
    protected abstract void beforeTransfer();

    /** 转换为json对象 */
    public JSONObject toJsonObject() throws Exception {
        beforeTransfer();
        JSONObject jsonObject = new JSONObject();
        /** 反射获取所有的内部属性，然后设置到json中 */
        Field[] fields = ParseUtil.getAllFields(getClass());
        for (Field field : fields) {
            field.setAccessible(true);
            jsonObject.put(field.getName(), field.get(this));
        }
        return jsonObject;
    }

    /** 重写转换字符串操作 */
    @Override
    public String toString() {
        try {
            beforeTransfer();
            return toJsonObject().toString();
        } catch (Exception e) {
            return null;
        }
    }
}
