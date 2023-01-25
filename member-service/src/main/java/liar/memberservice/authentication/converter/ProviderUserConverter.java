package liar.memberservice.authentication.converter;

public interface ProviderUserConverter<T, R> {

    R converter(T t);
}
