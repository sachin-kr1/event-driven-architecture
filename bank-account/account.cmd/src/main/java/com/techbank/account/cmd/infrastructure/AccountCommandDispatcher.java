package com.techbank.account.cmd.infrastructure;

import com.techbank.cqrs.core.commands.BaseCommand;
import com.techbank.cqrs.core.commands.CommandHandlerMethod;
import com.techbank.cqrs.core.infrastructure.CommandDispatcher;
import lombok.var;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class AccountCommandDispatcher implements CommandDispatcher {

    private final Map<Class< ? extends BaseCommand>, List<CommandHandlerMethod> >routes= new HashMap<>();
    @Override
    public <T extends BaseCommand> void registerHandler(Class<T> type, CommandHandlerMethod<T> handler) {
        List< CommandHandlerMethod> handlers = routes.computeIfAbsent(type,c->new LinkedList<>());
        handlers.add(handler);
    }

    @Override
    public void send(BaseCommand command) {
       List< CommandHandlerMethod > handlers= routes.get(command.getClass());

       if(handlers == null ||  handlers.size() == 0){
           throw  new RuntimeException("No command  ahndler was registered!");
       }
       if(handlers.size() > 1){
           throw new RuntimeException("cannot send command to the more than one handler!");
       }
       handlers.get(0).handle(command);
    }
}
