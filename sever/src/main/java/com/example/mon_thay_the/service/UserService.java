package com.example.mon_thay_the.service;


import com.example.mon_thay_the.entity.Role;
import com.example.mon_thay_the.entity.User;
import com.example.mon_thay_the.exception.UserNotFoundException;
import com.example.mon_thay_the.principal.MyUserDetails;
import com.example.mon_thay_the.repository.RoleRepository;
import com.example.mon_thay_the.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class UserService {

    public static final int USER_PER_PAGE = 9;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;



    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public User getCurrentlyLoggedInUser(Authentication authentication){
        if(authentication == null) return null;

        User user = null;
        Object principal = authentication.getPrincipal();

        if(principal instanceof MyUserDetails){
            String userName = ((MyUserDetails) principal).getUsername();
            user = userRepository.getUserByEmail(userName);
        }
        return user;
    }

    public User getUserByEmail(String email) throws UserNotFoundException {
        User user = userRepository.getUserByEmail(email);
        if(user == null){
            throw new UserNotFoundException("Couldn't found user with the email: "+ email);
        }
        return user;
    }

//    public boolean isEmailUnique(Integer userId , String email){
//
//        User userByEmail = userRepository.getUserByEmail(email);
//
//        if(userByEmail == null) return true;
//        boolean isCreatingNew = (userId == null);
//        if(isCreatingNew){
//            if(userByEmail != null) return false;
//        }else if(userByEmail.getId() != userId) return false;
//
//        return true;
//    }

    public boolean isEmailUnique(String email){

        User userByEmail = userRepository.getUserByEmail(email);

        return userByEmail == null;
    }

    public User saveUser(User user){
        encodePassword(user);
        return userRepository.save(user);
    }

    public User save(User user) {
        boolean isUpdatingUser = (user.getId() != null);
        User existingUser = null;
        if(isUpdatingUser){
            existingUser = userRepository.findById(user.getId()).get();
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
//            existingUser.setPhone(user.getPhone());
        }
        return userRepository.save(existingUser);
    }
//    public User save(User user) {
//        boolean isUpdatingUser = (user.getId() != null);
//        if(isUpdatingUser){
//            User existingUser = userRepo.findById(user.getId()).get();
//            if(user.getPassword().isEmpty()){
//                user.setPassword(existingUser.getPassword());
//            }
//            else encodePassword(user);
//        }else {
//            encodePassword(user);
//        }
//
//        return userRepo.save(user);
//    }


    public void encodePassword(User user){
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    public Role getRoles(){
        return roleRepository.getRole();
    }

    public User getById(Integer id) throws UserNotFoundException {
        try{
            return userRepository.findById(id).get();
        }catch (NoSuchElementException ex){
            throw new UserNotFoundException("Cant not find user with id: "+id);
        }
    }

    public Page<User> listByPage(Integer pageNum, String keyword, String sortField, String sortDir) {
        Pageable pageable = null;

        if(sortField != null && !sortField.isEmpty()) {
            Sort sort = Sort.by(sortField);
            sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
            pageable = PageRequest.of(pageNum - 1, USER_PER_PAGE, sort);
        }
        else {
            pageable = PageRequest.of(pageNum - 1, USER_PER_PAGE);
        }

        if (keyword != null && !keyword.isEmpty()) {
            return userRepository.findAll(keyword, pageable);
        }
        return userRepository.findAll(pageable);
    }


    public User createNewUser(){
        Role userRole = roleRepository.getRole();
        User newUser = new User();
        newUser.setRole(userRole);
        return newUser;
    }

    public List<Role> listRoles() {
        return (List<Role>) roleRepository.findAll();
    }
    public void deleteUser(Integer id) throws UserNotFoundException {
        Long count = userRepository.countById(id);
        if (count == null || count == 0) {
            throw new UserNotFoundException("Could not find any user with ID " + id);
        }

        userRepository.deleteById(id);
    }

    public User getUserByID(int id) throws UserNotFoundException {
        try {
            User user = userRepository.findById(id).get();
            return user;
        }
        catch(NoSuchElementException ex) {
            throw new UserNotFoundException("Could not find any user with ID " + id);

        }
    }


//    public boolean registerNewUser(String email, String firstName, String lastName, String phone, String password) {
//        User newUser = new User(email, password, firstName, lastName, phone);
//        Role userRole = new Role(2);
//        newUser.setRole(userRole);
//        encodePassword(newUser);
//        User savedUser =  userRepository.save(newUser);
//        if(savedUser != null){
//            return true;
//        }
//        return false;
//    }



}
