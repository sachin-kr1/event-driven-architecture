package com.techbank.account.cmd.domain;

import com.techbank.account.cmd.api.commands.OpenAccountCommand;
import com.techbank.account.common.events.AccountOpenedEvent;
import com.techbank.account.common.events.FundsDepositedEvent;
import com.techbank.account.common.events.FundsWithdrawnEvent;
import com.techbank.cqrs.core.domain.AggregateRoot;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
public class AccountAggregate extends AggregateRoot {
    private Boolean active;
    private double balance;

    public AccountAggregate(OpenAccountCommand command) {
        AccountOpenedEvent event= new AccountOpenedEvent();
        event.setId(command.getId());
        event.setAccountHolder(command.getAccountHolder());
        event.setAccountType(command.getAccountType());
        event.setDate( new Date());
        event.setOpeningBalance( command.getOpeningBalance());

        raiseEvent(event);
    }

    public void apply(AccountOpenedEvent event){
        this.id= event.getId();
        this.active= true;
        this.balance=event.getOpeningBalance();
    }

    public void depositFunds(double amount){
        if(!this.active){
            throw new IllegalStateException( " Funds cannot be deposited into a closed account!");
        }

        if(amount <=0 ){
            throw  new IllegalStateException(" The deposit amount must be greater than zero");
        }
        FundsDepositedEvent event= new FundsDepositedEvent();
        event.setId(this.id);
        event.setAmount(amount);

        raiseEvent(event);
    }

    public void apply(FundsDepositedEvent event){
        this.id= event.getId();
        this.balance += event.getAmount();
    }

    public void withdrawFunds( double amount){
        if(!this.active){
            throw new IllegalStateException( " Funds cannot be withdrawn from a closed account!");
        }
        FundsWithdrawnEvent event= new FundsWithdrawnEvent();
        event.setId(this.id);
        event.setAmount(amount);
        raiseEvent(event);

    }

    public void apply(FundsWithdrawnEvent event){
        this.id= event.getId();
        this.balance -= event.getAmount();
    }

    public  void closeAccount(){

    }

}
