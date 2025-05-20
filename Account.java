// Account.java

import java.math.BigDecimal;

/*
 Simple, thread-safe Account class encapsulates
 a balance and a transaction count.
*/
public class Account {
	private int id;
	private int balance;
	private int transactions;
	
	// It may work out to be handy for the account to
	// have a pointer to its Bank.
	// (a suggestion, not a requirement)
	private Bank bank;  
	
	public Account(Bank bank, int id, int balance) {
		this.bank = bank;
		this.id = id;
		this.balance = balance;
		transactions = 0;
	}


	public synchronized void deposit(int amount) {
		balance = balance+amount;
		transactions++;
	}
	public synchronized void withdraw(int amount) {
		balance = balance-amount;
		transactions++;
	}

	public synchronized int getBalance() {
		return balance;
	}

	public synchronized int getNumTransactions() {
		return transactions;
	}

	public int getId() {
		return id;
	}

	@Override
	public synchronized String toString() {
		return "Account " + id + ": balance=" + balance + ", transactions=" + transactions;
	}
	
}
