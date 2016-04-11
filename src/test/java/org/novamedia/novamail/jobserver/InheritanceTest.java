package org.novamedia.novamail.jobserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.TreeMap;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.json.simple.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.novamedia.novamail.jobserver.runtime.JobServer;
import org.novamedia.novamail.jobserver.utils.NetworkUtils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InheritanceTest {

	private static final Logger log = Logger.getLogger(InheritanceTest.class);

	private static JobServer server;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		log.info("SlackHandlerTest.setUpBeforeClass()");
		Properties properties = new Properties();
		// Start the job server daemon
		server = new JobServer(properties);
		server.start();
		
		Logger rootLogger = Logger.getRootLogger();
		rootLogger.setLevel(Level.INFO);
		rootLogger.addAppender(new ConsoleAppender(new PatternLayout("%d{ABSOLUTE} %5p %t %c{1}:%M:%L - %m%n")));
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		log.info("SlackHandlerTest.tearDownAfterClass()");
		server.stop();
	}

	@Before
	public void setUp() throws Exception {
		log.info("SlackHandlerTest.setUp()");
	}

	@After
	public void tearDown() throws Exception {
		log.info("SlackHandlerTest.tearDown()");
	}

	@Test
	/**
	 * Check simple file.
	 * 
	 * @throws Exception
	 */
	public void test001() throws Exception {
		try {
			Thread.currentThread().sleep(10*1000);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
		log.info("SlackHandlerTest.test001()");
	 	TreeMap<String, String> requestMapSlackApiKey = new TreeMap();
	 	JSONObject body = new JSONObject();
	 	body.put("id", 1);
	 	body.put("account_id", 11);
	 	body.put("list_id", 111);
	 	List<Integer> sub_list_ids = new ArrayList<Integer>();
	 	sub_list_ids.add(1);
	 	sub_list_ids.add(2);
	 	sub_list_ids.add(3);
	 	body.put("sub_list_ids", sub_list_ids);
	 	body.put("campaign_id", 1111);
	 	body.put("email_id", 2);
	 	body.put("email_body_id", 22);
	 	body.put("email_subject_id", 222);
	 	body.put("send_profile_id", 3);
	 	body.put("mta_id", 4);
	 	body.put("send_at", "20.04.2015");
		String gatewayStr = NetworkUtils.getMethodUrlText(requestMapSlackApiKey, body.toString(), "POST", "http://localhost:8801/jobScheduler/scheduledEmailSendJob");
	}

}
