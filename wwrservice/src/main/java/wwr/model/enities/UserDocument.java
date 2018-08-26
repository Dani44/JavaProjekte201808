package wwr.model.enities;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mysql.jdbc.Blob;



@lombok.Getter
@lombok.Setter
@Entity
@Table(name="user_files")
public class UserDocument {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="usfi_id" )
	private Long id ;
	
	@Temporal(TemporalType.TIMESTAMP) 
	@Column(name="usfi_date" , nullable=false )
	private Date date ;

	@ManyToOne(fetch=FetchType.LAZY  )
	@JoinColumn(name="usfi_pers_id" , nullable=false , foreignKey = @ForeignKey(name="FK_RECF_PERSON"))
	private Person owner ;
	
	@Column(name="usfi_filename" , nullable=false , length=255)
	private String filename;

	@Column(name="usfi_mediatype" , nullable=false , length=50)
	private String mediatype;
	
}
