package com.javasampleapproach.springintegration.endpoint;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.python.core.PyClass;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.javasampleapproach.springintegration.msg.HelloMsg;

@Component
public class WelcomeEndpoint {
	private Logger log = LoggerFactory.getLogger(this.getClass().getName());
	PythonInterpreter pythonInterpreter = new PythonInterpreter();
	public Message<?> get(Message<String> msg) {
        String name = msg.getPayload();
        // Log
        log.info("Request with name = " + name);
        
        // Get currentTime
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String currentTime = dtf.format(now);
        
        String strMsg = "Hello " + name + "! " + "Welcome to Spring Integration with Spring Boot!";
        
        HelloMsg returnMsg = new HelloMsg(strMsg, currentTime);
        
        return MessageBuilder.withPayload(returnMsg)
            .copyHeadersIfAbsent(msg.getHeaders())
            .setHeader("http_statusCode", HttpStatus.OK)
            .build();
    }
	 public void execScriptAsInputStream (InputStream inputStream) {
	        pythonInterpreter.execfile(inputStream);
	    }
	public Message<?> getPython(Message<String> msg) throws IOException{
		String name = msg.getPayload();
		 log.info("Request with name = " + name);
		 ClassLoader classLoader = this.getClass().getClassLoader();
		 
	        File file = new File(classLoader.getResource("python/hello_world.py").getFile());
	         
	        //File is found
	        System.out.println("File Found : " + file.exists());
	         
	        //Read File Content
	        String content = new String(Files.readAllBytes(file.toPath()));
	        System.out.println(content);
		
	       // pythonInterpreter.exec("print('Hello python')");
		    	pythonInterpreter.execfile("src/main/resources/python/hello_world.py");
		        PyObject pyObject1 = pythonInterpreter.eval("repr(displayHello('"+name+"',4,5))");
		        String output = "{\"Output\":"+pyObject1.toString()+"}";
		       // PyClass mainClass = (PyClass) pythonInterpreter.get("Hello");
		        //PyObject main = mainClass.__call__();
		 
		 return MessageBuilder.withPayload(output)
		            .copyHeadersIfAbsent(msg.getHeaders())
		            .setHeader("http_statusCode", HttpStatus.OK)
		            .build();
	}
}