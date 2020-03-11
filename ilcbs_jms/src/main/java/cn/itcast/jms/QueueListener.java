package cn.itcast.jms;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.aliyuncs.exceptions.ClientException;

import com.xiaojihua.utils.RandomCode;
import com.xiaojihua.utils.SmsUtils;

@Component
public class QueueListener implements MessageListener{

	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	@Override
	public void onMessage(Message arg0) {
		// TODO Auto-generated method stub
		try {
			MapMessage message = (MapMessage) arg0;
			//System.out.println(message.getString("telphone"));
			
			String phone = message.getString("telphone");
			
			//向该手机号发送随机短息验证码
			String genCode = RandomCode.genCode() + "";
			
			System.out.println("接收到手机号===="+phone);
			
			// 使用短信工具类发送短信，但是由于阿里云平台没有注册暂时先注掉，模拟发送短信
			// SmsUtils.sendSms(phone, genCode);
			
			// 将短信验证码存redis中
			redisTemplate.opsForValue().set(phone, genCode);
			
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
