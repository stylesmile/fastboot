package io.github.stylesmile.tool;

import io.github.stylesmile.file.MultipartFile;
import io.github.stylesmile.file.MultipartInputStream;
import io.github.stylesmile.file.UploadedFile;
import io.github.stylesmile.server.Request;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MultipartUtil {
    /**
     * 处理form-data参数，
     * 如果 为form-data数据，
     * <p>
     * 第一行是以28个“-”开头                         》》》    ----------------------------097682892719969847534030
     * 第二行以"Content-Disposition: form-data;“    》》》    Content-Disposition: form-data; name="username"
     * 第二行是空字符串  ”“                           》》》    ”“
     * 第四行是参数值                                 》》》    zhangsan
     * <p>
     * 文件
     * <p>
     * 第一行是以28个“-”开头,和第一行一样表示结束         》》》    ----------------------------097682892719969847534030--
     *
     * @param request
     * @param parameterMap
     * @return
     * @throws Exception
     */
    public static Map<String, Object> parseFormData(Request request, Map<String, Object> parameterMap) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getBody()));
        String line;
        //当前行号
        AtomicInteger integer = new AtomicInteger(0);

        // 是否为文件
        // 参数名称
        String parameterName = null;
        // 参数
        String parameterValue = null;
        // 文件名
        String filename = null;
        // 如果 为form-data数据，则第一行是以28个“-”开头，第二行以“Content-Disposition: form-data;开头”
        // 第一行  参数标识
        String line1 = null;
        // 第一行 行号
        AtomicInteger line1Line = new AtomicInteger(0);
        // 第二行
        String line2 = null;
        // 第二行 行号
        AtomicInteger line2Line = new AtomicInteger(0);

        // 第三行 参数标识符，
        String line3 = null;
        // 第三行 行号
        AtomicInteger line3Line = new AtomicInteger(0);

        // 第四行 参数标识符，
        String line4 = null;
        // 第四行 行号
        AtomicInteger line4Line = new AtomicInteger(0);
        MultipartFile multipartFile = null;

        AtomicBoolean isFile = new AtomicBoolean(false);
        // 第一行为http消息头 28个- 加一个长整型 18数字，后面一行为参数，一行为值
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            // 当前 数据数据值 的行号
            AtomicInteger currentLine = new AtomicInteger(integer.addAndGet(1));
            // 第一行为http消息头 28个- 加一个长整型为分隔符   后面为数字 数字，第二行为 参数基本信息，
            if (line.startsWith("------------------")) {
                // line.equals(line1 + "--") 完全结束 最后一行
                if (line.equals(line1 + "--")) {
                    continue;
                }
                // line.equals(line1) 前一个参数读取结束，接
                if (line.equals(line1)) {
                    // 结束标识符
                    if (isFile.get()) {
                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                                parameterValue.getBytes(StandardCharsets.UTF_8));
                        multipartFile.setBody(byteArrayInputStream);
                        multipartFile.setSize(Long.valueOf(parameterValue.length()));
                        parameterMap.put(parameterName, multipartFile);
                    } else {
                        parameterMap.put(parameterName, parameterValue);
                    }
                    parameterName = null;
                    parameterValue = null;
                    line1Line = new AtomicInteger(currentLine.get());
                    line2 = null;
                    line2Line = null;
                    line3 = null;
                    line3Line = null;
                    line4 = null;
                    line4Line = null;
                    multipartFile = null;
                    isFile = new AtomicBoolean(false);
                    continue;

                }
                line1 = line;
                line1Line = new AtomicInteger(currentLine.get());
                continue;
            }
            // 以”Content-Disposition:"开头为 参数名称
            if (line.startsWith("Content-Disposition:")) {
                line2 = line;
                line2Line = new AtomicInteger(currentLine.get());
                Map<String, String> map = Utils.getHeaderParams(line);
                parameterName = map.get("name");
                parameterMap.put(parameterName, "");
                filename = map.get("filename");
                String name = map.get("name");
                if (StringUtil.isNotEmpty(filename)) {
                    multipartFile = new MultipartFile();
                    multipartFile.setName(name);
                    multipartFile.setFilename(filename);
                }
                continue;
            }
            if (("").equals(line)) {
                if (StringUtil.isEmpty(line3)) {
                    line3 = line;
                    line3Line = new AtomicInteger(currentLine.get());
                    continue;
                } else {
                    line4 = line;
                    line4Line = new AtomicInteger(currentLine.get());
                    continue;
                }
            }
            if (line.startsWith("Content-Type: ")) {
                line3 = line;
                line3Line = new AtomicInteger(currentLine.get());
                continue;
            }
            if (line1Line.get() + 2 == line2Line.get() + 1
                    && line2Line.get() + 1 == line3Line.get()
                    && line1.startsWith("----------------------------")
                    && line2.startsWith("Content-Disposition:")
                    && line3.equals("")
            ) {
                // 此时为参数值，不是文件，参数处理
                parameterValue = line;
                parameterMap.put(parameterName, line);
                continue;
            }
            if (line1Line.get() + 1 == line2Line.get()
                    && line1Line.get() + 2 == line3Line.get()
                    && line1Line.get() + 3 == line4Line.get()
                    && line1.startsWith("----------------------------")
                    && line2.startsWith("Content-Disposition:")
                    && line3.startsWith("Content-Type: ")
                    && "".equals(line4)
            ) {
                // 参数类型为 文件
                isFile = new AtomicBoolean(true);
                // 为文件
                // 为参数值
                if (StringUtil.isNotEmpty(parameterValue)) {
                    parameterValue = parameterValue + line;
                } else {
                    parameterValue = line;
                }
            }
//            // 第一行为http消息头 28个- 加一个长整型 18数字，第二行为 基本信息，
//            String[] keyValue = line.split("=");
//            if (keyValue.length == 3) {
//
//            }
//            if (keyValue.length == 2) {
//                if (keyValue[1].startsWith("@")) {
//                    // 文件上传
//                    String fileName = keyValue[1].substring(1);
//                    File file = new File(fileName);
//                    try (FileOutputStream fos = new FileOutputStream(file)) {
//                        byte[] buffer = new byte[1024];
//                        int bytesRead;
//                        while ((bytesRead = inputStream.read(buffer)) != -1) {
//                            fos.write(buffer, 0, bytesRead);
//                        }
//                    }
//                    parameterMap.put(keyValue[0], file);
//                } else {
//                    // 普通参数
//                    parameterMap.put(keyValue[0], keyValue[1]);
//                }
//            }
        }
        return parameterMap;
    }

//    public static Map<String, Object> parseFormData(Request request) throws IOException {
//        return parseFormData(request.getBody());
//    }

    public static void buildParamsAndFiles(Request request, Map<String, Object> parameterMap) throws IOException {
        Map<String, String> ct = Utils.getHeaderParams(request.getHeaders().get("Content-Type"));
        if (!ct.containsKey("multipart/form-data")) {
            throw new IllegalArgumentException("Content-Type is not multipart/form-data");
        }
        String boundary = ct.get("boundary"); // should be US-ASCII
        if (boundary == null)
            throw new IllegalArgumentException("Content-Type is missing boundary");
        parameterMap.put("file", request.getBody());
        final MultipartInputStream in = new MultipartInputStream(request.getBody(),
                boundary.getBytes(StandardCharsets.UTF_8));

        MultipartFile p = new MultipartFile();
//        try {
//            p.headers = Utils.readHeaders(request.getHeaders());
//        } catch (IOException ioe) {
//            throw new RuntimeException(ioe);
//        }
        Map<String, String> cd = Utils.getHeaderParams(request.getHeaders().get("Content-Disposition"));
        p.name = cd.get("name");
        p.filename = cd.get("filename");
//        p.body = in;
        parameterMap.put("file", request.getBody());

//        doBuildFiles(request, p,parameterMap);
    }

    /**
     * 构建文件上传参数
     *
     * @param context
     * @param part
     * @param parameterMap
     * @throws IOException
     */
    private static void doBuildFiles(Request context, MultipartFile part, Map<String, Object> parameterMap) throws
            IOException {
        List<UploadedFile> uploadedFileList = new ArrayList<>();
        String contentType = part.getHeaders().get("Content-Type");
        // todo 文件大小限制
//        InputStream content = read(new LimitedInputStream(part.getBody(), request_maxFileSize));
//        InputStream content = read(part.getBody());
//        int contentSize = content.available();
        String name = part.getFilename();
        String extension = null;
        int idx = name.lastIndexOf(".");
        if (idx > 0) {
            extension = name.substring(idx + 1);
        }
//        UploadedFile f1 = new UploadedFile(contentType, contentSize, content, name, extension);
//        uploadedFileList.add(f1);
//        parameterMap.put("uploadedFileList", uploadedFileList);

    }

    private static ByteArrayInputStream read(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return new ByteArrayInputStream(output.toByteArray());
    }

    public static OutputStream stringToOutputStream(String str) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintWriter printWriter = new PrintWriter(byteArrayOutputStream);
        printWriter.write(str);
        printWriter.flush();
        return byteArrayOutputStream;
    }
}
