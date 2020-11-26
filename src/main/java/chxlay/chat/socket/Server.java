package chxlay.chat.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author: Alay
 * @Date: 2020-11-11 23:41
 */
public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(6666);

        System.out.println("服务器已启动~~~");
        System.out.println("服务端端信息：" + serverSocket.getInetAddress() + "Post：" + serverSocket.getLocalPort());

        while (true) {
            // 得到连接的客户端
            Socket client = serverSocket.accept();
            // 客户端构建异步线程
            ClientHandler clientHandler = new ClientHandler(client);
            // 启动线程
            clientHandler.start();
        }

    }


    private static class ClientHandler extends Thread {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            System.out.println("当前客户端:" + socket.getInetAddress() + "Post：" + socket.getPort());
            try {
                // 客户端输出流,服务器回送数据使用
                OutputStream socketOutS = socket.getOutputStream();
                // 打印流
                PrintStream socketOutPrint = new PrintStream(socketOutS);

                // 客户端输入流，接受客户端的数据
                InputStream socketIs = socket.getInputStream();
                BufferedReader socketInput = new BufferedReader(new InputStreamReader(socketIs));
                while (true) {
                    // 客户端拿到的数据
                    String socketInStr = socketInput.readLine();
                    if ("bye".equals(socketInStr)) {
                        // 回传 bye 给客户端
                        socketOutPrint.println("bye");
                        break;
                    } else {
                        System.out.println("客户端输出数据：" + socketInStr);
                        socketOutPrint.println("回送数据：" + socketInStr.length());
                    }
                }
                // 关闭流
                socketOutPrint.close();
                socketOutS.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    // 关闭连接
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("客户端已关闭");
        }
    }
}
