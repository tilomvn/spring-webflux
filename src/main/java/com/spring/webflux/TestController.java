package com.spring.webflux;

import java.time.Duration;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;

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
	
	/**
	 * Response message each second.
	 */
	@GetMapping(value = "/test/stream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<Model> testStreamFlux() {
		return Flux.generate((SynchronousSink<Model> sink) -> sink.next(new Model("body"))).delayElements(Duration.ofSeconds(1L));
	}
	
	class Model {
		String value;
		
		Model(String value) { this.value = value; }
		
		public String getValue() { return value; }
	}
}
