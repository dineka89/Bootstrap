package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserDao userDao;
    @Autowired
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }



    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {

        return userDao.getUserByEmail(email);
    }

    @Override
    public void addUser(User user) {
        userDao.addUser(user);
        passwordEncoder();
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) {

        return userDao.getUserById(id);
    }

    @Override
    @Transactional
    public void updateUser(User user) {

        userDao.updateUser(user);
    }

    @Override
    @Transactional
    public void removeUserById(Long id) {

        userDao.removeUserById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> listUsers() {

        return userDao.listUsers();
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userDao.getUserByEmail(email);
        user.getAuthorities().size();
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", email));
        }
        return user.fromUser();
    }
}