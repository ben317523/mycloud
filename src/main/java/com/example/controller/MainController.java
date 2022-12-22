package com.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Lists;
import com.example.entity.ApiResponse;

import com.example.entity.User;
import com.example.service.FileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.*;
import org.springframework.util.FileCopyUtils;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RestController
@CrossOrigin("*")
public class MainController {
    @Autowired
    private ResourceLoader resourceLoader;


    @Autowired
    private FileService fileService;

    public static List<User> users;
    public static String token = "dsgufn-d626sfsdfJHiI-Nnfvas94832u9f-h8e9q-UGubgyuGSg-7sASYAHya-8ashOIHA-8ya8iHDT7id-hui";

    static {
        String os = System.getProperty("os.name");
        System.out.println("System os : "+os);
        if (os.toLowerCase().matches(".*"+"linux"+".*")) {
            File basePath = new File("/data/files");
            if (!basePath.exists())
                basePath.mkdirs();

            File tempPath = new File("/data/temp");
            if (!tempPath.exists())
                tempPath.mkdirs();

            //Onedrive
            File oneBasePath = new File("/root/OneDrive/data/files");
            if (!oneBasePath.exists())
                oneBasePath.mkdirs();

            // File oneTempPath = new File("/root/OneDrive/data/temp");
            // if (!oneTempPath.exists())
            //     oneTempPath.mkdirs();
        }
        users = Collections.synchronizedList(new ArrayList<>());
        users.add( new User(0,"ben","89762230"));
        users.add( new User(1,"guest","123456"));

        for(User user : users){
            File path = new File("/data/files/"+user.getUserName());
            if (!path.exists())
                path.mkdirs();

            //Onedrive
            File onedrivePath = new File("/root/OneDrive/data/files/"+user.getUserName());
            if (!onedrivePath.exists())
                onedrivePath.mkdirs();
        }
    }



    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public ApiResponse fileUpload(@RequestParam("file") MultipartFile file,
                                  HttpServletRequest request) throws IOException {
        String bearerToken = request.getHeader("Authorization").split("\\s+")[1];

        if (bearerToken != null && token.equals(bearerToken)){}
        else
            return ApiResponse.fail("Unauthorized");

        boolean success = fileService.uploadFile(file, "/data/files/" + file.getOriginalFilename().replaceAll("[\\s\\[\\]{}]+", ""));
        if (success)
            return ApiResponse.success("File is upload successfully", null);
        else
            return ApiResponse.fail("Upload unsuccessful");
    }

    @RequestMapping(value = "/privateUpload",method = RequestMethod.POST)
    @CrossOrigin("*")
    public ApiResponse privateUpload(@RequestParam("file") MultipartFile file,
                                     @RequestParam(value = "name") String name,
                                     HttpServletRequest request) throws IOException {
        String bearerToken = request.getHeader("Authorization").split("\\s+")[1];

        if (bearerToken != null && token.equals(bearerToken)) {}
        else
            return ApiResponse.fail("Unauthorized");

        File userPath = new File("/data/files/"+name);
        if (!userPath.exists())
            userPath.mkdir();

        boolean success = fileService.uploadFile(file, "/data/files/"+name+"/"+file.getOriginalFilename().replaceAll("[\\s\\[\\]{}]+",""));

        if (success)
            return ApiResponse.success("File is upload successfully",null);
        else
            return ApiResponse.fail("Unsuccessfully");
    }

    @RequestMapping(value = "/download")
    public ResponseEntity download(@RequestParam("param") String param,
                                   @RequestParam(value = "isPublic",required = false,defaultValue = "true")Boolean isPublic,
                                   @RequestParam(value = "name",required = false) String name,
                                   @RequestParam(value = "isInline", required = false, defaultValue = "false")Boolean isInline,

                                   HttpServletRequest request) throws IOException {
        String authorizationHeader = request.getHeader("Authorization");
        String bearerToken = null;

        if (authorizationHeader != null) {
            bearerToken = authorizationHeader.split("\\s+")[1];
        } else {
            bearerToken = search(request.getCookies())!=null?search(request.getCookies()).getValue() : null;
            if (bearerToken == null){
                bearerToken = request.getParameter("token");
            }
        }

        if (bearerToken != null && token.equals(bearerToken))
            System.out.println("valid token");
        else
            return ResponseEntity.status(403).body("Unauthorized");


        try {
            File folder = null;
            if (!isPublic){
                folder = new File("/data/files/"+name);
            } else {
                folder = new File("/data/files");
            }
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File f, String name) {
                    Pattern pattern = Pattern.compile(".*"+param.trim().replaceAll("[\\(\\)]+",".")+".*",Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(name);
                    return matcher.find();
                }
            };
            String[] searchedName = folder.list(filter);
            File file = new File("/data/files/"+(isPublic?"":(name+"/"))+((searchedName.length!=0)?searchedName[0]:"") );

            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            HttpHeaders headers = new HttpHeaders();
            if (isInline){
                ContentDisposition inlineContentDisposition = ContentDisposition
                        .inline()
                        .filename(((searchedName!=null)?encode(searchedName[0]):"")).build();
                headers.setContentDisposition(inlineContentDisposition);
            } else {
                headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", ((searchedName!=null)?encode(searchedName[0]):"")));
            }
            headers.setAccessControlExposeHeaders(Lists.newArrayList("Content-Disposition"));
//            headers.add("Access-Control-Expose-Headers", "Content-Disposition");

            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

        } catch (FileNotFoundException e) {
            return ResponseEntity.status(404)
                    .body("File not found");
        }

    }

    @RequestMapping("/downloadPrivate")
    public ResponseEntity downloadPrivate(@RequestParam("name") String fileName,
                                          @RequestParam("uname") String name,
                                          @RequestHeader(value = "Authorization") String bearerToken,
                                          HttpServletRequest request) throws IOException {
        if (search(request.getCookies()) != null && token.equals(search(request.getCookies()).getValue()))
            System.out.println("valid token");
        else
            return ResponseEntity.status(403).body("Unauthorized");


        try {
            File folder = new File("/data/files/"+name);
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File f, String name) {
                    Pattern pattern = Pattern.compile(".*"+fileName.trim().replaceAll("[\\(\\)]+",".")+".*",Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(name);
                    return matcher.find();
                }
            };
            String[] searchedName = folder.list(filter);
            File file = new File("/data/files/"+name+"/"+((searchedName.length!=0)?searchedName[0]:"") );

            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", ((searchedName!=null)?encode(searchedName[0]):"")));

            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

        } catch (FileNotFoundException e) {
            return ResponseEntity.status(404)
                    .body("File not found");
        }

    }


    @RequestMapping("/downloadProxy")
    @ResponseBody
    public ApiResponse downloadProxy(@RequestParam(value = "url") String url,
                                @RequestParam(value = "name", required = false, defaultValue = "")String targetName,
                                @RequestParam(value = "isPublic",required = false,defaultValue = "true")Boolean isPublic,
                                @RequestParam(value = "uname",required = false)String uname,
                                @RequestHeader(value = "Authorization") String bearerToken,
                                HttpServletRequest request) throws FileNotFoundException {

        bearerToken = bearerToken.split("\\s+")[1];

        if (bearerToken != null && token.equals(bearerToken))
        {}
        else
            return ApiResponse.fail("Unauthorized");

        if (targetName == null || "".equals(targetName)){
            if (url.endsWith(".m3u8")){
                String[] split = url.split("/");
                targetName = "video_" + split[split.length-1].substring(0, split[split.length-1].length()-5) + "_" + System.currentTimeMillis();
            } else {
                String[] split = url.split("/");
                targetName = split[split.length-1];
            }
        }

        //init temp log file
        File tempLogFile = new File("/data/temp/log_" + targetName + ".log");

        if (!isPublic)
            targetName = uname + "/" + targetName;

        File uPath = new File("/data/files/" + (uname!=null?uname:""));
        if (!uPath.exists())
            uPath.mkdirs();

        String finalTargetName = targetName;

        CompletableFuture.runAsync(() -> {
            Runtime run = Runtime.getRuntime();

            String agent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 11_1_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.182 Safari/537.36";

            String[] cmd1 = {"ffmpeg", "-user_agent", agent ,  "-i", url, "-c", "copy", "-bsf:a", "aac_adtstoasc", "/data/files/"+ finalTargetName +".mp4" };

            ProcessBuilder p1 = new ProcessBuilder("wget","-q","-O","/data/files/"+ finalTargetName,url);
            ProcessBuilder p2 = new ProcessBuilder(cmd1);

            try {


                if (url.endsWith(".m3u8")) {
                    System.out.println("Download me3u8 url command : "+ Arrays.toString(cmd1));
                    final Process p = p2.start();
                    /**
                     * ————————————————
                     *             版权声明：本文为CSDN博主「霞之秋诗羽」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
                     *             原文链接：https://blog.csdn.net/zyd573803837/article/details/109576612
                     */
                    //打印错误信息
                    new Thread(() -> {


                        BufferedReader err = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                        String line = null;
                        try {
                            tempLogFile.createNewFile();
                            while ((line = err.readLine()) != null) {
                                System.out.println("process: " + line);

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                err.close();
                                tempLogFile.delete();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                    p.waitFor(3, TimeUnit.HOURS);
                    p.destroy();
                } else {
                    tempLogFile.createNewFile();
                    Process p = p1.start();

                }
            }
            catch (IOException | InterruptedException e) {
                System.out.println("Download Proxy error");
                e.printStackTrace();
                System.out.println("ERROR.RUNNING.CMD");
            } finally {
                if (tempLogFile.exists()){
                    tempLogFile.delete();
                }
            }
        });

        return ApiResponse.success("Offline Download Started",null);
    }

    @RequestMapping("/getAllFiles")
    @CrossOrigin(value = "*")
    @ResponseBody
    public ApiResponse getAllFiles(@RequestHeader(value = "Authorization",required = true) String bearerToken) {
        bearerToken = bearerToken.split("\\s+")[1];
        if (bearerToken == null || !token.equals(bearerToken)) {
            String[] t = new String[0];
            return ApiResponse.fail("Invalid token");
        }

        return ApiResponse.success("all public files", fileService.getAllFiles(bearerToken));
    }

    @RequestMapping("preview")
    @ResponseBody
    public String preview(@RequestParam("fileName")String fileName,
                        @RequestHeader("Authorization")String userToken,
                        @RequestParam("isPublic")Boolean isPublic,
                        @RequestParam(value = "name",required = false, defaultValue = "")String name) throws IOException {
        userToken = userToken.split("\\s+")[1];
        if (userToken == null || !token.equals(userToken))
            return "not ok";
        if (isPublic) {
            File srcFile = new File("/data/files/" + fileName);
            File temp = new File("/data/temp/"+fileName);
            FileCopyUtils.copy(srcFile,temp);
        } else {
            File srcFile = new File("/data/files/" +name+"/"+ fileName);
            File temp = new File("/data/temp/"+fileName);
            FileCopyUtils.copy(srcFile,temp);

        }
        return "ok";
    }

    @RequestMapping("/clearPreview")
    @ResponseBody
    public String clearTemp(@RequestParam("fileName")String fileName,
                            @RequestParam("isPublic")Boolean isPublic,
                            @RequestParam(value = "name",required = false)String name) throws IOException {
        System.out.println("Clear preview file");

        Files.deleteIfExists(Paths.get("/data/temp/" + fileName));

        System.out.println("Clear Cache : after");
        return "ok";
    }

    @RequestMapping(value = "getFileInfo",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity getFileInfo(@RequestParam("file_name")String fileName,
                                      @RequestParam("token")String userToken,
                                      @RequestParam("isPublic")Boolean isPublic,
                                      @RequestParam(value = "name",required = false)String name) {
        if (userToken != null && token.equals(userToken))
            System.out.println("valid token");
        else
            return ResponseEntity.status(403).body("Unauthorized");

        return fileService.getFileInfo(fileName, userToken, isPublic, name);
    }


    @RequestMapping("/makePublic")
    @ResponseBody
    public ApiResponse makePublic(@RequestParam("privateFileName")String privateFileName,
                             @RequestHeader("Authorization")String userToken,
                             @RequestParam("name")String name) throws IOException {
        if (userToken != null && token.equals(userToken))
        {}
        else
            return ApiResponse.fail("Unauthorized");

        File src = new File("/data/files/"+name+"/"+privateFileName);
        if (!src.exists())
            return ApiResponse.fail("File not exits");
        File target = new File("/data/files/"+privateFileName);
        FileCopyUtils.copy(src, target);
        return ApiResponse.success("File already make public", null);
    }


    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @CrossOrigin(value = "*")
    @ResponseBody
    public ApiResponse delete(@RequestParam("name")String name,
                         @RequestParam("uname")String uname,
                         @RequestParam(value = "isPublic",required = false,defaultValue ="true")Boolean isPublic,
                         @RequestHeader(value = "Authorization") String bearerToken) {
        bearerToken = bearerToken.split("\\s+")[1];
        if (bearerToken != null && token.equals(bearerToken))
        {}
        else
            return ApiResponse.fail("Unauthorized");

        try {
            if (isPublic) {
                String[] lists = name.split("/");
                if (lists.length > 1 && !lists[0].equals(uname))
                    return ApiResponse.fail("Not Allowed. This file does not belong to you");

                File deletedFile = new File("/data/files/" + name.trim());
                if (!deletedFile.exists())
                    return ApiResponse.fail("File not found!");
                deletedFile.delete();
                return ApiResponse.success("File : " + name + " deleted!", null);
            } else {
                File deletedFile = new File("/data/files/" +uname +"/" + name.trim());
                if (!deletedFile.exists())
                    return  ApiResponse.fail("File not found!");
                deletedFile.delete();
                return ApiResponse.success("File : " + name + " deleted!", null);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ApiResponse.fail("File not found!");
        }
    }

    @RequestMapping("/moveToOnedrive")
    @CrossOrigin(value = "*")
    @ResponseBody
    public ApiResponse moveToOnedrive(@RequestParam("fileName")String fileName,
                                 @RequestParam("name")String userName,
                                 @RequestParam(value = "isPublic",required = false,defaultValue ="true")Boolean isPublic,
                                 @RequestHeader(value = "Authorization") String bearerToken) {
        bearerToken = bearerToken.split("\\s+")[1];
        if (bearerToken != null && token.equals(bearerToken))
            System.out.println("valid token");
        else
            return ApiResponse.fail("Invalid token");

        try {

            if (isPublic) {
                File path = new File("/data/files/" + fileName);

                if (!path.exists())
                    return ApiResponse.fail("File not found!");

                FileCopyUtils.copy(path, new File("/root/OneDrive/data/files/" + fileName));

                return ApiResponse.success("success",null);

            } else {
                File path = new File("/data/files/" + userName + "/" + fileName);

                if (!path.exists())
                    return ApiResponse.fail("File not found!");

                FileCopyUtils.copy(path, new File("/root/OneDrive/data/files/" + userName + "/" + fileName));

                return ApiResponse.success("success",null);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ApiResponse.fail("File not found!");
        }
    }



    public static Cookie search(Cookie[] cookies) {
        if (cookies == null)
            return null;
        for (Cookie cookie : cookies) {
            if ("token".equals(cookie.getName()))
                return cookie;
        }
        return null;
    }

    public static String encode(String filename){
        try {
            URI uri = new URI(null,null,filename,null);
            String encodedName = uri.toASCIIString();
            return encodedName;
        }catch (Exception e) {
            return filename;
        }
    }
}
