package cn.itcast.client.action;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import cn.itcast.client.domain.UserClient;
import cn.itcast.client.service.UserClientService;
import com.xiaojihua.utils.ImageUtil;

public class UserAction extends BaseAction{

	private String vercode;  //图片验证码
	
	private String phoneVercode;  //短信验证码
	
	private String telephone;
	
	private String email;

	public String getVercode() {
		return vercode;
	}

	public void setVercode(String vercode) {
		this.vercode = vercode;
	}

	public String getPhoneVercode() {
		return phoneVercode;
	}

	public void setPhoneVercode(String phoneVercode) {
		this.phoneVercode = phoneVercode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	/**
	 * 配置文件中只配置了queue的，注掉了topic的否则就得写
	 * @Qualifier注解了
	 */
	@Autowired
	private JmsTemplate queueTemplate;
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	@Autowired
	private UserClientService userClientService;
	
	/**
	 * 绘制验证图片
	 * @return
	 * @throws Exception
	 */
	@Action(value="userAction_genActiveCode")
	public String genActiveCode() throws Exception {
		// TODO Auto-generated method stub
		HttpServletResponse response = ServletActionContext.getResponse();
		
		String rundomStr = ImageUtil.getRundomStr();
		
		session.put("phoneCode", rundomStr);  //通过session保存图片验证码
		
		ImageUtil.getImage(rundomStr, response.getOutputStream());
		return NONE;
	} 
	
	@Action(value="userAction_sendVerCode")
	public String sendVerCode() throws Exception {
		// TODO Auto-generated method stub
		// 发送一条消息
		queueTemplate.send("ilcbs_client", new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				// TODO Auto-generated method stub
				MapMessage mapMessage = session.createMapMessage();
				mapMessage.setString("telphone", telephone);
				return mapMessage;
			}
		});
		
		return NONE;
	}
	
	@Action(value="userAction_register")
	public String register() throws Exception {
		// TODO Auto-generated method stub
		String returnStr = "2";
		
		// 判断图片验证码是否正确
		String phoneCode = (String) session.get("phoneCode"); // 从session中取出用户的图片验证字符串进行比较
		if (!phoneCode.equalsIgnoreCase(vercode)){
			returnStr = "0";
		}
		
		// 判断短信验证码是否正确
		String smsCode = redisTemplate.opsForValue().get(telephone);
		if(!smsCode.equals(phoneVercode)){
			returnStr = "1";
		}
		
		// 清空缓存,将用户信息并保存数据库
		if(returnStr.equals("2")){
			session.remove("phoneCode");
			redisTemplate.delete(telephone);
			
			UserClient entity = new UserClient();
			entity.setTelephone(telephone);
			entity.setEmail(email);
			userClientService.saveOrUpdate(entity);
		}
		
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().write(returnStr);
		return NONE;
	}
}
