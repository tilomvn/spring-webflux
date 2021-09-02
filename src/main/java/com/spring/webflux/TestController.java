package com.spring.webflux;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class TestController {

	@GetMapping("/test")
	public Mono<Model> testMono() {
		return Mono.just(new Model("body"));
	}
	
	@GetMapping("/test/list")
	public Flux<Model> testFlux() {
		return Flux.just(new Model("body1"), new Model("body2"));
	}
	
	class Model {
		String value;
		
		Model(String value) { this.value = value; }
		
		public String getValue() { return value; }
	}
}
