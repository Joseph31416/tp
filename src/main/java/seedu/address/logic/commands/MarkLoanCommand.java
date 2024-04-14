package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Loan;


/**
 * Marks a loan as paid.
 */
public class MarkLoanCommand extends Command {
    public static final String COMMAND_WORD = "markloan";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the current person in view's loan(of loan number) as paid.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1\n";
    public static final String MESSAGE_SUCCESS = "Loan marked.\n"
            + "Loan: %1$s";
    public static final String MESSAGE_FAILURE_LOAN = "No loan has been found "
            + "for loan number: %1$d";
    private final Index loanIndex;

    /**
     * Creates a MarkLoanCommand to mark the specified loan.
     * @param loanIndex Index of the loan in the last shown loan list.
     */
    public MarkLoanCommand(Index loanIndex) {
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
        // mark specified loan number
        Loan loanToMark = lastShownList.get(loanIndex.getZeroBased());
        model.markLoan(loanToMark);
        return new CommandResult(generateSuccessMessage(loanToMark), false, false, true);
    }

    /**
     * Generates a command execution success message after loan is marked.
     */
    private String generateSuccessMessage(Loan markedLoan) {
        return String.format(MESSAGE_SUCCESS, markedLoan);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        // instanceof handles nulls
        if (!(other instanceof MarkLoanCommand)) {
            return false;
        }
        MarkLoanCommand e = (MarkLoanCommand) other;
        return loanIndex.equals(e.loanIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("loanIndex", loanIndex)
                .toString();
    }
}
