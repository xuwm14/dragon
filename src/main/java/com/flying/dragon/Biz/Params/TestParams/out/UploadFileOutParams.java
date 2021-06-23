package com.flying.dragon.Biz.Params.TestParams.out;

import com.flying.dragon.Biz.Params.CommonOutParams;

import java.util.List;

/**
 * @描述 文件上传测试的出参
 **/
public class UploadFileOutParams extends CommonOutParams {
    // 上传文件的个数
    private int num;
    // 上传文件的存储路径
    private List<String> paths;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<String> getPaths() {
        return paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }
}
