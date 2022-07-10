package io.github.stylesmile.tool;

import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class FileUploadUtil {

    private FileUploadUtil() {
    }

    public static class RequestFileItem {
        public String fileName;
        public String boundary;
        public String countentType;
    }

    public static class RequestBody {
        public static String boundary;
        public static List<Map<String, String>> formItems = new ArrayList<>();
        public static List<Map<String, File>> fileItems = new ArrayList<>();
    }

    private static RequestFileItem info;

    public static void fileUpload(HttpExchange he, String path) {
        path = StringUtil.isEmpty(path) ? "c://temp//" : path;
        OutputStream os = null;
        try {
            InputStream is = he.getRequestBody();
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];//前三行一定小于1024个byte
            int length = 0;
            File file;

            boolean isStart = true;
            while ((length = bis.read(buffer)) != -1) {
                //判断是不是文件的分隔符,不是的话,读到临时文件中
                //正常写入
                if (isStart) {
                    int startReadIndex = getFileReadIndex(buffer);
                    isStart = false;
                    if (info != null && info.fileName != null) {
                        file = new File(path + info.fileName);
                        if (!file.getParentFile().exists()) {
                            file.createNewFile();
                        }
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        os = new FileOutputStream(file);
                        byte[] realData = cartByte(buffer, startReadIndex, buffer.length - startReadIndex);
//                        if (new String(realData).contains("-")) {
//                            //文件非常小,第一个1024字节就包含了文件
//                            realData = cartByte(realData, 0, getIndexOf(realData, "-".getBytes()) - "\r\n-".getBytes().length);
//                            os.write(realData);
//                            os.close();
//                            break;
//                        }
                        os.write(buffer, startReadIndex, buffer.length - startReadIndex);
                        continue;
                    }
                }
                if (os == null) {
                    break;
                }
                //如果包含结束符,则只读结束符以前的数据
                if (!new String(buffer).contains(info.boundary)) {
                    os.write(buffer, 0, length);
                } else {
                    //结束符前有个换行符,所以减2
                    buffer = cartByte(buffer, 0, GetIndexOf(buffer, (info.boundary).getBytes()) - 2);
                    os.write(buffer, 0, buffer.length - 1);
                    os.close();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (Exception e) {

            }
        }
    }

    private static int getFileReadIndex(byte[] buffer) {
        //读四个\n符号就检查是不是文件
        try {
            for (int i = 0; i < buffer.length; i++) {
                int startIndex = getLineIndex(buffer, 4 * i);
                int endIndex = getLineIndex(buffer, 4 * (i + 1));
                byte[] fourLines = cartByte(buffer, startIndex, endIndex);
                if (isFileBoundary(fourLines)) {
                    return endIndex;
                }
            }
        } catch (Exception e) {
            System.out.println("出现异常:" + e.getMessage());
        }
        return 0;
    }

    private static boolean isFileBoundary(byte[] buffer) {
        String fileItem = new String(buffer);
        if (fileItem.contains("filename")) {
            if (info == null) {
                info = new RequestFileItem();
            }
            info.boundary = fileItem.substring(0, fileItem.indexOf("\n") - 1);
            fileItem = fileItem.substring(fileItem.indexOf("\n") + 1, fileItem.length());
            info.fileName = fileItem.substring(fileItem.indexOf("filename=\"") + "filename=\"".length(), fileItem.indexOf("\n") - "\"\n".length());
            info.countentType = fileItem.substring(fileItem.indexOf("Content-Type:"), fileItem.length());
            return true;
        }
        return false;
    }

    public static int getLineIndex(byte[] source, int lineNumber) {
        if (lineNumber <= 0) {
            return 0;
        }
        int lineCount = 0;
        for (int k = 0; k < source.length; k++) {
            if (lineCount == lineNumber) {
                return k;
            }
            if (source[k] == "\n".getBytes()[0] && lineCount <= lineNumber) {
                lineCount++;
            }
        }
        return 0;
    }

    /**
     * 获取byte[]指定起始位置的数组
     *
     * @param source     源
     * @param beginIndex 开始
     * @param endIndex   结束
     * @return byte
     */
    public static byte[] cartByte(byte[] source, int beginIndex, int endIndex) {
        if (source == null || source.length <= 0 || endIndex - beginIndex <= 0) {
            return null;
        }
        int byteLength = (endIndex + 1) - beginIndex;
        byte[] temp = new byte[byteLength];
        for (int i = 0; i < byteLength; i++) {
            temp[i] = source[i + beginIndex];
        }
        return temp;
    }

    /**
     * 获取某串byte数组在原byte数组的位置
     *
     * @param source 原byte数组
     * @param part   某串byte数组
     * @return -1 未找到  其他大于等于0的值
     */
    public static int getIndexOf(byte[] source, byte[] part) {
        if (source == null || part == null || source.length == 0 || part.length == 0) {
            return -1;
        }
        int i, j;
        for (i = 0; i < source.length; i++) {
            if (source[i] == part[0]) {
                for (j = 0; j < part.length; j++) {
                    if (source[i + j] != part[j]) {
                        break;
                    }
                }
                if (j == part.length) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 获取某串byte数组在原byte数组的位置
     *
     * @param source 原byte数组
     * @param part   某串byte数组
     * @return -1 未找到  其他大于等于0的值
     */
    public static int GetIndexOf(byte[] source, byte[] part) {
        if (source == null || part == null || source.length == 0 || part.length == 0) {
            return -1;
        }

        int i, j;
        for (i = 0; i < source.length; i++) {
            if (source[i] == part[0]) {
                for (j = 1; j < part.length; j++) {
                    if (source[i + j] != part[j]) {
                        break;
                    }
                }
                if (j == part.length) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static void compressedFiles_Gzip(String folderPath, String targzipFilePath, String targzipFileName) {
        File file = new File(folderPath);
        if (!file.exists() || !file.isDirectory() || file.listFiles().length <= 0) {
            return;
        }
        File[] childFiles = file.listFiles();
        try {
            //建立文件输出流
            OutputStream os = new FileOutputStream(targzipFilePath);
            //建立tar 压缩输出流
            //TarOutputStream tos = new TarOutputStream(os);
            for (int i = 0; i < childFiles.length; i++) {

            }
        } catch (Exception e) {

        }
    }
}