package chxlay.chat.udp;

import java.io.IOException;
import java.util.UUID;

/**
 * @Author: Alay
 * @Date: 2020-11-12 20:10
 */
public class UDPProvider {

    public static void main(String[] args) throws IOException {
        String sn = UUID.randomUUID().toString();
        Provider provider = new Provider(sn);
        provider.start();

        System.out.println("按任意键结束");
        System.in.read();
        provider.exit();
    }
}
