package wwr.model.enities;

import java.util.Date;

// import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.ForeignKey;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@lombok.Getter
@lombok.Setter
@Entity
@Table(name="reloading_lots")
public class Lot {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="lot_id" )
	private Long id ;
		

	@ManyToOne(fetch=FetchType.LAZY )
	@JoinColumn(name="lot_reci_id" , nullable=false , foreignKey = @ForeignKey(name="FK_LOT_RECIPE") )
	@JsonBackReference
	private Recipe recipe ;
	
	@Temporal(TemporalType.TIMESTAMP) 
	@Column(name="lot_date" , nullable=false )
	private Date date ;
	
	@Column(name="lot_casebrand" , nullable=true ,length=20)
	private String casebrand ;
	
	@Column(name="lot_primer" , nullable=true ,length=20)
	private String primer ;		
	
	@Column(name="lot_oal_inches" , nullable=true)	
	private double oalInches ;
	
	@Column(name="lot_oal_mm" , nullable=true )	
	private double oalmm ;
	
	@Column(name="lot_crimp" , nullable=true ,length=20)
	private String crimp;
	
	@Column(name="lot_size" , nullable=true ,length=20)
	private int size;	
	
	@Column(name="lot_note" , nullable=true ,length=500)
	private String note;
	
	
	
}
