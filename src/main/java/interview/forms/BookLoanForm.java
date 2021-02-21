package interview.forms;

import interview.models.Loan;

import java.time.LocalDate;

public class BookLoanForm {

    private String user;
    private String book_id;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public Loan getLoan(long loanPeriodInDays) {
        Loan l = new Loan();
        l.setStart_date(LocalDate.now().toString());
        l.setEnd_date(LocalDate.now().plusDays(loanPeriodInDays).toString());
        l.setUser(this.user);
        l.setBook_id(this.book_id);
        l.setReturned(false);

        return l;
    }
}
