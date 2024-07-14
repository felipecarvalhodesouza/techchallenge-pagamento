package br.com.postech.pagamento.main.security;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

public class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
	
    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
    	
    	List<String> groups = jwt.getClaimAsStringList("cognito:groups");
        if (groups != null) {
            return groups.stream()
                    .map(role -> "ROLE_" + role.toUpperCase())
                    .map(role -> (GrantedAuthority) () -> role)
                    .toList();
        } else {
            return Collections.emptyList();
        }
    }
}
