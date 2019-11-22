package ie.gmit.ds;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.protobuf.ByteString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@XmlRootElement
public class User {

    @NotNull
    private Integer uID;
    @NotBlank @Length(min=2, max=255)
    private String uName;
    @NotBlank @Length(min=2, max=255)
    private String uPwd;
    @Pattern(regexp=".+@.+\\.[a-z]+")
    private String uEmail;
    private ByteString hashedPwd;
    private ByteString salt;

    public User(){
    }

    public User(Integer id, String uName, String uEmail,ByteString hashedPwd, ByteString salt) {
        this.uID = id;
        this.uName = uName;
        this.uEmail = uEmail;
        this.hashedPwd = hashedPwd;
        this.salt = salt;
    }

    public User(Integer id, String uName, String uEmail) {
        this.uID = id;
        this.uName = uName;
        this.uEmail = uEmail;
    }

    @XmlElement
    @JsonProperty
    public Integer getuID() {
        return uID;
    }

    public void setuID(Integer id) {
        this.uID = id;
    }

    @XmlElement
    @JsonProperty
    public String getuName() {
        return uName;
    }

    public void setuName(String UName) {
        this.uName = uName;
    }
    @XmlElement
    @JsonProperty
    public String getuPwd() {
        return uPwd;
    }

    public void setuPwd(String pwd) {
        this.uPwd = pwd;
    }

    @XmlElement
    @JsonProperty
    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public ByteString getHashPwd() {
        return hashedPwd;
    }

    public void setHashedPwd(ByteString hashedPwd) {
        this.hashedPwd = hashedPwd;
    }

    public ByteString getSalt() {
        return salt;
    }

    public void setSalt(ByteString salt) {
        this.salt = salt;
    }
    
}