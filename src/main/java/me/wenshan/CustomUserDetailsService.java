package me.wenshan;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import me.wenshan.userinfo.domain.User;
import me.wenshan.userinfo.service.UserServiceImp;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String arg0) throws UsernameNotFoundException {
		User user = UserServiceImp.get().getUser(arg0);
		if (user == null)
			return null;

		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), true,
				true, true, true, getGrantedAuthorities(user));
	}
	
	 private List<org.springframework.security.core.GrantedAuthority> getGrantedAuthorities(User user){
	        List<org.springframework.security.core.GrantedAuthority> authorities = new ArrayList<org.springframework.security.core.GrantedAuthority>();
	        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
	        return authorities;
	    }

}
