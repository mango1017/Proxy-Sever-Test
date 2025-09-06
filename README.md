ProxyCache

ProxyCache 是一個簡單的代理伺服器程式，能夠處理來自客戶端的 HTTP 請求，轉發至目標伺服器，並將伺服器回應返回給客戶端。以下是如何使用這個程式的指引。

如何使用

系統需求

● Java 環境

使用步驟

● 編譯程式碼

在命令列執行以下指令以編譯 ProxyCache 程式：

● javac ProxyCache.java HttpRequest.java HttpResponse.java

執行程式

使用以下指令執行 ProxyCache 程式，指定欲監聽的端口號：

● java ProxyCache <port_number>

例如，若要在端口 8080 上運行代理伺服器：

● java ProxyCache 8080

設定瀏覽器

● 在瀏覽器的設定中指定代理伺服器的地址和端口（例如 localhost:8080），開始使用代理伺服器。

注意事項

● 此代理伺服器僅支援 HTTP 協定。

● 可能需要進行進一步的擴展，以支援其他協定或功能。


 







