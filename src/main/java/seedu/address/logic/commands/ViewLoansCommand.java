package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_NO_PERSONS;

import seedu.address.model.Model;

/**
 * Lists all active loans in the address book to the user.
 */
public class ViewLoansCommand extends ViewLoanRelatedCommand {

    public static final String MESSAGE_SUCCESS = "Listed all loans";

    /**
     * @param isShowAllLoans Whether to show all loans or only active loans.
     */
    public ViewLoansCommand(boolean isShowAllLoans) {
        super(isShowAllLoans);
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(PREDICATE_SHOW_NO_PERSONS);
        model.updateFilteredLoanList(unused -> true, isShowAllLoans);
        model.setIsLoansTab(true);
        model.setIsShowLoaneeInfo(true);
        return new CommandResult(MESSAGE_SUCCESS, false, false, true);
    }
}
