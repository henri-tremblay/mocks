package pro.tremblay.mock;

import org.easymock.Capture;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.getCurrentArguments;
import static org.easymock.EasyMock.isNull;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.niceMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

/**
 * @author Henri Tremblay
 */
public class SystemEasyMockTest {

  // Dummy
  @Test
  public void newlyCreatedSystem_hasNoLoggedInUsers() {
    Authorizer authorizer = niceMock(Authorizer.class);
    replay(authorizer);

    System system = new System(authorizer);
    assertThat(system.loginCount()).isEqualTo(0);
  }

  // Stub
  @Test
  public void isAuthorized() {
    Authorizer authorizer = mock(Authorizer.class);
    expect(authorizer.authorize(anyString(), anyString())).andStubReturn(AuthStatus.GRANTED);
    replay(authorizer);

    System system = new System(authorizer);
    assertThat(system.isAuthorized()).isTrue();
  }

  // Spy
  @Test
  public void isAuthorizedForSure() {
    Authorizer authorizer = mock(Authorizer.class);

    expect(authorizer.authorize(isNull(), anyString())).andReturn(AuthStatus.GRANTED);
    replay(authorizer);

    System system = new System(authorizer);
    assertThat(system.isAuthorized()).isTrue();

    verify(authorizer);
  }

  @Test
  public void isAuthorizedForSure_usingCapture() {
    Authorizer authorizer = mock(Authorizer.class);

    Capture<String> capture = Capture.newInstance();

    expect(authorizer.authorize(capture(capture), anyString())).andReturn(AuthStatus.GRANTED);
    replay(authorizer);

    System system = new System(authorizer);
    assertThat(system.isAuthorized()).isTrue();

    assertThat(capture.getValue()).isNull();

    verify(authorizer);
  }

  // Fake
  @Test
  public void bob() {
    Authorizer authorizer = mock(Authorizer.class);
    // EasyMock keeps an ordered recording. So you are sure the first expectation will be checked
    // first. So you can fake like this. Doing the same with Mockito won't work
    expect(authorizer.authorize(eq("Bob"), anyString())).andStubReturn(AuthStatus.GRANTED);
    expect(authorizer.authorize(anyString(), anyString())).andStubReturn(AuthStatus.DENIED);
    replay(authorizer);

    System system = new System(authorizer);
    system.login("Bob", "password");
    assertThat(system.isAuthorized()).isTrue();
    system.login("Joe", "password");
    assertThat(system.isAuthorized()).isFalse();
  }

  @Test
  public void bob2() {
    Authorizer authorizer = mock(Authorizer.class);
    expect(authorizer.authorize(anyString(), anyString()))
        .andStubAnswer(() -> "Bob".equals(getCurrentArguments()[0]) ? AuthStatus.GRANTED : AuthStatus.DENIED);
    replay(authorizer);

    System system = new System(authorizer);
    system.login("Bob", "password");
    assertThat(system.isAuthorized()).isTrue();
    system.login("Joe", "password");
    assertThat(system.isAuthorized()).isFalse();
  }
}

