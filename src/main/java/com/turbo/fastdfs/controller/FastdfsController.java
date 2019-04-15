package com.turbo.fastdfs.controller;

import com.github.tobato.fastdfs.conn.FdfsWebServer;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.proto.storage.DownloadCallback;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.rmi.server.ExportException;

/**
 * @author lingc
 * 文件上传下载
 */
@Controller
@RequestMapping
public class FastdfsController {

    @Autowired
    FastFileStorageClient fastFileStorageClient;

    @Autowired
    FdfsWebServer fdfsWebServer;

    /**
     * 文件上传
     * @param multipartFile
     * @return
     * @throws Exception
     */

    @PostMapping("/upload")
    @ResponseBody
    public String upload(MultipartFile multipartFile)throws Exception {
        StorePath storePath=fastFileStorageClient.uploadFile(multipartFile.getInputStream(),multipartFile.getSize(), FilenameUtils.getExtension(multipartFile.getOriginalFilename()),null);
        return  fdfsWebServer.getWebServerUrl()+"/"+storePath.getFullPath();
    }
    @PostMapping("/noauth/upload")
    @ResponseBody
    public String uploadWithoutAuth1(MultipartFile multipartFile)throws Exception {
        StorePath storePath=fastFileStorageClient.uploadFile(multipartFile.getInputStream(),multipartFile.getSize(), FilenameUtils.getExtension(multipartFile.getOriginalFilename()),null);
        return  fdfsWebServer.getWebServerUrl()+"/"+storePath.getFullPath();
    }
    /**
     * 删除文件
     * @param fileUrl 文件访问地址
     * @return
     */
    @DeleteMapping("/delete")
    public String deleteFile(String fileUrl) {
        StorePath storePath = StorePath.praseFromUrl(fileUrl);
        fastFileStorageClient.deleteFile(storePath.getGroup(), storePath.getPath());
        return "SUCCESS";
    }


//    /**
//     * 文件下载
//     * @param response
//     * @param fileUrl
//     * @throws Exception
//     * fileUrl: group/M00/00/00/wKgChFyu-7mATItcAAEeDY79-Hg250.png
//     */
//    @PostMapping("download")
//    public void download(HttpServletResponse response, String fileUrl)throws Exception {
//        String group = fileUrl.substring(0, fileUrl.indexOf("/"));
//        String path = fileUrl.substring(fileUrl.indexOf("/") + 1);
//        String fileName=fileUrl.substring(fileUrl.lastIndexOf("/")+1);
//       byte[] bytes= fastFileStorageClient.downloadFile(group, path, new DownloadByteArray());
//       try {
//           response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
//           response.setCharacterEncoding("UTF-8");
//           response.getOutputStream().write(bytes);
//       }finally {
//           response.getOutputStream().flush();
//           response.getOutputStream().close();
//       }
//    }
//    @RequestMapping("/view")
//    //做伪路径测试
//    public void browerFile(HttpServletResponse response,String fileName)throws  Exception{
//        response.setHeader("X-Accel-Redirect","/img/M00/00/00/"+fileName);
//       response.getOutputStream().flush();
//        response.getOutputStream().close();
//    }

}
