package entity;
// Generated Oct 24, 2017 6:39:27 PM by Hibernate Tools 4.3.1


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Human generated by hbm2java
 */
@Entity
@Table(name="human"
)
public class Human  implements java.io.Serializable {


     private long id;
     private Address address;
     private char sex;
     private Date birthdate;
     private String firstname;
     private String lastname;
     private String patronymic;

    public Human() {
    }

    public Human(long id, Address address, char sex, Date birthdate, String firstname, String lastname, String patronymic) {
       this.id = id;
       this.address = address;
       this.sex = sex;
       this.birthdate = birthdate;
       this.firstname = firstname;
       this.lastname = lastname;
       this.patronymic = patronymic;
    }
   
     @Id 

    
    @Column(name="id", nullable=false)
    public long getId() {
        return this.id;
    }
    
    public void setId(long id) {
        this.id = id;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="addressid", nullable=false)
    public Address getAddress() {
        return this.address;
    }
    
    public void setAddress(Address address) {
        this.address = address;
    }

    
    @Column(name="sex", nullable=false, length=1)
    public char getSex() {
        return this.sex;
    }
    
    public void setSex(char sex) {
        this.sex = sex;
    }

    @Temporal(TemporalType.DATE)
    @Column(name="birthdate", nullable=false, length=13)
    public Date getBirthdate() {
        return this.birthdate;
    }
    
    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    
    @Column(name="firstname", nullable=false)
    public String getFirstname() {
        return this.firstname;
    }
    
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    
    @Column(name="lastname", nullable=false)
    public String getLastname() {
        return this.lastname;
    }
    
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    
    @Column(name="patronymic", nullable=false)
    public String getPatronymic() {
        return this.patronymic;
    }
    
    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }




}


