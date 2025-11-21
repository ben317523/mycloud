package com.example.service;

import com.alibaba.fastjson.JSONObject;
import com.example.entity.FileInfo;
import com.example.entity.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class FileService {


    /**
     * Get all files name of a user
     *
     * @return
     */
    public List<FileInfo> getAllFiles() {
        try {
            File folder = new File("/data/files");
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File f, String name) {
                    File check = new File(f.getAbsolutePath() + "/" + name);
                    if (check.isFile())
                        return true;
                    else
                        return false;

                }
            };
            File[] searchedFiles = folder.listFiles(filter);
            if (searchedFiles == null) return new ArrayList<>();
            Arrays.sort(searchedFiles, Comparator.comparing(File::getName));
            return Arrays.stream(searchedFiles)
                    .map(file -> {
                        FileInfo fileInfo = new FileInfo(file.getName(), file.length());
                        File log = new File("/data/temp/log_" + (file.getName().substring(0, file.getName().lastIndexOf("."))) + ".log");
                        if (log.exists()) fileInfo.setCompleted(false);
                        // convert lastModified (long) to LocalDateTime to match FileInfo.lastUpdate
                        fileInfo.setLastUpdate(Instant.ofEpochMilli(file.lastModified()).atZone(ZoneId.systemDefault()).toLocalDateTime());
                        return fileInfo;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return new ArrayList<>();
    }

    public List<FileInfo> getUserFiles(String uname) {
        try {
            File folder = new File("/data/files/" + uname);
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File f, String name) {
                    File check = new File(f.getAbsolutePath() + "/" + name);
                    if (check.isFile())
                        return true;
                    else
                        return false;

                }
            };
            File[] searchedFiles = folder.listFiles(filter);
            Arrays.sort(searchedFiles, Comparator.comparing(File::getName));
            return Arrays.stream(searchedFiles)
                    .map(file -> {
                        FileInfo fileInfo = new FileInfo(file.getName(), file.length());
                        File log = new File("/data/temp/log_" + (file.getName().substring(0, file.getName().lastIndexOf("."))) + ".log");
                        if (log.exists()) fileInfo.setCompleted(false);
                        fileInfo.setLastUpdate(Instant.ofEpochMilli(file.lastModified()).atZone(ZoneId.systemDefault()).toLocalDateTime());
                        return fileInfo;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    public ResponseEntity getFileInfo(String fileName,
                                      String userToken,
                                      Boolean isPublic,
                                      String name) {
        if (isPublic) {
            File file = new File("/data/files/" + fileName);
            if (!file.exists())
                return ResponseEntity.status(404).body("File not found");

            JSONObject object = new JSONObject();
            object.put("fileName", file.getName());
            object.put("size", file.length());
            return ResponseEntity.ok(object);
        } else {
            File file = new File("/data/files/" + name + "/" + fileName);
            if (!file.exists())
                return ResponseEntity.status(404).body("File not found");

            JSONObject object = new JSONObject();
            object.put("fileName", file.getName());
            object.put("size", file.length());
            return ResponseEntity.ok(object);
        }
    }

    public boolean uploadFile(MultipartFile file, String newFilePath) {
        try {
            File convertFile = new File(newFilePath);
            if (convertFile.exists())
                convertFile.delete();
            FileOutputStream fout = new FileOutputStream(convertFile);
            BufferedInputStream fin = new BufferedInputStream(file.getInputStream());
            byte[] buffer = new byte[2048];
            int readBytes = 0;
            while ((readBytes = fin.read(buffer)) != -1) {
                fout.write(buffer, 0, readBytes);
            }
            fout.close();
            fin.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    /**
     * For chat room
     */

    public List<Message> getAllMessage() {
        File file = new File("/data/messages.txt");
        if (!file.exists()) return null;

        try {
            List<Message> result = Files.lines(Paths.get("/data/messages.txt"))
                    .map(str -> {
                        String[] tokens = str.split("@;@");
                        if (tokens.length < 2) return null;
                        return new Message(tokens[0], tokens[1]);
                    })
                    .filter(t -> t != null)
                    .collect(Collectors.toList());

            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Message> addMessage(String name, String message) {
        List<Message> list = getAllMessage();

        if (list == null) list = new ArrayList<>();
        list.add(new Message(name, message));

        File file = new File("/data/messages.txt");
        try {
            FileWriter fileWriter = new FileWriter(file, true);
            fileWriter.write(name + "@;@" + message + "\n");
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return list;
    }

    public boolean deleteMessage(int id, String name) {
        List<Message> list = getAllMessage();
        if (list == null) return false;
        if (id >= list.size()) return false;
        if (!list.get(id).getName().equals(name)) return false;

        list.remove(id);

        saveMessages(list);

        return true;
    }

    public boolean saveMessages(List<Message> list) {
        if (list == null) return false;

        File file = new File("/data/messages.txt");
        try {
            FileWriter fileWriter = new FileWriter(file);

            for (Message m : list) {
                fileWriter.write(m.getName() + "@;@" + m.getMessage() + "\n");
            }
            fileWriter.close();


        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
