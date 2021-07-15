package com.hengtianyi.dims.web;

import com.hengtianyi.common.core.feature.ServiceResult;
import com.hengtianyi.dims.config.CustomProperties;
import com.hengtianyi.dims.constant.FrameConstant;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/web/DemoVideoController")
public class DemoVideoController {


    @Resource
    private CustomProperties customProperties;

    @RequestMapping("/hello3333")
    public String helloSpringBoot() {
        return "Hello SpringBoot Proje333333333ct.";
    }

    /**
     * app 端接口
     * @param request
     * @return
     * @throws IOException
     */

    @RequestMapping(value = "/appUploadMult", method = RequestMethod.POST)
    @ResponseBody
    public Object appUploadMult(HttpServletRequest request) throws IOException {
        ServiceResult<Object> result = new ServiceResult<>();
        // 转型为MultipartHttpRequest：
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 获得文件：
        List<MultipartFile> files = multipartRequest.getFiles("fileVideo1");
        System.out.println( "-->" );
        System.out.println(files);
        if (files.isEmpty()) {
            result.setError("视频为空");
            return result;
        }
        File fileTemp = new File("");

        String staticPath = FrameConstant.PIC_STATIC;
        String locationPath = customProperties.getUploadPath();

        List<String> list = new ArrayList<String>();
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            String trueFileName = String.valueOf(System.currentTimeMillis()) +fileName;
            String relativeFilePath= staticPath + "/" + trueFileName;
            list.add(relativeFilePath);
            int size = (int) file.getSize();
            System.out.println(trueFileName + "-->" + size);
            if (file.isEmpty()) {
                result.setError("视频为空");
                return result;
            } else {
                File dest = new File(locationPath + "/" + trueFileName);
                if (!dest.getParentFile().exists()) { // 判断文件父目录是否存在
                    dest.getParentFile().mkdir();
                }
                try {
                    file.transferTo(dest);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    result.setError("服务器异常");
                    return result;
                }
            }
        }
        result.setResult(list);
        result.setSuccess(true);
        System.out.println(result);
        return result.toJson();
    }
}