package email.demo.rrr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class test {

    @Autowired
    MailUtil mu;

    @RequestMapping("/test")
    public String test(){
        System.out.print("123");
        return "hi";
    }


    @RequestMapping(value="/sendmail")
    public void sendMail(@RequestParam("email") String email) throws Exception{

//        String email="1206346335@qq.com";
        if(mu.cheakMail(email)){
        mu.sendMails(email,"测试标题","测试正文");
        }else {
            System.out.print("邮箱格式不正确");
        }
    }
}
