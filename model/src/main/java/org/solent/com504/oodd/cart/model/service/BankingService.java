/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.solent.com504.oodd.cart.model.service;


import java.util.List;
import org.solent.com504.oodd.bank.CreditCard;
import org.solent.com504.oodd.bank.BankTransaction;

/**
 *
 * @author Ben
 */
public interface BankingService {
    
    public BankTransaction sendTransaction(CreditCard fromCard, Double amount);

    public BankTransaction refundTransaction(BankTransaction transaction);
    
    public BankTransaction refundSimpleTransaction(CreditCard toCard, Double amount);
            
    List<BankTransaction> getLatestSuccessfulTransactions();
    
    public Boolean clearTransactions();

}
