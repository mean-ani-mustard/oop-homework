// Bank.java

/*
 Creates a bunch of accounts and uses threads
 to post transactions to the accounts concurrently.
*/


import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class Bank {
	private static final int DEFAULT_NUM_ACCOUNTS = 20;
	private static final int INITIAL_BALANCE = 1000;
	private static final String DATA_DIRECTORY = "";

	private final Map<Integer, Account> accountMap = new ConcurrentHashMap<>();
	private final BlockingQueue<Transaction> transactionQueue = new LinkedBlockingQueue<>();
	private final Transaction POISON_PILL = new Transaction(-1, -1, 0);
	private CountDownLatch latch;

	/*
	 * Main method validates arguments and starts the bank simulation.
	 * Expects a transaction file name and number of worker threads as inputs.
	 */

	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Args: transaction-file [num-workers [limit]]");
			return;
		}

		String fileName = args[0];
		int numThreads = Integer.parseInt(args[1]);

		new Bank().run(fileName, numThreads);
	}

	/*
	 * Runs the bank simulation by initializing accounts, launching worker threads,
	 * reading transactions from the file, waiting for processing to complete,
	 * and finally displaying the account balances.
	 */

	public void run(String fileName, int numThreads) {
		initializeAccounts(DEFAULT_NUM_ACCOUNTS);
		latch = new CountDownLatch(numThreads);
		launchWorkers(numThreads);
		readTransactionsFromFile(fileName, numThreads);
		waitForCompletion();
		displayFinalBalances();
	}

	/*
	 * Initializes the specified number of accounts with a default balance.
	 * Each account is stored in a thread-safe map with a unique ID.
	 */

	private void initializeAccounts(int numAccounts) {
		for (int i = 0; i < numAccounts; i++) {
			accountMap.put(i, new Account(this,i, INITIAL_BALANCE));
		}
	}
	/*
	 * Creates and starts the specified number of worker threads.
	 * Each worker continuously processes transactions from the queue.
	 */

	private void launchWorkers(int numThreads) {
		for (int i = 0; i < numThreads; i++) {
			new Worker().start();
		}
	}

	/*
	 * Reads transactions from a file line-by-line.
	 * Each valid transaction is added to the transaction queue for workers to process.
	 * Inserts a poison pill for each worker to signal completion.
	 */

	private void readTransactionsFromFile(String fileName, int numThreads) {
		try (BufferedReader reader = new BufferedReader(new FileReader(DATA_DIRECTORY + fileName))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.trim().split("\\s+");
				if (parts.length != 3) continue;

				int from = Integer.parseInt(parts[0]);
				int to = Integer.parseInt(parts[1]);
				int amount = Integer.parseInt(parts[2]);

				transactionQueue.put(new Transaction(from, to, amount));
			}

			for (int i = 0; i < numThreads; i++) {
				transactionQueue.put(POISON_PILL);
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	/*
	 * Waits for all worker threads to finish processing.
	 * Uses a CountDownLatch to block until all workers call countDown().
	 */

	private void waitForCompletion() {
		try {
			latch.await();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	/*
	 * Displays the final balances of all accounts.
	 * Sorts accounts by their ID before printing to ensure ordered output.
	 */

	private void displayFinalBalances() {
		System.out.println("\nFinal Account Balances:");
		accountMap.values().stream()
				.sorted(Comparator.comparingInt(Account::getId))
				.forEach(System.out::println);
	}

	/*
	 * Inner Worker thread class responsible for processing transactions.
	 * Repeatedly takes transactions from the queue until the poison pill is encountered.
	 * Performs withdrawals and deposits atomically on accounts to maintain consistency.
	 * Counts down the latch once finished to signal completion.
	 */

	private class Worker extends Thread {
		@Override
		public void run() {
			int processed = 0;

			try {
				while (true) {
					Transaction tx = transactionQueue.take();
					if (tx.equals(POISON_PILL)) break;

					Account from = accountMap.get(tx.getFrom());
					Account to = accountMap.get(tx.getTo());

					if (from != null && to != null && tx.getAmount() > 0) {
						from.withdraw(tx.getAmount());
						to.deposit(tx.getAmount());
						processed++;
					}
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			} finally {
				latch.countDown();
			}
		}
	}

}
