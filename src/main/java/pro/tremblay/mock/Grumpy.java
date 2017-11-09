package pro.tremblay.mock;

/**
 * @author Henri Tremblay
 */
public class Grumpy {

  public Grumpy() {
    throw new RuntimeException("I don't like to be called");
  }

  public String mood() {
    return "unhappy";
  }
}
