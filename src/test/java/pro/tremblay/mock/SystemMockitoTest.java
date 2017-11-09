package pro.tremblay.mock;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Henri Tremblay
 */
public class SystemMockitoTest {

  // Dummy
  @Test
  public void newlyCreatedSystem_hasNoLoggedInUsers() {
    Authorizer authorizer = mock(Authorizer.class);
    System system = new System(authorizer);
    
    assertThat(system.loginCount()).isEqualTo(0);
  }

  // Stub
  @Test
  public void isAuthorized() {
    Authorizer authorizer = mock(Authorizer.class);
    when(authorizer.authorize(any(), any())).thenReturn(AuthStatus.GRANTED); // NPE if commented

    System system = new System(authorizer);
    assertThat(system.isAuthorized()).isTrue();
  }

  // Spy
  @Test
  public void isAuthorizedForSure() {
    Authorizer authorizer = mock(Authorizer.class);
    when(authorizer.authorize(any(), any())).thenReturn(AuthStatus.GRANTED);

    System system = new System(authorizer);
    assertThat(system.isAuthorized()).isTrue();

    verify(authorizer).authorize(isNull(), isNull());
  }

  @Test
  public void isAuthorizedForSure_usingCapture() {
    Authorizer authorizer = mock(Authorizer.class);

    ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
    when(authorizer.authorize(captor.capture(), any())).thenReturn(AuthStatus.GRANTED);

    System system = new System(authorizer);
    assertThat(system.isAuthorized()).isTrue();

    assertThat(captor.getValue()).isNull();

    verify(authorizer).authorize(isNull(), isNull());
  }

  // Fake
  @Test
  public void bob() {
    Authorizer authorizer = mock(Authorizer.class);
    when(authorizer.authorize(any(), any()))
        .thenAnswer(invocationOnMock -> "Bob".equals(invocationOnMock.getArgument(0)) ?
            AuthStatus.GRANTED : AuthStatus.DENIED);

    System system = new System(authorizer);
    system.login("Bob", "password");
    assertThat(system.isAuthorized()).isTrue();
    system.login("Joe", "password");
    assertThat(system.isAuthorized()).isFalse();
  }
}
