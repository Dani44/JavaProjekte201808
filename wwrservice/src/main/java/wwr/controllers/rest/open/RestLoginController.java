package wwr.controllers.rest.open;

import java.security.Principal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestLoginController {
	
	@RequestMapping("/rest/public/user")
	public Principal user(Principal user) {
		return user;
	}
	
}
