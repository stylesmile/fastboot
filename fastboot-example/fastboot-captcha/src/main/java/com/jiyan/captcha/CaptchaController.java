package com.jiyan.captcha;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.jiyan.captcha.util.RandomCharUtil;
import io.github.stylesmile.annotation.AutoWired;
import io.github.stylesmile.annotation.Controller;
import io.github.stylesmile.annotation.RequestMapping;
import io.github.stylesmile.annotation.RequestParam;
import io.github.stylesmile.ioc.Value;
import io.github.stylesmile.request.RequestMethod;
import io.github.stylesmile.tool.StringUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.regex.Pattern;

@Controller
public class CaptchaController {
    @AutoWired
    private DefaultKaptcha defaultKaptcha;
    @AutoWired
    CaptchaService captchaService ;
    @Value(value = "fast.name")
    private String name;
    /**
     * 手机号码正则
     */
    public static String phoneRegex = "^1(3[0-9]|4[01456879]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[0-35-9])\\d{8}$";


//    private static final Logger log = LoggerFactory.getLogger(TestController.class);



    /**
     * 获取图形验证码-验证成功后才可以发送手机验证码
     *
     * @param number 手机号
     *  POST http://127.0.0.1:8080/captcha?number=15002770045
     */
    @RequestMapping(value = "/captcha", method = RequestMethod.POST) // 200 * 70
//    @ApiOperation("获取图形验证码-验证成功后才可以发送手机验证码")
    public String rucaptcha(@RequestParam(value = "number") String number
    ) throws IOException {
        if (StringUtil.isEmpty(number)) {
            return ("手机号码不能为空");
        }
        boolean isMatch = Pattern.matches(phoneRegex, number);
        if (!isMatch) {
            return ("请输入正确格式的手机号");
        }
//        log.info("手机号码为:{}", number);
        System.out.println("手机号码为:{}" + number);
        // 自定义验证码
        String code = RandomCharUtil.generateRandomString(6);
        DefaultKaptcha captchaProducer2= captchaService.getDefaultKaptcha();
        BufferedImage image = captchaProducer2.createImage(code);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        baos.flush();
        byte[] imageBytes = baos.toByteArray();
        baos.close();
        String captchaBase64 = "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes);
//        this.redisTemplate.opsForValue().set(RedisKeyConstant.CAPTCHA_PRE + number, code, 2, TimeUnit.MINUTES);
        return "data:image/png;base64," + captchaBase64;
    }

}
