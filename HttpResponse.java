import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HttpResponse {
    final static String CRLF = "\r\n"; // 定義換行符為 CRLF (\r\n)
    final static int BUF_SIZE = 8192;     // 定義緩衝區大小
    final static int MAX_OBJECT_SIZE = 100000;  // 定義最大物件大小為 100000 bytes

    String version; // HTTP 協 議版本
    int status;  // HTTP 響應狀態碼
    String statusLine = "";  // 響應狀態行
    String headers = "";  // 存儲響應中的標頭信息
    byte[] body = new byte[MAX_OBJECT_SIZE];  // 存儲響應主體的字節數組
    
    // HttpResponse 類的構造函數
    public HttpResponse(DataInputStream fromServer) {
        int length = -1; // 響應主體的長度
        boolean gotStatusLine = false;  // 判斷是否已獲取到狀態行
        
        try {
           /*  BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fromServer, "UTF-8"));  
            statusLine = bufferedReader.readLine();
            System.out.println("Status Line: " + statusLine);*/
            String line = fromServer.readLine();  // 讀取來自伺服器的第一行（狀態行）
              
            while (line.length() != 0 ) {
                if (!gotStatusLine) {
                    statusLine = line;
                    gotStatusLine = true;
                } else {
                    headers += line + CRLF;  // 將標頭信息逐行添加到 headers 字符串中
                }
                
                 // 檢查標頭中是否包含 Content-Length，以獲取響應主體的長度
                if (line.startsWith("Content-Length: ") || line.startsWith("Content-length: ") ) {
                    String[] tmp = line.split(" ");
		            length = Integer.parseInt(tmp[1]);
                }
                line = fromServer.readLine();
            }
        } catch (IOException e) {
            System.out.println("Error reading headers from server: " + e);
            
        }

        try {
            int bytesRead = 0;
            byte buf[] = new byte[BUF_SIZE];
            boolean loop = false;
            
            if (length == -1) {
                loop = true;
            }

            // 讀取響應主體的字節數據
            while (bytesRead < length || loop) {
                int res = fromServer.read(buf);  // 將讀取的字節數據存入 body 字節數组中
                if (res == -1) {
                    break;
                }

                for (int i = 0; i < res && (i + bytesRead) < MAX_OBJECT_SIZE; i++) {
                    body[bytesRead + i] = buf[i];
                }
                bytesRead += res;
            }
        } catch (IOException e) {
            System.out.println("Error reading response body: " + e);
            return;
        }
    }
    
    // 將響應轉換為字符串的方法
    public String toString() {
        String res = "";   // 將狀態行添加到字符串中
        res = statusLine + CRLF;  // 將狀態行添加到字符串中
        res += headers;  // 添加標頭信息
        res += CRLF;  // 添加空行表示標頭結束
        
        return res;  // 返回完整的響應字符串
    }
}
