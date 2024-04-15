package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RETURN_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_VALUE;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.LinkLoanCommand.LinkLoanDescriptor;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Loan;
import seedu.address.model.person.Person;

/**
 * Edits a loan of a person in the address book.
 */
public class EditLoanCommand extends Command {

    public static final String COMMAND_WORD = "editloan";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the loan specified "
            + "by its loan index number, possibly changing "
            + "its value, start date and/or return date. "
            + "At least one field must be changed.\n"
            + "Parameters: "
            + "LOAN_INDEX "
            + "[" + PREFIX_VALUE + "VALUE] "
            + "[" + PREFIX_START_DATE + "START_DATE] "
            + "[" + PREFIX_RETURN_DATE + "RETURN_DATE]\n"
            + "INDEX must be a positive integer.\n"
            + "Example: " + COMMAND_WORD + " "
            + "5 "
            + PREFIX_VALUE + "500.00 "
            + PREFIX_START_DATE + "2024-02-15 "
            + PREFIX_RETURN_DATE + "2024-04-21\n"
            + "This edits the loan at loan index 5 "
            + "to reflect a value of $500, "
            + "a start date of 15 Feb 2024 "
            + "and an end date of 21 April 2024.";

    public static final String MESSAGE_SUCCESS = "Loan successfully edited: %1$s";

    public static final String MESSAGE_FAILURE_LOAN = "No loan has been found "
            + "for loan number: %1$d";

    private final EditLoanDescriptor editedDetails;

    private final Index loanIndex;

    /**
     * @param editedDetails New value(s) of the edited field(s) of the loan, as an EditLoanDescriptor
     * @param loanIndex Index of the loan to be edited
     */
    public EditLoanCommand(EditLoanDescriptor editedDetails, Index loanIndex) {
        requireAllNonNull(editedDetails, loanIndex);
        this.editedDetails = editedDetails;
        this.loanIndex = loanIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Loan> lastShownList = model.getSortedLoanList();
        assert lastShownList != null;

        if (loanIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(String.format(MESSAGE_FAILURE_LOAN, loanIndex.getOneBased()));
        }

        Loan loanToEdit = lastShownList.get(loanIndex.getZeroBased());
        Person linkedPerson = loanToEdit.getAssignee();
        // Generate updated details of the loan
        LinkLoanDescriptor updatedLoanDetails = generateEditedLoanDetails(loanToEdit, editedDetails);
        // Delete the old loan
        model.deleteLoan(loanToEdit);
        // Create and link a new loan using the updated loan details
        Loan editedLoan = model.addLoan(updatedLoanDetails, linkedPerson);

        return new CommandResult(generateSuccessMessage(editedLoan), false, false, true);
    }

    /**
     * Generates a command execution success message after loan is edited.
     */
    private String generateSuccessMessage(Loan editedLoan) {
        return String.format(MESSAGE_SUCCESS, editedLoan);
    }

    /**
     * Generates the new loan details in the form of a LinkLoanDescriptor.
     *
     * @param loanToEdit The original loan that was edited.
     * @param editedDetails The details of the loan that were changed.
     * @return All details of the new loan, including the changed and unchanged details.
     * @throws CommandException If the edited date(s) are invalid.
     */
    private LinkLoanDescriptor generateEditedLoanDetails(Loan loanToEdit, EditLoanDescriptor editedDetails)
            throws CommandException {
        BigDecimal newValue = editedDetails.getValue().orElse(loanToEdit.getValue());
        Date newStartDate = editedDetails.getStartDate().orElse(loanToEdit.getStartDate());
        Date newReturnDate = editedDetails.getReturnDate().orElse(loanToEdit.getReturnDate());
        requireAllNonNull(newValue, newStartDate, newReturnDate);
        if (!Loan.isValidDates(newStartDate, newReturnDate)) {
            throw new CommandException(Loan.DATE_CONSTRAINTS);
        }
        return new LinkLoanDescriptor(newValue, newStartDate, newReturnDate);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditLoanCommand)) {
            return false;
        }

        EditLoanCommand otherEditLoanCommand = (EditLoanCommand) other;
        return editedDetails.equals(otherEditLoanCommand.editedDetails)
                && loanIndex.equals(otherEditLoanCommand.loanIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("loanIndex", loanIndex)
                .add("editedDetails", editedDetails)
                .toString();
    }

    /**
     * Stores the details of the loan that is edited.
     */
    public static class EditLoanDescriptor {
        private BigDecimal value = null;
        private Date startDate = null;
        private Date returnDate = null;

        /**
         * Creates an instance of this EditLoanDescriptor with empty fields.
         */
        public EditLoanDescriptor() {}

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(value, startDate, returnDate);
        }

        public void setValue(BigDecimal value) {
            this.value = value;
        }

        public Optional<BigDecimal> getValue() {
            return Optional.ofNullable(value);
        }

        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }

        public Optional<Date> getStartDate() {
            return Optional.ofNullable(startDate);
        }

        public void setReturnDate(Date returnDate) {
            this.returnDate = returnDate;
        }

        public Optional<Date> getReturnDate() {
            return Optional.ofNullable(returnDate);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditLoanDescriptor)) {
                return false;
            }

            EditLoanDescriptor otherEditLoanDescriptor = (EditLoanDescriptor) other;
            return Objects.equals(value, otherEditLoanDescriptor.value)
                    && Objects.equals(getStartDate(), otherEditLoanDescriptor.getStartDate())
                    && Objects.equals(getReturnDate(), otherEditLoanDescriptor.getReturnDate());
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("value", value)
                    .add("startDate", startDate)
                    .add("returnDate", returnDate)
                    .toString();
        }
    }
}
