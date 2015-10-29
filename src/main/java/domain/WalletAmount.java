package domain;

import javax.persistence.*;

@Entity
@Table(name="WALLET_AMOUNT")
public class WalletAmount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "WALLET_ID")
    private Wallet wallet;

    @Column(name = "AMOUNT")
    private Integer amount;

    public WalletAmount(){

    }

    public WalletAmount(Wallet wallet, Integer amount)
    {
        this.wallet = wallet;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}

