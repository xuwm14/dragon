package com.flying.dragon.Biz.Params.TestParams.in;

import com.flying.dragon.Base.Annotation.BizType;
import com.flying.dragon.Biz.BizType.TestBizEnum;
import com.flying.dragon.Biz.Params.CommonInParams;
import org.springframework.http.codec.multipart.FilePart;

/**
 * @描述 多种类型输入参数的测试
 **/
@BizType(testBiz = TestBizEnum.TEST_PARAMS)
public class TestInParams extends CommonInParams {
    /** 整数 */
    int id;
    /** 浮点数 */
    float num;
    /** 字符串数组 */
    String[] names;
    /** POST请求中的文件参数 */
    FilePart file;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getNum() {
        return num;
    }

    public void setNum(float num) {
        this.num = num;
    }

    public String[] getNames() {
        return names;
    }

    public void setNames(String[] names) {
        this.names = names;
    }

    public FilePart getFile() {
        return file;
    }

    public void setFile(FilePart file) {
        this.file = file;
    }
}
