package com.flying.dragon.Biz.Params.TestParams.in;

import com.flying.dragon.Base.Annotation.BizType;
import com.flying.dragon.Biz.BizType.TestBizEnum;
import com.flying.dragon.Biz.Params.CommonInParams;
import org.springframework.http.codec.multipart.FilePart;

/**
 * @描述 文件上传测试的入参
 **/
@BizType(testBiz = TestBizEnum.TEST_FILE_UPLOAD)
public class UploadFileInParams extends CommonInParams {
    /** 用户上传的文件列表 */
    FilePart[] files;

    public FilePart[] getFiles() {
        return files;
    }

    public void setFiles(FilePart[] files) {
        this.files = files;
    }
}
