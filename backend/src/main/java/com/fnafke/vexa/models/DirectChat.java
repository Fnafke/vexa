package com.fnafke.vexa.models;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "direct_chats")
@PrimaryKeyJoinColumn(name = "id")
public class DirectChat extends Chat {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_one_id", nullable = false)
    private User userOne;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_two_id", nullable = false)
    private User userTwo;

    protected DirectChat() {
    }

    public DirectChat(User user1, User user2) {
        super();
        this.userOne = user1;
        this.userTwo = user2;
    }

    public User getUserOne() {
        return userOne;
    }

    public void setUserOne(User user1) {
        this.userOne = user1;
    }

    public User getUserTwo() {
        return userTwo;
    }

    public void setUserTwo(User user2) {
        this.userTwo = user2;
    }

}
