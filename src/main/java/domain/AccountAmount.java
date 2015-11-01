package domain;

import javax.persistence.*;

@Entity
@Table(name="ACCOUNT_AMOUNT")
public class AccountAmount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @Column(name = "AMOUNT")
    private Integer amount;

    public AccountAmount(){

    }

    public AccountAmount(Account account, Integer amount)
    {
        this.account = account;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}

