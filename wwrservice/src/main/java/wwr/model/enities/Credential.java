package wwr.model.enities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@lombok.Getter
@lombok.Setter
@Entity
@Table(name="Credentials")

public class Credential {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="cred_id" )
	private Long id ;

	
	// Sample: {"id"="1" , "nickname"="swissman"}
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="pers_id")
	private Person person ;
	
	@Column(name="cred_password" , length=30 , nullable=false )
	private String password ;
	

}
