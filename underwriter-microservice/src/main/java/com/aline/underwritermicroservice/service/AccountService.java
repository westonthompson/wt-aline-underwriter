package com.aline.underwritermicroservice.service;

import com.aline.core.exception.BadRequestException;
import com.aline.core.model.Application;
import com.aline.core.model.ApplicationType;
import com.aline.core.model.Member;
import com.aline.core.model.account.Account;
import com.aline.core.model.account.AccountStatus;
import com.aline.core.model.account.CheckingAccount;
import com.aline.core.model.account.LoanAccount;
import com.aline.core.model.account.SavingsAccount;
import com.aline.core.model.loan.Loan;
import com.aline.core.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Account Service
 * <p>
 *     Used to create an account in the context of
 *     approving an application.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository repository;
    private final UnderwriterService underwriterService;

    /**
     * Create a single or multiple accounts based on the applicationType
     * @param application See {@link Application} to see what kinds of accounts can be created.
     * @param primaryAccountHolder The primary member.
     * @param members The members attached to the account including the primary member.
     * @return A set of accounts that were created.
     */
    @Transactional
    public Set<Account> createAccount(Application application, Member primaryAccountHolder, Set<Member> members) {
        Set<Account> accounts = new HashSet<>();
        switch (application.getApplicationType()) {
            case CHECKING:
                accounts.add(createCheckingAccount(primaryAccountHolder, members));
                break;
            case SAVINGS:
                accounts.add(createSavingsAccount(primaryAccountHolder, members));
                break;
            case CHECKING_AND_SAVINGS:
                accounts.add(createCheckingAccount(primaryAccountHolder, members));
                accounts.add(createSavingsAccount(primaryAccountHolder, members));
                break;
            case LOAN:
                accounts.add(createLoan(application, primaryAccountHolder, members));
                break;
            default:
                break;
        }

        return accounts;
    }

    private Account createCheckingAccount(Member primaryAccountHolder, Set<Member> members) {
        CheckingAccount account = CheckingAccount.builder()
                .primaryAccountHolder(primaryAccountHolder)
                .balance(0)
                .availableBalance(0)
                .status(AccountStatus.ACTIVE)
                .members(members)
                .build();
        return Optional.of(repository.save(account)).orElseThrow(() -> new BadRequestException("Account was not saved."));
    }

    private Account createSavingsAccount(Member primaryAccountHolder, Set<Member> members) {
        SavingsAccount account = SavingsAccount.builder()
                .primaryAccountHolder(primaryAccountHolder)
                .balance(0)
                .apy(0.006f)
                .members(members)
                .status(AccountStatus.ACTIVE)
                .build();
        return Optional.of(repository.save(account)).orElseThrow(() -> new BadRequestException("Account was not saved."));
    }


    private Account createLoan(Application application, Member primaryPayer, Set<Member> payers) {
        if (application.getApplicationType() != ApplicationType.LOAN)
            throw new BadRequestException("Attempting to create a loan with an application type that does not include a loan.");
        Loan loan = underwriterService.createLoan(application);
        LoanAccount account = LoanAccount.builder()
                .primaryAccountHolder(primaryPayer)
                .balance(0)
                .loan(loan)
                .members(payers)
                .payments(new ArrayList<>())
                .paymentHistory(new ArrayList<>())
                .status(AccountStatus.ACTIVE)
                .build();

        return Optional.of(repository.save(account)).orElseThrow(() -> new BadRequestException("Account was not saved."));
    }

}
