package com.cognizant.loan;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoanController {

	@GetMapping("/loans/{number}")
	public Loan getLoan(@PathVariable String number) {
		// Dummy response without any backend connectivity
		return new Loan(number, "car", 400000, 3258, 18);
	}

}
