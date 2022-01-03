/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.solent.com504.oodd.cart.service;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.solent.com504.oodd.bank.model.client.BankRestClient;
import org.solent.com504.oodd.bank.CreditCard;
import org.solent.com504.oodd.bank.BankTransaction;
import org.solent.com504.oodd.bank.TransactionRequestMessage;
import org.solent.com504.oodd.bank.TransactionReplyMessage;
import org.solent.com504.oodd.cart.model.service.BankingService;
import org.solent.com504.oodd.properties.dao.impl.PropertiesDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 *
 * @author Ben
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public abstract class BankService implements BankingService{

        private final CreditCard shopKeeperCard;

        private final List<BankTransaction> transactions = new ArrayList();

        @Autowired
        private BankRestClient client;

 
        public BankService(PropertiesDao properties){
            
            shopKeeperCard = new CreditCard();
            shopKeeperCard.setCardnumber(properties.getProperty("org.solent.oodd.pos.service.cardnumber"));
            shopKeeperCard.setCvv(properties.getProperty("org.solent.oodd.pos.service.cvv"));
            shopKeeperCard.setEndDate(properties.getProperty("org.solent.oodd.pos.service.expirydate"));
            shopKeeperCard.setIssueNumber(properties.getProperty("org.solent.oodd.pos.service.issuenumber"));
            shopKeeperCard.setName(properties.getProperty("org.solent.oodd.pos.service.name"));
        }
     
        
//        @Override
//        public BankTransaction sendTransaction(CreditCard fromCard, Double amount) {
//            try
//            {
//
//                TransactionRequestMessage request = new TransactionRequestMessage(fromCard, shopKeeperCard, amount);
//
//                TransactionReplyMessage response = client.transferMoney(request, apiUsername, apiPassword);
//
//
//
//                TransactionLogger.info("Sent Transaction: " + request.toString() + response.toString());
//
//                Transaction transaction = new Transaction(request, response);
//                transactions.add(transaction);
//                return transaction;
//            }
//            catch(Exception ex){
//                TransactionLogger.info("Transaction Failed: " + "From Card: " + fromCard.toString() + "To Card: " + shopKeeperCard.toString() +", amount:" + amount.toString());
//                throw ex;
//            }
//
//        }
//
//        @Override
//        public Transaction refundSimpleTransaction(Card toCard, Double amount){
//            try
//            {
//                logger.debug("Send Transaction to: " + toCard.getCardnumber() + " from: " + shopKeeperCard.getCardnumber() + " for: " + amount);
//
//                TransactionRequest request = new TransactionRequest(shopKeeperCard, toCard, amount);
//
//                TransactionResponse response = client.transferMoney(request);
//
//                logger.debug("Transaction Response Status: " + response.getStatus());
//
//                TransactionLogger.info("Sent Transaction: " + request.toString() + response.toString());
//
//                Transaction transaction = new Transaction(request, response);
//                transactions.add(transaction);
//                return transaction;
//            }
//            catch(Exception ex){
//                TransactionLogger.info("Transaction Failed: " + "From Card: " + shopKeeperCard.toString() + "To Card: " + toCard.toString() +", amount:" + amount.toString());
//                throw ex;
//            }
//        }
//
//        /**
//         * Refund Transaction takes in a full Transaction object and then fetches the 
//         * required information to refund the transaction
//         */
//        @Override
//        public Transaction refundTransaction(Transaction transaction) {
//            try
//            {
//                logger.debug("Refund Transaction from: " + transaction.getTransactionRequest().getFromCard().getCardnumber() + " to: " + transaction.getTransactionRequest().getToCard().getCardnumber() + " for: " + transaction.getTransactionRequest().getAmount());
//
//                Card fromCard = shopKeeperCard;       
//                Card toCard = transaction.getTransactionRequest().getFromCard();
//                Double amount = transaction.getTransactionRequest().getAmount();
//
//                TransactionRequest request = new TransactionRequest(fromCard, toCard, amount);
//
//                TransactionLogger.info("Transaction To Refund: " + transaction.getTransactionRequest().toString() + transaction.getTransactionResponse().toString());
//
//                TransactionResponse response = client.transferMoney(request);
//                logger.debug("Refund Response Status: " + response.getStatus());
//
//                TransactionLogger.info("Refund Transaction: " + request.toString() + response.toString());
//
//                Transaction refundTransaction = new Transaction(request, response);
//                if(refundTransaction.getTransactionResponse().getStatus().equals("SUCCESS")){
//                    refundTransaction.setIsRefund(true);
//                    transaction.setIsRefund(true);
//                    transactions.add(refundTransaction);
//                }
//                return refundTransaction;
//            }
//            catch(Exception ex){
//                TransactionLogger.info("Refund Failed - : " + transaction.getTransactionRequest().toString() + transaction.getTransactionResponse().toString());
//                throw ex;
//            }        
//        }
//
//        /**
//         * This uses the transactions list to get the 9 most recent transactions
//         */
//        @Override
//        public List<Transaction> getLatestSuccessfulTransactions(){
//            //Returns either all the transactions or the last 9 - whichever is smallest
//            logger.debug("Get Latest Transactions: " + transactions.size());
//            List<Transaction> successfulTransactions = new ArrayList<Transaction>();
//            for (Transaction transaction : transactions) {
//                if(transaction.getTransactionResponse().getStatus() != null && transaction.getTransactionResponse().getStatus().equals("SUCCESS") && transaction.getIsRefund() != true){
//                    successfulTransactions.add(transaction);
//                }
//            }
//            List<Transaction> latestTransactions = successfulTransactions.subList(successfulTransactions.size()- Math.min(successfulTransactions.size(), 9), successfulTransactions.size());
//            return latestTransactions;
//        }
//
//        /**
//         * Clears all transactions from the list of transactions
//         */
//        @Override
//        public Boolean clearTransactions(){
//            transactions.clear();
//            return true;
//        }
//
//    @Override
//    public BankTransaction refundTransaction(BankTransaction transaction) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public BankTransaction refundSimpleTransaction(CreditCard toCard, Double amount) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//    
}
