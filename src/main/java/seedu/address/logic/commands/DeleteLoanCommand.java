package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Loan;

/**
 * Deletes a loan from the address book.
 */
public class DeleteLoanCommand extends Command {
    public static final String COMMAND_WORD = "deleteloan";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Delete the loan number of current person in view. \n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1 ";
    public static final String MESSAGE_SUCCESS = "Loan deleted.\n"
            + "Loan: %1$s";
    public static final String MESSAGE_FAILURE_LOAN = "No loan has been found "
            + "for loan number: %1$d";
    private final Index loanIndex;

    /**
     * Creates a DeleteLoanCommand to delete the specified loan.
     * @param loanIndex Index of the loan in the last shown loan list.
     */
    public DeleteLoanCommand(Index loanIndex) {
        requireAllNonNull(loanIndex);
        this.loanIndex = loanIndex;
    }
    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Loan> lastShownList = model.getSortedLoanList();
        assert lastShownList != null;
        if (loanIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(String.format(MESSAGE_FAILURE_LOAN, loanIndex.getOneBased()));
        }
        // delete specified loan number
        Loan loanToRemove = lastShownList.get(loanIndex.getZeroBased());
        model.deleteLoan(loanToRemove);
        return new CommandResult(generateSuccessMessage(loanToRemove), false, false , true);
    }

    /**
     * Generates a command execution success message after loan is deleted.
     */
    private String generateSuccessMessage(Loan removedLoan) {
        return String.format(MESSAGE_SUCCESS, removedLoan);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteLoanCommand)) {
            return false;
        }

        DeleteLoanCommand e = (DeleteLoanCommand) other;
        return loanIndex.equals(e.loanIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("loanIndex", loanIndex)
                .toString();
    }
}
