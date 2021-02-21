package interview.repositories.impl;

import interview.models.Book;
import interview.models.Catalog;
import interview.models.Loan;
import interview.models.Loans;
import interview.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Repository
public class LoanXmlRepository implements LoanRepository {

    private final String fileLocation;
    private List<Loan> loans;

    public LoanXmlRepository(@Value("${xml.loans}") String fileLocation) {
        this.fileLocation = fileLocation;
        prepare();
    }

    @Override
    public List<Loan> findAll() {
        return this.loans;
    }

    @Override
    public Loan save(Loan loan) {
        loan.setId(generateLoanId());
        this.loans.add(loan);
        Loans newLoans = new Loans();
        newLoans.setLoans(loans);
        if (persist(newLoans)) {
            return loan;
        }
        this.loans.remove(loan);
        return null;
    }
    private String generateLoanId() {
        Random r = new Random();
        int num = r.nextInt(999999);
        LocalDate l = LocalDate.now();
        StringBuilder s = new StringBuilder();
        s.append("ln")
                .append(num)
                .append(l.getDayOfMonth())
                .append(l.getMonthValue())
                .append(l.getYear());
        return s.toString();
    }

    @Override
    public Loan findById(String id) {
        return this.loans.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean delete(Loan loan) {
        this.loans.remove(loan);
        return persist(new Loans(this.loans));
    }

    @Override
    public List<Loan> findLoansByEndDateAfter(LocalDate endDate) {

        return this.loans.stream()
                .filter(e -> e.getEndLocalDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    @Override
    public List<Loan> findLoansByReturned(boolean returned) {
        return this.loans.stream()
                .filter(e -> e.isReturned() == returned)
                .collect(Collectors.toList());
    }

    @Override
    public Loan findActiveLoanForBook(Book book) {
        return this.loans.stream()
                .filter(e -> !e.isReturned())
                .filter(e -> e.getBook_id().equals(book.getId()))
                .findFirst().orElse(null);
    }

    private void prepare() {
        Path path = Paths.get(fileLocation);

        if (Files.notExists(path)) {
            try {
                Files.writeString(path, "<?xml version=\"1.0\"?>\r\n<loans>\r\n</loans>\r\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.loans = new ArrayList<>();
            return;
        }

        try {

            JAXBContext jaxbContext = JAXBContext.newInstance(Loans.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            this.loans = ((Loans) jaxbUnmarshaller.unmarshal(
                    new FileInputStream(fileLocation)))
                    .getLoans();
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }

        if (this.loans == null) {
            this.loans = new ArrayList<>();
        }
    }

    private boolean persist(Loans loans) {

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Loans.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.marshal(loans, new FileOutputStream(fileLocation));
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
