package com.vivebest.banking.core.aml.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vivebest.banking.core.aml.web.service.TicketService;

@RestController
public class TicketController {

	@Autowired
	private TicketService ticketService;
	
	@GetMapping
	public String getTicket(String ticketSeqNo) {
		String res = ticketService.queryTicketStock(ticketSeqNo);
		return res;
	}
}
