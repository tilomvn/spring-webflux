package com.spring.webflux;

import java.time.Duration;
import java.util.Random;
import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@RestController
@RequestMapping("/api")
public class TestController {

	String[] bodies = {"body1", "test", "yuh", "meomeo", "care"};
	Random rand = new Random();
	Consumer<SynchronousSink<Model>> modelGenerator = args -> {
		args.next(new Model(bodies[rand.nextInt(5)]));
	};
	
	
	/**
	 * Annotation Base Support
	 */
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
		return Flux.generate(modelGenerator).delayElements(Duration.ofSeconds(1L));
	}
	
	/**
	 * Function Web Framework Support
	 */
	@Bean
	RouterFunction<ServerResponse> getTest() {
		return route(GET("/api/func/test"), req -> ok().body(Mono.just(new Model("test")), Model.class));
	}
	@Bean
	RouterFunction<ServerResponse> reactiveStreamTest() {
		return route(GET("/api/func/test/stream"), req -> ok().contentType(MediaType.APPLICATION_STREAM_JSON)
				.body(Flux.generate(modelGenerator).delayElements(Duration.ofSeconds(1L)), Model.class));
	}
	
	class Model {
		String value;
		
		Model(String value) { this.value = value; }
		
		public String getValue() { return value; }
	}
}
