package com.example.springMybatis.controller;

import com.example.springMybatis.entity.User;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class MainController {
    //@Autowired
    //private UserMapper userMapper;
    private static User[] users;
    private static String token = "dsgufn-d626sfsdfJHiI-Nnfvas94832u9f-h8e9q-UGubgyuGSg-7sASYAHya-8ashOIHA-8ya8iHDT7id-hui";

    static {
        users = new User[10];
        users[0] = new User(0,"ben","89762230");
        users[1] = new User(1,"kenny","123456");
        users[2] = new User(2,"guest","123456");
    }

    @RequestMapping("/")
    public String login(){
        return "login.html";
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String index(@RequestParam("name")String name,
                        @RequestParam("password")String password,
                        HttpServletResponse response) {
        //User user = userMapper.findUser(name, password);
        User user = null;
        for (User t : users) {
            if (t == null)
                continue;
            if (t.getUserName().equals(name) && t.getPassword().equals(password))
                user = t;
        }
        if (user != null) {
            //response.addHeader("Allow","GET, POST");
            Cookie cookie = new Cookie("token",token);
            cookie.setMaxAge(60*60);
            response.addCookie(cookie);

            Cookie uname = new Cookie("name",name);
            uname.setMaxAge(60*60);
            response.addCookie(uname);

            return "redirect:index.html";
        }
        else
            return "redirect:404.html";
    }

    @RequestMapping("/logout")
    @ResponseBody
    public String logout(HttpServletResponse response) {

        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        Cookie uname = new Cookie("name",null);
        uname.setMaxAge(0);
        response.addCookie(uname);
        //response.addHeader("Clear-Site-Data","\"cache\", \"cookies\", \"storage\", \"executionContexts\"");
        return "<h1>Log Out!</h1><script>var newWindow; setTimeout(newWindow = window.open('login.html','_self'),1000);setTimeout(newWindow.close(),2000);</script>";
    }


    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    @ResponseBody
    public String fileUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        if (search(request.getCookies()) != null && token.equals(search(request.getCookies()).getValue()))
            System.out.println("valid token");
        else
            return "Unauthorized";

        File convertFile = new File("/data/files/"+file.getOriginalFilename());
        FileOutputStream fout = new FileOutputStream(convertFile);
        BufferedInputStream fin = new BufferedInputStream(file.getInputStream());
        byte[] buffer = new byte[2048];
        int readBytes = 0;
        while ((readBytes = fin.read(buffer)) != -1) {
            fout.write(buffer,0,readBytes);
        }
        fout.close();
        fin.close();
        return "File is upload successfully";
    }

    @RequestMapping(value = "/privateUpload",method = RequestMethod.POST)
    @CrossOrigin("*")
    public ResponseEntity privateUpload(@RequestParam("file") MultipartFile file,
                                @RequestParam(value = "name") String name,
                                @RequestParam(value = "token") String userToken) throws IOException {
        if (userToken != null && token.equals(userToken))
            System.out.println("valid token");
        else
            return ResponseEntity.status(403).body("Unauthorized");

        File userPath = new File("/data/files/"+name);
        if (!userPath.exists())
            userPath.mkdir();

        File convertFile = new File("/data/files/"+name+"/"+file.getOriginalFilename());
        FileOutputStream fout = new FileOutputStream(convertFile);
        BufferedInputStream fin = new BufferedInputStream(file.getInputStream());
        byte[] buffer = new byte[2048];
        int readBytes = 0;
        while ((readBytes = fin.read(buffer)) != -1) {
            fout.write(buffer,0,readBytes);
        }
        fout.close();
        fin.close();
        return ResponseEntity.ok("File is upload successfully");
    }

    @RequestMapping(value = "/download")
    public ResponseEntity download(@RequestParam("param") String param, HttpServletRequest request) throws IOException {
        if (search(request.getCookies()) != null && token.equals(search(request.getCookies()).getValue()))
            System.out.println("valid token");
        else
            return ResponseEntity.status(403).body("Unauthorized");


        try {
            File folder = new File("/data/files");
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File f, String name) {
                    Pattern pattern = Pattern.compile(".*"+param+".*",Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(name);
                    return matcher.find();
                }
            };
            String[] searchedName = folder.list(filter);
            File file = new File("/data/files/"+((searchedName.length!=0)?searchedName[0]:"") );

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

    @RequestMapping("/downloadPrivate")
    public ResponseEntity downloadPrivate(@RequestParam("param") String param,
                                          @CookieValue("name") String name,
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
                    Pattern pattern = Pattern.compile(".*"+param+".*",Pattern.CASE_INSENSITIVE);
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
    public String downloadProxy(@RequestParam(value = "url") String url,
                                @RequestParam(value = "targetName")String targetName,
                                HttpServletRequest request) {
        if (search(request.getCookies()) != null && token.equals(search(request.getCookies()).getValue()))
            System.out.println("valid token");
        else
            return "Unauthorized";

        Runtime run = Runtime.getRuntime();

        Process p = null;
        String cmd = "sudo wget -q -O /data/files/"+targetName+" "+url;
        try {
            p = run.exec(cmd);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR.RUNNING.CMD");
            return "not ok";
        }
        return "ok";
    }

    @RequestMapping("/getAllFiles")
    @CrossOrigin(value = "*")
    @ResponseBody
    public String[] getAllFiles() {
        try{
            File folder = new File("/data/files");
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File f, String name) {
                    File check = new File(f.getAbsolutePath()+"/"+name);
                    if (check.isFile())
                        return true;
                    else
                        return false;

                }
            };
            String[] searchedName = folder.list(filter);
            return searchedName;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @RequestMapping("/getUserFiles")
    @CrossOrigin("*")
    @ResponseBody
    public String[] getUserFiles(@RequestParam("name")String name) {
        try {
            File folder = new File("/data/files/"+name);
            if (!folder.exists())
                return null;
            String[] searchedName = folder.list();
            return searchedName;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @RequestMapping("/delete")
    @CrossOrigin(value = "*")
    @ResponseBody
    public String delete(@RequestParam("name")String name,
                         @CookieValue("name")String cname,
                         HttpServletRequest request) {
        if (search(request.getCookies()) != null && token.equals(search(request.getCookies()).getValue()))
            System.out.println("valid token");
        else
            return "Unauthorized";

        try {
            String[] lists = name.split("/");
            if (lists.length > 1 && !lists[0].equals(cname))
                return "Not Allowed. This file does not belong to you";

            File deletedFile = new File("/data/files/"+name);
            if (!deletedFile.exists())
                return "File not found!";
            deletedFile.delete();
            return "File : "+name+" deleted!";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "File not found!";
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
