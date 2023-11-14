import java.net.*;
import java.io.*;
import java.util.*;


public class ProxyCache {
    private static int port;
    private static ServerSocket socket;
    
    // 初始化方法，設定監聽端口
    public static void init(int p) { 
        port = p;
        try {
            // 創建Socket服務器端，監聽網頁
            socket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Error reading request from client: " + e);
	        return;
        }
    }
     // 處理client端請求的方法
    public static void handle(Socket client) {
        Socket server = null;
        HttpRequest request = null;
        HttpResponse response = null;

        try {
            // 從client端獲取請求數據
            BufferedReader fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
            request = new HttpRequest(fromClient); 
        } catch (IOException e) {
            System.out.println("Error handling request: " + e);      
        }    
        try {
            // 與目標伺服器建立連接，並將請求數據發送至伺服器     
            server = new Socket(request.getHost(), 80);
            DataOutputStream toServer = new DataOutputStream(server.getOutputStream());
            toServer.writeBytes(request.toString());
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: " + request.getHost());
            System.out.println(e);
            return;
        } catch (IOException e) {
            System.out.println("Error writing request to server: " + e);
            return;
        }
        
        try {
             
            DataInputStream fromServer = new DataInputStream(server.getInputStream());
            response = new HttpResponse(fromServer); 
            
            
            DataOutputStream toClient = new DataOutputStream(client.getOutputStream());
            toClient.writeBytes(response.toString());
            toClient.write(response.body);
            client.close();
            server.close();

            
        } catch (Exception e) {
            System.out.println("Error writing response to client: " + e);
        }   
    }

    // 主方法
    public static void main(String args[]) {
        int myPort = 0;

        try {   
            myPort = Integer.parseInt(args[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Need a port number as an argument.");
            System.exit(-1);
        } catch (NumberFormatException e) {
            System.out.println("Please provide a valid port number as an integer.");
            System.exit(-1);
        }

        // 初始化Sever端
        init(myPort);

        Socket client = null;
         // 循環接受Clinet端連接並處理請求，直到使用者停止
        while (true) { 
            try {
                client = socket.accept(); 
                handle(client);
            } catch (IOException e) {
                System.out.println("Error handling request: " + e);
                continue;
            }
        }
    }
}
