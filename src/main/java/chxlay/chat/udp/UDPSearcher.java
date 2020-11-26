package chxlay.chat.udp;

import java.io.IOException;
import java.net.*;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: Alay
 * @Date: 2020-11-12 20:11
 */
public class UDPSearcher {

    // 监听端口
    public static final int LISTEN_PORT = 30000;


    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("启动搜索端");
        Listener listener = listener();
        sentBroadcast();

        System.out.println("任意键结束");
        System.in.read();
        // 结束监听拿到设备信息输出
        List<Listener.Device> devices = listener.exit();
        devices.forEach(device -> System.out.println(device.toString()));
        System.out.println("完毕>>>>>>>>>>");
    }

    /**
     * 监听方法
     */
    private static Listener listener() throws InterruptedException {
        System.out.println("开始监听");
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Listener listener = new Listener(LISTEN_PORT, countDownLatch);
        // 启动当前监听器
        listener.start();
        // 等待操作
        countDownLatch.await();
        return listener;
    }

    /**
     * 发送广播的方法
     */
    private static void sentBroadcast() throws IOException {
        System.out.println("启动搜索端");
        // 作为搜索方，无需指定端口，让系统分配
        DatagramSocket ds = new DatagramSocket();
        // 构建一份回送数据
        String sendData = MessageCreator.buildPort(LISTEN_PORT);
        byte[] sendDataBytes = sendData.getBytes();
        // 将信息回传回去给发送端，参数：数据，发送端的地址，发送端的端口
        DatagramPacket sendPacket = new DatagramPacket(sendDataBytes, sendDataBytes.length);
        // 通过set方法设置广播地址
        sendPacket.setAddress(InetAddress.getByName("255.255.255.255"));
        sendPacket.setPort(20000);
        // 发送信息
        ds.send(sendPacket);
        ds.close();
        System.out.println("发送广播已完成");
    }

}
