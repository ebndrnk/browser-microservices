package org.ebndrnk.springbrauser.service.user_actions;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.ebndrnk.springbrauser.model.dao.Role;
import org.ebndrnk.springbrauser.model.dao.User;
import org.ebndrnk.springbrauser.reposiroty.user.RoleRepository;
import org.ebndrnk.springbrauser.reposiroty.user.UserRepository;
import org.ebndrnk.springbrauser.service.util.LogService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    @PersistenceContext
    private final EntityManager em;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final LogService logService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logService.logInfo("Finding user by username");
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public User findUserById(Long userId) {
        Optional<User> userFromDb = userRepository.findById(userId);
        return userFromDb.orElse(new User());
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }

    public boolean saveUser(User user) {
        User userFromDB = userRepository.findByUsername(user.getUsername());
        logService.logInfo("Checking to sample usernames");
        if (userFromDB != null) {
            return false;
        }

        logService.logInfo("Setting role \"ROLE_USER\"");

        Role roleUser = roleRepository.findById(1L).orElse(new Role(1L, "ROLE_USER")); // Предполагается, что у вас есть roleRepository
        user.setRoles(Collections.singleton(roleUser));

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        logService.logInfo("Saving user to db");
        userRepository.save(user);
        return true;
    }

    public boolean deleteUser(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    public List<User> usergtList(Long idMin) {
        return em.createQuery("SELECT u FROM User u WHERE u.id > :paramId", User.class)
                .setParameter("paramId", idMin).getResultList();
    }

    public Long getCurrentUserId() {
        try {
            logService.logInfo("trying to getting user principal and cast to UserDetails");
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            logService.logInfo("assigment values");
            return ((User) userDetails).getId();
        }catch (Exception e){
            logService.logWarn("unauthorized user");
            return -1L;
        }
    }

}
