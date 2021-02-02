package com.hengtianyi.dims.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value = "/web/UploadController")
public class UploadController {
    // 因为uploadPage.jsp 在WEB-INF下，不能直接从浏览器访问，所以要在这里加一个uploadPage跳转，这样就可以通过
    @RequestMapping("/uploadPage")
    public String uploadPage() {
        return "uploadPage";   //过度跳转页
    }
    @RequestMapping("/hello")
    public String helloSpringBoot() {
        return "Hello SpringBoot Project.";
    }
    @PostMapping("/upload") // 等价于 @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String uplaod(HttpServletRequest req, @RequestParam("file") MultipartFile file, Model m) {//1. 接受上传的文件  @RequestParam("file") MultipartFile file
        try {
            //2.根据时间戳创建新的文件名，这样即便是第二次上传相同名称的文件，也不会把第一次的文件覆盖了
            String fileName = System.currentTimeMillis() + file.getOriginalFilename();
            //3.通过req.getServletContext().getRealPath("") 获取当前项目的真实路径，然后拼接前面的文件名
            String destFileName = req.getServletContext().getRealPath("") + "uploaded" + File.separator + fileName;
            //4.第一次运行的时候，这个文件所在的目录往往是不存在的，这里需要创建一下目录（创建到了webapp下uploaded文件夹下）
            File destFile = new File(destFileName);
            destFile.getParentFile().mkdirs();
            //5.把浏览器上传的文件复制到希望的位置
            file.transferTo(destFile);
            //6.把文件名放在model里，以便后续显示用
            m.addAttribute("fileName", fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "上传失败," + e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            return "上传失败," + e.getMessage();
        }

        return "showImg";
    }
}