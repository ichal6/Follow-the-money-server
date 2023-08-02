package com.mlkb.ftm.service;

import com.mlkb.ftm.entity.*;
import com.mlkb.ftm.exception.InputIncorrectException;
import com.mlkb.ftm.exception.InputValidationMessage;
import com.mlkb.ftm.exception.ResourceNotFoundException;
import com.mlkb.ftm.modelDTO.PaymentDTO;
import com.mlkb.ftm.modelDTO.TransactionDTO;
import com.mlkb.ftm.modelDTO.TransferDTO;
import com.mlkb.ftm.repository.*;
import com.mlkb.ftm.validation.InputValidator;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.time.Clock;

import static java.lang.String.format;

@Service
public class PaymentService {
    private final UserRepository userRepository;
    private final InputValidator inputValidator;
    private final TransactionRepository transactionRepository;
    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final PayeeRepository payeeRepository;
    private final Clock clock;

    public PaymentService(UserRepository userRepository, InputValidator inputValidator,
                          TransactionRepository transactionRepository, AccountRepository accountRepository,
                          CategoryRepository categoryRepository, PayeeRepository payeeRepository,
                          TransferRepository transferRepository, Clock clock) {
        this.userRepository = userRepository;
        this.inputValidator = inputValidator;
        this.transactionRepository = transactionRepository;
        this.transferRepository = transferRepository;
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
        this.payeeRepository = payeeRepository;
        this.clock = clock;
    }

    public List<PaymentDTO> getPaymentsWithParameters(String email, String accountId, String period) {
        try {
            Long accountIdLong = Long.parseLong(accountId);
            int periodInDays = Integer.parseInt(period);
            Optional<User> userOptional = userRepository.findByEmail(email);
            Optional<Account> accountOptional = accountRepository.findById(accountIdLong);
            if (userOptional.isPresent() && accountOptional.isPresent()) {
                User user = userOptional.get();
                Account account = accountOptional.get();
                if (user.getAccounts().contains(account)) {
                    return extractPaymentsForParameters(account, periodInDays, false);
                } else {
                    throw new ResourceNotFoundException("User with given email doesn't have an account with given id");
                }
            } else {
                throw new ResourceNotFoundException("Couldn't find user or account for given parameters");
            }
        } catch (NumberFormatException e) {
            throw new ResourceNotFoundException("Given parameters for transactions are incorrect");
        }
    }

    public List<PaymentDTO> getPaymentsForPeriod(String email, String period) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            throw new ResourceNotFoundException(format("Couldn't find user with email %s", email));
        }
        try {
            int periodInDays = Integer.parseInt(period);
            User user = userOptional.get();
            var accounts = user.getAccounts();
            List<PaymentDTO> payments = new ArrayList<>();
            for(Account account: accounts) {
                payments.addAll(extractPaymentsForParameters(account, periodInDays, true));
            }
            return payments.stream()
                    .filter(paymentDTO -> !paymentDTO.getIsInternal() ||  paymentDTO.getValue() >= 0.0)
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            throw new ResourceNotFoundException("Given parameters for transactions are incorrect");
        }
    }

    public List<PaymentDTO> getPaymentsWithAccount(String email, String accountId) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            throw new ResourceNotFoundException(format("Couldn't find user with email %s", email));
        }
        try {
            Long accountIdLong = Long.parseLong(accountId);
            Optional<Account> accountOptional = accountRepository.findById(accountIdLong);
            if (accountOptional.isPresent()) {
                User user = userOptional.get();
                Account account = accountOptional.get();
                if (user.getAccounts().contains(account)) {
                    return extractPaymentsForAccount(account);
                } else {
                    throw new ResourceNotFoundException("User with given email doesn't have an account with given id");
                }
            } else {
                throw new ResourceNotFoundException(format("Couldn't find account for account id %d", accountIdLong));
            }
        } catch (NumberFormatException e) {
            throw new ResourceNotFoundException("Given parameters for transactions are incorrect");
        }
    }

    public List<PaymentDTO> getPayments(String email) {
        Optional<User> possibleUser = userRepository.findByEmail(email);
        if (possibleUser.isPresent()) {
            User user = possibleUser.get();
            var accounts = user.getAccounts();
            List<PaymentDTO> payments = new ArrayList<>();
            for(Account account: accounts) {
                payments.addAll(extractPayments(account));
            }
            return payments.stream()
                    .filter(paymentDTO -> !paymentDTO.getIsInternal() ||  paymentDTO.getValue() >= 0.0)
                    .collect(Collectors.toList());
        } else {
            throw new ResourceNotFoundException(format("Couldn't find user with email %s", email));
        }
    }

    private List<PaymentDTO> extractPayments(Account account){

        List<PaymentDTO> payments = account.getTransactions().stream()
                .map(transaction -> makePaymentDTOFromTransaction(transaction, account.getName()))
                .collect(Collectors.toList());

        payments.addAll(account.getTransfersFrom().stream()
                .map(transfer -> makePaymentDTOFromTransferFrom(transfer, account.getName()))
                .collect(Collectors.toList()));

        payments.addAll(account.getTransfersTo().stream()
                .map(transfer -> makePaymentDTOFromTransferTo(transfer, account.getName()))
                .collect(Collectors.toList()));

        payments.sort((p1, p2) -> Long.compare(p2.getDate().getTime(), p1.getDate().getTime()));

        return payments;
    }

    public boolean isValidNewTransaction(TransactionDTO transactionDTO) throws InputIncorrectException {
        return transactionDTO != null
                && inputValidator.checkName(transactionDTO.getTitle())
                && inputValidator.checkIfPaymentTypeInEnum(transactionDTO.getType())
                && inputValidator.checkBalance(transactionDTO.getValue())
                && inputValidator.checkId(transactionDTO.getAccountId())
                && inputValidator.checkId(transactionDTO.getCategoryId())
                && inputValidator.checkId(transactionDTO.getPayeeId())
                && inputValidator.checkDate(transactionDTO.getDate());
    }

    public void isValidUpdateTransaction(TransactionDTO transactionDTO) throws InputIncorrectException {
        if(transactionDTO == null) {
            throw new InputIncorrectException(InputValidationMessage.NULL);
        }
        inputValidator.checkId(transactionDTO.getId());
        inputValidator.checkName(transactionDTO.getTitle());
        inputValidator.checkBalance(transactionDTO.getValue());
        inputValidator.checkIfPaymentTypeInEnum(transactionDTO.getType());
        var paymentType = PaymentType.valueOf(transactionDTO.getType().toUpperCase());
        inputValidator.checkIfPaymentTypeCorrectWithValue(paymentType, transactionDTO.getValue());
        inputValidator.checkDate(transactionDTO.getDate());
    }

    public boolean isValidNewTransfer(TransferDTO transferDTO) throws InputIncorrectException {
        return transferDTO != null
                && inputValidator.checkName(transferDTO.getTitle())
                && inputValidator.checkBalancePositive(transferDTO.getValue())
                && inputValidator.checkId(transferDTO.getAccountIdFrom())
                && inputValidator.checkId(transferDTO.getAccountIdTo())
                && inputValidator.checkDate(transferDTO.getDate());
    }

    public void createNewTransaction(TransactionDTO transactionDTO) {
        Optional<Category> categoryOptional = categoryRepository.findById(transactionDTO.getCategoryId());
        Optional<Account> accountOptional = accountRepository.findById(transactionDTO.getAccountId());
        Optional<Payee> payeeOptional = payeeRepository.findById(transactionDTO.getPayeeId());
        if (categoryOptional.isPresent() && accountOptional.isPresent() && payeeOptional.isPresent()) {
            Transaction transaction = new Transaction();
            transaction.setType(PaymentType.valueOf(transactionDTO.getType()));
            transaction.setValue(transactionDTO.getValue());
            transaction.setDate(transactionDTO.getDate());
            transaction.setTitle(transactionDTO.getTitle());
            transaction.setPayee(payeeOptional.get());
            transaction.setCategory(categoryOptional.get());

            transactionRepository.save(transaction);
            addTransactionToAccountInDB(accountOptional.get(), transaction);
            modifyTotalBalanceForAccount(accountOptional.get(), transactionDTO.getValue());
        } else {
            throw new ResourceNotFoundException("Couldn't create new transaction. Category, account or payee with given id don't exist");
        }
    }

    public void createNewTransfer(TransferDTO transferDTO) {
        Optional<Account> accountOptionalFrom = accountRepository.findById(transferDTO.getAccountIdFrom());
        Optional<Account> accountOptionalTo = accountRepository.findById(transferDTO.getAccountIdTo());
        if (accountOptionalFrom.isPresent() && accountOptionalTo.isPresent()) {
            Transfer transfer = new Transfer();
            transfer.setValue(transferDTO.getValue());
            transfer.setTitle(transferDTO.getTitle());
            transfer.setDate(transferDTO.getDate());
            transfer.setAccountFrom(accountOptionalFrom.get());
            transfer.setAccountTo(accountOptionalTo.get());

            transferRepository.save(transfer);
            addTransferToAccountsInDB(accountOptionalFrom.get(), accountOptionalTo.get(), transfer);
            modifyTotalBalanceForBothAccountsAfterTransfer(accountOptionalFrom.get(), accountOptionalTo.get(), transfer.getValue());
        } else {
            throw new ResourceNotFoundException("Couldn't create new transfer. AccountFrom or/and AccountTo with given ids don't exist.");
        }
    }

    @Transactional
    public void updateTransaction(TransactionDTO updateTransactionDTO, String email) {
        Account updateAccount = getAccountForTransactionDTO(updateTransactionDTO, email);
        Payee payee = getPayeeForTransactionDTO(updateTransactionDTO, email);
        Category category = getCategoryForTransactionDTO(updateTransactionDTO, email);

        if (!this.transactionRepository.existsByTransactionIdAndUserEmail(updateTransactionDTO.getId(), email)) {
            throw new ResourceNotFoundException(
                    String.format("Transaction for id = %d does not exist", updateTransactionDTO.getId()));
        }
        Transaction transaction = this.transactionRepository.findById(updateTransactionDTO.getId()).orElseThrow();
        Optional<Account> possibleActualAccount = this.accountRepository.findByTransactionsContains(transaction);
        possibleActualAccount.ifPresentOrElse(
                actualAccount -> modifyAccountForEditTransaction(actualAccount, updateAccount, transaction, updateTransactionDTO),
                ResourceNotFoundException::new);

        transaction.setTitle(updateTransactionDTO.getTitle());
        transaction.setValue(updateTransactionDTO.getValue());
        transaction.setType(PaymentType.valueOf(updateTransactionDTO.getType().toUpperCase()));
        transaction.setDate(updateTransactionDTO.getDate());
        transaction.setPayee(payee);
        transaction.setCategory(category);

        this.transactionRepository.save(transaction);
    }

    private Category getCategoryForTransactionDTO(TransactionDTO updateTransactionDTO, String email) {
        return this.categoryRepository.findByCategoryIdAndUserEmail(updateTransactionDTO.getCategoryId(), email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Couldn't update transaction id = %d, because category for id = %d doesn't exist",
                                updateTransactionDTO.getId(),
                                updateTransactionDTO.getCategoryId()))
                );
    }

    private Payee getPayeeForTransactionDTO(TransactionDTO updateTransactionDTO, String email) {
        return this.payeeRepository.findByPayeeIdAndUserEmail(updateTransactionDTO.getPayeeId(), email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Couldn't update transaction id = %d, because payee for id = %d doesn't exist",
                                updateTransactionDTO.getId(),
                                updateTransactionDTO.getPayeeId()))
                );
    }

    private Account getAccountForTransactionDTO(TransactionDTO updateTransactionDTO, String email) {
        return this.accountRepository.findByAccountIdAndUserEmail(updateTransactionDTO.getAccountId(), email)
                .orElseThrow(() ->  new ResourceNotFoundException(
                        String.format("Couldn't update transaction id = %d, because account for id = %d doesn't exist",
                                updateTransactionDTO.getId(),
                                updateTransactionDTO.getAccountId()))
                );
    }

    private void modifyAccountForEditTransaction(Account oldAccount, Account newAccount,
                                                 Transaction transaction, TransactionDTO updateTransactionDTO) {
        if(!newAccount.getId().equals(oldAccount.getId())) {
            var transactionsFromOldAccount = oldAccount.getTransactions().stream()
                    .filter(t -> !t.getId().equals(transaction.getId()))
                    .collect(Collectors.toSet());
            oldAccount.setTransactions(transactionsFromOldAccount);
            modifyTotalBalanceForAccount(oldAccount, -1*transaction.getValue());

            var isAdded = newAccount.getTransactions().add(transaction);
            if (!isAdded) {
                throw new ResourceNotFoundException(
                        String.format("Transaction id = %d cannot add to account id = %d",
                                transaction.getId(), newAccount.getId()));
            }
            modifyTotalBalanceForAccount(newAccount, updateTransactionDTO.getValue());
        } else {
            updateAccountValueAfterEditTransaction(newAccount, transaction.getValue(), updateTransactionDTO.getValue());
        }
    }

    private void updateAccountValueAfterEditTransaction(Account account, double oldValue, double newValue) {
        account.setCurrentBalance(account.getCurrentBalance() - oldValue + newValue);
        this.accountRepository.save(account);
    }

    private void addTransactionToAccountInDB(Account account, Transaction transaction) {
        account.getTransactions().add(transaction);
        accountRepository.save(account);
    }

    private void modifyTotalBalanceForAccount(Account account, Double value) {
        account.setCurrentBalance(account.getCurrentBalance() + value);
        accountRepository.save(account);
    }

    private void addTransferToAccountsInDB(Account accountFrom, Account accountTo, Transfer transfer) {
        accountFrom.getTransfersFrom().add(transfer);
        accountTo.getTransfersTo().add(transfer);
        accountRepository.save(accountFrom);
        accountRepository.save(accountTo);
    }

    private void modifyTotalBalanceForBothAccountsAfterTransfer(Account accountFrom, Account accountTo, Double value) {
        accountFrom.setCurrentBalance(accountFrom.getCurrentBalance() - value);
        accountTo.setCurrentBalance(accountTo.getCurrentBalance() + value);
        accountRepository.save(accountFrom);
        accountRepository.save(accountTo);
    }

    private List<PaymentDTO> extractPaymentsForParameters(Account account, int periodInDays, boolean isForAllAccount) {
        List<PaymentDTO> payments = new ArrayList<>();
        Instant now = clock.instant();
        Instant instantFrom = now.minus(periodInDays, ChronoUnit.DAYS);
        Date dateFrom = Date.from(instantFrom);

        List<PaymentDTO> transactions = account.getTransactions().stream()
                .filter(transaction -> transaction.getDate().getTime() > dateFrom.getTime())
                .map(transaction -> makePaymentDTOFromTransaction(transaction, account.getName()))
                .collect(Collectors.toList());

        List<PaymentDTO> transfersFrom = account.getTransfersFrom().stream()
                .filter(transfer -> transfer.getDate().getTime() > dateFrom.getTime())
                .map(transfer -> makePaymentDTOFromTransferFrom(transfer, account.getName()))
                .collect(Collectors.toList());

        List<PaymentDTO> transfersTo = account.getTransfersTo().stream()
                .filter(transfer -> transfer.getDate().getTime() > dateFrom.getTime())
                .map(transfer -> makePaymentDTOFromTransferTo(transfer, account.getName()))
                .collect(Collectors.toList());

        payments.addAll(transactions);
        payments.addAll(transfersFrom);
        payments.addAll(transfersTo);

        payments.sort((p1, p2) -> Long.compare(p2.getDate().getTime(), p1.getDate().getTime()));
        if(!isForAllAccount)
            calculateBalanceAfterEachPayment(payments, account.getCurrentBalance());

        return payments;
    }

    private List<PaymentDTO> extractPaymentsForAccount(Account account) {
        List<PaymentDTO> payments = new ArrayList<>();

        List<PaymentDTO> transactions = account.getTransactions().stream()
                .map(transaction -> makePaymentDTOFromTransaction(transaction, account.getName()))
                .collect(Collectors.toList());

        List<PaymentDTO> transfersFrom = account.getTransfersFrom().stream()
                .map(transfer -> makePaymentDTOFromTransferFrom(transfer, account.getName()))
                .collect(Collectors.toList());

        List<PaymentDTO> transfersTo = account.getTransfersTo().stream()
                .map(transfer -> makePaymentDTOFromTransferTo(transfer, account.getName()))
                .collect(Collectors.toList());

        payments.addAll(transactions);
        payments.addAll(transfersFrom);
        payments.addAll(transfersTo);

        payments.sort((p1, p2) -> Long.compare(p2.getDate().getTime(), p1.getDate().getTime()));

        calculateBalanceAfterEachPayment(payments, account.getCurrentBalance());

        return payments;
    }

    private PaymentDTO makePaymentDTOFromTransaction(Transaction transaction, String accountName) {
        PaymentDTO newPaymentDTO = new PaymentDTO();
        newPaymentDTO.setIsInternal(false);
        newPaymentDTO.setId(transaction.getId());
        newPaymentDTO.setValue(transaction.getValue());
        newPaymentDTO.setDate(transaction.getDate());
        newPaymentDTO.setTitle(transaction.getTitle());
        newPaymentDTO.setCategoryName(transaction.getCategory().getName());
        if (transaction.getValue() > 0) {
            newPaymentDTO.setFrom(transaction.getPayee().getName());
            newPaymentDTO.setTo(accountName);
        } else {
            newPaymentDTO.setFrom(accountName);
            newPaymentDTO.setTo(transaction.getPayee().getName());
        }
        return newPaymentDTO;
    }

    private PaymentDTO makePaymentDTOFromTransferFrom(Transfer transfer, String accountName) {
        PaymentDTO newPaymentDTO = new PaymentDTO();
        newPaymentDTO.setIsInternal(true);
        newPaymentDTO.setId(transfer.getId());
        newPaymentDTO.setValue(0 - transfer.getValue());
        newPaymentDTO.setDate(transfer.getDate());
        newPaymentDTO.setTitle(transfer.getTitle());
        newPaymentDTO.setFrom(accountName);
        newPaymentDTO.setTo(transfer.getAccountTo().getName());

        return newPaymentDTO;
    }

    private PaymentDTO makePaymentDTOFromTransferTo(Transfer transfer, String accountName) {
        PaymentDTO newPaymentDTO = new PaymentDTO();
        newPaymentDTO.setIsInternal(true);
        newPaymentDTO.setId(transfer.getId());
        newPaymentDTO.setValue(transfer.getValue());
        newPaymentDTO.setDate(transfer.getDate());
        newPaymentDTO.setTitle(transfer.getTitle());
        newPaymentDTO.setFrom(transfer.getAccountFrom().getName());
        newPaymentDTO.setTo(accountName);

        return newPaymentDTO;
    }

    private void calculateBalanceAfterEachPayment(List<PaymentDTO> payments, Double currentBalance) {
        for (PaymentDTO payment : payments) {
            payment.setBalanceAfter(BigDecimal.valueOf(currentBalance).setScale(2, RoundingMode.HALF_UP));
            currentBalance -= payment.getValue();
        }
    }

    public boolean removeTransaction(Long id, String email) {
        Optional<User> possibleUser = userRepository.findByEmail(email);

        Optional<Transaction> possibleTransaction = findTransaction(possibleUser, id);
        Optional<Account> possibleAccountToUpdate = findAccount(possibleUser, id);

        if(possibleAccountToUpdate.isEmpty()){
            throw new ResourceNotFoundException("Couldn't delete this transaction. Account doesn't exist update.");
        }
        if(possibleTransaction.isEmpty()){
            throw new ResourceNotFoundException("Couldn't delete this transaction. Transaction doesn't exist");
        }

        modifyTotalBalanceForAccount(possibleAccountToUpdate.get(), (possibleTransaction.get().getValue()*-1));

        System.out.print("Delete - ");
        System.out.println(id);
        transactionRepository.deleteById(id);

        return true;
    }

    private Optional<Account> findAccount(Optional<User> possibleUser, Long transactionId){
        if(possibleUser.isEmpty()){
            throw new ResourceNotFoundException("User for this email does not exist");
        }
        Set<Account> allAccountsForUser = possibleUser.get().getAccounts();
        for(Account account: allAccountsForUser){
            for(Transaction transaction: account.getTransactions()){
                if(transaction.getId().equals(transactionId)){
                    return Optional.of(account);
                }
            }
        }

        return Optional.empty();
    }

    private Optional<Transaction> findTransaction(Optional<User> possibleUser, Long transactionId){
        if(possibleUser.isEmpty()){
            throw new ResourceNotFoundException("User for this email does not exist");
        }
        return possibleUser.get().getAccounts().stream()
                .flatMap(account -> account.getTransactions().stream())
                .filter(transaction -> transaction.getId().equals(transactionId))
                .findFirst();
    }

    public boolean removeTransfer(Long id, String email) {
        Optional<User> possibleUser = userRepository.findByEmail(email);
        if(possibleUser.isEmpty()){
            throw new ResourceNotFoundException("Couldn't delete this transfer. User for this email does not exist");
        }
        Optional<Transfer> possibleTransfer = possibleUser.get().getAccounts().stream()
                .flatMap(account -> account.getTransfersTo().stream())
                .filter(transfer -> transfer.getId().equals(id))
                .findFirst();
        if(possibleTransfer.isEmpty()){
            possibleTransfer = possibleUser.get().getAccounts().stream()
                    .flatMap(account -> account.getTransfersFrom().stream())
                    .filter(transfer -> transfer.getId().equals(id))
                    .findFirst();
        }
        if(possibleTransfer.isEmpty()){
            throw new ResourceNotFoundException("Couldn't delete this transfer. Transfer doesn't exist");
        }
        System.out.print("Delete - ");
        System.out.println(id);
        transferRepository.deleteById(id);

        return true;
    }

}

