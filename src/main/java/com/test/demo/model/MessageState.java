package com.test.demo.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@ApiModel(value="消息状态")
public class MessageState {
    @Id
    @GeneratedValue
    Long Id;

    @Column
    Long userId;
    @Column
    int state;
    @ManyToOne
    @JoinColumn(name="message_id")
    Message message;
    public MessageState(){

    }

    public MessageState(Long userId, int state, Message message) {
        this.userId = userId;
        this.state = state;
        this.message = message;
    }
}
