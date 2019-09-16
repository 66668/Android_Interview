package lib.interview.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class URLDemo {
    public static void main(String[] args) {
        try {
            URL url = new URL("https://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=url%E4%B8%AD%20%E9%94%9A%E4%BF%A1%E6%81%AF%E7%9A%84%E4%BD%9C%E7%94%A8&step_word=&hs=0&pn=1&spn=0&di=8140&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&istype=0&ie=utf-8&oe=utf-8&in=&cl=2&lm=-1&st=undefined&cs=2217092555%2C3127584453&os=2508951675%2C3983930786&simid=4025458627%2C603881355&adpicid=0&lpn=0&ln=451&fr=&fmq=1568184786447_R&fm=&ic=undefined&s=undefined&hd=undefined&latest=undefined&copyright=undefined&se=&sme=&tab=0&width=undefined&height=undefined&face=undefined&ist=&jit=&cg=&bdtype=0&oriquery=&objurl=http%3A%2F%2Fwww.kfyryyl.com%2Fdata%2Fupload%2Fimage%2F20170608%2F1496890772230304.jpg&fromurl=ippr_z2C%24qAzdH3FAzdH3Fooo_z%26e3Bhuy6yys_z%26e3Bv54AzdH3FgjofAzdH3Fddc_z%26e3Bip4s&gsm=0&rpstart=0&rpnum=0&islist=&querylist=&force=undefined");
            System.out.println("协议" + url.getProtocol());
            System.out.println("主机" + url.getHost());
            System.out.println("端口" + url.getPort());
            System.out.println("文件路径" + url.getPath());
            System.out.println("查询" + url.getQuery());
            System.out.println("锚点" + url.getRef());

            //
            InputStream ins = url.openStream();
            InputStreamReader reader = new InputStreamReader(ins);
            BufferedReader br = new BufferedReader(reader);

            String data = br.readLine();
            while (data != null) {
                System.out.println(data);
                data = br.readLine();
            }
            //关闭流
            br.close();
            reader.close();
            ins.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
