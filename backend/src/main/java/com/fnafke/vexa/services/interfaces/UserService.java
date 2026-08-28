package com.fnafke.vexa.services.interfaces;

import java.util.UUID;

import com.fnafke.vexa.models.User;

/**
 * Service interface for managing users.
 */
public interface UserService {

    /**
     * Find a user by their ID.
     *
     * @param id the ID of the user to find
     * @return the user with the specified ID
     */
    public User findById(UUID id);

    /**
     * Find a user by their username.
     *
     * @param username the username to search for
     * @return the user with the specified username
     */
    public User findByUsername(String username);

    /**
     * Find a user by their email.
     *
     * @param email the email to search for
     * @return the user with the specified email
     */
    public User findByEmail(String email);

    /**
     * Check if a user exists by their username.
     *
     * @param username the username to check
     * @return true if a user with the specified username exists, false otherwise
     */
    public boolean existsByUsername(String username);

    /**
     * Check if a user exists by their email.
     *
     * @param email the email to check
     * @return true if a user with the specified email exists, false otherwise
     */
    public boolean existsByEmail(String email);

    /**
     * Create a new user.
     *
     * @param user the user to create
     * @return the created user
     */
    public User createUser(User user);
}
