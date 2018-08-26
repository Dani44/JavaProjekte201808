package wwr.model.enities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@lombok.Getter
@lombok.Setter
@Entity
@Table(name="Persons" , 
	   indexes = {   @Index(name="pers_email_i",  columnList = "pers_email" ) 
				   , @Index(name="pers_nickname_ui",  columnList = "pers_nickname" , unique=true)
	   			 } )
public class Person {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="pers_id" )
	private Long id ;
	
	@Column(name="pers_nickname" , length=15 , nullable=false )
	private String nickname ;

	@Column(name="pers_email" , length=30 , nullable=false )
	private String email;

	@JsonIgnore
	@Column(name="pers_active")
	private Boolean active ;
	
	@JsonIgnore	
	@Column(name="pers_sex")
	private Integer sex ;
	
	public Person(String nickname, String email) {
		super();
		this.nickname = nickname;
		this.email = email;
	}

	public Person() {
		super();
	}
	
	
	
}
