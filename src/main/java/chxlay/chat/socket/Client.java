package chxlay.chat.socket;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @Author: Alay
 * @Date: 2020-11-11 23:40
 */
public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        // 设置超时时间
        socket.setSoTimeout(3000);
        // 建立连接
        socket.connect(new InetSocketAddress(Inet4Address.getLocalHost(), 6666), 3000);
        System.out.println("客户端发起连接请求~~~");
        System.out.println("客户端信息：" + socket.getLocalAddress() + "Post：" + socket.getLocalPort());
        System.out.println("服务端端信息：" + socket.getInetAddress() + "Post：" + socket.getPort());

        try {
            // 执行
            toDo(socket);
        } catch (Exception e) {
            System.out.println("异常出错了");
        }finally {
            socket.close();
        }
    }

    private static void toDo(Socket client) throws IOException {
        // 键盘输入流
        InputStream in = System.in;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

        // 得到 Socket 输出流
        OutputStream socketOutS = client.getOutputStream();
        // 转换成打印流
        PrintStream printStream = new PrintStream(socketOutS);

        // 获取服务端的返回
        InputStream socketInS = client.getInputStream();
        BufferedReader serverBuffer = new BufferedReader(new InputStreamReader(socketInS));
        do {
            // 读取键盘答应的每一行
            String clientStr = bufferedReader.readLine();
            // 把键盘读取到的数据一行发送到服务器
            printStream.println(clientStr);

            // 服务端返回
            String serverStr = serverBuffer.readLine();
            if ("bye".equals(serverStr)) {
                break;
            }
            System.out.println("服务器发来的信息：" + serverStr);
        } while (true);

        // 关闭流释放资源
        printStream.close();
        serverBuffer.close();
    }
}