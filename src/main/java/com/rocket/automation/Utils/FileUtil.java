package com.rocket.automation.Utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import static com.rocket.automation.Utils.Constant.UI_CONFIG;

public class FileUtil {
    private static File myFile;
    public static PropertyLoader proploader;
    public static File customDir;
    private static File directory;
    public static List<String> multiplefileList = new ArrayList<String>();
    public static List<String> modifiedFileList = new ArrayList<String>();

    public FileUtil() {
        propertyLoader();
    }

    public static String getDynamicFileName() {
        return String.valueOf(myFile);
    }

    /**
     * @param filePath path with the file name
     * @return
     * @throws IOException
     */
    public static File createFile(String filePath) throws IOException {
        myFile = new File(filePath);
        myFile.createNewFile();
        return myFile;
    }

    public void propertyLoader() {
        proploader = new PropertyLoader();
        proploader.loadProperty();
    }

    /**
     * Create Multiple files
     *
     * @param filePath
     * @param noOfFiles
     * @return
     * @throws IOException
     */
    public static List<String> createMultipleFiles(String filePath, String noOfFiles) throws IOException {
        int filecount = 1;
        File file[] = new File[Integer.parseInt(noOfFiles)];
        for (File myFile : file) {
            myFile = new File(filePath, RandomStringUtils.randomAlphanumeric(3) + ".txt");
            myFile.createNewFile();
            multiplefileList.add(myFile.getAbsoluteFile().getName());
            LoggerUtil.logLoader_info("File Util", multiplefileList + " created");
        }

        return multiplefileList;
    }

    /**
     * Get multiple files
     *
     * @return
     */
    public static List<String> getMultiplefileList() {
        return multiplefileList;
    }

    public static List<String> renameFile(String filepath) {

        for (String oldName : multiplefileList) {
            File oldFile = new File(filepath + "\\" + oldName);
            File newFile = new File(filepath + "\\" + RandomStringUtils.randomAlphanumeric(3) + ".txt");
            if (oldFile.renameTo(newFile)) {
                modifiedFileList.add(newFile.getAbsoluteFile().getName());
                LoggerUtil.logLoader_info("", oldFile + " Renamed to " + newFile);
            } else {
                LoggerUtil.logLoader_error("", oldFile + "Not Renamed to " + newFile);
            }

        }
        return modifiedFileList;
    }

    /**
     * Get Modified file names
     *
     * @return
     */
    public static List<String> getModifiedFileList() {
        return modifiedFileList;
    }

    public static void deleteMultipleFiles(String Path) {
        boolean status = true;
        for (String temp : modifiedFileList) {
            File fileDelete = new File(Path, temp);
            fileDelete.delete();
        }
    }

    /**
     * @param folderPath for where the directory needs to be created
     * @return
     */
    public static File createRepoDirectory(String folderPath) {
        directory = new File(folderPath);
        directory.mkdir();
        return directory;
    }

    /**
     * @param filePath File Path
     * @param fileName File name
     * @return
     * @throws IOException
     */
    public static File createFile(File filePath, String fileName) throws IOException {
        myFile = new File(filePath, fileName);
        myFile.createNewFile();
        return myFile;
    }


    /* The below method is for creating a new directory.
        If the directory already exists it stores the file path in config file and returns false.
        If the directory is not there it creates a new directory and returns the file path in config file and returns true
     */
    public static boolean createDirectory(String path) throws IOException {
        Boolean status = false;
        Properties configProperty = new Properties();
        customDir = new File(path);
        if (!customDir.exists()) {
            customDir.mkdirs();
            FileOutputStream fileOut = new FileOutputStream(UI_CONFIG, true);
            configProperty.setProperty("Clonedirectory", String.valueOf(customDir));
            configProperty.store(fileOut, null);
            fileOut.close();

            LoggerUtil.logLoader_info("", customDir + " was created");
            status = true;

        } else {
            LoggerUtil.logLoader_info("", customDir + " was not created");
            status = false;
        }

        return status;

    }


    /* The below method is for clearing a directory.
     */
    public static void clearDirectory(String path) throws IOException {
        customDir = new File(path);
        if (customDir.exists()) {
            FileUtils.cleanDirectory(customDir);
        }
    }


    /* The below method is for verifying whether a file exists or not.
           If file exists it returns true else it returns false
         */
    public static boolean verifyFileExists(String path) {
        File file = new File(path);
        Boolean status = true;
        if (!file.exists()) {
            status = false;
        }
        return status;
    }

    /* The below method is for deleting a file
     */
    public static void deleteFile(String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            LoggerUtil.logLoader_info("FileUtil", "Deleted the file in" + path);
        } catch (Exception e) {
            LoggerUtil.logLoader_error("FileUtil", e.toString());
            e.printStackTrace();
            Assert.fail(path+" is invalid");

        }
    }

    public static void deleteFile(String path, String fileName) {
        try {
            File file = new File(path + fileName);
            if (file.exists()) {
                file.delete();
            }
            LoggerUtil.logLoader_info("FileUtil", "Deleted the file " + fileName + " from " + path);
        } catch (Exception e) {
            Assert.fail(path+fileName+" is invalid");
            LoggerUtil.logLoader_error("FileUtil", e.toString());
            e.printStackTrace();
        }
    }


    public static boolean fileCompare(String sourceFile, String tagrgetFile) {
        Boolean compare = false;
        try {
            File src = new File(sourceFile);
            File dest = new File(tagrgetFile);
            compare = FileUtils.contentEquals(src, dest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return compare;
    }

    /* The below method is for creating a file and writing the user defined data into the file
     */
    public static void createFileAndWriteData(String fileNameWithPath, String data) throws IOException {
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {
            fw = new FileWriter(fileNameWithPath);
            bw = new BufferedWriter(fw);
            bw.write(data);
            LoggerUtil.logLoader_info("FileUtil", "Created file with data " + fileNameWithPath);

        } catch (IOException e) {
            LoggerUtil.logLoader_error("FileUtil", e.toString());
            Assert.fail(fileNameWithPath+" is invalid");
            e.printStackTrace();
        } finally {
            try {

                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();

            } catch (IOException ex) {
                LoggerUtil.logLoader_error("FileUtil", ex.toString());
                ex.printStackTrace();

            }
        }
    }

    public static void modifyFileContent(String path) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(path);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            pw.println("");
            pw.println("Test Content added" + RandomStringUtils.randomAlphanumeric(25));
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail(path+" is invalid");

        }

    }

    public static int searchContentsofFile(String filePath, String searchText) {
        // Open the file
        List<String> list = new ArrayList<>();
        String strLine;
        try {
            FileInputStream fstream = new FileInputStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            //Read File Line By Line
            while ((strLine = br.readLine()) != null) {
                if (strLine.contains(searchText)) {
                    list.add(strLine);
                }
            }
            //Close the input stream
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list.size();
    }

    public static boolean fileCompareLineByLine(String sourceFile, String tagrgetFile) throws IOException {
        BufferedReader reader1 = new BufferedReader(new FileReader(sourceFile));
        BufferedReader reader2 = new BufferedReader(new FileReader(tagrgetFile));
        String line1 = reader1.readLine();
        String line2 = reader2.readLine();
        boolean areEqual = true;
        int lineNum = 1;
        while (line1 != null || line2 != null) {
            if (line1 == null || line2 == null) {
                areEqual = false;
                break;
            } else if (!line1.equalsIgnoreCase(line2)) {
                areEqual = false;
                break;
            }
            line1 = reader1.readLine();
            line2 = reader2.readLine();
            lineNum++;
        }
        if (areEqual) {
            LoggerUtil.logLoader_info("FileUtil", "Two files have same content.");
        } else {
            LoggerUtil.logLoader_info("FileUtil", "Two files have different content. They differ at line " + lineNum);
            LoggerUtil.logLoader_info("FileUtil", "File1 has " + line1 + " and File2 has " + line2 + " at line " + lineNum);
        }
        reader1.close();
        reader2.close();
        return areEqual;
    }

    public static boolean fileImageCompare(String sourceFile, String tagrgetFile) throws IOException {


        File src = new File(sourceFile);
        File dest = new File(tagrgetFile);
        BufferedImage bufferfileInput = ImageIO.read(src);
        DataBuffer dataBufferInput = bufferfileInput.getData().getDataBuffer();
        int sizasource = dataBufferInput.getSize();
        BufferedImage bufferfileOutput = ImageIO.read(dest);
        DataBuffer dataBufferOutput = bufferfileOutput.getData().getDataBuffer();
        int sizadest = dataBufferInput.getSize();

        if (sizasource == sizadest) {
            for (int i = 0; i < sizasource; i++) {
                if (dataBufferInput.getElem(i) != dataBufferOutput.getElem(i)) {
                    return false;
                }
            }
            return true;

        } else {
            return false;
        }
    }

    public static void trimFileContent(String sourceFile, String tagrgetFile, int index) {
        try {
            String strLine;
            FileInputStream fstream = new FileInputStream(sourceFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tagrgetFile));
            while ((strLine = br.readLine()) != null) {
                writer.write(strLine.substring(index));
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String returnFileContentToSTring(String filePath) {
        File file = null;
        String fileContent = null;
        BufferedReader bufferedReader = null;
        try {
            file = new File(filePath);
            bufferedReader = new BufferedReader(new FileReader(file));
            String line = bufferedReader.readLine();
            //Reading all lines of input text file into  filecontent
            while (line != null) {
                if (fileContent == null) {
                    fileContent = line + System.lineSeparator();
                    line = bufferedReader.readLine();
                } else {
                    fileContent = fileContent + line + System.lineSeparator();
                    line = bufferedReader.readLine();
                }
            }
        } catch (Exception e) {
            LoggerUtil.logLoader_info("File Util", e.getMessage());
        } finally {
            try {
                if (bufferedReader != null)
                    bufferedReader.close();
            } catch (Exception e) {
                LoggerUtil.logLoader_info("File Util", "Issue in closing File" + e.getMessage());
            }
        }
        return fileContent;
    }

    public static void replaceSpecficString(String oldStringName, String replaceStringName, String filePath) {
        File file = null;
        FileWriter fileWriter = null;
        String fileContent = returnFileContentToSTring(filePath);
        try {
            file = new File(filePath);
            //Replacing Oldstring with New String in the FileContent
            if (fileContent != null) {
                String modifiedcontent = fileContent.replaceAll(oldStringName, replaceStringName);
                fileWriter = new FileWriter(file);
                fileWriter.write(modifiedcontent);
            } else {
                throw new Exception("File is null");
            }
        } catch (Exception e) {
            LoggerUtil.logLoader_info("File Util", e.getMessage());
        } finally {
            try {
                if (fileWriter != null) {
                    fileWriter.close();
                }
            } catch (Exception e) {
                LoggerUtil.logLoader_info("File Util", "Issue in closing File" + e.getMessage());
            }
        }
    }

    public String readFile(String filePath) {
        String result = null;
        StringBuilder sb = new StringBuilder();
        FileWriter fileWriter = null;
        try {
            BufferedReader file = new BufferedReader(new FileReader(filePath));

            String line = file.readLine();
            while (line != null) {
                sb.append(line.trim());
                sb.append("\n");
                line = file.readLine();
            }
            result = sb.toString();
        } catch (Exception e) {
            LoggerUtil.logLoader_info(this.getClass().getSimpleName(), e.getMessage());
        }
        return result;
    }


    public static void writeSpecificContentToNewFile(String srcFile, String destFile, String startLine, String endLine) {
        try {
            File file = new File(srcFile);
            FileWriter fW = new FileWriter(destFile);
            Scanner in = null;
            String line;
            in = new Scanner(file);
            while (in.hasNext()) {
                line = in.nextLine();
                if (line.contains(startLine))
                    fW.write("\n" + line);
                if (line.contains(endLine))
                    break;

            }
            fW.flush();
            LoggerUtil.logLoader_info("File Util", "Content written to new file for given" + startLine + "to" + endLine);
        } catch (Exception e) {
            LoggerUtil.logLoader_info("File Util", "Content not written to new file for given" + startLine + "to" + endLine);
        }
    }

    /**
     * util to read all lines in a string
     *
     * @param filePath
     * @return
     */
    public static String readAllBytesInFile(String filePath) {
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;

    }

    public static boolean isFileDownloaded(String downloadPath, String fileName) {
        boolean flag = false;
        File dir = new File(downloadPath);
        File[] dir_contents = dir.listFiles();

        for (int i = 0; i < dir_contents.length; i++) {
            if (dir_contents[i].getName().equals(fileName))
                return flag=true;
        }
        return flag;
    }
}