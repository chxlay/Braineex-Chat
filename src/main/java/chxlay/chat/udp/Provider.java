package chxlay.chat.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * @Author: Alay
 * @Date: 2020-11-12 21:52
 */
public class Provider extends Thread {

    private final String sn;
    private boolean done = false;
    private DatagramSocket ds;

    public Provider(String sn) {
        this.sn = sn;
    }

    @Override
    public void run() {
        System.out.println("启动接收端");
        try {
            // 作为接收者，指定一个端口用于接收数据
            ds = new DatagramSocket(20000);
            while (!done) {
                // 构建接收实体
                final byte[] buffer = new byte[512];
                DatagramPacket receivePack = new DatagramPacket(buffer, buffer.length);

                // 接收信息
                ds.receive(receivePack);
                // 打印接收到的信息和发送端的信息
                // 发送者IP
                String ip = receivePack.getAddress().getHostAddress();
                // 发送者端口号
                int port = receivePack.getPort();
                // 数据的大小
                int dataLen = receivePack.getLength();
                // 接收到的数据转成String
                String data = new String(receivePack.getData(), 0, dataLen);

                // 输出接收到的信息
                System.out.println("接收端收到的信息，IP:" + ip + "\tPost:" + port + "\tData：" + data);

                // 解析发送端数据中指定的回传信息端口号
                int resPort = MessageCreator.parsePort(data);
                if (resPort != -1) {
                    // 构建一份回送数据
                    String responseData = MessageCreator.buildSn(sn);
                    byte[] resDataBytes = responseData.getBytes();
                    // 将信息回传回去给发送端，参数：数据，发送端的地址，发送端的端口
                    DatagramPacket datagramPacket = new DatagramPacket(resDataBytes, resDataBytes.length,
                            receivePack.getAddress(), resPort);
                    // 发送信息
                    ds.send(datagramPacket);
                }
            }
        } catch (Exception ignored) {
        } finally {
            this.close();
        }
        System.err.println("发送信息完毕>>>>>>>>>>>>>>>>>>");
    }

    public void exit() {
        done = true;
        this.close();
        System.out.println("已退出");
    }

    public void close() {
        if (null != ds) {
            ds.close();
            ds = null;
        }
    }

}