package liar.waitservice.wait.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.hash.ObjectHashMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class HashMapping<T> {
  private final HashOperations<String, byte[], byte[]> hashOperations;

  HashMapper<Object, byte[], byte[]> mapper = new ObjectHashMapper();

  public HashMapping(HashOperations<String, byte[], byte[]> hashOperations) {
    this.hashOperations = hashOperations;
  }

  public void writeHash(String key, T t) {

    Map<byte[], byte[]> mappedHash = mapper.toHash(t);
    hashOperations.putAll(key, mappedHash);
  }

  public T loadHash(String key) {

    Map<byte[], byte[]> loadedHash = hashOperations.entries("key");
    return (T) mapper.fromHash(loadedHash);
  }
}