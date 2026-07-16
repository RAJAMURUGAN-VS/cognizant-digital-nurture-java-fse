package com.cognizant.account;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

	@GetMapping("/accounts/{number}")
	public Account getAccount(@PathVariable String number) {
		// Dummy response without any backend connectivity
		return new Account(number, "savings", 234343);
	}

}
