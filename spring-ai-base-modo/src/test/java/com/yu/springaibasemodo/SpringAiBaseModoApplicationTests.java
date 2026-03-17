package com.yu.springaibasemodo;

import com.alibaba.cloud.ai.dashscope.audio.DashScopeAudioSpeechModel;
import com.alibaba.cloud.ai.dashscope.audio.DashScopeAudioSpeechOptions;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisPrompt;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisResponse;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageModel;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageOptions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.DefaultChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import reactor.core.publisher.Flux;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

@SpringBootTest
class SpringAiBaseModoApplicationTests {

	@Autowired
	private DashScopeChatModel chatModel;

	@Autowired
	private DashScopeImageModel imageModel;

	@Test
	void contextLoads() {
	}

	@Test
	void modelTest1(){
		ChatResponse res = chatModel.call(new Prompt("你是谁"));
		String response = res.getResult().getOutput().getText();
		System.out.println("模型回复: " + response);
	}


	//生成图片
	@Test
	void modelTest2(){
		DashScopeImageOptions imageOptions = DashScopeImageOptions.builder()
//				.withModel("wanx2.1-t2i-turbo")
				.withSize("1024*1024")
				.withN(1)
				.build();

		ImageResponse imageResponse = imageModel.call(
				new ImagePrompt("白色的狮子狗，眼睛周围是橘黄色", imageOptions));
		String imageUrl = imageResponse.getResult().getOutput().getUrl();

		// 图片url
		System.out.println(imageUrl);

		// 图片base64
		// imageResponse.getResult().getOutput().getB64Json();

        /*
        按文件流相应
        InputStream in = url.openStream();

        response.setHeader("Content-Type", MediaType.IMAGE_PNG_VALUE);
        response.getOutputStream().write(in.readAllBytes());
        response.getOutputStream().flush();*/
	}

	//生成语音
	// https://bailian.console.aliyun.com/?spm=5176.29619931.J__Z58Z6CX7MY__Ll8p1ZOR.1.74cd59fcXOTaDL&tab=doc#/doc/?type=model&url=https%3A%2F%2Fhelp.aliyun.com%2Fdocument_detail%2F2842586.html&renderType=iframe
	@Test
	public void testText2Audio(@Autowired DashScopeAudioSpeechModel speechSynthesisModel) throws IOException {
		DashScopeAudioSpeechOptions options = DashScopeAudioSpeechOptions.builder()
				//.voice()   // 人声
				//.speed()    // 语速
				//.model()    // 模型
				//.responseFormat(DashScopeSpeechSynthesisApi.ResponseFormat.MP3)
				.build();

		SpeechSynthesisResponse response = speechSynthesisModel.call(
				new SpeechSynthesisPrompt("大家好， 我是人帅活好的徐庶。",options)
		);

		File file = new File( System.getProperty("user.dir") + "/output.mp3");
		try (FileOutputStream fos = new FileOutputStream(file)) {
			ByteBuffer byteBuffer = response.getResult().getOutput().getAudio();
			fos.write(byteBuffer.array());
		}
		catch (IOException e) {
			throw new IOException(e.getMessage());
		}
	}

	//测试chatClient
	@Test
	public void chatClientTest(){
		ChatClient chatClient = ChatClient.builder(chatModel).build();

		Flux<String> response = chatClient.prompt()
				.user("你是什么模型") //用户提示词
				.stream()
				.content();

		//阻塞式输出
		response.toIterable().forEach(v -> System.out.println(v));
	}

	//提示词模版
	@Test
	public void PromptTemplateTestByChatModel(){
		//系统提示词
		SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate("""
				你是一个{something}领域的专家，你需要耐心的为用户解决{something}领域的问题。
				""");
		//用户提示词
		Message userMessage = new UserMessage("请告诉我rocketMq消息队列可以用在哪些地方");

		Message systemMessage = systemPromptTemplate.createMessage(Map.of("something", "java后端开发"));

		Prompt prompt1 = new Prompt(systemMessage, userMessage);
		//或者
		Prompt prompt2 = new Prompt(List.of(systemMessage, userMessage));

		Flux<ChatResponse> stream = chatModel.stream(prompt1);
		stream.toIterable().forEach(v -> System.out.println(v.getResult().getOutput().getText()));
	}

	@Test
	public void PromptTemplateTestByChatClient(){
		ChatClient chatClient = ChatClient.builder(chatModel).build();

		//系统提示词
		SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate("""
				你是一个{something}领域的专家，你需要耐心的为用户解决{something}领域的问题。
				""");
		//用户提示词
		Message userMessage = new UserMessage("请告诉我rocketMq消息队列可以用在哪些地方");

		Message systemMessage = systemPromptTemplate.createMessage(Map.of("something", "java后端开发"));

		Prompt prompt = new Prompt(systemMessage, userMessage);

		ChatClient.StreamResponseSpec stream = chatClient.prompt(prompt).stream();

		Flux<String> content = stream.content();
		content.toIterable().forEach(v -> System.out.println(v));

		//或者链式

		Flux<String> content1 = chatClient.prompt()
				.user(u -> u.text("告诉我{composer1}或{composer2}的五部电影.")
						.param("composer1", "周星驰")
						.param("composer2", "成龙"))
				.system(s -> s.text("""
								你是一个{something}领域的专家，你需要耐心的为用户解决{something}领域的问题。
								""")
						.param("something", "电影"))
				.stream()
				.content();
		content1.toIterable().forEach(v -> System.out.println(v));
	}

	//自定义提示词模版
	@Test
	public void createPromptTemplateByChatModel(){
		PromptTemplate promptTemplate = PromptTemplate.builder()
				.renderer(StTemplateRenderer.builder().startDelimiterToken('<').endDelimiterToken('>').build()) //渲染模版样式,占位符用<>表示
				.template("告诉我5部<composer>的电影.")
				.build();

		String prompt = promptTemplate.render(Map.of("composer", "周星驰"));
		Flux<String> stream = chatModel.stream(prompt);
	}

	@Test
	public void createPromptTemplateByChatClient(){
		//外部文件加载提示词模版
		SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemResource);

		String answer = ChatClient.create(chatModel).prompt()
				.user(u -> u
						.text("告诉我5部<composer>的电影")
						.param("composer", "John Williams"))
				.templateRenderer(StTemplateRenderer.builder().startDelimiterToken('<').endDelimiterToken('>').build())
				.call()
				.content();
		String call = chatModel.call(answer);
	}


	//外部文件加载提示词模版
	@Value("classpath:/prompts/system-message.st")
	private Resource systemResource;

	@Test
	public void testPrompt() {
		ChatClient  chatClient = ChatClient.builder(chatModel)
				.defaultSystem(systemResource)
				.build();

		String content = chatClient.prompt()
				.system(p -> p.param("composer","周星驰"))
				.call()
				.content();

		System.out.println(content);
	}


	/**
	 * 提示词技巧
	 * # 角色
	 * 你是一位热情、专业的导游，熟悉各种旅游目的地的风土人情和景点信息。你的任务是根据用户的需求，为他们规划一条合理且有趣的旅游路线。
	 *
	 * ## 技能
	 * ### 技能1：理解客户需求
	 * - 询问并了解用户的旅行偏好，包括但不限于目的地、预算、出行日期、活动偏好等信息。
	 * - 根据用户的需求，提供个性化的旅游建议。
	 *
	 * ### 技能2：规划旅游路线
	 * - 结合用户的旅行偏好，设计一条详细的旅游路线，包括行程安排、交通方式、住宿建议、餐饮推荐等。
	 * - 提供每个景点的详细介绍，包括历史背景、特色活动、最佳游览时间等。
	 *
	 * ### 技能3：提供实用旅行建议
	 * - 给出旅行中的实用建议，如必备物品清单、当地风俗习惯、安全提示等。
	 * - 回答用户关于旅行的各种问题，例如签证、保险、货币兑换等。
	 * - 如果有不确定的地方，可以调用搜索工具来获取相关信息。
	 *
	 * ## 限制
	 * - 只讨论与旅行相关的话题。
	 * - 确保所有推荐都基于客户的旅行需求。
	 * - 不得提供任何引导客户参与非法活动的建议。
	 * - 所提供的价格均为预估，可能会受到季节等因素的影响。
	 * - 不提供预订服务，只提供旅行建议和信息。
	 * # 知识库
	 * 请记住以下材料，他们可能对回答问题有帮助。
	 */


}
