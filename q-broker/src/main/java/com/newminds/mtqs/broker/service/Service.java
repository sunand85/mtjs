package com.newminds.mtqs.broker.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Sunand on 2019-02-27
 */
public interface Service<T> {

  Flux<T> findAll();

  Mono<T> find(String id);

  void delete(String id);

  void deleteAll();

  Mono<T> save(T object);

  Mono<T> update(T object);

}
