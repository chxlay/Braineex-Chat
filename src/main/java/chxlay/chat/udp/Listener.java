package chxlay.chat.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 广播监听类
 *
 * @Author: Alay
 * @Date: 2020-11-12 23:20
 */
public class Listener extends Thread {
    private final int listenPort;
    private final CountDownLatch countDownLatch;
    private final List<Device> devices = new ArrayList<>();
    private boolean done = false;
    private DatagramSocket ds;

    public Listener(int listenPort, CountDownLatch countDownLatch) {
        this.listenPort = listenPort;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        countDownLatch.countDown();
        try {
            // 监听回送端口
            ds = new DatagramSocket(listenPort);
            while (!done) {
                // 构建接收实体
                final byte[] buffer = new byte[512];
                DatagramPacket receivePack = new DatagramPacket(buffer, buffer.length);
                // 接收信息
                ds.receive(receivePack);

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

                String sn = MessageCreator.parseSn(data);
                if (null != sn) {
                    Device device = new Device(port, ip, sn);
                    devices.add(device);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.close();
        }
        System.out.println("结束操作");
    }

    public List<Device> exit() {
        done = true;
        this.close();
        return devices;
    }

    public void close() {
        if (null != ds) {
            ds.close();
            ds = null;
        }
    }

    static class Device {
        private final int port;
        private final String ip;
        private final String sn;

        public Device(int port, String ip, String sn) {
            this.port = port;
            this.ip = ip;
            this.sn = sn;
        }

        @Override
        public String toString() {
            return "Device{" +
                    "port=" + port +
                    ", ip='" + ip + '\'' +
                    ", sn='" + sn + '\'' +
                    '}';
        }
    }
}