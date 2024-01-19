package idusw.javaweb.blogapi.model;

//Lombok을 사용하면 boiler plate (상용구, 관용구) code를 손쉽게 작성할 수 있음.
public class MemberDTO {
    //DTO : Date Transfer Object (데이터 전송 객체),데이터를 주고 받을 때 사용하는 객체
    private Long mid; //Identifier, DB - Primary or Unique key, Candiate key(후보키)
    private String fullName;
    private String email;
    private String pw;
    private String zipcode;

    //getter and setter method

    public Long getMid() {
        return mid;
    }

    public void setMid(Long mid) {
        this.mid = mid;
    }

    public String getFullName() { return fullName; }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getZipcode() { return zipcode; }

    public void setZipcode(String zipcode) { this.zipcode = zipcode; }

}
