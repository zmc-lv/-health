package com.zmc.health.controller;

import com.alibaba.druid.util.StringUtils;
import com.zmc.health.constant.MessageConstant;
import com.zmc.health.constant.RedisMessageConstant;
import com.zmc.health.entity.Result;
import com.zmc.health.utils.ValidateCodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 包名: com.zmc.health.controller
 *
 * @author zmc
 * 日期: 2021/8/5
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    private static final Logger logger = LoggerFactory.getLogger(ValidateCodeController.class);

    @Autowired
    private JedisPool jedisPool;

    /**
     ( 体检预约 验证码
     * @param telephone
     * @return
     */
    @PostMapping("/send4Order")
    public Result send4Order(String telephone){
        Jedis jedis = jedisPool.getResource();
        //先判断是否发送过,从redis中去,如果有值,发送过来
        //redis中的key要带上业务标识
        String key = RedisMessageConstant.SENDTYPE_ORDER + ":" + telephone;
        //redis中的验证码
        String codeInRedis = jedis.get(key);
        logger.debug("Redis中的验证码:{},{}",codeInRedis,telephone);
        if (!StringUtils.isEmpty(codeInRedis)) {
            //如果有值.发送过了
            return new Result(false,"验证码已经发送过了，请注意查收");
        }
        //没有发送，则，生成验证码
        String code = String.valueOf(ValidateCodeUtils.generateValidateCode(6));
      /*  //调用SMSUtils发送
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code);
        } catch (ClientException e) {
            e.printStackTrace();
            logger.error("发送验证码失败了",e);
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }*/
        logger.debug("验证码发送成功{},{}",code,telephone);
        //验证码存入redis，同时要设置有效期，5-15 10分钟
        jedis.setex(key,10*60,code);
        jedis.close();
        //返回结果给页面
        return new Result(false, MessageConstant.SEND_VALIDATECODE_SUCCESS);

    }


    /**
     ( 快速登录 验证码
     * @param telephone
     * @return
     */
    @PostMapping("/send4Login")
    public Result send4Login(String telephone){
        Jedis jedis = jedisPool.getResource();
        //先判断是否发送过,从redis中去,如果有值,发送过来
        //redis中的key要带上业务标识
        String key = RedisMessageConstant.SENDTYPE_LOGIN + ":" + telephone;
        //redis中的验证码
        String codeInRedis = jedis.get(key);
        logger.debug("Redis中的验证码:{},{}",codeInRedis,telephone);
        if (!StringUtils.isEmpty(codeInRedis)) {
            //如果有值.发送过了
            return new Result(false,"验证码已经发送过了，请注意查收");
        }
        //没有发送，则，生成验证码
        String code = String.valueOf(ValidateCodeUtils.generateValidateCode(6));
      /*  //调用SMSUtils发送
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code);
        } catch (ClientException e) {
            e.printStackTrace();
            logger.error("发送验证码失败了",e);
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }*/
        logger.debug("验证码发送成功{},{}",code,telephone);
        //验证码存入redis，同时要设置有效期，5-15 10分钟
        jedis.setex(key,10*60,code);
        jedis.close();
        //返回结果给页面
        return new Result(false, MessageConstant.SEND_VALIDATECODE_SUCCESS);

    }

}
