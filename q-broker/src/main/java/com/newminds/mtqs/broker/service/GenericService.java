package com.newminds.mtqs.broker.service;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Sunand on 2019-02-27
 */
public abstract class GenericService<T> implements Service<T> {

  public Flux<T> findAll() {
    return getRepository().findAll();
  }

  public Mono<T> find(String id) {
    return getRepository().findById(id);
  }

  public void delete(String id) {
    getRepository().delete(id);
  }

  public void deleteAll() {
    getRepository().deleteAll();
  }

  public Mono<T> save(T object) {
    return getRepository().save(object);
  }

  public Mono<T> update(T object) {
    return getRepository().save(object);
  }

  public abstract ReactiveMongoRepository getRepository();
}
