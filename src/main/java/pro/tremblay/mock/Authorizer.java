package pro.tremblay.mock;

/**
 * @author Henri Tremblay
 */
public interface Authorizer {
  AuthStatus authorize(String username, String password);
}
