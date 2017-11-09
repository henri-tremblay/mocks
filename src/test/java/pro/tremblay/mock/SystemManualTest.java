package pro.tremblay.mock;

import org.junit.Test;
import org.objenesis.ObjenesisHelper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.niceMock;

/**
 * @author Henri Tremblay
 */
public class SystemManualTest {

  private static class GrumpyMock extends Grumpy {
    @Override
    public String mood() {
      return "happy";
    }
  }

  // Class mocking
  @Test
  public void mockAParentThatDoesntLikeToBeInstantiated() {
    Grumpy grumpy = ObjenesisHelper.newInstance(GrumpyMock.class);
    assertThat(grumpy.mood()).isEqualTo("happy");
  }

  public static class DummyAuthorizer implements Authorizer {
    public AuthStatus authorize(String username, String password) {
      return null;
    }
  }

  // Dummy
  @Test
  public void newlyCreatedSystem_hasNoLoggedInUsers() {
    Authorizer authorizer = new DummyAuthorizer();
    System system = new System(authorizer);

    assertThat(system.loginCount()).isEqualTo(0);
  }

  public static class AcceptingAuthorizerStub implements Authorizer {
    public AuthStatus authorize(String username, String password) {
      return AuthStatus.GRANTED;
    }
  }

  // Stub
  @Test
  public void isAuthorized() {
    Authorizer authorizer = new AcceptingAuthorizerStub();

    System system = new System(authorizer);
    assertThat(system.isAuthorized()).isTrue();
  }

  public static class AcceptingAuthorizerSpy implements Authorizer {
    public boolean authorizeWasCalled = false;

    public AuthStatus authorize(String username, String password) {
      authorizeWasCalled = true;
      return AuthStatus.GRANTED;
    }
  }

  // Spy
  @Test
  public void isAuthorizedForSure() {
    AcceptingAuthorizerSpy authorizer = new AcceptingAuthorizerSpy();

    System system = new System(authorizer);
    assertThat(system.isAuthorized()).isTrue();

    assertThat(authorizer.authorizeWasCalled).isTrue();
  }

  public static class AcceptingAuthorizerVerificationMock implements Authorizer {
    public boolean authorizeWasCalled = false;

    public AuthStatus authorize(String username, String password) {
      authorizeWasCalled = true;
      return AuthStatus.GRANTED;
    }

    public boolean verify() {
      return authorizeWasCalled;
    }
  }

  // Mock
  @Test
  public void isAuthorizedWithVerify() {
    AcceptingAuthorizerVerificationMock authorizer = new AcceptingAuthorizerVerificationMock();

    System system = new System(authorizer);
    assertThat(system.isAuthorized()).isTrue();

    authorizer.verify();
  }

  public static class AcceptingAuthorizerFake implements Authorizer {
    public AuthStatus authorize(String username, String password) {
      return username.equals("Bob") ? AuthStatus.GRANTED : AuthStatus.DENIED;
    }
  }

  // Fake
  @Test
  public void bob() {
    Authorizer authorizer = new AcceptingAuthorizerFake();

    System system = new System(authorizer);

    system.login("Bob", "password");
    assertThat(system.isAuthorized()).isTrue();

    system.login("Joe", "password");
    assertThat(system.isAuthorized()).isFalse();
  }
}

