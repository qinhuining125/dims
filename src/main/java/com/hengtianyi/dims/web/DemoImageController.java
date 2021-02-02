package com.hengtianyi.dims.web;
import com.hengtianyi.common.core.constant.BaseConstant;
import com.hengtianyi.common.core.feature.ServiceResult;
import com.hengtianyi.common.core.util.JsonUtil;
import com.hengtianyi.dims.config.CustomProperties;
import com.hengtianyi.dims.constant.FrameConstant;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/web/DemoImageController")
public class DemoImageController {


    @Resource
    private CustomProperties customProperties;

    @RequestMapping("/hello111")
    public String helloSpringBoot() {
        return "Hello SpringBoot Proje2222222222222ct.";
    }
    /**
     * 单张图片上传
     */
    @RequestMapping(path = "/save_image", method = {RequestMethod.POST})
    public String addDish(@RequestParam("img_file") MultipartFile file, HttpServletRequest request) throws Exception {
        String path = null;// 文件路径
        double fileSize = file.getSize();
        System.out.println("文件的大小是"+ fileSize);
        byte[] sizebyte=file.getBytes();
        System.out.println("文件的byte大小是"+ sizebyte.toString());
        if (file != null) {// 判断上传的文件是否为空
            String type = null;// 文件类型
            String fileName = file.getOriginalFilename();// 文件原名称
            System.out.println("上传的文件原名称:" + fileName);
            // 判断文件类型
            type = fileName.indexOf(".") != -1 ? fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()) : null;
            if (type != null) {// 判断文件类型是否为空
                if ("GIF".equals(type.toUpperCase()) || "PNG".equals(type.toUpperCase()) || "JPG".equals(type.toUpperCase())) {
                    // 项目在容器中实际发布运行的根路径
//                    String realPath = request.getSession().getServletContext().getRealPath("/");
                    File files = new File("");
                    String test= files.getCanonicalPath();
                    System.out.println("跟路径"+test);
                    // 自定义的文件名称
                    String trueFileName = String.valueOf(System.currentTimeMillis()) + "." + type;
                    System.out.println("图片:" + trueFileName);
                    // 设置存放图片文件的路径
                    path = test+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"static" +
                            File.separator+"upload"+File.separator+trueFileName;
                    System.out.println(   String.valueOf(System.currentTimeMillis()));
                    // 转存文件到指定的路径
                    file.transferTo(new File(path));
                    System.out.println("文件成功上传到指定目录下");

                    return "文件成功上传到指定目录下";
                }

            } else {
                System.out.println("不是我们想要的文件类型,请按要求重新上传");
                return "不是我们想要的文件类型,请按要求重新上传";
            }
        } else {
            System.out.println("文件类型为空");
            return "文件类型为空";
        }

        return "已经成功上传到指定目录";
    }
/*
* 多图上传
* */
    @PostMapping(value = "/filesUpload", produces = BaseConstant.JSON)
    public  String filesUpload(@RequestParam("myfiles") MultipartFile[] files,
                              HttpServletRequest request) {
        List<String> list = new ArrayList<String>();
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                // 保存文件
                list = saveFile(request, file, list);
            }
        }
        //写着测试，删了就可以
        for (int i = 0; i < list.size(); i++) {
//            System.out.println("集合里面的数据" + list.get(i));
        }
        System.out.println("返回数据"+list);
        return JsonUtil.toJson(list);
    }

    private List<String> saveFile(HttpServletRequest request,
                                  MultipartFile file, List<String> list) {
        // 判断文件是否为空
        if (!file.isEmpty()) {
            try {
                // 保存的文件路径(如果用的是Tomcat服务器，文件会上传到\\%TOMCAT_HOME%\\webapps\\YourWebProject\\upload\\文件夹中
                // )
//                String test= file.getCanonicalPath();
                String fileName = file.getOriginalFilename();// 文件原名称
                // 判断文件类型
                String  type = fileName.indexOf(".") != -1 ? fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()) : null;
                File files = new File("");
                String test= files.getCanonicalPath();
                String trueFileName = String.valueOf(System.currentTimeMillis()) +file.getOriginalFilename();
                String filePath = files.getCanonicalPath()
                        + "/src/main/resources/static/upload/" +trueFileName;
                String relativeFilePath= "static/upload/" +trueFileName;
                list.add(relativeFilePath);
                File saveDir = new File(filePath);
                if (!saveDir.getParentFile().exists())
                    saveDir.getParentFile().mkdirs();

                // 转存文件
                file.transferTo(saveDir);
                return list;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }


    @RequestMapping(value = "/uploadMult", method = RequestMethod.POST)
    @ResponseBody
    public List uploadMult(HttpServletRequest request) throws IOException {
        // 转型为MultipartHttpRequest：
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 获得文件：
        List<MultipartFile> files = multipartRequest.getFiles("file1");
        System.out.println( "-->" );
        System.out.println(files);
        if (files.isEmpty()) {
            ArrayList errlist=new ArrayList();
            errlist.add("falst");
            errlist.add("图片为空");
            return errlist;
        }
        File fileTemp = new File("");
        String staticPath = FrameConstant.PIC_STATIC;
        String locationPath = customProperties.getUploadPath();

        List<String> list = new ArrayList<String>();
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            String trueFileName = String.valueOf(System.currentTimeMillis()) +fileName;
            String relativeFilePath= staticPath +  "/" + trueFileName;
            list.add(relativeFilePath);
            int size = (int) file.getSize();
            System.out.println(trueFileName + "-->" + size);
            if (file.isEmpty()) {
                ArrayList errlist=new ArrayList();
                errlist.add("falst");
                errlist.add("图片为空");
                return errlist;
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
                    ArrayList errlist=new ArrayList();
                    errlist.add("falst");
                    errlist.add("服务器异常");
                    return errlist;
                }
            }
        }
        return list;
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
        List<MultipartFile> files = multipartRequest.getFiles("file1");
        System.out.println( "-->" );
        System.out.println(files);
        if (files.isEmpty()) {
            result.setError("图片为空");
            return result;
        }
        File fileTemp = new File("");
//        String path= request.getServletContext().getRealPath("WEB-INF/classes/static/upload");
//        String path= fileTemp.getCanonicalPath()+ "/src/main/resources/static/upload/";

        String staticPath = FrameConstant.PIC_STATIC;
        String locationPath = customProperties.getUploadPath();

        List<String> list = new ArrayList<String>();
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            String trueFileName = String.valueOf(System.currentTimeMillis()) +fileName;
//            String relativeFilePath= "static/upload/" +trueFileName;
            String relativeFilePath= staticPath +  "/" + trueFileName;
            list.add(relativeFilePath);
            int size = (int) file.getSize();
            System.out.println(trueFileName + "-->" + size);
            if (file.isEmpty()) {
                result.setError("图片为空");
                return result;
            } else {
//                File dest = new File(path + "/" + trueFileName);
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