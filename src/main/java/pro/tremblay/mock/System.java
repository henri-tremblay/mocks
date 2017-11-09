package pro.tremblay.mock;

/**
 * @author Henri Tremblay
 */
public class System {

  private Authorizer authorizer;
  private int loginCount;
  private String user;
  private String password;

  public System(Authorizer authorizer) {
    this.authorizer = authorizer;
  }

  public void login(String user, String password) {
    loginCount++;
    this.user = user;
    this.password = password;
  }

  public int loginCount() {
    return loginCount;
  }

  public boolean isAuthorized() {
    return authorizer.authorize(user, password).equals(AuthStatus.GRANTED);
  }
}
