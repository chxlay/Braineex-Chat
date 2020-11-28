package client;

import client.bean.ServerInfo;

public class Client {
    public static void main(String[] args) {
        // 搜索服务器端超时时间 10秒
        ServerInfo info = ClientSearcher.searchServer(10000);
        System.out.println("Server:" + info);
    }
}
