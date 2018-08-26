package wwr.configuration;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.stereotype.Component;



@Component
public class CustomCsrfTokenRepository implements CsrfTokenRepository {

	 private static final Logger log = org.slf4j.LoggerFactory.getLogger(CustomCsrfTokenRepository.class);

	
	  public static final String CSRF_PARAMETER_NAME = "_csrf";

	  public static final String CSRF_HEADER_NAME = "X-XSRF-TOKEN";

	  private final Map<String, CsrfToken> tokenRepository = new ConcurrentHashMap<>();

	  public CustomCsrfTokenRepository() {
		  log.info("Creating {}", CustomCsrfTokenRepository.class.getSimpleName());
	  }

	  @Override
	  public CsrfToken generateToken(HttpServletRequest request) {
	    return new DefaultCsrfToken(CSRF_HEADER_NAME, CSRF_PARAMETER_NAME, createNewToken());
	  }

	  @Override
	  public void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response) {
	    String key = getKey(request);
	    if (key == null)
	      return;

	    if (token == null) {
	      tokenRepository.remove(key);
	    } else {
	      tokenRepository.put(key, token);
	    }
	  }

	  @Override
	  public CsrfToken loadToken(HttpServletRequest request) {
	    String key = getKey(request);
	    return key == null ? null : tokenRepository.get(key);
	  }

	  private String getKey(HttpServletRequest request) {
	    return request.getHeader("Authorization");
	  }

	  private String createNewToken() {
	    return UUID.randomUUID().toString();
	  }
	}
