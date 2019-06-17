package es.codeurjc.test.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MessageController {

	private static Logger LOG = LoggerFactory.getLogger(MessageController.class);

	@Autowired
	private MessageRepository repository;

	@GetMapping("/")
	public String showMessages(Model model) {

		model.addAttribute("messages", repository.findAll());

		return "index";
	}

	@PostMapping("/")
	public String newMessage(Message message) {

		repository.save(message);

		LOG.info("Message "+message+" added");

		return "redirect:/";
	}

}
