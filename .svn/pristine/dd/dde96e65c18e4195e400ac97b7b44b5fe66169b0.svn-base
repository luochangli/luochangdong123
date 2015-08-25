package com.weidi.common;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

import com.weidi.util.Logger;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-6-4
 * @Description 上传文件到服务器类
 */
public class UploadUtil {

    private static final String TAG = "uploadFile";

    private static final int TIME_OUT = 10 * 1000; // 超时时间

    private static final String CHARSET = "utf-8"; // 设置编码



    /*
     * Java文件操作 获取文件扩展名
     * 
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /*
     * Java文件操作 获取不带扩展名的文件名
     * 
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    public static String downLoadUrl(String host, String userName,
            String filename) {
        StringBuilder sb = new StringBuilder();
        sb.append("http://");
        sb.append(host + ":9090");
        sb.append("/plugins/jsmfiles/filedownload?");
        sb.append("username=");
        sb.append(userName);
        sb.append("&filename=");
        sb.append(filename);
        Logger.i(TAG, sb.toString());
        return sb.toString();
    }

    /**
     * 上传路径URL
     * 
     * @param host
     * @param userName
     * @param fileName
     * @return
     */
    public static String upLoadUrl(String host, String userName, String fileName) {
        StringBuilder sb = new StringBuilder();
        sb.append("http://");
        sb.append(host + ":9090");
        sb.append("/plugins/jsmfiles/fileupload?");
        sb.append("username=");
        sb.append(userName);
        sb.append("&filename=");
        sb.append(fileName);
        Logger.i(TAG, sb.toString());
        return sb.toString();
    }

   

   public interface UpCallback{
       void upSendMsg();
       void onUploadProcess(int uploadProcess,int total);
       void onLoadingFailed();
   }

  
    /**
     * Android上传文件到服务端
     * 
     * @param file
     *            需要上传的文件
     * @param RequestURL
     *            请求的rul
     * @return 返回响应的内容
     */
    public  static String uploadFile(File file, String RequestURL,UpCallback upCallback) {
        String result = null;
        String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data"; // 内容类型

        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Charset", CHARSET); // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
                    + BOUNDARY);

            if (file != null) {
                /**
                 * 当文件不为空，把文件包装并且上传
                 */
                DataOutputStream dos = new DataOutputStream(
                        conn.getOutputStream());

                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                int curLen = 0;
                int total = is.available();
                while ((len = is.read(bytes)) != -1) {
                	curLen += len;
                    dos.write(bytes, 0, len);
                    upCallback.onUploadProcess(curLen,total);
                }

                is.close();
                dos.flush();
                /**
                 * 获取响应码 200=成功 当响应成功，获取响应的流
                 */
                int res = conn.getResponseCode();
                 if(res==200)
                 {
                	  Logger.e(TAG, "上传成功！");
                      InputStream input = conn.getInputStream();
                      StringBuffer sb1 = new StringBuffer();
                      int ss;
                      while ((ss = input.read()) != -1) {
                          sb1.append((char) ss);
                      }
                      result = sb1.toString();
                      upCallback.upSendMsg();
                      Logger.e(TAG, "result : " + result);
                 }else{
                	 Logger.e(TAG, "上传失败！");
                	 upCallback.onLoadingFailed();
                 }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    

    /**
     * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
     * 
     * @param url
     *            Service net address
     * @param params
     *            text content
     * @param files
     *            pictures
     * @return String result of Service response
     * @throws IOException
     */
    public static String post(String url, Map<String, String> params,
            Map<String, File> files) throws IOException {
        String BOUNDARY = java.util.UUID.randomUUID().toString();
        String PREFIX = "--", LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = "UTF-8";

        URL uri = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
        conn.setReadTimeout(10 * 1000); // 缓存的最长时间
        conn.setDoInput(true);// 允许输入
        conn.setDoOutput(true);// 允许输出
        conn.setUseCaches(false); // 不允许使用缓存
        conn.setRequestMethod("POST");
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Charsert", "UTF-8");
        conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
                + ";boundary=" + BOUNDARY);

        // 首先组拼文本类型的参数
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);
            sb.append("Content-Disposition: form-data; name=\""
                    + entry.getKey() + "\"" + LINEND);
            sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
            sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
            sb.append(LINEND);
            sb.append(entry.getValue());
            sb.append(LINEND);
        }

        DataOutputStream outStream = new DataOutputStream(
                conn.getOutputStream());
        outStream.write(sb.toString().getBytes());
        // 发送文件数据
        if (files != null)
            for (Map.Entry<String, File> file : files.entrySet()) {
                StringBuilder sb1 = new StringBuilder();
                sb1.append(PREFIX);
                sb1.append(BOUNDARY);
                sb1.append(LINEND);
                sb1.append("Content-Disposition: form-data; name=\"uploadfile\"; filename=\""
                        + file.getValue().getName() + "\"" + LINEND);
                sb1.append("Content-Type: application/octet-stream; charset="
                        + CHARSET + LINEND);
                sb1.append(LINEND);
                outStream.write(sb1.toString().getBytes());

                InputStream is = new FileInputStream(file.getValue());
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }

                is.close();
                outStream.write(LINEND.getBytes());
            }

        // 请求结束标志
        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
        outStream.write(end_data);
        outStream.flush();
        // 得到响应码
        int res = conn.getResponseCode();
        InputStream in = conn.getInputStream();
        StringBuilder sb2 = new StringBuilder();
        if (res == 200) {
            int ch;
            while ((ch = in.read()) != -1) {
                sb2.append((char) ch);
            }
        }
        outStream.close();
        conn.disconnect();
        return sb2.toString();
    }

}
