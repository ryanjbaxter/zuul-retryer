package com.example;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ryan Baxter
 */
@RestController
public class SelfController {
	@RequestMapping("/timeout")
	public String timeout() {
		try {
			Thread.sleep(80000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "timeout";
	}

	@RequestMapping("/servererror")
	public String error() {
		throw new RuntimeException("error");
	}
}
