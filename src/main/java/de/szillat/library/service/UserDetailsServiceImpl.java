package de.szillat.library.service;

import de.szillat.library.model.User;
import de.szillat.library.model.UserDetailsImpl;
import de.szillat.library.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger _log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        assert userRepository != null;

        _log.debug("Loading username = '{}'...", username);

        User user = userRepository.findByUsername(username);
        if (user == null) throw new UsernameNotFoundException(username);

        // TODO Ask AuthoritiesRepository for Roles.
        return new UserDetailsImpl(user);
    }
}
