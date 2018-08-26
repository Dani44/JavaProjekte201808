package wwr.configuration;



import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import wwr.model.enities.Credential;
import wwr.repositories.CredentialRepository;




@EnableWebSecurity
public class ApplicationSecurity extends WebSecurityConfigurerAdapter {
	
	private static final Logger log = org.slf4j.LoggerFactory.getLogger(ApplicationSecurity.class);

	PasswordEncoder passwordEncoder =
		    PasswordEncoderFactories.createDelegatingPasswordEncoder();

	@Autowired
    private CustomCsrfTokenRepository customCsrfTokenRepository;
	
    @Autowired
    private CredentialRepository credentialRepository;
    
    @Autowired
    private LogoutSuccess logoutSuccess ;
	
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	  
	  PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
	  
	  Iterable<Credential> credentials = credentialRepository.findAll() ;
	  
	  for( Credential cred: credentials ){
		  
		  UserDetails userDetails = User.withUsername(cred.getPerson().getNickname())
		  .password(encoder.encode(cred.getPassword()))
		  .roles("ADMIN","USER")
		  .build();
		  
		  auth.inMemoryAuthentication().withUser(userDetails);

		  log.info("Creating {}","User:" + cred.getPerson().getNickname() + " registriert ...");
	  }

	}
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {

    	http
    	.httpBasic()
    	
    	.and().authorizeRequests()
			   .antMatchers("/rest/secure/**").authenticated()
			   .anyRequest().permitAll()
			
		.and().logout().logoutSuccessHandler(logoutSuccess)
						.deleteCookies("JSESSIONID")
						.invalidateHttpSession(false).permitAll()
	    
		.and().csrf()
			.ignoringAntMatchers("/login", "/logout","/rest/secure/**")
			.csrfTokenRepository(customCsrfTokenRepository) ;
			 
		// .and().addFilterAfter(csrfHeaderFilter(), CsrfFilter.class) ;
        
    }
    
//    private OncePerRequestFilter csrfHeaderFilter() {
//        return new OncePerRequestFilter() {
//            @Override
//            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//                    throws ServletException, IOException {
//                CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
//                if (csrf != null) {
//                    Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
//                    String token = csrf.getToken();
//                    if (cookie == null || token != null && !token.equals(cookie.getValue())) {
//                        cookie = new Cookie("XSRF-TOKEN", token);
//                        cookie.setPath("/");
//                        response.addCookie(cookie);
//                    }
//                }
//                filterChain.doFilter(request, response);
//            }
//        };
//    }

    
    
    
    
	
}
