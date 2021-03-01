package com.tceasy.monitor.notifier;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.domain.events.InstanceStatusChangedEvent;
import de.codecentric.boot.admin.server.notify.AbstractStatusChangeNotifier;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Notifier submitting events to DingTalk.
 */
public class DingTalkNotifier  extends AbstractStatusChangeNotifier {

    private static final String DEFAULT_MESSAGE = "#{instance.registration.name} #{instance.id} is  #{event.statusInfo.status}";

    private final SpelExpressionParser parser = new SpelExpressionParser();

    /* DingTalk robot Webhook address*/
    private String apiUrl;

    /* Notifier switch*/
    private boolean enable = true;

    @Value("${spring.profiles.active}")
    private String profiles;

    private final Map<String, GroupConfiguration> groupConfigurations = new LinkedHashMap();

    private RestTemplate restTemplate = new RestTemplate();

    private Expression message;


    public DingTalkNotifier(InstanceRepository repository) {
        super(repository);
        this.message = parser.parseExpression(DEFAULT_MESSAGE, ParserContext.TEMPLATE_EXPRESSION);
    }

    @Override
    protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {
        return Mono.fromRunnable(
                () -> sendMessage(event, instance));
    }

    private void sendMessage(InstanceEvent event, Instance instance) {
        GroupConfiguration groupConfiguration = this.groupConfigurations.get(instance.getRegistration().getName());

        if (!GroupConfiguration.valid(groupConfiguration) && this.enable){
            return;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        TextMessage textMessage = TextMessage.builder().at(TextMessage.buildAtBean(Arrays.asList(groupConfiguration.getAtMobiles()), false))
                .msgtype("text")
                .text(TextMessage.buildTextBean(getDefaultMessage(event, instance)))
                .build();

        String content = JSONObject.toJSONString(textMessage);
        HttpEntity<String> request = new HttpEntity<>(content, headers);
        ResponseEntity<String> postForEntity = restTemplate.postForEntity(buildUrl(groupConfiguration), request, String.class);
        String body = postForEntity.getBody();
        System.out.println(body);
    }



    private String buildUrl(GroupConfiguration groupConfiguration) {
        Long timestamp = System.currentTimeMillis();
        return this.apiUrl + "?access_token=" + groupConfiguration.getAccessToken() + "&timestamp=" + timestamp + "&sign=" + getSign(timestamp, groupConfiguration.getSecret());
    }

    private String getSign(Long timestamp, String secret){
        try {
            String stringToSign = timestamp + "\n" + secret;
            Mac mac =  Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
            return URLEncoder.encode(new String(Base64.encodeBase64(signData)),"UTF-8");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 获取默认的消息
     * @param event
     * @param instance
     * @return
     */
    private String getDefaultMessage(InstanceEvent event, Instance instance){
        Map<String, Object> root = new HashMap<>();
        root.put("event", event);
        root.put("instance", instance);
        root.put("lastStatus", getLastStatus(event.getInstance()));
        StandardEvaluationContext context = new StandardEvaluationContext(root);
        context.addPropertyAccessor(new MapAccessor());
        return message.getValue(context, String.class);
    }


    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getProfiles() {
        return profiles;
    }

    public void setProfiles(String profiles) {
        this.profiles = profiles;
    }

    public Map<String, GroupConfiguration> getGroupConfigurations() {
        return groupConfigurations;
    }
}
