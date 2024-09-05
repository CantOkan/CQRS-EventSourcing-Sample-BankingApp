package com.bank.cqrs.core.events;

import com.bank.cqrs.core.commands.BaseCommand;
import com.bank.cqrs.core.messages.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BaseEvent extends Message {
    private int version;
    private String eventId;
}
