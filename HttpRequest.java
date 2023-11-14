import java.io.*;
import java.net.*;
import java.util.*;

public class HttpRequest {
    final static String CRLF = "\r\n"; // 定義換行符為 CRLF (\r\n)
    final static int HTTP_PORT = 80; // 設定Httport為80
   
 
    String method; // HTTP 請求方法（GET、POST 等）
    String URI; // 請求的 URI
    String version; // HTTP通訊協議版本
    String headers = ""; // 存儲請求中的標頭信息

    private String host; // 請求的目標主機名稱
    private int port; // 與目標主機建立連接的端口
    
    // HttpRequest 類的構造函數
    public HttpRequest(BufferedReader from) { 
        String firstLine = "";
        try {
            firstLine = from.readLine(); // 讀取 HTTP 請求的第一行
        } catch (Exception e) {
            System.out.println("Error reading request line: " + e);
        }
        
        // 解析 HTTP 請求的第一行，提取方法、URI 和協議版本
        String[] firstLineParts = firstLine.split(" ");          
        method = firstLineParts[0]; // 提取請求方法（GET、POST 等）
        URI = firstLineParts[1]; // 提取請求 URI
        version = firstLineParts[2];     // 提取通訊協議版本
        
        System.out.println("URI is: " + URI); 
        System.out.println("mrthod is: " + method); 
        System.out.println("version is: " + version);
        if (!method.equals("GET")) { 
            System.out.println("Error: Method not GET"); // 若請求方法不是 GET，輸出錯誤信息
        }
        try {
            String line = from.readLine();
            while (line.length() != 0) {
            headers += line + CRLF; // 將標頭信息逐行添加到 headers 字符串中
            
            // 檢查是否包含 Host 標頭，提取目標主機名稱和端口
            if (line.startsWith("Host:")) {
                firstLineParts = line.split(" ");
                if (firstLineParts[1].indexOf(':') > 0) {
                    String[] firstLineParts2 = firstLineParts[1].split(":");
                    host = firstLineParts2[0];
                    port = Integer.parseInt(firstLineParts2[1]);
                       
                } else {
                    host = firstLineParts[1];
                    port = HTTP_PORT;  // 若未指定端口，則使用默認的 HTTP 端口 80
                }
                    
            }
            line = from.readLine();
        }
        } catch (Exception e) {
            System.out.println("Error reading from socket: " + e);
            return;
        }
        System.out.println("Host to contact is: " + host + " at port " + port);
        
       
    }
    
    // 返回目標主機名稱的方法
    public String getHost() { 
        return host;
    }
    
    // 返回連接目標主機的方法
    public int getPort() { 
        return port;
    } 

    // 生成並返回 HTTP 請求字符串的方法
    public String toString() { 
        String request = method + " " + URI + " " + version + CRLF; // 構建 HTTP 請求的第一行
        System.out.println("request is: " + request);
        request += headers; // 添加標頭信息
        request += "Connection: close" + CRLF;  // 添加 Connection 標頭，告知伺服器關閉連接
        request += CRLF;  // 添加空行表示標頭結束

        return request;  // 返回完整的 HTTP 請求字符串
    }
}
