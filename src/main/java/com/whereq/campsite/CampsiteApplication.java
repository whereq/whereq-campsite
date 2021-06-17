package com.whereq.campsite;

import com.whereq.campsite.service.CampsiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class CampsiteApplication {

	@Autowired
	CampsiteService campsiteService;

	public static void main(String[] args) {
		SpringApplication.run(CampsiteApplication.class, args);
	}

	@PostConstruct
	public void postConstruct() {
		try {
		  // This is just for sync up some test data
			campsiteService.syncReservationAndAvailability();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("swallow the exception.");
			System.exit(0);
		}
	}
}
